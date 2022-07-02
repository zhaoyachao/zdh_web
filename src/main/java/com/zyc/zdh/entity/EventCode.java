package com.zyc.zdh.entity;

public enum EventCode {

    DATA_PUB("data_pub","数据发布"),
    DATA_APPLY("data_apply","数据申请"),
    PERMISSION_APPLY("permission_apply","权限申请");


    private String code;
    private String desc;

    private EventCode(String code,String desc) {
        this.code = code;

        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
