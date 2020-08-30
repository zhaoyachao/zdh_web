package com.zyc.zdh.entity;

import java.util.List;

public class ZdhSshInfo {

    //任务记录唯一标识(注意和调度任务的标识不一样)
    private String task_logs_id;

    private QuartzJobInfo quartzJobInfo;

    //etl 任务
    private SshTaskInfo sshTaskInfo;

    private List<JarFileInfo> jarFileInfos;

    private ZdhNginx zdhNginx;

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

    public SshTaskInfo getSshTaskInfo() {
        return sshTaskInfo;
    }

    public void setSshTaskInfo(SshTaskInfo sshTaskInfo) {
        this.sshTaskInfo = sshTaskInfo;
    }

    public List<JarFileInfo> getJarFileInfos() {
        return jarFileInfos;
    }

    public void setJarFileInfos(List<JarFileInfo> jarFileInfos) {
        this.jarFileInfos = jarFileInfos;
    }

    public ZdhNginx getZdhNginx() {
        return zdhNginx;
    }

    public void setZdhNginx(ZdhNginx zdhNginx) {
        this.zdhNginx = zdhNginx;
    }

    public void setZdhInfo(SshTaskInfo sshTaskInfo, QuartzJobInfo quartzJobInfo,ZdhNginx zdhNginx, List<JarFileInfo> jarFileInfos){

        this.quartzJobInfo=quartzJobInfo;

        this.sshTaskInfo=sshTaskInfo;

        this.zdhNginx=zdhNginx;

        this.jarFileInfos=jarFileInfos;
    }


}
