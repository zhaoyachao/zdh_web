package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.SftpException;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.JobCommon2;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.DBUtil;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.SFTPUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.IOUtils;
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

/**
 * 非结构化数据服务
 */
@Controller
public class ZdhUnstructureController extends BaseController{
    public Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    ZdhNginxMapper zdhNginxMapper;
    @Autowired
    EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;
    @Autowired
    JarTaskMapper jarTaskMapper;
    @Autowired
    JarFileMapper jarFileMapper;
    @Autowired
    EtlTaskUnstructureMapper etlTaskUnstructureMapper;
    @Autowired
    DataSourcesMapper dataSourcesMapper;
    @Autowired
    EtlTaskUnstructureLogMapper etlTaskUnstructureLogMapper;


    /**
     * 非结构化任务首页
     * @return
     */
    @RequestMapping("/etl_task_unstructure_index")
    public String etl_task_unstructure_index() {

        return "etl/etl_task_unstructure_index";
    }

    @RequestMapping("/etl_task_unstructure_add_index")
    public String etl_task_unstructure_add_index() {

        return "etl/etl_task_unstructure_add_index";
    }

    @RequestMapping("/etl_task_unstructure_upload_index")
    public String etl_task_unstructure_upload_index() {

        return "etl/etl_task_unstructure_upload_index";
    }

    @RequestMapping("/etl_task_unstructure_log_index")
    public String etl_task_unstructure_log_index() {

        return "etl/etl_task_unstructure_log_index";
    }

