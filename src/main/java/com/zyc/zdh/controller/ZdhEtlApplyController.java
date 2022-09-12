package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.zyc.zdh.dao.DataSourcesMapper;
import com.zyc.zdh.dao.EtlApplyTaskMapper;
import com.zyc.zdh.dao.EtlTaskUpdateLogsMapper;
import com.zyc.zdh.dao.ZdhNginxMapper;
import com.zyc.zdh.entity.*;
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 申请源ETL任务服务
 */
@Controller
public class ZdhEtlApplyController extends BaseController{
    public Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    DataSourcesMapper dataSourcesMapper;
    @Autowired
    EtlApplyTaskMapper etlApplyTaskMapper;
    @Autowired
    EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;
    @Autowired
    ZdhNginxMapper zdhNginxMapper;


    /**
     * 申请源首页
     * @return
     */
    @RequestMapping("/etl_task_apply_index")
    public String etl_task_apply_index() {

        return "etl/etl_task_apply_index";
    }

    /**
     * 申请源明细
     * @param id 数据发布ID
     * @return
     */
    @RequestMapping(value = "/etl_task_apply_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<EtlApplyTaskInfo> etl_task_apply_detail(String id) {

        try{
            EtlApplyTaskInfo etlApplyTaskInfo=etlApplyTaskMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", etlApplyTaskInfo);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 根据条件模糊查询申请源ETL任务信息
     * @param etl_context
     * @param file_name
     * @return
     */
    @RequestMapping(value = "/etl_task_apply_list2", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String etl_task_list2(String etl_context, String file_name) {
        try{
            List<EtlTaskInfo> list = new ArrayList<>();
            if(!StringUtils.isEmpty(etl_context)){
                etl_context=getLikeCondition(etl_context);
            }
            if(!StringUtils.isEmpty(file_name)){
                file_name=getLikeCondition(file_name);
            }
            list = etlApplyTaskMapper.selectByParams(getOwner(), etl_context, file_name);
            return JSON.toJSONString(list);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return JSON.toJSONString(e.getMessage());
        }

    }

    /**
     * 批量删除申请源ETL任务
     * @param ids
     * @return
     */
    @RequestMapping(value = "/etl_task_apply_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_apply_task_delete(String[] ids) {
        try{
            etlApplyTaskMapper.deleteLogicByIds("etl_apply_task_info",ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }
    }

    /**
     * 新增Apply ETL任务首页
     * @param request
     * @param response
     * @param id
     * @param edit
     * @return
     */
    @RequestMapping("/etl_task_apply_add_index")
    public String etl_task_apply_add_index(HttpServletRequest request, HttpServletResponse response, Long id, String edit) {

        request.setAttribute("edit", edit);
        return "etl/etl_task_apply_add_index";
    }


    /**
     * 新增申请源ETL任务
     * 如果输入数据源类型是外部上传,会补充文件服务器信息
     * @param etlApplyTaskInfo
     * @return
     */
    @RequestMapping(value = "/etl_task_apply_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_apply_task_add(EtlApplyTaskInfo etlApplyTaskInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try{
            String owner = getOwner();
            etlApplyTaskInfo.setOwner(owner);
            debugInfo(etlApplyTaskInfo);

            etlApplyTaskInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
            etlApplyTaskInfo.setCreate_time(new Timestamp(new Date().getTime()));
            etlApplyTaskInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            etlApplyTaskInfo.setIs_delete(Const.NOT_DELETE);

            etlApplyTaskMapper.insert(etlApplyTaskInfo);
            if (etlApplyTaskInfo.getUpdate_context() != null && !etlApplyTaskInfo.getUpdate_context().equals("")) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlApplyTaskInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlApplyTaskInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insert(etlTaskUpdateLogs);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }




    /**
     * 申请源ETL任务更新
     *
     * @param etlApplyTaskInfo
     * @return
     */
    @RequestMapping(value = "/etl_task_apply_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_apply_update(EtlApplyTaskInfo etlApplyTaskInfo) {
        try{
            String owner = getOwner();
            etlApplyTaskInfo.setOwner(owner);
            etlApplyTaskInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            etlApplyTaskInfo.setIs_delete(Const.NOT_DELETE);
            debugInfo(etlApplyTaskInfo);
            if (etlApplyTaskInfo.getData_source_type_input().equals("外部上传")) {
                ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(owner);
                String[] file_name_ary = etlApplyTaskInfo.getData_sources_file_name_input().split("/");
                String file_name = file_name_ary[0];
                if (file_name_ary.length > 0)
                    file_name = file_name_ary[file_name_ary.length - 1];

                if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                    etlApplyTaskInfo.setData_sources_file_name_input(zdhNginx.getNginx_dir() + "/" + owner + "/" + file_name);
                } else {
                    etlApplyTaskInfo.setData_sources_file_name_input(zdhNginx.getTmp_dir() + "/" + owner + "/" + file_name);
                }

            }

            //获取旧数据是否更新说明
            EtlApplyTaskInfo etlApplyTaskInfo1 = etlApplyTaskMapper.selectByIds(new String[]{etlApplyTaskInfo.getId()});

            etlApplyTaskMapper.updateByPrimaryKey(etlApplyTaskInfo);

            if (etlApplyTaskInfo.getUpdate_context() != null && !etlApplyTaskInfo.getUpdate_context().equals("")
                    && !etlApplyTaskInfo1.getUpdate_context().equals(etlApplyTaskInfo.getUpdate_context())) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlApplyTaskInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlApplyTaskInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insert(etlTaskUpdateLogs);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
                     logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            }
        }
    }

}
