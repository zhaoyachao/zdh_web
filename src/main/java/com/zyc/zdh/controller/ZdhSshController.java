package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.jcraft.jsch.SftpException;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.DateUtil;
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
 * SSH服务
 */
@Controller
public class ZdhSshController extends BaseController{
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
    SshTaskMapper sshTaskMapper;

    /**
     * SSH 任务首页
     * @return
     */
    @RequestMapping("/etl_task_ssh_index")
    public String etl_task_ssh_index() {

        return "etl/etl_task_ssh_index";
    }

    /**
     * SSH任务新增首页
     * @return
     */
    @RequestMapping("/etl_task_ssh_add_index")
    public String etl_task_ssh_add_index() {

        return "etl/etl_task_ssh_add_index";
    }

//    /**
//     * 文件上传(废弃)
//     * @param jar_files
//     * @param jarTaskInfo
//     * @param request
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/etl_task_jar_add_file")
//    @ResponseBody
//    @Deprecated
//    public String etl_task_jar_add_file(MultipartFile[] jar_files, JarTaskInfo jarTaskInfo, HttpServletRequest request) throws Exception {
//        String json_str = JSON.toJSONString(request.getParameterMap());
//        String owner = getOwner();
//        String id = SnowflakeIdWorker.getInstance().nextId() + "";
//        jarTaskInfo.setId(id);
//        jarTaskInfo.setOwner(owner);
//        jarTaskInfo.setFiles("");
//        jarTaskInfo.setCreate_time(new Timestamp(new Date().getTime()));
//        debugInfo(jarTaskInfo);
//        jarTaskMapper.insert(jarTaskInfo);
//        System.out.println(json_str);
//        System.out.println(jar_files);
//        if (jar_files != null && jar_files.length > 0) {
//            for (MultipartFile jar_file : jar_files) {
//                String fileName = jar_file.getOriginalFilename();
//                System.out.println("上传文件不为空");
//                JarFileInfo jarFileInfo = new JarFileInfo();
//                jarFileInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
//                jarFileInfo.setJar_etl_id(id);
//                jarFileInfo.setFile_name(fileName);
//                jarFileInfo.setCreate_time(DateUtil.formatTime(new Timestamp(new Date().getTime())));
//                jarFileInfo.setOwner(owner);
//                jarFileMapper.insert(jarFileInfo);
//
//                ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(owner);
//                File tempFile = new File(zdhNginx.getTmp_dir() + "/" + owner + "/" + fileName);
//                File fileDir = new File(zdhNginx.getTmp_dir() + "/" + owner);
//                if (!fileDir.exists()) {
//                    fileDir.mkdirs();
//                }
//                String nginx_dir = zdhNginx.getNginx_dir();
//                try {
//                    FileCopyUtils.copy(jar_file.getInputStream(), Files.newOutputStream(tempFile.toPath()));
//                    if (!zdhNginx.getHost().equals("")) {
//                        System.out.println("通过sftp上传文件");
//                        SFTPUtil sftp = new SFTPUtil(zdhNginx.getUsername(), zdhNginx.getPassword(),
//                                zdhNginx.getHost(), new Integer(zdhNginx.getPort()));
//                        sftp.login();
//                        InputStream is = new FileInputStream(tempFile);
//                        sftp.upload(nginx_dir + "/" + owner + "/", fileName, is);
//                        sftp.logout();
//                    }
//                    jarFileInfo.setStatus("success");
//                    jarFileMapper.updateByPrimaryKey(jarFileInfo);
//                } catch (IOException e) {
//                     logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
//                } catch (SftpException e) {
//                     logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
//                }
//            }
//
//
//        }
//
//
//        return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
//    }


//    /**
//     * 文件更新(废弃)
//     * @param jar_files
//     * @param jarTaskInfo
//     * @param request
//     * @return
//     */
//    @RequestMapping("/etl_task_jar_update")
//    @ResponseBody
//    @Transactional(propagation= Propagation.NESTED)
//    @Deprecated
//    public String etl_task_jar_update(MultipartFile[] jar_files, JarTaskInfo jarTaskInfo, HttpServletRequest request){
//        try{
//            String json_str = JSON.toJSONString(request.getParameterMap());
//            String owner = getOwner();
//            String id =jarTaskInfo.getId();
//            jarTaskInfo.setOwner(owner);
//            debugInfo(jarTaskInfo);
//            jarTaskMapper.updateByPrimaryKey(jarTaskInfo);
//            System.out.println(json_str);
//
//            if (jar_files != null && jar_files.length > 0) {
//                for (MultipartFile jar_file : jar_files) {
//                    String fileName = jar_file.getOriginalFilename();
//                    if(fileName.isEmpty()){
//                        System.out.println("上传文件名称为空"+fileName);
//                        continue;
//                    }
//                    System.out.println("上传文件不为空"+fileName);
//                    JarFileInfo jarFileInfo = new JarFileInfo();
//                    jarFileInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
//                    jarFileInfo.setJar_etl_id(id);
//                    jarFileInfo.setFile_name(fileName);
//                    jarFileInfo.setCreate_time(DateUtil.formatTime(new Timestamp(new Date().getTime())));
//                    jarFileInfo.setOwner(owner);
//                    jarFileMapper.insert(jarFileInfo);
//
//                    ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(owner);
//                    File tempFile = new File(zdhNginx.getTmp_dir() + "/" + owner + "/" + fileName);
//                    File fileDir = new File(zdhNginx.getTmp_dir() + "/" + owner);
//                    if (!fileDir.exists()) {
//                        fileDir.mkdirs();
//                    }
//                    String nginx_dir = zdhNginx.getNginx_dir();
//                    try {
//                        FileCopyUtils.copy(jar_file.getInputStream(), Files.newOutputStream(tempFile.toPath()));
//                        if (!zdhNginx.getHost().equals("")) {
//                            System.out.println("通过sftp上传文件");
//                            SFTPUtil sftp = new SFTPUtil(zdhNginx.getUsername(), zdhNginx.getPassword(),
//                                    zdhNginx.getHost(), new Integer(zdhNginx.getPort()));
//                            sftp.login();
//                            InputStream is = new FileInputStream(tempFile);
//                            sftp.upload(nginx_dir + "/" + owner + "/", fileName, is);
//                            sftp.logout();
//                        }
//                        jarFileInfo.setStatus("success");
//                        jarFileMapper.updateByPrimaryKey(jarFileInfo);
//                    } catch (IOException e) {
//                         logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
//                        throw e;
//                    } catch (SftpException e) {
//                         logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
//                        throw e;
//                    }
//                }
//
//            }
//            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);
//        }catch (Exception e){
//            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
//            logger.error(error, e);
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(),"更新失败", e);
//        }
//
//    }


