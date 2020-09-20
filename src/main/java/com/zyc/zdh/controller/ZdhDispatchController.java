package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.SftpException;
import com.zyc.zdh.config.DateConverter;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.JobCommon;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.DataSourcesService;
import com.zyc.zdh.service.DispatchTaskService;
import com.zyc.zdh.service.EtlTaskService;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.DBUtil;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.HttpUtil;
import com.zyc.zdh.util.SFTPUtil;
import org.apache.shiro.SecurityUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class ZdhDispatchController extends BaseController{


    @Autowired
    DispatchTaskService dispatchTaskService;
    @Autowired
    ZdhLogsService zdhLogsService;
    @Autowired
    QuartzJobMapper quartzJobMapper;
    @Autowired
    QuartzManager2 quartzManager2;
    @Autowired
    ZdhHaInfoMapper zdhHaInfoMapper;
    @Autowired
    EtlTaskService etlTaskService;




    /**
     * 调度任务首页
     * @return
     */
    @RequestMapping("/dispatch_task_index")
    public String dispatch_task_index() {
        return "/etl/dispatch_task_index";
    }

    /**
     * 调度任务明细
     * @param ids
     * @return
     */
    @RequestMapping(value = "/dispatch_task_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String dispatch_task_list(String[] ids) {
        List<QuartzJobInfo> list = new ArrayList<>();
        QuartzJobInfo quartzJobInfo = new QuartzJobInfo();
        quartzJobInfo.setOwner(getUser().getId());
        if (ids == null)
            list = quartzJobMapper.selectByOwner(quartzJobInfo.getOwner());
        else {
            quartzJobInfo.setJob_id(ids[0]);
            list.add(quartzJobMapper.selectByPrimaryKey(quartzJobInfo));
        }

        return JSON.toJSONString(list);
    }

    /**
     * 模糊匹配调度任务明细
     * @param job_context
     * @param etl_context
     * @return
     */
    @RequestMapping(value = "/dispatch_task_list2", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String dispatch_task_list2(String job_context, String etl_context,String status,String last_status) {
        List<QuartzJobInfo> list = new ArrayList<>();
        list = quartzJobMapper.selectByParams(getUser().getId(), job_context, etl_context,status,last_status);
        return JSON.toJSONString(list);
    }

    /**
     * 新增调度任务首页
     * @return
     */
    @RequestMapping("/dispatch_task_add_index")
    public String dispatch_task_add_index() {
        return "/etl/dispatch_task_add_index";
    }

    /**
     * 新增调度任务
     * @param quartzJobInfo
     * @return
     */
    @RequestMapping("/dispatch_task_add")
    @ResponseBody
    public String dispatch_task_add(QuartzJobInfo quartzJobInfo) {
        debugInfo(quartzJobInfo);
        quartzJobInfo.setOwner(getUser().getId());
        quartzJobInfo.setJob_id(SnowflakeIdWorker.getInstance().nextId() + "");
        quartzJobInfo.setStatus("create");
        String end_expr = quartzJobInfo.getExpr().toLowerCase();
        if (end_expr.endsWith("s") || end_expr.endsWith("m")
                || end_expr.endsWith("h")) {
            //SimpleScheduleBuilder 表达式 必须指定一个次数,默认式
            if (quartzJobInfo.getPlan_count().equals("")) {
                quartzJobInfo.setPlan_count("-1");
            }
        }
        debugInfo(quartzJobInfo);
        quartzJobMapper.insert(quartzJobInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    /**
     * 批量删除调度任务
     * @param ids
     * @return
     */
    @RequestMapping("/dispatch_task_delete")
    @ResponseBody
    public String dispatch_task_delete(String[] ids) {
        QuartzJobInfo quartzJobInfo = new QuartzJobInfo();
        for (String id : ids) {
            quartzJobInfo.setJob_id(id);
            quartzJobMapper.deleteByPrimaryKey(quartzJobInfo);
        }

        JSONObject json = new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }

    /**
     * 更新调度任务
     * @param quartzJobInfo
     * @return
     */
    @RequestMapping("/dispatch_task_update")
    @ResponseBody
    public String dispatch_task_update(QuartzJobInfo quartzJobInfo) {

        debugInfo(quartzJobInfo);
        quartzJobInfo.setOwner(getUser().getId());
        QuartzJobInfo qji = quartzJobMapper.selectByPrimaryKey(quartzJobInfo);
        quartzJobMapper.updateByPrimaryKey(quartzJobInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    /**
     * 手动执行调度任务
     * @param quartzJobInfo
     * @return
     */
    @RequestMapping("/dispatch_task_execute")
    @ResponseBody
    public String dispatch_task_execute(QuartzJobInfo quartzJobInfo,String reset_count) {
        debugInfo(quartzJobInfo);
        try {
            QuartzJobInfo dti = quartzJobMapper.selectByPrimaryKey(quartzJobInfo.getJob_id());
            if(reset_count.equalsIgnoreCase("true")){
                dti.setCount(0);
                quartzJobMapper.updateByPrimaryKey(dti);
            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    JobCommon.chooseJobBean(dti,false);
                }
            }).start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    /**
     * 自动执行调度任务
     * @param quartzJobInfo
     * @return
     */
    @RequestMapping("/dispatch_task_execute_quartz")
    @ResponseBody
    public String dispatch_task_execute_quartz(QuartzJobInfo quartzJobInfo) {

        debugInfo(quartzJobInfo);

        // dispatchTaskService.update(dispatchTaskInfo);
        String url = "http://127.0.0.1:60001/api/v1/zdh";
        QuartzJobInfo dti = quartzJobMapper.selectByPrimaryKey(quartzJobInfo.getJob_id());

        //ZdhInfo zdhInfo = create_zhdInfo(quartzJobInfo);
        //重置次数
        dti.setCount(0);
        JSONObject json = new JSONObject();
        try {
            quartzManager2.addTaskToQuartz(dti);
            json.put("status", "200");
        } catch (Exception e) {
            e.printStackTrace();
            json.put("status", "-1");
            json.put("msg",e.getMessage());

        }


        return json.toJSONString();
    }


    /**
     * 暂停调度任务
     * @param quartzJobInfo
     * @return
     */
    @RequestMapping("/dispatch_task_quartz_pause")
    @ResponseBody
    public String dispatch_task_quartz_pause(QuartzJobInfo quartzJobInfo) {

        debugInfo(quartzJobInfo);
        QuartzJobInfo dti = quartzJobMapper.selectByPrimaryKey(quartzJobInfo.getJob_id());

        if (quartzJobInfo.getStatus().equals("running")) {
            //需要恢复暂停任务
            quartzManager2.resumeTask(dti);
            quartzJobMapper.updateStatus(quartzJobInfo.getJob_id(), quartzJobInfo.getStatus());
        } else {
            //暂停任务,//状态在pauseTask 方法中修改
            quartzManager2.pauseTask(dti);
        }


        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    /**
     * 删除调度任务,如果是单源 ETL的流任务需要做单独处理
     * @param quartzJobInfo
     * @return
     */
    @RequestMapping("/dispatch_task_quartz_del")
    @ResponseBody
    public String dispatch_task_quartz_del(QuartzJobInfo quartzJobInfo) {

        QuartzJobInfo qji = quartzJobMapper.selectByPrimaryKey(quartzJobInfo);
        quartzManager2.deleteTask(qji, "remove");
        JSONObject json = new JSONObject();

        json.put("success", "200");
        if (!qji.getMore_task().equals("单源ETL")) {
            return json.toJSONString();
        }
        EtlTaskInfo etlTaskInfo = etlTaskService.selectById(qji.getEtl_task_id());
        String dataSourcesType = etlTaskInfo.getData_source_type_input().toLowerCase();
        if (dataSourcesType.equals("kafka") || dataSourcesType.equals("flume")) {
            quartzJobMapper.updateStatus(qji.getJob_id(), "finish");
            String url = JobCommon.getZdhUrl(zdhHaInfoMapper).getZdh_url().replace("zdh", "del");
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("job_id", qji.getJob_id());
                jsonObject.put("del_type", dataSourcesType);
                HttpUtil.postJSON(url, jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return json.toJSONString();
    }


    /**
     * 调度任务日志首页
     * @return
     */
    @RequestMapping("/log_txt")
    public String log_txt() {

        return "etl/log_txt";
    }

    /**
     * 获取任务执行日志
     * @param job_id
     * @param start_time
     * @param end_time
     * @param del
     * @param level
     * @return
     */
    @RequestMapping(value = "/zhd_logs", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String zhd_logs(String job_id,String task_log_id, String start_time, String end_time, String del, String level) {
        System.out.println("id:" + job_id+" ,task_log_id:"+task_log_id + " ,start_time:" + start_time + " ,end_time:" + end_time);


        Timestamp ts_start = null;
        Timestamp ts_end = null;
        if (!start_time.equals("")) {
            ts_start = Timestamp.valueOf(start_time);
        } else {
            ts_start = Timestamp.valueOf("1970-01-01 00:00:00");
        }
        if (!end_time.equals("")) {
            ts_end = Timestamp.valueOf(end_time);
        } else {
            ts_end = Timestamp.valueOf("2999-01-01 00:00:00");
        }

        if (del != null && !del.equals("")) {
            zdhLogsService.deleteByTime(job_id, task_log_id,ts_start, ts_end);
        }

        String levels = "'DEBUG','WARN','INFO','ERROR'";

        if (level != null && level.equals("INFO")) {
            levels = "'WARN','INFO','ERROR'";
        }
        if (level != null && level.equals("WARN")) {
            levels = "'WARN','ERROR'";
        }
        if (level != null && level.equals("ERROR")) {
            levels = "'ERROR'";
        }

        List<ZdhLogs> zhdLogs = zdhLogsService.selectByTime(job_id,task_log_id, ts_start, ts_end, levels);
        Iterator<ZdhLogs> it = zhdLogs.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            ZdhLogs next = it.next();
            String info = "任务ID:" + next.getJob_id() + ",任务执行时间:" + next.getLog_time().toString() + ",日志[" + next.getLevel() + "]:" + next.getMsg();
            sb.append(info + "\r\n");
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("logs", sb.toString());
        return jsonObject.toJSONString();
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
