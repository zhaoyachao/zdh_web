package com.zyc.zdh.pushx.entity;

import java.util.List;

public class WechatTemplate
    {
        private String template_id;

        private String title;

        private String primary_industry;

        private String deputy_industry;

        private String content;

        private String example;

        private List<WechatTemplateContentParam> wechatTemplateContentParams;

        public void setTemplate_id(String template_id){
            this.template_id = template_id;
        }
        public String getTemplate_id(){
            return this.template_id;
        }
        public void setTitle(String title){
            this.title = title;
        }
        public String getTitle(){
            return this.title;
        }
        public void setPrimary_industry(String primary_industry){
            this.primary_industry = primary_industry;
        }
        public String getPrimary_industry(){
            return this.primary_industry;
        }
        public void setDeputy_industry(String deputy_industry){
            this.deputy_industry = deputy_industry;
        }
        public String getDeputy_industry(){
            return this.deputy_industry;
        }
        public void setContent(String content){
            this.content = content;
        }
        public String getContent(){
            return this.content;
        }
        public void setExample(String example){
            this.example = example;
        }
        public String getExample(){
            return this.example;
        }

        public List<WechatTemplateContentParam> getWechatTemplateContentParams() {
            return wechatTemplateContentParams;
        }

        public void setWechatTemplateContentParams(List<WechatTemplateContentParam> wechatTemplateContentParams) {
            this.wechatTemplateContentParams = wechatTemplateContentParams;
        }
    }