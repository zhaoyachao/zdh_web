package com.zyc.zdh.job;

/**
 * ETL任务类型
 */
public enum MoreTask {

    ETL("1","ETL","单源ETL"),//为了异步数据一致性
    MORE_ETL("2","MORE_ETL","多源ETL"),
    SSH("3","SSH","SSH"),
    SQL("4","SQL","SQL"),
    Drools("5","DROOLS","DROOLS"),
    APPLY("6","APPLY","申请源ETL"),
    FLINK("7","FLINK","FLINK"),
    JDBC("8","JDBC","JDBC"),
    DATAX("9","DATAX","DATAX"),
    QUALITY("10","QUALITY","质量检测"),
    UNSTRUCTURE("11","UNSTRUCTURE","非结构化采集"),
    DATAX_WEB("12","DATAX_WEB","DATAX_WEB"),
    KETTLE("13","KETTLE","KETTLE");


    private String code;
    private String value;
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
