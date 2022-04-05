package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.Const;
import org.apache.shiro.SecurityUtils;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class SystemController extends BaseController{

    public Logger logger= LoggerFactory.getLogger(this.getClass());
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
    @Autowired
    EveryDayNoticeMapper everyDayNoticeMapper;
    @Autowired
    ResourceTreeMapper resourceTreeMapper;
    @Autowired
    NoticeMapper noticeMapper;

    @RequestMapping(value = "/{url}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    public String dynApiDemo2(@PathVariable("url") String url) {
        System.out.println(url);
        return "404";
    }

    @RequestMapping(value = "/get_platform_name", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @White
    public String get_platform_name() {
        String platform_name = ev.getProperty("platform_name", "ZDH数据平台");
        return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "查询成功", platform_name);
    }


    @RequestMapping(value = "/zdh_help", method = RequestMethod.GET)
    public String zdh_help() {

        return "etl/zdh_help";
    }

    @RequestMapping(value = "/cron", method = RequestMethod.GET)
    public String cron() {

        return "cron/cron";
    }

    @RequestMapping(value = "/file_manager", method = RequestMethod.GET)
    public String file_manager() {

        return "file_manager";
    }

    @RequestMapping(value = "/getFileManager", method = RequestMethod.POST )
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

    @RequestMapping(value = "/notice_list", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String notice() {
        //System.out.println("加载缓存中通知事件");
        List<NoticeInfo> noticeInfos = new ArrayList<>();

        NoticeInfo ni=new NoticeInfo();
        ni.setIs_see("false");
        ni.setOwner(getUser().getId());
        noticeInfos=noticeMapper.select(ni);


        Map map=new HashMap<String,Object>();
        map.put("total_num", noticeInfos.size());
        if(noticeInfos.size()>=10){
            map.put("notices", noticeInfos.subList(0,10));
        }else{
            map.put("notices", noticeInfos);
        }


        return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"查询完成", map);

        //return JSON.toJSONString(noticeInfos);


//        if (!redisUtil.exists("zdhdownloadinfos_" + getUser().getId()) && !redisUtil.exists("zdhapplyinfos_" + getUser().getId())) {
//            //return JSON.toJSONString(noticeInfos);
//        }
//        if(redisUtil.exists("zdhdownloadinfos_" + getUser().getId())){
//            String json = redisUtil.get("zdhdownloadinfos_" + getUser().getId()).toString();
//            if (json != null && !json.equals("")) {
//                List<ZdhDownloadInfo> cache = JSON.parseArray(json, ZdhDownloadInfo.class);
//                Iterator<ZdhDownloadInfo> iterator = cache.iterator();
//                while (iterator.hasNext()) {
//                    ZdhDownloadInfo zdhDownloadInfo = iterator.next();
//                    if (zdhDownloadInfo.getOwner().equals(getUser().getId())) {
//                        NoticeInfo noticeInfo = new NoticeInfo();
//                        noticeInfo.setMsg_type("文件下载");
//                        int last_index = zdhDownloadInfo.getFile_name().lastIndexOf("/");
//                        noticeInfo.setMsg_title(zdhDownloadInfo.getFile_name().substring(last_index + 1) + "完成下载");
//                        noticeInfo.setMsg_url("download_index");
//                        noticeInfos.add(noticeInfo);
//                    }
//                }
//            }
//        }
//
//        if(redisUtil.exists("zdhapplyinfos_" + getUser().getId())){
//            String json2 = redisUtil.get("zdhapplyinfos_" + getUser().getId()).toString();
//            if (json2 != null && !json2.equals("")) {
//                List<ApplyInfo> cache = JSON.parseArray(json2, ApplyInfo.class);
//                Iterator<ApplyInfo> iterator = cache.iterator();
//                while (iterator.hasNext()) {
//                    ApplyInfo applyInfo = iterator.next();
//                    NoticeInfo noticeInfo = new NoticeInfo();
//                    noticeInfo.setMsg_type("审批");
//                    noticeInfo.setMsg_title(applyInfo.getApply_context()+"_数据审批单");
//                    noticeInfo.setMsg_url("data_approve_index");
//                    noticeInfos.add(noticeInfo);
//                }
//            }
//        }

       // return JSON.toJSONString(noticeInfos);
    }

    @RequestMapping(value = "/del_system_job", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Deprecated
    public String del_system_job(){

        return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "当前功能已废弃,可使用调度管理=>调度器功能代替", "当前功能已废弃");
//        JSONObject js=new JSONObject();
//        if(!SecurityUtils.getSubject().isPermitted("function:del_system_job()")){
//            js.put("data","您没有权限访问,请联系管理员添加权限");
//            return js.toJSONString();
//        }
//        //1 获取所有的email,retry 任务
//        String sql="delete from QRTZ_SIMPLE_TRIGGERS where TRIGGER_GROUP in ('email','retry','check')";
//        String sql2="delete from QRTZ_TRIGGERS where TRIGGER_GROUP in ('email','retry','check')";
//        String sql3="delete from QRTZ_JOB_DETAILS where  JOB_GROUP in ('email','retry','check')";
//        jdbcTemplate.execute(sql);
//        jdbcTemplate.execute(sql2);
//        jdbcTemplate.execute(sql3);
//
//        quartzJobMapper.deleteSystemJob();
//        //2 重新添加到调度队列
//
//        String expr = ev.getProperty("email.schedule.interval");
//        QuartzJobInfo quartzJobInfo = quartzManager2.createQuartzJobInfo("EMAIL", JobModel.REPEAT.getValue(), new Date(), new Date(), "检查告警任务", expr, "-1", "", "email");
//        quartzJobInfo.setJob_id(SnowflakeIdWorker.getInstance().nextId() + "");
//        quartzManager2.addQuartzJobInfo(quartzJobInfo);
//        String expr2 = ev.getProperty("retry.schedule.interval");
//        QuartzJobInfo quartzJobInfo2 = quartzManager2.createQuartzJobInfo("RETRY", JobModel.REPEAT.getValue(), new Date(), new Date(), "检查失败重试任务", expr2, "-1", "", "retry");
//        quartzJobInfo2.setJob_id(SnowflakeIdWorker.getInstance().nextId() + "");
//        quartzManager2.addQuartzJobInfo(quartzJobInfo2);
//        String expr3 = "5s";
//        QuartzJobInfo quartzJobInfo3 = quartzManager2.createQuartzJobInfo("CHECK", JobModel.REPEAT.getValue(), new Date(), new Date(), "检查依赖任务", expr3, "-1", "", "check");
//        quartzJobInfo3.setJob_id(SnowflakeIdWorker.getInstance().nextId() + "");
//        quartzManager2.addQuartzJobInfo(quartzJobInfo3);
//
//
//        try {
//            quartzManager2.addTaskToQuartz(quartzJobInfo);
//            quartzManager2.addTaskToQuartz(quartzJobInfo2);
//            quartzManager2.addTaskToQuartz(quartzJobInfo3);
//        } catch (Exception e) {
//            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
//            logger.error(error, e);
//        }
//
//        js.put("data","success");
//        return js.toJSONString();

    }

    @RequestMapping(value = "/notice_detail_index", method = RequestMethod.GET)
    public String notice_detail_index() {

        return "admin/notice_detail_index";
    }

    @RequestMapping(value = "/notice_message", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String notice_message(String id) {
        //System.out.println("加载缓存中通知事件");

        try{
            NoticeInfo ni=new NoticeInfo();
            ni.setId(id);
            ni = noticeMapper.selectOne(ni);
            if(ni!=null && ni.getIs_see().equalsIgnoreCase("false")){
                ni.setIs_see("true");
                ni.setUpdate_time(new Timestamp(new Date().getTime()));
                noticeMapper.updateByPrimaryKey(ni);
            }
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "查询成功", ni);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }

    @RequestMapping(value = "/notice_index", method = RequestMethod.GET)
    public String notice_index() {

        return "admin/notice_index";
    }

    @RequestMapping(value = "/notice_list2", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String notice2(String message) {
        //System.out.println("加载缓存中通知事件");
        List<NoticeInfo> noticeInfos = new ArrayList<>();

        noticeInfos = noticeMapper.selectByMessage(message, getUser().getId());

        return JSON.toJSONString(noticeInfos);
    }

    @RequestMapping(value = "/notice_delete", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String notice_delete(String[] ids) {
        try{
            for (String id :ids){
                noticeMapper.deleteByPrimaryKey(id);
            }
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }

    @RequestMapping(value = "/notice_update_see", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String notice_update_see(String[] ids, String is_see) {
        try{
            noticeMapper.updateIsSeeByIds(ids, is_see);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
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


    @RequestMapping("/notice_update_index")
    public String notice_update_index() {

        return "admin/notice_update_index";
    }

    @RequestMapping(value = "/every_day_notice", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String every_day_notice() {

        try{
            EveryDayNotice everyDayNotice=new EveryDayNotice();
            everyDayNotice.setIs_delete(Const.NOT_DELETE);
            List<EveryDayNotice> list=everyDayNoticeMapper.select(everyDayNotice);
            if(list != null && list.size()>0){
                return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "", list.get(0));
            }

            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "暂无通知", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "查询通知失败", e.getMessage());
        }
    }

    @RequestMapping(value = "/notice_update", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String notice_update(String msg) {

        EveryDayNotice everyDayNotice=new EveryDayNotice();
        everyDayNotice.setIs_delete(Const.DELETE);
        Example example=new Example(everyDayNotice.getClass());
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("is_delete", Const.NOT_DELETE);
        everyDayNoticeMapper.updateByExampleSelective(everyDayNotice, example);
        everyDayNotice.setMsg(msg);
        everyDayNotice.setIs_delete(Const.NOT_DELETE);
        everyDayNotice.setId(SnowflakeIdWorker.getInstance().nextId()+"");
        everyDayNoticeMapper.insert(everyDayNotice);

        JSONObject json=new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping(value = "/version", method = RequestMethod.POST,produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String version() {
        String version=ev.getProperty("version","");
        return  ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "查询成功", "当前版本:"+version);
    }

}
