package com.zyc.zdh.run;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.zyc.rqueue.RQueueManager;
import com.zyc.zdh.dao.*;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.*;
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
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


@Component
public class SystemCommandLineRunner implements CommandLineRunner {

    public static String myid = "";
    //instance_myid:SnowflakeIdWorker
    public static String web_application_id = "";

    public ThreadGroup threadGroup = new ThreadGroup("zdh_init");
    public ThreadPoolExecutor threadPool = new ThreadPoolExecutor(Const.SYSTEM_THREAD_MIN_NUM,
            Const.SYSTEM_THREAD_MAX_NUM, Const.SYSTEM_THREAD_KEEP_ACTIVE_TIME, TimeUnit.HOURS, new LinkedBlockingQueue<>(), new ZdhThreadFactory("zdh_schedule_", threadGroup));
    @Autowired
    QuartzManager2 quartzManager2;

    @Autowired
    QuartzJobMapper quartzJobMapper;

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    public Environment ev;

    @Autowired
    ConfigurableEnvironment cev;

    @Override
    public void run(String... strings) throws Exception {
        String line = "----------------------------------------------------------";
        LogUtil.info(this.getClass(), line);
        LogUtil.info(this.getClass(), "系统初始化...");
        initRedisParams();
        initRQueue();
        clearQueue();
        runSnowflakeIdWorker();
        runRetryMQ();
        killJobGroup();
        quartzExecutor();
        init2Ip();
        scheduleByCurrentServer();
        removeValidSession();

        //EmailJob.notice_event();
        initSentinelRule();
        initBeaconFireConsumer();
        initAlarmEmail();
        initRetry();
        initCheck();

        onlyCheckQuartz();

        LogUtil.info(this.getClass(), "系统初始化完成...");
        LogUtil.info(this.getClass(), line);
        Thread.sleep(1000*2);

    }

    public void initRedisParams(){
        LogUtil.info(this.getClass(), "初始Redis默认参数");
        Iterator<PropertySource<?>> i = cev.getPropertySources().iterator();
        while (i.hasNext()){
            PropertySource n = i.next();
            if(n.getName().contains("applicationConfig")){
                Map<String,Object> config = (Map<String,Object>)n.getSource();
                for (String key: config.keySet()){
                    if(key.contains("zdh.init.redis.")){
                        String k = key.split("zdh.init.redis.")[1];
                        if(!ConfigUtil.getParamUtil().exists(ConfigUtil.getProductCode(), k)){
                            ConfigUtil.getParamUtil().setValue(ConfigUtil.getProductCode(), k, config.get(key).toString());
                        }
                        LogUtil.info(this.getClass(), "zdh init redis config " + k + "====>" + config.get(key).toString());
                    }
                }
            }
        }

        String redisHost = ConfigUtil.getValue(ConfigUtil.SPRING_REDIS_HOSTNAME);
        String redisPort = ConfigUtil.getValue(ConfigUtil.SPRING_REDIS_PORT);
        String redisPassword = ConfigUtil.getValue(ConfigUtil.SPRING_REDIS_PASSWORD);
        if(redisHost.contains(",")){
            RQueueManager.buildDefault(redisHost, redisPassword);
        }else{
            RQueueManager.buildDefault(redisHost+":"+redisPort, redisPassword);
        }
    }

    public void initRQueue(){
        LogUtil.info(this.getClass(), "初始分布式队列RQueue");
        String redisHost = ConfigUtil.getValue(ConfigUtil.SPRING_REDIS_HOSTNAME);
        String redisPort = ConfigUtil.getValue(ConfigUtil.SPRING_REDIS_PORT);
        String redisPassword = ConfigUtil.getValue(ConfigUtil.SPRING_REDIS_PASSWORD);
        if(redisHost.contains(",")){
            RQueueManager.buildDefault(redisHost, redisPassword);
        }else{
            RQueueManager.buildDefault(redisHost+":"+redisPort, redisPassword);
        }
    }

    public void clearQueue(){
        LogUtil.info(this.getClass(), "初始化限流队列");
        ZdhRunableTask zdhRunableTask=new ZdhRunableTask("schedule ratelimit", new Runnable() {
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
        });
        threadPool.submit(zdhRunableTask);
    }

