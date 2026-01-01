package com.zyc.zdh.controller.zdhpush;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.WechatQrcodeMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.WechatQrcodeInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.pushx.PushxWechatQrcodeService;
import com.zyc.zdh.pushx.entity.WechatQrcodeResponse;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.ImageUtils;
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
import org.springframework.web.multipart.MultipartFile;
import tk.mybatis.mapper.entity.Example;

import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.List;

/**
 * 微信二维码信息表服务
 *
 * 使用权限控制需要WechatQrcodeInfo 继承BaseProductAuthInfo 或者 BaseProductAndDimGroupAuthInfo
 *
 * 目前有2种类型的二维码：
 *
 *     临时二维码，是有过期时间的，最长可以设置为在二维码生成后的30天（即2592000秒）后过期，但能够生成较多数量。临时二维码主要用于账号绑定等不要求二维码永久保存的业务场景。
 *     永久二维码，是无过期时间的，但数量较少（目前为最多10万个）。永久二维码主要用于适用于账号绑定、用户来源统计等场景。
 *
 * 用户扫描带场景值二维码时，可能推送以下两种事件：
 *
 *     如果用户还未关注公众号，则用户可以关注公众号，关注后微信会将带场景值关注事件推送给开发者。
 *     如果用户已经关注公众号，在用户扫描后会自动进入会话，微信也会将带场景值扫描事件推送给开发者。
 * 微信接口文档： https://developers.weixin.qq.com/doc/service/new.html
 */
@Controller
public class WechatQrcodeController extends BaseController {

    @Autowired
    private WechatQrcodeMapper wechatQrcodeMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;
    @Autowired
    private PushxWechatQrcodeService pushxWechatQrcodeService;

    /**
     * 微信二维码信息表列表首页
     * @return
     */
    @RequestMapping(value = "/wechat_qrcode_index", method = RequestMethod.GET)
    public String wechat_qrcode_index() {

        return "push/wechat_qrcode_index";
    }

