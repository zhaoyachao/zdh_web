package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.lib.fn.ELFunctionDefinition;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.EtlTaskService;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.*;
import org.apache.commons.beanutils.BeanUtils;
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
import org.apache.shiro.SecurityUtils;
import org.quartz.TriggerUtils;
import org.quartz.impl.triggers.CronTriggerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.FileCopyUtils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.*;

public class JobDigitalMarket {

    public static Logger logger = LoggerFactory.getLogger(JobDigitalMarket.class);

    public static String myid = "";

    //instance_myid:SnowflakeIdWorker
    public static String web_application_id = "";

    public static LinkedBlockingDeque<ZdhLogs> linkedBlockingDeque = new LinkedBlockingDeque<ZdhLogs>();

    public static ConcurrentHashMap<String, Thread> chm = new ConcurrentHashMap<String, Thread>();
    public static ConcurrentHashMap<String, SSHUtil> chm_ssh = new ConcurrentHashMap<String, SSHUtil>();

    public static DelayQueue<RetryJobInfo> retryQueue = new DelayQueue<>();

    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1, 1000, 500, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

    public static void logThread(ZdhLogsService zdhLogsService) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        ZdhLogs log = JobDigitalMarket.linkedBlockingDeque.take();
                        if (log != null) {
                            zdhLogsService.insert(log);
                        }
                    } catch (Exception e) {
                        String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
                        logger.error(error, e);
                    }
                }

            }
        }).start();
    }


    public static void updateTaskLog(StrategyGroupInstance sgi, StrategyGroupInstanceMapper sgim) {
        // debugInfo(tgli);
        sgim.updateByPrimaryKey(sgi);
    }

    public static void updateTaskLog(StrategyInstance si, StrategyInstanceMapper sim) {
        // debugInfo(tgli);
        sim.updateByPrimaryKey(si);
    }

    public static void insertLog(StrategyGroupInstance sgi, String level, String msg) {

        ZdhLogs zdhLogs = new ZdhLogs();
        zdhLogs.setJob_id(sgi.getStrategy_group_id());
        Timestamp lon_time = new Timestamp(new Date().getTime());
        zdhLogs.setTask_logs_id(sgi.getId());
        zdhLogs.setLog_time(lon_time);
        zdhLogs.setMsg(msg);
        zdhLogs.setLevel(level.toUpperCase());
        //linkedBlockingDeque.add(zdhLogs);

        RedisUtil redisUtil=(RedisUtil) SpringContext.getBean("redisUtil");
        Object logType=redisUtil.get("zdh_log_type");

        if(logType == null || logType.toString().equalsIgnoreCase(Const.LOG_MYSQL)){
            ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
            zdhLogsService.insert(zdhLogs);
        }else if(logType.toString().equalsIgnoreCase(Const.LOG_MONGODB)){
            MongoTemplate mongoTemplate = (MongoTemplate) SpringContext.getBean("mongoTemplate");
            mongoTemplate.insert(zdhLogs);
        }
    }

    public static void insertLog(StrategyInstance si, String level, String msg) {

        ZdhLogs zdhLogs = new ZdhLogs();
        zdhLogs.setJob_id(si.getStrategy_id());
        Timestamp lon_time = new Timestamp(new Date().getTime());
        zdhLogs.setTask_logs_id(si.getId());
        zdhLogs.setLog_time(lon_time);
        zdhLogs.setMsg(msg);
        zdhLogs.setLevel(level.toUpperCase());
        //linkedBlockingDeque.add(zdhLogs);
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        zdhLogsService.insert(zdhLogs);
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
                    String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
                    logger.error(error, e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}", e);
            }
        }
    }



    /**
     * 根据时间(timestamp) 生成jinjava 模板中的时间参数
     *
     * @param tli
     * @return
     */
    public static Map<String, Object> getJinJavaParam(StrategyGroupInstance tli) {
        String msg = "目前支持日期参数以下模式: {{zdh_date}} => yyyy-MM-dd ,{{zdh_date_nodash}}=> yyyyMMdd " +
                ",{{zdh_date_time}}=> yyyy-MM-dd HH:mm:ss,{{zdh_year}}=> 年,{{zdh_month}}=> 月,{{zdh_day}}=> 日," +
                "{{zdh_hour}}=>24小时制,{{zdh_minute}}=>分钟,{{zdh_second}}=>秒,{{zdh_time}}=>时间戳";
        logger.info(msg);
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
        jinJavaParam.put("zdh_time", cur_time.getTime());
        jinJavaParam.put("zdh_task_log_id", tli.getId());

        return jinJavaParam;

    }

    public static void DynamicParams(Map<String, Object> map, StrategyGroupInstance tli, Object taskInfo) {
        try {
            Map<String, Object> jinJavaParam = getJinJavaParam(tli);

            Jinjava jj = new Jinjava();

            jj.getGlobalContext().registerFunction(new ELFunctionDefinition("", "add_day",
                    DateUtil.class, "addDay", String.class, Integer.class));
            jj.getGlobalContext().registerFunction(new ELFunctionDefinition("", "add_hour",
                    DateUtil.class, "addHour", String.class, Integer.class));
            jj.getGlobalContext().registerFunction(new ELFunctionDefinition("", "add_minute",
                    DateUtil.class, "addMinute", String.class, Integer.class));

            String msg = "目前支持日期操作: {{add_day('2021-12-01 00:00:00', 1)}} => 2021-12-02 00:00:00 ,{{add_hour('2021-12-01 00:00:00', 1)}} => 2021-12-01 01:00:00,{{add_minute('2021-12-01 00:00:00', 1)}} => 2021-12-01 00:01:00";
            logger.info(msg);
            insertLog(tli, "info", msg);

            map.forEach((k, v) -> {
                logger.info("key:" + k + ",value:" + v);
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
                }

            }
            if (taskInfo instanceof EtlTaskFlinkInfo) {
                EtlTaskFlinkInfo etlTaskFlinkInfo = (EtlTaskFlinkInfo) taskInfo;
                if (etlTaskFlinkInfo != null) {
                    final String etl_sql = jj.render(etlTaskFlinkInfo.getEtl_sql(), jinJavaParam);
                    final String command = jj.render(etlTaskFlinkInfo.getCommand(), jinJavaParam);
                    etlTaskFlinkInfo.setEtl_sql(etl_sql);
                    etlTaskFlinkInfo.setCommand(command);
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
                }
            }

            if (taskInfo instanceof EtlTaskUnstructureInfo) {
                EtlTaskUnstructureInfo etlTaskUnstructureInfo = (EtlTaskUnstructureInfo) taskInfo;
                if (etlTaskUnstructureInfo != null) {
                    final String input_path = jj.render(etlTaskUnstructureInfo.getInput_path(), jinJavaParam);
                    final String output_path = jj.render(etlTaskUnstructureInfo.getOutput_path(), jinJavaParam);
                    etlTaskUnstructureInfo.setInput_path(input_path);
                    etlTaskUnstructureInfo.setOutput_path(output_path);
                }
            }

        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            throw e;
        }


    }

    public static String DynamicParams(Map<String, Object> map, StrategyGroupInstance tli, String old_str) {
        try {
            Map<String, Object> jinJavaParam = getJinJavaParam(tli);

            Jinjava jj = new Jinjava();

            if (map != null) {
                map.forEach((k, v) -> {
                    logger.info("key:" + k + ",value:" + v);
                    jinJavaParam.put(k, v);
                });
            }

            String new_str = jj.render(old_str, jinJavaParam);

            return new_str;
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            throw e;
        }


    }

    /**
     * 选择具体的job执行引擎,只有调度和手动重试触发(手动执行部分请参见其他方法)
     * 解析任务组时间->解析创建任务组实例->创建子任务实例
     *
     * @param strategyGroupInfo
     * @param is_retry      0:调度,2:手动重试
     * @param sub_tasks     重试的子任务divId,如果全部重试可传null,或者所有的divId
     */
    public static void chooseJobBean(StrategyGroupInfo strategyGroupInfo, int is_retry, StrategyGroupInstance retry_sgi, String[] sub_tasks) {
        StrategyGroupMapper sgm = (StrategyGroupMapper) SpringContext.getBean("strategyGroupMapper");

        StrategyGroupInstanceMapper sgim = (StrategyGroupInstanceMapper) SpringContext.getBean("strategyGroupInstanceMapper");
        //手动重试增加重试实例,自动重试在原来的基础上
        //线程池执行具体调度任务
        threadPoolExecutor.execute(new Runnable() {
                @Override
                public void run() {

                    StrategyGroupInstance sgi = new StrategyGroupInstance();
                    try {
                        //复制quartzjobinfo到tli,任务基础信息完成复制
                        BeanUtils.copyProperties(sgi, strategyGroupInfo);
                        sgi.setId(SnowflakeIdWorker.getInstance().nextId() + "");
                        //逻辑发送错误代码捕获发生自动重试(retry_job) 不重新生成实例id,使用旧的实例id
                        String last_task_id = "";
                        if (is_retry == 0) {
                            //调度触发,直接获取执行时间
                            Timestamp cur = getCurTime(strategyGroupInfo);
                            sgi.setCur_time(cur);
                            sgi.setSchedule_source(ScheduleSource.SYSTEM.getCode());
                            sgi.setRun_time(new Timestamp(new Date().getTime()));//实例开始时间
                            sgi.setUpdate_time(new Timestamp(new Date().getTime()));
                        }
                        if (is_retry == 2) {
                            //手动点击重试按钮触发
                            //手动点击重试,会生成新的实例信息,默认重置执行次数,并将上次执行失败的实例id 付给last_task_id
                            sgi = retry_sgi;
                            //retry_sgii.setProcess("1");
                            sgi.setSchedule_source(ScheduleSource.MANUAL.getCode());
                            sgi.setRun_time(new Timestamp(new Date().getTime()));//实例开始时间
                            sgi.setUpdate_time(new Timestamp(new Date().getTime()));
                            //retry_sgii.setCount(0L);
                            //retry_sgii.setIs_retryed("0");
                            sgi.setId(SnowflakeIdWorker.getInstance().nextId() + "");
                            sgi.setStatus(JobStatus.NON.getValue());
                        }

                        //此处更新主要是为了 日期超时时 也能记录下日志
                        //insertLog(tgli, "INFO", "生成任务组信息,任务组数据处理日期:" + tgli.getEtl_date());
                        sgim.insert(sgi);

                        sgm.updateByPrimaryKey(strategyGroupInfo);
                        //公共设置
                        sgi.setStatus(JobStatus.NON.getValue());//新实例状态设置为dispatch
                        //设置调度器唯一标识,调度故障转移时使用,如果服务器重启会自动生成新的唯一标识
                        //tgli.setServer_id(JobCommon2.web_application_id);

                        //todo 生成具体任务组下任务实例
                        sub_strategy_instance(sgi, sub_tasks);
                        sgim.updateStatus2Create(new String[]{sgi.getId()});
                        debugInfo(sgi);
                        sgim.updateByPrimaryKey(sgi);

                        //检查任务依赖,和并行不冲突--此逻辑删除,目前任务组之间的依赖以子任务检查逻辑实现
                        //boolean dep = checkDep(quartzJobInfo.getJob_type(), tgli);
                        //更新任务依赖时间
                        //process_time_info pti = tgli.getProcess_time2();
                        //pti.setCheck_dep_time(DateUtil.getCurrentTime());
                        //tgli.setProcess_time(pti);
                        //todo 修改组任务状态,及修改子任务状态为检查依赖中
                        //CheckDepJob.updateTaskGroupLogInstanceStatus(tgli);

                    } catch (IllegalAccessException e) {
                        logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
                    } catch (InvocationTargetException e) {
                        logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
                    } catch (Exception e) {
                        logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
                    }




                }
            });

        //如果调度任务类型是一次性则删除调度
//        if (strategyGroupInfo.getJob_model().equalsIgnoreCase(JobModel.ONCE.getValue())) {
//            quartzManager2.deleteTask(strategyGroupInfo, JobStatus.FINISH.getValue());
//        }
    }

    /**
     * 只管QUARTZ调度触发,手动执行另算
     * 如果使用调度时间,直接赋值即可
     * 如果使用自定义时间,获取上次时间和本次做diff
     */
    public static Timestamp getCurTime(StrategyGroupInfo strategyGroupInfo) throws Exception {
        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        if (strategyGroupInfo.getUse_quartz_time() != null && strategyGroupInfo.getUse_quartz_time().equalsIgnoreCase(Const.ON)) {
            if (!StringUtils.isEmpty(strategyGroupInfo.getTime_diff())) {
                int seconds = 0;
                try {
                    seconds = Integer.parseInt(strategyGroupInfo.getTime_diff());
                } catch (NumberFormatException e) {
                    logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
                }
                return DateUtil.add(strategyGroupInfo.getQuartz_time(), Calendar.SECOND, -seconds);
            }
            return strategyGroupInfo.getQuartz_time();
        }else{
            throw new Exception("目前调度只支持quartz触发时间,不支持自定义时间");
        }


    }


    /**
     * 任务触发后,等待依赖任务完成触发,其他外部均调用此方法执行子任务实例
     *
     * @param sgi
     */
    public static void chooseJobBean(StrategyGroupInstance sgi) {
        QuartzJobMapper qjm = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        StrategyGroupInstanceMapper tlim = (StrategyGroupInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");

        //线程池执行具体调度任务
        //threadPoolExecutor.execute();
        new Thread(new Runnable() {
            @Override
            public void run() {
                sub_strategy_instance(sgi, new String[]{});
            }
        }).start();
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
                dates.add(DateUtil.add(new Timestamp(new Date().getTime()), dateType, num * i));
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
     * 根据sgi 任务组生成具体的子任务实例
     *
     * @param sgi
     * @param sub_tasks 具体执行的子任务divId
     */
    public static List<StrategyInstance> sub_strategy_instance(StrategyGroupInstance sgi, String[] sub_tasks) {
        List<StrategyInstance> siList = new ArrayList<>();
        try {
            StrategyGroupInstanceMapper sgim = (StrategyGroupInstanceMapper) SpringContext.getBean("strategyGroupInstanceMapper");
            StrategyInstanceMapper sim = (StrategyInstanceMapper) SpringContext.getBean("strategyInstanceMapper");
            // divId和实例id映射
            Map<String, String> map = new HashMap<>(); //divId->id
            Map<String, String> map2 = new HashMap<>();//id->divId
            DAG dag = new DAG();
            if (StringUtils.isEmpty(sgi.getJsmind_data())) {
                insertLog(sgi, "ERROR", "无法生成子任务,请检查是否配置有子任务信息!");
                //todo sgim.updateStatusById3(JobStatus.ERROR.getValue(), "100", DateUtil.getCurrentTime(), tgli.getId());
                return siList;
            }
            JSONArray tasks = JSON.parseObject(sgi.getJsmind_data()).getJSONArray("tasks");
            //JSONArray shell=JSON.parseObject(tgli.getJsmind_data()).getJSONArray("shell");
            JSONArray lines = JSON.parseObject(sgi.getJsmind_data()).getJSONArray("line");
            for (Object job : tasks) {
                StrategyInstance si = new StrategyInstance();
                BeanUtils.copyProperties(si, sgi);

                String pageSourceId = ((JSONObject) job).getString("divId");//前端生成的div 标识
                String more_task = ((JSONObject) job).getString("more_task");
                String is_disenable = ((JSONObject) job).getString("is_disenable");
                String depend_level = ((JSONObject) job).getString("depend_level");
                String time_out = ((JSONObject) job).getString("time_out");
                String touch_type = ((JSONObject) job).getString("touch_type");
                String data_node = ((JSONObject) job).getString("data_node");
                if (!StringUtils.isEmpty(time_out)) {
                    //si.setTime_out(time_out);
                }
                if (StringUtils.isEmpty(is_disenable)) {
                    is_disenable = "false";
                }
                if (StringUtils.isEmpty(depend_level)) {
                    depend_level = "0";
                }
                if (StringUtils.isEmpty(data_node)) {
                    data_node = "";
                }

                si.setRun_time(new Timestamp(new Date().getTime()));


                si.setJsmind_data("");
                si.setJsmind_data(JSON.toJSONString(job));
                //si.setJsmind_data(((JSONObject) job).toJSONString());
                si.setRun_jsmind_data(((JSONObject) job).toJSONString());

                String t_id = SnowflakeIdWorker.getInstance().nextId() + "";
                map.put(pageSourceId, t_id);//div标识和任务实例id 对应关系
                map2.put(t_id, pageSourceId);

                si.setId(t_id);//具体执行策略实例id,每次执行都会重新生成
                si.setInstance_type(more_task);
                si.setGroup_instance_id(sgi.getId());//策略组实例id
                si.setGroup_id(sgi.getStrategy_group_id());//策略组id
                si.setGroup_context(sgi.getGroup_context());//策略组任务说明

                si.setStatus(JobStatus.NON.getValue());

                si.setOwner(sgi.getOwner());
                si.setIs_disenable(is_disenable);
                si.setDepend_level(depend_level);
                si.setTouch_type(touch_type);
                si.setData_node(data_node);

                String name = ((JSONObject) job).getString("name");
                si.setStrategy_context(name);
                si.setStrategy_id(pageSourceId);
                siList.add(si);
            }

            JSONArray jary_line = new JSONArray();
            for (Object job : lines) {
                String pageSourceId = ((JSONObject) job).getString("pageSourceId");
                String pageTargetId = ((JSONObject) job).getString("pageTargetId");
                ((JSONObject) job).put("id", map.get(pageSourceId));
                ((JSONObject) job).put("parentid", map.get(pageTargetId));
                if (pageSourceId != null && !pageSourceId.equalsIgnoreCase("root")) {
                    boolean is_loop = dag.addEdge(map.get(pageSourceId), map.get(pageTargetId));//此处的依赖关系 都是生成的任务实例id
                    if (!is_loop) {
                        insertLog(sgi, "ERROR", "无法生成子任务,请检查子任务是否存在相互依赖的任务!");
                        //sgim.updateStatusById3(JobStatus.ERROR.getValue(), "100", DateUtil.getCurrentTime(), tgli.getId());
                        return siList;
                    }

                    JSONObject json_line = new JSONObject();
                    json_line.put("from", map.get(pageSourceId));
                    json_line.put("to", map.get(pageTargetId));
                    jary_line.add(json_line);
                }
            }

            //run_data 结构：run_data:[{task_log_instance_id,etl_task_id,etl_context,more_task}]
            JSONArray jary = new JSONArray();
            for (StrategyInstance si : siList) {
                JSONObject jsonObject1 = new JSONObject();
                String sid = si.getId();
                System.out.println("=======================");

                jsonObject1.put("strategy_instance_id", sid);
                //jsonObject1.put("etl_task_id", tli.getEtl_task_id());
                //jsonObject1.put("etl_context", tli.getEtl_context());
                //jsonObject1.put("more_task", tli.getMore_task());
                //jsonObject1.put("job_type", tli.getJob_type());
                jsonObject1.put("divId", map2.get(sid));
                jary.add(jsonObject1);
            }

            JSONObject jsonObject = JSON.parseObject(sgi.getJsmind_data());
            jsonObject.put("run_data", jary);
            jsonObject.put("run_line", jary_line);
            sgi.setRun_jsmind_data(jsonObject.toJSONString());
            //sgi.setProcess("6.5");
            sgim.updateByPrimaryKey(sgi);
            //debugInfo(tgli);


            //生成实例
            for (StrategyInstance si : siList) {
                String sid = si.getId();
                String next_tasks = StringUtils.join(dag.getChildren(sid).toArray(), ",");
                String pre_tasks = StringUtils.join(dag.getParent(sid), ",");
                si.setNext_tasks(next_tasks);
                si.setPre_tasks(pre_tasks);
                si.setSchedule_source(sgi.getSchedule_source());
                si.setMisfire("0");
                if (sub_tasks == null) {
                    si.setStatus(JobStatus.NON.getValue());
                } else {
                    //不再sub_tasks 中的div 状态设置为跳过状态,并且非禁用状态
                    if (Arrays.asList(sub_tasks).contains(map2.get(sid)) && !si.getIs_disenable().equalsIgnoreCase("true")) {
                        //设置为初始态
                        si.setStatus(JobStatus.NON.getValue());
                    } else {
                        insertLog(si,"INFO", "当前策略实例设置跳过");
                        si.setStatus(JobStatus.SKIP.getValue());
                    }
                }

                sim.insert(si);
                // debugInfo(tli);
            }
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            JobDigitalMarket.insertLog(sgi, "ERROR", "生成子任务失败," + e.getMessage());
        }
        return siList;
    }

}
