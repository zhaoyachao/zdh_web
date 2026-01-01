package com.zyc.zdh.controller.zdhpush;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.WechatQrsceneMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.WechatQrsceneInfo;
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

/**
 * 微信二维码场景信息表服务
 *
 * 使用权限控制需要WechatQrsceneInfo 继承BaseProductAuthInfo 或者 BaseProductAndDimGroupAuthInfo
 */
@Controller
public class WechatQrsceneController extends BaseController {

    @Autowired
    private WechatQrsceneMapper wechatQrsceneMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 微信二维码场景信息表列表首页
     * @return
     */
    @RequestMapping(value = "/wechat_qrscene_index", method = RequestMethod.GET)
    public String wechat_qrscene_index() {

        return "push/wechat_qrscene_index";
    }

    /**
     * 微信二维码场景信息表列表
     * @param context 关键字
     * @param product_code 产品
     * @param dim_group 归属组
     * @return
     */
    @SentinelResource(value = "wechat_qrscene_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_qrscene_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<WechatQrsceneInfo>> wechat_qrscene_list(String context, String product_code, String dim_group) {
        try{
            Example example=new Example(WechatQrsceneInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
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

            List<WechatQrsceneInfo> wechatQrsceneInfos = wechatQrsceneMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, wechatQrsceneInfos);

            return ReturnInfo.buildSuccess(wechatQrsceneInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信二维码场景信息表列表查询失败", e);
        }

    }


    /**
    * 微信二维码场景信息表分页列表
    * @param context 关键字
    * @param product_code 产品code
    * @param dim_group 归属组code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "wechat_qrscene_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_qrscene_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<WechatQrsceneInfo>>> wechat_qrscene_list_by_page(String context,String product_code, String dim_group, int limit, int offset) {
        try{
            Example example=new Example(WechatQrsceneInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

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
            int total = wechatQrsceneMapper.selectCountByExample(example);

            List<WechatQrsceneInfo> wechatQrsceneInfos = wechatQrsceneMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, wechatQrsceneInfos);

            PageResult<List<WechatQrsceneInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(wechatQrsceneInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("微信二维码场景信息表列表分页查询失败", e);
        }

    }

    /**
     * 微信二维码场景信息表新增首页
     * @return
     */
    @RequestMapping(value = "/wechat_qrscene_add_index", method = RequestMethod.GET)
    public String wechat_qrscene_add_index() {

        return "push/wechat_qrscene_add_index";
    }

    /**
     * 微信二维码场景信息表明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "wechat_qrscene_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_qrscene_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<WechatQrsceneInfo> wechat_qrscene_detail(String id) {
        try {
            WechatQrsceneInfo wechatQrsceneInfo = wechatQrsceneMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService,  wechatQrsceneInfo.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", wechatQrsceneInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 微信二维码场景信息表更新
     * @param wechatQrsceneInfo
     * @return
     */
    @SentinelResource(value = "wechat_qrscene_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_qrscene_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatQrsceneInfo> wechat_qrscene_update(WechatQrsceneInfo wechatQrsceneInfo) {
        try {

            WechatQrsceneInfo oldWechatQrsceneInfo = wechatQrsceneMapper.selectByPrimaryKey(wechatQrsceneInfo.getId());

            checkAttrPermissionByProduct(zdhPermissionService, wechatQrsceneInfo.getProduct_code(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService, oldWechatQrsceneInfo.getProduct_code(), getAttrEdit());


            wechatQrsceneInfo.setCreate_time(oldWechatQrsceneInfo.getCreate_time());
            wechatQrsceneInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            wechatQrsceneInfo.setIs_delete(Const.NOT_DELETE);
            wechatQrsceneMapper.updateByPrimaryKeySelective(wechatQrsceneInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", wechatQrsceneInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 微信二维码场景信息表新增
     * @param wechatQrsceneInfo
     * @return
     */
    @SentinelResource(value = "wechat_qrscene_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_qrscene_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<WechatQrsceneInfo> wechat_qrscene_add(WechatQrsceneInfo wechatQrsceneInfo) {
        try {
            wechatQrsceneInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            wechatQrsceneInfo.setOwner(getOwner());
            wechatQrsceneInfo.setIs_delete(Const.NOT_DELETE);
            wechatQrsceneInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            wechatQrsceneInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProduct(zdhPermissionService, wechatQrsceneInfo.getProduct_code(), getAttrAdd());
            wechatQrsceneMapper.insertSelective(wechatQrsceneInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", wechatQrsceneInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 微信二维码场景信息表删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "wechat_qrscene_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/wechat_qrscene_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo wechat_qrscene_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, wechatQrsceneMapper, wechatQrsceneMapper.getTable(), ids, getAttrDel());
            wechatQrsceneMapper.deleteLogicByIds(wechatQrsceneMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }
}