    /**
     * 微信二维码信息表列表
     * @param context 关键字
     * @param product_code 产品
     * @return
     */
    @SentinelResource(value = "wechat_qrcode_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_qrcode_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<WechatQrcodeInfo>> wechat_qrcode_list(String context, String product_code) {
        try{
            Example example=new Example(WechatQrcodeInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            List<WechatQrcodeInfo> wechatQrcodeInfos = wechatQrcodeMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, wechatQrcodeInfos);

            return ReturnInfo.buildSuccess(wechatQrcodeInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信二维码信息表列表查询失败", e);
        }

    }


    /**
    * 微信二维码信息表分页列表
    * @param context 关键字
    * @param product_code 产品code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "wechat_qrcode_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_qrcode_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<WechatQrcodeInfo>>> wechat_qrcode_list_by_page(String context,String product_code, int limit, int offset) {
        try{
            Example example=new Example(WechatQrcodeInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

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
            int total = wechatQrcodeMapper.selectCountByExample(example);

            List<WechatQrcodeInfo> wechatQrcodeInfos = wechatQrcodeMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, wechatQrcodeInfos);

            PageResult<List<WechatQrcodeInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(wechatQrcodeInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信二维码信息表列表分页查询失败", e);
        }

    }

    /**
     * 微信二维码信息表新增首页
     * @return
     */
    @RequestMapping(value = "/wechat_qrcode_add_index", method = RequestMethod.GET)
    public String wechat_qrcode_add_index() {

        return "push/wechat_qrcode_add_index";
    }

    /**
     * 自定义二维码页面
     * @return
     */
    @RequestMapping(value = "/wechat_qrcode_custom_index", method = RequestMethod.GET)
    public String wechat_qrcode_custom_index() {

        return "push/wechat_qrcode_custom_index";
    }

    /**
     * 自定义二维码添加
     * @param id
     * @return
     */
    @SentinelResource(value = "wechat_qrcode_custom_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_qrcode_custom_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatQrcodeInfo> wechat_qrcode_custom_add(String id, MultipartFile jar_files) {
        try {
            WechatQrcodeInfo wechatQrcodeInfo = wechatQrcodeMapper.selectByPrimaryKey(id);

            checkAttrPermissionByProduct(zdhPermissionService, wechatQrcodeInfo.getProduct_code(), getAttrEdit());

            String imageBase64 = ImageUtils.fileToBase64(jar_files);
            if(StringUtils.isEmpty(imageBase64)){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "图片转换失败", null);
            }
            wechatQrcodeInfo.setQrcode_custom_image(imageBase64);
            wechatQrcodeInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            wechatQrcodeMapper.updateByPrimaryKeySelective(wechatQrcodeInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", wechatQrcodeInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 微信二维码信息表明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "wechat_qrcode_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_qrcode_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<WechatQrcodeInfo> wechat_qrcode_detail(String id) {
        try {
            WechatQrcodeInfo wechatQrcodeInfo = wechatQrcodeMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService,  wechatQrcodeInfo.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", wechatQrcodeInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 微信二维码信息表更新
     * @param wechatQrcodeInfo
     * @return
     */
    @SentinelResource(value = "wechat_qrcode_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_qrcode_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatQrcodeInfo> wechat_qrcode_update(WechatQrcodeInfo wechatQrcodeInfo) {
        try {

            WechatQrcodeInfo oldWechatQrcodeInfo = wechatQrcodeMapper.selectByPrimaryKey(wechatQrcodeInfo.getId());

            checkAttrPermissionByProduct(zdhPermissionService, wechatQrcodeInfo.getProduct_code(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService, oldWechatQrcodeInfo.getProduct_code(), getAttrEdit());


            wechatQrcodeInfo.setCreate_time(oldWechatQrcodeInfo.getCreate_time());
            wechatQrcodeInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            wechatQrcodeInfo.setIs_delete(Const.NOT_DELETE);
            wechatQrcodeMapper.updateByPrimaryKeySelective(wechatQrcodeInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", wechatQrcodeInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 微信二维码信息表新增
     * @param wechatQrcodeInfo
     * @return
     */
    @SentinelResource(value = "wechat_qrcode_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_qrcode_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatQrcodeInfo> wechat_qrcode_add(WechatQrcodeInfo wechatQrcodeInfo) {
        try {
            wechatQrcodeInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            wechatQrcodeInfo.setOwner(getOwner());
            wechatQrcodeInfo.setIs_delete(Const.NOT_DELETE);
            wechatQrcodeInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            wechatQrcodeInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProduct(zdhPermissionService, wechatQrcodeInfo.getProduct_code(), getAttrAdd());
            wechatQrcodeMapper.insertSelective(wechatQrcodeInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", wechatQrcodeInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 微信二维码信息表删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "wechat_qrcode_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_qrcode_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo wechat_qrcode_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatQrcodeMapper, wechatQrcodeMapper.getTable(), ids, getAttrDel());
            wechatQrcodeMapper.deleteLogicByIds(wechatQrcodeMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    /**
     * 微信端-生成二维码
     * @param id
     * @return
     */
    @SentinelResource(value = "wechat_qrcode_cloud_edit", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_qrcode_cloud_edit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo wechat_qrcode_cloud_edit(String id) {
        try {
            WechatQrcodeInfo wechatQrcodeInfo = wechatQrcodeMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService,  wechatQrcodeInfo.getProduct_code(), getAttrSelect());

            WechatQrcodeResponse qrcodeResponse = pushxWechatQrcodeService.createQrcode(wechatQrcodeInfo);

            if(qrcodeResponse.isSuccess()){
                if(qrcodeResponse.getData().getExpire_seconds() != null){
                    wechatQrcodeInfo.setExpire_seconds(String.valueOf(qrcodeResponse.getData().getExpire_seconds()));
                }
                wechatQrcodeInfo.setTicket(qrcodeResponse.getData().getTicket());
                wechatQrcodeInfo.setUrl(qrcodeResponse.getData().getUrl());
                String encodedTicket = URLEncoder.encode(qrcodeResponse.getData().getTicket(), "UTF-8").replaceAll("\\+", "%20");
                String imageUrl = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=" + encodedTicket;
                String image = ImageUtils.urlToBase64(imageUrl);
                System.out.println("image: " + image);
                wechatQrcodeInfo.setQrcode_image(image);
                wechatQrcodeInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                wechatQrcodeMapper.updateByPrimaryKeySelective(wechatQrcodeInfo);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    /**
     * 微信端-更新二维码规则
     * @param id
     * @return
     */
    @SentinelResource(value = "wechat_qrcode_cloud_app_edit", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_qrcode_cloud_app_edit", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo wechat_qrcode_cloud_app_edit(String id) {
        try {
            WechatQrcodeInfo wechatQrcodeInfo = wechatQrcodeMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService,  wechatQrcodeInfo.getProduct_code(), getAttrSelect());

            //判断状态是否可以修改规则1:生成二维码,2:规则创建,3:规则发布
            if(wechatQrcodeInfo.getStatus().equals("3")){
                throw new Exception("二维码规则已发布，无法修改");
            }

            WechatQrcodeResponse qrcodeResponse = pushxWechatQrcodeService.qrcodeJumpAdd(wechatQrcodeInfo);

            if(qrcodeResponse.isSuccess()){
                wechatQrcodeInfo.setStatus("2");
                wechatQrcodeInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                wechatQrcodeMapper.updateByPrimaryKeySelective(wechatQrcodeInfo);
                return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新建成功", null);
            }
            throw new Exception("新建失败: "+qrcodeResponse.getMsg());
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新建失败", e.getMessage());
        }
    }


    /**
     * 微信端-二维码规则发布
     * @param id
     * @return
     */
    @SentinelResource(value = "wechat_qrcode_cloud_app_publish", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_qrcode_cloud_app_publish", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo wechat_qrcode_cloud_app_publish(String id) {
        try {
            WechatQrcodeInfo wechatQrcodeInfo = wechatQrcodeMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService,  wechatQrcodeInfo.getProduct_code(), getAttrSelect());

            WechatQrcodeResponse qrcodeResponse = pushxWechatQrcodeService.qrcodeJumpPublish(wechatQrcodeInfo);

            if(qrcodeResponse.isSuccess()){
                wechatQrcodeInfo.setStatus("3");
                wechatQrcodeInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                wechatQrcodeMapper.updateByPrimaryKeySelective(wechatQrcodeInfo);
                return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "发布成功", null);
            }
            throw new Exception("发布失败: "+qrcodeResponse.getMsg());
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "发布失败", e.getMessage());
        }
    }
}
