package com.zyc.zdh.entity;

public class EtlEcharts {

    private String etl_date;
    private String running;
    private String error;
    private String finish;

    public String getEtl_date() {
        return etl_date;
    }

    public void setEtl_date(String etl_date) {
        this.etl_date = etl_date;
    }

    public String getRunning() {
        return running;
    }

    public void setRunning(String running) {
        this.running = running;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getFinish() {
        return finish;
    }

    public void setFinish(String finish) {
        this.finish = finish;
    }
}
