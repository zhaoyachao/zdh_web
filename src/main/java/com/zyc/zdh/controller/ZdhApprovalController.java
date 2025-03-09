package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.ApprovalAuditorFlowMapper;
import com.zyc.zdh.dao.ApprovalAuditorMapper;
import com.zyc.zdh.dao.ApprovalConfigMapper;
import com.zyc.zdh.dao.ApprovalEventMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
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

import java.sql.Timestamp;
import java.util.List;

/**
 * 审批流服务
 */
@Controller
public class ZdhApprovalController extends BaseController{

    public Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private ApprovalConfigMapper approvalConfigMapper;

    @Autowired
    private ApprovalAuditorMapper approvalAuditorMapper;

    @Autowired
    private ApprovalEventMapper approvalEventMapper;

    @Autowired
    private ApprovalAuditorFlowMapper approvalAuditorFlowMapper;

    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 审批节点首页
     * @return
     */
    @RequestMapping("/approval_config_index")
    public String approve_config_index() {

        return "admin/approval_config_index";
    }


    /**
     * 审批节点配置
     * @param approval_context 关键字
     * @return
     */
    @SentinelResource(value = "approval_config_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_config_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<ApprovalConfigInfo>> approve_config_list(String product_code,String approval_context) {
        try{
            //checkPermissionByOwner(product_code);
            Example example=new Example(ApprovalConfigInfo.class);
            Example.Criteria criteria = example.createCriteria();
            //动态增加数据权限控制
            dynamicPermissionByProductAndFilterProduct(zdhPermissionService, criteria, product_code);

            if(!StringUtils.isEmpty(approval_context)){
                criteria.orLike("code", getLikeCondition(approval_context));
                criteria.orLike("code_name", getLikeCondition(approval_context));
                criteria.orLike("id", getLikeCondition(approval_context));
            }
            List<ApprovalConfigInfo> approvalConfigInfos=approvalConfigMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, approvalConfigInfos);

            return ReturnInfo.buildSuccess(approvalConfigInfos);
        }catch (Exception e){
            logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.buildError(e);
        }
    }


    /**
     * 审批节点新增页面
     * @return
     */
    @RequestMapping("/approval_config_add_index")
    public String approval_config_add_index() {

        return "admin/approval_config_add_index";
    }

    /**
     * 审批节点明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "approval_config_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_config_detail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<ApprovalConfigInfo> approval_config_detail(String id) {
        try{
            ApprovalConfigInfo approvalConfigInfo=approvalConfigMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService, approvalConfigInfo.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", approvalConfigInfo);
        }catch (Exception e){
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }


    /**
     * 审批节点新增
     * @param approvalConfigInfo
     * @return
     */
    @SentinelResource(value = "approval_config_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_config_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> approval_config_add(ApprovalConfigInfo approvalConfigInfo) {
        try{
            List<ApprovalConfigInfo> approvalConfigInfos=approvalConfigMapper.select(approvalConfigInfo);
            if(approvalConfigInfos!=null && approvalConfigInfos.size()>0){
                throw new Exception("审批节点以存在");
            }
            approvalConfigInfo.setEmployee_id(getOwner());
            approvalConfigInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProduct(zdhPermissionService, approvalConfigInfo.getProduct_code(), getAttrAdd());

            approvalConfigMapper.insertSelective(approvalConfigInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        }catch (Exception e){
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 审批节点更新
     * @param approvalConfigInfo
     * @return
     */
    @SentinelResource(value = "approval_config_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_config_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> approval_config_update(ApprovalConfigInfo approvalConfigInfo) {
        try{
            ApprovalConfigInfo oldApprovalConfigInfo=approvalConfigMapper.selectByPrimaryKey(approvalConfigInfo.getId());

            checkAttrPermissionByProduct(zdhPermissionService, approvalConfigInfo.getProduct_code(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService, oldApprovalConfigInfo.getProduct_code(), getAttrEdit());

            approvalConfigInfo.setEmployee_id(getOwner());
            approvalConfigInfo.setCreate_time(oldApprovalConfigInfo.getCreate_time());
            approvalConfigMapper.updateByPrimaryKeySelective(approvalConfigInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 审批人首页
     * @return
     */
    @RequestMapping("/approval_auditor_index")
    public String approval_auditor_index() {

        return "admin/approval_auditor_index";
    }

    /**
     * 审批人列表
     * @param approval_context 关键字
     * @param auditor_id 审批人账号
     * @return
     */
    @SentinelResource(value = "approval_auditor_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_auditor_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<ApprovalAuditorInfo>> approval_auditor_list(String approval_context, String auditor_id) {
        try{
            if(!StringUtils.isEmpty(approval_context)){
                approval_context = getLikeCondition(approval_context);
            }
            List<ApprovalAuditorInfo> approvalAuditorInfos=approvalAuditorMapper.selectByContext(approval_context, auditor_id);
            return ReturnInfo.buildSuccess(approvalAuditorInfos);
        }catch (Exception e){
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.buildError(e);
        }
    }

    /**
     * 审批人新增首页
     * @return
     */
    @RequestMapping("/approval_auditor_add_index")
    public String approval_auditor_add_index() {

        return "admin/approval_auditor_add_index";
    }


    /**
     * 审批流首页
     * @return
     */
    @RequestMapping("/approval_auditor_flow_index")
    public String approval_auditor_flow_index() {

        return "admin/approval_auditor_flow_index";
    }

    /**
     * 审批流新增首页
     * @return
     */
    @RequestMapping("/approval_auditor_flow_add_index")
    public String approval_auditor_flow_add_index() {

        return "admin/approval_auditor_flow_add_index";
    }


    /**
     * 审批人新增
     * @param approvalAuditorInfo
     * @return
     */
    @SentinelResource(value = "approval_auditor_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_auditor_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<ApprovalAuditorInfo> approval_auditor_add(ApprovalAuditorInfo approvalAuditorInfo) {
        try{

            checkAttrPermissionByProduct(zdhPermissionService, approvalAuditorInfo.getProduct_code(), getAttrAdd());

            List<ApprovalAuditorInfo> approvalAuditorInfos=approvalAuditorMapper.select(approvalAuditorInfo);
            if(approvalAuditorInfos!=null && approvalAuditorInfos.size()>0){
                throw new Exception("审批节点以存在");
            }
            approvalAuditorInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            approvalAuditorInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            approvalAuditorMapper.insertSelective(approvalAuditorInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", approvalAuditorInfo);
        }catch (Exception e){
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 审批人说明
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "approval_auditor_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_auditor_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> approval_auditor_delete(String[] ids) {
        try{
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, approvalAuditorMapper,approvalAuditorMapper.getTable(), ids, getAttrDel());
            approvalAuditorMapper.deleteByPrimaryKeys(ids);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }

    /**
     * 审批人更新
     * @param approvalAuditorInfo
     * @return
     */
    @SentinelResource(value = "approval_auditor_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_auditor_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<ApprovalAuditorInfo> approval_auditor_update(ApprovalAuditorInfo approvalAuditorInfo) {
        try{
            ApprovalAuditorInfo old=approvalAuditorMapper.selectByPrimaryKey(approvalAuditorInfo.getId());

            checkAttrPermissionByProduct(zdhPermissionService, approvalAuditorInfo.getProduct_code(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService, old.getProduct_code(), getAttrEdit());

            approvalAuditorInfo.setCreate_time(old.getCreate_time());
            approvalAuditorMapper.updateByPrimaryKeySelective(approvalAuditorInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", approvalAuditorInfo);
        }catch (Exception e){
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 审批人明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "approval_auditor_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_auditor_detail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<ApprovalAuditorInfo> approval_auditor_detail(String id) {
        try{
            ApprovalAuditorInfo approvalAuditorInfo=approvalAuditorMapper.selectById(id);
            checkAttrPermissionByProduct(zdhPermissionService, approvalAuditorInfo.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", approvalAuditorInfo);
        }catch (Exception e){
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }


    /**
     * 审批流列表
     * @param flow_context 关键字
     * @return
     */
    @SentinelResource(value = "approval_auditor_flow_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_auditor_flow_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<ApprovalAuditorFlowInfo>>> approval_auditor_flow_list(String flow_context, int limit, int offset, String product_code) {
        try{

            Example example=new Example(ApprovalAuditorFlowInfo.class);
            Example.Criteria criteria=example.createCriteria();

            dynamicPermissionByProductAndFilterProduct(zdhPermissionService, criteria, product_code);

            if(!StringUtils.isEmpty(flow_context)){
                flow_context = getLikeCondition(flow_context);
                criteria.andLike("flow_context", flow_context);
            }

            example.setOrderByClause("id desc");
            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = approvalAuditorFlowMapper.selectCountByExample(example);

            List<ApprovalAuditorFlowInfo> approvalAuditorFlowInfos=approvalAuditorFlowMapper.selectByExampleAndRowBounds(example, rowBounds);

            PageResult<List<ApprovalAuditorFlowInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(approvalAuditorFlowInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch (Exception e){
            logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.buildError(e);
        }
    }


    /**
     * 审批流新增
     * @param approvalAuditorFlowInfo
     * @return
     */
    @SentinelResource(value = "approval_auditor_flow_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_auditor_flow_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<ApprovalAuditorFlowInfo> approval_auditor_flow_add(ApprovalAuditorFlowInfo approvalAuditorFlowInfo) {
        try{
            checkPermissionByProduct(zdhPermissionService, approvalAuditorFlowInfo.getProduct_code());

            List<ApprovalAuditorFlowInfo> approvalAuditorFlowInfos=approvalAuditorFlowMapper.select(approvalAuditorFlowInfo);
            if(approvalAuditorFlowInfos!=null && approvalAuditorFlowInfos.size()>0){
                throw new Exception("审批节点以存在");
            }
            approvalAuditorFlowInfo.setOwner(getOwner());
            approvalAuditorFlowInfo.setIs_delete(Const.NOT_DELETE);
            approvalAuditorFlowInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            approvalAuditorFlowInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            approvalAuditorFlowInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            approvalAuditorFlowMapper.insertSelective(approvalAuditorFlowInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", approvalAuditorFlowInfo);
        }catch (Exception e){
            logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 审批流删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "approval_auditor_flow_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_auditor_flow_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> approval_auditor_flow_delete(String[] ids) {
        try{
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, approvalAuditorFlowMapper, approvalAuditorFlowMapper.getTable(), ids, getAttrDel());
            approvalAuditorFlowMapper.deleteLogicByIds("approval_auditor_flow_info", ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }

    /**
     * 审批流-更新
     * @param approvalAuditorFlowInfo
     * @return
     */
    @SentinelResource(value = "approval_auditor_flow_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_auditor_flow_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<ApprovalAuditorFlowInfo> approval_auditor_flow_update(ApprovalAuditorFlowInfo approvalAuditorFlowInfo) {
        try{
            ApprovalAuditorFlowInfo old = approvalAuditorFlowMapper.selectByPrimaryKey(approvalAuditorFlowInfo.getId());

            checkAttrPermissionByProduct(zdhPermissionService, approvalAuditorFlowInfo.getProduct_code(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService, old.getProduct_code(), getAttrEdit());

            approvalAuditorFlowInfo.setCreate_time(old.getCreate_time());
            approvalAuditorFlowInfo.setOwner(old.getOwner());
            approvalAuditorFlowInfo.setIs_delete(Const.NOT_DELETE);
            approvalAuditorFlowInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            approvalAuditorFlowMapper.updateByPrimaryKeySelective(approvalAuditorFlowInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", approvalAuditorFlowInfo);
        }catch (Exception e){
            logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 审批流明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "approval_auditor_flow_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_auditor_flow_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<ApprovalAuditorFlowInfo> approval_auditor_flow_detail(String id) {
        try{
            ApprovalAuditorFlowInfo approvalAuditorFlowInfo=approvalAuditorFlowMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService, approvalAuditorFlowInfo.getProduct_code(), getAttrSelect());

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", approvalAuditorFlowInfo);
        }catch (Exception e){
            logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }


    /**
     * 审批事件首页(废弃,使用审批流代替)
     * @return
     */
    @RequestMapping("/approval_event_index")
    public String approval_event_index() {

        return "admin/approval_event_index";
    }

    /**
     * 审批事件新增首页(废弃,使用审批流代替)
     * @return
     */
    @RequestMapping("/approval_event_add_index")
    public String approval_event_add_index() {

        return "admin/approval_event_add_index";
    }


    /**
     * 审批事件新增
     * @param approvalEventInfo
     * @return
     */
    @SentinelResource(value = "approval_event_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_event_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Object> approval_event_add(ApprovalEventInfo approvalEventInfo) {
        try{
            checkAttrPermissionByProduct(zdhPermissionService, approvalEventInfo.getProduct_code(), getAttrAdd());

            List<ApprovalEventInfo> approvalEventInfos=approvalEventMapper.select(approvalEventInfo);
            if(approvalEventInfos!=null && approvalEventInfos.size()>0){
                throw new Exception("审批事件已存在");
            }
            approvalEventInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            approvalEventInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            approvalEventMapper.insertSelective(approvalEventInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        }catch (Exception e){
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 审批事件明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "approval_event_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_event_detail", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<ApprovalEventInfo> approval_event_detail(String id) {
        try{
            ApprovalEventInfo approvalEventInfo=approvalEventMapper.selectById(id);
            checkAttrPermissionByProduct(zdhPermissionService, approvalEventInfo.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", approvalEventInfo);
        }catch (Exception e){
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 审批事件列表
     * @param event_context 关键字
     * @return
     */
    @SentinelResource(value = "approval_event_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_event_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<ApprovalEventInfo>> approval_event_list(String product_code,String event_context) {
        try{
            List<String> product_codes = getPermissionByProduct(zdhPermissionService);
            //checkPermissionByOwner(product_code);
            if(!StringUtils.isEmpty(event_context)){
                event_context = getLikeCondition(event_context);
            }
            List<ApprovalEventInfo> approvalEventInfos=approvalEventMapper.selectByContext(event_context, product_codes);
            dynamicAuth(zdhPermissionService, approvalEventInfos);

            return ReturnInfo.buildSuccess(approvalEventInfos);
        }catch (Exception e){
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.buildError(e);
        }
    }

    /**
     * 审批事件更新
     * @param approvalEventInfo
     * @return
     */
    @SentinelResource(value = "approval_event_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/approval_event_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<ApprovalEventInfo> approval_event_update(ApprovalEventInfo approvalEventInfo) {
        try{
            ApprovalEventInfo old = approvalEventMapper.selectByPrimaryKey(approvalEventInfo.getId());

            checkAttrPermissionByProduct(zdhPermissionService, approvalEventInfo.getProduct_code(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService, old.getProduct_code(), getAttrEdit());

            approvalEventInfo.setCreate_time(old.getCreate_time());
            approvalEventInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            approvalEventMapper.updateByPrimaryKeySelective(approvalEventInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", approvalEventInfo);
        }catch (Exception e){
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

}
