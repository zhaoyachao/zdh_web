package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table
public class JarTaskInfo {

    @Id
    @Column
    private String id;

    @Column
    private String etl_context;

    private String files;

    private String master;

    private String deploy_mode;

    private String cpu;

    private String memory;

    private String main_class;

    private String spark_submit_params;

    private String owner;

    private Timestamp create_time;

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEtl_context() {
        return etl_context;
    }

    public void setEtl_context(String etl_context) {
        this.etl_context = etl_context;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getDeploy_mode() {
        return deploy_mode;
    }

    public void setDeploy_mode(String deploy_mode) {
        this.deploy_mode = deploy_mode;
    }

    public String getCpu() {
        return cpu;
    }

    public void setCpu(String cpu) {
        this.cpu = cpu;
    }

    public String getMemory() {
        return memory;
    }

    public void setMemory(String memory) {
        this.memory = memory;
    }

    public String getMain_class() {
        return main_class;
    }

    public void setMain_class(String main_class) {
        this.main_class = main_class;
    }

    public String getSpark_submit_params() {
        return spark_submit_params;
    }

    public void setSpark_submit_params(String spark_submit_params) {
        this.spark_submit_params = spark_submit_params;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
