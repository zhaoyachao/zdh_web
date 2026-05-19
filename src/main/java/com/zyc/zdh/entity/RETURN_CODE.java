package com.zyc.zdh.entity;

public enum RETURN_CODE {

    SUCCESS("200","success","成功"),
    FAIL("201","fail","失败");

    private String value;
    private String code;
    private String desc;

    private RETURN_CODE(String code,String value,String desc) {
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
