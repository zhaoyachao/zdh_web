package com.zyc.zdh.controller.zdhpush;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.WechatRuleInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
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
import com.zyc.zdh.dao.WechatRuleMapper;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 微信回复规则表服务
 *
 * 使用权限控制需要WechatRuleInfo 继承BaseProductAuthInfo 或者 BaseProductAndDimGroupAuthInfo
 */
@Slf4j
@Controller
public class WechatRuleController extends BaseController {

    @Autowired
    private WechatRuleMapper wechatRuleMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 微信回复规则表列表首页
     * @return
     */
    @RequestMapping(value = "/wechat_rule_index", method = RequestMethod.GET)
    public String wechat_rule_index() {

        return "push/wechat_rule_index";
    }

    /**
     * 微信回复规则表列表
     * @param context 关键字
     * @param product_code 产品
     * @return
     */
    @SentinelResource(value = "wechat_rule_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_rule_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<WechatRuleInfo>> wechat_rule_list(String context, String product_code) {
        try{
            Example example=new Example(WechatRuleInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            //dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);
            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            List<WechatRuleInfo> wechatRuleInfos = wechatRuleMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, wechatRuleInfos);

            return ReturnInfo.buildSuccess(wechatRuleInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信回复规则表列表查询失败", e);
        }

    }


    /**
    * 微信回复规则表分页列表
    * @param context 关键字
    * @param product_code 产品code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "wechat_rule_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_rule_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<WechatRuleInfo>>> wechat_rule_list_by_page(String context,String product_code, int limit, int offset) {
        try{
            Example example=new Example(WechatRuleInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            //dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);
            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = wechatRuleMapper.selectCountByExample(example);

            List<WechatRuleInfo> wechatRuleInfos = wechatRuleMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, wechatRuleInfos);

            PageResult<List<WechatRuleInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(wechatRuleInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信回复规则表列表分页查询失败", e);
        }

    }

    /**
     * 微信回复规则表新增首页
     * @return
     */
    @RequestMapping(value = "/wechat_rule_add_index", method = RequestMethod.GET)
    public String wechat_rule_add_index() {

        return "push/wechat_rule_add_index";
    }

    /**
     * 微信回复规则表明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "wechat_rule_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_rule_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<WechatRuleInfo> wechat_rule_detail(String id) {
        try {
            WechatRuleInfo wechatRuleInfo = wechatRuleMapper.selectByPrimaryKey(id);
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService,  wechatRuleInfo.getProduct_code(), wechatRuleInfo.getDim_group(), getAttrSelect());
            checkAttrPermissionByProduct(zdhPermissionService,  wechatRuleInfo.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", wechatRuleInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 微信回复规则表更新
     * @param wechatRuleInfo
     * @return
     */
    @SentinelResource(value = "wechat_rule_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_rule_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatRuleInfo> wechat_rule_update(WechatRuleInfo wechatRuleInfo, String msg_type, String content, String title,
                                                         String description, String musicurl, String hqmusicurl, String thumbmediaid, String picurl, String url) {
        try {

            WechatRuleInfo oldWechatRuleInfo = wechatRuleMapper.selectByPrimaryKey(wechatRuleInfo.getId());

            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatRuleInfo.getProduct_code(), wechatRuleInfo.getDim_group(), getAttrEdit());
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldWechatRuleInfo.getProduct_code(), oldWechatRuleInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService, wechatRuleInfo.getProduct_code(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService, oldWechatRuleInfo.getProduct_code(), getAttrEdit());

            WechatRuleInfo.RuleConfig ruleConfig = new WechatRuleInfo.RuleConfig();
            ruleConfig.setMsg_type(msg_type);
            ruleConfig.setContent(content);
            ruleConfig.setTitle(title);
            ruleConfig.setDescription(description);
            ruleConfig.setMusicurl(musicurl);
            ruleConfig.setHqmusicurl(hqmusicurl);
            ruleConfig.setThumbmediaid(thumbmediaid);
            ruleConfig.setPicurl(picurl);
            ruleConfig.setUrl(url);
            wechatRuleInfo.setRule_config(JsonUtil.formatJsonString(ruleConfig));

            wechatRuleInfo.setCreate_time(oldWechatRuleInfo.getCreate_time());
            wechatRuleInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            wechatRuleInfo.setIs_delete(Const.NOT_DELETE);
            wechatRuleMapper.updateByPrimaryKeySelective(wechatRuleInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", wechatRuleInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 微信回复规则表新增
     * @param wechatRuleInfo
     * @return
     */
    @SentinelResource(value = "wechat_rule_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_rule_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatRuleInfo> wechat_rule_add(WechatRuleInfo wechatRuleInfo, String msg_type, String content, String title,
                                                      String description, String musicurl, String hqmusicurl, String thumbmediaid, String picurl, String url) {
        try {
            wechatRuleInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");

            WechatRuleInfo.RuleConfig ruleConfig = new WechatRuleInfo.RuleConfig();
            ruleConfig.setMsg_type(msg_type);
            ruleConfig.setContent(content);
            ruleConfig.setTitle(title);
            ruleConfig.setDescription(description);
            ruleConfig.setMusicurl(musicurl);
            ruleConfig.setHqmusicurl(hqmusicurl);
            ruleConfig.setThumbmediaid(thumbmediaid);
            ruleConfig.setPicurl(picurl);
            ruleConfig.setUrl(url);
            wechatRuleInfo.setRule_config(JsonUtil.formatJsonString(ruleConfig));

            wechatRuleInfo.setOwner(getOwner());
            wechatRuleInfo.setIs_delete(Const.NOT_DELETE);
            wechatRuleInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            wechatRuleInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatRuleInfo.getProduct_code(), wechatRuleInfo.getDim_group(), getAttrAdd());
            checkAttrPermissionByProduct(zdhPermissionService, wechatRuleInfo.getProduct_code(), getAttrAdd());
            wechatRuleMapper.insertSelective(wechatRuleInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", wechatRuleInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 微信回复规则表删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "wechat_rule_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_rule_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo wechat_rule_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatRuleMapper, wechatRuleMapper.getTable(), ids, getAttrDel());
            wechatRuleMapper.deleteLogicByIds(wechatRuleMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    @SentinelResource(value = "wechat_rule_status", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_rule_status", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo wechat_rule_status(String id, String status) {
        try {
            WechatRuleInfo oldWechatRuleInfo = wechatRuleMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService, oldWechatRuleInfo.getProduct_code(), getAttrEdit());
            oldWechatRuleInfo.setStatus(status);
            oldWechatRuleInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            wechatRuleMapper.updateByPrimaryKeySelective(oldWechatRuleInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e.getMessage());
        }
    }
}
