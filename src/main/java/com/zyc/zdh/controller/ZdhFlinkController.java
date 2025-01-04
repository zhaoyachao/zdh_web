package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.EtlTaskFlinkMapper;
import com.zyc.zdh.dao.EtlTaskUpdateLogsMapper;
import com.zyc.zdh.entity.EtlTaskFlinkInfo;
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
import java.util.List;
import java.util.Map;

/**
 * FLINK采集服务
 */
@Controller
public class ZdhFlinkController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;
    @Autowired
    private EtlTaskFlinkMapper etlTaskFlinkMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;
    /**
     * flink任务首页
     * @return
     */
    @RequestMapping("/etl_task_flink_index")
    public String etl_task_flink_index() {

        return "etl/etl_task_flink_index";
    }

    /**
     * flink任务新增首页
     * @return
     */
    @RequestMapping("/etl_task_flink_add_index")
    public String etl_task_flink_add_index() {

        return "etl/etl_task_flink_add_index";
    }

    /**
     * 模糊查询flink任务
     *
     * @param sql_context sql任务说明
     * @param id          sql任务id
     * @return
     */
    @SentinelResource(value = "etl_task_flink_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_flink_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<EtlTaskFlinkInfo>> etl_task_flink_list(String sql_context, String id, String product_code, String dim_group) {

        try{
            List<EtlTaskFlinkInfo> sqlTaskInfos = new ArrayList<>();
            if(!StringUtils.isEmpty(sql_context)){
                sql_context=getLikeCondition(sql_context);
            }
            Map<String,List<String>> dimMap = dynamicPermissionByProductAndGroup(zdhPermissionService);
            sqlTaskInfos = etlTaskFlinkMapper.selectByParams(getOwner(), sql_context, id, product_code, dim_group, dimMap.get("product_codes"), dimMap.get("dim_groups"));

            return ReturnInfo.buildSuccess(sqlTaskInfos);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("flink任务列表查询失败", e);
        }

    }

    /**
     * 批量删除sql任务
     *
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "etl_task_flink_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_flink_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_flink_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskFlinkMapper, etlTaskFlinkMapper.getTable(), ids, getAttrDel());
            etlTaskFlinkMapper.deleteLogicByIds(etlTaskFlinkMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }

    /**
     * 新增flink任务
     *
     * @param etlTaskFlinkInfo
     * @return
     */
    @SentinelResource(value = "etl_task_flink_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_flink_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_flink_add(EtlTaskFlinkInfo etlTaskFlinkInfo) {
        //String json_str=JsonUtil.formatJsonString(request.getParameterMap());
        try {
            String owner = getOwner();
            etlTaskFlinkInfo.setOwner(owner);
            etlTaskFlinkInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
            etlTaskFlinkInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskFlinkInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskFlinkInfo.setIs_delete(Const.NOT_DELETE);

            debugInfo(etlTaskFlinkInfo);

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskFlinkInfo.getProduct_code(), etlTaskFlinkInfo.getDim_group(), getAttrAdd());

            etlTaskFlinkMapper.insertSelective(etlTaskFlinkInfo);


            if (etlTaskFlinkInfo.getUpdate_context() != null && !etlTaskFlinkInfo.getUpdate_context().equals("")) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskFlinkInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlTaskFlinkInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(System.currentTimeMillis()));
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
     * 更新flink任务
     *
     * @param etlTaskFlinkInfo
     * @return
     */
    @SentinelResource(value = "etl_task_flink_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_flink_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_flink_update(EtlTaskFlinkInfo etlTaskFlinkInfo) {
        //String json_str=JsonUtil.formatJsonString(request.getParameterMap());
        try {
            String owner = getOwner();
            etlTaskFlinkInfo.setOwner(owner);
            etlTaskFlinkInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskFlinkInfo.setIs_delete(Const.NOT_DELETE);
            debugInfo(etlTaskFlinkInfo);

            EtlTaskFlinkInfo oldEtlTaskFlinkInfo = etlTaskFlinkMapper.selectByPrimaryKey(etlTaskFlinkInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskFlinkInfo.getProduct_code(), etlTaskFlinkInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldEtlTaskFlinkInfo.getProduct_code(), oldEtlTaskFlinkInfo.getDim_group(), getAttrEdit());

            etlTaskFlinkMapper.updateByPrimaryKeySelective(etlTaskFlinkInfo);

            if (etlTaskFlinkInfo.getUpdate_context() != null && !etlTaskFlinkInfo.getUpdate_context().equals("")
                    && !etlTaskFlinkInfo.getUpdate_context().equals(oldEtlTaskFlinkInfo.getUpdate_context())) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskFlinkInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlTaskFlinkInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                etlTaskUpdateLogs.setOwner(getOwner());
                etlTaskUpdateLogsMapper.insertSelective(etlTaskUpdateLogs);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
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
