package com.zyc.zdh.entity;

import com.zyc.zdh.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Table(name = "customer_manager_info")
public class CustomerManagerInfo extends BaseProductAuthInfo{
    @Id
    private String id;

    /**
     * 用户id
     */
    private String uid;

    /**
     * 用户id类型
     */
    private String uid_type;

    /**
     * 用户所属平台
     */
    private String source;

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
     * 其他信息--公司,地址等
     */
    private String config;

    @Transient
    private List<Map<String, Object>> param_json_object;

    public List<Map<String, Object>> getParam_json_object() {
        if(!StringUtils.isEmpty(config)){
            return JsonUtil.toJavaListMap(config);
        }
        return param_json_object;
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
     * 获取用户id
     *
     * @return uid - 用户id
     */
    public String getUid() {
        return uid;
    }

    /**
     * 设置用户id
     *
     * @param uid 用户id
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * 获取用户id类型
     *
     * @return uid_type - 用户id类型
     */
    public String getUid_type() {
        return uid_type;
    }

    /**
     * 设置用户id类型
     *
     * @param uid_type 用户id类型
     */
    public void setUid_type(String uid_type) {
        this.uid_type = uid_type;
    }

    /**
     * 获取用户所属平台
     *
     * @return source - 用户所属平台
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置用户所属平台
     *
     * @param source 用户所属平台
     */
    public void setSource(String source) {
        this.source = source;
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
     * 获取其他信息--公司,地址等
     *
     * @return config - 其他信息--公司,地址等
     */
    public String getConfig() {
        return config;
    }

    /**
     * 设置其他信息--公司,地址等
     *
     * @param config 其他信息--公司,地址等
     */
    public void setConfig(String config) {
        this.config = config;
    }


}