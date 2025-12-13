package com.zyc.zdh.controller.zdhpush;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.WechatTagMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.WechatTagInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.pushx.PushxWechatTagService;
import com.zyc.zdh.pushx.entity.WechatTagResponse;
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
 * 微信标签信息表服务
 */
@Controller
public class WechatTagController extends BaseController {

    @Autowired
    private WechatTagMapper wechatTagMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;
    @Autowired
    private PushxWechatTagService pushxWechatTagService;

    /**
     * 微信标签信息表列表首页
     * @return
     */
    @RequestMapping(value = "/wechat_tag_index", method = RequestMethod.GET)
    @White
    public String wechat_tag_index() {

        return "push/wechat_tag_index";
    }

    /**
     * 微信标签信息表列表
     * @param context 关键字
     * @param product_code 产品
     * @return
     */
    @SentinelResource(value = "wechat_tag_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_tag_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<List<WechatTagInfo>> wechat_tag_list(String context, String product_code) {
        try{
            Example example=new Example(WechatTagInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("tname", getLikeCondition(context));
                example.and(criteria2);
            }

            List<WechatTagInfo> wechatTagInfos = wechatTagMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, wechatTagInfos);

            return ReturnInfo.buildSuccess(wechatTagInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信标签信息表列表查询失败", e);
        }

    }


    /**
    * 微信标签信息表分页列表
    * @param context 关键字
    * @param product_code 产品code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "wechat_tag_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_tag_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<PageResult<List<WechatTagInfo>>> wechat_tag_list_by_page(String context,String product_code, int limit, int offset) {
        try{
            Example example=new Example(WechatTagInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("tname", getLikeCondition(context));
                example.and(criteria2);
            }

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = wechatTagMapper.selectCountByExample(example);

            List<WechatTagInfo> wechatTagInfos = wechatTagMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, wechatTagInfos);

            PageResult<List<WechatTagInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(wechatTagInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信标签信息表列表分页查询失败", e);
        }

    }

    /**
     * 微信标签信息表新增首页
     * @return
     */
    @RequestMapping(value = "/wechat_tag_add_index", method = RequestMethod.GET)
    @White
    public String wechat_tag_add_index() {

        return "push/wechat_tag_add_index";
    }

    /**
     * 微信标签信息表明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "wechat_tag_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_tag_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<WechatTagInfo> wechat_tag_detail(String id) {
        try {
            WechatTagInfo wechatTagInfo = wechatTagMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService,  wechatTagInfo.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", wechatTagInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 微信标签信息表更新
     * @param wechatTagInfo
     * @return
     */
    @SentinelResource(value = "wechat_tag_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_tag_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo<WechatTagInfo> wechat_tag_update(WechatTagInfo wechatTagInfo) {
        try {

            WechatTagInfo oldWechatTagInfo = wechatTagMapper.selectByPrimaryKey(wechatTagInfo.getId());

            checkAttrPermissionByProduct(zdhPermissionService, wechatTagInfo.getProduct_code(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService, oldWechatTagInfo.getProduct_code(), getAttrEdit());

            wechatTagInfo.setCreate_time(oldWechatTagInfo.getCreate_time());
            wechatTagInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            wechatTagInfo.setIs_delete(Const.NOT_DELETE);
            wechatTagMapper.updateByPrimaryKeySelective(wechatTagInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", wechatTagInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 微信标签信息表新增
     * @param wechatTagInfo
     * @return
     */
    @SentinelResource(value = "wechat_tag_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_tag_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo<WechatTagInfo> wechat_tag_add(WechatTagInfo wechatTagInfo) {
        try {
            wechatTagInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            wechatTagInfo.setOwner(getOwner());
            wechatTagInfo.setIs_delete(Const.NOT_DELETE);
            wechatTagInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            wechatTagInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProduct(zdhPermissionService, wechatTagInfo.getProduct_code(), getAttrAdd());
            wechatTagMapper.insertSelective(wechatTagInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", wechatTagInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 微信标签信息表删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "wechat_tag_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_tag_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo wechat_tag_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatTagMapper, wechatTagMapper.getTable(), ids, getAttrDel());
            wechatTagMapper.deleteLogicByIds(wechatTagMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    /**
     * 微信端-标签编辑,新增/编辑
     * @param id
     * @return
     */
    @SentinelResource(value = "wechat_tag_cloud_edit", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_tag_cloud_edit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo wechat_tag_cloud_edit(String id) {
        try {
            WechatTagInfo wechatTagInfo = wechatTagMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService,  wechatTagInfo.getProduct_code(), getAttrEdit());

            //调用pushx创建标签
            WechatTagResponse response = null;
            if(StringUtils.isEmpty(wechatTagInfo.getTid())){
                response = pushxWechatTagService.createTag(wechatTagInfo);
            }else{
                response = pushxWechatTagService.updateTag(wechatTagInfo);
            }

            wechatTagInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            if(response == null){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "调用pushx异常", null);
            }

            if(response.isSuccess()){
                if(response.getData() != null && response.getData().getTag() != null){
                    wechatTagInfo.setTid(String.valueOf(response.getData().getTag().getId()));
                }
            }else{
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "同步失败", response.getMsg());
            }
            //成功更新状态
            wechatTagMapper.updateByPrimaryKeySelective(wechatTagInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "同步成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "同步失败", e.getMessage());
        }
    }

    /**
     * 微信端-标签删除
     * @param id
     * @return
     */
    @SentinelResource(value = "wechat_tag_cloud_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_tag_cloud_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo wechat_tag_cloud_delete(String id) {
        try {
            WechatTagInfo wechatTagInfo = wechatTagMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService,  wechatTagInfo.getProduct_code(), getAttrEdit());

            //调用pushx创建标签
            WechatTagResponse response = pushxWechatTagService.deleteTag(wechatTagInfo);
            wechatTagInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            if(response == null){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "调用pushx异常", null);
            }

            if(response.isSuccess()){
                wechatTagInfo.setTid("");
            }else{
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "同步失败", response.getMsg());
            }
            //成功更新状态
            wechatTagMapper.updateByPrimaryKeySelective(wechatTagInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "同步成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "同步失败", e.getMessage());
        }
    }

}
