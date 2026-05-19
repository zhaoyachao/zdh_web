package com.zyc.zdh.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "alarm_sms_info")
public class AlarmSmsInfo {
    @Id
    private String id;

    /**
     * 任务说明
     */
    private String title;

    /**
     * 信息类型，通知,营销
     */
    private String msg_type;

    /**
     * 短信附带连接
     */
    private String msg_url;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 状态,0:未处理,1:处理中,2:失败,3:成功,4:不处理
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
     * 信息
     */
    private String msg;

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
     * 获取任务说明
     *
     * @return title - 任务说明
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置任务说明
     *
     * @param title 任务说明
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取信息类型，通知,营销
     *
     * @return msg_type - 信息类型，通知,营销
     */
    public String getMsg_type() {
        return msg_type;
    }

    /**
     * 设置信息类型，通知,营销
     *
     * @param msg_type 信息类型，通知,营销
     */
    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    /**
     * 获取短信附带连接
     *
     * @return msg_url - 短信附带连接
     */
    public String getMsg_url() {
        return msg_url;
    }

    /**
     * 设置短信附带连接
     *
     * @param msg_url 短信附带连接
     */
    public void setMsg_url(String msg_url) {
        this.msg_url = msg_url;
    }

    /**
     * 获取手机号
     *
     * @return phone - 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号
     *
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取状态,0:未处理,1:处理中,2:失败,3:成功,4:不处理
     *
     * @return status - 状态,0:未处理,1:处理中,2:失败,3:成功,4:不处理
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置状态,0:未处理,1:处理中,2:失败,3:成功,4:不处理
     *
     * @param status 状态,0:未处理,1:处理中,2:失败,3:成功,4:不处理
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
     * 获取信息
     *
     * @return msg - 信息
     */
    public String getMsg() {
        return msg;
    }

    /**
     * 设置信息
     *
     * @param msg 信息
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
}