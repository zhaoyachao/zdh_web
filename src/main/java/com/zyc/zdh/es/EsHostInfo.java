package com.zyc.zdh.es;

public class EsHostInfo {

    public EsHostInfo(String hostname, String port){
        new EsHostInfo(hostname,port,"http");
    }
    public EsHostInfo(String hostname, String port, String type){
        this.hostname=hostname;
        this.port=port;
        this.type=type;
    }
    private String hostname;

    private String port;

    private String type = "http";

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
