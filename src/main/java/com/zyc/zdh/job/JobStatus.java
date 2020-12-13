package com.zyc.zdh.job;

public enum JobStatus {

    CREATE("1","create","实例创建"),
    DISPATCH("2","dispatch","调度程序调度中"),
    CHECK_DEP("3","check_dep","检查依赖中"),
    CHECK_DEP_FINISH("4","check_dep_finish","检查依赖完成"),
    WAIT_RETRY("5","wait_retry","等待重试中"),
    ERROR("6","error","异常"),
    ETL("7","etl","数据采集中"),
    KILL("8","kill","杀死中"),
    KILLED("9","killed","以杀死"),
    FINISH("10","finish","完成数据采集"),
    SUB_TASK_DISPATCH("11","sub_task_dispatch","子任务调度中");

    private String value;
    private String code;
    private String desc;

    private JobStatus(String code,String value,String desc) {
        this.code = code;
        this.value = value;
        this.desc = desc;
    }
    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
