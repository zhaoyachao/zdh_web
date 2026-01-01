package com.zyc.zdh.pushx.entity;

public class PushxBaseResponse {
    private Integer code;
    private String msg;
    private Object data;
    private Object third_party_code;
    private String third_party_message;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isSuccess(){
        return this.code == 0;
    }

    public Object getThird_party_code() {
        return third_party_code;
    }

    public void setThird_party_code(Object third_party_code) {
        this.third_party_code = third_party_code;
    }

    public String getThird_party_message() {
        return third_party_message;
    }

    public void setThird_party_message(String third_party_message) {
        this.third_party_message = third_party_message;
    }
}
