package com.zyc.zdh.run;

import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.entity.QuartzJobInfo;
import com.zyc.zdh.job.EmailJob;
import com.zyc.zdh.job.JobCommon;
import com.zyc.zdh.job.JobModel;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.util.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Component
public class SystemCommandLineRunner implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(SystemCommandLineRunner.class);
    @Autowired
    QuartzManager2 quartzManager2;

    @Autowired
    QuartzJobMapper quartzJobMapper;

    @Autowired
    Environment ev;

    @Override
    public void run(String... strings) throws Exception {
        runSnowflakeIdWorker();
        logger.info("初始化通知事件");
        EmailJob.notice_event();
        logger.info("初始化失败任务监控程序");
        //检测是否有email 任务 如果没有则添加
        QuartzJobInfo qj = new QuartzJobInfo();
        qj.setJob_type("EMAIL");
        List<QuartzJobInfo> quartzJobInfos = quartzJobMapper.select(qj);
        if (quartzJobInfos.size() > 0) {
            logger.info("已经存在历史监控任务...");
        }else{
            logger.info("自动生成监控任务");
            String expr = ev.getProperty("email.schedule.interval");
            QuartzJobInfo quartzJobInfo = quartzManager2.createQuartzJobInfo("EMAIL", JobModel.REPEAT.getValue(), new Date(), new Date(), "", expr, "-1", "", "email");
            quartzJobInfo.setJob_id(SnowflakeIdWorker.getInstance().nextId() + "");
            quartzManager2.addQuartzJobInfo(quartzJobInfo);
            quartzManager2.addTaskToQuartz(quartzJobInfo);
        }
    }


    public void runSnowflakeIdWorker() {
        logger.info("初始化分布式id生成器");
        //获取服务id
        String myid = ev.getProperty("myid", "0");
        SnowflakeIdWorker.init(Integer.parseInt(myid), 0);
    }

    public void runLogMQ(){
        logger.info("初始化日志");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        JobCommon.logThread(zdhLogsService);
    }
}
