package com.zyc.zdh.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * 当前项目配置参数工具类
 * 单独写此工具的作用: 屏蔽env配置
 *
 * 使用限制：redis, paramutil初始化相关配置不可使用,因当前类使用了redis,paramutil不可循环引用
 */
public class ConfigUtil {

    public static String VERSION = "version";
    public static String MYID = "myid";
    public static String INSTANCE = "instance";
    public static String SERVER_PORT = "server.port";
    public static String ZDP_PRODUCT = "zdp.product";
    public static String ZDP_AK = "zdp.ak";
    public static String ZDP_SK = "zdp.sk";
    public static String ZDP_INIT_ROLES = "zdp.init.roles";
    public static String ZDP_AUTO_ENABLE_PRODUCT = "zdp.auto.enable.product";

    public static String WEB_PATH = "web.path";
    public static String COOKIE_PATH = "cookie.path";

    public static String ZDH_INIT_REDIS_ZDH_CHECK_IMPLS = "zdh.init.redis.zdh_check_impls";
    public static String ZDH_INIT_REDIS_ZDH_PLATFORM_NAME = "zdh.init.redis.zdh_platform_name";
    public static String ZDH_SCHEDULE_QUARTZ_AUTO_STARTUP = "zdh.schedule.quartz.auto.startup";
    public static String ZDH_SCHEDULE_QUARTZ_SCHEDULER_INSTANCENAME = "zdh.schedule.quartz.scheduler.instanceName";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_SCHEDULER_INSTANCEID = "zdh.schedule.org.quartz.scheduler.instanceId";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_SCHEDULER_INSTANCEIDGENERATOR_CLASS = "zdh.schedule.org.quartz.scheduler.instanceIdGenerator.class";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_SCHEDULER_SKIPUPDATECHECK = "zdh.schedule.org.quartz.scheduler.skipUpdateCheck";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_JOBSTORE_CLASS = "zdh.schedule.org.quartz.jobStore.class";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_JOBSTORE_DRIVERDELEGATECLASS = "zdh.schedule.org.quartz.jobStore.driverDelegateClass";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_JOBSTORE_DATASOURCE = "zdh.schedule.org.quartz.jobStore.dataSource";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_JOBSTORE_TABLEPREFIX = "zdh.schedule.org.quartz.jobStore.tablePrefix";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_JOBSTORE_ISCLUSTERED = "zdh.schedule.org.quartz.jobStore.isClustered";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_JOBSTORE_CLUSTERCHECKININTERVAL = "zdh.schedule.org.quartz.jobStore.clusterCheckinInterval";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_THREADPOOL_CLASS = "zdh.schedule.org.quartz.threadPool.class";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_THREADPOOL_THREADCOUNT = "zdh.schedule.org.quartz.threadPool.threadCount";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_JOBSTORE_MAXMISFIRESTOHANDLEATATIME = "zdh.schedule.org.quartz.jobStore.maxMisfiresToHandleAtATime";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_JOBSTORE_MISFIRETHRESHOLD = "zdh.schedule.org.quartz.jobStore.misfireThreshold";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_JOBSTORE_TXISOLATIONLEVELSERIALIZABLE = "zdh.schedule.org.quartz.jobStore.txIsolationLevelSerializable";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_JOBSTORE_USEPROPERTIES = "zdh.schedule.org.quartz.jobStore.useProperties";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_JOBSTORE_ACQUIRETRIGGERWITHINLOCK = "zdh.schedule.org.quartz.jobstore.acquireTriggerWithinLock";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_DATASOURCE_QUARTZDATASOURCE_DRIVER = "zdh.schedule.org.quartz.dataSource.quartzDataSource.driver";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_DATASOURCE_QUARTZDATASOURCE_URL = "zdh.schedule.org.quartz.dataSource.quartzDataSource.URL";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_DATASOURCE_QUARTZDATASOURCE_USER = "zdh.schedule.org.quartz.dataSource.quartzDataSource.user";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_DATASOURCE_QUARTZDATASOURCE_PASSWORD = "zdh.schedule.org.quartz.dataSource.quartzDataSource.password";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_DATASOURCE_QUARTZDATASOURCE_MAXCONNECTIONS = "zdh.schedule.org.quartz.dataSource.quartzDataSource.maxConnections";
    public static String ZDH_SCHEDULE_ORG_QUARTZ_JOBSTORE_DONTSETAUTOCOMMITFALSE = "zdh.schedule.org.quartz.jobStore.dontSetAutoCommitFalse";
    public static String ZDH_DISENABLE_MORE_TASK = "zdh.disenable.more_task";
    public static String ZDH_DISENABLE_JOB_TYPE = "zdh.disenable.job_type";

    public static String SPRING_REDIS_HOSTNAME = "spring.redis.hostName";
    public static String SPRING_REDIS_PORT = "spring.redis.port";
    public static String SPRING_REDIS_PASSWORD = "spring.redis.password";
    public static String SPRING_REDIS_TIMEOUT = "spring.redis.timeOut";
    public static String SPRING_REDIS_MAXIDLE = "spring.redis.maxIdle";
    public static String SPRING_REDIS_MAXWAITMILLIS = "spring.redis.maxWaitMillis";
    public static String SPRING_REDIS_TESTONBORROW = "spring.redis.testOnBorrow";
    public static String SPRING_REDIS_TESTWHILEIDLE = "spring.redis.testWhileIdle";

