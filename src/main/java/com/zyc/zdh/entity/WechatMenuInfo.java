package com.zyc.zdh.entity;

import com.zyc.zdh.util.JsonUtil;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Table(name = "wechat_menu_info")
public class WechatMenuInfo extends BaseProductAuthInfo{

    @Id
    private String id;

    /**
     * 公众号
     */
    private String wechat_app;

    /**
     * 菜单类型, 1:自定义菜单, 2:个性化菜单
     */
    private String menu_type;

    /**
     * 菜单状态, 1:新建,2:启用,3:禁用
     */
    private String status;

    /**
     *  菜单名称
     */
    private String menu_name;

    /**
     * 个性化规则,匹配用户标签
     */
    private String tag_id;

    /**
     * 客户端版本，当前只具体到系统型号：IOS(1), Android(2),Others(3)，不填则不做匹配
     */
    private String client_platform_type;

    /**
     * 菜单配置
     */
    private String config;

    /**
     * 拥有者
     */
    private String owner;

    /**
     * 创建时间
     */
    private Timestamp create_time;

    /**
     * 更新时间
     */
    private Timestamp update_time;

    /**
     * 是否删除,0:未删除,1:删除
     */
    private String is_delete;

    /**
     * 产品code
     */
    private String product_code;

    @Transient
    private List<ButtionTree> menu_list;

    @Transient
    private Map<String, ButtionTree> menu_map;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWechat_app() {
        return wechat_app;
    }

    public void setWechat_app(String wechat_app) {
        this.wechat_app = wechat_app;
    }

    public String getMenu_type() {
        return menu_type;
    }

    public void setMenu_type(String menu_type) {
        this.menu_type = menu_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMenu_name() {
        return menu_name;
    }

    public void setMenu_name(String menu_name) {
        this.menu_name = menu_name;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getClient_platform_type() {
        return client_platform_type;
    }

    public void setClient_platform_type(String client_platform_type) {
        this.client_platform_type = client_platform_type;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public Timestamp getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Timestamp update_time) {
        this.update_time = update_time;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    @Override
    public String getProduct_code() {
        return product_code;
    }

    @Override
    public void setProduct_code(String product_code) {
        this.product_code = product_code;
    }

    public List<ButtionTree> getMenu_list() {
        return JsonUtil.toJavaListBean(config, ButtionTree.class);
    }

    public Map<String, ButtionTree> getMenu_map() {
        List<ButtionTree> javaListBean = JsonUtil.toJavaListBean(config, ButtionTree.class);
        Map<String, ButtionTree> map = javaListBean.stream()
                .collect(Collectors.toMap(ButtionTree::getId, Function.identity()));
        return map;
    }

    public static class ButtionTree{

        /**
         * 主键id
         */

        private String id;
        /**
         * 上级id
         */
        private String parent="#";
        /**
         * 资源名称
         */
        private String text;

        /**
         * 资源图标
         */
        private String icon="non";
        /**
         * 层级（1-系统,2-模块, 3-菜单，4-按钮）
         */
        private String level;

        /**
         * 排序
         */
        private String order="1";


        //按钮基础配置

        /**
         * 微信菜单id
         * 调用微信接口后存在
         */
        private String wechat_menu_id;
        /**
         * 菜单的响应动作类型（与 sub_button 互斥）	click view scancode_push scancode_waitmsg pic_sysphoto pic_photo_or_album pic_weixin location_select media_id article_id article_view_limited
         */
        private String type;

        /**
         * 菜单标题，不超过16个字节，子菜单不超过60个字节
         */
        private String name;

        /**
         * 菜单KEY值，用于消息接口推送，不超过128字节。click等点击类型必须
         */
        private String key;

        /**
         * 网页链接，用户点击菜单可打开链接，不超过1024字节。 type为miniprogram时，不支持小程序的老版本客户端将打开本url。view、miniprogram类型必填
         */
        private String url;

        /**
         * 小程序的appid（仅认证公众号可配置），miniprogram类型必须
         */
        private String appid;

        /**
         * 小程序的页面路径，miniprogram类型必须
         */
        private String pagepath;

        /**
         * 发布后获得的合法 article_id，article_id类型和article_view_limited类型必须
         */
        private String article_id;

        /**
         * 调用新增永久素材接口返回的合法media_id。media_id类型和view_limited类型必须
         */
        private String media_id;

        /**
         * 二级菜单
         */
        private String sub_button;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getParent() {
            return parent;
        }

        public void setParent(String parent) {
            this.parent = parent;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public String getWechat_menu_id() {
            return wechat_menu_id;
        }

        public void setWechat_menu_id(String wechat_menu_id) {
            this.wechat_menu_id = wechat_menu_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPagepath() {
            return pagepath;
        }

        public void setPagepath(String pagepath) {
            this.pagepath = pagepath;
        }

        public String getArticle_id() {
            return article_id;
        }

        public void setArticle_id(String article_id) {
            this.article_id = article_id;
        }

        public String getMedia_id() {
            return media_id;
        }

        public void setMedia_id(String media_id) {
            this.media_id = media_id;
        }

        public String getSub_button() {
            return sub_button;
        }

        public void setSub_button(String sub_button) {
            this.sub_button = sub_button;
        }
    }
}