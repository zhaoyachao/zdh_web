package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.hubspot.jinjava.Jinjava;
import com.zyc.zdh.controller.ZdhMonitorController;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.service.AccountService;
import com.zyc.zdh.service.JemailService;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.SpringContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.zyc.zdh.job.JobCommon2.*;

//定期拉取失败任务并发送邮件
public class EmailJob {

    public static String jobType = "EMAIL";
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
            List<TaskLogInstance> tlis=taskLogInstanceMapper.selectByStatus(JobStatus.ERROR.getValue());
            String line = Const.LINE_SEPARATOR;
            //根据任务执行时间，主键 获取对应的日志信息
            for(TaskLogInstance tli:tlis){

                logger.info("检测失败任务:"+tli.getJob_id()+",对应主键:"+tli.getId()+",对应任务组id:"+tli.getGroup_id());

                String msg="失败任务:\r\n" +
                        "调度任务:"+tli.getJob_id()+",调度名:"+tli.getJob_context()+"\r\n"+
                        "任务组:"+tli.getGroup_id()+",任务组名:"+tli.getGroup_context()+"\r\n"+
                        "ETL任务:"+tli.getEtl_task_id()+",ETL任务名:"+tli.getEtl_context()+"\r\n"+
                        "ETL任务类型:"+tli.getMore_task()+"\r\n"+
                        "任务实例id:"+tli.getId()+",任务实例名:"+tli.getEtl_context() +"\r\n"+
                        "ETL日期:"+tli.getEtl_date()+"\r\n"+
                        "开始时间:"+DateUtil.formatTime(tli.getRun_time())+"\r\n";
                alarm(tli, "任务失败通知: "+tli.getJob_context(),msg);
                logger.info("检测失败任务:"+tli.getJob_id()+",对应主键:"+tli.getId()+",并完成更新");
            }

            //获取超时任务
            List<TaskLogInstance> taskLogInstances= taskLogInstanceMapper.selectOverTime();
            if(taskLogInstances!=null && taskLogInstances.size()>0){
                System.out.println("超时任务量:"+taskLogInstances.size());
                for(TaskLogInstance tli : taskLogInstances){
                    String msg="超时任务:\r\n" +
                            "调度任务:"+tli.getJob_id()+",调度名:"+tli.getJob_context()+"\r\n"+
                            "任务组:"+tli.getGroup_id()+",任务组名:"+tli.getGroup_context()+"\r\n"+
                            "ETL任务:"+tli.getEtl_task_id()+",ETL任务名:"+tli.getEtl_context()+"\r\n"+
                            "ETL任务类型:"+tli.getMore_task()+"\r\n"+
                            "任务实例id:"+tli.getId()+",任务实例名:"+tli.getEtl_context() +"\r\n"+
                            "ETL日期:"+tli.getEtl_date()+"\r\n"+
                            "开始时间:"+DateUtil.formatTime(tli.getRun_time())+"\r\n";
                    if (tli.getJob_type().equalsIgnoreCase(ShellJob.jobType)){
                        msg = msg +"\r\nSHELL任务超时自动杀死";
                        ZdhMonitorController zdhmc = (ZdhMonitorController) SpringContext.getBean("zdhMonitorController");
                        zdhmc.killJob(tli.getId());

                    }
                    alarm(tli, "任务超时通知: "+tli.getJob_context(),msg);
                }
            }

            //超时,但之后完成任务
            List<TaskLogInstance> taskLogInstances2= taskLogInstanceMapper.selectOverTimeFinish();
            if(taskLogInstances2!=null && taskLogInstances2.size()>0){
                System.out.println("超时完成任务量:"+taskLogInstances2.size());
                for(TaskLogInstance tli : taskLogInstances2){
                    String msg="超时任务id:"+tli.getId()+" ,已完成,完成时间:"+ DateUtil.formatTime(tli.getUpdate_time());
                    alarm(tli, "超时任务完成通知: "+tli.getJob_context(),msg);
                }
            }

