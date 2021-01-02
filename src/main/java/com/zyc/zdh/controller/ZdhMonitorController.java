package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.dao.TaskGroupLogInstanceMapper;
import com.zyc.zdh.dao.TaskLogInstanceMapper;
import com.zyc.zdh.dao.ZdhHaInfoMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.JobCommon2;
import com.zyc.zdh.job.JobStatus;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.ZdhLogsService;
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
import java.util.Iterator;
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
    ZdhLogsService zdhLogsService;
    @Autowired
    TaskLogInstanceMapper taskLogInstanceMapper;

    @Autowired
    TaskGroupLogInstanceMapper tglim;


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

        List<EtlEcharts> echartsList = taskLogInstanceMapper.slectByOwner(getUser().getId());

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
        List<EtlEcharts> echartsList = taskLogInstanceMapper.slectByOwnerEtlDate(getUser().getId(), etl_date);

        return echartsList;
    }

    @RequestMapping(value = "/task_logs", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String task_logs(String start_time, String end_time, String status) {

        System.out.println("开始加载任务日志start_time:" + start_time + ",end_time:" + end_time + ",status:" + status);

        List<TaskLogInstance> list = taskLogInstanceMapper.selectByTaskLogs(getUser().getId(), Timestamp.valueOf(start_time + " 00:00:00"),
                Timestamp.valueOf(end_time + " 23:59:59"), status);

        return JSON.toJSONString(list);
    }

    @RequestMapping(value = "/task_logs_delete", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String task_logs_delete(String[] ids) {

        System.out.println("开始删除任务日志");
        taskLogInstanceMapper.deleteByIds(ids);
        JSONObject json = new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping(value = "/task_group_logs_delete", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String task_group_logs_delete(String[] ids) {

        System.out.println("开始删除任务组日志");
        tglim.deleteByIds(ids);
        JSONObject json = new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }

    /**
     * 杀死单个任务
     * @param id
     * @return
     */
    @RequestMapping("/kill")
    @ResponseBody
    public String killJob(String id){
        // check_dep,wait_retry 状态 直接killed
        // dispatch,etl 状态 kill
        taskLogInstanceMapper.updateStatusById2(id);
        TaskLogInstance tli=taskLogInstanceMapper.selectByPrimaryKey(id);
        JobCommon2.insertLog(tli,"INFO","接受到杀死请求,开始进行杀死操作...");
        if(tli.getStatus().equalsIgnoreCase("killed")){
            JobCommon2.insertLog(tli,"INFO","任务已杀死");
        }
        JSONObject json2 = new JSONObject();
        json2.put("success", "200");
        return json2.toJSONString();

    }

    /**
     * 杀死任务组
     * @param id
     * @return
     */
    @RequestMapping("/killJobGroup")
    @ResponseBody
    public String killJobGroup(String id){
        // check_dep,wait_retry,create,check_dep_finish 状态 直接killed
        // dispatch,etl 状态 kill
        tglim.updateStatusById2(id);
        taskLogInstanceMapper.updateStatusByGroupId(id);
        TaskGroupLogInstance tgli=tglim.selectByPrimaryKey(id);
        JobCommon2.insertLog(tgli,"INFO","接受到杀死请求,开始进行杀死操作...");
        if(tgli.getStatus().equalsIgnoreCase("killed")){
            JobCommon2.insertLog(tgli,"INFO","任务已杀死");
        }
        JSONObject json2 = new JSONObject();
        json2.put("success", "200");
        return json2.toJSONString();

    }

    @RequestMapping("/retryJob")
    @ResponseBody
    public String retryJob(String id,String new_version){
        //taskLogInstanceMapper.updateStatusById2("kill",id);
        TaskLogInstance tli=taskLogInstanceMapper.selectByPrimaryKey(id);
        tli.setIs_retryed("1");
        taskLogInstanceMapper.updateByPrimaryKey(tli);
        QuartzJobInfo qji=quartzJobMapper.selectByPrimaryKey(tli.getJob_id());

        //重试最新版-拉去quartJobInfo 中的shell 及参数
        if(new_version.equalsIgnoreCase("true")){
            tli.setIs_script(qji.getIs_script());
            tli.setJob_ids(qji.getJob_ids());
            tli.setJump_script(qji.getJump_script());
            tli.setJump_dep(qji.getJump_dep());
            tli.setInterval_time(qji.getInterval_time());
            tli.setEmail_and_sms(qji.getEmail_and_sms());
            tli.setAlarm_account(qji.getAlarm_account());
            tli.setAlarm_enabled(qji.getAlarm_enabled());
            tli.setCommand(qji.getCommand());
            tli.setParams(qji.getParams());
            tli.setTime_out(qji.getTime_out());
        }

        String new_id=SnowflakeIdWorker.getInstance().nextId()+"";
        tli.setIs_retryed("0");
        tli.setId(new_id);
        tli.setCount(0);
        tli.setProcess("1");
        tli.setRun_time(new Timestamp(new Date().getTime()));
        tli.setUpdate_time(new Timestamp(new Date().getTime()));
        tli.setStatus(JobStatus.CREATE.getValue());
        taskLogInstanceMapper.insert(tli);
        //JobCommon2.chooseJobBean(tli);
        JSONObject json2 = new JSONObject();
        json2.put("success", "200");
        return json2.toJSONString();

    }

    @RequestMapping("/retryJobGroup")
    @ResponseBody
    public String retryJobGroup(String id,String new_version,String[] sub_tasks){
        //taskLogInstanceMapper.updateStatusById2("kill",id);
        TaskGroupLogInstance tgli=tglim.selectByPrimaryKey(id);
        tgli.setIs_retryed("1");
        tglim.updateByPrimaryKey(tgli);
        QuartzJobInfo qji=quartzJobMapper.selectByPrimaryKey(tgli.getJob_id());

        //重试最新版-拉去quartJobInfo 中的shell 及参数
        if(new_version.equalsIgnoreCase("true")){
            tgli.setIs_script(qji.getIs_script());
            tgli.setJob_ids(qji.getJob_ids());
            tgli.setJump_script(qji.getJump_script());
            tgli.setJump_dep(qji.getJump_dep());
            tgli.setInterval_time(qji.getInterval_time());
            tgli.setEmail_and_sms(qji.getEmail_and_sms());
            tgli.setAlarm_account(qji.getAlarm_account());
            tgli.setAlarm_enabled(qji.getAlarm_enabled());
            tgli.setCommand(qji.getCommand());
            tgli.setParams(qji.getParams());
            tgli.setTime_out(qji.getTime_out());
            tgli.setOwner(getUser().getId());
        }
        tgli.setOwner(getUser().getId());
//        tgli.setStatus(JobStatus.NON.getValue());
//        tgli.setRun_time(new Timestamp(new Date().getTime()));
//        tgli.setUpdate_time(new Timestamp(new Date().getTime()));
//        tglim.insert(tgli);
//        JobCommon2.sub_task_log_instance(tgli);

        JobCommon2.chooseJobBean(qji,2,tgli,sub_tasks);
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

    @RequestMapping("/getTotalNum")
    @ResponseBody
    public String getTotalNum(){

       int allTaskNum=taskLogInstanceMapper.allTaskNum();
       int allDispatchNum=taskLogInstanceMapper.allDispatchNum();
       int allDispatchRunNum=taskLogInstanceMapper.allDispatchRunNum();
       int successNum=taskLogInstanceMapper.successNum();
       int errorNum=taskLogInstanceMapper.errorNum();
       int alarmNum=taskLogInstanceMapper.alarmNum();

       JSONObject js=new JSONObject();
       js.put("allTaskNum",allTaskNum);
       js.put("allDispatchNum",allDispatchNum);
       js.put("allDispatchRunNum",allDispatchRunNum);
       js.put("successNum",successNum);
       js.put("errorNum",errorNum);
       js.put("alarmNum",alarmNum);

       return js.toJSONString();

    }


    @RequestMapping(value = "/task_log_instance_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String task_log_instance_list(String status,String group_id) {

        List<TaskLogInstance> list = taskLogInstanceMapper.selectByTaskLogs2(getUser().getId(), null,
                null, status,group_id);

        return JSON.toJSONString(list);
    }

    @RequestMapping(value = "/task_group_log_instance_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String task_group_log_instance_list(String start_time, String end_time, String status,String job_id) {

        System.out.println("开始加载任务日志start_time:" + start_time + ",end_time:" + end_time + ",status:" + status);

        List<TaskGroupLogInstance> list = tglim.selectByTaskLogs2(getUser().getId(), Timestamp.valueOf(start_time + " 00:00:00"),
                Timestamp.valueOf(end_time + " 23:59:59"), status,job_id);

        return JSON.toJSONString(list);
    }

    @RequestMapping(value = "/task_group_log_instance_list2", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String task_group_log_instance_list2(String start_time, String end_time, String status) {

        System.out.println("开始加载任务日志start_time:" + start_time + ",end_time:" + end_time + ",status:" + status);

        List<TaskGroupLogInstance> list = tglim.selectByTaskLogs3(getUser().getId(), Timestamp.valueOf(start_time + " 00:00:00"),
                Timestamp.valueOf(end_time + " 23:59:59"), status);

        return JSON.toJSONString(list);
    }

    @RequestMapping(value = "/task_group_log_instance_list3", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String task_group_log_instance_list3(String[] ids) {
        List<TaskGroupLogInstance> list = tglim.selectByIds(ids,null);
        return JSON.toJSONString(list);
    }

    /**
     * 获取任务执行日志
     *
     * @param job_id
     * @param start_time
     * @param end_time
     * @param del
     * @param level
     * @return
     */
    @RequestMapping(value = "/zhd_logs", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String zhd_logs(String job_id, String task_log_id, String start_time, String end_time, String del, String level) {
        System.out.println("id:" + job_id + " ,task_log_id:" + task_log_id + " ,start_time:" + start_time + " ,end_time:" + end_time);


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
            zdhLogsService.deleteByTime(job_id, task_log_id, ts_start, ts_end);
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

        List<ZdhLogs> zhdLogs = zdhLogsService.selectByTime(job_id, task_log_id, ts_start, ts_end, levels);
        Iterator<ZdhLogs> it = zhdLogs.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            ZdhLogs next = it.next();
            String info = "调度任务ID:" + next.getJob_id()+",任务实例ID:"+task_log_id + ",任务执行时间:" + next.getLog_time().toString() + ",日志[" + next.getLevel() + "]:" + next.getMsg();
            sb.append(info + "\r\n");
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("logs", sb.toString());
        return jsonObject.toJSONString();
    }

    /**
     * 调度任务日志首页
     *
     * @return
     */
    @RequestMapping("/log_txt")
    public String log_txt() {

        return "etl/log_txt";
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
