package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.EtlTaskDataxMapper;
import com.zyc.zdh.dao.EtlTaskUpdateLogsMapper;
import com.zyc.zdh.entity.EtlTaskDataxInfo;
import com.zyc.zdh.entity.EtlTaskUpdateLogs;
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

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * ETL-datax服务
 */
@Controller
public class ZdhDataxController extends BaseController{

    public Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    private EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;
    @Autowired
    private EtlTaskDataxMapper etlTaskDataxMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * datax 任务首页
     * @return
     */
    @RequestMapping("/etl_task_datax_index")
    public String etl_task_datax_index() {

        return "etl/etl_task_datax_index";
    }

    /**
     * datax任务新增首页
     * @return
     */
    @RequestMapping("/etl_task_datax_add_index")
    public String etl_task_datax_add_index() {

        return "etl/etl_task_datax_add_index";
    }


    /**
     * datax任务列表
     * @param datax_context 关键字
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "etl_task_datax_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_datax_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<EtlTaskDataxInfo>> etl_task_datax_list(String datax_context, String id) {
        try{
            EtlTaskDataxInfo etlTaskDataxInfo=new EtlTaskDataxInfo();
            Example etlTaskDataxInfoExample= new Example(etlTaskDataxInfo.getClass());
            List<EtlTaskDataxInfo> etlTaskDataxInfos = new ArrayList<>();
            Example.Criteria cri=etlTaskDataxInfoExample.createCriteria();
            if(!StringUtils.isEmpty(datax_context)){
                cri.andLike("datax_context", getLikeCondition(datax_context));
            }
            if(!StringUtils.isEmpty(id)){
                cri.andEqualTo("id", id);
            }
            //cri.andEqualTo("owner", getOwner());
            cri.andEqualTo("is_delete", Const.NOT_DELETE);

            dynamicPermissionByProductAndGroup(zdhPermissionService, cri);

            etlTaskDataxInfos = etlTaskDataxMapper.selectByExample(etlTaskDataxInfoExample);

            return ReturnInfo.buildSuccess(etlTaskDataxInfos);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("查询datax任务列表",e);
        }

    }

    /**
     * 删除datax任务
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "etl_task_datax_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_datax_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> etl_task_datax_delete(String[] ids) {

        try{
            checkPermissionByProductAndDimGroup(zdhPermissionService, etlTaskDataxMapper, etlTaskDataxMapper.getTable(), ids);
            etlTaskDataxMapper.deleteLogicByIds("etl_task_datax_info",ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }
    }

    /**
     * 新增datax任务
     * @param etlTaskDataxInfo
     * @return
     */
    @SentinelResource(value = "etl_task_datax_add", blockHandler = "handleReturn")
    @RequestMapping(value="/etl_task_datax_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_datax_add(EtlTaskDataxInfo etlTaskDataxInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try{
            String owner = getOwner();
            etlTaskDataxInfo.setOwner(owner);
            debugInfo(etlTaskDataxInfo);
            String id=SnowflakeIdWorker.getInstance().nextId()+"";
            etlTaskDataxInfo.setId(id);
            etlTaskDataxInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskDataxInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskDataxInfo.setIs_delete(Const.NOT_DELETE);

            checkPermissionByProductAndDimGroup(zdhPermissionService, etlTaskDataxInfo.getProduct_code(), etlTaskDataxInfo.getDim_group());

            etlTaskDataxMapper.insertSelective(etlTaskDataxInfo);

            if (etlTaskDataxInfo.getUpdate_context() != null && !etlTaskDataxInfo.getUpdate_context().equals("")) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskDataxInfo.getId().toString());
                etlTaskUpdateLogs.setUpdate_context(etlTaskDataxInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insertSelective(etlTaskUpdateLogs);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }

    /**
     * 更新datax任务
     * @param etlTaskDataxInfo
     * @return
     */
    @SentinelResource(value = "etl_task_datax_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_datax_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_datax_update(EtlTaskDataxInfo etlTaskDataxInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try{
            String owner = getOwner();
            etlTaskDataxInfo.setOwner(owner);
            String id=etlTaskDataxInfo.getId();
            etlTaskDataxInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskDataxInfo.setIs_delete(Const.NOT_DELETE);
            debugInfo(etlTaskDataxInfo);
            etlTaskDataxMapper.updateByPrimaryKeySelective(etlTaskDataxInfo);

            EtlTaskDataxInfo oldEtlTaskDataxInfo = etlTaskDataxMapper.selectByPrimaryKey(etlTaskDataxInfo.getId());

            checkPermissionByProductAndDimGroup(zdhPermissionService, etlTaskDataxInfo.getProduct_code(), etlTaskDataxInfo.getDim_group());
            checkPermissionByProductAndDimGroup(zdhPermissionService, oldEtlTaskDataxInfo.getProduct_code(), oldEtlTaskDataxInfo.getDim_group());

            if (etlTaskDataxInfo.getUpdate_context() != null && !etlTaskDataxInfo.getUpdate_context().equals("")
                    && !etlTaskDataxInfo.getUpdate_context().equals(oldEtlTaskDataxInfo.getUpdate_context())) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskDataxInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlTaskDataxInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insertSelective(etlTaskUpdateLogs);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);

        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"更新失败", e);
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
                     logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            }
        }
    }

}
