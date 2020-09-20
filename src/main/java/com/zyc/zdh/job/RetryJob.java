package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.dao.TaskLogsMapper;
import com.zyc.zdh.dao.ZdhDownloadMapper;
import com.zyc.zdh.dao.ZdhHaInfoMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.service.JemailService;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.HttpUtil;
import com.zyc.zdh.util.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

//定期拉取重试任务
public class RetryJob {

    private final static String task_log_status="etl";
    private static Logger logger = LoggerFactory.getLogger(RetryJob.class);

    public static List<ZdhDownloadInfo> zdhDownloadInfos = new ArrayList<>();

    public static void run(QuartzJobInfo quartzJobInfo) {
        try {
            logger.debug("开始检测重试任务...");
            QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
            TaskLogsMapper taskLogsMapper=(TaskLogsMapper) SpringContext.getBean("taskLogsMapper");
            ZdhHaInfoMapper zdhHaInfoMapper=(ZdhHaInfoMapper) SpringContext.getBean("zdhHaInfoMapper");
            //获取重试的任务
            List<TaskLogs> taskLogsList=taskLogsMapper.selectThreadByStatus2("wait_retry");
            for(TaskLogs tl :taskLogsList){
                QuartzJobInfo qj= quartzJobMapper.selectByPrimaryKey(tl.getJob_id());
                logger.info("检测到需要重试的任务,添加到重试队列,job_id:" + qj.getJob_id() + ",job_context:" + qj.getJob_context());
                JobCommon.insertLog(qj, "INFO", "检测到需要重试的任务,添加到重试队列,job_id:" + qj.getJob_id() + ",job_context:" + qj.getJob_context());
                if (!qj.getPlan_count().equals("-1") && qj.getCount()>=Long.parseLong(qj.getPlan_count())) {
                    JobCommon.insertLog(qj, "INFO", "检测到需要重试的任务,重试次数超过限制,实际重试:" + qj.getCount() + "次,job_id:" + qj.getJob_id() + ",job_context:" + qj.getJob_context());
                    taskLogsMapper.updateStatusById("error",tl.getId());
                    quartzJobMapper.updateLastStatus(qj.getJob_id(), "error");
                    continue;
                }
                qj.setLast_status("retry");
                quartzJobMapper.updateLastStatus(qj.getJob_id(), "retry");//retry表示当前的任务是重试发起的
                taskLogsMapper.updateStatusById("error",tl.getId());//error表示任务已置为失败
                logger.info("开始执行重试任务,job_id:" + qj.getJob_id() + ",job_context:" + qj.getJob_context());
                JobCommon.insertLog(qj, "INFO", "开始执行重试任务,job_id:" + qj.getJob_id() + ",job_context:" + qj.getJob_context());
                JobCommon.chooseJobBean(qj,true);

            }
            if (taskLogsList == null || taskLogsList.isEmpty()) {
                logger.debug("当前没有需要重试的任务");
            }

            //获取ETL处理的任务
            List<TaskLogs> taskLogsList2=taskLogsMapper.selectThreadByStatus3(task_log_status);
            List<ZdhHaInfo> zdhHaInfos=zdhHaInfoMapper.selectByStatus("enabled");
            if(zdhHaInfos.size()<1){
                logger.debug("没有可用的执行器,请启动zdh_server.....");
                return ;
            }
            Map<String,String> zdhHaMap=new HashMap<>();
            for(ZdhHaInfo zdhHaInfo:zdhHaInfos){
                zdhHaMap.put(zdhHaInfo.getId(),"");
            }
            for(TaskLogs t2 :taskLogsList2){
                if(t2.getUrl()==null||t2.getUrl().isEmpty()){
                    continue;
                }
                //http://ip:port/api/v1/zdh
                String executor=t2.getExecutor();
                if(!zdhHaMap.containsKey(executor)){
                 //executor 意外死亡需要重新发送任务
                    QuartzJobInfo q2=new QuartzJobInfo();
                    q2.setTask_log_id(t2.getId());
                    q2.setJob_id(t2.getJob_id());
                    //此处再次确认是否正在执行中执行器发生死亡
                    TaskLogs second_task_logs=taskLogsMapper.selectByPrimaryKey(t2.getId());
                    if(second_task_logs.getStatus().equalsIgnoreCase(task_log_status))

                    logger.info("检测到执行任务的EXECUTOR意外死亡,将重新选择EXECUTOR执行任务");
                    JobCommon.insertLog(q2,"INFO","检测到执行任务的EXECUTOR意外死亡,将重新选择EXECUTOR执行任务");
                    ZdhHaInfo zdhHaInfo=JobCommon.getZdhUrl(zdhHaInfoMapper);
                    URI old_uri=URI.create(t2.getUrl());
                    String new_authori=URI.create(zdhHaInfo.getZdh_url()).getAuthority();
                    String new_url=old_uri.getScheme()+"://"+new_authori+old_uri.getPath();
                    logger.info("重新发送请求地址:"+new_url+",参数:"+t2.getEtl_info());
                    JobCommon.insertLog(q2,"INFO","重新发送请求地址:"+new_url+",参数:"+t2.getEtl_info());
                    HttpUtil.postJSON(new_url, t2.getEtl_info());
                    t2.setExecutor(zdhHaInfo.getId());
                    t2.setUrl(new_url);
                    t2.setUpdate_time(new Timestamp(new Date().getTime()));
                    JobCommon.updateTaskLog(t2, taskLogsMapper);
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
