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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Controller
public class ZdhMonitorController extends BaseController{

    public Logger logger= LoggerFactory.getLogger(this.getClass());
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


    @RequestMapping(value = "/etlEcharts", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String get1() {
//        调度总任务数：
//        正在执行任务数：
//        已完成任务数
//        失败任务数

        //    int total_task_num = quartzJobMapper.selectCountByOwner(getUser().getId());

        List<EtlEcharts> echartsList = taskLogInstanceMapper.slectByOwner(getUser().getId());
        if(echartsList == null || echartsList.size()==0){
            echartsList = new ArrayList<>();
            EtlEcharts ee=new EtlEcharts();
            ee.setError("0");
            ee.setFinish("0");
            ee.setRunning("0");
            ee.setEtl_date(DateUtil.format(new Date()));
            echartsList.add(ee);
        }

        return JSON.toJSONString(echartsList);
    }

    @RequestMapping(value = "/etlEchartsCurrent", produces = "text/html;charset=UTF-8")
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
    @Transactional
    public String task_logs_delete(String[] ids) {

        try{
            System.out.println("开始删除任务日志");
            taskLogInstanceMapper.deleteByIds(ids);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }
    }


    @RequestMapping(value = "/task_group_logs_delete", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional
    public String task_group_logs_delete(String[] ids) {

        try{
            System.out.println("开始删除任务组日志");
            tglim.deleteByIds(ids);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }
    }

    /**
     * 杀死单个任务
     * @param id
     * @return
     */
    @RequestMapping(value = "/kill", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String killJob(String id){
        // check_dep,wait_retry 状态 直接killed
        // dispatch,etl 状态 kill
        try{
            taskLogInstanceMapper.updateStatusById2(id);
            TaskLogInstance tli=taskLogInstanceMapper.selectByPrimaryKey(id);
            JobCommon2.insertLog(tli,"INFO","接受到杀死请求,开始进行杀死操作...");
            if(tli.getStatus().equalsIgnoreCase("killed")){
                JobCommon2.insertLog(tli,"INFO","任务已杀死");
            }
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"杀死任务成功", null);
        }catch (Exception e){
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"杀死任务失败", e);
        }

    }

    /**
     * 杀死任务组
     * @param id
     * @return
     */
    @RequestMapping(value = "/killJobGroup", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String killJobGroup(String id){
        // check_dep,wait_retry,create,check_dep_finish 状态 直接killed
        // dispatch,etl 状态 kill
        try{
            tglim.updateStatusById2(id);
            taskLogInstanceMapper.updateStatusByGroupId(id);
            TaskGroupLogInstance tgli=tglim.selectByPrimaryKey(id);
            JobCommon2.insertLog(tgli,"INFO","接受到杀死请求,开始进行杀死操作...");
            if(tgli.getStatus().equalsIgnoreCase("killed")){
                JobCommon2.insertLog(tgli,"INFO","任务已杀死");
            }
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"杀死任务组成功", null);
        }catch (Exception e){
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"杀死任务组失败", e);
        }
    }

