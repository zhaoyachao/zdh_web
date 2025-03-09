package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.EtlTaskLogMapper;
import com.zyc.zdh.entity.EtlTaskLogInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 *日志采集服务
 */
@Controller
public class ZdhEtlLogController extends BaseController{

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EtlTaskLogMapper etlTaskLogMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;
    /**
     * 日志采集首页
     * @return
     */
    @RequestMapping("/etl_task_log_index")
    public String etl_task_log_index() {

        return "etl/etl_task_log_index";
    }

    /**
     * 获取日志任务明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "etl_task_log_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_log_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<EtlTaskLogInfo> etl_task_log_detail(String id) {
        try{
            EtlTaskLogInfo eti=etlTaskLogMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, eti.getProduct_code(), eti.getDim_group(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", eti);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }

    /**
     * 根据条件模糊查询任务信息
     * @param log_context 关键字
     * @return
     */
    @SentinelResource(value = "etl_task_log_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_log_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<EtlTaskLogInfo>> etl_task_log_list2(String log_context, String product_code, String dim_group) {
        try{
            List<EtlTaskLogInfo> list = new ArrayList<>();
            Example example = new Example(EtlTaskLogInfo.class);

            Example.Criteria criteria = example.createCriteria();
            //criteria.andEqualTo("owner", getOwner());
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            //动态增加数据权限控制
            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);
            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }

            Example.Criteria criteria2 = example.createCriteria();
            if (!StringUtils.isEmpty(log_context)) {
                criteria2.andLike("log_context", getLikeCondition(log_context));
                criteria2.orLike("data_sources_output", getLikeCondition(log_context));
                example.and(criteria2);
            }

            list = etlTaskLogMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, list);

            return ReturnInfo.buildSuccess(list);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("查询失败", e);
        }

    }

    /**
     * 批量删除日志采集任务
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "etl_task_log_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_log_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_log_delete(String[] ids) {
        try{
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskLogMapper, etlTaskLogMapper.getTable(), ids, getAttrDel());
            etlTaskLogMapper.deleteLogicByIds(etlTaskLogMapper.getTable(), ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),RETURN_CODE.SUCCESS.getDesc(), null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),e.getMessage(), null);
        }
    }

    /**
     * 新增日志采集任务首页
     * @return
     */
    @RequestMapping("/etl_task_log_add_index")
    public String etl_task_log_add() {
        return "etl/etl_task_log_add_index";
    }


    /**
     * 新增日志采集任务
     * 如果输入数据源类型是外部上传,会补充文件服务器信息
     * @param etlTasklogInfo
     * @return
     */
    @SentinelResource(value = "etl_task_log_add", blockHandler = "handleReturn")
    @RequestMapping(value="/etl_task_log_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_log_add(EtlTaskLogInfo etlTasklogInfo) {
        //String json_str=JsonUtil.formatJsonString(request.getParameterMap());
        try{
            String owner = getOwner();
            etlTasklogInfo.setOwner(owner);

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTasklogInfo.getProduct_code(), etlTasklogInfo.getDim_group(), getAttrAdd());

            etlTasklogInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
            etlTasklogInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            etlTasklogInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlTasklogInfo.setIs_delete(Const.NOT_DELETE);

            etlTaskLogMapper.insertSelective(etlTasklogInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),RETURN_CODE.SUCCESS.getDesc(), null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),e.getMessage(), null);
        }
    }

    /**
     * 日志采集任务更新
     * todo 此次是否每次都更新文件服务器信息,待优化
     * @param etlTaskLogInfo
     * @return
     */
    @SentinelResource(value = "etl_task_log_update", blockHandler = "handleReturn")
    @RequestMapping(value="/etl_task_log_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_log_update(EtlTaskLogInfo etlTaskLogInfo) {
        try{
            String owner = getOwner();
            etlTaskLogInfo.setOwner(owner);
            etlTaskLogInfo.setIs_delete(Const.NOT_DELETE);
            etlTaskLogInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            EtlTaskLogInfo oldEtlTaskLogInfo = etlTaskLogMapper.selectByPrimaryKey(etlTaskLogInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskLogInfo.getProduct_code(), etlTaskLogInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldEtlTaskLogInfo.getProduct_code(), oldEtlTaskLogInfo.getDim_group(), getAttrEdit());

            etlTaskLogMapper.updateByPrimaryKeySelective(etlTaskLogInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),RETURN_CODE.SUCCESS.getDesc(), null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            			String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),e.getMessage(), null);
        }
    }

}
