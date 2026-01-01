package com.zyc.zdh.controller.zdhpush;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.PushChannelMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.PushChannelInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
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
 * push通道配置服务
 */
@Controller
public class PushChannelController extends BaseController {

    @Autowired
    private PushChannelMapper pushChannelMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * push通道配置列表首页
     * @return
     */
    @RequestMapping(value = "/push_channel_index", method = RequestMethod.GET)
    public String push_channel_index() {

        return "push/push_channel_index";
    }

    /**
     * push通道配置列表
     * @param context 关键字
     * @param product_code 产品
     * @param dim_group 归属组
     * @return
     */
    @SentinelResource(value = "push_channel_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_channel_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PushChannelInfo>> push_channel_list(String context, String product_code, String dim_group, String channel_type) {
        try{
            Example example=new Example(PushChannelInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }

            if(!StringUtils.isEmpty(channel_type)){
                criteria.andEqualTo("channel_type", channel_type);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            List<PushChannelInfo> pushChannelInfos = pushChannelMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, pushChannelInfos);

            return ReturnInfo.buildSuccess(pushChannelInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("push通道配置列表查询失败", e);
        }

    }


    /**
    * push通道配置分页列表
    * @param context 关键字
    * @param product_code 产品code
    * @param dim_group 归属组code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "push_channel_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_channel_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<PushChannelInfo>>> push_channel_list_by_page(String context,String product_code, String dim_group, int limit, int offset) {
        try{
            Example example=new Example(PushChannelInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }


            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = pushChannelMapper.selectCountByExample(example);

            List<PushChannelInfo> pushChannelInfos = pushChannelMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, pushChannelInfos);

            PageResult<List<PushChannelInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(pushChannelInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("push通道配置列表分页查询失败", e);
        }

    }

    /**
     * push通道配置新增首页
     * @return
     */
    @RequestMapping(value = "/push_channel_add_index", method = RequestMethod.GET)
    public String push_channel_add_index() {

        return "push/push_channel_add_index";
    }

    /**
     * xx明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "push_channel_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_channel_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PushChannelInfo> push_channel_detail(String id) {
        try {
            PushChannelInfo pushChannelInfo = pushChannelMapper.selectByPrimaryKey(id);
            checkPermissionByProductAndDimGroup(zdhPermissionService, pushChannelInfo.getProduct_code(), pushChannelInfo.getDim_group());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pushChannelInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * push通道配置更新
     * @param pushChannelInfo
     * @return
     */
    @SentinelResource(value = "push_channel_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_channel_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PushChannelInfo> push_channel_update(PushChannelInfo pushChannelInfo) {
        try {

            PushChannelInfo oldPushChannelInfo = pushChannelMapper.selectByPrimaryKey(pushChannelInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pushChannelInfo.getProduct_code(), pushChannelInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldPushChannelInfo.getProduct_code(), oldPushChannelInfo.getDim_group(), getAttrEdit());


            pushChannelInfo.setCreate_time(oldPushChannelInfo.getCreate_time());
            pushChannelInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            pushChannelInfo.setIs_delete(Const.NOT_DELETE);
            pushChannelMapper.updateByPrimaryKeySelective(pushChannelInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", pushChannelInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * push通道配置新增
     * @param pushChannelInfo
     * @return
     */
    @SentinelResource(value = "push_channel_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_channel_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PushChannelInfo> push_channel_add(PushChannelInfo pushChannelInfo) {
        try {
            pushChannelInfo.setOwner(getOwner());
            pushChannelInfo.setIs_delete(Const.NOT_DELETE);
            pushChannelInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            pushChannelInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pushChannelInfo.getProduct_code(), pushChannelInfo.getDim_group(), getAttrAdd());

            pushChannelMapper.insertSelective(pushChannelInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", pushChannelInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * push通道配置删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "push_channel_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_channel_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo push_channel_delete(String[] ids) {
        try {
            checkPermissionByProductAndDimGroup(zdhPermissionService, pushChannelMapper, pushChannelMapper.getTable(), ids);

            pushChannelMapper.deleteLogicByIds("push_channel_info",ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    /**
     * push通道配置更新
     * @param id
     * @param status
     * @return
     */
    @SentinelResource(value = "push_channel_update_status", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_channel_update_status", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PushChannelInfo> push_channel_update_status(String id, String status) {
        try {

            PushChannelInfo oldPushChannelInfo = pushChannelMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldPushChannelInfo.getProduct_code(), oldPushChannelInfo.getDim_group(), getAttrEdit());

            oldPushChannelInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            oldPushChannelInfo.setIs_delete(Const.NOT_DELETE);
            oldPushChannelInfo.setStatus(status);
            pushChannelMapper.updateByPrimaryKeySelective(oldPushChannelInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", oldPushChannelInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }
}
