package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hubspot.jinjava.Jinjava;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.EtlTaskService;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.service.impl.DataSourcesServiceImpl;
import com.zyc.zdh.util.*;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

public class JobCommon2 {

    public static Logger logger = LoggerFactory.getLogger(JobCommon2.class);

    public static String myid = "";

    public static String web_application_id = "";

    public static LinkedBlockingDeque<ZdhLogs> linkedBlockingDeque = new LinkedBlockingDeque<ZdhLogs>();

    public static ConcurrentHashMap<String, Thread> chm = new ConcurrentHashMap<String, Thread>();

    public static DelayQueue<RetryJobInfo> retryQueue = new DelayQueue<>();

    public static ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(1, 1000,  500, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    public static void logThread(ZdhLogsService zdhLogsService) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        ZdhLogs log = JobCommon2.linkedBlockingDeque.take();
                        if (log != null) {
                            zdhLogsService.insert(log);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    /**
     * 超过次数限制会主动杀掉调度,并设置状态为error
     *
     * @param jobType SHELL,JDBC,FTP,CLASS
     * @param tli     return true 表示超过次数限制
     */
    public static boolean isCount(String jobType, TaskLogInstance tli) {
        logger.info("[" + jobType + "] JOB ,开始检查任务次数限制");
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
        tli.setProcess("8");

        if (tli.getPlan_count().trim().equals("-1")) {
            logger.info("[" + jobType + "] JOB ,当前任务未设置执行次数限制");
            insertLog(tli, "info", "[" + jobType + "] JOB ,当前任务未设置执行次数限制");
        }

        if (!tli.getPlan_count().trim().equals("") && !tli.getPlan_count().trim().equals("-1")) {
            //任务有次数限制,重试多次后仍失败会删除任务
            System.out.println(tli.getCount() + "================" + tli.getPlan_count().trim());
            if (tli.getCount() > Long.parseLong(tli.getPlan_count().trim())) {
                logger.info("[" + jobType + "] JOB 检任务次测到重试数超过限制,删除任务并直接返回结束");
                insertLog(tli, "info", "[" + jobType + "] JOB 检任务次测到重试数超过限制,删除任务并直接返回结束");
                QuartzJobInfo qji = new QuartzJobInfo();
                qji.setJob_id(tli.getJob_id());
                qji.setEtl_task_id(tli.getEtl_task_id());
                quartzManager2.deleteTask(tli, "finish", "error");
                quartzJobMapper.updateLastStatus(tli.getJob_id(), "error");

                insertLog(tli, "info", "[" + jobType + "] JOB ,结束调度任务");
                tli.setStatus(InstanceStatus.ERROR.getValue());
                updateTaskLog(tli, taskLogInstanceMapper);
                return true;
            }
        }

        updateTaskLog(tli, taskLogInstanceMapper);
        return false;
    }

    public static void debugInfo(Object obj) {
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
                    logger.info("传入的对象中包含一个如下的变量：" + varName + " = " + o);
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


    public static ZdhInfo create_zhdInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                         EtlTaskService etlTaskService, DataSourcesServiceImpl dataSourcesServiceImpl, ZdhNginxMapper zdhNginxMapper, EtlMoreTaskMapper etlMoreTaskMapper) throws Exception {

        JSONObject json = new JSONObject();
        String date = DateUtil.formatTime(tli.getCur_time());
        json.put("ETL_DATE", date);
        logger.info(" JOB ,单源,处理当前日期,传递参数ETL_DATE 为" + date);
        tli.setParams(json.toJSONString());

        String etl_task_id = tli.getEtl_task_id();
        //获取etl 任务信息
        EtlTaskInfo etlTaskInfo = etlTaskService.selectById(etl_task_id);
        if (etlTaskInfo == null) {
            logger.info("无法找到对应的ETL任务,任务id:" + etl_task_id);
            throw new Exception("无法找到对应的ETL任务,任务id:" + etl_task_id);
        }

        Map<String, Object> map = (Map<String, Object>) JSON.parseObject(tli.getParams());
        //此处做参数匹配转换
        if (map != null) {
            logger.info("单源,自定义参数不为空,开始替换:" + tli.getParams());
            //System.out.println("自定义参数不为空,开始替换:" + dti.getParams());
            DynamicParams(map, tli, etlTaskInfo, null, null, null);
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

        if (dataSourcesInfoInput.getData_source_type().equals("外部上传")) {
            //获取文件服务器信息 配置到数据源选项
            ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(dataSourcesInfoInput.getOwner());
            if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                dataSourcesInfoInput.setUrl(zdhNginx.getHost() + ":" + zdhNginx.getPort());
                dataSourcesInfoInput.setUsername(zdhNginx.getUsername());
                dataSourcesInfoInput.setPassword(zdhNginx.getPassword());
            }
        }

        if (dataSourcesInfoOutput.getData_source_type().equals("外部下载")) {
            //获取文件服务器信息 配置到数据源选项
            ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(dataSourcesInfoOutput.getOwner());
            if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                dataSourcesInfoOutput.setUrl(zdhNginx.getHost() + ":" + zdhNginx.getPort());
                dataSourcesInfoOutput.setUsername(zdhNginx.getUsername());
                dataSourcesInfoOutput.setPassword(zdhNginx.getPassword());
                if (etlTaskInfo.getData_sources_params_output() != null && !etlTaskInfo.getData_sources_params_output().trim().equals("")) {
                    JSONObject jsonObject = JSON.parseObject(etlTaskInfo.getData_sources_params_output());
                    jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + getUser().getId());
                    etlTaskInfo.setData_sources_params_output(JSON.toJSONString(jsonObject));
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + getUser().getId());
                    etlTaskInfo.setData_sources_params_output(JSON.toJSONString(jsonObject));
                }
            } else {
                if (etlTaskInfo.getData_sources_params_output() != null && !etlTaskInfo.getData_sources_params_output().trim().equals("")) {
                    JSONObject jsonObject = JSON.parseObject(etlTaskInfo.getData_sources_params_output());
                    jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + getUser().getId());
                    etlTaskInfo.setData_sources_params_output(JSON.toJSONString(jsonObject));
                } else {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + getUser().getId());
                    etlTaskInfo.setData_sources_params_output(JSON.toJSONString(jsonObject));
                }
            }
        }

        ZdhInfo zdhInfo = new ZdhInfo();
        zdhInfo.setZdhInfo(dataSourcesInfoInput, etlTaskInfo, dataSourcesInfoOutput, tli);

        return zdhInfo;

    }

    public static ZdhMoreInfo create_more_task_zdhInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                                       EtlTaskService etlTaskService, DataSourcesServiceImpl dataSourcesServiceImpl, ZdhNginxMapper zdhNginxMapper, EtlMoreTaskMapper etlMoreTaskMapper) {
        try {
            JSONObject json = new JSONObject();
            String date = DateUtil.formatTime(tli.getCur_time());
            json.put("ETL_DATE", date);
            logger.info(" JOB ,多源,处理当前日期,传递参数ETL_DATE 为" + date);
            tli.setParams(json.toJSONString());

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
            DataSourcesInfo dataSourcesInfoOutput = dataSourcesServiceImpl.selectById(data_sources_choose_output);

            if (dataSourcesInfoOutput.getData_source_type().equals("外部下载")) {
                //获取文件服务器信息 配置到数据源选项
                ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(dataSourcesInfoOutput.getOwner());
                if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                    dataSourcesInfoOutput.setUrl(zdhNginx.getHost() + ":" + zdhNginx.getPort());
                    dataSourcesInfoOutput.setUsername(zdhNginx.getUsername());
                    dataSourcesInfoOutput.setPassword(zdhNginx.getPassword());
                    if (etlMoreTaskInfo.getData_sources_params_output() != null && !etlMoreTaskInfo.getData_sources_params_output().trim().equals("")) {
                        JSONObject jsonObject = JSON.parseObject(etlMoreTaskInfo.getData_sources_params_output());
                        jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + getUser().getId());
                        etlMoreTaskInfo.setData_sources_params_output(JSON.toJSONString(jsonObject));
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + getUser().getId());
                        etlMoreTaskInfo.setData_sources_params_output(JSON.toJSONString(jsonObject));
                    }
                } else {
                    if (etlMoreTaskInfo.getData_sources_params_output() != null && !etlMoreTaskInfo.getData_sources_params_output().trim().equals("")) {
                        JSONObject jsonObject = JSON.parseObject(etlMoreTaskInfo.getData_sources_params_output());
                        jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + getUser().getId());
                        etlMoreTaskInfo.setData_sources_params_output(JSON.toJSONString(jsonObject));
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + getUser().getId());
                        etlMoreTaskInfo.setData_sources_params_output(JSON.toJSONString(jsonObject));
                    }
                }
            }


            Map<String, Object> map = (Map<String, Object>) JSON.parseObject(tli.getParams());
            //此处做参数匹配转换
            for (EtlTaskInfo etlTaskInfo : etlTaskInfos) {
                if (map != null) {
                    logger.info("多源,自定义参数不为空,开始替换:" + tli.getParams());
                    DynamicParams(map, tli, etlTaskInfo, null, null, null);
                }

                //获取数据源信息
                String data_sources_choose_input = etlTaskInfo.getData_sources_choose_input();
                DataSourcesInfo dataSourcesInfoInput = dataSourcesServiceImpl.selectById(data_sources_choose_input);
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
            e.printStackTrace();
            throw e;
        }
    }

    public static ZdhSqlInfo create_zhdSqlInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                               SqlTaskMapper sqlTaskMapper, DataSourcesServiceImpl dataSourcesServiceImpl, ZdhNginxMapper zdhNginxMapper) {

        try {
            JSONObject json = new JSONObject();
            String date = DateUtil.formatTime(tli.getCur_time());
            json.put("ETL_DATE", date);
            logger.info(" JOB ,SQL,处理当前日期,传递参数ETL_DATE 为" + date);
            tli.setParams(json.toJSONString());

            String etl_task_id = tli.getEtl_task_id();
            //获取etl 任务信息
            SqlTaskInfo sqlTaskInfo = sqlTaskMapper.selectByPrimaryKey(etl_task_id);

            Map<String, Object> map = (Map<String, Object>) JSON.parseObject(tli.getParams());
            //此处做参数匹配转换
            if (map != null) {
                logger.info("SQL,自定义参数不为空,开始替换:" + tli.getParams());
                //System.out.println("自定义参数不为空,开始替换:" + dti.getParams());
                DynamicParams(map, tli, null, sqlTaskInfo, null, null);
            }

            //获取数据源信息
            String data_sources_choose_input = sqlTaskInfo.getData_sources_choose_input();
            String data_sources_choose_output = sqlTaskInfo.getData_sources_choose_output();
            DataSourcesInfo dataSourcesInfoInput = new DataSourcesInfo();
            if (data_sources_choose_input != null) {
                dataSourcesInfoInput = dataSourcesServiceImpl.selectById(data_sources_choose_input);
            }
            DataSourcesInfo dataSourcesInfoOutput = null;
            if (data_sources_choose_input == null || !data_sources_choose_input.equals(data_sources_choose_output)) {
                dataSourcesInfoOutput = dataSourcesServiceImpl.selectById(data_sources_choose_output);
            } else {
                dataSourcesInfoOutput = dataSourcesInfoInput;
            }


            if (dataSourcesInfoOutput.getData_source_type().equals("外部下载")) {
                //获取文件服务器信息 配置到数据源选项
                ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(dataSourcesInfoOutput.getOwner());
                if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                    dataSourcesInfoOutput.setUrl(zdhNginx.getHost() + ":" + zdhNginx.getPort());
                    dataSourcesInfoOutput.setUsername(zdhNginx.getUsername());
                    dataSourcesInfoOutput.setPassword(zdhNginx.getPassword());
                    if (sqlTaskInfo.getData_sources_params_output() != null && !sqlTaskInfo.getData_sources_params_output().trim().equals("")) {
                        JSONObject jsonObject = JSON.parseObject(sqlTaskInfo.getData_sources_params_output());
                        jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + getUser().getId());
                        sqlTaskInfo.setData_sources_params_output(JSON.toJSONString(jsonObject));
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + getUser().getId());
                        sqlTaskInfo.setData_sources_params_output(JSON.toJSONString(jsonObject));
                    }
                } else {
                    if (sqlTaskInfo.getData_sources_params_output() != null && !sqlTaskInfo.getData_sources_params_output().trim().equals("")) {
                        JSONObject jsonObject = JSON.parseObject(sqlTaskInfo.getData_sources_params_output());
                        jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + getUser().getId());
                        sqlTaskInfo.setData_sources_params_output(JSON.toJSONString(jsonObject));
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + getUser().getId());
                        sqlTaskInfo.setData_sources_params_output(JSON.toJSONString(jsonObject));
                    }
                }
            }

            ZdhSqlInfo zdhSqlInfo = new ZdhSqlInfo();
            zdhSqlInfo.setZdhInfo(dataSourcesInfoInput, sqlTaskInfo, dataSourcesInfoOutput, tli);

            return zdhSqlInfo;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public static ZdhSshInfo create_zhdSshInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                               SshTaskMapper sshTaskMapper, ZdhNginxMapper zdhNginxMapper) {

        try {

            JarFileMapper jarFileMapper = (JarFileMapper) SpringContext.getBean("jarFileMapper");
            JSONObject json = new JSONObject();
            String date = DateUtil.formatTime(tli.getCur_time());
            json.put("ETL_DATE", date);
            logger.info(" JOB ,外部JAR,处理当前日期,传递参数ETL_DATE 为" + date);
            tli.setParams(json.toJSONString());

            String etl_task_id = tli.getEtl_task_id();
            //获取etl 任务信息
            SshTaskInfo sshTaskInfo = sshTaskMapper.selectByPrimaryKey(etl_task_id);

            Map<String, Object> map = (Map<String, Object>) JSON.parseObject(tli.getParams());
            //此处做参数匹配转换
            if (map != null) {
                logger.info("JAR,自定义参数不为空,开始替换:" + tli.getParams());
                //System.out.println("自定义参数不为空,开始替换:" + dti.getParams());
                DynamicParams(map, tli, null, null, null, sshTaskInfo);
            }
            ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(sshTaskInfo.getOwner());
            List<JarFileInfo> jarFileInfos = jarFileMapper.selectByParams2(sshTaskInfo.getOwner(), new String[]{sshTaskInfo.getId()});

            ZdhSshInfo zdhSshInfo = new ZdhSshInfo();
            zdhSshInfo.setZdhInfo(sshTaskInfo, tli, zdhNginx, jarFileInfos);

            return zdhSshInfo;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }


    public static ZdhDroolsInfo create_zdhDroolsInfo(TaskLogInstance tli, QuartzJobMapper quartzJobMapper,
                                                     EtlTaskService etlTaskService, DataSourcesServiceImpl dataSourcesServiceImpl, ZdhNginxMapper zdhNginxMapper, EtlDroolsTaskMapper etlDroolsTaskMapper,
                                                     EtlMoreTaskMapper etlMoreTaskMapper, SqlTaskMapper sqlTaskMapper) throws Exception {
        try {
            JSONObject json = new JSONObject();
            String date = DateUtil.formatTime(tli.getCur_time());
            json.put("ETL_DATE", date);
            logger.info(" JOB ,Drools,处理当前日期,传递参数ETL_DATE 为" + date);
            tli.setParams(json.toJSONString());

            String etl_task_id = tli.getEtl_task_id();

            //获取Drools任务id
            EtlDroolsTaskInfo etlDroolsTaskInfo = etlDroolsTaskMapper.selectByPrimaryKey(etl_task_id);

            ZdhDroolsInfo zdhDroolsInfo = new ZdhDroolsInfo();

            //获取最终输出数据源
            String data_sources_choose_output = etlDroolsTaskInfo.getData_sources_choose_output();
            DataSourcesInfo dataSourcesInfoOutput = dataSourcesServiceImpl.selectById(data_sources_choose_output);

            if (dataSourcesInfoOutput.getData_source_type().equals("外部下载")) {
                //获取文件服务器信息 配置到数据源选项
                ZdhNginx zdhNginx = zdhNginxMapper.selectByOwner(dataSourcesInfoOutput.getOwner());
                if (zdhNginx != null && !zdhNginx.getHost().equals("")) {
                    dataSourcesInfoOutput.setUrl(zdhNginx.getHost() + ":" + zdhNginx.getPort());
                    dataSourcesInfoOutput.setUsername(zdhNginx.getUsername());
                    dataSourcesInfoOutput.setPassword(zdhNginx.getPassword());
                    if (etlDroolsTaskInfo.getData_sources_params_output() != null && !etlDroolsTaskInfo.getData_sources_params_output().trim().equals("")) {
                        JSONObject jsonObject = JSON.parseObject(etlDroolsTaskInfo.getData_sources_params_output());
                        jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + getUser().getId());
                        etlDroolsTaskInfo.setData_sources_params_output(JSON.toJSONString(jsonObject));
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("root_path", zdhNginx.getNginx_dir() + "/" + getUser().getId());
                        etlDroolsTaskInfo.setData_sources_params_output(JSON.toJSONString(jsonObject));
                    }
                } else {
                    if (etlDroolsTaskInfo.getData_sources_params_output() != null && !etlDroolsTaskInfo.getData_sources_params_output().trim().equals("")) {
                        JSONObject jsonObject = JSON.parseObject(etlDroolsTaskInfo.getData_sources_params_output());
                        jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + getUser().getId());
                        etlDroolsTaskInfo.setData_sources_params_output(JSON.toJSONString(jsonObject));
                    } else {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("root_path", zdhNginx.getTmp_dir() + "/" + getUser().getId());
                        etlDroolsTaskInfo.setData_sources_params_output(JSON.toJSONString(jsonObject));
                    }
                }
            }


            if (etlDroolsTaskInfo.getMore_task().equalsIgnoreCase("单源ETL")) {
                //解析Drools任务中的单任务
                String etl_id = etlDroolsTaskInfo.getEtl_id();
                //获取etl 任务信息
                EtlTaskInfo etlTaskInfo = etlTaskService.selectById(etl_id);

                zdhDroolsInfo.setEtlDroolsTaskInfo(etlDroolsTaskInfo);

                Map<String, Object> map = (Map<String, Object>) JSON.parseObject(tli.getParams());
                //此处做参数匹配转换

                if (map != null) {
                    logger.info("多源,自定义参数不为空,开始替换:" + tli.getParams());
                    DynamicParams(map, tli, etlTaskInfo, null, null, null);
                }

                //获取数据源信息
                String data_sources_choose_input = etlTaskInfo.getData_sources_choose_input();
                DataSourcesInfo dataSourcesInfoInput = dataSourcesServiceImpl.selectById(data_sources_choose_input);
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

            if (etlDroolsTaskInfo.getMore_task().equalsIgnoreCase("多源ETL")) {
                //获取多源任务id
                EtlMoreTaskInfo etlMoreTaskInfo = etlMoreTaskMapper.selectByPrimaryKey(etlDroolsTaskInfo.getEtl_id());
                zdhDroolsInfo.setEtlMoreTaskInfo(etlMoreTaskInfo);

                //解析多源任务中的单任务
                String[] etl_ids = etlMoreTaskInfo.getEtl_ids().split(",");
                //获取etl 任务信息
                List<EtlTaskInfo> etlTaskInfos = etlTaskService.selectByIds(etl_ids);

                for (EtlTaskInfo etlTaskInfo : etlTaskInfos) {
                    Map<String, Object> map = (Map<String, Object>) JSON.parseObject(tli.getParams());
                    //此处做参数匹配转换
                    if (map != null) {
                        logger.info("多源,自定义参数不为空,开始替换:" + tli.getParams());
                        DynamicParams(map, tli, etlTaskInfo, null, null, null);
                    }

                    //获取数据源信息
                    String data_sources_choose_input = etlTaskInfo.getData_sources_choose_input();
                    DataSourcesInfo dataSourcesInfoInput = dataSourcesServiceImpl.selectById(data_sources_choose_input);
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

                Map<String, Object> map = (Map<String, Object>) JSON.parseObject(tli.getParams());
                //此处做参数匹配转换
                if (map != null) {
                    logger.info("SQL,自定义参数不为空,开始替换:" + tli.getParams());
                    DynamicParams(map, tli, null, sqlTaskInfo, null, null);
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
            e.printStackTrace();
            throw e;
        }
    }

    public static void DynamicParams(Map<String, Object> map, TaskLogInstance tli, EtlTaskInfo etlTaskInfo, SqlTaskInfo sqlTaskInfo, JarTaskInfo jarTaskInfo, SshTaskInfo sshTaskInfo) {
        try {
            String date_nodash = DateUtil.formatNodash(tli.getCur_time());
            String date_time = DateUtil.formatTime(tli.getCur_time());
            String date_dt = DateUtil.format(tli.getCur_time());
            Map<String, Object> jinJavaParam = new HashMap<>();
            jinJavaParam.put("zdh.date.nodash", date_nodash);
            jinJavaParam.put("zdh.date.time", date_time);
            jinJavaParam.put("zdh.date", date_dt);
            Jinjava jj = new Jinjava();

            map.forEach((k, v) -> {
                logger.info("key:" + k + ",value:" + v);
                jinJavaParam.put(k, v);
            });

            if (etlTaskInfo != null) {
                final String filter = jj.render(etlTaskInfo.getData_sources_filter_input(), jinJavaParam);
                final String clear = jj.render(etlTaskInfo.getData_sources_clear_output(), jinJavaParam);
                final String file_name=jj.render(etlTaskInfo.getData_sources_file_name_input(),jinJavaParam);
                final String file_name2=jj.render(etlTaskInfo.getData_sources_file_name_output(),jinJavaParam);
                final String table_name=jj.render(etlTaskInfo.getData_sources_table_name_input(),jinJavaParam);
                final String table_name2=jj.render(etlTaskInfo.getData_sources_table_name_output(),jinJavaParam);
                etlTaskInfo.setData_sources_filter_input(filter);
                etlTaskInfo.setData_sources_clear_output(clear);
                etlTaskInfo.setData_sources_file_name_input(file_name);
                etlTaskInfo.setData_sources_file_name_output(file_name2);
                etlTaskInfo.setData_sources_table_name_input(table_name);
                etlTaskInfo.setData_sources_table_name_output(table_name2);
            }

            if (sqlTaskInfo != null) {
                final String etl_sql = jj.render(sqlTaskInfo.getEtl_sql(), jinJavaParam);
                final String clear = jj.render(sqlTaskInfo.getData_sources_clear_output(), jinJavaParam);
                final String file_name=jj.render(sqlTaskInfo.getData_sources_file_name_output(),jinJavaParam);
                final String table_name=jj.render(sqlTaskInfo.getData_sources_table_name_output(),jinJavaParam);

                sqlTaskInfo.setEtl_sql(etl_sql);
                sqlTaskInfo.setData_sources_clear_output(clear);
                sqlTaskInfo.setData_sources_file_name_output(file_name);
                sqlTaskInfo.setData_sources_table_name_output(table_name);

            }

            if (jarTaskInfo != null) {
                final String spark_submit_params = jj.render(jarTaskInfo.getSpark_submit_params(), jinJavaParam);
                jarTaskInfo.setSpark_submit_params(spark_submit_params);
            }

            if (sshTaskInfo != null) {
                final String script_path = jj.render(sshTaskInfo.getSsh_script_path(), jinJavaParam);
                sshTaskInfo.setSsh_script_path(script_path);

                jinJavaParam.put("zdh_online_file", sshTaskInfo.getSsh_script_path() + "/" + sshTaskInfo.getId() + "_online");
                final String ssh_cmd = jj.render(sshTaskInfo.getSsh_cmd(), jinJavaParam);
                sshTaskInfo.setSsh_cmd(ssh_cmd);

                final String script_context = jj.render(sshTaskInfo.getSsh_script_context(), jinJavaParam);
                sshTaskInfo.setSsh_script_context(script_context);

            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }


    }


    /**
     * 获取后台url
     *
     * @param zdhHaInfoMapper
     * @return
     */
    public static ZdhHaInfo getZdhUrl(ZdhHaInfoMapper zdhHaInfoMapper) {
        logger.info("获取后台处理URL");
        String url = "http://127.0.0.1:60001/api/v1/zdh";
        List<ZdhHaInfo> zdhHaInfoList = zdhHaInfoMapper.selectByStatus("enabled");
        String id = "-1";
        if (zdhHaInfoList != null && zdhHaInfoList.size() >= 1) {
            int random = new Random().nextInt(zdhHaInfoList.size());
            return zdhHaInfoList.get(random);
        }
        ZdhHaInfo zdhHaInfo = new ZdhHaInfo();
        zdhHaInfo.setId(id);
        zdhHaInfo.setZdh_url(url);
        return zdhHaInfo;
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
        Timestamp lon_time = new Timestamp(new Date().getTime());
        zdhLogs.setTask_logs_id(tli.getId());
        zdhLogs.setLog_time(lon_time);
        zdhLogs.setMsg(msg);
        zdhLogs.setLevel(level.toUpperCase());
        //linkedBlockingDeque.add(zdhLogs);
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        zdhLogsService.insert(zdhLogs);
    }

    public static void insertLog(TaskGroupLogInstance tli, String level, String msg) {

        ZdhLogs zdhLogs = new ZdhLogs();
        zdhLogs.setJob_id(tli.getJob_id());
        Timestamp lon_time = new Timestamp(new Date().getTime());
        zdhLogs.setTask_logs_id(tli.getId());
        zdhLogs.setLog_time(lon_time);
        zdhLogs.setMsg(msg);
        zdhLogs.setLevel(level.toUpperCase());
        //linkedBlockingDeque.add(zdhLogs);
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        zdhLogsService.insert(zdhLogs);
    }


    /**
     * @param task_logs_id
     * @param model_log
     * @param exe_status
     * @param tli
     * @return
     */
    public static Boolean sendZdh(String task_logs_id, String model_log, Boolean exe_status, TaskLogInstance tli) {
        logger.info("开始发送信息到zdh处理引擎");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        EtlTaskService etlTaskService = (EtlTaskService) SpringContext.getBean("etlTaskServiceImpl");
        DataSourcesServiceImpl dataSourcesServiceImpl = (DataSourcesServiceImpl) SpringContext.getBean("dataSourcesServiceImpl");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        ZdhHaInfoMapper zdhHaInfoMapper = (ZdhHaInfoMapper) SpringContext.getBean("zdhHaInfoMapper");
        ZdhNginxMapper zdhNginxMapper = (ZdhNginxMapper) SpringContext.getBean("zdhNginxMapper");
        EtlMoreTaskMapper etlMoreTaskMapper = (EtlMoreTaskMapper) SpringContext.getBean("etlMoreTaskMapper");
        SqlTaskMapper sqlTaskMapper = (SqlTaskMapper) SpringContext.getBean("sqlTaskMapper");
        JarTaskMapper jarTaskMapper = (JarTaskMapper) SpringContext.getBean("jarTaskMapper");
        EtlDroolsTaskMapper etlDroolsTaskMapper = (EtlDroolsTaskMapper) SpringContext.getBean("etlDroolsTaskMapper");
        SshTaskMapper sshTaskMapper = (SshTaskMapper) SpringContext.getBean("sshTaskMapper");
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");

        String params = tli.getParams().trim();
        ZdhHaInfo zdhHaInfo = getZdhUrl(zdhHaInfoMapper);
        String url = zdhHaInfo.getZdh_url();
        JSONObject json = new JSONObject();
        if (!params.equals("")) {
            logger.info(model_log + " JOB ,参数不为空判断是否有url 参数");
            String value = JSON.parseObject(params).getString("url");
            if (value != null && !value.equals("")) {
                url = value;
            }
            json = JSON.parseObject(params);
        }
        System.out.println("========fdsfsf=========" + tli.getCur_time());
        String date = DateUtil.formatTime(tli.getCur_time());
        json.put("ETL_DATE", date);
        logger.info(model_log + " JOB ,处理当前日期,传递参数ETL_DATE 为" + date);
        tli.setParams(json.toJSONString());

        logger.info(model_log + " JOB ,获取当前的[url]:" + url);

        ZdhMoreInfo zdhMoreInfo = new ZdhMoreInfo();
        ZdhInfo zdhInfo = new ZdhInfo();
        ZdhSqlInfo zdhSqlInfo = new ZdhSqlInfo();
        ZdhJarInfo zdhJarInfo = new ZdhJarInfo();
        ZdhDroolsInfo zdhDroolsInfo = new ZdhDroolsInfo();
        ZdhSshInfo zdhSshInfo = new ZdhSshInfo();

        try {
            if (tli.getMore_task().equals("多源ETL")) {
                logger.info("组装多源ETL任务信息");
                zdhMoreInfo = create_more_task_zdhInfo(tli, quartzJobMapper, etlTaskService, dataSourcesServiceImpl, zdhNginxMapper, etlMoreTaskMapper);
            } else if (tli.getMore_task().equals("单源ETL")) {
                logger.info("组装单源ETL任务信息");
                zdhInfo = create_zhdInfo(tli, quartzJobMapper, etlTaskService, dataSourcesServiceImpl, zdhNginxMapper, etlMoreTaskMapper);
            } else if (tli.getMore_task().equalsIgnoreCase("SQL")) {
                logger.info("组装SQL任务信息");
                zdhSqlInfo = create_zhdSqlInfo(tli, quartzJobMapper, sqlTaskMapper, dataSourcesServiceImpl, zdhNginxMapper);
            } else if (tli.getMore_task().equalsIgnoreCase("外部JAR")) {
                logger.info("组装外部JAR任务信息");
                //zdhJarInfo = create_zhdJarInfo(tli, quartzJobMapper, jarTaskMapper, zdhNginxMapper);
            } else if (tli.getMore_task().equalsIgnoreCase("Drools")) {
                logger.info("组装Drools任务信息");
                zdhDroolsInfo = create_zdhDroolsInfo(tli, quartzJobMapper, etlTaskService, dataSourcesServiceImpl, zdhNginxMapper, etlDroolsTaskMapper, etlMoreTaskMapper, sqlTaskMapper);
            } else if (tli.getMore_task().equalsIgnoreCase("SSH")) {
                logger.info("组装SSH任务信息");
                zdhSshInfo = create_zhdSshInfo(tli, quartzJobMapper, sshTaskMapper, zdhNginxMapper);
            }

            if (exe_status == true) {
                logger.info(model_log + " JOB ,开始发送ETL处理请求");
                zdhInfo.setTask_logs_id(task_logs_id);
                zdhMoreInfo.setTask_logs_id(task_logs_id);
                zdhSqlInfo.setTask_logs_id(task_logs_id);
                zdhJarInfo.setTask_logs_id(task_logs_id);
                zdhDroolsInfo.setTask_logs_id(task_logs_id);
                zdhSshInfo.setTask_logs_id(task_logs_id);
                insertLog(tli, "DEBUG", "[调度平台]:" + model_log + " JOB ,开始发送ETL处理请求");
                //tli.setEtl_date(date);
                tli.setProcess("10");
                tli.setUpdate_time(new Timestamp(new Date().getTime()));
                updateTaskLog(tli, tlim);
                String executor = zdhHaInfo.getId();
                String url_tmp = "";
                String etl_info = "";
                if (tli.getMore_task().equals("多源ETL")) {
                    url_tmp = url + "/more";
                    etl_info = JSON.toJSONString(zdhMoreInfo);
                } else if (tli.getMore_task().equals("单源ETL")) {
                    url_tmp = url;
                    etl_info = JSON.toJSONString(zdhInfo);
                } else if (tli.getMore_task().equals("SQL")) {
                    url_tmp = url + "/sql";
                    etl_info = JSON.toJSONString(zdhSqlInfo);
                } else if (tli.getMore_task().equals("外部JAR")) {
                    logger.info("[调度平台]:外部JAR,参数:" + JSON.toJSONString(zdhJarInfo));
                    insertLog(tli, "DEBUG", "[调度平台]:外部JAR,参数:" + JSON.toJSONString(zdhJarInfo));
                    // submit_jar(tli, zdhJarInfo);
                } else if (tli.getMore_task().equals("Drools")) {
                    url_tmp = url + "/drools";
                    etl_info = JSON.toJSONString(zdhDroolsInfo);
                } else if (tli.getMore_task().equalsIgnoreCase("SSH")) {
                    logger.info("[调度平台]:SSH,参数:" + JSON.toJSONString(zdhSshInfo));
                    insertLog(tli, "DEBUG", "[调度平台]:SSH,参数:" + JSON.toJSONString(zdhSshInfo));
                    tli.setLast_status("etl");
                    boolean rs = ssh_exec(tli, zdhSshInfo);
                    if (rs) {
                        tli.setLast_status("finish");
                        //此处是按照同步方式设计的,如果执行的命令是异步命令那些需要用户自己维护这个状态
                        tli.setStatus(InstanceStatus.FINISH.getValue());
                        tli.setProcess("100");
                        tli.setUpdate_time(new Timestamp(new Date().getTime()));
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
                tli.setUpdate_time(new Timestamp(new Date().getTime()));
                debugInfo(tli);
                updateTaskLog(tli, tlim);

                // System.exit(-1);

                if (!tli.getMore_task().equalsIgnoreCase("SSH")) {
                    logger.info("[调度平台]:" + url_tmp + " ,参数:" + etl_info);
                    insertLog(tli, "DEBUG", "[调度平台]:" + url_tmp + " ,参数:" + etl_info);
                    HttpUtil.postJSON(url_tmp, etl_info);
                    logger.info(model_log + " JOB ,更新调度任务状态为etl");
                    tli.setLast_status("etl");
                    tli.setStatus(InstanceStatus.ETL.getValue());
                    tli.setProcess("15");
                    //tlim.updateTaskLogsById3(tli);
                    //updateTaskLog(tli, tlim);
                    tlim.updateStatusById3("etl", "15", tli.getId());
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("[调度平台]:" + model_log + " JOB ,开始发送" + tli.getMore_task() + " 处理请求,异常请检查zdh_server服务是否正常运行,或者检查网络情况" + e.getMessage());
            insertLog(tli, "ERROR", "[调度平台]:" + model_log + " JOB ,开始发送" + tli.getMore_task() + " 处理请求,异常请检查zdh_server服务是否正常运行,或者检查网络情况" + e.getMessage());
            tli.setStatus(InstanceStatus.ERROR.getValue());
            tli.setProcess("17");
            tli.setUpdate_time(new Timestamp(new Date().getTime()));
            updateTaskLog(tli, tlim);
            //更新执行状态为error
            logger.info(model_log + " JOB ,更新调度任务状态为error");
            tli.setLast_status("error");
            logger.error(e.getMessage());
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
            String script_path = zdhSshInfo.getSshTaskInfo().getSsh_script_path();
            String script_context = zdhSshInfo.getSshTaskInfo().getSsh_script_context();
            List<JarFileInfo> jarFileInfos = zdhSshInfo.getJarFileInfos();
            ZdhNginx zdhNginx = zdhSshInfo.getZdhNginx();
            if (!script_context.isEmpty() || !jarFileInfos.isEmpty()) {
                SFTPUtil sftpUtil = new SFTPUtil(username, password, host, Integer.parseInt(port));
                sftpUtil.login();
                if (!script_context.isEmpty()) {
                    insertLog(tli, "DEBUG", "[调度平台]:SSH,发现在线脚本,使用在线脚本ssh 命令 可配合{{zdh_online_file}} 使用 example sh {{zdh_online_file}} 即是执行在线的脚本");
                    InputStream inputStream = new ByteArrayInputStream(script_context.getBytes());
                    sftpUtil.upload(script_path, id + "_online", inputStream);
                }

                if (!jarFileInfos.isEmpty()) {
                    for (JarFileInfo jarFileInfo : jarFileInfos) {
                        //下载文件
                        if (zdhNginx.getHost() != null && !zdhNginx.getHost().equals("")) {
                            logger.info("开始下载文件:SFTP方式" + jarFileInfo.getFile_name());
                            insertLog(tli, "DEBUG", "[调度平台]:SSH,开始下载文件:SFTP方式" + jarFileInfo.getFile_name());
                            //连接sftp 下载
                            SFTPUtil sftp = new SFTPUtil(zdhNginx.getUsername(), zdhNginx.getPassword(),
                                    zdhNginx.getHost(), new Integer(zdhNginx.getPort()));
                            sftp.login();
                            byte[] fileByte = sftp.download(zdhNginx.getNginx_dir() + "/" + zdhNginx.getOwner(), jarFileInfo.getFile_name());
                            sftpUtil.upload(script_path, jarFileInfo.getFile_name(), fileByte);
                            sftp.logout();
                        } else {
                            logger.info("开始下载文件:本地方式" + jarFileInfo.getFile_name());
                            insertLog(tli, "DEBUG", "[调度平台]:SSH,开始下载文件:本地方式" + jarFileInfo.getFile_name());
                            //本地文件

                            FileInputStream in = null;
                            try {
                                in = new FileInputStream(zdhNginx.getTmp_dir() + "/" + zdhNginx.getOwner() + "/" + jarFileInfo.getFile_name());
                                ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
                                System.out.println("bytes available:" + in.available());
                                byte[] temp = new byte[1024];
                                int size = 0;
                                while ((size = in.read(temp)) != -1) {
                                    out.write(temp, 0, size);
                                }
                                byte[] bytes = out.toByteArray();
                                System.out.println("bytes size got is:" + bytes.length);
                                sftpUtil.upload(script_path, jarFileInfo.getFile_name(), in);

                            } catch (Exception e) {
                                e.printStackTrace();
                                throw e;
                            } finally {
                                if (in != null) in.close();
                            }

                        }
                    }
                }

                sftpUtil.logout();
            }

            SSHUtil sshUtil = new SSHUtil(username, password, host, Integer.parseInt(port));
            sshUtil.login();

            insertLog(tli, "DEBUG", "[调度平台]:SSH,使用在线脚本," + ssh_cmd);
            String[] result = sshUtil.exec(ssh_cmd);
            String error = result[0];
            String out = result[1];
            long t2 = System.currentTimeMillis();

            for (String li : out.split("\r\n|\n")) {
                if (!li.trim().isEmpty())
                    insertLog(tli, "DEBUG", li);
            }
            for (String li : error.split("\r\n|\n")) {
                if (!li.trim().isEmpty())
                    insertLog(tli, "ERROR", li);
            }
            insertLog(tli, "DEBUG", "[调度平台]:SSH,SSH任务执行结束,耗时:" + (t2 - t1) / 1000 + "s");

            if (!error.isEmpty()) {
                return false;
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    public static void updateTaskLog(TaskLogInstance tli, TaskLogInstanceMapper tlim) {
        System.out.println("updateTaskLog===============");
        if(tli.getLast_time()==null){
            if(tli.getCur_time()==null){
                tli.setLast_time(tli.getStart_time());
            }else{
                tli.setLast_time(tli.getCur_time());
            }
        }
        debugInfo(tli);
        tlim.updateByPrimaryKey(tli);
    }

    public static void updateTaskLog(TaskGroupLogInstance tgli, TaskGroupLogInstanceMapper tglim) {
        System.out.println("updateTaskLog===============");
        if(tgli.getLast_time()==null){
            if(tgli.getCur_time()==null){
                tgli.setLast_time(tgli.getStart_time());
            }else{
                tgli.setLast_time(tgli.getCur_time());
            }
        }
        debugInfo(tgli);
        tglim.updateByPrimaryKey(tgli);
    }

    /**
     * 更新任务日志,etl_date
     *
     * @param tli
     * @param tlim
     */
    public static void updateTaskLogEtlDate(TaskLogInstance tli, TaskLogInstanceMapper tlim) {
        tli.setUpdate_time(new Timestamp(new Date().getTime()));
        tli.setProcess("6");
        updateTaskLog(tli, tlim);
    }


    public static void updateTaskLogError(TaskLogInstance tli, String process, TaskLogInstanceMapper tlim, String status, int interval_time) {
        tli.setUpdate_time(new Timestamp(new Date().getTime()));
        tli.setStatus(status);//error,retry
        tli.setProcess(process);//调度完成
        tli.setRetry_time(DateUtil.add(new Timestamp(new Date().getTime()), Calendar.SECOND, interval_time));
        updateTaskLog(tli, tlim);
    }

    public static void updateTaskLogError(TaskGroupLogInstance tgli, String process, TaskGroupLogInstanceMapper tglim, String status, int interval_time) {
        tgli.setUpdate_time(new Timestamp(new Date().getTime()));
        tgli.setStatus(status);//error,retry
        tgli.setProcess(process);//调度完成
        tgli.setRetry_time(DateUtil.add(new Timestamp(new Date().getTime()), Calendar.SECOND, interval_time));
        updateTaskLog(tgli, tglim);
    }

    /**
     * 检查子任务依赖
     *
     * @param jobType
     * @param tli
     */
    public static boolean checkDep(String jobType, TaskLogInstance tli) {
        logger.info("[" + jobType + "] JOB ,开始检查任务依赖");
        insertLog(tli, "INFO", "[" + jobType + "] JOB ,开始检查任务依赖");
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");

        //检查任务依赖
        if (tli.getJump_dep() != null && tli.getJump_dep().equalsIgnoreCase("on")) {
            String msg2 = "[" + jobType + "] JOB ,跳过依赖任务";
            logger.info(msg2);
            insertLog(tli, "INFO", msg2);
            return true;
        }

        if(tli.getPre_tasks()==null || tli.getPre_tasks().equals("") || tli.getPre_tasks().equalsIgnoreCase("root")){
            String msg2 = "[" + jobType + "] JOB ,是根节点任务,无依赖,直接执行";
            logger.info(msg2);
            insertLog(tli, "INFO", msg2);
            return true;
        }else{
            String task_log_ids = tli.getPre_tasks();
            if (task_log_ids != null && task_log_ids.split(",").length > 0) {
                for (String pre_task_id : task_log_ids.split(",")) {
                    String etl_date = tli.getEtl_date();
                    TaskLogInstance dep_pre_tli=tlim.selectByIdStatus(pre_task_id);
                    if (dep_pre_tli == null ) {
                        String msg = "[" + jobType + "] JOB ,依赖任务" + pre_task_id + ",ETL日期" + etl_date + ",未完成";
                        logger.info(msg);
                        insertLog(tli, "INFO", msg);
                        tli.setThread_id(""); //设置为空主要是为了 在检查依赖任务期间杀死
                        tli.setStatus("check_dep");
                        tli.setProcess("7");
                        tli.setUpdate_time(new Timestamp(new Date().getTime()));
                        updateTaskLog(tli,tlim);
                        return false;
                    }
                    String msg2 = "[" + jobType + "] JOB ,依赖任务" + pre_task_id + ",ETL日期" + etl_date + ",已完成";
                    logger.info(msg2);
                    insertLog(tli, "INFO", msg2);
                }
            }

        }


        return true;
    }

    /**
     * 检查任务组依赖
     * @param jobType
     * @param tgli
     * @return
     */
    public static boolean checkDep(String jobType, TaskGroupLogInstance tgli) {
        logger.info("[" + jobType + "] JOB ,开始检查任务依赖");
        insertLog(tgli, "INFO", "[" + jobType + "] JOB ,开始检查任务依赖");
        TaskGroupLogInstanceMapper tglim = (TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");

        //检查任务依赖
        if (tgli.getJump_dep() != null && tgli.getJump_dep().equalsIgnoreCase("on")) {
            String msg2 = "[" + jobType + "] JOB ,跳过依赖任务";
            logger.info(msg2);
            insertLog(tgli, "INFO", msg2);
            return true;
        }
        String job_ids = tgli.getJob_ids();
        if (job_ids != null && job_ids.split(",").length > 0) {
            for (String dep_job_id : job_ids.split(",")) {
                String etl_date = tgli.getEtl_date();
                List<TaskGroupLogInstance> taskLogsList = tglim.selectByIdEtlDate(dep_job_id, etl_date);
                if (taskLogsList == null || taskLogsList.size() <= 0) {
                    String msg = "[" + jobType + "] JOB ,依赖任务" + dep_job_id + ",ETL日期" + etl_date + ",未完成";
                    logger.info(msg);
                    insertLog(tgli, "INFO", msg);
                    tgli.setThread_id(""); //设置为空主要是为了 在检查依赖任务期间杀死
                    tgli.setStatus(JobStatus.CHECK_DEP.getValue());
                    tgli.setProcess("7");
                    tgli.setUpdate_time(new Timestamp(new Date().getTime()));
                    updateTaskLog(tgli,tglim);
                    return false;
                }
                String msg2 = "[" + jobType + "] JOB ,依赖任务" + dep_job_id + ",ETL日期" + etl_date + ",已完成";
                logger.info(msg2);
                insertLog(tgli, "INFO", msg2);
            }

        }
        String msg2 = "[" + jobType + "] JOB ,无依赖任务,或者所有依赖任务已完成";
        logger.info(msg2);
        insertLog(tgli, "INFO", msg2);
        tgli.setStatus(JobStatus.CHECK_DEP_FINISH.getValue());
        return true;
    }
    /**
     * 检查任务状态,并修改参数(任务执行日期等)
     * 检查上次任务状态,未找到上次任务 直接使用quartzJobInfo 信息生成
     * 找到上次任务,判断状态-成功生成新的调度时间,失败使用上次调度时间,运行中直接跳过
     * @param jobType
     * @param tli
     * @param tli
     * @return
     */
    public static boolean checkStatus(String jobType, TaskLogInstance tli) {
        logger.info("[" + jobType + "] JOB,根据上次执行任务状态,修改执行日期");
        insertLog(tli, "info", "[" + jobType + "] JOB,根据上次执行任务状态,修改执行日期");

        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        QuartzJobMapper qjm = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");


        //设置本次执行时间
        if ( !tli.getUse_quartz_time().equalsIgnoreCase("on") && tli.getCur_time() == null) {
            tli.setCur_time(tli.getStart_time());
            logger.info("[" + jobType + "] JOB,设置执行日期为" + tli.getStart_time());
            insertLog(tli, "info", "[" + jobType + "] JOB,设置执行日期为" + tli.getStart_time());
        }

        if (tli.getUse_quartz_time().equalsIgnoreCase("on")){
            tli.setCur_time(tli.getQuartTime());
            logger.info("[" + jobType + "] JOB,使用调度时间,设置执行日期为" + tli.getCur_time());
            insertLog(tli, "info", "[" + jobType + "] JOB,使用调度时间,设置执行日期为" + tli.getCur_time());
        }

        //判断是串行执行
        if (tli.getConcurrency().equalsIgnoreCase("0")) {
            logger.info("[" + jobType + "] JOB,任务执行为串行模式");
            insertLog(tli, "info", "[" + jobType + "] JOB,任务执行为串行模式");
            if(!sio(jobType,tli)) return false;
        } else if (tli.getConcurrency().equalsIgnoreCase("1")) {
            //1并行(不检查状态),提前规划好时间
            logger.info("[" + jobType + "] JOB,任务执行为并行模式");
            insertLog(tli, "info", "[" + jobType + "] JOB,任务执行为并行模式");
            if(!sio_concurrence(jobType,tli)) return false;

        } else {
            //跳过不检查
        }


        //更新tli
        tli.setProcess("6");
        tli.setUpdate_time(new Timestamp(new Date().getTime()));
        tli.setEtl_date(DateUtil.formatTime(tli.getCur_time()));
        if(tli.getUse_quartz_time().equalsIgnoreCase("on")){
            tli.setEtl_date(DateUtil.formatTime(getEtlDate(tli.getCur_time(),tli.getTime_diff())));
        }
        process_time_info pti=tli.getProcess_time2();
        pti.setInit_time(DateUtil.getCurrentTime());
        tli.setProcess_time(pti);

        tlim.updateByPrimaryKey(tli);
        //todo 重要标识-必不可少-查询日志时使用
        qjm.updateTaskLogId(tli.getJob_id(), tli.getId());
        QuartzJobInfo qj = qjm.selectByPrimaryKey(tli.getJob_id());
        qj.setLast_time(tli.getCur_time());
        qj.setNext_time(tli.getNext_time());
        qjm.updateByPrimaryKey(qj);
        debugInfo(tli);
        return true;

    }

    public static boolean checkStatus(String jobType, TaskGroupLogInstance tgli) {
        logger.info("[" + jobType + "] JOB,根据上次执行任务状态,修改执行日期");
        insertLog(tgli, "info", "[" + jobType + "] JOB,根据上次执行任务状态,修改执行日期");

        TaskGroupLogInstanceMapper tglim = (TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
        QuartzJobMapper qjm = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");


        //设置本次执行时间
        if ( !tgli.getUse_quartz_time().equalsIgnoreCase("on") && tgli.getCur_time() == null) {
            tgli.setCur_time(tgli.getStart_time());
            logger.info("[" + jobType + "] JOB,设置执行日期为" + tgli.getStart_time());
            insertLog(tgli, "info", "[" + jobType + "] JOB,设置执行日期为" + tgli.getStart_time());
        }

        if (tgli.getUse_quartz_time().equalsIgnoreCase("on")){
            tgli.setCur_time(tgli.getQuartTime());
            logger.info("[" + jobType + "] JOB,使用调度时间,设置执行日期为" + tgli.getCur_time());
            insertLog(tgli, "info", "[" + jobType + "] JOB,使用调度时间,设置执行日期为" + tgli.getCur_time());
        }

        //判断是串行执行
        if (tgli.getConcurrency().equalsIgnoreCase("0")) {
            logger.info("[" + jobType + "] JOB,任务执行为串行模式");
            insertLog(tgli, "info", "[" + jobType + "] JOB,任务执行为串行模式");
            if(!sio(jobType,tgli)) return false;
        } else if (tgli.getConcurrency().equalsIgnoreCase("1")) {
            //1并行(不检查状态),提前规划好时间
            logger.info("[" + jobType + "] JOB,任务执行为并行模式");
            insertLog(tgli, "info", "[" + jobType + "] JOB,任务执行为并行模式");
            if(!sio_concurrence(jobType,tgli)) return false;

        } else {
            //跳过不检查
        }


        //更新tli
        tgli.setProcess("6");
        tgli.setUpdate_time(new Timestamp(new Date().getTime()));
        tgli.setEtl_date(DateUtil.formatTime(tgli.getCur_time()));
        if(tgli.getUse_quartz_time().equalsIgnoreCase("on")){
            tgli.setEtl_date(DateUtil.formatTime(getEtlDate(tgli.getCur_time(),tgli.getTime_diff())));
        }
        process_time_info pti=tgli.getProcess_time2();
        pti.setInit_time(DateUtil.getCurrentTime());
        tgli.setProcess_time(pti);

        tglim.updateByPrimaryKey(tgli);
        //todo 重要标识-必不可少-查询日志时使用
        qjm.updateTaskLogId(tgli.getJob_id(), tgli.getId());
        QuartzJobInfo qj = qjm.selectByPrimaryKey(tgli.getJob_id());
        qj.setLast_time(tgli.getCur_time());
        qj.setNext_time(tgli.getNext_time());
        qjm.updateByPrimaryKey(qj);
        debugInfo(tgli);
        return true;

    }

    /**
     * 串行执行,日期确定逻辑
     * @param jobType
     * @param tli
     * @return
     */
    public static Boolean sio(String jobType, TaskLogInstance tli) {
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        QuartzJobMapper qjm = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");

        if(tli.getUse_quartz_time().equalsIgnoreCase("on")){
            logger.info("[" + jobType + "] JOB ,当前任务为串行模式,并使用调度触发时间");
            insertLog(tli, "info", "[" + jobType + "] JOB ,当前任务为串行模式,并使用调度触发时间");
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
            List<Date> dates=new ArrayList<>();
            try {
                dates = resolveQuartzExpr(tli.getExpr());
            } catch (ParseException e) {
                e.printStackTrace();
                String msg="[" + jobType + "] JOB ,当前任务解析表达式异常,表达式如下:"+tli.getExpr()+",任务结束";
                logger.info(msg);
                insertLog(tli, "info", msg);
                //quartzJobInfo.setStatus("finish");
                //删除quartz 任务
                int interval_time = (tli.getInterval_time() == null || tli.getInterval_time().equals("")) ? 5 : Integer.parseInt(tli.getInterval_time());
                updateTaskLogError(tli, "6", tlim, "error", interval_time);
                quartzManager2.deleteTask(tli, "finish", "finish");
                return false;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp nextTime=new Timestamp(dates.get(1).getTime());

            tli.setNext_time(nextTime);
            String msg="[" + jobType + "] JOB ,当前任务为串行模式,并使用调度触发时间,生成调度触发时间为:"+DateUtil.formatTime(getEtlDate(tli.getCur_time(),tli.getTime_diff()));
            logger.info(msg);
            insertLog(tli, "info", msg);
            tli.setEtl_date(DateUtil.formatTime(getEtlDate(tli.getCur_time(),tli.getTime_diff())));
            tlim.updateByPrimaryKey(tli);
            return true;
        }

        String step_size = tli.getStep_size();
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
        //串行
        //获取上次的task_logs_id,判断状态
        //时间1 先取上次任务的时间,2 如果为空 取调度原始信息的last_time,3如果为空去调度原始信息的start_time
        //根据上步的时间和上次任务状态判断

        TaskLogInstance last_tli = null;
        if (StringUtils.isEmpty(tli.getLast_task_log_id()) || tlim.selectByPrimaryKey(tli.getLast_task_log_id()) == null) {

            debugInfo(tli);
            if(tli.getLast_time()==null){
                tli.setCur_time(tli.getStart_time());
            }else{
                tli.setCur_time(DateUtil.add(tli.getLast_time(),dateType,num));
            }
            //tli.setNext_time(DateUtil.add(tli.getCur_time(), dateType, num));
            tli.setNext_time(DateUtil.add(tli.getCur_time(),dateType,num));
            debugInfo(tli);
            String msg="[" + jobType + "] JOB,无法找到上次执行的任务实例"+tli.getLast_task_log_id()+",设置ETL_DATE日期为" + DateUtil.formatTime(tli.getCur_time()) + ",下次执行日期为" + tli.getNext_time();
            logger.info(msg);
            insertLog(tli, "info", msg);

            if (tli.getCur_time().after(tli.getEnd_time())) {
                logger.info("[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                insertLog(tli, "info", "[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                //quartzJobInfo.setStatus("finish");
                //删除quartz 任务
                int interval_time = (tli.getInterval_time() == null || tli.getInterval_time().equals("")) ? 5 : Integer.parseInt(tli.getInterval_time());
                updateTaskLogError(tli, "6", tlim, "error", interval_time);
                quartzManager2.deleteTask(tli, "finish", "finish");
                return false;
            }
        }

        last_tli = tlim.selectByPrimaryKey(tli.getLast_task_log_id());

        if (last_tli != null && !last_tli.getId().equals(last_tli.getLast_task_log_id()) && (last_tli.getStatus().equals("etl") || last_tli.getStatus().equals("wait_retry") || last_tli.getStatus().equals("dispatch"))) {
            String msg="[" + jobType + "] JOB ,上一个任务正在处理中,任务实例id:"+last_tli.getId()+",任务状态:" + last_tli.getStatus();
            logger.info(msg);
            insertLog(tli, "info", msg);
            //此处不做处理,单独的超时告警监控
            return false;
        }

        if (last_tli != null && last_tli.getEtl_date() != null) {
            //找到上次执行日志
            String msg="[" + jobType + "] JOB,找到上次执行的任务实例"+tli.getLast_task_log_id()+",上次ETL_DATE日期为" + last_tli.getEtl_date();
            logger.info(msg);
            insertLog(tli, "info", msg);
        }

        //finish状态计算新的etl_date
        if (last_tli != null && last_tli.getStatus() != null && last_tli.getStatus().equals("finish") && last_tli.getCur_time() != null) {

            //finish成功状态 判断last_time 是否超过结束日期,超过，删除任务,更新状态
            if (DateUtil.add(last_tli.getCur_time(), dateType, num).after(tli.getEnd_time())) {
                logger.info("[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                insertLog(tli, "info", "[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                //quartzJobInfo.setStatus("finish");
                //删除quartz 任务
                int interval_time = (tli.getInterval_time() == null || tli.getInterval_time().equals("")) ? 5 : Integer.parseInt(tli.getInterval_time());
                updateTaskLogError(tli, "6", tlim, "error", interval_time);
                quartzManager2.deleteTask(tli, "finish", "finish");

                return false;
            }

            if (tli.getStart_time().before(DateUtil.add(last_tli.getCur_time(), dateType, num)) ||
                    tli.getStart_time().equals(DateUtil.add(last_tli.getCur_time(), dateType, num))) {
                logger.info("[" + jobType + "] JOB,上次执行任务成功,计算新的执行日期:" + DateUtil.add(Timestamp.valueOf(last_tli.getEtl_date()), dateType, num));
                insertLog(tli, "info", "[" + jobType + "] JOB,上次执行任务成功,计算新的执行日期:" + DateUtil.add(Timestamp.valueOf(last_tli.getEtl_date()), dateType, num));
                tli.setCur_time(DateUtil.add(Timestamp.valueOf(last_tli.getEtl_date()), dateType, num));
                tli.setNext_time(DateUtil.add(tli.getCur_time(), dateType, num));
                tli.setEtl_date(DateUtil.formatTime(tli.getCur_time()));
            } else {
                logger.info("[" + jobType + "] JOB,首次执行任务,下次执行日期为起始日期:" + tli.getStart_time());
                insertLog(tli, "info", "[" + jobType + "] JOB,首次执行任务,下次执行日期为起始日期:" + tli.getStart_time());
                tli.setCur_time(tli.getStart_time());
                tli.setNext_time(DateUtil.add(tli.getStart_time(), dateType, num));
                tli.setEtl_date(DateUtil.formatTime(tli.getStart_time()));
            }
        }

        //error 状态  last_time 不变继续执行
        if (last_tli != null && last_tli.getStatus() != null && last_tli.getStatus().equals("error")) {
            String msg ="[" + jobType + "] JOB ,上次任务处理失败,将重新执行,上次日期:" + last_tli.getEtl_date();
            logger.info(msg);
            //插入日志
            insertLog(tli, "info", msg);
            tli.setCur_time(Timestamp.valueOf(last_tli.getEtl_date()));
            tli.setNext_time(DateUtil.add(Timestamp.valueOf(last_tli.getEtl_date()), dateType, num));
        }
        tlim.updateByPrimaryKey(tli);
        return true;

    }

    public static Boolean sio(String jobType, TaskGroupLogInstance tgli) {
        TaskGroupLogInstanceMapper tglim = (TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
        QuartzJobMapper qjm = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");

        if(tgli.getUse_quartz_time().equalsIgnoreCase("on")){
            logger.info("[" + jobType + "] JOB ,当前任务为串行模式,并使用调度触发时间");
            insertLog(tgli, "info", "[" + jobType + "] JOB ,当前任务为串行模式,并使用调度触发时间");
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
            List<Date> dates=new ArrayList<>();
            try {
                dates = resolveQuartzExpr(tgli.getExpr());
            } catch (ParseException e) {
                e.printStackTrace();
                String msg="[" + jobType + "] JOB ,当前任务解析表达式异常,表达式如下:"+tgli.getExpr()+",任务结束";
                logger.info(msg);
                insertLog(tgli, "info", msg);
                //quartzJobInfo.setStatus("finish");
                //删除quartz 任务
                int interval_time = (tgli.getInterval_time() == null || tgli.getInterval_time().equals("")) ? 5 : Integer.parseInt(tgli.getInterval_time());
                updateTaskLogError(tgli, "6", tglim, "error", interval_time);
                quartzManager2.deleteTask(tgli, "finish", "finish");
                return false;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp nextTime=new Timestamp(dates.get(1).getTime());

            tgli.setNext_time(nextTime);
            String msg="[" + jobType + "] JOB ,当前任务为串行模式,并使用调度触发时间,生成调度触发时间为:"+DateUtil.formatTime(getEtlDate(tgli.getCur_time(),tgli.getTime_diff()));
            logger.info(msg);
            insertLog(tgli, "info", msg);
            tgli.setEtl_date(DateUtil.formatTime(getEtlDate(tgli.getCur_time(),tgli.getTime_diff())));
            tglim.updateByPrimaryKey(tgli);
            return true;
        }

        String step_size = tgli.getStep_size();
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
        //串行
        //获取上次的task_logs_id,判断状态
        //时间1 先取上次任务的时间,2 如果为空 取调度原始信息的last_time,3如果为空去调度原始信息的start_time
        //根据上步的时间和上次任务状态判断

        TaskGroupLogInstance last_tgli = null;
        if (StringUtils.isEmpty(tgli.getLast_task_log_id()) || tglim.selectByPrimaryKey(tgli.getLast_task_log_id()) == null) {

            debugInfo(tgli);
            if(tgli.getLast_time()==null){
                tgli.setCur_time(tgli.getStart_time());
            }else{
                tgli.setCur_time(DateUtil.add(tgli.getLast_time(),dateType,num));
            }
            //tli.setNext_time(DateUtil.add(tli.getCur_time(), dateType, num));
            tgli.setNext_time(DateUtil.add(tgli.getCur_time(),dateType,num));
            debugInfo(tgli);
            String msg="[" + jobType + "] JOB,无法找到上次执行的任务实例"+tgli.getLast_task_log_id()+",设置ETL_DATE日期为" + DateUtil.formatTime(tgli.getCur_time()) + ",下次执行日期为" + tgli.getNext_time();
            logger.info(msg);
            insertLog(tgli, "info", msg);

            if (tgli.getCur_time().after(tgli.getEnd_time())) {
                logger.info("[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                insertLog(tgli, "info", "[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                //quartzJobInfo.setStatus("finish");
                //删除quartz 任务
                int interval_time = (tgli.getInterval_time() == null || tgli.getInterval_time().equals("")) ? 5 : Integer.parseInt(tgli.getInterval_time());
                updateTaskLogError(tgli, "6", tglim, "error", interval_time);
                quartzManager2.deleteTask(tgli, "finish", "finish");
                return false;
            }
        }

        last_tgli = tglim.selectByPrimaryKey(tgli.getLast_task_log_id());

        if (last_tgli != null && !last_tgli.getId().equals(last_tgli.getLast_task_log_id()) && (last_tgli.getStatus().equals("etl") || last_tgli.getStatus().equals("wait_retry") || last_tgli.getStatus().equals("dispatch"))) {
            String msg="[" + jobType + "] JOB ,上一个任务正在处理中,任务实例id:"+last_tgli.getId()+",任务状态:" + last_tgli.getStatus();
            logger.info(msg);
            insertLog(tgli, "info", msg);
            //此处不做处理,单独的超时告警监控
            return false;
        }

        if (last_tgli != null && last_tgli.getEtl_date() != null) {
            //找到上次执行日志
            String msg="[" + jobType + "] JOB,找到上次执行的任务实例"+tgli.getLast_task_log_id()+",上次ETL_DATE日期为" + last_tgli.getEtl_date();
            logger.info(msg);
            insertLog(tgli, "info", msg);
        }

        //finish状态计算新的etl_date
        if (last_tgli != null && last_tgli.getStatus() != null && last_tgli.getStatus().equals("finish") && last_tgli.getCur_time() != null) {

            //finish成功状态 判断last_time 是否超过结束日期,超过，删除任务,更新状态
            if (DateUtil.add(last_tgli.getCur_time(), dateType, num).after(tgli.getEnd_time())) {
                logger.info("[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                insertLog(tgli, "info", "[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                //quartzJobInfo.setStatus("finish");
                //删除quartz 任务
                int interval_time = (tgli.getInterval_time() == null || tgli.getInterval_time().equals("")) ? 5 : Integer.parseInt(tgli.getInterval_time());
                updateTaskLogError(tgli, "6", tglim, "error", interval_time);
                quartzManager2.deleteTask(tgli, "finish", "finish");

                return false;
            }

            if (tgli.getStart_time().before(DateUtil.add(last_tgli.getCur_time(), dateType, num)) ||
                    tgli.getStart_time().equals(DateUtil.add(last_tgli.getCur_time(), dateType, num))) {
                logger.info("[" + jobType + "] JOB,上次执行任务成功,计算新的执行日期:" + DateUtil.add(Timestamp.valueOf(last_tgli.getEtl_date()), dateType, num));
                insertLog(tgli, "info", "[" + jobType + "] JOB,上次执行任务成功,计算新的执行日期:" + DateUtil.add(Timestamp.valueOf(last_tgli.getEtl_date()), dateType, num));
                tgli.setCur_time(DateUtil.add(Timestamp.valueOf(last_tgli.getEtl_date()), dateType, num));
                tgli.setNext_time(DateUtil.add(tgli.getCur_time(), dateType, num));
                tgli.setEtl_date(DateUtil.formatTime(tgli.getCur_time()));
            } else {
                logger.info("[" + jobType + "] JOB,首次执行任务,下次执行日期为起始日期:" + tgli.getStart_time());
                insertLog(tgli, "info", "[" + jobType + "] JOB,首次执行任务,下次执行日期为起始日期:" + tgli.getStart_time());
                tgli.setCur_time(tgli.getStart_time());
                tgli.setNext_time(DateUtil.add(tgli.getStart_time(), dateType, num));
                tgli.setEtl_date(DateUtil.formatTime(tgli.getStart_time()));
            }
        }

        //error 状态  last_time 不变继续执行
        if (last_tgli != null && last_tgli.getStatus() != null && last_tgli.getStatus().equals("error")) {
            String msg ="[" + jobType + "] JOB ,上次任务处理失败,将重新执行,上次日期:" + last_tgli.getEtl_date();
            logger.info(msg);
            //插入日志
            insertLog(tgli, "info", msg);
            tgli.setCur_time(Timestamp.valueOf(last_tgli.getEtl_date()));
            tgli.setNext_time(DateUtil.add(Timestamp.valueOf(last_tgli.getEtl_date()), dateType, num));
        }
        tglim.updateByPrimaryKey(tgli);
        return true;

    }

    public static Boolean sio_concurrence(String jobType, TaskLogInstance tli) {
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        QuartzJobMapper qjm = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");

        //使用调度时间
        if(tli.getUse_quartz_time().equalsIgnoreCase("on")){
            logger.info("[" + jobType + "] JOB ,当前任务为并行模式,并使用调度触发时间");
            insertLog(tli, "info", "[" + jobType + "] JOB ,当前任务为并行模式,并使用调度触发时间");

            List<Date> dates = null;
            try {
                dates = JobCommon2.resolveQuartzExpr(tli.getExpr());
            } catch (ParseException e) {
                String msg="[" + jobType + "] JOB ,当前任务解析表达式异常,表达式如下:"+tli.getExpr()+",任务结束";
                logger.info(msg);
                insertLog(tli, "info", msg);
                //quartzJobInfo.setStatus("finish");
                //删除quartz 任务
                int interval_time = (tli.getInterval_time() == null || tli.getInterval_time().equals("")) ? 5 : Integer.parseInt(tli.getInterval_time());
                updateTaskLogError(tli, "6", tlim, "error", interval_time);
                quartzManager2.deleteTask(tli, "finish", "finish");

                return false;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp nextTime=new Timestamp(dates.get(1).getTime());

            tli.setNext_time(nextTime);
            String msg="[" + jobType + "] JOB ,当前任务为串行模式,并使用调度触发时间,生成调度触发时间为:"+DateUtil.formatTime(getEtlDate(tli.getCur_time(),tli.getTime_diff()));
            logger.info(msg);
            insertLog(tli, "info", msg);

            tli.setEtl_date(DateUtil.formatTime(getEtlDate(tli.getCur_time(),tli.getTime_diff())));
            tlim.updateByPrimaryKey(tli);
            return true;
        }

        String step_size = tli.getStep_size();
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
        //并行
        //获取上次的task_logs_id,判断状态
        //时间1 先取上次任务的时间,2 如果为空 取调度原始信息的last_time,3如果为空去调度原始信息的start_time
        //根据上步的时间和上次任务状态判断

        TaskLogInstance last_tli = null;
        if (StringUtils.isEmpty(tli.getLast_task_log_id()) || tlim.selectByPrimaryKey(tli.getLast_task_log_id()) == null) {

            debugInfo(tli);
            if(tli.getLast_time()==null){
                tli.setCur_time(tli.getStart_time());
            }else{
                tli.setCur_time(DateUtil.add(tli.getLast_time(),dateType,num));
            }
            //tli.setNext_time(DateUtil.add(tli.getCur_time(), dateType, num));
            tli.setNext_time(DateUtil.add(tli.getCur_time(),dateType,num));
            debugInfo(tli);
            String msg="[" + jobType + "] JOB,无法找到上次执行的任务实例"+tli.getLast_task_log_id()+",设置ETL_DATE日期为" + DateUtil.formatTime(tli.getCur_time()) + ",下次执行日期为" + tli.getNext_time();
            logger.info(msg);
            insertLog(tli, "info", msg);

            if (tli.getCur_time().after(tli.getEnd_time())) {
                logger.info("[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                insertLog(tli, "info", "[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                //quartzJobInfo.setStatus("finish");
                //删除quartz 任务
                int interval_time = (tli.getInterval_time() == null || tli.getInterval_time().equals("")) ? 5 : Integer.parseInt(tli.getInterval_time());
                updateTaskLogError(tli, "6", tlim, "error", interval_time);
                quartzManager2.deleteTask(tli, "finish", "finish");
                return false;
            }
        }

        last_tli = tlim.selectByPrimaryKey(tli.getLast_task_log_id());

        //finish状态计算新的etl_date
        if (last_tli != null && last_tli.getStatus() != null &&  last_tli.getCur_time() != null) {

            //finish成功状态 判断last_time 是否超过结束日期,超过，删除任务,更新状态
            if (DateUtil.add(last_tli.getCur_time(), dateType, num).after(tli.getEnd_time())) {
                logger.info("[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                insertLog(tli, "info", "[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                //quartzJobInfo.setStatus("finish");
                //删除quartz 任务
                int interval_time = (tli.getInterval_time() == null || tli.getInterval_time().equals("")) ? 5 : Integer.parseInt(tli.getInterval_time());
                updateTaskLogError(tli, "6", tlim, "error", interval_time);
                quartzManager2.deleteTask(tli, "finish", "finish");

                return false;
            }

            if (tli.getStart_time().before(DateUtil.add(last_tli.getCur_time(), dateType, num)) ||
                    tli.getStart_time().equals(DateUtil.add(last_tli.getCur_time(), dateType, num))) {
                logger.info("[" + jobType + "] JOB,找到上次执行任务(状态不判断),计算新的执行日期:" + DateUtil.add(Timestamp.valueOf(last_tli.getEtl_date()), dateType, num));
                insertLog(tli, "info", "[" + jobType + "] JOB,找到上次执行任务(状态不判断),计算新的执行日期:"  + DateUtil.add(Timestamp.valueOf(last_tli.getEtl_date()), dateType, num));
                tli.setCur_time(DateUtil.add(Timestamp.valueOf(last_tli.getEtl_date()), dateType, num));
                tli.setNext_time(DateUtil.add(tli.getCur_time(), dateType, num));
                tli.setEtl_date(DateUtil.formatTime(tli.getCur_time()));
            } else {
                logger.info("[" + jobType + "] JOB,首次执行任务,下次执行日期为起始日期:" + tli.getStart_time());
                insertLog(tli, "info", "[" + jobType + "] JOB,首次执行任务,下次执行日期为起始日期:" + tli.getStart_time());
                tli.setCur_time(tli.getStart_time());
                tli.setNext_time(DateUtil.add(tli.getStart_time(), dateType, num));
                tli.setEtl_date(DateUtil.formatTime(tli.getStart_time()));
            }
        }

        tlim.updateByPrimaryKey(tli);
        return true;

    }

    public static Boolean sio_concurrence(String jobType, TaskGroupLogInstance tgli) {
        TaskGroupLogInstanceMapper tglim = (TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
        QuartzJobMapper qjm = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");

        //使用调度时间
        if(tgli.getUse_quartz_time().equalsIgnoreCase("on")){
            logger.info("[" + jobType + "] JOB ,当前任务为并行模式,并使用调度触发时间");
            insertLog(tgli, "info", "[" + jobType + "] JOB ,当前任务为并行模式,并使用调度触发时间");

            List<Date> dates = null;
            try {
                dates = JobCommon2.resolveQuartzExpr(tgli.getExpr());
            } catch (ParseException e) {
                String msg="[" + jobType + "] JOB ,当前任务解析表达式异常,表达式如下:"+tgli.getExpr()+",任务结束";
                logger.info(msg);
                insertLog(tgli, "info", msg);
                //quartzJobInfo.setStatus("finish");
                //删除quartz 任务
                int interval_time = (tgli.getInterval_time() == null || tgli.getInterval_time().equals("")) ? 5 : Integer.parseInt(tgli.getInterval_time());
                updateTaskLogError(tgli, "6", tglim, "error", interval_time);
                quartzManager2.deleteTask(tgli, "finish", "finish");

                return false;
            }

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp nextTime=new Timestamp(dates.get(1).getTime());

            tgli.setNext_time(nextTime);
            String msg="[" + jobType + "] JOB ,当前任务为串行模式,并使用调度触发时间,生成调度触发时间为:"+DateUtil.formatTime(getEtlDate(tgli.getCur_time(),tgli.getTime_diff()));
            logger.info(msg);
            insertLog(tgli, "info", msg);

            tgli.setEtl_date(DateUtil.formatTime(getEtlDate(tgli.getCur_time(),tgli.getTime_diff())));
            tglim.updateByPrimaryKey(tgli);
            return true;
        }

        String step_size = tgli.getStep_size();
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
        //并行
        //获取上次的task_logs_id,判断状态
        //时间1 先取上次任务的时间,2 如果为空 取调度原始信息的last_time,3如果为空去调度原始信息的start_time
        //根据上步的时间和上次任务状态判断

        TaskGroupLogInstance last_tgli = null;
        if (StringUtils.isEmpty(tgli.getLast_task_log_id()) || tglim.selectByPrimaryKey(tgli.getLast_task_log_id()) == null) {

            debugInfo(tgli);
            if(tgli.getLast_time()==null){
                tgli.setCur_time(tgli.getStart_time());
            }else{
                tgli.setCur_time(DateUtil.add(tgli.getLast_time(),dateType,num));
            }
            //tli.setNext_time(DateUtil.add(tli.getCur_time(), dateType, num));
            tgli.setNext_time(DateUtil.add(tgli.getCur_time(),dateType,num));
            debugInfo(tgli);
            String msg="[" + jobType + "] JOB,无法找到上次执行的任务实例"+tgli.getLast_task_log_id()+",设置ETL_DATE日期为" + DateUtil.formatTime(tgli.getCur_time()) + ",下次执行日期为" + tgli.getNext_time();
            logger.info(msg);
            insertLog(tgli, "info", msg);

            if (tgli.getCur_time().after(tgli.getEnd_time())) {
                logger.info("[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                insertLog(tgli, "info", "[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                //quartzJobInfo.setStatus("finish");
                //删除quartz 任务
                int interval_time = (tgli.getInterval_time() == null || tgli.getInterval_time().equals("")) ? 5 : Integer.parseInt(tgli.getInterval_time());
                updateTaskLogError(tgli, "6", tglim, "error", interval_time);
                quartzManager2.deleteTask(tgli, "finish", "finish");
                return false;
            }
        }

        last_tgli = tglim.selectByPrimaryKey(tgli.getLast_task_log_id());

        //finish状态计算新的etl_date
        if (last_tgli != null && last_tgli.getStatus() != null &&  last_tgli.getCur_time() != null) {

            //finish成功状态 判断last_time 是否超过结束日期,超过，删除任务,更新状态
            if (DateUtil.add(last_tgli.getCur_time(), dateType, num).after(tgli.getEnd_time())) {
                logger.info("[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                insertLog(tgli, "info", "[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                //quartzJobInfo.setStatus("finish");
                //删除quartz 任务
                int interval_time = (tgli.getInterval_time() == null || tgli.getInterval_time().equals("")) ? 5 : Integer.parseInt(tgli.getInterval_time());
                updateTaskLogError(tgli, "6", tglim, "error", interval_time);
                quartzManager2.deleteTask(tgli, "finish", "finish");

                return false;
            }

            if (tgli.getStart_time().before(DateUtil.add(last_tgli.getCur_time(), dateType, num)) ||
                    tgli.getStart_time().equals(DateUtil.add(last_tgli.getCur_time(), dateType, num))) {
                logger.info("[" + jobType + "] JOB,找到上次执行任务(状态不判断),计算新的执行日期:" + DateUtil.add(Timestamp.valueOf(last_tgli.getEtl_date()), dateType, num));
                insertLog(tgli, "info", "[" + jobType + "] JOB,找到上次执行任务(状态不判断),计算新的执行日期:"  + DateUtil.add(Timestamp.valueOf(last_tgli.getEtl_date()), dateType, num));
                tgli.setCur_time(DateUtil.add(Timestamp.valueOf(last_tgli.getEtl_date()), dateType, num));
                tgli.setNext_time(DateUtil.add(tgli.getCur_time(), dateType, num));
                tgli.setEtl_date(DateUtil.formatTime(tgli.getCur_time()));
            } else {
                logger.info("[" + jobType + "] JOB,首次执行任务,下次执行日期为起始日期:" + tgli.getStart_time());
                insertLog(tgli, "info", "[" + jobType + "] JOB,首次执行任务,下次执行日期为起始日期:" + tgli.getStart_time());
                tgli.setCur_time(tgli.getStart_time());
                tgli.setNext_time(DateUtil.add(tgli.getStart_time(), dateType, num));
                tgli.setEtl_date(DateUtil.formatTime(tgli.getStart_time()));
            }
        }

        tglim.updateByPrimaryKey(tgli);
        return true;

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

        logger.info("[" + jobType + "] JOB,任务模式为[时间序列]");
        insertLog(tli, "info", "[" + jobType + "] JOB,任务模式为[时间序列]");

        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        logger.info("[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        insertLog(tli, "info", "[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        exe_status = sendZdh(task_logs_id, "[" + jobType + "]", exe_status, tli);

        if (exe_status) {
            logger.info("[" + jobType + "] JOB ,执行命令成功");
            insertLog(tli, "info", "[" + jobType + "] JOB ,执行命令成功");

            if (tli.getEnd_time() == null) {
                logger.info("[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                insertLog(tli, "info", "[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                tli.setEnd_time(new Timestamp(new Date().getTime()));
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
        logger.info("[" + jobType + "] JOB,任务模式为[ONCE]");
        insertLog(tli, "info", "[" + jobType + "] JOB,任务模式为[ONCE]");

        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        logger.info("[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        insertLog(tli, "info", "[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        exe_status = sendZdh(task_logs_id, "[" + jobType + "]", exe_status, tli);

        if (exe_status) {
            logger.info("[" + jobType + "] JOB ,执行命令成功");
            insertLog(tli, "info", "[" + jobType + "] JOB ,执行命令成功");

            if (tli.getEnd_time() == null) {
                logger.info("[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                insertLog(tli, "info", "[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                tli.setEnd_time(new Timestamp(new Date().getTime()));
            }
            tli.setStatus("finish");
            //插入日志
            logger.info("[" + jobType + "] JOB ,结束调度任务");
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
        logger.info("[" + jobType + "] JOB,任务模式为[重复执行模式]");
        insertLog(tli, "info", "[" + jobType + "] JOB,任务模式为[重复执行模式]");

        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        logger.info("[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        insertLog(tli, "info", "[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        exe_status = sendZdh(task_logs_id, "[" + jobType + "]", exe_status, tli);
        if (exe_status) {
            logger.info("[" + jobType + "] JOB ,执行命令成功");
            insertLog(tli, "INFO", "[" + jobType + "] JOB ,执行命令成功");

            if (tli.getEnd_time() == null) {
                logger.info("[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                insertLog(tli, "INFO", "[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                tli.setEnd_time(new Timestamp(new Date().getTime()));
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
        logger.info("[" + jobType + "] JOB ,调度命令执行失败未能发往任务到后台ETL执行");
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
        logger.info(msg);
        insertLog(tli, "ERROR", msg);
        int interval_time = (tli.getInterval_time() == null || tli.getInterval_time().equals("")) ? 5 : Integer.parseInt(tli.getInterval_time());
        //调度时异常
        updateTaskLogError(tli, "9", tlim, status, interval_time);

        if (status.equalsIgnoreCase("error")) {
            quartzManager2.deleteTask(tli, "finish", status);
        }
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
        logger.info(msg);
        insertLog(tli, "ERROR", msg);
        int interval_time = (tli.getInterval_time() == null || tli.getInterval_time().equals("")) ? 5 : Integer.parseInt(tli.getInterval_time());
        updateTaskLogError(tli, "17", tlim, status, interval_time);
        if (status.equalsIgnoreCase("error")) {
            quartzManager2.deleteTask(tli, "finish", status);
        }
    }

    public static User getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

    /**
     * 选择具体的job执行引擎
     *  解析创建任务实例->检查更新任务状态->检查依赖-> 执行具体任务
     * @param quartzJobInfo
     * @param is_retry      0:调度,1:自动重试,2:手动重试,3:手动执行,4:并行手动执行
     */
    public static void chooseJobBean(QuartzJobInfo quartzJobInfo, int is_retry, TaskGroupLogInstance retry_tgli) {
        QuartzJobMapper qjm = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        TaskGroupLogInstanceMapper tglim = (TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        //手动重试增加重试实例,自动重试在原来的基础上

        if (quartzJobInfo.getJob_type().equals("EMAIL")) {
            logger.debug("调度任务[EMAIL],开始调度");
            EmailJob.run(quartzJobInfo);
            EmailJob.notice_event();
            return;
        } else if (quartzJobInfo.getJob_type().equals("RETRY")) {
            logger.debug("调度任务[RETRY],开始调度");
            RetryJob.run(quartzJobInfo);
            return;
        } else if (quartzJobInfo.getJob_type().equals("CHECK")) {
            logger.debug("调度任务[CHECK],开始调度");
            CheckDepJob.run(quartzJobInfo);
            return;
        }

        //线程池执行具体调度任务
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {

                TaskGroupLogInstance tgli = new TaskGroupLogInstance();
                tgli.setId(SnowflakeIdWorker.getInstance().nextId() + "");
                try {
                    //复制quartzjobinfo到tli,任务基础信息完成复制
                    BeanUtils.copyProperties(tgli, quartzJobInfo);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                //逻辑发送错误代码捕获发生自动重试(retry_job) 不重新生成实例id,使用旧的实例id
                String last_task_id="";
                if(is_retry==0){
                    //调度触发
                    //1 获取上次执行task_id,并付给新的任务实列last_task_log_id
                    tgli.setLast_task_log_id(quartzJobInfo.getTask_log_id());
                    tgli.setRun_time(new Timestamp(new Date().getTime()));//实例开始时间
                }
                if (is_retry == 1) {
                    //失败自动重试触发
                    //重试需要使用上次执行实例信息,并将上次执行失败的实例id重新付给quartjobinfo
                    tgli = retry_tgli;
                    last_task_id=tgli.getLast_task_log_id();
                    quartzJobInfo.setTask_log_id(last_task_id);
                    quartzJobInfo.setStatus("dispatch");//此处赋值dispatch 主要是为了下方判断上次任务状态
                    tgli.setStatus("dispatch");
                }
                if(is_retry==2){
                    //手动点击重试按钮触发
                    //手动点击重试,会生成新的实例信息,默认重置执行次数,并将上次执行失败的实例id 付给last_task_id
                    tgli=retry_tgli;
                    tgli.setRun_time(new Timestamp(new Date().getTime()));//实例开始时间
                    tgli.setCount(0L);
                    tgli.setIs_retryed("0");
                    tgli.setLast_task_log_id(retry_tgli.getId());
                    tgli.setId(SnowflakeIdWorker.getInstance().nextId() + "");
                    tgli.setStatus("dispatch");
                }
                if(is_retry==3){
                    //手动点击执行触发,重置次数和上次任务实例id
                    tgli.setCount(0);
                    tgli.setRun_time(new Timestamp(new Date().getTime()));//实例开始时间
                    tgli.setLast_task_log_id(quartzJobInfo.getTask_log_id());
                    //tli.setLast_time(quartzJobInfo.getStart_time());
                }
                if(is_retry==4){
                    tgli=retry_tgli;
                    tgli.setCount(0);
                    tgli.setRun_time(new Timestamp(new Date().getTime()));//实例开始时间
                }
                //此处更新主要是为了 日期超时时 也能记录下日志
                if (is_retry == 1) {
                    tglim.updateByPrimaryKey(tgli);
                } else {
                    tglim.insert(tgli);
                }
                //公共设置
                tgli.setStatus("dispatch");//新实例状态设置为dispatch
                //todo 检查状态--重新组装调度实例信息,返回false 表示不运行此次任务
                if(!checkStatus("QUARTZ", tgli)){
                    tgli.setStatus("error");//新实例状态设置为dispatch
                    tglim.updateByPrimaryKey(tgli);
                    return ;
                }
                //设置调度器唯一标识,调度故障转移时使用,如果服务器重启会自动生成新的唯一标识
                tgli.setServer_id(JobCommon2.web_application_id);

                //todo 生成具体任务组下任务实例
                sub_task_log_instance(tgli);

                // todo abc
                debugInfo(tgli);
                tglim.updateByPrimaryKey(tgli);

                //更新quartzjobinfo  上次执行task_log_id
                qjm.updateTaskLogId(tgli.getJob_id(), tgli.getId());

                //检查任务依赖,和并行不冲突
                boolean dep = checkDep(quartzJobInfo.getJob_type(), tgli);
                //更新任务依赖时间
                process_time_info pti=tgli.getProcess_time2();
                pti.setCheck_dep_time(DateUtil.getCurrentTime());
                tgli.setProcess_time(pti);
                if(dep){
                    //修改组任务状态,及修改子任务状态为检查依赖中
                    CheckDepJob.updateTaskGroupLogInstanceStatus(tgli);
                }else
                    updateTaskLog(tgli,tglim);
                    return ;



//                if (quartzJobInfo.getJob_type().equals("SHELL")) {
//                    logger.info("调度任务[SHELL],开始调度");
//                    ShellJob.run(tli, false);
//                } else if (quartzJobInfo.getJob_type().equals("JDBC")) {
//                    logger.info("调度任务[JDBC],开始调度");
//                    JdbcJob.run(tli, false);
//                } else if (quartzJobInfo.getJob_type().equals("FTP")) {
//                    logger.info("调度任务[FTP],开始调度");
//                    FtpJob.run(quartzJobInfo);
//                } else if (quartzJobInfo.getJob_type().equals("HDFS")) {
//                    logger.info("调度任务[HDFS],开始调度");
//                    HdfsJob.run(tli, false);
//                } else {
//                    ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
//                    quartzJobInfo.setTask_log_id("system");
//                    JobCommon2.insertLog(null, "ERROR",
//                            "无法找到对应的任务类型,请检查调度任务配置中的任务类型");
//                    logger.info("无法找到对应的任务类型,请检查调度任务配置中的任务类型");
//                }

            }
        });

    }


    /**
     * 任务触发后,等待依赖任务完成触发
     * @param tli
     */
    public static void chooseJobBean(TaskLogInstance tli) {
        QuartzJobMapper qjm = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");

        //线程池执行具体调度任务
        threadPoolExecutor.execute(new Runnable() {
            @Override
            public void run() {

                if (tli.getJob_type().equals("SHELL")) {
                    logger.info("调度任务[SHELL],开始调度");
                    ShellJob.run(tli, false);
                } else if (tli.getJob_type().equals("JDBC")) {
                    logger.info("调度任务[JDBC],开始调度");
                    JdbcJob.run(tli, false);
                } else if (tli.getJob_type().equals("FTP")) {
                    logger.info("调度任务[FTP],开始调度");
                } else if (tli.getJob_type().equals("HDFS")) {
                    logger.info("调度任务[HDFS],开始调度");
                    HdfsJob.run(tli, false);
                } else {
                    logger.info("无法找到对应的任务类型,请检查调度任务配置中的任务类型");
                }

            }
        });

    }


    /**
     * 公共解析任务流程入口
     * 检查次数限制->匹配动态脚本并执行->解析任务明细执行
     *
     * @param jobType
     * @param tli
     */
    public static void chooseCommand(String jobType, TaskLogInstance tli) {
        TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        logger.info("开始执行[" + jobType + "] JOB");
        insertLog(tli, "INFO", "开始执行[" + jobType + "] JOB");


        boolean end = isCount(jobType, tli);

        if (end == true) return;

        Boolean exe_status = false;

        logger.info("[" + jobType + "] JOB ,开始执行动态命令或者脚本");
        insertLog(tli, "INFO", "[" + jobType + "] JOB ,开始执行动态命令或者脚本");
        if (jobType.equalsIgnoreCase(ShellJob.jobType)) {
            exe_status = ShellJob.shellCommand(tli);
        } else if (jobType.equalsIgnoreCase(JdbcJob.jobType)) {
            exe_status = JdbcJob.runCommand(tli);
        } else if (jobType.equalsIgnoreCase(HdfsJob.jobType)) {
            exe_status = HdfsJob.hdfsCommand(tli);
        }

        //更新在线条件(shell,jdbc....)时间
        process_time_info pti=tli.getProcess_time2();
        pti.setCommand_time(DateUtil.getCurrentTime());
        tli.setProcess_time(pti);
        updateTaskLog(tli,tlim);

        if (!exe_status) {
            jobFail(jobType, tli);
        }

        //拼接任务信息发送请求
        if (exe_status) {
            if (tli.getJob_model().equals(JobModel.TIME_SEQ.getValue())) {
                runTimeSeq(jobType, tli.getId(), exe_status, tli);
            } else if (tli.getJob_model().equals(JobModel.ONCE.getValue())) {
                runOnce(jobType, tli.getId(), exe_status, tli);
            } else if (tli.getJob_model().equals(JobModel.REPEAT.getValue())) {
                runRepeat(jobType, tli.getId(), exe_status, tli);
            }

            //更新最后阶段(shell,jdbc....)时间
            process_time_info pti2=tli.getProcess_time2();
            pti2.setServer_time(DateUtil.getCurrentTime());
            tli.setProcess_time(pti2);
            updateTaskLog(tli,tlim);

        }
    }


    public static List<Date> resolveQuartzExpr(String expr) throws ParseException {

        List<Date> dates = new ArrayList<>();

        if(expr.endsWith("s") || expr.endsWith("m") || expr.endsWith("h")){
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

            for(int i=1;i<=10;i++){
                dates.add(DateUtil.add(new Timestamp(new Date().getTime()),dateType,num*i));
            }

        }else{
            CronTriggerImpl cronTriggerImpl = new CronTriggerImpl();
            cronTriggerImpl.setCronExpression(expr);//这里写要准备猜测的cron表达式
            dates=TriggerUtils.computeFireTimes(cronTriggerImpl, null, 10);
        }

        return dates;

    }


    /**
     * 使用quartz 触发时间时,获取指定时间差的etl_date
     * @param cur
     * @param step_size
     * @return
     */
    public static Timestamp getEtlDate(Timestamp cur,String step_size){
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

        return DateUtil.add(cur,dateType,num);
    }

    /**
     * 根据tli 任务组生成具体的子任务实例
     * @param tgli
     */
    public static List<TaskLogInstance> sub_task_log_instance(TaskGroupLogInstance tgli){
        List<TaskLogInstance> tliList=new ArrayList<>();
        try{
            TaskGroupLogInstanceMapper tglim = (TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
            TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
            // 任务id 与具体实例id 映射
            Map<String,String> map=new HashMap<>();
            DAG dag=new DAG();
            JSONArray jry=JSON.parseObject(tgli.getJsmind_data()).getJSONArray("tasks");
            JSONArray lines=JSON.parseObject(tgli.getJsmind_data()).getJSONArray("line");
            for(Object job :jry){
                TaskLogInstance taskLogInstance=new TaskLogInstance();
                BeanUtils.copyProperties(taskLogInstance,tgli);
                String etl_task_id=((JSONObject) job).getString("etl_task_id");//具体任务id
                String pageSourceId=((JSONObject) job).getString("divId");//前端生成的div 标识
                String more_task=((JSONObject) job).getString("more_task");

                String etl_context=((JSONObject) job).getString("etl_context");
                String t_id=SnowflakeIdWorker.getInstance().nextId()+"";
                map.put(pageSourceId,t_id);//div标识和任务实例id 对应关系

                taskLogInstance.setMore_task(more_task);

                taskLogInstance.setId(t_id);//具体执行任务实例id,每次执行都会重新生成
                taskLogInstance.setJob_id(tgli.getJob_id());//调度任务id
                taskLogInstance.setJob_context(tgli.getJob_context());//调度任务说明
                taskLogInstance.setEtl_task_id(etl_task_id);//etl任务id
                taskLogInstance.setEtl_context(etl_context);//etl任务说明
                taskLogInstance.setStatus("check_dep");
                taskLogInstance.setJsmind_data("");
                taskLogInstance.setRun_jsmind_data("");
                tliList.add(taskLogInstance);
            }

            // 生成实例依赖关系
            //"line": [{
            //		"connectionId": "con_16",
            //		"pageSourceId": "chart-servere28-4ee7-ac89-b6",
            //		"pageTargetId": "chart-servere18-45ab-9864-cd"
            //	}, {
            //		"connectionId": "con_23",
            //		"pageSourceId": "chart-servere28-4ee7-ac89-b6",
            //		"pageTargetId": "chart-serverad6-4f2a-a07a-9c"
            //	}]
            for(Object job :lines){
                String pageSourceId=((JSONObject) job).getString("pageSourceId");
                String pageTargetId=((JSONObject) job).getString("pageTargetId");
                ((JSONObject) job).put("id",map.get(pageSourceId));
                ((JSONObject) job).put("parentid",map.get(pageTargetId));
                if(pageSourceId !=null && !pageSourceId.equalsIgnoreCase("root")){
                    dag.addEdge(map.get(pageSourceId),map.get(pageTargetId));//此处的依赖关系 都是生成的任务实例id
                }
            }

            //run_data 结构：run_data:[{task_log_instance_id,etl_task_id,etl_context,more_task}]
            JSONArray jary=new JSONArray();
            for(TaskLogInstance tli:tliList){
                JSONObject jsonObject1=new JSONObject();
                String tid=tli.getId();
                System.out.println("=======================");

                jsonObject1.put("task_log_instance_id",tid);
                jsonObject1.put("etl_task_id",tli.getEtl_task_id());
                jsonObject1.put("etl_context",tli.getEtl_context());
                jsonObject1.put("more_task",tli.getMore_task());
                jary.add(jsonObject1);
            }

            JSONObject jsonObject=JSON.parseObject(tgli.getJsmind_data());
            jsonObject.put("run_data",jary);
            tgli.setRun_jsmind_data(jsonObject.toJSONString());
            tgli.setProcess("6.5");
            tglim.updateByPrimaryKey(tgli);
            //debugInfo(tgli);


            //生成实例
            for(TaskLogInstance tli:tliList){
                JSONObject jsonObject1=new JSONObject();
                String tid=tli.getId();
                String next_tasks=StringUtils.join(dag.getChildren(tid).toArray(),",");
                String pre_tasks=StringUtils.join(dag.getParent(tid),",");
                tli.setGroup_id(tgli.getId());
                tli.setGroup_context(tgli.getJob_context());
                tli.setNext_tasks(next_tasks);
                tli.setPre_tasks(pre_tasks);
                tli.setStatus(JobStatus.CREATE.getValue());
                tlim.insert(tli);
               // debugInfo(tli);
                System.out.println("=======================");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return tliList;
    }

    /**
     * 根据tli 任务组生成具体的子任务实例
     * @param tgli
     */
    public static List<TaskLogInstance> sub_task_log_instance2(TaskGroupLogInstance tgli){
        List<TaskLogInstance> tliList=new ArrayList<>();
        try{
            TaskGroupLogInstanceMapper tglim = (TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
            TaskLogInstanceMapper tlim = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
            // 任务id 与具体实例id 映射
            Map<String,String> map=new HashMap<>();
            DAG dag=new DAG();
            JSONArray jry=JSON.parseObject(tgli.getJsmind_data()).getJSONArray("data");
            for(Object job :jry){
                TaskLogInstance taskLogInstance=new TaskLogInstance();
                BeanUtils.copyProperties(taskLogInstance,tgli);
                String id=((JSONObject) job).getString("id");
                String isroot=((JSONObject) job).getString("isroot");
                String more_task=((JSONObject) job).getString("more_task");
                String parent_id=((JSONObject) job).getString("parentid");
                String topic=((JSONObject) job).getString("topic");
                String t_id=SnowflakeIdWorker.getInstance().nextId()+"";
                map.put(id,t_id);
                if(isroot!=null && isroot.equalsIgnoreCase("true")){
                    map.put("root","root");
                }else{
                    taskLogInstance.setId(t_id);
                    taskLogInstance.setJob_id(tgli.getJob_id());
                    taskLogInstance.setJob_context(topic);
                    taskLogInstance.setStatus("check_dep");
                    tliList.add(taskLogInstance);
                }

            }

            // 生成实例依赖关系
            for(Object job :jry){
                String id=((JSONObject) job).getString("id");
                String parent_id=((JSONObject) job).getString("parentid");
                ((JSONObject) job).put("id",map.get(id));
                ((JSONObject) job).put("parentid",map.get(parent_id));
                if(id !=null && !id.equalsIgnoreCase("root")){
                    dag.addEdge(map.get(parent_id),map.get(id));
                }
            }

            JSONObject jsonObject=JSON.parseObject(tgli.getJsmind_data());
            jsonObject.put("run_data",jry);
            tgli.setRun_jsmind_data(jsonObject.toJSONString());
            tgli.setProcess("6.5");
            tglim.updateByPrimaryKey(tgli);
            //debugInfo(tgli);

            //生成实例
            for(TaskLogInstance tli:tliList){
                String tid=tli.getId();
                String next_tasks=StringUtils.join(dag.getChildren(tid).toArray(),",");
                String pre_tasks=StringUtils.join(dag.getParent(tid),",");
                tli.setGroup_id(tgli.getId());
                tli.setGroup_context(tgli.getJob_context());
                tli.setNext_tasks(next_tasks);
                tli.setPre_tasks(pre_tasks);
                tli.setStatus(JobStatus.CREATE.getValue());
                tlim.insert(tli);
                // debugInfo(tli);
                System.out.println("=======================");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return tliList;
    }
}
