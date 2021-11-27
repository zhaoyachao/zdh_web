package com.zyc.zdh.entity;

import com.alibaba.fastjson.JSON;


public class ReturnInfo {

    private String code;

    private String msg;

    private Object result;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public static String createInfo(String code,String msg,Object result){
        ReturnInfo ri=new ReturnInfo();
        ri.code=code;
        ri.msg=msg;
        ri.result=result;
        return JSON.toJSONString(ri);
    }
}
