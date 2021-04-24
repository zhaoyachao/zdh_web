package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
@Table(name = "data_sources_info")
public class DataSourcesInfo extends PageBase implements Serializable {

    private static final long serialVersionUID = -2126718447287158588L;


    @Id
    @Column(name = "id")
    private String id;
    private String data_source_context;
    private String data_source_type;
    private String driver;
    private String url;
    private String username;
    private String password;

    private String  owner;

    private String is_delete;

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setData_source_context(String data_source_context) {
        this.data_source_context = data_source_context;
    }

    public void setData_source_type(String data_source_type) {
        this.data_source_type = data_source_type;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getData_source_context() {
        return data_source_context;
    }

    public String getData_source_type() {
        return data_source_type;
    }

    public String getDriver() {
        return driver;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }


}
