package com.zyc.zdh.util;

import java.util.HashMap;
import java.util.Map;

public class Const {

    //j2cache, myredis 都是srpingboot cachemanager 用到,和shiro 无关,shiro缓存,采用redis自定义单独实现
    public static String JAVA_2CACHE = "j2CacheRedis";//java 2级缓存实现,
    public static String JAVA_REDISCACHE = "myRedis";//

    public static String SHIRO_SESSION_CACHE_NAME="shiro-activeSessionCache1";

    public static String SHIRO_SESSION_ID_CACHE_PREFIX_KEY = "shiro:cache:shiro-activeSessionCache1:";

    public static String SUPER_ADMIN_ROLE="super_admin";//超级管理员

    public static String SYSTEM_ALARM_USER="system_alarm_user";//系统告警用户

    public static String TRUR="true";
    public static String FALSE="false";

    public static String ON="on";
    public static String OFF="off";

    public static String STATUS_PUB="1";//发布
    public static String STATUS_NOT_PUB="2";//未发布

    public static String SHOW="1";
    public static String NOT_SHOW="0";

    public static String END="1";
    public static String NOT_END="0";

    //通用任务类状态管理
    public static String STATUS_COMMON_INIT="1";
    public static String STATUS_COMMON_RUNNING="2";
    public static String STATUS_COMMON_SUCCESS="3";
    public static String STATUS_COMMON_FAIL="4";


    //流程状态,0:未审批,1:审批完成,2:不通过,3:撤销
    public static String STATUS_INIT="0";
    public static String STATUS_SUCCESS="1";
    public static String STATUS_FAIL="2";
    public static String STATUS_RECALL="3";

    public static String PROCESS_OTHER_STATUS_INIT="0";//未处理
    public static String PROCESS_OTHER_STATUS_SUCCESS="1";//处理成功
    public static String PROCESS_OTHER_STATUS_FAIL="2";//处理失败


    public static String DELETE="1";
    public static String NOT_DELETE="0";

    public static String TASK_SCHEDULE_MODE_CRON="cron";//cron表达式
    public static String TASK_SCHEDULE_MODE_SIMPLE="simple";//简洁表达式

    public static String STRATEGY_JOB_KEY_GROUP = "strategy_group"; //智能策略job_key 对应的group信息
    public static String BEACONFIRE_JOB_KEY_GROUP = "beaconfire"; //烽火台job_key 对应的group信息

    public static String SMS_INIT = "0";//未处理
    public static String SMS_HANDLING = "1";//处理中
    public static String SMS_FAIL = "2";//处理失败
    public static String SMS_SUCCESS = "3";//成功
    public static String SMS_NOT_HANDLE = "4";//不做处理

    public static String BATCH_INIT = "0";//未执行
    public static String BATCH_RUNNING = "1";//执行中
    public static String BATCH_FAIL = "2";//执行失败
    public static String BATCH_SUCCESS = "3";//执行成功

    public static String ENABLE = "1";//启用
    public static String UN_ENABLE = "2";//未启用

    public static String NOTHING="0";//无操作
    public static String ALL_HISTORY="1";//所有历史
    public static String LAST_HISTORY="2";//最近一次历史

    public static String APPLY_STATUS_INIT="0";//申请状态,未处理
    public static String APPLY_STATUS_SUCCESS="1";//申请状态,申请成功
    public static String APPLY_STATUS_FAIL="2";//申请状态,申请失败

    public static String LINE_SEPARATOR=System.getProperty("line.separator");

    public static String ZDH_CHECK_IMPLS="zdh_check_impls";//zdh 调度检查实现类

    public static String ZDH_SCHEDULE_HEART_PRE="schedule_heart_";//执行器心态前缀

    public static String ZDH_IS_PASS = "zdh_is_pass";//系统是否可访问

    public static String ZDH_IS_PASS_USER = "zdh_is_pass_user";//系统可访问用户

    public static String ZDH_IP_BACKLIST= "zdh_ip_backlist";//IP黑名单参数

    public static String ZDH_USER_BACKLIST= "zdh_user_backlist";//用户黑名单参数

    public static String ZDH_USER_UNENABLE= "zdh_user_unenable";//用户冻结参数

    public static String ZDH_PLATFORM_NAME = "zdh_platform_name";//系统名称,登录页显示

    public static String ZDH_BACKGROUND_IMAGE = "zdh_background_image";//系统背景页

    public static String ZDH_SYSTEM_DNS = "zdh_system_dns";//系统dns

    public static String ZDH_IP_RATELIMIT = "zdh_ip_ratelimit";//系统IP访问次数限制
    public static String ZDH_USER_RATELIMIT = "zdh_user_ratelimit";//系统账号访问次数限制
    public static String ZDH_RATELIMIT_CLEART_TIME = "zdh_ratelimit_cleart_time";//系统IP访问次数限制
    public static String ZDH_RATELIMIT_KEY_USER_PRE = "zdh_ratelimiter_user_";

    public static String ZDH_SSH_MAX_THREAD = "zdh_ssh_max_thread";
    public static String ZDH_THREADPOOL_MAX = "zdh_threadpool_max";

    public static String ZDH_LOG_TYPE = "zdh_log_type";//日志存储类型

    public static String ZDH_FLOW_DEFAULT_USER = "zdh_flow_default_user";//审批流默认user

    //public static String ZDH_DISENABLE_MORE_TASK = "zdh.disenable.more_task";//禁用more_task
    //public static String ZDH_DISENABLE_JOB_TYPE = "zdh.disenable.job_type";//禁用job_type