//    /**
//     * 文件删除(废弃)
//     * @param ids id数组
//     * @param request
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/etl_task_jar_del_file")
//    @ResponseBody
//    @Deprecated
//    public String etl_task_jar_del_file(String[] ids, HttpServletRequest request) throws Exception {
//        String json_str = JSON.toJSONString(request.getParameterMap());
//        String owner = getOwner();
//
//        List<JarFileInfo> jarFileInfos= jarFileMapper.selectByParams(owner,ids);
//        ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(owner);
//        String nginx_dir = zdhNginx.getNginx_dir();
//        for(JarFileInfo jarFileInfo:jarFileInfos){
//            String fileName=jarFileInfo.getFile_name();
//            if (!zdhNginx.getHost().equals("")) {
//                System.out.println("通过sftp删除文件");
//                SFTPUtil sftp = new SFTPUtil(zdhNginx.getUsername(), zdhNginx.getPassword(),
//                        zdhNginx.getHost(), new Integer(zdhNginx.getPort()));
//                sftp.login();
//
//                try {
//                    sftp.delete(nginx_dir + "/" + owner + "/", fileName);
//                } catch (SftpException e) {
//                     logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
//                }
//                sftp.logout();
//            }else{
//                File tempFile = new File(zdhNginx.getTmp_dir() + "/" + owner + "/" + fileName);
//                if(tempFile.exists()){
//                    tempFile.delete();
//                }
//            }
//            jarFileMapper.deleteByPrimaryKey(jarFileInfo.getId());
//        }
//
//
//        JSONObject json = new JSONObject();
//
//        json.put("success", "200");
//        return json.toJSONString();
//    }

