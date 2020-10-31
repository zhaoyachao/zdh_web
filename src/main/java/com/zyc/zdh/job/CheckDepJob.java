package com.zyc.zdh.job;

import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.dao.TaskLogInstanceMapper;
import com.zyc.zdh.dao.ZdhHaInfoMapper;
import com.zyc.zdh.entity.QuartzJobInfo;
import com.zyc.zdh.entity.TaskLogInstance;
import com.zyc.zdh.entity.ZdhDownloadInfo;
import com.zyc.zdh.entity.ZdhHaInfo;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.HttpUtil;
import com.zyc.zdh.util.SpringContext;
import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.net.URI;
import java.sql.Timestamp;
import java.util.*;

//定期拉取重试任务
public class CheckDepJob {

    private final static String task_log_status="etl";
    private static Logger logger = LoggerFactory.getLogger(CheckDepJob.class);

    public static List<ZdhDownloadInfo> zdhDownloadInfos = new ArrayList<>();

    public static void run(QuartzJobInfo quartzJobInfo) {
        try {
            logger.info("开始检测依赖任务...");
            TaskLogInstanceMapper taskLogInstanceMapper=(TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
            //获取重试的任务
            List<TaskLogInstance> taskLogInstanceList=taskLogInstanceMapper.selectThreadByStatus("check_dep");
            for(TaskLogInstance tl :taskLogInstanceList){
                if(JobCommon.checkDep(tl.getJob_type(),tl)){
                    String tmp_status=taskLogInstanceMapper.selectByPrimaryKey(tl.getId()).getStatus();
                    if( tmp_status=="kill" || tmp_status =="killed" ) continue; //在检查依赖时杀死任务
                    tl.setStatus("dispatch");
                    tl.setServer_id(JobCommon.web_application_id);//重新设置调度器标识,retry任务会定期检查标识是否有效
                    JobCommon.updateTaskLog(tl,taskLogInstanceMapper);
                    debugInfo(tl);
                    JobCommon.chooseJobBean(tl);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

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

}
