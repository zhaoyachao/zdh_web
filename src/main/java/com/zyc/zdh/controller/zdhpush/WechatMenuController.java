package com.zyc.zdh.controller.zdhpush;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.google.common.collect.Lists;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.WechatMenuMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.WechatMenuInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.pushx.PushxWechatMenuService;
import com.zyc.zdh.pushx.entity.WechatMenuResponse;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * 微信菜单信息表服务
 *
 * 使用权限控制需要WechatMenuInfo 继承BaseProductAuthInfo 或者 BaseProductAndDimGroupAuthInfo
 */
@Controller
public class WechatMenuController extends BaseController {

    @Autowired
    private WechatMenuMapper wechatMenuMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;
    @Autowired
    private PushxWechatMenuService pushxWechatMenuService;

    private static final String MENU_TYPE_CUSTOM = "1";
    private static final String MENU_TYPE_CONDITIONAL = "2";
    /**
     * 微信菜单信息表列表首页
     * @return
     */
    @RequestMapping(value = "/wechat_menu_index", method = RequestMethod.GET)
    public String wechat_menu_index() {

        return "push/wechat_menu_index";
    }

    /**
     * 微信菜单信息表列表
     * @param context 关键字
     * @param product_code 产品
     * @return
     */
    @SentinelResource(value = "wechat_menu_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_menu_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<WechatMenuInfo>> wechat_menu_list(String context, String product_code) {
        try{
            Example example=new Example(WechatMenuInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("name", getLikeCondition(context));
                example.and(criteria2);
            }

            List<WechatMenuInfo> wechatMenuInfos = wechatMenuMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, wechatMenuInfos);

            return ReturnInfo.buildSuccess(wechatMenuInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信菜单信息表列表查询失败", e);
        }

    }


    /**
    * 微信菜单信息表分页列表
    * @param context 关键字
    * @param product_code 产品code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "wechat_menu_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_menu_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<WechatMenuInfo>>> wechat_menu_list_by_page(String context,String product_code, int limit, int offset) {
        try{
            Example example=new Example(WechatMenuInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("name", getLikeCondition(context));
                example.and(criteria2);
            }

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = wechatMenuMapper.selectCountByExample(example);

            List<WechatMenuInfo> wechatMenuInfos = wechatMenuMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, wechatMenuInfos);

            PageResult<List<WechatMenuInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(wechatMenuInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信菜单信息表列表分页查询失败", e);
        }

    }

    /**
     * 微信菜单信息表新增首页
     * @return
     */
    @RequestMapping(value = "/wechat_menu_add_index", method = RequestMethod.GET)
    public String wechat_menu_add_index() {

        return "push/wechat_menu_add_index";
    }

    /**
     * 微信菜单信息配置页面
     * @return
     */
    @RequestMapping(value = "/wechat_menu_add_index2", method = RequestMethod.GET)
    public String wechat_menu_add_index2() {

        return "push/wechat_menu_add_index2";
    }

    /**
     * 微信菜单信息配置页面
     * @return
     */
    @RequestMapping(value = "/wechat_menu_add_index3", method = RequestMethod.GET)
    public String wechat_menu_add_index3() {

        return "push/wechat_menu_add_index3";
    }

