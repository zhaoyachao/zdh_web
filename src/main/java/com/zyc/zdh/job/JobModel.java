package com.zyc.zdh.job;

public enum JobModel {

    TIME_SEQ("1"),ONCE("2"),REPEAT("3");

    private String value;


    private JobModel(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
