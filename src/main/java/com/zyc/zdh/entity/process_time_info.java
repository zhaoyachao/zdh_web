package com.zyc.zdh.entity;

public class process_time_info {
    private String init_time;
    private String check_dep_time;
    private String command_time;
    private String server_time;//数据采集时间
    private String qa_time;//质量检测时间

    public String getInit_time() {
        return init_time;
    }

    public void setInit_time(String init_time) {
        this.init_time = init_time;
    }

    public String getCheck_dep_time() {
        return check_dep_time;
    }

    public void setCheck_dep_time(String check_dep_time) {
        this.check_dep_time = check_dep_time;
    }

    public String getCommand_time() {
        return command_time;
    }

    public void setCommand_time(String command_time) {
        this.command_time = command_time;
    }

    public String getServer_time() {
        return server_time;
    }

    public void setServer_time(String server_time) {
        this.server_time = server_time;
    }

    public String getQa_time() {
        return qa_time;
    }

    public void setQa_time(String qa_time) {
        this.qa_time = qa_time;
    }
}
