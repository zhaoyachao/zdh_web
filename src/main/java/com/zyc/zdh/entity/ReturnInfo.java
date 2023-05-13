package com.zyc.zdh.entity;

import com.alibaba.fastjson.JSON;


public class ReturnInfo<T> {

    /**
     * 200:成功,201:失败
     */
    private String code;

    /**
     * 返回说明
     */
    private String msg;

    /**
     * 返回结果
     */
    private T result;

    private Exception e;

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

    public void setResult(T result) {
        this.result = result;
    }

    public Exception getE() {
        return e;
    }

    /**
     * 返回json-string入口
     * @param code
     * @param msg
     * @param result
     * @return
     */
    public static String createInfo(String code,String msg,Object result){
        ReturnInfo ri=new ReturnInfo();
        ri.code=code;
        ri.msg=msg;
        ri.result=result;
        return JSON.toJSONString(ri);
    }

    /**
     * 正常返回ReturnInfo对象入口
     * @param code
     * @param msg
     * @param result
     * @param <T>
     * @return
     */
    public static <T> ReturnInfo<T> build(String code,String msg,T result){
        ReturnInfo ri=new ReturnInfo<>();
        ri.code=code;
        ri.msg=msg;
        ri.result=result;
        return ri;
    }

    /**
     * 异常时返回ReturnInfo对象入口
     * @param code
     * @param msg
     * @param e
     * @param <T>
     * @return
     */
    public static <T> ReturnInfo<T> build(String code,String msg,Exception e){
        ReturnInfo ri=new ReturnInfo<>();
        ri.code=code;
        ri.msg=msg;
        ri.e=e;
        return ri;
    }
}
