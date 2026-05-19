package com.zyc.zdh.entity;

import com.zyc.zdh.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

@Table(name = "push_app_info")
public class PushAppInfo extends BaseProductAndDimGroupAuthInfo{
    @Id
    private String id;

    /**
     * app
     */
    private String app;

    /**
     * app名称
     */
    private String app_name;

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
     * 厂商,多个逗号分割
     */
    private String company;
    /**
     * app配置,json结构
     */
    private String config;

    @Transient
    private Map<String,Object> configMap;

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
     * 获取app
     *
     * @return app - app
     */
    public String getApp() {
        return app;
    }

    /**
     * 设置app
     *
     * @param app app
     */
    public void setApp(String app) {
        this.app = app;
    }

    /**
     * 获取app名称
     *
     * @return app_name - app名称
     */
    public String getApp_name() {
        return app_name;
    }

    /**
     * 设置app名称
     *
     * @param app_name app名称
     */
    public void setApp_name(String app_name) {
        this.app_name = app_name;
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

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public Map<String, Object> getConfigMap(){
        if(StringUtils.isEmpty(config)){
            return new HashMap<String, Object>();
        }
        return JsonUtil.toJavaMap(config);
    }
}