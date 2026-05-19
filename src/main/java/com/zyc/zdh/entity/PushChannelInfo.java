package com.zyc.zdh.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "push_channel_info")
public class PushChannelInfo extends BaseProductAndDimGroupAuthInfo{
    @Id
    private String id;

    /**
     * 通道名称
     */
    private String channel_name;

    /**
     * 通道码
     */
    private String channel_code;

    /**
     * 通道类型
     */
    private String channel_type;

    /**
     * 通道服务商
     */
    private String channel_server;

    /**
     * 状态,0:编辑,1:启用,2:禁用
     */
    private String status;

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
     * 产品code
     */
    private String product_code;

    /**
     * 用户组
     */
    private String dim_group;

    /**
     * 通道配置,json结构
     */
    private String config;

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
     * 获取通道名称
     *
     * @return channel_name - 通道名称
     */
    public String getChannel_name() {
        return channel_name;
    }

    /**
     * 设置通道名称
     *
     * @param channel_name 通道名称
     */
    public void setChannel_name(String channel_name) {
        this.channel_name = channel_name;
    }

    /**
     * 获取通道码
     *
     * @return channel_code - 通道码
     */
    public String getChannel_code() {
        return channel_code;
    }

    /**
     * 设置通道码
     *
     * @param channel_code 通道码
     */
    public void setChannel_code(String channel_code) {
        this.channel_code = channel_code;
    }

    /**
     * 获取通道类型
     *
     * @return channel_type - 通道类型
     */
    public String getChannel_type() {
        return channel_type;
    }

    /**
     * 设置通道类型
     *
     * @param channel_type 通道类型
     */
    public void setChannel_type(String channel_type) {
        this.channel_type = channel_type;
    }

    /**
     * 获取通道服务商
     *
     * @return channel_server - 通道服务商
     */
    public String getChannel_server() {
        return channel_server;
    }

    /**
     * 设置通道服务商
     *
     * @param channel_server 通道服务商
     */
    public void setChannel_server(String channel_server) {
        this.channel_server = channel_server;
    }

    /**
     * 获取状态,0:编辑,1:启用,2:禁用
     *
     * @return status - 状态,0:编辑,1:启用,2:禁用
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态,0:编辑,1:启用,2:禁用
     *
     * @param status 状态,0:编辑,1:启用,2:禁用
     */
    public void setStatus(String status) {
        this.status = status;
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
     * 获取产品code
     *
     * @return product_code - 产品code
     */
    @Override
    public String getProduct_code() {
        return product_code;
    }

    /**
     * 设置产品code
     *
     * @param product_code 产品code
     */
    @Override
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    /**
     * 获取用户组
     *
     * @return dim_group - 用户组
     */
    @Override
    public String getDim_group() {
        return dim_group;
    }

    /**
     * 设置用户组
     *
     * @param dim_group 用户组
     */
    @Override
    public void setDim_group(String dim_group) {
        this.dim_group = dim_group;
    }

    /**
     * 获取通道配置,json结构
     *
     * @return config - 通道配置,json结构
     */
    public String getConfig() {
        return config;
    }

    /**
     * 设置通道配置,json结构
     *
     * @param config 通道配置,json结构
     */
    public void setConfig(String config) {
        this.config = config;
    }


}