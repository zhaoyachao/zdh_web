package com.zyc.zdh.pushx.impl;

import com.zyc.zdh.entity.WechatMenuInfo;
import com.zyc.zdh.pushx.PushxWechatMenuService;
import com.zyc.zdh.pushx.entity.WechatMenuMatchRule;
import com.zyc.zdh.pushx.entity.WechatMenuRequest;
import com.zyc.zdh.pushx.entity.WechatMenuResponse;
import com.zyc.zdh.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PushxWechatMenuServiceImpl implements PushxWechatMenuService {

    private static final String MENU_CREATE = "/api/v1/pushx/wechat/menu/create";
    private static final String MENU_DELETE = "/api/v1/pushx/wechat/menu/delete";

    @Override
    public WechatMenuResponse createMenu(WechatMenuInfo wechatMenuInfo) {

        try{
            WechatMenuRequest wechatMenuRequest = buildMenuRequest(wechatMenuInfo);

            String sign = SignUtil.generatSign(JsonUtil.toJavaMap(JsonUtil.formatJsonString(wechatMenuRequest)), ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_SERVICE_KEY));
            wechatMenuRequest.setSign(sign);

            String json = JsonUtil.formatJsonString(wechatMenuRequest);

            String response = HttpUtil.builder().retryCount(1).postJSON(ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_BASE_URL) + MENU_CREATE, json);

            LogUtil.info(this.getClass(), "pushx menu response: {}", response);
            WechatMenuResponse wechatMenuResponse = JsonUtil.toJavaBean(response, WechatMenuResponse.class);
            return wechatMenuResponse;
        }catch (Exception e){
            LogUtil.error(this.getClass(), "创建菜单异常", e);
        }
        return null;
    }

    @Override
    public WechatMenuResponse deleteMenu(WechatMenuInfo wechatMenuInfo) {
        try{
            WechatMenuRequest wechatMenuRequest = new WechatMenuRequest();
            wechatMenuRequest.setChannel(wechatMenuInfo.getWechat_app());
            wechatMenuRequest.setMenu_type(wechatMenuInfo.getMenu_type());

            String sign = SignUtil.generatSign(JsonUtil.toJavaMap(JsonUtil.formatJsonString(wechatMenuRequest)), ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_SERVICE_KEY));
            wechatMenuRequest.setSign(sign);

            String json = JsonUtil.formatJsonString(wechatMenuRequest);

            String response =  HttpUtil.builder().retryCount(1).postJSON(ConfigUtil.getValue(ConfigUtil.ZDH_PUSHX_BASE_URL) + MENU_DELETE, json);
            LogUtil.info(this.getClass(), "pushx menu response: {}", response);
            WechatMenuResponse wechatMenuResponse = JsonUtil.toJavaBean(response, WechatMenuResponse.class);
            return wechatMenuResponse;

        }catch (Exception e){
            LogUtil.error(this.getClass(), "删除菜单异常", e);
        }
        return null;
    }


    private WechatMenuRequest buildMenuRequest(WechatMenuInfo wechatMenuInfo){
        WechatMenuRequest wechatMenuRequest = new WechatMenuRequest();
        wechatMenuRequest.setChannel(wechatMenuInfo.getWechat_app());
        wechatMenuRequest.setMenu_type(wechatMenuInfo.getMenu_type());

        //个性化菜单
        if(!StringUtils.isEmpty(wechatMenuInfo.getTag_id()) || !StringUtils.isEmpty(wechatMenuInfo.getClient_platform_type())){
            WechatMenuMatchRule wechatMenuMatchRule = new WechatMenuMatchRule();
            wechatMenuMatchRule.setTag_id(wechatMenuInfo.getTag_id());
            wechatMenuMatchRule.setClient_platform_type(wechatMenuInfo.getClient_platform_type());
            wechatMenuRequest.setMatch_rule(wechatMenuMatchRule);
        }

        String config = wechatMenuInfo.getConfig();

        List<WechatMenuInfo.ButtionTree> buttionTrees = JsonUtil.toJavaListBean(config, WechatMenuInfo.ButtionTree.class);

        Map<String, WechatMenuRequest.WechatMenuButton> menuButtonMap = new HashMap<String, WechatMenuRequest.WechatMenuButton>();

        for(WechatMenuInfo.ButtionTree buttionTree : buttionTrees){
            if(buttionTree.getParent().equals("#")){
                continue;
            }
            if(buttionTree.getLevel().equals("1")){
                continue;
            }

            WechatMenuRequest.WechatMenuButton wechatMenuButton = new WechatMenuRequest.WechatMenuButton();

            wechatMenuButton.setName(buttionTree.getText());
            wechatMenuButton.setKey(buttionTree.getKey());
            wechatMenuButton.setType(buttionTree.getType());
            wechatMenuButton.setUrl(buttionTree.getUrl());
            wechatMenuButton.setMedia_id(buttionTree.getMedia_id());
            wechatMenuButton.setArticle_id(buttionTree.getArticle_id());
            wechatMenuButton.setAppid(buttionTree.getAppid());
            wechatMenuButton.setPagepath(buttionTree.getPagepath());
            menuButtonMap.put(buttionTree.getId(), wechatMenuButton);
        }

        for(WechatMenuInfo.ButtionTree buttionTree : buttionTrees){
            if(buttionTree.getParent().equals("#")){
                continue;
            }

            String parent = buttionTree.getParent();
            String id = buttionTree.getId();
            //二级菜单
            if(menuButtonMap.containsKey(parent)){
                WechatMenuRequest.WechatMenuButton wechatMenuButton = menuButtonMap.get(parent);
                List<WechatMenuRequest.WechatMenuButton> subButton = wechatMenuButton.getSub_button();
                if(subButton == null){
                    subButton = new ArrayList<WechatMenuRequest.WechatMenuButton>();
                    subButton.add(menuButtonMap.get(id));
                }
                wechatMenuButton.setSub_button(subButton);
            }
        }

        List<WechatMenuRequest.WechatMenuButton> wechatMenuButtons = new ArrayList<WechatMenuRequest.WechatMenuButton>();
        for(WechatMenuInfo.ButtionTree buttionTree : buttionTrees){
            if(buttionTree.getLevel().equals("2")){
                wechatMenuButtons.add(menuButtonMap.get(buttionTree.getId()));
            }
        }

        wechatMenuRequest.setButton(wechatMenuButtons);
        return wechatMenuRequest;
    }
}
