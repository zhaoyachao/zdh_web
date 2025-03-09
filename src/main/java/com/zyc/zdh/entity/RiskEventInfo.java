package com.zyc.zdh.entity;

import com.zyc.zdh.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Table(name = "risk_event_info")
public class RiskEventInfo extends BaseProductAndDimGroupAuthInfo{
    @Id
    private String id;

    /**
     * 事件类型
     */
    private String event_type;

    /**
     * 事件code
     */
    private String event_code;

    /**
     * 事件名称
     */
    private String event_name;

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
     * 事件配置
     */
    private String event_json;

    /**
     * 归属组
     */
    private String dim_group;

    /**
     * 归属产品
     */
    private String product_code;


    @Transient
    private List<Map<String, Object>> param_json_object;

    public List<Map<String, Object>> getParam_json_object() {
        if(!StringUtils.isEmpty(event_json)){
            return JsonUtil.toJavaListMap(event_json);
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
     * 获取事件类型
     *
     * @return event_type - 事件类型
     */
    public String getEvent_type() {
        return event_type;
    }

    /**
     * 设置事件类型
     *
     * @param event_type 事件类型
     */
    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    /**
     * 获取事件code
     *
     * @return event_code - 事件code
     */
    public String getEvent_code() {
        return event_code;
    }

    /**
     * 设置事件code
     *
     * @param event_code 事件code
     */
    public void setEvent_code(String event_code) {
        this.event_code = event_code;
    }

    /**
     * 获取事件名称
     *
     * @return event_name - 事件名称
     */
    public String getEvent_name() {
        return event_name;
    }

    /**
     * 设置事件名称
     *
     * @param event_name 事件名称
     */
    public void setEvent_name(String event_name) {
        this.event_name = event_name;
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
     * 获取事件配置
     *
     * @return event_json - 事件配置
     */
    public String getEvent_json() {
        return event_json;
    }

    /**
     * 设置事件配置
     *
     * @param event_json 事件配置
     */
    public void setEvent_json(String event_json) {
        this.event_json = event_json;
    }

    public String getDim_group() {
        return dim_group;
    }

    public void setDim_group(String dim_group) {
        this.dim_group = dim_group;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}