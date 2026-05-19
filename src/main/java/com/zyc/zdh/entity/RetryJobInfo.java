package com.zyc.zdh.entity;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class RetryJobInfo implements Delayed {

    private long time;
    private String name;
    private QuartzJobInfo quartzJobInfo;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public QuartzJobInfo getQuartzJobInfo() {
        return quartzJobInfo;
    }

    public void setQuartzJobInfo(QuartzJobInfo quartzJobInfo) {
        this.quartzJobInfo = quartzJobInfo;
    }

    public RetryJobInfo(String name, QuartzJobInfo quartzJobInfo, long time, TimeUnit unit) {
        this.name = name;
        this.time = System.currentTimeMillis() + (time > 0 ? unit.toMillis(time) : 0);
        this.quartzJobInfo = quartzJobInfo;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        return time - System.currentTimeMillis();
    }

    @Override
    public int compareTo(Delayed o) {
        RetryJobInfo item = (RetryJobInfo) o;
        long diff = this.time - item.time;
        if (diff <= 0) {// 改成>=会造成问题
            return -1;
        }else {
            return 1;
        }
    }
}
