package com.zyc.zdh.controller.digitalmarket;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.StrategyGroupInstanceMapper;
import com.zyc.zdh.dao.StrategyGroupLogMapper;
import com.zyc.zdh.dao.StrategyGroupMapper;
import com.zyc.zdh.dao.StrategyInstanceMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.*;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.SFTPUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

/**
 * 智能营销-策略组服务
 */
@Controller
public class StrategyGroupController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private StrategyGroupMapper strategyGroupMapper;

    @Autowired
    private StrategyGroupInstanceMapper strategyGroupInstanceMapper;

    @Autowired
    private StrategyInstanceMapper strategyInstanceMapper;

    @Autowired
    private QuartzManager2 quartzManager2;

    @Autowired
    private Environment env;

    @Autowired
    private ZdhPermissionService zdhPermissionService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private StrategyGroupLogMapper strategyGroupLogMapper;


    @RequestMapping(value = "/get_id", method = RequestMethod.GET)
    @ResponseBody
    @White
    public String get_id() {
        return SnowflakeIdWorker.getInstance().nextId()+"";
    }

    /**
     * 策略组列表首页
     * @return
     */
    @RequestMapping(value = "/strategy_group_index", method = RequestMethod.GET)
    public String strategy_group_index() {

        return "digitalmarket/strategy_group_index";
    }


    /**
     * 策略组列表
     * @param group_context 关键字
     * @return
     */
    @SentinelResource(value = "strategy_group_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/strategy_group_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<StrategyGroupInfo>>> strategy_group_list(String group_context, String product_code, String dim_group, int limit, int offset) {
        try{

            Example example=new Example(StrategyGroupInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            //动态增加数据权限控制
            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(group_context)){
                criteria2.orLike("group_context", getLikeCondition(group_context));
                criteria2.orLike("id", getLikeCondition(group_context));
            }
            example.and(criteria2);

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = strategyGroupMapper.selectCountByExample(example);

            List<StrategyGroupInfo> strategyGroupInfos = strategyGroupMapper.selectByExampleAndRowBounds(example, rowBounds);

            PageResult<List<StrategyGroupInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(strategyGroupInfos);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pageResult);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 策略组新增首页
     * @return
     */
    @RequestMapping(value = "/strategy_group_add_index", method = RequestMethod.GET)
    public String strategy_group_add_index() {

        return "digitalmarket/strategy_group_add_index";
    }


    /**
     * 策略组明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "strategy_group_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/strategy_group_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo strategy_group_detail(String id) {
        try {
            StrategyGroupInfo strategyGroupInfo = strategyGroupMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", strategyGroupInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 策略组更新
     * @param strategyGroupInfo
     * @return
     */
    @SentinelResource(value = "strategy_group_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/strategy_group_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo strategy_group_update(StrategyGroupInfo strategyGroupInfo) {
        try {

            StrategyGroupInfo oldStrategyGroupInfo = strategyGroupMapper.selectByPrimaryKey(strategyGroupInfo.getId());

            checkPermissionByProductAndDimGroup(zdhPermissionService, strategyGroupInfo.getProduct_code(), strategyGroupInfo.getDim_group());
            checkPermissionByProductAndDimGroup(zdhPermissionService, oldStrategyGroupInfo.getProduct_code(), oldStrategyGroupInfo.getDim_group());

            //strategyGroupInfo.setRule_json(jsonArray.toJSONString());
            strategyGroupInfo.setOwner(oldStrategyGroupInfo.getOwner());
            strategyGroupInfo.setMisfire(oldStrategyGroupInfo.getMisfire());
            strategyGroupInfo.setSchedule_source(oldStrategyGroupInfo.getSchedule_source());
            strategyGroupInfo.setUse_quartz_time(oldStrategyGroupInfo.getUse_quartz_time());
            strategyGroupInfo.setCreate_time(oldStrategyGroupInfo.getCreate_time());
            strategyGroupInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            strategyGroupInfo.setIs_delete(Const.NOT_DELETE);
            strategyGroupMapper.updateByPrimaryKeySelective(strategyGroupInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", strategyGroupInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 策略组新增
     * @param strategyGroupInfo
     * @return
     */
    @SentinelResource(value = "strategy_group_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/strategy_group_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo strategy_group_add(StrategyGroupInfo strategyGroupInfo) {
        try {

            strategyGroupInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            strategyGroupInfo.setOwner(getOwner());
            strategyGroupInfo.setIs_delete(Const.NOT_DELETE);
            strategyGroupInfo.setMisfire("0");
            strategyGroupInfo.setSchedule_source(ScheduleSource.SYSTEM.getCode());
            strategyGroupInfo.setUse_quartz_time(Const.OFF);
            strategyGroupInfo.setTime_diff("0");
            strategyGroupInfo.setCreate_time(new Timestamp(new Date().getTime()));
            strategyGroupInfo.setUpdate_time(new Timestamp(new Date().getTime()));

            checkPermissionByProductAndDimGroup(zdhPermissionService, strategyGroupInfo.getProduct_code(), strategyGroupInfo.getDim_group());

            strategyGroupMapper.insertSelective(strategyGroupInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e.getMessage());
        }
    }

    /**
     * 策略组删除
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "strategy_group_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/strategy_group_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo strategy_group_delete(String[] ids) {
        try {
            checkPermissionByProductAndDimGroup(zdhPermissionService, strategyGroupMapper, strategyGroupMapper.getTable(), ids);
            strategyGroupMapper.deleteLogicByIds(strategyGroupMapper.getTable(),ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    /**
     * 策略组手动执行页面
     * @return
     */
    @RequestMapping(value = "/strategy_group_task_exe_detail_index", method = RequestMethod.GET)
    public String strategy_group_task_exe_detail_index() {

        return "digitalmarket/strategy_group_task_exe_detail_index";
    }


    /**
     * 策略组手动执行
     * @param strategyGroupInfo
     * @return
     */
    @SentinelResource(value = "strategy_group_task_execute", blockHandler = "handleReturn")
    @RequestMapping(value = "/strategy_group_task_execute", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo strategy_group_task_execute(StrategyGroupInfo strategyGroupInfo) {
        try {

            StrategyGroupInfo sgi=strategyGroupMapper.selectByPrimaryKey(strategyGroupInfo.getId());
            sgi.setStart_time(strategyGroupInfo.getStart_time());
            sgi.setCur_time(strategyGroupInfo.getStart_time());

            StrategyGroupInstance strategyGroupInstance = new StrategyGroupInstance();
            BeanUtils.copyProperties(strategyGroupInstance, sgi);

            strategyGroupInstance.setId(SnowflakeIdWorker.getInstance().nextId() + "");

            strategyGroupInstance.setStrategy_group_id(strategyGroupInfo.getId());
            strategyGroupInstance.setStatus(JobStatus.NON.getValue());
            strategyGroupInstance.setSchedule_source(ScheduleSource.MANUAL.getCode());
            strategyGroupInstance.setRun_time(new Timestamp(new Date().getTime()));//实例开始时间
            strategyGroupInstance.setCreate_time(new Timestamp(new Date().getTime()));
            strategyGroupInstance.setUpdate_time(new Timestamp(new Date().getTime()));
            strategyGroupInstance.setMisfire("0");
            if(strategyGroupInstance.getGroup_type().equalsIgnoreCase("offline")){
                strategyGroupInstance.setSmall_flow_rate("1,100");
            }

            debugInfo(strategyGroupInstance);
            strategyGroupInstanceMapper.insertSelective(strategyGroupInstance);

            List<StrategyInstance> strategyInstances=JobDigitalMarket.sub_strategy_instance(strategyGroupInstance, null);

            strategyGroupInstance.setStatus(JobStatus.CREATE.getValue());
            strategyGroupInstanceMapper.updateStatus2Create(new String[]{strategyGroupInstance.getId()});

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e.getMessage());
        }
    }


    /**
     * 策略组执行实例页面
     * @return
     */
    @RequestMapping(value = "/strategy_group_instance_index", method = RequestMethod.GET)
    public String strategy_group_instance_index() {

        return "digitalmarket/strategy_group_instance_index";
    }

    /**
     * 策略组执行列表
     * @param group_id 关键字
     * @return
     */
    @SentinelResource(value = "strategy_group_instance_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/strategy_group_instance_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<StrategyGroupInstance>>> strategy_group_instance_list(String id, String group_id,String group_context, int limit, int offset) {
        try{
            StrategyGroupInstance strategyGroupInstance = new StrategyGroupInstance();
            Example example = new Example(strategyGroupInstance.getClass());
            List<StrategyGroupInstance> strategyGroupInstances = new ArrayList<>();
            Example.Criteria cri = example.createCriteria();
            cri.andEqualTo("strategy_group_id", group_id);
            if (!StringUtils.isEmpty(group_context)) {
                Example.Criteria cri2 = example.and();
                cri2.andLike("group_context", getLikeCondition(group_context));
                cri2.orLike("jsmind_data", getLikeCondition(group_context));
                cri2.orLike("run_jsmind_data", getLikeCondition(group_context));
                cri2.orLike("owner", getLikeCondition(group_context));
            }


            example.setOrderByClause("create_time desc");
            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = strategyGroupInstanceMapper.selectCountByExample(example);

            strategyGroupInstances = strategyGroupInstanceMapper.selectByExampleAndRowBounds(example, rowBounds);

            PageResult<List<StrategyGroupInstance>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(strategyGroupInstances);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pageResult);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }


    /**
     * 获取组策略实例信息
     * @param id
     * @return
     */
    @SentinelResource(value = "strategy_group_instance_list2", blockHandler = "handleReturn")
    @RequestMapping(value = "/strategy_group_instance_list2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<StrategyGroupInstance> strategy_group_instance_list2(String id) {
        try{

            StrategyGroupInstance strategyGroupInstance = strategyGroupInstanceMapper.selectByPrimaryKey(id);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", strategyGroupInstance);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }


    /**
     * 策略实例执行日志首页
     * @return
     */
    @RequestMapping(value = "/strategy_instance_index", method = RequestMethod.GET)
    public String strategy_instance_index() {

        return "digitalmarket/strategy_instance_index";
    }

    /**
     * 获取策略组实例下的所有子策略实例
     * @param strategy_group_instance_id
     * @return
     */
    @SentinelResource(value = "strategy_instance_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/strategy_instance_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<StrategyInstance>> strategy_instance_list(String strategy_group_instance_id,String status) {
        try{
            List<StrategyInstance> strategyGroupInstances = strategyInstanceMapper.selectByGroupInstanceId(strategy_group_instance_id, status);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", strategyGroupInstances);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 策略组实例重试首页
     * @return
     */
    @RequestMapping(value = "/strategy_group_retry_detail_index", method = RequestMethod.GET)
    public String strategy_group_retry_detail_index() {

        return "digitalmarket/strategy_group_retry_detail_index";
    }

    /**
     * 策略组实例重试
     * @param strategy_group_instance_id
     * @param sub_tasks
     * @return
     */
    @SentinelResource(value = "retry_strategy_group_instance", blockHandler = "handleReturn")
    @RequestMapping(value = "/retry_strategy_group_instance", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<List<StrategyInstance>> retry_strategy_group_instance(String strategy_group_instance_id, String[] sub_tasks) {
        try{
            List<StrategyInstance> strategyInstances = strategyInstanceMapper.selectByGroupInstanceId(strategy_group_instance_id, null);
            List<String> ids=new ArrayList<>();
            List<StrategyInstance> manualConfirmStrategys=new ArrayList<>();
            for (StrategyInstance strategyInstance:strategyInstances){
                String divId = JSON.parseObject(strategyInstance.getRun_jsmind_data()).getString("divId");
                if(Arrays.asList(sub_tasks).contains(divId)){
                    ids.add(strategyInstance.getId());
                    if(strategyInstance.getInstance_type().equalsIgnoreCase(InstanceType.MANUAL_CONFIRM.getCode())){
                        strategyInstance.setIs_disenable("false");
                        strategyInstance.setStatus( JobStatus.CREATE.getValue());
                        JSONObject jsonObject = JSON.parseObject(strategyInstance.getRun_jsmind_data());
                        jsonObject.put("is_disenable","false");
                        strategyInstance.setRun_jsmind_data(jsonObject.toJSONString());
                        manualConfirmStrategys.add(strategyInstance);
                    }
                }
            }

            if(ids.size()<=0){
                throw new Exception("无法找到对应的子策略重试,请检查是否有正确选择策略实例");
            }
            strategyInstanceMapper.updateStatusByIds(ids.toArray(new String[]{}), JobStatus.CREATE.getValue());
            if(manualConfirmStrategys.size()>0){
                for (StrategyInstance strategyInstance: manualConfirmStrategys){
                    strategyInstanceMapper.updateByPrimaryKeySelective(strategyInstance);
                }
            }
            strategyGroupInstanceMapper.updateStatusById3(JobStatus.SUB_TASK_DISPATCH.getValue(), DateUtil.getCurrentTime(), strategy_group_instance_id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", strategyInstances);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 获取下载地址
     * @param id 策略实例任务id
     */
    @SentinelResource(value = "get_strategy_task_download", blockHandler = "handleReturn")
    @RequestMapping(value = "/get_strategy_task_download", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<String> get_strategy_task_download(String id) {

        //根据id 获取策略任务信息
        StrategyInstance strategyInstance = strategyInstanceMapper.selectByPrimaryKey(id);
        String basePath = "/home/data/label/";

        String url = String.format("%s/%s/%s/%s", basePath, strategyInstance.getGroup_id(),strategyInstance.getGroup_instance_id(),strategyInstance.getId());

        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "",url);
    }

    /**
     * 下载地址
     * @param id 策略实例任务id
     */
    @RequestMapping(value = "/strategy_task_download", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @White
    public void strategy_task_download(HttpServletResponse response, String id) {
        response.setHeader("content-type", "text/html;charset=UTF-8");

        response.setContentType("text/html;charset=UTF-8");
        BufferedInputStream bis = null;
        OutputStream os = null;
        try{
            //根据id 获取策略任务信息
            StrategyInstance strategyInstance = strategyInstanceMapper.selectByPrimaryKey(id);
            String basePath = "/home/data/label/";
            String dir =  String.format("%s/%s/%s", basePath, strategyInstance.getGroup_id(),strategyInstance.getGroup_instance_id());
            String fileName = strategyInstance.getId();
            String url = String.format("%s/%s/%s/%s", basePath, strategyInstance.getGroup_id(),strategyInstance.getGroup_instance_id(),strategyInstance.getId());
            byte[] buff = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};

            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode("日志_" + strategyInstance.getStrategy_context() + ".log", "UTF-8"));

            String storeType = env.getProperty("digitalmarket.store.type");

            //判断是本地存储
            if(storeType.equalsIgnoreCase("local")){
                File logFile = new File(url);
                os = response.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(logFile));
                int i = bis.read(buff);
                while (i != -1) {
                    os.write(buff, 0, buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
            }else if(storeType.equalsIgnoreCase("sftp")){
                String host = env.getProperty("digitalmarket.sftp.host");
                String port = env.getProperty("digitalmarket.sftp.port");
                String username = env.getProperty("digitalmarket.sftp.username");
                String password = env.getProperty("digitalmarket.sftp.password");

                SFTPUtil sftp = new SFTPUtil(username, password,
                        host, new Integer(port));
                sftp.login();
                byte[] buff1 = sftp.download(dir, fileName);
                os = response.getOutputStream();
                os.write(buff1, 0, buff1.length);
                os.flush();
            }
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
        }finally {
            if(os != null){
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(bis != null){
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 小流量配置页面
     * @return
     */
    @RequestMapping(value = "/small_flow_rate_index", method = RequestMethod.GET)
    public String small_flow_rate_index() {

        return "digitalmarket/small_flow_rate_index";
    }




    /**
     * 小流量更新
     * @param id
     * @param small_flow_rate
     * @return
     */
    @SentinelResource(value = "small_flow_rate_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/small_flow_rate_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo small_flow_rate_update(String id, String small_flow_rate) {
        try {

            StrategyGroupInstance oldStrategyGroupInstance = strategyGroupInstanceMapper.selectByPrimaryKey(id);

            //strategyGroupInfo.setRule_json(jsonArray.toJSONString());

            oldStrategyGroupInstance.setUpdate_time(new Timestamp(new Date().getTime()));
            oldStrategyGroupInstance.setSmall_flow_rate(small_flow_rate.replace(";",","));

            strategyGroupInstanceMapper.updateByPrimaryKeySelective(oldStrategyGroupInstance);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", oldStrategyGroupInstance);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 小流量-生效
     * @param ids
     * @return
     */
    @SentinelResource(value = "small_flow_rate_refash", blockHandler = "handleReturn")
    @RequestMapping(value = "/small_flow_rate_refash", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo small_flow_rate_refash(String[] ids) {
        try {

            Example example = new Example(StrategyGroupInstance.class);

            Example.Criteria criteria = example.createCriteria();

            criteria.andIn("id", Lists.newArrayList(ids));
            List<StrategyGroupInstance> strategyGroupInstances = strategyGroupInstanceMapper.selectByExample(example);

            String strategyGroupId="";
            Map<String, String> tmp = new HashMap<>();

            for (StrategyGroupInstance strategyGroupInstance: strategyGroupInstances){
                strategyGroupId = strategyGroupInstance.getStrategy_group_id();
                tmp.put(strategyGroupInstance.getId(), strategyGroupInstance.getSmall_flow_rate());
            }
            redisUtil.set("small_flow_rate_"+strategyGroupId, JSON.toJSONString(tmp));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 杀死策略组
     * 杀死策略组-不走策略流转逻辑(杀死整个策略组时,意味着用户想要终结当前组下所有的操作)
     * @param id 策略组实例ID
     * @return
     */
    @SentinelResource(value = "killStrategyGroup", blockHandler = "handleReturn")
    @RequestMapping(value = "/killStrategyGroup", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo killJobGroup(String id) {
        //执行中,跳过,失败的任务保留,其余策略状态都改为已杀死,策略组实例改为kill杀死中
        try {
            strategyGroupInstanceMapper.updateStatusById3(JobStatus.KILL.getValue(),DateUtil.getCurrentTime(), id);
            strategyInstanceMapper.updateStatusKilledByGroupInstanceId(id,JobStatus.KILLED.getValue());
            strategyInstanceMapper.updateStatusKillByGroupInstanceId(id,JobStatus.KILL.getValue());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "杀死任务组成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "杀死任务组失败", e);
        }
    }

    /**
     * 杀死单个任务
     *
     * @param id 任务实例ID
     * @return
     */
    @SentinelResource(value = "killStrategy", blockHandler = "handleReturn")
    @RequestMapping(value = "/killStrategy", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo killStrategy(String id) {
        //此处直接设置为已杀死,后续带优化
        try {
            strategyInstanceMapper.updateStatusKillByIds(new String[]{id});
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "杀死任务成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "杀死任务失败", e);
        }
    }

    /**
     * 手动跳过任务
     * 更新策略组状态为执行中,策略实例状态为执行中,并策略实例执行信息中is_disenable更新为true(禁用)
     * @param id 任务实例ID
     * @return
     */
    @SentinelResource(value = "strategy_skip", blockHandler = "handleReturn")
    @RequestMapping(value = "/strategy_skip", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo strategy_skip(String id) {
        try {
            StrategyInstance strategyInstance = strategyInstanceMapper.selectByPrimaryKey(id);

            StrategyGroupInstance strategyGroupInstance = strategyGroupInstanceMapper.selectByPrimaryKey(strategyInstance.getGroup_instance_id());
            //此处需要修改策略实例状态新建,策略组状态新建
            strategyInstance.setStatus(JobStatus.CREATE.getValue());
            String run_jsmind_data = strategyInstance.getRun_jsmind_data();
            JSONObject jsonObject = JSON.parseObject(run_jsmind_data);
            jsonObject.put("is_disenable","true");
            strategyInstance.setRun_jsmind_data(jsonObject.toJSONString());
            strategyInstance.setIs_disenable("true");
            strategyInstanceMapper.updateByPrimaryKeySelective(strategyInstance);
            if(strategyGroupInstance.getGroup_type().equalsIgnoreCase("offline")){
                strategyGroupInstanceMapper.updateStatusById3(JobStatus.CREATE.getValue(), DateUtil.getCurrentTime(),strategyInstance.getGroup_instance_id());
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "手动跳过任务成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "手动跳过任务失败", e);
        }
    }

    /**
     * 手动重试
     * @param id
     * @return
     */
    @SentinelResource(value = "strategy_retry", blockHandler = "handleReturn")
    @RequestMapping(value = "/strategy_retry", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo strategy_retry(String id) {
        try {
            StrategyInstance strategyInstance = strategyInstanceMapper.selectByPrimaryKey(id);
            strategyInstanceMapper.updateStatusByIds(new String[]{id},JobStatus.CREATE.getValue());
            strategyGroupInstanceMapper.updateStatusById3(JobStatus.SUB_TASK_DISPATCH.getValue(), DateUtil.getCurrentTime(), strategyInstance.getGroup_instance_id());

            //查询所有的策略实例是否包含人工确认,包含人工确认,需要修改is_disenable=false


            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "手动跳过任务成功", null);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "手动跳过任务失败", e);
        }
    }


    /**
     * 自动执行调度任务
     *
     * @param id
     * @return
     */
    @SentinelResource(value = "strategy_group_execute_quartz", blockHandler = "handleReturn")
    @RequestMapping(value = "/strategy_group_execute_quartz", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo strategy_group_execute_quartz(String id,String reset) {
        StrategyGroupInfo strategyGroupInfo = strategyGroupMapper.selectByPrimaryKey(id);
        ReturnInfo result= null;
        try {
            //添加调度器并更新quartzjobinfo
            quartzManager2.addTaskToQuartz(strategyGroupInfo);
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
     * @param strategyGroupInfo
     * @return
     */
    @SentinelResource(value = "strategy_group_quartz_pause", blockHandler = "handleReturn")
    @RequestMapping(value = "/strategy_group_quartz_pause",method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo strategy_group_quartz_pause(StrategyGroupInfo strategyGroupInfo) {

        try{

            if (strategyGroupInfo.getStatus().equals("running")) {
                //需要恢复暂停任务
                quartzManager2.resumeTask(strategyGroupInfo);
                strategyGroupMapper.updateByPrimaryKeySelective(strategyGroupInfo);
            } else {
                //暂停任务,//状态在pauseTask 方法中修改
                quartzManager2.pauseTask(strategyGroupInfo);
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
     * 删除调度任务
     *
     * @param strategyGroupInfo
     * @return
     */
    @SentinelResource(value = "strategy_group_quartz_del", blockHandler = "handleReturn")
    @RequestMapping(value = "/strategy_group_quartz_del", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo strategy_group_quartz_del(StrategyGroupInfo strategyGroupInfo) {

        try{
            quartzManager2.deleteTask(strategyGroupInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }
    }

    /**
     * 更新优先级
     * @param ids
     * @param priority
     * @return
     */
    @SentinelResource(value = "strategy_instance_priority", blockHandler = "handleReturn")
    @RequestMapping(value = "/strategy_instance_priority", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo strategy_instance_priority(String[] ids, String priority) {

        try{
            StrategyInstance strategyInstance = new StrategyInstance();
            strategyInstance.setPriority(priority);

            Example example = new Example(StrategyInstance.class);

            Example.Criteria criteria = example.createCriteria();

            criteria.andIn("group_instance_id", Lists.newArrayList(ids));

            strategyInstanceMapper.updateByExampleSelective(strategyInstance, example);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"更新失败", e);
        }
    }

    /**
     * 策略组版本保存
     * @param strategyGroupInfo
     * @return
     */
    @SentinelResource(value = "strategy_group_version_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/strategy_group_version_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo strategy_group_version_add(StrategyGroupInfo strategyGroupInfo) {
        try {

            StrategyGroupInfo oldStrategyGroupInfo = strategyGroupMapper.selectByPrimaryKey(strategyGroupInfo.getId());

            checkPermissionByProductAndDimGroup(zdhPermissionService, strategyGroupInfo.getProduct_code(), strategyGroupInfo.getDim_group());
            checkPermissionByProductAndDimGroup(zdhPermissionService, oldStrategyGroupInfo.getProduct_code(), oldStrategyGroupInfo.getDim_group());

            //strategyGroupInfo.setRule_json(jsonArray.toJSONString());
            strategyGroupInfo.setOwner(oldStrategyGroupInfo.getOwner());
            strategyGroupInfo.setMisfire(oldStrategyGroupInfo.getMisfire());
            strategyGroupInfo.setSchedule_source(oldStrategyGroupInfo.getSchedule_source());
            strategyGroupInfo.setUse_quartz_time(oldStrategyGroupInfo.getUse_quartz_time());
            strategyGroupInfo.setCreate_time(oldStrategyGroupInfo.getCreate_time());
            strategyGroupInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            strategyGroupInfo.setIs_delete(Const.NOT_DELETE);

            StrategyGroupLog strategyGroupLog = new StrategyGroupLog();
            strategyGroupLog.setLog_type("strategy_group");
            strategyGroupLog.setLog_object_id(Long.valueOf(strategyGroupInfo.getId()));

            //此处必须设置同步
            synchronized (strategyGroupInfo.getId().intern()){
                Integer log_version = strategyGroupLogMapper.selectMaxLogVersion("strategy_group", strategyGroupInfo.getId());
                if(log_version == null){
                    log_version = 1;
                }else{
                    log_version = log_version + 1;
                }
                strategyGroupLog.setLog_json(JSON.toJSONString(strategyGroupInfo));
                strategyGroupLog.setOwner(getOwner());
                strategyGroupLog.setProduct_code(strategyGroupInfo.getProduct_code());
                strategyGroupLog.setDim_group(strategyGroupInfo.getDim_group());
                strategyGroupLog.setLog_type("strategy_group");
                strategyGroupLog.setLog_version(log_version);
                strategyGroupLogMapper.insertSelective(strategyGroupLog);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e.getMessage());
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
