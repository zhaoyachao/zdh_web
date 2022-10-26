package com.zyc.zdh.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "we_mock_data_info")
public class WeMockDataInfo {
    @Id
    private String id;

    private String wemock_context;

    /**
     * 请求类型
     */
    private String req_type;

    /**
     * url
     */
    private String url;

    /**
     * 解析类型,0:静态，1:动态
     */
    private String resolve_type;

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
     * 产品code
     */
    private String product_code;

    /**
     * mock tree id
     */
    private String mock_tree_id;

    /**
     * 响应信息
     */
    private String header;

    /**
     * 内容
     */
    private String resp_context;

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
     * @return wemock_context
     */
    public String getWemock_context() {
        return wemock_context;
    }

    /**
     * @param wemock_context
     */
    public void setWemock_context(String wemock_context) {
        this.wemock_context = wemock_context;
    }

    /**
     * 获取请求类型
     *
     * @return req_type - 请求类型
     */
    public String getReq_type() {
        return req_type;
    }

    /**
     * 设置请求类型
     *
     * @param req_type 请求类型
     */
    public void setReq_type(String req_type) {
        this.req_type = req_type;
    }

    /**
     * 获取url
     *
     * @return url - url
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置url
     *
     * @param url url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取解析类型,0:静态，1:动态
     *
     * @return resolve_type - 解析类型,0:静态，1:动态
     */
    public String getResolve_type() {
        return resolve_type;
    }

    /**
     * 设置解析类型,0:静态，1:动态
     *
     * @param resolve_type 解析类型,0:静态，1:动态
     */
    public void setResolve_type(String resolve_type) {
        this.resolve_type = resolve_type;
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
     * 获取mock tree id
     *
     * @return mock_tree_id - mock tree id
     */
    public String getMock_tree_id() {
        return mock_tree_id;
    }

    /**
     * 设置mock tree id
     *
     * @param mock_tree_id mock tree id
     */
    public void setMock_tree_id(String mock_tree_id) {
        this.mock_tree_id = mock_tree_id;
    }

    /**
     * 获取响应信息
     *
     * @return header - 响应信息
     */
    public String getHeader() {
        return header;
    }

    /**
     * 设置响应信息
     *
     * @param header 响应信息
     */
    public void setHeader(String header) {
        this.header = header;
    }

    /**
     * 获取内容
     *
     * @return resp_context - 内容
     */
    public String getResp_context() {
        return resp_context;
    }

    /**
     * 设置内容
     *
     * @param resp_context 内容
     */
    public void setResp_context(String resp_context) {
        this.resp_context = resp_context;
    }
}