    //public static String ZDH_SPARK_QUEUE_ENABLE = "zdh.spark.queue_enable";//是否启用spark etl队列

    //public static String ZDH_SPARK_QUEUE_PRE_KEY = "zdh.spark.queue_pre_key";//spark etl队列前缀

    public static String ZDH_SPARK_SKIP_RETRY_VALID_TIME = "zdh.spark.skip_retry_valid_time";//etl任务重试有效时间,单位秒,100 表示100秒内可进行重试,默认3600

    public static String HTTP_POST = "0";
    public static  String HTTP_GET = "1";

    public static String EMAIL_TXT = "0";
    public static  String EMAIL_HTML = "1";

    public static String PRODUCT_ENABLE = "0";
    public static String PRODUCT_NOT_APPLY = "1";
    public static String PRODUCT_UN_ENABLE = "2";

    //权限申请
    public static String PERMISSION_APPLY_INIT="0";//未处理
    public static String PERMISSION_APPLY_RUNNING="1";//处理中
    public static String PERMISSION_APPLY_FAIL="2";//申请失败
    public static String PERMISSION_APPLY_SUCCESS="3";//申请通过
    public static String PERMISSION_APPLY_RECALL="4";//申请撤销

    public static String PERMISSION_APPLY_TYPE_ROLE="role";//申请角色
    public static String PERMISSION_APPLY_TYPE_USER_GROUP="user_group";//申请用户组
    public static String PERMISSION_APPLY_TYPE_DATA_GROUP="data_group";//申请数据组
    public static String PERMISSION_APPLY_TYPE_PRODUCT_ADMIN="product_admin";//申请产品管理员

    public static String PERMISSION_DIM_PRODUCT_CODE = "dim_product";
    public static String PERMISSION_DIM_GROUP_CODE = "dim_group";

    public static String LOG_MYSQL="mysql";//
    public static String LOG_MONGODB="mongodb";//

    public static String PARAM_TYPE_STRING="1";//参数类型,字符串
    public static String PARAM_TYPE_JSON="2";//参数类型,json

    public static String AUDITOR_RULE_LEADER="leader";
    public static String AUDITOR_RULE_SECURITY="security";
    public static String AUDITOR_RULE_LEGAL="legal";
    public static String AUDITOR_RULE_RESOURCES_LEADER="resources_leader";

    public static String TN_TYPE_RELATIVE="relative";
    public static String TN_TYPE_ABSOLUTE="absolute";

    public static int ETL_THREAD_MIN_NUM = 1;
    public static int ETL_THREAD_MAX_NUM = 1000;
    public static long ETL_THREAD_KEEP_ACTIVE_TIME=180;

    public static int DIGITAL_MARKET_THREAD_MIN_NUM = 1;
    public static int DIGITAL_MARKET_THREAD_MAX_NUM = 1000;
    public static long DIGITAL_MARKET_THREAD_KEEP_ACTIVE_TIME=120;

    public static int SYSTEM_THREAD_MIN_NUM = 20;
    public static int SYSTEM_THREAD_MAX_NUM = 50;
    public static long SYSTEM_THREAD_KEEP_ACTIVE_TIME=120;

    public static int BEACON_FIRE_THREAD_MIN_NUM = 1;
    public static int BEACON_FIRE_THREAD_MAX_NUM = 10;
    public static long BEACON_FIRE_THREAD_KEEP_ACTIVE_TIME=180;

    public static String STRATEGY_GROUP_TYPE_OFFLINE = "offline";
    public static String STRATEGY_GROUP_TYPE_ONLINE = "online";

    public static String STRATEGY_INSTANCE_RETRY_COUNT = "retry_count";
    public static String STRATEGY_INSTANCE_DOUBLECHECK_TIME = "doublecheck_time";
    public static String STRATEGY_INSTANCE_SUCCESS_NUM = "success_num";
    public static String STRATEGY_INSTANCE_FAILED_NUM = "failed_num";
    public static String STRATEGY_INSTANCE_ASYNC_TASK_ID = "async_task_id";
    public static String STRATEGY_INSTANCE_ASYNC_TASK_STATUS = "async_task_status";
    public static String STRATEGY_INSTANCE_ASYNC_TASK_EXT = "async_task_ext";

    public static String FILTER_OPERATE_ADD = "add";
    public static String FILTER_OPERATE_DEL = "del";


    public static String getEnumName(String type,String code){
        Map<String, Map<String, String>> enumMap=new HashMap<>();

        Map<String,String> permission=new HashMap<>();
        permission.put(PERMISSION_APPLY_INIT, "未处理");
        permission.put(PERMISSION_APPLY_RUNNING, "处理中");
        permission.put(PERMISSION_APPLY_FAIL, "申请失败");
        permission.put(PERMISSION_APPLY_SUCCESS, "申请通过");
        permission.put(PERMISSION_APPLY_RECALL, "申请撤销");

        Map<String,String> paramType=new HashMap<>();
        paramType.put(PARAM_TYPE_STRING, "字符串");
        paramType.put(PARAM_TYPE_JSON, "JSON");

        enumMap.put("PERMISSION_APPLY", permission);
        enumMap.put("PARAM_TYPE", paramType);

        if(enumMap.containsKey(type)){
            if(enumMap.get(type).containsKey(code)){
                return enumMap.get(type).get(code);
            }else{
                return "";
            }
        }

        return "";

    }
}
