package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.dao.TaskLogInstanceMapper;
import com.zyc.zdh.dao.ZdhDownloadMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.service.AccountService;
import com.zyc.zdh.service.JemailService;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.SpringContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

//定期拉取失败任务并发送邮件
public class EmailJob {

    private static Logger logger= LoggerFactory.getLogger(EmailJob.class);

    public static List<ZdhDownloadInfo> zdhDownloadInfos=new ArrayList<>();

    public static void run(QuartzJobInfo quartzJobInfo) {
        try{
            logger.debug("开始检测失败任务...");
            TaskLogInstanceMapper taskLogInstanceMapper = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
            ZdhLogsService zdhLogsService= (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
            JemailService jemailService= (JemailService) SpringContext.getBean("jemailServiceImpl");
            QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
            AccountService accountService=(AccountService) SpringContext.getBean("accountService");
            //获取失败的任务
            List<EmailTaskLogs> emailTaskLogsList=taskLogInstanceMapper.selectByStatus();
            String line = System.getProperty("line.separator");
            //根据任务执行时间，主键 获取对应的日志信息
            for(EmailTaskLogs emailTaskLogs:emailTaskLogsList){
                String levels = "'DEBUG','WARN','INFO','ERROR'";
                List<ZdhLogs> zhdLogs = zdhLogsService.selectByTime(emailTaskLogs.getJob_id(),emailTaskLogs.getId(),
                        emailTaskLogs.getStart_time(), emailTaskLogs.getUpdate_time(), levels);
                Iterator<ZdhLogs> it = zhdLogs.iterator();
                StringBuilder sb = new StringBuilder("");
                sb.append("调度任务ID:"+emailTaskLogs.getJob_id()+",调度任务名:"+emailTaskLogs.getJob_context()+line);
                sb.append("任务组实例ID:"+emailTaskLogs.getGroup_id()+",任务组名:"+emailTaskLogs.getJob_context()+line);
                sb.append("子任务ID:"+emailTaskLogs.getEtl_task_id()+",子任务名:"+emailTaskLogs.getEtl_context()+line);
                sb.append("本次子任务实例ID:"+emailTaskLogs.getId()+",本次任务时间:"+emailTaskLogs.getEtl_date()+line);
                //拼接邮件信息
                while (it.hasNext()) {
                    ZdhLogs next = it.next();
                    String info = "任务ID:" + next.getJob_id() + ",任务执行时间:" + next.getLog_time().toString() + ",日志["+next.getLevel()+"]:" + next.getMsg();
                    sb.append(info + line);
                }
                logger.info("检测失败任务:"+emailTaskLogs.getJob_id()+",对应主键:"+emailTaskLogs.getId()+",对应任务组id:"+emailTaskLogs.getGroup_id());
                List<String> emails=new ArrayList<>();
                List<String> phones=new ArrayList<>();
                QuartzJobInfo qj=quartzJobMapper.selectByPrimaryKey(emailTaskLogs.getJob_id());
                if(qj.getAlarm_enabled()!=null && qj.getAlarm_enabled().equalsIgnoreCase("on") &&
                        qj.getAlarm_account()!=null && !qj.getAlarm_account().equalsIgnoreCase("")){
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


                taskLogInstanceMapper.updateNoticeById("true",emailTaskLogs.getId());
                logger.info("检测失败任务:"+emailTaskLogs.getJob_id()+",对应主键:"+emailTaskLogs.getId()+",并完成更新");
            }

            //获取超时任务
            List<TaskLogInstance> taskLogInstances= taskLogInstanceMapper.selectOverTime();
            if(taskLogInstances!=null && taskLogInstances.size()>0){
                System.out.println("超时任务量:"+taskLogInstances.size());
                for(TaskLogInstance tli : taskLogInstances){
                    List<User> users=accountService.findByUserName2(tli.getAlarm_account().split(","));
                    List<String> emails=new ArrayList<>();
                    List<String> phones=new ArrayList<>();
                    for(User user:users){
                        if(user.getEmail()!=null){
                            System.out.println("email:"+user.getEmail());
                            emails.add(user.getEmail());
                        }
                        if(user.getPhone()!=null){
                            phones.add(user.getPhone());
                        }
                    }

                    String msg="超时任务:\r\n" +
                    "调度任务:"+tli.getJob_id()+","+tli.getJob_context()+"\r\n"+
                    "任务组:"+tli.getGroup_id()+","+tli.getGroup_context()+"\r\n"+
                    "ETL任务:"+tli.getEtl_task_id()+","+tli.getEtl_context()+"\r\n"+
                    "任务实例id:"+tli.getId()+","+tli.getEtl_context();
                    if(emails.size()>0){
                        jemailService.sendEmail(emails.toArray(new String[0]),"任务监控:"+tli.getJob_context(),msg);
                    }
                    if(phones.size()>0&& tli.getEmail_and_sms()!=null && tli.getEmail_and_sms().equalsIgnoreCase("on")){
                        logger.info("手机短信监控,暂时未开通,需要连接第三方短信服务");
                    }

                    taskLogInstanceMapper.updateNoticeById("alarm",tli.getId());
                }
            }

            //超时,但之后完成任务
            List<TaskLogInstance> taskLogInstances2= taskLogInstanceMapper.selectOverTimeFinish();
            if(taskLogInstances2!=null && taskLogInstances2.size()>0){
                System.out.println("超时完成任务量:"+taskLogInstances2.size());
                for(TaskLogInstance tli : taskLogInstances2){
                    List<User> users=accountService.findByUserName2(tli.getAlarm_account().split(","));
                    List<String> emails=new ArrayList<>();
                    List<String> phones=new ArrayList<>();
                    for(User user:users){
                        if(user.getEmail()!=null){
                            System.out.println("email:"+user.getEmail());
                            emails.add(user.getEmail());
                        }
                        if(user.getPhone()!=null){
                            phones.add(user.getPhone());
                        }
                    }

                    String msg="超时任务id:"+tli.getId()+" ,已完成,完成时间:"+ DateUtil.formatTime(tli.getUpdate_time());
                    if(emails.size()>0){
                        jemailService.sendEmail(emails.toArray(new String[0]),"任务监控:"+tli.getJob_context(),msg);
                    }
                    if(phones.size()>0&& tli.getEmail_and_sms()!=null && tli.getEmail_and_sms().equalsIgnoreCase("on")){
                        logger.info("手机短信监控,暂时未开通,需要连接第三方短信服务");
                    }

                    taskLogInstanceMapper.updateNoticeById("true",tli.getId());
                }
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
