package com.zyc.zdh.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "push_task_log")
public class PushTaskLog extends BaseProductAndDimGroupAuthInfo {
    /**
     * 主键ID
     */
    @Id
    private String id;

    /**
     * 由当前服务自动生成(表示当前一次会话id)
     */
    private Long request_id;

    /**
     * 由当前服务生成,唯一的一条消息id
     */
    private Long message_id;

    /**
     * 来源，一般指平台名称
     */
    private String source;

    /**
     * 子来源，SAAS服务使用方公司的标识
     */
    private String sub_source;

    /**
     * 推送模版ID
     */
    private String template_id;

    /**
     * 推送账号
     */
    private String acc;

    /**
     * 账号类型
     */
    private String acc_type;

    /**
     * 推送标题
     */
    private String title;

    /**
     * 内容签名
     */
    private String sign_name;

    /**
     * 其他引用
     */
    private String ref;

    /**
     * 渠道
     */
    private String channel;

    /**
     * 请求时间,毫秒级时间戳
     */
    private String request_time;

    /**
     * 当前推送服务
     */
    private String push_server;

    /**
     * 消息类型
     */
    private String push_msg_type;

    /**
     * 队列名称
     */
    private String queue_name;

    /**
     * 通道池
     */
    private String channel_pool;

    /**
     * 通道可识别的用户账号
     */
    private String channel_acc;

    /**
     * 通道用户账号类型
     */
    private String channel_acc_type;

    /**
     * 设备类型, ios, huawei, android
     */
    private String device_type;

    /**
     * 设备ID
     */
    private String device_id;

    /**
     * app标识, 主要用于push推送时区分app应用
     */
    private String app;

    /**
     * 通道商
     */
    private String push_channel;

    /**
     * 通道商模版id
     */
    private String channel_template_id;

    /**
     * 完整id mapping url
     */
    private String id_mapping_url;

    /**
     * 是否html, 0:false, 1:true
     */
    private Boolean is_html;

    /**
     * 小程序应用id
     */
    private String app_id;

    /**
     * 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
     */
    private String miniprogram_state;

    /**
     * 点击模板卡片后的跳转页面，仅限本小程序内的页面
     */
    private String page;

    /**
     * 进入小程序查看的语言类型
     */
    private String lang;

    /**
     * URL
     */
    private String url;

    /**
     * 当前任务状态: 1:新建,2:发送中,3:调用成功,4:调用失败
     */
    private String status;

    /**
     * 失败码
     */
    private String error_code;

    /**
     * 失败说明
     */
    private String error_msg;

    /**
     * 第三方失败码
     */
    private String third_code;

    /**
     * 第三方失败说明
     */
    private String third_msg;

    /**
     * 送达时间
     */
    private String deliver_time;

    /**
     * 送达状态
     */
    private String deliver_status;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 更新时间
     */
    private Timestamp update_time;

    /**
     * 推送内容，支持变量
     */
    private String content;

    /**
     * 自定义参数，JSON格式
     */
    private String param;

    /**
     * 已经使用过的通道列表，JSON数组格式
     */
    private String channels;

    private String is_delete;

    private String product_code;

    private String dim_group;

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
     * 获取由当前服务自动生成(表示当前一次会话id)
     *
     * @return request_id - 由当前服务自动生成(表示当前一次会话id)
     */
    public Long getRequest_id() {
        return request_id;
    }

    /**
     * 设置由当前服务自动生成(表示当前一次会话id)
     *
     * @param request_id 由当前服务自动生成(表示当前一次会话id)
     */
    public void setRequest_id(Long request_id) {
        this.request_id = request_id;
    }

    /**
     * 获取由当前服务生成,唯一的一条消息id
     *
     * @return message_id - 由当前服务生成,唯一的一条消息id
     */
    public Long getMessage_id() {
        return message_id;
    }

    /**
     * 设置由当前服务生成,唯一的一条消息id
     *
     * @param message_id 由当前服务生成,唯一的一条消息id
     */
    public void setMessage_id(Long message_id) {
        this.message_id = message_id;
    }

