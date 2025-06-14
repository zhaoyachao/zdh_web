package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.dao.EveryDayNoticeMapper;
import com.zyc.zdh.dao.NoticeMapper;
import com.zyc.zdh.dao.ZdhNginxMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.ConfigUtil;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 系统服务
 */
@Controller
public class SystemController extends BaseController{

    @Autowired
    private ZdhNginxMapper zdhNginxMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private Environment ev;
    @Autowired
    private EveryDayNoticeMapper everyDayNoticeMapper;
    @Autowired
    private NoticeMapper noticeMapper;

//    @RequestMapping(value = "/{url}", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
//    public String dynApiDemo2(@PathVariable("url") String url) {
//        System.out.println(url);
//        return "404";
//    }

    /**
     * 获取平台名称
     * @return
     */
    @SentinelResource(value = "get_platform_name", blockHandler = "handleReturn")
    @RequestMapping(value = "/get_platform_name", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<LoginBaseInfo> get_platform_name() {
        LoginBaseInfo loginBaseInfo=new LoginBaseInfo();
        loginBaseInfo.setPlatform_name("ZDH数据平台");
        loginBaseInfo.setBackground_image("img/b7.jpeg");
        try{
            Object o = ConfigUtil.getParamUtil().getValue(ConfigUtil.getProductCode(), Const.ZDH_PLATFORM_NAME);
            if(o!=null){
                loginBaseInfo.setPlatform_name(o.toString());
            }
            Object o1 = ConfigUtil.getParamUtil().getValue(ConfigUtil.getProductCode(), Const.ZDH_BACKGROUND_IMAGE);
            if(o1!=null){
                loginBaseInfo.setBackground_image(o1.toString());
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", loginBaseInfo);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
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
    @SentinelResource(value = "getFileManager", blockHandler = "handleReturn")
    @RequestMapping(value = "/getFileManager", method = RequestMethod.POST , produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<ZdhNginx> getFileManager() throws Exception {
        try{
            ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(getOwner());
            return ReturnInfo.buildSuccess(zdhNginx);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError(e);
        }
    }

    /**
     * 文件服务器信息更新
     * @param zdhNginx
     * @return
     * @throws Exception
     */
    @SentinelResource(value = "file_manager_up", blockHandler = "handleReturn")
    @RequestMapping(value = "/file_manager_up", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<String> file_manager_up(ZdhNginx zdhNginx) throws Exception {
        try{
            ZdhNginx zdhNginx1 = zdhNginxMapper.selectByOwner(getOwner());
            zdhNginx.setOwner(getOwner());
            if (zdhNginx.getPort().equals("")) {
                zdhNginx.setPort("22");
            }
            if (zdhNginx1 != null) {
                zdhNginx.setId(zdhNginx1.getId());
                zdhNginxMapper.updateByPrimaryKeySelective(zdhNginx);
            } else {
                zdhNginxMapper.insertSelective(zdhNginx);
            }

            return ReturnInfo.buildSuccess("200");
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError(e);
        }
    }

    /**
     * 系统通知信息
     * @return
     * @throws Exception
     */
    @SentinelResource(value = "notice_list", blockHandler = "handleReturn")
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
    @SentinelResource(value = "del_system_job", blockHandler = "handleReturn")
    @RequestMapping(value = "/del_system_job", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Deprecated
    public ReturnInfo<String> del_system_job(){
        return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "当前功能已废弃,可使用调度管理=>调度器功能代替", "当前功能已废弃");
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
    @SentinelResource(value = "notice_message", blockHandler = "handleReturn")
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
                ni.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                noticeMapper.updateByPrimaryKeySelective(ni);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", ni);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
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
    @SentinelResource(value = "notice_list2", blockHandler = "handleReturn")
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

            if(getUser()!=null && !getUser().getRoles().contains(Const.SUPER_ADMIN_ROLE)){
                //超级管理员有所有权限
                Example.Criteria criteria2=example.createCriteria();
                criteria2.andEqualTo("owner", getOwner());
                example.and(criteria2);
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
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 删除通知信息
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "notice_delete", blockHandler = "handleReturn")
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
            LogUtil.error(this.getClass(), e);
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
    @SentinelResource(value = "notice_update_see", blockHandler = "handleReturn")
    @RequestMapping(value = "/notice_update_see", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> notice_update_see(String[] ids, String is_see) {
        try{
            noticeMapper.updateIsSeeByIds(ids, is_see);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
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


    /**
     * 解析quartz表达式
     * @param crontab
     * @return
     * @throws ParseException
     */
    @SentinelResource(value = "quartz-cron", blockHandler = "handleReturn")
    @RequestMapping(value = "quartz-cron", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<String>> cronExpression2executeDates(String crontab) throws ParseException {
        try{
            System.out.println(crontab);
            List<String> resultList = new ArrayList<String>() ;
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
            cronTriggerImpl.setCronExpression(crontab);//这里写要准备猜测的cron表达式
            List<Date> dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 10);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Date date : dates) {
                resultList.add(dateFormat.format(date));
            }
            return ReturnInfo.buildSuccess(resultList);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError(e);
        }
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
    @SentinelResource(value = "every_day_notice", blockHandler = "handleReturn")
    @RequestMapping(value = "/every_day_notice", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<EveryDayNotice> every_day_notice(String product_code) {

        try{
            if(StringUtils.isEmpty(product_code)){
                product_code = getProductCode();
            }

            EveryDayNotice everyDayNotice=new EveryDayNotice();
            everyDayNotice.setIs_delete(Const.NOT_DELETE);
            everyDayNotice.setProduct_code(product_code);
            List<EveryDayNotice> list=everyDayNoticeMapper.select(everyDayNotice);
            if(list != null && list.size()>0){
                return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "", list.get(0));
            }
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "暂无通知", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询通知失败", e);
        }
    }


    /**
     * 更新系统登录通知
     * @param product_code 产品code
     * @param msg 通知内容
     * @param show_type 1弹框,2文字,3不展示
     * @return
     */
    @SentinelResource(value = "notice_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/notice_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<String> notice_update(String product_code, String msg, String show_type) {
        try{
            checkPermissionByOwner(product_code);
            EveryDayNotice everyDayNotice=new EveryDayNotice();
            everyDayNotice.setIs_delete(Const.DELETE);
            Example example=new Example(everyDayNotice.getClass());
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            everyDayNoticeMapper.updateByExampleSelective(everyDayNotice, example);
            everyDayNotice.setMsg(msg);
            everyDayNotice.setIs_delete(Const.NOT_DELETE);
            everyDayNotice.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            everyDayNotice.setShow_type(show_type);
            everyDayNotice.setProduct_code(product_code);
            everyDayNoticeMapper.insertSelective(everyDayNotice);
            return ReturnInfo.buildSuccess("");
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError(e);
        }
    }


    /**
     * 获取当前系统版本
     * @return
     */
    @SentinelResource(value = "version", blockHandler = "handleReturn")
    @RequestMapping(value = "/version", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<String> version() {
        String version= ConfigUtil.getValue(ConfigUtil.VERSION,"");
        return  ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", "当前版本:"+version);
    }

}
