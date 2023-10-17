package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSON;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.EmailJob;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

/**
 * 审批服务
 */
@Controller
public class ZdhProcessFlowController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ProcessFlowMapper processFlowMapper;
    @Autowired
    private IssueDataMapper issueDataMapper;
    @Autowired
    private ApplyMapper applyMapper;
    @Autowired
    private ApprovalAuditorMapper approvalAuditorMapper;
    @Autowired
    private ApprovalEventMapper approvalEventMapper;
    @Autowired
    private PermissionApplyMapper permissionApplyMapper;
    @Autowired
    private PermissionMapper permissionMapper;
    @Resource
    private PermissionController permissionController;
    @Autowired
    private ProductTagMapper productTagMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private Environment ev;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 流程审批页面
     *
     * @return
     */
    @RequestMapping("/process_flow_index")
    public String process_flow_index() {

        return "etl/process_flow_index";
    }

    /**
     * 我的发起流程
     *
     * @return
     */
    @RequestMapping("/process_flow_index2")
    public String process_flow_index2() {

        return "etl/process_flow_index2";
    }

    /**
     * 审批列表
     * @param context 关键字
     * @return
     */
    @SentinelResource(value = "process_flow_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/process_flow_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<ProcessFlowInfo>> process_flow_list(String context) {
        try{
            List<String> product_codes = getPermissionByProduct(zdhPermissionService);
            if(!StringUtils.isEmpty(context)){
                context = getLikeCondition(context);
            }
            List<ProcessFlowInfo> pfis = processFlowMapper.selectByAuditorId(Const.SHOW, getUser().getUserName(), context, product_codes);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "获取流程列表", pfis);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "获取流程列表", e);
        }

    }

    /**
     * 我的发起流程列表
     * 根据流程表process_flow_info 按用户+flow_id去重
     * @param context 关键字
     * @return
     */
    @SentinelResource(value = "process_flow_list2", blockHandler = "handleReturn")
    @RequestMapping(value = "/process_flow_list2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<ProcessFlowInfo>> process_flow_list2(String context) {
        try{
            //String product_code = ev.getProperty("zdh.product", "zdh");
            List<String> product_codes = getPermissionByProduct(zdhPermissionService);
            if(!StringUtils.isEmpty(context)){
                context = getLikeCondition(context);
            }
            List<ProcessFlowInfo> pfis = processFlowMapper.selectByOwner(getUser().getUserName(), context, product_codes);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "获取流程列表", pfis);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "获取流程列表", e);
        }

    }

    /**
     * 审批
     * @param pfi
     * @return
     */
    @SentinelResource(value = "process_flow_status", blockHandler = "handleReturn")
    @RequestMapping(value = "/process_flow_status", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<ProcessFlowInfo> process_flow_status(ProcessFlowInfo pfi) {
        ProcessFlowInfo pfi_old = processFlowMapper.selectByPrimaryKey(pfi.getId());
        processFlowMapper.updateStatus(pfi.getId(), pfi.getStatus());
        pfi_old = processFlowMapper.selectByPrimaryKey(pfi.getId());
        if (pfi.getStatus().equalsIgnoreCase(Const.STATUS_SUCCESS)) {
            //设置下游流程可展示
            processFlowMapper.updateIsShow(pfi.getId(), Const.SHOW);
        }
        if (pfi.getStatus().equalsIgnoreCase(Const.STATUS_FAIL)) {
            //审批不通过,设置流程结束
            apply(pfi_old);
        }
        //最后一个审批,表示审批流程结束 todo 之后新增事件,可在此处处理
        if (pfi_old.getIs_end().equalsIgnoreCase(Const.END)) {
            apply(pfi_old);
        }

        //获取下游流程审批人-发送邮件通知,此处需要异步发送
        ProcessFlowInfo flowInfo=new ProcessFlowInfo();
        flowInfo.setPre_id(pfi.getId());
        List<ProcessFlowInfo> pfis=processFlowMapper.select(flowInfo);
        if(pfis!=null && pfis.size()>0){
            String url = redisUtil.get(Const.ZDH_SYSTEM_DNS, "http://127.0.0.1:8081/").toString();

            for (ProcessFlowInfo f:pfis){
                // 审批人 flowInfo1.getAuditor_id()
                // 申请人 flowInfo1.getOwner()
                String context=f.getContext();
                if(!StringUtils.isEmpty(f.getAuditor_id())){
                    String[] auditors = f.getAuditor_id().split(",");
                    for (String auditor: auditors){
                        EmailJob.send_notice(auditor, "审批通知", "你有一条审批待处理, 流程名: 【"+context+"】 请登录平台审批, "+url, "通知");
                    }
                }
                EmailJob.send_notice(f.getOwner(), "流程进度", "你的流程【"+context+"】 已到下一环节, 审批人: "+f.getAuditor_id(), "通知");


            }
        }

        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "审批", pfi);
    }

    /**
     * 数据审批触发事件
     * @param pfi
     */
    public void apply(ProcessFlowInfo pfi){

        if (pfi.getEvent_code().equalsIgnoreCase(EventCode.DATA_PUB.getCode())) {
            //更新发布表
            IssueDataInfo idi = issueDataMapper.selectByPrimaryKey(pfi.getEvent_id());
            idi.setStatus(Const.STATUS_NOT_PUB);//未发布
            if (pfi.getStatus().equalsIgnoreCase(Const.STATUS_SUCCESS)) {
                //审批通过
                idi.setStatus(Const.STATUS_PUB);//发布
            }
            issueDataMapper.updateByPrimaryKey(idi);
        }
        if (pfi.getEvent_code().equalsIgnoreCase(EventCode.DATA_APPLY.getCode())) {
            //更新申请表
            ApplyInfo ai = applyMapper.selectByPrimaryKey(pfi.getEvent_id());
            ai.setStatus(Const.STATUS_NOT_PUB);//未通过
            if (pfi.getStatus().equalsIgnoreCase(Const.STATUS_SUCCESS)) {
                //审批通过
                ai.setStatus(Const.STATUS_PUB);
            }
            applyMapper.updateByPrimaryKey(ai);
        }
        if (pfi.getEvent_code().equalsIgnoreCase(EventCode.PERMISSION_APPLY.getCode())) {
            //更新权限申请表
            PermissionApplyInfo pai = permissionApplyMapper.selectByPrimaryKey(pfi.getEvent_id());
            pai.setStatus(Const.PERMISSION_APPLY_FAIL);//未通过
            if (pfi.getStatus().equalsIgnoreCase(Const.STATUS_SUCCESS)) {
                //审批通过
                pai.setStatus(Const.PERMISSION_APPLY_SUCCESS);//通过
                permission_apply_event(pai);
            }
            permissionApplyMapper.updateByPrimaryKey(pai);
        }

        //此处增加回调机制(只有回调才能做到,万能审批流,调用方可以不关心审批流程,但是必须提供一个审批完成/失败后的回调接口(http))

        if(!StringUtils.isEmpty(pfi.getEvent_code())){
            ApprovalEventInfo approvalEventInfo=new ApprovalEventInfo();
            approvalEventInfo.setEvent_code(pfi.getEvent_code());
            approvalEventInfo = approvalEventMapper.selectOne(approvalEventInfo);
            if(!StringUtils.isEmpty(approvalEventInfo.getCall_back())){
                //发送http请求
                try {
                    String result = HttpUtil.postJSON(approvalEventInfo.getCall_back(), JSON.toJSONString(pfi));
                    logger.info(result);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 权限申请事件表,审批通过后,添加权限
     * @param pai
     */
    public void permission_apply_event(PermissionApplyInfo pai){

        PermissionUserInfo permissionUserInfo=new PermissionUserInfo();
        permissionUserInfo.setProduct_code(pai.getProduct_code());
        permissionUserInfo.setUser_account(pai.getOwner());//此处是账号,非id
        //获取旧信息
        PermissionUserInfo permissionUserInfo1 = permissionMapper.selectOne(permissionUserInfo);
        if(pai.getApply_type().equalsIgnoreCase("role")){
            String roles=pai.getApply_code();
            if(!StringUtils.isEmpty(permissionUserInfo1.getRoles())){
                roles=permissionUserInfo1.getRoles()+","+pai.getApply_code();
            }
            permissionUserInfo1.setRoles(roles);
        }else if(pai.getApply_type().equalsIgnoreCase("data_group")){
            String data_group=pai.getApply_code();
//            if(!StringUtils.isEmpty(permissionUserInfo1.getTag_group_code())){
//                data_group=permissionUserInfo1.getTag_group_code()+","+pai.getApply_code();
//            }
            permissionUserInfo1.setTag_group_code(data_group);
        }else if(pai.getApply_type().equalsIgnoreCase("user_group")){
            String user_group=pai.getApply_code();
//            if(!StringUtils.isEmpty(permissionUserInfo1.getUser_group())){
//                user_group=permissionUserInfo1.getUser_group()+","+pai.getApply_code();
//            }
            permissionUserInfo1.setTag_group_code(user_group);
        }else if(pai.getApply_type().equalsIgnoreCase("product_admin")){
            ProductTagInfo productTagInfo=new ProductTagInfo();
            productTagInfo.setProduct_code(pai.getProduct_code());
            productTagInfo.setIs_delete(Const.NOT_DELETE);
            productTagInfo = productTagMapper.selectOne(productTagInfo);
            if(StringUtils.isEmpty(productTagInfo.getProduct_admin())){
                productTagInfo.setProduct_admin(pai.getOwner());
            }else{
                productTagInfo.setProduct_admin(productTagInfo.getProduct_admin()+","+pai.getOwner());
            }
            productTagInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            productTagMapper.updateByPrimaryKey(productTagInfo);
        }else{

        }

        permissionMapper.updateByPrimaryKey(permissionUserInfo1);
    }


    /**
     * 撤销申请
     *
     * @param pfi
     * @return
     */
    @SentinelResource(value = "process_flow_status2", blockHandler = "handleReturn")
    @RequestMapping(value = "/process_flow_status2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<ProcessFlowInfo> process_flow_status2(ProcessFlowInfo pfi) {
        processFlowMapper.updateStatus2(pfi.getFlow_id(), pfi.getStatus());
        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "撤销", pfi);
    }


    /**
     * 流程审批进度
     *
     * @return
     */
    @RequestMapping("/process_flow_detail_index")
    public String process_flow_detail() {

        return "etl/process_flow_detail_index";
    }

    /**
     * 审批明细
     * @param flow_id 审批流程ID
     * @return
     */
    @SentinelResource(value = "process_flow_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/process_flow_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<ProcessFlowInfo>> process_flow_detail(String flow_id) {

        try {
            List<ProcessFlowInfo> pfis = processFlowMapper.selectByFlowId(flow_id, getUser().getUserName());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "获取流程进度", pfis);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "获取流程进度", e);
        }

    }

    /**
     * ZDH创建审批流统一入口
     * 在调用处处理异常
     * @param event_code
     * @param context
     * @param event_id
     */
    public void createProcess(String event_code, String context, String event_id) throws Exception{
        try{
            String group = getUser().getUser_group();
            String product_code = ev.getProperty("zdp.product", "zdh");
            //获取事件对应的审批流code
            List<ApprovalAuditorInfo> approvalAuditorInfos = approvalAuditorMapper.selectByEvent(event_code, group, product_code);

            List<ProcessFlowInfo> pfis = new ArrayList<>();
            //生成唯一标识,用于串联审批节点
            String uid = SnowflakeIdWorker.getInstance().nextId() + "";
            String pre_id = "";
            String auditors = "";
            //遍历层级生成上下游关系
            for (int i = 1; i < 10; i++) {
                ProcessFlowInfo pfi = new ProcessFlowInfo();
                String id = SnowflakeIdWorker.getInstance().nextId() + "";
                pfi.setId(id);
                pfi.setOwner(getUser().getUserName());
                pfi.setFlow_id(uid);
                pfi.setEvent_code(event_code);
                Set<String> sb = new TreeSet<>();
                for (ApprovalAuditorInfo aai : approvalAuditorInfos) {
                    if (aai.getLevel().equalsIgnoreCase(i + "")) {
                        pfi.setConfig_code(aai.getCode());
                        //根据规则获取审批人账号,审批人账号
                        List<String> auditorAccounts = getAuditorByRule(getUser().getUserName(), product_code, aai);
                        sb.addAll(auditorAccounts);
                        if(!StringUtils.isEmpty(aai.getAuditor_id())){
                            //如果存在指定审批人,则加入此审批人的账号
                            sb.add(aai.getAuditor_id());
                        }
                    }
                }
                if (StringUtils.isEmpty(pfi.getConfig_code()))
                    continue;

                String defaultUsers = getDefaultAuditor(product_code);
                if(!StringUtils.isEmpty(defaultUsers)){
                    sb.addAll(Arrays.asList(defaultUsers.split(",")));
                }
                //强制增加一个内部账号
                sb.add("admin");
                //多个审批人,逗号分割
                pfi.setAuditor_id(org.apache.commons.lang3.StringUtils.join(sb, ","));
                if (i == 1) {
                    pfi.setIs_show(Const.SHOW);
                    //发送审批通知
                    auditors = pfi.getAuditor_id();
                } else {
                    pfi.setIs_show(Const.NOT_SHOW);
                }
                pfi.setLevel(i + "");
                pfi.setCreate_time(new Timestamp(new Date().getTime()));
                pfi.setPre_id(pre_id);
                pfi.setContext(context);
                pfi.setIs_end(Const.NOT_END);
                pfi.setStatus(Const.STATUS_INIT);
                pfi.setEvent_id(event_id);
                pfi.setOther_handle(Const.PROCESS_OTHER_STATUS_INIT);
                pfi.setAgent_user("");
                processFlowMapper.insert(pfi);
                pre_id = id;
            }
            processFlowMapper.updateIsEnd(pre_id, Const.END);
            String url = redisUtil.get(Const.ZDH_SYSTEM_DNS, "http://127.0.0.1:8081/").toString();
            if(!StringUtils.isEmpty(auditors)){
                for (String auditor: auditors.split(",")){
                    EmailJob.send_notice(auditor, "审批通知", "你有一条审批待处理, 流程名: 【"+context+"】 请登录平台审批, "+url, "通知");
                }
            }
            EmailJob.send_notice(getUser().getUserName(), "流程进度", "你的流程【"+context+"】 已到下一环节, 审批人: "+auditors, "通知");

        }catch (Exception e){
            throw e;
        }
    }

    /**
     * 根据审批规则动态获取审批人
     * @param approvalAuditorInfo
     * @return
     */
    public List<String> getAuditorByRule(String userName,String product_code,ApprovalAuditorInfo approvalAuditorInfo){
        String auditorRule = approvalAuditorInfo.getAuditor_rule();
        if(!StringUtils.isEmpty(auditorRule)){
            if(auditorRule.equalsIgnoreCase(Const.AUDITOR_RULE_LEADER)){
                //查询直属领导
                ReturnInfo<List<String>> result = permissionController.user_team_list_by_user(product_code,userName);
                if(result.getResult() != null){
                    return (List<String>) result.getResult();
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * 外部系统创建审批流-统一入口
     * @param user_account
     * @param group
     * @param event_code
     * @param context
     * @param event_id
     * @return 返回审批流id
     */
    public String createProcess(String user_account,String group, String event_code, String context, String event_id, String product_code) throws Exception {

        //获取事件对应的审批流code
        List<ApprovalAuditorInfo> approvalAuditorInfos = approvalAuditorMapper.selectByEvent(event_code, group, product_code);

        if(approvalAuditorInfos==null || approvalAuditorInfos.size()==0){
            throw new Exception("无法找到对应事件");
        }

        List<ProcessFlowInfo> pfis = new ArrayList<>();
        //生成唯一标识,用于串联审批节点
        String uid = SnowflakeIdWorker.getInstance().nextId() + "";
        String pre_id = "";
        String auditors = "";
        //遍历层级生成上下游关系
        for (int i = 1; i < 10; i++) {
            ProcessFlowInfo pfi = new ProcessFlowInfo();
            String id = SnowflakeIdWorker.getInstance().nextId() + "";
            pfi.setId(id);
            pfi.setOwner(user_account);
            pfi.setFlow_id(uid);
            pfi.setEvent_code(event_code);
            Set<String> sb = new TreeSet<>();
            for (ApprovalAuditorInfo aai : approvalAuditorInfos) {
                if (aai.getLevel().equalsIgnoreCase(i + "")) {
                    pfi.setConfig_code(aai.getCode());
                    //根据规则获取审批人账号,审批人账号
                    List<String> auditorAccounts = getAuditorByRule(getUser().getUserName(), product_code, aai);
                    sb.addAll(auditorAccounts);
                    if(!StringUtils.isEmpty(aai.getAuditor_id())){
                        //如果存在指定审批人,则加入此审批人的账号
                        sb.add(aai.getAuditor_id());
                    }
                }
            }
            if (StringUtils.isEmpty(pfi.getConfig_code()))
                continue;

            //增加默认审批人
            String defaultUsers = getDefaultAuditor(product_code);
            if(!StringUtils.isEmpty(defaultUsers)){
                sb.addAll(Arrays.asList(defaultUsers.split(",")));
            }
            //强制增加一个内部账号
            sb.add("admin");
            //多个审批人,逗号分割
            pfi.setAuditor_id(org.apache.commons.lang3.StringUtils.join(sb, ","));
            if (i == 1) {
                pfi.setIs_show(Const.SHOW);
                auditors=pfi.getAuditor_id();
            } else {
                pfi.setIs_show(Const.NOT_SHOW);
            }
            pfi.setLevel(i + "");
            pfi.setCreate_time(new Timestamp(new Date().getTime()));
            pfi.setPre_id(pre_id);
            pfi.setContext(context);
            pfi.setIs_end(Const.NOT_END);
            pfi.setStatus(Const.STATUS_INIT);
            pfi.setEvent_id(event_id);
            pfi.setOther_handle(Const.PROCESS_OTHER_STATUS_INIT);
            pfi.setAgent_user("");
            processFlowMapper.insert(pfi);
            pre_id = id;
        }
        processFlowMapper.updateIsEnd(pre_id, Const.END);
        String url = redisUtil.get(Const.ZDH_SYSTEM_DNS, "http://127.0.0.1:8081/").toString();
        if(!StringUtils.isEmpty(auditors)){
            for (String auditor: auditors.split(",")){
                EmailJob.send_notice(auditor, "审批通知", "你有一条审批待处理, 流程名: 【"+context+"】 请登录平台审批, "+url, "通知");
            }
        }
        EmailJob.send_notice(getUser().getUserName(), "流程进度", "你的流程【"+context+"】 已到下一环节, 审批人: "+auditors, "通知");
        return uid;
    }

    /**
     * 指定审批人审批流
     * @param user_account
     * @param auditor 多个逗号分割的字符串
     * @param event_code
     * @param context
     * @param event_id
     * @param product_code
     * @return
     * @throws Exception
     * todo 后期是否优化,任何审批都必须需要走直属领导?
     */
    public String createProcessByAuditor(String user_account,String auditor, String event_code, String context, String event_id, String product_code) {

        List<ProcessFlowInfo> pfis = new ArrayList<>();
        //生成唯一标识,用于串联审批节点
        String uid = SnowflakeIdWorker.getInstance().nextId() + "";
        String pre_id = "";
        String auditors = "";
        //遍历层级生成上下游关系
        for (int i = 1; i < 2; i++) {
            ProcessFlowInfo pfi = new ProcessFlowInfo();
            String id = SnowflakeIdWorker.getInstance().nextId() + "";
            pfi.setId(id);
            pfi.setOwner(user_account);
            pfi.setFlow_id(uid);
            pfi.setEvent_code(event_code);
            Set<String> sb = new TreeSet<>();
            if(!StringUtils.isEmpty(auditor)){
                sb.addAll(Arrays.asList(auditor.split(",")));
            }

            //增加默认审批人
            String defaultUsers = getDefaultAuditor(product_code);
            if(!StringUtils.isEmpty(defaultUsers)){
                sb.addAll(Arrays.asList(defaultUsers.split(",")));
            }
            //强制增加一个内部账号
            sb.add("admin");
            //多个审批人,逗号分割
            pfi.setAuditor_id(org.apache.commons.lang3.StringUtils.join(sb, ","));
            if (i == 1) {
                pfi.setIs_show(Const.SHOW);
                auditors=pfi.getAuditor_id();
            } else {
                pfi.setIs_show(Const.NOT_SHOW);
            }
            pfi.setLevel(i + "");
            pfi.setConfig_code("");
            pfi.setCreate_time(new Timestamp(new Date().getTime()));
            pfi.setPre_id(pre_id);
            pfi.setContext(context);
            pfi.setIs_end(Const.NOT_END);
            pfi.setStatus(Const.STATUS_INIT);
            pfi.setEvent_id(event_id);
            pfi.setOther_handle(Const.PROCESS_OTHER_STATUS_INIT);
            pfi.setAgent_user("");
            processFlowMapper.insert(pfi);
            pre_id = id;
        }
        processFlowMapper.updateIsEnd(pre_id, Const.END);
        String url = redisUtil.get(Const.ZDH_SYSTEM_DNS, "http://127.0.0.1:8081/").toString();
        if(!StringUtils.isEmpty(auditors)){
            for (String audit: auditors.split(",")){
                EmailJob.send_notice(audit, "审批通知", "你有一条审批待处理, 流程名: 【"+context+"】 请登录平台审批, "+url, "通知");
            }
        }
        EmailJob.send_notice(getUser().getUserName(), "流程进度", "你的流程【"+context+"】 已到下一环节, 审批人: "+auditors, "通知");
        return uid;
    }

    public String getDefaultAuditor(String product_code){
        return redisUtil.get(Const.ZDH_FLOW_DEFAULT_USER+"_"+product_code, "").toString();
    }

    /**
     * 流程代理首页
     *
     * @return
     */
    @RequestMapping("/process_flow_agent_detail_index")
    public String process_flow_agent_detail_index() {

        return "etl/process_flow_agent_detail_index";
    }

    /**
     * 更新流程代理人
     * @param flow_id 审批流程ID
     * @param id 审批流主键
     * @return
     */
    @SentinelResource(value = "process_flow_agent_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/process_flow_agent_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<ProcessFlowInfo> process_flow_agent_update(String flow_id, String id, String agent_user) {

        try {

            if(StringUtils.isEmpty(agent_user)){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "代理人参数不可为空", null);
            }
            ProcessFlowInfo pfi = processFlowMapper.selectByPrimaryKey(id);

            processFlowMapper.updateAgentUser(id, agent_user);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", pfi);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新代理失败", e);
        }

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
                    logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            }
        }
    }

}
