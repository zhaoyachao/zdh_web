package com.zyc.zdh.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.dao.ApplyMapper;
import com.zyc.zdh.dao.ApprovalEventMapper;
import com.zyc.zdh.dao.IssueDataMapper;
import com.zyc.zdh.dao.ResourceTreeMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.EmailJob;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.JemailService;
import com.zyc.zdh.service.ZdhPermissionService;
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
import java.util.List;
import java.util.Map;

/**
 * 数据仓库服务
 * 发布服务,申请服务
 */
@Controller
public class ZdhIssueDataController extends BaseController {
    public Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private IssueDataMapper issueDataMapper;
    @Autowired
    private ApplyMapper applyMapper;
    @Autowired
    private JemailService jemailService;
    @Autowired
    private ZdhProcessFlowController zdhProcessFlowController;
    @Autowired
    private ResourceTreeMapper resourceTreeMapper;
    @Autowired
    private ApprovalEventMapper approvalEventMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 数据发布首页
     * @return
     */
    @RequestMapping("/data_issue_index")
    public String data_issue_index() {

        return "etl/data_issue_index";
    }

    /**
     * 数据发布新增首页
     * @return
     */
    @RequestMapping("/data_issue_add_index")
    public String data_issue_add_index() {

        return "etl/data_issue_add_index";
    }

    /**
     * 数据集市查询首页
     *
     * @return
     */
    @RequestMapping("/data_ware_house_index")
    public String etl_task_index() {

        return "etl/data_ware_house_index";
    }

    /**
     * 数据已申请首页
     *
     * @return
     */
    @RequestMapping("/data_apply_index")
    public String data_apply_index() {

        return "etl/data_apply_index";
    }

    /**
     * 数据申请首页
     *
     * @return
     */
    @White
    @RequestMapping("/data_apply_add_index")
    public String data_apply_add_index() {

        return "etl/data_apply_add_index";
    }

