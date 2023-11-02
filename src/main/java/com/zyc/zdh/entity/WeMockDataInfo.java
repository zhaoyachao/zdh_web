package com.zyc.zdh.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

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
     * 请求编码
     */
    private String req_encode;

    /**
     * 超时时间
     */
    private String req_timeout;

    /**
     * 响应content_type
     */
    private String resp_content_type;

    /**
     * 响应编码
     */
    private String resp_encode;

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
     * 是否禁用on,off
     */
    private String is_disenable;

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
     * 获取请求编码
     *
     * @return req_encode - 请求编码
     */
    public String getReq_encode() {
        return req_encode;
    }

    /**
     * 设置请求编码
     *
     * @param req_encode 请求编码
     */
    public void setReq_encode(String req_encode) {
        this.req_encode = req_encode;
    }

    /**
     * 获取超时时间
     *
     * @return req_timeout - 超时时间
     */
    public String getReq_timeout() {
        return req_timeout;
    }

    /**
     * 设置超时时间
     *
     * @param req_timeout 超时时间
     */
    public void setReq_timeout(String req_timeout) {
        this.req_timeout = req_timeout;
    }

    /**
     * 获取响应content_type
     *
     * @return resp_content_type - 响应content_type
     */
    public String getResp_content_type() {
        return resp_content_type;
    }

    /**
     * 设置响应content_type
     *
     * @param resp_content_type 响应content_type
     */
    public void setResp_content_type(String resp_content_type) {
        this.resp_content_type = resp_content_type;
    }

    /**
     * 获取响应编码
     *
     * @return resp_encode - 响应编码
     */
    public String getResp_encode() {
        return resp_encode;
    }

    /**
     * 设置响应编码
     *
     * @param resp_encode 响应编码
     */
    public void setResp_encode(String resp_encode) {
        this.resp_encode = resp_encode;
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
     * 获取是否禁用on,off
     *
     * @return is_disenable - 是否禁用on,off
     */
    public String getIs_disenable() {
        return is_disenable;
    }

    /**
     * 设置是否禁用on,off
     *
     * @param is_disenable 是否禁用on,off
     */
    public void setIs_disenable(String is_disenable) {
        this.is_disenable = is_disenable;
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