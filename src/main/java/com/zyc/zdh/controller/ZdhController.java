package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jcraft.jsch.SftpException;
import com.zyc.zdh.config.DateConverter;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.*;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.DataSourcesService;
import com.zyc.zdh.service.DispatchTaskService;
import com.zyc.zdh.service.EtlTaskService;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.DBUtil;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.HttpUtil;
import com.zyc.zdh.util.SFTPUtil;
import org.apache.shiro.SecurityUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class ZdhController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Timestamp.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                setValue(new DateConverter().convert(text));
            }
        });

    }


    @Autowired
    DataSourcesService dataSourcesServiceImpl;
    @Autowired
    EtlTaskService etlTaskService;
    @Autowired
    DispatchTaskService dispatchTaskService;
    @Autowired
    ZdhLogsService zdhLogsService;
    @Autowired
    QuartzJobMapper quartzJobMapper;
    @Autowired
    QuartzManager2 quartzManager2;
    @Autowired
    ZdhHaInfoMapper zdhHaInfoMapper;
    @Autowired
    TaskLogsMapper taskLogsMapper;
    @Autowired
    ZdhNginxMapper zdhNginxMapper;
    @Autowired
    EtlMoreTaskMapper etlMoreTaskMapper;
    @Autowired
    ZdhDownloadMapper zdhDownloadMapper;
    @Autowired
    EtlTaskMetaMapper etlTaskMetaMapper;
    @Autowired
    EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    QualityMapper qualityMapper;
    @Autowired
    SqlTaskMapper sqlTaskMapper;
    @Autowired
    MetaDatabaseMapper metaDatabaseMapper;
    @Autowired
    JarTaskMapper jarTaskMapper;
    @Autowired
    JarFileMapper jarFileMapper;


    @RequestMapping("/data_sources_index")
    public String data_sources_index() {

        return "etl/data_sources_index";
    }

    @RequestMapping(value = "/data_sources_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_sources_list(String[] ids) {

        DataSourcesInfo dataSourcesInfo = new DataSourcesInfo();
        dataSourcesInfo.setOwner(getUser().getId());
        List<DataSourcesInfo> list = dataSourcesServiceImpl.select(dataSourcesInfo);

        return JSON.toJSONString(list);
    }

    @RequestMapping(value = "/data_sources_list2", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_sources_list2(String data_source_context, String data_source_type, String url) {


        List<DataSourcesInfo> list = dataSourcesServiceImpl.selectByParams(data_source_context, data_source_type, url, getUser().getId());

        return JSON.toJSONString(list);
    }

    @RequestMapping(value = "/data_sources_info", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_sources_info(String id) {

        DataSourcesInfo dataSourcesInfo = dataSourcesServiceImpl.selectById(id);

        return JSON.toJSONString(dataSourcesInfo);
    }

    @RequestMapping("/data_sources_delete")
    @ResponseBody
    public String deleteIds(Long[] ids) {
        dataSourcesServiceImpl.deleteBatchById(ids);

        JSONObject json = new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/data_sources_add")
    public String data_sources_add(HttpServletRequest request, HttpServletResponse response, Long id) {

        return "etl/data_sources_add";
    }

    @RequestMapping("/add_data_sources")
    @ResponseBody
    public String add_data_sources(DataSourcesInfo dataSourcesInfo) {
        dataSourcesInfo.setOwner(getUser().getId());
        dataSourcesServiceImpl.insert(dataSourcesInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/data_sources_update")
    @ResponseBody
    public String data_sources_update(DataSourcesInfo dataSourcesInfo) {
        dataSourcesInfo.setOwner(getUser().getId());
        dataSourcesServiceImpl.update(dataSourcesInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/data_sources_type")
    @ResponseBody
    public List<String> data_sources_type() {
        List<String> result = dataSourcesServiceImpl.selectDataSourcesType();

        return result;
    }


    @RequestMapping("/etl_task_index")
    public String etl_task_index() {

        //选择输入数据源
        //配置表名/文件名
        //配置选择的列

        // 转换选择的列

        //选择输出数据源
        //配置表名/文件名

        return "etl/etl_task_index";
    }


    @RequestMapping(value = "/etl_task_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String etl_task_list(String[] ids) {
        List<EtlTaskInfo> list = new ArrayList<>();
        EtlTaskInfo etlTaskInfo = new EtlTaskInfo();
        etlTaskInfo.setOwner(getUser().getId());
        if (ids == null)
            list = etlTaskService.select(etlTaskInfo);
        else
            list.add(etlTaskService.selectById(ids[0]));

        return JSON.toJSONString(list);
    }

    @RequestMapping(value = "/etl_task_list2", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String etl_task_list2(String etl_context, String file_name) {
        List<EtlTaskInfo> list = new ArrayList<>();
        list = etlTaskService.selectByParams(getUser().getId(), etl_context, file_name);
        return JSON.toJSONString(list);
    }

    @RequestMapping("/etl_task_delete")
    @ResponseBody
    public String etl_task_delete(Long[] ids) {
        etlTaskService.deleteBatchById(ids);

        JSONObject json = new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/etl_task_add_index")
    public String etl_task_add(HttpServletRequest request, HttpServletResponse response, Long id, String edit) {

        System.out.println(edit);
        request.setAttribute("edit", edit);
        return "etl/etl_task_add_index";
    }


    @RequestMapping("/etl_task_add")
    @ResponseBody
    public String etl_task_add(EtlTaskInfo etlTaskInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        String owner = getUser().getId();
        etlTaskInfo.setOwner(owner);
        debugInfo(etlTaskInfo);

        etlTaskInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
        etlTaskInfo.setCreate_time(new Timestamp(new Date().getTime()));
        if (etlTaskInfo.getData_source_type_input().equals("外部上传")) {
            ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(owner);
            if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                etlTaskInfo.setData_sources_file_name_input(zdhNginx.getNginx_dir() + "/" + owner + "/" + etlTaskInfo.getData_sources_file_name_input());
            } else {
                etlTaskInfo.setData_sources_file_name_input(zdhNginx.getTmp_dir() + "/" + owner + "/" + etlTaskInfo.getData_sources_file_name_input());
            }

        }

//        etlTaskInfo.getColumn_data_list().forEach(column_data -> {
//            System.out.println(column_data.getColumn_expr() + "=" + column_data.getColumn_alias());
//        });

        etlTaskService.insert(etlTaskInfo);
        if (etlTaskInfo.getUpdate_context() != null && !etlTaskInfo.getUpdate_context().equals("")) {
            //插入更新日志表
            EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
            etlTaskUpdateLogs.setId(etlTaskInfo.getId());
            etlTaskUpdateLogs.setUpdate_context(etlTaskInfo.getUpdate_context());
            etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTaskUpdateLogs.setOwner(getUser().getId());
            etlTaskUpdateLogsMapper.insert(etlTaskUpdateLogs);
        }

        //dataSourcesServiceImpl.insert(dataSourcesInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping("/etl_task_add_file")
    @ResponseBody
    public String etl_task_add_file(MultipartFile up_file, HttpServletRequest request) {
        String json_str = JSON.toJSONString(request.getParameterMap());
        String owner = getUser().getId();
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
            try {
                up_file.transferTo(tempFile);
                if (!zdhNginx.getHost().equals("")) {
                    System.out.println("通过sftp上传文件");
                    SFTPUtil sftp = new SFTPUtil(zdhNginx.getUsername(), zdhNginx.getPassword(),
                            zdhNginx.getHost(), new Integer(zdhNginx.getPort()));
                    sftp.login();
                    InputStream is = new FileInputStream(tempFile);
                    sftp.upload(nginx_dir + "/" + owner + "/", fileName, is);
                    sftp.logout();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SftpException e) {
                e.printStackTrace();
            }

        }

        // etlTaskService.insert(etlTaskInfo);
        //dataSourcesServiceImpl.insert(dataSourcesInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/etl_task_update")
    @ResponseBody
    public String etl_task_update(EtlTaskInfo etlTaskInfo) {
        String owner = getUser().getId();
        etlTaskInfo.setOwner(owner);
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
            etlTaskUpdateLogs.setOwner(getUser().getId());
            etlTaskUpdateLogsMapper.insert(etlTaskUpdateLogs);
        }

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping("/etl_task_tables")
    @ResponseBody
    public String etl_task_tables(String id) {

        DataSourcesInfo dataSourcesInfo = dataSourcesServiceImpl.selectById(id);

        String jsonArrayStr = tables(dataSourcesInfo);

        System.out.println(jsonArrayStr);
        return jsonArrayStr;
    }

    private String tables(DataSourcesInfo dataSourcesInfo) {

        debugInfo(dataSourcesInfo);

        String url = dataSourcesInfo.getUrl();


        try {
            return JSON.toJSONString(new DBUtil().R3(dataSourcesInfo.getDriver(), dataSourcesInfo.getUrl(), dataSourcesInfo.getUsername(), dataSourcesInfo.getPassword(),
                    ""));
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }


    }

    /**
     * mysql 使用
     *
     * @param url
     * @return
     */
    private String tableSchema(String url) {

        int index = url.split("\\?")[0].lastIndexOf("/");
        return url.split("\\?")[0].substring(index + 1);

    }

    @RequestMapping("/etl_task_schema")
    @ResponseBody
    public String etl_task_schema(String id, String table_name) {

        DataSourcesInfo dataSourcesInfo = dataSourcesServiceImpl.selectById(id);

        String jsonArrayStr = schema(dataSourcesInfo, table_name);

        System.out.println(jsonArrayStr);
        return jsonArrayStr;
    }


    private String schema(DataSourcesInfo dataSourcesInfo, String table_name) {

        String url = dataSourcesInfo.getUrl();

        try {
            return JSON.toJSONString(new DBUtil().R4(dataSourcesInfo.getDriver(), dataSourcesInfo.getUrl(), dataSourcesInfo.getUsername(), dataSourcesInfo.getPassword(),
                    "select * from " + table_name + " where 1=2", table_name));
        } catch (Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }

    @RequestMapping(value = "/etl_task_meta", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String etl_task_meta(String id) {

        EtlTaskMeta etlTaskMeta = new EtlTaskMeta();

        if (id == null || id.equals("")) {
            etlTaskMeta.setParent_id("0");
        } else {
            etlTaskMeta.setParent_id(id);
        }
        List<EtlTaskMeta> etlTaskMetas = etlTaskMetaMapper.select(etlTaskMeta);

        return JSON.toJSONString(etlTaskMetas);
    }


    @RequestMapping("/etl_task_more_sources_index")
    public String etl_task_more_sources_index() {

        //选择输入数据源
        //配置表名/文件名
        //配置选择的列

        // 转换选择的列

        //选择输出数据源
        //配置表名/文件名

        return "etl/etl_task_more_sources_index";
    }

    @RequestMapping(value = "/etl_task_more_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String etl_task_more_list(String[] ids) {
        EtlMoreTaskInfo etlMoreTaskInfo = new EtlMoreTaskInfo();
        etlMoreTaskInfo.setOwner(getUser().getId());
        List<EtlMoreTaskInfo> etlMoreTaskInfos = new ArrayList<EtlMoreTaskInfo>();
        if (ids == null) {
            etlMoreTaskInfos = etlMoreTaskMapper.select(etlMoreTaskInfo);
        } else {
            etlMoreTaskInfos.add(etlMoreTaskMapper.selectByPrimaryKey(ids[0]));
        }

        return JSON.toJSONString(etlMoreTaskInfos);
    }

    @RequestMapping(value = "/etl_task_more_list2", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String etl_task_more_list2(String etl_context, String file_name) {
        List<EtlMoreTaskInfo> etlMoreTaskInfos = new ArrayList<EtlMoreTaskInfo>();
        etlMoreTaskInfos = etlMoreTaskMapper.selectByParams(getUser().getId(), etl_context, file_name);
        return JSON.toJSONString(etlMoreTaskInfos);
    }


    @RequestMapping("/etl_task_more_sources_add_index")
    public String etl_task_more_sources_add_index() {

        //选择输入数据源
        //配置表名/文件名
        //配置选择的列

        // 转换选择的列

        //选择输出数据源
        //配置表名/文件名

        return "etl/etl_task_more_sources_add_index";
    }

    @RequestMapping("/etl_task_more_sources_add")
    @ResponseBody
    public String etl_task_more_sources_add(EtlMoreTaskInfo etlMoreTaskInfo) {
        etlMoreTaskInfo.setOwner(getUser().getId());
        etlMoreTaskInfo.setCreate_time(new Timestamp(new Date().getTime()));
        debugInfo(etlMoreTaskInfo);
        etlMoreTaskMapper.insert(etlMoreTaskInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/etl_task_more_sources_delete")
    @ResponseBody
    public String etl_task_more_sources_delete(String[] ids) {
        if (ids != null) {
            for (String id : ids) {
                etlMoreTaskMapper.deleteBatchById(id);
            }
        }
        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/etl_task_more_update")
    @ResponseBody
    public String etl_task_more_update(EtlMoreTaskInfo etlMoreTaskInfo) {
        String owner = getUser().getId();
        etlMoreTaskInfo.setOwner(owner);
        debugInfo(etlMoreTaskInfo);

        etlMoreTaskMapper.updateByPrimaryKey(etlMoreTaskInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping("/dispatch_task_index")
    public String dispatch_task_index() {

        //配置调度环境
        //调度条件---定时,特定条件
        //选则ETL任务
        //点击执行任务


        return "/etl/dispatch_task_index";
    }

    @RequestMapping(value = "/dispatch_task_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String dispatch_task_list(String[] ids) {
        List<QuartzJobInfo> list = new ArrayList<>();
        QuartzJobInfo quartzJobInfo = new QuartzJobInfo();
        quartzJobInfo.setOwner(getUser().getId());
        if (ids == null)
            list = quartzJobMapper.selectByOwner(quartzJobInfo.getOwner());
        else {
            quartzJobInfo.setJob_id(ids[0]);
            list.add(quartzJobMapper.selectByPrimaryKey(quartzJobInfo));
        }

        return JSON.toJSONString(list);
    }

    @RequestMapping(value = "/dispatch_task_list2", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String dispatch_task_list2(String job_context, String etl_context) {
        List<QuartzJobInfo> list = new ArrayList<>();
        list = quartzJobMapper.selectByParams(getUser().getId(), job_context, etl_context);
        return JSON.toJSONString(list);
    }

    @RequestMapping("/dispatch_task_add_index")
    public String dispatch_task_add_index() {

        //配置调度环境
        //调度条件---定时,特定条件
        //选则ETL任务
        //点击执行任务

        return "/etl/dispatch_task_add_index";
    }

    @RequestMapping("/dispatch_task_add")
    @ResponseBody
    public String dispatch_task_add(QuartzJobInfo quartzJobInfo) {
        debugInfo(quartzJobInfo);
        quartzJobInfo.setOwner(getUser().getId());
        quartzJobInfo.setJob_id(SnowflakeIdWorker.getInstance().nextId() + "");
        quartzJobInfo.setStatus("create");
        String end_expr = quartzJobInfo.getExpr().toLowerCase();
        if (end_expr.endsWith("s") || end_expr.endsWith("m")
                || end_expr.endsWith("h")) {
            //SimpleScheduleBuilder 表达式 必须指定一个次数,默认式
            if (quartzJobInfo.getPlan_count().equals("")) {
                quartzJobInfo.setPlan_count("-1");
            }
        }
        debugInfo(quartzJobInfo);
        quartzJobMapper.insert(quartzJobInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping("/dispatch_task_delete")
    @ResponseBody
    public String dispatch_task_delete(String[] ids) {
        QuartzJobInfo quartzJobInfo = new QuartzJobInfo();
        for (String id : ids) {
            quartzJobInfo.setJob_id(id);
            quartzJobMapper.deleteByPrimaryKey(quartzJobInfo);
        }

        JSONObject json = new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/dispatch_task_update")
    @ResponseBody
    public String dispatch_task_update(QuartzJobInfo quartzJobInfo) {

        debugInfo(quartzJobInfo);
        quartzJobInfo.setOwner(getUser().getId());
        QuartzJobInfo qji = quartzJobMapper.selectByPrimaryKey(quartzJobInfo);
        quartzJobMapper.updateByPrimaryKey(quartzJobInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/dispatch_task_execute")
    @ResponseBody
    public String dispatch_task_execute(QuartzJobInfo quartzJobInfo) {

        debugInfo(quartzJobInfo);

        // String url = "http://127.0.0.1:60001/api/v1/zdh";

        // ZdhInfo zdhInfo = create_zhdInfo(quartzJobInfo);

        try {
            QuartzJobInfo dti = quartzJobMapper.selectByPrimaryKey(quartzJobInfo.getJob_id());
            if (dti.getJob_type().equals("SHELL")) {
                ShellJob.run(dti);
            } else if (dti.getJob_type().equals("FTP")) {

            } else if (dti.getJob_type().equals("JDBC")) {
                JdbcJob.run(dti);
            } else if (dti.getJob_type().equals("HDFS")) {
                HdfsJob.run(dti);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping("/dispatch_task_execute_quartz")
    @ResponseBody
    public String dispatch_task_execute_quartz(QuartzJobInfo quartzJobInfo) {

        debugInfo(quartzJobInfo);

        // dispatchTaskService.update(dispatchTaskInfo);
        String url = "http://127.0.0.1:60001/api/v1/zdh";
        QuartzJobInfo dti = quartzJobMapper.selectByPrimaryKey(quartzJobInfo.getJob_id());

        //ZdhInfo zdhInfo = create_zhdInfo(quartzJobInfo);
        //重置次数
        dti.setCount(0);
        quartzManager2.addTaskToQuartz(dti);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping("/dispatch_task_quartz_pause")
    @ResponseBody
    public String dispatch_task_quartz_pause(QuartzJobInfo quartzJobInfo) {

        debugInfo(quartzJobInfo);
        QuartzJobInfo dti = quartzJobMapper.selectByPrimaryKey(quartzJobInfo.getJob_id());

        if (quartzJobInfo.getStatus().equals("running")) {
            //需要恢复暂停任务
            quartzManager2.resumeTask(dti);
            quartzJobMapper.updateStatus(quartzJobInfo.getJob_id(), quartzJobInfo.getStatus());
        } else {
            //暂停任务,//状态在pauseTask 方法中修改
            quartzManager2.pauseTask(dti);
        }


        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/dispatch_task_quartz_del")
    @ResponseBody
    public String dispatch_task_quartz_del(QuartzJobInfo quartzJobInfo) {

        QuartzJobInfo qji = quartzJobMapper.selectByPrimaryKey(quartzJobInfo);
        quartzManager2.deleteTask(qji, "remove");
        JSONObject json = new JSONObject();

        json.put("success", "200");
        if (qji.getMore_task().equals("多源ETL") || qji.getMore_task().equals("SQL")) {
            return json.toJSONString();
        }
        EtlTaskInfo etlTaskInfo = etlTaskService.selectById(qji.getEtl_task_id());
        String dataSourcesType = etlTaskInfo.getData_source_type_input().toLowerCase();
        if (dataSourcesType.equals("kafka") || dataSourcesType.equals("flume")) {
            quartzJobMapper.updateStatus(qji.getJob_id(), "finish");
            String url = JobCommon.getZdhUrl(zdhHaInfoMapper).replace("zdh", "del");
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("job_id", qji.getJob_id());
                jsonObject.put("del_type", dataSourcesType);
                HttpUtil.postJSON(url, jsonObject.toJSONString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return json.toJSONString();
    }

    private ZdhInfo create_zhdInfo(QuartzJobInfo quartzJobInfo) {
        // dispatchTaskService.update(dispatchTaskInfo);


        //获取调度任务信息
        QuartzJobInfo dti = quartzJobMapper.selectByPrimaryKey(quartzJobInfo.getJob_id());

        String etl_task_id = dti.getEtl_task_id();
        //获取etl 任务信息
        EtlTaskInfo etlTaskInfo = etlTaskService.selectById(etl_task_id);

        Map<String, Object> map = (Map<String, Object>) JSON.parseObject(dti.getParams());
        //此处做参数匹配转换
        if (map != null) {
            System.out.println("自定义参数不为空,开始替换:" + dti.getParams());
            map.forEach((k, v) -> {
                System.out.println("key:" + k + ",value:" + v);
                String filter = etlTaskInfo.getData_sources_filter_input();
                etlTaskInfo.setData_sources_filter_input(filter.replace(k, v.toString()));
                String clear = etlTaskInfo.getData_sources_clear_output();
                etlTaskInfo.setData_sources_clear_output(clear.replace(k, v.toString()));
            });


        }

        //获取数据源信息
        String data_sources_choose_input = etlTaskInfo.getData_sources_choose_input();
        String data_sources_choose_output = etlTaskInfo.getData_sources_choose_output();
        DataSourcesInfo dataSourcesInfoInput = dataSourcesServiceImpl.selectById(data_sources_choose_input);
        DataSourcesInfo dataSourcesInfoOutput = null;
        if (!data_sources_choose_input.equals(data_sources_choose_output)) {
            dataSourcesInfoOutput = dataSourcesServiceImpl.selectById(data_sources_choose_output);
        } else {
            dataSourcesInfoOutput = dataSourcesInfoInput;
        }

        ZdhInfo zdhInfo = new ZdhInfo();
        zdhInfo.setZdhInfo(dataSourcesInfoInput, etlTaskInfo, dataSourcesInfoOutput, dti);

        return zdhInfo;


    }


    @RequestMapping("/log_txt")
    public String log_txt() {

        return "etl/log_txt";
    }

    @RequestMapping(value = "/zhd_logs", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String zhd_logs(String id, String start_time, String end_time, String del, String level) {
        System.out.println("id:" + id + ",start_time:" + start_time + ",end_time:" + end_time);


        Timestamp ts_start = null;
        Timestamp ts_end = null;
        if (!start_time.equals("")) {
            ts_start = Timestamp.valueOf(start_time);
        } else {
            ts_start = Timestamp.valueOf("1970-01-01 00:00:00");
        }
        if (!end_time.equals("")) {
            ts_end = Timestamp.valueOf(end_time);
        } else {
            ts_end = Timestamp.valueOf("2999-01-01 00:00:00");
        }

        if (del != null && !del.equals("")) {
            zdhLogsService.deleteByTime(id, ts_start, ts_end);
        }

        String levels = "'DEBUG','WARN','INFO','ERROR'";

        if (level != null && level.equals("INFO")) {
            levels = "'WARN','INFO','ERROR'";
        }
        if (level != null && level.equals("WARN")) {
            levels = "'WARN','ERROR'";
        }
        if (level != null && level.equals("ERROR")) {
            levels = "'ERROR'";
        }

        List<ZdhLogs> zhdLogs = zdhLogsService.selectByTime(id, ts_start, ts_end, levels);
        Iterator<ZdhLogs> it = zhdLogs.iterator();
        StringBuilder sb = new StringBuilder();
        while (it.hasNext()) {
            ZdhLogs next = it.next();
            String info = "任务ID:" + next.getJob_id() + ",任务执行时间:" + next.getLog_time().toString() + ",日志[" + next.getLevel() + "]:" + next.getMsg();
            sb.append(info + "\r\n");
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("logs", sb.toString());
        return jsonObject.toJSONString();
    }

    @RequestMapping(value = "/task_logs", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String task_logs(String start_time, String end_time, String status) {

        System.out.println("开始加载任务日志start_time:" + start_time + ",end_time:" + end_time + ",status:" + status);

        List<TaskLogs> list = taskLogsMapper.selectByTaskLogs(getUser().getId(), Timestamp.valueOf(start_time + " 00:00:00"),
                Timestamp.valueOf(end_time + " 23:59:59"), status);

        return JSON.toJSONString(list);
    }

    @RequestMapping(value = "/task_logs_delete", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String task_logs_delete(String[] ids) {

        System.out.println("开始删除任务日志");
        TaskLogs taskLogs = new TaskLogs();
        for (String id : ids) {
            taskLogs.setId(id);
            taskLogsMapper.deleteByPrimaryKey(taskLogs);
        }
        JSONObject json = new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }

    public User getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }


    @RequestMapping("/monitor")
    public String index_v1() {

        return "etl/monitor";
    }


    @RequestMapping("/etlEcharts")
    @ResponseBody
    public List<EtlEcharts> get1() {
//        调度总任务数：
//        正在执行任务数：
//        已完成任务数
//        失败任务数

        //    int total_task_num = quartzJobMapper.selectCountByOwner(getUser().getId());

        List<EtlEcharts> echartsList = taskLogsMapper.slectByOwner(getUser().getId());

        return echartsList;
    }

    @RequestMapping("/etlEchartsCurrent")
    @ResponseBody
    public List<EtlEcharts> get2() {
//        调度总任务数：
//        正在执行任务数：
//        已完成任务数
//        失败任务数

        int total_task_num = quartzJobMapper.selectCountByOwner(getUser().getId());

        String etl_date = DateUtil.format(new Date()) + " 00:00:00";
        List<EtlEcharts> echartsList = taskLogsMapper.slectByOwnerEtlDate(getUser().getId(), etl_date);

        return echartsList;
    }

    @RequestMapping("/getScheduleTask")
    @ResponseBody
    public List<QuartzJobInfo> getScheduleTask() {
        String owner = getUser().getId();
        try {
            return quartzManager2.getScheduleTask(owner);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new ArrayList<QuartzJobInfo>();
        }
    }

    @RequestMapping("/readme")
    public String read_me() {

        return "read_me";
    }

    @RequestMapping(value = "/file_manager", method = RequestMethod.GET)
    public String file_manager() {

        return "file_manager";
    }

    @RequestMapping("/getFileManager")
    @ResponseBody
    public ZdhNginx getFileManager() {
        ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(getUser().getId());
        return zdhNginx;
    }

    @RequestMapping(value = "/file_manager_up", method = RequestMethod.POST)
    @ResponseBody
    public String file_manager_up(ZdhNginx zdhNginx) {

        ZdhNginx zdhNginx1 = zdhNginxMapper.selectByOwner(getUser().getId());
        zdhNginx.setOwner(getUser().getId());
        if (zdhNginx.getPort().equals("")) {
            zdhNginx.setPort("22");
        }
        if (zdhNginx1 != null) {
            zdhNginx.setId(zdhNginx1.getId());
            zdhNginxMapper.updateByPrimaryKey(zdhNginx);
        } else {
            zdhNginxMapper.insert(zdhNginx);
        }

        JSONObject json = new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping(value = "/download_index", method = RequestMethod.GET)
    public String download_index() {

        return "etl/download_index";
    }

    @RequestMapping(value = "/download_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String download_list(String file_name) {
        List<ZdhDownloadInfo> list = new ArrayList<>();
        list = zdhDownloadMapper.slectByOwner(getUser().getId(), file_name);
        return JSON.toJSONString(list);
    }


    @RequestMapping(value = "/download_delete", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String download_delete(String[] ids) {

        System.out.println("开始删除下载数据");
        ZdhDownloadInfo zdhDownloadInfo = new ZdhDownloadInfo();

        List<ZdhDownloadInfo> zdhDownloadInfoList = new ArrayList<>();
        for (String id : ids) {
            zdhDownloadInfo.setId(id);
            zdhDownloadMapper.deleteByPrimaryKey(zdhDownloadInfo);
        }

        zdhDownloadInfoList = zdhDownloadMapper.slectByOwner(getUser().getId(), "");

        if (zdhDownloadInfoList.size() > 0) {
            redisUtil.set("zdhdownloadinfos_" + getUser().getId(), JSON.toJSONString(zdhDownloadInfoList));
        } else {
            redisUtil.remove("zdhdownloadinfos_" + getUser().getId());
        }

        JSONObject json2 = new JSONObject();
        json2.put("success", "200");
        return json2.toJSONString();
    }


    @RequestMapping(value = "/download_file", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public void download_file(HttpServletResponse response, String id) {

        ZdhDownloadInfo zdhDownloadInfo = zdhDownloadMapper.selectByPrimaryKey(id);
        int start_index = zdhDownloadInfo.getFile_name().lastIndexOf('/');
        String fileName = zdhDownloadInfo.getFile_name().substring(start_index + 1);
        File path = null;
        response.setHeader("content-type", "text/html;charset=UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        try {
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        byte[] buff = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
        BufferedInputStream bis = null;
        OutputStream os = null;
        try {
            ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(getUser().getId());
            if (zdhNginx.getHost() != null && !zdhNginx.getHost().equals("")) {
                //连接sftp 下载
                SFTPUtil sftp = new SFTPUtil(zdhNginx.getUsername(), zdhNginx.getPassword(),
                        zdhNginx.getHost(), new Integer(zdhNginx.getPort()));
                sftp.login();
                byte[] buff1 = sftp.download(zdhDownloadInfo.getFile_name().substring(0, start_index), fileName);
                os = response.getOutputStream();
                os.write(buff1, 0, buff1.length);
                os.flush();
            } else {
                //本地文件
                path = new File(zdhDownloadInfo.getFile_name());
                os = response.getOutputStream();
                bis = new BufferedInputStream(new FileInputStream(path));
                int i = bis.read(buff);
                while (i != -1) {
                    os.write(buff, 0, buff.length);
                    os.flush();
                    i = bis.read(buff);
                }
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SftpException e) {
            e.printStackTrace();
        } finally {
            String json = redisUtil.get("zdhdownloadinfos_" + getUser().getId()).toString();
            List<ZdhDownloadInfo> zdhDownloadInfoList = JSON.parseArray(json, ZdhDownloadInfo.class);
            Iterator<ZdhDownloadInfo> iterator = zdhDownloadInfoList.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getId().equals(id)) {
                    iterator.remove();
                }
            }
            if (zdhDownloadInfoList.size() > 0) {
                redisUtil.set("zdhdownloadinfos_" + getUser().getId(), JSON.toJSONString(zdhDownloadInfoList));
            } else {
                redisUtil.remove("zdhdownloadinfos_" + getUser().getId());
            }

            zdhDownloadInfo.setDown_count(zdhDownloadInfo.getDown_count() + 1);
            zdhDownloadMapper.updateByPrimaryKey(zdhDownloadInfo);


            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    @RequestMapping(value = "/{url}", method = RequestMethod.GET)
    public String dynApiDemo2(@PathVariable("url") String url) {

        return "etl/" + url;
    }

    @RequestMapping(value = "/quota_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String quota_list(String column_desc, String column_alias, String company, String section, String service) {

        List<QuotaInfo> etlTaskInfos = new ArrayList<>();
        etlTaskInfos = etlTaskService.selectByColumn(getUser().getId(), column_desc, column_alias, company, section, service);
        return JSON.toJSONString(etlTaskInfos);
    }


    @RequestMapping(value = "/notice_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String notice() {
        System.out.println("加载缓存中通知事件");
        List<NoticeInfo> noticeInfos = new ArrayList<>();
        if (!redisUtil.exists("zdhdownloadinfos_" + getUser().getId())) {
            return JSON.toJSONString(noticeInfos);
        }
        String json = redisUtil.get("zdhdownloadinfos_" + getUser().getId()).toString();
        if (json != null && !json.equals("")) {
            List<ZdhDownloadInfo> cache = JSON.parseArray(json, ZdhDownloadInfo.class);
            Iterator<ZdhDownloadInfo> iterator = cache.iterator();
            while (iterator.hasNext()) {
                ZdhDownloadInfo zdhDownloadInfo = iterator.next();
                if (zdhDownloadInfo.getOwner().equals(getUser().getId())) {
                    NoticeInfo noticeInfo = new NoticeInfo();
                    noticeInfo.setMsg_type("文件下载");
                    int last_index = zdhDownloadInfo.getFile_name().lastIndexOf("/");
                    noticeInfo.setMsg_title(zdhDownloadInfo.getFile_name().substring(last_index + 1) + "完成下载");
                    noticeInfo.setMsg_url("download_index");
                    noticeInfos.add(noticeInfo);
                }
            }
        }

        return JSON.toJSONString(noticeInfos);
    }


    @RequestMapping(value = "/quality_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String quality_list(String job_context, String etl_context, String status) {

        List<Quality> qualities = new ArrayList<>();

        qualities = qualityMapper.selectByOwner(getUser().getId(), job_context, etl_context, status);

        return JSON.toJSONString(qualities);
    }


    @RequestMapping(value = "/quality_delete", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String quality_delete(String[] ids) {

        for (String id : ids) {
            qualityMapper.deleteByPrimaryKey(id);
        }
        JSONObject json2 = new JSONObject();
        json2.put("success", "200");
        return json2.toJSONString();
    }


    @RequestMapping(value = "/sql_task_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String sql_task_list(String sql_context, String id) {

        List<SqlTaskInfo> sqlTaskInfos = new ArrayList<>();

        sqlTaskInfos = sqlTaskMapper.selectByParams(getUser().getId(), sql_context, id);

        return JSON.toJSONString(sqlTaskInfos);
    }

    @RequestMapping("/sql_task_delete")
    @ResponseBody
    public String sql_task_delete(String[] ids) {

        for (String id : ids)
            sqlTaskMapper.deleteByPrimaryKey(id);

        JSONObject json = new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/sql_task_add")
    @ResponseBody
    public String sql_task_add(SqlTaskInfo sqlTaskInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        String owner = getUser().getId();
        sqlTaskInfo.setOwner(owner);
        debugInfo(sqlTaskInfo);

        sqlTaskInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
        sqlTaskInfo.setCreate_time(new Timestamp(new Date().getTime()));


        sqlTaskMapper.insert(sqlTaskInfo);


        if (sqlTaskInfo.getUpdate_context() != null && !sqlTaskInfo.getUpdate_context().equals("")) {
            //插入更新日志表
            EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
            etlTaskUpdateLogs.setId(sqlTaskInfo.getId());
            etlTaskUpdateLogs.setUpdate_context(sqlTaskInfo.getUpdate_context());
            etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTaskUpdateLogs.setOwner(getUser().getId());
            etlTaskUpdateLogsMapper.insert(etlTaskUpdateLogs);
        }

        //dataSourcesServiceImpl.insert(dataSourcesInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/sql_task_update")
    @ResponseBody
    public String sql_task_update(SqlTaskInfo sqlTaskInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        String owner = getUser().getId();
        sqlTaskInfo.setOwner(owner);
        debugInfo(sqlTaskInfo);

        sqlTaskMapper.updateByPrimaryKey(sqlTaskInfo);

        SqlTaskInfo sti = sqlTaskMapper.selectByPrimaryKey(sqlTaskInfo.getId());

        if (sqlTaskInfo.getUpdate_context() != null && !sqlTaskInfo.getUpdate_context().equals("")
                && !sqlTaskInfo.getUpdate_context().equals(sti.getUpdate_context())) {
            //插入更新日志表
            EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
            etlTaskUpdateLogs.setId(sqlTaskInfo.getId());
            etlTaskUpdateLogs.setUpdate_context(sqlTaskInfo.getUpdate_context());
            etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTaskUpdateLogs.setOwner(getUser().getId());
            etlTaskUpdateLogsMapper.insert(etlTaskUpdateLogs);
        }

        //dataSourcesServiceImpl.insert(dataSourcesInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping("/load_meta_databases")
    @ResponseBody
    public String load_meta_databases() {

        String url = JobCommon.getZdhUrl(zdhHaInfoMapper);
        try {
            String databases = HttpUtil.postJSON(url + "/show_databases", new JSONObject().toJSONString());

            List<meta_database_info> meta_database_infos = new ArrayList<meta_database_info>();

            System.out.println("databases:" + databases);
            JSONArray jary = JSON.parseArray(databases);
            for (Object o : jary) {
                JSONObject jo = new JSONObject();
                String tableNames = HttpUtil.postJSON(url + "/show_tables", "{\"databaseName\":\"" + o.toString() + "\"}");
                JSONArray tableAry = JSON.parseArray(tableNames);
                meta_database_info meta_database_info_d = new meta_database_info();
                meta_database_info_d.setOwner(getUser().getId());
                metaDatabaseMapper.delete(meta_database_info_d);
                if (tableAry.isEmpty()) {
                    meta_database_info meta_database_info = new meta_database_info();
                    meta_database_info.setDb_name(o.toString());
                    meta_database_info.setTb_name("");
                    meta_database_info.setOwner(getUser().getId());
                    meta_database_info.setCreate_time(new Timestamp(new Date().getTime()));
                    metaDatabaseMapper.insert(meta_database_info);
                }

                for (Object t : tableAry) {
                    meta_database_info meta_database_info = new meta_database_info();
                    meta_database_info.setDb_name(o.toString());
                    meta_database_info.setTb_name(t.toString());
                    meta_database_info.setOwner(getUser().getId());
                    meta_database_info.setCreate_time(new Timestamp(new Date().getTime()));
                    meta_database_infos.add(meta_database_info);
                    metaDatabaseMapper.insert(meta_database_info);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();

        }


        return "{}";
    }

    @RequestMapping("/show_databases")
    @ResponseBody
    public String show_databases() {
        meta_database_info meta_database_info = new meta_database_info();
        meta_database_info.setOwner(getUser().getId());
        List<meta_database_info> meta_database_infos = metaDatabaseMapper.select(meta_database_info);
        try {
            JSONArray jsa = new JSONArray();
            Map<String, String> dbMap = new HashMap<>();

            for (meta_database_info o : meta_database_infos) {
                dbMap.put(o.getDb_name(), "");
                JSONObject jo1 = new JSONObject();
                jo1.put("id", o.getDb_name() + "." + o.getTb_name());
                jo1.put("parent", o.getDb_name());
                jo1.put("text", o.getTb_name());
                jsa.add(jo1);
            }

            for (Map.Entry<String, String> entry : dbMap.entrySet()) {
                String dbName = entry.getKey();
                JSONObject jo1 = new JSONObject();
                jo1.put("id", dbName);
                jo1.put("parent", "#");
                jo1.put("text", dbName);
                jsa.add(jo1);
            }

            return jsa.toJSONString();

        } catch (Exception e) {
            e.printStackTrace();

        }


        return "";
    }

    @RequestMapping("/show_tables")
    @ResponseBody
    public String show_tables() {

        String url = JobCommon.getZdhUrl(zdhHaInfoMapper);
        try {
            String tableNames = HttpUtil.postJSON(url + "/show_tables", "");

            return tableNames;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return "";
    }


    @RequestMapping("/desc_table")
    @ResponseBody
    public String desc_table(String table) {

        String url = JobCommon.getZdhUrl(zdhHaInfoMapper);
        try {
            JSONObject p = new JSONObject();
            p.put("table", table);
            String desc_table = HttpUtil.postJSON(url + "/desc_table", p.toJSONString());
            return desc_table;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "";
    }

    @RequestMapping("/spark_web_ui_port")
    @ResponseBody
    public String spark_web_ui_port() {
        List<ZdhHaInfo> zdhHaInfoList = new ArrayList<>();
        zdhHaInfoList = zdhHaInfoMapper.selectByStatus("enabled");

        return JSON.toJSONString(zdhHaInfoList);
    }


    @RequestMapping(value = "/etl_task_jar_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String etl_task_jar_list(String etl_context) {
        List<JarTaskInfo> jarTaskInfos = new ArrayList<>();
        String owner = getUser().getId();
        jarTaskInfos = jarTaskMapper.selectByParams(owner, etl_context);
        return JSON.toJSONString(jarTaskInfos);
    }
    @RequestMapping(value = "/etl_task_jar_delete", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String etl_task_jar_delete(String[] ids) {
        List<JarTaskInfo> jarTaskInfos = new ArrayList<>();
        String owner = getUser().getId();
        jarTaskMapper.deleteBatchById(ids);
        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping("/etl_task_jar_add_file")
    @ResponseBody
    public String etl_task_jar_add_file(MultipartFile[] jar_files, JarTaskInfo jarTaskInfo, HttpServletRequest request) {
        String json_str = JSON.toJSONString(request.getParameterMap());
        String owner = getUser().getId();
        String id = SnowflakeIdWorker.getInstance().nextId() + "";
        jarTaskInfo.setId(id);
        jarTaskInfo.setOwner(owner);
        jarTaskInfo.setFiles("");
        jarTaskInfo.setCreate_time(new Timestamp(new Date().getTime()));
        debugInfo(jarTaskInfo);
        jarTaskMapper.insert(jarTaskInfo);
        System.out.println(json_str);
        System.out.println(jar_files);
        if (jar_files != null && jar_files.length > 0) {
            for (MultipartFile jar_file : jar_files) {
                String fileName = jar_file.getOriginalFilename();
                System.out.println("上传文件不为空");
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
                    jar_file.transferTo(tempFile);
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
                    e.printStackTrace();
                } catch (SftpException e) {
                    e.printStackTrace();
                }
            }


        }


        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping("/etl_task_jar_update")
    @ResponseBody
    public String etl_task_jar_update(MultipartFile[] jar_files, JarTaskInfo jarTaskInfo, HttpServletRequest request){
        String json_str = JSON.toJSONString(request.getParameterMap());
        String owner = getUser().getId();
        String id =jarTaskInfo.getId();
        jarTaskInfo.setOwner(owner);
        debugInfo(jarTaskInfo);
        jarTaskMapper.updateByPrimaryKey(jarTaskInfo);
        System.out.println(json_str);

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
                String nginx_dir = zdhNginx.getNginx_dir();
                try {
                    jar_file.transferTo(tempFile);
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
                    e.printStackTrace();
                } catch (SftpException e) {
                    e.printStackTrace();
                }
            }

        }


        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping("/etl_task_jar_del_file")
    @ResponseBody
    public String etl_task_jar_del_file(String[] ids, HttpServletRequest request) {
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
                    e.printStackTrace();
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


        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/etl_task_jar_file_list")
    @ResponseBody
    public List<JarFileInfo> etl_task_jar_file_list(String id, HttpServletRequest request) {
        String json_str = JSON.toJSONString(request.getParameterMap());
        String owner = getUser().getId();
        JarFileInfo jarFileInfo = new JarFileInfo();
        jarFileInfo.setOwner(owner);
        jarFileInfo.setJar_etl_id(id);
        List<JarFileInfo> jarFileInfos=jarFileMapper.select(jarFileInfo);

        return jarFileInfos;
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
                    e.printStackTrace();
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
    }

}
