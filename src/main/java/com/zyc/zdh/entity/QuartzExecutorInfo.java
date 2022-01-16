package com.zyc.zdh.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "quartz_executor_info")
public class QuartzExecutorInfo {
    @Id
    private String id;

    /**
     * 调度器唯一实例名
     */
    private String instance_name;

    /**
     * 任务说明
     */
    private String status;

    /**
     * 是否处理过,true/false
     */
    private String is_handle;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 更新时间
     */
    private Timestamp update_time;

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
     * 获取调度器唯一实例名
     *
     * @return instance_name - 调度器唯一实例名
     */
    public String getInstance_name() {
        return instance_name;
    }

    /**
     * 设置调度器唯一实例名
     *
     * @param instance_name 调度器唯一实例名
     */
    public void setInstance_name(String instance_name) {
        this.instance_name = instance_name;
    }

    /**
     * 获取任务说明
     *
     * @return status - 任务说明
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置任务说明
     *
     * @param status 任务说明
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取是否处理过,true/false
     *
     * @return is_handle - 是否处理过,true/false
     */
    public String getIs_handle() {
        return is_handle;
    }

    /**
     * 设置是否处理过,true/false
     *
     * @param is_handle 是否处理过,true/false
     */
    public void setIs_handle(String is_handle) {
        this.is_handle = is_handle;
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
}