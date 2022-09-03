package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.zyc.zdh.dao.EtlMoreTaskMapper;
import com.zyc.zdh.entity.EtlMoreTaskInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
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
 * 多源ETL服务
 */
@Controller
public class ZdhMoreEtlController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    EtlMoreTaskMapper etlMoreTaskMapper;


    /**
     * 多源ETL任务首页
     *
     * @return
     */
    @RequestMapping("/etl_task_more_sources_index")
    public String etl_task_more_sources_index() {

        return "etl/etl_task_more_sources_index";
    }

    /**
     * 根据指定任务id,或者查询当前用户下的所有多源任务
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/etl_task_more_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo etl_task_more_detail(String id) {
        try {
            EtlMoreTaskInfo etlMoreTaskInfo = etlMoreTaskMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", etlMoreTaskInfo);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 模糊查询多源ETL任务信息
     *
     * @param etl_context 任务说明
     * @param file_name   输出文件名/表名
     * @return
     */
    @RequestMapping(value = "/etl_task_more_list2", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String etl_task_more_list2(String etl_context, String file_name) {
        try{
            List<EtlMoreTaskInfo> etlMoreTaskInfos = new ArrayList<EtlMoreTaskInfo>();
            if(!StringUtils.isEmpty(etl_context)){
                etl_context=getLikeCondition(etl_context);
            }
            if(!StringUtils.isEmpty(file_name)){
                file_name=getLikeCondition(file_name);
            }
            etlMoreTaskInfos = etlMoreTaskMapper.selectByParams(getOwner(), etl_context, file_name);
            return JSON.toJSONString(etlMoreTaskInfos);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return JSON.toJSONString(e.getMessage());
        }

    }


    /**
     * 新增多源ETL任务首页
     *
     * @return
     */
    @RequestMapping("/etl_task_more_sources_add_index")
    public String etl_task_more_sources_add_index() {

        return "etl/etl_task_more_sources_add_index";
    }

    /**
     * 新增多源ETL任务
     *
     * @param etlMoreTaskInfo
     * @return
     */
    @RequestMapping(value = "/etl_task_more_sources_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo etl_task_more_sources_add(EtlMoreTaskInfo etlMoreTaskInfo) {
        try {
            etlMoreTaskInfo.setOwner(getOwner());
            etlMoreTaskInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            etlMoreTaskInfo.setCreate_time(new Timestamp(new Date().getTime()));
            etlMoreTaskInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            etlMoreTaskInfo.setIs_delete(Const.NOT_DELETE);
            debugInfo(etlMoreTaskInfo);
            etlMoreTaskMapper.insert(etlMoreTaskInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);

        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 删除多源ETL任务
     *
     * @param ids id数组
     * @return
     */
    @RequestMapping(value = "/etl_task_more_sources_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_more_sources_delete(String[] ids) {
        try {
            etlMoreTaskMapper.deleteLogicByIds("etl_more_task_info",ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }

    /**
     * 更新多源ETL任务
     *
     * @param etlMoreTaskInfo
     * @return
     */
    @RequestMapping(value = "/etl_task_more_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo etl_task_more_update(EtlMoreTaskInfo etlMoreTaskInfo) {
        try {
            String owner = getOwner();
            etlMoreTaskInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            etlMoreTaskInfo.setIs_delete(Const.NOT_DELETE);
            etlMoreTaskInfo.setOwner(owner);
            debugInfo(etlMoreTaskInfo);

            etlMoreTaskMapper.updateByPrimaryKey(etlMoreTaskInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
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
                    String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
                    logger.error(error, e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            }
        }
    }

}