    /**
     * 获取来源，一般指平台名称
     *
     * @return source - 来源，一般指平台名称
     */
    public String getSource() {
        return source;
    }

    /**
     * 设置来源，一般指平台名称
     *
     * @param source 来源，一般指平台名称
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * 获取子来源，SAAS服务使用方公司的标识
     *
     * @return sub_source - 子来源，SAAS服务使用方公司的标识
     */
    public String getSub_source() {
        return sub_source;
    }

    /**
     * 设置子来源，SAAS服务使用方公司的标识
     *
     * @param sub_source 子来源，SAAS服务使用方公司的标识
     */
    public void setSub_source(String sub_source) {
        this.sub_source = sub_source;
    }

    /**
     * 获取推送模版ID
     *
     * @return template_id - 推送模版ID
     */
    public String getTemplate_id() {
        return template_id;
    }

    /**
     * 设置推送模版ID
     *
     * @param template_id 推送模版ID
     */
    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    /**
     * 获取推送账号
     *
     * @return acc - 推送账号
     */
    public String getAcc() {
        return acc;
    }

    /**
     * 设置推送账号
     *
     * @param acc 推送账号
     */
    public void setAcc(String acc) {
        this.acc = acc;
    }

    /**
     * 获取账号类型
     *
     * @return acc_type - 账号类型
     */
    public String getAcc_type() {
        return acc_type;
    }

    /**
     * 设置账号类型
     *
     * @param acc_type 账号类型
     */
    public void setAcc_type(String acc_type) {
        this.acc_type = acc_type;
    }

    /**
     * 获取推送标题
     *
     * @return title - 推送标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置推送标题
     *
     * @param title 推送标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取内容签名
     *
     * @return sign_name - 内容签名
     */
    public String getSign_name() {
        return sign_name;
    }

    /**
     * 设置内容签名
     *
     * @param sign_name 内容签名
     */
    public void setSign_name(String sign_name) {
        this.sign_name = sign_name;
    }

    /**
     * 获取其他引用
     *
     * @return ref - 其他引用
     */
    public String getRef() {
        return ref;
    }

    /**
     * 设置其他引用
     *
     * @param ref 其他引用
     */
    public void setRef(String ref) {
        this.ref = ref;
    }

    /**
     * 获取渠道
     *
     * @return channel - 渠道
     */
    public String getChannel() {
        return channel;
    }

    /**
     * 设置渠道
     *
     * @param channel 渠道
     */
    public void setChannel(String channel) {
        this.channel = channel;
    }

    /**
     * 获取请求时间,毫秒级时间戳
     *
     * @return request_time - 请求时间,毫秒级时间戳
     */
    public String getRequest_time() {
        return request_time;
    }

    /**
     * 设置请求时间,毫秒级时间戳
     *
     * @param request_time 请求时间,毫秒级时间戳
     */
    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    /**
     * 获取当前推送服务
     *
     * @return push_server - 当前推送服务
     */
    public String getPush_server() {
        return push_server;
    }

    /**
     * 设置当前推送服务
     *
     * @param push_server 当前推送服务
     */
    public void setPush_server(String push_server) {
        this.push_server = push_server;
    }

    /**
     * 获取消息类型
     *
     * @return push_msg_type - 消息类型
     */
    public String getPush_msg_type() {
        return push_msg_type;
    }

    /**
     * 设置消息类型
     *
     * @param push_msg_type 消息类型
     */
    public void setPush_msg_type(String push_msg_type) {
        this.push_msg_type = push_msg_type;
    }

    /**
     * 获取队列名称
     *
     * @return queue_name - 队列名称
     */
    public String getQueue_name() {
        return queue_name;
    }

    /**
     * 设置队列名称
     *
     * @param queue_name 队列名称
     */
    public void setQueue_name(String queue_name) {
        this.queue_name = queue_name;
    }

    /**
     * 获取通道池
     *
     * @return channel_pool - 通道池
     */
    public String getChannel_pool() {
        return channel_pool;
    }

    /**
     * 设置通道池
     *
     * @param channel_pool 通道池
     */
    public void setChannel_pool(String channel_pool) {
        this.channel_pool = channel_pool;
    }