    @RequestMapping(value = "/retryJob", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String retryJob(String id,String new_version){
        //taskLogInstanceMapper.updateStatusById2("kill",id);
        try{
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
                tli.setAlarm_email(qji.getAlarm_email());
                tli.setAlarm_sms(qji.getAlarm_sms());
                tli.setAlarm_zdh(qji.getAlarm_zdh());
                tli.setNotice_error(qji.getNotice_error());
                tli.setNotice_finish(qji.getNotice_finish());
                tli.setNotice_timeout(qji.getNotice_timeout());
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
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"重试任务成功", null);
        }catch (Exception e){
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"重试任务失败", e);
        }

    }

    /**
     *
     * @param id
     * @param new_version
     * @param sub_tasks 重试的子任务,不可为空
     * @return
     */
    @RequestMapping(value = "/retryJobGroup", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional
    public String retryJobGroup(String id,String new_version,String[] sub_tasks){
        //taskLogInstanceMapper.updateStatusById2("kill",id);
        try{
            if(sub_tasks == null || sub_tasks.length<1){
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"重试子任务不可为空", "");
            }
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
                tgli.setAlarm_email(qji.getAlarm_email());
                tgli.setAlarm_sms(qji.getAlarm_sms());
                tgli.setAlarm_zdh(qji.getAlarm_zdh());
                tgli.setNotice_error(qji.getNotice_error());
                tgli.setNotice_finish(qji.getNotice_finish());
                tgli.setNotice_timeout(qji.getNotice_timeout());
            }
            tgli.setOwner(getUser().getId());
//        tgli.setStatus(JobStatus.NON.getValue());
//        tgli.setRun_time(new Timestamp(new Date().getTime()));
//        tgli.setUpdate_time(new Timestamp(new Date().getTime()));
//        tglim.insert(tgli);
//        JobCommon2.sub_task_log_instance(tgli);

            JobCommon2.chooseJobBean(qji,2,tgli,sub_tasks);

            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"重试任务组成功", null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"重试任务组失败", e);
        }
    }


    @RequestMapping(value = "/getScheduleTask", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public String getScheduleTask() {
        String owner = getUser().getId();
        try {
            return JSON.toJSONString(quartzManager2.getScheduleTask(owner));
        } catch (SchedulerException e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.error(error, e.getCause());
            return JSON.toJSONString(new JSONObject());
        }
    }


    @RequestMapping(value = "/getSparkMonitor", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getSparkMonitor(String executor) {
        ZdhHaInfo zdhHaInfo=zdhHaInfoMapper.selectByPrimaryKey(executor);

        return JSON.toJSONString(zdhHaInfo);
    }

    @RequestMapping(value = "/getTotalNum", produces = "text/html;charset=UTF-8")
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


    @RequestMapping("/task_group_log_instance_index")
    public String task_group_log_instance_index() {
        return "etl/task_group_log_instance_index";
    }

    @RequestMapping("/task_log_instance_index")
    public String task_log_instance_index() {
        return "etl/task_log_instance_index";
    }

    @RequestMapping("/task_group_retry_detail_index")
    public String task_group_retry_detail_index() {
        return "etl/task_group_retry_detail_index";
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
    @RequestMapping(value = "/zdh_logs", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String zdh_logs(String job_id, String task_log_id, String start_time, String end_time, String del, String level) {
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
            zdhLogsService.deleteByTime(null, task_log_id, ts_start, ts_end);
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


    @RequestMapping(value = "/download_log", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void download_log(HttpServletResponse response, String job_id, String task_log_id){
        String levels = "'DEBUG','WARN','INFO','ERROR'";
        File path = null;
        response.setHeader("content-type", "text/html;charset=UTF-8");

        response.setContentType("text/html;charset=UTF-8");
        OutputStream os = null;
        ByteArrayInputStream bis = null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode("日志"+task_log_id+".log", "UTF-8"));
            byte[] buff = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

            List<ZdhLogs> zhdLogs = zdhLogsService.selectByTime(job_id, task_log_id, null, null, levels);
            Iterator<ZdhLogs> it = zhdLogs.iterator();
            StringBuilder sb = new StringBuilder();
            while (it.hasNext()) {
                ZdhLogs next = it.next();
                String info = "调度任务ID:" + next.getJob_id()+",任务实例ID:"+task_log_id + ",任务执行时间:" + next.getLog_time().toString() + ",日志[" + next.getLevel() + "]:" + next.getMsg();
                sb.append(info + "\r\n");
            }
            os = response.getOutputStream();
            bis = new ByteArrayInputStream(sb.toString().getBytes());
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }



        } catch (UnsupportedEncodingException e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.error(error, e.getCause());
        } catch (IOException e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName();
            logger.error(error, e.getCause());
        }finally {
            try {
                bis.close();
                os.close();
            } catch (IOException e) {
                String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName();
                logger.error(error, e.getCause());
            }
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
                    String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName();
                    logger.error(error, e.getCause());
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName(), e.getCause());
            }
        }
    }

}