    public void runSnowflakeIdWorker() {
        LogUtil.info(this.getClass(), "初始化分布式id生成器");
        //获取服务id
        String myid = ConfigUtil.getValue(ConfigUtil.MYID, "0");
        String instance=ConfigUtil.getValue(ConfigUtil.INSTANCE, "zdh_web");
        SystemCommandLineRunner.myid=myid;
        SnowflakeIdWorker.init(Integer.parseInt(myid), 0);
        SystemCommandLineRunner.web_application_id=instance+"_"+myid+":"+SnowflakeIdWorker.getInstance().nextId();
        //检查基础参数是否重复
        String myidKey = instance+"_"+myid;
        if(redisUtil.get(myidKey)!=null){
            LogUtil.error(this.getClass(), "请检查基础参数myid 是否已被其他机器占用,如果没有请等待30s 后重新启动,或尝试手动删除key:" + myidKey);
            System.exit(-1);
        }
        ZdhRunableTask zdhRunableTask=new ZdhRunableTask("服务主心跳线程", new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        //此处设置10s 超时 ,quartz 故障检测时间为30s
                        redisUtil.set(myidKey,SystemCommandLineRunner.web_application_id,10L, TimeUnit.SECONDS);
                        //此处设置2s 每2秒向redis 设置一个当前服务,作为一个心跳检测使用
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        LogUtil.error(this.getClass(), e);
                    }
                }
            }
        });
        threadPool.submit(zdhRunableTask);
    }

    public void runLogMQ(){
        LogUtil.info(this.getClass(), "初始化日志");
        ZdhLogsService zdhLogsService = (ZdhLogsService) SpringContext.getBean("zdhLogsServiceImpl");
        JobCommon2.logThread(zdhLogsService);
    }

    public void runRetryMQ(){
        LogUtil.info(this.getClass(), "初始化重试队列事件");
        // JobCommon.retryThread();
    }

    public void killJobGroup(){
        LogUtil.info(this.getClass(), "初始化监控杀死任务");
        TaskGroupLogInstanceMapper taskGroupLogInstanceMapper = (TaskGroupLogInstanceMapper) SpringContext.getBean("taskGroupLogInstanceMapper");
        TaskLogInstanceMapper taskLogInstanceMapper = (TaskLogInstanceMapper) SpringContext.getBean("taskLogInstanceMapper");
        ZdhHaInfoMapper zdhHaInfoMapper = (ZdhHaInfoMapper) SpringContext.getBean("zdhHaInfoMapper");
        String myid = ConfigUtil.getValue(ConfigUtil.MYID, "0");

        ZdhRunableTask zdhRunableTask=new ZdhRunableTask("schedule kill job task", new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        LogUtil.debug(this.getClass(), "检查要杀死的任务组..");
                        List<TaskLogInstance> tlis=taskLogInstanceMapper.selectThreadByStatus("kill");
                        for(TaskLogInstance tl : tlis){

                            if(tl.getThread_id()!=null && tl.getThread_id().startsWith(myid)){
                                Future<?> future = JobCommon2.chm.get(tl.getThread_id());
                                if(future!=null){
                                    String msg="杀死线程:线程名:"+tl.getJob_context()+",任务实例id:"+tl.getId();
                                    LogUtil.info(this.getClass(), msg);
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
                                                    LogUtil.error(this.getClass(), e);
                                                }

                                            }
                                        }
                                    }
                                    try{
                                        future.cancel(true);
                                    }catch (Exception e){
                                        LogUtil.error(this.getClass(), e);
                                    }finally {
                                        JobCommon2.chm.remove(tl.getId());
                                        taskLogInstanceMapper.updateStatusById("killed",DateUtil.getCurrentTime(),tl.getId());
                                        JobCommon2.insertLog(tl,"INFO","已杀死当前任务");
                                    }
                                }else{
                                    String msg="调度部分已经执行完成,ETL部分正在执行提交到后端的任务进行杀死";
                                    LogUtil.info(this.getClass(), msg);
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
                                            JobCommon2.insertLog(tl,"INFO","杀死当前任务异常,判定服务已死亡,自动更新状态为killed");
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

                                        List<Map<String, Object>> jsonArray= JsonUtil.toJavaListMap(restul);
                                        List<String> killJobs=new ArrayList<>();
                                        for(Map<String, Object> jo:jsonArray){
                                            if(jo.getOrDefault(jobGroup, "").toString().startsWith(tl.getId())){
                                                killJobs.add(jo.getOrDefault(jobGroup, "").toString());
                                            }
                                        }

                                        Map<String, Object> js=JsonUtil.createEmptyMap();
                                        js.put("task_logs_id",tl.getId());//写日志使用
                                        js.put("jobGroups",killJobs);
                                        js.put("job_id",tl.getJob_id());
                                        //发送杀死请求
                                        String kill_url="http://"+zdhHaInfo.getZdh_host()+":"+zdhHaInfo.getZdh_port()+"/api/v1/kill";
                                        HttpUtil.postJSON(kill_url, JsonUtil.formatJsonString(js));
                                        taskLogInstanceMapper.updateStatusById("killed",DateUtil.getCurrentTime(),tl.getId());
                                    }else{
                                        String msg2="无法获取具体执行器,判断任务已杀死";
                                        taskLogInstanceMapper.updateStatusById("killed",DateUtil.getCurrentTime(),tl.getId());
                                        LogUtil.info(this.getClass(), msg2);
                                        JobCommon2.insertLog(tl,"INFO",msg2);
                                    }

                                }
                            }
                        }
                        // List<QuartzJobInfo> quartzJobInfos = quartzJobMapper.select(qj);
                        Thread.sleep(1000*2);
                    } catch (Exception e) {
                        LogUtil.error(this.getClass(), e);
                    }
                }
            }
        });
        threadPool.submit(zdhRunableTask);
    }

    public void quartzExecutor(){
        LogUtil.info(this.getClass(), "初始化调度器监控");
        ZdhRunableTask zdhRunableTask=new ZdhRunableTask("schedule check executor task", new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        SchedulerFactoryBean schedulerFactoryBean = (SchedulerFactoryBean) SpringContext.getBean("&schedulerFactoryBean");
                        QuartzExecutorMapper quartzExecutorMapper = (QuartzExecutorMapper) SpringContext.getBean("quartzExecutorMapper");
                        QrtzSchedulerStateMapper qrtzSchedulerStateMapper=(QrtzSchedulerStateMapper) SpringContext.getBean("qrtzSchedulerStateMapper");
                        LogUtil.debug(this.getClass(), "检查要操作的Quartz Executor..");
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
                            q.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                            //更新上下线任务已处理
                            quartzExecutorMapper.updateByPrimaryKeySelective(q);


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
                        LogUtil.error(this.getClass(), e);
                    }
                }
            }
        });
        threadPool.submit(zdhRunableTask);
    }

    public void init2Ip(){
        LogUtil.info(this.getClass(), "初始化IP库");
        String dbPath = ConfigUtil.getValue(ConfigUtil.IP2REGION_PATH,"");
        //IpUtil.init(dbPath);
    }

    public void scheduleByCurrentServer(){
        LogUtil.info(this.getClass(), "初始化当前系统执行器监听任务");
        ZdhRunableTask zdhRunableTask = new ZdhRunableTask("schedule current server task", new Runnable() {
            @Override
            public void run() {
                Scheduler scheduler = (Scheduler) SpringContext.getBean("scheduler");
                QrtzSchedulerStateMapper qrtzSchedulerStateMapper = (QrtzSchedulerStateMapper) SpringContext.getBean("qrtzSchedulerStateMapper");
                while (true) {
                    try {
                        String instanceId = scheduler.getSchedulerInstanceId();
                        //注册调度器心跳信息,用于调度器执行任务之前判断
                        redisUtil.set(Const.ZDH_SCHEDULE_HEART_PRE+instanceId,instanceId,50L, TimeUnit.SECONDS);
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
        });
        threadPool.submit(zdhRunableTask);
    }

    /**
     * 初始化限流控制
     */
    public void initSentinelRule(){
        try{
            LogUtil.info(this.getClass(), "初始化限流插件");
            String product_code = ConfigUtil.getValue(ConfigUtil.ZDP_PRODUCT);
            ZdhRunableTask zdhRunableTask=new ZdhRunableTask("schedule init sentinel rule", new Runnable() {
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
                            LogUtil.debug(this.getClass(), "限流重置规则: " + JsonUtil.formatJsonString(rules));
                            FlowRuleManager.loadRules(rules);

                            Thread.sleep(1000*60);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });

            threadPool.submit(zdhRunableTask);
        }catch (Exception e){

        }
    }
    @SentinelResource("test")
    public void test(){
        System.out.println("====");
    }

    public void removeValidSession(){
        LogUtil.info(this.getClass(), "初始化session失效监听任务");

        ZdhRunableTask zdhRunableTask = new ZdhRunableTask("schedule check session", new Runnable() {

            @Override
            public void run() {
                RedisUtil redisUtil=(RedisUtil) SpringContext.getBean("redisUtil");
                RedisTemplate shirRedisTemplate=(RedisTemplate) SpringContext.getBean("shirRedisTemplate");
                try{
                    while (true){
                        Set<String> sets = redisUtil.scan("shiro:cache:shiro-activeSessionCache1*");
                        for(String key:sets){
                            if( !((SimpleSession) shirRedisTemplate.opsForValue().get(key)).isValid()){
                                LogUtil.info(this.getClass(), "检测到过期session: " + JsonUtil.formatJsonString(redisUtil.get(key)));
                                redisUtil.remove(key);
                            }
                        }
                        Thread.sleep(1000*60*10);
                    }

                }catch (Exception e){
                    LogUtil.info(this.getClass(), "检测到过期session异常: " + e.getMessage());
                }
            }
        });

        threadPool.submit(zdhRunableTask);
    }

    public void initBeaconFireConsumer(){
        try{
            LogUtil.info(this.getClass(), "初始化烽火台监听任务");
            ZdhRunableTask zdhRunableTask=new ZdhRunableTask("schedule init beaconfire consumer", new Runnable() {
                @Override
                public void run() {
                    try {
                        JobBeaconFire.consumer();
                        Thread.sleep(1000*60);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            threadPool.submit(zdhRunableTask);
        }catch (Exception e){

        }
    }

    public void initAlarmEmail() throws Exception {
        LogUtil.info(this.getClass(), "初始化告警任务");
        //检测是否有email 任务 如果没有则添加
        List<QuartzJobInfo> quartzJobInfos = quartzJobMapper.selectByJobType(JobType.EMAIL.getCode());
        if (quartzJobInfos.size() > 0) {
            LogUtil.info(this.getClass(), "已经存在[EMAIL]历史监控任务...");
        }else{
            String expr = ConfigUtil.getValue(ConfigUtil.EMAIL_SCHEDULE_INTERVAL, "30s");
            QuartzJobInfo quartzJobInfo = quartzManager2.createQuartzJobInfo(JobType.EMAIL.getCode(), JobModel.REPEAT.getValue(), new Date(), DateUtil.pase("2999-12-31"), "检查告警任务", expr, "-1", "", JobType.EMAIL.getCode());
            quartzJobInfo.setJob_id(SnowflakeIdWorker.getInstance().nextId() + "");
            quartzManager2.addQuartzJobInfo(quartzJobInfo);
            quartzManager2.addTaskToQuartz(quartzJobInfo);
        }
    }

    public void initRetry() throws Exception {
        LogUtil.info(this.getClass(), "初始化重试任务");
        //检测是否有retry 任务 如果没有则添加
        List<QuartzJobInfo> quartzJobInfos2 = quartzJobMapper.selectByJobType(JobType.RETRY.getCode());
        if (quartzJobInfos2.size() > 0) {
            LogUtil.info(this.getClass(), "已经存在[RETRY]历史监控任务...");
        }else{
            String expr = ConfigUtil.getValue(ConfigUtil.RETRY_SCHEDULE_INTERVAL, "30s");
            QuartzJobInfo quartzJobInfo = quartzManager2.createQuartzJobInfo(JobType.RETRY.getCode(), JobModel.REPEAT.getValue(), new Date(), DateUtil.pase("2999-12-31"), "检查失败重试任务", expr, "-1", "", JobType.RETRY.getCode());
            quartzJobInfo.setJob_id(SnowflakeIdWorker.getInstance().nextId() + "");
            quartzManager2.addQuartzJobInfo(quartzJobInfo);
            quartzManager2.addTaskToQuartz(quartzJobInfo);
        }
    }

    public void initCheck() throws Exception {
        LogUtil.info(this.getClass(), "初始化依赖检查任务");
        //检测是否有check_dep 任务 如果没有则添加
        List<QuartzJobInfo> quartzJobInfos3 = quartzJobMapper.selectByJobType(JobType.CHECK.getCode());
        if (quartzJobInfos3.size() > 0) {
            LogUtil.info(this.getClass(), "已经存在[CHECK]历史监控任务...");
        }else{
            String expr = ConfigUtil.getValue(ConfigUtil.CHECK_SCHEDULE_INTERVAL, "30s");
            QuartzJobInfo quartzJobInfo = quartzManager2.createQuartzJobInfo(JobType.CHECK.getCode(), JobModel.REPEAT.getValue(), new Date(), DateUtil.pase("2999-12-31"), "检查依赖任务", expr, "-1", "", JobType.CHECK.getCode());
            quartzJobInfo.setJob_id(SnowflakeIdWorker.getInstance().nextId() + "");
            quartzManager2.addQuartzJobInfo(quartzJobInfo);
            quartzManager2.addTaskToQuartz(quartzJobInfo);
        }
    }

    public void onlyCheckQuartz(){
        try{
            ZdhRunableTask zdhRunableTask=new ZdhRunableTask("检查基础quartz任务", new Runnable() {
                @Override
                public void run() {
                    try {
                        LogUtil.info(this.getClass(), "异步检查基础quartz任务-开始");
                        Thread.sleep(2000*20);
                        Example example=new Example(QuartzJobInfo.class);

                        Example.Criteria criteria = example.createCriteria();
                        criteria.andIn("job_type", Arrays.asList(new String[]{JobType.CHECK.getCode(),JobType.RETRY.getCode(),JobType.EMAIL.getCode()}));

                        List<QuartzJobInfo> quartzJobInfos = quartzJobMapper.selectByExample(example);

                        SchedulerFactoryBean schedulerFactoryBean = (SchedulerFactoryBean) SpringContext.getBean("&schedulerFactoryBean");
                        if(quartzJobInfos != null && quartzJobInfos.size()>0){
                            for (QuartzJobInfo quartzJobInfo: quartzJobInfos){
                                TriggerKey triggerKey = new TriggerKey(quartzJobInfo.getJob_id(), quartzJobInfo.getEtl_task_id());
                                Trigger.TriggerState triggerState = schedulerFactoryBean.getScheduler().getTriggerState(triggerKey);
                                if(triggerState == null || triggerState.equals(Trigger.TriggerState.NONE)){
                                    LogUtil.info(this.getClass(), "异步检查基础quartz任务【"+quartzJobInfo.getJob_type()+"】,未启用");
                                    Object alarmUserStr = ConfigUtil.getParamUtil().getValue(ConfigUtil.getValue(ConfigUtil.ZDP_PRODUCT), Const.SYSTEM_ALARM_USER);
                                    if(alarmUserStr != null){
                                        String[] alarmUsers = alarmUserStr.toString().split(",");
                                        for (String user: alarmUsers){
                                            EmailJob.send_notice(user, "系统任务未启用", "【"+quartzJobInfo.getJob_type()+"】系统任务未启用", "告警");
                                        }
                                    }
                                }
                            }
                        }

                        LogUtil.info(this.getClass(), "异步检查基础quartz任务-完成");

                    } catch (InterruptedException e) {
                        LogUtil.error(this.getClass(), e);
                    } catch (SchedulerException e) {
                        LogUtil.error(this.getClass(), e);
                    }
                }
            });
            threadPool.submit(zdhRunableTask);
        }catch (Exception e){

        }
    }

}
