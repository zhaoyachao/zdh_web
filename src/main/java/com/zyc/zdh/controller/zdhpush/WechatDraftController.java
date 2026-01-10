package com.zyc.zdh.controller.zdhpush;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.WechatDraftInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.pushx.PushxWechatDraftService;
import com.zyc.zdh.pushx.entity.WechatDraftResponse;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;
import com.zyc.zdh.dao.WechatDraftMapper;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 微信草稿表服务
 *
 * 使用权限控制需要WechatDraftInfo 继承BaseProductAuthInfo 或者 BaseProductAndDimGroupAuthInfo
 */
@Controller
public class WechatDraftController extends BaseController {

    @Autowired
    private WechatDraftMapper wechatDraftMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;
    @Autowired
    private PushxWechatDraftService pushxWechatDraftService;

    /**
     * 微信草稿表列表首页
     * @return
     */
    @RequestMapping(value = "/wechat_draft_index", method = RequestMethod.GET)
    public String wechat_draft_index() {

        return "push/wechat_draft_index";
    }

    /**
     * 微信草稿表列表
     * @param context 关键字
     * @param product_code 产品
     * @return
     */
    @SentinelResource(value = "wechat_draft_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_draft_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<WechatDraftInfo>> wechat_draft_list(String context, String product_code) {
        try{
            Example example=new Example(WechatDraftInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            //dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);
            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            List<WechatDraftInfo> wechatDraftInfos = wechatDraftMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, wechatDraftInfos);

            return ReturnInfo.buildSuccess(wechatDraftInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信草稿表列表查询失败", e);
        }

    }


    /**
    * 微信草稿表分页列表
    * @param context 关键字
    * @param product_code 产品code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "wechat_draft_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_draft_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<WechatDraftInfo>>> wechat_draft_list_by_page(String context,String product_code, int limit, int offset) {
        try{
            Example example=new Example(WechatDraftInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            //dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);
            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = wechatDraftMapper.selectCountByExample(example);

            List<WechatDraftInfo> wechatDraftInfos = wechatDraftMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, wechatDraftInfos);

            PageResult<List<WechatDraftInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(wechatDraftInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信草稿表列表分页查询失败", e);
        }

    }

    /**
     * 微信草稿表新增首页
     * @return
     */
    @RequestMapping(value = "/wechat_draft_add_index", method = RequestMethod.GET)
    public String wechat_draft_add_index() {

        return "push/wechat_draft_add_index";
    }

    /**
     * 微信草稿表明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "wechat_draft_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_draft_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<WechatDraftInfo> wechat_draft_detail(String id) {
        try {
            WechatDraftInfo wechatDraftInfo = wechatDraftMapper.selectByPrimaryKey(id);
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService,  wechatDraftInfo.getProduct_code(), wechatDraftInfo.getDim_group(), getAttrSelect());
            checkAttrPermissionByProduct(zdhPermissionService,  wechatDraftInfo.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", wechatDraftInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 微信草稿表更新
     * @param wechatDraftInfo
     * @return
     */
    @SentinelResource(value = "wechat_draft_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_draft_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatDraftInfo> wechat_draft_update(WechatDraftInfo wechatDraftInfo) {
        try {

            WechatDraftInfo oldWechatDraftInfo = wechatDraftMapper.selectByPrimaryKey(wechatDraftInfo.getId());

            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatDraftInfo.getProduct_code(), wechatDraftInfo.getDim_group(), getAttrEdit());
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldWechatDraftInfo.getProduct_code(), oldWechatDraftInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService, wechatDraftInfo.getProduct_code(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService, oldWechatDraftInfo.getProduct_code(), getAttrEdit());


            wechatDraftInfo.setCreate_time(oldWechatDraftInfo.getCreate_time());
            wechatDraftInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            wechatDraftInfo.setIs_delete(Const.NOT_DELETE);
            wechatDraftMapper.updateByPrimaryKeySelective(wechatDraftInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", wechatDraftInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 微信草稿表新增
     * @param wechatDraftInfo
     * @return
     */
    @SentinelResource(value = "wechat_draft_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_draft_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatDraftInfo> wechat_draft_add(WechatDraftInfo wechatDraftInfo) {
        try {
            wechatDraftInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            wechatDraftInfo.setOwner(getOwner());
            wechatDraftInfo.setIs_delete(Const.NOT_DELETE);
            wechatDraftInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            wechatDraftInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatDraftInfo.getProduct_code(), wechatDraftInfo.getDim_group(), getAttrAdd());
            checkAttrPermissionByProduct(zdhPermissionService, wechatDraftInfo.getProduct_code(), getAttrAdd());

            //调用pushx上传草稿
            WechatDraftResponse wechatDraftResponse = pushxWechatDraftService.addDraft(wechatDraftInfo);
            if(wechatDraftResponse.isSuccess()){
                wechatDraftInfo.setMedia_id(wechatDraftResponse.getData().getMedia_id());
                wechatDraftMapper.insertSelective(wechatDraftInfo);
                return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", wechatDraftInfo);
            }
            throw new Exception(wechatDraftResponse.getMsg());
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 微信草稿表删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "wechat_draft_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_draft_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo wechat_draft_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatDraftMapper, wechatDraftMapper.getTable(), ids, getAttrDel());
            wechatDraftMapper.deleteLogicByIds(wechatDraftMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }
}
