package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.config.DateConverter;
import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.dao.ZdhNginxMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.JobModel;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.shiro.RedisUtil;
import org.apache.shiro.SecurityUtils;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyEditorSupport;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class SystemController extends BaseController{

    @Autowired
    ZdhNginxMapper zdhNginxMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    QuartzJobMapper quartzJobMapper;
    @Autowired
    QuartzManager2 quartzManager2;
    @Autowired
    Environment ev;

    @RequestMapping(value = "/{url}", method = RequestMethod.GET)
    public String dynApiDemo2(@PathVariable("url") String url) {
        System.out.println(url);
        return "etl/" + url;
    }

    @RequestMapping(value = "/cron", method = RequestMethod.GET)
    public String cron() {

        return "cron/cron";
    }

    @RequestMapping(value = "/file_manager", method = RequestMethod.GET)
    public String file_manager() {

        return "file_manager";
    }

    @RequestMapping("/getFileManager")
    @ResponseBody
    public ZdhNginx getFileManager() {
        ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(getUser().getId());
        return zdhNginx;
    }

    @RequestMapping(value = "/file_manager_up", method = RequestMethod.POST)
    @ResponseBody
    public String file_manager_up(ZdhNginx zdhNginx) {

        ZdhNginx zdhNginx1 = zdhNginxMapper.selectByOwner(getUser().getId());
        zdhNginx.setOwner(getUser().getId());
        if (zdhNginx.getPort().equals("")) {
            zdhNginx.setPort("22");
        }
        if (zdhNginx1 != null) {
            zdhNginx.setId(zdhNginx1.getId());
            zdhNginxMapper.updateByPrimaryKey(zdhNginx);
        } else {
            zdhNginxMapper.insert(zdhNginx);
        }

        JSONObject json = new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping(value = "/notice_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String notice() {
        //System.out.println("加载缓存中通知事件");
        List<NoticeInfo> noticeInfos = new ArrayList<>();
        if (!redisUtil.exists("zdhdownloadinfos_" + getUser().getId())) {
            return JSON.toJSONString(noticeInfos);
        }
        String json = redisUtil.get("zdhdownloadinfos_" + getUser().getId()).toString();
        if (json != null && !json.equals("")) {
            List<ZdhDownloadInfo> cache = JSON.parseArray(json, ZdhDownloadInfo.class);
            Iterator<ZdhDownloadInfo> iterator = cache.iterator();
            while (iterator.hasNext()) {
                ZdhDownloadInfo zdhDownloadInfo = iterator.next();
                if (zdhDownloadInfo.getOwner().equals(getUser().getId())) {
                    NoticeInfo noticeInfo = new NoticeInfo();
                    noticeInfo.setMsg_type("文件下载");
                    int last_index = zdhDownloadInfo.getFile_name().lastIndexOf("/");
                    noticeInfo.setMsg_title(zdhDownloadInfo.getFile_name().substring(last_index + 1) + "完成下载");
                    noticeInfo.setMsg_url("download_index");
                    noticeInfos.add(noticeInfo);
                }
            }
        }

        return JSON.toJSONString(noticeInfos);
    }

    @RequestMapping(value = "/del_system_job", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String del_system_job(){

        JSONObject js=new JSONObject();
        if(!getUser().getUserName().equalsIgnoreCase("admin")){
            js.put("data","只有admin用户才能做此操作");
            return js.toJSONString();
        }
        //1 获取所有的email,retry 任务
        String sql="delete from QRTZ_SIMPLE_TRIGGERS where TRIGGER_GROUP in ('email','retry','check')";
        String sql2="delete from QRTZ_TRIGGERS where TRIGGER_GROUP in ('email','retry','check')";
        String sql3="delete from QRTZ_JOB_DETAILS where  JOB_GROUP in ('email','retry','check')";
        jdbcTemplate.execute(sql);
        jdbcTemplate.execute(sql2);
        jdbcTemplate.execute(sql3);

        quartzJobMapper.deleteSystemJob();
        //2 重新添加到调度队列

        String expr = ev.getProperty("email.schedule.interval");
        QuartzJobInfo quartzJobInfo = quartzManager2.createQuartzJobInfo("EMAIL", JobModel.REPEAT.getValue(), new Date(), new Date(), "", expr, "-1", "", "email");
        quartzJobInfo.setJob_id(SnowflakeIdWorker.getInstance().nextId() + "");
        quartzManager2.addQuartzJobInfo(quartzJobInfo);
        String expr2 = ev.getProperty("retry.schedule.interval");
        QuartzJobInfo quartzJobInfo2 = quartzManager2.createQuartzJobInfo("RETRY", JobModel.REPEAT.getValue(), new Date(), new Date(), "", expr2, "-1", "", "retry");
        quartzJobInfo2.setJob_id(SnowflakeIdWorker.getInstance().nextId() + "");
        quartzManager2.addQuartzJobInfo(quartzJobInfo2);
        String expr3 = "30s";
        QuartzJobInfo quartzJobInfo3 = quartzManager2.createQuartzJobInfo("CHECK", JobModel.REPEAT.getValue(), new Date(), new Date(), "", expr3, "-1", "", "retry");
        quartzJobInfo3.setJob_id(SnowflakeIdWorker.getInstance().nextId() + "");
        quartzManager2.addQuartzJobInfo(quartzJobInfo3);


        try {
            quartzManager2.addTaskToQuartz(quartzJobInfo);
            quartzManager2.addTaskToQuartz(quartzJobInfo2);
            quartzManager2.addTaskToQuartz(quartzJobInfo3);
        } catch (Exception e) {
            e.printStackTrace();
        }

        js.put("data","success");
        return js.toJSONString();

    }

    /**
     * 使用帮助
     * @return
     */
    @RequestMapping("/readme")
    public String read_me() {

        return "read_me";
    }


    public User getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

    @RequestMapping(value = "quartz-cron", method = RequestMethod.POST)
    @ResponseBody
    public String cronExpression2executeDates(String crontab) throws ParseException {
        System.out.println(crontab);
        List<String> resultList = new ArrayList<String>() ;
        CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
        cronTriggerImpl.setCronExpression(crontab);//这里写要准备猜测的cron表达式
        List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 10);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Date date : dates) {
            resultList.add(dateFormat.format(date));
        }
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("dateList",resultList);
        return jsonObject.toJSONString();
    }


}
