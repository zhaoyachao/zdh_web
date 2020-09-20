package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.dao.TaskLogsMapper;
import com.zyc.zdh.dao.ZdhDownloadMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.service.AccountService;
import com.zyc.zdh.service.JemailService;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

//定期拉取失败任务并发送邮件
public class EmailJob {

    private static Logger logger= LoggerFactory.getLogger(EmailJob.class);

    public static List<ZdhDownloadInfo> zdhDownloadInfos=new ArrayList<>();

    public static void run(QuartzJobInfo quartzJobInfo) {
        try{
            logger.debug("开始检测失败任务...");
            TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");
            ZdhLogsService zdhLogsService= (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
            JemailService jemailService= (JemailService) SpringContext.getBean("jemailServiceImpl");
            QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
            AccountService accountService=(AccountService) SpringContext.getBean("accountService");
            //获取失败的任务
            List<EmailTaskLogs> emailTaskLogsList=taskLogsMapper.selectByStatus();

            //根据任务执行时间，主键 获取对应的日志信息
            for(EmailTaskLogs emailTaskLogs:emailTaskLogsList){
                String levels = "'DEBUG','WARN','INFO','ERROR'";
                List<ZdhLogs> zhdLogs = zdhLogsService.selectByTime(emailTaskLogs.getJob_id(),emailTaskLogs.getId(),
                        emailTaskLogs.getStart_time(), emailTaskLogs.getUpdate_time(), levels);
                Iterator<ZdhLogs> it = zhdLogs.iterator();
                StringBuilder sb = new StringBuilder("");
                //拼接邮件信息
                while (it.hasNext()) {
                    ZdhLogs next = it.next();
                    String info = "任务ID:" + next.getJob_id() + ",任务执行时间:" + next.getLog_time().toString() + ",日志["+next.getLevel()+"]:" + next.getMsg();
                    sb.append(info + "\r\n");
                }
                logger.info("检测失败任务:"+emailTaskLogs.getJob_id()+",对应主键:"+emailTaskLogs.getId());
                List<String> emails=new ArrayList<>();
                List<String> phones=new ArrayList<>();
                QuartzJobInfo qj=quartzJobMapper.selectByPrimaryKey(emailTaskLogs.getJob_id());
                if(qj.getAlarm_enabled()!=null && qj.getAlarm_enabled().equalsIgnoreCase("on") && qj.getAlarm_account()!=null && !qj.getAlarm_account().equalsIgnoreCase("")){
                    List<User> users=accountService.findByUserName2(qj.getAlarm_account().split(","));
                    for(User user:users){
                      if(user.getEmail()!=null){
                          emails.add(user.getEmail());
                      }
                      if(user.getPhone()!=null){
                        phones.add(user.getPhone());
                      }
                    }

                    if(emails.size()>0){
                        jemailService.sendEmail(emails.toArray(new String[0]),"任务监控:"+emailTaskLogs.getJob_context(),sb.toString());
                    }
                    if(phones.size()>0&& qj.getEmail_and_sms()!=null && qj.getEmail_and_sms().equalsIgnoreCase("on")){
                        logger.info("手机短信监控,暂时未开通,需要连接第三方短信服务");
                    }
                }

                TaskLogs taskLogs=new TaskLogs();
                taskLogs.setId(emailTaskLogs.getId());
                taskLogs.setJob_id(emailTaskLogs.getJob_id());
                taskLogs.setJob_context(emailTaskLogs.getJob_context());
                taskLogs.setStatus(emailTaskLogs.getStatus());
                taskLogs.setStart_time(emailTaskLogs.getStart_time());
                taskLogs.setUpdate_time(emailTaskLogs.getUpdate_time());
                taskLogs.setProcess(emailTaskLogs.getProcess());
                taskLogs.setOwner(emailTaskLogs.getOwner());
                taskLogs.setEtl_date(emailTaskLogs.getEtl_date());
                taskLogs.setIs_notice("true");
                taskLogsMapper.updateByPrimaryKey(taskLogs);
                logger.info("检测失败任务:"+emailTaskLogs.getJob_id()+",对应主键:"+emailTaskLogs.getId()+",并完成更新");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public static void notice_event(){

        logger.debug("开始加载待下载的文件信息");
        ZdhDownloadMapper zdhDownloadMapper = (ZdhDownloadMapper) SpringContext.getBean("zdhDownloadMapper");
        RedisUtil redisUtil = (RedisUtil) SpringContext.getBean("redisUtil");

        List<ZdhDownloadInfo> zdhDownloadInfos=zdhDownloadMapper.selectNotice();

        Iterator<ZdhDownloadInfo> iterator=zdhDownloadInfos.iterator();
        Map<String,List<ZdhDownloadInfo>> map=new HashMap<>();
        while (iterator.hasNext()){
            ZdhDownloadInfo zdhDownloadInfo=iterator.next();
            if(map.containsKey(zdhDownloadInfo.getOwner())){
                map.get(zdhDownloadInfo.getOwner()).add(zdhDownloadInfo);
            }else{
                List<ZdhDownloadInfo> zdhDownloadInfos2=new ArrayList<>();
                zdhDownloadInfos2.add(zdhDownloadInfo);
                map.put(zdhDownloadInfo.getOwner(),zdhDownloadInfos2);
            }
        }

        for(Map.Entry<String,List<ZdhDownloadInfo>> a: map.entrySet()){
            String key=a.getKey();
            redisUtil.set("zdhdownloadinfos_"+key,JSON.toJSONString(a.getValue()));
        }
        logger.debug("完成加载待下载的文件信息");

    }
}
