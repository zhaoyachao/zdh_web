package com.zyc.zdh.controller.zdhpush;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.WechatMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.WechatInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
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

import java.sql.Timestamp;
import java.util.List;

/**
 * 微信信息表服务
 *
 * 使用权限控制需要WechatInfo 继承BaseProductAuthInfo 或者 BaseProductAndDimGroupAuthInfo
 */
@Controller
public class WechatController extends BaseController {

    @Autowired
    private WechatMapper wechatMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 微信信息表列表首页
     * @return
     */
    @RequestMapping(value = "/wechat_index", method = RequestMethod.GET)
    public String wechat_index() {

        return "push/wechat_index";
    }

    /**
     * 微信信息表列表
     * @param context 关键字
     * @param product_code 产品
     * @return
     */
    @SentinelResource(value = "wechat_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<WechatInfo>> wechat_list(String context, String product_code) {
        try{
            Example example=new Example(WechatInfo.class);
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

            List<WechatInfo> wechatInfos = wechatMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, wechatInfos);

            return ReturnInfo.buildSuccess(wechatInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信信息表列表查询失败", e);
        }

    }


    /**
    * 微信信息表分页列表
    * @param context 关键字
    * @param product_code 产品code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "wechat_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<WechatInfo>>> wechat_list_by_page(String context,String product_code, int limit, int offset) {
        try{
            Example example=new Example(WechatInfo.class);
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
            int total = wechatMapper.selectCountByExample(example);

            List<WechatInfo> wechatInfos = wechatMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, wechatInfos);

            PageResult<List<WechatInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(wechatInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信信息表列表分页查询失败", e);
        }

    }

    /**
     * 微信信息表新增首页
     * @return
     */
    @RequestMapping(value = "/wechat_add_index", method = RequestMethod.GET)
    public String wechat_add_index() {

        return "push/wechat_add_index";
    }

    /**
     * 微信信息表明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "wechat_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<WechatInfo> wechat_detail(String id) {
        try {
            WechatInfo wechatInfo = wechatMapper.selectByPrimaryKey(id);
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService,  wechatInfo.getProduct_code(), wechatInfo.getDim_group(), getAttrSelect());
            //checkAttrPermissionByProduct(zdhPermissionService,  wechatInfo.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", wechatInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 微信信息表更新
     * @param wechatInfo
     * @return
     */
    @SentinelResource(value = "wechat_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatInfo> wechat_update(WechatInfo wechatInfo) {
        try {

            WechatInfo oldWechatInfo = wechatMapper.selectByPrimaryKey(wechatInfo.getId());

            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatInfo.getProduct_code(), wechatInfo.getDim_group(), getAttrEdit());
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldWechatInfo.getProduct_code(), oldWechatInfo.getDim_group(), getAttrEdit());
            //checkAttrPermissionByProduct(zdhPermissionService, wechatInfo.getProduct_code(), getAttrEdit());
            //checkAttrPermissionByProduct(zdhPermissionService, oldWechatInfo.getProduct_code(), getAttrEdit());


            wechatInfo.setCreate_time(oldWechatInfo.getCreate_time());
            wechatInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            wechatInfo.setIs_delete(Const.NOT_DELETE);
            wechatMapper.updateByPrimaryKeySelective(wechatInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", wechatInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 微信信息表新增
     * @param wechatInfo
     * @return
     */
    @SentinelResource(value = "wechat_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatInfo> wechat_add(WechatInfo wechatInfo) {
        try {
            wechatInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            wechatInfo.setOwner(getOwner());
            wechatInfo.setIs_delete(Const.NOT_DELETE);
            wechatInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            wechatInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatInfo.getProduct_code(), wechatInfo.getDim_group(), getAttrAdd());
            //checkAttrPermissionByProduct(zdhPermissionService, wechatInfo.getProduct_code(), getAttrAdd());
            wechatMapper.insertSelective(wechatInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", wechatInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 微信信息表删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "wechat_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo wechat_delete(String[] ids) {
        try {
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatMapper, wechatMapper.getTable(), ids, getAttrDel());
            wechatMapper.deleteLogicByIds(wechatMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }
}
