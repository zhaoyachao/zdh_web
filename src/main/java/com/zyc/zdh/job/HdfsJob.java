package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.entity.TaskLogInstance;
import com.zyc.zdh.util.DateUtil;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 废弃,查询hdfs 文件是否存在,请使用shell,或者ssh 任务代替
 */
@Deprecated
public class HdfsJob extends JobCommon2 {

    public static String jobType = "HDFS";


    public static Boolean hdfsCommand(TaskLogInstance tli) {
        Boolean exe_status = true;
        //执行命令
        try {
            //当前只支持检查文件是否存在 if [ ! -f "/data/filename" ];then echo "文件不存在"; else echo "true"; fi
            //日期替换zdh.date => yyyy-MM-dd 模式
            //日期替换zdh.date.nodash=> yyyyMMdd 模式
            logger.info("目前支持日期参数3种模式:zdh.date => yyyy-MM-dd ,zdh.date.nodash=> yyyyMMdd " +
                    ",zdh.date.time=> yyyy-MM-dd HH:mm:ss");
            insertLog(tli, "info", "目前支持日期参数3种模式:zdh.date => yyyy-MM-dd ,zdh.date.nodash=> yyyyMMdd " +
                    ",zdh.date.time=> yyyy-MM-dd HH:mm:ss");
            if (tli.getLast_time() == null) {
                //第一次执行,下次执行时间为起始时间+1
                if (tli.getStart_time() == null) {
                    logger.info("[" + jobType + "] JOB ,开始日期为空设置当前日期为开始日期");
                    insertLog(tli, "info", "[" + jobType + "] JOB ,开始日期为空设置当前日期为开始日期");
                    tli.setStart_time(new Timestamp(new Date().getTime()));
                }
                logger.info("上次执行日期,下次执行日期均为空,赋值为:" + tli.getStart_time());
                insertLog(tli, "info", "上次执行日期,下次执行日期均为空,赋值为:" + tli.getStart_time());
                tli.setLast_time(tli.getStart_time());
                tli.setNext_time(tli.getStart_time());
            }
            //hdfs 调用参数配置,调度其他参数
            String params = tli.getParams().trim();

            JSONObject json = new JSONObject();
            if (!params.equals("")) {
                logger.info("[" + jobType + "]" + " JOB ,参数不为空判断是否有url 参数");
                json = JSON.parseObject(params);
            }

            if (!tli.getCommand().trim().equals("")) {
                logger.info("========+++++" + tli.getLast_time());
                String date_nodash = DateUtil.formatNodash(tli.getLast_time());
                String date_time = DateUtil.formatTime(tli.getLast_time());
                String date = DateUtil.format(tli.getLast_time());

                logger.info("[" + jobType + "] JOB ,COMMAND:" + tli.getCommand());
                insertLog(tli, "info", "[" + jobType + "] JOB ,COMMAND:" + tli.getCommand());
                String result = "fail";
                if (tli.getCommand().trim().equals("")) {
                    result = "success";
                } else {
                    Configuration conf = new Configuration();
                    String hadoop_user_name = "root";
                    String fs_defaultFS = "hdfs://localhost:40050";
                    if (json.containsKey("zdh.hadoop.user.name")) {
                        hadoop_user_name = json.getString("zdh.hadoop.user.name");
                    }
                    if (json.containsKey("zdh.fs.defaultFS")) {
                        fs_defaultFS = json.getString("zdh.fs.defaultFS");
                    }

                    Path path = new Path(tli.getCommand().
                            replace("zdh.date.nodash", date_nodash).
                            replace("zdh.date.time", date_time).
                            replace("zdh.date", date));

                    logger.info("[" + jobType + "] JOB ,开始连接hadoop,参数url:" + fs_defaultFS + ",用户:" + hadoop_user_name);
                    insertLog(tli, "info", "[" + jobType + "] JOB ,开始连接hadoop,参数url:" + fs_defaultFS + ",用户:" + hadoop_user_name);

                    FileSystem fs = FileSystem.get(new URI(fs_defaultFS), conf, hadoop_user_name);

                    String[] path_strs = tli.getCommand().
                            replace("zdh.date.nodash", date_nodash).
                            replace("zdh.date.time", date_time).
                            replace("zdh.date", date).split(",");
                    result = "success";
                    for (String path_str : path_strs) {
                        exe_status = fs.exists(new Path(path_str));
                        if (exe_status == false) {
                            result = "fail";
                            break;
                        }
                    }
                }
                logger.info("[" + jobType + "] JOB ,执行结果:" + result.trim());
                insertLog(tli, "info", "[" + jobType + "] JOB ,执行结果:" + result.trim());
                if (!result.trim().contains("success")) {
                    throw new Exception("文件不存在");
                }
            } else {
                logger.info("[" + jobType + "] JOB ,执行命令为空,默认返回成功状态");
                insertLog(tli, "info", "[" + jobType + "] JOB ,执行命令为空,默认返回成功状态");
            }
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常:"+e.getMessage()+", 异常详情:{}";
            logger.error(error, e);
            exe_status = false;
        }
        return exe_status;
    }

}
