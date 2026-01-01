package com.zyc.zdh.controller.zdhpush;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.WechatMediaInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.User;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.pushx.PushxWechatMediaService;
import com.zyc.zdh.pushx.entity.WechatMediaResponse;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.ImageUtils;
import com.zyc.zdh.util.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.SecurityUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;
import com.zyc.zdh.dao.WechatMediaMapper;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 微信素材表服务
 *
 * 使用权限控制需要WechatMediaInfo 继承BaseProductAuthInfo 或者 BaseProductAndDimGroupAuthInfo
 */
@Slf4j
@Controller
public class WechatMediaController extends BaseController {

    @Autowired
    private WechatMediaMapper wechatMediaMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;
    @Autowired
    private PushxWechatMediaService pushxWechatMediaService;

    /**
     * 微信素材表列表首页
     * @return
     */
    @RequestMapping(value = "/wechat_media_index", method = RequestMethod.GET)
    public String wechat_media_index() {

        return "push/wechat_media_index";
    }

    /**
     * 微信素材表列表
     * @param context 关键字
     * @param product_code 产品
     * @param dim_group 归属组
     * @return
     */
    @SentinelResource(value = "wechat_media_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_media_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<WechatMediaInfo>> wechat_media_list(String context, String product_code, String dim_group) {
        try{
            Example example=new Example(WechatMediaInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            //dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);
            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }
            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            List<WechatMediaInfo> wechatMediaInfos = wechatMediaMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, wechatMediaInfos);

            return ReturnInfo.buildSuccess(wechatMediaInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信素材表列表查询失败", e);
        }

    }


    /**
    * 微信素材表分页列表
    * @param context 关键字
    * @param product_code 产品code
    * @param dim_group 归属组code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "wechat_media_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_media_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<WechatMediaInfo>>> wechat_media_list_by_page(String context,String product_code, String dim_group, int limit, int offset) {
        try{
            Example example=new Example(WechatMediaInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            //dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);
            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = wechatMediaMapper.selectCountByExample(example);

            List<WechatMediaInfo> wechatMediaInfos = wechatMediaMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, wechatMediaInfos);

            PageResult<List<WechatMediaInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(wechatMediaInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信素材表列表分页查询失败", e);
        }

    }

    /**
     * 微信素材表新增首页
     * @return
     */
    @RequestMapping(value = "/wechat_media_add_index", method = RequestMethod.GET)
    public String wechat_media_add_index() {

        return "push/wechat_media_add_index";
    }

    /**
     * 微信素材表明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "wechat_media_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_media_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<WechatMediaInfo> wechat_media_detail(String id) {
        try {
            WechatMediaInfo wechatMediaInfo = wechatMediaMapper.selectByPrimaryKey(id);
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService,  wechatMediaInfo.getProduct_code(), wechatMediaInfo.getDim_group(), getAttrSelect());
            checkAttrPermissionByProduct(zdhPermissionService,  wechatMediaInfo.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", wechatMediaInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 微信素材表更新
     * @param wechatMediaInfo
     * @return
     */
    @SentinelResource(value = "wechat_media_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_media_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatMediaInfo> wechat_media_update(WechatMediaInfo wechatMediaInfo) {
        try {

            WechatMediaInfo oldWechatMediaInfo = wechatMediaMapper.selectByPrimaryKey(wechatMediaInfo.getId());

            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatMediaInfo.getProduct_code(), wechatMediaInfo.getDim_group(), getAttrEdit());
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldWechatMediaInfo.getProduct_code(), oldWechatMediaInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService, wechatMediaInfo.getProduct_code(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService, oldWechatMediaInfo.getProduct_code(), getAttrEdit());

            oldWechatMediaInfo.setMedia_name(wechatMediaInfo.getMedia_name());
            oldWechatMediaInfo.setMedia_desc(wechatMediaInfo.getMedia_desc());

            oldWechatMediaInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            wechatMediaInfo.setIs_delete(Const.NOT_DELETE);
            wechatMediaMapper.updateByPrimaryKeySelective(wechatMediaInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", wechatMediaInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 微信素材表新增
     * @param wechatMediaInfo
     * @return
     */
    @SentinelResource(value = "wechat_media_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_media_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatMediaInfo> wechat_media_add(WechatMediaInfo wechatMediaInfo, MultipartFile jar_files) {
        try {
            wechatMediaInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            wechatMediaInfo.setOwner(getOwner());
            wechatMediaInfo.setIs_delete(Const.NOT_DELETE);
            wechatMediaInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            wechatMediaInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            String base64Str = ImageUtils.fileToBase64(jar_files, true);
            wechatMediaInfo.setMedia_str(base64Str);

            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatMediaInfo.getProduct_code(), wechatMediaInfo.getDim_group(), getAttrAdd());
            checkAttrPermissionByProduct(zdhPermissionService, wechatMediaInfo.getProduct_code(), getAttrAdd());

            //调用微信素材上传接口
            WechatMediaResponse mediaResponse = pushxWechatMediaService.addWechatMedia(wechatMediaInfo);
            if(mediaResponse.isSuccess()){
                if(!StringUtils.isEmpty(mediaResponse.getData().getUrl())){
                    wechatMediaInfo.setUrl(mediaResponse.getData().getUrl());
                }
                if(!StringUtils.isEmpty(mediaResponse.getData().getMedia_id())){
                    wechatMediaInfo.setMedia_id(mediaResponse.getData().getMedia_id());
                }
                if(!StringUtils.isEmpty(mediaResponse.getData().getCreated_at())){
                    wechatMediaInfo.setValid_start_time(mediaResponse.getData().getCreated_at());
                    wechatMediaInfo.setValid_end_time(Long.valueOf(mediaResponse.getData().getCreated_at()) +3*24*60*60 + "");
                }


                wechatMediaMapper.insertSelective(wechatMediaInfo);
                return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", wechatMediaInfo);
            }
            throw new Exception("微信素材上传失败, 微信失败码: "+mediaResponse.getCode()+", 微信失败信息: "+mediaResponse.getMsg());
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 微信素材表删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "wechat_media_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_media_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo wechat_media_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatMediaMapper, wechatMediaMapper.getTable(), ids, getAttrDel());

            List<WechatMediaInfo> wechatMediaInfos = wechatMediaMapper.selectObjectByIds(wechatMediaMapper.getTable(), ids);
            for (WechatMediaInfo wechatMediaInfo : wechatMediaInfos) {
                if(wechatMediaInfo.getMedia_category().equals("1")){
                    //永久素材删除
                    WechatMediaResponse mediaResponse = pushxWechatMediaService.deleteWechatMedia(wechatMediaInfo);
                    if(!mediaResponse.isSuccess()) {
                        throw new Exception("微信素材删除失败, 微信失败码: " + mediaResponse.getCode() + ", 微信失败信息: " + mediaResponse.getMsg());
                    }
                }
                wechatMediaMapper.deleteLogicByIds(wechatMediaMapper.getTable(),new String[]{wechatMediaInfo.getId()}, new Timestamp(System.currentTimeMillis()));
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }
}
