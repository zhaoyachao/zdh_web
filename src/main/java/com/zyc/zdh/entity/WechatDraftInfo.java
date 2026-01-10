package com.zyc.zdh.entity;

import java.sql.Timestamp;
import javax.persistence.*;

@Table(name = "wechat_draft_info")
public class WechatDraftInfo extends BaseProductAuthInfo{
    /**
     * 主键ID
     */
    @Id
    private String id;

    /**
     * 服务号
     */
    private String wechat_channel;

    /**
     * 草稿名称
     */
    private String draft_name;

    /**
     * 文章类型，分别有图文消息（news）、图片消息（newspic），不填默认为图文消息（news）
     */
    private String article_type;

    /**
     * 标题
     */
    private String title;

    /**
     * 作者
     */
    private String author;

    /**
     * 图文消息的摘要，总长度不超过128个字。仅有单图文消息才有摘要，多图文此处为空。如果本字段为没有填写，则默认抓取正文前54个字
     */
    private String digest;

    /**
     * 是否打开评论，0不打开(默认)，1打开
     */
    private String need_open_comment;

    /**
     * 是否粉丝才可评论，0所有人可评论(默认)，1粉丝才可评论
     */
    private String only_fans_can_comment;

    /**
     * 图文消息封面裁剪为2.35:1规格的坐标字段。以原始图片（thumb_media_id）左上角（0,0），右下角（1,1）建立平面坐标系，经过裁剪后的图片，其左上角所在的坐标即为（X1,Y1）,右下角所在的坐标则为（X2,Y2），用分隔符_拼接为X1_Y1_X2_Y2，每个坐标值的精度为不超过小数点后6位数字。示例见下图，图中(X1,Y1) 等于（0.1945,0）,(X2,Y2)等于（1,0.5236），所以请求参数值为0.1945_0_1_0.5236。
     */
    private String pic_crop_235_1;

    /**
     * 图文消息封面裁剪为1:1规格的坐标字段，裁剪原理同pic_crop_235_1，裁剪后的图片必须符合规格要求。
     */
    private String pic_crop_1_1;

    /**
     * 草稿上传微信后生成的素材id
     */
    private String media_id;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 产品code
     */
    private String product_code;

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
     * 图文消息的具体内容
     */
    private String content;

    /**
     * 即点击“阅读原文”后的URL
     */
    private String content_source_url;

    /**
     * article_type为图文消息（news）时必填，图文消息的封面图片素材id（必须是永久MediaID）
     */
    private String thumb_media_id;

    /**
     * 图片消息里的图片相关信息，图片数量最多为20张，首张图片即为封面图
     */
    private String image_info;

    /**
     * 图片消息的封面信息
     */
    private String cover_info;

    /**
     * 商品信息
     */
    private String product_info;

    /**
     * 获取主键ID
     *
     * @return id - 主键ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置主键ID
     *
     * @param id 主键ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取服务号
     *
     * @return wechat_channel - 服务号
     */
    public String getWechat_channel() {
        return wechat_channel;
    }

    /**
     * 设置服务号
     *
     * @param wechat_channel 服务号
     */
    public void setWechat_channel(String wechat_channel) {
        this.wechat_channel = wechat_channel;
    }

    /**
     * 获取草稿名称
     *
     * @return draft_name - 草稿名称
     */
    public String getDraft_name() {
        return draft_name;
    }

    /**
     * 设置草稿名称
     *
     * @param draft_name 草稿名称
     */
    public void setDraft_name(String draft_name) {
        this.draft_name = draft_name;
    }

    /**
     * 获取文章类型，分别有图文消息（news）、图片消息（newspic），不填默认为图文消息（news）
     *
     * @return article_type - 文章类型，分别有图文消息（news）、图片消息（newspic），不填默认为图文消息（news）
     */
    public String getArticle_type() {
        return article_type;
    }

