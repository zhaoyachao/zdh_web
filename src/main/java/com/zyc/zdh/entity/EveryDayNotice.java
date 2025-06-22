package com.zyc.zdh.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
public class EveryDayNotice extends BaseProductAuthInfo{

    @Id
    @Column
    private String id;

    private String msg;

    private String is_delete;

    private String show_type;//1:弹框,2:文字

    private String product_code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    public String getShow_type() {
        return show_type;
    }

    public void setShow_type(String show_type) {
        this.show_type = show_type;
    }

    @Override
    public String getProduct_code() {
        return product_code;
    }

    @Override
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }
}
