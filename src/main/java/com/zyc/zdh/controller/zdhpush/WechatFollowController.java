package com.zyc.zdh.controller.zdhpush;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.WechatMapper;
import com.zyc.zdh.dao.WechatSubscriptionMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.WechatSubscriptionInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.pushx.PushxWechatUserService;
import com.zyc.zdh.pushx.entity.WechatUserRemarkResponse;
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
 * 微信关注状态表服务
 *
 * 使用权限控制需要WechatSubscriptionInfo 继承BaseProductAuthInfo 或者 BaseProductAndDimGroupAuthInfo
 */
@Controller
public class WechatFollowController extends BaseController {

    @Autowired
    private WechatSubscriptionMapper wechatSubscriptionMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;
    @Autowired
    private WechatMapper wechatMapper;
    @Autowired
    private PushxWechatUserService pushxWechatUserService;

    /**
     * 微信关注状态表列表首页
     * @return
     */
    @RequestMapping(value = "/wechat_follow_index", method = RequestMethod.GET)
    public String wechat_follow_index() {

        return "push/wechat_follow_index";
    }

    /**
     * 微信关注状态表列表
     * @param context 关键字
     * @param wechat_channel 服务号
     * @return
     */
    @SentinelResource(value = "wechat_follow_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_follow_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<WechatSubscriptionInfo>> wechat_follow_list(String context, String wechat_channel) {
        try{
            Example example=new Example(WechatSubscriptionInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
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

            List<WechatSubscriptionInfo> wechatSubscriptionInfos = wechatSubscriptionMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, wechatSubscriptionInfos);

            return ReturnInfo.buildSuccess(wechatSubscriptionInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信关注状态表列表查询失败", e);
        }

    }


    /**
    * 微信关注状态表分页列表
    * @param context 关键字
    * @param wechat_channel 服务号
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "wechat_follow_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_follow_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<WechatSubscriptionInfo>>> wechat_follow_list_by_page(String context,String wechat_channel, int limit, int offset) {
        try{
            Example example=new Example(WechatSubscriptionInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andIn("wechat_channel", getWechatChannelList(wechatMapper, zdhPermissionService));
            //dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);
            //dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(wechat_channel)){
                criteria.andEqualTo("wechat_channel", wechat_channel);
            }
//            if(!StringUtils.isEmpty(dim_group)){
//                criteria.andEqualTo("dim_group", dim_group);
//            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = wechatSubscriptionMapper.selectCountByExample(example);

            List<WechatSubscriptionInfo> wechatSubscriptionInfos = wechatSubscriptionMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, wechatSubscriptionInfos);

            PageResult<List<WechatSubscriptionInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(wechatSubscriptionInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信关注状态表列表分页查询失败", e);
        }

    }

    /**
     * 微信关注状态表新增首页
     * @return
     */
    @RequestMapping(value = "/wechat_follow_add_index", method = RequestMethod.GET)
    public String wechat_follow_add_index() {

        return "push/wechat_follow_add_index";
    }

    /**
     * 微信关注状态表明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "wechat_follow_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_follow_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<WechatSubscriptionInfo> wechat_follow_detail(String id) {
        try {
            WechatSubscriptionInfo wechatSubscriptionInfo = wechatSubscriptionMapper.selectByPrimaryKey(id);
            String product_code = getProductCodeByChannel(wechatMapper, wechatSubscriptionInfo.getWechat_channel());
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService,  wechatSubscriptionInfo.getProduct_code(), wechatSubscriptionInfo.getDim_group(), getAttrSelect());
            checkAttrPermissionByProduct(zdhPermissionService,  product_code, getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", wechatSubscriptionInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 微信关注状态表更新
     * @param wechatSubscriptionInfo
     * @return
     */
    @SentinelResource(value = "wechat_follow_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_follow_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatSubscriptionInfo> wechat_follow_update(WechatSubscriptionInfo wechatSubscriptionInfo) {
        try {

            WechatSubscriptionInfo oldWechatSubscriptionInfo = wechatSubscriptionMapper.selectByPrimaryKey(wechatSubscriptionInfo.getId());

            checkAttrPermissionByProduct(zdhPermissionService,  getProductCodeByChannel(wechatMapper,oldWechatSubscriptionInfo.getWechat_channel()), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService,  getProductCodeByChannel(wechatMapper,wechatSubscriptionInfo.getWechat_channel()), getAttrEdit());
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatSubscriptionInfo.getProduct_code(), wechatSubscriptionInfo.getDim_group(), getAttrEdit());
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldWechatSubscriptionInfo.getProduct_code(), oldWechatSubscriptionInfo.getDim_group(), getAttrEdit());
            //checkAttrPermissionByProduct(zdhPermissionService, wechatSubscriptionInfo.getProduct_code(), getAttrEdit());
            //checkAttrPermissionByProduct(zdhPermissionService, oldWechatSubscriptionInfo.getProduct_code(), getAttrEdit());

            wechatSubscriptionInfo.setCreate_time(oldWechatSubscriptionInfo.getCreate_time());
            wechatSubscriptionInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            wechatSubscriptionInfo.setIs_delete(Const.NOT_DELETE);
            wechatSubscriptionMapper.updateByPrimaryKeySelective(wechatSubscriptionInfo);

            //如果修改修改备注需要同步微信
            if(!StringUtils.isEmpty(wechatSubscriptionInfo.getRemark()) && !wechatSubscriptionInfo.getRemark().equals(oldWechatSubscriptionInfo.getRemark())){
                WechatUserRemarkResponse remarkResponse = pushxWechatUserService.updateRemark(wechatSubscriptionInfo.getWechat_channel(), wechatSubscriptionInfo.getOpenid(), wechatSubscriptionInfo.getRemark());
                if(!remarkResponse.isSuccess()){
                    throw new Exception(remarkResponse.getMsg());
                }
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", wechatSubscriptionInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 微信关注状态表新增
     * @param wechatSubscriptionInfo
     * @return
     */
    @SentinelResource(value = "wechat_follow_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_follow_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatSubscriptionInfo> wechat_follow_add(WechatSubscriptionInfo wechatSubscriptionInfo) {
        try {
            wechatSubscriptionInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            //wechatSubscriptionInfo.setOwner(getOwner());
            wechatSubscriptionInfo.setIs_delete(Const.NOT_DELETE);
            wechatSubscriptionInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            wechatSubscriptionInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProduct(zdhPermissionService,  getProductCodeByChannel(wechatMapper,wechatSubscriptionInfo.getWechat_channel()), getAttrAdd());
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatSubscriptionInfo.getProduct_code(), wechatSubscriptionInfo.getDim_group(), getAttrAdd());
            //checkAttrPermissionByProduct(zdhPermissionService, wechatSubscriptionInfo.getProduct_code(), getAttrAdd());
            wechatSubscriptionMapper.insertSelective(wechatSubscriptionInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", wechatSubscriptionInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 微信关注状态表删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "wechat_follow_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_follow_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo wechat_follow_delete(String[] ids) {
        try {
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatSubscriptionMapper, wechatSubscriptionMapper.getTable(), ids, getAttrDel());
            List<WechatSubscriptionInfo> wechatSubscriptionInfos = wechatSubscriptionMapper.selectObjectByIds(wechatSubscriptionMapper.getTable(), ids);
            Set<String> collect = wechatSubscriptionInfos.stream().map(wechatSubscriptionInfo -> wechatSubscriptionInfo.getWechat_channel()).collect(Collectors.toSet());
            for (String wechat_channel:collect){
                checkAttrPermissionByProduct(zdhPermissionService,  getProductCodeByChannel(wechatMapper, wechat_channel), getAttrDel());
            }
            wechatSubscriptionMapper.deleteLogicByIds(wechatSubscriptionMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

}
