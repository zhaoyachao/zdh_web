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
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 系统服务
 */
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

//    @RequestMapping(value = "/{url}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
//    public String dynApiDemo2(@PathVariable("url") String url) {
//        System.out.println(url);
//        return "404";
//    }

    /**
     * 获取平台名称
     * @return
     */
    @RequestMapping(value = "/get_platform_name", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<LoginBaseInfo> get_platform_name() {
        LoginBaseInfo loginBaseInfo=new LoginBaseInfo();
        loginBaseInfo.setPlatform_name("ZDH数据平台");
        loginBaseInfo.setBackground_image("img/b7.jpeg");
        try{
            Object o = redisUtil.get(Const.ZDH_PLATFORM_NAME);
            if(o!=null){
                loginBaseInfo.setPlatform_name(o.toString());
            }
            Object o1 = redisUtil.get(Const.ZDH_BACKGROUND_IMAGE);
            if(o1!=null){
                loginBaseInfo.setBackground_image(o1.toString());
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", loginBaseInfo);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "异常,返回默认配置", loginBaseInfo);
        }

    }


    /**
     * 帮助页
     * @return
     */
    @RequestMapping(value = "/zdh_help", method = RequestMethod.GET)
    public String zdh_help() {

        return "etl/zdh_help";
    }

    /**
     * quartz表达式页面
     * @return
     */
    @RequestMapping(value = "/cron", method = RequestMethod.GET)
    public String cron() {

        return "cron/cron";
    }

    /**
     * 文件服务器设置页面
     * @return
     */
    @RequestMapping(value = "/file_manager", method = RequestMethod.GET)
    public String file_manager() {

        return "file_manager";
    }

    /**
     * 获取文件服务器
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getFileManager", method = RequestMethod.POST )
    @ResponseBody
    public ZdhNginx getFileManager() throws Exception {
        ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(getOwner());
        return zdhNginx;
    }

    /**
     * 文件服务器信息更新
     * @param zdhNginx
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/file_manager_up", method = RequestMethod.POST)
    @ResponseBody
    public String file_manager_up(ZdhNginx zdhNginx) throws Exception {

        ZdhNginx zdhNginx1 = zdhNginxMapper.selectByOwner(getOwner());
        zdhNginx.setOwner(getOwner());
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

    /**
     * 系统通知信息
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/notice_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Map<String,Object>> notice() throws Exception {
        //System.out.println("加载缓存中通知事件");
        List<NoticeInfo> noticeInfos = new ArrayList<>();

        NoticeInfo ni=new NoticeInfo();
        ni.setIs_see("false");
        ni.setOwner(getOwner());
        noticeInfos=noticeMapper.select(ni);


        Map map=new HashMap<String,Object>();
        map.put("total_num", noticeInfos.size());
        if(noticeInfos.size()>=10){
            map.put("notices", noticeInfos.subList(0,10));
        }else{
            map.put("notices", noticeInfos);
        }


        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"查询完成", map);
    }

    /**
     * 删除系统任务
     * @return
     */
    @RequestMapping(value = "/del_system_job", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Deprecated
    public ReturnInfo<String> del_system_job(){

        return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "当前功能已废弃,可使用调度管理=>调度器功能代替", "当前功能已废弃");
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

    /**
     * 通知信息详情页面
     * @return
     */
    @RequestMapping(value = "/notice_detail_index", method = RequestMethod.GET)
    public String notice_detail_index() {

        return "admin/notice_detail_index";
    }

    /**
     * 通知信息明细
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/notice_message", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<NoticeInfo> notice_message(String id) {
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
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", ni);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }

    /**
     * 通知首页
     * @return
     */
    @RequestMapping(value = "/notice_index", method = RequestMethod.GET)
    public String notice_index() {

        return "admin/notice_index";
    }

    /**
     * 通知信息列表
     * @param message 关键字
     * @param limit 分页大小
     * @param offset 分页开始下标
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/notice_list2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<NoticeInfo>>> notice2(String message, int limit, int offset) throws Exception {
        //System.out.println("加载缓存中通知事件");
        try{
            NoticeInfo ni = new NoticeInfo();
            Example example = new Example(NoticeInfo.class);
            List<NoticeInfo> noticeInfos = new ArrayList<>();
            Example.Criteria cri = example.createCriteria();
            //cri.andBetween("create_time", start_time, end_time);
            if (!StringUtils.isEmpty(message)) {
                cri.andLike("msg_type", getLikeCondition(message));
                cri.orLike("msg_title", getLikeCondition(message));
                cri.orLike("msg", getLikeCondition(message));
            }
            example.setOrderByClause("update_time desc");
            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = noticeMapper.selectCountByExample(example);

            noticeInfos = noticeMapper.selectByExampleAndRowBounds(example, rowBounds);

            PageResult<List<NoticeInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(noticeInfos);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pageResult);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 删除通知信息
     * @param ids id数组
     * @return
     */
    @RequestMapping(value = "/notice_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> notice_delete(String[] ids) {
        try{
            for (String id :ids){
                noticeMapper.deleteByPrimaryKey(id);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }

    /**
     * 通知信息标记已读
     * @param ids id数组
     * @param is_see 是否已读 true/false
     * @return
     */
    @RequestMapping(value = "/notice_update_see", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> notice_update_see(String[] ids, String is_see) {
        try{
            noticeMapper.updateIsSeeByIds(ids, is_see);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
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

    /**
     * 解析quartz表达式
     * @param crontab
     * @return
     * @throws ParseException
     */
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


    /**
     * 系统登录通知页面
     * @return
     */
    @RequestMapping("/notice_update_index")
    public String notice_update_index() {

        return "admin/notice_update_index";
    }

    /**
     * 系统登录通知
     * @return
     */
    @RequestMapping(value = "/every_day_notice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<EveryDayNotice> every_day_notice() {

        try{
            EveryDayNotice everyDayNotice=new EveryDayNotice();
            everyDayNotice.setIs_delete(Const.NOT_DELETE);
            List<EveryDayNotice> list=everyDayNoticeMapper.select(everyDayNotice);
            if(list != null && list.size()>0){
                return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "", list.get(0));
            }

            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "暂无通知", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询通知失败", e);
        }
    }


    /**
     * 更新系统登录通知
     * @param msg 通知内容
     * @param show_type 1弹框,2文字,3不展示
     * @return
     */
    @RequestMapping(value = "/notice_update", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String notice_update(String msg, String show_type) {

        EveryDayNotice everyDayNotice=new EveryDayNotice();
        everyDayNotice.setIs_delete(Const.DELETE);
        Example example=new Example(everyDayNotice.getClass());
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("is_delete", Const.NOT_DELETE);
        everyDayNoticeMapper.updateByExampleSelective(everyDayNotice, example);
        everyDayNotice.setMsg(msg);
        everyDayNotice.setIs_delete(Const.NOT_DELETE);
        everyDayNotice.setId(SnowflakeIdWorker.getInstance().nextId()+"");
        everyDayNotice.setShow_type(show_type);
        everyDayNoticeMapper.insert(everyDayNotice);

        JSONObject json=new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }


    /**
     * 获取当前系统版本
     * @return
     */
    @RequestMapping(value = "/version", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<String> version() {
        String version=ev.getProperty("version","");
        return  ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", "当前版本:"+version);
    }

}
