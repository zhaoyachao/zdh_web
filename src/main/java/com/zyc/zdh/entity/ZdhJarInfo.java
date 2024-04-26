package com.zyc.zdh.entity;

import java.util.List;

public class ZdhJarInfo extends ZdhBaseInfo{

    //任务记录唯一标识(注意和调度任务的标识不一样)
    private String task_logs_id;

    private QuartzJobInfo quartzJobInfo;

    private ZdhNginx zdhNginx;

    private List<JarFileInfo> jarFileInfos;

    //etl 任务
    private JarTaskInfo jarTaskInfo;

    public String getTask_logs_id() {
        return task_logs_id;
    }

    public void setTask_logs_id(String task_logs_id) {
        this.task_logs_id = task_logs_id;
    }

    public QuartzJobInfo getQuartzJobInfo() {
        return quartzJobInfo;
    }

    public void setQuartzJobInfo(QuartzJobInfo quartzJobInfo) {
        this.quartzJobInfo = quartzJobInfo;
    }

    public ZdhNginx getZdhNginx() {
        return zdhNginx;
    }

    public void setZdhNginx(ZdhNginx zdhNginx) {
        this.zdhNginx = zdhNginx;
    }

    public JarTaskInfo getJarTaskInfo() {
        return jarTaskInfo;
    }

    public void setJarTaskInfo(JarTaskInfo jarTaskInfo) {
        this.jarTaskInfo = jarTaskInfo;
    }

    public void setZdhInfo(JarTaskInfo jarTaskInfo, QuartzJobInfo quartzJobInfo, ZdhNginx zdhNginx,List<JarFileInfo> jarFileInfos){

        // this.dataSourcesInfoInput=dataSourcesInfoInput;

        // this.dataSourcesInfoOutput=dataSourcesInfoOutput;
        this.quartzJobInfo=quartzJobInfo;
        this.jarTaskInfo=jarTaskInfo;
        this.zdhNginx=zdhNginx;
        this.jarFileInfos=jarFileInfos;

    }

    public List<JarFileInfo> getJarFileInfos() {
        return jarFileInfos;
    }

    public void setJarFileInfos(List<JarFileInfo> jarFileInfos) {
        this.jarFileInfos = jarFileInfos;
    }
}
