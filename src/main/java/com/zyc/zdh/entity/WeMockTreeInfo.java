package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "we_mock_tree_info")
public class WeMockTreeInfo extends BaseProductAuthInfo{
    @Id
    private String id;

    private String parent;

    private String text;

    /**
     * 层级
     */
    private String level;

    /**
     * 拥有者
     */
    private String owner;

    private String icon;

    /**
     * 资源说明
     */
    private String resource_desc;

    @Column(name = "`order`")
    private String order;

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
     * 1:目录,2:菜单
     */
    private String resource_type;

    /**
     * 提示语
     */
    private String notice_title;

    /**
     * 绑定事件
     */
    private String event_code;

    /**
     * 产品code
     */
    private String product_code;

    /**
     * url链接
     */
    private String url;

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
     * @return parent
     */
    public String getParent() {
        return parent;
    }

    /**
     * @param parent
     */
    public void setParent(String parent) {
        this.parent = parent;
    }

    /**
     * @return text
     */
    public String getText() {
        return text;
    }

    /**
     * @param text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * 获取层级
     *
     * @return level - 层级
     */
    public String getLevel() {
        return level;
    }

    /**
     * 设置层级
     *
     * @param level 层级
     */
    public void setLevel(String level) {
        this.level = level;
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
     * @return icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * @param icon
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * 获取资源说明
     *
     * @return resource_desc - 资源说明
     */
    public String getResource_desc() {
        return resource_desc;
    }

    /**
     * 设置资源说明
     *
     * @param resource_desc 资源说明
     */
    public void setResource_desc(String resource_desc) {
        this.resource_desc = resource_desc;
    }

    public int getOrderN() {
        if(order==null || order.trim().equals("")){
            return 1;
        }
        return new Integer(order);
    }

    /**
     * @return order
     */
    public String getOrder() {
        return order;
    }

    /**
     * @param order
     */
    public void setOrder(String order) {
        this.order = order;
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
     * 获取1:目录,2:菜单
     *
     * @return resource_type - 1:目录,2:菜单
     */
    public String getResource_type() {
        return resource_type;
    }

    /**
     * 设置1:目录,2:菜单
     *
     * @param resource_type 1:目录,2:菜单
     */
    public void setResource_type(String resource_type) {
        this.resource_type = resource_type;
    }

    /**
     * 获取提示语
     *
     * @return notice_title - 提示语
     */
    public String getNotice_title() {
        return notice_title;
    }

    /**
     * 设置提示语
     *
     * @param notice_title 提示语
     */
    public void setNotice_title(String notice_title) {
        this.notice_title = notice_title;
    }

    /**
     * 获取绑定事件
     *
     * @return event_code - 绑定事件
     */
    public String getEvent_code() {
        return event_code;
    }

    /**
     * 设置绑定事件
     *
     * @param event_code 绑定事件
     */
    public void setEvent_code(String event_code) {
        this.event_code = event_code;
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
     * 获取url链接
     *
     * @return url - url链接
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置url链接
     *
     * @param url url链接
     */
    public void setUrl(String url) {
        this.url = url;
    }
}