package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.*;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.MapStructMapper;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
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
import java.util.*;

/**
 * 调度服务
 */
@Controller
public class ZdhDispatchController extends BaseController {

    public Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private QuartzJobMapper quartzJobMapper;
    @Autowired
    private QuartzManager2 quartzManager2;
    @Autowired
    private ZdhHaInfoMapper zdhHaInfoMapper;
    @Autowired
    private TaskGroupLogInstanceMapper tglim;
    @Autowired
    private QrtzSchedulerStateMapper qrtzSchedulerStateMapper;
    @Autowired
    private QuartzExecutorMapper quartzExecutorMapper;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 调度任务首页
     *
     * @return
     */
    @RequestMapping("/dispatch_task_index")
    public String dispatch_task_index() {
        return "/etl/dispatch_task_index";
    }

    /**
     * 调度任务列表
     *
     * @param ids ids数组,可为空
     * @return
     */
    @SentinelResource(value = "dispatch_task_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/dispatch_task_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<QuartzJobInfo>> dispatch_task_list(String[] ids, String product_code, String dim_group) {
        try{
            List<QuartzJobInfo> list = new ArrayList<>();
            QuartzJobInfo quartzJobInfo = new QuartzJobInfo();
            quartzJobInfo.setOwner(getOwner());
            if (ids == null){
                Map<String,List<String>> dimMap = dynamicPermissionByProductAndGroup(zdhPermissionService);
                list = quartzJobMapper.selectByOwner(quartzJobInfo.getOwner(), product_code, dim_group, dimMap.get("product_codes"), dimMap.get("dim_groups"));
            } else {
                quartzJobInfo.setJob_id(ids[0]);
                list.add(quartzJobMapper.selectByPrimaryKey(quartzJobInfo));
            }

            return ReturnInfo.buildSuccess(list);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("查询调度任务列表失败", e);
        }

    }

