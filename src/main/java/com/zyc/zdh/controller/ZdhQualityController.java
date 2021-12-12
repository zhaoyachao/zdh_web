package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.SftpException;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.DataSourcesService;
import com.zyc.zdh.service.DispatchTaskService;
import com.zyc.zdh.service.EtlTaskService;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.SFTPUtil;
import org.apache.commons.lang3.StringUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class ZdhQualityController extends BaseController{


    @Autowired
    EtlTaskService etlTaskService;
    @Autowired
    QualityMapper qualityMapper;
    @Autowired
    QualityRuleMapper qualityRuleMapper;
    @Autowired
    QualityTaskMapper qualityTaskMapper;
    @Autowired
    ZdhHaInfoMapper zdhHaInfoMapper;

    /**
     * 质量检测规则首页
     * @return
     */
    @RequestMapping("/quality_rule_index")
    public String quality_rule_index() {

        return "etl/quality_rule_index";
    }

    @RequestMapping("/quality_rule_add_index")
    public String quality_rule_add_index() {

        return "etl/quality_rule_add_index";
    }

    @RequestMapping(value = "/quality_rule_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String quality_rule_list(QualityRuleInfo qualityRuleInfo) {

        List<QualityRuleInfo> qualityRuleInfos = new ArrayList<>();
        Example example = new Example(qualityRuleInfo.getClass());
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(qualityRuleInfo.getId())){
            criteria.andEqualTo("id", qualityRuleInfo.getId());
        }
        if(!StringUtils.isEmpty(qualityRuleInfo.getRule_code())){
            criteria.andLike("rule_code", "%"+qualityRuleInfo.getRule_code());
        }
        if(!StringUtils.isEmpty(qualityRuleInfo.getRule_name())){
            criteria.andLike("rule_name", "%"+qualityRuleInfo.getRule_name());
        }

        qualityRuleInfos = qualityRuleMapper.selectByExample(example);
        //etlTaskInfos = etlTaskService.selectByColumn(getUser().getId(), column_desc, column_alias, company, section, service);
        return JSON.toJSONString(qualityRuleInfos);
    }

    @RequestMapping(value = "/quality_rule_add", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional
    public String quality_rule_add(QualityRuleInfo qualityRuleInfo) {
        try{
            if(StringUtils.isEmpty(qualityRuleInfo.getRule_code())){
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"新增失败", "规则code不可为空");
            }
            Example example = new Example(qualityRuleInfo.getClass());
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("rule_code", qualityRuleInfo.getRule_code());
            int count = qualityRuleMapper.selectCountByExample(example);
            if(count>0){
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"新增失败", "规则code已存在,不可重复");
            }
            qualityRuleInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            qualityRuleInfo.setCreate_time(new Timestamp(new Date().getTime()));
            qualityRuleInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            qualityRuleInfo.setOwner(getUser().getId());
            qualityRuleMapper.insert(qualityRuleInfo);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }

    @RequestMapping(value = "/quality_rule_update", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional
    public String quality_rule_update(QualityRuleInfo qualityRuleInfo) {
        try{
            qualityRuleInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            qualityRuleInfo.setOwner(getUser().getId());
            qualityRuleMapper.updateByPrimaryKey(qualityRuleInfo);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"更新失败", e);
        }
    }

    /**
     * 数据质量任务
     * @return
     */
    @RequestMapping("/quality_task_index")
    public String quality_task_index() {

        return "etl/quality_task_index";
    }

    @RequestMapping("/quality_task_add_index")
    public String quality_task_add_index() {

        return "etl/quality_task_add_index";
    }

    @RequestMapping(value = "/quality_task_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String quality_task_list(QualityTaskInfo qualityTaskInfo, String rule_code) {

        List<QualityTaskInfo> qualityTaskInfos = new ArrayList<>();
        Example example = new Example(qualityTaskInfo.getClass());
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(qualityTaskInfo.getId())){
            criteria.andEqualTo("id", qualityTaskInfo.getId());
        }
        if(!StringUtils.isEmpty(qualityTaskInfo.getQuality_context())){
            Example.Criteria criteria1 = example.createCriteria();
            criteria1.orLike("quality_context", "%"+qualityTaskInfo.getQuality_context()+"%");
            criteria1.orLike("data_sources_table_name_input", "%"+qualityTaskInfo.getQuality_context()+"%");
            criteria1.orLike("data_sources_file_name_input", "%"+qualityTaskInfo.getQuality_context()+"%");
            criteria1.orLike("data_source_type_input", "%"+qualityTaskInfo.getQuality_context()+"%");
            criteria1.orLike("quality_rule_config", "%"+qualityTaskInfo.getQuality_context()+"%");
            example.and(criteria1);
        }
        if(!StringUtils.isEmpty(rule_code)){
            criteria.andLike("quality_rule_config", "%"+rule_code+"%");
        }

        qualityTaskInfos = qualityTaskMapper.selectByExample(example);
        //etlTaskInfos = etlTaskService.selectByColumn(getUser().getId(), column_desc, column_alias, company, section, service);
        return JSON.toJSONString(qualityTaskInfos);
    }

    @RequestMapping(value = "/quality_task_add", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional
    public String quality_task_add(QualityTaskInfo qualityTaskInfo,String[] quality_rule, String[] quality_columns) {
        try{
            qualityTaskInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            qualityTaskInfo.setCreate_time(new Timestamp(new Date().getTime()));
            qualityTaskInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            qualityTaskInfo.setOwner(getUser().getId());

            JSONArray jsonArray=new JSONArray();
            for (int i=0;i<quality_rule.length;i++){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("quality_rule", quality_rule[i]);
                jsonObject.put("quality_columns", quality_columns[i]);
                jsonArray.add(jsonObject);
            }
            qualityTaskInfo.setQuality_rule_config(jsonArray.toJSONString());
            qualityTaskMapper.insert(qualityTaskInfo);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }

    @RequestMapping(value = "/quality_task_update", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional
    public String quality_task_update(QualityTaskInfo qualityTaskInfo,String[] quality_rule, String[] quality_columns) {
        try{
            qualityTaskInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            qualityTaskInfo.setOwner(getUser().getId());
            JSONArray jsonArray=new JSONArray();
            for (int i=0;i<quality_rule.length;i++){
                JSONObject jsonObject=new JSONObject();
                jsonObject.put("quality_rule", quality_rule[i]);
                jsonObject.put("quality_columns", quality_columns[i]);
                jsonArray.add(jsonObject);
            }
            qualityTaskInfo.setQuality_rule_config(jsonArray.toJSONString());
            qualityTaskMapper.updateByPrimaryKey(qualityTaskInfo);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"更新失败", e);
        }
    }

    @RequestMapping(value = "/quality_task_delete", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional
    public String quality_task_delete(String[] ids) {
        try{
            Example example = new Example(QualityTaskInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("id", Arrays.asList(ids));
            qualityTaskMapper.deleteByExample(example);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }
    }

    /**
     * 质量报告首页
     * @return
     */
    @RequestMapping("/quality_index")
    public String etl_task_index() {

        return "etl/quality_index";
    }

    @RequestMapping("/quota_index")
    public String quota_index() {

        return "etl/quota_index";
    }

    /**
     * 指标明细
     * @param column_desc
     * @param column_alias
     * @param company
     * @param section
     * @param service
     * @return
     */
    @RequestMapping(value = "/quota_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String quota_list(String column_desc, String column_alias, String company, String section, String service) {

        List<QuotaInfo> etlTaskInfos = new ArrayList<>();
        etlTaskInfos = etlTaskService.selectByColumn(getUser().getId(), column_desc, column_alias, company, section, service);
        return JSON.toJSONString(etlTaskInfos);
    }


    /**
     * 质量报告明细
     * @param job_context
     * @param etl_context
     * @param status
     * @return
     */
    @RequestMapping(value = "/quality_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String quality_list(String job_context, String etl_context, String status) {

        List<QualityInfo> qualities = new ArrayList<>();

        Example example=new Example(QualityInfo.class);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(job_context)){
            criteria.andLike("job_context","%"+job_context+"%");
        }
        if(!StringUtils.isEmpty(etl_context)){
            criteria.andLike("etl_context","%"+etl_context+"%");
        }
        if(!StringUtils.isEmpty(status)){
            criteria.andEqualTo("status",status);
        }

        criteria.andEqualTo("owner",getUser().getId());

        qualities = qualityMapper.selectByExample(example);

        return JSON.toJSONString(qualities);
    }


    /**
     * 删除质量报告
     * @param ids
     * @return
     */
    @RequestMapping(value = "/quality_delete", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional
    public String quality_delete(String[] ids) {

        try{
            Example example=new Example(QualityInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("id", Arrays.asList(ids));
            qualityMapper.deleteByExample(example);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"删除失败", e);
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
                    e.printStackTrace();
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
    }

}
