package com.zyc.zdh.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "beacon_fire_info")
public class BeaconFireInfo extends BaseProductAndDimGroupAuthInfo{
    @Id
    private String id;

    /**
     * 输入数据源id
     */
    private String beacon_fire_context;

    /**
     * 输入数据源id
     */
    private String data_sources_choose_input;

    /**
     * 输入数据源类型
     */
    private String data_source_type_input;

    /**
     * cron表达式/自定义表达式
     */
    private String expr;

    public String getTime_diff() {
        return time_diff;
    }

    public void setTime_diff(String time_diff) {
        this.time_diff = time_diff;
    }

    private String time_diff;

    /**
     * 告警级别,p0,p1,p2,p3,数字大小和危险程度成反比
     */
    private String alarm_level;

    /**
     * 告警组
     */
    private String alarm_group;

    /**
     * 产品code
     */
    private String product_code;

    /**
     * 用户组
     */
    private String dim_group;

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
     * sql表达式
     */
    private String sql_script;

    /**
     * 结果处理脚本
     */
    private String groovy_script;

    private String status;

    private String frequency_config;
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
     * 获取输入数据源id
     *
     * @return beacon_fire_context - 输入数据源id
     */
    public String getBeacon_fire_context() {
        return beacon_fire_context;
    }

    /**
     * 设置输入数据源id
     *
     * @param beacon_fire_context 输入数据源id
     */
    public void setBeacon_fire_context(String beacon_fire_context) {
        this.beacon_fire_context = beacon_fire_context;
    }

    /**
     * 获取输入数据源id
     *
     * @return data_sources_choose_input - 输入数据源id
     */
    public String getData_sources_choose_input() {
        return data_sources_choose_input;
    }

    /**
     * 设置输入数据源id
     *
     * @param data_sources_choose_input 输入数据源id
     */
    public void setData_sources_choose_input(String data_sources_choose_input) {
        this.data_sources_choose_input = data_sources_choose_input;
    }

    /**
     * 获取输入数据源类型
     *
     * @return data_source_type_input - 输入数据源类型
     */
    public String getData_source_type_input() {
        return data_source_type_input;
    }

    /**
     * 设置输入数据源类型
     *
     * @param data_source_type_input 输入数据源类型
     */
    public void setData_source_type_input(String data_source_type_input) {
        this.data_source_type_input = data_source_type_input;
    }

    /**
     * 获取cron表达式/自定义表达式
     *
     * @return expr - cron表达式/自定义表达式
     */
    public String getExpr() {
        return expr;
    }

    /**
     * 设置cron表达式/自定义表达式
     *
     * @param expr cron表达式/自定义表达式
     */
    public void setExpr(String expr) {
        this.expr = expr;
    }

    /**
     * 获取告警级别,p0,p1,p2,p3,数字大小和危险程度成反比
     *
     * @return alarm_level - 告警级别,p0,p1,p2,p3,数字大小和危险程度成反比
     */
    public String getAlarm_level() {
        return alarm_level;
    }

    /**
     * 设置告警级别,p0,p1,p2,p3,数字大小和危险程度成反比
     *
     * @param alarm_level 告警级别,p0,p1,p2,p3,数字大小和危险程度成反比
     */
    public void setAlarm_level(String alarm_level) {
        this.alarm_level = alarm_level;
    }

    /**
     * 获取告警组
     *
     * @return alarm_group - 告警组
     */
    public String getAlarm_group() {
        return alarm_group;
    }

    /**
     * 设置告警组
     *
     * @param alarm_group 告警组
     */
    public void setAlarm_group(String alarm_group) {
        this.alarm_group = alarm_group;
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
     * 获取用户组
     *
     * @return dim_group - 用户组
     */
    public String getDim_group() {
        return dim_group;
    }

    /**
     * 设置用户组
     *
     * @param dim_group 用户组
     */
    public void setDim_group(String dim_group) {
        this.dim_group = dim_group;
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
     * 获取sql表达式
     *
     * @return sql_script - sql表达式
     */
    public String getSql_script() {
        return sql_script;
    }

    /**
     * 设置sql表达式
     *
     * @param sql_script sql表达式
     */
    public void setSql_script(String sql_script) {
        this.sql_script = sql_script;
    }

    /**
     * 获取结果处理脚本
     *
     * @return groovy_script - 结果处理脚本
     */
    public String getGroovy_script() {
        return groovy_script;
    }

    /**
     * 设置结果处理脚本
     *
     * @param groovy_script 结果处理脚本
     */
    public void setGroovy_script(String groovy_script) {
        this.groovy_script = groovy_script;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrequency_config() {
        return frequency_config;
    }

    public void setFrequency_config(String frequency_config) {
        this.frequency_config = frequency_config;
    }
}