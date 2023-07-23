package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.DataSourcesMapper;
import com.zyc.zdh.dao.EtlTaskMapper;
import com.zyc.zdh.dao.EtlTaskUpdateLogsMapper;
import com.zyc.zdh.dao.ZdhNginxMapper;
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
 * 单源ETL任务服务
 */
@Controller
public class ZdhEtlController extends BaseController{

    public Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    DataSourcesMapper dataSourcesMapper;
    @Autowired
    EtlTaskService etlTaskService;
    @Autowired
    EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;
    @Autowired
    ZdhNginxMapper zdhNginxMapper;
    @Autowired
    EtlTaskMapper etlTaskMapper;

    /**
     * 单源ETL首页
     * @return
     */
    @RequestMapping("/etl_task_index")
    public String etl_task_index() {

        return "etl/etl_task_index";
    }

    /**
     * 获取单源ETL任务明细
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/etl_task_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<EtlTaskInfo> etl_task_detail(String id) {
        try{
            EtlTaskInfo eti=etlTaskService.selectById(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", eti);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }

    /**
     * 根据条件模糊查询单源ETL任务信息
     * @param etl_context 关键字
     * @param file_name 数据源关键字
     * @return
     */
    @RequestMapping(value = "/etl_task_list2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<EtlTaskInfo>> etl_task_list2(String etl_context, String file_name) {
        try{
            List<EtlTaskInfo> list = new ArrayList<>();
            if(!StringUtils.isEmpty(etl_context)){
                etl_context=getLikeCondition(etl_context);
            }
            if(!StringUtils.isEmpty(file_name)){
                file_name=getLikeCondition(file_name);
            }
            list = etlTaskMapper.selectByParams(getOwner(),etl_context,file_name);
            return ReturnInfo.buildSuccess(list);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("单源ETL任务列表查询失败", e);
        }

    }

