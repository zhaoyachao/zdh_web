package com.zyc.zdh.entity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "label_info")
public class LabelInfo {
    @Id
    private String id;

    /**
     * 标签名
     */
    private String label_code;

    /**
     * 说明
     */
    private String label_context;

    /**
     * 标签分类
     */
    private String label_type;

    /**
     * 联系人
     */
    private String label_person;

    /**
     * 账号
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
     * 标签计算引擎,mysql,hive,spark,presto
     */
    private String label_engine;

    /**
     * 标签可用参数
     */
    private String param_json;

    /**
     * 运算表达式,仅支持sql
     */
    private String label_expression;

    @Transient
    private JSONArray param_json_object;

    public JSONArray getParam_json_object() {
        if(!StringUtils.isEmpty(param_json)){
            return JSON.parseArray(param_json);
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
     * 获取标签名
     *
     * @return label_code - 标签名
     */
    public String getLabel_code() {
        return label_code;
    }

    /**
     * 设置标签名
     *
     * @param label_code 标签名
     */
    public void setLabel_code(String label_code) {
        this.label_code = label_code;
    }

    /**
     * 获取说明
     *
     * @return label_context - 说明
     */
    public String getLabel_context() {
        return label_context;
    }

    /**
     * 设置说明
     *
     * @param label_context 说明
     */
    public void setLabel_context(String label_context) {
        this.label_context = label_context;
    }

    /**
     * 获取标签分类
     *
     * @return label_type - 标签分类
     */
    public String getLabel_type() {
        return label_type;
    }

    /**
     * 设置标签分类
     *
     * @param label_type 标签分类
     */
    public void setLabel_type(String label_type) {
        this.label_type = label_type;
    }

    /**
     * 获取联系人
     *
     * @return label_person - 联系人
     */
    public String getLabel_person() {
        return label_person;
    }

    /**
     * 设置联系人
     *
     * @param label_person 联系人
     */
    public void setLabel_person(String label_person) {
        this.label_person = label_person;
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
     * 获取标签计算引擎,mysql,hive,spark,presto
     *
     * @return label_engine - 标签计算引擎,mysql,hive,spark,presto
     */
    public String getLabel_engine() {
        return label_engine;
    }

    /**
     * 设置标签计算引擎,mysql,hive,spark,presto
     *
     * @param label_engine 标签计算引擎,mysql,hive,spark,presto
     */
    public void setLabel_engine(String label_engine) {
        this.label_engine = label_engine;
    }

    /**
     * 获取标签可用参数
     *
     * @return param_json - 标签可用参数
     */
    public String getParam_json() {
        return param_json;
    }

    /**
     * 设置标签可用参数
     *
     * @param param_json 标签可用参数
     */
    public void setParam_json(String param_json) {
        this.param_json = param_json;
    }

    /**
     * 获取运算表达式,仅支持sql
     *
     * @return label_expression - 运算表达式,仅支持sql
     */
    public String getLabel_expression() {
        return label_expression;
    }

    /**
     * 设置运算表达式,仅支持sql
     *
     * @param label_expression 运算表达式,仅支持sql
     */
    public void setLabel_expression(String label_expression) {
        this.label_expression = label_expression;
    }
}