package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.dao.TaskLogsMapper;
import com.zyc.zdh.entity.QuartzJobInfo;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.util.CommandUtils;
import com.zyc.zdh.util.DateUtil;
import com.zyc.zdh.util.SpringContext;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class HdfsJob  extends JobCommon{

    private static String jobType="HDFS";

    public static void run(QuartzJobInfo quartzJobInfo) {
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        QuartzManager2 quartzManager2=(QuartzManager2)SpringContext.getBean("quartzManager2");
        String task_logs_id=SnowflakeIdWorker.getInstance().nextId()+"";

        insertTaskLog(task_logs_id,quartzJobInfo,null,"etl","5",taskLogsMapper);

        logger.info("开始执行["+jobType+"] JOB");
        insertLog(quartzJobInfo.getJob_id(), "INFO", "开始执行["+jobType+"] JOB");
        //debugInfo(quartzJobInfo);


        //第一次 last_time 为空 赋值start_time
        if(quartzJobInfo.getLast_time()==null){
            quartzJobInfo.setLast_time(quartzJobInfo.getStart_time());
        }

        //last_status 表示 finish,etl,error
        //finish 表示成功,etl 表示正在处理,error 表示失败
        if (quartzJobInfo.getLast_status() != null && quartzJobInfo.getLast_status().equals("etl")) {
            logger.info("["+jobType+"] JOB ,当前任务正在处理中");
            insertLog(quartzJobInfo.getJob_id(), "INFO", "["+jobType+"] JOB ,当前任务正在处理中");
            return;
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
                logger.info("["+jobType+"] JOB ,当前任务时间超过结束时间,任务结束");
                insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,当前任务时间超过结束时间,任务结束");
                quartzJobInfo.setStatus("finish");
                //删除quartz 任务
                quartzManager2.deleteTask(quartzJobInfo, "finish");

                return;
            }

            if(quartzJobInfo.getStart_time().before(DateUtil.add(last, dateType, num)) ||
                    quartzJobInfo.getStart_time().equals(DateUtil.add(last, dateType, num)) ){
                logger.info("上次执行任务成功,计数新的执行日期:"+DateUtil.add(last, dateType, num));
                insertLog(quartzJobInfo.getJob_id(), "info", "上次执行任务成功,计数新的执行日期:"+DateUtil.add(last, dateType, num));
                quartzJobInfo.setLast_time(DateUtil.add(last, dateType, num));
                quartzJobInfo.setNext_time(DateUtil.add(quartzJobInfo.getLast_time(), dateType, num));
            }else{
                logger.info("上次执行任务成功,下次执行日期为起始日期:"+quartzJobInfo.getStart_time());
                insertLog(quartzJobInfo.getJob_id(), "info", "上次执行任务成功,下次执行日期为起始日期:"+quartzJobInfo.getStart_time());
            }
        }

        //error 状态  last_time 不变继续执行
        if (quartzJobInfo.getLast_status() != null && quartzJobInfo.getLast_status().equals("error")) {
            logger.info("["+jobType+"] JOB ,上次任务处理失败,将重新执行,上次日期:"+quartzJobInfo.getLast_time());
            //插入日志
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,上次任务处理失败,将重新执行,上次日期:"+quartzJobInfo.getLast_time());
        }

        if (quartzJobInfo.getJob_model().equals(JobModel.TIME_SEQ.getValue())) {
            runTimeSeq(quartzJobInfo,task_logs_id);
        } else if (quartzJobInfo.getJob_model().equals(JobModel.ONCE.getValue())) {
            runOnce(quartzJobInfo,task_logs_id);
        } else if (quartzJobInfo.getJob_model().equals(JobModel.REPEAT.getValue())) {
            runRepeat(quartzJobInfo,task_logs_id);
        }


    }

    private static Boolean hdfsCommand(QuartzJobInfo quartzJobInfo) {
        Boolean exe_status = true;
        //执行命令
        try {
            //当前只支持检查文件是否存在 if [ ! -f "/data/filename" ];then echo "文件不存在"; else echo "true"; fi
            //日期替换zdh.date => yyyy-MM-dd 模式
            //日期替换zdh.date.nodash=> yyyyMMdd 模式
            logger.info("目前支持日期参数3种模式:zdh.date => yyyy-MM-dd ,zdh.date.nodash=> yyyyMMdd " +
                    ",zdh.date.time=> yyyy-MM-dd HH:mm:ss");
            insertLog(quartzJobInfo.getJob_id(), "info", "目前支持日期参数3种模式:zdh.date => yyyy-MM-dd ,zdh.date.nodash=> yyyyMMdd " +
                    ",zdh.date.time=> yyyy-MM-dd HH:mm:ss");
            if (quartzJobInfo.getLast_time() == null) {
                //第一次执行,下次执行时间为起始时间+1
                if (quartzJobInfo.getStart_time() == null) {
                    logger.info("["+jobType+"] JOB ,开始日期为空设置当前日期为开始日期");
                    insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,开始日期为空设置当前日期为开始日期");
                    quartzJobInfo.setStart_time(new Timestamp(new Date().getTime()));
                }
                logger.info("上次执行日期,下次执行日期均为空,赋值为:" + quartzJobInfo.getStart_time());
                insertLog(quartzJobInfo.getJob_id(), "info", "上次执行日期,下次执行日期均为空,赋值为:" + quartzJobInfo.getStart_time());
                quartzJobInfo.setLast_time(quartzJobInfo.getStart_time());
                quartzJobInfo.setNext_time(quartzJobInfo.getStart_time());
            }
            //hdfs 调用参数配置,调度其他参数
            String params = quartzJobInfo.getParams().trim();

            JSONObject json=new JSONObject();
            if (!params.equals("")) {
                logger.info("["+jobType+"]"+" JOB ,参数不为空判断是否有url 参数");
                json = JSON.parseObject(params);
            }

            if (!quartzJobInfo.getCommand().trim().equals("")) {
                logger.info("========+++++" + quartzJobInfo.getLast_time());
                String date_nodash = DateUtil.formatNodash(quartzJobInfo.getLast_time());
                String date_time = DateUtil.formatTime(quartzJobInfo.getLast_time());
                String date = DateUtil.format(quartzJobInfo.getLast_time());

                logger.info("["+jobType+"] JOB ,COMMAND:" + quartzJobInfo.getCommand());
                insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,COMMAND:" + quartzJobInfo.getCommand());
                String result = "fail";
                if (quartzJobInfo.getCommand().trim().equals("")) {
                    result = "success";
                } else {
                    Configuration conf = new Configuration();
                    String hadoop_user_name="root";
                    String fs_defaultFS="hdfs://localhost:40050";
                    if(json.containsKey("zdh.hadoop.user.name")){
                        hadoop_user_name=json.getString("zdh.hadoop.user.name");
                    }
                    if(json.containsKey("zdh.fs.defaultFS")){
                        fs_defaultFS =json.getString("zdh.fs.defaultFS");
                    }

                    Path path=new Path(quartzJobInfo.getCommand().
                            replace("zdh.date.nodash", date_nodash).
                            replace("zdh.date.time", date_time).
                            replace("zdh.date", date));

                    logger.info("["+jobType+"] JOB ,开始连接hadoop,参数url:"+fs_defaultFS+",用户:"+hadoop_user_name);
                    insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,开始连接hadoop,参数url:"+fs_defaultFS+",用户:"+hadoop_user_name);

                    FileSystem fs = FileSystem.get(new URI(fs_defaultFS),conf,hadoop_user_name);

                    String[] path_strs=quartzJobInfo.getCommand().
                            replace("zdh.date.nodash", date_nodash).
                            replace("zdh.date.time", date_time).
                            replace("zdh.date", date).split(",");
                    result = "success";
                    for( String path_str : path_strs){
                        exe_status = fs.exists(new Path(path_str));
                        if(exe_status==false){
                            result="fail";
                            break;
                        }
                    }
                }
                logger.info("["+jobType+"] JOB ,执行结果:" + result.trim());
                insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,执行结果:" + result.trim());
                if (!result.trim().contains("success")) {
                    throw new Exception("文件不存在");
                }
            } else {
                logger.info("["+jobType+"] JOB ,执行命令为空,默认返回成功状态");
                insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,执行命令为空,默认返回成功状态");
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            exe_status = false;
        }
        return exe_status;
    }

    /**
     * 执行时间序列任务,会根据配置的起始时间和结束时间 按天执行
     *
     * @param quartzJobInfo
     */
    private static void runTimeSeq(QuartzJobInfo quartzJobInfo,String task_logs_id) {
        logger.info("["+jobType+"] JOB,任务模式为[时间序列]");
        insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB,任务模式为[时间序列]");
        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");

        Boolean is_count = true;
        //判断次数
        if(quartzJobInfo.getLast_status()==null || quartzJobInfo.getLast_status().equals("finish")){
            quartzJobInfo.setCount(0);
        }
        quartzJobInfo.setCount(quartzJobInfo.getCount() + 1);
        boolean end=isCount(""+jobType+"", quartzManager2, quartzJobInfo);
        if(end==true){
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,结束调度任务");
            return;
        }

        if (quartzJobInfo.getPlan_count().trim().equals("-1")) {
            logger.info("["+jobType+"] JOB ,当前任务未设置执行次数限制");
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,当前任务未设置执行次数限制");
        }
        Boolean exe_status = false;
        exe_status = hdfsCommand(quartzJobInfo);

        //拼接任务信息发送请求
        if (exe_status) {
            logger.info("["+jobType+"] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
            exe_status = sendZdh(task_logs_id,"["+jobType+"]", exe_status, quartzJobInfo);

            if (exe_status) {
                logger.info("["+jobType+"] JOB ,执行命令成功");
                insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,执行命令成功");

                if (quartzJobInfo.getEnd_time() == null) {
                    logger.info("["+jobType+"] JOB ,结束日期为空设置当前日期为结束日期");
                    insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,结束日期为空设置当前日期为结束日期");
                    quartzJobInfo.setEnd_time(new Timestamp(new Date().getTime()));
                }

            }else{
                logger.info("发送ETL任务到zdh处理引擎,存在问题");
                updateTaskLogError(task_logs_id,"12",taskLogsMapper);
                quartzJobInfo.setLast_status("error");
            }
        } else {
            logger.info("["+jobType+"] JOB ,调度命令执行失败未能发往任务到后台ETL执行");
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,调度命令执行失败未能发往任务到后台ETL执行");
            updateTaskLogError(task_logs_id,"8",taskLogsMapper);
            quartzJobInfo.setLast_status("error");
        }

        //更新任务信息
        debugInfo(quartzJobInfo);

        quartzJobMapper.updateByPrimaryKey(quartzJobInfo);

    }


    /**
     * 执行一次任务
     *
     * @param quartzJobInfo
     */
    private static void runOnce(QuartzJobInfo quartzJobInfo,String task_logs_id) {

        logger.info("["+jobType+"] JOB,任务模式为[ONCE]");
        insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB,任务模式为[ONCE]");
        Boolean is_count = true;
        //判断次数
        quartzJobInfo.setCount(quartzJobInfo.getCount() + 1);
        if (!quartzJobInfo.getPlan_count().trim().equals("") && !quartzJobInfo.getPlan_count().trim().equals("-1")) {
            //任务有次数限制,满足添加说明这是最后一次任务
            System.out.println(quartzJobInfo.getCount() + "================" + quartzJobInfo.getPlan_count().trim());
            if (quartzJobInfo.getCount() > Long.parseLong(quartzJobInfo.getPlan_count().trim())) {
                logger.info("["+jobType+"] JOB 检测到任务次数超过限制,直接返回结束");
                insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB 检测到任务次数超过限制,直接返回结束");
                return;
            }
            if (quartzJobInfo.getCount() == Long.parseLong(quartzJobInfo.getPlan_count().trim())) {
                logger.info("["+jobType+"] JOB ,当前执行的任务是最后一次任务,设置调度任务的状态为[finish]");
                insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,当前执行的任务是最后一次任务,设置调度任务的状态为[finish]");
                quartzJobInfo.setStatus("finish");
            }

        }

        if (quartzJobInfo.getPlan_count().trim().equals("-1")) {
            logger.info("["+jobType+"] JOB ,当前任务未设置执行次数限制");
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,当前任务未设置执行次数限制");
        }
        Boolean exe_status = false;
        exe_status = hdfsCommand(quartzJobInfo);
        //拼接任务信息发送请求

        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");

        if (exe_status) {
            logger.info("["+jobType+"] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
            exe_status = sendZdh(task_logs_id,"["+jobType+"]", exe_status, quartzJobInfo);

            if (exe_status) {
                logger.info("["+jobType+"] JOB ,执行命令成功");
                insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,执行命令成功");

                if (quartzJobInfo.getEnd_time() == null) {
                    logger.info("["+jobType+"] JOB ,结束日期为空设置当前日期为结束日期");
                    insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,结束日期为空设置当前日期为结束日期");
                    quartzJobInfo.setEnd_time(new Timestamp(new Date().getTime()));
                }

                System.out.println("===================================");
                quartzJobInfo.setStatus("finish");
                //delete 里面包含更新
                quartzManager2.deleteTask(quartzJobInfo, "finish");
                //插入日志
                logger.info("["+jobType+"] JOB ,结束调度任务");
                insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,结束调度任务");
            } else {
                updateTaskLogError(task_logs_id,"12",taskLogsMapper);
                quartzJobInfo.setLast_status("error");
                //如果执行失败 next_time 时间不变,last_time 不变
            }

        } else {
            logger.info("["+jobType+"] JOB ,调度命令执行失败未能发往任务到后台ETL执行");
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,调度命令执行失败未能发往任务到后台ETL执行");
            updateTaskLogError(task_logs_id,"8",taskLogsMapper);
            quartzJobInfo.setLast_status("error");
        }
        //更新任务信息
        debugInfo(quartzJobInfo);
        quartzJobMapper.updateByPrimaryKey(quartzJobInfo);
    }


    /**
     * 重复执行
     *
     * @param quartzJobInfo
     */
    private static void runRepeat(QuartzJobInfo quartzJobInfo,String task_logs_id) {

        logger.info("["+jobType+"] JOB,任务模式为[重复执行模式]");
        insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB,任务模式为[重复执行模式]");

        QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");
        QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        TaskLogsMapper taskLogsMapper = (TaskLogsMapper) SpringContext.getBean("taskLogsMapper");


        //判断次数
        quartzJobInfo.setCount(quartzJobInfo.getCount() + 1);

        boolean end=isCount(""+jobType+"", quartzManager2, quartzJobInfo);
        if(end==true){
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,结束调度任务");
            return;
        }

        if (quartzJobInfo.getPlan_count().trim().equals("-1")) {
            logger.info("["+jobType+"] JOB ,当前任务未设置执行次数限制,此任务将一直执行");
            insertLog(quartzJobInfo.getJob_id(), "INFO", "["+jobType+"] JOB ,当前任务未设置执行次数限制,此任务将一直执行");
        }
        Boolean exe_status = false;
        exe_status = hdfsCommand(quartzJobInfo);

        //拼接任务信息发送请求
        if (exe_status) {
            logger.info("["+jobType+"] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,调度命令执行成功,准备发往任务到后台ETL执行");
            exe_status = sendZdh(task_logs_id,"["+jobType+"]", exe_status, quartzJobInfo);
            if (exe_status) {
                logger.info("["+jobType+"] JOB ,执行命令成功");
                insertLog(quartzJobInfo.getJob_id(), "INFO", "["+jobType+"] JOB ,执行命令成功");

                if (quartzJobInfo.getEnd_time() == null) {
                    logger.info("["+jobType+"] JOB ,结束日期为空设置当前日期为结束日期");
                    insertLog(quartzJobInfo.getJob_id(), "INFO", "["+jobType+"] JOB ,结束日期为空设置当前日期为结束日期");
                    quartzJobInfo.setEnd_time(new Timestamp(new Date().getTime()));
                }
            }else{
                updateTaskLogError(task_logs_id,"12",taskLogsMapper);
                quartzJobInfo.setLast_status("error");
            }

        } else {
            logger.info("["+jobType+"] JOB ,调度命令执行失败未能发往任务到后台ETL执行");
            insertLog(quartzJobInfo.getJob_id(), "info", "["+jobType+"] JOB ,调度命令执行失败未能发往任务到后台ETL执行");
            updateTaskLogError(task_logs_id,"8",taskLogsMapper);
            quartzJobInfo.setLast_status("error");
        }

        //更新任务信息
        debugInfo(quartzJobInfo);

        //如果执行失败 next_time 时间不变,last_time 不变
        quartzJobMapper.updateByPrimaryKey(quartzJobInfo);


    }
}
