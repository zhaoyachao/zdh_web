package com.zyc.zdh.hadoop;

/**
 * 构造返回值信息
 */
public class ReturnInfo {
    private boolean is_success;

    private String msg;

    private Object result;


    public ReturnInfo(boolean is_success, String msg, Object result){

        this.is_success=is_success;
        this.msg=msg;
        this.result=result;
    }

    public boolean isIs_success() {
        return is_success;
    }

    public void setIs_success(boolean is_success) {
        this.is_success = is_success;
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
}
