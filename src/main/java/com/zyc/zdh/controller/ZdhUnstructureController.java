package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.jcraft.jsch.SftpException;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.JobCommon2;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.*;
import org.apache.commons.io.IOUtils;
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
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 非结构化数据服务
 */
@Controller
public class ZdhUnstructureController extends BaseController{

    @Autowired
    private ZdhNginxMapper zdhNginxMapper;
    @Autowired
    private EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;
    @Autowired
    private JarFileMapper jarFileMapper;
    @Autowired
    private EtlTaskUnstructureMapper etlTaskUnstructureMapper;
    @Autowired
    private DataSourcesMapper dataSourcesMapper;
    @Autowired
    private EtlTaskUnstructureLogMapper etlTaskUnstructureLogMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 非结构化任务首页
     * @return
     */
    @RequestMapping("/etl_task_unstructure_index")
    public String etl_task_unstructure_index() {

        return "etl/etl_task_unstructure_index";
    }

    /**
     * 非结构化任务新增首页
     * @return
     */
    @RequestMapping("/etl_task_unstructure_add_index")
    public String etl_task_unstructure_add_index() {

        return "etl/etl_task_unstructure_add_index";
    }

    /**
     * 非结构化任务上传首页
     * @return
     */
    @RequestMapping("/etl_task_unstructure_upload_index")
    public String etl_task_unstructure_upload_index() {

        return "etl/etl_task_unstructure_upload_index";
    }

    /**
     * 非结构化任务日志首页
     * @return
     */
    @RequestMapping("/etl_task_unstructure_log_index")
    public String etl_task_unstructure_log_index() {

        return "etl/etl_task_unstructure_log_index";
    }

    /**
     *  非结构化任务列表
     * @param unstructure_context 关键字
     * @param id id
     * @return
     */
    @SentinelResource(value = "etl_task_unstructure_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_unstructure_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<EtlTaskUnstructureInfo>> etl_task_unstructure_list(String unstructure_context, String id, String product_code, String dim_group) {

        try{
            List<EtlTaskUnstructureInfo> etlTaskUnstructureInfos = new ArrayList<>();

            Example example=new Example(EtlTaskUnstructureInfo.class);
            Example.Criteria criteria=example.createCriteria();
            //criteria.andEqualTo("owner",getOwner());
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            //动态增加数据权限控制
            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }

            if(!StringUtils.isEmpty(id)){
                criteria.andEqualTo("id", id);
            }
            if(!StringUtils.isEmpty(unstructure_context)){
                criteria.andLike("unstructure_context", getLikeCondition(unstructure_context));
            }
            etlTaskUnstructureInfos = etlTaskUnstructureMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, etlTaskUnstructureInfos);

