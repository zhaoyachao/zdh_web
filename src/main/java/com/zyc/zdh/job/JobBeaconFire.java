package com.zyc.zdh.job;

import cn.hutool.db.Entity;
import cn.hutool.db.ds.simple.SimpleDataSource;
import cn.hutool.db.handler.EntityListHandler;
import cn.hutool.db.sql.SqlExecutor;
import com.hubspot.jinjava.Jinjava;
import com.zyc.zdh.dao.BeaconFireAlarmGroupMapper;
import com.zyc.zdh.dao.BeaconFireAlarmMsgMapper;
import com.zyc.zdh.dao.BeaconFireMapper;
import com.zyc.zdh.dao.DataSourcesMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.run.ZdhThreadFactory;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.*;
import org.apache.commons.lang3.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class JobBeaconFire {

    public static LinkedBlockingQueue<BeaconFireTask> linkedBlockingQueue = new LinkedBlockingQueue<>();

    public static ThreadGroup threadGroup = new ThreadGroup("zdh_bneaconfire");

    public static ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(Const.BEACON_FIRE_THREAD_MIN_NUM,
            Const.BEACON_FIRE_THREAD_MAX_NUM, Const.BEACON_FIRE_THREAD_KEEP_ACTIVE_TIME, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(),  new ZdhThreadFactory("zdh_bneaconfire_", threadGroup));


    public static void consumer(){
        while (true){
            try{
                BeaconFireTask beaconFireTask = linkedBlockingQueue.poll(10, TimeUnit.SECONDS);
                if(beaconFireTask == null){
                    continue;
                }

                //根据task_id 获取具体任务
                BeaconFireMapper beaconFireMapper = (BeaconFireMapper) SpringContext.getBean("beaconFireMapper");
                BeaconFireAlarmGroupMapper beaconFireAlarmGroupMapper = (BeaconFireAlarmGroupMapper) SpringContext.getBean("beaconFireAlarmGroupMapper");
                DataSourcesMapper dataSourcesMapper = (DataSourcesMapper) SpringContext.getBean("dataSourcesMapper");
                RedisUtil redisUtil = (RedisUtil) SpringContext.getBean("redisUtil");
                BeaconFireAlarmMsgMapper beaconFireAlarmMsgMapper = (BeaconFireAlarmMsgMapper) SpringContext.getBean("beaconFireAlarmMsgMapper");
                QuartzManager2 quartzManager2 = (QuartzManager2) SpringContext.getBean("quartzManager2");

                BeaconFireInfo beaconFireInfo = beaconFireMapper.selectByPrimaryKey(beaconFireTask.getBeacon_fire_id());
                if(beaconFireInfo == null){
                    quartzManager2.deleteTask(beaconFireTask.getBeacon_fire_id(),Const.BEACONFIRE_JOB_KEY_GROUP);
                    continue;
                }
                DataSourcesInfo dataSourcesInfo = dataSourcesMapper.selectByPrimaryKey(beaconFireInfo.getData_sources_choose_input());
                BeaconFireAlarmGroupInfo beaconFireAlarmGroupInfo = new BeaconFireAlarmGroupInfo();
                if(!StringUtils.isEmpty(beaconFireInfo.getAlarm_group())){
                    beaconFireAlarmGroupInfo.setAlarm_group_code(beaconFireInfo.getAlarm_group());
                    beaconFireAlarmGroupInfo = beaconFireAlarmGroupMapper.selectOne(beaconFireAlarmGroupInfo);
                }

                Timestamp cur_time = getCurTime(beaconFireInfo, beaconFireTask.getCur_time());
                BeaconFireThread beaconFireThread = new BeaconFireThread(beaconFireInfo, cur_time, dataSourcesInfo,
                        beaconFireAlarmGroupInfo, redisUtil, beaconFireAlarmMsgMapper);
                threadPoolExecutor.submit(beaconFireThread);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }




    /**
     * 只管QUARTZ调度触发,手动执行另算
     * 如果使用调度时间,直接赋值即可
     * 如果使用自定义时间,获取上次时间和本次做diff
     */
    public static Timestamp getCurTime(BeaconFireInfo beaconFireInfo, Timestamp cur_time) throws Exception {

        String time_diff = beaconFireInfo.getTime_diff();
        if (StringUtils.isEmpty(time_diff)) {
            return cur_time;
        }
        int dateType = Calendar.SECOND;
        int num = 0;
        if (time_diff.endsWith("s")) {
            dateType = Calendar.SECOND;
            num = Integer.parseInt(time_diff.split("s")[0]);
        }else if (time_diff.endsWith("m")) {
            dateType = Calendar.MINUTE;
            num = Integer.parseInt(time_diff.split("m")[0]);
        }else if (time_diff.endsWith("h")) {
            dateType = Calendar.HOUR;
            num = Integer.parseInt(time_diff.split("h")[0]);
        }else if (time_diff.endsWith("d")) {
            dateType = Calendar.DAY_OF_MONTH;
            num = Integer.parseInt(time_diff.split("d")[0]);
        }else{
            throw new Exception("不支持的时间差格式");
        }
        return DateUtil.add(cur_time, dateType, -num);
    }


    public static class BeaconFireThread implements Runnable{

        private BeaconFireInfo beaconFireInfo;
        private Timestamp cur_time;
        private DataSourcesInfo dataSourcesInfo;
        private BeaconFireAlarmGroupInfo beaconFireAlarmGroupInfo;
        private  RedisUtil redisUtil;
        private BeaconFireAlarmMsgMapper beaconFireAlarmMsgMapper;
        public BeaconFireThread(BeaconFireInfo beaconFireInfo, Timestamp cur_time, DataSourcesInfo dataSourcesInfo,
                                BeaconFireAlarmGroupInfo beaconFireAlarmGroupInfo,  RedisUtil redisUtil,
                                BeaconFireAlarmMsgMapper beaconFireAlarmMsgMapper){
            this.beaconFireInfo = beaconFireInfo;
            this.cur_time = cur_time;
            this.dataSourcesInfo = dataSourcesInfo;
            this.beaconFireAlarmGroupInfo = beaconFireAlarmGroupInfo;
            this.redisUtil = redisUtil;
            this.beaconFireAlarmMsgMapper = beaconFireAlarmMsgMapper;
        }

        @Override
        public void run() {
            //执行具体的告警信息
            Connection conn = null;
            try{
                DataSource ds = new SimpleDataSource(dataSourcesInfo.getUrl(), dataSourcesInfo.getUsername(),
                        dataSourcesInfo.getPassword());
                conn = ds.getConnection();
                String sql = beaconFireInfo.getSql_script();
                Map<String, Object> jinJavaParam = getJinJavaParam(cur_time);
                Jinjava jinjava=new Jinjava();

                String new_sql = jinjava.render(sql, jinJavaParam);
                List<Entity> entityList = SqlExecutor.query(conn, new_sql, new EntityListHandler());
                Map<String, Object> params = new HashMap<>();
                params.put("rs", entityList);
                params.put("out", new Out());
                String groovy = beaconFireInfo.getGroovy_script();
                Out out = (Out)GroovyFactory.execExpress(groovy, params);
                System.out.println(JsonUtil.formatJsonString(out));
                if(out != null && out.code != "0"){
                    String frequency = beaconFireInfo.getFrequency_config();
                    Long time = 1800L;
                    int time_num = 1;
                    if(!StringUtils.isEmpty(frequency) && frequency.split(",").length == 2){
                        time = Long.parseLong(frequency.split(",")[0]);
                        time_num = Integer.parseInt(frequency.split(",")[1]);
                    }

                    BeaconFireAlarmMsgInfo beaconFireAlarmMsgInfo = new BeaconFireAlarmMsgInfo();
                    beaconFireAlarmMsgInfo.setStatus(Const.STATUS_COMMON_INIT);
                    //获取
                    String alarm_key = "beacon_fire_"+beaconFireInfo.getId();
                    if(redisUtil.exists(alarm_key)){
                        String num = redisUtil.get(alarm_key,"1").toString();
                        if(Integer.valueOf(num) >= time_num){
                            beaconFireAlarmMsgInfo.setStatus(Const.STATUS_COMMON_FAIL);
                        }
                        Long expire = redisUtil.getRedisTemplate().getExpire(alarm_key, TimeUnit.SECONDS);
                        redisUtil.set(alarm_key, String.valueOf(Integer.valueOf(num) + 1), expire, TimeUnit.SECONDS);
                    }else{
                        redisUtil.set(alarm_key, "1", time, TimeUnit.SECONDS);
                    }
                    //告警
                    out.sqlScript = new_sql;
                    out.message = beaconFireInfo.getBeacon_fire_context();
                    out.setAlarmConfig(beaconFireAlarmGroupInfo.getAlarm_config());
                    beaconFireAlarmMsgInfo.setDim_group(beaconFireInfo.getDim_group());
                    beaconFireAlarmMsgInfo.setProduct_code(beaconFireInfo.getProduct_code());
                    beaconFireAlarmMsgInfo.setOwner(beaconFireInfo.getOwner());
                    beaconFireAlarmMsgInfo.setAlarm_msg(JsonUtil.formatJsonString(out));
                    beaconFireAlarmMsgMapper.insertSelective(beaconFireAlarmMsgInfo);
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        /**
         * 根据时间(timestamp) 生成jinjava 模板中的时间参数
         *
         * @param cur_time
         * @return
         */
        public Map<String, Object> getJinJavaParam(Timestamp cur_time) {
            String msg = "目前支持日期参数以下模式: {{zdh_date}} => yyyy-MM-dd ,{{zdh_date_nodash}}=> yyyyMMdd " +
                    ",{{zdh_date_time}}=> yyyy-MM-dd HH:mm:ss,{{zdh_year}}=> 年,{{zdh_month}}=> 月,{{zdh_day}}=> 日," +
                    "{{zdh_hour}}=>24小时制,{{zdh_minute}}=>分钟,{{zdh_second}}=>秒,{{zdh_time}}=>时间戳";
            LogUtil.info(this.getClass(), msg);

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

            return jinJavaParam;
        }
    }

    public static class Out{

        public String code;

        public String message;

        public Object o;

        public String sqlScript;

        private String alarmConfig;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Object getO() {
            return o;
        }

        public void setO(Object o) {
            this.o = o;
        }

        public String getSqlScript() {
            return sqlScript;
        }

        public void setSqlScript(String sqlScript) {
            this.sqlScript = sqlScript;
        }

        public String getAlarmConfig() {
            return alarmConfig;
        }

        public void setAlarmConfig(String alarmConfig) {
            this.alarmConfig = alarmConfig;
        }
    }
}