    /**
     * 模糊匹配调度任务明细
     *
     * @param job_context 调度说明
     * @param etl_context etl任务说明
     * @param status 调度任务状态 create,finish,running,remove,pause
     * @param last_status etl任务状态(废弃)
     * @return
     */
    @SentinelResource(value = "dispatch_task_list2", blockHandler = "handleReturn")
    @RequestMapping(value = "/dispatch_task_list2",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<QuartzJobInfo>> dispatch_task_list2(String job_context, String etl_context, String status, String last_status, String product_code, String dim_group) {
        try{
            List<QuartzJobInfo> list = new ArrayList<>();
            if(!StringUtils.isEmpty(job_context)){
                job_context=getLikeCondition(job_context);
            }
            if(!StringUtils.isEmpty(etl_context)){
                etl_context=getLikeCondition(etl_context);
            }
            Map<String,List<String>> dimMap = dynamicPermissionByProductAndGroup(zdhPermissionService);
            list = quartzJobMapper.selectByParams(getOwner(), job_context, etl_context, status, last_status, product_code, dim_group, dimMap.get("product_codes"), dimMap.get("dim_groups"));
            return ReturnInfo.buildSuccess(list);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("调度任务列表2查询失败", e);
        }

    }

    /**
     * 新增调度任务首页
     *
     * @return
     */
    @RequestMapping("/dispatch_task_add_index")
    public String dispatch_task_add_index() {
        return "etl/dispatch_task_add_index";
    }

    /**
     * 调度手动执行页面
     * @return
     */
    @RequestMapping("/task_group_exe_detail_index")
    public String task_group_exe_detail_index() {
        return "etl/task_group_exe_detail_index";
    }

    /**
     * 调度任务新增页面
     * @return
     */
    @RequestMapping("/dispatch_task_group_add_index")
    public String dispatch_task_group_add_index() {
        return "etl/dispatch_task_group_add_index";
    }


    /**
     * hdfs任务页面
     * @return
     */
    @RequestMapping("/hdfs_detail")
    public String hdfs_detail() {
        return "etl/hdfs_detail";
    }


    /**
     * etl任务页面
     * @return
     */
    @RequestMapping("/job_detail")
    public String job_detail() {
        return "etl/job_detail";
    }

    /**
     * jdbc任务页面
     * @return
     */
    @RequestMapping("/jdbc_detail")
    public String jdbc_detail() {
        return "etl/jdbc_detail";
    }

    /**
     * 调度任务组页面
     * @return
     */
    @RequestMapping("/group_detail")
    public String group_detail() {
        return "etl/group_detail";
    }

    /**
     * shell任务页面
     * @return
     */
    @RequestMapping("/shell_detail")
    public String shell_detail() {
        return "etl/shell_detail";
    }

    /**
     * http任务页面
     * @return
     */
    @RequestMapping("/http_detail")
    public String http_detail() {
        return "etl/http_detail";
    }

    /**
     * email任务页面
     * @return
     */
    @RequestMapping("/email_detail")
    public String email_detail() {
        return "etl/email_detail";
    }

    /**
     * fluem任务页面
     * @return
     */
    @RequestMapping("/flume_detail")
    public String flume_detail() {
        return "etl/flume_detail";
    }

    /**
     * 调度执行器首页
     * @return
     */
    @RequestMapping("/dispatch_executor_index")
    public String dispatch_executor_index() {
        return "etl/dispatch_executor_index";
    }
    /**
     * 新增调度任务
     *
     * @param quartzJobInfo
     * @return
     */
    @SentinelResource(value = "dispatch_task_group_add", blockHandler = "handleReturn")
    @RequestMapping(value="/dispatch_task_group_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo dispatch_task_group_add(QuartzJobInfo quartzJobInfo) {

        try{
            debugInfo(quartzJobInfo);
            quartzJobInfo.setOwner(getOwner());
            quartzJobInfo.setJob_id(SnowflakeIdWorker.getInstance().nextId() + "");
            quartzJobInfo.setStatus("create");
            String end_expr = quartzJobInfo.getExpr().toLowerCase();
            if (end_expr.endsWith("s") || end_expr.endsWith("m")
                    || end_expr.endsWith("h")) {
                //SimpleScheduleBuilder 表达式 必须指定一个次数,默认式
                if (quartzJobInfo.getPlan_count().equals("")) {
                    quartzJobInfo.setPlan_count("3");
                }
            }
            debugInfo(quartzJobInfo);
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, quartzJobInfo.getProduct_code(), quartzJobInfo.getDim_group(), getAttrAdd());
            quartzJobMapper.insertSelective(quartzJobInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }

    /**
     * 批量删除调度任务
     *
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "dispatch_task_group_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/dispatch_task_group_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo dispatch_task_group_delete(String[] ids) {
        try{
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, quartzJobMapper, quartzJobMapper.getTable(), ids, getAttrDel());
            quartzJobMapper.deleteLogicByIds( quartzJobMapper.getTable(), ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }

    }

    /**
     * 更新调度任务
     *
     * @param quartzJobInfo
     * @return
     */
    @SentinelResource(value = "dispatch_task_group_update", blockHandler = "handleReturn")
    @RequestMapping(value="/dispatch_task_group_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo dispatch_task_group_update(QuartzJobInfo quartzJobInfo) {

        try{
            debugInfo(quartzJobInfo);
            quartzJobInfo.setOwner(getOwner());
            QuartzJobInfo oldQuartzJobInfo = quartzJobMapper.selectByPrimaryKey(quartzJobInfo);

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, quartzJobInfo.getProduct_code(), quartzJobInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldQuartzJobInfo.getProduct_code(), oldQuartzJobInfo.getDim_group(), getAttrEdit());
            //每次更新都重新设置任务实例id
            oldQuartzJobInfo.setTask_log_id(null);
            quartzJobMapper.updateByPrimaryKeySelective(quartzJobInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"更新失败", e);
        }
    }


    /**
     * 手动执行调度任务
     * 手动执行都会提前生成实例信息,串行会生成组依赖关系
     * 串行并行都需要提前选择时间,调度本身时间和手动执行时间分开处理,手动执行不能影响调度时间,
     * 手动重试一定会确定好时间
     * @param quartzJobInfo
     * @param reset_count true,false
     * @param concurrency 0:串行,1:并行
     * @return
     */
    @SentinelResource(value = "dispatch_task_execute", blockHandler = "handleReturn")
    @RequestMapping(value = "/dispatch_task_execute", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo dispatch_task_execute(QuartzJobInfo quartzJobInfo, String reset_count,String concurrency,String start_time,String end_time,String[] sub_tasks) {
        debugInfo(quartzJobInfo);

        try {
            QuartzJobInfo dti = quartzJobMapper.selectByPrimaryKey(quartzJobInfo.getJob_id());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, dti.getProduct_code(), dti.getDim_group(), getAttrEdit());
            List<Date> dates = JobCommon2.resolveQuartzExpr(quartzJobInfo.getUse_quartz_time(),dti.getStep_size(),dti.getExpr(),start_time,end_time);
            List<String> tgli_ids=new ArrayList<>();
            for(Date dt:dates){
                tgli_ids.add(SnowflakeIdWorker.getInstance().nextId() + "");
            }
            dti.setCount(0);
            //dti.setTask_log_id(null);
            //dti.setLast_time(null);
            //dti.setNext_time(null);
            quartzJobMapper.updateByPrimaryKeySelective(dti);
            for(int i=0;i<dates.size();i++){
                TaskGroupLogInstance tgli=new TaskGroupLogInstance();
                tgli = MapStructMapper.INSTANCE.quartzJobInfoToTaskGroupLogInstance(dti);
                //BeanUtils.copyProperties(tgli, dti);
                tgli.setId(tgli_ids.get(i));
                tgli.setStart_time(null);
                tgli.setEnd_time(null);
                tgli.setLast_time(null);
                tgli.setLast_task_log_id(null);
                tgli.setNext_time(null);
                tgli.setStatus(JobStatus.NON.getValue());//单独的线程扫描状态时创建并且pre_tasks 不为空的任务
                tgli.setConcurrency(concurrency);
                tgli.setRun_time(new Timestamp(System.currentTimeMillis()));
                tgli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                tgli.setCur_time(new Timestamp(dates.get(i).getTime()));
                tgli.setEtl_date(DateUtil.formatTime(new Timestamp(dates.get(i).getTime())));
                tgli.setOwner(getOwner());
                tgli.setSchedule_source(ScheduleSource.MANUAL.getCode());
                //串行生成依赖关系,并行跳过
                if(concurrency==null || concurrency.equalsIgnoreCase("0")){
                    if(i>0){
                        tgli.setPre_tasks(tgli_ids.get(i-1));
                    }
                    if(i<dates.size()-1){
                        tgli.setNext_tasks(tgli_ids.get(i+1));
                    }
                }
                JobCommon2.insertLog(tgli,"INFO","生成任务组信息,任务组数据处理日期:"+tgli.getEtl_date());
                tglim.insertSelective(tgli);
                JobCommon2.sub_task_log_instance(tgli,sub_tasks);
            }

            tglim.updateStatus2Create(tgli_ids.toArray(new String[]{}));

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"执行成功", null);

        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"执行失败", e);
        }
    }

    /**
     * 获取执行时间
     * @param quartzJobInfo
     * @param reset_count
     * @param concurrency
     * @param start_time
     * @param end_time
     * @param sub_tasks
     * @return
     */
    @SentinelResource(value = "dispatch_task_execute_time", blockHandler = "handleReturn")
    @RequestMapping(value = "/dispatch_task_execute_time",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<String>> dispatch_task_execute_time(QuartzJobInfo quartzJobInfo, String reset_count,String concurrency,String start_time,String end_time,String[] sub_tasks) {
        debugInfo(quartzJobInfo);
        System.out.println(concurrency);
        System.out.println(Arrays.toString(sub_tasks));
        JSONObject json = new JSONObject();

        try {
            QuartzJobInfo dti = quartzJobMapper.selectByPrimaryKey(quartzJobInfo.getJob_id());
            List<Date> dates = JobCommon2.resolveQuartzExpr(quartzJobInfo.getUse_quartz_time(),dti.getStep_size(),dti.getExpr(),start_time,end_time);
            List<String> result=new ArrayList<>();
            for(Date date:dates){
                result.add(DateUtil.formatTime(new Timestamp(date.getTime())));
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"执行成功", result);

        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"执行失败", e);
        }
    }

    /**
     * 自动执行调度任务
     *
     * @param quartzJobInfo
     * @return
     */
    @SentinelResource(value = "dispatch_task_execute_quartz", blockHandler = "handleReturn")
    @RequestMapping(value = "/dispatch_task_execute_quartz", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo dispatch_task_execute_quartz(QuartzJobInfo quartzJobInfo,String reset) {

        debugInfo(quartzJobInfo);

        // dispatchTaskService.update(dispatchTaskInfo);
        String url = "http://127.0.0.1:60001/api/v1/zdh";
        QuartzJobInfo dti = quartzJobMapper.selectByPrimaryKey(quartzJobInfo.getJob_id());

        dti.setCount(0);

        ReturnInfo result= null;

        //重置次数,清除上次运行日志id
        if(reset.equalsIgnoreCase("1")){
            dti.setTask_log_id(null);
            dti.setLast_time(null);
            dti.setNext_time(null);
            dti.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            quartzJobMapper.updateByPrimaryKey(dti);
        }else{
            //判断调度时间是否超过限制
            if(dti.getUse_quartz_time().equalsIgnoreCase(Const.OFF) && dti.getLast_time()!=null && dti.getLast_time().getTime() >= dti.getEnd_time().getTime()){
                result= ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"调度时间超过最大限制",null);
                return result;
            }
        }
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, dti.getProduct_code(), dti.getDim_group(), getAttrEdit());
            //添加调度器并更新quartzjobinfo
            quartzManager2.addTaskToQuartz(dti);
            result = ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"调度开启成功",null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            result=ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"调度开启失败",e);

        }


        return result;
    }


    /**
     * 暂停调度任务
     *
     * @param quartzJobInfo
     * @return
     */
    @SentinelResource(value = "dispatch_task_quartz_pause", blockHandler = "handleReturn")
    @RequestMapping(value = "/dispatch_task_quartz_pause",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo dispatch_task_quartz_pause(QuartzJobInfo quartzJobInfo) {

        try{
            debugInfo(quartzJobInfo);
            QuartzJobInfo dti = quartzJobMapper.selectByPrimaryKey(quartzJobInfo.getJob_id());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, dti.getProduct_code(), dti.getDim_group(), getAttrEdit());
            if (quartzJobInfo.getStatus().equals("running")) {
                //需要恢复暂停任务
                quartzManager2.resumeTask(dti);
                quartzJobMapper.updateStatus(quartzJobInfo.getJob_id(), quartzJobInfo.getStatus());
            } else {
                //暂停任务,//状态在pauseTask 方法中修改
                quartzManager2.pauseTask(dti);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"暂停成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"暂停失败", e);
        }

    }

    /**
     * 删除调度任务,如果是单源 ETL的流任务需要做单独处理
     *
     * @param quartzJobInfo
     * @return
     */
    @SentinelResource(value = "dispatch_task_quartz_del", blockHandler = "handleReturn")
    @RequestMapping(value = "/dispatch_task_quartz_del", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo dispatch_task_quartz_del(QuartzJobInfo quartzJobInfo) {

        try{
            QuartzJobInfo qji = quartzJobMapper.selectByPrimaryKey(quartzJobInfo);
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, qji.getProduct_code(), qji.getDim_group(), getAttrEdit());
            quartzManager2.deleteTask(qji, "remove");
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }
    }


    /**
     * 获取server实例列表
     * @return
     */
    @SentinelResource(value = "zdh_instance_list", blockHandler = "handleReturn")
    @RequestMapping(value = "zdh_instance_list", method = RequestMethod.POST)
    @ResponseBody
    public ReturnInfo<List<String>> zdh_instance_list() {
        try{
            List<String> instances = zdhHaInfoMapper.selectServerInstance();
            return ReturnInfo.buildSuccess(instances);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("查询执行server失败",e);
        }
    }

    /**
     * 调度器列表
     * @param qrtzSchedulerState
     * @return
     */
    @SentinelResource(value = "dispatch_executor_list", blockHandler = "handleReturn")
    @RequestMapping(value="/dispatch_executor_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<QrtzSchedulerState>> dispatch_executor_list(QrtzSchedulerState qrtzSchedulerState) {
        try{

            Example example=new Example(qrtzSchedulerState.getClass());
            Example.Criteria criteria=example.createCriteria();
            if(!StringUtils.isEmpty(qrtzSchedulerState.getInstance_name())){
                criteria.orLike("instance_name", getLikeCondition(qrtzSchedulerState.getInstance_name()));
            }
            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(qrtzSchedulerState.getStatus())){
                criteria2.andEqualTo("status", qrtzSchedulerState.getStatus());
                example.and(criteria2);
            }

            List<QrtzSchedulerState> qrtzSchedulerStates = qrtzSchedulerStateMapper.selectByExample(example);
            return ReturnInfo.buildSuccess(qrtzSchedulerStates);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("查询失败", e);
        }
    }

    /**
     * 更新调度器状态
     * @param instance_name
     * @param status
     * @return
     */
    @SentinelResource(value = "dispatch_executor_status", blockHandler = "handleReturn")
    @RequestMapping(value="/dispatch_executor_status", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo dispatch_executor_status(String instance_name, String status) {
        try{

            QuartzExecutorInfo qei=new QuartzExecutorInfo();
            qei.setCreate_time(new Timestamp(System.currentTimeMillis()));
            qei.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            qei.setInstance_name(instance_name);
            qei.setStatus(status);
            qei.setIs_handle(Const.FALSE);
            quartzExecutorMapper.insertSelective(qei);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }


    /**
     * 新增系统调度任务首页
     * @return
     */
    @RequestMapping("/dispatch_system_task_add_index")
    public String dispatch_system_task_add_index() {
        return "etl/dispatch_system_task_add_index";
    }

    /**
     *查询系统调度任务列表
     * @param instance_name
     * @return
     */
    @SentinelResource(value = "dispatch_system_task_list", blockHandler = "handleReturn")
    @RequestMapping(value="/dispatch_system_task_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<QuartzJobInfo>> dispatch_system_task_list(String instance_name) {
        try{

            Example example=new Example(QuartzJobInfo.class);

            Example.Criteria criteria = example.createCriteria();
            criteria.andIn("job_type", Arrays.asList(new String[]{JobType.CHECK.getCode(),JobType.EMAIL.getCode(),JobType.RETRY.getCode(),JobType.BLOOD.getCode()}));
            if(!StringUtils.isEmpty(instance_name)){
                criteria.andLike("job_context", getLikeCondition(instance_name));
            }
            List<QuartzJobInfo> quartzJobInfos = quartzJobMapper.selectByExample(example);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"查询成功", quartzJobInfos);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"查询失败", e);
        }
    }

    /**
     * 新增系统调度任务
     * @param quartzJobInfo
     * @return
     */
    @SentinelResource(value = "dispatch_system_task_add", blockHandler = "handleReturn")
    @RequestMapping(value="/dispatch_system_task_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo dispatch_system_task_add(QuartzJobInfo quartzJobInfo) {
        try{

            QuartzJobInfo qji = quartzManager2.createQuartzJobInfo(quartzJobInfo.getJob_type(),quartzJobInfo.getJob_model(),new Date(), DateUtil.pase("2999-12-31"),
                    quartzJobInfo.getJob_context(), quartzJobInfo.getExpr(),"-1","",  quartzJobInfo.getJob_type());
            qji.setAlarm_account(getUser().getUserName());
            qji.setAlarm_email("on");
            qji.setAlarm_zdh("on");
            qji.setAlarm_sms("on");
            quartzManager2.addQuartzJobInfo(qji);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }

    /**
     * 更新系统调度任务
     * @param quartzJobInfo
     * @return
     */
    @SentinelResource(value = "dispatch_system_task_update", blockHandler = "handleReturn")
    @RequestMapping(value="/dispatch_system_task_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo dispatch_system_task_update(QuartzJobInfo quartzJobInfo) {
        try{

            quartzJobMapper.updateByPrimaryKeySelective(quartzJobInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }

    /**
     * 重新创建系统调度任务
     * @param id
     * @return
     */
    @SentinelResource(value = "dispatch_system_task_create", blockHandler = "handleReturn")
    @RequestMapping(value="/dispatch_system_task_create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo dispatch_system_task_create(String id) {
        try{

            if(StringUtils.isEmpty(id)){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"新增失败", "id参数不可为空");
            }
            QuartzJobInfo qji = quartzJobMapper.selectByPrimaryKey(id);
            String sql=String.format("delete from QRTZ_SIMPLE_TRIGGERS where TRIGGER_GROUP in ('%s')", qji.getJob_type());
            String sql2=String.format("delete from QRTZ_TRIGGERS where TRIGGER_GROUP in ('%s')", qji.getJob_type());
            String sql3=String.format("delete from QRTZ_JOB_DETAILS where  JOB_GROUP in ('%s')", qji.getJob_type());
            jdbcTemplate.execute(sql);
            jdbcTemplate.execute(sql2);
            jdbcTemplate.execute(sql3);

            quartzManager2.addTaskToQuartz(qji);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }

    /**
     * 删除系统调度任务
     * @param id
     * @return
     */
    @SentinelResource(value = "dispatch_system_task_delete", blockHandler = "handleReturn")
    @RequestMapping(value="/dispatch_system_task_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo dispatch_system_task_delete(String id, String is_delete) {
        try{
            if(StringUtils.isEmpty(id)){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"禁用失败", "id参数不可为空");
            }
            QuartzJobInfo qji = quartzJobMapper.selectByPrimaryKey(id);


            if(!EnumUtils.isValidEnum(JobType.class, qji.getJob_type())){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"禁用失败", "无法识别当前任务类型:"+qji.getJob_type());
            }

            String sql=String.format("delete from QRTZ_SIMPLE_TRIGGERS where TRIGGER_GROUP in ('%s')", qji.getJob_type());
            String sql2=String.format("delete from QRTZ_TRIGGERS where TRIGGER_GROUP in ('%s')", qji.getJob_type());
            String sql3=String.format("delete from QRTZ_JOB_DETAILS where  JOB_GROUP in ('%s')", qji.getJob_type());
            jdbcTemplate.execute(sql);
            jdbcTemplate.execute(sql2);
            jdbcTemplate.execute(sql3);
            quartzManager2.deleteTask(qji, "remove");
            if(!StringUtils.isEmpty(is_delete) && is_delete.equalsIgnoreCase(Const.TRUR)){
                quartzJobMapper.deleteByPrimaryKey(id);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"禁用成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"禁用失败", e);
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
                    String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
                    logger.error(error, e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            }
        }
    }

}
