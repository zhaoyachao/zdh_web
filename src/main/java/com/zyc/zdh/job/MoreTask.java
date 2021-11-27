package com.zyc.zdh.job;

public enum MoreTask {

    ETL("1","单源ETL","单源ETL"),//为了异步数据一致性
    MORE_ETL("2","多源ETL","多源ETL"),
    SSH("3","SSH","SSH"),
    SQL("4","SQL","SQL"),
    Drools("5","DROOLS","DROOLS"),
    APPLY("6","APPLY","申请源ETL"),
    FLINK("7","FLINK","FLINK");


    private String value;
    private String code;
    private String desc;

    private MoreTask(String code,String value,String desc) {
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
