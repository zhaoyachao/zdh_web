package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 *
 */
@Table
public class BloodSourceInfo {

    @Id
    @Column
    private String id;

    private String context;

    private String input_type;

    //输入源唯一标识，数据源类型+数据源url 组合生成的md5
    private String input_md5;

    //数据库名称+表名/远程文件路径
    private String input;

    private String output_type;

    private String output_md5;

    private String output;

    private String version;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getInput_type() {
        return input_type;
    }

    public void setInput_type(String input_type) {
        this.input_type = input_type;
    }

    public String getInput_md5() {
        return input_md5;
    }

    public void setInput_md5(String input_md5) {
        this.input_md5 = input_md5;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput_type() {
        return output_type;
    }

    public void setOutput_type(String output_type) {
        this.output_type = output_type;
    }

    public String getOutput_md5() {
        return output_md5;
    }

    public void setOutput_md5(String output_md5) {
        this.output_md5 = output_md5;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