    /**
     * 数据明细首页
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
     * @param id 发布数据ID
     * @return
     */
    @SentinelResource(value = "data_ware_house_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_ware_house_list", method=RequestMethod.POST,produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<IssueDataInfo> data_ware_house_list(String id) {
        try{
            IssueDataInfo idi = issueDataMapper.selectById(id);
            checkAttrPermissionByProduct(zdhPermissionService, idi.getProduct_code(), getAttrSelect());
            return ReturnInfo.buildSuccess(idi);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("数据集市任务列表查询失败", e);
        }
    }

    /**
     * 数据集市-查询列表
     * 获取有权限的产品线数据
     *
     * 根据条件模糊查询发布数据
     *
     * @param issue_context 关键字
     * @return
     */
    @SentinelResource(value = "data_ware_house_list2", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_ware_house_list2",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<IssueDataInfo>> data_ware_house_list2(String issue_context, String product_code) {
        try{
            List<IssueDataInfo> list = new ArrayList<>();
            if(!StringUtils.isEmpty(issue_context)){
                issue_context = getLikeCondition(issue_context);
            }
            checkParam(product_code, "产品code");
            Map<String,List<String>> dimMap = dynamicPermissionByProductAndGroup(zdhPermissionService);

            list = issueDataMapper.selectByParams(issue_context,new String[]{}, product_code, dimMap.get("product_codes"));
            return ReturnInfo.buildSuccess(list);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("数据集市任务列表2查询失败"+e.getMessage(), e);
        }
    }

    /**
     * 数据发布-查询列表
     * 根据条件模糊查询发布数据源,查询有权限的产品
     *
     * @param issue_context
     * @param product_code 产品code
     * @return
     */
    @SentinelResource(value = "data_ware_house_list3", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_ware_house_list3", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<IssueDataInfo>> data_ware_house_list3(String issue_context, String product_code) throws Exception {
        try{
            checkProductCode(product_code);
            List<IssueDataInfo> list = new ArrayList<>();
            String owner = getOwner();
            if(!StringUtils.isEmpty(issue_context)){
                issue_context = getLikeCondition(issue_context);
            }
            Map<String,List<String>> dimMap = dynamicPermissionByProductAndGroup(zdhPermissionService);
            list = issueDataMapper.selectByOwner(issue_context, null, product_code, dimMap.get("product_codes"), dimMap.get("dim_groups"));
            return ReturnInfo.buildSuccess(list);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("数据集市任务列表3查询失败"+e.getMessage(), e);
        }
    }


    /**
     * 发布数据删除
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "data_ware_house_del", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_ware_house_del", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo data_ware_house_del(String id) {
        try {

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, issueDataMapper, issueDataMapper.getTable(), new String[]{id}, getAttrDel());

            int result = issueDataMapper.deleteByPrimaryKey(id);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }


    /**
     * 发布数据表信息
     * 走审批流
     * 1 创建数据表-> 2 根据当前接口从功能资源列表查询绑定的审批事件 -> 3 根据审批事件查询所有审批节点 -> 4 创建审批流 -> 审批人在审批页面审批
     * @param issueDataInfo
     * @return
     */
    @SentinelResource(value = "issue_data_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/issue_data_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo issue_data_add(IssueDataInfo issueDataInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try {

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, issueDataInfo.getProduct_code(),issueDataInfo.getDim_group(), getAttrAdd());

            String owner = getOwner();
            String issue_id = SnowflakeIdWorker.getInstance().nextId() + "";
            issueDataInfo.setId(issue_id);
            issueDataInfo.setOwner(owner);
            issueDataInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            issueDataInfo.setStatus(Const.STATUS_NOT_PUB);
            if (!issueDataInfo.getData_source_type_input().equalsIgnoreCase("jdbc") && !StringUtils.isEmpty(issueDataInfo.getData_sources_file_name_input())) {
                issueDataInfo.setData_sources_table_name_input(issueDataInfo.getData_sources_file_name_input());
                issueDataInfo.setData_sources_table_columns(issueDataInfo.getData_sources_file_columns());
            }
            debugInfo(issueDataInfo);
            //checkPermissionByProductAndDimGroup(zdhPermissionService, issueDataInfo.getProduct_code(), issueDataInfo.getDim_group());
            issueDataMapper.insertSelective(issueDataInfo);
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
                    issueDataMapper.updateByPrimaryKeySelective(issueDataInfo);
                    return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功,且跳过审批", null);
                }
            }
            zdhProcessFlowController.createProcess(issueDataInfo.getProduct_code(), event_code, "发布数据-" + issueDataInfo.getIssue_context(), issue_id);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功,等待审批", null);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 发布数据更新
     * @param issueDataInfo
     * @return
     */
    @SentinelResource(value = "issue_data_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/issue_data_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo issue_data_update(IssueDataInfo issueDataInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try {

            IssueDataInfo idi = issueDataMapper.selectByPrimaryKey(issueDataInfo);

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, issueDataInfo.getProduct_code(),issueDataInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, idi.getProduct_code(), idi.getDim_group(), getAttrEdit());

            String owner = getOwner();
            issueDataInfo.setOwner(owner);
            issueDataInfo.setStatus(Const.STATUS_PUB);
            if (!issueDataInfo.getData_source_type_input().equalsIgnoreCase("jdbc") && !StringUtils.isEmpty(issueDataInfo.getData_sources_file_name_input())) {
                issueDataInfo.setData_sources_table_name_input(issueDataInfo.getData_sources_file_name_input());
                issueDataInfo.setData_sources_table_columns(issueDataInfo.getData_sources_file_columns());
            }
            debugInfo(issueDataInfo);
            IssueDataInfo issueDataInfo2 = issueDataMapper.selectById(issueDataInfo.getId());
            issueDataMapper.updateByPrimaryKeySelective(issueDataInfo);
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
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }

    }

    /**
     * 发布数据删除
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "issue_data_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/issue_data_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo issue_data_delete(String[] ids) {
        //String json_str=JSON.toJSONString(request.getParameterMap());

        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, issueDataMapper, issueDataMapper.getTable(), ids, getAttrDel());
            issueDataMapper.deleteBatchByIds(ids);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }


    /**
     * 申请数据表信息
     *
     * @param dim_group 申请组
     * @param issue_id 数据仓库发布数据id
     * @return
     */
    @SentinelResource(value = "data_apply_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_apply_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo data_apply_add(String product_code,String dim_group,String issue_id) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        //根据发布id,获取数据信息,找到对应的管理者
        try {
            IssueDataInfo issueDataInfo = issueDataMapper.selectById(issue_id);
            String owner = issueDataInfo.getOwner();

            checkProductCode(product_code);
            checkParam(dim_group, "归属组");

            String current_owner = getOwner();

            if (product_code.equalsIgnoreCase(issueDataInfo.getProduct_code()) && dim_group.equalsIgnoreCase(issueDataInfo.getDim_group())) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "当前数据不用申请,即可使用", null);
            }

            if(!issueDataInfo.getProduct_code().equalsIgnoreCase(product_code)){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "申请数据产品code必须和发布数据一致", null);
            }

            checkPermissionByProductAndDimGroup(zdhPermissionService, product_code, dim_group);

            //判断是否已经申请过
            Example example1=new Example(ApplyInfo.class);
            Example.Criteria criteria1=example1.createCriteria();
            criteria1.andEqualTo("product_code", product_code);
            criteria1.andEqualTo("dim_group", dim_group);
            criteria1.andEqualTo("issue_id", issue_id);
            criteria1.andIn("status", Arrays.asList(new String[]{Const.APPLY_STATUS_INIT,Const.APPLY_STATUS_SUCCESS}));
            List<ApplyInfo> applyInfos = applyMapper.selectByExample(example1);
            if (applyInfos != null && applyInfos.size() > 0) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "当前数据已申请完成/正在申请中,不可重复申请", null);
            }

            String id = SnowflakeIdWorker.getInstance().nextId() + "";
            //数据审批信息创建
            ApplyInfo applyInfo = new ApplyInfo();
            applyInfo.setProduct_code(issueDataInfo.getProduct_code());
            applyInfo.setDim_group(dim_group);
            applyInfo.setId(id);
            applyInfo.setIssue_id(issue_id);
            applyInfo.setApprove_id(owner);
            applyInfo.setApply_context(issueDataInfo.getData_sources_table_name_input());
            applyInfo.setOwner(current_owner);
            applyInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            applyInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            applyInfo.setStatus(Const.APPLY_STATUS_INIT);
            debugInfo(applyInfo);
            applyMapper.insertSelective(applyInfo);

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
                    applyMapper.updateByPrimaryKeySelective(applyInfo);
                    return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "申请成功,且跳过审批", null);
                }
            }

            zdhProcessFlowController.createProcess(product_code, event_code, "申请数据-" + applyInfo.getApply_context(), applyInfo.getId());

            //EmailJob会自动加载通知信息
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "申请已发起", null);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "申请失败", e);
        }

    }

    /**
     * 数据申请列表
     * @param product_code 产品code
     * @param apply_context 关键字
     * @return
     * @throws Exception
     */
    @SentinelResource(value = "data_apply_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_apply_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<ApplyInfo>> data_apply_list(String product_code, String apply_context) throws Exception {

        try{
            checkProductCode(product_code);
            ApplyInfo applyInfo = new ApplyInfo();
            applyInfo.setOwner(getOwner());
            if(!StringUtils.isEmpty(apply_context)){
                apply_context = getLikeCondition(apply_context);
            }

            Map<String,List<String>> dimMap = dynamicPermissionByProductAndGroup(zdhPermissionService);

            List<ApplyInfo> applyInfos = applyMapper.selectByParams(apply_context, null, null, null, product_code, dimMap.get("product_codes"));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "获取申请列表成功", applyInfos);
        }catch (Exception e){
            return ReturnInfo.buildError("数据申请列表查询失败"+e.getMessage(), e);
        }

    }

    /**
     * 数据申请
     * @param apply_context 关键字
     * @return
     * @throws Exception
     */
    @SentinelResource(value = "data_apply_list2", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_apply_list2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<ApplyIssueInfo>> data_apply_list2(String apply_context, String product_code, String dim_group) throws Exception {

        ApplyInfo applyInfo = new ApplyInfo();
        applyInfo.setOwner(getOwner());
        if(!StringUtils.isEmpty(apply_context)){
            apply_context = getLikeCondition(apply_context);
        }

        Map<String,List<String>> dimMap = dynamicPermissionByProductAndGroup(zdhPermissionService);
        List<ApplyIssueInfo> applyInfos = applyMapper.selectByParams3(apply_context, "1", null, null,product_code, dim_group, dimMap.get("product_codes"), dimMap.get("dim_groups"));
        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "获取申请明细", applyInfos);
    }

    /**
     * 根据主键查询数据申请信息
     * @param id 申请任务主键ID
     * @return
     * @throws Exception
     */
    @SentinelResource(value = "data_apply_list3", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_apply_list3", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<ApplyIssueInfo> data_apply_list3(String id) throws Exception {

        ApplyInfo applyInfo = new ApplyInfo();
        applyInfo.setOwner(getOwner());
        ApplyIssueInfo applyInfos = applyMapper.selectByParams4(id, "1", null, getOwner());
        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "获取申请明细", applyInfos);
    }

    /**
     * 取消申请
     * @param id
     * @return
     */
    @SentinelResource(value = "data_apply_cancel", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_apply_cancel", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> data_apply_cancel(String id) {
        try {
            applyMapper.updateStatus(id, Const.STATUS_RECALL);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "撤销申请完成", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "撤销申请失败", e);
        }
    }

    /**
     * 审批列表
     *
     * @param apply_context 关键字
     * @return
     */
    @SentinelResource(value = "data_approve_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_approve_list", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<ApplyInfo>> data_approve_list(String apply_context) throws Exception {

        ApplyInfo applyInfo = new ApplyInfo();
        applyInfo.setApprove_id(getOwner());
        if(!StringUtils.isEmpty(apply_context)){
            apply_context = getLikeCondition(apply_context);
        }
        List<ApplyInfo> applyInfos = applyMapper.selectByParams2(apply_context, getOwner(), null);
        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "获取审批明细", applyInfos);
    }

    /**
     * 数据审批
     * @param id 申请任务ID
     * @param status 审批状态
     * @return
     */
    @SentinelResource(value = "data_approve", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_approve", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo data_approve(String id, String status) {
        try {
            applyMapper.updateStatus(id, status);
            EmailJob.apply_notice();
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "审批流程结束", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "审批失败", e);
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
