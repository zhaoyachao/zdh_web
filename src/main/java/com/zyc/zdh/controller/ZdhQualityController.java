package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.SftpException;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.DataSourcesService;
import com.zyc.zdh.service.DispatchTaskService;
import com.zyc.zdh.service.EtlTaskService;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.SFTPUtil;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Controller
public class ZdhQualityController extends BaseController{


    @Autowired
    EtlTaskService etlTaskService;
    @Autowired
    QualityMapper qualityMapper;
    @Autowired
    ZdhHaInfoMapper zdhHaInfoMapper;


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

        List<Quality> qualities = new ArrayList<>();

        qualities = qualityMapper.selectByOwner(getUser().getId(), job_context, etl_context, status);

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
            for (String id : ids) {
                qualityMapper.deleteByPrimaryKey(id);
            }
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
