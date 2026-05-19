package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.jcraft.jsch.SftpException;
import com.zyc.zdh.dao.EtlTaskUpdateLogsMapper;
import com.zyc.zdh.dao.JarFileMapper;
import com.zyc.zdh.dao.SshTaskMapper;
import com.zyc.zdh.dao.ZdhNginxMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.*;
import org.apache.commons.lang3.StringUtils;
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
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * SSH服务
 */
@Controller
public class ZdhSshController extends BaseController{

    @Autowired
    private ZdhNginxMapper zdhNginxMapper;
    @Autowired
    private EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;
    @Autowired
    private JarFileMapper jarFileMapper;
    @Autowired
    private SshTaskMapper sshTaskMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;
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

    /**
     * ssh任务明细
     * @param ssh_context 关键字
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "etl_task_ssh_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_ssh_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<SshTaskInfo>> etl_task_ssh_list(String ssh_context, String id, String product_code, String dim_group) {
        try{
            List<SshTaskInfo> sshTaskInfos = new ArrayList<>();
            if(!StringUtils.isEmpty(ssh_context)){
                ssh_context=getLikeCondition(ssh_context);
            }
            Map<String,List<String>> dimMap = dynamicPermissionByProductAndGroup(zdhPermissionService);
            sshTaskInfos = sshTaskMapper.selectByParams(getOwner(), ssh_context, id, product_code, dim_group, dimMap.get("product_codes"), dimMap.get("dim_groups"));
            dynamicAuth(zdhPermissionService, sshTaskInfos);

            return ReturnInfo.buildSuccess(sshTaskInfos);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("ssh任务列表查询失败", e);
        }

    }

    /**
     * 删除ssh任务
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "etl_task_ssh_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_ssh_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_ssh_delete(String[] ids) {

        try{
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, sshTaskMapper, sshTaskMapper.getTable(), ids, getAttrDel());
            sshTaskMapper.deleteLogicByIds(sshTaskMapper.getTable(), ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
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
    @SentinelResource(value = "etl_task_ssh_add", blockHandler = "handleReturn")
    @RequestMapping(value="/etl_task_ssh_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_ssh_add(SshTaskInfo sshTaskInfo,MultipartFile[] jar_files) {
        try{
            String owner = getOwner();
            sshTaskInfo.setOwner(owner);
            String id=SnowflakeIdWorker.getInstance().nextId() + "";
            sshTaskInfo.setId(id);
            sshTaskInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            sshTaskInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            sshTaskInfo.setIs_delete(Const.NOT_DELETE);

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, sshTaskInfo.getProduct_code(), sshTaskInfo.getDim_group(), getAttrAdd());

            sshTaskMapper.insertSelective(sshTaskInfo);


            if (sshTaskInfo.getUpdate_context() != null && !sshTaskInfo.getUpdate_context().equals("")) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(sshTaskInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(sshTaskInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insertSelective(etlTaskUpdateLogs);
            }

            if (jar_files != null && jar_files.length > 0) {
                for (MultipartFile jar_file : jar_files) {
                    String fileName = MultipartFileUtil.getFileName(jar_file);
                    if(fileName==null||fileName.trim().equalsIgnoreCase("")){
                        continue;
                    }
                    System.out.println("上传文件不为空:"+fileName);
                    JarFileInfo jarFileInfo = new JarFileInfo();
                    jarFileInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
                    jarFileInfo.setJar_etl_id(id);
                    jarFileInfo.setFile_name(fileName);
                    jarFileInfo.setCreate_time(DateUtil.formatTime(new Timestamp(System.currentTimeMillis())));
                    jarFileInfo.setOwner(owner);
                    jarFileMapper.insertSelective(jarFileInfo);

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
                        jarFileMapper.updateByPrimaryKeySelective(jarFileInfo);
                    } catch (IOException e) {
                        LogUtil.error(this.getClass(), e);
                        throw e;
                    } catch (SftpException e) {
                        LogUtil.error(this.getClass(), e);
                        throw e;
                    }
                }
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
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
    @SentinelResource(value = "etl_task_ssh_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_ssh_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo sql_task_update(SshTaskInfo sshTaskInfo,MultipartFile[] jar_files) {
        try{
            String owner = getOwner();
            sshTaskInfo.setOwner(owner);
            sshTaskInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            sshTaskInfo.setIs_delete(Const.NOT_DELETE);

            String id=sshTaskInfo.getId();

            SshTaskInfo oldSshTaskInfo = sshTaskMapper.selectByPrimaryKey(sshTaskInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, sshTaskInfo.getProduct_code(), sshTaskInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldSshTaskInfo.getProduct_code(), oldSshTaskInfo.getDim_group(), getAttrEdit());

            sshTaskMapper.updateByPrimaryKeySelective(sshTaskInfo);



            if (sshTaskInfo.getUpdate_context() != null && !sshTaskInfo.getUpdate_context().equals("")
                    && !sshTaskInfo.getUpdate_context().equals(oldSshTaskInfo.getUpdate_context())) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(sshTaskInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(sshTaskInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insertSelective(etlTaskUpdateLogs);
            }

            if (jar_files != null && jar_files.length > 0) {
                for (MultipartFile jar_file : jar_files) {
                    String fileName = MultipartFileUtil.getFileName(jar_file);
                    if(fileName == null || fileName.isEmpty()){
                        System.out.println("上传文件名称为空"+fileName);
                        continue;
                    }
                    System.out.println("上传文件不为空"+fileName);
                    JarFileInfo jarFileInfo = new JarFileInfo();
                    jarFileInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
                    jarFileInfo.setJar_etl_id(id);
                    jarFileInfo.setFile_name(fileName);
                    jarFileInfo.setCreate_time(DateUtil.formatTime(new Timestamp(System.currentTimeMillis())));
                    jarFileInfo.setOwner(owner);
                    jarFileMapper.insertSelective(jarFileInfo);

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
                        jarFileMapper.updateByPrimaryKeySelective(jarFileInfo);
                    } catch (IOException e) {
                        LogUtil.error(this.getClass(), e);
                        throw e;
                    } catch (SftpException e) {
                        LogUtil.error(this.getClass(), e);
                        throw e;
                    }
                }
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);

        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
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
    @SentinelResource(value = "etl_task_ssh_del_file", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_ssh_del_file", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_ssh_del_file(String[] ids, HttpServletRequest request) {
        try{
            String json_str = JsonUtil.formatJsonString(request.getParameterMap());
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
                        LogUtil.error(this.getClass(), e);
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
            LogUtil.error(this.getClass(), e);
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
    @SentinelResource(value = "etl_task_ssh_file_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_ssh_file_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<JarFileInfo>> etl_task_ssh_file_list(String id, HttpServletRequest request) throws Exception {
        try{
            String json_str = JsonUtil.formatJsonString(request.getParameterMap());
            String owner = getOwner();
            JarFileInfo jarFileInfo = new JarFileInfo();
            jarFileInfo.setOwner(owner);
            jarFileInfo.setJar_etl_id(id);
            List<JarFileInfo> jarFileInfos=jarFileMapper.select(jarFileInfo);

            return ReturnInfo.buildSuccess(jarFileInfos);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("ssh任务上传文件查询失败",e);
        }
    }

}
