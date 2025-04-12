package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.ServerTaskInstanceMappeer;
import com.zyc.zdh.dao.ServerTaskMappeer;
import com.zyc.zdh.dao.ZdhHaInfoMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SetUpJob;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.LogUtil;
import com.zyc.zdh.util.MapStructMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 升级扩容服务
 */
@Controller
public class NodeController extends BaseController{

    @Autowired
    private ZdhHaInfoMapper zdhHaInfoMapper;
    @Autowired
    private ServerTaskMappeer serverTaskMappeer;
    @Autowired
    private ServerTaskInstanceMappeer serverTaskInstanceMappeer;

    /**
     * server构建首页
     * @return
     */
    @RequestMapping(value = "/server_manager_index", method = RequestMethod.GET)
    public String server_manager_index() {

        return "admin/server_manager_index";
    }

    /**
     * server构建列表
     * @param id id,非必填
     * @param context 关键字
     * @return
     */
    @RequestMapping(value = "/server_manager_list", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String server_manager_list(String id,String context) {
        if(!StringUtils.isEmpty(context)){
            context = getLikeCondition(context);
        }
        List<ServerTaskInfo> serverTaskInfos = serverTaskMappeer.selectServer(id,context);

        return JsonUtil.formatJsonString(serverTaskInfos);

    }

    /**
     * server服务列表
     * @param online 0下线/1在线
     * @param context 关键字
     * @return
     */
    @RequestMapping(value = "/server_manager_online_list", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String server_manager_online_list(String online,String context) {
        if(!StringUtils.isEmpty(context)){
            context = getLikeCondition(context);
        }
        List<ZdhHaInfo> zdhHaInfos = zdhHaInfoMapper.selectServer(online,context);

        return JsonUtil.formatJsonString(zdhHaInfos);

    }

    /**
     * server服务更新上下线
     * @param id 主键ID
     * @param online 0:逻辑下线,1:逻辑上线,2:物理下线
     * @return
     */
    @SentinelResource(value = "server_manager_online_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/server_manager_online_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo server_manager_online_update(String id,String online) {
        try{
            zdhHaInfoMapper.updateOnline(online,id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * server新增模板首页
     * @return
     */
    @RequestMapping(value = "/server_add_index", method = RequestMethod.GET)
    public String server_add_index() {

        return "admin/server_add_index";
    }

    /**
     * server新增
     * @param serverTaskInfo
     * @return
     */
    @SentinelResource(value = "server_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/server_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo server_add(ServerTaskInfo serverTaskInfo) {
        try{
            serverTaskInfo.setOwner(getOwner());
            serverTaskInfo.setCreate_time(new Date());
            serverTaskInfo.setUpdate_time(new Date());
            serverTaskMappeer.insertSelective(serverTaskInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }

    }

    /**
     * server模板更新
     * @param serverTaskInfo
     * @return
     */
    @SentinelResource(value = "server_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/server_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo server_update(ServerTaskInfo serverTaskInfo) {
        try{
            serverTaskInfo.setOwner(getOwner());
            serverTaskInfo.setCreate_time(new Date());
            serverTaskInfo.setUpdate_time(new Date());
            serverTaskMappeer.updateByPrimaryKeySelective(serverTaskInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
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
    @SentinelResource(value = "server_setup", blockHandler = "handleReturn")
    @RequestMapping(value = "/server_setup", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo server_setup(String id,String build_branch){

        //第一步：登陆构建服务器
        //第二步：拉取git
        //第三步：执行构建命令
        //第四步：scp 远程服务器
        ServerTaskInfo serverTaskInfo=serverTaskMappeer.selectByPrimaryKey(id);
        ServerTaskInstance sti=new ServerTaskInstance();
        String id2=SnowflakeIdWorker.getInstance().nextId()+"";
        try {
            //BeanUtils.copyProperties(sti, serverTaskInfo);
            sti = MapStructMapper.INSTANCE.serverTaskInfoToServerTaskInstance(serverTaskInfo);
            sti.setVersion_type("branch");
            sti.setVersion(DateUtil.formatNodash(new Date()));
            sti.setId(id2);
            sti.setTemplete_id(serverTaskInfo.getId().toString());
            sti.setStatus("0");
            sti.setCreate_time(new Timestamp(System.currentTimeMillis()));
            sti.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            if(StringUtils.isEmpty(sti.getBuild_branch())){
                sti.setBuild_branch("master");
            }
            if(!StringUtils.isEmpty(build_branch)){
                sti.setBuild_branch(build_branch);
            }

            serverTaskInstanceMappeer.insertSelective(sti);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    ServerTaskInstance sti2=serverTaskInstanceMappeer.selectByPrimaryKey(id2);
                    SetUpJob.run(sti2);
                }
            }).start();

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"一键部署失败", e);
        }

        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"一键部署任务已生成", sti);

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

        return JsonUtil.formatJsonString(serverTaskInstances);
    }


    @SentinelResource(value = "server_logs_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/server_logs_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo server_logs_delete(String[] ids){

        try{
            serverTaskInstanceMappeer.deleteById(ids);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }

    }

}
