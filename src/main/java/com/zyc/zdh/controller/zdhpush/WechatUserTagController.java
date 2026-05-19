package com.zyc.zdh.controller.zdhpush;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.google.common.collect.Lists;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.WechatMapper;
import com.zyc.zdh.dao.WechatTagMapper;
import com.zyc.zdh.dao.WechatUserTagMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.pushx.PushxWechatTagService;
import com.zyc.zdh.pushx.entity.WechatTagResponse;
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
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 微信用户标签明细服务
 *
 * 使用权限控制需要WechatUserTagInfo 继承BaseProductAuthInfo 或者 BaseProductAndDimGroupAuthInfo
 */
@Controller
public class WechatUserTagController extends BaseController {

    @Autowired
    private WechatTagMapper wechatTagMapper;
    @Autowired
    private WechatUserTagMapper wechatUserTagMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;
    @Autowired
    private PushxWechatTagService pushxWechatTagService;
    @Autowired
    private WechatMapper wechatMapper;

    /**
     * 微信用户标签明细列表首页
     * @return
     */
    @RequestMapping(value = "/wechat_user_tag_index", method = RequestMethod.GET)
    public String wechat_user_tag_index() {

        return "push/wechat_user_tag_index";
    }

    /**
     * 微信用户标签明细列表
     * @param context 关键字
     * @param wechat_channel 微信服务号
     * @return
     */
    @SentinelResource(value = "wechat_user_tag_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_user_tag_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<WechatUserTagInfo>> wechat_user_tag_list(String context, String wechat_channel) {
        try{
            Example example=new Example(WechatUserTagInfo.class);
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

            List<WechatUserTagInfo> wechatUserTagInfos = wechatUserTagMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, wechatUserTagInfos);

            return ReturnInfo.buildSuccess(wechatUserTagInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信用户标签明细列表查询失败", e);
        }

    }


    /**
    * 微信用户标签明细分页列表
    * @param context 关键字
    * @param wechat_channel 微信服务号
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "wechat_user_tag_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_user_tag_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<WechatUserTagInfo>>> wechat_user_tag_list_by_page(String context,String wechat_channel, int limit, int offset) {
        try{
            Example example=new Example(WechatUserTagInfo.class);
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

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = wechatUserTagMapper.selectCountByExample(example);

            List<WechatUserTagInfo> wechatUserTagInfos = wechatUserTagMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, wechatUserTagInfos);

            //替换tag枚举
            Example example3=new Example(WechatTagInfo.class);
            Example.Criteria criteria3=example3.createCriteria();
            criteria3.andEqualTo("is_delete", Const.NOT_DELETE);
            dynamicPermissionByProduct(zdhPermissionService, criteria3);
            List<WechatTagInfo> wechatTagInfos = wechatTagMapper.selectByExample(example3);

            //生成tid和tname的映射关系
            Map<String, String> tidAndTname = wechatTagInfos.stream().collect(Collectors.toMap(WechatTagInfo::getTid, WechatTagInfo::getTname));

            wechatUserTagInfos.forEach(wechatUserTagInfo -> {
                wechatUserTagInfo.setTag_name(tidAndTname.get(wechatUserTagInfo.getTag_id()));
            });

            PageResult<List<WechatUserTagInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(wechatUserTagInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信用户标签明细列表分页查询失败", e);
        }

    }

    /**
     * 微信用户标签明细新增首页
     * @return
     */
    @RequestMapping(value = "/wechat_user_tag_add_index", method = RequestMethod.GET)
    public String wechat_user_tag_add_index() {

        return "push/wechat_user_tag_add_index";
    }

    /**
     * 微信用户标签明细明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "wechat_user_tag_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_user_tag_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<WechatUserTagInfo> wechat_user_tag_detail(String id) {
        try {
            WechatUserTagInfo wechatUserTagInfo = wechatUserTagMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService,  getProductCodeByChannel(wechatMapper, wechatUserTagInfo.getWechat_channel()), getAttrSelect());
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService,  wechatUserTagInfo.getProduct_code(), wechatUserTagInfo.getDim_group(), getAttrSelect());
            //checkAttrPermissionByProduct(zdhPermissionService,  wechatUserTagInfo.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", wechatUserTagInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 微信用户标签明细新增
     * @param wechatUserTagInfo
     * @return
     */
    @SentinelResource(value = "wechat_user_tag_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_user_tag_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatUserTagInfo> wechat_user_tag_add(WechatUserTagInfo wechatUserTagInfo) {
        try {
            wechatUserTagInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            //wechatUserTagInfo.setOwner(getOwner());
            wechatUserTagInfo.setIs_delete(Const.NOT_DELETE);
            wechatUserTagInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            wechatUserTagInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProduct(zdhPermissionService,  getProductCodeByChannel(wechatMapper,wechatUserTagInfo.getWechat_channel()), getAttrAdd());
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatUserTagInfo.getProduct_code(), wechatUserTagInfo.getDim_group(), getAttrAdd());
            //checkAttrPermissionByProduct(zdhPermissionService, wechatUserTagInfo.getProduct_code(), getAttrAdd());
            WechatTagResponse batchuntagging = pushxWechatTagService.batchuntagging(wechatUserTagInfo, Lists.newArrayList(wechatUserTagInfo.getOpenid()));
            if(!batchuntagging.isSuccess()){
                throw new Exception(batchuntagging.getMsg());
            }
            wechatUserTagMapper.insertSelective(wechatUserTagInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", wechatUserTagInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 微信用户标签明细删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "wechat_user_tag_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_user_tag_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo wechat_user_tag_delete(String[] ids) {
        try {
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatUserTagMapper, wechatUserTagMapper.getTable(), ids, getAttrDel());
            List<WechatUserTagInfo> wechatUserTagInfos = wechatUserTagMapper.selectObjectByIds(wechatUserTagMapper.getTable(), ids);
            String lastTagId = "";
            for (WechatUserTagInfo wechatUserTagInfo : wechatUserTagInfos) {
              if(!StringUtils.isEmpty(lastTagId) && !lastTagId.equals(wechatUserTagInfo.getTag_id())){
                  throw new Exception("微信用户标签明细删除失败,请选择同一标签进行删除");
              }
            }
            Set<String> collect = wechatUserTagInfos.stream().map(wechatUserTagInfo -> wechatUserTagInfo.getWechat_channel()).collect(Collectors.toSet());
            for (String wechat_channel:collect){
                checkAttrPermissionByProduct(zdhPermissionService,  getProductCodeByChannel(wechatMapper, wechat_channel), getAttrDel());
            }

            WechatTagResponse batchuntagging = pushxWechatTagService.batchuntagging(wechatUserTagInfos.get(0), Lists.newArrayList(ids));
            if(!batchuntagging.isSuccess()){
                throw new Exception(batchuntagging.getMsg());
            }
            wechatUserTagMapper.deleteLogicByIds(wechatUserTagMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }
}
