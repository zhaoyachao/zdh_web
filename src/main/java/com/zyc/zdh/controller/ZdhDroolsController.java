package com.zyc.zdh.controller;

import com.zyc.zdh.dao.EtlDroolsTaskMapper;
import com.zyc.zdh.entity.EtlDroolsTaskInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * drools服务-废弃
 */
@Controller
public class ZdhDroolsController extends BaseController{

    public Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    EtlDroolsTaskMapper etlDroolsTaskMapper;


    /**
     * drools任务首页
     * @return
     */
    @RequestMapping("/etl_task_drools_index")
    public String etl_task_drools_index() {

        return "etl/etl_task_drools_index";
    }

    /**
     * drools任务新增首页
     * @return
     */
    @RequestMapping("/etl_task_drools_add_index")
    public String etl_task_drools_add_index() {

        return "etl/etl_task_drools_add_index";
    }

    /**
     * 获取drools任务明细
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/etl_task_drools_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo etl_task_drools_detail(String id) {
        try{
            EtlDroolsTaskInfo etlDroolsTaskInfo=etlDroolsTaskMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", etlDroolsTaskInfo);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 模糊查询drools任务明细
     * @param etl_context 关键字
     * @param file_name 输出数据源关键字
     * @return
     */
    @RequestMapping(value = "/etl_task_drools_list2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<EtlDroolsTaskInfo>> etl_task_drools_list2(String etl_context, String file_name) throws Exception {
        try{
            List<EtlDroolsTaskInfo> etlDroolsTaskInfos = new ArrayList<EtlDroolsTaskInfo>();
            if(!StringUtils.isEmpty(etl_context)){
                etl_context=getLikeCondition(etl_context);
            }
            if(!StringUtils.isEmpty(file_name)){
                file_name=getLikeCondition(file_name);
            }
            etlDroolsTaskInfos = etlDroolsTaskMapper.selectByParams(getOwner(), etl_context, file_name);
            return ReturnInfo.buildSuccess(etlDroolsTaskInfos);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("drools任务列表查询失败", e);
        }

    }


    /**
     * 新增drools任务
     * @param etlDroolsTaskInfo
     * @return
     */
    @RequestMapping(value = "/etl_task_drools_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo etl_task_drools_add(EtlDroolsTaskInfo etlDroolsTaskInfo) {
        try{
            String id=SnowflakeIdWorker.getInstance().nextId() + "";
            etlDroolsTaskInfo.setId(id);
            etlDroolsTaskInfo.setOwner(getOwner());
            etlDroolsTaskInfo.setCreate_time(new Timestamp(new Date().getTime()));
            debugInfo(etlDroolsTaskInfo);
            etlDroolsTaskMapper.insert(etlDroolsTaskInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }

    /**
     * 删除drools任务
     * @param ids id数组
     * @return
     */
    @RequestMapping(value = "/etl_task_drools_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_drools_delete(String[] ids) {
        try{
            if (ids != null) {
                for (String id : ids) {
                    etlDroolsTaskMapper.deleteBatchById(id);
                }
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }
    }

    /**
     * 更新drools 任务
     * @param etlDroolsTaskInfo
     * @return
     */
    @RequestMapping(value = "/etl_task_drools_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo etl_task_drools_update(EtlDroolsTaskInfo etlDroolsTaskInfo) {
        try{
            String owner = getOwner();
            etlDroolsTaskInfo.setOwner(owner);
            debugInfo(etlDroolsTaskInfo);

            etlDroolsTaskMapper.updateByPrimaryKey(etlDroolsTaskInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"更新失败", e);
        }
    }


    private void debugInfo(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            // 对于每个属性，获取属性名
            String varName = fields[i].getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o;
                try {
                    o = fields[i].get(obj);
                    System.err.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
                    logger.error(error, e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            }
        }
    }

}
