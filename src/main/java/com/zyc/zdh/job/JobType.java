package com.zyc.zdh.job;

public enum JobType {

    ETL("ETL","ETL","ETL任务","true"),//为了异步数据一致性
    EMAIL("EMAIL","EMAIL","告警任务","false"),
    CHECK("CHECK","CHECK","检查任务依赖","true"),
    RETRY("RETRY","RETRY","检查失败需要重试的任务","true"),
    BLOOD("BLOOD","BLOOD","血缘任务","true"),
    FLUME("FLUME","FLUME","FLUME任务","false"),
    SHELL("SHELL","SHELL","SHELL任务","false"),
    HTTP("HTTP","HTTP","HTTP任务","false"),
    DIGITALMARKET("DIGITALMARKET","DIGITALMARKET","智能营销任务","true");


    private String value;
    private String code;
    private String desc;
    private String async;//是否异步,true:异步 false:同步

    private JobType(String code, String value, String desc,String async) {
        this.code = code;
        this.value = value;
        this.desc = desc;
        this.async=async;
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
