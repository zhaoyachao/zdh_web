package com.zyc.zdh.hadoop;

/**
 * 数据库配置信息
 */
public class Dsi_Info {
    private String id;
    private String data_source_context;
    private String data_source_type;
    private String driver;
    private String url;
    private String user;
    private String password;
    private String dbtable;
    private String paths;//表名或者文件全路径

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData_source_context() {
        return data_source_context;
    }

    public void setData_source_context(String data_source_context) {
        this.data_source_context = data_source_context;
    }

    public String getData_source_type() {
        return data_source_type;
    }

    public void setData_source_type(String data_source_type) {
        this.data_source_type = data_source_type;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbtable() {
        return dbtable;
    }

    public void setDbtable(String dbtable) {
        this.dbtable = dbtable;
    }

    public String getPaths() {
        return paths;
    }

    public void setPaths(String paths) {
        this.paths = paths;
    }
}