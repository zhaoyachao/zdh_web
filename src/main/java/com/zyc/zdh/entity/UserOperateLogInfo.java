package com.zyc.zdh.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "user_operate_log")
public class UserOperateLogInfo {
    @Id
    private String id;

    /**
     * 账号
     */
    private String owner;

    /**
     * 用户名
     */
    private String user_name;

    /**
     * 操作url
     */
    private String operate_url;

    /**
     * 操作说明
     */
    private String operate_context;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 更新时间
     */
    private Timestamp update_time;

    /**
     * 输入参数
     */
    private String operate_input;

    /**
     * 输出结果
     */
    private String operate_output;

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
     * 获取账号
     *
     * @return owner - 账号
     */
    public String getOwner() {
        return owner;
    }

    /**
     * 设置账号
     *
     * @param owner 账号
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * 获取用户名
     *
     * @return user_name - 用户名
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     * 设置用户名
     *
     * @param user_name 用户名
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    /**
     * 获取操作url
     *
     * @return operate_url - 操作url
     */
    public String getOperate_url() {
        return operate_url;
    }

    /**
     * 设置操作url
     *
     * @param operate_url 操作url
     */
    public void setOperate_url(String operate_url) {
        this.operate_url = operate_url;
    }

    /**
     * 获取操作说明
     *
     * @return operate_context - 操作说明
     */
    public String getOperate_context() {
        return operate_context;
    }

    /**
     * 设置操作说明
     *
     * @param operate_context 操作说明
     */
    public void setOperate_context(String operate_context) {
        this.operate_context = operate_context;
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
     * 获取输入参数
     *
     * @return operate_input - 输入参数
     */
    public String getOperate_input() {
        return operate_input;
    }

    /**
     * 设置输入参数
     *
     * @param operate_input 输入参数
     */
    public void setOperate_input(String operate_input) {
        this.operate_input = operate_input;
    }

    /**
     * 获取输出结果
     *
     * @return operate_output - 输出结果
     */
    public String getOperate_output() {
        return operate_output;
    }

    /**
     * 设置输出结果
     *
     * @param operate_output 输出结果
     */
    public void setOperate_output(String operate_output) {
        this.operate_output = operate_output;
    }
}