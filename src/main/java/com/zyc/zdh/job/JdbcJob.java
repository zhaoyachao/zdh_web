package com.zyc.zdh.job;

import com.alibaba.fastjson.JSON;
import com.zyc.zdh.entity.TaskLogInstance;
import com.zyc.zdh.util.DBUtil;
import com.zyc.zdh.util.DateUtil;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 废弃,jdbc检查 请使用JobCommon2中check
 */
@Deprecated
public class JdbcJob extends JobCommon2 {

    public static String jobType = "JDBC";

    public static Boolean runCommand(TaskLogInstance tli) {
        try {
            logger.info("开始执行调度命令判断是否可行");
            DBUtil dbUtil = new DBUtil();
            Boolean exe_status = true;
            /// /连接jdbc
            String params = tli.getParams().trim();
            if (params.equals("")) {
                exe_status = false;
                logger.info("参数不可为空,必须包含特定参数,zdh.jdbc.url,zdh.jdbc.driver,zdh.jdbc.username,zdh.jdbc.password");
                insertLog(tli, "error",
                        "参数不可为空,必须包含特定参数,zdh.jdbc.url,zdh.jdbc.driver,zdh.jdbc.username,zdh.jdbc.password");

                return exe_status;
            }
            String url = JSON.parseObject(params).getString("zdh.jdbc.url");
            String driver = JSON.parseObject(params).getString("zdh.jdbc.driver");
            String username = JSON.parseObject(params).getString("zdh.jdbc.username");
            String password = JSON.parseObject(params).getString("zdh.jdbc.password");

            if (url == null || url.equals("") || driver == null || driver.equals("") || username == null || username.equals("") || password == null || password.equals("")) {
                exe_status = false;
                logger.info("[" + jobType + "] JOB ,参数不可为空");
                //插入日志
                insertLog(tli, "error",
                        "参数不可为空,必须包含特定参数,zdh.jdbc.url,zdh.jdbc.driver,zdh.jdbc.username,zdh.jdbc.password");

                return exe_status;
            }

            String command = tli.getCommand();


            if (command.equals("")) {
                exe_status = true;
            }


            if (tli.getLast_time() == null) {
                //第一次执行,下次执行时间为起始时间+1
                if (tli.getStart_time() == null) {
                    logger.info("[" + jobType + "] JOB ,开始日期为空设置当前日期为开始日期");
                    tli.setStart_time(new Timestamp(new Date().getTime()));
                }
                tli.setNext_time(tli.getStart_time());
            }
            String date_nodash = DateUtil.formatNodash(tli.getNext_time());
            String date_time = DateUtil.formatTime(tli.getNext_time());
            String date = DateUtil.format(tli.getNext_time());
            String new_command = command.replace("zdh.date.nodash", date_nodash)
                    .replace("zdh.date.time", date_time)
                    .replace("zdh.date", date);
            List<String> results = dbUtil.R(driver, url, username, password, new_command, null);
            if (results.size() >= 1) {
                exe_status = true;
            }
            return exe_status;
        } catch (Exception e) {
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName(), e.getCause());
            //插入日志
            insertLog(tli, "error",
                    "[调度平台]:" + e.getMessage());
            logger.error(e.getMessage());
            return false;
        }

    }


}
