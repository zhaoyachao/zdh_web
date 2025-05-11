package com.zyc.zdh.job;

public enum JobStatus {

    NON("non","无状态"),//为了异步数据一致性
    CREATE("create","实例创建"),
    DISPATCH("dispatch","调度程序调度中(依赖型检查任务)"),
    CHECK_DEP("check_dep","检查依赖中"),
    CHECK_DEP_FINISH("check_dep_finish","检查依赖完成"),
    WAIT_RETRY("wait_retry","等待重试中"),
    ERROR("error","异常"),
    ETL("etl","数据采集中"),
    KILL("kill","杀死中"),
    KILLED("killed","以杀死"),
    FINISH("finish","完成数据采集"),
    SUB_TASK_DISPATCH("sub_task_dispatch","子任务调度中"),
    SKIP("skip","跳过"),
    PAUSE("pause","暂停");//暂停仅适用调度发现模块

    private String value;
    private String desc;

    private JobStatus(String value,String desc) {
        this.value = value;
        this.desc = desc;
    }
    public String getValue() {
        return value;
    }

    public String getDesc() {
        return desc;
    }
}
