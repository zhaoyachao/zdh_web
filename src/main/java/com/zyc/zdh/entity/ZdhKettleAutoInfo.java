package com.zyc.zdh.entity;



public class ZdhKettleAutoInfo {

    //任务记录唯一标识(注意和调度任务的标识不一样)
    private String task_logs_id;

    private TaskLogInstance tli;

    //输入数据源
    private Dsi_Info dsi_Input;


    //输出数据源
    private Dsi_Info dsi_Output;


    //etl 任务
    private EtlTaskKettleInfo etlTaskKettleInfo;

    public String getTask_logs_id() {
        return task_logs_id;
    }

    public void setTask_logs_id(String task_logs_id) {
        this.task_logs_id = task_logs_id;
    }

    public Dsi_Info getDsi_Input() {
        return dsi_Input;
    }

    public void setDsi_Input(Dsi_Info dsi_Input) {
        this.dsi_Input = dsi_Input;
    }

    public Dsi_Info getDsi_Output() {
        return dsi_Output;
    }

    public void setDsi_Output(Dsi_Info dsi_Output) {
        this.dsi_Output = dsi_Output;
    }

    public EtlTaskKettleInfo getEtlTaskKettleInfo() {
        return etlTaskKettleInfo;
    }

    public void setEtlTaskKettleInfo(EtlTaskKettleInfo etlTaskKettleInfo) {
        this.etlTaskKettleInfo = etlTaskKettleInfo;
    }

    public TaskLogInstance getTli() {
        return tli;
    }

    public void setTli(TaskLogInstance tli) {
        this.tli = tli;
    }

    public void setZdhInfo(DataSourcesInfo dataSourcesInfoInput , EtlTaskKettleInfo etlTaskKettleInfo, TaskLogInstance tli){

        this.tli=tli;

        this.etlTaskKettleInfo=etlTaskKettleInfo;
        Dsi_Info dsi_Input=new Dsi_Info();
        dsi_Input.setId(dataSourcesInfoInput.getId());
        dsi_Input.setData_source_context(dataSourcesInfoInput.getData_source_context());
        dsi_Input.setData_source_type(dataSourcesInfoInput.getData_source_type());
        dsi_Input.setDriver(dataSourcesInfoInput.getDriver());
        dsi_Input.setUrl(dataSourcesInfoInput.getUrl());
        dsi_Input.setUser(dataSourcesInfoInput.getUsername());
        dsi_Input.setPassword(dataSourcesInfoInput.getPassword());

        this.dsi_Input=dsi_Input;

    }


}
