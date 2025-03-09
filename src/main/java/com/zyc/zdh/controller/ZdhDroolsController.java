package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.EtlDroolsTaskMapper;
import com.zyc.zdh.entity.EtlDroolsTaskInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
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
 * drools服务-废弃
 */
@Controller
public class ZdhDroolsController extends BaseController{

    public Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private EtlDroolsTaskMapper etlDroolsTaskMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

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
    @SentinelResource(value = "etl_task_drools_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_drools_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo etl_task_drools_detail(String id) {
        try{
            EtlDroolsTaskInfo etlDroolsTaskInfo=etlDroolsTaskMapper.selectByPrimaryKey(id);
            checkPermissionByProductAndDimGroup(zdhPermissionService, etlDroolsTaskInfo.getProduct_code(), etlDroolsTaskInfo.getDim_group());
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
    @SentinelResource(value = "etl_task_drools_list2", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_drools_list2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<EtlDroolsTaskInfo>> etl_task_drools_list2(String etl_context, String file_name, String product_code, String dim_group) throws Exception {
        try{
            List<EtlDroolsTaskInfo> etlDroolsTaskInfos = new ArrayList<EtlDroolsTaskInfo>();
            if(!StringUtils.isEmpty(etl_context)){
                etl_context=getLikeCondition(etl_context);
            }
            if(!StringUtils.isEmpty(file_name)){
                file_name=getLikeCondition(file_name);
            }
            Map<String,List<String>> dimMap = dynamicPermissionByProductAndGroup(zdhPermissionService);
            etlDroolsTaskInfos = etlDroolsTaskMapper.selectByParams(getOwner(), etl_context, file_name, product_code, dim_group, dimMap.get("product_codes"), dimMap.get("dim_groups"));
            dynamicAuth(zdhPermissionService, etlDroolsTaskInfos);

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
    @SentinelResource(value = "etl_task_drools_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_drools_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo etl_task_drools_add(EtlDroolsTaskInfo etlDroolsTaskInfo) {
        try{
            String id=SnowflakeIdWorker.getInstance().nextId() + "";
            etlDroolsTaskInfo.setId(id);
            etlDroolsTaskInfo.setOwner(getOwner());
            etlDroolsTaskInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlDroolsTaskInfo.getProduct_code(), etlDroolsTaskInfo.getDim_group(), getAttrAdd());
            etlDroolsTaskMapper.insertSelective(etlDroolsTaskInfo);
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
    @SentinelResource(value = "etl_task_drools_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_drools_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_drools_delete(String[] ids) {
        try{
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlDroolsTaskMapper, etlDroolsTaskMapper.getTable(), ids, getAttrDel());
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
    @SentinelResource(value = "etl_task_drools_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_drools_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo etl_task_drools_update(EtlDroolsTaskInfo etlDroolsTaskInfo) {
        try{
            String owner = getOwner();
            etlDroolsTaskInfo.setOwner(owner);
            EtlDroolsTaskInfo oldEtlDroolsTaskInfo = etlDroolsTaskMapper.selectByPrimaryKey(etlDroolsTaskInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlDroolsTaskInfo.getProduct_code(), etlDroolsTaskInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldEtlDroolsTaskInfo.getProduct_code(), oldEtlDroolsTaskInfo.getDim_group(), getAttrEdit());

            etlDroolsTaskMapper.updateByPrimaryKeySelective(etlDroolsTaskInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"更新失败", e);
        }
    }

}
