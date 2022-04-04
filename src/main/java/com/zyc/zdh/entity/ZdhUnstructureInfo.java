package com.zyc.zdh.entity;



public class ZdhUnstructureInfo {

    //任务记录唯一标识(注意和调度任务的标识不一样)
    private String task_logs_id;



    private TaskLogInstance tli;

    //输入数据源
    private Dsi_Info dsi_Input;


    //输出数据源
    private Dsi_Info dsi_Output;

    //输出数据源
    private Dsi_Info dsi_Output_Jdbc;

    //etl 任务
    private EtlTaskUnstructureInfo etlTaskUnstructureInfo;

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

    public Dsi_Info getDsi_Output_Jdbc() {
        return dsi_Output_Jdbc;
    }

    public void setDsi_Output_Jdbc(Dsi_Info dsi_Output_Jdbc) {
        this.dsi_Output_Jdbc = dsi_Output_Jdbc;
    }

    public EtlTaskUnstructureInfo getEtlTaskUnstructureInfo() {
        return etlTaskUnstructureInfo;
    }

    public void setEtlTaskUnstructureInfo(EtlTaskUnstructureInfo etlTaskUnstructureInfo) {
        this.etlTaskUnstructureInfo = etlTaskUnstructureInfo;
    }

    public TaskLogInstance getTli() {
        return tli;
    }

    public void setTli(TaskLogInstance tli) {
        this.tli = tli;
    }

    public void setZdhInfo(DataSourcesInfo dataSourcesInfoInput , EtlTaskUnstructureInfo etlTaskUnstructureInfo, DataSourcesInfo dataSourcesInfoOutput,DataSourcesInfo dataSourcesInfoOutputJdbc,TaskLogInstance tli){

        this.tli=tli;

        this.etlTaskUnstructureInfo=etlTaskUnstructureInfo;
        Dsi_Info dsi_Input=new Dsi_Info();
        dsi_Input.setId(dataSourcesInfoInput.getId());
        dsi_Input.setData_source_context(dataSourcesInfoInput.getData_source_context());
        dsi_Input.setData_source_type(dataSourcesInfoInput.getData_source_type());
        dsi_Input.setDriver(dataSourcesInfoInput.getDriver());
        dsi_Input.setUrl(dataSourcesInfoInput.getUrl());
        dsi_Input.setUser(dataSourcesInfoInput.getUsername());
        dsi_Input.setPassword(dataSourcesInfoInput.getPassword());
        dsi_Input.setPaths(etlTaskUnstructureInfo.getInput_path());
        this.dsi_Input=dsi_Input;

        Dsi_Info dsi_Output=new Dsi_Info();
        dsi_Output.setId(dataSourcesInfoOutput.getId());
        dsi_Output.setData_source_context(dataSourcesInfoOutput.getData_source_context());
        dsi_Output.setData_source_type(dataSourcesInfoOutput.getData_source_type());
        dsi_Input.setDriver(dataSourcesInfoOutput.getDriver());
        dsi_Output.setUrl(dataSourcesInfoOutput.getUrl());
        dsi_Output.setUser(dataSourcesInfoOutput.getUsername());
        dsi_Output.setPassword(dataSourcesInfoOutput.getPassword());
        dsi_Output.setPaths(etlTaskUnstructureInfo.getOutput_path());
        this.dsi_Output=dsi_Output;

        Dsi_Info dsi_OutputJdbc=new Dsi_Info();
        if(dataSourcesInfoOutputJdbc!=null){
            dsi_OutputJdbc.setId(dataSourcesInfoOutputJdbc.getId());
            dsi_OutputJdbc.setData_source_context(dataSourcesInfoOutputJdbc.getData_source_context());
            dsi_OutputJdbc.setData_source_type(dataSourcesInfoOutputJdbc.getData_source_type());
            dsi_OutputJdbc.setDriver(dataSourcesInfoOutputJdbc.getDriver());
            dsi_OutputJdbc.setUrl(dataSourcesInfoOutputJdbc.getUrl());
            dsi_OutputJdbc.setUser(dataSourcesInfoOutputJdbc.getUsername());
            dsi_OutputJdbc.setPassword(dataSourcesInfoOutputJdbc.getPassword());
        }

        this.dsi_Output_Jdbc=dsi_OutputJdbc;


    }


}
