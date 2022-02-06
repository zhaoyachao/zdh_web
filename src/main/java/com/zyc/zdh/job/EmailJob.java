package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
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
import com.zyc.zdh.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
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
            List<TaskLogInstance> tlis=taskLogInstanceMapper.selectByStatus(JobStatus.ERROR.getValue());
            String line = System.getProperty("line.separator");
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

            //完成任务后通知
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
            logger.error("告警模块", e.getCause());
        }

    }


    private static void alarm(TaskLogInstance tli, String title, String msg){
        JemailService jemailService= (JemailService) SpringContext.getBean("jemailServiceImpl");
        TaskLogInstanceMapper taskLogInstanceMapper = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        AccountService accountService=(AccountService) SpringContext.getBean("accountService");
        NoticeMapper noticeMapper = (NoticeMapper) SpringContext.getBean("noticeMapper");
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
                    alarmSmsMapper.insert(alarmSmsInfo);
                }
            }catch (Exception e){
                String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常:"+e.getMessage()+", 异常详情:{}";
                logger.error(error, e);
                logger.error("发送告警短信失败",e.getCause());
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
                ni.setOwner(user.getId());
                ni.setCreate_time(new Timestamp(new Date().getTime()));
                ni.setUpdate_time(new Timestamp(new Date().getTime()));
                noticeMapper.insert(ni);
            }
        }
        taskLogInstanceMapper.updateNoticeById(Const.TRUR,tli.getId());
    }


    public static void notice_event(){

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
            zdhDownloadMapper.updateByPrimaryKey(zdhDownloadInfo);
            NoticeInfo ni=new NoticeInfo();
            ni.setMsg_type("文件下载");
            ni.setMsg_title("文件下载:"+zdhDownloadInfo.getJob_context());
            ni.setMsg("文件以生成,请尽快下载, 文件由任务【"+zdhDownloadInfo.getJob_context()+"】于"+zdhDownloadInfo.getCreate_time()
                    +"产生, 文件唯一码: "+zdhDownloadInfo.getId()+", 请前往系统=>下载管理页面进行下载,时间:"+DateUtil.getCurrentTime());
            ni.setIs_see(Const.FALSE);
            ni.setOwner(zdhDownloadInfo.getOwner());
            ni.setCreate_time(new Timestamp(new Date().getTime()));
            ni.setUpdate_time(new Timestamp(new Date().getTime()));
            noticeMapper.insert(ni);

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

    }

    public static void apply_notice(){
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
                applyMapper.updateByPrimaryKey(applyInfo);
                NoticeInfo ni=new NoticeInfo();
                ni.setMsg_type("审批");
                ni.setMsg_title("审批通知:"+applyInfo.getApply_context());
                ni.setMsg("你有一个审批单,需要处理,单号: "+applyInfo.getId()+",申请原因:"+ applyInfo.getReason()+",时间:"+DateUtil.getCurrentTime());
                ni.setIs_see(Const.FALSE);
                ni.setOwner(applyInfo.getOwner());
                ni.setCreate_time(new Timestamp(new Date().getTime()));
                ni.setUpdate_time(new Timestamp(new Date().getTime()));
                noticeMapper.insert(ni);

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
    }

    public static void send_notice(String owner, String title, String msg){
        try{
            NoticeMapper noticeMapper = (NoticeMapper) SpringContext.getBean("noticeMapper");
            NoticeInfo ni=new NoticeInfo();
            ni.setMsg_type("告警");
            ni.setMsg_title(title);
            ni.setMsg_url("");
            ni.setMsg(msg);
            ni.setIs_see(Const.FALSE);
            ni.setOwner(owner);
            ni.setCreate_time(new Timestamp(new Date().getTime()));
            ni.setUpdate_time(new Timestamp(new Date().getTime()));
            noticeMapper.insert(ni);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常:"+e.getMessage()+", 异常详情:{}";
            logger.error(error, e);
            logger.error("接口无权限告警异常",e.getCause());
        }
    }
}
