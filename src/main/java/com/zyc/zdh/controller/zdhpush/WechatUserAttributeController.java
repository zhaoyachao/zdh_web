package com.zyc.zdh.controller.zdhpush;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.WechatMapper;
import com.zyc.zdh.dao.WechatUserAttributeMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.WechatUserAttribute;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 微信用户属性绑定表服务
 *
 * 使用权限控制需要WechatUserAttribute 继承BaseProductAuthInfo 或者 BaseProductAndDimGroupAuthInfo
 */
@Controller
public class WechatUserAttributeController extends BaseController {

    @Autowired
    private WechatMapper wechatMapper;
    @Autowired
    private WechatUserAttributeMapper wechatUserAttributeMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 微信用户属性绑定表列表首页
     * @return
     */
    @RequestMapping(value = "/wechat_user_attribute_index", method = RequestMethod.GET)
    public String wechat_user_attribute_index() {

        return "push/wechat_user_attribute_index";
    }

    /**
     * 微信用户属性绑定表列表
     * @param context 关键字
     * @param product_code 产品
     * @param dim_group 归属组
     * @return
     */
    @SentinelResource(value = "wechat_user_attribute_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_user_attribute_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<WechatUserAttribute>> wechat_user_attribute_list(String context, String product_code, String dim_group) {
        try{
            Example example=new Example(WechatUserAttribute.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andIn("wechat_channel", getWechatChannelList(wechatMapper, zdhPermissionService));
            //dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);
            //dynamicPermissionByProduct(zdhPermissionService, criteria);

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            List<WechatUserAttribute> wechatUserAttributes = wechatUserAttributeMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, wechatUserAttributes);

            return ReturnInfo.buildSuccess(wechatUserAttributes);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信用户属性绑定表列表查询失败", e);
        }

    }


    /**
    * 微信用户属性绑定表分页列表
    * @param context 关键字
    * @param openid openid
    * @param wechat_channel 微信渠道
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "wechat_user_attribute_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_user_attribute_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<WechatUserAttribute>>> wechat_user_attribute_list_by_page(String context,String openid, String wechat_channel, int limit, int offset) {
        try{
            Example example=new Example(WechatUserAttribute.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andIn("wechat_channel", getWechatChannelList(wechatMapper, zdhPermissionService));
            //dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);
            //dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(openid)){
                criteria.andEqualTo("openid", openid);
            }
            if(!StringUtils.isEmpty(wechat_channel)){
                criteria.andEqualTo("wechat_channel", wechat_channel);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("attribute_type", getLikeCondition(context));
                criteria2.orLike("attribute_value", getLikeCondition(context));
                example.and(criteria2);
            }

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = wechatUserAttributeMapper.selectCountByExample(example);

            List<WechatUserAttribute> wechatUserAttributes = wechatUserAttributeMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, wechatUserAttributes);

            PageResult<List<WechatUserAttribute>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(wechatUserAttributes);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信用户属性绑定表列表分页查询失败", e);
        }

    }

    /**
     * 微信用户属性绑定表新增首页
     * @return
     */
    @RequestMapping(value = "/wechat_user_attribute_add_index", method = RequestMethod.GET)
    public String wechat_user_attribute_add_index() {

        return "push/wechat_user_attribute_add_index";
    }

    /**
     * 微信用户属性绑定表明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "wechat_user_attribute_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_user_attribute_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<WechatUserAttribute> wechat_user_attribute_detail(String id) {
        try {
            WechatUserAttribute wechatUserAttribute = wechatUserAttributeMapper.selectByPrimaryKey(id);
            String product_code = getBaseWechatChannelAuthInfoByChannel(zdhPermissionService, wechatUserAttribute.getWechat_channel()).getProduct_code();
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService,  wechatSubscriptionInfo.getProduct_code(), wechatSubscriptionInfo.getDim_group(), getAttrSelect());
            checkAttrPermissionByProduct(zdhPermissionService,  product_code, getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", wechatUserAttribute);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 微信用户属性绑定表更新
     * @param wechatUserAttribute
     * @return
     */
    @SentinelResource(value = "wechat_user_attribute_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_user_attribute_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatUserAttribute> wechat_user_attribute_update(WechatUserAttribute wechatUserAttribute) {
        try {

            WechatUserAttribute oldWechatUserAttribute = wechatUserAttributeMapper.selectByPrimaryKey(wechatUserAttribute.getId());

            checkAttrPermissionByProduct(zdhPermissionService,  getBaseWechatChannelAuthInfoByChannel(zdhPermissionService,oldWechatUserAttribute.getWechat_channel()).getProduct_code(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService,  getBaseWechatChannelAuthInfoByChannel(zdhPermissionService,wechatUserAttribute.getWechat_channel()).getProduct_code(), getAttrEdit());



            wechatUserAttribute.setCreate_time(oldWechatUserAttribute.getCreate_time());
            wechatUserAttribute.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            wechatUserAttribute.setIs_delete(Const.NOT_DELETE);
            wechatUserAttributeMapper.updateByPrimaryKeySelective(wechatUserAttribute);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", wechatUserAttribute);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 微信用户属性绑定表新增
     * @param wechatUserAttribute
     * @return
     */
    @SentinelResource(value = "wechat_user_attribute_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_user_attribute_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatUserAttribute> wechat_user_attribute_add(WechatUserAttribute wechatUserAttribute) {
        try {
            wechatUserAttribute.setId(SnowflakeIdWorker.getInstance().nextId()+"");

            wechatUserAttribute.setIs_delete(Const.NOT_DELETE);
            wechatUserAttribute.setCreate_time(new Timestamp(System.currentTimeMillis()));
            wechatUserAttribute.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProduct(zdhPermissionService,  getBaseWechatChannelAuthInfoByChannel(zdhPermissionService,wechatUserAttribute.getWechat_channel()).getProduct_code(), getAttrAdd());

            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatUserAttribute.getProduct_code(), wechatUserAttribute.getDim_group(), getAttrAdd());
            //checkAttrPermissionByProduct(zdhPermissionService, wechatUserAttribute.getProduct_code(), getAttrAdd());
            wechatUserAttributeMapper.insertSelective(wechatUserAttribute);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", wechatUserAttribute);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 微信用户属性绑定表删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "wechat_user_attribute_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_user_attribute_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo wechat_user_attribute_delete(String[] ids) {
        try {
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatUserAttributeMapper, wechatUserAttributeMapper.getTable(), ids, getAttrDel());
            List<WechatUserAttribute> wechatUserAttributes = wechatUserAttributeMapper.selectObjectByIds(wechatUserAttributeMapper.getTable(), ids);
            Set<String> collect = wechatUserAttributes.stream().map(wechatSubscriptionInfo -> wechatSubscriptionInfo.getWechat_channel()).collect(Collectors.toSet());
            for (String wechat_channel:collect){
                checkAttrPermissionByProduct(zdhPermissionService,  getBaseWechatChannelAuthInfoByChannel(zdhPermissionService, wechat_channel).getProduct_code(), getAttrDel());
            }
            wechatUserAttributeMapper.deleteLogicByIds(wechatUserAttributeMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }
}
