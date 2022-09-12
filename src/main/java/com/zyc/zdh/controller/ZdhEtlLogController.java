package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.EtlTaskService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.DBUtil;
import com.zyc.zdh.util.SFTPUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *日志采集服务
 */
@Controller
public class ZdhEtlLogController extends BaseController{

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    DataSourcesMapper dataSourcesMapper;
    @Autowired
    EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;
    @Autowired
    ZdhNginxMapper zdhNginxMapper;
    @Autowired
    com.zyc.zdh.dao.EtlTaskLogMapper etlTaskLogMapper;

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
    @RequestMapping(value = "/etl_task_log_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<EtlTaskLogInfo> etl_task_log_detail(String id) {
        try{
            EtlTaskLogInfo eti=etlTaskLogMapper.selectByPrimaryKey(id);
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
    @RequestMapping(value = "/etl_task_log_list", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String etl_task_log_list2(String log_context) {
        try{

            List<EtlTaskLogInfo> list = new ArrayList<>();
            Example example = new Example(EtlTaskLogInfo.class);

            Example.Criteria criteria2 = example.createCriteria();
            criteria2.andEqualTo("owner", getOwner());
            criteria2.andEqualTo("is_delete", Const.NOT_DELETE);

            Example.Criteria criteria = example.createCriteria();
            if (!StringUtils.isEmpty(log_context)) {
                criteria.andLike("log_context", getLikeCondition(log_context));
                criteria.orLike("data_sources_output", getLikeCondition(log_context));
            }
            example.and(criteria);

            list = etlTaskLogMapper.selectByExample(example);
            return JSON.toJSONString(list);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return JSON.toJSONString(e.getMessage());
        }

    }

    /**
     * 批量删除日志采集任务
     * @param ids id数组
     * @return
     */
    @RequestMapping(value = "/etl_task_log_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_log_delete(String[] ids) {
        try{
            etlTaskLogMapper.deleteLogicByIds("etl_task_log_info", ids, new Timestamp(new Date().getTime()));
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
    @RequestMapping(value="/etl_task_log_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_log_add(EtlTaskLogInfo etlTasklogInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try{
            String owner = getOwner();
            etlTasklogInfo.setOwner(owner);
            debugInfo(etlTasklogInfo);

            etlTasklogInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
            etlTasklogInfo.setCreate_time(new Timestamp(new Date().getTime()));
            etlTasklogInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTasklogInfo.setIs_delete(Const.NOT_DELETE);

            etlTaskLogMapper.insert(etlTasklogInfo);
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
    @RequestMapping(value="/etl_task_log_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_log_update(EtlTaskLogInfo etlTaskLogInfo) {
        try{
            String owner = getOwner();
            etlTaskLogInfo.setOwner(owner);
            etlTaskLogInfo.setIs_delete(Const.NOT_DELETE);
            etlTaskLogInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            debugInfo(etlTaskLogInfo);
            etlTaskLogMapper.updateByPrimaryKeySelective(etlTaskLogInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),RETURN_CODE.SUCCESS.getDesc(), null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            			String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),e.getMessage(), null);
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
