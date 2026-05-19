package com.zyc.zdh.entity;


import java.sql.Timestamp;

/**
 * 烽火台任务
 */
public class BeaconFireTask {

    private String beacon_fire_id;

    private Timestamp cur_time;

    public String getBeacon_fire_id() {
        return beacon_fire_id;
    }

    public void setBeacon_fire_id(String beacon_fire_id) {
        this.beacon_fire_id = beacon_fire_id;
    }

    public Timestamp getCur_time() {
        return cur_time;
    }

    public void setCur_time(Timestamp cur_time) {
        this.cur_time = cur_time;
    }

    public void init(String beacon_fire_id, Timestamp cur_time){
        this.beacon_fire_id = beacon_fire_id;
        this.cur_time = cur_time;
    }
}