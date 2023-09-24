package com.zyc.zdh.run;

import cn.hutool.core.util.StrUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.controller.SystemController;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.*;
import com.zyc.zdh.monitor.Sys;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.queue.Message;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.shiro.session.mgt.SimpleSession;
import org.quartz.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Component
public class SystemCommandLineRunner implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(SystemCommandLineRunner.class);

    public ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 50, 60, TimeUnit.HOURS, new LinkedBlockingQueue<>(), new ThreadFactory() {

        @Override
        public Thread newThread(Runnable r) {
            Thread thread=new Thread(r);
            thread.setName("zdh_schedule_"+thread.getId());
            return thread;
        }
    });
    @Autowired
    QuartzManager2 quartzManager2;

    @Autowired
    QuartzJobMapper quartzJobMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    public Environment ev;

    @Override
    public void run(String... strings) throws Exception {
        clearQueue();
        runSnowflakeIdWorker();
        runRetryMQ();
        killJobGroup();
        quartzExecutor();
        init2Ip();
        scheduleByCurrentServer();
        removeValidSession();
        logger.info("初始化通知事件");
        EmailJob.notice_event();
        initSentinelRule();
        logger.info("初始化失败任务监控程序");
        //检测是否有email 任务 如果没有则添加
        QuartzJobInfo qj = new QuartzJobInfo();
        qj.setJob_type("EMAIL");
        List<QuartzJobInfo> quartzJobInfos = quartzJobMapper.selectByJobType("EMAIL");
        if (quartzJobInfos.size() > 0) {
            logger.info("已经存在[EMAIL]历史监控任务...");
        }else{
            logger.info("自动生成监控任务");
            String expr = ev.getProperty("email.schedule.interval");
            QuartzJobInfo quartzJobInfo = quartzManager2.createQuartzJobInfo("EMAIL", JobModel.REPEAT.getValue(), new Date(), new Date(), "检查告警任务", expr, "-1", "", "email");
            quartzJobInfo.setJob_id(SnowflakeIdWorker.getInstance().nextId() + "");
            quartzManager2.addQuartzJobInfo(quartzJobInfo);
            quartzManager2.addTaskToQuartz(quartzJobInfo);
        }

        //检测是否有email 任务 如果没有则添加
        QuartzJobInfo qj_retry = new QuartzJobInfo();
        qj_retry.setJob_type("RETRY");
        List<QuartzJobInfo> quartzJobInfos2 = quartzJobMapper.selectByJobType("RETRY");
        if (quartzJobInfos2.size() > 0) {
            logger.info("已经存在[RETRY]历史监控任务...");
        }else{
            logger.info("自动生成监控任务");
            String expr = ev.getProperty("retry.schedule.interval");
            QuartzJobInfo quartzJobInfo = quartzManager2.createQuartzJobInfo("RETRY", JobModel.REPEAT.getValue(), new Date(), new Date(), "检查失败重试任务", expr, "-1", "", "retry");
            quartzJobInfo.setJob_id(SnowflakeIdWorker.getInstance().nextId() + "");
            quartzManager2.addQuartzJobInfo(quartzJobInfo);
            quartzManager2.addTaskToQuartz(quartzJobInfo);
        }

        //检测是否有check_dep 任务 如果没有则添加
        QuartzJobInfo qj_check = new QuartzJobInfo();
        qj_retry.setJob_type("CHECK");
        List<QuartzJobInfo> quartzJobInfos3 = quartzJobMapper.selectByJobType("CHECK");
        if (quartzJobInfos3.size() > 0) {
            logger.info("已经存在[CHECK]历史监控任务...");
        }else{
            logger.info("自动生成监控任务");
            String expr = "30s" ;//ev.getProperty("retry.schedule.interval");
            QuartzJobInfo quartzJobInfo = quartzManager2.createQuartzJobInfo("CHECK", JobModel.REPEAT.getValue(), new Date(), new Date(), "检查依赖任务", expr, "-1", "", "retry");
            quartzJobInfo.setJob_id(SnowflakeIdWorker.getInstance().nextId() + "");
            quartzManager2.addQuartzJobInfo(quartzJobInfo);
            quartzManager2.addTaskToQuartz(quartzJobInfo);
        }
    }

    public void clearQueue(){
        logger.info("初始化限流队列");
        ZdhRunableTask zdhRunableTask=new ZdhRunableTask(){

            @Override
            public void run() {
                while (true) {
                    try {
                        Message take = IpUtil.queue.take();
                        if(redisUtil.exists(take.getBody())){
                            redisUtil.increment(take.getBody(),-1L);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public String name() {
                return "schedule ratelimit";
            }
        };
        threadPool.submit(createThread(zdhRunableTask));
    }

    public void runSnowflakeIdWorker() {
        logger.info("初始化分布式id生成器");
        //获取服务id
        String myid = ev.getProperty("myid", "0");
        String instance=ev.getProperty("instance","zdh_web");
        JobCommon2.myid=myid;
        SnowflakeIdWorker.init(Integer.parseInt(myid), 0);
        JobCommon2.web_application_id=instance+"_"+myid+":"+SnowflakeIdWorker.getInstance().nextId();
        //检查基础参数是否重复
        if(redisUtil.get(instance+"_"+myid)!=null){
            logger.error("请检查基础参数myid 是否已被其他机器占用,如果没有请等待30s 后重新启动....");
            System.exit(-1);
        }
        ZdhRunableTask zdhRunableTask=new ZdhRunableTask(){

            @Override
            public void run() {
                Thread.currentThread().setName("自定义线程名称");
                while(true){
                    try {
                        //此处设置10s 超时 ,quartz 故障检测时间为30s
                        redisUtil.set(instance+"_"+myid,JobCommon2.web_application_id,10L, TimeUnit.SECONDS);
                        //此处设置2s 每2秒向redis 设置一个当前服务,作为一个心跳检测使用
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
                        logger.error(error, e);
                    }
                }
            }

            @Override
            public String name() {
                return "schedule id generator task";
            }
        };
        threadPool.submit(createThread(zdhRunableTask));
    }

    public void runLogMQ(){
        logger.info("初始化日志");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        JobCommon2.logThread(zdhLogsService);
    }

    public void runRetryMQ(){
        logger.info("初始化重试队列事件");
        // JobCommon.retryThread();
    }

    public void killJobGroup(){
        logger.info("初始化监控杀死任务");
        TaskGroupLogInstanceMapper taskGroupLogInstanceMapper = (TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
        TaskLogInstanceMapper taskLogInstanceMapper = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        ZdhHaInfoMapper zdhHaInfoMapper = (ZdhHaInfoMapper) SpringContext.getBean("zdhHaInfoMapper");
        String myid = ev.getProperty("myid", "0");

        ZdhRunableTask zdhRunableTask=new ZdhRunableTask(){

            @Override
            public void run() {
                while(true){
                    try {
                        logger.debug("检查要杀死的任务组..");
                        List<TaskLogInstance> tlis=taskLogInstanceMapper.selectThreadByStatus("kill");
                        for(TaskLogInstance tl : tlis){

                            if(tl.getThread_id()!=null && tl.getThread_id().startsWith(myid)){
                                Thread td=JobCommon2.chm.get(tl.getThread_id());
                                if(td!=null){
                                    String msg="杀死线程:线程名:"+td.getName()+",线程id:"+td.getId();
                                    logger.info(msg);
                                    JobCommon2.insertLog(tl,"INFO",msg);
                                    if(tl.getMore_task().equalsIgnoreCase("ssh") || tl.getMore_task().equalsIgnoreCase("datax") || tl.getJob_type().equalsIgnoreCase("flume")){
                                        SSHUtil sshUtil =JobCommon2.chm_ssh.get(tl.getId());
                                        if(sshUtil!=null){
                                            String[] connectUri= sshUtil.createUri();
                                            sshUtil.logout();
                                            JobCommon2.chm_ssh.get(tl.getId()).logout();
                                            JobCommon2.chm_ssh.remove(tl.getId());
                                            if(connectUri.length==2 && !StringUtils.isEmpty(connectUri[1])){
                                                try{
                                                    String kill_cmd=String.format("kill -9 `ps -ef |grep '%s' |awk -F \" \" '{print $2}'`",connectUri[1]);
                                                    JobCommon2.insertLog(tl,"INFO",kill_cmd);
                                                    SshUtils.kill(connectUri[0],kill_cmd);
                                                }catch (Exception e){
                                                    String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
                                                    logger.error(error, e);
                                                }

                                            }
                                        }
                                    }
                                    try{
                                        td.interrupt();
                                        td.stop();

                                    }catch (Exception e){
                                        String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
                                        logger.error(error, e);
                                    }finally {
                                        JobCommon2.chm.remove(tl.getThread_id());
                                        taskLogInstanceMapper.updateStatusById("killed",DateUtil.getCurrentTime(),tl.getId());
                                        JobCommon2.insertLog(tl,"INFO","已杀死当前任务");
                                    }
                                }else{
                                    String msg="调度部分已经执行完成,ETL部分正在执行提交到后端的任务进行杀死";
                                    logger.info(msg);
                                    JobCommon2.insertLog(tl,"INFO",msg);
                                    List<NameValuePair> npl=new ArrayList<>();

                                    //如果是flink任务,需要调用远程地址杀死
                                    if(tl.getMore_task().equalsIgnoreCase(MoreTask.FLINK.getValue())){
                                        //如果找不到flink_job_id,历史服务器,则跳过
                                        if(StringUtils.isEmpty(tl.getApplication_id()) || StringUtils.isEmpty(tl.getHistory_server())){
                                            if(System.currentTimeMillis()-tl.getUpdate_time().getTime() > 2*60*1000){
                                                taskLogInstanceMapper.updateStatusById("killed",DateUtil.getCurrentTime(),tl.getId());
                                            }
                                            continue;
                                        }

                                        String cancel_url = tl.getHistory_server()+"/jobs/"+tl.getApplication_id()+"/yarn-cancel";
                                        npl.add(new BasicNameValuePair("mode","cancel"));
                                        JobCommon2.insertLog(tl,"INFO","杀死任务url: "+cancel_url);
                                        try{
                                            String restul=HttpUtil.getRequest(cancel_url,npl);
                                        }catch (Exception e){
                                            JobCommon2.insertLog(tl,"INFO","杀死当前任务异常,判定服务以死亡,自动更新状态为killed");
                                            taskLogInstanceMapper.updateStatusById("killed",DateUtil.getCurrentTime(),tl.getId());
                                            continue;
                                        }
                                        taskLogInstanceMapper.updateStatusById("killed",DateUtil.getCurrentTime(),tl.getId());
                                        JobCommon2.insertLog(tl,"INFO","已杀死当前任务");
                                        continue;
                                    }else{

                                    }

                                    String executor=tl.getExecutor();//数据采集机器id
                                    ZdhHaInfo zdhHaInfo=zdhHaInfoMapper.selectByPrimaryKey(executor);
                                    String jobGroup="jobGroup";
                                    if(zdhHaInfo!=null){
                                        String url="http://"+zdhHaInfo.getZdh_host()+":"+zdhHaInfo.getWeb_port()+"/api/v1/applications/"+zdhHaInfo.getApplication_id()+"/jobs";
                                        //获取杀死的任务名称
                                        System.out.println(url);
                                        //npl.add(new BasicNameValuePair("status","running"));
                                        String restul="";
                                        try{
                                            restul=HttpUtil.getRequest(url,npl);
                                        }catch (Exception e){
                                            taskLogInstanceMapper.updateStatusById("killed",DateUtil.getCurrentTime(),tl.getId());
                                            continue;
                                        }

                                        JSONArray jsonArray= JSON.parseArray(restul);
                                        List<String> killJobs=new ArrayList<>();
                                        for(Object jo:jsonArray){
                                            JSONObject j=(JSONObject) jo;
                                            if(j.getString(jobGroup).startsWith(tl.getId())){
                                                killJobs.add(j.getString(jobGroup));
                                            }
                                        }

                                        JSONObject js=new JSONObject();
                                        js.put("task_logs_id",tl.getId());//写日志使用
                                        js.put("jobGroups",killJobs);
                                        js.put("job_id",tl.getJob_id());
                                        //发送杀死请求
                                        String kill_url="http://"+zdhHaInfo.getZdh_host()+":"+zdhHaInfo.getZdh_port()+"/api/v1/kill";
                                        HttpUtil.postJSON(kill_url,js.toJSONString());
                                        taskLogInstanceMapper.updateStatusById("killed",DateUtil.getCurrentTime(),tl.getId());
                                    }else{
                                        String msg2="无法获取具体执行器,判断任务已杀死";
                                        taskLogInstanceMapper.updateStatusById("killed",DateUtil.getCurrentTime(),tl.getId());
                                        logger.info(msg2);
                                        JobCommon2.insertLog(tl,"INFO",msg2);
                                    }

                                }
                            }
                        }
                        // List<QuartzJobInfo> quartzJobInfos = quartzJobMapper.select(qj);
                        Thread.sleep(1000*2);
                    } catch (Exception e) {
                        String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
                        logger.error(error, e);
                    }
                }
            }

            @Override
            public String name() {
                return "schedule kill job task";
            }
        };
        threadPool.submit(createThread(zdhRunableTask));
    }

    public void quartzExecutor(){
        ZdhRunableTask zdhRunableTask=new ZdhRunableTask(){

            @Override
            public void run() {
                while(true){
                    try {
                        SchedulerFactoryBean schedulerFactoryBean = (SchedulerFactoryBean) SpringContext.getBean("&schedulerFactoryBean");
                        QuartzExecutorMapper quartzExecutorMapper = (QuartzExecutorMapper) SpringContext.getBean("quartzExecutorMapper");
                        QrtzSchedulerStateMapper qrtzSchedulerStateMapper=(QrtzSchedulerStateMapper) SpringContext.getBean("qrtzSchedulerStateMapper");
                        logger.debug("检查要操作的Quartz Executor..");
                        String instance_name = schedulerFactoryBean.getScheduler().getSchedulerInstanceId();
                        QuartzExecutorInfo qei=new QuartzExecutorInfo();
                        qei.setIs_handle("false");
                        qei.setInstance_name(instance_name);
                        List<QuartzExecutorInfo> quartzExecutorInfos = quartzExecutorMapper.select(qei);
                        for (QuartzExecutorInfo q:quartzExecutorInfos){
                            if(q.getStatus().equalsIgnoreCase("online")){
                                schedulerFactoryBean.start();
                            }
                            if(q.getStatus().equalsIgnoreCase("offline")){
                                schedulerFactoryBean.stop();
                            }
                            q.setIs_handle(Const.TRUR);
                            q.setUpdate_time(new Timestamp(new Date().getTime()));
                            //更新上下线任务已处理
                            quartzExecutorMapper.updateByPrimaryKey(q);


                            //更新executor状态
                            QrtzSchedulerState qss=new QrtzSchedulerState();
                            qss.setStatus(q.getStatus());
                            Example example=new Example(qss.getClass());
                            Example.Criteria criteria = example.createCriteria();
                            criteria.andEqualTo("instance_name", instance_name);
                            qrtzSchedulerStateMapper.updateByExampleSelective(qss, example);
                        }

                        //更新正在执行中的任务数
                        int running = JobCommon2.chm.size()+ JobCommon2.chm_ssh.size();
                        Example example=new Example(QrtzSchedulerState.class);
                        Example.Criteria criteria=example.createCriteria();
                        if(!StringUtils.isEmpty(instance_name)){
                            criteria.andEqualTo("instance_name", instance_name);
                            QrtzSchedulerState qss=new QrtzSchedulerState();
                            qss.setRunning(String.valueOf(running));
                            qrtzSchedulerStateMapper.updateByExampleSelective(qss, example);
                        }
                        Thread.sleep(1000*2);
                    } catch (Exception e) {
                        String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[2].getMethodName();
                        e.printStackTrace();
                        logger.error(error, e);
                    }
                }
            }

            @Override
            public String name() {
                return "schedule check executor task";
            }
        };
        threadPool.submit(createThread(zdhRunableTask));
    }

    public void init2Ip(){
        logger.info("初始化IP库");
        String dbPath = ev.getProperty("ip2region.path","");
        //IpUtil.init(dbPath);
    }

    public void scheduleByCurrentServer(){
        ZdhRunableTask zdhRunableTask = new ZdhRunableTask(){

            @Override
            public void run() {
                Scheduler scheduler = (Scheduler) SpringContext.getBean("scheduler");
                QrtzSchedulerStateMapper qrtzSchedulerStateMapper = (QrtzSchedulerStateMapper) SpringContext.getBean("qrtzSchedulerStateMapper");
                while (true) {
                    try {
                        String instanceId = scheduler.getSchedulerInstanceId();
                        redisUtil.set("schedule_"+instanceId,instanceId,50L, TimeUnit.SECONDS);
                        Example example=new Example(QrtzSchedulerState.class);
                        Example.Criteria criteria=example.createCriteria();
                        criteria.andEqualTo("instance_name", instanceId);
                        criteria.andEqualTo("status", "online");

                        List<QrtzSchedulerState> qrtzSchedulerStates = qrtzSchedulerStateMapper.selectByExample(example);
                        if(qrtzSchedulerStates == null || qrtzSchedulerStates.size()<=0){
                            Thread.sleep(1000*60);
                            continue;
                        }
                        //根据instanceId获取指定调度器任务
                        CheckDepJob.run_sub_task(instanceId);
                        Thread.sleep(1000*30);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public String name() {
                return "schedule current server task";
            }
        };
        threadPool.submit(createThread(zdhRunableTask));
    }

    /**
     * 初始化限流控制
     */
    public void initSentinelRule(){
        try{
            String product_code = ev.getProperty("zdp.product");
            ZdhRunableTask zdhRunableTask=new ZdhRunableTask(){
                @Override
                public void run() {

                    while (true){

                        try {
                            ResourceTreeMapper resourceTreeMapper=(ResourceTreeMapper) SpringContext.getBean("resourceTreeMapper");

                            Example example=new Example(ResourceTreeInfo.class);
                            Example.Criteria criteria=example.createCriteria();
                            criteria.andEqualTo("product_code", product_code);

                            List<ResourceTreeInfo> rtis = resourceTreeMapper.selectByExample(example);
                            rtis.sort(Comparator.comparing(ResourceTreeInfo::getOrderN));
                            List<FlowRule> rules = new ArrayList<>();

                            for (ResourceTreeInfo rti: rtis){
                                if(StringUtils.isEmpty(rti.getQps()) || !StringUtils.isNumeric(rti.getQps())){
                                    continue ;
                                }
                                FlowRule rule = new FlowRule();
                                String url = rti.getUrl();
                                if(url.startsWith("/")){
                                    url = url.substring(1);
                                }
                                if(url.contains(".")){
                                    url = url.split("\\.")[0];
                                }
                                rule.setResource(url);
                                rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
                                // Set limit QPS to 20.
                                rule.setCount(Integer.valueOf(rti.getQps()));
                                rules.add(rule);
                            }
                            logger.info("限流重置规则: "+ JSON.toJSONString(rules));
                            FlowRuleManager.loadRules(rules);

                            Thread.sleep(1000*60);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public String name() {
                    return "schedule init sentinel rule";
                }
            };

            threadPool.submit(zdhRunableTask);
        }catch (Exception e){

        }
    }
    @SentinelResource("test")
    public void test(){
        System.out.println("====");
    }

    public void removeValidSession(){
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {
                RedisUtil redisUtil=(RedisUtil) SpringContext.getBean("redisUtil");
                try{
                    while (true){
                        Set<String> sets = redisUtil.scan("shiro:cache:shiro-activeSessionCache1*");
                        for(String key:sets){
                            if( !((SimpleSession) redisUtil.get(key)).isValid()){
                                logger.info("检测到过期session: "+ JSON.toJSONString(redisUtil.get(key)));
                                redisUtil.remove(key);
                            }
                        }
                        Thread.sleep(1000*60*10);
                    }

                }catch (Exception e){
                    logger.info("检测到过期session异常: "+e.getMessage());
                }
            }
        });
       t.start();
    }

    public Thread createThread(ZdhRunableTask zdhRunableTask){
        Thread thread=new Thread(zdhRunableTask);
        thread.setName(zdhRunableTask.name());
        return thread;
    }

    public interface ZdhRunableTask extends Runnable{
        public String name();
    }
}
