package com.zyc.zdh.entity;

import com.alibaba.fastjson.JSON;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.*;

@Table(name = "beacon_fire_alarm_group_info")
public class BeaconFireAlarmGroupInfo {
    @Id
    private String id;

    /**
     * 告警组code
     */
    private String alarm_group_code;

    /**
     * 告警组说明
     */
    private String alarm_group_context;

    /**
     * 产品code
     */
    private String product_code;

    /**
     * 用户组
     */
    private String dim_group;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 更新时间
     */
    private Timestamp update_time;

    /**
     * 是否删除,0:未删除,1:删除
     */
    private String is_delete;

    /**
     * 告警配置,告警类型及告警账号
     */
    private String alarm_config;

    @Transient
    private Map<String, String> alarm_config_map;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取告警组code
     *
     * @return getAlarm_group_code - 告警组code
     */
    public String getAlarm_group_code() {
        return alarm_group_code;
    }

    /**
     * 设置告警组code
     *
     * @param alarm_group_code 告警组code
     */
    public void setAlarm_group_code(String alarm_group_code) {
        this.alarm_group_code = alarm_group_code;
    }

    /**
     * 获取告警组说明
     *
     * @return alarm_group_context - 告警组说明
     */
    public String getAlarm_group_context() {
        return alarm_group_context;
    }

    /**
     * 设置告警组说明
     *
     * @param alarm_group_context 告警组说明
     */
    public void setAlarm_group_context(String alarm_group_context) {
        this.alarm_group_context = alarm_group_context;
    }

    /**
     * 获取产品code
     *
     * @return product_code - 产品code
     */
    public String getProduct_code() {
        return product_code;
    }

    /**
     * 设置产品code
     *
     * @param product_code 产品code
     */
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    /**
     * 获取用户组
     *
     * @return dim_group - 用户组
     */
    public String getDim_group() {
        return dim_group;
    }

    /**
     * 设置用户组
     *
     * @param dim_group 用户组
     */
    public void setDim_group(String dim_group) {
        this.dim_group = dim_group;
    }

    /**
     * 获取拥有者
     *
     * @return owner - 拥有者
     */
    public String getOwner() {
        return owner;
    }

    /**
     * 设置拥有者
     *
     * @param owner 拥有者
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public Timestamp getCreate_time() {
        return create_time;
    }

    /**
     * 设置创建时间
     *
     * @param create_time 创建时间
     */
    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public Timestamp getUpdate_time() {
        return update_time;
    }

    /**
     * 设置更新时间
     *
     * @param update_time 更新时间
     */
    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }

    /**
     * 获取是否删除,0:未删除,1:删除
     *
     * @return is_delete - 是否删除,0:未删除,1:删除
     */
    public String getIs_delete() {
        return is_delete;
    }

    /**
     * 设置是否删除,0:未删除,1:删除
     *
     * @param is_delete 是否删除,0:未删除,1:删除
     */
    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    /**
     * 获取告警配置,告警类型及告警账号
     *
     * @return alarm_config - 告警配置,告警类型及告警账号
     */
    public String getAlarm_config() {
        return alarm_config;
    }

    /**
     * 设置告警配置,告警类型及告警账号
     *
     * @param alarm_config 告警配置,告警类型及告警账号
     */
    public void setAlarm_config(String alarm_config) {
        this.alarm_config = alarm_config;
    }

    public Map<String, String> getAlarm_config_map() {
        if(this.alarm_config == null){
            return new HashMap<>();
        }
        return JSON.parseObject(this.alarm_config, Map.class);
    }

    public void setAlarm_config_map(Map<String, String> alarm_config_map) {
        this.alarm_config_map = alarm_config_map;
    }
}