    public static String SPRING_MAIL_HOST = "spring.mail.host";
    public static String SPRING_MAIL_USERNAME = "spring.mail.username";
    public static String SPRING_MAIL_PASSWORD = "spring.mail.password";
    public static String SPRING_MAIL_PORT = "spring.mail.port";
    public static String SPRING_MAIL_PROPERTIES_MAIL_SMTP_AUTH = "spring.mail.properties.mail.smtp.auth";
    public static String SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_ENABLE = "spring.mail.properties.mail.smtp.starttls.enable";
    public static String SPRING_MAIL_PROPERTIES_MAIL_SMTP_STARTTLS_REQUIRED = "spring.mail.properties.mail.smtp.starttls.required";
    public static String SPRING_MAIL_PROPERTIES_MAIL_SMTP_SSL_ENABLE = "spring.mail.properties.mail.smtp.ssl.enable";
    public static String EMAIL_SCHEDULE_INTERVAL = "email.schedule.interval";
    public static String RETRY_SCHEDULE_INTERVAL = "retry.schedule.interval";
    public static String CHECK_SCHEDULE_INTERVAL = "check.schedule.interval";

    public static String QUEUE_SERVER_DB_URL = "queue.server.db.url";
    public static String QUEUE_SERVER_DB_DRIVER = "queue.server.db.driver";
    public static String QUEUE_SERVER_DB_USER = "queue.server.db.user";
    public static String QUEUE_SERVER_DB_PASSWORD = "queue.server.db.password";
    public static String PYTHON_ENGINE = "python_engine";
    public static String DATAX_HOME = "datax_home";
    public static String ALARM_ADMIN_EMAIL = "alarm.admin.email";
    public static String DIGITALMARKET_TMP_PATH = "digitalmarket.tmp.path";
    public static String DIGITALMARKET_STORE_TYPE = "digitalmarket.store.type";
    public static String DIGITALMARKET_LOCAL_PATH = "digitalmarket.local.path";
    public static String DIGITALMARKET_SFTP_HOST = "digitalmarket.sftp.host";
    public static String DIGITALMARKET_SFTP_PORT = "digitalmarket.sftp.port";
    public static String DIGITALMARKET_SFTP_USERNAME = "digitalmarket.sftp.username";
    public static String DIGITALMARKET_SFTP_PASSWORD = "digitalmarket.sftp.password";
    public static String DIGITALMARKET_SFTP_PATH = "digitalmarket.sftp.path";
    public static String DIGITALMARKET_MINIO_AK = "digitalmarket.minio.ak";
    public static String DIGITALMARKET_MINIO_SK = "digitalmarket.minio.sk";
    public static String DIGITALMARKET_MINIO_ENDPOINT = "digitalmarket.minio.endpoint";
    public static String DIGITALMARKET_MINIO_REGION = "digitalmarket.minio.region";
    public static String DIGITALMARKET_MINIO_BUCKET = "digitalmarket.minio.bucket";
    public static String ZDH_AUTH_PASSWORD_KEY = "zdh.auth.password.key";
    public static String ZDH_AUTH_PASSWORD_IV = "zdh.auth.password.iv";
    public static String IP2REGION_PATH = "ip2region.path";
    public static String ZDH_SHIP_URL = "zdh.ship.url";
    public static String BEACON_FIRE_SMS_REGIN_ID = "beacon.fire.sms.regin.id";
    public static String BEACON_FIRE_SMS_AK = "beacon.fire.sms.ak";
    public static String BEACON_FIRE_SMS_SK = "beacon.fire.sms.sk";
    public static String BEACON_FIRE_SMS_TEMPLATE = "beacon.fire.sms.template";
    public static String BEACON_FIRE_SMS_SIGN = "beacon.fire.sms.sign";
    public static String ZDH_WEMOCK_SHORT_HOST = "zdh.wemock.short.host";
    public static String ZDH_WEMOCK_SHORT_GENERATOR = "zdh.wemock.short.generator";
    public static String ZDH_SPARK_QUEUE_ENABLE = "zdh.spark.queue_enable";
    public static String ZDH_SPARK_QUEUE_PRE_KEY = "zdh.spark.queue_pre_key";

    public static String SPRING_PROFILES_ACTIVE = "spring.profiles.active";

    public static String getValue(String key, String value){
        Environment environment= getEnv();
        return environment.getProperty(key, value);
    }

    public static String getValue(String key){
        Environment environment= getEnv();
        return environment.getProperty(key);
    }

    public static Environment getEnv(){
        Environment environment= (Environment) SpringContext.getBean("environment");
        return environment;
    }

    public static ParamUtil getParamUtil(){
        ParamUtil paramUtil= (ParamUtil) SpringContext.getBean("paramUtil");
        return paramUtil;
    }

    public static boolean isInValue(String key, String value){
        String tmp = getValue(key, "");
        if(StringUtils.isEmpty(tmp)){
            return false;
        }
        return  Arrays.stream(tmp.split(",")).map(str->str.toLowerCase()).collect(Collectors.toSet()).contains(value.toLowerCase());
    }

    public static boolean isInEnv(String key){
        return getEnv().containsProperty(key);
    }

    public static boolean isInRedis(String product_code, String key){
        return getParamUtil().exists(product_code, key);
    }

    public static boolean isInRedisValue(String product_code, String key, String value){
        String tmp = getParamUtil().getValue(product_code, key, "").toString();
        if(StringUtils.isEmpty(tmp)){
            return false;
        }
        return  Arrays.stream(tmp.split(",")).map(str->str.toLowerCase()).collect(Collectors.toSet()).contains(value.toLowerCase());
    }

}