    /**
     * 批量删除单源ETL任务
     * @param ids id数组
     * @return
     */
    @RequestMapping(value = "/etl_task_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_delete(String[] ids) {
        try{
            etlTaskMapper.deleteLogicByIds("etl_task_info", ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),RETURN_CODE.SUCCESS.getDesc(), null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),e.getMessage(), null);
        }
    }

    /**
     * 新增单源ETL任务首页
     * @param request
     * @param edit 废弃
     * @return
     */
    @RequestMapping("/etl_task_add_index")
    public String etl_task_add(HttpServletRequest request, String edit) {
        return "etl/etl_task_add_index";
    }


    /**
     * 新增单源ETL任务
     * 如果输入数据源类型是外部上传,会补充文件服务器信息
     * @param etlTaskInfo
     * @return
     */
    @RequestMapping(value="/etl_task_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_add(EtlTaskInfo etlTaskInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try{
            String owner = getOwner();
            etlTaskInfo.setOwner(owner);
            debugInfo(etlTaskInfo);

            etlTaskInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
            etlTaskInfo.setCreate_time(new Timestamp(new Date().getTime()));
            etlTaskInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTaskInfo.setIs_delete(Const.NOT_DELETE);
            if (etlTaskInfo.getData_source_type_input().equals("外部上传")) {
                ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(owner);
                if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                    etlTaskInfo.setData_sources_file_name_input(zdhNginx.getNginx_dir() + "/" + owner + "/" + etlTaskInfo.getData_sources_file_name_input());
                } else {
                    etlTaskInfo.setData_sources_file_name_input(zdhNginx.getTmp_dir() + "/" + owner + "/" + etlTaskInfo.getData_sources_file_name_input());
                }

            }

            etlTaskService.insert(etlTaskInfo);
            if (etlTaskInfo.getUpdate_context() != null && !etlTaskInfo.getUpdate_context().equals("")) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlTaskInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insert(etlTaskUpdateLogs);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),RETURN_CODE.SUCCESS.getDesc(), null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),e.getMessage(), null);
        }
    }


    /**
     * 单源ETL任务输入数据源是外部上传时,上传文件服务
     * @param up_file 上传文件
     * @param request 请求回话
     * @return
     */
    @RequestMapping(value="/etl_task_add_file", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo etl_task_add_file(MultipartFile up_file, HttpServletRequest request) {
        try{
            String json_str = JSON.toJSONString(request.getParameterMap());
            String owner = getOwner();
            System.out.println(json_str);
            System.out.println(up_file);
            if (up_file != null) {
                String fileName = up_file.getOriginalFilename();
                System.out.println("上传文件不为空");
                ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(owner);
                File tempFile = new File(zdhNginx.getTmp_dir() + "/" + owner + "/" + fileName);
                File fileDir = new File(zdhNginx.getTmp_dir() + "/" + owner);
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                String nginx_dir = zdhNginx.getNginx_dir();

                FileCopyUtils.copy(up_file.getInputStream(), Files.newOutputStream(tempFile.toPath()));
                if (!zdhNginx.getHost().equals("")) {
                    System.out.println("通过sftp上传文件");
                    SFTPUtil sftp = new SFTPUtil(zdhNginx.getUsername(), zdhNginx.getPassword(),
                            zdhNginx.getHost(), new Integer(zdhNginx.getPort()));
                    sftp.login();
                    InputStream is = new FileInputStream(tempFile);
                    sftp.upload(nginx_dir + "/" + owner + "/", fileName, is);
                    sftp.logout();
                }
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),RETURN_CODE.SUCCESS.getDesc(), null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),e.getMessage(), null);
        }
    }

    /**
     * 单源ETL任务更新
     * todo 此次是否每次都更新文件服务器信息,待优化
     * @param etlTaskInfo
     * @return
     */
    @RequestMapping(value="/etl_task_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_update(EtlTaskInfo etlTaskInfo) {
        try{
            String owner = getOwner();
            etlTaskInfo.setOwner(owner);
            etlTaskInfo.setIs_delete(Const.NOT_DELETE);
            etlTaskInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            debugInfo(etlTaskInfo);
            if (etlTaskInfo.getData_source_type_input().equals("外部上传")) {
                ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(owner);
                String[] file_name_ary = etlTaskInfo.getData_sources_file_name_input().split("/");
                String file_name = file_name_ary[0];
                if (file_name_ary.length > 0)
                    file_name = file_name_ary[file_name_ary.length - 1];

                if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                    etlTaskInfo.setData_sources_file_name_input(zdhNginx.getNginx_dir() + "/" + owner + "/" + file_name);
                } else {
                    etlTaskInfo.setData_sources_file_name_input(zdhNginx.getTmp_dir() + "/" + owner + "/" + file_name);
                }

            }

            //获取旧数据是否更新说明
            EtlTaskInfo etlTaskInfo1 = etlTaskService.selectById(etlTaskInfo.getId());

            etlTaskService.update(etlTaskInfo);

            if (etlTaskInfo.getUpdate_context() != null && !etlTaskInfo.getUpdate_context().equals("")
                    && !etlTaskInfo1.getUpdate_context().equals(etlTaskInfo.getUpdate_context())) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlTaskInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insert(etlTaskUpdateLogs);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),RETURN_CODE.SUCCESS.getDesc(), null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),e.getMessage(), null);
        }
    }


    /**
     * 根据数据源id 获取数据源下所有的表名字
     * @param id 数据源id
     * @return
     */
    @RequestMapping(value = "/etl_task_tables", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<String>> etl_task_tables(String id) {
        try{
            DataSourcesInfo dataSourcesInfo = dataSourcesMapper.selectByPrimaryKey(id);
            List<String> tables = tables(dataSourcesInfo);
            return ReturnInfo.buildSuccess(tables);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("获取数据源表列表失败",e);
        }

    }

    private List<String> tables(DataSourcesInfo dataSourcesInfo) {
        debugInfo(dataSourcesInfo);
        String url = dataSourcesInfo.getUrl();

        try {
            return new DBUtil().R3(dataSourcesInfo.getDriver(), dataSourcesInfo.getUrl(), dataSourcesInfo.getUsername(), dataSourcesInfo.getPassword(),
                    "");
        } catch (Exception e) {
            logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return new ArrayList<>();
        }


    }


    /**
     * 根据数据源id,表名获取表的schema
     * @param id
     * @param table_name
     * @return
     */
    @RequestMapping(value="/etl_task_schema", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<String>> etl_task_schema(String id, String table_name) {

        DataSourcesInfo dataSourcesInfo = dataSourcesMapper.selectByPrimaryKey(id);
        List<String> columns = schema(dataSourcesInfo, table_name);
        if(columns==null || columns.size()==0){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询结果为空", columns);
        }
        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", columns);
    }


    private List<String> schema(DataSourcesInfo dataSourcesInfo, String table_name) {

        String url = dataSourcesInfo.getUrl();

        try {
            return new DBUtil().R4(dataSourcesInfo.getDriver(), dataSourcesInfo.getUrl(), dataSourcesInfo.getUsername(), dataSourcesInfo.getPassword(),
                    "select * from " + table_name + " where 1=2 limit 1", table_name);
        } catch (Exception e) {
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            return new ArrayList<>();
        }
    }

    public void create_etl(){
        //整库迁移-生成小工具,根据输入源(JDBC)获取对应的表,批量生成ETL任务


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
