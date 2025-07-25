package com.zyc.zdh.job;

import cn.hutool.core.util.NumberUtil;
import com.google.common.collect.Lists;
import com.hubspot.jinjava.Jinjava;
import com.hubspot.jinjava.lib.fn.ELFunctionDefinition;
import com.zyc.zdh.dao.StrategyGroupInstanceMapper;
import com.zyc.zdh.dao.StrategyGroupMapper;
import com.zyc.zdh.dao.StrategyInstanceMapper;
import com.zyc.zdh.entity.StrategyGroupInfo;
import com.zyc.zdh.entity.StrategyGroupInstance;
import com.zyc.zdh.entity.StrategyInstance;
import com.zyc.zdh.entity.ZdhLogs;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.run.ZdhThreadFactory;
import com.zyc.zdh.service.ZdhLogsService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class JobDigitalMarket {

    public static ThreadGroup threadGroup = new ThreadGroup("zdh_strategy");
    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(Const.DIGITAL_MARKET_THREAD_MIN_NUM,
            Const.DIGITAL_MARKET_THREAD_MAX_NUM, Const.DIGITAL_MARKET_THREAD_KEEP_ACTIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new ZdhThreadFactory("zdh_strategy_", threadGroup));

    public static void updateTaskLog(StrategyGroupInstance sgi, StrategyGroupInstanceMapper sgim) {
        // debugInfo(tgli);
        sgim.updateByPrimaryKeySelective(sgi);
    }

    public static void updateTaskLog(StrategyInstance si, StrategyInstanceMapper sim) {
        // debugInfo(tgli);
        sim.updateByPrimaryKeySelective(si);
    }

    public static void insertLog(StrategyGroupInstance sgi, String level, String msg) {

        ZdhLogs zdhLogs = new ZdhLogs();
        zdhLogs.setJob_id(sgi.getStrategy_group_id());
        Timestamp lon_time = new Timestamp(System.currentTimeMillis());
        zdhLogs.setTask_logs_id(sgi.getId());
        zdhLogs.setLog_time(lon_time);
        zdhLogs.setMsg(msg);
        zdhLogs.setLevel(level.toUpperCase());
        //linkedBlockingDeque.add(zdhLogs);

        Object logType=ConfigUtil.getParamUtil().getValue(ConfigUtil.getProductCode(), Const.ZDH_LOG_TYPE);

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
        Timestamp lon_time = new Timestamp(System.currentTimeMillis());
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
                    LogUtil.info(JobDigitalMarket.class, "传入的对象中包含一个如下的变量：" + varName + " = " + o);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    LogUtil.error(JobDigitalMarket.class, e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                LogUtil.error(JobDigitalMarket.class, e);
            }
        }
    }


    /**
     * 检查t+n模块是否满足
     * @param si
     */
    public static boolean checkTnDepends(StrategyInstance si, Map<String,StrategyInstance> dagStrategyInstance) throws Exception {
        //1 获取对比时间类型,相对或者绝对
        Map<String, Object> run_jsmind_data = JsonUtil.toJavaMap(si.getRun_jsmind_data());
        String tn_type = run_jsmind_data.getOrDefault("tn_type", "").toString();
        String tn_unit = run_jsmind_data.getOrDefault("tn_unit", "").toString();
        String tn_value = run_jsmind_data.getOrDefault("tn_value", "").toString();
        if(StringUtils.isEmpty(tn_value)){
            JobDigitalMarket.insertLog(si,"ERROR","tn模块时间参数不可为空");
            throw new Exception("tn模块时间参数不可为空");
        }
        //替换时间参数
        Map<String, Object> jinJavaParam = getJinJavaParam(si);
        Jinjava jinjava = DynamicParams();
        tn_value = jinjava.render(tn_value, jinJavaParam);

        Date currentDate = new Date();
        if(tn_type.equalsIgnoreCase(Const.TN_TYPE_RELATIVE)){
            //相对时间

            Timestamp updateTime = null;
            if(StringUtils.isEmpty(si.getPre_tasks())){
                //throw new Exception("tn模块设置相对时间,必须有上游任务");
                //无上游任务获取当前任务开始执行时间
                updateTime = new Timestamp(si.getRun_time().getTime());
            }else{
                String parentId = si.getPre_tasks().split(",")[0];
                StrategyInstance parentStrategyInstance = dagStrategyInstance.get(parentId);
                //获取上游执行完成时间
                updateTime = parentStrategyInstance.getUpdate_time();
            }


            if(StringUtils.isEmpty(tn_unit)){
                throw new Exception("tn模块设置相对时间单位为空");
            }
            if(!NumberUtil.isInteger(tn_value)){
                throw new Exception("tn模块设置相对时间必须是整数");
            }
            Timestamp executeTime = null;
            if(tn_unit.equalsIgnoreCase("minute")){
                executeTime = DateUtil.addMinute(DateUtil.formatTime(updateTime), Integer.valueOf(tn_value));
            }else if(tn_unit.equalsIgnoreCase("hour")){
                executeTime =DateUtil.addHour(DateUtil.formatTime(updateTime), Integer.valueOf(tn_value));
            }else if(tn_unit.equalsIgnoreCase("day")){
                executeTime =DateUtil.addDay(DateUtil.formatTime(updateTime), Integer.valueOf(tn_value));
            }

            //此处还少一个之前/之后参数,默认当前只支持之后

            if(currentDate.getTime() >= executeTime.getTime()){
                //校验通过
                return true;
            }
            if(executeTime.getTime() - System.currentTimeMillis() > 1000*60*5 ){
                run_jsmind_data.put(Const.STRATEGY_INSTANCE_DOUBLECHECK_TIME, System.currentTimeMillis()+1000*60*5);
            }else{
                run_jsmind_data.put(Const.STRATEGY_INSTANCE_DOUBLECHECK_TIME, System.currentTimeMillis()+(executeTime.getTime() - System.currentTimeMillis()-1000));
            }
            si.setRun_jsmind_data(JsonUtil.formatJsonString(run_jsmind_data));
            return false;

        }else if(tn_type.equalsIgnoreCase(Const.TN_TYPE_ABSOLUTE)){

            long start=0;
            long end=0;
            //绝对时间
            String[] values = tn_value.split(";");
            if(values.length == 1){
                //起始结束一样
                start = DateUtil.pase(values[0], DateUtil.df_time.getPattern()).getTime();
                end = start;
            }else if(values.length != 2){
                throw new Exception("tn模块设置绝对时间必须设置开始和结束2个时间");
            }else{
                start = DateUtil.pase(values[0], DateUtil.df_time.getPattern()).getTime();
                end = DateUtil.pase(values[1], DateUtil.df_time.getPattern()).getTime();
            }

            //只做包含校验
            if(currentDate.getTime()>=start && currentDate.getTime()<=end){
                return true;
            }
            if(start - System.currentTimeMillis() > 1000*60*5 ){
                run_jsmind_data.put(Const.STRATEGY_INSTANCE_DOUBLECHECK_TIME, System.currentTimeMillis()+1000*60*5);
            }else{
                run_jsmind_data.put(Const.STRATEGY_INSTANCE_DOUBLECHECK_TIME, System.currentTimeMillis()+(start - System.currentTimeMillis()-1000));
            }
            si.setRun_jsmind_data(JsonUtil.formatJsonString(run_jsmind_data));
            return false;
        }
        return false;
    }

    /**
     * 根据时间(timestamp) 生成jinjava 模板中的时间参数
     *
     * @param si
     * @return
     */
    public static Map<String, Object> getJinJavaParam(StrategyInstance si) {
        String msg = "目前支持日期参数以下模式: {{zdh_date}} => yyyy-MM-dd ,{{zdh_date_nodash}}=> yyyyMMdd " +
                ",{{zdh_date_time}}=> yyyy-MM-dd HH:mm:ss,{{zdh_year}}=> 年,{{zdh_month}}=> 月,{{zdh_day}}=> 日," +
                "{{zdh_hour}}=>24小时制,{{zdh_minute}}=>分钟,{{zdh_second}}=>秒,{{zdh_time}}=>时间戳";
        LogUtil.debug(JobDigitalMarket.class, msg);
        //insertLog(si, "info", msg);
        Timestamp cur_time = si.getCur_time();
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
        jinJavaParam.put("zdh_monthx", DateUtil.monthx(cur_time));
        jinJavaParam.put("zdh_dayx", DateUtil.dayx(cur_time));
        jinJavaParam.put("zdh_hourx", DateUtil.hourx(cur_time));
        jinJavaParam.put("zdh_minutex", DateUtil.minutex(cur_time));
        jinJavaParam.put("zdh_secondx", DateUtil.secondx(cur_time));

        jinJavaParam.put("zdh_time", cur_time.getTime());
        jinJavaParam.put("zdh_task_log_id", si.getId());

        jinJavaParam.put("zdh_dt", new DateUtil());

        return jinJavaParam;

    }

    public static Jinjava DynamicParams(){
        Jinjava jj = new Jinjava();

        jj.getGlobalContext().registerFunction(new ELFunctionDefinition("", "add_day",
                DateUtil.class, "addDay", String.class, Integer.class));
        jj.getGlobalContext().registerFunction(new ELFunctionDefinition("", "add_hour",
                DateUtil.class, "addHour", String.class, Integer.class));
        jj.getGlobalContext().registerFunction(new ELFunctionDefinition("", "add_minute",
                DateUtil.class, "addMinute", String.class, Integer.class));
        jj.getGlobalContext().registerFunction(new ELFunctionDefinition("", "pase",
                DateUtil.class, "pase", String.class, String.class));

        String msg = "目前支持日期操作: {{add_day('2021-12-01 00:00:00', 1)}} => 2021-12-02 00:00:00 ,{{add_hour('2021-12-01 00:00:00', 1)}} => 2021-12-01 01:00:00,{{add_minute('2021-12-01 00:00:00', 1)}} => 2021-12-01 00:01:00, 更多高级函数, 可参考【系统内置参数】点击链接查看具体使用例子";
        LogUtil.debug(JobDigitalMarket.class, msg);
        return jj;
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
        RedisUtil redisUtil=(RedisUtil) SpringContext.getBean("redisUtil");
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
                        //BeanUtils.copyProperties(sgi, strategyGroupInfo);
                        sgi = MapStructMapper.INSTANCE.strategyGroupInfoToStrategyGroupInstance(strategyGroupInfo);
                        sgi.setId(SnowflakeIdWorker.getInstance().nextId() + "");
                        sgi.setStrategy_group_id(strategyGroupInfo.getId());
                        sgi.setCreate_time(new Timestamp(System.currentTimeMillis()));
                        sgi.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                        if(sgi.getGroup_type().equalsIgnoreCase(JobGroupType.OFFLINE.getValue())){
                            sgi.setSmall_flow_rate("1,100");
                        }
                        //逻辑发送错误代码捕获发生自动重试(retry_job) 不重新生成实例id,使用旧的实例id
                        String last_task_id = "";
                        if (is_retry == 0) {
                            //调度触发,直接获取执行时间
                            Timestamp cur = getCurTime(strategyGroupInfo);
                            sgi.setCur_time(cur);
                            sgi.setSchedule_source(ScheduleSource.SYSTEM.getCode());
                            sgi.setRun_time(new Timestamp(System.currentTimeMillis()));//实例开始时间
                            sgi.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                        }
                        if (is_retry == 2) {
                            //手动点击重试按钮触发
                            //手动点击重试,会生成新的实例信息,默认重置执行次数,并将上次执行失败的实例id 付给last_task_id
                            sgi = retry_sgi;
                            //retry_sgii.setProcess("1");
                            sgi.setSchedule_source(ScheduleSource.MANUAL.getCode());
                            sgi.setRun_time(new Timestamp(System.currentTimeMillis()));//实例开始时间
                            sgi.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                            //retry_sgii.setCount(0L);
                            //retry_sgii.setIs_retryed("0");
                            sgi.setId(SnowflakeIdWorker.getInstance().nextId() + "");
                            sgi.setStatus(JobStatus.NON.getValue());
                        }

                        //此处更新主要是为了 日期超时时 也能记录下日志
                        //insertLog(tgli, "INFO", "生成任务组信息,任务组数据处理日期:" + tgli.getEtl_date());
                        sgim.insertSelective(sgi);

                        sgm.updateByPrimaryKeySelective(strategyGroupInfo);
                        //公共设置
                        sgi.setStatus(JobStatus.NON.getValue());//新实例状态设置为dispatch
                        //设置调度器唯一标识,调度故障转移时使用,如果服务器重启会自动生成新的唯一标识
                        //tgli.setServer_id(JobCommon2.web_application_id);

                        //todo 生成具体任务组下任务实例
                        sub_strategy_instance(sgi, sub_tasks);
                        sgim.updateStatus2Create(new String[]{sgi.getId()});

                        //在线策略小流量生效,谨慎操作,不会清理历史小流量配置,清理历史小流量需要在平台手动同步小流量
                        if(!StringUtils.isEmpty(sgi.getSmall_flow_rate()) && sgi.getGroup_type().equalsIgnoreCase(JobGroupType.ONLINE.getValue())){
                            Map<String, Object> tmp = new HashMap<>();
                            String small_flow_key = "small_flow_rate_"+sgi.getStrategy_group_id();
                            Object small_flow_value = redisUtil.get(small_flow_key);
                            if(small_flow_value != null){
                                tmp = JsonUtil.toJavaMap(small_flow_value.toString());
                            }
                            tmp.put(sgi.getId(), sgi.getSmall_flow_rate());
                            redisUtil.set(small_flow_key, JsonUtil.formatJsonString(tmp));
                        }
                        debugInfo(sgi);

                    } catch (IllegalAccessException e) {
                        LogUtil.error(JobDigitalMarket.class, e);
                    } catch (InvocationTargetException e) {
                        LogUtil.error(JobDigitalMarket.class, e);
                    } catch (Exception e) {
                        LogUtil.error(JobDigitalMarket.class, e);
                    }




                }
            });
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
                    LogUtil.error(JobDigitalMarket.class, e);
                }
                return DateUtil.add(strategyGroupInfo.getQuartz_time(), Calendar.SECOND, -seconds);
            }
            return strategyGroupInfo.getQuartz_time();
        }else{
            throw new Exception("目前调度只支持quartz触发时间,不支持自定义时间");
        }


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
            List<Map<String, Object>> tasks = (List<Map<String, Object>>)JsonUtil.toJavaMap(sgi.getJsmind_data()).getOrDefault("tasks", Lists.newArrayList());
            List<Map<String, Object>> lines = (List<Map<String, Object>>)JsonUtil.toJavaMap(sgi.getJsmind_data()).getOrDefault("line", Lists.newArrayList());
            for (Map<String, Object> job : tasks) {
                StrategyInstance si = new StrategyInstance();
                si = MapStructMapper.INSTANCE.strategyGroupInstanceToStrategyInstance(sgi);

                String pageSourceId = job.getOrDefault("divId", "").toString();//前端生成的div 标识
                String more_task = job.getOrDefault("more_task", "").toString();
                String is_disenable = job.getOrDefault("is_disenable", "").toString();
                String depend_level = job.getOrDefault("depend_level", "").toString();
                String time_out = job.getOrDefault("time_out", "").toString();
                String touch_type = job.getOrDefault("touch_type", "").toString();
                String data_node = job.getOrDefault("data_node", "").toString();
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

                si.setRun_time(new Timestamp(System.currentTimeMillis()));


                si.setJsmind_data("");
                si.setJsmind_data(JsonUtil.formatJsonString(job));
                si.setRun_jsmind_data(JsonUtil.formatJsonString(job));

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

                String name = job.getOrDefault("name", "").toString();
                si.setStrategy_context(name);
                si.setStrategy_id(pageSourceId);
                siList.add(si);
            }

            List<Map<String, Object>> jary_line = JsonUtil.createEmptyListMap();
            for (Map<String, Object> job : lines) {
                String pageSourceId = job.getOrDefault("pageSourceId", "").toString();
                String pageTargetId = job.getOrDefault("pageTargetId", "").toString();
                job.put("id", map.get(pageSourceId));
                job.put("parentid", map.get(pageTargetId));
                if (pageSourceId != null && !pageSourceId.equalsIgnoreCase("root")) {
                    boolean is_loop = dag.addEdge(map.get(pageSourceId), map.get(pageTargetId));//此处的依赖关系 都是生成的任务实例id
                    if (!is_loop) {
                        insertLog(sgi, "ERROR", "无法生成子任务,请检查子任务是否存在相互依赖的任务!");
                        //sgim.updateStatusById3(JobStatus.ERROR.getValue(), "100", DateUtil.getCurrentTime(), tgli.getId());
                        return siList;
                    }

                    Map<String, Object> json_line = JsonUtil.createEmptyMap();
                    json_line.put("from", map.get(pageSourceId));
                    json_line.put("to", map.get(pageTargetId));
                    jary_line.add(json_line);
                }
            }

            //run_data 结构：run_data:[{task_log_instance_id,etl_task_id,etl_context,more_task}]
            List<Map<String, Object>> jary = JsonUtil.createEmptyListMap();
            for (StrategyInstance si : siList) {
                Map<String, Object> jsonObject1 = JsonUtil.createEmptyMap();
                String sid = si.getId();

                jsonObject1.put("strategy_instance_id", sid);
                //jsonObject1.put("etl_task_id", tli.getEtl_task_id());
                //jsonObject1.put("etl_context", tli.getEtl_context());
                //jsonObject1.put("more_task", tli.getMore_task());
                //jsonObject1.put("job_type", tli.getJob_type());
                jsonObject1.put("divId", map2.get(sid));
                jary.add(jsonObject1);
            }

            Map<String, Object> jsonObject = JsonUtil.toJavaMap(sgi.getJsmind_data());
            jsonObject.put("run_data", jary);
            jsonObject.put("run_line", jary_line);
            sgi.setRun_jsmind_data(JsonUtil.formatJsonString(jsonObject));
            //sgi.setProcess("6.5");
            sgim.updateByPrimaryKeySelective(sgi);
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
                    }else {
                        si.setStatus(JobStatus.NON.getValue());
                    }
                }
                sim.insert(si);
            }
        } catch (Exception e) {
            LogUtil.error(JobDigitalMarket.class, e);
            JobDigitalMarket.insertLog(sgi, "ERROR", "生成子任务失败," + e.getMessage());
        }
        return siList;
    }

}