    /**
     * 获取通道可识别的用户账号
     *
     * @return channel_acc - 通道可识别的用户账号
     */
    public String getChannel_acc() {
        return channel_acc;
    }

    /**
     * 设置通道可识别的用户账号
     *
     * @param channel_acc 通道可识别的用户账号
     */
    public void setChannel_acc(String channel_acc) {
        this.channel_acc = channel_acc;
    }

    /**
     * 获取通道用户账号类型
     *
     * @return channel_acc_type - 通道用户账号类型
     */
    public String getChannel_acc_type() {
        return channel_acc_type;
    }

    /**
     * 设置通道用户账号类型
     *
     * @param channel_acc_type 通道用户账号类型
     */
    public void setChannel_acc_type(String channel_acc_type) {
        this.channel_acc_type = channel_acc_type;
    }

    /**
     * 获取设备类型, ios, huawei, android
     *
     * @return device_type - 设备类型, ios, huawei, android
     */
    public String getDevice_type() {
        return device_type;
    }

    /**
     * 设置设备类型, ios, huawei, android
     *
     * @param device_type 设备类型, ios, huawei, android
     */
    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }

    /**
     * 获取设备ID
     *
     * @return device_id - 设备ID
     */
    public String getDevice_id() {
        return device_id;
    }

    /**
     * 设置设备ID
     *
     * @param device_id 设备ID
     */
    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    /**
     * 获取app标识, 主要用于push推送时区分app应用
     *
     * @return app - app标识, 主要用于push推送时区分app应用
     */
    public String getApp() {
        return app;
    }

    /**
     * 设置app标识, 主要用于push推送时区分app应用
     *
     * @param app app标识, 主要用于push推送时区分app应用
     */
    public void setApp(String app) {
        this.app = app;
    }

    /**
     * 获取通道商
     *
     * @return push_channel - 通道商
     */
    public String getPush_channel() {
        return push_channel;
    }

    /**
     * 设置通道商
     *
     * @param push_channel 通道商
     */
    public void setPush_channel(String push_channel) {
        this.push_channel = push_channel;
    }

    /**
     * 获取通道商模版id
     *
     * @return channel_template_id - 通道商模版id
     */
    public String getChannel_template_id() {
        return channel_template_id;
    }

    /**
     * 设置通道商模版id
     *
     * @param channel_template_id 通道商模版id
     */
    public void setChannel_template_id(String channel_template_id) {
        this.channel_template_id = channel_template_id;
    }

    /**
     * 获取完整id mapping url
     *
     * @return id_mapping_url - 完整id mapping url
     */
    public String getId_mapping_url() {
        return id_mapping_url;
    }

    /**
     * 设置完整id mapping url
     *
     * @param id_mapping_url 完整id mapping url
     */
    public void setId_mapping_url(String id_mapping_url) {
        this.id_mapping_url = id_mapping_url;
    }

    /**
     * 获取是否html, 0:false, 1:true
     *
     * @return is_html - 是否html, 0:false, 1:true
     */
    public Boolean getIs_html() {
        return is_html;
    }

    /**
     * 设置是否html, 0:false, 1:true
     *
     * @param is_html 是否html, 0:false, 1:true
     */
    public void setIs_html(Boolean is_html) {
        this.is_html = is_html;
    }

    /**
     * 获取小程序应用id
     *
     * @return app_id - 小程序应用id
     */
    public String getApp_id() {
        return app_id;
    }

    /**
     * 设置小程序应用id
     *
     * @param app_id 小程序应用id
     */
    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    /**
     * 获取跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
     *
     * @return miniprogram_state - 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
     */
    public String getMiniprogram_state() {
        return miniprogram_state;
    }

    /**
     * 设置跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
     *
     * @param miniprogram_state 跳转小程序类型：developer为开发版；trial为体验版；formal为正式版；默认为正式版
     */
    public void setMiniprogram_state(String miniprogram_state) {
        this.miniprogram_state = miniprogram_state;
    }

    /**
     * 获取点击模板卡片后的跳转页面，仅限本小程序内的页面
     *
     * @return page - 点击模板卡片后的跳转页面，仅限本小程序内的页面
     */
    public String getPage() {
        return page;
    }

    /**
     * 设置点击模板卡片后的跳转页面，仅限本小程序内的页面
     *
     * @param page 点击模板卡片后的跳转页面，仅限本小程序内的页面
     */
    public void setPage(String page) {
        this.page = page;
    }

    /**
     * 获取进入小程序查看的语言类型
     *
     * @return lang - 进入小程序查看的语言类型
     */
    public String getLang() {
        return lang;
    }

    /**
     * 设置进入小程序查看的语言类型
     *
     * @param lang 进入小程序查看的语言类型
     */
    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * 获取URL
     *
     * @return url - URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置URL
     *
     * @param url URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取当前任务状态: 1:新建,2:发送中,3:调用成功,4:调用失败
     *
     * @return status - 当前任务状态: 1:新建,2:发送中,3:调用成功,4:调用失败
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置当前任务状态: 1:新建,2:发送中,3:调用成功,4:调用失败
     *
     * @param status 当前任务状态: 1:新建,2:发送中,3:调用成功,4:调用失败
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取失败码
     *
     * @return error_code - 失败码
     */
    public String getError_code() {
        return error_code;
    }

    /**
     * 设置失败码
     *
     * @param error_code 失败码
     */
    public void setError_code(String error_code) {
        this.error_code = error_code;
    }

    /**
     * 获取失败说明
     *
     * @return error_msg - 失败说明
     */
    public String getError_msg() {
        return error_msg;
    }

    /**
     * 设置失败说明
     *
     * @param error_msg 失败说明
     */
    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }

    /**
     * 获取第三方失败码
     *
     * @return third_code - 第三方失败码
     */
    public String getThird_code() {
        return third_code;
    }

    /**
     * 设置第三方失败码
     *
     * @param third_code 第三方失败码
     */
    public void setThird_code(String third_code) {
        this.third_code = third_code;
    }

    /**
     * 获取第三方失败说明
     *
     * @return third_msg - 第三方失败说明
     */
    public String getThird_msg() {
        return third_msg;
    }

    /**
     * 设置第三方失败说明
     *
     * @param third_msg 第三方失败说明
     */
    public void setThird_msg(String third_msg) {
        this.third_msg = third_msg;
    }

    /**
     * 获取送达时间
     *
     * @return deliver_time - 送达时间
     */
    public String getDeliver_time() {
        return deliver_time;
    }

    /**
     * 设置送达时间
     *
     * @param deliver_time 送达时间
     */
    public void setDeliver_time(String deliver_time) {
        this.deliver_time = deliver_time;
    }

    /**
     * 获取送达状态
     *
     * @return deliver_status - 送达状态
     */
    public String getDeliver_status() {
        return deliver_status;
    }

    /**
     * 设置送达状态
     *
     * @param deliver_status 送达状态
     */
    public void setDeliver_status(String deliver_status) {
        this.deliver_status = deliver_status;
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
     * 获取推送内容，支持变量
     *
     * @return content - 推送内容，支持变量
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置推送内容，支持变量
     *
     * @param content 推送内容，支持变量
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取自定义参数，JSON格式
     *
     * @return param - 自定义参数，JSON格式
     */
    public String getParam() {
        return param;
    }

    /**
     * 设置自定义参数，JSON格式
     *
     * @param param 自定义参数，JSON格式
     */
    public void setParam(String param) {
        this.param = param;
    }

    /**
     * 获取已经使用过的通道列表，JSON数组格式
     *
     * @return channels - 已经使用过的通道列表，JSON数组格式
     */
    public String getChannels() {
        return channels;
    }

    /**
     * 设置已经使用过的通道列表，JSON数组格式
     *
     * @param channels 已经使用过的通道列表，JSON数组格式
     */
    public void setChannels(String channels) {
        this.channels = channels;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    @Override
    public String getProduct_code() {
        return product_code;
    }

    @Override
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    @Override
    public String getDim_group() {
        return dim_group;
    }

    @Override
    public void setDim_group(String dim_group) {
        this.dim_group = dim_group;
    }
}