package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

/**
 * 数据仓库服务
 * 审批服务
 */
@Controller
public class ZdhProcessFlowController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ProcessFlowMapper processFlowMapper;
    @Autowired
    IssueDataMapper issueDataMapper;
    @Autowired
    ApplyMapper applyMapper;
    @Autowired
    ApprovalAuditorMapper approvalAuditorMapper;
    @Autowired
    ApprovalEventMapper approvalEventMapper;
    @Autowired
    PermissionApplyMapper permissionApplyMapper;
    @Autowired
    PermissionMapper permissionMapper;

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

    @RequestMapping(value = "/process_flow_list", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String process_flow_list(String context) {

        if(!StringUtils.isEmpty(context)){
            context = getLikeCondition(context);
        }
        List<ProcessFlowInfo> pfis = processFlowMapper.selectByAuditorId(Const.SHOW, getUser().getUserName(), context);
        String json = ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "获取流程列表", pfis);
        return json;
    }

    /**
     * 我的发起流程列表
     *
     * @param context
     * @return
     */
    @RequestMapping(value = "/process_flow_list2", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String process_flow_list2(String context) {
        if(!StringUtils.isEmpty(context)){
            context = getLikeCondition(context);
        }
        List<ProcessFlowInfo> pfis = processFlowMapper.selectByOwner(getUser().getUserName(), context);
        String json = ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "获取流程列表", pfis);
        return json;
    }

    /**
     * 审批
     * @param pfi
     * @return
     */
    @RequestMapping(value = "/process_flow_status", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String process_flow_status(ProcessFlowInfo pfi) {
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

        return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "审批", pfi);
    }

    /**
     * 审批通过后
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

        //此处增加回调机制(只有回调才能做到,万能审批流,调度方可以不关系审批流程,但是必须提供一个审批完成/失败后的回调接口(http))

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
    @RequestMapping(value = "/process_flow_status2", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String process_flow_status2(ProcessFlowInfo pfi) {
        processFlowMapper.updateStatus2(pfi.getFlow_id(), pfi.getStatus());
        return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "撤销", pfi);
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

    @RequestMapping(value = "/process_flow_detail", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String process_flow_detail(String flow_id) {

        try {
            List<ProcessFlowInfo> pfis = processFlowMapper.selectByFlowId(flow_id, getUser().getUserName());
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "获取流程进度", pfis);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "获取流程进度", e);
        }

    }

    /**
     * ZDH创建审批流统一入口
     *
     * @param event_code
     * @param context
     * @param event_id
     */
    public void createProcess(String event_code, String context, String event_id) {
        String group = getUser().getUser_group();
        //获取事件对应的审批流code
        List<ApprovalAuditorInfo> approvalAuditorInfos = approvalAuditorMapper.selectByEvent(event_code, group);

        List<ProcessFlowInfo> pfis = new ArrayList<>();
        //生成唯一标识,用于串联审批节点
        String uid = SnowflakeIdWorker.getInstance().nextId() + "";
        String pre_id = "";
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
                    //审批人账号
                    sb.add(aai.getAuditor_id());
                }
            }
            if (StringUtils.isEmpty(pfi.getConfig_code()))
                continue;
            //多个审批人,逗号分割
            pfi.setAuditor_id(org.apache.commons.lang3.StringUtils.join(sb, ","));
            if (i == 1) {
                pfi.setIs_show(Const.SHOW);
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
            processFlowMapper.insert(pfi);
            pre_id = id;
        }
        processFlowMapper.updateIsEnd(pre_id, Const.END);
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
    public String createProcess(String user_account,String group, String event_code, String context, String event_id) throws Exception {

        //获取事件对应的审批流code
        List<ApprovalAuditorInfo> approvalAuditorInfos = approvalAuditorMapper.selectByEvent(event_code, group);

        if(approvalAuditorInfos==null || approvalAuditorInfos.size()==0){
            throw new Exception("无法找到对应事件");
        }

        List<ProcessFlowInfo> pfis = new ArrayList<>();
        //生成唯一标识,用于串联审批节点
        String uid = SnowflakeIdWorker.getInstance().nextId() + "";
        String pre_id = "";
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
                    //审批人账号
                    sb.add(aai.getAuditor_id());
                }
            }
            if (StringUtils.isEmpty(pfi.getConfig_code()))
                continue;
            //多个审批人,逗号分割
            pfi.setAuditor_id(org.apache.commons.lang3.StringUtils.join(sb, ","));
            if (i == 1) {
                pfi.setIs_show(Const.SHOW);
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
            processFlowMapper.insert(pfi);
            pre_id = id;
        }
        processFlowMapper.updateIsEnd(pre_id, Const.END);
        return uid;
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
