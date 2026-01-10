package com.zyc.zdh.pushx.entity;

import java.util.List;

public class WechatDraftRequest {
    /**
     * 服务号
     */
    private String wechat_channel;

    private String media_id;

    private List<Article> articles;

    private String sign;

    public String getWechat_channel() {
        return wechat_channel;
    }

    public void setWechat_channel(String wechat_channel) {
        this.wechat_channel = wechat_channel;
    }

    public String getMedia_id() {
        return media_id;
    }

    public void setMedia_id(String media_id) {
        this.media_id = media_id;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public static class Article {
        private String article_type;
        private String title;
        private String author;
        private String digest;
        private String content;
        private String content_source_url;
        private String thumb_media_id;
        private int need_open_comment;
        private int only_fans_can_comment;
        private String pic_crop_235_1;
        private String pic_crop_1_1;
        private ImageInfo image_info;
        private CoverInfo cover_info;
        private ProductInfo product_info;

        public String getArticle_type() {
            return article_type;
        }

        public void setArticle_type(String article_type) {
            this.article_type = article_type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getDigest() {
            return digest;
        }

        public void setDigest(String digest) {
            this.digest = digest;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getContent_source_url() {
            return content_source_url;
        }

        public void setContent_source_url(String content_source_url) {
            this.content_source_url = content_source_url;
        }

        public String getThumb_media_id() {
            return thumb_media_id;
        }

        public void setThumb_media_id(String thumb_media_id) {
            this.thumb_media_id = thumb_media_id;
        }

        public int getNeed_open_comment() {
            return need_open_comment;
        }

        public void setNeed_open_comment(int need_open_comment) {
            this.need_open_comment = need_open_comment;
        }

        public int getOnly_fans_can_comment() {
            return only_fans_can_comment;
        }

        public void setOnly_fans_can_comment(int only_fans_can_comment) {
            this.only_fans_can_comment = only_fans_can_comment;
        }

        public String getPic_crop_235_1() {
            return pic_crop_235_1;
        }

        public void setPic_crop_235_1(String pic_crop_235_1) {
            this.pic_crop_235_1 = pic_crop_235_1;
        }

        public String getPic_crop_1_1() {
            return pic_crop_1_1;
        }

        public void setPic_crop_1_1(String pic_crop_1_1) {
            this.pic_crop_1_1 = pic_crop_1_1;
        }

        public ImageInfo getImage_info() {
            return image_info;
        }

        public void setImage_info(ImageInfo image_info) {
            this.image_info = image_info;
        }

        public CoverInfo getCover_info() {
            return cover_info;
        }

        public void setCover_info(CoverInfo cover_info) {
            this.cover_info = cover_info;
        }

        public ProductInfo getProduct_info() {
            return product_info;
        }

        public void setProduct_info(ProductInfo product_info) {
            this.product_info = product_info;
        }
    }

    public static class ImageInfo {
        private List<ImageMediaId> image_list;
        public List<ImageMediaId> getImage_list() {
            return image_list;
        }
        public void setImage_list(List<ImageMediaId> image_list) {
            this.image_list = image_list;
        }
    }


    public static class ImageMediaId{
        private String image_media_id;
        public String getImage_media_id() {
            return image_media_id;
        }
        public void setImage_media_id(String image_media_id) {
            this.image_media_id = image_media_id;
        }
    }


    public static class CoverInfo {
        private List<CropPercent> crop_percent_list;
        public List<CropPercent> getCrop_percent_list() {
            return crop_percent_list;
        }
        public void setCrop_percent_list(List<CropPercent> crop_percent_list) {
            this.crop_percent_list = crop_percent_list;
        }
    }


    public static class CropPercent {

        private String ratio;
        private String x1;
        private String y1;
        private String x2;
        private String y2;

        public String getRatio() {
            return ratio;
        }
        public void setRatio(String ratio) {
            this.ratio = ratio;
        }
        public String getX1() {
            return x1;
        }
        public void setX1(String x1) {
            this.x1 = x1;
        }
        public String getY1() {
            return y1;
        }

        public void setY1(String y1) {
            this.y1 = y1;
        }
        public String getX2() {
            return x2;
        }
        public void setX2(String x2) {
            this.x2 = x2;
        }
        public String getY2() {
            return y2;
        }
        public void setY2(String y2) {
            this.y2 = y2;
        }
    }

    public static class ProductInfo {
        private FooterProductInfo footer_product_info;
        public FooterProductInfo getFooter_product_info() {
            return footer_product_info;
        }
        public void setFooter_product_info(FooterProductInfo footer_product_info) {
            this.footer_product_info = footer_product_info;
        }
    }

    public static class FooterProductInfo{
        private String product_key;
        public String getProduct_key() {
            return product_key;
        }
        public void setProduct_key(String product_key) {
            this.product_key = product_key;
        }
    }

}
