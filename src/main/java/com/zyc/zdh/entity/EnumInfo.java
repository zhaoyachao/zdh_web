package com.zyc.zdh.entity;

import com.zyc.zdh.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Table(name = "enum_info")
public class EnumInfo extends BaseProductAuthInfo{
    @Id
    private String id;

    /**
     * 枚举标识
     */
    private String enum_code;

    /**
     * 枚举说明
     */
    private String enum_context;

    /**
     * 枚举类型
     */
    private String enum_type;

    /**
     * 拥有者
     */
    private String owner;

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
     * 枚举明细
     */
    private String enum_json;

    @Transient
    private List<Map<String, Object>> enum_json_object;

    /**
     * 产品code
     */
    private String product_code;

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
     * 获取枚举标识
     *
     * @return enum_code - 枚举标识
     */
    public String getEnum_code() {
        return enum_code;
    }

    /**
     * 设置枚举标识
     *
     * @param enum_code 枚举标识
     */
    public void setEnum_code(String enum_code) {
        this.enum_code = enum_code;
    }

    /**
     * 获取枚举说明
     *
     * @return enum_context - 枚举说明
     */
    public String getEnum_context() {
        return enum_context;
    }

    /**
     * 设置枚举说明
     *
     * @param enum_context 枚举说明
     */
    public void setEnum_context(String enum_context) {
        this.enum_context = enum_context;
    }

    /**
     * 获取枚举类型
     *
     * @return enum_type - 枚举类型
     */
    public String getEnum_type() {
        return enum_type;
    }

    /**
     * 设置枚举类型
     *
     * @param enum_type 枚举类型
     */
    public void setEnum_type(String enum_type) {
        this.enum_type = enum_type;
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
     * 获取枚举明细
     *
     * @return enum_json - 枚举明细
     */
    public String getEnum_json() {
        return enum_json;
    }

    /**
     * 设置枚举明细
     *
     * @param enum_json 枚举明细
     */
    public void setEnum_json(String enum_json) {
        this.enum_json = enum_json;
    }

    public List<Map<String, Object>> getEnum_json_object() {
        if(!StringUtils.isEmpty(enum_json)){
            return JsonUtil.toJavaListMap(enum_json);
        }
        return new ArrayList<>();
    }

    public void setEnum_json_object(List<Map<String, Object>> enum_json_object) {
        this.enum_json_object = enum_json_object;
    }

    @Override
    public String getProduct_code() {
        return product_code;
    }

    @Override
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}