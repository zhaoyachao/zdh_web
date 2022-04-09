package com.zyc.zdh.controller;

import com.zyc.zdh.dao.ApplyMapper;
import com.zyc.zdh.dao.ApprovalAuditorMapper;
import com.zyc.zdh.dao.IssueDataMapper;
import com.zyc.zdh.dao.ProcessFlowMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
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

    @RequestMapping(value = "/process_flow_list", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String process_flow_list(String context) {

        if(!StringUtils.isEmpty(context)){
            context = getLikeCondition(context);
        }
        List<ProcessFlowInfo> pfis = processFlowMapper.selectByAuditorId(Const.SHOW, getUser().getId(), context);
        String json = ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "获取流程列表", pfis);
        return json;
    }

    /**
     * 我的发起流程列表
     *
     * @param context
     * @return
     */
    @RequestMapping(value = "/process_flow_list2", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String process_flow_list2(String context) {
        if(!StringUtils.isEmpty(context)){
            context = getLikeCondition(context);
        }
        List<ProcessFlowInfo> pfis = processFlowMapper.selectByOwner(getUser().getId(), context);
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
        if (pfi.getStatus().equalsIgnoreCase(Const.STATUS_PUB)) {
            //设置下游流程可展示
            processFlowMapper.updateIsShow(pfi.getId(), Const.SHOW);
        }


        //最后一个审批,表示审批流程结束 todo 之后新增事件,可在此处处理
        if (pfi_old.getIs_end().equalsIgnoreCase(Const.END)) {
            if (pfi_old.getEvent_code().equalsIgnoreCase(EventCode.DATA_PUB.getCode())) {
                //更新发布表
                IssueDataInfo idi = issueDataMapper.selectByPrimaryKey(pfi_old.getEvent_id());
                idi.setStatus(Const.STATUS_NOT_PUB);//未发布
                if (pfi.getStatus().equalsIgnoreCase(Const.STATUS_PUB)) {
                    //审批通过
                    idi.setStatus(Const.STATUS_PUB);//发布
                }
                issueDataMapper.updateByPrimaryKey(idi);
            }
            if (pfi_old.getEvent_code().equalsIgnoreCase(EventCode.DATA_APPLY.getCode())) {
                //更新申请表
                ApplyInfo ai = applyMapper.selectByPrimaryKey(pfi_old.getEvent_id());
                ai.setStatus(Const.STATUS_NOT_PUB);//未通过
                if (pfi.getStatus().equalsIgnoreCase(Const.STATUS_PUB)) {
                    //审批通过
                    ai.setStatus(Const.STATUS_PUB);
                }
                applyMapper.updateByPrimaryKey(ai);
            }
        }

        return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "审批", pfi);
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
            List<ProcessFlowInfo> pfis = processFlowMapper.selectByFlowId(flow_id);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "获取流程进度", pfis);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "获取流程进度", e);
        }

    }

    /**
     * 创建审批流统一入口
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
            pfi.setOwner(getUser().getId());
            pfi.setFlow_id(uid);
            pfi.setEvent_code(event_code);
            Set<String> sb = new TreeSet<>();
            for (ApprovalAuditorInfo aai : approvalAuditorInfos) {
                if (aai.getLevel().equalsIgnoreCase(i + "")) {
                    pfi.setConfig_code(aai.getCode());
                    sb.add(aai.getAuditor_id());
                }
            }
            if (StringUtils.isEmpty(pfi.getConfig_code()))
                continue;
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
            processFlowMapper.insert(pfi);
            pre_id = id;
        }
        processFlowMapper.updateIsEnd(pre_id, Const.END);
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
                    logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" + e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" + e);
            }
        }
    }

}
