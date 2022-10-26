package com.zyc.zdh.job;

/**
 * 统一检查接口,实现此接口,可动态控制任务检查
 */
public interface CheckDepJobInterface {

    public void setObject(Object o);
    public void run();

}
