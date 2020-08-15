package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.dao.TaskLogsMapper;
import com.zyc.zdh.dao.ZdhDownloadMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.service.JemailService;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.TimeUnit;

//定期拉取重试任务
public class RetryJob {

    private static Logger logger = LoggerFactory.getLogger(RetryJob.class);

    public static List<ZdhDownloadInfo> zdhDownloadInfos = new ArrayList<>();

    public static void run(QuartzJobInfo quartzJobInfo) {
        try {
            logger.info("开始检测重试任务...");
            QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
            TaskLogsMapper taskLogsMapper=(TaskLogsMapper) SpringContext.getBean("taskLogsMapper");
            //获取重试的任务
            List<TaskLogs> taskLogsList=taskLogsMapper.selectThreadByStatus2("wait_retry");
            for(TaskLogs tl :taskLogsList){
                QuartzJobInfo qj= quartzJobMapper.selectByPrimaryKey(tl.getJob_id());
                logger.info("检测到需要重试的任务,添加到重试队列,job_id:" + qj.getJob_id() + ",job_context:" + qj.getJob_context());
                JobCommon.insertLog(qj.getJob_id(), "INFO", "检测到需要重试的任务,添加到重试队列,job_id:" + qj.getJob_id() + ",job_context:" + qj.getJob_context());
                if (!qj.getPlan_count().equals("-1") && qj.getCount()>=Long.parseLong(qj.getPlan_count())) {
                    JobCommon.insertLog(qj.getJob_id(), "INFO", "检测到需要重试的任务,重试次数超过限制,实际重试:" + qj.getCount() + "次,job_id:" + qj.getJob_id() + ",job_context:" + qj.getJob_context());
                    quartzJobMapper.updateLastStatus(qj.getJob_id(), "error");
                    continue;
                }
                int interval_time=(qj.getInterval_time()==null || qj.getInterval_time().equals("")) ? 5:Integer.parseInt(qj.getInterval_time());
                RetryJobInfo retryJobInfo = new RetryJobInfo(qj.getJob_context(), qj, interval_time, TimeUnit.SECONDS);
                qj.setLast_status("retry");
                quartzJobMapper.updateLastStatus(qj.getJob_id(), "retry");
                taskLogsMapper.updateStatusById("retryed",tl.getId());
                //JobCommon.retryQueue.add(retryJobInfo);
                logger.info("开始执行重试任务,job_id:" + qj.getJob_id() + ",job_context:" + qj.getJob_context());
                JobCommon.insertLog(qj.getJob_id(), "INFO", "开始执行重试任务,job_id:" + qj.getJob_id() + ",job_context:" + qj.getJob_context());
                JobCommon.chooseJobBean(qj,true);

            }
            if (taskLogsList == null || taskLogsList.isEmpty()) {
                logger.info("当前没有需要重试的任务");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
