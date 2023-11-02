package com.zyc.zdh.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "touch_config_info")
public class TouchConfigInfo {
    @Id
    private String id;

    private String touch_context;

    /**
     * 触达任务类型
     */
    private String touch_task;

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
     * 签名
     */
    private String sign;

    /**
     * 模板code
     */
    private String template_code;

    /**
     * 短信平台
     */
    private String platform;

    /**
     * 配置json格式
     */
    private String touch_config;

    /**
     * 触达参数
     */
    private String touch_param_codes;

    /**
     * 归属组
     */
    private String dim_group;

    /**
     * 归属产品
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
     * @return touch_context
     */
    public String getTouch_context() {
        return touch_context;
    }

    /**
     * @param touch_context
     */
    public void setTouch_context(String touch_context) {
        this.touch_context = touch_context;
    }

    /**
     * 获取触达任务类型
     *
     * @return touch_task - 触达任务类型
     */
    public String getTouch_task() {
        return touch_task;
    }

    /**
     * 设置触达任务类型
     *
     * @param touch_task 触达任务类型
     */
    public void setTouch_task(String touch_task) {
        this.touch_task = touch_task;
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
     * 获取签名
     *
     * @return sign - 签名
     */
    public String getSign() {
        return sign;
    }

    /**
     * 设置签名
     *
     * @param sign 签名
     */
    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * 获取模板code
     *
     * @return template_code - 模板code
     */
    public String getTemplate_code() {
        return template_code;
    }

    /**
     * 设置模板code
     *
     * @param template_code 模板code
     */
    public void setTemplate_code(String template_code) {
        this.template_code = template_code;
    }

    /**
     * 获取短信平台
     *
     * @return platform - 短信平台
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * 设置短信平台
     *
     * @param platform 短信平台
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * 获取配置json格式
     *
     * @return touch_config - 配置json格式
     */
    public String getTouch_config() {
        return touch_config;
    }

    /**
     * 设置配置json格式
     *
     * @param touch_config 配置json格式
     */
    public void setTouch_config(String touch_config) {
        this.touch_config = touch_config;
    }

    public String getTouch_param_codes() {
        return touch_param_codes;
    }

    public void setTouch_param_codes(String touch_param_codes) {
        this.touch_param_codes = touch_param_codes;
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