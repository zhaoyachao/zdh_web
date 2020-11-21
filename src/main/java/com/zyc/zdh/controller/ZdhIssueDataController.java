package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.SftpException;
import com.zyc.zdh.dao.EtlTaskUpdateLogsMapper;
import com.zyc.zdh.dao.IssueDataMapper;
import com.zyc.zdh.dao.ZdhNginxMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.DataSourcesService;
import com.zyc.zdh.service.EtlTaskService;
import com.zyc.zdh.util.DBUtil;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.SFTPUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 单源ETL任务服务
 */
@Controller
public class ZdhIssueDataController extends BaseController{


    @Autowired
    DataSourcesService dataSourcesServiceImpl;

    @Autowired
    EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;


    @Autowired
    IssueDataMapper issueDataMapper;


    /**
     * 单源ETL首页
     * @return
     */
    @RequestMapping("/data_ware_house_index")
    public String etl_task_index() {

        return "etl/data_ware_house_index";
    }

    /**
     * 获取单源ETL任务明细
     * @param ids
     * @return
     */
    @RequestMapping(value = "/data_ware_house_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_ware_house_list(String[] ids) {

        return JSON.toJSONString(null);
    }

    /**
     * 根据条件模糊查询单源ETL任务信息
     * @param issue_context
     * @return
     */
    @RequestMapping(value = "/data_ware_house_list2", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_ware_house_list2(String issue_context) {
        List<IssueDataInfo> list = new ArrayList<>();
        list = issueDataMapper.selectByParams(issue_context);

        return JSON.toJSONString(list);
    }

    @RequestMapping(value = "/data_ware_house_del", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_ware_house_del(String id) {
        int result = issueDataMapper.deleteByPrimaryKey(id);
        JSONObject json = new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }



    /**
     * 发布数据表信息
     *
     * @param issueDataInfo
     * @return
     */
    @RequestMapping("/issue_data_add")
    @ResponseBody
    public String issue_data_add(IssueDataInfo issueDataInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        String owner = getUser().getId();
        issueDataInfo.setOwner(owner);
        issueDataInfo.setCreate_time(new Timestamp(new Date().getTime()));
        debugInfo(issueDataInfo);

        issueDataMapper.insert(issueDataInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
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
