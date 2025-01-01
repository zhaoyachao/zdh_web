package com.zyc.zdh.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "quality_rule_info")
public class QualityRuleInfo {
    @Id
    private String id;

    /**
     * 规则code
     */
    private String rule_code;

    /**
     * 规则名称
     */
    private String rule_name;

    /**
     * 规则类型,1:sql表达式,2:正则
     */
    private String rule_type;

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
     * 规则内容
     */
    private String rule_expr;

    /**
     * 产品code
     */
    private String product_code;

    /**
     * 归属组
     */
    private String dim_group;

    /**
     * 逻辑删除
     */
    private String is_delete;

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
     * 获取规则code
     *
     * @return rule_code - 规则code
     */
    public String getRule_code() {
        return rule_code;
    }

    /**
     * 设置规则code
     *
     * @param rule_code 规则code
     */
    public void setRule_code(String rule_code) {
        this.rule_code = rule_code;
    }

    /**
     * 获取规则名称
     *
     * @return rule_name - 规则名称
     */
    public String getRule_name() {
        return rule_name;
    }

    /**
     * 设置规则名称
     *
     * @param rule_name 规则名称
     */
    public void setRule_name(String rule_name) {
        this.rule_name = rule_name;
    }

    /**
     * 获取规则类型,1:sql表达式,2:正则
     *
     * @return rule_type - 规则类型,1:sql表达式,2:正则
     */
    public String getRule_type() {
        return rule_type;
    }

    /**
     * 设置规则类型,1:sql表达式,2:正则
     *
     * @param rule_type 规则类型,1:sql表达式,2:正则
     */
    public void setRule_type(String rule_type) {
        this.rule_type = rule_type;
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
     * 获取规则内容
     *
     * @return rule_expr - 规则内容
     */
    public String getRule_expr() {
        return rule_expr;
    }

    /**
     * 设置规则内容
     *
     * @param rule_expr 规则内容
     */
    public void setRule_expr(String rule_expr) {
        this.rule_expr = rule_expr;
    }

    public String getProduct_code() {
        return product_code;
    }

    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public String getDim_group() {
        return dim_group;
    }

    public void setDim_group(String dim_group) {
        this.dim_group = dim_group;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }
}