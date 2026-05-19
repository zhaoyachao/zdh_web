package com.zyc.zdh.entity;


import java.util.ArrayList;
import java.util.List;


public class ZdhDroolsInfo extends ZdhBaseInfo{

    //任务记录唯一标识(注意和调度任务的标识不一样)
    private String task_logs_id;



    private TaskLogInstance tli;

    //输入数据源--单源,多源信息
    private List<Dsi_EtlInfo> dsi_EtlInfo=new ArrayList<>();

    //输出数据源
    private Dsi_Info dsi_Output;

    //etl drools任务
    private EtlDroolsTaskInfo etlDroolsTaskInfo;

    //etl sql 任务
    private SqlTaskInfo sqlTaskInfo;

    //etl 多源任务
    private EtlMoreTaskInfo etlMoreTaskInfo;

    public String getTask_logs_id() {
        return task_logs_id;
    }

    public void setTask_logs_id(String task_logs_id) {
        this.task_logs_id = task_logs_id;
    }

    public Dsi_Info getDsi_Output() {
        return dsi_Output;
    }

    public void setDsi_Output(Dsi_Info dsi_Output) {
        this.dsi_Output = dsi_Output;
    }
    public void setDsi_Output(DataSourcesInfo dataSourcesInfoOutput) {
        this.dsi_Output = dsi_Output;
    }

    public TaskLogInstance getTli() {
        return tli;
    }

    public void setTli(TaskLogInstance tli) {
        this.tli = tli;
    }

    public EtlDroolsTaskInfo getEtlDroolsTaskInfo() {
        return etlDroolsTaskInfo;
    }

    public void setEtlDroolsTaskInfo(EtlDroolsTaskInfo etlDroolsTaskInfo) {
        this.etlDroolsTaskInfo = etlDroolsTaskInfo;
    }

    public List<Dsi_EtlInfo> getDsi_EtlInfo() {
        return dsi_EtlInfo;
    }


    public EtlMoreTaskInfo getEtlMoreTaskInfo() {
        return etlMoreTaskInfo;
    }

    public void setEtlMoreTaskInfo(EtlMoreTaskInfo etlMoreTaskInfo) {
        this.etlMoreTaskInfo = etlMoreTaskInfo;
    }

    public void setDsi_Info(DataSourcesInfo dataSourcesInfoOutput ) {
        Dsi_Info dsi_Output=new Dsi_Info();
        dsi_Output.setId(dataSourcesInfoOutput.getId());
        dsi_Output.setData_source_context(dataSourcesInfoOutput.getData_source_context());
        dsi_Output.setData_source_type(dataSourcesInfoOutput.getData_source_type());
        dsi_Output.setDbtable(etlDroolsTaskInfo.getData_sources_table_name_output());
        dsi_Output.setDriver(dataSourcesInfoOutput.getDriver());
        dsi_Output.setUrl(dataSourcesInfoOutput.getUrl());
        dsi_Output.setUser(dataSourcesInfoOutput.getUsername());
        dsi_Output.setPassword(dataSourcesInfoOutput.getPassword());
        dsi_Output.setPaths(etlDroolsTaskInfo.getData_sources_file_name_output());

        this.dsi_Output = dsi_Output;
    }

    public SqlTaskInfo getSqlTaskInfo() {
        return sqlTaskInfo;
    }

    public void setSqlTaskInfo(SqlTaskInfo sqlTaskInfo) {
        this.sqlTaskInfo = sqlTaskInfo;
    }

    public void setZdhDroolsInfo(DataSourcesInfo dataSourcesInfoInput , EtlTaskInfo etlTaskInfo, DataSourcesInfo dataSourcesInfoOutput, TaskLogInstance tli, EtlDroolsTaskInfo etlDroolsTaskInfo){

        this.tli=tli;
        this.etlDroolsTaskInfo=etlDroolsTaskInfo;

        Dsi_EtlInfo dsi_etlInfo=new Dsi_EtlInfo();
        dsi_etlInfo.setEtlTaskInfo(etlTaskInfo);

        Dsi_Info dsi_Input=new Dsi_Info();
        dsi_Input.setId(dataSourcesInfoInput.getId());
        dsi_Input.setData_source_context(dataSourcesInfoInput.getData_source_context());
        dsi_Input.setData_source_type(dataSourcesInfoInput.getData_source_type());
        dsi_Input.setDbtable(etlTaskInfo.getData_sources_table_name_input());
        dsi_Input.setDriver(dataSourcesInfoInput.getDriver());
        dsi_Input.setUrl(dataSourcesInfoInput.getUrl());
        dsi_Input.setUser(dataSourcesInfoInput.getUsername());
        dsi_Input.setPassword(dataSourcesInfoInput.getPassword());
        dsi_Input.setPaths(etlTaskInfo.getData_sources_file_name_input());
        dsi_etlInfo.setDsi_Input(dsi_Input);
        this.dsi_EtlInfo.add(dsi_etlInfo);

        Dsi_Info dsi_Output=new Dsi_Info();
        dsi_Output.setId(dataSourcesInfoOutput.getId());
        dsi_Output.setData_source_context(dataSourcesInfoOutput.getData_source_context());
        dsi_Output.setData_source_type(dataSourcesInfoOutput.getData_source_type());
        dsi_Output.setDbtable(etlDroolsTaskInfo.getData_sources_table_name_output());
        dsi_Output.setDriver(dataSourcesInfoOutput.getDriver());
        dsi_Output.setUrl(dataSourcesInfoOutput.getUrl());
        dsi_Output.setUser(dataSourcesInfoOutput.getUsername());
        dsi_Output.setPassword(dataSourcesInfoOutput.getPassword());
        dsi_Output.setPaths(etlDroolsTaskInfo.getData_sources_file_name_output());

        this.dsi_Output=dsi_Output;

    }




}
