package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SetUpJob;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.DateUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Controller
public class NodeController extends BaseController{
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
    ZdhHaInfoMapper zdhHaInfoMapper;
    @Autowired
    ServerTaskMappeer serverTaskMappeer;
    @Autowired
    ServerTaskInstanceMappeer serverTaskInstanceMappeer;
    @Autowired
    Environment ev;



    @RequestMapping(value = "/server_manager_index", method = RequestMethod.GET)
    public String permission_index() {

        return "admin/server_manager_index";
    }

    @RequestMapping(value = "/server_manager_list", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String server_manager_list(String id,String context) {
        if(!StringUtils.isEmpty(context)){
            context = getLikeCondition(context);
        }
        List<ServerTaskInfo> serverTaskInfos = serverTaskMappeer.selectServer(id,context);

        return JSONObject.toJSONString(serverTaskInfos);

    }

    @RequestMapping(value = "/server_manager_online_list", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String server_manager_online_list(String online,String context) {
        if(!StringUtils.isEmpty(context)){
            context = getLikeCondition(context);
        }
        List<ZdhHaInfo> zdhHaInfos = zdhHaInfoMapper.selectServer(online,context);

        return JSONObject.toJSONString(zdhHaInfos);

    }

    @RequestMapping(value = "/server_manager_online_update", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String server_manager_online_update(String id,String online) {
        try{
            zdhHaInfoMapper.updateOnline(online,id);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    @RequestMapping(value = "/server_add_index", method = RequestMethod.GET)
    public String server_add_index() {

        return "admin/server_add_index";
    }

    @RequestMapping(value = "/server_add", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String server_add(ServerTaskInfo serverTaskInfo) {
        try{
            serverTaskInfo.setOwner(getUser().getId());
            serverTaskInfo.setCreate_time(new Date());
            serverTaskInfo.setUpdate_time(new Date());
            serverTaskMappeer.insert(serverTaskInfo);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }

    }

    @RequestMapping(value = "/server_update", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String server_update(ServerTaskInfo serverTaskInfo) {
        try{
            serverTaskInfo.setOwner(getUser().getId());
            serverTaskInfo.setCreate_time(new Date());
            serverTaskInfo.setUpdate_time(new Date());
            serverTaskMappeer.updateByPrimaryKey(serverTaskInfo);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }

    }

    /**
     * 手动构建配置
     * @return
     */
    @RequestMapping(value = "/server_build_exe_detail_index", method = RequestMethod.GET)
    public String server_build_exe_detail_index() {

        return "admin/server_build_exe_detail_index";
    }

    /**
     * server 一键部署
     */
    @RequestMapping(value = "/server_setup", method = RequestMethod.GET)
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String server_setup(String id,String build_branch){

        //第一步：登陆构建服务器
        //第二步：拉取git
        //第三步：执行构建命令
        //第四步：scp 远程服务器
        ServerTaskInfo serverTaskInfo=serverTaskMappeer.selectByPrimaryKey(id);
        ServerTaskInstance sti=new ServerTaskInstance();
        String id2=SnowflakeIdWorker.getInstance().nextId()+"";
        try {
            BeanUtils.copyProperties(sti, serverTaskInfo);
            sti.setVersion_type("branch");
            sti.setVersion(DateUtil.formatNodash(new Date()));
            sti.setId(id2);
            sti.setTemplete_id(serverTaskInfo.getId().toString());
            sti.setStatus("0");
            sti.setCreate_time(new Timestamp(new Date().getTime()));
            sti.setUpdate_time(new Timestamp(new Date().getTime()));
            if(StringUtils.isEmpty(sti.getBuild_branch())){
                sti.setBuild_branch("master");
            }
            if(!StringUtils.isEmpty(build_branch)){
                sti.setBuild_branch(build_branch);
            }

            serverTaskInstanceMappeer.insert(sti);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    ServerTaskInstance sti2=serverTaskInstanceMappeer.selectByPrimaryKey(id2);
                    SetUpJob.run(sti2);
                }
            }).start();

        } catch (IllegalAccessException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"一键部署失败", e);
        } catch (InvocationTargetException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"一键部署失败", e);
        }

        return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"一键部署任务已生成", sti);

    }

    /**
     * 获取server部署记录
     * @return
     */
    @RequestMapping(value = "/server_log_instance", method = RequestMethod.GET)
    public String server_log_instance(){

        return "admin/server_log_instance";
    }

    /**
     * 获取server部署记录
     * @param templete_id
     * @return
     */
    @RequestMapping(value = "/build_server_log", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String build_server_log(String templete_id){

        List<ServerTaskInstance> serverTaskInstances = serverTaskInstanceMappeer.selectByTempleteId(templete_id);

        return JSON.toJSONString(serverTaskInstances);
    }


    @RequestMapping(value = "/server_logs_delete", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String server_logs_delete(String[] ids){

        try{
            serverTaskInstanceMappeer.deleteById(ids);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }

    }

    public User getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }


    private void debugInfo(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            // 对于每个属性，获取属性名
            String varName = fields[i].getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o;
                try {
                    o = fields[i].get(obj);
                    System.err.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                     logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            }
        }
    }



}
