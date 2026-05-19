package com.zyc.zdh.pushx.entity;

import java.util.List;

public class WechatCommentResponse extends PushxBaseResponse {

    private CommentData data;

    @Override
    public CommentData getData() {
        return data;
    }

    public void setData(CommentData data) {
        this.data = data;
    }

    public static class CommentData {
        private String comment_id;
        private List<CommentItem> list;

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public List<CommentItem> getList() {
            return list;
        }

        public void setList(List<CommentItem> list) {
            this.list = list;
        }
    }

    public static class CommentItem {
        private String article_id;
        private String comment_id;
        private String parent_comment_id;
        private String openid;
        private String content;
        private String create_time;
        private String comment_type;
        private String status;

        public String getArticle_id() {
            return article_id;
        }

        public void setArticle_id(String article_id) {
            this.article_id = article_id;
        }

        public String getComment_id() {
            return comment_id;
        }

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public String getParent_comment_id() {
            return parent_comment_id;
        }

        public void setParent_comment_id(String parent_comment_id) {
            this.parent_comment_id = parent_comment_id;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreate_time() {
            return create_time;
        }

        public void setCreate_time(String create_time) {
            this.create_time = create_time;
        }

        public String getComment_type() {
            return comment_type;
        }

        public void setComment_type(String comment_type) {
            this.comment_type = comment_type;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
