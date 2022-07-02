package com.zyc.zdh.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "crowd_rule_info")
public class CrowdRuleInfo {
    @Id
    private String id;

    /**
     * 人群规则说明
     */
    private String rule_context;

    /**
     * 人群规则类别
     */
    private String rule_type;

    /**
     * 创建人
     */
    private String create_person;

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
     * 规则表达式
     */
    private String rule_expression;

    /**
     * 中文规则表达式
     */
    private String rule_expression_cn;

    /**
     * 规则信息
     */
    private String rule_json;

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
     * 获取人群规则说明
     *
     * @return rule_context - 人群规则说明
     */
    public String getRule_context() {
        return rule_context;
    }

    /**
     * 设置人群规则说明
     *
     * @param rule_context 人群规则说明
     */
    public void setRule_context(String rule_context) {
        this.rule_context = rule_context;
    }

    /**
     * 获取人群规则类别
     *
     * @return rule_type - 人群规则类别
     */
    public String getRule_type() {
        return rule_type;
    }

    /**
     * 设置人群规则类别
     *
     * @param rule_type 人群规则类别
     */
    public void setRule_type(String rule_type) {
        this.rule_type = rule_type;
    }

    /**
     * 获取创建人
     *
     * @return create_person - 创建人
     */
    public String getCreate_person() {
        return create_person;
    }

    /**
     * 设置创建人
     *
     * @param create_person 创建人
     */
    public void setCreate_person(String create_person) {
        this.create_person = create_person;
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
     * 获取规则表达式
     *
     * @return rule_expression - 规则表达式
     */
    public String getRule_expression() {
        return rule_expression;
    }

    /**
     * 设置规则表达式
     *
     * @param rule_expression 规则表达式
     */
    public void setRule_expression(String rule_expression) {
        this.rule_expression = rule_expression;
    }

    /**
     * 获取中文规则表达式
     *
     * @return rule_expression_cn - 中文规则表达式
     */
    public String getRule_expression_cn() {
        return rule_expression_cn;
    }

    /**
     * 设置中文规则表达式
     *
     * @param rule_expression_cn 中文规则表达式
     */
    public void setRule_expression_cn(String rule_expression_cn) {
        this.rule_expression_cn = rule_expression_cn;
    }

    /**
     * 获取规则信息
     *
     * @return rule_json - 规则信息
     */
    public String getRule_json() {
        return rule_json;
    }

    /**
     * 设置规则信息
     *
     * @param rule_json 规则信息
     */
    public void setRule_json(String rule_json) {
        this.rule_json = rule_json;
    }
}