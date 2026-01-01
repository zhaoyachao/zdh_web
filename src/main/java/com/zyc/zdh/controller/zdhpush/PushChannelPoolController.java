package com.zyc.zdh.controller.zdhpush;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.PushChannelPoolMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.PushChannelPoolInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
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
 * push通道池配置服务
 */
@Controller
public class PushChannelPoolController extends BaseController {

    @Autowired
    private PushChannelPoolMapper pushChannelPoolMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * push通道池配置列表首页
     * @return
     */
    @RequestMapping(value = "/push_channel_pool_index", method = RequestMethod.GET)
    public String push_channel_pool_index() {

        return "push/push_channel_pool_index";
    }

    /**
     * push通道池配置列表
     * @param context 关键字
     * @param product_code 产品
     * @param dim_group 归属组
     * @return
     */
    @SentinelResource(value = "push_channel_pool_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_channel_pool_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PushChannelPoolInfo>> push_channel_pool_list(String context, String product_code, String dim_group) {
        try{
            Example example=new Example(PushChannelPoolInfo.class);
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

            List<PushChannelPoolInfo> pushChannelPoolInfos = pushChannelPoolMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, pushChannelPoolInfos);

            return ReturnInfo.buildSuccess(pushChannelPoolInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("push通道池配置列表查询失败", e);
        }

    }


    /**
    * push通道池配置分页列表
    * @param context 关键字
    * @param product_code 产品code
    * @param dim_group 归属组code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "push_channel_pool_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_channel_pool_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<PushChannelPoolInfo>>> push_channel_pool_list_by_page(String context,String product_code, String dim_group, int limit, int offset) {
        try{
            Example example=new Example(PushChannelPoolInfo.class);
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
            int total = pushChannelPoolMapper.selectCountByExample(example);

            List<PushChannelPoolInfo> pushChannelPoolInfos = pushChannelPoolMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, pushChannelPoolInfos);

            PageResult<List<PushChannelPoolInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(pushChannelPoolInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("push通道池配置列表分页查询失败", e);
        }

    }

    /**
     * push通道池配置新增首页
     * @return
     */
    @RequestMapping(value = "/push_channel_pool_add_index", method = RequestMethod.GET)
    public String push_channel_pool_add_index() {

        return "push/push_channel_pool_add_index";
    }

    /**
     * push通道池配置明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "push_channel_pool_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_channel_pool_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PushChannelPoolInfo> push_channel_pool_detail(String id) {
        try {
            PushChannelPoolInfo pushChannelPoolInfo = pushChannelPoolMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService,  pushChannelPoolInfo.getProduct_code(),  pushChannelPoolInfo.getDim_group(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pushChannelPoolInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * push通道池配置更新
     * @param pushChannelPoolInfo
     * @return
     */
    @SentinelResource(value = "push_channel_pool_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_channel_pool_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PushChannelPoolInfo> push_channel_pool_update(PushChannelPoolInfo pushChannelPoolInfo) {
        try {

            PushChannelPoolInfo oldPushChannelPoolInfo = pushChannelPoolMapper.selectByPrimaryKey(pushChannelPoolInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pushChannelPoolInfo.getProduct_code(), pushChannelPoolInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldPushChannelPoolInfo.getProduct_code(), oldPushChannelPoolInfo.getDim_group(), getAttrEdit());


            pushChannelPoolInfo.setCreate_time(oldPushChannelPoolInfo.getCreate_time());
            pushChannelPoolInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            pushChannelPoolInfo.setIs_delete(Const.NOT_DELETE);
            pushChannelPoolMapper.updateByPrimaryKeySelective(pushChannelPoolInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", pushChannelPoolInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * push通道池配置新增
     * @param pushChannelPoolInfo
     * @return
     */
    @SentinelResource(value = "push_channel_pool_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_channel_pool_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PushChannelPoolInfo> push_channel_pool_add(PushChannelPoolInfo pushChannelPoolInfo) {
        try {
            pushChannelPoolInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            pushChannelPoolInfo.setOwner(getOwner());
            pushChannelPoolInfo.setIs_delete(Const.NOT_DELETE);
            pushChannelPoolInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            pushChannelPoolInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pushChannelPoolInfo.getProduct_code(), pushChannelPoolInfo.getDim_group(), getAttrAdd());
            pushChannelPoolMapper.insertSelective(pushChannelPoolInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", pushChannelPoolInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * push通道池配置删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "push_channel_pool_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_channel_pool_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo push_channel_pool_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pushChannelPoolMapper, pushChannelPoolMapper.getTable(), ids, getAttrDel());
            pushChannelPoolMapper.deleteLogicByIds(pushChannelPoolMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }
}
