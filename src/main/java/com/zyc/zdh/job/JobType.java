package com.zyc.zdh.job;

public enum JobType {

    ETL("ETL","ETL","ETL任务"),//为了异步数据一致性
    EMAIL("EMAIL","EMAIL","告警任务"),
    CHECK("CHECK","CHECK","检查任务依赖"),
    RETRY("RETRY","RETRY","检查失败需要重试的任务"),
    BLOOD("BLOOD","BLOOD","血缘任务");


    private String value;
    private String code;
    private String desc;

    private JobType(String code, String value, String desc) {
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
