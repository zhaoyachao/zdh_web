package com.zyc.zdh.entity;

import com.zyc.zdh.util.JsonUtil;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "wechat_rule_info")
public class WechatRuleInfo extends BaseProductAuthInfo{
    /**
     * 主键ID
     */
    @Id
    private String id;

    /**
     * 服务号
     */
    private String wechat_channel;

    /**
     * 规则名称
     */
    private String rule_name;

    /**
     * 事件类型
     */
    private String event;

    /**
     * 事件key
     */
    private String event_key;

    /**
     * 权重
     */
    private String weight;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 是否删除,0:未删除,1:删除
     */
    private String is_delete;

    /**
     * 状态:1-启用,2-禁用
     */
    private String status;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 更新时间
     */
    private Timestamp update_time;

    /**
     * 回复规则
     */
    private String rule_config;

    /**
     * 产品code
     */
    private String product_code;

    @Transient
    private RuleConfig ruleConfig;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取服务号
     *
     * @return wechat_channel - 服务号
     */
    public String getWechat_channel() {
        return wechat_channel;
    }

    /**
     * 设置服务号
     *
     * @param wechat_channel 服务号
     */
    public void setWechat_channel(String wechat_channel) {
        this.wechat_channel = wechat_channel;
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
     * 获取事件类型
     *
     * @return event - 事件类型
     */
    public String getEvent() {
        return event;
    }

    /**
     * 设置事件类型
     *
     * @param event 事件类型
     */
    public void setEvent(String event) {
        this.event = event;
    }

    /**
     * 获取事件key
     *
     * @return event_key - 事件key
     */
    public String getEvent_key() {
        return event_key;
    }

    /**
     * 设置事件key
     *
     * @param event_key 事件key
     */
    public void setEvent_key(String event_key) {
        this.event_key = event_key;
    }

    /**
     * 获取权重
     *
     * @return weight - 权重
     */
    public String getWeight() {
        return weight;
    }

    /**
     * 设置权重
     *
     * @param weight 权重
     */
    public void setWeight(String weight) {
        this.weight = weight;
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
     * 获取状态:1-启用,2-禁用
     *
     * @return status - 状态:1-启用,2-禁用
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态:1-启用,2-禁用
     *
     * @param status 状态:1-启用,2-禁用
     */
    public void setStatus(String status) {
        this.status = status;
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
     * 获取回复规则
     *
     * @return rule_config - 回复规则
     */
    public String getRule_config() {
        return rule_config;
    }

    /**
     * 设置回复规则
     *
     * @param rule_config 回复规则
     */
    public void setRule_config(String rule_config) {
        this.rule_config = rule_config;
    }

    public String getProduct_code() {
        return product_code;
    }
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public RuleConfig getRuleConfig() {
        return JsonUtil.toJavaBean(rule_config, RuleConfig.class);
    }

    public static class RuleConfig{
        private String msg_type;
        private String content;
        private String title;
        private String description;
        private String musicurl;
        private String hqmusicurl;
        private String thumbmediaid;
        private String picurl;
        private String url;
        public String getMsg_type() {
            return msg_type;
        }
        public void setMsg_type(String msg_type) {
            this.msg_type = msg_type;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getMusicurl() {
            return musicurl;
        }

        public void setMusicurl(String musicurl) {
            this.musicurl = musicurl;
        }

        public String getHqmusicurl() {
            return hqmusicurl;
        }

        public void setHqmusicurl(String hqmusicurl) {
            this.hqmusicurl = hqmusicurl;
        }

        public String getThumbmediaid() {
            return thumbmediaid;
        }

        public void setThumbmediaid(String thumbmediaid) {
            this.thumbmediaid = thumbmediaid;
        }

        public String getPicurl() {
            return picurl;
        }

        public void setPicurl(String picurl) {
            this.picurl = picurl;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}