    /**
     *  非结构化任务列表
     * @param unstructure_context
     * @param id
     * @return
     */
    @RequestMapping(value = "/etl_task_unstructure_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String etl_task_unstructure_list(String unstructure_context, String id) {

        List<EtlTaskUnstructureInfo> etlTaskUnstructureInfos = new ArrayList<>();

        Example example=new Example(EtlTaskUnstructureInfo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("owner",getUser().getId());
        criteria.andEqualTo("is_delete", Const.NOT_DELETE);
        if(!StringUtils.isEmpty(id)){
            criteria.andEqualTo("id", id);
        }
        if(!StringUtils.isEmpty(unstructure_context)){
            criteria.andLike("unstructure_context", getLikeCondition(unstructure_context));
        }
        etlTaskUnstructureInfos = etlTaskUnstructureMapper.selectByExample(example);

        return JSON.toJSONString(etlTaskUnstructureInfos);
    }

    /**
     * 删除 非结构化任务
     * @param ids
     * @return
     */
    @RequestMapping(value = "/etl_task_unstructure_delete", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String etl_task_unstructure_delete(String[] ids) {

        try{
            etlTaskUnstructureMapper.deleteBatchById(ids, new Timestamp(new Date().getTime()));
            int i=1/0;
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }
    }

    /**
     * 新增 非结构化任务
     * @param etlTaskUnstructureInfo
     * @param jar_files
     * @return
     */
    @RequestMapping(value="/etl_task_unstructure_add", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String etl_task_unstructure_add(EtlTaskUnstructureInfo etlTaskUnstructureInfo,MultipartFile[] jar_files) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try{
            String owner = getUser().getId();
            etlTaskUnstructureInfo.setOwner(owner);
            String id=SnowflakeIdWorker.getInstance().nextId() + "";
            etlTaskUnstructureInfo.setId(id);
            etlTaskUnstructureInfo.setCreate_time(new Timestamp(new Date().getTime()));
            etlTaskUnstructureInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTaskUnstructureInfo.setIs_delete(Const.NOT_DELETE);
            debugInfo(etlTaskUnstructureInfo);

            etlTaskUnstructureMapper.insert(etlTaskUnstructureInfo);


            if (etlTaskUnstructureInfo.getUpdate_context() != null && !etlTaskUnstructureInfo.getUpdate_context().equals("")) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskUnstructureInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlTaskUnstructureInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
                etlTaskUpdateLogs.setOwner(getUser().getId());
                etlTaskUpdateLogsMapper.insert(etlTaskUpdateLogs);
            }

            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }

    /**
     * 更新 非结构化任务
     * @param etlTaskUnstructureInfo
     * @param jar_files
     * @return
     */
    @RequestMapping(value = "/etl_task_unstructure_update", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String sql_task_update(EtlTaskUnstructureInfo etlTaskUnstructureInfo,MultipartFile[] jar_files) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try{
            String owner = getUser().getId();
            etlTaskUnstructureInfo.setOwner(owner);
            etlTaskUnstructureInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTaskUnstructureInfo.setIs_delete(Const.NOT_DELETE);
            debugInfo(etlTaskUnstructureInfo);
            String id=etlTaskUnstructureInfo.getId();
            etlTaskUnstructureMapper.updateByPrimaryKey(etlTaskUnstructureInfo);

            EtlTaskUnstructureInfo etui = etlTaskUnstructureMapper.selectByPrimaryKey(etlTaskUnstructureInfo.getId());

            if (etlTaskUnstructureInfo.getUpdate_context() != null && !etlTaskUnstructureInfo.getUpdate_context().equals("")
                    && !etlTaskUnstructureInfo.getUpdate_context().equals(etui.getUpdate_context())) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskUnstructureInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlTaskUnstructureInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
                etlTaskUpdateLogs.setOwner(getUser().getId());
                etlTaskUpdateLogsMapper.insert(etlTaskUpdateLogs);
            }

            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);

        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"更新失败", e);
        }
    }

    /**
     * 手动上传文件-生成源信息
     * @param etlTaskUnstructureInfo
     * @param files
     * @return
     */
    @RequestMapping(value="/etl_task_unstructure_upload", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String etl_task_unstructure_upload(EtlTaskUnstructureInfo etlTaskUnstructureInfo,MultipartFile[] files) {
        String owner = getUser().getId();

        String id=etlTaskUnstructureInfo.getId();
        etlTaskUnstructureInfo = etlTaskUnstructureMapper.selectByPrimaryKey(id);
        EtlTaskUnstructureLogInfo etlTaskUnstructureLogInfo=new EtlTaskUnstructureLogInfo();
        try {
            BeanUtils.copyProperties(etlTaskUnstructureLogInfo, etlTaskUnstructureInfo);
            etlTaskUnstructureLogInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            etlTaskUnstructureLogInfo.setUnstructure_id(etlTaskUnstructureInfo.getId());
            etlTaskUnstructureLogInfo.setCreate_time(new Timestamp(new Date().getTime()));
            etlTaskUnstructureLogInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTaskUnstructureLogInfo.setIs_delete(Const.NOT_DELETE);
            etlTaskUnstructureLogInfo.setOwner(owner);
        } catch (Exception e) {
            logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
        try{

            debugInfo(etlTaskUnstructureInfo);
            //获取元数据输出源
            String data_sources_choose_jdbc_output=etlTaskUnstructureInfo.getData_sources_choose_jdbc_output();
            String data_sources_choose_file_output=etlTaskUnstructureInfo.getData_sources_choose_file_output();

            DataSourcesInfo dataSourcesInfo = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_jdbc_output);
            DataSourcesInfo fileDataSourcesInfo =  dataSourcesMapper.selectByPrimaryKey(data_sources_choose_file_output);

            List<String> etl_sqls=new ArrayList<>();
            if (files != null && files.length > 0) {
                for (MultipartFile jar_file : files) {
                    String fileName = jar_file.getOriginalFilename();
                    if(fileName==null||fileName.trim().equalsIgnoreCase("")){
                        continue;
                    }
                    System.out.println("上传文件不为空:"+fileName);
                    JarFileInfo jarFileInfo = new JarFileInfo();
                    jarFileInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
                    jarFileInfo.setJar_etl_id(id);
                    jarFileInfo.setFile_name(fileName);
                    jarFileInfo.setCreate_time(DateUtil.formatTime(new Timestamp(new Date().getTime())));
                    jarFileInfo.setOwner(owner);
                    etlTaskUnstructureInfo.setInput_path(fileName);
                    ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(owner);

                    File tempFile = new File(zdhNginx.getTmp_dir() + "/" + owner + "/" + fileName);
                    File fileDir = new File(zdhNginx.getTmp_dir() + "/" + owner);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    try {
                        FileCopyUtils.copy(jar_file.getInputStream(), Files.newOutputStream(tempFile.toPath()));
                        Dsi_Info dsiInfo=new Dsi_Info();
                        dsiInfo.setData_source_context(fileDataSourcesInfo.getData_source_context());
                        dsiInfo.setId(fileDataSourcesInfo.getId());
                        dsiInfo.setData_source_type(fileDataSourcesInfo.getData_source_type());
                        dsiInfo.setDriver(fileDataSourcesInfo.getDriver());
                        dsiInfo.setUrl(fileDataSourcesInfo.getUrl());
                        dsiInfo.setUser(fileDataSourcesInfo.getUsername());
                        dsiInfo.setPassword(fileDataSourcesInfo.getPassword());
                        if(fileDataSourcesInfo.getData_source_type().equalsIgnoreCase("sftp")){
                            InputStream is = new FileInputStream(tempFile);
                            JobCommon2.writeSftp(dsiInfo, etlTaskUnstructureInfo, IOUtils.toByteArray(is));
                        }
                        if(fileDataSourcesInfo.getData_source_type().equalsIgnoreCase("ftp")){
                            InputStream is = new FileInputStream(tempFile);
                            JobCommon2.writeFtp(dsiInfo, etlTaskUnstructureInfo, IOUtils.toByteArray(is));
                        }
                        if(fileDataSourcesInfo.getData_source_type().equalsIgnoreCase("hdfs")){
                            InputStream is = new FileInputStream(tempFile);
                            JobCommon2.writeHdfs(dsiInfo, etlTaskUnstructureInfo, IOUtils.toByteArray(is));
                        }

                    } catch (IOException e) {
                        logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                        throw e;
                    } catch (SftpException e) {
                        logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                        throw e;
                    }

                    //解析etl模板,目前先支持当前时间
                    TaskLogInstance tli=new TaskLogInstance();
                    tli.setCur_time(new Timestamp(new Date().getTime()));
                    tli.setOwner(getUser().getId());
                    etl_sqls = JobCommon2.getUnstructureEtlSql(tli, etlTaskUnstructureInfo);

                }
            }
            //入库
            String[] result = new DBUtil().CUD(dataSourcesInfo.getDriver(), dataSourcesInfo.getUrl(), dataSourcesInfo.getUsername(), dataSourcesInfo.getPassword(),
                    etl_sqls.toArray(new String[]{}));

            String msg = "目前支持以下参数,zdh_user:当前执行人,zdh_create_time:任务生成时间,zdh_date_time:调度ETL基准时间,file_name:上传文件名,file_path:文件写入路径";

            etlTaskUnstructureLogInfo.setMsg(msg+Const.LINE_SEPARATOR+StringUtils.join(etl_sqls,';'));
            if(result[0].equalsIgnoreCase("false")){
                etlTaskUnstructureLogInfo.setStatus(Const.FALSE);
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                etlTaskUnstructureLogMapper.insert(etlTaskUnstructureLogInfo);
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"新增失败", result[1]);
            }
            etlTaskUnstructureLogInfo.setStatus(Const.TRUR);
            etlTaskUnstructureLogMapper.insert(etlTaskUnstructureLogInfo);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            etlTaskUnstructureLogInfo.setStatus(Const.FALSE);
            etlTaskUnstructureLogInfo.setMsg(e.getMessage());
            etlTaskUnstructureLogMapper.insert(etlTaskUnstructureLogInfo);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }

    /**
     *  非结构化任务删除文件
     * @param ids
     * @param request
     * @return
     */
    @RequestMapping(value = "/etl_task_unstructure_del_file", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String etl_task_unstructure_del_file(String[] ids, HttpServletRequest request) {
        try{
            String json_str = JSON.toJSONString(request.getParameterMap());
            String owner = getUser().getId();

            List<JarFileInfo> jarFileInfos= jarFileMapper.selectByParams(owner,ids);
            ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(owner);
            String nginx_dir = zdhNginx.getNginx_dir();
            for(JarFileInfo jarFileInfo:jarFileInfos){
                String fileName=jarFileInfo.getFile_name();
                if (!zdhNginx.getHost().equals("")) {
                    System.out.println("通过sftp删除文件");
                    SFTPUtil sftp = new SFTPUtil(zdhNginx.getUsername(), zdhNginx.getPassword(),
                            zdhNginx.getHost(), new Integer(zdhNginx.getPort()));
                    sftp.login();

                    try {
                        sftp.delete(nginx_dir + "/" + owner + "/", fileName);
                    } catch (SftpException e) {
                         logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                    }
                    sftp.logout();
                }else{
                    File tempFile = new File(zdhNginx.getTmp_dir() + "/" + owner + "/" + fileName);
                    if(tempFile.exists()){
                        tempFile.delete();
                    }
                }
                jarFileMapper.deleteByPrimaryKey(jarFileInfo.getId());
            }
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"删除文件成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"删除文件失败", e);
        }
    }

    /**
     *  非结构化任务已上传文件明细
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/etl_task_unstructure_file_list", method = RequestMethod.GET)
    @ResponseBody
    public List<JarFileInfo> etl_task_unstructure_file_list(String id, HttpServletRequest request) {
        String json_str = JSON.toJSONString(request.getParameterMap());
        String owner = getUser().getId();
        JarFileInfo jarFileInfo = new JarFileInfo();
        jarFileInfo.setOwner(owner);
        jarFileInfo.setJar_etl_id(id);
        List<JarFileInfo> jarFileInfos=jarFileMapper.select(jarFileInfo);

        return jarFileInfos;
    }

    /**
     * 非结构化任务日志列表
     * @param id
     * @return
     */
    @RequestMapping(value = "/etl_task_unstructure_log_list", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String etl_task_unstructure_log_list(String id, String unstructure_id) {

        List<EtlTaskUnstructureLogInfo> etlTaskUnstructureInfos = new ArrayList<>();

        Example example=new Example(EtlTaskUnstructureLogInfo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("owner",getUser().getId());
        criteria.andEqualTo("is_delete", Const.NOT_DELETE);
        if(!StringUtils.isEmpty(id)){
            criteria.andEqualTo("id", id);
        }
        if(!StringUtils.isEmpty(unstructure_id)){
            criteria.andEqualTo("unstructure_id", unstructure_id);
        }


        etlTaskUnstructureInfos = etlTaskUnstructureLogMapper.selectByExample(example);

        return JSON.toJSONString(etlTaskUnstructureInfos);
    }

    /**
     * 删除 非结构化任务日志
     * @param ids
     * @return
     */
    @RequestMapping(value = "/etl_task_unstructure_log_delete", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String etl_task_unstructure_log_delete(String[] ids) {

        try{
            etlTaskUnstructureLogMapper.deleteBatchById(ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"删除失败", e);
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
