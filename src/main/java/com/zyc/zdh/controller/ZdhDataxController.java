package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.SftpException;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.SFTPUtil;
import com.zyc.zdh.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ZdhDataxController extends BaseController{

    public Logger logger= LoggerFactory.getLogger(this.getClass());
    @Autowired
    EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;
    @Autowired
    EtlTaskDataxMapper etlTaskDataxMapper;

    /**
     * datax 任务首页
     * @return
     */
    @RequestMapping("/etl_task_datax_index")
    public String etl_task_datax_index() {

        return "etl/etl_task_datax_index";
    }

    @RequestMapping("/etl_task_datax_add_index")
    public String etl_task_datax_add_index() {

        return "etl/etl_task_datax_add_index";
    }


    /**
     * datax任务明细
     * @param datax_context
     * @param id
     * @return
     */
    @RequestMapping(value = "/etl_task_datax_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String etl_task_datax_list(String datax_context, String id) {
        EtlTaskDataxInfo etlTaskDataxInfo=new EtlTaskDataxInfo();
        Example etlTaskDataxInfoExample= new Example(etlTaskDataxInfo.getClass());
        List<EtlTaskDataxInfo> etlTaskDataxInfos = new ArrayList<>();
        Example.Criteria cri=etlTaskDataxInfoExample.createCriteria();
        if(!StringUtils.isEmpty(datax_context)){
            cri.andLike("datax_context", "%"+datax_context+"%");
        }
        if(!StringUtils.isEmpty(id)){
            cri.andEqualTo("id", id);
        }
        cri.andEqualTo("owner", getUser().getId());
        cri.andEqualTo("is_delete", Const.NOT_DELETE);
        etlTaskDataxInfos = etlTaskDataxMapper.selectByExample(etlTaskDataxInfoExample);

        return JSON.toJSONString(etlTaskDataxInfos);
    }

    /**
     * 删除datax任务
     * @param ids
     * @return
     */
    @RequestMapping(value = "/etl_task_datax_delete", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional
    public String etl_task_datax_delete(String[] ids) {

        try{
            etlTaskDataxMapper.deleteBatchById(ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }
    }

    /**
     * 新增datax任务
     * @param etlTaskDataxInfo
     * @return
     */
    @RequestMapping(value="/etl_task_datax_add", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional
    public String etl_task_datax_add(EtlTaskDataxInfo etlTaskDataxInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try{
            String owner = getUser().getId();
            etlTaskDataxInfo.setOwner(owner);
            debugInfo(etlTaskDataxInfo);
            String id=SnowflakeIdWorker.getInstance().nextId()+"";
            etlTaskDataxInfo.setId(id);
            etlTaskDataxInfo.setCreate_time(new Timestamp(new Date().getTime()));
            etlTaskDataxInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTaskDataxInfo.setIs_delete(Const.NOT_DELETE);
            etlTaskDataxMapper.insert(etlTaskDataxInfo);

            if (etlTaskDataxInfo.getUpdate_context() != null && !etlTaskDataxInfo.getUpdate_context().equals("")) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskDataxInfo.getId().toString());
                etlTaskUpdateLogs.setUpdate_context(etlTaskDataxInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
                etlTaskUpdateLogs.setOwner(getUser().getId());
                etlTaskUpdateLogsMapper.insert(etlTaskUpdateLogs);
            }

            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }

    /**
     * 更新datax任务
     * @param etlTaskDataxInfo
     * @param jar_files
     * @return
     */
    @RequestMapping(value = "/etl_task_datax_update", produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional
    public String etl_task_datax_update(EtlTaskDataxInfo etlTaskDataxInfo,MultipartFile[] jar_files) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try{
            String owner = getUser().getId();
            etlTaskDataxInfo.setOwner(owner);
            String id=etlTaskDataxInfo.getId();
            etlTaskDataxInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTaskDataxInfo.setIs_delete(Const.NOT_DELETE);
            debugInfo(etlTaskDataxInfo);
            etlTaskDataxMapper.updateByPrimaryKey(etlTaskDataxInfo);

            EtlTaskDataxInfo sti = etlTaskDataxMapper.selectByPrimaryKey(etlTaskDataxInfo.getId());

            if (etlTaskDataxInfo.getUpdate_context() != null && !etlTaskDataxInfo.getUpdate_context().equals("")
                    && !etlTaskDataxInfo.getUpdate_context().equals(sti.getUpdate_context())) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskDataxInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlTaskDataxInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
                etlTaskUpdateLogs.setOwner(getUser().getId());
                etlTaskUpdateLogsMapper.insert(etlTaskUpdateLogs);
            }

            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);

        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"更新失败", e);
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
                     logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName(), e.getCause());
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName(), e.getCause());
            }
        }
    }

}
