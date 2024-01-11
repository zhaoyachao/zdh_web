package com.zyc.zdh.job;

public enum JobGroupType {

    OFFLINE("offline","offline","离线","false"),//为了异步数据一致性
    ONLINE("online","online","在线","false");


    private String value;
    private String code;
    private String desc;
    private String async;//是否异步,true:异步 false:同步

    private JobGroupType(String code, String value, String desc, String async) {
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
