package com.zyc.zdh.entity;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.persistence.*;

@Table(name = "push_template_info")
public class PushTemplateInfo {
    @Id
    private String id;

    /**
     * 模板名称
     */
    private String template_name;

    /**
     * 模板Id
     */
    private String template_id;

    /**
     * 消息类型,1:营销,2:通知,3:验证码,4:告警,5:其他
     */
    private String push_type;

    /**
     * 推送服务,sms,email
     */
    private String push_server;

    /**
     * 状态,0:编辑,1:启用,2:审批中,3:审批失败,4:禁用
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
     * 更新说明
     */
    private String update_context;

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
     * 推送配置,json结构
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
     * 获取模板名称
     *
     * @return template_name - 模板名称
     */
    public String getTemplate_name() {
        return template_name;
    }

    /**
     * 设置模板名称
     *
     * @param template_name 模板名称
     */
    public void setTemplate_name(String template_name) {
        this.template_name = template_name;
    }

    /**
     * 获取模板Id
     *
     * @return template_id - 模板Id
     */
    public String getTemplate_id() {
        return template_id;
    }

    /**
     * 设置模板Id
     *
     * @param template_id 模板Id
     */
    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    /**
     * 获取消息类型,1:营销,2:通知,3:验证码,4:告警,5:其他
     *
     * @return push_type - 消息类型,1:营销,2:通知,3:验证码,4:告警,5:其他
     */
    public String getPush_type() {
        return push_type;
    }

    /**
     * 设置消息类型,1:营销,2:通知,3:验证码,4:告警,5:其他
     *
     * @param push_type 消息类型,1:营销,2:通知,3:验证码,4:告警,5:其他
     */
    public void setPush_type(String push_type) {
        this.push_type = push_type;
    }

    /**
     * 获取推送服务,sms,email
     *
     * @return push_server - 推送服务,sms,email
     */
    public String getPush_server() {
        return push_server;
    }

    /**
     * 设置推送服务,sms,email
     *
     * @param push_server 推送服务,sms,email
     */
    public void setPush_server(String push_server) {
        this.push_server = push_server;
    }

    /**
     * 获取状态,0:编辑,1:启用,2:审批中,3:审批失败,4:禁用
     *
     * @return status - 状态,0:编辑,1:启用,2:审批中,3:审批失败,4:禁用
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态,0:编辑,1:启用,2:审批中,3:审批失败,4:禁用
     *
     * @param status 状态,0:编辑,1:启用,2:审批中,3:审批失败,4:禁用
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
     * 获取更新说明
     *
     * @return update_context - 更新说明
     */
    public String getUpdate_context() {
        return update_context;
    }

    /**
     * 设置更新说明
     *
     * @param update_context 更新说明
     */
    public void setUpdate_context(String update_context) {
        this.update_context = update_context;
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
     * 获取推送配置,json结构
     *
     * @return config - 推送配置,json结构
     */
    public String getConfig() {
        return config;
    }

    /**
     * 设置推送配置,json结构
     *
     * @param config 推送配置,json结构
     */
    public void setConfig(String config) {
        this.config = config;
    }

    public Map<String, Object> getConfigMap(){
        if(StringUtils.isEmpty(config)){
            return new HashMap<String, Object>();
        }
        return JSON.parseObject(config, Map.class);
    }
}