package com.zyc.zdh.pushx.entity;

import java.util.List;

public class WechatTemplateResponse{
        private Integer code;
        private String msg;
        private List<WechatTemplate> data;

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }
        public String getMsg() {
            return msg;
        }
        public void setMsg(String msg) {
            this.msg = msg;
        }
        public List<WechatTemplate> getData() {
            return data;
        }
        public void setData(List<WechatTemplate> data) {
            this.data = data;
        }
    }