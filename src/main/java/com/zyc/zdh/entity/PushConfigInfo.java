package com.zyc.zdh.entity;

import com.zyc.zdh.util.JsonUtil;

import java.sql.Timestamp;
import java.util.Map;
import javax.persistence.*;

@Table(name = "push_config_info")
public class PushConfigInfo extends BaseProductAuthInfo{
    @Id
    private String id;

    /**
     * 配置标识
     */
    private String config_key;

    /**
     * 配置名称
     */
    private String config_name;

    /**
     * 配置类型
     */
    private String config_type;

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
     * app配置,json结构
     */
    private String config;

    @Transient
    private Map<String, Object> config_json;

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
     * 获取配置标识
     *
     * @return config_key - 配置标识
     */
    public String getConfig_key() {
        return config_key;
    }

    /**
     * 设置配置标识
     *
     * @param config_key 配置标识
     */
    public void setConfig_key(String config_key) {
        this.config_key = config_key;
    }

    /**
     * 获取配置名称
     *
     * @return config_name - 配置名称
     */
    public String getConfig_name() {
        return config_name;
    }

    /**
     * 设置配置名称
     *
     * @param config_name 配置名称
     */
    public void setConfig_name(String config_name) {
        this.config_name = config_name;
    }

    /**
     * 获取配置类型
     *
     * @return config_type - 配置类型
     */
    public String getConfig_type() {
        return config_type;
    }

    /**
     * 设置配置类型
     *
     * @param config_type 配置类型
     */
    public void setConfig_type(String config_type) {
        this.config_type = config_type;
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
     * 获取app配置,json结构
     *
     * @return config - app配置,json结构
     */
    public String getConfig() {
        return config;
    }

    /**
     * 设置app配置,json结构
     *
     * @param config app配置,json结构
     */
    public void setConfig(String config) {
        this.config = config;
    }

    public Map<String, Object> getConfig_json() {
		return JsonUtil.toJavaMap(config);
	}

    public void setConfig_json(Map<String, Object> config_json) {
		this.config_json = config_json;
	}
}