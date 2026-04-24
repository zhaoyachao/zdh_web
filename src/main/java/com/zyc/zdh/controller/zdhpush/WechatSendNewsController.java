package com.zyc.zdh.controller.zdhpush;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.WechatMapper;
import com.zyc.zdh.dao.WechatSendNewsMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.WechatSendNewsInfo;
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
 * 微信草稿发布明细服务
 *
 * 使用权限控制需要WechatSendNewsInfo 继承BaseProductAuthInfo 或者 BaseProductAndDimGroupAuthInfo
 */
@Controller
public class WechatSendNewsController extends BaseController {

    @Autowired
    private WechatSendNewsMapper wechatSendNewsMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;
    @Autowired
    private WechatMapper wechatMapper;

    /**
     * 微信草稿发布明细列表首页
     * @return
     */
    @RequestMapping(value = "/wechat_send_news_index", method = RequestMethod.GET)
    @White
    public String wechat_send_news_index() {

        return "push/wechat_send_news_index";
    }

    /**
     * 微信草稿发布明细列表
     * @param context 关键字
     * @param wechat_channel 产品
     * @return
     */
    @SentinelResource(value = "wechat_send_news_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_send_news_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<List<WechatSendNewsInfo>> wechat_send_news_list(String context, String wechat_channel) {
        try{
            Example example=new Example(WechatSendNewsInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andIn("wechat_channel", getWechatChannelList(wechatMapper, zdhPermissionService));
            //dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);
            //dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(wechat_channel)){
                criteria.andEqualTo("wechat_channel", wechat_channel);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            List<WechatSendNewsInfo> wechatSendNewsInfos = wechatSendNewsMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, wechatSendNewsInfos);

            return ReturnInfo.buildSuccess(wechatSendNewsInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信草稿发布明细列表查询失败", e);
        }

    }


    /**
    * 微信草稿发布明细分页列表
    * @param context 关键字
    * @param wechat_channel 产品code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "wechat_send_news_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_send_news_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<PageResult<List<WechatSendNewsInfo>>> wechat_send_news_list_by_page(String context,String wechat_channel, int limit, int offset) {
        try{
            Example example=new Example(WechatSendNewsInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andIn("wechat_channel", getWechatChannelList(wechatMapper, zdhPermissionService));

            if(!StringUtils.isEmpty(wechat_channel)){
                criteria.andEqualTo("wechat_channel", wechat_channel);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = wechatSendNewsMapper.selectCountByExample(example);

            List<WechatSendNewsInfo> wechatSendNewsInfos = wechatSendNewsMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, wechatSendNewsInfos);

            PageResult<List<WechatSendNewsInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(wechatSendNewsInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信草稿发布明细列表分页查询失败", e);
        }

    }

    /**
     * 微信草稿发布明细新增首页
     * @return
     */
    @RequestMapping(value = "/wechat_send_news_add_index", method = RequestMethod.GET)
    @White
    public String wechat_send_news_add_index() {

        return "push/wechat_send_news_add_index";
    }

    /**
     * 微信草稿发布明细明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "wechat_send_news_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_send_news_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<WechatSendNewsInfo> wechat_send_news_detail(String id) {
        try {
            WechatSendNewsInfo wechatSendNewsInfo = wechatSendNewsMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService,  getProductCodeByChannel(wechatMapper, wechatSendNewsInfo.getWechat_channel()), getAttrSelect());
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService,  wechatSendNewsInfo.getProduct_code(), wechatSendNewsInfo.getDim_group(), getAttrSelect());
            //checkAttrPermissionByProduct(zdhPermissionService,  wechatSendNewsInfo.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", wechatSendNewsInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 微信草稿发布明细更新
     * @param wechatSendNewsInfo
     * @return
     */
    @SentinelResource(value = "wechat_send_news_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_send_news_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo<WechatSendNewsInfo> wechat_send_news_update(WechatSendNewsInfo wechatSendNewsInfo) {
        try {

            WechatSendNewsInfo oldWechatSendNewsInfo = wechatSendNewsMapper.selectByPrimaryKey(wechatSendNewsInfo.getId());

            checkAttrPermissionByProduct(zdhPermissionService,  getProductCodeByChannel(wechatMapper,oldWechatSendNewsInfo.getWechat_channel()), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService,  getProductCodeByChannel(wechatMapper,wechatSendNewsInfo.getWechat_channel()), getAttrEdit());

            wechatSendNewsInfo.setCreate_time(oldWechatSendNewsInfo.getCreate_time());
            wechatSendNewsInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            wechatSendNewsMapper.updateByPrimaryKeySelective(wechatSendNewsInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", wechatSendNewsInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 微信草稿发布明细新增
     * @param wechatSendNewsInfo
     * @return
     */
    @SentinelResource(value = "wechat_send_news_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_send_news_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo<WechatSendNewsInfo> wechat_send_news_add(WechatSendNewsInfo wechatSendNewsInfo) {
        try {
            wechatSendNewsInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            wechatSendNewsInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            wechatSendNewsInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProduct(zdhPermissionService,  getProductCodeByChannel(wechatMapper,wechatSendNewsInfo.getWechat_channel()), getAttrAdd());
            wechatSendNewsMapper.insertSelective(wechatSendNewsInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", wechatSendNewsInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 微信草稿发布明细删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "wechat_send_news_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_send_news_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo wechat_send_news_delete(String[] ids) {
        try {
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatSendNewsMapper, wechatSendNewsMapper.getTable(), ids, getAttrDel());
            List<WechatSendNewsInfo> wechatSendNewsInfos = wechatSendNewsMapper.selectObjectByIds(wechatSendNewsMapper.getTable(), ids);

            Set<String> collect = wechatSendNewsInfos.stream().map(wechatSendNewsInfo -> wechatSendNewsInfo.getWechat_channel()).collect(Collectors.toSet());
            for (String wechat_channel:collect){
                checkAttrPermissionByProduct(zdhPermissionService,  getProductCodeByChannel(wechatMapper, wechat_channel), getAttrDel());
            }
            wechatSendNewsMapper.deleteLogicByIds(wechatSendNewsMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }
}
