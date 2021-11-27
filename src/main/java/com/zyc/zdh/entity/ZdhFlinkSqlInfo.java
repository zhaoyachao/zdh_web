package com.zyc.zdh.entity;

public class ZdhFlinkSqlInfo {

    //任务记录唯一标识(注意和调度任务的标识不一样)
    private String task_logs_id;


    private TaskLogInstance tli;


    //etl flink任务
    private EtlTaskFlinkInfo etlTaskFlinkInfo;

    public String getTask_logs_id() {
        return task_logs_id;
    }

    public void setTask_logs_id(String task_logs_id) {
        this.task_logs_id = task_logs_id;
    }

    public TaskLogInstance getTli() {
        return tli;
    }

    public void setTli(TaskLogInstance tli) {
        this.tli = tli;
    }

    public EtlTaskFlinkInfo getEtlTaskFlinkInfo() {
        return etlTaskFlinkInfo;
    }

    public void setEtlTaskFlinkInfo(EtlTaskFlinkInfo etlTaskFlinkInfo) {
        this.etlTaskFlinkInfo = etlTaskFlinkInfo;
    }

    public void setZdhInfo(EtlTaskFlinkInfo etlTaskFlinkInfo,TaskLogInstance tli){
        this.tli=tli;
        this.etlTaskFlinkInfo=etlTaskFlinkInfo;

    }

}