//    /**
//     * 获取文件列表(废弃)
//     * @param id
//     * @param request
//     * @return
//     * @throws Exception
//     */
//    @RequestMapping("/etl_task_jar_file_list")
//    @ResponseBody
//    @Deprecated
//    public List<JarFileInfo> etl_task_jar_file_list(String id, HttpServletRequest request) throws Exception {
//        String json_str = JSON.toJSONString(request.getParameterMap());
//        String owner = getOwner();
//        JarFileInfo jarFileInfo = new JarFileInfo();
//        jarFileInfo.setOwner(owner);
//        jarFileInfo.setJar_etl_id(id);
//        List<JarFileInfo> jarFileInfos=jarFileMapper.select(jarFileInfo);
//
//        return jarFileInfos;
//    }


    /**
     * ssh任务明细
     * @param ssh_context 关键字
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/etl_task_ssh_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<SshTaskInfo>> etl_task_ssh_list(String ssh_context, String id) {
        try{
            List<SshTaskInfo> sshTaskInfos = new ArrayList<>();
            if(!StringUtils.isEmpty(ssh_context)){
                ssh_context=getLikeCondition(ssh_context);
            }
            sshTaskInfos = sshTaskMapper.selectByParams(getOwner(), ssh_context, id);

            return ReturnInfo.buildSuccess(sshTaskInfos);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("ssh任务列表查询失败", e);
        }

    }

    /**
     * 删除ssh任务
     * @param ids id数组
     * @return
     */
    @RequestMapping(value = "/etl_task_ssh_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_ssh_delete(String[] ids) {

        try{
            sshTaskMapper.deleteLogicByIds("ssh_task_info", ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }
    }

    /**
     * 新增ssh任务
     * @param sshTaskInfo
     * @param jar_files 文件
     * @return
     */
    @RequestMapping(value="/etl_task_ssh_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_ssh_add(SshTaskInfo sshTaskInfo,MultipartFile[] jar_files) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try{
            String owner = getOwner();
            sshTaskInfo.setOwner(owner);
            String id=SnowflakeIdWorker.getInstance().nextId() + "";
            sshTaskInfo.setId(id);
            sshTaskInfo.setCreate_time(new Timestamp(new Date().getTime()));
            sshTaskInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            sshTaskInfo.setIs_delete(Const.NOT_DELETE);
            debugInfo(sshTaskInfo);

            sshTaskMapper.insert(sshTaskInfo);


            if (sshTaskInfo.getUpdate_context() != null && !sshTaskInfo.getUpdate_context().equals("")) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(sshTaskInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(sshTaskInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insert(etlTaskUpdateLogs);
            }

            if (jar_files != null && jar_files.length > 0) {
                for (MultipartFile jar_file : jar_files) {
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
                    jarFileMapper.insert(jarFileInfo);

                    ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(owner);
                    File tempFile = new File(zdhNginx.getTmp_dir() + "/" + owner + "/" + fileName);
                    File fileDir = new File(zdhNginx.getTmp_dir() + "/" + owner);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    String nginx_dir = zdhNginx.getNginx_dir();
                    try {
                        FileCopyUtils.copy(jar_file.getInputStream(), Files.newOutputStream(tempFile.toPath()));
                        if (!zdhNginx.getHost().equals("")) {
                            System.out.println("通过sftp上传文件");
                            SFTPUtil sftp = new SFTPUtil(zdhNginx.getUsername(), zdhNginx.getPassword(),
                                    zdhNginx.getHost(), new Integer(zdhNginx.getPort()));
                            sftp.login();
                            InputStream is = new FileInputStream(tempFile);
                            sftp.upload(nginx_dir + "/" + owner + "/", fileName, is);
                            sftp.logout();
                        }
                        jarFileInfo.setStatus("success");
                        jarFileMapper.updateByPrimaryKey(jarFileInfo);
                    } catch (IOException e) {
                         logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                        throw e;
                    } catch (SftpException e) {
                         logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                        throw e;
                    }
                }
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
     * 更新ssh任务
     * @param sshTaskInfo
     * @param jar_files 文件
     * @return
     */
    @RequestMapping(value = "/etl_task_ssh_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo sql_task_update(SshTaskInfo sshTaskInfo,MultipartFile[] jar_files) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try{
            String owner = getOwner();
            sshTaskInfo.setOwner(owner);
            sshTaskInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            sshTaskInfo.setIs_delete(Const.NOT_DELETE);
            debugInfo(sshTaskInfo);
            String id=sshTaskInfo.getId();
            sshTaskMapper.updateByPrimaryKey(sshTaskInfo);

            SshTaskInfo sti = sshTaskMapper.selectByPrimaryKey(sshTaskInfo.getId());

            if (sshTaskInfo.getUpdate_context() != null && !sshTaskInfo.getUpdate_context().equals("")
                    && !sshTaskInfo.getUpdate_context().equals(sti.getUpdate_context())) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(sshTaskInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(sshTaskInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insert(etlTaskUpdateLogs);
            }

            if (jar_files != null && jar_files.length > 0) {
                for (MultipartFile jar_file : jar_files) {
                    String fileName = jar_file.getOriginalFilename();
                    if(fileName.isEmpty()){
                        System.out.println("上传文件名称为空"+fileName);
                        continue;
                    }
                    System.out.println("上传文件不为空"+fileName);
                    JarFileInfo jarFileInfo = new JarFileInfo();
                    jarFileInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
                    jarFileInfo.setJar_etl_id(id);
                    jarFileInfo.setFile_name(fileName);
                    jarFileInfo.setCreate_time(DateUtil.formatTime(new Timestamp(new Date().getTime())));
                    jarFileInfo.setOwner(owner);
                    jarFileMapper.insert(jarFileInfo);

                    ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(owner);
                    File tempFile = new File(zdhNginx.getTmp_dir() + "/" + owner + "/" + fileName);
                    File fileDir = new File(zdhNginx.getTmp_dir() + "/" + owner);
                    if (!fileDir.exists()) {
                        fileDir.mkdirs();
                    }
                    System.out.println("=================="+tempFile.getAbsolutePath());
                    String nginx_dir = zdhNginx.getNginx_dir();
                    try {
                        FileCopyUtils.copy(jar_file.getInputStream(), Files.newOutputStream(tempFile.toPath()));
                        if (!zdhNginx.getHost().equals("")) {
                            System.out.println("通过sftp上传文件");
                            SFTPUtil sftp = new SFTPUtil(zdhNginx.getUsername(), zdhNginx.getPassword(),
                                    zdhNginx.getHost(), new Integer(zdhNginx.getPort()));
                            sftp.login();
                            InputStream is = new FileInputStream(tempFile);
                            sftp.upload(nginx_dir + "/" + owner + "/", fileName, is);
                            sftp.logout();
                        }
                        jarFileInfo.setStatus("success");
                        jarFileMapper.updateByPrimaryKey(jarFileInfo);
                    } catch (IOException e) {
                         logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                        throw e;
                    } catch (SftpException e) {
                         logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                        throw e;
                    }
                }
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);

        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"更新失败", e);
        }
    }

    /**
     * ssh任务删除文件
     * @param ids
     * @param request
     * @return
     */
    @RequestMapping(value = "/etl_task_ssh_del_file", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_ssh_del_file(String[] ids, HttpServletRequest request) {
        try{
            String json_str = JSON.toJSONString(request.getParameterMap());
            String owner = getOwner();

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
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"删除文件成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"删除文件失败", e);
        }
    }

    /**
     * ssh任务已上传文件明细
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value = "/etl_task_ssh_file_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<JarFileInfo>> etl_task_ssh_file_list(String id, HttpServletRequest request) throws Exception {
        try{
            String json_str = JSON.toJSONString(request.getParameterMap());
            String owner = getOwner();
            JarFileInfo jarFileInfo = new JarFileInfo();
            jarFileInfo.setOwner(owner);
            jarFileInfo.setJar_etl_id(id);
            List<JarFileInfo> jarFileInfos=jarFileMapper.select(jarFileInfo);

            return ReturnInfo.buildSuccess(jarFileInfos);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("ssh任务上传文件查询失败",e);
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
