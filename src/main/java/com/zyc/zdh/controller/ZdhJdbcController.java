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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * MPP jdbc服务
 */
@Controller
public class ZdhJdbcController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;
    @Autowired
    private EtlTaskJdbcMapper etlTaskJdbcMapper;
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
            dynamicAuth(zdhPermissionService, etlTaskJdbcInfos);

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
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskJdbcMapper, etlTaskJdbcMapper.getTable(), ids, getAttrDel());
            etlTaskJdbcMapper.deleteLogicByIds(etlTaskJdbcMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
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
            etlTaskJdbcInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskJdbcInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskJdbcInfo.setIs_delete(Const.NOT_DELETE);

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskJdbcInfo.getProduct_code(), etlTaskJdbcInfo.getDim_group(), getAttrAdd());
            etlTaskJdbcMapper.insertSelective(etlTaskJdbcInfo);


            if (etlTaskJdbcInfo.getUpdate_context() != null && !etlTaskJdbcInfo.getUpdate_context().equals("")) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskJdbcInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlTaskJdbcInfo.getUpdate_context());
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
            etlTaskJdbcInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskJdbcInfo.setIs_delete(Const.NOT_DELETE);

            EtlTaskJdbcInfo oldEtlTaskJdbcInfo = etlTaskJdbcMapper.selectByPrimaryKey(etlTaskJdbcInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskJdbcInfo.getProduct_code(), etlTaskJdbcInfo.getDim_group(),getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldEtlTaskJdbcInfo.getProduct_code(), oldEtlTaskJdbcInfo.getDim_group(), getAttrEdit());


            etlTaskJdbcMapper.updateByPrimaryKeySelective(etlTaskJdbcInfo);



            if (etlTaskJdbcInfo.getUpdate_context() != null && !etlTaskJdbcInfo.getUpdate_context().equals("")
                    && !etlTaskJdbcInfo.getUpdate_context().equals(oldEtlTaskJdbcInfo.getUpdate_context())) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskJdbcInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlTaskJdbcInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(System.currentTimeMillis()));
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

}
