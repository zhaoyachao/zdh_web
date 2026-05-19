package com.zyc.zdh.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "wechat_qrcode_info")
public class WechatQrcodeInfo extends BaseProductAuthInfo{
    @Id
    private String id;

    /**
     * 二维码code
     */
    private String qrcode;

    /**
     * 二维码名称
     */
    private String qrcode_name;

    /**
     * 有效时间（秒），最大2592000
     */
    private String expire_seconds;

    /**
     * 二维码类型：QR_SCENE(临时整型)/QR_STR_SCENE(临时字符串)/QR_LIMIT_SCENE(永久整型)/QR_LIMIT_STR_SCENE(永久字符串)
     */
    private String action_name;

    /**
     * 公众号
     */
    private String wechat_app;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 1:新建,2:成功,3:失败,4:过期
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
     * 是否删除,0:未删除,1:删除
     */
    private String is_delete;

    /**
     * 产品code
     */
    private String product_code;

    /**
     * 二维码详细信息
     */
    private String action_info;

    /**
     * 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
     */
    private String ticket;

    /**
     * 二维码base64
     */
    private String qrcode_image;

    /**
     * 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     */
    private String url;

    private String qrcode_custom_image;

    private String appid;

    private String path;

    private String open_version;

    private String debug_url;

    private String permit_sub_rule;

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
     * 获取二维码code
     *
     * @return qrcode - 二维码code
     */
    public String getQrcode() {
        return qrcode;
    }

    /**
     * 设置二维码code
     *
     * @param qrcode 二维码code
     */
    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    /**
     * 获取二维码名称
     *
     * @return qrcode_name - 二维码名称
     */
    public String getQrcode_name() {
        return qrcode_name;
    }

    /**
     * 设置二维码名称
     *
     * @param qrcode_name 二维码名称
     */
    public void setQrcode_name(String qrcode_name) {
        this.qrcode_name = qrcode_name;
    }

    /**
     * 获取有效时间（秒），最大2592000
     *
     * @return expire_seconds - 有效时间（秒），最大2592000
     */
    public String getExpire_seconds() {
        return expire_seconds;
    }

    /**
     * 设置有效时间（秒），最大2592000
     *
     * @param expire_seconds 有效时间（秒），最大2592000
     */
    public void setExpire_seconds(String expire_seconds) {
        this.expire_seconds = expire_seconds;
    }

    /**
     * 获取二维码类型：QR_SCENE(临时整型)/QR_STR_SCENE(临时字符串)/QR_LIMIT_SCENE(永久整型)/QR_LIMIT_STR_SCENE(永久字符串)
     *
     * @return action_name - 二维码类型：QR_SCENE(临时整型)/QR_STR_SCENE(临时字符串)/QR_LIMIT_SCENE(永久整型)/QR_LIMIT_STR_SCENE(永久字符串)
     */
    public String getAction_name() {
        return action_name;
    }

    /**
     * 设置二维码类型：QR_SCENE(临时整型)/QR_STR_SCENE(临时字符串)/QR_LIMIT_SCENE(永久整型)/QR_LIMIT_STR_SCENE(永久字符串)
     *
     * @param action_name 二维码类型：QR_SCENE(临时整型)/QR_STR_SCENE(临时字符串)/QR_LIMIT_SCENE(永久整型)/QR_LIMIT_STR_SCENE(永久字符串)
     */
    public void setAction_name(String action_name) {
        this.action_name = action_name;
    }

    /**
     * 获取公众号
     *
     * @return wechat_app - 公众号
     */
    public String getWechat_app() {
        return wechat_app;
    }

    /**
     * 设置公众号
     *
     * @param wechat_app 公众号
     */
    public void setWechat_app(String wechat_app) {
        this.wechat_app = wechat_app;
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
     * 获取1:新建,2:成功,3:失败,4:过期
     *
     * @return status - 1:新建,2:成功,3:失败,4:过期
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置1:新建,2:成功,3:失败,4:过期
     *
     * @param status 1:新建,2:成功,3:失败,4:过期
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
     * 获取二维码详细信息
     *
     * @return action_info - 二维码详细信息
     */
    public String getAction_info() {
        return action_info;
    }

    /**
     * 设置二维码详细信息
     *
     * @param action_info 二维码详细信息
     */
    public void setAction_info(String action_info) {
        this.action_info = action_info;
    }

    /**
     * 获取获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
     *
     * @return ticket - 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
     */
    public String getTicket() {
        return ticket;
    }

    /**
     * 设置获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
     *
     * @param ticket 获取的二维码ticket，凭借此ticket可以在有效时间内换取二维码。
     */
    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getQrcode_image() {
        return qrcode_image;
    }

    public void setQrcode_image(String qrcode_image) {
        this.qrcode_image = qrcode_image;
    }
    
    /**
     * 获取二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     *
     * @return url - 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     *
     * @param url 二维码图片解析后的地址，开发者可根据该地址自行生成需要的二维码图片
     */
    public void setUrl(String url) {
        this.url = url;
    }

    public String getQrcode_custom_image() {
        return qrcode_custom_image;
    }

    public void setQrcode_custom_image(String qrcode_custom_image) {
        this.qrcode_custom_image = qrcode_custom_image;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getOpen_version() {
        return open_version;
    }

    public void setOpen_version(String open_version) {
        this.open_version = open_version;
    }

    public String getDebug_url() {
        return debug_url;
    }

    public void setDebug_url(String debug_url) {
        this.debug_url = debug_url;
    }

    public String getPermit_sub_rule() {
        return permit_sub_rule;
    }

    public void setPermit_sub_rule(String permit_sub_rule) {
        this.permit_sub_rule = permit_sub_rule;
    }
}