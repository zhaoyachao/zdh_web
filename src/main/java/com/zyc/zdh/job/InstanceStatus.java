package com.zyc.zdh.job;

/**
 * 废弃,使用JobStatus 代替
 */
@Deprecated
public enum InstanceStatus {
    DISPATCH("dispatch"),WAIT_RETRY("wait_retry"),FINISH("finish"),ERROR("error"),ETL("etl"),KILL("kill");

    private String value;


    private InstanceStatus(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
