package com.zyc.zdh.entity;

import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

@Table(name = "crowd_file_info")
public class CrowdFileInfo extends BaseProductAndDimGroupAuthInfo{
    @Id
    private String id;

    /**
     * 文件名
     */
    private String file_name;

    /**
     * 文件地址
     */
    private String file_url;

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
     * 数据类型,phone,email,id_card,可扩展
     */
    private String data_type;

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
     * 获取文件名
     *
     * @return file_name - 文件名
     */
    public String getFile_name() {
        return file_name;
    }

    /**
     * 设置文件名
     *
     * @param file_name 文件名
     */
    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    /**
     * 获取文件地址
     *
     * @return file_url - 文件地址
     */
    public String getFile_url() {
        return file_url;
    }

    /**
     * 设置文件地址
     *
     * @param file_url 文件地址
     */
    public void setFile_url(String file_url) {
        this.file_url = file_url;
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
     * 获取数据类型,phone,email,id_card,可扩展
     *
     * @return data_type - 数据类型,phone,email,id_card,可扩展
     */
    public String getData_type() {
        return data_type;
    }

    /**
     * 设置数据类型,phone,email,id_card,可扩展
     *
     * @param data_type 数据类型,phone,email,id_card,可扩展
     */
    public void setData_type(String data_type) {
        this.data_type = data_type;
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