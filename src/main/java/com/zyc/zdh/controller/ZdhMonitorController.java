package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.dao.TaskGroupLogInstanceMapper;
import com.zyc.zdh.dao.TaskLogInstanceMapper;
import com.zyc.zdh.dao.ZdhHaInfoMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.JobCommon2;
import com.zyc.zdh.job.JobStatus;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.monitor.Server;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.SpringContext;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.swing.StringUIClientPropertyKey;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

/**
 * 监控服务
 */
@Controller
public class ZdhMonitorController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());
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

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 监控首页
     * @return
     */
    @RequestMapping("/monitor")
    public String index_v1() {
        return "etl/monitor";
    }


    /**
     * 调度任务监控
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/etlEcharts", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String get1() throws Exception {
//        调度总任务数：
//        正在执行任务数：
//        已完成任务数
//        失败任务数

        List<EtlEcharts> echartsList = taskLogInstanceMapper.slectByOwner(getOwner());
        if (echartsList == null || echartsList.size() == 0) {
            echartsList = new ArrayList<>();
            EtlEcharts ee = new EtlEcharts();
            ee.setError("0");
            ee.setFinish("0");
            ee.setRunning("0");
            ee.setEtl_date(DateUtil.format(new Date()));
            echartsList.add(ee);
        }

        return JSON.toJSONString(echartsList);
    }

    @RequestMapping(value = "/etlEchartsCurrent", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public List<EtlEcharts> get2() throws Exception {
//        调度总任务数：
//        正在执行任务数：
//        已完成任务数
//        失败任务数

        int total_task_num = quartzJobMapper.selectCountByOwner(getOwner());

        String etl_date = DateUtil.format(new Date()) + " 00:00:00";
        List<EtlEcharts> echartsList = taskLogInstanceMapper.slectByOwnerEtlDate(getOwner(), etl_date);

        return echartsList;
    }

    @Deprecated
    @RequestMapping(value = "/task_logs", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String task_logs(String start_time, String end_time, String status) throws Exception {

        //System.out.println("开始加载任务日志start_time:" + start_time + ",end_time:" + end_time + ",status:" + status);

        List<TaskLogInstance> list = taskLogInstanceMapper.selectByTaskLogs(getOwner(), Timestamp.valueOf(start_time + " 00:00:00"),
                Timestamp.valueOf(end_time + " 23:59:59"), status);

        return JSON.toJSONString(list);
    }

    /**
     * 任务实例删除
     * @param ids id数组
     * @return
     */
    @RequestMapping(value = "/task_logs_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo task_logs_delete(String[] ids) {

        try {
            System.out.println("开始删除任务日志");
            taskLogInstanceMapper.deleteByIds(ids);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }


    /**
     * 组任务删除
     * @param ids id数组
     * @return
     */
    @RequestMapping(value = "/task_group_logs_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo task_group_logs_delete(String[] ids) {

        try {
            System.out.println("开始删除任务组日志");
            tglim.deleteByIds(ids);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }

    /**
     * 杀死单个任务
     *
     * @param id 任务实例ID
     * @return
     */
    @RequestMapping(value = "/kill", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo killJob(String id) {
        // check_dep,wait_retry 状态 直接killed
        // dispatch,etl 状态 kill
        try {
            taskLogInstanceMapper.updateStatusById2(id);
            TaskLogInstance tli = taskLogInstanceMapper.selectByPrimaryKey(id);
            JobCommon2.insertLog(tli, "INFO", "接受到杀死请求,开始进行杀死操作...");
            if (tli.getStatus().equalsIgnoreCase("killed")) {
                JobCommon2.insertLog(tli, "INFO", "任务已杀死");
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "杀死任务成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "杀死任务失败", e);
        }

    }

    /**
     * 手动跳过任务
     * @param id 任务实例ID
     * @return
     */
    @RequestMapping(value = "/skip", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo skipJob(String id) {
        try {
            taskLogInstanceMapper.updateSkipById(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "手动跳过任务成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "手动跳过任务失败", e);
        }

    }

    /**
     * 杀死任务组
     *
     * @param id 任务组实例ID
     * @return
     */
    @RequestMapping(value = "/killJobGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo killJobGroup(String id) {
        // check_dep,wait_retry,create,check_dep_finish 状态 直接killed
        // dispatch,etl 状态 kill
        try {
            tglim.updateStatusById2(id);
            taskLogInstanceMapper.updateStatusByGroupId(id);
            TaskGroupLogInstance tgli = tglim.selectByPrimaryKey(id);
            JobCommon2.insertLog(tgli, "INFO", "接受到杀死请求,开始进行杀死操作...");
            if (tgli.getStatus().equalsIgnoreCase("killed")) {
                JobCommon2.insertLog(tgli, "INFO", "任务已杀死");
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "杀死任务组成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "杀死任务组失败", e);
        }
    }

    /**
     * 重试任务实例(废弃)
     * @param id 任务实例ID
     * @param new_version 是否最新版
     * @return
     */
    @RequestMapping(value = "/retryJob", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo retryJob(String id, String new_version) {
        //taskLogInstanceMapper.updateStatusById2("kill",id);
        try {
            TaskLogInstance tli = taskLogInstanceMapper.selectByPrimaryKey(id);
            tli.setIs_retryed("1");
            taskLogInstanceMapper.updateByPrimaryKey(tli);
            QuartzJobInfo qji = quartzJobMapper.selectByPrimaryKey(tli.getJob_id());

            //重试最新版-拉去quartJobInfo 中的shell 及参数
            if (new_version.equalsIgnoreCase("true")) {
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

            String new_id = SnowflakeIdWorker.getInstance().nextId() + "";
            tli.setIs_retryed("0");
            tli.setId(new_id);
            tli.setCount(0);
            tli.setProcess("1");
            tli.setRun_time(new Timestamp(new Date().getTime()));
            tli.setUpdate_time(new Timestamp(new Date().getTime()));
            tli.setStatus(JobStatus.CREATE.getValue());
            taskLogInstanceMapper.insert(tli);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "重试任务成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "重试任务失败", e);
        }

    }

    /**
     * 重试任务组
     * @param id 任务组实例ID
     * @param new_version 是否最新版 true/false
     * @param sub_tasks   重试的子任务,不可为空
     * @return
     */
    @RequestMapping(value = "/retryJobGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo retryJobGroup(String id, String new_version, String[] sub_tasks) {
        //taskLogInstanceMapper.updateStatusById2("kill",id);
        try {
            if (sub_tasks == null || sub_tasks.length < 1) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "重试子任务不可为空", "");
            }
            TaskGroupLogInstance tgli = tglim.selectByPrimaryKey(id);
            tgli.setIs_retryed("1");
            tglim.updateByPrimaryKey(tgli);
            QuartzJobInfo qji = quartzJobMapper.selectByPrimaryKey(tgli.getJob_id());

            //重试最新版-拉去quartJobInfo 中的shell 及参数
            if (new_version.equalsIgnoreCase("true")) {
                tgli.setIs_script(qji.getIs_script());
                tgli.setJob_ids(qji.getJob_ids());
                tgli.setJsmind_data(qji.getJsmind_data());
                tgli.setJump_script(qji.getJump_script());
                tgli.setJump_dep(qji.getJump_dep());
                tgli.setInterval_time(qji.getInterval_time());
                tgli.setEmail_and_sms(qji.getEmail_and_sms());
                tgli.setAlarm_account(qji.getAlarm_account());
                tgli.setAlarm_enabled(qji.getAlarm_enabled());
                tgli.setCommand(qji.getCommand());
                tgli.setParams(qji.getParams());
                tgli.setTime_out(qji.getTime_out());
                tgli.setOwner(getOwner());
                tgli.setAlarm_email(qji.getAlarm_email());
                tgli.setAlarm_sms(qji.getAlarm_sms());
                tgli.setAlarm_zdh(qji.getAlarm_zdh());
                tgli.setNotice_error(qji.getNotice_error());
                tgli.setNotice_finish(qji.getNotice_finish());
                tgli.setNotice_timeout(qji.getNotice_timeout());
            }
            tgli.setOwner(getOwner());
//        tgli.setStatus(JobStatus.NON.getValue());
//        tgli.setRun_time(new Timestamp(new Date().getTime()));
//        tgli.setUpdate_time(new Timestamp(new Date().getTime()));
//        tglim.insert(tgli);
//        JobCommon2.sub_task_log_instance(tgli);

            JobCommon2.chooseJobBean(qji, 2, tgli, sub_tasks);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "重试任务组成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "重试任务组失败", e);
        }
    }


    /**
     * 获取正在执行中调度任务
     * @return
     */
    @RequestMapping(value = "/getScheduleTask", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String getScheduleTask() {
        try {
            String owner = getOwner();
            return JSON.toJSONString(quartzManager2.getScheduleTask(owner));
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return JSON.toJSONString(new JSONObject());
        }
    }


    /**
     * 获取spark历史服务器地址
     * @param executor
     * @return
     */
    @RequestMapping(value = "/getSparkMonitor", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getSparkMonitor(String executor) {
        ZdhHaInfo zdhHaInfo = zdhHaInfoMapper.selectByPrimaryKey(executor);

        return JSON.toJSONString(zdhHaInfo);
    }

    /**
     * 获取任务总览
     * @return
     */
    @RequestMapping(value = "/getTotalNum", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getTotalNum() {

        int allTaskNum = taskLogInstanceMapper.allTaskNum();
        int allDispatchNum = taskLogInstanceMapper.allDispatchNum();
        int allDispatchRunNum = taskLogInstanceMapper.allDispatchRunNum();
        int successNum = taskLogInstanceMapper.successNum();
        int errorNum = taskLogInstanceMapper.errorNum();
        int alarmNum = taskLogInstanceMapper.alarmNum();

        JSONObject js = new JSONObject();
        js.put("allTaskNum", allTaskNum);
        js.put("allDispatchNum", allDispatchNum);
        js.put("allDispatchRunNum", allDispatchRunNum);
        js.put("successNum", successNum);
        js.put("errorNum", errorNum);
        js.put("alarmNum", alarmNum);

        return js.toJSONString();

    }


    /**
     * 获取任务组实例首页
     * @return
     */
    @RequestMapping("/task_group_log_instance_index")
    public String task_group_log_instance_index() {
        return "etl/task_group_log_instance_index";
    }

    /**
     * 获取任务实例首页
     * @return
     */
    @RequestMapping("/task_log_instance_index")
    public String task_log_instance_index() {
        return "etl/task_log_instance_index";
    }

    /**
     * 任务组重试页面
     * @return
     */
    @RequestMapping("/task_group_retry_detail_index")
    public String task_group_retry_detail_index() {
        return "etl/task_group_retry_detail_index";
    }


    /**
     * 任务实例列表
     * @param status
     * @param group_id 调度任务ID
     * @return
     */
    @RequestMapping(value = "/task_log_instance_list", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String task_log_instance_list(String status, String group_id) {

        try{
            List<TaskLogInstance> list = taskLogInstanceMapper.selectByTaskLogs2(getOwner(), null,
                    null, status, group_id);

            return JSON.toJSONString(list);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return JSON.toJSONString(e.getMessage());
        }

    }

    /**
     * 任务组实例列表
     * @param start_time
     * @param end_time
     * @param status
     * @param job_id 必填,调度任务ID
     * @return
     */
    @RequestMapping(value = "/task_group_log_instance_list",method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String task_group_log_instance_list(String start_time, String end_time, String status, String job_id) {

        try{
            //System.out.println("开始加载任务日志start_time:" + start_time + ",end_time:" + end_time + ",status:" + status);

            List<TaskGroupLogInstance> list = tglim.selectByTaskLogs2(getOwner(), Timestamp.valueOf(start_time + " 00:00:00"),
                    Timestamp.valueOf(end_time + " 23:59:59"), status, job_id);

            return JSON.toJSONString(list);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return JSON.toJSONString(e.getMessage());
        }

    }

//    /**
//     * 任务组实例列表
//     * @param start_time
//     * @param end_time
//     * @param status
//     * @return
//     */
//    @RequestMapping(value = "/task_group_log_instance_list2", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
//    @ResponseBody
//    public String task_group_log_instance_list2(String start_time, String end_time, String status) {
//
//        try{
//            System.out.println("开始加载任务日志start_time:" + start_time + ",end_time:" + end_time + ",status:" + status);
//
//            List<TaskGroupLogInstance> list = tglim.selectByTaskLogs3(getOwner(), Timestamp.valueOf(start_time + " 00:00:00"),
//                    Timestamp.valueOf(end_time + " 23:59:59"), status);
//
//            return JSON.toJSONString(list);
//        }catch (Exception e){
//            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
//            logger.error(error, e);
//            return JSON.toJSONString(e.getMessage());
//        }
//    }

    /**
     * 任务组实例列表-分页
     * @param start_time
     * @param end_time
     * @param status
     * @return
     */
    @RequestMapping(value = "/task_group_log_instance_list2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<TaskGroupLogInstance>>> task_group_log_instance_list2(String start_time, String end_time,
                                                                                            String status, int limit, int offset) {
        try{
            if(StringUtils.isEmpty(start_time)){
                start_time = DateUtil.format(new Date());
                end_time = DateUtil.format(new Date());
            }
            if(status == null){
                status="";
            }
            //System.out.println("开始加载任务日志start_time:" + start_time + ",end_time:" + end_time + ",status:" + status+",limit:"+limit+",offset:"+offset);
            List<TaskGroupLogInstance> list = tglim.selectByTaskLogs4(getOwner(), Timestamp.valueOf(start_time + " 00:00:00"),
                    Timestamp.valueOf(end_time + " 23:59:59"), status, limit, offset);
            int total = tglim.selectCountByTaskLogs4(getOwner(), Timestamp.valueOf(start_time + " 00:00:00"),
                    Timestamp.valueOf(end_time + " 23:59:59"), status);
            PageResult<List<TaskGroupLogInstance>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(list);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pageResult);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 任务组实例列表
     * @param ids id数组
     * @return
     */
    @RequestMapping(value = "/task_group_log_instance_list3", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String task_group_log_instance_list3(String[] ids) {
        List<TaskGroupLogInstance> list = tglim.selectByIds(ids, null);
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
    @RequestMapping(value = "/zdh_logs", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String zdh_logs(String job_id, String task_log_id, String start_time, String end_time, String del, String level,String log_type) {
        //System.out.println("id:" + job_id + " ,task_log_id:" + task_log_id + " ,start_time:" + start_time + " ,end_time:" + end_time);

        if(!StringUtils.isEmpty(log_type) && log_type.equalsIgnoreCase("mock")){
            return mockLog(task_log_id);
        }
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
        String levels2 = "DEBUG,WARN,INFO,ERROR";
        if (level != null && level.equals("INFO")) {
            levels = "'WARN','INFO','ERROR'";
            levels2 = "WARN,INFO,ERROR";
        }
        if (level != null && level.equals("WARN")) {
            levels = "'WARN','ERROR'";
            levels2 = "WARN,ERROR";
        }
        if (level != null && level.equals("ERROR")) {
            levels = "'ERROR'";
            levels2 = "ERROR";
        }

        StringBuilder sb = new StringBuilder();
        Object logType=redisUtil.get("zdh_log_type");
        if(logType==null || logType.toString().equalsIgnoreCase(Const.LOG_MYSQL)){
            List<ZdhLogs> zhdLogs = zdhLogsService.selectByTime(job_id, task_log_id, ts_start, ts_end, levels);
            Iterator<ZdhLogs> it = zhdLogs.iterator();
            while (it.hasNext()) {
                ZdhLogs next = it.next();
                String info = "调度任务ID:" + next.getJob_id() + ",任务实例ID:" + task_log_id + ",任务执行时间:" + next.getLog_time().toString() + ",日志[" + next.getLevel() + "]:" + next.getMsg();
                sb.append(info + "\r\n");
            }
        }

        if(logType!=null && logType.toString().equalsIgnoreCase(Const.LOG_MONGODB)){
            MongoTemplate mongoTemplate = (MongoTemplate) SpringContext.getBean("mongoTemplate");
            Query query = new Query(Criteria.where("task_logs_id").is(task_log_id));
            query.addCriteria(Criteria.where("level").in(Arrays.asList(levels2.split(","))));
            query.addCriteria(Criteria.where("log_time").gte(ts_start).andOperator(Criteria.where("log_time").lte(ts_end)));
            List<Map> result= mongoTemplate.find(query, Map.class, "zdhLogs");
            for (Map m:result){
                String info = "调度任务ID:" + m.get("job_id") + ",任务实例ID:" + task_log_id + ",任务执行时间:" + m.get("log_time").toString() + ",日志[" + m.get("level") + "]:" + m.get("msg");
                sb.append(info + "\r\n");
            }
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("logs", sb.toString());
        return jsonObject.toJSONString();
    }

    private String mockLog(String job_id){
        StringBuilder sb = new StringBuilder();
        List<ZdhLogs> zhdLogs = zdhLogsService.selectByTime(job_id);
        Iterator<ZdhLogs> it = zhdLogs.iterator();
        while (it.hasNext()) {
            ZdhLogs next = it.next();
            String info = "MOCK ID:" + next.getJob_id() + ",请求ID:" + next.getTask_logs_id() + ",任务执行时间:" + next.getLog_time().toString() + ",日志[" + next.getLevel() + "]:" + next.getMsg();
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


    /**
     * 下载日志
     * @param response
     * @param job_id
     * @param task_log_id 日志
     */
    @RequestMapping(value = "/download_log", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void download_log(HttpServletResponse response, String job_id, String task_log_id) {
        String levels = "'DEBUG','WARN','INFO','ERROR'";
        File path = null;
        response.setHeader("content-type", "text/html;charset=UTF-8");

        response.setContentType("text/html;charset=UTF-8");
        OutputStream os = null;
        ByteArrayInputStream bis = null;
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode("日志" + task_log_id + ".log", "UTF-8"));
            byte[] buff = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

            List<ZdhLogs> zhdLogs = zdhLogsService.selectByTime(job_id, task_log_id, null, null, levels);
            Iterator<ZdhLogs> it = zhdLogs.iterator();
            StringBuilder sb = new StringBuilder();
            while (it.hasNext()) {
                ZdhLogs next = it.next();
                String info = "调度任务ID:" + next.getJob_id() + ",任务实例ID:" + task_log_id + ",任务执行时间:" + next.getLog_time().toString() + ",日志[" + next.getLevel() + "]:" + next.getMsg();
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
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
        } catch (IOException e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
        } finally {
            try {
                bis.close();
                os.close();
            } catch (IOException e) {
                String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
                logger.error(error, e);
            }
        }


    }

    /**
     * 系统监控
     * @return
     * @throws Exception
     */
    @White()
    @RequestMapping(value = "/system_monitor", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo getInfo() throws Exception {
        Server server = new Server();
        server.copyTo();

        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "成功", server);
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
                    logger.error(error, e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            }
        }
    }

}
