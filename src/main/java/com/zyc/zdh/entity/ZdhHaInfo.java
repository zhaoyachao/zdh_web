package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Table(name = "zdh_ha_info")
public class ZdhHaInfo extends PageBase implements Serializable {


    @Id
    @Column(name = "ID")
    private String id;
    private String zdh_instance;
    private String zdh_url;
    private String zdh_host;
    private String zdh_port;
    private String web_port;
    private String zdh_status;
    private String application_id;
    private String history_server;
    private String master;
    private String online;//0下线,1在线,2下线并退出


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getZdh_instance() {
        return zdh_instance;
    }

    public void setZdh_instance(String zdh_instance) {
        this.zdh_instance = zdh_instance;
    }

    public String getZdh_url() {
        return zdh_url;
    }

    public void setZdh_url(String zdh_url) {
        this.zdh_url = zdh_url;
    }

    public String getZdh_host() {
        return zdh_host;
    }

    public void setZdh_host(String zdh_host) {
        this.zdh_host = zdh_host;
    }

    public String getZdh_port() {
        return zdh_port;
    }

    public void setZdh_port(String zdh_port) {
        this.zdh_port = zdh_port;
    }

    public String getZdh_status() {
        return zdh_status;
    }

    public void setZdh_status(String zdh_status) {
        this.zdh_status = zdh_status;
    }

    public String getWeb_port() {
        return web_port;
    }

    public void setWeb_port(String web_port) {
        this.web_port = web_port;
    }

    public String getApplication_id() {
        return application_id;
    }

    public void setApplication_id(String application_id) {
        this.application_id = application_id;
    }

    public String getHistory_server() {
        return history_server;
    }

    public void setHistory_server(String history_server) {
        this.history_server = history_server;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getOnline() {
        return online;
    }

    public void setOnline(String online) {
        this.online = online;
    }
}