            //正常完成任务后通知
            List<TaskLogInstance> taskLogInstances3= taskLogInstanceMapper.selectNoNoticeFinish();
            if(taskLogInstances3!=null && taskLogInstances3.size()>0){
                for(TaskLogInstance tli : taskLogInstances3){

                    String msg="任务完成通知:\r\n" +
                            "调度任务:"+tli.getJob_id()+",调度名:"+tli.getJob_context()+"\r\n"+
                            "任务组:"+tli.getGroup_id()+",任务组名:"+tli.getGroup_context()+"\r\n"+
                            "ETL任务:"+tli.getEtl_task_id()+",ETL任务名:"+tli.getEtl_context()+"\r\n"+
                            "ETL任务类型:"+tli.getMore_task()+"\r\n"+
                            "任务实例id:"+tli.getId()+",任务实例名:"+tli.getEtl_context() +"\r\n"+
                            "ETL日期:"+tli.getEtl_date()+"\r\n"+
                            "开始时间:"+DateUtil.formatTime(tli.getRun_time())+"\r\n"+
                            "完成时间:"+ DateUtil.formatTime(tli.getUpdate_time());
                    alarm(tli, "任务完成通知: "+tli.getJob_context(),msg);
                }
            }


        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
        }

    }


    private static void alarm(TaskLogInstance tli, String title, String msg){
        try{
            JemailService jemailService= (JemailService) SpringContext.getBean("jemailServiceImpl");
            TaskLogInstanceMapper taskLogInstanceMapper = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
            AccountService accountService=(AccountService) SpringContext.getBean("accountService");
            NoticeMapper noticeMapper = (NoticeMapper) SpringContext.getBean("noticeMapper");

            if(StringUtils.isEmpty(tli.getAlarm_account())){
                logger.warn("当前告警为找到告警账号:"+JSON.toJSONString(tli));
                return ;
            }

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

            if(emails.size()>0 && tli.getAlarm_email()!=null  && tli.getAlarm_email().equalsIgnoreCase("on")){
                jemailService.sendEmail(emails.toArray(new String[0]),title,msg);
            }
            if(phones.size()>0&& tli.getAlarm_sms()!=null && tli.getAlarm_sms().equalsIgnoreCase("on")){
                logger.info("手机短信监控,暂时未开通,需要连接第三方短信服务");
                try{
                    //此处信息写入短信表,待平台接入短信服务
                    AlarmSmsMapper alarmSmsMapper=  (AlarmSmsMapper) SpringContext.getBean("alarmSmsMapper");
                    AlarmSmsInfo alarmSmsInfo=new AlarmSmsInfo();
                    alarmSmsInfo.setTitle(title);
                    alarmSmsInfo.setMsg(msg);
                    alarmSmsInfo.setMsg_url("log_txt.html?job_id="+tli.getJob_id()+"&task_log_id="+tli.getId());
                    alarmSmsInfo.setMsg_type("通知");
                    alarmSmsInfo.setStatus(Const.SMS_INIT);
                    alarmSmsInfo.setCreate_time(new Timestamp(new Date().getTime()));
                    alarmSmsInfo.setUpdate_time(new Timestamp(new Date().getTime()));
                    for(String phone:phones){
                        alarmSmsInfo.setPhone(phone);
                        alarmSmsMapper.insertSelective(alarmSmsInfo);
                    }
                }catch (Exception e){
                    String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
                    logger.error(error, e);
                    logger.error("发送告警短信失败, {}",e);
                }

            }

            if( !StringUtils.isEmpty(tli.getAlarm_account()) && tli.getAlarm_zdh()!=null  && tli.getAlarm_zdh().equalsIgnoreCase("on")){
                for(User user:users){
                    NoticeInfo ni=new NoticeInfo();
                    ni.setMsg_type("通知");
                    ni.setMsg_title(title);
                    ni.setMsg_url("log_txt.html?job_id="+tli.getJob_id()+"&task_log_id="+tli.getId());
                    ni.setMsg(msg);
                    ni.setIs_see(Const.FALSE);
                    ni.setOwner(user.getUserName());
                    ni.setCreate_time(new Timestamp(new Date().getTime()));
                    ni.setUpdate_time(new Timestamp(new Date().getTime()));
                    noticeMapper.insertSelective(ni);
                }
            }
            taskLogInstanceMapper.updateNoticeById(Const.TRUR,tli.getId());
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
        }

    }

    public static void alarm(QuartzJobInfo qji, String title, String msg){
        try{
            JemailService jemailService= (JemailService) SpringContext.getBean("jemailServiceImpl");
            AccountService accountService=(AccountService) SpringContext.getBean("accountService");
            NoticeMapper noticeMapper = (NoticeMapper) SpringContext.getBean("noticeMapper");
            if(StringUtils.isEmpty(qji.getAlarm_account())){
                logger.warn("当前告警为找到告警账号:"+JSON.toJSONString(qji));
                return ;
            }
            List<User> users=accountService.findByUserName2(qji.getAlarm_account().split(","));
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

            if(emails.size()>0 && qji.getAlarm_email()!=null  && qji.getAlarm_email().equalsIgnoreCase("on")){
                jemailService.sendEmail(emails.toArray(new String[0]),title,msg);
            }
            if(phones.size()>0&& qji.getAlarm_sms()!=null && qji.getAlarm_sms().equalsIgnoreCase("on")){
                logger.info("手机短信监控,暂时未开通,需要连接第三方短信服务");
                try{
                    //此处信息写入短信表,待平台接入短信服务
                    AlarmSmsMapper alarmSmsMapper=  (AlarmSmsMapper) SpringContext.getBean("alarmSmsMapper");
                    AlarmSmsInfo alarmSmsInfo=new AlarmSmsInfo();
                    alarmSmsInfo.setTitle(title);
                    alarmSmsInfo.setMsg(msg);
                    alarmSmsInfo.setMsg_url("");
                    alarmSmsInfo.setMsg_type("通知");
                    alarmSmsInfo.setStatus(Const.SMS_INIT);
                    alarmSmsInfo.setCreate_time(new Timestamp(new Date().getTime()));
                    alarmSmsInfo.setUpdate_time(new Timestamp(new Date().getTime()));
                    for(String phone:phones){
                        alarmSmsInfo.setPhone(phone);
                        alarmSmsMapper.insertSelective(alarmSmsInfo);
                    }
                }catch (Exception e){
                    String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
                    logger.error(error, e);
                    logger.error("发送告警短信失败, {}",e);
                }

            }

            if( !StringUtils.isEmpty(qji.getAlarm_account()) && qji.getAlarm_zdh()!=null  && qji.getAlarm_zdh().equalsIgnoreCase("on")){
                for(User user:users){
                    NoticeInfo ni=new NoticeInfo();
                    ni.setMsg_type("通知");
                    ni.setMsg_title(title);
                    ni.setMsg_url("");
                    ni.setMsg(msg);
                    ni.setIs_see(Const.FALSE);
                    ni.setOwner(user.getUserName());
                    ni.setCreate_time(new Timestamp(new Date().getTime()));
                    ni.setUpdate_time(new Timestamp(new Date().getTime()));
                    noticeMapper.insertSelective(ni);
                }
            }
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
        }

    }

    public static void notice_event(){

        try{
            logger.debug("开始加载通知信息");
            ZdhDownloadMapper zdhDownloadMapper = (ZdhDownloadMapper) SpringContext.getBean("zdhDownloadMapper");
            RedisUtil redisUtil = (RedisUtil) SpringContext.getBean("redisUtil");
            NoticeMapper noticeMapper = (NoticeMapper) SpringContext.getBean("noticeMapper");
            List<ZdhDownloadInfo> zdhDownloadInfos=zdhDownloadMapper.selectNotice();

            Iterator<ZdhDownloadInfo> iterator=zdhDownloadInfos.iterator();
            Map<String,List<ZdhDownloadInfo>> map=new HashMap<>();
            while (iterator.hasNext()){
                ZdhDownloadInfo zdhDownloadInfo=iterator.next();
                zdhDownloadInfo.setIs_notice(Const.TRUR);
                zdhDownloadMapper.updateByPrimaryKeySelective(zdhDownloadInfo);
                NoticeInfo ni=new NoticeInfo();
                ni.setMsg_type("文件下载");
                ni.setMsg_title("文件下载:"+zdhDownloadInfo.getJob_context());
                ni.setMsg("文件以生成,请尽快下载, 文件由任务【"+zdhDownloadInfo.getJob_context()+"】于"+zdhDownloadInfo.getCreate_time()
                        +"产生, 文件唯一码: "+zdhDownloadInfo.getId()+", 请前往系统=>下载管理页面进行下载,时间:"+DateUtil.getCurrentTime());
                ni.setIs_see(Const.FALSE);
                ni.setOwner(zdhDownloadInfo.getOwner());
                ni.setCreate_time(new Timestamp(new Date().getTime()));
                ni.setUpdate_time(new Timestamp(new Date().getTime()));
                noticeMapper.insertSelective(ni);

//            if(map.containsKey(zdhDownloadInfo.getOwner())){
//                map.get(zdhDownloadInfo.getOwner()).add(zdhDownloadInfo);
//            }else{
//                List<ZdhDownloadInfo> zdhDownloadInfos2=new ArrayList<>();
//                zdhDownloadInfos2.add(zdhDownloadInfo);
//                map.put(zdhDownloadInfo.getOwner(),zdhDownloadInfos2);
//            }
            }

//        for(Map.Entry<String,List<ZdhDownloadInfo>> a: map.entrySet()){
//            String key=a.getKey();
//            redisUtil.set("zdhdownloadinfos_"+key,JSON.toJSONString(a.getValue()));
//        }
            apply_notice();
            logger.debug("完成加载通知信息");
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
        }


    }

    public static void apply_notice(){
        try{
            logger.debug("加载申请通知信息");
            ApplyMapper applyMapper = (ApplyMapper) SpringContext.getBean("applyMapper");
            RedisUtil redisUtil = (RedisUtil) SpringContext.getBean("redisUtil");
            NoticeMapper noticeMapper = (NoticeMapper) SpringContext.getBean("noticeMapper");

            List<ApplyInfo> applyInfos=applyMapper.selectNotice();
            if(applyInfos!=null){
                Iterator<ApplyInfo> iterator2=applyInfos.iterator();
                Map<String,List<ApplyInfo>> map2=new HashMap<>();
                while (iterator2.hasNext()){
                    ApplyInfo applyInfo=iterator2.next();
                    applyInfo.setIs_notice(Const.TRUR);
                    applyMapper.updateByPrimaryKeySelective(applyInfo);
                    NoticeInfo ni=new NoticeInfo();
                    ni.setMsg_type("审批");
                    ni.setMsg_title("审批通知:"+applyInfo.getApply_context());
                    ni.setMsg("你有一个审批单,需要处理,单号: "+applyInfo.getId()+",申请原因:"+ applyInfo.getReason()+",时间:"+DateUtil.getCurrentTime());
                    ni.setIs_see(Const.FALSE);
                    ni.setOwner(applyInfo.getOwner());
                    ni.setCreate_time(new Timestamp(new Date().getTime()));
                    ni.setUpdate_time(new Timestamp(new Date().getTime()));
                    noticeMapper.insertSelective(ni);

//                if(map2.containsKey(applyInfo.getApprove_id())){
//                    map2.get(applyInfo.getApprove_id()).add(applyInfo);
//                }else{
//                    List<ApplyInfo> applyInfos2=new ArrayList<>();
//                    applyInfos2.add(applyInfo);
//                    map2.put(applyInfo.getApprove_id(),applyInfos2);
//                }
                }

//            for(Map.Entry<String,List<ApplyInfo>> a: map2.entrySet()){
//                String key=a.getKey();
//                redisUtil.set("zdhapplyinfos_"+key,JSON.toJSONString(a.getValue()));
//            }
            }
            logger.debug("完成加载申请通知信息");
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
        }
    }

    public static void send_notice(String owner, String title, String msg, String msg_type){
        try{
            NoticeMapper noticeMapper = (NoticeMapper) SpringContext.getBean("noticeMapper");
            NoticeInfo ni=new NoticeInfo();
            ni.setMsg_type(msg_type);
            ni.setMsg_title(title);
            ni.setMsg_url("");
            ni.setMsg(msg);
            ni.setIs_see(Const.FALSE);
            ni.setOwner(owner);
            ni.setCreate_time(new Timestamp(new Date().getTime()));
            ni.setUpdate_time(new Timestamp(new Date().getTime()));
            noticeMapper.insertSelective(ni);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
        }
    }


    public static boolean run(TaskLogInstance tli){

        Boolean exe_status = true;
        //执行命令
        try {
            logger.info("email任务当前只支持同步email,异步email暂不支持");
            insertLog(tli,"info","email任务当前只支持同步email,异步email暂不支持");
            //当前只支持检查文件是否存在 if [ ! -f "/data/filename" ];then echo "文件不存在"; else echo "true"; fi
            //日期替换zdh.date => yyyy-MM-dd 模式
            //日期替换zdh.date.nodash=> yyyyMMdd 模式
            Map<String, Object> jinJavaParam = getJinJavaParam(tli);

            Jinjava jj = new Jinjava();

            String run_jsmind = tli.getRun_jsmind_data();
            String subject = JSON.parseObject(run_jsmind).getString("subject");
            String to_emails = JSON.parseObject(run_jsmind).getString("to_emails");
            String email_type = JSON.parseObject(run_jsmind).getString("email_type");
            String email_context = JSON.parseObject(run_jsmind).getString("email_context");
            email_context = jj.render(email_context, jinJavaParam);

            if(StringUtils.isEmpty(email_type)){
                throw new Exception("email任务类型为空");
            }
            if(StringUtils.isEmpty(to_emails)){
                throw new Exception("email任务接收方为空");
            }
            if(email_type.equalsIgnoreCase(Const.EMAIL_TXT)){
                sendTxtEmail(to_emails.split(","), subject, email_context);
            }
            if(email_type.equalsIgnoreCase(Const.EMAIL_HTML)){
                sendHtmlEmail(to_emails.split(","), subject, email_context);
            }
            if(!email_type.equalsIgnoreCase(Const.EMAIL_TXT) && !email_type.equalsIgnoreCase(Const.EMAIL_HTML)){
                throw new Exception("无法识别的email类型,系统只支持txt,html格式");
            }

            insertLog(tli, "info", "[" + jobType + "] JOB 发送成功");
        } catch (Exception e) {
            logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            logger.error(e.getMessage());
            insertLog(tli, "error","[" + jobType + "] JOB ,"+ e.getMessage());
            jobFail(jobType,tli);
            exe_status = false;
        }
        return exe_status;
    }

    public static void sendTxtEmail(String[] to,String subject, String context){

        Environment environment= (Environment) SpringContext.getBean("environment");
        JavaMailSender javaMailSender= (JavaMailSender)SpringContext.getBean("javaMailSender");

        SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
        simpleMailMessage.setFrom(environment.getProperty("spring.mail.username"));
        simpleMailMessage.setTo(to);
        simpleMailMessage.setText(context);
        simpleMailMessage.setSubject(subject);

        javaMailSender.send(simpleMailMessage);
    }

    public static void sendHtmlEmail(String[] to,String subject, String context) throws MessagingException {

        Environment environment= (Environment) SpringContext.getBean("environment");
        JavaMailSender javaMailSender= (JavaMailSender)SpringContext.getBean("javaMailSender");

        MimeMessage mailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage);
        messageHelper.setFrom(environment.getProperty("spring.mail.username"));
        messageHelper.setTo(to);
        messageHelper.setText(context,true);
        messageHelper.setSubject(subject);
        javaMailSender.send(mailMessage);
    }

    public static void sendAdminAlarmEmail(String subject, String context){
        RedisUtil redisUtil = (RedisUtil) SpringContext.getBean("redisUtil");
        Environment environment= (Environment) SpringContext.getBean("environment");
        JavaMailSender javaMailSender= (JavaMailSender)SpringContext.getBean("javaMailSender");
        String key = "alarm.admin.email_"+subject;
        if(redisUtil.get(key) == null){
            redisUtil.set(key, "1", 1L, TimeUnit.HOURS);
        }else{
            logger.debug("系统告警,已存在,忽略当前告警, {}", key);
            return;
        }
        try{
            //多个告警邮箱,逗号分割
            String to=environment.getProperty("alarm.admin.email");
            SimpleMailMessage simpleMailMessage=new SimpleMailMessage();
            simpleMailMessage.setFrom(environment.getProperty("spring.mail.username"));
            simpleMailMessage.setTo(to.split(","));
            simpleMailMessage.setText(context);
            simpleMailMessage.setSubject(subject);

            javaMailSender.send(simpleMailMessage);
        }catch (Exception e){
            redisUtil.remove("alarm.admin.email_"+subject);
        }

    }
}
