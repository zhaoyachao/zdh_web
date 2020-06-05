package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
public class ZdhNginx {

    @Id
    @Column
    private String id;
    private String username;
    private String password;
    private String host;
    private String port="22";
    private String owner;
    private String tmp_dir;
    private String nginx_dir;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTmp_dir() {
        return tmp_dir;
    }

    public void setTmp_dir(String tmp_dir) {
        this.tmp_dir = tmp_dir;
    }

    public String getNginx_dir() {
        return nginx_dir;
    }

    public void setNginx_dir(String nginx_dir) {
        this.nginx_dir = nginx_dir;
    }
}
