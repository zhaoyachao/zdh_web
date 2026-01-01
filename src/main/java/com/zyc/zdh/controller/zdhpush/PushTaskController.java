package com.zyc.zdh.controller.zdhpush;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.PushTaskLog;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
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
import com.zyc.zdh.dao.PushTaskMapper;


import java.sql.Timestamp;
import java.util.List;

/**
 * 推送任务基础表服务
 *
 * 使用权限控制需要PushTaskLog 继承BaseProductAuthInfo 或者 BaseProductAndDimGroupAuthInfo
 */
@Controller
public class PushTaskController extends BaseController {

    @Autowired
    private PushTaskMapper pushTaskMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 推送任务基础表列表首页
     * @return
     */
    @RequestMapping(value = "/push_task_index", method = RequestMethod.GET)
    public String push_task_index() {

        return "push/push_task_index";
    }

    /**
     * 推送任务基础表列表
     * @param context 关键字
     * @param product_code 产品
     * @param dim_group 归属组
     * @return
     */
    @SentinelResource(value = "push_task_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_task_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PushTaskLog>> push_task_list(String context, String product_code, String dim_group) {
        try{
            Example example=new Example(PushTaskLog.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);
            //dynamicPermissionByProduct(zdhPermissionService, criteria);

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

            List<PushTaskLog> pushTaskLogs = pushTaskMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, pushTaskLogs);

            return ReturnInfo.buildSuccess(pushTaskLogs);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("推送任务基础表列表查询失败", e);
        }

    }


    /**
    * 推送任务基础表分页列表
    * @param context 关键字
    * @param product_code 产品code
    * @param dim_group 归属组code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "push_task_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_task_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<PushTaskLog>>> push_task_list_by_page(String context,String product_code, String dim_group, int limit, int offset) {
        try{
            Example example=new Example(PushTaskLog.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);
            //dynamicPermissionByProduct(zdhPermissionService, criteria);

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
            int total = pushTaskMapper.selectCountByExample(example);

            List<PushTaskLog> pushTaskLogs = pushTaskMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, pushTaskLogs);

            PageResult<List<PushTaskLog>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(pushTaskLogs);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("推送任务基础表列表分页查询失败", e);
        }

    }

    /**
     * 推送任务基础表新增首页
     * @return
     */
    @RequestMapping(value = "/push_task_add_index", method = RequestMethod.GET)
    public String push_task_add_index() {

        return "push/push_task_add_index";
    }

    /**
     * 推送任务基础表明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "push_task_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_task_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PushTaskLog> push_task_detail(String id) {
        try {
            PushTaskLog pushTaskLog = pushTaskMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService,  pushTaskLog.getProduct_code(), pushTaskLog.getDim_group(), getAttrSelect());
            //checkAttrPermissionByProduct(zdhPermissionService,  pushTaskLog.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pushTaskLog);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 推送任务基础表更新
     * @param pushTaskLog
     * @return
     */
    @SentinelResource(value = "push_task_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_task_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PushTaskLog> push_task_update(PushTaskLog pushTaskLog) {
        try {

            PushTaskLog oldPushTaskLog = pushTaskMapper.selectByPrimaryKey(pushTaskLog.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pushTaskLog.getProduct_code(), pushTaskLog.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldPushTaskLog.getProduct_code(), oldPushTaskLog.getDim_group(), getAttrEdit());
            //checkAttrPermissionByProduct(zdhPermissionService, pushTaskLog.getProduct_code(), getAttrEdit());
            //checkAttrPermissionByProduct(zdhPermissionService, oldPushTaskLog.getProduct_code(), getAttrEdit());


            pushTaskLog.setCreate_time(oldPushTaskLog.getCreate_time());
            pushTaskLog.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            pushTaskLog.setIs_delete(Const.NOT_DELETE);
            pushTaskMapper.updateByPrimaryKeySelective(pushTaskLog);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", pushTaskLog);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 推送任务基础表新增
     * @param pushTaskLog
     * @return
     */
    @SentinelResource(value = "push_task_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_task_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PushTaskLog> push_task_add(PushTaskLog pushTaskLog) {
        try {
            pushTaskLog.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            //pushTaskLog.setOwner(getOwner());
            pushTaskLog.setIs_delete(Const.NOT_DELETE);
            pushTaskLog.setCreate_time(new Timestamp(System.currentTimeMillis()));
            pushTaskLog.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pushTaskLog.getProduct_code(), pushTaskLog.getDim_group(), getAttrAdd());
            //checkAttrPermissionByProduct(zdhPermissionService, pushTaskLog.getProduct_code(), getAttrAdd());
            pushTaskMapper.insertSelective(pushTaskLog);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", pushTaskLog);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 推送任务基础表删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "push_task_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_task_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo push_task_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pushTaskMapper, pushTaskMapper.getTable(), ids, getAttrDel());
            pushTaskMapper.deleteLogicByIds(pushTaskMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }
}
