package com.zyc.zdh.controller.zdhpush;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.WechatCommentMapper;
import com.zyc.zdh.dao.WechatMapper;
import com.zyc.zdh.dao.WechatSendNewsMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.pushx.PushxWechatCommentService;
import com.zyc.zdh.pushx.entity.WechatCommentRequest;
import com.zyc.zdh.pushx.entity.WechatCommentResponse;
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

@Controller
public class WechatCommentController extends BaseController {

    @Autowired
    private WechatCommentMapper wechatCommentMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;
    @Autowired
    private PushxWechatCommentService pushxWechatCommentService;
    @Autowired
    private WechatMapper wechatMapper;
    @Autowired
    private WechatSendNewsMapper wechatSendNewsMapper;

    /**
     * 微信评论列表
     * @return
     */
    @RequestMapping(value = "/wechat_comment_index", method = RequestMethod.GET)
    public String wechat_comment_index() {
        return "push/wechat_comment_index";
    }

    /**
     * 获取微信评论列表
     * @param context
     * @param wechat_channel
     * @param article_id
     * @param limit
     * @param offset
     * @return
     */
    @SentinelResource(value = "wechat_comment_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_comment_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<WechatCommentInfo>>> wechat_comment_list_by_page(String context, String wechat_channel,
                                                                                        String article_id, int limit, int offset) {
        try {
            Example example = new Example(WechatCommentInfo.class);
            example.setOrderByClause("create_time desc");
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andIn("wechat_channel", getWechatChannelList(wechatMapper, zdhPermissionService));

            if(!StringUtils.isEmpty(wechat_channel)){
                criteria.andEqualTo("wechat_channel", wechat_channel);
            }

            if (!StringUtils.isEmpty(article_id)) {
                criteria.andEqualTo("article_id", article_id);
            }

            if (!StringUtils.isEmpty(context)) {
                Example.Criteria criteria2 = example.createCriteria();
                criteria2.orLike("content", getLikeCondition(context));
                criteria2.orLike("reply_content", getLikeCondition(context));
                criteria2.orLike("openid", getLikeCondition(context));
                criteria2.orLike("comment_id", getLikeCondition(context));
                example.and(criteria2);
            }

            RowBounds rowBounds = new RowBounds(offset, limit);
            int total = wechatCommentMapper.selectCountByExample(example);
            List<WechatCommentInfo> list = wechatCommentMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, list);

            PageResult<List<WechatCommentInfo>> pageResult = new PageResult<>();
            pageResult.setRows(list);
            pageResult.setTotal(total);
            return ReturnInfo.buildSuccess(pageResult);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信评论分页查询失败", e);
        }
    }

    /**
     * 添加微信评论回复
     * @param id
     * @param content
     * @return
     */
    @SentinelResource(value = "wechat_comment_reply_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_comment_reply_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation = Propagation.NESTED)
    public ReturnInfo wechat_comment_reply_add(String id, String content) {
        try {
            if (StringUtils.isEmpty(id) || StringUtils.isEmpty(content)) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "参数缺失", null);
            }
            WechatCommentInfo parent = wechatCommentMapper.selectByPrimaryKey(id);
            if (parent == null || "1".equals(parent.getIs_delete())) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "父评论不存在", null);
            }
            checkAttrPermissionByProduct(zdhPermissionService,  getBaseWechatChannelAuthInfoByChannel(zdhPermissionService, parent.getWechat_channel()).getProduct_code(), getAttrSelect());

            //根据wechat_channel和article_id查询文章
            WechatSendNewsInfo wechatSendNewsInfo = new WechatSendNewsInfo();
            wechatSendNewsInfo.setArticle_id(parent.getArticle_id());
            wechatSendNewsInfo.setWechat_channel(parent.getWechat_channel());

            List<WechatSendNewsInfo> wechatSendNewsInfos = wechatSendNewsMapper.select(wechatSendNewsInfo);
            if (wechatSendNewsInfos == null || wechatSendNewsInfos.size() == 0) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "文章不存在", null);
            }
            wechatSendNewsInfo = wechatSendNewsInfos.get(0);

            WechatCommentRequest request = new WechatCommentRequest();
            request.setWechat_channel(parent.getWechat_channel());
            request.setArticle_id(parent.getArticle_id());
            request.setMsg_data_id(wechatSendNewsInfo.getMsg_data_id());
            request.setComment_id(parent.getComment_id());
            request.setContent(content);

            WechatCommentResponse response = pushxWechatCommentService.replyAdd(request);
            if (response == null) {
                throw new RuntimeException("微信评论回复失败, 响应为空");
            }
            if (!response.isSuccess()) {
                throw new RuntimeException("微信评论回复失败, 微信失败码: " + response.getCode() + ", 微信失败信息: " + response.getMsg());
            }

            //wechatCommentMapper.insertSelective(reply);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "回复成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "回复失败", e.getMessage());
        }
    }

    /**
     * 删除微信评论回复
     * @param id
     * @return
     */
    @SentinelResource(value = "wechat_comment_reply_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_comment_reply_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation = Propagation.NESTED)
    public ReturnInfo wechat_comment_reply_delete(String id) {
        try {
            if (StringUtils.isEmpty(id)) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "参数缺失", null);
            }
            WechatCommentInfo parent = wechatCommentMapper.selectByPrimaryKey(id);
            if (parent == null || "1".equals(parent.getIs_delete())) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "父评论不存在", null);
            }
            checkAttrPermissionByProduct(zdhPermissionService,  getBaseWechatChannelAuthInfoByChannel(zdhPermissionService, parent.getWechat_channel()).getProduct_code(), getAttrSelect());

            //根据wechat_channel和article_id查询文章
            WechatSendNewsInfo wechatSendNewsInfo = new WechatSendNewsInfo();
            wechatSendNewsInfo.setArticle_id(parent.getArticle_id());
            wechatSendNewsInfo.setWechat_channel(parent.getWechat_channel());

            List<WechatSendNewsInfo> wechatSendNewsInfos = wechatSendNewsMapper.select(wechatSendNewsInfo);
            if (wechatSendNewsInfos == null || wechatSendNewsInfos.size() == 0) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "文章不存在", null);
            }
            wechatSendNewsInfo = wechatSendNewsInfos.get(0);

            WechatCommentRequest request = new WechatCommentRequest();
            request.setWechat_channel(parent.getWechat_channel());
            request.setArticle_id(parent.getArticle_id());
            request.setMsg_data_id(wechatSendNewsInfo.getMsg_data_id());
            request.setComment_id(parent.getComment_id());

            WechatCommentResponse response = pushxWechatCommentService.replyDelete(request);
            if (response == null) {
                throw new RuntimeException("微信评论回复删除失败, 响应为空");
            }
            if (!response.isSuccess()) {
                throw new RuntimeException("微信评论回复删除失败, 微信失败码: " + response.getCode() + ", 微信失败信息: " + response.getMsg());
            }
            parent.setReply_content("");
            parent.setReply_time("");
            parent.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            wechatCommentMapper.updateByPrimaryKey(parent);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    /**
     * 删除微信评论
     * @param ids
     * @return
     */
    @SentinelResource(value = "wechat_comment_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_comment_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation = Propagation.NESTED)
    public ReturnInfo wechat_comment_delete(String[] ids) {
        try {
            if (ids == null || ids.length == 0) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "请选择要删除的记录", null);
            }
            List<WechatCommentInfo> comments = wechatCommentMapper.selectObjectByIds(wechatCommentMapper.getTable(), ids);
            for (WechatCommentInfo item : comments) {
                checkAttrPermissionByProduct(zdhPermissionService,  getBaseWechatChannelAuthInfoByChannel(zdhPermissionService, item.getWechat_channel()).getProduct_code(), getAttrDel());

                //根据wechat_channel和article_id查询文章
                WechatSendNewsInfo wechatSendNewsInfo = new WechatSendNewsInfo();
                wechatSendNewsInfo.setArticle_id(item.getArticle_id());
                wechatSendNewsInfo.setWechat_channel(item.getWechat_channel());

                List<WechatSendNewsInfo> wechatSendNewsInfos = wechatSendNewsMapper.select(wechatSendNewsInfo);
                if (wechatSendNewsInfos == null || wechatSendNewsInfos.size() == 0) {
                    return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "文章不存在", null);
                }
                wechatSendNewsInfo = wechatSendNewsInfos.get(0);

                WechatCommentRequest request = new WechatCommentRequest();
                request.setWechat_channel(item.getWechat_channel());
                request.setArticle_id(item.getArticle_id());
                request.setComment_id(item.getComment_id());
                request.setMsg_data_id(wechatSendNewsInfo.getMsg_data_id());
                WechatCommentResponse response = pushxWechatCommentService.commentDelete(request);
                if (response == null || !response.isSuccess()) {
                    throw new RuntimeException("删除失败, comment_id=" + item.getComment_id());
                }
                wechatCommentMapper.deleteLogicByIds(wechatCommentMapper.getTable(), new String[]{item.getId()}, new Timestamp(System.currentTimeMillis()));
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }
}
