package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.dao.TaskLogsMapper;
import com.zyc.zdh.dao.ZdhHaInfoMapper;
import com.zyc.zdh.entity.EtlEcharts;
import com.zyc.zdh.entity.QuartzJobInfo;
import com.zyc.zdh.entity.TaskLogs;
import com.zyc.zdh.entity.ZdhHaInfo;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.util.DateUtil;
import org.quartz.SchedulerException;
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
public class ZdhMonitorController extends BaseController{

    @Autowired
    QuartzJobMapper quartzJobMapper;
    @Autowired
    QuartzManager2 quartzManager2;
    @Autowired
    ZdhHaInfoMapper zdhHaInfoMapper;
    @Autowired
    TaskLogsMapper taskLogsMapper;


    @RequestMapping("/monitor")
    public String index_v1() {
        return "etl/monitor";
    }


    @RequestMapping("/etlEcharts")
    @ResponseBody
    public List<EtlEcharts> get1() {
//        调度总任务数：
//        正在执行任务数：
//        已完成任务数
//        失败任务数

        //    int total_task_num = quartzJobMapper.selectCountByOwner(getUser().getId());

        List<EtlEcharts> echartsList = taskLogsMapper.slectByOwner(getUser().getId());

        return echartsList;
    }

    @RequestMapping("/etlEchartsCurrent")
    @ResponseBody
    public List<EtlEcharts> get2() {
//        调度总任务数：
//        正在执行任务数：
//        已完成任务数
//        失败任务数

        int total_task_num = quartzJobMapper.selectCountByOwner(getUser().getId());

        String etl_date = DateUtil.format(new Date()) + " 00:00:00";
        List<EtlEcharts> echartsList = taskLogsMapper.slectByOwnerEtlDate(getUser().getId(), etl_date);

        return echartsList;
    }

    @RequestMapping(value = "/task_logs", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String task_logs(String start_time, String end_time, String status) {

        System.out.println("开始加载任务日志start_time:" + start_time + ",end_time:" + end_time + ",status:" + status);

        List<TaskLogs> list = taskLogsMapper.selectByTaskLogs(getUser().getId(), Timestamp.valueOf(start_time + " 00:00:00"),
                Timestamp.valueOf(end_time + " 23:59:59"), status);

        return JSON.toJSONString(list);
    }

    @RequestMapping(value = "/task_logs_delete", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String task_logs_delete(String[] ids) {

        System.out.println("开始删除任务日志");
        TaskLogs taskLogs = new TaskLogs();
        for (String id : ids) {
            taskLogs.setId(id);
            taskLogsMapper.deleteByPrimaryKey(taskLogs);
        }
        JSONObject json = new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping("/kill")
    @ResponseBody
    public String killJob(String id){
        taskLogsMapper.updateStatusById2("kill",id);
        JSONObject json2 = new JSONObject();
        json2.put("success", "200");
        return json2.toJSONString();

    }

    @RequestMapping("/getScheduleTask")
    @ResponseBody
    public List<QuartzJobInfo> getScheduleTask() {
        String owner = getUser().getId();
        try {
            return quartzManager2.getScheduleTask(owner);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new ArrayList<QuartzJobInfo>();
        }
    }


    @RequestMapping("/getSparkMonitor")
    @ResponseBody
    public String getSparkMonitor(String executor) {
        ZdhHaInfo zdhHaInfo=zdhHaInfoMapper.selectByPrimaryKey(executor);

        return JSON.toJSONString(zdhHaInfo);
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
