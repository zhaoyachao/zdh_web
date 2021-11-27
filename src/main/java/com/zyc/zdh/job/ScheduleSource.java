package com.zyc.zdh.job;

public enum ScheduleSource {

    SYSTEM("1","system","例行"),//为了异步数据一致性
    MANUAL("2","manual","手动");

    private String value;
    private String code;
    private String desc;

    private ScheduleSource(String code, String value, String desc) {
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
