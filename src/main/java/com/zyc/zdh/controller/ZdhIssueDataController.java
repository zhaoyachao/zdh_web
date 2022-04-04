package com.zyc.zdh.controller;


import com.alibaba.fastjson.JSON;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.EmailJob;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.JemailService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.Const;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 数据仓库服务
 * 发布服务,申请服务
 */
@Controller
public class ZdhIssueDataController extends BaseController {
    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;


    @Autowired
    IssueDataMapper issueDataMapper;

    @Autowired
    ApplyMapper applyMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    JemailService jemailService;

    @Autowired
    ApprovalConfigMapper approvalConfigMapper;

    @Autowired
    ApprovalAuditorMapper approvalAuditorMapper;

    @Autowired
    ProcessFlowMapper processFlowMapper;

    @Autowired
    ZdhProcessFlowController zdhProcessFlowController;

    @Autowired
    ResourceTreeMapper resourceTreeMapper;

    @Autowired
    ApprovalEventMapper approvalEventMapper;

    @RequestMapping("/data_issue_index")
    public String data_issue_index() {

        return "etl/data_issue_index";
    }

    @RequestMapping("/data_issue_add_index")
    public String data_issue_add_index() {

        return "etl/data_issue_add_index";
    }

    /**
     * 数据查询首页
     *
     * @return
     */
    @RequestMapping("/data_ware_house_index")
    public String etl_task_index() {

        return "etl/data_ware_house_index";
    }

    /**
     * 申请首页
     *
     * @return
     */
    @RequestMapping("/data_apply_index")
    public String data_apply_index() {

        return "etl/data_apply_index";
    }

    /**
     * 表明细首页
     *
     * @return
     */
    @RequestMapping("/data_ware_house_detail_index")
    public String data_ware_house_detail_index() {

        return "etl/data_ware_house_detail_index";
    }


