package com.zyc.zdh.monitor;

import com.zyc.zdh.ZdhApplication;
import com.zyc.zdh.util.DateUtil;

import java.lang.management.ManagementFactory;
import java.sql.Timestamp;
import java.util.Date;

/**
 * JVM相关信息
 * @author zengxueqi
 * @since 2020/07/14
 */
public class Jvm {

    /**
     * 当前JVM占用的内存总数(M)
     */
    private double total;

    /**
     * JVM最大可用内存总数(M)
     */
    private double max;

    /**
     * JVM空闲内存(M)
     */
    private double free;

    /**
     * JDK版本
     */
    private String version;

    /**
     * JDK路径
     */
    private String home;

    public double getTotal() {
        return Arith.div(total, (1024 * 1024), 2);
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getMax() {
        return Arith.div(max, (1024 * 1024), 2);
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getFree() {
        return Arith.div(free, (1024 * 1024), 2);
    }

    public void setFree(double free) {
        this.free = free;
    }

    public double getUsed() {
        return Arith.div(total - free, (1024 * 1024), 2);
    }

    public double getUsage() {
        return Arith.mul(Arith.div(total - free, total, 4), 100);
    }

    /**
     * 获取JDK名称
     */
    public String getName() {
        return ManagementFactory.getRuntimeMXBean().getVmName();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    /**
     * JDK启动时间
     */
    public String getStartTime() {
        return ZdhApplication.start_time;
    }

    /**
     * JDK运行时间
     */
    public String getRunTime() {

        Timestamp end = new Timestamp(new Date().getTime());
        Timestamp start = Timestamp.valueOf(ZdhApplication.start_time);
        long day=(end.getTime() -start.getTime())/(1000 * 60 * 60 * 24);
        long hour = ((end.getTime()-start.getTime()) / (60 * 60 * 1000) - day * 24);
        long min = (((end.getTime()-start.getTime()) / (60 * 1000)) - day * 24 * 60 - hour * 60);
        return day+"天"+hour+"小时"+min+"分";
    }

}