package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.EtlTaskMapper;
import com.zyc.zdh.dao.QualityMapper;
import com.zyc.zdh.dao.QualityRuleMapper;
import com.zyc.zdh.dao.QualityTaskMapper;
import com.zyc.zdh.entity.*;
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
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 质量检测服务
 */
@Controller
public class ZdhQualityController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private QualityMapper qualityMapper;
    @Autowired
    private QualityRuleMapper qualityRuleMapper;
    @Autowired
    private QualityTaskMapper qualityTaskMapper;
    @Autowired
    private EtlTaskMapper etlTaskMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 质量检测规则首页
     *
     * @return
     */
    @RequestMapping("/quality_rule_index")
    public String quality_rule_index() {

        return "etl/quality_rule_index";
    }

    /**
     * 质量检测规则新增首页
     * @return
     */
    @RequestMapping("/quality_rule_add_index")
    public String quality_rule_add_index() {

        return "etl/quality_rule_add_index";
    }

    /**
     * 质量检测规则列表
     * @param qualityRuleInfo
     * @return
     */
    @SentinelResource(value = "quality_rule_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/quality_rule_list",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<QualityRuleInfo>> quality_rule_list(QualityRuleInfo qualityRuleInfo) {
        try{
            List<QualityRuleInfo> qualityRuleInfos = new ArrayList<>();
            Example example = new Example(qualityRuleInfo.getClass());
            Example.Criteria criteria = example.createCriteria();
            if (!StringUtils.isEmpty(qualityRuleInfo.getId())) {
                criteria.andEqualTo("id", qualityRuleInfo.getId());
            }
            if (!StringUtils.isEmpty(qualityRuleInfo.getRule_code())) {
                criteria.andLike("rule_code", getLikeCondition(qualityRuleInfo.getRule_code()));
            }
            if (!StringUtils.isEmpty(qualityRuleInfo.getRule_name())) {
                criteria.andLike("rule_name", getLikeCondition(qualityRuleInfo.getRule_name()));
            }

            qualityRuleInfos = qualityRuleMapper.selectByExample(example);
            return ReturnInfo.buildSuccess(qualityRuleInfos);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("质量检测规则列表查询失败", e);
        }

    }

    /**
     * 质量检测规则新增
     * @param qualityRuleInfo
     * @return
     */
    @SentinelResource(value = "quality_rule_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/quality_rule_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo quality_rule_add(QualityRuleInfo qualityRuleInfo) {
        try {
            if (StringUtils.isEmpty(qualityRuleInfo.getRule_code())) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", "规则code不可为空");
            }
            Example example = new Example(qualityRuleInfo.getClass());
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("rule_code", qualityRuleInfo.getRule_code());
            int count = qualityRuleMapper.selectCountByExample(example);
            if (count > 0) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", "规则code已存在,不可重复");
            }
            qualityRuleInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
            qualityRuleInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            qualityRuleInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            qualityRuleInfo.setOwner(getOwner());
            //checkPermissionByProductAndDimGroup(zdhPermissionService, qualityRuleInfo.getProduct_code(), qualityRuleInfo.getDim_group());

            qualityRuleMapper.insertSelective(qualityRuleInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 质量检测规则更新
     * @param qualityRuleInfo
     * @return
     */
    @SentinelResource(value = "quality_rule_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/quality_rule_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo quality_rule_update(QualityRuleInfo qualityRuleInfo) {
        try {
            qualityRuleInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            qualityRuleInfo.setOwner(getOwner());
            qualityRuleMapper.updateByPrimaryKeySelective(qualityRuleInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 数据质量任务
     *
     * @return
     */
    @RequestMapping("/quality_task_index")
    public String quality_task_index() {

        return "etl/quality_task_index";
    }

    /**
     * 质量检测任务新增首页
     * @return
     */
    @RequestMapping("/quality_task_add_index")
    public String quality_task_add_index() {

        return "etl/quality_task_add_index";
    }

    /**
     * 质量检测列表
     * @param qualityTaskInfo
     * @param rule_code 规则code
     * @return
     */
    @SentinelResource(value = "quality_task_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/quality_task_list",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<QualityTaskInfo>> quality_task_list(QualityTaskInfo qualityTaskInfo, String rule_code) {

        try{
            List<QualityTaskInfo> qualityTaskInfos = new ArrayList<>();
            Example example = new Example(qualityTaskInfo.getClass());
            Example.Criteria criteria = example.createCriteria();
            if (!StringUtils.isEmpty(qualityTaskInfo.getId())) {
                criteria.andEqualTo("id", qualityTaskInfo.getId());
            }
            if (!StringUtils.isEmpty(qualityTaskInfo.getQuality_context())) {
                Example.Criteria criteria1 = example.createCriteria();
                criteria1.orLike("quality_context", getLikeCondition(qualityTaskInfo.getQuality_context()));
                criteria1.orLike("data_sources_table_name_input", getLikeCondition(qualityTaskInfo.getQuality_context()));
                criteria1.orLike("data_sources_file_name_input", getLikeCondition(qualityTaskInfo.getQuality_context()));
                criteria1.orLike("data_source_type_input", getLikeCondition(qualityTaskInfo.getQuality_context()));
                criteria1.orLike("quality_rule_config", getLikeCondition(qualityTaskInfo.getQuality_context()));
                example.and(criteria1);
            }
            if (!StringUtils.isEmpty(rule_code)) {
                criteria.andLike("quality_rule_config", getLikeCondition(rule_code));
            }

            qualityTaskInfos = qualityTaskMapper.selectByExample(example);
            return ReturnInfo.buildSuccess(qualityTaskInfos);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("", e);
        }

    }

    /**
     * 质量检测任务新增
     * @param qualityTaskInfo
     * @param quality_rule 质量检测规则
     * @param quality_columns 质量检测字段
     * @return
     */
    @SentinelResource(value = "quality_task_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/quality_task_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo quality_task_add(QualityTaskInfo qualityTaskInfo, String[] quality_rule, String[] quality_columns) {
        try {
            qualityTaskInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
            qualityTaskInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            qualityTaskInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            qualityTaskInfo.setOwner(getOwner());

            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < quality_rule.length; i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("quality_rule", quality_rule[i]);
                jsonObject.put("quality_columns", quality_columns[i]);
                jsonArray.add(jsonObject);
            }
            qualityTaskInfo.setQuality_rule_config(jsonArray.toJSONString());
            qualityTaskMapper.insertSelective(qualityTaskInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 质量检测任务更新
     * @param qualityTaskInfo
     * @param quality_rule 质量检测规则
     * @param quality_columns 质量检测字段
     * @return
     */
    @SentinelResource(value = "quality_task_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/quality_task_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo quality_task_update(QualityTaskInfo qualityTaskInfo, String[] quality_rule, String[] quality_columns) {
        try {
            qualityTaskInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            qualityTaskInfo.setOwner(getOwner());
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < quality_rule.length; i++) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("quality_rule", quality_rule[i]);
                jsonObject.put("quality_columns", quality_columns[i]);
                jsonArray.add(jsonObject);
            }
            qualityTaskInfo.setQuality_rule_config(jsonArray.toJSONString());
            qualityTaskMapper.updateByPrimaryKeySelective(qualityTaskInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 质量检测任务删除
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "quality_task_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/quality_task_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo quality_task_delete(String[] ids) {
        try {
            checkPermissionByProductAndDimGroup(zdhPermissionService, qualityTaskMapper, qualityTaskMapper.getTable(), ids);
            Example example = new Example(QualityTaskInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("id", Arrays.asList(ids));
            qualityTaskMapper.deleteByExample(example);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }

    /**
     * 质量报告首页
     *
     * @return
     */
    @RequestMapping("/quality_index")
    public String etl_task_index() {

        return "etl/quality_index";
    }

    /**
     * 指标首页
     * @return
     */
    @RequestMapping("/quota_index")
    public String quota_index() {

        return "etl/quota_index";
    }

    /**
     * 指标明细
     *
     * @param column_desc
     * @param column_alias
     * @param company
     * @param section
     * @param service
     * @return
     */
    @SentinelResource(value = "quota_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/quota_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<QuotaInfo>> quota_list(String column_desc, String column_alias, String company, String section, String service) {

        try{
            List<QuotaInfo> etlTaskInfos = new ArrayList<>();
            if(!StringUtils.isEmpty(column_desc)){
                column_desc = getLikeCondition(column_desc);
            }
            if(!StringUtils.isEmpty(column_alias)){
                column_alias = getLikeCondition(column_alias);
            }
            if(!StringUtils.isEmpty(company)){
                company = getLikeCondition(company);
            }
            if(!StringUtils.isEmpty(section)){
                section = getLikeCondition(section);
            }
            if(!StringUtils.isEmpty(service)){
                service = getLikeCondition(service);
            }
            etlTaskInfos = etlTaskMapper.selectByColumn(getOwner(), column_desc, column_alias, company, section, service);
            return ReturnInfo.buildSuccess(etlTaskInfos);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("指标列表查询失败", e);
        }

    }


    /**
     * 质量报告明细
     *
     * @param job_context
     * @param etl_context
     * @param status
     * @return
     */
    @SentinelResource(value = "quality_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/quality_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<QualityInfo>> quality_list(String job_context, String etl_context, String status) {

        try{
            List<QualityInfo> qualities = new ArrayList<>();

            Example example = new Example(QualityInfo.class);
            Example.Criteria criteria = example.createCriteria();
            if (!StringUtils.isEmpty(job_context)) {
                criteria.andLike("job_context", "%" + job_context + "%");
            }
            if (!StringUtils.isEmpty(etl_context)) {
                criteria.andLike("etl_context", "%" + etl_context + "%");
            }
            if (!StringUtils.isEmpty(status)) {
                criteria.andEqualTo("status", status);
            }

            criteria.andEqualTo("owner", getOwner());

            qualities = qualityMapper.selectByExample(example);

            return ReturnInfo.buildSuccess(qualities);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("质量报告列表查询失败", e);
        }
    }


    /**
     * 删除质量报告
     *
     * @param ids
     * @return
     */
    @SentinelResource(value = "quality_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/quality_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo quality_delete(String[] ids) {

        try {
            Example example = new Example(QualityInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("id", Arrays.asList(ids));
            qualityMapper.deleteByExample(example);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
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
                    logger.error(error);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}");
            }
        }
    }

}
