package com.zyc.zdh.es;

/**
 * 同步写入监控,其他开发人员可实现此接口,完成定制开发
 */
public interface ResponseListener<T>{

    /**
     * 处理成功
     * @param t
     */
    public void success(T t);

    /**
     * 处理失败
     * @param t
     */
    public void fail(T t);

    /**
     * 单条写入时使用
     * @param e
     */
    public void fail(Exception e);

}