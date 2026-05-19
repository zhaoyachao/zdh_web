package com.zyc.zdh.job;

public enum StrategyInstanceType {

    LABEL("LABEL","LABEL","LABEL任务"),//为了异步数据一致性
    CROWD_RULE("CROWD_RULE","CROWD_RULE","CROWD_RULE任务"),
    FILTER("FILTER","FILTER","FILTER任务"),
    SHUNT("SHUNT","SHUNT","SHUNT任务"),
    RIGHTS("RIGHTS","RIGHTS","RIGHTS任务");


    private String value;
    private String code;
    private String desc;

    private StrategyInstanceType(String code, String value, String desc) {
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