            return ReturnInfo.buildSuccess(etlTaskUnstructureInfos);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("查询失败", e);
        }

    }

    /**
     * 删除 非结构化任务
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "etl_task_unstructure_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_unstructure_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_unstructure_delete(String[] ids) {

        try{
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskUnstructureMapper,etlTaskUnstructureMapper.getTable(), ids, getAttrDel());
            etlTaskUnstructureMapper.deleteLogicByIds("etl_task_unstructure_info",ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }
    }

    /**
     * 新增 非结构化任务
     * @param etlTaskUnstructureInfo
     * @param jar_files 文件
     * @return
     */
    @SentinelResource(value = "etl_task_unstructure_add", blockHandler = "handleReturn")
    @RequestMapping(value="/etl_task_unstructure_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_unstructure_add(EtlTaskUnstructureInfo etlTaskUnstructureInfo,MultipartFile[] jar_files) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try{
            String owner = getOwner();
            etlTaskUnstructureInfo.setOwner(owner);
            String id=SnowflakeIdWorker.getInstance().nextId() + "";
            etlTaskUnstructureInfo.setId(id);
            etlTaskUnstructureInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskUnstructureInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskUnstructureInfo.setIs_delete(Const.NOT_DELETE);

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskUnstructureInfo.getProduct_code(), etlTaskUnstructureInfo.getDim_group(), getAttrAdd());

            etlTaskUnstructureMapper.insertSelective(etlTaskUnstructureInfo);


            if (etlTaskUnstructureInfo.getUpdate_context() != null && !etlTaskUnstructureInfo.getUpdate_context().equals("")) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskUnstructureInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlTaskUnstructureInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insertSelective(etlTaskUpdateLogs);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }

    /**
     * 更新 非结构化任务
     * @param etlTaskUnstructureInfo
     * @param jar_files 文件
     * @return
     */
    @SentinelResource(value = "etl_task_unstructure_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_unstructure_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo sql_task_update(EtlTaskUnstructureInfo etlTaskUnstructureInfo,MultipartFile[] jar_files) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try{
            String owner = getOwner();
            etlTaskUnstructureInfo.setOwner(owner);
            etlTaskUnstructureInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskUnstructureInfo.setIs_delete(Const.NOT_DELETE);

            String id=etlTaskUnstructureInfo.getId();

            EtlTaskUnstructureInfo oldEtlTaskUnstructureInfo = etlTaskUnstructureMapper.selectByPrimaryKey(etlTaskUnstructureInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskUnstructureInfo.getProduct_code(), etlTaskUnstructureInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldEtlTaskUnstructureInfo.getProduct_code(), oldEtlTaskUnstructureInfo.getDim_group(), getAttrEdit());

            etlTaskUnstructureMapper.updateByPrimaryKeySelective(etlTaskUnstructureInfo);


            if (etlTaskUnstructureInfo.getUpdate_context() != null && !etlTaskUnstructureInfo.getUpdate_context().equals("")
                    && !etlTaskUnstructureInfo.getUpdate_context().equals(oldEtlTaskUnstructureInfo.getUpdate_context())) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskUnstructureInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlTaskUnstructureInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insertSelective(etlTaskUpdateLogs);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"更新成功", null);

        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"更新失败", e);
        }
    }

    /**
     * 手动上传文件-生成源信息
     * @param etlTaskUnstructureInfo
     * @param files 文件
     * @return
     */
    @SentinelResource(value = "etl_task_unstructure_upload", blockHandler = "handleReturn")
    @RequestMapping(value="/etl_task_unstructure_upload", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_unstructure_upload(EtlTaskUnstructureInfo etlTaskUnstructureInfo,MultipartFile[] files) {
        String owner = null;
        try {
            owner = getOwner();
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }

        String id=etlTaskUnstructureInfo.getId();
        etlTaskUnstructureInfo = etlTaskUnstructureMapper.selectByPrimaryKey(id);
        EtlTaskUnstructureLogInfo etlTaskUnstructureLogInfo=new EtlTaskUnstructureLogInfo();
        try {
            if(!StringUtils.isEmpty(etlTaskUnstructureInfo.getUnstructure_params_output())) {
                //此处当做校验参数是否正常json格式
                if(!JsonUtil.isJsonValid(etlTaskUnstructureInfo.getUnstructure_params_output())){
                    throw new Exception("参数格式异常");
                }
            }
            etlTaskUnstructureLogInfo = MapStructMapper.INSTANCE.etlTaskUnstructureInfoToEtlTaskUnstructureLogInfo(etlTaskUnstructureInfo);
            //BeanUtils.copyProperties(etlTaskUnstructureLogInfo, etlTaskUnstructureInfo);
            etlTaskUnstructureLogInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            etlTaskUnstructureLogInfo.setUnstructure_id(etlTaskUnstructureInfo.getId());
            etlTaskUnstructureLogInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskUnstructureLogInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskUnstructureLogInfo.setIs_delete(Const.NOT_DELETE);
            etlTaskUnstructureLogInfo.setOwner(owner);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
        try{
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
                    jarFileInfo.setCreate_time(DateUtil.formatTime(new Timestamp(System.currentTimeMillis())));
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
                        LogUtil.error(this.getClass(), e);
                        throw e;
                    } catch (SftpException e) {
                        LogUtil.error(this.getClass(), e);
                        throw e;
                    }

                    //解析etl模板,目前先支持当前时间
                    TaskLogInstance tli=new TaskLogInstance();
                    tli.setCur_time(new Timestamp(System.currentTimeMillis()));
                    tli.setOwner(getOwner());
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
                etlTaskUnstructureLogMapper.insertSelective(etlTaskUnstructureLogInfo);
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"新增失败", result[1]);
            }
            etlTaskUnstructureLogInfo.setStatus(Const.TRUR);
            etlTaskUnstructureLogMapper.insertSelective(etlTaskUnstructureLogInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"新增成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            etlTaskUnstructureLogInfo.setStatus(Const.FALSE);
            etlTaskUnstructureLogInfo.setMsg(e.getMessage());
            etlTaskUnstructureLogMapper.insertSelective(etlTaskUnstructureLogInfo);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"新增失败", e);
        }
    }

    /**
     *  非结构化任务删除文件
     * @param ids id数组
     * @param request
     * @return
     */
    @SentinelResource(value = "etl_task_unstructure_del_file", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_unstructure_del_file", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_unstructure_del_file(String[] ids, HttpServletRequest request) {
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
     *  非结构化任务已上传文件明细
     * @param id id
     * @param request
     * @return
     */
    @SentinelResource(value = "etl_task_unstructure_file_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_unstructure_file_list", method = RequestMethod.GET)
    @ResponseBody
    public ReturnInfo<List<JarFileInfo>> etl_task_unstructure_file_list(String id, HttpServletRequest request) throws Exception {
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
            return ReturnInfo.buildError("非结构化任务上传文件查询失败", e);
        }

    }

    /**
     * 非结构化任务日志列表
     * @param id
     * @return
     */
    @SentinelResource(value = "etl_task_unstructure_log_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_unstructure_log_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<EtlTaskUnstructureLogInfo>> etl_task_unstructure_log_list(String id, String unstructure_id) {

        try{
            List<EtlTaskUnstructureLogInfo> etlTaskUnstructureInfos = new ArrayList<>();

            Example example=new Example(EtlTaskUnstructureLogInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("owner",getOwner());
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            if(!StringUtils.isEmpty(id)){
                criteria.andEqualTo("id", id);
            }
            if(!StringUtils.isEmpty(unstructure_id)){
                criteria.andEqualTo("unstructure_id", unstructure_id);
            }
            etlTaskUnstructureInfos = etlTaskUnstructureLogMapper.selectByExample(example);

            return ReturnInfo.buildSuccess(etlTaskUnstructureInfos);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("非结构化任务日志查询失败", e);
        }

    }

    /**
     * 删除 非结构化任务日志
     * @param ids
     * @return
     */
    @SentinelResource(value = "etl_task_unstructure_log_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_unstructure_log_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_unstructure_log_delete(String[] ids) {

        try{
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskUnstructureLogMapper, etlTaskUnstructureLogMapper.getTable(), ids, getAttrDel());
            etlTaskUnstructureLogMapper.deleteLogicByIds("etl_task_unstructure_log_info",ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),"删除成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"删除失败", e);
        }
    }

}
