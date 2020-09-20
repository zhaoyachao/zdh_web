package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.EtlMoreTaskInfo;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.DataSourcesService;
import com.zyc.zdh.service.DispatchTaskService;
import com.zyc.zdh.service.EtlTaskService;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.shiro.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ZdhMoreEtlController extends BaseController{

    @Autowired
    EtlMoreTaskMapper etlMoreTaskMapper;


    /**
     * 多源ETL任务首页
     * @return
     */
    @RequestMapping("/etl_task_more_sources_index")
    public String etl_task_more_sources_index() {

        return "etl/etl_task_more_sources_index";
    }

    /**
     * 根据指定任务id,或者查询当前用户下的所有多源任务
     * @param ids
     * @return
     */
    @RequestMapping(value = "/etl_task_more_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String etl_task_more_list(String[] ids) {
        EtlMoreTaskInfo etlMoreTaskInfo = new EtlMoreTaskInfo();
        etlMoreTaskInfo.setOwner(getUser().getId());
        List<EtlMoreTaskInfo> etlMoreTaskInfos = new ArrayList<EtlMoreTaskInfo>();
        if (ids == null) {
            etlMoreTaskInfos = etlMoreTaskMapper.select(etlMoreTaskInfo);
        } else {
            etlMoreTaskInfos.add(etlMoreTaskMapper.selectByPrimaryKey(ids[0]));
        }

        return JSON.toJSONString(etlMoreTaskInfos);
    }

    /**
     * 模糊查询多源ETL任务信息
     * @param etl_context 任务说明
     * @param file_name 输出文件名/表名
     * @return
     */
    @RequestMapping(value = "/etl_task_more_list2", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String etl_task_more_list2(String etl_context, String file_name) {
        List<EtlMoreTaskInfo> etlMoreTaskInfos = new ArrayList<EtlMoreTaskInfo>();
        etlMoreTaskInfos = etlMoreTaskMapper.selectByParams(getUser().getId(), etl_context, file_name);
        return JSON.toJSONString(etlMoreTaskInfos);
    }


    /**
     * 新增多源ETL任务首页
     * @return
     */
    @RequestMapping("/etl_task_more_sources_add_index")
    public String etl_task_more_sources_add_index() {

        return "etl/etl_task_more_sources_add_index";
    }

    /**
     * 新增多源ETL任务
     * @param etlMoreTaskInfo
     * @return
     */
    @RequestMapping("/etl_task_more_sources_add")
    @ResponseBody
    public String etl_task_more_sources_add(EtlMoreTaskInfo etlMoreTaskInfo) {
        etlMoreTaskInfo.setOwner(getUser().getId());
        etlMoreTaskInfo.setCreate_time(new Timestamp(new Date().getTime()));
        debugInfo(etlMoreTaskInfo);
        etlMoreTaskMapper.insert(etlMoreTaskInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    /**
     * 删除多源ETL任务
     * @param ids
     * @return
     */
    @RequestMapping("/etl_task_more_sources_delete")
    @ResponseBody
    public String etl_task_more_sources_delete(String[] ids) {
        if (ids != null) {
            for (String id : ids) {
                etlMoreTaskMapper.deleteBatchById(id);
            }
        }
        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    /**
     * 更新多源ETL任务
     * @param etlMoreTaskInfo
     * @return
     */
    @RequestMapping("/etl_task_more_update")
    @ResponseBody
    public String etl_task_more_update(EtlMoreTaskInfo etlMoreTaskInfo) {
        String owner = getUser().getId();
        etlMoreTaskInfo.setOwner(owner);
        debugInfo(etlMoreTaskInfo);

        etlMoreTaskMapper.updateByPrimaryKey(etlMoreTaskInfo);

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
