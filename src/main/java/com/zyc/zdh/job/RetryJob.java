package com.zyc.zdh.job;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.util.NumberUtil;
import com.zyc.rqueue.RQueueClient;
import com.zyc.rqueue.RQueueManager;
import com.zyc.rqueue.RQueueMode;
import com.zyc.zdh.dao.QuartzJobMapper;
import com.zyc.zdh.dao.TaskGroupLogInstanceMapper;
import com.zyc.zdh.dao.TaskLogInstanceMapper;
import com.zyc.zdh.dao.ZdhHaInfoMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.net.URI;
import java.sql.Timestamp;
import java.util.*;

/**
 * 任务失败重试,自动拉取
 */
public class RetryJob {

    private final static String task_log_status="etl";
    private static Logger logger = LoggerFactory.getLogger(RetryJob.class);

    public static List<ZdhDownloadInfo> zdhDownloadInfos = new ArrayList<>();

    public static void run(QuartzJobInfo quartzJobInfo) {
        try {
            logger.debug("开始检测重试任务...");
            QuartzJobMapper quartzJobMapper = (QuartzJobMapper) SpringContext.getBean("quartzJobMapper");
            TaskLogInstanceMapper tlim=(TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
            TaskGroupLogInstanceMapper tglim = (TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
            ZdhHaInfoMapper zdhHaInfoMapper=(ZdhHaInfoMapper) SpringContext.getBean("zdhHaInfoMapper");
            RedisUtil redisUtil=(RedisUtil) SpringContext.getBean("redisUtil");
            //获取重试的任务
            List<TaskLogInstance> taskLogInstanceList=tlim.selectThreadByStatus2(JobStatus.WAIT_RETRY.getValue());
            for(TaskLogInstance tl :taskLogInstanceList){
                QuartzJobInfo qj= quartzJobMapper.selectByPrimaryKey(tl.getJob_id());
                logger.info("检测到需要重试的任务,添加到重试队列,job_id:" + qj.getJob_id() + ",job_context:" + qj.getJob_context());
                JobCommon2.insertLog(tl, "INFO", "检测到需要重试的任务,添加到重试队列,job_id:" + qj.getJob_id() + ",job_context:" + qj.getJob_context());
                if (!tl.getPlan_count().equals("-1") && tl.getCount()>=Long.parseLong(tl.getPlan_count())) {
                    JobCommon2.insertLog(tl, "INFO", "检测到需要重试的任务,重试次数超过限制,实际重试:" + tl.getCount() + "次,job_id:" + tl.getJob_id() + ",job_context:" + tl.getJob_context());
                    tlim.updateStatusById(JobStatus.ERROR.getValue(), DateUtil.getCurrentTime(),tl.getId());
                    //quartzJobMapper.updateLastStatus(qj.getJob_id(), "error");
                    continue;
                }
                //qj.setLast_status("retry");
                //quartzJobMapper.updateLastStatus(qj.getJob_id(), "retry");//retry表示当前的任务是重试发起的
                tl.setStatus("dispatch");
                tlim.updateStatusById("dispatch",DateUtil.getCurrentTime(),tl.getId());//error表示任务已置为失败
                logger.info("开始执行重试任务,job_id:" + qj.getJob_id() + ",job_context:" + qj.getJob_context());
                //debugInfo(tl);
                //JobCommon.insertLog(tl, "INFO", "开始执行重试任务,job_id:" + qj.getJob_id() + ",job_context:" + qj.getJob_context());
                //tl.setRetry_type("auth");
                //BeanUtils.copyProperties(qj,tl);

                JobCommon2.chooseJobBean(tl);

            }
            if (taskLogInstanceList == null || taskLogInstanceList.isEmpty()) {
                logger.debug("当前没有需要重试的任务");
            }

            //获取dispatch,ETL处理的任务
            List<TaskLogInstance> taskLogsList2=tlim.selectThreadByStatus3();
            List<ZdhHaInfo> zdhHaInfos=zdhHaInfoMapper.selectByStatus("enabled","");

            Map<String,String> zdhHaMap=new HashMap<>();
            for(ZdhHaInfo zdhHaInfo:zdhHaInfos){
                zdhHaMap.put(zdhHaInfo.getId(),"");
            }
            for(TaskLogInstance t2 :taskLogsList2){
                // 此处表示shell,datax,data_web任务在执行中,挂掉需要进行回复
                if((Arrays.asList("shell","email","http").contains(t2.getJob_type().toLowerCase()) || Arrays.asList("datax","datax_web").contains(t2.getMore_task().toLowerCase())) && !StringUtils.isEmpty(t2.getServer_id())
                && ( !redisUtil.exists(t2.getServer_id().split(":")[0]) || !redisUtil.get(t2.getServer_id().split(":")[0]).toString().equalsIgnoreCase(t2.getServer_id()) )
                ){
                    if(t2.getJob_type().equalsIgnoreCase("shell")){
                        if(JobCommon2.check_thread_limit(t2)) {
                            continue;
                        }
                    }
                    logger.info("检测到任务意外死亡,将在本节点自动重试任务,任务id: {}", t2.getId());
                    JobCommon2.insertLog(t2,"INFO","检测到任务意外死亡,将在本节点自动重试任务,任务id: "+t2.getId());
                    t2.setServer_id(JobCommon2.web_application_id);
                    tlim.updateByPrimaryKeySelective(t2);
                    JobCommon2.chooseJobBean(t2);
                    continue;
                }
                if(t2.getStatus().equalsIgnoreCase("dispatch")){
                    //如果调度标识为空则直接跳过
                    if(t2.getServer_id()==null || t2.getServer_id().equalsIgnoreCase("")) {
                        continue;
                    }
                    //判断标识是否有效
                    if(!redisUtil.get(t2.getServer_id().split(":")[0]).toString().equalsIgnoreCase(t2.getServer_id())){
                        //调度异常,进行故障转移,同自动重试
                        logger.info("检测到执行任务的调度器意外死亡,将在本节点自动重试任务,任务id: {}", t2.getId());
                        JobCommon2.insertLog(t2,"INFO","检测到执行任务的调度器意外死亡,将在本节点自动重试任务,任务id: "+t2.getId());
                        t2.setServer_id(JobCommon2.web_application_id);
                        tlim.updateByPrimaryKeySelective(t2);
                        JobCommon2.chooseJobBean(t2);
                    }
                    continue; // dispatch 状态表示 还未执行到具体采集任务 所以直接跳过不进行exector存活判断
                }
                //flink任务监控,在CheckDepJob中实现重新拉起
                if(t2.getMore_task().equalsIgnoreCase(MoreTask.FLINK.getValue())){
                    continue;
                }

                if(t2.getUrl()==null||t2.getUrl().isEmpty()){
                    continue;
                }
                if(zdhHaInfos.size()<1){
                    logger.info("没有可用的执行器,请启动zdh_server.....");
                    continue ;
                }
                //http://ip:port/api/v1/zdh
                String executor=t2.getExecutor();
                TaskGroupLogInstance tgli = tglim.selectByPrimaryKey(t2.getGroup_id());

                String product_code = tgli.getProduct_code();
                String dim_group = tgli.getDim_group();

                String queue_enable = ConfigUtil.getValue(ConfigUtil.ZDH_SPARK_QUEUE_ENABLE, "false");
                String redis_queue_enable = ConfigUtil.getParamUtil().getValue(product_code, ConfigUtil.ZDH_SPARK_QUEUE_ENABLE, "false").toString();
                if(executor!=null && !executor.trim().equalsIgnoreCase("") && !zdhHaMap.containsKey(executor)){
                    //executor 意外死亡需要重新发送任务
                    QuartzJobInfo q2=new QuartzJobInfo();
                    q2.setTask_log_id(t2.getId());
                    q2.setJob_id(t2.getJob_id());
                    //此处再次确认是否正在执行中执行器发生死亡
                    TaskLogInstance second_task_logs=tlim.selectByPrimaryKey(t2.getId());
                    if(second_task_logs.getStatus().equalsIgnoreCase(task_log_status)) {
                        logger.info("检测到执行任务的EXECUTOR意外死亡,将重新选择EXECUTOR执行任务");
                    }
                    JobCommon2.insertLog(t2,"INFO","检测到执行任务的EXECUTOR意外死亡,将重新选择EXECUTOR执行任务");

                    //如果任务超过一定时间且executor死亡,则直接置任务失败
                    String valid_time = ConfigUtil.getParamUtil().getValue(product_code, Const.ZDH_SPARK_SKIP_RETRY_VALID_TIME, "3600").toString();
                    if(!NumberUtil.isLong(valid_time)){
                        valid_time = "3600";
                    }
                    if(cn.hutool.core.date.DateUtil.between(t2.getUpdate_time(), new Date(), DateUnit.SECOND) <= Long.valueOf(valid_time)){
                        ZdhHaInfo zdhHaInfo=JobCommon2.getZdhUrl(zdhHaInfoMapper,t2.getParams());
                        String instance = zdhHaInfo.getZdh_instance();
                        URI old_uri=URI.create(t2.getUrl());
                        String new_authori=URI.create(zdhHaInfo.getZdh_url()).getAuthority();
                        String new_url=old_uri.getScheme()+"://"+new_authori+old_uri.getPath();
                        logger.info("重新发送请求地址:"+new_url+",参数:"+t2.getEtl_info());
                        JobCommon2.insertLog(t2,"INFO","重新发送请求地址:"+new_url+",参数:"+t2.getEtl_info());
                       if(redis_queue_enable.equalsIgnoreCase("true")){
                            //公共参数配置-发送队列
                            String queue = ConfigUtil.getParamUtil().getValue(product_code, ConfigUtil.ZDH_SPARK_QUEUE_PRE_KEY, "redis")+"_"+instance;
                            RQueueClient rQueueClient = RQueueManager.getRQueueClient(queue, RQueueMode.BLOCKQUEUE);
                            rQueueClient.add(t2.getEtl_info());
                        }
//                       else if(queue_enable.equalsIgnoreCase("true")){
//                            //本地参数配置-发送队列
//                            String queue = ConfigUtil.getValue(ConfigUtil.ZDH_SPARK_QUEUE_PRE_KEY, "")+"_"+instance;
//                            RQueueClient rQueueClient = RQueueManager.getRQueueClient(queue, RQueueMode.BLOCKQUEUE);
//                            rQueueClient.add(t2.getEtl_info());
//                        }
                       else{
                            HttpUtil.postJSON(new_url, t2.getEtl_info());
                        }

                        t2.setExecutor(zdhHaInfo.getId());
                        t2.setUrl(new_url);
                        t2.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                        JobCommon2.updateTaskLog(t2, tlim);
                    }else{
                        t2.setStatus(JobStatus.ERROR.getValue());
                        JobCommon2.updateTaskLog(t2, tlim);
                    }

                }

            }


        } catch (Exception e) {
             logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
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
                     logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            }
        }
    }

}
