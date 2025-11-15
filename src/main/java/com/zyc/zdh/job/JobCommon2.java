package com.zyc.zdh.job;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.NumberUtil;
import com.alibaba.druid.util.JdbcUtils;
import com.google.common.collect.Lists;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.lib.fn.ELFunctionDefinition;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.zyc.rqueue.RQueueClient;
import com.zyc.rqueue.RQueueManager;
import com.zyc.rqueue.RQueueMode;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.datax_generator.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.run.SystemCommandLineRunner;
import com.zyc.zdh.run.ZdhRunableTask;
import com.zyc.zdh.run.ZdhThreadFactory;
import com.zyc.zdh.service.EtlTaskService;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.MDC;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.*;

public class JobCommon2 {

    public static LinkedBlockingDeque<ZdhLogs> linkedBlockingDeque = new LinkedBlockingDeque<ZdhLogs>();

    public static ConcurrentHashMap<String, Future<?>> chm = new ConcurrentHashMap<String, Future<?>>();
    public static ConcurrentHashMap<String, Thread> chm_thread = new ConcurrentHashMap<String, Thread>();
    public static ConcurrentHashMap<String, Process> chm_process = new ConcurrentHashMap<String, Process>();
    public static ConcurrentHashMap<String, SSHUtil> chm_ssh = new ConcurrentHashMap<String, SSHUtil>();

    public static ThreadGroup threadGroup = new ThreadGroup("zdh_task");

    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(Const.ETL_THREAD_MIN_NUM,
            Const.ETL_THREAD_MAX_NUM, Const.ETL_THREAD_KEEP_ACTIVE_TIME, TimeUnit.SECONDS, new LinkedBlockingQueue<>(),  new ZdhThreadFactory("zdh_task_", threadGroup));