    /**
     * 设置文章类型，分别有图文消息（news）、图片消息（newspic），不填默认为图文消息（news）
     *
     * @param article_type 文章类型，分别有图文消息（news）、图片消息（newspic），不填默认为图文消息（news）
     */
    public void setArticle_type(String article_type) {
        this.article_type = article_type;
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取作者
     *
     * @return author - 作者
     */
    public String getAuthor() {
        return author;
    }

    /**
     * 设置作者
     *
     * @param author 作者
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * 获取图文消息的摘要，总长度不超过128个字。仅有单图文消息才有摘要，多图文此处为空。如果本字段为没有填写，则默认抓取正文前54个字
     *
     * @return digest - 图文消息的摘要，总长度不超过128个字。仅有单图文消息才有摘要，多图文此处为空。如果本字段为没有填写，则默认抓取正文前54个字
     */
    public String getDigest() {
        return digest;
    }

    /**
     * 设置图文消息的摘要，总长度不超过128个字。仅有单图文消息才有摘要，多图文此处为空。如果本字段为没有填写，则默认抓取正文前54个字
     *
     * @param digest 图文消息的摘要，总长度不超过128个字。仅有单图文消息才有摘要，多图文此处为空。如果本字段为没有填写，则默认抓取正文前54个字
     */
    public void setDigest(String digest) {
        this.digest = digest;
    }

    /**
     * 获取是否打开评论，0不打开(默认)，1打开
     *
     * @return need_open_comment - 是否打开评论，0不打开(默认)，1打开
     */
    public String getNeed_open_comment() {
        return need_open_comment;
    }

    /**
     * 设置是否打开评论，0不打开(默认)，1打开
     *
     * @param need_open_comment 是否打开评论，0不打开(默认)，1打开
     */
    public void setNeed_open_comment(String need_open_comment) {
        this.need_open_comment = need_open_comment;
    }

    /**
     * 获取是否粉丝才可评论，0所有人可评论(默认)，1粉丝才可评论
     *
     * @return only_fans_can_comment - 是否粉丝才可评论，0所有人可评论(默认)，1粉丝才可评论
     */
    public String getOnly_fans_can_comment() {
        return only_fans_can_comment;
    }

    /**
     * 设置是否粉丝才可评论，0所有人可评论(默认)，1粉丝才可评论
     *
     * @param only_fans_can_comment 是否粉丝才可评论，0所有人可评论(默认)，1粉丝才可评论
     */
    public void setOnly_fans_can_comment(String only_fans_can_comment) {
        this.only_fans_can_comment = only_fans_can_comment;
    }

    /**
     * 获取图文消息封面裁剪为2.35:1规格的坐标字段。以原始图片（thumb_media_id）左上角（0,0），右下角（1,1）建立平面坐标系，经过裁剪后的图片，其左上角所在的坐标即为（X1,Y1）,右下角所在的坐标则为（X2,Y2），用分隔符_拼接为X1_Y1_X2_Y2，每个坐标值的精度为不超过小数点后6位数字。示例见下图，图中(X1,Y1) 等于（0.1945,0）,(X2,Y2)等于（1,0.5236），所以请求参数值为0.1945_0_1_0.5236。
     *
     * @return pic_crop_235_1 - 图文消息封面裁剪为2.35:1规格的坐标字段。以原始图片（thumb_media_id）左上角（0,0），右下角（1,1）建立平面坐标系，经过裁剪后的图片，其左上角所在的坐标即为（X1,Y1）,右下角所在的坐标则为（X2,Y2），用分隔符_拼接为X1_Y1_X2_Y2，每个坐标值的精度为不超过小数点后6位数字。示例见下图，图中(X1,Y1) 等于（0.1945,0）,(X2,Y2)等于（1,0.5236），所以请求参数值为0.1945_0_1_0.5236。
     */
    public String getPic_crop_235_1() {
        return pic_crop_235_1;
    }

    /**
     * 设置图文消息封面裁剪为2.35:1规格的坐标字段。以原始图片（thumb_media_id）左上角（0,0），右下角（1,1）建立平面坐标系，经过裁剪后的图片，其左上角所在的坐标即为（X1,Y1）,右下角所在的坐标则为（X2,Y2），用分隔符_拼接为X1_Y1_X2_Y2，每个坐标值的精度为不超过小数点后6位数字。示例见下图，图中(X1,Y1) 等于（0.1945,0）,(X2,Y2)等于（1,0.5236），所以请求参数值为0.1945_0_1_0.5236。
     *
     * @param pic_crop_235_1 图文消息封面裁剪为2.35:1规格的坐标字段。以原始图片（thumb_media_id）左上角（0,0），右下角（1,1）建立平面坐标系，经过裁剪后的图片，其左上角所在的坐标即为（X1,Y1）,右下角所在的坐标则为（X2,Y2），用分隔符_拼接为X1_Y1_X2_Y2，每个坐标值的精度为不超过小数点后6位数字。示例见下图，图中(X1,Y1) 等于（0.1945,0）,(X2,Y2)等于（1,0.5236），所以请求参数值为0.1945_0_1_0.5236。
     */
    public void setPic_crop_235_1(String pic_crop_235_1) {
        this.pic_crop_235_1 = pic_crop_235_1;
    }

    /**
     * 获取图文消息封面裁剪为1:1规格的坐标字段，裁剪原理同pic_crop_235_1，裁剪后的图片必须符合规格要求。
     *
     * @return pic_crop_1_1 - 图文消息封面裁剪为1:1规格的坐标字段，裁剪原理同pic_crop_235_1，裁剪后的图片必须符合规格要求。
     */
    public String getPic_crop_1_1() {
        return pic_crop_1_1;
    }

    /**
     * 设置图文消息封面裁剪为1:1规格的坐标字段，裁剪原理同pic_crop_235_1，裁剪后的图片必须符合规格要求。
     *
     * @param pic_crop_1_1 图文消息封面裁剪为1:1规格的坐标字段，裁剪原理同pic_crop_235_1，裁剪后的图片必须符合规格要求。
     */
    public void setPic_crop_1_1(String pic_crop_1_1) {
        this.pic_crop_1_1 = pic_crop_1_1;
    }

    /**
     * 获取草稿上传微信后生成的素材id
     *
     * @return media_id - 草稿上传微信后生成的素材id
     */
    public String getMedia_id() {
        return media_id;
    }

    /**
     * 设置草稿上传微信后生成的素材id
     *
     * @param media_id 草稿上传微信后生成的素材id
     */
    public void setMedia_id(String media_id) {
        this.media_id = media_id;
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
     * 获取图文消息的具体内容
     *
     * @return content - 图文消息的具体内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置图文消息的具体内容
     *
     * @param content 图文消息的具体内容
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获取即点击“阅读原文”后的URL
     *
     * @return content_source_url - 即点击“阅读原文”后的URL
     */
    public String getContent_source_url() {
        return content_source_url;
    }

    /**
     * 设置即点击“阅读原文”后的URL
     *
     * @param content_source_url 即点击“阅读原文”后的URL
     */
    public void setContent_source_url(String content_source_url) {
        this.content_source_url = content_source_url;
    }

    /**
     * 获取article_type为图文消息（news）时必填，图文消息的封面图片素材id（必须是永久MediaID）
     *
     * @return thumb_media_id - article_type为图文消息（news）时必填，图文消息的封面图片素材id（必须是永久MediaID）
     */
    public String getThumb_media_id() {
        return thumb_media_id;
    }

    /**
     * 设置article_type为图文消息（news）时必填，图文消息的封面图片素材id（必须是永久MediaID）
     *
     * @param thumb_media_id article_type为图文消息（news）时必填，图文消息的封面图片素材id（必须是永久MediaID）
     */
    public void setThumb_media_id(String thumb_media_id) {
        this.thumb_media_id = thumb_media_id;
    }

    /**
     * 获取图片消息里的图片相关信息，图片数量最多为20张，首张图片即为封面图
     *
     * @return image_info - 图片消息里的图片相关信息，图片数量最多为20张，首张图片即为封面图
     */
    public String getImage_info() {
        return image_info;
    }

    /**
     * 设置图片消息里的图片相关信息，图片数量最多为20张，首张图片即为封面图
     *
     * @param image_info 图片消息里的图片相关信息，图片数量最多为20张，首张图片即为封面图
     */
    public void setImage_info(String image_info) {
        this.image_info = image_info;
    }

    /**
     * 获取图片消息的封面信息
     *
     * @return cover_info - 图片消息的封面信息
     */
    public String getCover_info() {
        return cover_info;
    }

    /**
     * 设置图片消息的封面信息
     *
     * @param cover_info 图片消息的封面信息
     */
    public void setCover_info(String cover_info) {
        this.cover_info = cover_info;
    }

    /**
     * 获取商品信息
     *
     * @return product_info - 商品信息
     */
    public String getProduct_info() {
        return product_info;
    }

    /**
     * 设置商品信息
     *
     * @param product_info 商品信息
     */
    public void setProduct_info(String product_info) {
        this.product_info = product_info;
    }
}