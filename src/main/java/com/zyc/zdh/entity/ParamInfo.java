package com.zyc.zdh.entity;

import com.zyc.zdh.util.Const;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;

@Table(name = "param_info")
public class ParamInfo {
    @Id
    private String id;

    /**
     * 参数名称
     */
    private String param_name;

    /**
     * 参数说明
     */
    private String param_context;

    /**
     * 参数类型
     */
    private String param_type;

    /**
     * 缓存超时时间,单位秒
     */
    private String param_timeout;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 状态,启用:on, 关闭:off
     */
    private String status;

    /**
     * 是否删除,0:未删除,1:删除
     */
    private String is_delete;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 更新时间
     */
    private Timestamp update_time;

    /**
     * 参数名称
     */
    private String param_value;

    @Transient
    private String param_type_name;

    public String getParam_type_name(){
        return Const.getEnumName("PARAM_TYPE", getParam_type());
    }

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
     * 获取参数名称
     *
     * @return param_name - 参数名称
     */
    public String getParam_name() {
        return param_name;
    }

    /**
     * 设置参数名称
     *
     * @param param_name 参数名称
     */
    public void setParam_name(String param_name) {
        this.param_name = param_name;
    }

    /**
     * 获取参数说明
     *
     * @return param_context - 参数说明
     */
    public String getParam_context() {
        return param_context;
    }

    /**
     * 设置参数说明
     *
     * @param param_context 参数说明
     */
    public void setParam_context(String param_context) {
        this.param_context = param_context;
    }

    /**
     * 获取参数类型
     *
     * @return param_type - 参数类型
     */
    public String getParam_type() {
        return param_type;
    }

    /**
     * 设置参数类型
     *
     * @param param_type 参数类型
     */
    public void setParam_type(String param_type) {
        this.param_type = param_type;
    }

    /**
     * 获取缓存超时时间,单位秒
     *
     * @return param_timeout - 缓存超时时间,单位秒
     */
    public String getParam_timeout() {
        return param_timeout;
    }

    /**
     * 设置缓存超时时间,单位秒
     *
     * @param param_timeout 缓存超时时间,单位秒
     */
    public void setParam_timeout(String param_timeout) {
        this.param_timeout = param_timeout;
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
     * 获取状态,启用:on, 关闭:off
     *
     * @return status - 状态,启用:on, 关闭:off
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态,启用:on, 关闭:off
     *
     * @param status 状态,启用:on, 关闭:off
     */
    public void setStatus(String status) {
        this.status = status;
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
     * 获取参数名称
     *
     * @return param_value - 参数名称
     */
    public String getParam_value() {
        return param_value;
    }

    /**
     * 设置参数名称
     *
     * @param param_value 参数名称
     */
    public void setParam_value(String param_value) {
        this.param_value = param_value;
    }
}