    public static void logThread(ZdhLogsService zdhLogsService) {

        ZdhRunableTask zdhRunableTask = new ZdhRunableTask("zdh_log_thread", new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        ZdhLogs log = JobCommon2.linkedBlockingDeque.take();
                        if (log != null) {
                            zdhLogsService.insert(log);
                        }
                    } catch (Exception e) {
                        LogUtil.error(this.getClass(), e);
                    }
                }
            }
        });
        threadPoolExecutor.submit(zdhRunableTask);
    }

    /**
     * 超过次数限制会主动杀掉调度,并设置状态为error
     *
     * @param jobType SHELL,JDBC,FTP,CLASS
     * @param tli     return true 表示超过次数限制
     */
    public static boolean isCount(String jobType, TaskLogInstance tli) {
        LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,开始检查任务次数限制");
        insertLog(tli, "INFO", "[" + jobType + "] JOB ,开始检查任务次数限制");

        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        TaskLogInstanceMapper taskLogInstanceMapper = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        //判断次数,上次任务完成重置次数
        if (tli.getLast_status() == null || tli.getLast_status().equals("finish")) {
            tli.setCount(0);
        }
        tli.setCount(tli.getCount() + 1);

        //设置执行的进度
        tli.setProcess(ProcessEnum.CHECK_COUNT);

        if (tli.getPlan_count().trim().equals("-1")) {
            LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,当前任务未设置执行次数限制");
            insertLog(tli, "info", "[" + jobType + "] JOB ,当前任务未设置执行次数限制");
        }

        if (!tli.getPlan_count().trim().equals("") && !tli.getPlan_count().trim().equals("-1")) {
            //任务有次数限制,重试多次后仍失败会删除任务
            if (tli.getCount() - 1 > Long.parseLong(tli.getPlan_count().trim())) {
                LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB 检任务次测到重试数超过限制,删除任务并直接返回结束");
                insertLog(tli, "info", "[" + jobType + "] JOB 检任务次测到重试数超过限制,删除任务并直接返回结束");
                QuartzJobInfo qji = new QuartzJobInfo();
                qji.setJob_id(tli.getJob_id());
                qji.setEtl_task_id(tli.getEtl_task_id());
                quartzManager2.deleteTask(tli, "finish", "error");
                quartzJobMapper.updateLastStatus(tli.getJob_id(), "error");

                insertLog(tli, "info", "[" + jobType + "] JOB ,结束调度任务");
                tli.setStatus(JobStatus.ERROR.getValue());
                updateTaskLog(tli, taskLogInstanceMapper);
                return true;
            }
        }
        insertLog(tli, "INFO", "[" + jobType + "] JOB ,完成检查任务次数限制,未超过限制次数");
        updateTaskLog(tli, taskLogInstanceMapper);
        return false;
    }

    /**
     * 通用新增etl_date 参数
     * @param tli
     * @param cur_time
     */
    public static void addEtlDate2Params(TaskLogInstance tli, Timestamp cur_time){
        Map<String, Object> new_params = JsonUtil.createEmptyMap();
        String date = DateUtil.formatTime(cur_time);
        LogUtil.info(JobCommon2.class, " JOB ,{},处理当前日期,传递参数ETL_DATE 为 {}", tli.getMore_task(), date);
        if(StringUtils.isEmpty(tli.getParams())){
            new_params.put("ETL_DATE", date);
        }else{
            new_params = JsonUtil.toJavaMap(tli.getParams());
            new_params.put("ETL_DATE", date);
        }

        tli.setParams(JsonUtil.formatJsonString(new_params));
    }

    public static ZdhInfo create_zdhInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                         EtlTaskService etlTaskService, DataSourcesMapper dataSourcesMapper, ZdhNginxMapper zdhNginxMapper, EtlMoreTaskMapper etlMoreTaskMapper) throws Exception {

        addEtlDate2Params(tli, tli.getCur_time());

        String etl_task_id = tli.getEtl_task_id();
        //获取etl 任务信息
        EtlTaskInfo etlTaskInfo = etlTaskService.selectById(etl_task_id);
        if (etlTaskInfo == null) {
            LogUtil.info(JobCommon2.class, "无法找到对应的[单源]ETL任务,任务id:" + etl_task_id);
            throw new Exception("无法找到对应的[单源]ETL任务,任务id:" + etl_task_id);
        }

        Map<String, Object> map = (Map<String, Object>)JsonUtil.toJavaMap(tli.getParams());
        //此处做参数匹配转换
        if (map != null) {
            LogUtil.info(JobCommon2.class, "单源,自定义参数不为空,开始替换:" + tli.getParams());
            //System.out.println("自定义参数不为空,开始替换:" + dti.getParams());
            DynamicParams(map, tli, etlTaskInfo);
        }

        //获取数据源信息
        String data_sources_choose_input = etlTaskInfo.getData_sources_choose_input();
        String data_sources_choose_output = etlTaskInfo.getData_sources_choose_output();
        DataSourcesInfo dataSourcesInfoInput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_input);
        if (dataSourcesInfoInput == null) {
            LogUtil.info(JobCommon2.class, "[单源任务]无法找到对应的[输入]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
            throw new Exception("[单源任务]无法找到对应的[输入]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
        }
        DataSourcesInfo dataSourcesInfoOutput = null;
        if (data_sources_choose_output != null && !data_sources_choose_output.equals("")) {
            dataSourcesInfoOutput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_output);
        }
        if (dataSourcesInfoOutput == null) {
            LogUtil.info(JobCommon2.class, "[单源任务]无法找到对应的[输出]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_output);
            throw new Exception("[单源任务]无法找到对应的[输出]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_output);
        }

        if (dataSourcesInfoInput.getData_source_type() != null && dataSourcesInfoInput.getData_source_type().equals("外部上传")) {
            //获取文件服务器信息 配置到数据源选项
            ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(dataSourcesInfoInput.getOwner());
            if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                dataSourcesInfoInput.setUrl(zdhNginx.getHost() + ":" + zdhNginx.getPort());
                dataSourcesInfoInput.setUsername(zdhNginx.getUsername());
                dataSourcesInfoInput.setPassword(zdhNginx.getPassword());
            }
        }

        if (dataSourcesInfoOutput.getData_source_type() != null && dataSourcesInfoOutput.getData_source_type().equals("外部下载")) {
            //获取文件服务器信息 配置到数据源选项
            ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(dataSourcesInfoOutput.getOwner());
            if(zdhNginx == null){
                LogUtil.info(JobCommon2.class, "当前用户未配置文件服务器信息");
                throw new Exception("当前用户未配置文件服务器信息");
            }
            if (!zdhNginx.getHost().equals("")) {
                dataSourcesInfoOutput.setUrl(zdhNginx.getHost() + ":" + zdhNginx.getPort());
                dataSourcesInfoOutput.setUsername(zdhNginx.getUsername());
                dataSourcesInfoOutput.setPassword(zdhNginx.getPassword());
                if (etlTaskInfo.getData_sources_params_output() != null && !etlTaskInfo.getData_sources_params_output().trim().equals("")) {
                    Map<String, Object> jsonObject = JsonUtil.toJavaMap(etlTaskInfo.getData_sources_params_output());
                    jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + tli.getOwner());
                    etlTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                } else {
                    Map<String, Object> jsonObject = JsonUtil.createEmptyMap();
                    jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + tli.getOwner());
                    etlTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                }
            } else {
                if (etlTaskInfo.getData_sources_params_output() != null && !etlTaskInfo.getData_sources_params_output().trim().equals("")) {
                    Map<String, Object> jsonObject = JsonUtil.toJavaMap(etlTaskInfo.getData_sources_params_output());
                    jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + tli.getOwner());
                    etlTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                } else {
                    Map<String, Object> jsonObject = JsonUtil.createEmptyMap();
                    jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + tli.getOwner());
                    etlTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                }
            }
        }

        ZdhInfo zdhInfo = new ZdhInfo();
        zdhInfo.setZdhInfo(dataSourcesInfoInput, etlTaskInfo, dataSourcesInfoOutput, tli);

        return zdhInfo;

    }

    public static ZdhMoreInfo create_more_task_zdhInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                                       EtlTaskService etlTaskService, DataSourcesMapper dataSourcesMapper, ZdhNginxMapper zdhNginxMapper, EtlMoreTaskMapper etlMoreTaskMapper) throws Exception {
        try {

            addEtlDate2Params(tli, tli.getCur_time());

            String etl_task_id = tli.getEtl_task_id();

            //获取多源任务id
            EtlMoreTaskInfo etlMoreTaskInfo = etlMoreTaskMapper.selectByPrimaryKey(etl_task_id);

            //解析多源任务中的单任务
            String[] etl_ids = etlMoreTaskInfo.getEtl_ids().split(",");
            //获取etl 任务信息
            List<EtlTaskInfo> etlTaskInfos = etlTaskService.selectByIds(etl_ids);

            ZdhMoreInfo zdhMoreInfo = new ZdhMoreInfo();
            zdhMoreInfo.setEtlMoreTaskInfo(etlMoreTaskInfo);
            //获取最终输出数据源
            String data_sources_choose_output = etlMoreTaskInfo.getData_sources_choose_output();
            DataSourcesInfo dataSourcesInfoOutput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_output);
            if (dataSourcesInfoOutput == null) {
                LogUtil.info(JobCommon2.class, "[多源任务]无法找到对应的[输出]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_output);
                JobCommon2.insertLog(tli, "WARN", "[多源任务]无法找到对应的[输出]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_output);
                dataSourcesInfoOutput = new DataSourcesInfo();
            }

            if (dataSourcesInfoOutput.getData_source_type() != null && dataSourcesInfoOutput.getData_source_type().equals("外部下载")) {
                //获取文件服务器信息 配置到数据源选项
                ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(dataSourcesInfoOutput.getOwner());
                if(zdhNginx == null){
                    LogUtil.info(JobCommon2.class, "当前用户未配置文件服务器信息");
                    throw new Exception("当前用户未配置文件服务器信息");
                }
                if (!zdhNginx.getHost().equals("")) {
                    dataSourcesInfoOutput.setUrl(zdhNginx.getHost() + ":" + zdhNginx.getPort());
                    dataSourcesInfoOutput.setUsername(zdhNginx.getUsername());
                    dataSourcesInfoOutput.setPassword(zdhNginx.getPassword());
                    if (etlMoreTaskInfo.getData_sources_params_output() != null && !etlMoreTaskInfo.getData_sources_params_output().trim().equals("")) {
                        Map<String, Object> jsonObject = JsonUtil.toJavaMap(etlMoreTaskInfo.getData_sources_params_output());
                        jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + etlMoreTaskInfo.getOwner());
                        etlMoreTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                    } else {
                        Map<String, Object> jsonObject = JsonUtil.createEmptyMap();
                        jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + etlMoreTaskInfo.getOwner());
                        etlMoreTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                    }
                } else {
                    if (etlMoreTaskInfo.getData_sources_params_output() != null && !etlMoreTaskInfo.getData_sources_params_output().trim().equals("")) {
                        Map<String, Object> jsonObject = JsonUtil.toJavaMap(etlMoreTaskInfo.getData_sources_params_output());
                        jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + etlMoreTaskInfo.getOwner());
                        etlMoreTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                    } else {
                        Map<String, Object> jsonObject = JsonUtil.createEmptyMap();
                        jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + etlMoreTaskInfo.getOwner());
                        etlMoreTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                    }
                }
            }


            Map<String, Object> map = (Map<String, Object>) JsonUtil.toJavaMap(tli.getParams());
            //此处做参数匹配转换
            for (EtlTaskInfo etlTaskInfo : etlTaskInfos) {
                if (map != null) {
                    LogUtil.info(JobCommon2.class, "多源,自定义参数不为空,开始替换:" + tli.getParams());
                    DynamicParams(map, tli, etlTaskInfo);
                }

                //获取数据源信息
                String data_sources_choose_input = etlTaskInfo.getData_sources_choose_input();
                DataSourcesInfo dataSourcesInfoInput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_input);
                if (dataSourcesInfoInput == null) {
                    LogUtil.info(JobCommon2.class, "[多源任务]无法找到对应的[输入]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
                    JobCommon2.insertLog(tli, "ERROR", "[多源任务]无法找到对应的[输入]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
                    throw new Exception("[多源任务]无法找到对应的[输入]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
                }
                if (dataSourcesInfoInput.getData_source_type().equals("外部上传")) {
                    //获取文件服务器信息 配置到数据源选项
                    ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(dataSourcesInfoInput.getOwner());
                    if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                        dataSourcesInfoInput.setUrl(zdhNginx.getHost() + ":" + zdhNginx.getPort());
                        dataSourcesInfoInput.setUsername(zdhNginx.getUsername());
                        dataSourcesInfoInput.setPassword(zdhNginx.getPassword());
                    }
                }


                zdhMoreInfo.setZdhMoreInfo(dataSourcesInfoInput, etlTaskInfo, dataSourcesInfoOutput, tli, etlMoreTaskInfo);
            }


            return zdhMoreInfo;
        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            throw e;
        }
    }

    public static ZdhSqlInfo create_zdhSqlInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                               SqlTaskMapper sqlTaskMapper, DataSourcesMapper dataSourcesMapper, ZdhNginxMapper zdhNginxMapper) throws Exception {

        try {

            addEtlDate2Params(tli, tli.getCur_time());

            String etl_task_id = tli.getEtl_task_id();
            //获取etl 任务信息
            SqlTaskInfo sqlTaskInfo = sqlTaskMapper.selectByPrimaryKey(etl_task_id);

            Map<String, Object> map = (Map<String, Object>) JsonUtil.toJavaMap(tli.getParams());
            //此处做参数匹配转换
            if (map != null) {
                LogUtil.info(JobCommon2.class, "SQL,自定义参数不为空,开始替换:" + tli.getParams());
                //System.out.println("自定义参数不为空,开始替换:" + dti.getParams());
                DynamicParams(map, tli, sqlTaskInfo);
            }

            //获取数据源信息
            String data_sources_choose_input = sqlTaskInfo.getData_sources_choose_input();
            String data_sources_choose_output = sqlTaskInfo.getData_sources_choose_output();
            DataSourcesInfo dataSourcesInfoInput = new DataSourcesInfo();

            DataSourcesInfo dataSourcesInfoOutput = new DataSourcesInfo();
            if (data_sources_choose_output != null && !data_sources_choose_output.equalsIgnoreCase("")) {
                dataSourcesInfoOutput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_output);
            }

            if (dataSourcesInfoOutput == null) {
                LogUtil.info(JobCommon2.class, "[SQL]无法找到对应的[输出]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_output);
                JobCommon2.insertLog(tli, "WARN", "[SQL]无法找到对应的[输出]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_output);
                //throw new Exception("[SQL]无法找到对应的[输出]数据源,任务id:" + etl_task_id+",数据源id:"+data_sources_choose_output);
            }

            if (dataSourcesInfoOutput.getData_source_type() != null && dataSourcesInfoOutput.getData_source_type().equals("外部下载")) {
                //获取文件服务器信息 配置到数据源选项
                ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(dataSourcesInfoOutput.getOwner());
                if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                    dataSourcesInfoOutput.setUrl(zdhNginx.getHost() + ":" + zdhNginx.getPort());
                    dataSourcesInfoOutput.setUsername(zdhNginx.getUsername());
                    dataSourcesInfoOutput.setPassword(zdhNginx.getPassword());
                    if (sqlTaskInfo.getData_sources_params_output() != null && !sqlTaskInfo.getData_sources_params_output().trim().equals("")) {
                        Map<String, Object> jsonObject = JsonUtil.toJavaMap(sqlTaskInfo.getData_sources_params_output());
                        jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + sqlTaskInfo.getOwner());
                        sqlTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                    } else {
                        Map<String, Object> jsonObject = JsonUtil.createEmptyMap();
                        jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" +  sqlTaskInfo.getOwner());
                        sqlTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                    }
                } else {
                    if (sqlTaskInfo.getData_sources_params_output() != null && !sqlTaskInfo.getData_sources_params_output().trim().equals("")) {
                        Map<String, Object> jsonObject = JsonUtil.toJavaMap(sqlTaskInfo.getData_sources_params_output());
                        jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" +  sqlTaskInfo.getOwner());
                        sqlTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                    } else {
                        Map<String, Object> jsonObject = JsonUtil.createEmptyMap();
                        jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" +  sqlTaskInfo.getOwner());
                        sqlTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                    }
                }
            }

            ZdhSqlInfo zdhSqlInfo = new ZdhSqlInfo();
            zdhSqlInfo.setZdhInfo(dataSourcesInfoInput, sqlTaskInfo, dataSourcesInfoOutput, tli);

            return zdhSqlInfo;

        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            throw e;
        }

    }

    public static ZdhSshInfo create_zhdSshInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                               SshTaskMapper sshTaskMapper, ZdhNginxMapper zdhNginxMapper) {

        try {

            JarFileMapper jarFileMapper = (JarFileMapper) SpringContext.getBean("jarFileMapper");

            addEtlDate2Params(tli, tli.getCur_time());


            String etl_task_id = tli.getEtl_task_id();
            //获取etl 任务信息
            SshTaskInfo sshTaskInfo = sshTaskMapper.selectByPrimaryKey(etl_task_id);

            Map<String, Object> map = (Map<String, Object>) JsonUtil.toJavaMap(tli.getParams());
            //此处做参数匹配转换
            if (map != null) {
                LogUtil.info(JobCommon2.class, "JAR,自定义参数不为空,开始替换:" + tli.getParams());
                //System.out.println("自定义参数不为空,开始替换:" + dti.getParams());
                DynamicParams(map, tli, sshTaskInfo);
            }
            ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(sshTaskInfo.getOwner());
            List<JarFileInfo> jarFileInfos = jarFileMapper.selectByParams2(sshTaskInfo.getOwner(), new String[]{sshTaskInfo.getId()});

            ZdhSshInfo zdhSshInfo = new ZdhSshInfo();
            zdhSshInfo.setZdhInfo(sshTaskInfo, tli, zdhNginx, jarFileInfos);

            return zdhSshInfo;

        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            throw e;
        }

    }


    public static ZdhDroolsInfo create_zdhDroolsInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                                     EtlTaskService etlTaskService, DataSourcesMapper dataSourcesMapper, ZdhNginxMapper zdhNginxMapper, EtlDroolsTaskMapper etlDroolsTaskMapper,
                                                     EtlMoreTaskMapper etlMoreTaskMapper, SqlTaskMapper sqlTaskMapper) throws Exception {
        try {

            addEtlDate2Params(tli, tli.getCur_time());

            String etl_task_id = tli.getEtl_task_id();

            //获取Drools任务id
            EtlDroolsTaskInfo etlDroolsTaskInfo = etlDroolsTaskMapper.selectByPrimaryKey(etl_task_id);

            ZdhDroolsInfo zdhDroolsInfo = new ZdhDroolsInfo();

            //获取最终输出数据源
            String data_sources_choose_output = etlDroolsTaskInfo.getData_sources_choose_output();
            DataSourcesInfo dataSourcesInfoOutput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_output);
            if (dataSourcesInfoOutput == null) {
                LogUtil.info(JobCommon2.class, "无法找到对应的数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_output);
                throw new Exception("无法找到对应的数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_output);
            }
            if (dataSourcesInfoOutput.getData_source_type() != null && dataSourcesInfoOutput.getData_source_type().equals("外部下载")) {
                //获取文件服务器信息 配置到数据源选项
                ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(dataSourcesInfoOutput.getOwner());
                if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                    dataSourcesInfoOutput.setUrl(zdhNginx.getHost() + ":" + zdhNginx.getPort());
                    dataSourcesInfoOutput.setUsername(zdhNginx.getUsername());
                    dataSourcesInfoOutput.setPassword(zdhNginx.getPassword());
                    if (etlDroolsTaskInfo.getData_sources_params_output() != null && !etlDroolsTaskInfo.getData_sources_params_output().trim().equals("")) {
                        Map<String, Object> jsonObject = JsonUtil.toJavaMap(etlDroolsTaskInfo.getData_sources_params_output());
                        jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" +  etlDroolsTaskInfo.getOwner());
                        etlDroolsTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                    } else {
                        Map<String, Object> jsonObject = JsonUtil.createEmptyMap();
                        jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + etlDroolsTaskInfo.getOwner());
                        etlDroolsTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                    }
                } else {
                    if (etlDroolsTaskInfo.getData_sources_params_output() != null && !etlDroolsTaskInfo.getData_sources_params_output().trim().equals("")) {
                        Map<String, Object> jsonObject = JsonUtil.toJavaMap(etlDroolsTaskInfo.getData_sources_params_output());
                        jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + etlDroolsTaskInfo.getOwner());
                        etlDroolsTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                    } else {
                        Map<String, Object> jsonObject = JsonUtil.createEmptyMap();
                        jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + etlDroolsTaskInfo.getOwner());
                        etlDroolsTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                    }
                }
            }


            if (etlDroolsTaskInfo.getMore_task().equalsIgnoreCase(MoreTask.ETL.getValue())) {
                //解析Drools任务中的单任务
                String etl_id = etlDroolsTaskInfo.getEtl_id();
                //获取etl 任务信息
                EtlTaskInfo etlTaskInfo = etlTaskService.selectById(etl_id);

                zdhDroolsInfo.setEtlDroolsTaskInfo(etlDroolsTaskInfo);

                Map<String, Object> map = (Map<String, Object>) JsonUtil.toJavaMap(tli.getParams());
                //此处做参数匹配转换

                if (map != null) {
                    LogUtil.info(JobCommon2.class, "多源,自定义参数不为空,开始替换:" + tli.getParams());
                    DynamicParams(map, tli, etlTaskInfo);
                }

                //获取数据源信息
                String data_sources_choose_input = etlTaskInfo.getData_sources_choose_input();
                DataSourcesInfo dataSourcesInfoInput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_input);
                if (dataSourcesInfoInput == null) {
                    LogUtil.info(JobCommon2.class, "无法找到对应的数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
                    throw new Exception("无法找到对应的数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
                }
                if (dataSourcesInfoInput.getData_source_type().equals("外部上传")) {
                    //获取文件服务器信息 配置到数据源选项
                    ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(dataSourcesInfoInput.getOwner());
                    if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                        dataSourcesInfoInput.setUrl(zdhNginx.getHost() + ":" + zdhNginx.getPort());
                        dataSourcesInfoInput.setUsername(zdhNginx.getUsername());
                        dataSourcesInfoInput.setPassword(zdhNginx.getPassword());
                    }
                }
                zdhDroolsInfo.setZdhDroolsInfo(dataSourcesInfoInput, etlTaskInfo, dataSourcesInfoOutput, tli, etlDroolsTaskInfo);
            }

            if (etlDroolsTaskInfo.getMore_task().equalsIgnoreCase("MOER_ETL")) {
                //获取多源任务id
                EtlMoreTaskInfo etlMoreTaskInfo = etlMoreTaskMapper.selectByPrimaryKey(etlDroolsTaskInfo.getEtl_id());
                zdhDroolsInfo.setEtlMoreTaskInfo(etlMoreTaskInfo);

                //解析多源任务中的单任务
                String[] etl_ids = etlMoreTaskInfo.getEtl_ids().split(",");
                //获取etl 任务信息
                List<EtlTaskInfo> etlTaskInfos = etlTaskService.selectByIds(etl_ids);

                for (EtlTaskInfo etlTaskInfo : etlTaskInfos) {
                    Map<String, Object> map = (Map<String, Object>) JsonUtil.toJavaMap(tli.getParams());
                    //此处做参数匹配转换
                    if (map != null) {
                        LogUtil.info(JobCommon2.class, "多源,自定义参数不为空,开始替换:" + tli.getParams());
                        DynamicParams(map, tli, etlTaskInfo);
                    }

                    //获取数据源信息
                    String data_sources_choose_input = etlTaskInfo.getData_sources_choose_input();
                    DataSourcesInfo dataSourcesInfoInput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_input);
                    if (dataSourcesInfoOutput == null) {
                        LogUtil.info(JobCommon2.class, "无法找到对应的数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_output);
                        throw new Exception("无法找到对应的数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_output);
                    }


                    if (dataSourcesInfoInput.getData_source_type().equals("外部上传")) {
                        //获取文件服务器信息 配置到数据源选项
                        ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(dataSourcesInfoInput.getOwner());
                        if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                            dataSourcesInfoInput.setUrl(zdhNginx.getHost() + ":" + zdhNginx.getPort());
                            dataSourcesInfoInput.setUsername(zdhNginx.getUsername());
                            dataSourcesInfoInput.setPassword(zdhNginx.getPassword());
                        }
                    }
                    zdhDroolsInfo.setZdhDroolsInfo(dataSourcesInfoInput, etlTaskInfo, dataSourcesInfoOutput, tli, etlDroolsTaskInfo);
                }

            }
            if (etlDroolsTaskInfo.getMore_task().equalsIgnoreCase("SQL")) {

                //获取etl 任务信息
                SqlTaskInfo sqlTaskInfo = sqlTaskMapper.selectByPrimaryKey(etlDroolsTaskInfo.getEtl_id());

                if (sqlTaskInfo == null) {
                    throw new Exception("无法找到对应的SQL任务");
                }

                Map<String, Object> map = (Map<String, Object>) JsonUtil.toJavaMap(tli.getParams());
                //此处做参数匹配转换
                if (map != null) {
                    LogUtil.info(JobCommon2.class, "SQL,自定义参数不为空,开始替换:" + tli.getParams());
                    DynamicParams(map, tli, sqlTaskInfo);
                }

                zdhDroolsInfo.setSqlTaskInfo(sqlTaskInfo);
                zdhDroolsInfo.setTli(tli);
            }

            //设置drool 任务信息
            zdhDroolsInfo.setEtlDroolsTaskInfo(etlDroolsTaskInfo);
            //设置drool 输出数据源信息
            zdhDroolsInfo.setDsi_Info(dataSourcesInfoOutput);


            return zdhDroolsInfo;
        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            throw e;
        }
    }


    public static ZdhApplyInfo create_zdhApplyInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                                   EtlTaskService etlTaskService, DataSourcesMapper dataSourcesMapper, ZdhNginxMapper zdhNginxMapper, EtlMoreTaskMapper etlMoreTaskMapper) throws Exception {

        EtlApplyTaskMapper etlApplyTaskMapper = (EtlApplyTaskMapper) SpringContext.getBean("etlApplyTaskMapper");
        ApplyMapper applyMapper = (ApplyMapper) SpringContext.getBean("applyMapper");

        addEtlDate2Params(tli, tli.getCur_time());

        String etl_task_id = tli.getEtl_task_id();
        //获取etl 任务信息
        EtlApplyTaskInfo etlApplyTaskInfo = etlApplyTaskMapper.selectByIds(new String[]{etl_task_id});
        if (etlApplyTaskInfo == null) {
            LogUtil.info(JobCommon2.class, "无法找到对应的[申请源]ETL任务,任务id:" + etl_task_id);
            throw new Exception("无法找到对应的[申请源]ETL任务,任务id:" + etl_task_id);
        }

        Map<String, Object> map = (Map<String, Object>) JsonUtil.toJavaMap(tli.getParams());
        //此处做参数匹配转换
        if (map != null) {
            LogUtil.info(JobCommon2.class, "申请源,自定义参数不为空,开始替换:" + tli.getParams());
            //System.out.println("自定义参数不为空,开始替换:" + dti.getParams());
            DynamicParams(map, tli, etlApplyTaskInfo);
        }

        //获取数据源信息
        String data_sources_choose_input = etlApplyTaskInfo.getData_sources_choose_input();
        String data_sources_choose_output = etlApplyTaskInfo.getData_sources_choose_output();

        //根据输入源id(申请表id)获取具体的数据源
        ApplyIssueInfo applyIssueInfo = applyMapper.selectByParams4(data_sources_choose_input, null, null, etlApplyTaskInfo.getOwner());

        if (applyIssueInfo == null) {
            LogUtil.info(JobCommon2.class, "[申请源任务]无法找到对应的[申请id],任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
            throw new Exception("[申请源任务]无法找到对应的[申请id],任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
        }

        DataSourcesInfo dataSourcesInfoInput = dataSourcesMapper.selectByPrimaryKey(applyIssueInfo.getData_sources_choose_input());

        if (dataSourcesInfoInput == null) {
            LogUtil.info(JobCommon2.class, "[申请源任务]无法找到对应的[输入]数据源,任务id:" + etl_task_id + ",数据源id:" + applyIssueInfo.getData_sources_choose_input());
            throw new Exception("[申请源任务]无法找到对应的[输入]数据源,任务id:" + etl_task_id + ",数据源id:" + applyIssueInfo.getData_sources_choose_input());
        }


        DataSourcesInfo dataSourcesInfoOutput = null;
        if (data_sources_choose_output != null && !data_sources_choose_output.equals("")) {
            dataSourcesInfoOutput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_output);
        }
        if (dataSourcesInfoOutput == null) {
            LogUtil.info(JobCommon2.class, "[申请源任务]无法找到对应的[输出]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_output);
            throw new Exception("[申请源任务]无法找到对应的[输出]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_output);
        }

        if (dataSourcesInfoOutput.getData_source_type() != null && dataSourcesInfoOutput.getData_source_type().equals("外部下载")) {
            //获取文件服务器信息 配置到数据源选项
            ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(dataSourcesInfoOutput.getOwner());
            if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                dataSourcesInfoOutput.setUrl(zdhNginx.getHost() + ":" + zdhNginx.getPort());
                dataSourcesInfoOutput.setUsername(zdhNginx.getUsername());
                dataSourcesInfoOutput.setPassword(zdhNginx.getPassword());
                if (etlApplyTaskInfo.getData_sources_params_output() != null && !etlApplyTaskInfo.getData_sources_params_output().trim().equals("")) {
                    Map<String, Object> jsonObject = JsonUtil.toJavaMap(etlApplyTaskInfo.getData_sources_params_output());
                    jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + etlApplyTaskInfo.getOwner());
                    etlApplyTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                } else {
                    Map<String, Object> jsonObject = JsonUtil.createEmptyMap();
                    jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + etlApplyTaskInfo.getOwner());
                    etlApplyTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                }
            } else {
                if (etlApplyTaskInfo.getData_sources_params_output() != null && !etlApplyTaskInfo.getData_sources_params_output().trim().equals("")) {
                    Map<String, Object> jsonObject = JsonUtil.toJavaMap(etlApplyTaskInfo.getData_sources_params_output());
                    jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + etlApplyTaskInfo.getOwner());
                    etlApplyTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                } else {
                    Map<String, Object> jsonObject = JsonUtil.createEmptyMap();
                    jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + etlApplyTaskInfo.getOwner());
                    etlApplyTaskInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                }
            }
        }

        ZdhApplyInfo zdhApplyInfo = new ZdhApplyInfo();
        zdhApplyInfo.setZdhApplyInfo(dataSourcesInfoInput, etlApplyTaskInfo, dataSourcesInfoOutput, tli);

        return zdhApplyInfo;

    }


    public static ZdhFlinkSqlInfo create_zdhFlinkInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                                      EtlTaskFlinkMapper etlTaskFlinkMapper, DataSourcesMapper dataSourcesMapper, ZdhNginxMapper zdhNginxMapper) throws Exception {

        try {

            addEtlDate2Params(tli, tli.getCur_time());

            String etl_task_id = tli.getEtl_task_id();
            //获取etl 任务信息
            EtlTaskFlinkInfo etlTaskFlinkInfo = etlTaskFlinkMapper.selectByPrimaryKey(etl_task_id);

            Map<String, Object> map = (Map<String, Object>) JsonUtil.toJavaMap(tli.getParams());
            //此处做参数匹配转换
            if (map != null) {
                LogUtil.info(JobCommon2.class, "SQL,自定义参数不为空,开始替换:" + tli.getParams());
                //System.out.println("自定义参数不为空,开始替换:" + dti.getParams());
                DynamicParams(map, tli, etlTaskFlinkInfo);
            }


            ZdhFlinkSqlInfo zdhFlinkSqlInfo = new ZdhFlinkSqlInfo();
            zdhFlinkSqlInfo.setZdhInfo(etlTaskFlinkInfo, tli);

            return zdhFlinkSqlInfo;

        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            throw e;
        }

    }

    public static ZdhJdbcInfo create_zdhJdbcInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                                 EtlTaskJdbcMapper etlTaskJdbcMapper, DataSourcesMapper dataSourcesMapper, ZdhNginxMapper zdhNginxMapper) throws Exception {

        try {

            addEtlDate2Params(tli, tli.getCur_time());

            String etl_task_id = tli.getEtl_task_id();
            //获取etl 任务信息
            EtlTaskJdbcInfo etlTaskJdbcInfo = etlTaskJdbcMapper.selectByPrimaryKey(etl_task_id);

            Map<String, Object> map = (Map<String, Object>) JsonUtil.toJavaMap(tli.getParams());
            //此处做参数匹配转换
            if (map != null) {
                LogUtil.info(JobCommon2.class, "SQL,自定义参数不为空,开始替换:" + tli.getParams());
                //System.out.println("自定义参数不为空,开始替换:" + dti.getParams());
                DynamicParams(map, tli, etlTaskJdbcInfo);
            }

            //获取数据源信息
            String data_sources_choose_input = etlTaskJdbcInfo.getData_sources_choose_input();
            String data_sources_choose_output = etlTaskJdbcInfo.getData_sources_choose_output();

            DataSourcesInfo dataSourcesInfoInput = new DataSourcesInfo();
            if (data_sources_choose_input != null && !data_sources_choose_input.equalsIgnoreCase("")) {
                dataSourcesInfoInput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_input);
            }

            DataSourcesInfo dataSourcesInfoOutput = new DataSourcesInfo();
            if (data_sources_choose_output != null && !data_sources_choose_output.equalsIgnoreCase("")) {
                dataSourcesInfoOutput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_output);
            }

            if (dataSourcesInfoOutput == null) {
                LogUtil.info(JobCommon2.class, "[JDBC]无法找到对应的[输出]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_output);
                JobCommon2.insertLog(tli, "WARN", "[JDBC]无法找到对应的[输出]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_output);
                //throw new Exception("[SQL]无法找到对应的[输出]数据源,任务id:" + etl_task_id+",数据源id:"+data_sources_choose_output);
            }

            if (dataSourcesInfoOutput.getData_source_type() != null && dataSourcesInfoOutput.getData_source_type().equals("外部下载")) {
                //获取文件服务器信息 配置到数据源选项
                ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(dataSourcesInfoOutput.getOwner());
                if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                    dataSourcesInfoOutput.setUrl(zdhNginx.getHost() + ":" + zdhNginx.getPort());
                    dataSourcesInfoOutput.setUsername(zdhNginx.getUsername());
                    dataSourcesInfoOutput.setPassword(zdhNginx.getPassword());
                    if (etlTaskJdbcInfo.getData_sources_params_output() != null && !etlTaskJdbcInfo.getData_sources_params_output().trim().equals("")) {
                        Map<String, Object> jsonObject = JsonUtil.toJavaMap(etlTaskJdbcInfo.getData_sources_params_output());
                        jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + etlTaskJdbcInfo.getOwner());
                        etlTaskJdbcInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                    } else {
                        Map<String, Object> jsonObject = JsonUtil.createEmptyMap();
                        jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + etlTaskJdbcInfo.getOwner());
                        etlTaskJdbcInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                    }
                } else {
                    if (etlTaskJdbcInfo.getData_sources_params_output() != null && !etlTaskJdbcInfo.getData_sources_params_output().trim().equals("")) {
                        Map<String, Object> jsonObject = JsonUtil.toJavaMap(etlTaskJdbcInfo.getData_sources_params_output());
                        jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + etlTaskJdbcInfo.getOwner());
                        etlTaskJdbcInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                    } else {
                        Map<String, Object> jsonObject = JsonUtil.createEmptyMap();
                        jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + etlTaskJdbcInfo.getOwner());
                        etlTaskJdbcInfo.setData_sources_params_output(JsonUtil.formatJsonString(jsonObject));
                    }
                }
            }

            ZdhJdbcInfo zdhJdbcInfo = new ZdhJdbcInfo();
            zdhJdbcInfo.setZdhInfo(dataSourcesInfoInput, etlTaskJdbcInfo, dataSourcesInfoOutput, tli);

            return zdhJdbcInfo;

        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            throw e;
        }

    }


    public static ZdhDataxInfo create_zdhDataxInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                                   EtlTaskDataxMapper etlTaskDataxMapper, DataSourcesMapper dataSourcesMapper, ZdhNginxMapper zdhNginxMapper) throws Exception {

        try {

            addEtlDate2Params(tli, tli.getCur_time());

            String etl_task_id = tli.getEtl_task_id();
            //获取etl 任务信息
            EtlTaskDataxInfo etlTaskDataxInfo = etlTaskDataxMapper.selectByPrimaryKey(etl_task_id);

            Map<String, Object> map = (Map<String, Object>) JsonUtil.toJavaMap(tli.getParams());
            //此处做参数匹配转换
            if (map != null) {
                LogUtil.info(JobCommon2.class, "SQL,自定义参数不为空,开始替换:" + tli.getParams());
                //System.out.println("自定义参数不为空,开始替换:" + dti.getParams());
                DynamicParams(map, tli, etlTaskDataxInfo);
            }

            //获取数据源信息
            String data_sources_choose_input = etlTaskDataxInfo.getData_sources_choose_input();


            DataSourcesInfo dataSourcesInfoInput = null;
            if (data_sources_choose_input != null && !data_sources_choose_input.equalsIgnoreCase("")) {
                dataSourcesInfoInput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_input);
            }

            if (dataSourcesInfoInput == null) {
                LogUtil.info(JobCommon2.class, "[DATAX]无法找到对应的[输入]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
                //JobCommon2.insertLog(tli,"WARN","[DATAX]无法找到对应的[输入]数据源,任务id:" + etl_task_id+",数据源id:"+data_sources_choose_input);
                throw new Exception("[DATAX]无法找到对应的[输入]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
            }


            ZdhDataxInfo zdhDataxInfo = new ZdhDataxInfo();
            zdhDataxInfo.setZdhInfo(dataSourcesInfoInput, etlTaskDataxInfo, tli);

            return zdhDataxInfo;

        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            throw e;
        }

    }


    public static ZdhQualityInfo create_zdhQualityInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                                       QualityTaskMapper qualityTaskMapper, DataSourcesMapper dataSourcesMapper, ZdhNginxMapper zdhNginxMapper, QualityRuleMapper qualityRuleMapper) throws Exception {

        try{
            addEtlDate2Params(tli, tli.getCur_time());

            String etl_task_id = tli.getEtl_task_id();
            //获取etl 任务信息
            QualityTaskInfo qualityTaskInfo = qualityTaskMapper.selectByPrimaryKey(etl_task_id);
            if (qualityTaskInfo == null) {
                LogUtil.info(JobCommon2.class, "无法找到对应的[QUALITY]任务,任务id:" + etl_task_id);
                throw new Exception("无法找到对应的[QUALITY]任务,任务id:" + etl_task_id);
            }

            //增加quality_rule_info信息
            List<Map<String, Object>> jsonArray = new ArrayList<>();
            if (!StringUtils.isEmpty(qualityTaskInfo.getQuality_rule_config())) {
                for (Map<String, Object> obj : JsonUtil.toJavaListMap(qualityTaskInfo.getQuality_rule_config())) {

                    String id = obj.getOrDefault("quality_rule", "").toString();
                    QualityRuleInfo qualityRuleInfo = qualityRuleMapper.selectByPrimaryKey(id);
                    Map<String, Object> objectMap = BeanUtil.beanToMap(qualityRuleInfo);
                    objectMap.put("quality_columns", obj.getOrDefault("quality_columns", ""));
                    jsonArray.add(objectMap);
                }
            }

            qualityTaskInfo.setQuality_rule_config(JsonUtil.formatJsonString(jsonArray));

            Map<String, Object> map = (Map<String, Object>) JsonUtil.toJavaMap(tli.getParams());
            //此处做参数匹配转换
            if (map != null) {
                LogUtil.info(JobCommon2.class, "单源,自定义参数不为空,开始替换:" + tli.getParams());
                //System.out.println("自定义参数不为空,开始替换:" + dti.getParams());
                DynamicParams(map, tli, qualityTaskInfo);
            }

            //获取数据源信息
            String data_sources_choose_input = qualityTaskInfo.getData_sources_choose_input();

            DataSourcesInfo dataSourcesInfoInput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_input);
            if (dataSourcesInfoInput == null) {
                LogUtil.info(JobCommon2.class, "[单源任务]无法找到对应的[输入]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
                throw new Exception("[单源任务]无法找到对应的[输入]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
            }
            DataSourcesInfo dataSourcesInfoOutput = null;

            if (dataSourcesInfoInput.getData_source_type() != null && dataSourcesInfoInput.getData_source_type().equals("外部上传")) {
                //获取文件服务器信息 配置到数据源选项
                ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(dataSourcesInfoInput.getOwner());
                if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                    dataSourcesInfoInput.setUrl(zdhNginx.getHost() + ":" + zdhNginx.getPort());
                    dataSourcesInfoInput.setUsername(zdhNginx.getUsername());
                    dataSourcesInfoInput.setPassword(zdhNginx.getPassword());
                }
            }

            ZdhQualityInfo zdhQualityInfo = new ZdhQualityInfo();
            zdhQualityInfo.setZdhInfo(dataSourcesInfoInput, qualityTaskInfo, dataSourcesInfoOutput, tli);

            return zdhQualityInfo;
        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            throw e;
        }

    }


    public static ZdhUnstructureInfo create_zdhUnstructureInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                                               EtlTaskUnstructureMapper etlTaskUnstructureMapper, DataSourcesMapper dataSourcesMapper) throws Exception {

        try{
            addEtlDate2Params(tli, tli.getCur_time());

            String etl_task_id = tli.getEtl_task_id();
            //获取etl 任务信息
            EtlTaskUnstructureInfo etlTaskUnstructureInfo = etlTaskUnstructureMapper.selectByPrimaryKey(etl_task_id);
            if (etlTaskUnstructureInfo == null) {
                LogUtil.info(JobCommon2.class, "无法找到对应的[UNSTRUCTURE]任务,任务id:" + etl_task_id);
                throw new Exception("无法找到对应的[UNSTRUCTURE]任务,任务id:" + etl_task_id);
            }


            Map<String, Object> map = (Map<String, Object>) JsonUtil.toJavaMap(tli.getParams());
            //此处做参数匹配转换
            if (map != null) {
                LogUtil.info(JobCommon2.class, "非结构化任务,自定义参数不为空,开始替换:" + tli.getParams());
                //System.out.println("自定义参数不为空,开始替换:" + dti.getParams());
                DynamicParams(map, tli, etlTaskUnstructureInfo);
            }

            //获取数据源信息
            String data_sources_choose_input = etlTaskUnstructureInfo.getData_sources_choose_file_input();

            DataSourcesInfo dataSourcesInfoInput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_input);
            if (dataSourcesInfoInput == null) {
                LogUtil.info(JobCommon2.class, "[非结构化任务]无法找到对应的[输入]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
                throw new Exception("[非结构化任务]无法找到对应的[输入]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
            }

            String data_sources_choose_output = etlTaskUnstructureInfo.getData_sources_choose_file_output();
            DataSourcesInfo dataSourcesInfoOutput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_output);

            if (dataSourcesInfoOutput == null) {
                LogUtil.info(JobCommon2.class, "[非结构化任务]无法找到对应的[输出]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
                throw new Exception("[非结构化任务]无法找到对应的[输出]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
            }

            String data_sources_choose_output_jdbc = etlTaskUnstructureInfo.getData_sources_choose_jdbc_output();
            DataSourcesInfo dataSourcesInfoOutputJdbc = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_output_jdbc);


            ZdhUnstructureInfo zdhUnstructureInfo = new ZdhUnstructureInfo();
            zdhUnstructureInfo.setZdhInfo(dataSourcesInfoInput, etlTaskUnstructureInfo, dataSourcesInfoOutput, dataSourcesInfoOutputJdbc, tli);

            return zdhUnstructureInfo;
        }catch (Exception e){
            LogUtil.error(JobCommon2.class, e);
            throw e;
        }

    }

    public static ZdhDataxAutoInfo create_zdhDataxAutoInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                                   EtlTaskDataxAutoMapper etlTaskDataxAutoMapper, DataSourcesMapper dataSourcesMapper, Environment environment) throws Exception {

        try {

            addEtlDate2Params(tli, tli.getCur_time());

            String etl_task_id = tli.getEtl_task_id();
            //获取etl 任务信息
            EtlTaskDataxAutoInfo etlTaskDataxAutoInfo = etlTaskDataxAutoMapper.selectByPrimaryKey(etl_task_id);

            Map<String, Object> map = (Map<String, Object>) JsonUtil.toJavaMap(tli.getParams());
            //此处做参数匹配转换
            if (map != null) {
                LogUtil.info(JobCommon2.class, "SQL,自定义参数不为空,开始替换:" + tli.getParams());
                //System.out.println("自定义参数不为空,开始替换:" + dti.getParams());
                DynamicParams(map, tli, etlTaskDataxAutoInfo);
            }

            //获取数据源信息
            String data_sources_choose_input = etlTaskDataxAutoInfo.getData_sources_choose_input();


            DataSourcesInfo dataSourcesInfoInput = null;
            if (data_sources_choose_input != null && !data_sources_choose_input.equalsIgnoreCase("")) {
                dataSourcesInfoInput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_input);
            }

            if (dataSourcesInfoInput == null) {
                LogUtil.info(JobCommon2.class, "[DATAX]无法找到对应的[输入]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
                //JobCommon2.insertLog(tli,"WARN","[DATAX]无法找到对应的[输入]数据源,任务id:" + etl_task_id+",数据源id:"+data_sources_choose_input);
                throw new Exception("[DATAX]无法找到对应的[输入]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
            }

            String data_sources_choose_output = etlTaskDataxAutoInfo.getData_sources_choose_output();
            DataSourcesInfo dataSourcesInfoOutput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_output);

            if (dataSourcesInfoOutput == null) {
                LogUtil.info(JobCommon2.class, "[DATAX]无法找到对应的[输出]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
                throw new Exception("[DATAX]无法找到对应的[输出]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
            }

            String python_home = environment.getProperty("python_engine");
            String datax_home = environment.getProperty("datax_home");

            if(map.containsKey("python_engine")){
                python_home = map.get("python_engine").toString();
            }
            if(map.containsKey("datax_home")){
                datax_home = map.get("datax_home").toString();
            }

            if(StringUtils.isEmpty(python_home)){
                LogUtil.info(JobCommon2.class, "[DATAX]无法找到对应的PYTHON环境,任务id:" + etl_task_id);
                throw new Exception("[DATAX]无法找到对应的PYTHON环境,任务id:" + etl_task_id );
            }
            if(StringUtils.isEmpty(datax_home)){
                LogUtil.info(JobCommon2.class, "[DATAX]无法找到对应的DATAX环境,任务id:" + etl_task_id);
                throw new Exception("[DATAX]无法找到对应的DATAX环境,任务id:" + etl_task_id );
            }

            ZdhDataxAutoInfo zdhDataxAutoInfo = new ZdhDataxAutoInfo();
            zdhDataxAutoInfo.setZdhInfo(dataSourcesInfoInput, etlTaskDataxAutoInfo,dataSourcesInfoOutput, tli, python_home, datax_home);

            return zdhDataxAutoInfo;

        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            throw e;
        }

    }


    public static ZdhKettleAutoInfo create_zdhKettleAutoInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                                           EtlTaskKettleMapper etlTaskKettleMapper, DataSourcesMapper dataSourcesMapper, Environment environment) throws Exception {

        try {

            addEtlDate2Params(tli, tli.getCur_time());

            String etl_task_id = tli.getEtl_task_id();
            //获取etl 任务信息
            EtlTaskKettleInfo etlTaskKettleInfo = etlTaskKettleMapper.selectByPrimaryKey(etl_task_id);

            Map<String, Object> map = (Map<String, Object>) JsonUtil.toJavaMap(tli.getParams());
            //此处做参数匹配转换
            if (map != null) {
                LogUtil.info(JobCommon2.class, "SQL,自定义参数不为空,开始替换:" + tli.getParams());
                //System.out.println("自定义参数不为空,开始替换:" + dti.getParams());
                DynamicParams(map, tli, etlTaskKettleInfo);
            }

            //获取数据源信息
            String data_sources_choose_input = etlTaskKettleInfo.getData_sources_choose_input();


            DataSourcesInfo dataSourcesInfoInput = null;
            if (data_sources_choose_input != null && !data_sources_choose_input.equalsIgnoreCase("")) {
                dataSourcesInfoInput = dataSourcesMapper.selectByPrimaryKey(data_sources_choose_input);
            }

            if (dataSourcesInfoInput == null) {
                LogUtil.info(JobCommon2.class, "[KETTLE]无法找到对应的[输入]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
                //JobCommon2.insertLog(tli,"WARN","[DATAX]无法找到对应的[输入]数据源,任务id:" + etl_task_id+",数据源id:"+data_sources_choose_input);
                throw new Exception("[KETTLE]无法找到对应的[输入]数据源,任务id:" + etl_task_id + ",数据源id:" + data_sources_choose_input);
            }

            ZdhKettleAutoInfo zdhKettleAutoInfo = new ZdhKettleAutoInfo();
            zdhKettleAutoInfo.setZdhInfo(dataSourcesInfoInput, etlTaskKettleInfo, tli);

            return zdhKettleAutoInfo;

        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            throw e;
        }

    }


    /**
     * 根据时间(timestamp) 生成jinjava 模板中的时间参数
     *
     * @param tli
     * @return
     */
    public static Map<String, Object> getJinJavaParam(TaskLogInstance tli) {
        String msg = "目前支持日期参数以下模式: {{zdh_date}} => yyyy-MM-dd ,{{zdh_date_nodash}}=> yyyyMMdd " +
                ",{{zdh_date_time}}=> yyyy-MM-dd HH:mm:ss,{{zdh_year}}=> yyyy年,{{zdh_month}}=> 月,{{zdh_day}}=> 日," +
                "{{zdh_hour}}=>24小时制,{{zdh_minute}}=>分钟,{{zdh_second}}=>秒,{{zdh_time}}=>时间戳, 更多参数可参考【系统内置参数】点击链接查看具体使用例子";
        LogUtil.info(JobCommon2.class, msg);
        insertLog(tli, "info", msg);
        Timestamp cur_time = tli.getCur_time();
        String date_nodash = DateUtil.formatNodash(cur_time);
        String date_time = DateUtil.formatTime(cur_time);
        String date_dt = DateUtil.format(cur_time);
        Map<String, Object> jinJavaParam = new HashMap<>();
        jinJavaParam.put("zdh_date_nodash", date_nodash);
        jinJavaParam.put("zdh_date_time", date_time);
        jinJavaParam.put("zdh_date", date_dt);
        jinJavaParam.put("zdh_year", DateUtil.year(cur_time));
        jinJavaParam.put("zdh_month", DateUtil.month(cur_time));
        jinJavaParam.put("zdh_day", DateUtil.day(cur_time));
        jinJavaParam.put("zdh_hour", DateUtil.hour(cur_time));
        jinJavaParam.put("zdh_minute", DateUtil.minute(cur_time));
        jinJavaParam.put("zdh_second", DateUtil.second(cur_time));
        jinJavaParam.put("zdh_monthx", DateUtil.monthx(cur_time));
        jinJavaParam.put("zdh_dayx", DateUtil.dayx(cur_time));
        jinJavaParam.put("zdh_hourx", DateUtil.hourx(cur_time));
        jinJavaParam.put("zdh_minutex", DateUtil.minutex(cur_time));
        jinJavaParam.put("zdh_secondx", DateUtil.secondx(cur_time));

        jinJavaParam.put("zdh_time", cur_time.getTime());
        jinJavaParam.put("zdh_task_log_id", tli.getId());

        jinJavaParam.put("zdh_dt", new DateUtil());
        return jinJavaParam;

    }

    public static void DynamicParams(Map<String, Object> map, TaskLogInstance tli, Object taskInfo) {
        try {
            Map<String, Object> jinJavaParam = getJinJavaParam(tli);

            Jinjava jj = new Jinjava();

            jj.getGlobalContext().registerFunction(new ELFunctionDefinition("", "add_day",
                    DateUtil.class, "addDay", String.class, Integer.class));
            jj.getGlobalContext().registerFunction(new ELFunctionDefinition("", "add_hour",
                    DateUtil.class, "addHour", String.class, Integer.class));
            jj.getGlobalContext().registerFunction(new ELFunctionDefinition("", "add_minute",
                    DateUtil.class, "addMinute", String.class, Integer.class));
            jj.getGlobalContext().registerFunction(new ELFunctionDefinition("", "pase",
                    DateUtil.class, "pase", String.class, String.class));

            String msg = "目前支持日期操作: {{add_day('2021-12-01 00:00:00', 1)}} => 2021-12-02 00:00:00 ,{{add_hour('2021-12-01 00:00:00', 1)}} => 2021-12-01 01:00:00,{{add_minute('2021-12-01 00:00:00', 1)}} => 2021-12-01 00:01:00, 更多高级函数, 可参考【系统内置参数】点击链接查看具体使用例子";
            LogUtil.info(JobCommon2.class, msg);
            insertLog(tli, "info", msg);

            map.forEach((k, v) -> {
                LogUtil.info(JobCommon2.class, "key:" + k + ",value:" + v);
                jinJavaParam.put(k, v);
            });
//            EtlTaskInfo etlTaskInfo, SqlTaskInfo sqlTaskInfo, JarTaskInfo jarTaskInfo, SshTaskInfo sshTaskInfo,
//                    EtlApplyTaskInfo etlApplyTaskInfo,EtlTaskFlinkInfo etlTaskFlinkInfo,EtlTaskJdbcInfo etlTaskJdbcInfo,EtlTaskDataxInfo etlTaskDataxInfo
            if (taskInfo instanceof EtlTaskInfo) {
                EtlTaskInfo etlTaskInfo = (EtlTaskInfo) taskInfo;
                if (etlTaskInfo != null) {
                    final String filter = jj.render(etlTaskInfo.getData_sources_filter_input(), jinJavaParam);
                    final String clear = jj.render(etlTaskInfo.getData_sources_clear_output(), jinJavaParam);
                    final String file_name = jj.render(etlTaskInfo.getData_sources_file_name_input(), jinJavaParam);
                    final String file_name2 = jj.render(etlTaskInfo.getData_sources_file_name_output(), jinJavaParam);
                    final String table_name = jj.render(etlTaskInfo.getData_sources_table_name_input(), jinJavaParam);
                    final String table_name2 = jj.render(etlTaskInfo.getData_sources_table_name_output(), jinJavaParam);
                    if(!StringUtils.isEmpty(etlTaskInfo.getData_sources_params_input())){
                        final String params_input = jj.render(etlTaskInfo.getData_sources_params_input(), jinJavaParam);
                        etlTaskInfo.setData_sources_params_input(params_input);
                    }
                    if(StringUtils.isEmpty(etlTaskInfo.getData_sources_params_output())){
                        final String params_output = jj.render(etlTaskInfo.getData_sources_params_output(), jinJavaParam);
                        etlTaskInfo.setData_sources_params_output(params_output);
                    }

                    etlTaskInfo.setData_sources_filter_input(filter);
                    etlTaskInfo.setData_sources_clear_output(clear);
                    etlTaskInfo.setData_sources_file_name_input(file_name);
                    etlTaskInfo.setData_sources_file_name_output(file_name2);
                    etlTaskInfo.setData_sources_table_name_input(table_name);
                    etlTaskInfo.setData_sources_table_name_output(table_name2);


                }
            }

            if (taskInfo instanceof SqlTaskInfo) {
                SqlTaskInfo sqlTaskInfo = (SqlTaskInfo) taskInfo;
                if (sqlTaskInfo != null) {
                    final String etl_sql = jj.render(sqlTaskInfo.getEtl_sql(), jinJavaParam);
                    final String clear = jj.render(sqlTaskInfo.getData_sources_clear_output(), jinJavaParam);
                    final String file_name = jj.render(sqlTaskInfo.getData_sources_file_name_output(), jinJavaParam);
                    final String table_name = jj.render(sqlTaskInfo.getData_sources_table_name_output(), jinJavaParam);

                    if(!StringUtils.isEmpty(sqlTaskInfo.getData_sources_params_input())){
                        final String params_input = jj.render(sqlTaskInfo.getData_sources_params_input(), jinJavaParam);
                        sqlTaskInfo.setData_sources_params_input(params_input);
                    }
                    if(!StringUtils.isEmpty(sqlTaskInfo.getData_sources_params_output())){
                        final String params_output = jj.render(sqlTaskInfo.getData_sources_params_output(), jinJavaParam);
                        sqlTaskInfo.setData_sources_params_output(params_output);
                    }
                    sqlTaskInfo.setEtl_sql(etl_sql);
                    sqlTaskInfo.setData_sources_clear_output(clear);
                    sqlTaskInfo.setData_sources_file_name_output(file_name);
                    sqlTaskInfo.setData_sources_table_name_output(table_name);



                }
            }
            if (taskInfo instanceof JarTaskInfo) {
                JarTaskInfo jarTaskInfo = (JarTaskInfo) taskInfo;
                if (jarTaskInfo != null) {
                    final String spark_submit_params = jj.render(jarTaskInfo.getSpark_submit_params(), jinJavaParam);
                    jarTaskInfo.setSpark_submit_params(spark_submit_params);
                }
            }

            if (taskInfo instanceof SshTaskInfo) {
                SshTaskInfo sshTaskInfo = (SshTaskInfo) taskInfo;
                if (sshTaskInfo != null) {
                    final String script_path = jj.render(sshTaskInfo.getSsh_script_path(), jinJavaParam);
                    sshTaskInfo.setSsh_script_path(script_path);

                    jinJavaParam.put("zdh_online_file", sshTaskInfo.getSsh_script_path() + "/" + tli.getId() + "_online");
                    final String ssh_cmd = jj.render(sshTaskInfo.getSsh_cmd(), jinJavaParam);
                    sshTaskInfo.setSsh_cmd(ssh_cmd);

                    final String script_context = jj.render(sshTaskInfo.getSsh_script_context(), jinJavaParam);
                    sshTaskInfo.setSsh_script_context(script_context);

                    if(!StringUtils.isEmpty(sshTaskInfo.getSsh_params_input())){
                        final String params_input = jj.render(sshTaskInfo.getSsh_params_input(), jinJavaParam);
                        sshTaskInfo.setSsh_params_input(params_input);
                    }

                }
            }
            if (taskInfo instanceof EtlApplyTaskInfo) {
                EtlApplyTaskInfo etlApplyTaskInfo = (EtlApplyTaskInfo) taskInfo;
                if (etlApplyTaskInfo != null) {
                    final String filter = jj.render(etlApplyTaskInfo.getData_sources_filter_input(), jinJavaParam);
                    final String clear = jj.render(etlApplyTaskInfo.getData_sources_clear_output(), jinJavaParam);
                    final String file_name = jj.render(etlApplyTaskInfo.getData_sources_file_name_input(), jinJavaParam);
                    final String file_name2 = jj.render(etlApplyTaskInfo.getData_sources_file_name_output(), jinJavaParam);
                    final String table_name = jj.render(etlApplyTaskInfo.getData_sources_table_name_input(), jinJavaParam);
                    final String table_name2 = jj.render(etlApplyTaskInfo.getData_sources_table_name_output(), jinJavaParam);
                    etlApplyTaskInfo.setData_sources_filter_input(filter);
                    etlApplyTaskInfo.setData_sources_clear_output(clear);
                    etlApplyTaskInfo.setData_sources_file_name_input(file_name);
                    etlApplyTaskInfo.setData_sources_file_name_output(file_name2);
                    etlApplyTaskInfo.setData_sources_table_name_input(table_name);
                    etlApplyTaskInfo.setData_sources_table_name_output(table_name2);

                    if(!StringUtils.isEmpty(etlApplyTaskInfo.getData_sources_params_input())){
                        final String params_input = jj.render(etlApplyTaskInfo.getData_sources_params_input(), jinJavaParam);
                        etlApplyTaskInfo.setData_sources_params_input(params_input);
                    }
                    if(!StringUtils.isEmpty(etlApplyTaskInfo.getData_sources_params_output())){
                        final String params_output = jj.render(etlApplyTaskInfo.getData_sources_params_output(), jinJavaParam);
                        etlApplyTaskInfo.setData_sources_params_output(params_output);
                    }

                }

            }
            if (taskInfo instanceof EtlTaskFlinkInfo) {
                EtlTaskFlinkInfo etlTaskFlinkInfo = (EtlTaskFlinkInfo) taskInfo;
                if (etlTaskFlinkInfo != null) {
                    final String etl_sql = jj.render(etlTaskFlinkInfo.getEtl_sql(), jinJavaParam);
                    final String command = jj.render(etlTaskFlinkInfo.getCommand(), jinJavaParam);
                    etlTaskFlinkInfo.setEtl_sql(etl_sql);
                    etlTaskFlinkInfo.setCommand(command);

                    if(!StringUtils.isEmpty(etlTaskFlinkInfo.getData_sources_params_input())){
                        final String params_input = jj.render(etlTaskFlinkInfo.getData_sources_params_input(), jinJavaParam);
                        etlTaskFlinkInfo.setData_sources_params_input(params_input);
                    }

                }
            }
            if (taskInfo instanceof EtlTaskJdbcInfo) {
                EtlTaskJdbcInfo etlTaskJdbcInfo = (EtlTaskJdbcInfo) taskInfo;
                if (etlTaskJdbcInfo != null) {
                    final String etl_sql = jj.render(etlTaskJdbcInfo.getEtl_sql(), jinJavaParam);
                    final String clear = jj.render(etlTaskJdbcInfo.getData_sources_clear_output(), jinJavaParam);
                    final String file_name = jj.render(etlTaskJdbcInfo.getData_sources_file_name_output(), jinJavaParam);
                    final String table_name = jj.render(etlTaskJdbcInfo.getData_sources_table_name_output(), jinJavaParam);

                    etlTaskJdbcInfo.setEtl_sql(etl_sql);
                    etlTaskJdbcInfo.setData_sources_clear_output(clear);
                    etlTaskJdbcInfo.setData_sources_file_name_output(file_name);
                    etlTaskJdbcInfo.setData_sources_table_name_output(table_name);

                    if(!StringUtils.isEmpty(etlTaskJdbcInfo.getData_sources_params_output())){
                        final String params_output = jj.render(etlTaskJdbcInfo.getData_sources_params_output(), jinJavaParam);
                        etlTaskJdbcInfo.setData_sources_params_output(params_output);
                    }

                }
            }

            if (taskInfo instanceof EtlTaskDataxInfo) {
                EtlTaskDataxInfo etlTaskDataxInfo = (EtlTaskDataxInfo) taskInfo;
                if (etlTaskDataxInfo != null) {
                    final String datax_json = jj.render(etlTaskDataxInfo.getDatax_json(), jinJavaParam);
                    etlTaskDataxInfo.setDatax_json(datax_json);
                }
            }

            if (taskInfo instanceof QualityTaskInfo) {
                QualityTaskInfo qualityTaskInfo = (QualityTaskInfo) taskInfo;
                if (qualityTaskInfo != null) {
                    final String filter = jj.render(qualityTaskInfo.getData_sources_filter_input(), jinJavaParam);
                    final String file_name = jj.render(qualityTaskInfo.getData_sources_file_name_input(), jinJavaParam);
                    final String table_name = jj.render(qualityTaskInfo.getData_sources_table_name_input(), jinJavaParam);
                    final String quality_rule_config = jj.render(qualityTaskInfo.getQuality_rule_config(), jinJavaParam);
                    qualityTaskInfo.setData_sources_filter_input(filter);
                    qualityTaskInfo.setData_sources_file_name_input(file_name);
                    qualityTaskInfo.setData_sources_table_name_input(table_name);
                    qualityTaskInfo.setQuality_rule_config(quality_rule_config);

                    if(!StringUtils.isEmpty(qualityTaskInfo.getData_sources_params_input())){
                        final String params_input = jj.render(qualityTaskInfo.getData_sources_params_input(), jinJavaParam);
                        qualityTaskInfo.setData_sources_params_input(params_input);
                    }
                }
            }

            if (taskInfo instanceof EtlTaskUnstructureInfo) {
                EtlTaskUnstructureInfo etlTaskUnstructureInfo = (EtlTaskUnstructureInfo) taskInfo;
                if (etlTaskUnstructureInfo != null) {
                    final String input_path = jj.render(etlTaskUnstructureInfo.getInput_path(), jinJavaParam);
                    final String output_path = jj.render(etlTaskUnstructureInfo.getOutput_path(), jinJavaParam);
                    etlTaskUnstructureInfo.setInput_path(input_path);
                    etlTaskUnstructureInfo.setOutput_path(output_path);

                    if(!StringUtils.isEmpty(etlTaskUnstructureInfo.getUnstructure_params_output())){
                        final String params_output = jj.render(etlTaskUnstructureInfo.getUnstructure_params_output(), jinJavaParam);
                        etlTaskUnstructureInfo.setUnstructure_params_output(params_output);
                    }
                }
            }

            if (taskInfo instanceof EtlTaskDataxAutoInfo) {
                EtlTaskDataxAutoInfo etlTaskDataxAutoInfo = (EtlTaskDataxAutoInfo) taskInfo;
                if (etlTaskDataxAutoInfo != null) {

                    final String filter = jj.render(etlTaskDataxAutoInfo.getData_sources_filter_input(), jinJavaParam);
                    final String clear = jj.render(etlTaskDataxAutoInfo.getData_sources_clear_output(), jinJavaParam);

                    final String table_name = jj.render(etlTaskDataxAutoInfo.getData_sources_table_name_input(), jinJavaParam);
                    final String table_name2 = jj.render(etlTaskDataxAutoInfo.getData_sources_table_name_output(), jinJavaParam);
                    etlTaskDataxAutoInfo.setData_sources_filter_input(filter);
                    etlTaskDataxAutoInfo.setData_sources_clear_output(clear);
                    etlTaskDataxAutoInfo.setData_sources_table_name_input(table_name);
                    etlTaskDataxAutoInfo.setData_sources_table_name_output(table_name2);

                    if(!StringUtils.isEmpty(etlTaskDataxAutoInfo.getData_sources_params_input())){
                        final String params_input = jj.render(etlTaskDataxAutoInfo.getData_sources_params_input(), jinJavaParam);
                        etlTaskDataxAutoInfo.setData_sources_params_input(params_input);
                    }
                    if(!StringUtils.isEmpty(etlTaskDataxAutoInfo.getData_sources_params_output())){
                        final String params_output = jj.render(etlTaskDataxAutoInfo.getData_sources_params_output(), jinJavaParam);
                        etlTaskDataxAutoInfo.setData_sources_params_output(params_output);
                    }
                }
            }

            if (taskInfo instanceof EtlTaskKettleInfo) {
                EtlTaskKettleInfo etlTaskKettleInfo = (EtlTaskKettleInfo) taskInfo;
                if (etlTaskKettleInfo != null) {
                    final String kettle_repository_path = jj.render(etlTaskKettleInfo.getKettle_repository_path(), jinJavaParam);
                    etlTaskKettleInfo.setKettle_repository_path(kettle_repository_path);

                    if(!StringUtils.isEmpty(etlTaskKettleInfo.getData_sources_params_input())){
                        final String params_input = jj.render(etlTaskKettleInfo.getData_sources_params_input(), jinJavaParam);
                        etlTaskKettleInfo.setData_sources_params_input(params_input);
                    }
                }
            }

        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            throw e;
        }


    }

    public static String DynamicParams(Map<String, Object> map, TaskLogInstance tli, String old_str) {
        try {
            Map<String, Object> jinJavaParam = getJinJavaParam(tli);

            Jinjava jj = new Jinjava();

            if (map != null) {
                map.forEach((k, v) -> {
                    LogUtil.info(JobCommon2.class, "key:" + k + ",value:" + v);
                    jinJavaParam.put(k, v);
                });
            }

            String new_str = jj.render(old_str, jinJavaParam);

            return new_str;
        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            throw e;
        }


    }

    /**
     * 获取后台url
     *
     * @param zdhHaInfoMapper
     * @return
     */
    public static ZdhHaInfo getZdhUrl(ZdhHaInfoMapper zdhHaInfoMapper, String params) {
        LogUtil.info(JobCommon2.class, "获取后台处理URL");
        String url = "http://127.0.0.1:60001/api/v1/zdh";
        String zdh_instance = null;
        if (!StringUtils.isEmpty(params)) {
            zdh_instance = JsonUtil.toJavaMap(params).getOrDefault("zdh_instance", "").toString();
        }

        List<ZdhHaInfo> zdhHaInfoList = zdhHaInfoMapper.selectByStatus("enabled", zdh_instance);
        String id = "-1";
        if (zdhHaInfoList != null && zdhHaInfoList.size() >= 1) {
            int random = new Random().nextInt(zdhHaInfoList.size());
            return zdhHaInfoList.get(random);
        }
        ZdhHaInfo zdhHaInfo = new ZdhHaInfo();
        zdhHaInfo.setId(id);
        zdhHaInfo.setZdh_url(url);
        zdhHaInfo.setZdh_instance("default_server");
        return zdhHaInfo;
    }

    public static void insertLog(String job_id, String task_logs_id, String level, String msg) {

        ZdhLogs zdhLogs = new ZdhLogs();
        zdhLogs.setJob_id(job_id);
        Timestamp lon_time = new Timestamp(System.currentTimeMillis());
        zdhLogs.setTask_logs_id(task_logs_id);
        zdhLogs.setLog_time(lon_time);
        zdhLogs.setMsg(msg);
        zdhLogs.setLevel(level.toUpperCase());
        //linkedBlockingDeque.add(zdhLogs);

        Object logType=ConfigUtil.getParamUtil().getValue(ConfigUtil.getProductCode(), Const.ZDH_LOG_TYPE);

        if(logType == null || logType.toString().equalsIgnoreCase(Const.LOG_MYSQL)){
            ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
            zdhLogsService.insert(zdhLogs);
        }else if(logType.toString().equalsIgnoreCase(Const.LOG_MONGODB)){
            MongoTemplate mongoTemplate = (MongoTemplate) SpringContext.getBean("mongoTemplate");
            mongoTemplate.insert(zdhLogs);
        }

    }

    /**
     * 插入日志
     *
     * @param tli
     * @param level
     * @param msg
     */
    public static void insertLog(TaskLogInstance tli, String level, String msg) {

        ZdhLogs zdhLogs = new ZdhLogs();
        zdhLogs.setJob_id(tli.getJob_id());
        Timestamp lon_time = new Timestamp(System.currentTimeMillis());
        zdhLogs.setTask_logs_id(tli.getId());
        zdhLogs.setLog_time(lon_time);
        zdhLogs.setMsg(msg);
        zdhLogs.setLevel(level.toUpperCase());
        //linkedBlockingDeque.add(zdhLogs);
//        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
//        zdhLogsService.insert(zdhLogs);

        Object logType=ConfigUtil.getParamUtil().getValue(ConfigUtil.getProductCode(), Const.ZDH_LOG_TYPE);

        if(logType == null || logType.toString().equalsIgnoreCase(Const.LOG_MYSQL)){
            ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
            zdhLogsService.insert(zdhLogs);
        }else if(logType.toString().equalsIgnoreCase(Const.LOG_MONGODB)){
            MongoTemplate mongoTemplate = (MongoTemplate) SpringContext.getBean("mongoTemplate");
            mongoTemplate.insert(zdhLogs);
        }
    }

    public static void insertLog(TaskGroupLogInstance tli, String level, String msg) {

        ZdhLogs zdhLogs = new ZdhLogs();
        zdhLogs.setJob_id(tli.getJob_id());
        Timestamp lon_time = new Timestamp(System.currentTimeMillis());
        zdhLogs.setTask_logs_id(tli.getId());
        zdhLogs.setLog_time(lon_time);
        zdhLogs.setMsg(msg);
        zdhLogs.setLevel(level.toUpperCase());
        //linkedBlockingDeque.add(zdhLogs);
//        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
//        zdhLogsService.insert(zdhLogs);

        RedisUtil redisUtil=(RedisUtil) SpringContext.getBean("redisUtil");
        Object logType=ConfigUtil.getParamUtil().getValue(ConfigUtil.getProductCode(), Const.ZDH_LOG_TYPE);

        if(logType == null || logType.toString().equalsIgnoreCase(Const.LOG_MYSQL)){
            ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
            zdhLogsService.insert(zdhLogs);
        }else if(logType.toString().equalsIgnoreCase(Const.LOG_MONGODB)){
            MongoTemplate mongoTemplate = (MongoTemplate) SpringContext.getBean("mongoTemplate");
            mongoTemplate.insert(zdhLogs);
        }
    }


    /**
     * @param task_logs_id
     * @param model_log
     * @param exe_status
     * @param tli
     * @return
     */
    public static Boolean sendZdh(String task_logs_id, String model_log, Boolean exe_status, TaskLogInstance tli) {
        LogUtil.info(JobCommon2.class, "开始发送信息到zdh处理引擎");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        EtlTaskService etlTaskService = (EtlTaskService) SpringContext.getBean("etlTaskServiceImpl");
        DataSourcesMapper dataSourcesMapper = (DataSourcesMapper) SpringContext.getBean("dataSourcesMapper");
        ZdhHaInfoMapper zdhHaInfoMapper = (ZdhHaInfoMapper) SpringContext.getBean("zdhHaInfoMapper");
        ZdhNginxMapper zdhNginxMapper = (ZdhNginxMapper) SpringContext.getBean("zdhNginxMapper");
        EtlMoreTaskMapper etlMoreTaskMapper = (EtlMoreTaskMapper) SpringContext.getBean("etlMoreTaskMapper");
        SqlTaskMapper sqlTaskMapper = (SqlTaskMapper) SpringContext.getBean("sqlTaskMapper");
        EtlDroolsTaskMapper etlDroolsTaskMapper = (EtlDroolsTaskMapper) SpringContext.getBean("etlDroolsTaskMapper");
        SshTaskMapper sshTaskMapper = (SshTaskMapper) SpringContext.getBean("sshTaskMapper");
        EtlTaskFlinkMapper etlTaskFlinkMapper = (EtlTaskFlinkMapper) SpringContext.getBean("etlTaskFlinkMapper");
        EtlTaskJdbcMapper etlTaskJdbcMapper = (EtlTaskJdbcMapper) SpringContext.getBean("etlTaskJdbcMapper");
        EtlTaskDataxMapper etlTaskDataxMapper = (EtlTaskDataxMapper) SpringContext.getBean("etlTaskDataxMapper");
        QualityTaskMapper qualityTaskMapper = (QualityTaskMapper) SpringContext.getBean("qualityTaskMapper");
        QualityRuleMapper qualityRuleMapper = (QualityRuleMapper) SpringContext.getBean("qualityRuleMapper");
        EtlTaskUnstructureMapper etlTaskUnstructureMapper = (EtlTaskUnstructureMapper) SpringContext.getBean("etlTaskUnstructureMapper");
        EtlTaskDataxAutoMapper etlTaskDataxAutoMapper = (EtlTaskDataxAutoMapper) SpringContext.getBean("etlTaskDataxAutoMapper");
        EtlTaskKettleMapper etlTaskKettleMapper = (EtlTaskKettleMapper) SpringContext.getBean("etlTaskKettleMapper");

        Environment environment= (Environment) SpringContext.getBean("environment");

        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        TaskGroupLogInstanceMapper tglim = (TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");

        TaskGroupLogInstance tgli = tglim.selectByPrimaryKey(tli.getGroup_id());

        String product_code = tgli.getProduct_code();
        String dim_group = tgli.getDim_group();

        String params = tli.getParams().trim();
        insertLog(tli, "INFO", "获取服务端url,指定参数:" + params);
        ZdhHaInfo zdhHaInfo = getZdhUrl(zdhHaInfoMapper, params);
        String url = zdhHaInfo.getZdh_url();
        String instance = zdhHaInfo.getZdh_instance();

        if (!params.equals("")) {
            LogUtil.info(JobCommon2.class, model_log + " JOB ,参数不为空判断是否有url 参数");
            String value = JsonUtil.toJavaMap(params).getOrDefault("url", "").toString();
            if (value != null && !value.equals("")) {
                url = value;
            }
        }

        addEtlDate2Params(tli, tli.getCur_time());

        LogUtil.info(JobCommon2.class, model_log + " JOB ,获取当前的[url]:" + url);
        JobCommon2.insertLog(tli, "INFO", model_log + " JOB ,获取当前的[url]:" + url);
        if (url.contains("127.0.0.1")) {
            JobCommon2.insertLog(tli, "INFO", model_log + " JOB ,当前url经检测,可能使用默认url,默认url必须保证web和server在同一服务器中");
        }
        ZdhBaseInfo zdhBaseInfo = new ZdhBaseInfo();

        ZdhMoreInfo zdhMoreInfo = new ZdhMoreInfo();
        ZdhInfo zdhInfo = new ZdhInfo();
        ZdhSqlInfo zdhSqlInfo = new ZdhSqlInfo();
        ZdhJarInfo zdhJarInfo = new ZdhJarInfo();
        ZdhDroolsInfo zdhDroolsInfo = new ZdhDroolsInfo();
        ZdhSshInfo zdhSshInfo = new ZdhSshInfo();
        ZdhApplyInfo zdhApplyInfo = new ZdhApplyInfo();
        ZdhFlinkSqlInfo zdhFlinkSqlInfo = new ZdhFlinkSqlInfo();
        ZdhJdbcInfo zdhJdbcInfo = new ZdhJdbcInfo();
        ZdhDataxInfo zdhDataxInfo = new ZdhDataxInfo();
        ZdhQualityInfo zdhQualityInfo = new ZdhQualityInfo();
        ZdhUnstructureInfo zdhUnstructureInfo = new ZdhUnstructureInfo();
        ZdhDataxAutoInfo zdhDataxAutoInfo = new ZdhDataxAutoInfo();
        ZdhKettleAutoInfo zdhKettleAutoInfo = new ZdhKettleAutoInfo();
        try {
            if (tli.getMore_task().equals(MoreTask.MORE_ETL.getValue())) {
                LogUtil.info(JobCommon2.class, "组装多源ETL任务信息");
                zdhMoreInfo = create_more_task_zdhInfo(tli, quartzJobMapper, etlTaskService, dataSourcesMapper, zdhNginxMapper, etlMoreTaskMapper);
            } else if (tli.getMore_task().equals(MoreTask.ETL.getValue())) {
                LogUtil.info(JobCommon2.class, "组装单源ETL任务信息");
                zdhInfo = create_zdhInfo(tli, quartzJobMapper, etlTaskService, dataSourcesMapper, zdhNginxMapper, etlMoreTaskMapper);
            } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.SQL.getValue())) {
                LogUtil.info(JobCommon2.class, "组装SQL任务信息");
                zdhSqlInfo = create_zdhSqlInfo(tli, quartzJobMapper, sqlTaskMapper, dataSourcesMapper, zdhNginxMapper);
            } else if (tli.getMore_task().equalsIgnoreCase("外部JAR")) {
                LogUtil.info(JobCommon2.class, "组装外部JAR任务信息");
                LogUtil.info(JobCommon2.class, "请使用SSH任务代替Jar任务");
                //zdhJarInfo = create_zhdJarInfo(tli, quartzJobMapper, jarTaskMapper, zdhNginxMapper);
            } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.Drools.getValue())) {
                LogUtil.info(JobCommon2.class, "组装Drools任务信息");
                zdhDroolsInfo = create_zdhDroolsInfo(tli, quartzJobMapper, etlTaskService, dataSourcesMapper, zdhNginxMapper, etlDroolsTaskMapper, etlMoreTaskMapper, sqlTaskMapper);
            } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.SSH.getValue())) {
                LogUtil.info(JobCommon2.class, "组装SSH任务信息");
                zdhSshInfo = create_zhdSshInfo(tli, quartzJobMapper, sshTaskMapper, zdhNginxMapper);
            } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.APPLY.getValue())) {
                LogUtil.info(JobCommon2.class, "组装申请源任务信息");
                zdhApplyInfo = create_zdhApplyInfo(tli, quartzJobMapper, etlTaskService, dataSourcesMapper, zdhNginxMapper, etlMoreTaskMapper);
            } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.FLINK.getValue())) {
                LogUtil.info(JobCommon2.class, "组装FLINK任务信息");
                zdhFlinkSqlInfo = create_zdhFlinkInfo(tli, quartzJobMapper, etlTaskFlinkMapper, dataSourcesMapper, zdhNginxMapper);
            } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.JDBC.getValue())) {
                LogUtil.info(JobCommon2.class, "组装JDBC任务信息");
                zdhJdbcInfo = create_zdhJdbcInfo(tli, quartzJobMapper, etlTaskJdbcMapper, dataSourcesMapper, zdhNginxMapper);
            } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.DATAX.getValue())) {
                LogUtil.info(JobCommon2.class, "组装DATAX任务信息");
                zdhDataxInfo = create_zdhDataxInfo(tli, quartzJobMapper, etlTaskDataxMapper, dataSourcesMapper, zdhNginxMapper);
            } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.QUALITY.getValue())) {
                LogUtil.info(JobCommon2.class, "组装QUALITY任务信息");
                zdhQualityInfo = create_zdhQualityInfo(tli, quartzJobMapper, qualityTaskMapper, dataSourcesMapper, zdhNginxMapper, qualityRuleMapper);
            } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.UNSTRUCTURE.getValue())) {
                LogUtil.info(JobCommon2.class, "组装UNSTRUCTURE任务信息");
                zdhUnstructureInfo = create_zdhUnstructureInfo(tli, quartzJobMapper, etlTaskUnstructureMapper, dataSourcesMapper);
            } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.DATAX_WEB.getValue())) {
                LogUtil.info(JobCommon2.class, "组装DATAX_WEB任务信息");
                zdhDataxAutoInfo = create_zdhDataxAutoInfo(tli, quartzJobMapper, etlTaskDataxAutoMapper, dataSourcesMapper, environment);
            }else if (tli.getMore_task().equalsIgnoreCase(MoreTask.KETTLE.getValue())) {
                LogUtil.info(JobCommon2.class, "组装KETTLE任务信息");
                zdhKettleAutoInfo = create_zdhKettleAutoInfo(tli, quartzJobMapper, etlTaskKettleMapper, dataSourcesMapper, environment);
            }

            if (exe_status == true) {
                LogUtil.info(JobCommon2.class, model_log + " JOB ,开始发送ETL处理请求");
                zdhInfo.setTask_logs_id(task_logs_id);
                zdhMoreInfo.setTask_logs_id(task_logs_id);
                zdhSqlInfo.setTask_logs_id(task_logs_id);
                zdhJarInfo.setTask_logs_id(task_logs_id);
                zdhDroolsInfo.setTask_logs_id(task_logs_id);
                zdhSshInfo.setTask_logs_id(task_logs_id);
                zdhApplyInfo.setTask_logs_id(task_logs_id);
                zdhFlinkSqlInfo.setTask_logs_id(task_logs_id);
                zdhJdbcInfo.setTask_logs_id(task_logs_id);
                zdhDataxInfo.setTask_logs_id(task_logs_id);
                zdhQualityInfo.setTask_logs_id(task_logs_id);
                zdhDataxAutoInfo.setTask_logs_id(task_logs_id);

                insertLog(tli, "DEBUG", "[调度平台]:" + model_log + " JOB ,开始发送ETL处理请求");
                //tli.setEtl_date(date);
                tli.setProcess(ProcessEnum.CREATE_ETL_TASKINFO);
                tli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                updateTaskLog(tli, tlim);
                String executor = zdhHaInfo.getId();
                String url_tmp = "";
                String etl_info = "";
                //校验是否禁用任务类型
                if(ConfigUtil.isInValue(ConfigUtil.ZDH_DISENABLE_MORE_TASK, tli.getMore_task()) || ConfigUtil.isInValue(ConfigUtil.ZDH_DISENABLE_MORE_TASK, tli.getMore_task())){
                    LogUtil.error(JobCommon2.class, "当前【" + tli.getMore_task() + "】类任务,被系统禁用,具体可咨询管理员");
                    insertLog(tli, "ERROR", "当前【"+tli.getMore_task()+"】类任务,被系统禁用,具体可咨询管理员");
                    throw new Exception("当前【"+tli.getMore_task()+"】类任务,被系统禁用,具体可咨询管理员");
                }

                if (tli.getMore_task().equalsIgnoreCase(MoreTask.MORE_ETL.getValue())) {
                    url_tmp = url + "/more";
                    etl_info = JsonUtil.formatJsonString(zdhMoreInfo);
                } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.ETL.getValue())) {
                    url_tmp = url;
                    etl_info = JsonUtil.formatJsonString(zdhInfo);
                } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.SQL.getValue())) {
                    url_tmp = url + "/sql";
                    etl_info = JsonUtil.formatJsonString(zdhSqlInfo);
                } else if (tli.getMore_task().equalsIgnoreCase("外部JAR")) {
                    LogUtil.info(JobCommon2.class, "[调度平台]:外部JAR,参数:" + JsonUtil.formatJsonString(zdhJarInfo));
                    insertLog(tli, "DEBUG", "[调度平台]:外部JAR,参数:" + JsonUtil.formatJsonString(zdhJarInfo));
                    // submit_jar(tli, zdhJarInfo);
                } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.Drools.getValue())) {
                    url_tmp = url + "/drools";
                    etl_info = JsonUtil.formatJsonString(zdhDroolsInfo);
                } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.SSH.getValue())) {
                    etl_info = JsonUtil.formatJsonString(zdhSshInfo);
                    LogUtil.info(JobCommon2.class, "[调度平台]:SSH,参数:" + JsonUtil.formatJsonString(zdhSshInfo));
                    insertLog(tli, "DEBUG", "[调度平台]:SSH,参数:" + JsonUtil.formatJsonString(zdhSshInfo));

                    tli.setEtl_info(etl_info);
                    tli.setStatus(JobStatus.ETL.getValue());
                    updateTaskLog(tli, tlim);
                    boolean rs = ssh_exec(tli, zdhSshInfo);
                    if (rs) {
                        tli.setLast_status("finish");
                        //此处是按照同步方式设计的,如果执行的命令是异步命令那些需要用户自己维护这个状态
                        tli.setStatus(JobStatus.FINISH.getValue());
                        tli.setProcess(ProcessEnum.FINISH);
                        tli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                        updateTaskLog(tli, tlim);
                    }
                    return rs;
                } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.APPLY.getValue())) {
                    url_tmp = url + "/apply";
                    etl_info = JsonUtil.formatJsonString(zdhApplyInfo);
                } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.FLINK.getValue())) {
                    etl_info = JsonUtil.formatJsonString(zdhFlinkSqlInfo);
                    LogUtil.info(JobCommon2.class, "[调度平台]:FLINK,参数:" + JsonUtil.formatJsonString(zdhFlinkSqlInfo));
                    insertLog(tli, "DEBUG", "[调度平台]:FLINK,参数:" + JsonUtil.formatJsonString(zdhFlinkSqlInfo));
                    tli.setEtl_info(etl_info);
                    tli.setStatus(JobStatus.ETL.getValue());
                    updateTaskLog(tli, tlim);
                    ZdhSshInfo zdhSshInfo_tmp = new ZdhSshInfo();
                    zdhSshInfo_tmp.setTask_logs_id(tli.getId());
                    zdhSshInfo_tmp.setTli(tli);
                    SshTaskInfo sshTaskInfo = new SshTaskInfo();
                    String tmp = StringEscapeUtils.escapeJava(etl_info);
                    LogUtil.info(JobCommon2.class, tmp);
                    //sshTaskInfo.setSsh_cmd("/home/zyc/flink/bin/flink run -c com.zyc.SystemInit -p 1 /home/zyc/zdh_flink.jar "+tli.getId() +" --zdh_config='/home/zyc/conf'");
                    //sshTaskInfo.setSsh_cmd("/FirefoxDownload/flink-1.12.4-bin-scala_2.11/flink-1.12.4/bin/flink.bat run -c com.zyc.SystemInit -p 1 /home/zyc/zdh_flink.jar "+tli.getId());
                    sshTaskInfo.setHost(zdhFlinkSqlInfo.getEtlTaskFlinkInfo().getHost());
                    sshTaskInfo.setPassword(zdhFlinkSqlInfo.getEtlTaskFlinkInfo().getPassword());
                    sshTaskInfo.setUser_name(zdhFlinkSqlInfo.getEtlTaskFlinkInfo().getUser_name());
                    sshTaskInfo.setPort(zdhFlinkSqlInfo.getEtlTaskFlinkInfo().getPort());
                    sshTaskInfo.setSsh_cmd(zdhFlinkSqlInfo.getEtlTaskFlinkInfo().getCommand());
                    zdhSshInfo_tmp.setSshTaskInfo(sshTaskInfo);
                    LogUtil.info(JobCommon2.class, "FLINK 任务,需下载zdh_flink.jar");
                    insertLog(tli, "INFO", "FLINK 任务,需下载zdh_flink.jar");

                    if (zdhFlinkSqlInfo.getEtlTaskFlinkInfo().getServer_type().equalsIgnoreCase("windows")) {
                        LogUtil.info(JobCommon2.class, "FLINK 任务,判断当前集群为windows, 将使用本地shell命令方式执行");
                        insertLog(tli, "INFO", "FLINK 任务,判断当前集群为windows, 将使用本地shell命令方式执行");
                        String cmd = zdhFlinkSqlInfo.getEtlTaskFlinkInfo().getCommand();
                        Map result = CommandUtils.exeCommand2(tli, "cmd.exe", "/c", cmd, "GBK");
                        if (result.get("result").toString().equalsIgnoreCase("success")) {
//                            tli.setStatus(JobStatus.FINISH.getValue());
//                            tli.setProcess("100");
//                            tli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
//                            updateTaskLog(tli, tlim);
                            return true;
                        }
                        return false;
                    } else {
                        boolean rs = ssh_exec(tli, zdhSshInfo_tmp);
                        if (rs) {
                            //此处是按照同步方式设计的,如果执行的命令是异步命令那些需要用户自己维护这个状态
                            tlim.updateStatusById4(JobStatus.ETL.getValue(), "65", tli.getId());
                        }
                        return rs;
                    }
                } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.JDBC.getValue())) {
                    url_tmp = url + "/jdbc";
                    etl_info = JsonUtil.formatJsonString(zdhJdbcInfo);//todo
                    insertLog(tli, "INFO", etl_info);
                    if (zdhJdbcInfo.getEtlTaskJdbcInfo().getEngine_type().equalsIgnoreCase("loocal")) {
                        String msg = "当前JDBC引擎为本地模式,将在本地执行,不会发送zdh_server模块执行";
                        LogUtil.info(JobCommon2.class, msg);
                        JobCommon2.insertLog(tli, "INFO", msg);
                        String driver = zdhJdbcInfo.getDsi_Input().getDriver();
                        String jdbc_url = zdhJdbcInfo.getDsi_Input().getUrl();
                        String username = zdhJdbcInfo.getDsi_Input().getUser();
                        String password = zdhJdbcInfo.getDsi_Input().getPassword();
                        String[] sqls = zdhJdbcInfo.getEtlTaskJdbcInfo().getEtl_sql().split("\r\n|\n");
                        String[] ret = new DBUtil().CUD(driver, jdbc_url, username, password, sqls);
                        if (ret[0].equalsIgnoreCase("true")) {
                            //tli.setLast_status("finish");
                            //此处是按照同步方式设计的,如果执行的命令是异步命令那些需要用户自己维护这个状态
                            tli.setStatus(JobStatus.FINISH.getValue());
                            tli.setProcess(ProcessEnum.FINISH);
                        }
                        tli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                        updateTaskLog(tli, tlim);
                        return Boolean.valueOf(ret[0]);
                    } else {
                        JobCommon2.insertLog(tli, "INFO", "当前版本不支持JDBC引擎发送到zdh_server,请更改计算引擎为本地");
                        return false;
                    }

                } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.DATAX.getValue())) {
                    url_tmp = url + "/datax";
                    etl_info = JsonUtil.formatJsonString(zdhDataxInfo);//todo
                    LogUtil.info(JobCommon2.class, "[调度平台]:DATAX,参数:" + etl_info);
                    insertLog(tli, "DEBUG", "[调度平台]:DATAX,参数:" + etl_info);
                    tli.setEtl_info(etl_info);
                    tli.setStatus(JobStatus.ETL.getValue());
                    updateTaskLog(tli, tlim);
                    boolean rs = datax_exec(tli, zdhDataxInfo);
                    if (rs) {
                        tli.setLast_status("finish");
                        //此处是按照同步方式设计的,如果执行的命令是异步命令那些需要用户自己维护这个状态
                        tli.setStatus(JobStatus.FINISH.getValue());
                        tli.setProcess(ProcessEnum.FINISH);
                        tli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                        updateTaskLog(tli, tlim);
                    }
                    return rs;

                } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.QUALITY.getValue())) {
                    url_tmp = url + "/quality";
                    etl_info = JsonUtil.formatJsonString(zdhQualityInfo);
                } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.UNSTRUCTURE.getValue())) {
                    etl_info = JsonUtil.formatJsonString(zdhUnstructureInfo);
                    LogUtil.info(JobCommon2.class, "[调度平台]:非结构化数据采集,参数:" + etl_info);
                    insertLog(tli, "DEBUG", "[调度平台]:非结构化数据采集,参数:" + etl_info);
                    //todo 未完成
                    boolean rs = unstructure_exec(tli, zdhUnstructureInfo);
                    if (rs) {
                        tli.setLast_status("finish");
                        //此处是按照同步方式设计的,如果执行的命令是异步命令那些需要用户自己维护这个状态
                        tli.setStatus(JobStatus.FINISH.getValue());
                        tli.setProcess(ProcessEnum.FINISH);
                        tli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                        updateTaskLog(tli, tlim);
                    }
                    return rs;
                } else if (tli.getMore_task().equalsIgnoreCase(MoreTask.DATAX_WEB.getValue())) {
                    etl_info = JsonUtil.formatJsonString(zdhDataxAutoInfo);//todo
                    LogUtil.info(JobCommon2.class, "[调度平台]:DATAX_WEB,参数:" + etl_info);
                    insertLog(tli, "DEBUG", "[调度平台]:DATAX_WEB,参数:" + etl_info);
                    tli.setEtl_info(etl_info);
                    tli.setStatus(JobStatus.ETL.getValue());
                    updateTaskLog(tli, tlim);
                    boolean rs = datax_auto_exec(tli, zdhDataxAutoInfo);
                    if (rs) {
                        tli.setLast_status("finish");
                        //此处是按照同步方式设计的,如果执行的命令是异步命令那些需要用户自己维护这个状态
                        tli.setStatus(JobStatus.FINISH.getValue());
                        tli.setProcess(ProcessEnum.FINISH);
                        tli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                        updateTaskLog(tli, tlim);
                    }
                    return rs;
                }else if (tli.getMore_task().equalsIgnoreCase(MoreTask.KETTLE.getValue())) {
                    etl_info = JsonUtil.formatJsonString(zdhKettleAutoInfo);//todo
                    LogUtil.info(JobCommon2.class, "[调度平台]:KETTLE,参数:" + etl_info);
                    insertLog(tli, "DEBUG", "[调度平台]:KETTLE,参数:" + etl_info);
                    tli.setEtl_info(etl_info);
                    tli.setStatus(JobStatus.ETL.getValue());
                    updateTaskLog(tli, tlim);
                    boolean rs = KettleJob.kettleCommand(tli, zdhKettleAutoInfo);
                    if (rs) {
                        tli.setLast_status("finish");
                        //此处是按照同步方式设计的,如果执行的命令是异步命令那些需要用户自己维护这个状态
                        tli.setStatus(JobStatus.FINISH.getValue());
                        tli.setProcess(ProcessEnum.FINISH);
                        tli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                        updateTaskLog(tli, tlim);
                    }
                    return rs;
                }

                tli.setExecutor(executor);
                tli.setUrl(url_tmp);
                tli.setEtl_info(etl_info);
                tli.setHistory_server(zdhHaInfo.getHistory_server());
                tli.setMaster(zdhHaInfo.getMaster());
                tli.setApplication_id(zdhHaInfo.getApplication_id());
                tli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                //debugInfo(tli);
                updateTaskLog(tli, tlim);


                Boolean is_send_server = true;
                if (zdhJdbcInfo.getEtlTaskJdbcInfo() != null && zdhJdbcInfo.getEtlTaskJdbcInfo().getEngine_type().equalsIgnoreCase("spark")) {
                    //tips: jdbc类任务,当前不支持spark引擎,因此当jdbc任务选择spark引擎时,并不会触发任务执行
                    is_send_server = false;
                }

                //发往server处任务
                if (!tli.getMore_task().equalsIgnoreCase("SSH") && !tli.getMore_task().equalsIgnoreCase("FLINK") && is_send_server) {
                    LogUtil.info(JobCommon2.class, "[调度平台]:" + url_tmp + " ,参数:" + etl_info);

                    //新增参数判断http发送或者是队列发送
                    if(ConfigUtil.isInRedis(product_code, ConfigUtil.ZDH_SPARK_QUEUE_ENABLE) && ConfigUtil.getParamUtil().getValue(product_code, ConfigUtil.ZDH_SPARK_QUEUE_ENABLE, "false").toString().equalsIgnoreCase("true") ){
                        //公共参数配置-发送队列
                        String queue = ConfigUtil.getValue(ConfigUtil.ZDH_SPARK_QUEUE_PRE_KEY)+"_"+instance;
                        insertLog(tli, "DEBUG", "[调度平台]:" + "发送任务到队列:"+queue + " ,:" + etl_info);
                        RQueueClient rQueueClient = RQueueManager.getRQueueClient(queue, RQueueMode.BLOCKQUEUE);
                        rQueueClient.add(etl_info);
                    }
//                    if(ConfigUtil.isInEnv(ConfigUtil.ZDH_SPARK_QUEUE_ENABLE) && ConfigUtil.getValue(ConfigUtil.ZDH_SPARK_QUEUE_ENABLE, "false").equalsIgnoreCase("true") ){
//                        //本地参数配置-发送队列
//                        String queue = ConfigUtil.getValue(ConfigUtil.ZDH_SPARK_QUEUE_PRE_KEY)+"_"+instance;
//                        insertLog(tli, "DEBUG", "[调度平台]:" + "发送任务到队列:"+queue + " ,:" + etl_info);
//                        RQueueClient rQueueClient = RQueueManager.getRQueueClient(queue, RQueueMode.BLOCKQUEUE);
//                        rQueueClient.add(etl_info);
//                    }else
                    else{
                        insertLog(tli, "DEBUG", "[调度平台]:" + url_tmp + " ,参数:" + etl_info);
                        HttpUtil.postJSON(url_tmp, etl_info);
                    }
                    LogUtil.info(JobCommon2.class, model_log + " JOB ,更新调度任务状态为etl");
                    tli.setLast_status("etl");
                    tli.setStatus(JobStatus.ETL.getValue());
                    tli.setProcess(ProcessEnum.INIT_ETL_ENGINE15);
                    //tlim.updateTaskLogsById3(tli);
                    //updateTaskLog(tli, tlim);
                    tlim.updateStatusById3("etl", "15", tli.getId());
                }
            }
        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            String msg = "[调度平台]:" + model_log + " JOB ,处理" + tli.getMore_task() + " 请求,异常请检查zdh_server服务是否正常运行,或者检查网络情况,如果是本地任务,请检查本地任务执行,错误信息如下:" + e.getMessage();
            LogUtil.info(JobCommon2.class, msg);
            insertLog(tli, "ERROR", msg);
            tli.setStatus(JobStatus.ERROR.getValue());
            tli.setProcess(ProcessEnum.INIT_ETL_ENGINE17);
            tli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            updateTaskLog(tli, tlim);
            //更新执行状态为error
            LogUtil.info(JobCommon2.class, model_log + " JOB ,更新调度任务状态为error");
            tli.setLast_status("error");
            LogUtil.error(JobCommon2.class, e.getMessage());
            exe_status = false;
        }

        return exe_status;

    }


    public static boolean ssh_exec(TaskLogInstance tli, ZdhSshInfo zdhSshInfo) throws IOException, JSchException, SftpException {
        try {
            String system = System.getProperty("os.name");
            long t1 = System.currentTimeMillis();
            insertLog(tli, "DEBUG", "[调度平台]:SSH,当前系统为:" + system + ",请耐心等待SSH任务开始执行....");
            String host = zdhSshInfo.getSshTaskInfo().getHost();
            String port = zdhSshInfo.getSshTaskInfo().getPort();
            String username = zdhSshInfo.getSshTaskInfo().getUser_name();
            String password = zdhSshInfo.getSshTaskInfo().getPassword();
            String ssh_cmd = zdhSshInfo.getSshTaskInfo().getSsh_cmd();
            String id = zdhSshInfo.getSshTaskInfo().getId();
            String t_id = zdhSshInfo.getTask_logs_id();
            String script_path = zdhSshInfo.getSshTaskInfo().getSsh_script_path();
            String script_context = zdhSshInfo.getSshTaskInfo().getSsh_script_context();
            List<JarFileInfo> jarFileInfos = zdhSshInfo.getJarFileInfos();
            ZdhNginx zdhNginx = zdhSshInfo.getZdhNginx();
            insertLog(tli, "DEBUG", "设置免密登录,请在密码中填写私钥地址,且以privateKey:开头, privateKey:/home/root/.ssh/id_rsa");
            if (!org.apache.commons.lang3.StringUtils.isEmpty(script_context) || (jarFileInfos != null && !jarFileInfos.isEmpty())) {
                SFTPUtil sftpUtil = new SFTPUtil(username, password, host, Integer.parseInt(port));
                sftpUtil.login();
                if (!script_context.isEmpty()) {
                    insertLog(tli, "DEBUG", "[调度平台]:SSH,发现在线脚本,使用在线脚本ssh 命令 可配合{{zdh_online_file}} 使用 example sh {{zdh_online_file}} 即是执行在线的脚本");
                    InputStream inputStream = new ByteArrayInputStream(script_context.replaceAll("\r\n", "\n").getBytes());
                    sftpUtil.upload(script_path, t_id + "_online", inputStream);
                }

                if (!jarFileInfos.isEmpty()) {
                    for (JarFileInfo jarFileInfo : jarFileInfos) {
                        //下载文件
                        if (zdhNginx.getHost() != null && !zdhNginx.getHost().equals("")) {
                            LogUtil.info(JobCommon2.class, "开始下载文件:SFTP方式" + jarFileInfo.getFile_name());
                            insertLog(tli, "DEBUG", "[调度平台]:SSH,开始下载文件:SFTP方式" + jarFileInfo.getFile_name());
                            //连接sftp 下载
                            SFTPUtil sftp = new SFTPUtil(zdhNginx.getUsername(), zdhNginx.getPassword(),
                                    zdhNginx.getHost(), new Integer(zdhNginx.getPort()));
                            sftp.login();
                            byte[] fileByte = sftp.download(zdhNginx.getNginx_dir() + "/" + zdhNginx.getOwner(), jarFileInfo.getFile_name());
                            sftpUtil.upload(script_path, jarFileInfo.getFile_name(), fileByte);
                            sftp.logout();
                        } else {
                            LogUtil.info(JobCommon2.class, "开始下载文件:本地方式" + jarFileInfo.getFile_name());
                            insertLog(tli, "DEBUG", "[调度平台]:SSH,开始下载文件:本地方式" + jarFileInfo.getFile_name());
                            //本地文件

                            FileInputStream in = null;
                            try {
                                in = new FileInputStream(zdhNginx.getTmp_dir() + "/" + zdhNginx.getOwner() + "/" + jarFileInfo.getFile_name());
                                ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
                                //System.out.println("bytes available:" + in.available());
                                byte[] temp = new byte[1024];
                                int size = 0;
                                while ((size = in.read(temp)) != -1) {
                                    out.write(temp, 0, size);
                                }
                                //byte[] bytes = out.toByteArray();
                                //System.out.println("bytes size got is:" + bytes.length);
                                sftpUtil.upload(script_path, jarFileInfo.getFile_name(), in);

                            } catch (Exception e) {
                                LogUtil.error(JobCommon2.class, e);
                                throw e;
                            } finally {
                                if (in != null) {
                                    in.close();
                                }
                            }

                        }
                    }
                }

                sftpUtil.logout();
            }

            SSHUtil sshUtil = new SSHUtil(username, password, host, Integer.parseInt(port));
            sshUtil.login();
            chm_ssh.put(tli.getId(), sshUtil);
            ssh_cmd = "echo task_id=" + tli.getId() + " && " + ssh_cmd;
            insertLog(tli, "DEBUG", "[调度平台]:SSH,使用在线脚本," + ssh_cmd);
            String[] result = sshUtil.exec(ssh_cmd, tli.getId(), tli.getJob_id());
            String error = result[0];
            String out = result[1];
            if (chm_ssh.get(tli.getId()) != null) {
                chm_ssh.get(tli.getId()).logout();
            }
            chm_ssh.remove(tli.getId());
            long t2 = System.currentTimeMillis();

            insertLog(tli, "DEBUG", "[调度平台]:SSH,SSH任务执行结束,耗时:" + (t2 - t1) / 1000 + "s");

            if (!error.isEmpty()) {
                return false;
            }
            return true;

        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            throw e;
        }

    }

    public static boolean datax_exec(TaskLogInstance tli, ZdhDataxInfo zdhDataxInfo) throws IOException, JSchException, SftpException {
        try {
            String system = System.getProperty("os.name");
            long t1 = System.currentTimeMillis();
            insertLog(tli, "DEBUG", "[调度平台]:DATAX,使用datax 服务,建议在datax 数据源中配置驱动格式: python_home/bin/python datax_home, 通过单个空格分割");
            insertLog(tli, "DEBUG", "[调度平台]:DATAX,当前系统为:" + system + ",请耐心等待DATAX任务开始执行....");
            String host = "";
            String url = "";
            if (!StringUtils.isEmpty(zdhDataxInfo.getDsi_Input().getUrl())) {
                String[] urls = zdhDataxInfo.getDsi_Input().getUrl().split(",");
                url = urls[new Random(urls.length).nextInt()];
                host = url.split(":")[0];
            }

            String port = "22";
            if (url.contains(":")) {
                port = url.split(":")[1];
            }


            String username = zdhDataxInfo.getDsi_Input().getUser();
            String password = zdhDataxInfo.getDsi_Input().getPassword();
            String datax_home = zdhDataxInfo.getDsi_Input().getDriver();
            String python_home = "python";
            if (system.toLowerCase().startsWith("win")) {
                python_home = "python.exe";
            }
            if (datax_home.contains(" ")) {
                python_home = datax_home.split(" ", 2)[0];
                datax_home = datax_home.split(" ", 2)[1];
            }

            String id = zdhDataxInfo.getEtlTaskDataxInfo().getId();
            String t_id = zdhDataxInfo.getTask_logs_id();
            String script_path = datax_home + "/zdh_datax/" + DateUtil.formatNodash(new Date());
            String script_context = zdhDataxInfo.getEtlTaskDataxInfo().getDatax_json();
            String file_name = script_path + "/" + t_id + "_online";
            //校验是否本地datax
            if (StringUtils.isEmpty(host)) {
                //本地直接生成文件
                insertLog(tli, "DEBUG", "[调度平台]:DATAX,本地执行");
                FileUtils.forceMkdirParent(new File(file_name));
                FileUtils.write(new File(file_name), script_context, Charset.defaultCharset().name(), false);
                String newcommand = python_home + " " + datax_home + "/bin/datax.py " + file_name;
                insertLog(tli, "DEBUG", script_context);
                insertLog(tli, "DEBUG", newcommand);
                Map result = new HashMap<String, String>();
                insertLog(tli, "DEBUG", "系统编码:" + Charset.defaultCharset().name());
                if (system.toLowerCase().startsWith("win")) {
                    result = CommandUtils.exeCommand2(tli, "cmd.exe", "/c", newcommand, Charset.defaultCharset().name());
                } else {
                    result = CommandUtils.exeCommand2(tli, "sh", "-c", newcommand, Charset.defaultCharset().name());
                }
                long t2 = System.currentTimeMillis();
                insertLog(tli, "DEBUG", "[调度平台]:DATAX,DATAX任务执行结束,耗时:" + (t2 - t1) / 1000 + "s");
                if (result.get("result").equals("success")) {
                    return true;
                }
                return false;

            } else {
                SFTPUtil sftpUtil = new SFTPUtil(username, password, host, Integer.parseInt(port));
                sftpUtil.login();
                if (!script_context.isEmpty()) {
                    insertLog(tli, "DEBUG", "[调度平台]:DATAX,上传配置信息");
                    InputStream inputStream = new ByteArrayInputStream(script_context.replaceAll("\r\n", "\n").getBytes());
                    sftpUtil.upload(script_path, file_name, inputStream);
                }

                sftpUtil.logout();

                SSHUtil sshUtil = new SSHUtil(username, password, host, Integer.parseInt(port));
                sshUtil.login();
                chm_ssh.put(tli.getId(), sshUtil);
                String ssh_cmd = "echo task_id=" + tli.getId() + " && " + python_home + " " + datax_home + "bin/datax.py " + file_name;
                insertLog(tli, "DEBUG", "[调度平台]:DATAX,远程执行," + ssh_cmd);
                String[] result = sshUtil.exec(ssh_cmd, tli.getId(), tli.getJob_id());
                if (chm_ssh.get(tli.getId()) != null) {
                    chm_ssh.get(tli.getId()).logout();
                }
                chm_ssh.remove(tli.getId());
                long t2 = System.currentTimeMillis();

                insertLog(tli, "DEBUG", "[调度平台]:DATAX,DATAX任务执行结束,耗时:" + (t2 - t1) / 1000 + "s");
                String error = result[0];
                if (!error.isEmpty()) {
                    return false;
                }
                return true;
            }


        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            throw e;
        }

    }

    public static boolean unstructure_exec(TaskLogInstance tli, ZdhUnstructureInfo zdhUnstructureInfo) throws Exception {
        try {
            LogUtil.info(JobCommon2.class, "开始执行非结构化数据采集任务");
            insertLog(tli, "DEBUG", "开始执行非结构化数据采集任务");
            //读取数据,ftp,sftp,hdfs
            Dsi_Info dsiInfo = zdhUnstructureInfo.getDsi_Input();
            String data_source_type = zdhUnstructureInfo.getDsi_Input().getData_source_type();
            LogUtil.info(JobCommon2.class, "非结构化数据输入数据源为" + data_source_type);
            insertLog(tli, "DEBUG", "非结构化数据输入数据源为"+data_source_type);
            byte[] file = new byte[]{};
            if (data_source_type.equalsIgnoreCase("sftp")) {
                LogUtil.info(JobCommon2.class, "设置免密登录,请在密码中填写私钥地址,且以privateKey:开头, privateKey:/home/root/.ssh/id_rsa");
                insertLog(tli, "DEBUG", "设置免密登录,请在密码中填写私钥地址,且以privateKey:开头, privateKey:/home/root/.ssh/id_rsa");
                file = readSftp(dsiInfo, zdhUnstructureInfo.getEtlTaskUnstructureInfo());
            } else if (data_source_type.equalsIgnoreCase("ftp")) {
                file = readFtp(dsiInfo, zdhUnstructureInfo.getEtlTaskUnstructureInfo());
            } else if (data_source_type.equalsIgnoreCase("hdfs")) {
                file = readHdfs(dsiInfo, zdhUnstructureInfo.getEtlTaskUnstructureInfo());
            }
            Dsi_Info dsiOutInfo = zdhUnstructureInfo.getDsi_Output();
            LogUtil.info(JobCommon2.class, "非结构化数据输出数据源为" + dsiOutInfo.getData_source_type());
            insertLog(tli, "DEBUG", "非结构化数据输出数据源为"+dsiOutInfo.getData_source_type());
            if (dsiOutInfo.getData_source_type().equalsIgnoreCase("sftp")) {
                writeSftp(dsiOutInfo, zdhUnstructureInfo.getEtlTaskUnstructureInfo(), file);
            }else if(dsiOutInfo.getData_source_type().equalsIgnoreCase("ftp")){
                writeFtp(dsiOutInfo, zdhUnstructureInfo.getEtlTaskUnstructureInfo(), file);
            }else if(dsiOutInfo.getData_source_type().equalsIgnoreCase("ftp")){
                writeHdfs(dsiOutInfo, zdhUnstructureInfo.getEtlTaskUnstructureInfo(), file);
            }
            List<String> etl_sqls = getUnstructureEtlSql(tli, zdhUnstructureInfo.getEtlTaskUnstructureInfo());
            Dsi_Info dsiJdbcInfo=zdhUnstructureInfo.getDsi_Output_Jdbc();
            LogUtil.info(JobCommon2.class, "非结构化数据元数据逻辑" + StringUtils.join(etl_sqls, ";"));
            insertLog(tli, "DEBUG", "非结构化数据元数据逻辑"+StringUtils.join(etl_sqls,";"));
            //入库
            String[] result = new DBUtil().CUD(dsiJdbcInfo.getDriver(), dsiJdbcInfo.getUrl(), dsiJdbcInfo.getUser(), dsiJdbcInfo.getPassword(),
                    etl_sqls.toArray(new String[]{}));
            if(result[0].equalsIgnoreCase("false")){
                throw new Exception(result[1]);
            }
            return true;

        } catch (Exception e) {
            throw e;
        }

    }

    public static boolean datax_auto_exec(TaskLogInstance tli, ZdhDataxAutoInfo zdhDataxAutoInfo) throws Exception {
        try {
            String system = System.getProperty("os.name");
            long t1 = System.currentTimeMillis();
            insertLog(tli, "DEBUG", "[调度平台]:DATAX,使用datax 服务,建议在datax 数据源中配置驱动格式: python_home/bin/python datax_home, 通过单个空格分割");
            insertLog(tli, "DEBUG", "[调度平台]:DATAX,当前系统为:" + system + ",请耐心等待DATAX任务开始执行....");

            DataxReader reader = generator_reader(zdhDataxAutoInfo);
            DataxWriter writer = generator_writer(zdhDataxAutoInfo);
            Content content=new Content();
            content.setReader(reader);
            content.setWriter(writer);

            Map<String, Object> jsonObject=JsonUtil.toJavaMap(zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getData_sources_params_input());
            DataxConfig dataxConfig=DataxConfig.build(Lists.newArrayList(content), jsonObject,jsonObject);

            LogUtil.info(JobCommon2.class, JsonUtil.formatJsonString(dataxConfig));

            String t_id = zdhDataxAutoInfo.getTask_logs_id();
            String python_home=zdhDataxAutoInfo.getPython_home();
            String datax_home=zdhDataxAutoInfo.getDatax_home();
            String script_path = datax_home+"/zdh_datax";
            String script_context = JsonUtil.formatJsonString(dataxConfig);
            String file_name = script_path + "/" + t_id + "_online";
            //校验是否本地datax
            if (StringUtils.isEmpty("")) {
                //本地直接生成文件
                insertLog(tli, "DEBUG", "[调度平台]:DATAX,本地执行");
                FileUtils.forceMkdirParent(new File(file_name));
                FileUtils.write(new File(file_name), script_context, Charset.defaultCharset().name(), false);
                String newcommand = python_home + " " + datax_home + "/bin/datax.py " + file_name;
                insertLog(tli, "DEBUG", script_context);
                insertLog(tli, "DEBUG", newcommand);
                Map result = new HashMap<String, String>();
                insertLog(tli, "DEBUG", "系统编码:" + Charset.defaultCharset().name());
                if (system.toLowerCase().startsWith("win")) {
                    result = CommandUtils.exeCommand2(tli, "cmd.exe", "/c", newcommand, Charset.defaultCharset().name());
                } else {
                    result = CommandUtils.exeCommand2(tli, "sh", "-c", newcommand, Charset.defaultCharset().name());
                }
                long t2 = System.currentTimeMillis();
                insertLog(tli, "DEBUG", "[调度平台]:DATAX,DATAX任务执行结束,耗时:" + (t2 - t1) / 1000 + "s");
                if (result.get("result").equals("success")) {
                    return true;
                }
                return false;
            }

            return false;

        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            throw e;
        }

    }

    private static DataxReader generator_reader(ZdhDataxAutoInfo zdhDataxAutoInfo) throws Exception {
        String username = zdhDataxAutoInfo.getDsi_Input().getUser();
        String password = zdhDataxAutoInfo.getDsi_Input().getPassword();
        String driver = zdhDataxAutoInfo.getDsi_Input().getDriver();
        String url = zdhDataxAutoInfo.getDsi_Input().getUrl();
        String input_type = zdhDataxAutoInfo.getDsi_Input().getData_source_type();

        Map<String,Object> config=new HashMap<>();
        config.put("username", username);
        config.put("password", password);
        config.put("driverClassName", driver);
        config.put("url", url);
        config.put("table", zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getData_sources_table_name_input());
        config.put("column", zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getData_sources_table_columns());
        config.put("where", zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getData_sources_filter_input());
        config.put("splitPK", zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getRepartition_cols_input());
        if(!StringUtils.isEmpty(zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getData_sources_params_input())){
            config.put("param",JsonUtil.toJavaMap(zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getData_sources_params_input()));
        }else{
            config.put("param", JsonUtil.createEmptyMap());
        }
        config.put("fileType", zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getFile_type_input());
        config.put("encoding", zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getEncoding_input());
        config.put("fieldDelimiter", zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getSep_input());
        config.put("skipHeader", zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getHeader_input());

        String dbType = JdbcUtils.getDbType(url, zdhDataxAutoInfo.getDsi_Input().getDriver());

        DataxReader reader=null;
        if(!StringUtils.isEmpty(dbType) && input_type.equalsIgnoreCase("JDBC")){
            config.put("name", dbType);
            reader=new JdbcReader();
        }else if(input_type.equalsIgnoreCase("HDFS")){
            //hdfs,或者本地文件
            config.put("name", "hdfsreader");
            reader=new HdfsReader();
            if(!url.startsWith("hdfs")){
                //本地文件
                config.put("name", "txtfilewriter");
                reader=new TxtFileReader();
            }
        }else if(input_type.equalsIgnoreCase("HABSE")){
            config.put("name", "hbase11xreader");
            reader=new HbaseReader();
        }else if(input_type.equalsIgnoreCase("MONGODB")){
            config.put("name", "mongodbreader");
            reader=new MongoDbReader();
        }else if(input_type.equalsIgnoreCase("HIVE")){
            config.put("name", "hdfsreader");
            reader=new HdfsReader();
        }else if(input_type.equalsIgnoreCase("SFTP")){
            config.put("name", "ftpreader");
            reader=new FtpReader();
        }else{
            throw new Exception("暂不支持的数据源类型:"+input_type);
        }
        reader.reader(config);

        return  reader;
    }

    private static DataxWriter generator_writer(ZdhDataxAutoInfo zdhDataxAutoInfo) throws Exception {
        String username = zdhDataxAutoInfo.getDsi_Output().getUser();
        String password = zdhDataxAutoInfo.getDsi_Output().getPassword();
        String driver = zdhDataxAutoInfo.getDsi_Output().getDriver();
        String url = zdhDataxAutoInfo.getDsi_Output().getUrl();
        String input_type = zdhDataxAutoInfo.getDsi_Output().getData_source_type();

        Map<String,Object> config=new HashMap<>();
        config.put("username", username);
        config.put("password", password);
        config.put("driverClassName", driver);
        config.put("url", url);
        config.put("table", zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getData_sources_table_name_output());
        config.put("column", zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getData_sources_table_columns());
        config.put("clear", zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getData_sources_clear_output());
        if(!StringUtils.isEmpty(zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getData_sources_params_output())){
            config.put("param",JsonUtil.toJavaMap(zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getData_sources_params_output()));
        }else{
            config.put("param", JsonUtil.createEmptyMap());
        }
        config.put("fileType", zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getFile_type_output());
        config.put("encoding", zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getEncoding_output());
        config.put("fieldDelimiter", zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getSep_output());
        config.put("model", zdhDataxAutoInfo.getEtlTaskDataxAutoInfo().getModel_output());

        String dbType = JdbcUtils.getDbType(url, zdhDataxAutoInfo.getDsi_Input().getDriver());

        DataxWriter writer=null;
        if(!StringUtils.isEmpty(dbType) && input_type.equalsIgnoreCase("JDBC")){
            config.put("name", dbType);
            writer=new JdbcWriter();
        }else if(input_type.equalsIgnoreCase("HDFS")){
            //hdfs,或者本地文件
            config.put("name", "hdfswriter");
            writer=new HdfsWriter();
            if(!url.startsWith("hdfs")){
                //本地文件
                config.put("name", "txtfilewriter");
                writer=new TxtFileWriter();
            }

        }else if(input_type.equalsIgnoreCase("HABSE")){
            config.put("name", "hbase11xwriter");
            writer=new HbaseWriter();
        }else if(input_type.equalsIgnoreCase("MONGODB")){
            config.put("name", "mongodbwriter");
            writer=new MongoDbWriter();
        }else if(input_type.equalsIgnoreCase("HIVE")){
            config.put("name", "hdfswriter");
            writer=new HdfsWriter();
        }else if(input_type.equalsIgnoreCase("SFTP")){
            config.put("name", "ftpwriter");
            writer=new FtpWriter();
        }else{
            throw new Exception("暂不支持的数据源类型:"+input_type);
        }
        writer.writer(config);

        return  writer;
    }

    public static byte[] readSftp(Dsi_Info dsiInfo, EtlTaskUnstructureInfo etlTaskUnstructureInfo) throws IOException, SftpException {
        try {
            String port = "22";
            String host = dsiInfo.getUrl();
            if (dsiInfo.getUrl().contains(":")) {
                port = dsiInfo.getUrl().split(":")[1];
                host = dsiInfo.getUrl().split(":")[0];
            }
            SFTPUtil sftpUtil = new SFTPUtil(dsiInfo.getUser(), dsiInfo.getPassword(), host, Integer.parseInt(port));
            sftpUtil.login();
            String input_path = etlTaskUnstructureInfo.getInput_path();
            int end_index = input_path.lastIndexOf('/');
            String dir = input_path.substring(0, end_index);
            String file_name = input_path.substring(end_index+1);
            byte[] file_tmp = sftpUtil.download(dir, file_name);
            return file_tmp;
        } catch (Exception e) {
            throw e;
        }
    }

    public static void writeSftp(Dsi_Info dsiInfo, EtlTaskUnstructureInfo etlTaskUnstructureInfo, byte[] file) throws IOException, SftpException {
        try {
            String port = "22";
            String host = dsiInfo.getUrl();
            if (dsiInfo.getUrl().contains(":")) {
                port = dsiInfo.getUrl().split(":")[1];
                host = dsiInfo.getUrl().split(":")[0];
            }
            SFTPUtil sftpUtil = new SFTPUtil(dsiInfo.getUser(), dsiInfo.getPassword(), host, Integer.parseInt(port));
            sftpUtil.login();
            String output_path = etlTaskUnstructureInfo.getOutput_path();
            String input_path = etlTaskUnstructureInfo.getInput_path();
            String dir = output_path;
            String file_name = input_path.substring(input_path.lastIndexOf('/')+1);
            sftpUtil.upload(dir, file_name, file);
            byte[] file_tmp = sftpUtil.download(dir, file_name);
        } catch (Exception e) {

            throw e;
        }
    }

    public static byte[] readFtp(Dsi_Info dsiInfo, EtlTaskUnstructureInfo etlTaskUnstructureInfo) throws Exception {
        String server = dsiInfo.getUrl();
        int port = 21;
        String user = dsiInfo.getUser();
        String pass = dsiInfo.getPassword();
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(server, port);
            ftpClient.login(user, pass);

            // use local passive mode to pass firewall
            ftpClient.enterLocalPassiveMode();

            // get details of a file or directory
            String remoteFilePath = etlTaskUnstructureInfo.getInput_path();
            InputStream is=ftpClient.retrieveFileStream(remoteFilePath);
            //FTPFile ftpFile = ftpClient.mlistFile(remoteFilePath);
//            if (ftpFile != null) {
//                String name = ftpFile.getName();
//                long size = ftpFile.getSize();
//                String timestamp = ftpFile.getTimestamp().getTime().toString();
//                String type = ftpFile.isDirectory() ? "Directory" : "File";
//                if (type != "File") {
//                    throw new Exception("读取的不是一个文件类型");
//                }
//                is = ftpClient.retrieveFileStream(remoteFilePath);
//            } else {
//                System.out.println("The specified file/directory may not exist!");
//                throw new Exception("读取的不是一个文件类型");
//            }

            ftpClient.logout();
            ftpClient.disconnect();
            return IOUtils.toByteArray(is);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (ftpClient.isConnected()) {
                try {
                    ftpClient.disconnect();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

    public static void writeFtp(Dsi_Info dsiInfo, EtlTaskUnstructureInfo etlTaskUnstructureInfo, byte[] file) throws IOException {

        String input_path = etlTaskUnstructureInfo.getInput_path();
        String file_name = input_path.substring(input_path.lastIndexOf('/')+1);
        String path=etlTaskUnstructureInfo.getOutput_path();
        if(!etlTaskUnstructureInfo.getOutput_path().startsWith("/")){
            path = "/"+path;
        }
        String filename = "ftp://"+dsiInfo.getUser()+":"+ dsiInfo.getPassword()+"@"+dsiInfo.getUrl()+":21"+path+"/"+file_name;
        URL ftp = new URL(filename);
        PrintWriter pw = new PrintWriter(ftp.openConnection().getOutputStream());
        String s = new String(file);
        pw.write(s);
        pw.flush();
        pw.close();
    }

    public static byte[] readHdfs(Dsi_Info dsiInfo, EtlTaskUnstructureInfo etlTaskUnstructureInfo) throws Exception {
        Configuration conf = new Configuration();
        boolean result = true;
        try {
            String fs_defaultFS = dsiInfo.getUrl();
            String hadoop_user_name = dsiInfo.getUser();
            FileSystem fs = FileSystem.getLocal(conf);
            if (!StringUtils.isEmpty(fs_defaultFS)) {
                fs = FileSystem.get(new URI(fs_defaultFS), conf, hadoop_user_name);
            }
            String input_path = etlTaskUnstructureInfo.getInput_path();
            Path hdfsReadPath = new Path(input_path);
            FSDataInputStream inputStream = fs.open(hdfsReadPath);
            //Classical input stream usage
            byte[] out= IOUtils.toByteArray(inputStream);
            inputStream.close();
            fs.close();
            return out;
        }catch (Exception e){
            throw e;
        }
    }

    public static void writeHdfs(Dsi_Info dsiInfo, EtlTaskUnstructureInfo etlTaskUnstructureInfo, byte[] file) throws Exception {
        Configuration conf = new Configuration();
        boolean result = true;
        try {
            String fs_defaultFS = dsiInfo.getUrl();
            String hadoop_user_name = dsiInfo.getUser();
            FileSystem fs = FileSystem.getLocal(conf);
            if (!StringUtils.isEmpty(fs_defaultFS)) {
                fs = FileSystem.get(new URI(fs_defaultFS), conf, hadoop_user_name);
            }
            String input_path = etlTaskUnstructureInfo.getInput_path();
            String file_name = input_path.substring(input_path.lastIndexOf('/')+1);
            Path hdfsWritePath = new Path(etlTaskUnstructureInfo.getOutput_path()+"/"+file_name);
            if(file_name.contains(".xlsx") || file_name.contains(".xls")){
                File fileDir = new File( "tmp" );
                if (!fileDir.exists()) {
                    fileDir.mkdirs();
                }
                File f = new File("tmp/"+file_name);
                FileCopyUtils.copy(file, f );
                fs.copyFromLocalFile(new Path(f.getAbsolutePath()), hdfsWritePath);
                FileUtils.forceDelete(f);
            }else{
                FSDataOutputStream fsDataOutputStream = fs.create(hdfsWritePath,true);

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(fsDataOutputStream, StandardCharsets.UTF_8));
                bufferedWriter.write(IOUtils.toString(file, "UTF-8"));
                bufferedWriter.newLine();
                bufferedWriter.close();
            }
            fs.close();
        }catch (Exception e){
            throw e;
        }
    }

    public static List<String> getUnstructureEtlSql(TaskLogInstance tli, EtlTaskUnstructureInfo etlTaskUnstructureInfo){
        //执行结构化元数据
        String input_path = etlTaskUnstructureInfo.getInput_path();
        Timestamp cur_time=tli.getCur_time();
        String date_nodash = DateUtil.formatNodash(cur_time);
        String date_time = DateUtil.formatTime(cur_time);
        String date_dt = DateUtil.format(cur_time);
        Map<String, Object> jinJavaParam = new HashMap<>();
        jinJavaParam.put("zdh_date_nodash", date_nodash);
        jinJavaParam.put("zdh_date_time", date_time);
        jinJavaParam.put("zdh_date", date_dt);
        jinJavaParam.put("zdh_year",DateUtil.year(cur_time));
        jinJavaParam.put("zdh_month",DateUtil.month(cur_time));
        jinJavaParam.put("zdh_day",DateUtil.day(cur_time));
        jinJavaParam.put("zdh_hour",DateUtil.hour(cur_time));
        jinJavaParam.put("zdh_minute",DateUtil.minute(cur_time));
        jinJavaParam.put("zdh_second",DateUtil.second(cur_time));
        jinJavaParam.put("zdh_time",cur_time.getTime());
        jinJavaParam.put("zdh_user",tli.getOwner());
        jinJavaParam.put("zdh_create_time", new Timestamp(System.currentTimeMillis()));
        jinJavaParam.put("file_name",input_path.substring(input_path.lastIndexOf('/')+1));
        jinJavaParam.put("file_path",etlTaskUnstructureInfo.getOutput_path());
        String params = etlTaskUnstructureInfo.getUnstructure_params_output();
        if(!StringUtils.isEmpty(params)){
            Map<String, Object> jsonObject = JsonUtil.toJavaMap(params);
            for(Map.Entry<String, Object> p:jsonObject.entrySet()){
                jinJavaParam.put(p.getKey(), p.getValue());
            }
        }
        List<String> etl_sqls=new ArrayList<>();
        Jinjava jj = new Jinjava();
        String etl_sql = jj.render(etlTaskUnstructureInfo.getEtl_sql(), jinJavaParam);
        etl_sqls.add(etl_sql);
        return etl_sqls;
    }

    public static void updateTaskStatus(String status, String id, String process, TaskLogInstanceMapper tlim) {
        tlim.updateStatusById4(status, process, id);

    }

    public static void updateTaskLog(TaskLogInstance tli, TaskLogInstanceMapper tlim) {
        if (tli.getLast_time() == null) {
            if (tli.getCur_time() == null) {
                tli.setLast_time(tli.getStart_time());
            } else {
                tli.setLast_time(tli.getCur_time());
            }
        }
        //debugInfo(tli);
        tlim.updateByPrimaryKeySelective(tli);
    }

    public static void updateTaskLog(TaskGroupLogInstance tgli, TaskGroupLogInstanceMapper tglim) {
        if (tgli.getLast_time() == null) {
            if (tgli.getCur_time() == null) {
                tgli.setLast_time(tgli.getStart_time());
            } else {
                tgli.setLast_time(tgli.getCur_time());
            }
        }
        // debugInfo(tgli);
        tglim.updateByPrimaryKeySelective(tgli);
    }

    /**
     * 更新任务日志,etl_date
     *
     * @param tli
     * @param tlim
     */
    public static void updateTaskLogEtlDate(TaskLogInstance tli, TaskLogInstanceMapper tlim) {
        tli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
        tli.setProcess(ProcessEnum.INIT_DISPATCH_TIME);
        updateTaskLog(tli, tlim);
    }


    public static void updateTaskLogError(TaskLogInstance tli, String process, TaskLogInstanceMapper tlim, String status, int interval_time) {
        tli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
        tli.setStatus(status);//error,retry
        tli.setProcess(process);//调度完成
        tli.setRetry_time(DateUtil.add(new Timestamp(System.currentTimeMillis()), Calendar.SECOND, interval_time));
        updateTaskLog(tli, tlim);
    }

    public static void updateTaskLogError(TaskGroupLogInstance tgli, String process, TaskGroupLogInstanceMapper tglim, String status, int interval_time) {
        tgli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
        tgli.setStatus(status);//error,retry
        tgli.setProcess(process);//调度完成
        tgli.setRetry_time(DateUtil.add(new Timestamp(System.currentTimeMillis()), Calendar.SECOND, interval_time));
        updateTaskLog(tgli, tglim);
    }

    /**
     * 检查子任务依赖
     * 所有的调度之间的依赖关系均由此判断
     *
     * @param jobType
     * @param tli
     */
    public static boolean checkDep(String jobType, TaskLogInstance tli) {
        LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,初步检查当前任务{}依赖", tli.getId());
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        TaskGroupLogInstanceMapper tglim = (TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
        //检查任务依赖
        if (tli.getJump_dep() != null && tli.getJump_dep().equalsIgnoreCase("on")) {
            String msg2 = "[" + jobType + "] JOB ,跳过依赖任务,任务id: "+tli.getId();
            LogUtil.info(JobCommon2.class, msg2);
            insertLog(tli, "INFO", msg2);
            return true;
        }

        if (tli.getPre_tasks() == null || tli.getPre_tasks().equals("") || tli.getPre_tasks().equalsIgnoreCase("root")) {
            String msg2 = "[" + jobType + "] JOB ,是根节点任务,无依赖,直接执行,任务id: "+tli.getId();
            LogUtil.info(JobCommon2.class, msg2);
            insertLog(tli, "INFO", msg2);
            return true;
        } else {

            String group_id = tli.getGroup_id();
            TaskGroupLogInstance tgli = tglim.selectByPrimaryKey(group_id);
            String run_jsmind = tgli.getRun_jsmind_data();
            DAG dag = new DAG();
            if (!StringUtils.isEmpty(run_jsmind)) {
                List<Map<String, Object>> json_ary = (List<Map<String, Object>>)JsonUtil.toJavaMap(run_jsmind).get("run_line");
                for (int i = 0; i < json_ary.size(); i++) {
                    Map<String, Object> jsonObject = json_ary.get(i);
                    dag.addEdge(jsonObject.get("from").toString(), jsonObject.get("to").toString());
                }

                Set<String> parents = dag.getAllParent(tli.getId());

                List<task_num_info> tnis = tlim.selectByIds(parents.toArray(new String[parents.size()]));
                for (task_num_info tni : tnis) {
                    //debugInfo(tni);
                    if (tni.getStatus().equalsIgnoreCase(JobStatus.DISPATCH.getValue()) || tni.getStatus().equalsIgnoreCase(JobStatus.CREATE.getValue())
                            || tni.getStatus().equalsIgnoreCase(JobStatus.WAIT_RETRY.getValue())
                            || tni.getStatus().equalsIgnoreCase(JobStatus.CHECK_DEP.getValue()) || tni.getStatus().equalsIgnoreCase(JobStatus.ETL.getValue())
                            || tni.getStatus().equalsIgnoreCase(JobStatus.NON.getValue())) {
                        if (tni.getNum() > 0)
                            //insertLog(tli, "INFO", "所有的父级,祖先级任务存在未完成,跳过当前任务检查");
                        {
                            return false;
                        }
                    }
                }
                LogUtil.info(JobCommon2.class, "所有的父级,祖先级任务:" + parents);
                insertLog(tli, "INFO", "目前只判断父级任务是否完成,如果判断祖先级任务是否完成,可在此处做深度处理...,任务id: "+tli.getId());
                insertLog(tli, "INFO", "所有的父级,祖先级任务:" + parents+", 任务id: "+tli.getId());
            }

            LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,开始检查当前任务上游任务依赖,任务id: " + tli.getId());
            insertLog(tli, "INFO", "[" + jobType + "] JOB ,开始检查当前任务上游任务依赖,任务id: "+tli.getId());

            StringBuilder finish_id = new StringBuilder();
            StringBuilder error_id = new StringBuilder();
            StringBuilder kill_id = new StringBuilder();
            StringBuilder run_id = new StringBuilder();
            List<TaskLogInstance> finish = new ArrayList<>();
            List<TaskLogInstance> error = new ArrayList<>();
            List<TaskLogInstance> kill = new ArrayList<>();
            List<TaskLogInstance> run = new ArrayList<>();
            //判断逻辑,级别0：所有的上游任务都执行成功可触发
            //        级别1：所有上游任务执行完成,并存在杀死的任务
            //        级别2：所有上游任务执行完成,并存在失败的任务
            String task_log_ids = tli.getPre_tasks();

            List<TaskLogInstance> taskLogInstances = tlim.selectByIds2(task_log_ids.split(","));
            StringBuilder sb = new StringBuilder();
            for (TaskLogInstance tli_0 : taskLogInstances) {
                if (tli_0.getStatus().equalsIgnoreCase(JobStatus.FINISH.getValue()) || tli_0.getStatus().equalsIgnoreCase(JobStatus.SKIP.getValue())) {
                    finish.add(tli_0);
                    finish_id.append(",");
                    finish_id.append(tli_0.getId());
                } else if (tli_0.getStatus().equalsIgnoreCase(JobStatus.ERROR.getValue())) {
                    error.add(tli_0);
                    error_id.append(",");
                    error_id.append(tli_0.getId());
                } else if (tli_0.getStatus().equalsIgnoreCase(JobStatus.KILLED.getValue())) {
                    kill.add(tli_0);
                    kill_id.append(",");
                    kill_id.append(tli_0.getId());
                } else {
                    run.add(tli_0);
                    run_id.append(",");
                    run_id.append(tli_0.getId());
                }

            }
            boolean is_pass = true;
            String status = "成功";
            if (tli.getDepend_level().equalsIgnoreCase("0")) {
                if (run.size() > 0 || kill.size() > 0 || error.size() > 0) {
                    is_pass = false;
                }
            } else if (tli.getDepend_level().equalsIgnoreCase("1")) {
                status = "杀死";
                if (run.size() > 0) {
                    is_pass = false;
                }
            } else if (tli.getDepend_level().equalsIgnoreCase("2")) {
                status = "失败";
                if (run.size() > 0) {
                    is_pass = false;
                }
            }else if (tli.getDepend_level().equalsIgnoreCase("3")) {
                status = "成功,跳过,失败";
                if (run.size() > 0) {
                    is_pass = false;
                }
            }

            String msg = "[" + jobType + "] JOB ,当前任务依赖上游任务状态:[" + status + "],才会触发,任务id: "+tli.getId();
            LogUtil.info(JobCommon2.class, msg);
            insertLog(tli, "INFO", msg);
            String etl_date = tli.getEtl_date();
            String msg2 = "已完成任务:" + (finish_id.toString().startsWith(",") ? finish_id.toString().substring(1) : finish_id.toString());
            String msg3 = "已杀死任务:" + (kill_id.toString().startsWith(",") ? kill_id.toString().substring(1) : kill_id.toString());
            String msg4 = "已失败任务:" + (error_id.toString().startsWith(",") ? error_id.toString().substring(1) : error_id.toString());
            String msg5 = "未完成任务:" + (run_id.toString().startsWith(",") ? run_id.toString().substring(1) : run_id.toString());
            LogUtil.info(JobCommon2.class, msg2);
            LogUtil.info(JobCommon2.class, msg3);
            LogUtil.info(JobCommon2.class, msg4);
            LogUtil.info(JobCommon2.class, msg5);
            insertLog(tli, "INFO", msg2);
            insertLog(tli, "INFO", msg3);
            insertLog(tli, "INFO", msg4);
            insertLog(tli, "INFO", msg5);

//            tli.setStatus("check_dep");
//            tli.setProcess("7");
//            tli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
//            updateTaskLog(tli,tlim);
            updateTaskStatus(JobStatus.CHECK_DEP.getValue(), tli.getId(), "7", tlim);

            if (is_pass) {
                String msg6 = "[" + jobType + "] JOB ,依赖任务状态,满足当前任务触发条件" + ",ETL日期" + etl_date+" ,任务id: "+tli.getId();
                LogUtil.info(JobCommon2.class, msg6);
                insertLog(tli, "INFO", msg6);
                return true;
            } else {
                String msg6 = "[" + jobType + "] JOB ,依赖任务状态,未满足当前任务触发条件" + ",ETL日期" + etl_date+" ,任务id: "+tli.getId();
                LogUtil.info(JobCommon2.class, msg6);
                insertLog(tli, "INFO", msg6);
                return false;
            }

        }


    }

    /**
     * 检查子任务中的任务组依赖(特殊依赖-group)
     * 可理解为类似数据采集的任务(只是这个任务只做依赖关系)
     *
     * @param jobType
     * @param tli
     * @return
     */
    public static boolean checkDep_group(String jobType, TaskLogInstance tli) {
        LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,开始检查任务依赖, 任务id: " + tli.getId());
        insertLog(tli, "INFO", "[" + jobType + "] JOB ,开始检查任务依赖, 任务id: "+tli.getId());
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        TaskGroupLogInstanceMapper tglim = (TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
        String dep_job_id = tli.getEtl_task_id();
        String etl_date = tli.getEtl_date();

        //判断当前节点父任务是否全部已完成,否直接返回
        if (!checkDep(jobType, tli)) {
            return false;
        }

        List<TaskGroupLogInstance> taskLogsList = tglim.selectByIdEtlDate(dep_job_id, etl_date);
        if (taskLogsList == null || taskLogsList.size() <= 0) {
            String msg = "[" + jobType + "] JOB ,依赖任务组:" + dep_job_id + ",ETL日期:" + etl_date + ",未完成, 任务id: "+tli.getId();
            LogUtil.info(JobCommon2.class, msg);
            insertLog(tli, "INFO", msg);
            tli.setThread_id(""); //设置为空主要是为了 在检查依赖任务期间杀死
            tli.setStatus(JobStatus.CHECK_DEP.getValue());
            tli.setProcess(ProcessEnum.CHECK_DEP);
            tli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            updateTaskLog(tli, tlim);
            return false;
        }
        String msg2 = "[" + jobType + "] JOB ,依赖任务组:" + dep_job_id + ",ETL日期:" + etl_date + ",已完成, 任务id: "+tli.getId();
        LogUtil.info(JobCommon2.class, msg2);
        insertLog(tli, "INFO", msg2);
        return true;
    }


    /**
     * 检查子任务中的jdbc依赖(特殊依赖-jdbc)
     * 可理解为类似数据采集的任务(只是这个任务只做依赖关系)
     *
     * @param jobType
     * @param tli
     * @return
     */
    public static boolean checkDep_jdbc(String jobType, TaskLogInstance tli) {
        LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,开始检查任务中的JDBC依赖,目前jdbc依赖只支持单条sql语句检查");
        insertLog(tli, "INFO", "[" + jobType + "] JOB ,开始检查任务中的JDBC依赖,目前jdbc依赖只支持单条sql语句检查");
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        TaskGroupLogInstanceMapper tglim = (TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
        DataSourcesMapper dataSourcesMapper = (DataSourcesMapper) SpringContext.getBean("dataSourcesMapper");
        String etl_date = tli.getEtl_date();

        if (!checkDep(jobType, tli)) {
            return false;
        }

        DBUtil dbUtil = new DBUtil();

        LogUtil.info(JobCommon2.class, "checkDep_jdbc:" + tli.getRun_jsmind_data());
        if (!org.apache.commons.lang3.StringUtils.isEmpty(tli.getRun_jsmind_data())) {
            Map<String, Object> jdbc = JsonUtil.toJavaMap(tli.getRun_jsmind_data());
            String driver = jdbc.getOrDefault("driver", "").toString();
            String url = jdbc.getOrDefault("url", "").toString();
            String username = jdbc.getOrDefault("username", "").toString();
            String password = jdbc.getOrDefault("password", "").toString();
            String jdbc_sql = jdbc.getOrDefault("jdbc_sql", "").toString();
            if(!StringUtils.isEmpty(jdbc.getOrDefault("data_sources_choose_input", "").toString())){
                DataSourcesInfo dsi = dataSourcesMapper.selectByPrimaryKey(jdbc.getOrDefault("data_sources_choose_input", "").toString());
                if(dsi!=null){
                    driver = dsi.getDriver();
                    url = dsi.getUrl();
                    username = dsi.getUsername();
                    password = dsi.getPassword();
                }
            }
            Map<String, Object> map = (Map<String, Object>) JsonUtil.toJavaMap(tli.getParams());

            if (StringUtils.isEmpty(jdbc_sql)) {
                insertLog(tli, "INFO", "JDBC依赖检查SQL:sql语句为空,直接跳过检查");
                return true;
            }
            //jdbc_sql 动态参数替换
            String new_jdbc_sql = DynamicParams(map, tli, jdbc_sql);


            insertLog(tli, "INFO", "JDBC依赖检查SQL: " + new_jdbc_sql);

            if (new_jdbc_sql.trim().toLowerCase().startsWith("insert")) {

                String[] rst = dbUtil.CUD(driver, url, username, password, new_jdbc_sql, new String[]{});
                if (rst[0].equalsIgnoreCase("true")) {
                    String msg = "[" + jobType + "] JOB ,JDBC写入成功 " + ",ETL日期:" + etl_date;
                    insertLog(tli, "INFO", msg);
                    return true;
                }
                String msg = "[" + jobType + "] JOB ,JDBC写入异常:" + rst[1] + ",ETL日期:" + etl_date;
                insertLog(tli, "INFO", msg);
                jobFail(tli.getJob_type(), tli);
                return false;
            }
            try {
                List<Map<String, Object>> result = dbUtil.R5(driver, url, username, password, new_jdbc_sql);
                if (result.size() < 1) {
                    String msg = "[" + jobType + "] JOB ,依赖JDBC任务检查" + ",ETL日期:" + etl_date + ",未完成";
                    insertLog(tli, "INFO", msg);
                    return false;
                }

            } catch (Exception e) {
                LogUtil.error(JobCommon2.class, e);
                insertLog(tli, "error", "JDBC依赖检查异常," + e.getMessage());
                jobFail(tli.getJob_type(), tli);
                return false;
            }

        } else {
            insertLog(tli, "error", "请检查jdbc链接需要的基础参数是否缺少");
            jobFail(tli.getJob_type(), tli);
        }

        String msg2 = "[" + jobType + "] JOB ,依赖JDBC任务检查" + ",ETL日期:" + etl_date + ",已完成";
        LogUtil.info(JobCommon2.class, msg2);
        insertLog(tli, "INFO", msg2);
        return true;
    }

    /**
     * 检查hdfs依赖
     *
     * @param jobType
     * @param tli
     * @return
     */
    public static boolean checkDep_hdfs(String jobType, TaskLogInstance tli) {
        LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,开始检查任务中的HDFS依赖,目前HDFS依赖支持检查单个文件和写入单个文件,不支持密码验证");
        insertLog(tli, "INFO", "[" + jobType + "] JOB ,开始检查任务中的HDFS依赖,目前HDFS依赖支持检查单个文件和写入单个文件,不支持密码验证");
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        TaskGroupLogInstanceMapper tglim = (TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
        String etl_date = tli.getEtl_date();

        if (!checkDep(jobType, tli)) {
            return false;
        }
        Map<String, Object> param = getJinJavaParam(tli);
        Jinjava jj = new Jinjava();
        String jsmin_data = jj.render(tli.getRun_jsmind_data(), param);
        Map<String, Object> jsmin_json = JsonUtil.toJavaMap(jsmin_data);

        String fs_defaultFS = jsmin_json.getOrDefault("url", "").toString();
        String hadoop_user_name = jsmin_json.getOrDefault("username", "").toString();
        String path_str = jj.render(jsmin_json.getOrDefault("hdfs_path", "").toString(), param);
        String hdfs_mode = jsmin_json.getOrDefault("hdfs_mode", "").toString();
        Configuration conf = new Configuration();
        boolean result = true;
        try {
            LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,开始连接hadoop,参数url:" + fs_defaultFS + ",用户:" + hadoop_user_name + " ,路径:" + path_str);
            insertLog(tli, "info", "[" + jobType + "] JOB ,开始连接hadoop,参数url:" + fs_defaultFS + ",用户:" + hadoop_user_name + " ,路径:" + path_str);
            FileSystem fs = FileSystem.getLocal(conf);
            if (!StringUtils.isEmpty(fs_defaultFS)) {
                fs = FileSystem.get(new URI(fs_defaultFS), conf, hadoop_user_name);
            }

            if (StringUtils.isEmpty(hdfs_mode)) {
                throw new Exception("请选择正确的文件模式,检查文件/生成文件");
            }

            if (!StringUtils.isEmpty(hdfs_mode) && hdfs_mode.equalsIgnoreCase("0")) {
                insertLog(tli, "info", "[" + jobType + "] JOB , 开始检查文件:" + path_str + " ,是否存在");
                result = fs.exists(new Path(path_str));

            } else if (!StringUtils.isEmpty(hdfs_mode) && hdfs_mode.equalsIgnoreCase("1")) {
                insertLog(tli, "info", "[" + jobType + "] JOB , 开始生成文件:" + path_str);
                result = fs.createNewFile(new Path(path_str));
            }
            if (result) {
                insertLog(tli, "info", "[" + jobType + "] JOB , 完成检查文件/生成文件:" + path_str);
            } else {
                insertLog(tli, "info", "[" + jobType + "] JOB , 检查文件/生成文件:" + path_str + ", 未完成,将再次检查/生成");
            }
            return result;

        } catch (IOException e) {
            LogUtil.error(JobCommon2.class, e);
            insertLog(tli, "ERROR", e.getMessage());
            JobCommon2.jobFail(jobType, tli);
        } catch (InterruptedException e) {
            LogUtil.error(JobCommon2.class, e);
            insertLog(tli, "ERROR", e.getMessage());
            JobCommon2.jobFail(jobType, tli);
        } catch (URISyntaxException e) {
            LogUtil.error(JobCommon2.class, e);
            insertLog(tli, "ERROR", e.getMessage());
        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            insertLog(tli, "ERROR", e.getMessage());
        }

        JobCommon2.jobFail(jobType, tli);
        return false;
    }

    /**
     * 时间序列发送etl任务到后台执行
     *
     * @param jobType
     * @param task_logs_id
     * @param exe_status
     * @param tli
     * @return
     */
    public static boolean runTimeSeq(String jobType, String task_logs_id, boolean exe_status, TaskLogInstance tli) {

        LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB,任务模式为[时间序列]");
        insertLog(tli, "info", "[" + jobType + "] JOB,任务模式为[时间序列]");

        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        insertLog(tli, "info", "[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        exe_status = sendZdh(task_logs_id, "[" + jobType + "]", exe_status, tli);

        if (exe_status) {
            LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,执行命令成功");
            insertLog(tli, "info", "[" + jobType + "] JOB ,执行命令成功");

            if (tli.getEnd_time() == null) {
                LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                insertLog(tli, "info", "[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                tli.setEnd_time(new Timestamp(System.currentTimeMillis()));
            }

        } else {
            setJobLastStatus(tli, task_logs_id, tlim);
        }
        return exe_status;
    }

    /**
     * 执行一次任务发送etl任务到后台执行
     *
     * @param jobType
     * @param task_logs_id
     * @param exe_status
     * @param tli
     * @return
     */
    public static boolean runOnce(String jobType, String task_logs_id, boolean exe_status, TaskLogInstance tli) {
        LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB,任务模式为[ONCE]");
        insertLog(tli, "info", "[" + jobType + "] JOB,任务模式为[ONCE]");

        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        insertLog(tli, "info", "[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        exe_status = sendZdh(task_logs_id, "[" + jobType + "]", exe_status, tli);

        if (exe_status) {
            LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,执行命令成功");
            insertLog(tli, "info", "[" + jobType + "] JOB ,执行命令成功");

            if (tli.getEnd_time() == null) {
                LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                insertLog(tli, "info", "[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                tli.setEnd_time(new Timestamp(System.currentTimeMillis()));
            }
            tli.setStatus("finish");
            //插入日志
            LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,结束调度任务");
            insertLog(tli, "info", "[" + jobType + "] JOB ,结束调度任务");

        } else {
            setJobLastStatus(tli, task_logs_id, tlim);
        }
        quartzManager2.deleteTask(tli, "finish", "finish");
        return exe_status;
    }

    /**
     * 重复执行任务发送etl任务到后台执行
     *
     * @param jobType
     * @param task_logs_id
     * @param exe_status
     * @param tli
     * @return
     */
    public static boolean runRepeat(String jobType, String task_logs_id, boolean exe_status, TaskLogInstance tli) {
        LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB,任务模式为[重复执行模式]");
        insertLog(tli, "info", "[" + jobType + "] JOB,任务模式为[重复执行模式]");

        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        insertLog(tli, "info", "[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        exe_status = sendZdh(task_logs_id, "[" + jobType + "]", exe_status, tli);
        if (exe_status) {
            LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,执行命令成功");
            insertLog(tli, "INFO", "[" + jobType + "] JOB ,执行命令成功");

            if (tli.getEnd_time() == null) {
                LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                insertLog(tli, "INFO", "[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                tli.setEnd_time(new Timestamp(System.currentTimeMillis()));
            }
        } else {
            setJobLastStatus(tli, task_logs_id, tlim);
        }
        return exe_status;
    }

    /**
     * 调度执行的任务命令,jdbc,失败后触发此方法
     *
     * @param jobType
     * @param tli
     */
    public static void jobFail(String jobType, TaskLogInstance tli) {
        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        LogUtil.info(JobCommon2.class, "[" + jobType + "] JOB ,调度命令执行失败未能发往任务到后台ETL执行");
        insertLog(tli, "info", "[" + jobType + "] JOB ,调度命令执行失败未能发往任务到后台ETL执行");
        String msg = "[" + jobType + "] JOB ,调度命令执行失败未能发往任务到后台ETL执行,重试次数已达到最大,状态设置为error";
        String status = "error";
        if (tli.getPlan_count().equalsIgnoreCase("-1") || Long.parseLong(tli.getPlan_count()) > tli.getCount()) {
            //重试
            status = "wait_retry";
            msg = "[" + jobType + "] JOB ,调度命令执行失败未能发往任务到后台ETL执行,状态设置为wait_retry等待重试";
            if (tli.getPlan_count().equalsIgnoreCase("-1")) {
                msg = msg + ",并检测到重试次数为无限次";
            }
        }
        LogUtil.info(JobCommon2.class, msg);
        insertLog(tli, "ERROR", msg);
        int interval_time = (tli.getInterval_time() == null || tli.getInterval_time().equals("")) ? 5 : Integer.parseInt(tli.getInterval_time());
        //调度时异常
        updateTaskLogError(tli, ProcessEnum.DISPATCH_JOB_FAIL, tlim, status, interval_time);
    }

    public static void setJobLastStatus(TaskLogInstance tli, String task_logs_id, TaskLogInstanceMapper tlim) {
        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        String status = "error";
        String msg = "发送ETL任务到zdh处理引擎,存在问题,重试次数已达到最大,状态设置为error";
        if (tli.getPlan_count().equalsIgnoreCase("-1") || Long.parseLong(tli.getPlan_count()) > tli.getCount()) {
            //重试
            status = "wait_retry";
            msg = "发送ETL任务到zdh处理引擎,存在问题,状态设置为wait_retry等待重试";
            if (tli.getPlan_count().equalsIgnoreCase("-1")) {
                msg = msg + ",并检测到重试次数为无限次";
            }
        }
        LogUtil.info(JobCommon2.class, msg);
        insertLog(tli, "ERROR", msg);
        int interval_time = (tli.getInterval_time() == null || tli.getInterval_time().equals("")) ? 5 : Integer.parseInt(tli.getInterval_time());
        updateTaskLogError(tli, ProcessEnum.INIT_ETL_ENGINE17, tlim, status, interval_time);
//        if (status.equalsIgnoreCase("error")) {
//            quartzManager2.deleteTask(tli, "finish", status);
//        }
    }

//    public static User getUser() {
//        User user = (User) SecurityUtils.getSubject().getPrincipal();
//        return user;
//    }

    /**
     * 选择具体的job执行引擎,只有调度和手动重试触发(手动执行部分请参见其他方法)
     * 解析任务组时间->解析创建任务组实例->创建子任务实例
     *
     * @param quartzJobInfo
     * @param is_retry      0:调度,2:手动重试
     * @param sub_tasks     重试的子任务divId,如果全部重试可传null,或者所有的divId
     */
    public static void chooseJobBean(QuartzJobInfo quartzJobInfo, int is_retry, TaskGroupLogInstance retry_tgli, String[] sub_tasks) {
        QuartzJobMapper qjm = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        TaskGroupLogInstanceMapper tglim = (TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        RedisUtil redisUtil = (RedisUtil) SpringContext.getBean("redisUtil");
        //手动重试增加重试实例,自动重试在原来的基础上
        String logId = UUID.randomUUID().toString();
        if(StringUtils.isEmpty(MDC.get(Const.MDC_LOG_ID))){
            MDC.put(Const.MDC_LOG_ID, logId);
        }
        if (quartzJobInfo.getJob_type().equalsIgnoreCase(JobType.EMAIL.getCode())) {
            LogUtil.debug(JobCommon2.class, "调度任务[EMAIL],开始调度");
            EmailJob.run(quartzJobInfo);
            EmailJob.notice_event();
            EmailJob.beaconFireAlarm();
            return;
        } else if (quartzJobInfo.getJob_type().equals(JobType.RETRY.getCode())) {
            LogUtil.debug(JobCommon2.class, "调度任务[RETRY],开始调度");
            RetryJob.run(quartzJobInfo);
            return;
        } else if (quartzJobInfo.getJob_type().equalsIgnoreCase(JobType.CHECK.getCode())) {
            LogUtil.debug(JobCommon2.class, "调度任务[CHECK],开始调度");
            //通过redis获取check接口类,遍历所有接口实现执行任务
            String checkImpls = ConfigUtil.getParamUtil().getValue(ConfigUtil.getProductCode(), Const.ZDH_CHECK_IMPLS, "").toString();
            if(!StringUtils.isEmpty(checkImpls)){
                for (String impl: checkImpls.split(",")){
                    MDC.put(Const.MDC_LOG_ID, UUID.randomUUID().toString());
                    try {
                        CheckDepJobInterface cdji = (CheckDepJobInterface)Class.forName(impl).newInstance();
                        cdji.setObject(quartzJobInfo);
                        cdji.run();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }else{
                LogUtil.warn(JobCommon2.class, "当前系统未设置默认检查实现,请通过工具箱->参数设置->zdh_check_impls");
                //推送管理员告警
                EmailJob.sendAdminAlarmEmail("【系统告警】-高优处理","【ZDH系统】未设置默认检查实现,请通过工具箱->参数设置->zdh_check_impls");
            }
            //CheckDepJob.run(quartzJobInfo);
            //CheckStrategyDepJob.run();
            return;
        } else if (quartzJobInfo.getJob_type().equalsIgnoreCase(JobType.BLOOD.getCode())) {
            LogUtil.debug(JobCommon2.class, "调度任务[BLOOD],开始调度");
            CheckBloodSourceJob.Check(ConfigUtil.getValue(ConfigUtil.ZDP_PRODUCT));
            return;
        } else if(quartzJobInfo.getJob_type().equalsIgnoreCase(JobType.ETL.getCode())){
            //线程池执行具体调度任务
            threadPoolExecutor.execute( new Runnable() {
                @Override
                public void run() {

                    MDC.put(Const.MDC_LOG_ID, logId);
                    TaskGroupLogInstance tgli = new TaskGroupLogInstance();
                    try {
                        //复制quartzjobinfo到tli,任务基础信息完成复制
                        //BeanUtils.copyProperties(tgli, quartzJobInfo);
                        tgli = MapStructMapper.INSTANCE.quartzJobInfoToTaskGroupLogInstance(quartzJobInfo);
                        tgli.setId(SnowflakeIdWorker.getInstance().nextId() + "");
                    } catch (Exception e) {
                        LogUtil.error(JobCommon2.class, e);
                    } //catch (Exception e) {
//                         LogUtil.error(JobCommon2.class, e);
//                    }
                    //逻辑发送错误代码捕获发生自动重试(retry_job) 不重新生成实例id,使用旧的实例id
                    String last_task_id = "";
                    if (is_retry == 0) {
                        //调度触发,直接获取执行时间
                        Timestamp cur = getCurTime(quartzJobInfo);
                        tgli.setCur_time(cur);
                        tgli.setSchedule_source(ScheduleSource.SYSTEM.getCode());
                        tgli.setEtl_date(DateUtil.formatTime(cur));
                        tgli.setRun_time(new Timestamp(System.currentTimeMillis()));//实例开始时间
                        tgli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                        tgli.setStatus(JobStatus.NON.getValue());
                    }

                    if (is_retry == 2) {
                        //手动点击重试按钮触发
                        //手动点击重试,会生成新的实例信息,默认重置执行次数,并将上次执行失败的实例id 付给last_task_id
                        tgli = retry_tgli;
                        tgli.setProcess("1");
                        tgli.setSchedule_source(ScheduleSource.MANUAL.getCode());
                        tgli.setRun_time(new Timestamp(System.currentTimeMillis()));//实例开始时间
                        tgli.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                        tgli.setCount(0L);
                        tgli.setIs_retryed("0");
                        tgli.setId(SnowflakeIdWorker.getInstance().nextId() + "");
                        tgli.setStatus(JobStatus.NON.getValue());
                    }
                    //日期超限控制
                    if (is_retry == 0 && tgli.getCur_time().getTime()/1000 > quartzJobInfo.getEnd_time().getTime()/1000) {
                        //通知信息
                        LogUtil.info(JobCommon2.class, "任务ID:" + tgli.getJob_id() + ", 任务名称: " + tgli.getJob_context() + ", 当前任务超过日期控制,无法生成任务组信息,自动结束,请调整调度任务组中结束时间");
                        EmailJob.send_notice(tgli.getOwner(), "调度任务结束", "任务ID:"+tgli.getJob_id()+", 任务名称: "+tgli.getJob_context()+", 当前任务超过日期控制,无法生成任务组信息,自动结束,请调整调度任务组中结束时间", "告警");
                        return;
                    }

                    //此处更新主要是为了 日期超时时 也能记录下日志
                    if (is_retry == 2) {
                        insertLog(tgli, "INFO", "重试任务组更新信息,任务组数据处理日期:" + tgli.getEtl_date());
                        tglim.insertSelective(tgli);
                    } else {
                        insertLog(tgli, "INFO", "生成任务组信息,任务组数据处理日期:" + tgli.getEtl_date());
                        tglim.insertSelective(tgli);
                    }
                    //此处需要更新状态和last_time
                    //qjm.updateByPrimaryKeySelective(quartzJobInfo);
                    qjm.updateStatusLastTime(quartzJobInfo.getJob_id(), quartzJobInfo.getStatus(),quartzJobInfo.getLast_time());
                    //公共设置
                    tgli.setStatus(JobStatus.NON.getValue());//新实例状态设置为dispatch
                    //设置调度器唯一标识,调度故障转移时使用,如果服务器重启会自动生成新的唯一标识
                    //tgli.setServer_id(JobCommon2.web_application_id);

                    //todo 生成具体任务组下任务实例
                    sub_task_log_instance(tgli, sub_tasks);
                    tglim.updateStatus2Create(new String[]{tgli.getId()});
                    LogUtil.info(JobCommon2.class, "任务组信息: {}", JsonUtil.formatJsonString(tgli));
                    //debugInfo(tgli);
                    //tglim.updateByPrimaryKeySelective(tgli);

                    //检查任务依赖,和并行不冲突--此逻辑删除,目前任务组之间的依赖以子任务检查逻辑实现
                    //boolean dep = checkDep(quartzJobInfo.getJob_type(), tgli);
                    //更新任务依赖时间
                    process_time_info pti = tgli.getProcess_time2();
                    pti.setCheck_dep_time(DateUtil.getCurrentTime());
                    tgli.setProcess_time(pti);
                    //修改组任务状态,及修改子任务状态为检查依赖中
                    CheckDepJob.updateTaskGroupLogInstanceStatus(tgli);
//                if(dep){
//
//                }else{
//                    updateTaskLog(tgli,tglim);
//                    return ;
//                }
                }
            });
        } else {
            LogUtil.warn(JobCommon2.class, "不支持的调度任务类型:" + quartzJobInfo.getJob_type() + " ,将自动杀死");
            quartzManager2.deleteTask(quartzJobInfo, JobStatus.ERROR.getValue());
            EmailJob.alarm(quartzJobInfo, "调度任务解析异常告警", "不支持的调度任务类型:"+quartzJobInfo.getJob_type()+" ,将自动杀死");
        }

//        //如果调度任务类型是一次性则删除调度
//        if (quartzJobInfo.getJob_model().equalsIgnoreCase(JobModel.ONCE.getValue())) {
//            quartzManager2.deleteTask(quartzJobInfo, JobStatus.FINISH.getValue());
//        }
    }

    /**
     * 只管QUARTZ调度触发,手动执行另算
     * 如果使用调度时间,直接赋值即可
     * 如果使用自定义时间,获取上次时间和本次做diff
     */
    public static Timestamp getCurTime(QuartzJobInfo quartzJobInfo) {
        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        if (quartzJobInfo.getUse_quartz_time() != null && quartzJobInfo.getUse_quartz_time().equalsIgnoreCase(Const.ON)) {
            if (!StringUtils.isEmpty(quartzJobInfo.getTime_diff())) {
                int seconds = 0;
                try {
                    seconds = Integer.parseInt(quartzJobInfo.getTime_diff());
                } catch (NumberFormatException e) {
                    LogUtil.error(JobCommon2.class, e);
                }
                return DateUtil.add(quartzJobInfo.getQuartz_time(), Calendar.SECOND, -seconds);
            }
            return quartzJobInfo.getQuartz_time();
        }

        Timestamp cur = null;
        String step_size = quartzJobInfo.getStep_size();
        int dateType = Calendar.DAY_OF_MONTH;
        int num = 1;
        if (step_size.endsWith("s")) {
            dateType = Calendar.SECOND;
            num = Integer.parseInt(step_size.split("s")[0]);
        }
        if (step_size.endsWith("m")) {
            dateType = Calendar.MINUTE;
            num = Integer.parseInt(step_size.split("m")[0]);
        }
        if (step_size.endsWith("h")) {
            dateType = Calendar.HOUR;
            num = Integer.parseInt(step_size.split("h")[0]);
        }
        if (step_size.endsWith("d")) {
            dateType = Calendar.DAY_OF_MONTH;
            num = Integer.parseInt(step_size.split("d")[0]);
        }

        if (quartzJobInfo.getLast_time() == null || quartzJobInfo.getLast_time().getTime()/1000 < quartzJobInfo.getStart_time().getTime()/1000) {
            quartzJobInfo.setLast_time(quartzJobInfo.getStart_time());
            cur = quartzJobInfo.getStart_time();
        } else {
            //此处不适用月因时间无法正常确定,调度中时间按照秒做单位
            cur = DateUtil.add(quartzJobInfo.getLast_time(), dateType, num);
            quartzJobInfo.setLast_time(cur);
        }

        //判断自定义时间是否超过,超过删除删除调度
        Timestamp second = DateUtil.add(quartzJobInfo.getLast_time(), dateType, num);
        if (cur.getTime()/1000 > quartzJobInfo.getEnd_time().getTime()/1000 || second.getTime()/1000 > quartzJobInfo.getEnd_time().getTime()/1000) {
            quartzJobInfo.setStatus(JobStatus.FINISH.getValue());
            quartzManager2.deleteTask(quartzJobInfo, JobStatus.FINISH.getValue());
        }

        return cur;


    }


    /**
     * 任务触发后,等待依赖任务完成触发,其他外部均调用此方法执行子任务实例
     *
     * @param tli
     */
    public static void chooseJobBean(TaskLogInstance tli) {
        QuartzJobMapper qjm = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");

        CountDownLatch latch = new CountDownLatch(1);
        try{
            //线程池执行具体调度任务
            ZdhRunableTask zdhRunableTask = new ZdhRunableTask("etl", new Runnable() {
                @Override
                public void run() {
                    try {
                        latch.await();
                        MDC.put(Const.MDC_LOG_ID, UUID.randomUUID().toString());
                        run_sub_task_log_instance(tli.getJob_type(), tli);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            Future<?> submit = threadPoolExecutor.submit(zdhRunableTask);

            JobCommon2.chm.put(String.valueOf(tli.getId()), submit);
        }catch (Exception e){

        }finally {
            latch.countDown();
        }

    }


    /**
     * 公共执行子任务入口,只能由chooseJobBean 调用
     *
     * @param jobType
     * @param tli
     */
    private static void run_sub_task_log_instance(String jobType, TaskLogInstance tli) {
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        EtlTaskLogMapper etlm = (EtlTaskLogMapper) SpringContext.getBean("etlTaskLogMapper");
        LogUtil.info(JobCommon2.class, "开始执行[" + jobType + "] JOB");
        insertLog(tli, "INFO", "开始执行[" + jobType + "] JOB");

        Thread td = Thread.currentThread();
        JobCommon2.chm_thread.put(String.valueOf(tli.getId()), td);
        long threadId = td.getId();
        LogUtil.info(JobCommon2.class, "线程id:" + threadId);
        insertLog(tli, "INFO", "[" + jobType + "] JOB, 线程ID: "+threadId);
        String tk = SystemCommandLineRunner.myid + "_" + threadId + "_" + tli.getId();

        tlim.updateThreadById(tk, tli.getId());
        tli.setThread_id(tk);
        try {
            boolean end = isCount(jobType, tli);
            if (end == true) {
                return;
            }
            Boolean exe_status = true;
            //判断是否禁用任务类型
            if(ConfigUtil.isInValue(ConfigUtil.ZDH_DISENABLE_JOB_TYPE, jobType)  || ConfigUtil.isInValue(ConfigUtil.ZDH_DISENABLE_MORE_TASK, tli.getMore_task())){
                LogUtil.error(JobCommon2.class, "[" + jobType + "] JOB, 任务被禁用,具体可咨询系统管理员");
                insertLog(tli, "ERROR", "[" + jobType + "] JOB,任务被禁用,具体可咨询系统管理员");
                tli.setStatus(JobStatus.ERROR.getValue());
                tli.setProcess(ProcessEnum.FINISH);
                updateTaskLog(tli, tlim);
                return ;
            }
            if (jobType.equalsIgnoreCase(JobType.ETL.getCode())) {
                //拼接任务信息发送请求
                if (tli.getJob_model().equals(JobModel.TIME_SEQ.getValue())) {
                    runTimeSeq(jobType, tli.getId(), exe_status, tli);
                } else if (tli.getJob_model().equals(JobModel.ONCE.getValue())) {
                    runOnce(jobType, tli.getId(), exe_status, tli);
                } else if (tli.getJob_model().equals(JobModel.REPEAT.getValue())) {
                    runRepeat(jobType, tli.getId(), exe_status, tli);
                }
                //insertLog(tli, "INFO", "此处需要做修改,是web和server的交互处,必须保证web先更新,server后更新,目前不能保证");
                //更新end_time
                tlim.updateEndTimeById(tli.getEnd_time(), tli.getId());
                //此处更新如果时间晚于server更新,则会覆盖server处数据,改为只更新end_time
//              tli.setProcess_time("");
//              updateTaskLog(tli,tlim);
            } else if (jobType.equalsIgnoreCase(JobType.SHELL.getCode())) {
                tli.setStatus(JobStatus.ETL.getValue());
                tli.setProcess(ProcessEnum.EXEC_SCRIPT);
                updateTaskLog(tli, tlim);
                exe_status = ShellJob.shellCommand(tli);
                if (exe_status) {
                    //设置任务状态为finish
                    tli.setStatus(JobStatus.FINISH.getValue());
                    tli.setProcess(ProcessEnum.FINISH);
                    updateTaskLog(tli, tlim);
                }
            }else if (jobType.equalsIgnoreCase(JobType.HTTP.getCode())) {
                tli.setStatus(JobStatus.ETL.getValue());
                updateTaskLog(tli, tlim);
                exe_status = HttpJob.httpCommand(tli);
                if (exe_status) {
                    //设置任务状态为finish
                    tli.setStatus(JobStatus.FINISH.getValue());
                    tli.setProcess(ProcessEnum.FINISH);
                    updateTaskLog(tli, tlim);
                }
            }else if (jobType.equalsIgnoreCase(JobType.EMAIL.getCode())) {
                tli.setStatus(JobStatus.ETL.getValue());
                updateTaskLog(tli, tlim);
                exe_status = EmailJob.run(tli);
                if (exe_status) {
                    //设置任务状态为finish
                    tli.setStatus(JobStatus.FINISH.getValue());
                    tli.setProcess(ProcessEnum.FINISH);
                    updateTaskLog(tli, tlim);
                }
            } else if (jobType.equalsIgnoreCase(JobType.FLUME.getCode())) {
                tli.setStatus(JobStatus.ETL.getValue());
                //更新内容
                EtlTaskLogInfo etlTaskLogInfo=etlm.selectByPrimaryKey(tli.getEtl_task_id());
                //调用jinJava构造内容
                Map<String, Object> param = getJinJavaParam(tli);

                Jinjava jinjava=new Jinjava();
                String job_config = jinjava.render(etlTaskLogInfo.getJob_config(), param);
                //todo 此处增加自定义channel配置

                String flume_path = jinjava.render(etlTaskLogInfo.getFlume_path(), param);
                etlTaskLogInfo.setJob_config(job_config);
                etlTaskLogInfo.setFlume_path(flume_path);
                tli.setEtl_info(JsonUtil.formatJsonString(etlTaskLogInfo));

                updateTaskLog(tli, tlim);
                exe_status = FlumeJob.flumeCommand(tli);
                if (exe_status) {
                    //设置任务状态为finish
                    tli.setStatus(JobStatus.FINISH.getValue());
                    tli.setProcess(ProcessEnum.FINISH);
                    updateTaskLog(tli, tlim);
                }
            }
        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
        } finally {
            JobCommon2.chm.remove(tli.getId());
            JobCommon2.chm_thread.remove(tli.getId());
            JobCommon2.chm_process.remove(tli.getId());
        }

    }


    public static List<Date> resolveQuartzExpr(String expr) throws ParseException {

        List<Date> dates = new ArrayList<>();

        if (expr.endsWith("s") || expr.endsWith("m") || expr.endsWith("h") || expr.endsWith("d")) {
            long time = Integer.valueOf(expr.substring(0, expr.length() - 1));

            int dateType = Calendar.DAY_OF_MONTH;
            int num = 1;
            if (expr.endsWith("s")) {
                dateType = Calendar.SECOND;
                num = Integer.parseInt(expr.split("s")[0]);
            }
            if (expr.endsWith("m")) {
                dateType = Calendar.MINUTE;
                num = Integer.parseInt(expr.split("m")[0]);
            }
            if (expr.endsWith("h")) {
                dateType = Calendar.HOUR;
                num = Integer.parseInt(expr.split("h")[0]);
            }
            if (expr.endsWith("d")) {
                dateType = Calendar.DAY_OF_MONTH;
                num = Integer.parseInt(expr.split("d")[0]);
            }

            for (int i = 1; i <= 10; i++) {
                dates.add(DateUtil.add(new Timestamp(System.currentTimeMillis()), dateType, num * i));
            }

        } else {
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
            cronTriggerImpl.setCronExpression(expr);//这里写要准备猜测的cron表达式
            dates = TriggerUtils.computeFireTimes(cronTriggerImpl, null, 10);
        }

        return dates;

    }

    public static List<Date> resolveQuartzExpr(String use_quartz_time, String step_size, String expr, String start_time, String end_time) throws ParseException {

        List<Date> dates1 = new ArrayList<>();
        List<Date> dates = new ArrayList<>();
        //对于自定义时间
        if (expr.endsWith("s") || expr.endsWith("m") || expr.endsWith("h") || expr.endsWith("d")) {
            long time = Integer.valueOf(expr.substring(0, expr.length() - 1));

            int dateType = Calendar.DAY_OF_MONTH;
            int num = 1;
            if (expr.endsWith("s")) {
                dateType = Calendar.SECOND;
                num = Integer.parseInt(expr.split("s")[0]);
            }
            if (expr.endsWith("m")) {
                dateType = Calendar.MINUTE;
                num = Integer.parseInt(expr.split("m")[0]);
            }
            if (expr.endsWith("h")) {
                dateType = Calendar.HOUR;
                num = Integer.parseInt(expr.split("h")[0]);
            }
            if (expr.endsWith("d")) {
                dateType = Calendar.DAY_OF_MONTH;
                num = Integer.parseInt(expr.split("d")[0]);
            }

            boolean if_end = true;
            Timestamp st = new Timestamp(DateUtil.pase(start_time, "yyyy-MM-dd HH:mm:ss").getTime());
            Timestamp end = new Timestamp(DateUtil.pase(end_time, "yyyy-MM-dd HH:mm:ss").getTime());
            dates.add(st);
            while (if_end) {
                st = DateUtil.add(st, dateType, num);
                if (st.getTime() <= end.getTime()) {
                    dates.add(st);
                } else {
                    if_end = false;
                }
            }
        } else {
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
            cronTriggerImpl.setCronExpression(expr);//这里写要准备猜测的cron表达式
            dates = TriggerUtils.computeFireTimesBetween(cronTriggerImpl, null, DateUtil.pase(start_time, "yyyy-MM-dd HH:mm:ss"), DateUtil.pase(end_time, "yyyy-MM-dd HH:mm:ss"));
        }

        if (use_quartz_time.equalsIgnoreCase("on")) {
            return dates;
        }


        int num = 0;
        int dateType = Calendar.DAY_OF_MONTH;
        if (step_size.endsWith("s")) {
            dateType = Calendar.SECOND;
            num = Integer.parseInt(step_size.split("s")[0]);
        }
        if (step_size.endsWith("m")) {
            dateType = Calendar.MINUTE;
            num = Integer.parseInt(step_size.split("m")[0]);
        }
        if (step_size.endsWith("h")) {
            dateType = Calendar.HOUR;
            num = Integer.parseInt(step_size.split("h")[0]);
        }
        if (step_size.endsWith("d")) {
            dateType = Calendar.DAY_OF_MONTH;
            num = Integer.parseInt(step_size.split("d")[0]);
        }

        Timestamp tmp = Timestamp.valueOf(start_time);
        dates1.add(tmp);
        for (int i = 0; i < dates.size(); i++) {
            tmp = DateUtil.add(tmp, dateType, num);
            if (tmp.getTime() <= Timestamp.valueOf(end_time).getTime()) {
                dates1.add(tmp);
            } else {
                break;
            }
        }

        return dates1;

    }

    /**
     * 使用quartz 触发时间时,获取指定时间差的etl_date
     *
     * @param cur
     * @param step_size
     * @return
     */
    public static Timestamp getEtlDate(Timestamp cur, String step_size) {
        int dateType = Calendar.SECOND;
        int num = 0;
        if (step_size.endsWith("s")) {
            dateType = Calendar.SECOND;
            num = Integer.parseInt(step_size.split("s")[0]);
        }
        if (step_size.endsWith("m")) {
            dateType = Calendar.MINUTE;
            num = Integer.parseInt(step_size.split("m")[0]);
        }
        if (step_size.endsWith("h")) {
            dateType = Calendar.HOUR;
            num = Integer.parseInt(step_size.split("h")[0]);
        }
        if (step_size.endsWith("d")) {
            dateType = Calendar.DAY_OF_MONTH;
            num = Integer.parseInt(step_size.split("d")[0]);
        }

        return DateUtil.add(cur, dateType, num);
    }

    /**
     * 根据tli 任务组生成具体的子任务实例
     *
     * @param tgli
     * @param sub_tasks 具体执行的子任务divId
     */
    public static List<TaskLogInstance> sub_task_log_instance(TaskGroupLogInstance tgli, String[] sub_tasks) {
        List<TaskLogInstance> tliList = new ArrayList<>();
        try {
            TaskGroupLogInstanceMapper tglim = (TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
            TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
            // divId和实例id映射
            Map<String, String> map = new HashMap<>(); //divId->id
            Map<String, String> map2 = new HashMap<>();//id->divId
            DAG dag = new DAG();
            if (StringUtils.isEmpty(tgli.getJsmind_data())) {
                insertLog(tgli, "ERROR", "无法生成子任务,请检查是否配置有子任务信息!");
                tglim.updateStatusById3(JobStatus.ERROR.getValue(), "100", DateUtil.getCurrentTime(), tgli.getId());
                return tliList;
            }
            List<Map<String, Object>> tasks = (List<Map<String, Object>>)JsonUtil.toJavaMap(tgli.getJsmind_data()).getOrDefault("tasks", Lists.newArrayList());
            List<Map<String, Object>> lines =  (List<Map<String, Object>>)JsonUtil.toJavaMap(tgli.getJsmind_data()).getOrDefault("line", Lists.newArrayList());
            for (Map<String, Object> job : tasks) {
                TaskLogInstance taskLogInstance = new TaskLogInstance();
                taskLogInstance = MapStructMapper.INSTANCE.taskGroupLogInstanceToTaskLogInstance(tgli);

                String etl_task_id = ((Map<String, Object>) job).getOrDefault("etl_task_id", "").toString();//具体任务id
                String pageSourceId = ((Map<String, Object>) job).getOrDefault("divId", "").toString();//前端生成的div 标识
                String more_task = ((Map<String, Object>) job).getOrDefault("more_task", "").toString();
                String is_disenable = ((Map<String, Object>) job).getOrDefault("is_disenable", "").toString();
                String depend_level = ((Map<String, Object>) job).getOrDefault("depend_level", "").toString();
                String schedule_id = ((Map<String, Object>) job).getOrDefault("schedule_id", "").toString();
                if(StringUtils.isEmpty(schedule_id)){
                    schedule_id = "";
                }
                String time_out = ((Map<String, Object>) job).getOrDefault("time_out", "").toString();
                if (!org.apache.commons.lang3.StringUtils.isEmpty(time_out)) {
                    taskLogInstance.setTime_out(time_out);
                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(is_disenable)) {
                    is_disenable = "false";
                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(depend_level)) {
                    depend_level = "0";
                }

                String etl_context = ((Map<String, Object>) job).getOrDefault("etl_context", "").toString();
                String command = ((Map<String, Object>) job).getOrDefault("command", "").toString();//具体任务id
                String is_script = ((Map<String, Object>) job).getOrDefault("is_script", "").toString();//是否脚本方式执行
                String async = ((Map<String, Object>) job).getOrDefault("async", "").toString();//同步/异步
                taskLogInstance.setRun_time(new Timestamp(System.currentTimeMillis()));

                String task_type = ((Map<String, Object>) job).getOrDefault("type", "").toString();
                if (task_type.equalsIgnoreCase("tasks")) {
                    taskLogInstance.setMore_task(more_task);
                    taskLogInstance.setJob_type(JobType.ETL.getCode());
                    String zdh_instance = ((Map<String, Object>) job).getOrDefault("zdh_instance", "").toString();
                    if (!org.apache.commons.lang3.StringUtils.isEmpty(zdh_instance) && org.apache.commons.lang3.StringUtils.isEmpty(taskLogInstance.getParams())) {
                        Map<String, Object> jsonObject = JsonUtil.createEmptyMap();
                        jsonObject.put("zdh_instance", zdh_instance);
                        taskLogInstance.setParams(JsonUtil.formatJsonString(jsonObject));
                    }

                    if (!org.apache.commons.lang3.StringUtils.isEmpty(zdh_instance) && !org.apache.commons.lang3.StringUtils.isEmpty(taskLogInstance.getParams())) {
                        Map<String, Object> jsonObject = JsonUtil.toJavaMap(taskLogInstance.getParams());
                        jsonObject.put("zdh_instance", zdh_instance);
                        taskLogInstance.setParams(JsonUtil.formatJsonString(jsonObject));
                    }

                }

                taskLogInstance.setJsmind_data("");
                taskLogInstance.setRun_jsmind_data("");

                if (task_type.equalsIgnoreCase(JobType.SHELL.getCode())) {
                    taskLogInstance.setMore_task("");
                    taskLogInstance.setJob_type(JobType.SHELL.getCode());
                }
                if (task_type.equalsIgnoreCase(JobType.GROUP.getCode())) {
                    taskLogInstance.setMore_task("");
                    taskLogInstance.setJob_type(JobType.GROUP.getCode());
                }
                if (task_type.equalsIgnoreCase(JobType.EMAIL.getCode())) {
                    taskLogInstance.setMore_task("");
                    taskLogInstance.setJob_type(JobType.EMAIL.getCode());
                    taskLogInstance.setJsmind_data(JsonUtil.formatJsonString(job));
                    taskLogInstance.setRun_jsmind_data(JsonUtil.formatJsonString(job));
                }
                if (task_type.equalsIgnoreCase(JobType.HTTP.getCode())) {
                    taskLogInstance.setMore_task("");
                    taskLogInstance.setJob_type(JobType.HTTP.getCode());
                    taskLogInstance.setJsmind_data(JsonUtil.formatJsonString(job));
                    taskLogInstance.setRun_jsmind_data(JsonUtil.formatJsonString(job));
                }
                if (task_type.equalsIgnoreCase(JobType.FLUME.getCode())) {
                    taskLogInstance.setMore_task("");
                    taskLogInstance.setJob_type(JobType.FLUME.getCode());
                    taskLogInstance.setJsmind_data(JsonUtil.formatJsonString(job));
                    taskLogInstance.setRun_jsmind_data(JsonUtil.formatJsonString(job));

                }


                if (task_type.equalsIgnoreCase(JobType.JDBC.getCode())) {
                    taskLogInstance.setMore_task("");
                    taskLogInstance.setJob_type(JobType.JDBC.getCode());
                    taskLogInstance.setJsmind_data(JsonUtil.formatJsonString(job));
                    taskLogInstance.setRun_jsmind_data(JsonUtil.formatJsonString(job));
                }

                if (task_type.equalsIgnoreCase(JobType.HDFS.getCode())) {
                    taskLogInstance.setMore_task("");
                    taskLogInstance.setJob_type(JobType.HDFS.getCode());
                    taskLogInstance.setJsmind_data(JsonUtil.formatJsonString(job));
                    taskLogInstance.setRun_jsmind_data(JsonUtil.formatJsonString(job));

                }


                String t_id = SnowflakeIdWorker.getInstance().nextId() + "";
                map.put(pageSourceId, t_id);//div标识和任务实例id 对应关系
                map2.put(t_id, pageSourceId);

                taskLogInstance.setId(t_id);//具体执行任务实例id,每次执行都会重新生成
                taskLogInstance.setJob_id(tgli.getJob_id());//调度任务id
                taskLogInstance.setJob_context(etl_context);//调度任务说明
                taskLogInstance.setEtl_task_id(etl_task_id);//etl任务id
                taskLogInstance.setEtl_context(etl_context);//etl任务说明
                taskLogInstance.setCommand(command);
                taskLogInstance.setIs_script(is_script);

                taskLogInstance.setStatus(JobStatus.NON.getValue());

                //设置调度器
                taskLogInstance.setSchedule_id(schedule_id);

                taskLogInstance.setCount(0);
                taskLogInstance.setOwner(tgli.getOwner());
                taskLogInstance.setIs_disenable(is_disenable);
                taskLogInstance.setDepend_level(depend_level);
                tliList.add(taskLogInstance);
            }

            List<Map<String, Object>> jary_line = JsonUtil.createEmptyListMap();
            for (Map<String, Object> job : lines) {
                String pageSourceId = ( job).getOrDefault("pageSourceId", "").toString();
                String pageTargetId = ( job).getOrDefault("pageTargetId", "").toString();
                ( job).put("id", map.get(pageSourceId));
                ( job).put("parentid", map.get(pageTargetId));
                if (pageSourceId != null && !pageSourceId.equalsIgnoreCase("root")) {
                    boolean is_loop = dag.addEdge(map.get(pageSourceId), map.get(pageTargetId));//此处的依赖关系 都是生成的任务实例id
                    if (!is_loop) {
                        insertLog(tgli, "ERROR", "无法生成子任务,请检查子任务是否存在相互依赖的任务!");
                        tglim.updateStatusById3(JobStatus.ERROR.getValue(), "100", DateUtil.getCurrentTime(), tgli.getId());
                        return tliList;
                    }

                    Map<String, Object> json_line = JsonUtil.createEmptyMap();
                    json_line.put("from", map.get(pageSourceId));
                    json_line.put("to", map.get(pageTargetId));
                    jary_line.add(json_line);
                }
            }

            //run_data 结构：run_data:[{task_log_instance_id,etl_task_id,etl_context,more_task}]
            List<Map<String, Object>> jary = JsonUtil.createEmptyListMap();
            for (TaskLogInstance tli : tliList) {
                Map<String, Object> jsonObject1 = JsonUtil.createEmptyMap();
                String tid = tli.getId();

                jsonObject1.put("task_log_instance_id", tid);
                jsonObject1.put("etl_task_id", tli.getEtl_task_id());
                jsonObject1.put("etl_context", tli.getEtl_context());
                jsonObject1.put("more_task", tli.getMore_task());
                jsonObject1.put("job_type", tli.getJob_type());
                jsonObject1.put("divId", map2.get(tid));
                jary.add(jsonObject1);
            }

            Map<String, Object> jsonObject = JsonUtil.toJavaMap(tgli.getJsmind_data());
            jsonObject.put("run_data", jary);
            jsonObject.put("run_line", jary_line);
            tgli.setRun_jsmind_data(JsonUtil.formatJsonString(jsonObject));
            tgli.setProcess("6.5");
            tglim.updateByPrimaryKeySelective(tgli);
            //debugInfo(tgli);


            //生成实例
            for (TaskLogInstance tli : tliList) {
                String tid = tli.getId();
                String next_tasks = StringUtils.join(dag.getChildren(tid).toArray(), ",");
                String pre_tasks = StringUtils.join(dag.getParent(tid), ",");
                tli.setGroup_id(tgli.getId());
                tli.setGroup_context(tgli.getJob_context());
                tli.setNext_tasks(next_tasks);
                tli.setPre_tasks(pre_tasks);
                tli.setSchedule_source(tgli.getSchedule_source());
                if (sub_tasks == null) {
                    tli.setStatus(JobStatus.NON.getValue());
                } else {
                    //不再sub_tasks 中的div 状态设置为跳过状态,并且非禁用状态
                    if (Arrays.asList(sub_tasks).contains(map2.get(tid)) && !tli.getIs_disenable().equalsIgnoreCase("true")) {
                        //设置为初始态
                        tli.setStatus(JobStatus.NON.getValue());
                    } else {
                        tli.setStatus(JobStatus.SKIP.getValue());
                        tli.setProcess(ProcessEnum.FINISH);
                    }
                }

                tlim.insertSelective(tli);
                // debugInfo(tli);
            }
        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            JobCommon2.insertLog(tgli, "ERROR", "生成子任务失败," + e.getMessage());
        }
        return tliList;
    }


    /**
     * 检查当前运行线程是否超限
     *
     * @param tli
     * @return true:超限,false:未超限
     */
    public static boolean check_thread_limit(TaskLogInstance tli) {
        try {
            //检查ssh任务并行限制
            TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");

            if (ConfigUtil.getParamUtil().exists(ConfigUtil.getProductCode(), Const.ZDH_SSH_MAX_THREAD)) {
                TaskLogInstance t = new TaskLogInstance();
                t.setJob_type("SHELL");
                t.setStatus(JobStatus.ETL.getValue());
                List<TaskLogInstance> tlis = tlim.selectByTaskLogInstance(t);
                int max_thread = Integer.parseInt(ConfigUtil.getParamUtil().getValue(ConfigUtil.getProductCode(),Const.ZDH_SSH_MAX_THREAD).toString());
                if (tlis.size() > max_thread) {
                    String msg = String.format("当前系统中SSH任务超过阀值: %d,跳过本次执行,等待下次检查....", max_thread);
                    JobCommon2.insertLog(tli, "INFO", msg);
                    LogUtil.info(JobCommon2.class, msg);
                    return true;
                }
            }
        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e);
            JobCommon2.insertLog(tli, "ERROR", e.getMessage());
        }
        return false;
    }

    /**
     * 检查当前运行spark任务是否超限
     * todo 对于非spark 数据采集任务,当前未实现-后续可优化
     * @param tli
     * @return true:超限,false:未超限
     */
    public static boolean check_spark_limit(TaskLogInstance tli){

        try {
            //检查ssh任务并行限制
            TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
            int max=10;
            String zdh_instance="";
            String redis_key = "zdh_spark_max";
            if(!StringUtils.isEmpty(tli.getParams())){
                Map<String, Object> jsonObject=JsonUtil.toJavaMap(tli.getParams());
                if(jsonObject.containsKey("zdh_instance")){
                    zdh_instance=jsonObject.getOrDefault("zdh_instance", "").toString();
                }
            }
            if(!StringUtils.isEmpty(zdh_instance)){
                redis_key=redis_key+"_"+zdh_instance;
            }
            TaskLogInstance t = new TaskLogInstance();
            t.setJob_type(JobType.ETL.getCode());
            t.setStatus(JobStatus.ETL.getValue());
            List<TaskLogInstance> tlis = tlim.selectByTaskLogInstance(t);
            if (ConfigUtil.getParamUtil().exists(ConfigUtil.getProductCode(), redis_key)) {
                max = Integer.parseInt(ConfigUtil.getParamUtil().getValue(ConfigUtil.getProductCode(), redis_key).toString());
                if (tlis.size() > max) {
                    String msg = String.format("检查Spark集群: %s,运行任务量: %d, 超过阀值: %d,跳过本次执行,等待下次检查....",zdh_instance,tlis.size(), max);
                    JobCommon2.insertLog(tli, "INFO", msg);
                    LogUtil.info(JobCommon2.class, msg);
                    return true;
                }
            }else{
                //使用默认10个任务并发
                if (tlis.size() > max) {
                    String msg = String.format("检查Spark集群: %s,运行任务量: %d, 超过阀值: %d,跳过本次执行,等待下次检查....",zdh_instance,tlis.size(), max);
                    JobCommon2.insertLog(tli, "INFO", msg);
                    LogUtil.info(JobCommon2.class, msg);
                    return true;
                }
            }
            String msg = String.format("检查Spark集群: %s,运行任务量: %d, 未超过阀值: %d",zdh_instance,tlis.size(), max);
            JobCommon2.insertLog(tli, "INFO", msg);
        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e.getMessage(), e);
            JobCommon2.insertLog(tli, "ERROR", e.getMessage());
        }

        return false;
    }


    /**
     * 检查当前线程池队列是否超限
     *
     * @param tli
     * @return true:超限,false:未超限
     */
    public static boolean check_treadpool_limit(TaskLogInstance tli){

        try {
            //检查检查当前线程池队列是否超限

            if (ConfigUtil.getParamUtil().exists(ConfigUtil.getProductCode(), Const.ZDH_THREADPOOL_MAX)) {
                if(!NumberUtil.isInteger(ConfigUtil.getParamUtil().getValue(ConfigUtil.getProductCode(), Const.ZDH_THREADPOOL_MAX).toString())){
                    return false;
                }
                int max_thread = Integer.parseInt(ConfigUtil.getParamUtil().getValue(ConfigUtil.getProductCode(), Const.ZDH_THREADPOOL_MAX).toString());
                int current_thread_num = threadPoolExecutor.getQueue().size();
                if(current_thread_num>=max_thread){
                    String msg = String.format("检查当前机器: %s, 线程池超过阈值, 运行任务量: %d, 超过阀值: %d,跳过本次执行,等待下次检查....", SystemCommandLineRunner.web_application_id,current_thread_num, max_thread);
                    JobCommon2.insertLog(tli, "INFO", msg);
                    return true;
                }
            }
        } catch (Exception e) {
            LogUtil.error(JobCommon2.class, e.getMessage(), e);
        }

        return false;
    }
}
