package com.zyc.zdh.monitor;

public class ThreadInfo {
    private String threadId;

    private String threadName;

    private String startTime;

    private String userTime;

    private String upTime;

    private String parentThreadId;

    private String startMemoryAddress;

    private String state;

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }

    public String getParentThreadId() {
        return parentThreadId;
    }

    public void setParentThreadId(String parentThreadId) {
        this.parentThreadId = parentThreadId;
    }

    public String getStartMemoryAddress() {
        return startMemoryAddress;
    }

    public void setStartMemoryAddress(String startMemoryAddress) {
        this.startMemoryAddress = startMemoryAddress;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getUserTime() {
        return userTime;
    }

    public void setUserTime(String userTime) {
        this.userTime = userTime;
    }

    public String getUpTime() {
        return upTime;
    }

    public void setUpTime(String upTime) {
        this.upTime = upTime;
    }
}
