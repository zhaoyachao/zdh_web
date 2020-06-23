package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.EtlTaskService;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.service.impl.DataSourcesServiceImpl;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.HttpUtil;
import com.zyc.zdh.util.SpringContext;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;

public class JobCommon {

    public static Logger logger = LoggerFactory.getLogger(JobCommon.class);

    public static LinkedBlockingDeque<ZdhLogs> linkedBlockingDeque = new LinkedBlockingDeque<ZdhLogs>();


    public static void logThread(ZdhLogsService zdhLogsService) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        ZdhLogs log = JobCommon.linkedBlockingDeque.take();
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
     * @param jobType        SHELL,JDBC,FTP,CLASS
     * @param quartzManager2
     * @param quartzJobInfo
     */
    public static boolean isCount(String jobType, QuartzManager2 quartzManager2, QuartzJobInfo quartzJobInfo) {

        //判断次数,上次任务完成重置次数
        if (quartzJobInfo.getLast_status() == null || quartzJobInfo.getLast_status().equals("finish")) {
            quartzJobInfo.setCount(0);
        }
        quartzJobInfo.setCount(quartzJobInfo.getCount() + 1);

        if (quartzJobInfo.getPlan_count().trim().equals("-1")) {
            logger.info("[" + jobType + "] JOB ,当前任务未设置执行次数限制");
            insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,当前任务未设置执行次数限制");
        }

        if (!quartzJobInfo.getPlan_count().trim().equals("") && !quartzJobInfo.getPlan_count().trim().equals("-1")) {
            //任务有次数限制,满足添加说明这是最后一次任务
            System.out.println(quartzJobInfo.getCount() + "================" + quartzJobInfo.getPlan_count().trim());
            if (quartzJobInfo.getCount() > Long.parseLong(quartzJobInfo.getPlan_count().trim())) {
                logger.info("[" + jobType + "] JOB 检测到任务次数超过限制,删除任务并直接返回结束");
                insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB 检测到任务次数超过限制,删除任务并直接返回结束");
                quartzManager2.deleteTask(quartzJobInfo, "finish");
                return true;
            }
            if (quartzJobInfo.getCount() == Long.parseLong(quartzJobInfo.getPlan_count().trim())) {
                logger.info("[" + jobType + "] JOB ,当前执行的任务是最后一次任务,设置调度任务的状态为[finish]");
                insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,当前执行的任务是最后一次任务,设置调度任务的状态为[finish]");
                quartzJobInfo.setStatus("finish");
                return false;
            }

        }
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


    public static ZdhInfo create_zhdInfo(QuartzJobInfo quartzJobInfo, QuartzJobMapper quartzJobMapper,
                                         EtlTaskService etlTaskService, DataSourcesServiceImpl dataSourcesServiceImpl, ZdhNginxMapper zdhNginxMapper, EtlMoreTaskMapper etlMoreTaskMapper) {

        JSONObject json = new JSONObject();
        String date = DateUtil.formatTime(quartzJobInfo.getLast_time());
        json.put("ETL_DATE", date);
        logger.info(" JOB ,单源,处理当前日期,传递参数ETL_DATE 为" + date);
        quartzJobInfo.setParams(json.toJSONString());

        String etl_task_id = quartzJobInfo.getEtl_task_id();
        //获取etl 任务信息
        EtlTaskInfo etlTaskInfo = etlTaskService.selectById(etl_task_id);

        Map<String, Object> map = (Map<String, Object>) JSON.parseObject(quartzJobInfo.getParams());
        //此处做参数匹配转换
        if (map != null) {
            logger.info("单源,自定义参数不为空,开始替换:" + quartzJobInfo.getParams());
            //System.out.println("自定义参数不为空,开始替换:" + dti.getParams());
            DynamicParams(map, quartzJobInfo, etlTaskInfo, null);
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
        zdhInfo.setZdhInfo(dataSourcesInfoInput, etlTaskInfo, dataSourcesInfoOutput, quartzJobInfo);

        return zdhInfo;

    }

    public static ZdhMoreInfo create_more_task_zdhInfo(QuartzJobInfo quartzJobInfo, QuartzJobMapper quartzJobMapper,
                                                       EtlTaskService etlTaskService, DataSourcesServiceImpl dataSourcesServiceImpl, ZdhNginxMapper zdhNginxMapper, EtlMoreTaskMapper etlMoreTaskMapper) {
        try {
            JSONObject json = new JSONObject();
            String date = DateUtil.formatTime(quartzJobInfo.getLast_time());
            json.put("ETL_DATE", date);
            logger.info(" JOB ,多源,处理当前日期,传递参数ETL_DATE 为" + date);
            quartzJobInfo.setParams(json.toJSONString());

            String etl_task_id = quartzJobInfo.getEtl_task_id();

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


            Map<String, Object> map = (Map<String, Object>) JSON.parseObject(quartzJobInfo.getParams());
            //此处做参数匹配转换
            for (EtlTaskInfo etlTaskInfo : etlTaskInfos) {
                if (map != null) {
                    logger.info("多源,自定义参数不为空,开始替换:" + quartzJobInfo.getParams());
                    DynamicParams(map, quartzJobInfo, etlTaskInfo, null);
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


                zdhMoreInfo.setZdhMoreInfo(dataSourcesInfoInput, etlTaskInfo, dataSourcesInfoOutput, quartzJobInfo, etlMoreTaskInfo);
            }


            return zdhMoreInfo;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    public static ZdhSqlInfo create_zhdSqlInfo(QuartzJobInfo quartzJobInfo, QuartzJobMapper quartzJobMapper,
                                               SqlTaskMapper sqlTaskMapper, DataSourcesServiceImpl dataSourcesServiceImpl, ZdhNginxMapper zdhNginxMapper) {

        try {
            JSONObject json = new JSONObject();
            String date = DateUtil.formatTime(quartzJobInfo.getLast_time());
            json.put("ETL_DATE", date);
            logger.info(" JOB ,单源,处理当前日期,传递参数ETL_DATE 为" + date);
            quartzJobInfo.setParams(json.toJSONString());

            String etl_task_id = quartzJobInfo.getEtl_task_id();
            //获取etl 任务信息
            SqlTaskInfo sqlTaskInfo = sqlTaskMapper.selectByPrimaryKey(etl_task_id);

            Map<String, Object> map = (Map<String, Object>) JSON.parseObject(quartzJobInfo.getParams());
            //此处做参数匹配转换
            if (map != null) {
                logger.info("单源,自定义参数不为空,开始替换:" + quartzJobInfo.getParams());
                //System.out.println("自定义参数不为空,开始替换:" + dti.getParams());
                DynamicParams(map, quartzJobInfo, null, sqlTaskInfo);
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
            zdhSqlInfo.setZdhInfo(dataSourcesInfoInput, sqlTaskInfo, dataSourcesInfoOutput, quartzJobInfo);

            return zdhSqlInfo;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }


    public static void DynamicParams(Map<String, Object> map, QuartzJobInfo quartzJobInfo, EtlTaskInfo etlTaskInfo, SqlTaskInfo sqlTaskInfo) {
        String date_nodash = DateUtil.formatNodash(quartzJobInfo.getLast_time());
        String date_time = DateUtil.formatTime(quartzJobInfo.getLast_time());
        String date_dt = DateUtil.format(quartzJobInfo.getLast_time());
        if (etlTaskInfo != null) {
            final String filter = etlTaskInfo.getData_sources_filter_input().replace("zdh.date.nodash", date_nodash).
                    replace("zdh.date.time", date_time).
                    replace("zdh.date", date_dt);
            final String clear = etlTaskInfo.getData_sources_clear_output().replace("zdh.date.nodash", date_nodash).
                    replace("zdh.date.time", date_time).
                    replace("zdh.date", date_dt);
            map.forEach((k, v) -> {
                logger.info("key:" + k + ",value:" + v);
                etlTaskInfo.setData_sources_filter_input(filter.replace(k, v.toString()));
                etlTaskInfo.setData_sources_clear_output(clear.replace(k, v.toString()));
            });
        }

        if (sqlTaskInfo != null) {
            final String etl_sql = sqlTaskInfo.getEtl_sql().replace("zdh.date.nodash", date_nodash).
                    replace("zdh.date.time", date_time).
                    replace("zdh.date", date_dt);
            final String clear = sqlTaskInfo.getData_sources_clear_output().replace("zdh.date.nodash", date_nodash).
                    replace("zdh.date.time", date_time).
                    replace("zdh.date", date_dt);

            map.forEach((k, v) -> {
                logger.info("key:" + k + ",value:" + v);
                sqlTaskInfo.setEtl_sql(etl_sql.replace(k, v.toString()));
                sqlTaskInfo.setData_sources_clear_output(clear.replace(k, v.toString()));
            });
        }

    }


    /**
     * 获取后台url
     *
     * @param zdhHaInfoMapper
     * @return
     */
    public static String getZdhUrl(ZdhHaInfoMapper zdhHaInfoMapper) {
        logger.info("获取后台处理URL");
        String url = "http://127.0.0.1:60001/api/v1/zdh";
        List<ZdhHaInfo> zdhHaInfoList = zdhHaInfoMapper.selectByStatus("enabled");
        if (zdhHaInfoList != null && zdhHaInfoList.size() == 1) {
            url = zdhHaInfoList.get(0).getZdh_url();
        }
        return url;
    }


    /**
     * 插入日志
     *
     * @param job_id
     * @param level
     * @param msg
     */
    public static void insertLog(String job_id, String level, String msg) {

        ZdhLogs zdhLogs = new ZdhLogs();
        zdhLogs.setJob_id(job_id);
        Timestamp lon_time = new Timestamp(new Date().getTime());
        zdhLogs.setLog_time(lon_time);
        zdhLogs.setMsg(msg);
        zdhLogs.setLevel(level.toUpperCase());
        //linkedBlockingDeque.add(zdhLogs);
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        zdhLogsService.insert(zdhLogs);
    }


    public static Boolean sendZdh(String task_logs_id, String model_log, Boolean exe_status, QuartzJobInfo quartzJobInfo) {
        logger.info("开始发送信息到zdh处理引擎");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        EtlTaskService etlTaskService = (EtlTaskService) SpringContext.getBean("etlTaskServiceImpl");
        DataSourcesServiceImpl dataSourcesServiceImpl = (DataSourcesServiceImpl) SpringContext.getBean("dataSourcesServiceImpl");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        ZdhHaInfoMapper zdhHaInfoMapper = (ZdhHaInfoMapper) SpringContext.getBean("zdhHaInfoMapper");
        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");
        ZdhNginxMapper zdhNginxMapper = (ZdhNginxMapper) SpringContext.getBean("zdhNginxMapper");
        EtlMoreTaskMapper etlMoreTaskMapper = (EtlMoreTaskMapper) SpringContext.getBean("etlMoreTaskMapper");
        SqlTaskMapper sqlTaskMapper = (SqlTaskMapper) SpringContext.getBean("sqlTaskMapper");
        String params = quartzJobInfo.getParams().trim();
        String url = getZdhUrl(zdhHaInfoMapper);
        JSONObject json = new JSONObject();
        if (!params.equals("")) {
            logger.info(model_log + " JOB ,参数不为空判断是否有url 参数");
            String value = JSON.parseObject(params).getString("url");
            if (value != null && !value.equals("")) {
                url = value;
            }
            json = JSON.parseObject(params);
        }
        System.out.println("========fdsfsf=========" + quartzJobInfo.getLast_time());
        String date = DateUtil.formatTime(quartzJobInfo.getLast_time());
        json.put("ETL_DATE", date);
        logger.info(model_log + " JOB ,处理当前日期,传递参数ETL_DATE 为" + date);
        quartzJobInfo.setParams(json.toJSONString());

        logger.info(model_log + " JOB ,获取当前的[url]:" + url);

        ZdhMoreInfo zdhMoreInfo = new ZdhMoreInfo();
        ZdhInfo zdhInfo = new ZdhInfo();
        ZdhSqlInfo zdhSqlInfo = new ZdhSqlInfo();
        if (quartzJobInfo.getMore_task().equals("多源ETL")) {
            logger.info("组装多源ETL任务信息");
            zdhMoreInfo = create_more_task_zdhInfo(quartzJobInfo, quartzJobMapper, etlTaskService, dataSourcesServiceImpl, zdhNginxMapper, etlMoreTaskMapper);
        } else if (quartzJobInfo.getMore_task().equals("单源ETL")) {
            logger.info("组装单源ETL任务信息");
            zdhInfo = create_zhdInfo(quartzJobInfo, quartzJobMapper, etlTaskService, dataSourcesServiceImpl, zdhNginxMapper, etlMoreTaskMapper);
        } else {
            logger.info("组装SQL任务信息");
            zdhSqlInfo = create_zhdSqlInfo(quartzJobInfo, quartzJobMapper, sqlTaskMapper, dataSourcesServiceImpl, zdhNginxMapper);

        }
        TaskLogs taskLogs = taskLogsMapper.selectByPrimaryKey(task_logs_id);
        try {
            if (exe_status == true) {
                logger.info(model_log + " JOB ,开始发送ETL处理请求");
                zdhInfo.setTask_logs_id(task_logs_id);
                zdhMoreInfo.setTask_logs_id(task_logs_id);
                zdhSqlInfo.setTask_logs_id(task_logs_id);
                insertLog(quartzJobInfo.getJob_id(), "DEBUG", "[调度平台]:" + model_log + " JOB ,开始发送ETL处理请求");
                taskLogs.setEtl_date(date);
                taskLogs.setProcess("10");
                taskLogs.setUpdate_time(new Timestamp(new Date().getTime()));
                updateTaskLog(taskLogs, taskLogsMapper);

                if (quartzJobInfo.getMore_task().equals("多源ETL")) {
                    logger.info("[调度平台]:" + url + "/more,参数:" + JSON.toJSONString(zdhMoreInfo));
                    insertLog(zdhMoreInfo.getQuartzJobInfo().getJob_id(), "DEBUG", "[调度平台]:" + url + "/sql,参数:" + JSON.toJSONString(zdhMoreInfo));
                    HttpUtil.postJSON(url + "/more", JSON.toJSONString(zdhMoreInfo));
                } else if (quartzJobInfo.getMore_task().equals("单源ETL")) {
                    logger.info("[调度平台]:" + url + "参数:" + JSON.toJSONString(zdhInfo));
                    insertLog(zdhInfo.getQuartzJobInfo().getJob_id(), "DEBUG", "[调度平台]:" + url + "/sql,参数:" + JSON.toJSONString(zdhInfo));
                    HttpUtil.postJSON(url, JSON.toJSONString(zdhInfo));
                } else {
                    logger.info("[调度平台]:" + url + "/sql,参数:" + JSON.toJSONString(zdhSqlInfo));
                    insertLog(zdhSqlInfo.getQuartzJobInfo().getJob_id(), "DEBUG", "[调度平台]:" + url + "/sql,参数:" + JSON.toJSONString(zdhSqlInfo));
                    HttpUtil.postJSON(url + "/sql", JSON.toJSONString(zdhSqlInfo));
                }
                logger.info(model_log + " JOB ,更新调度任务状态为etl");
                quartzJobInfo.setLast_status("etl");
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (quartzJobInfo.getMore_task().equals("多源ETL")) {
                insertLog(zdhMoreInfo.getQuartzJobInfo().getJob_id(), "ERROR", "[调度平台]:" + model_log + " JOB ,开始发送ETL处理请求," + e.getMessage());
            } else {
                insertLog(zdhInfo.getQuartzJobInfo().getJob_id(), "ERROR", "[调度平台]:" + model_log + " JOB ,开始发送ETL处理请求," + e.getMessage());
            }
            taskLogs.setStatus("error");
            taskLogs.setProcess("15");
            taskLogs.setUpdate_time(new Timestamp(new Date().getTime()));
            updateTaskLog(taskLogs, taskLogsMapper);
            //更新执行状态为error
            logger.info(model_log + " JOB ,更新调度任务状态为error");
            quartzJobInfo.setLast_status("error");
            logger.error(e.getMessage());
            exe_status = false;
        }

        return exe_status;

    }

    public static void create_sql_file(QuartzJobInfo quartzJobInfo, SqlTaskInfo sqlTaskInfo) {
        try {
            String system = System.getProperty("os.name");
            String[] str = sqlTaskInfo.getEtl_sql().split("\r\n");
            String newcommand = "";
            String line = System.getProperty("line.separator");
            for (String s : str) {
                newcommand = newcommand + s + line;
            }

            String fileName = sqlTaskInfo.getSql_context();
            if (system.toLowerCase().startsWith("win")) {
                fileName = fileName + ".bat";
            } else {
                fileName = fileName + ".sh";
            }

            File file = new File("shell_script/" + quartzJobInfo.getJob_id());
            if (!file.exists()) {
                file.mkdirs();
            }

            File file2 = new File("shell_script/" + quartzJobInfo.getJob_id() + "/" + fileName);
            if (file2.exists()) {
                file2.delete();
            }
            file2.createNewFile();

            logger.info("生成脚本临时文件:" + file2.getAbsolutePath());
            logger.info("脚本内容:" + line + newcommand);
            BufferedWriter fileWritter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file2.getAbsolutePath(), true), "UTF-8"));
            fileWritter.write(newcommand);
            fileWritter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static TaskLogs insertTaskLog(String task_logs_id, QuartzJobInfo quartzJobInfo, String etl_date, String status, String process, TaskLogsMapper taskLogsMapper) {

        Timestamp tm = new Timestamp(new Date().getTime());
        TaskLogs taskLogs = new TaskLogs();
        taskLogs.setId(task_logs_id);
        taskLogs.setJob_id(quartzJobInfo.getJob_id());
        taskLogs.setJob_context(quartzJobInfo.getJob_context());
        taskLogs.setEtl_date(etl_date);
        taskLogs.setStatus(status);
        taskLogs.setStart_time(tm);
        taskLogs.setUpdate_time(tm);
        taskLogs.setOwner(quartzJobInfo.getOwner());
        taskLogs.setProcess(process);
        taskLogsMapper.insert(taskLogs);
        return taskLogs;
    }

    public static void updateTaskLog(TaskLogs taskLogs, TaskLogsMapper taskLogsMapper) {
        taskLogsMapper.updateByPrimaryKey(taskLogs);
    }

    /**
     * 更新任务日志,etl_date
     *
     * @param taskLogs
     * @param quartzJobInfo
     * @param taskLogsMapper
     */
    public static void updateTaskLogEtlDate(TaskLogs taskLogs, QuartzJobInfo quartzJobInfo, TaskLogsMapper taskLogsMapper) {
        String date = DateUtil.formatTime(quartzJobInfo.getLast_time());
        taskLogs.setEtl_date(date);
        taskLogs.setUpdate_time(new Timestamp(new Date().getTime()));
        updateTaskLog(taskLogs, taskLogsMapper);
    }


    public static void updateTaskLog12(String task_logs_id, TaskLogsMapper taskLogsMapper) {
        TaskLogs taskLogs = taskLogsMapper.selectByPrimaryKey(task_logs_id);
        taskLogs.setUpdate_time(new Timestamp(new Date().getTime()));
        taskLogs.setStatus("error");
        taskLogs.setProcess("12");//调度完成
        updateTaskLog(taskLogs, taskLogsMapper);
    }

    public static void updateTaskLogError(String task_logs_id, String process, TaskLogsMapper taskLogsMapper) {
        TaskLogs taskLogs = taskLogsMapper.selectByPrimaryKey(task_logs_id);
        taskLogs.setUpdate_time(new Timestamp(new Date().getTime()));
        taskLogs.setStatus("error");
        taskLogs.setProcess(process);//调度完成
        updateTaskLog(taskLogs, taskLogsMapper);
    }

    /**
     * 检查任务依赖
     *
     * @param jobType
     * @param quartzJobInfo
     * @param taskLogsMapper
     */
    public static boolean checkDep(String jobType, QuartzJobInfo quartzJobInfo, TaskLogsMapper taskLogsMapper) {
        //检查任务依赖
        String job_ids = quartzJobInfo.getJob_ids();
        if (job_ids != null && job_ids.split(",").length > 0) {
            for (String dep_job_id : job_ids.split(",")) {
                String etl_date = DateUtil.formatTime(quartzJobInfo.getLast_time());
                List<TaskLogs> taskLogsList = taskLogsMapper.selectByIdEtlDate(getUser().getId(), dep_job_id, etl_date);
                if (taskLogsList == null || taskLogsList.size() <= 0) {
                    String msg = "[" + jobType + "] JOB ,依赖任务" + dep_job_id + ",ETL日期" + etl_date + ",未完成";
                    logger.info(msg);
                    insertLog(quartzJobInfo.getJob_id(), "INFO", msg);
                    return false;
                }
                String msg2 = "[" + jobType + "] JOB ,依赖任务" + dep_job_id + ",ETL日期" + etl_date + ",已完成";
                logger.info(msg2);
                insertLog(quartzJobInfo.getJob_id(), "INFO", msg2);
            }

        }
        return true;
    }

    /**
     * 检查任务状态,并修改参数(任务执行日期等)
     *
     * @param jobType
     * @param quartzJobInfo
     * @param taskLogsMapper
     * @param quartzManager2
     * @return
     */
    public static boolean checkStatus(String jobType, QuartzJobInfo quartzJobInfo, TaskLogsMapper taskLogsMapper, QuartzManager2 quartzManager2) {
        //第一次 last_time 为空 赋值start_time
        if (quartzJobInfo.getLast_time() == null) {
            quartzJobInfo.setLast_time(quartzJobInfo.getStart_time());
        }

        //last_status 表示 finish,etl,error
        //finish 表示成功,etl 表示正在处理,error 表示失败
        if (quartzJobInfo.getLast_status() != null &&
                (quartzJobInfo.getLast_status().equals("etl") || quartzJobInfo.getLast_status().equals("dispatch"))) {
            logger.info("[" + jobType + "] JOB ,当前任务正在处理中");
            insertLog(quartzJobInfo.getJob_id(), "INFO", "[" + jobType + "] JOB ,当前任务正在处理中");
            return false;
        }


        //finish 状态 last_time 增加步长
        if (quartzJobInfo.getLast_status() != null && quartzJobInfo.getLast_status().equals("finish")) {
            Timestamp last = quartzJobInfo.getLast_time();
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

            //finish成功状态 判断last_time 是否超过结束日期,超过，删除任务,更新状态
            if (DateUtil.add(last, dateType, num).after(quartzJobInfo.getEnd_time())) {
                logger.info("[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,当前任务时间超过结束时间,任务结束");
                quartzJobInfo.setStatus("finish");
                //删除quartz 任务
                quartzManager2.deleteTask(quartzJobInfo, "finish");

                return false;
            }

            if (quartzJobInfo.getStart_time().before(DateUtil.add(last, dateType, num)) ||
                    quartzJobInfo.getStart_time().equals(DateUtil.add(last, dateType, num))) {
                logger.info("[" + jobType + "] JOB,上次执行任务成功,计数新的执行日期:" + DateUtil.add(last, dateType, num));
                insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB,上次执行任务成功,计数新的执行日期:" + DateUtil.add(last, dateType, num));
                quartzJobInfo.setLast_time(DateUtil.add(last, dateType, num));
                quartzJobInfo.setNext_time(DateUtil.add(quartzJobInfo.getLast_time(), dateType, num));
            } else {
                logger.info("[" + jobType + "] JOB,首次执行任务,下次执行日期为起始日期:" + quartzJobInfo.getStart_time());
                insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB,首次执行任务,下次执行日期为起始日期:" + quartzJobInfo.getStart_time());
            }
        }

        //error 状态  last_time 不变继续执行
        if (quartzJobInfo.getLast_status() != null && quartzJobInfo.getLast_status().equals("error")) {
            logger.info("[" + jobType + "] JOB ,上次任务处理失败,将重新执行,上次日期:" + quartzJobInfo.getLast_time());
            //插入日志
            insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,上次任务处理失败,将重新执行,上次日期:" + quartzJobInfo.getLast_time());
        }

        return true;

    }


    /**
     * 时间序列发送etl任务到后台执行
     *
     * @param jobType
     * @param task_logs_id
     * @param exe_status
     * @param quartzJobInfo
     * @param taskLogsMapper
     * @param quartzManager2
     * @return
     */
    public static boolean runTimeSeq(String jobType, String task_logs_id, boolean exe_status, QuartzJobInfo quartzJobInfo, TaskLogsMapper taskLogsMapper, QuartzManager2 quartzManager2) {
        logger.info("[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        exe_status = sendZdh(task_logs_id, "[" + jobType + "]", exe_status, quartzJobInfo);

        if (exe_status) {
            logger.info("[" + jobType + "] JOB ,执行命令成功");
            insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,执行命令成功");

            if (quartzJobInfo.getEnd_time() == null) {
                logger.info("[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                quartzJobInfo.setEnd_time(new Timestamp(new Date().getTime()));
            }

        } else {
            logger.info("发送ETL任务到zdh处理引擎,存在问题");
            updateTaskLogError(task_logs_id, "12", taskLogsMapper);
            quartzJobInfo.setLast_status("error");
        }
        return exe_status;
    }

    /**
     * 执行一次任务发送etl任务到后台执行
     *
     * @param jobType
     * @param task_logs_id
     * @param exe_status
     * @param quartzJobInfo
     * @param taskLogsMapper
     * @param quartzManager2
     * @return
     */
    public static boolean runOnce(String jobType, String task_logs_id, boolean exe_status, QuartzJobInfo quartzJobInfo, TaskLogsMapper taskLogsMapper, QuartzManager2 quartzManager2) {
        logger.info("[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        exe_status = sendZdh(task_logs_id, "[" + jobType + "]", exe_status, quartzJobInfo);

        if (exe_status) {
            logger.info("[" + jobType + "] JOB ,执行命令成功");
            insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,执行命令成功");

            if (quartzJobInfo.getEnd_time() == null) {
                logger.info("[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                quartzJobInfo.setEnd_time(new Timestamp(new Date().getTime()));
            }

            System.out.println("===================================");
            quartzJobInfo.setStatus("finish");
            //delete 里面包含更新
            quartzManager2.deleteTask(quartzJobInfo, "finish");
            //插入日志
            logger.info("[" + jobType + "] JOB ,结束调度任务");
            insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,结束调度任务");

        } else {
            //调度完成,拼接任务信息异常
            updateTaskLogError(task_logs_id, "12", taskLogsMapper);
            quartzJobInfo.setLast_status("error");
            //如果执行失败 next_time 时间不变,last_time 不变
        }
        return exe_status;
    }

    /**
     * 重复执行任务发送etl任务到后台执行
     *
     * @param jobType
     * @param task_logs_id
     * @param exe_status
     * @param quartzJobInfo
     * @param taskLogsMapper
     * @param quartzManager2
     * @return
     */
    public static boolean runRepeat(String jobType, String task_logs_id, boolean exe_status, QuartzJobInfo quartzJobInfo, TaskLogsMapper taskLogsMapper, QuartzManager2 quartzManager2) {
        logger.info("[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
        exe_status = sendZdh(task_logs_id, "[" + jobType + "]", exe_status, quartzJobInfo);
        if (exe_status) {
            logger.info("[" + jobType + "] JOB ,执行命令成功");
            insertLog(quartzJobInfo.getJob_id(), "INFO", "[" + jobType + "] JOB ,执行命令成功");

            if (quartzJobInfo.getEnd_time() == null) {
                logger.info("[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                insertLog(quartzJobInfo.getJob_id(), "INFO", "[" + jobType + "] JOB ,结束日期为空设置当前日期为结束日期");
                quartzJobInfo.setEnd_time(new Timestamp(new Date().getTime()));
            }
        } else {
            //调度完成,发送任务时异常
            updateTaskLogError(task_logs_id, "12", taskLogsMapper);
            quartzJobInfo.setLast_status("error");
        }
        return exe_status;
    }

    public static void jobFail(String jobType, String task_logs_id, QuartzJobInfo quartzJobInfo, TaskLogsMapper taskLogsMapper) {
        logger.info("[" + jobType + "] JOB ,调度命令执行失败未能发往任务到后台ETL执行");
        insertLog(quartzJobInfo.getJob_id(), "info", "[" + jobType + "] JOB ,调度命令执行失败未能发往任务到后台ETL执行");
        //调度时异常
        updateTaskLogError(task_logs_id, "8", taskLogsMapper);
        quartzJobInfo.setLast_status("error");
    }

    public static void resetCount() {

    }

    public static User getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }
}