    /**
     * 微信菜单信息表明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "wechat_menu_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_menu_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<WechatMenuInfo> wechat_menu_detail(String id) {
        try {
            WechatMenuInfo wechatMenuInfo = wechatMenuMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService,  wechatMenuInfo.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", wechatMenuInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 微信菜单信息表更新
     * @param wechatMenuInfo
     * @return
     */
    @SentinelResource(value = "wechat_menu_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_menu_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatMenuInfo> wechat_menu_update(WechatMenuInfo wechatMenuInfo) {
        try {
            String id = wechatMenuInfo.getId();

            WechatMenuInfo oldWechatMenuInfo = wechatMenuMapper.selectByPrimaryKey(id);

            checkAttrPermissionByProduct(zdhPermissionService, oldWechatMenuInfo.getProduct_code(), getAttrEdit());

            oldWechatMenuInfo.setMenu_name(wechatMenuInfo.getMenu_name());
            //oldWechatMenuInfo.setMenu_type(wechatMenuInfo.getMenu_type());
            oldWechatMenuInfo.setTag_id(wechatMenuInfo.getTag_id());
            oldWechatMenuInfo.setClient_platform_type(wechatMenuInfo.getClient_platform_type());

            oldWechatMenuInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            oldWechatMenuInfo.setStatus(Const.WECHAT_MENU_STAUTS_INIT);
            wechatMenuMapper.updateByPrimaryKeySelective(oldWechatMenuInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", oldWechatMenuInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 微信菜单信息表新增
     * @param wechatMenuInfo
     * @return
     */
    @SentinelResource(value = "wechat_menu_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_menu_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatMenuInfo> wechat_menu_add(WechatMenuInfo wechatMenuInfo) {
        try {

            checkAttrPermissionByProduct(zdhPermissionService, wechatMenuInfo.getProduct_code(), getAttrAdd());

            WechatMenuInfo wechatMenuInfo1 = new WechatMenuInfo();
            wechatMenuInfo1.setProduct_code(wechatMenuInfo.getProduct_code());
            wechatMenuInfo1.setIs_delete(Const.NOT_DELETE);
            wechatMenuInfo1.setWechat_app(wechatMenuInfo.getWechat_app());
            wechatMenuInfo1.setMenu_name(wechatMenuInfo.getMenu_name());
            wechatMenuInfo1.setMenu_type(wechatMenuInfo.getMenu_type());

            int i = wechatMenuMapper.selectCount(wechatMenuInfo1);

            if(i>0){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败，产品和公众号已存在菜单", null);
            }

            wechatMenuInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            wechatMenuInfo.setOwner(getOwner());
            wechatMenuInfo.setIs_delete(Const.NOT_DELETE);
            wechatMenuInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            wechatMenuInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            WechatMenuInfo.ButtionTree buttonTree = new WechatMenuInfo.ButtionTree();

            buttonTree.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            buttonTree.setText(wechatMenuInfo.getWechat_app());
            buttonTree.setLevel("1");

            String config = JsonUtil.formatJsonString(Lists.newArrayList(buttonTree));
            wechatMenuInfo.setConfig(config);

            wechatMenuMapper.insertSelective(wechatMenuInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", wechatMenuInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }


    /**
     * 微信菜单信息表删除
     * 删除微信自定义菜单,会同时删除所有的个性化菜单
     * 删除逻辑可不开放给用户,菜单可直接更新
     * @param ids
     * @return
     */
    @SentinelResource(value = "wechat_menu_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_menu_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo wechat_menu_delete(String[] ids) {
        try {
            if(ids.length > 1){
                throw new Exception("一次只能删除一个菜单");
            }
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatMenuMapper, wechatMenuMapper.getTable(), ids, getAttrDel());
            WechatMenuInfo wechatMenuInfo = wechatMenuMapper.selectByPrimaryKey(ids[0]);
            WechatMenuResponse wechatMenuResponse = pushxWechatMenuService.deleteMenu(wechatMenuInfo);
            if(wechatMenuResponse !=null && wechatMenuResponse.isSuccess()){
                wechatMenuMapper.deleteLogicByIds(wechatMenuMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
                return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
            }
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    /**
     * 微信菜单信息表更新
     * @param buttionTree
     * @return
     */
    @SentinelResource(value = "wechat_menu_add_node", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_menu_add_node", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatMenuInfo> wechat_menu_add_node(WechatMenuInfo.ButtionTree buttionTree) {
        try {
            String id = buttionTree.getId();

            WechatMenuInfo oldWechatMenuInfo = wechatMenuMapper.selectByPrimaryKey(id);

            checkAttrPermissionByProduct(zdhPermissionService, oldWechatMenuInfo.getProduct_code(), getAttrEdit());

            buttionTree.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            List<WechatMenuInfo.ButtionTree> menuList = oldWechatMenuInfo.getMenu_list();
            menuList.add(buttionTree);

            oldWechatMenuInfo.setConfig(JsonUtil.formatJsonString(menuList));
            oldWechatMenuInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            oldWechatMenuInfo.setStatus(Const.WECHAT_MENU_STAUTS_INIT);
            wechatMenuMapper.updateByPrimaryKeySelective(oldWechatMenuInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", oldWechatMenuInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 微信菜单信息表更新
     * @param buttionTree
     * @return
     */
    @SentinelResource(value = "wechat_menu_update_node", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_menu_update_node", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatMenuInfo> wechat_menu_update_node(WechatMenuInfo.ButtionTree buttionTree, String node_id) {
        try {
            String id = buttionTree.getId();

            WechatMenuInfo oldWechatMenuInfo = wechatMenuMapper.selectByPrimaryKey(id);

            checkAttrPermissionByProduct(zdhPermissionService, oldWechatMenuInfo.getProduct_code(), getAttrEdit());

            List<WechatMenuInfo.ButtionTree> menuList = oldWechatMenuInfo.getMenu_list();

            for (WechatMenuInfo.ButtionTree menu : menuList) {
                if(menu.getId().equals(node_id)){
                    menu.setText(buttionTree.getName());
                    menu.setLevel(buttionTree.getLevel());
                    menu.setParent(buttionTree.getParent());
                    menu.setIcon(buttionTree.getIcon());
                    menu.setOrder(buttionTree.getOrder());

                    menu.setName(buttionTree.getName());
                    menu.setType(buttionTree.getType());
                    menu.setKey(buttionTree.getKey());
                    menu.setUrl(buttionTree.getUrl());
                    menu.setMedia_id(buttionTree.getMedia_id());
                    menu.setAppid(buttionTree.getAppid());
                    menu.setArticle_id(buttionTree.getArticle_id());
                    menu.setPagepath(buttionTree.getPagepath());

                    menu.setName(buttionTree.getName());
                }
            }

            oldWechatMenuInfo.setConfig(JsonUtil.formatJsonString(menuList));
            oldWechatMenuInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            oldWechatMenuInfo.setStatus(Const.WECHAT_MENU_STAUTS_INIT);
            wechatMenuMapper.updateByPrimaryKeySelective(oldWechatMenuInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", oldWechatMenuInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 微信菜单信息表删除
     * @param id
     * @return
     */
    @SentinelResource(value = "wechat_menu_del_node", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_menu_del_node", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatMenuInfo> wechat_menu_del_node(String id, String node_id) {
        try {

            WechatMenuInfo oldWechatMenuInfo = wechatMenuMapper.selectByPrimaryKey(id);

            checkAttrPermissionByProduct(zdhPermissionService, oldWechatMenuInfo.getProduct_code(), getAttrEdit());

            List<WechatMenuInfo.ButtionTree> menuList = oldWechatMenuInfo.getMenu_list();

            List<WechatMenuInfo.ButtionTree> newMenuList = new ArrayList<>();

            for (WechatMenuInfo.ButtionTree menu : menuList) {
                if(menu.getId().equals(node_id) || menu.getParent().equals(node_id)){
                    continue;
                }
                newMenuList.add(menu);
            }

            oldWechatMenuInfo.setConfig(JsonUtil.formatJsonString(newMenuList));
            oldWechatMenuInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            oldWechatMenuInfo.setStatus(Const.WECHAT_MENU_STAUTS_INIT);
            wechatMenuMapper.updateByPrimaryKeySelective(oldWechatMenuInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", oldWechatMenuInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    @SentinelResource(value = "wechat_menu_upload", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_menu_upload", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo wechat_menu_upload(String id) {
        try {
            WechatMenuInfo wechatMenuInfo = wechatMenuMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService,  wechatMenuInfo.getProduct_code(), getAttrEdit());

            //调用pushx生成菜单
            WechatMenuResponse response = pushxWechatMenuService.createMenu(wechatMenuInfo);
            wechatMenuInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            if(response == null){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "调用pushx异常", null);
            }

            if(response != null && response.isSuccess()){
                wechatMenuInfo.setStatus(Const.WECHAT_MENU_STAUTS_ENABLE);
            }else{
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "同步失败", response.getMsg());
            }
            //成功更新状态
            wechatMenuMapper.updateByPrimaryKeySelective(wechatMenuInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "同步失败", e.getMessage());
        }
    }
}
