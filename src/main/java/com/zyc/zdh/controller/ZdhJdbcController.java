package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.EtlTaskJdbcMapper;
import com.zyc.zdh.dao.EtlTaskUpdateLogsMapper;
import com.zyc.zdh.entity.EtlTaskJdbcInfo;
import com.zyc.zdh.entity.EtlTaskUpdateLogs;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
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
import java.util.Map;

/**
 * MPP jdbc服务
 */
@Controller
public class ZdhJdbcController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;
    @Autowired
    EtlTaskJdbcMapper etlTaskJdbcMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * jdbc任务首页
     * @return
     */
    @RequestMapping("/etl_task_jdbc_index")
    public String etl_task_jdbc_index() {

        return "etl/etl_task_jdbc_index";
    }

    /**
     * jdbc任务新增首页
     * @return
     */
    @RequestMapping("/etl_task_jdbc_add_index")
    public String etl_task_jdbc_add_index() {

        return "etl/etl_task_jdbc_add_index";
    }


    /**
     * 模糊查询jdbc任务
     *
     * @param etl_context jdbc任务说明
     * @param id          jdbc任务id
     * @return
     */
    @SentinelResource(value = "etl_task_jdbc_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_jdbc_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<EtlTaskJdbcInfo>> etl_task_jdbc_list(String etl_context, String id, String product_code, String dim_group) {

        try{
            List<EtlTaskJdbcInfo> etlTaskJdbcInfos = new ArrayList<>();
            if(!StringUtils.isEmpty(etl_context)){
                etl_context=getLikeCondition(etl_context);
            }
            Map<String,List<String>> dimMap = dynamicPermissionByProductAndGroup(zdhPermissionService);
            etlTaskJdbcInfos = etlTaskJdbcMapper.selectByParams(getOwner(), etl_context, id, product_code, dim_group, dimMap.get("product_codes"), dimMap.get("dim_groups"));

            return ReturnInfo.buildSuccess(etlTaskJdbcInfos);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("jdbc任务列表查询失败", e);
        }

    }

    /**
     * 批量删除sql任务
     *
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "etl_task_jdbc_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_jdbc_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_jdbc_delete(String[] ids) {
        try {
            etlTaskJdbcMapper.deleteLogicByIds("etl_task_jdbc_info",ids, new Timestamp(new Date().getTime()));

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }

    /**
     * 新增sql 任务
     *
     * @param etlTaskJdbcInfo
     * @return
     */
    @SentinelResource(value = "etl_task_jdbc_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_jdbc_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_jdbc_add(EtlTaskJdbcInfo etlTaskJdbcInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try {
            String owner = getOwner();
            etlTaskJdbcInfo.setOwner(owner);
            etlTaskJdbcInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
            etlTaskJdbcInfo.setCreate_time(new Timestamp(new Date().getTime()));
            etlTaskJdbcInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTaskJdbcInfo.setIs_delete(Const.NOT_DELETE);

            debugInfo(etlTaskJdbcInfo);
            etlTaskJdbcMapper.insertSelective(etlTaskJdbcInfo);


            if (etlTaskJdbcInfo.getUpdate_context() != null && !etlTaskJdbcInfo.getUpdate_context().equals("")) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskJdbcInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlTaskJdbcInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insertSelective(etlTaskUpdateLogs);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 更新sql 任务
     *
     * @param etlTaskJdbcInfo
     * @return
     */
    @SentinelResource(value = "etl_task_jdbc_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_jdbc_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo etl_task_jdbc_update(EtlTaskJdbcInfo etlTaskJdbcInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try {
            String owner = getOwner();
            etlTaskJdbcInfo.setOwner(owner);
            etlTaskJdbcInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTaskJdbcInfo.setIs_delete(Const.NOT_DELETE);
            debugInfo(etlTaskJdbcInfo);

            etlTaskJdbcMapper.updateByPrimaryKeySelective(etlTaskJdbcInfo);

            EtlTaskJdbcInfo sti = etlTaskJdbcMapper.selectByPrimaryKey(etlTaskJdbcInfo.getId());

            if (etlTaskJdbcInfo.getUpdate_context() != null && !etlTaskJdbcInfo.getUpdate_context().equals("")
                    && !etlTaskJdbcInfo.getUpdate_context().equals(sti.getUpdate_context())) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskJdbcInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlTaskJdbcInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
                etlTaskUpdateLogs.setOwner(getOwner());
                etlTaskUpdateLogsMapper.insertSelective(etlTaskUpdateLogs);
            }
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
                    logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            }
        }
    }

}