    /**
     * 根据id获取对应的数据明细
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/data_ware_house_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_ware_house_list(String id) {

        IssueDataInfo idi = issueDataMapper.selectById(id);
        return JSON.toJSONString(idi);
    }

    /**
     * 根据条件模糊查询发布数据源
     *
     * @param issue_context
     * @return
     */
    @RequestMapping(value = "/data_ware_house_list2", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_ware_house_list2(String issue_context) {
        List<IssueDataInfo> list = new ArrayList<>();
        list = issueDataMapper.selectByParams(issue_context,new String[]{});

        return JSON.toJSONString(list);
    }

    /**
     * 根据条件模糊查询发布数据源,只查询当前用户
     *
     * @param issue_context
     * @return
     */
    @RequestMapping(value = "/data_ware_house_list3", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_ware_house_list3(String issue_context) {
        List<IssueDataInfo> list = new ArrayList<>();
        String owner = getUser().getId();
        list = issueDataMapper.selectByOwner(issue_context, owner);

        return JSON.toJSONString(list);
    }


    @RequestMapping(value = "/data_ware_house_del", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_ware_house_del(String id) {
        try {
            int result = issueDataMapper.deleteByPrimaryKey(id);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }


    /**
     * 发布数据表信息
     * 走审批流
     * 1 创建数据表-> 2 根据当前接口从功能资源列表查询绑定的审批事件 -> 3 根据审批事件查询所有审批节点 -> 4 创建审批流 -> 审批人在审批页面审批
     * @param issueDataInfo
     * @return
     */
    @RequestMapping(value = "/issue_data_add", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String issue_data_add(IssueDataInfo issueDataInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try {
            String owner = getUser().getId();
            String issue_id = SnowflakeIdWorker.getInstance().nextId() + "";
            issueDataInfo.setId(issue_id);
            issueDataInfo.setOwner(owner);
            issueDataInfo.setCreate_time(new Timestamp(new Date().getTime()));
            issueDataInfo.setStatus(Const.STATUS_NOT_PUB);
            if (!issueDataInfo.getData_source_type_input().equalsIgnoreCase("jdbc") && !StringUtils.isEmpty(issueDataInfo.getData_sources_file_name_input())) {
                issueDataInfo.setData_sources_table_name_input(issueDataInfo.getData_sources_file_name_input());
                issueDataInfo.setData_sources_table_columns(issueDataInfo.getData_sources_file_columns());
            }
            debugInfo(issueDataInfo);
            issueDataMapper.insert(issueDataInfo);
            //根据资源信息获取审批流

            Example example = new Example(ResourceTreeInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("url", "issue_data_add");
            List<ResourceTreeInfo> resourceTreeInfos = resourceTreeMapper.selectByExample(example);
            if (resourceTreeInfos.size() != 1) {
                throw new Exception("无法找到对应事件配置,请检查权限管理-菜单配置是否正常");
            }

            //执行审批流程
            //String event_code=EventCode.DATA_PUB.getCode();
            String event_code = resourceTreeInfos.get(0).getEvent_code();
            if (org.apache.commons.lang3.StringUtils.isEmpty(event_code)) {
                throw new Exception("无法找到对应事件配置,请检查权限管理-菜单配置是否包含事件信息");
            }
            //获取事件配置,校验是否跳过
            Example example1=new Example(ApprovalEventInfo.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("event_code", event_code);
            List<ApprovalEventInfo> approvalEventInfos = approvalEventMapper.selectByExample(example1);
            if(approvalEventInfos!=null && approvalEventInfos.size()>0){
                if(Arrays.asList(approvalEventInfos.get(0).getSkip_account().split(",")).contains(getUser().getUserName())){
                    //跳过审批
                    issueDataInfo.setStatus(Const.STATUS_PUB);
                    issueDataMapper.updateByPrimaryKey(issueDataInfo);
                    return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "新增成功,且跳过审批", null);
                }
            }
            zdhProcessFlowController.createProcess(event_code, "发布数据-" + issueDataInfo.getIssue_context(), issue_id);

            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "新增成功,等待审批", null);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("数据发布异常, {} : ", e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    @RequestMapping(value = "/issue_data_update", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String issue_data_update(IssueDataInfo issueDataInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try {
            IssueDataInfo idi = issueDataMapper.selectByPrimaryKey(issueDataInfo);

            String owner = getUser().getId();
            issueDataInfo.setOwner(owner);
            issueDataInfo.setStatus(Const.STATUS_PUB);
            if (!issueDataInfo.getData_source_type_input().equalsIgnoreCase("jdbc") && !StringUtils.isEmpty(issueDataInfo.getData_sources_file_name_input())) {
                issueDataInfo.setData_sources_table_name_input(issueDataInfo.getData_sources_file_name_input());
                issueDataInfo.setData_sources_table_columns(issueDataInfo.getData_sources_file_columns());
            }
            debugInfo(issueDataInfo);
            IssueDataInfo issueDataInfo2 = issueDataMapper.selectById(issueDataInfo.getId());
            issueDataMapper.updateByPrimaryKey(issueDataInfo);
            String line = Const.LINE_SEPARATOR;
            String change_message = "";
            if (!idi.getIssue_context().equalsIgnoreCase(issueDataInfo.getIssue_context())) {
                change_message = change_message + line + "数据源描述发生变动: 由\"" + idi.getIssue_context() + "\" 变更为\"" + issueDataInfo.getIssue_context() + "\"";
            }
            if (!idi.getData_sources_table_name_input().equalsIgnoreCase(issueDataInfo.getData_sources_table_name_input())) {
                change_message = change_message + line + "数据源表名发生变动: 由\"" + idi.getData_sources_table_name_input() + "\" 变更为\"" + issueDataInfo.getData_sources_table_name_input() + "\"";
            }
            if (!idi.getData_sources_file_name_input().equalsIgnoreCase(issueDataInfo.getData_sources_file_name_input())) {
                change_message = change_message + line + "数据源文件名发生变动: 由\"" + idi.getData_sources_file_name_input() + "\" 变更为\"" + issueDataInfo.getData_sources_file_name_input() + "\"";
            }
            if (!idi.getColumn_datas().equalsIgnoreCase(issueDataInfo.getColumn_datas())) {
                change_message = change_message + line + "数据源字段结构发生变动: 由\"" + idi.getColumn_datas() + "\" 变更为\"" + issueDataInfo.getColumn_datas() + "\"";
            }

            if (!change_message.equalsIgnoreCase("")) {
                // 查找下游用户,发送更新邮件
                send_email_downstream(issueDataInfo2, change_message);

            }
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }

    }

    @RequestMapping(value = "/issue_data_delete", method = RequestMethod.POST)
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String issue_data_delete(String[] ids) {
        //String json_str=JSON.toJSONString(request.getParameterMap());

        try {
            issueDataMapper.deleteBatchByIds(ids);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }


    /**
     * 申请数据表信息
     *
     * @param issue_id 数据仓库发布数据id
     * @return
     */
    @RequestMapping(value = "/data_apply_add", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String data_apply_add(String issue_id) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        //根据发布id,获取数据信息,找到对应的管理者
        try {
            IssueDataInfo issueDataInfo = issueDataMapper.selectById(issue_id);
            String owner = issueDataInfo.getOwner();

            String current_owner = getUser().getId();

            if (owner.equalsIgnoreCase(current_owner)) {
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "当前数据不用申请,即可使用", null);
            }

            //判断是否已经申请过
            Example example1=new Example(ApplyInfo.class);
            Example.Criteria criteria1=example1.createCriteria();
            criteria1.andEqualTo("owner", getUser().getId());
            criteria1.andEqualTo("issue_id", issue_id);
            criteria1.andIn("status", Arrays.asList(new String[]{Const.APPLY_STATUS_INIT,Const.APPLY_STATUS_SUCCESS}));
//            ApplyInfo app = new ApplyInfo();
//            app.setOwner(getUser().getId());
//            app.setIssue_id(issue_id);
            List<ApplyInfo> applyInfos = applyMapper.selectByExample(example1);
            if (applyInfos != null && applyInfos.size() > 0) {
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "当前数据以申请过,不可重复申请", null);
            }

            String id = SnowflakeIdWorker.getInstance().nextId() + "";
            //数据审批信息创建
            ApplyInfo applyInfo = new ApplyInfo();
            applyInfo.setId(id);
            applyInfo.setIssue_id(issue_id);
            applyInfo.setApprove_id(owner);
            applyInfo.setApply_context(issueDataInfo.getData_sources_table_name_input());
            applyInfo.setOwner(current_owner);
            applyInfo.setCreate_time(new Timestamp(new Date().getTime()));
            applyInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            applyInfo.setStatus(Const.APPLY_STATUS_INIT);
            debugInfo(applyInfo);
            applyMapper.insert(applyInfo);

            //根据资源信息获取审批流
            Example example = new Example(ResourceTreeInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("url", "data_apply_add");
            List<ResourceTreeInfo> resourceTreeInfos = resourceTreeMapper.selectByExample(example);
            if (resourceTreeInfos.size() != 1) {
                throw new Exception("无法找到对应事件配置,请检查权限管理-菜单配置是否正常");
            }

            //执行审批流程
            //String event_code=EventCode.DATA_PUB.getCode();
            String event_code = resourceTreeInfos.get(0).getEvent_code();
            if (org.apache.commons.lang3.StringUtils.isEmpty(event_code)) {
                throw new Exception("无法找到对应事件配置,请检查权限管理-菜单配置是否包含事件信息");
            }

            //String event_code = EventCode.DATA_APPLY.getCode();
            //获取事件配置,校验是否跳过
            Example example2=new Example(ApprovalEventInfo.class);
            Example.Criteria criteria2 = example2.createCriteria();
            criteria2.andEqualTo("event_code", event_code);
            List<ApprovalEventInfo> approvalEventInfos = approvalEventMapper.selectByExample(example2);
            if(approvalEventInfos!=null && approvalEventInfos.size()>0){
                if(Arrays.asList(approvalEventInfos.get(0).getSkip_account().split(",")).contains(getUser().getUserName())){
                    //跳过审批
                    applyInfo.setStatus(Const.APPLY_STATUS_SUCCESS);
                    applyMapper.updateByPrimaryKey(applyInfo);
                    return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "申请成功,且跳过审批", null);
                }
            }

            zdhProcessFlowController.createProcess(event_code, "申请数据-" + applyInfo.getApply_context(), applyInfo.getId());

            //EmailJob会自动加载通知信息
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "申请以发起", null);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "申请失败", e);
        }

    }

    @RequestMapping(value = "/data_apply_list", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_apply_list(String apply_context) {

        ApplyInfo applyInfo = new ApplyInfo();
        applyInfo.setOwner(getUser().getId());
        List<ApplyInfo> applyInfos = applyMapper.selectByParams(apply_context, null, null, getUser().getId());
        String json = ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "获取申请明细", applyInfos);
        return json;
    }

    @RequestMapping(value = "/data_apply_list2", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_apply_list2(String apply_context) {

        ApplyInfo applyInfo = new ApplyInfo();
        applyInfo.setOwner(getUser().getId());
        List<ApplyIssueInfo> applyInfos = applyMapper.selectByParams3(apply_context, "1", null, getUser().getId());
        String json = ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "获取申请明细", applyInfos);
        return json;
    }

    @RequestMapping(value = "/data_apply_list3", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_apply_list3(String id) {

        ApplyInfo applyInfo = new ApplyInfo();
        applyInfo.setOwner(getUser().getId());
        ApplyIssueInfo applyInfos = applyMapper.selectByParams4(id, "1", null, getUser().getId());
        String json = ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "获取申请明细", applyInfos);
        return json;
    }

    /**
     * @param id
     * @return
     */
    @RequestMapping(value = "/data_apply_cancel", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_apply_cancel(String id) {
        try {
            applyMapper.updateStatus(id, Const.STATUS_RECALL);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "撤销申请完成", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "撤销申请失败", e);
        }
    }

    /**
     * 审批明细
     *
     * @param apply_context
     * @return
     */
    @RequestMapping(value = "/data_approve_list", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_approve_list(String apply_context) {

        ApplyInfo applyInfo = new ApplyInfo();
        applyInfo.setApprove_id(getUser().getId());
        List<ApplyInfo> applyInfos = applyMapper.selectByParams2(apply_context, getUser().getId(), null);
        String json = ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "获取审批明细", applyInfos);
        return json;
    }

    @RequestMapping(value = "/data_approve", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_approve(String id, String status) {
        try {
            applyMapper.updateStatus(id, status);
            //redisUtil.remove("zdhapplyinfos_"+getUser().getId());
            EmailJob.apply_notice();
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "审批流程结束", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "审批失败", e);
        }

    }

    private void send_email_downstream(IssueDataInfo issueDataInfo, String message) {

        new Thread(new Runnable() {
            @Override
            public void run() {

                try{
                    List<ApplyAlarmInfo> applyAlarmInfos = applyMapper.selectByIssueId(issueDataInfo.getId());

                    List<String> to = new ArrayList<>();
                    for (ApplyAlarmInfo applyAlarmInfo : applyAlarmInfos) {
                        if (!StringUtils.isEmpty(applyAlarmInfo.getEmail())) {
                            to.add(applyAlarmInfo.getEmail());
                        }
                    }
                    if (to.size() < 1) {
                        return;
                    }
                    String line = Const.LINE_SEPARATOR;
                    String table = issueDataInfo.getData_sources_table_name_input();
                    String subject = "上游数据源变动";
                    String context = "你申请的上游数据源【" + table + "】发生变动,请及时检查和此数据源相关的任务;" + line + message;
                    jemailService.sendEmail(to.toArray(new String[applyAlarmInfos.size()]), subject, context);
                }catch (Exception e){
                    logger.error("发布源变动,发送下游通知异常:{}",e);
                }

            }
        }).start();


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
                    String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
                    logger.error(error, e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}");
            }
        }
    }

}
