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
     * 数据源
     */
    private String data_sources_choose_input;

    /**
     * 标签可用参数
     */
    private String param_json;

    /**
     * 运算表达式,仅支持sql
     */
    private String label_expression;


    /**
     * 使用方式,batch:值查人,single:人查值
     */
    private String label_use_type;

    /**
     * 数据时效性, day:天级,hour:小时级,second:准实时
     */
    private String label_data_time_effect;

    /**
     * 数据更新类型,overwrite:覆盖,append:追加,get_append:值追加
     */
    private String label_data_update_type;

    /**
     * 事件事件字段,默认为空
     */
    private String label_event_time_column;

    /**
     * 数据标识,准实时时使用
     * 使用场景：从kafka来的数据根据此标识找到可以加工的标签都有哪些
     */
    private String label_data_code;

    /**
     * 标签更新时间,配合标签更新时效,用于调度发现标签生成任务
     */
    private String label_data_update_time;

    /**
     * 1:新建,2:启用,3:禁用
     */
    private String status;

    /**
     * 标签默认值,json格式
     */
    private String label_default;

    private String product_code;

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

    public String getData_sources_choose_input() {
        return data_sources_choose_input;
    }

    public void setData_sources_choose_input(String data_sources_choose_input) {
        this.data_sources_choose_input = data_sources_choose_input;
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

    public String getLabel_use_type() {
        return label_use_type;
    }

    public void setLabel_use_type(String label_use_type) {
        this.label_use_type = label_use_type;
    }

    public String getLabel_data_time_effect() {
        return label_data_time_effect;
    }

    public void setLabel_data_time_effect(String label_data_time_effect) {
        this.label_data_time_effect = label_data_time_effect;
    }

    public String getLabel_data_update_type() {
        return label_data_update_type;
    }

    public void setLabel_data_update_type(String label_data_update_type) {
        this.label_data_update_type = label_data_update_type;
    }

    public String getLabel_event_time_column() {
        return label_event_time_column;
    }

    public void setLabel_event_time_column(String label_event_time_column) {
        this.label_event_time_column = label_event_time_column;
    }

    public String getLabel_data_code() {
        return label_data_code;
    }

    public void setLabel_data_code(String label_data_code) {
        this.label_data_code = label_data_code;
    }

    public String getLabel_data_update_time() {
        return label_data_update_time;
    }

    public void setLabel_data_update_time(String label_data_update_time) {
        this.label_data_update_time = label_data_update_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLabel_default() {
        return label_default;
    }

    public void setLabel_default(String label_default) {
        this.label_default = label_default;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}