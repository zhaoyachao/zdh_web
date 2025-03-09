package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.EtlMoreTaskMapper;
import com.zyc.zdh.entity.EtlMoreTaskInfo;
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
 * 多源ETL服务
 */
@Controller
public class ZdhMoreEtlController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private EtlMoreTaskMapper etlMoreTaskMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

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
    @SentinelResource(value = "etl_task_more_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_more_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo etl_task_more_detail(String id) {
        try {
            EtlMoreTaskInfo etlMoreTaskInfo = etlMoreTaskMapper.selectByPrimaryKey(id);
            checkPermissionByProductAndDimGroup(zdhPermissionService, etlMoreTaskInfo.getProduct_code(),etlMoreTaskInfo.getDim_group());
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
    @SentinelResource(value = "etl_task_more_list2", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_more_list2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<EtlMoreTaskInfo>> etl_task_more_list2(String etl_context, String file_name, String product_code, String dim_group) {
        try{
            List<EtlMoreTaskInfo> etlMoreTaskInfos = new ArrayList<EtlMoreTaskInfo>();
            if(!StringUtils.isEmpty(etl_context)){
                etl_context=getLikeCondition(etl_context);
            }
            if(!StringUtils.isEmpty(file_name)){
                file_name=getLikeCondition(file_name);
            }
            Map<String,List<String>> dimMap = dynamicPermissionByProductAndGroup(zdhPermissionService);
            etlMoreTaskInfos = etlMoreTaskMapper.selectByParams(getOwner(), etl_context, file_name, product_code, dim_group, dimMap.get("product_codes"), dimMap.get("dim_groups"));
            dynamicAuth(zdhPermissionService, etlMoreTaskInfos);

            return ReturnInfo.buildSuccess(etlMoreTaskInfos);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("多源ETL任务查询失败", e);
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
    @SentinelResource(value = "etl_task_more_sources_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_more_sources_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo etl_task_more_sources_add(EtlMoreTaskInfo etlMoreTaskInfo) {
        try {
            etlMoreTaskInfo.setOwner(getOwner());
            etlMoreTaskInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            etlMoreTaskInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            etlMoreTaskInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlMoreTaskInfo.setIs_delete(Const.NOT_DELETE);

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlMoreTaskInfo.getProduct_code(), etlMoreTaskInfo.getDim_group(), getAttrAdd());
            etlMoreTaskMapper.insertSelective(etlMoreTaskInfo);
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
    @SentinelResource(value = "etl_task_more_sources_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_more_sources_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_more_sources_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlMoreTaskMapper, etlMoreTaskMapper.getTable(), ids, getAttrDel());
            etlMoreTaskMapper.deleteLogicByIds(etlMoreTaskMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
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
    @SentinelResource(value = "etl_task_more_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_more_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo etl_task_more_update(EtlMoreTaskInfo etlMoreTaskInfo) {
        try {
            String owner = getOwner();
            etlMoreTaskInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlMoreTaskInfo.setIs_delete(Const.NOT_DELETE);
            etlMoreTaskInfo.setOwner(owner);

            EtlMoreTaskInfo oldEtlMoreTaskInfo = etlMoreTaskMapper.selectByPrimaryKey(etlMoreTaskInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlMoreTaskInfo.getProduct_code(), etlMoreTaskInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldEtlMoreTaskInfo.getProduct_code(), oldEtlMoreTaskInfo.getDim_group(), getAttrEdit());


            etlMoreTaskMapper.updateByPrimaryKeySelective(etlMoreTaskInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

}
