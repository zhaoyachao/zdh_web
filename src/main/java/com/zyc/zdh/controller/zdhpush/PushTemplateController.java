package com.zyc.zdh.controller.zdhpush;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.PushTemplateMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.PushTemplateInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
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
 * push模板配置服务
 */
@Controller
public class PushTemplateController extends BaseController {

    @Autowired
    private PushTemplateMapper pushTemplateMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * push模板配置列表首页
     * @return
     */
    @RequestMapping(value = "/push_template_index", method = RequestMethod.GET)
    @White
    public String push_template_index() {

        return "push/push_template_index";
    }

    /**
     * push模板配置列表
     * @param context 关键字
     * @param product_code 产品
     * @param dim_group 归属组
     * @return
     */
    @SentinelResource(value = "push_template_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_template_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<List<PushTemplateInfo>> push_template_list(String context, String product_code, String dim_group) {
        try{
            Example example=new Example(PushTemplateInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);

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

            List<PushTemplateInfo> pushTemplateInfos = pushTemplateMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, pushTemplateInfos);

            return ReturnInfo.buildSuccess(pushTemplateInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("push模板配置列表查询失败", e);
        }

    }


    /**
     * push模板配置分页列表
     * @param context 关键字
     * @param product_code 产品code
     * @param dim_group 归属组code
     * @param limit 分页大小
     * @param offset 分页下标
     * @return
     */
    @SentinelResource(value = "push_template_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_template_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<PageResult<List<PushTemplateInfo>>> push_template_list_by_page(String context,String product_code, String dim_group, int limit, int offset) {
        try{
            Example example=new Example(PushTemplateInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);

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
            int total = pushTemplateMapper.selectCountByExample(example);

            List<PushTemplateInfo> pushTemplateInfos = pushTemplateMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, pushTemplateInfos);

            PageResult<List<PushTemplateInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(pushTemplateInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("push模板配置列表分页查询失败", e);
        }

    }

    /**
     * push模板配置新增首页
     * @return
     */
    @RequestMapping(value = "/push_template_add_index", method = RequestMethod.GET)
    @White
    public String push_template_add_index() {

        return "push/push_template_add_index";
    }

    /**
     * xx明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "push_template_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_template_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<PushTemplateInfo> push_template_detail(String id) {
        try {
            PushTemplateInfo pushTemplateInfo = pushTemplateMapper.selectByPrimaryKey(id);
            checkPermissionByProductAndDimGroup(zdhPermissionService, pushTemplateInfo.getProduct_code(), pushTemplateInfo.getDim_group());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pushTemplateInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * push模板配置更新
     * @param pushTemplateInfo
     * @return
     */
    @SentinelResource(value = "push_template_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_template_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo<PushTemplateInfo> push_template_update(PushTemplateInfo pushTemplateInfo) {
        try {

            PushTemplateInfo oldPushTemplateInfo = pushTemplateMapper.selectByPrimaryKey(pushTemplateInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pushTemplateInfo.getProduct_code(), pushTemplateInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldPushTemplateInfo.getProduct_code(), oldPushTemplateInfo.getDim_group(), getAttrEdit());

            pushTemplateInfo.setCreate_time(oldPushTemplateInfo.getCreate_time());
            pushTemplateInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            pushTemplateInfo.setIs_delete(Const.NOT_DELETE);
            pushTemplateMapper.updateByPrimaryKeySelective(pushTemplateInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", pushTemplateInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * push模板配置新增
     * @param pushTemplateInfo
     * @return
     */
    @SentinelResource(value = "push_template_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_template_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo<PushTemplateInfo> push_template_add(PushTemplateInfo pushTemplateInfo) {
        try {
            pushTemplateInfo.setOwner(getOwner());
            pushTemplateInfo.setIs_delete(Const.NOT_DELETE);
            pushTemplateInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            pushTemplateInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pushTemplateInfo.getProduct_code(), pushTemplateInfo.getDim_group(), getAttrAdd());
            pushTemplateMapper.insertSelective(pushTemplateInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", pushTemplateInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * push模板配置删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "push_template_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_template_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo push_template_delete(String[] ids) {
        try {
            checkPermissionByProductAndDimGroup(zdhPermissionService, pushTemplateMapper, pushTemplateMapper.getTable(), ids);
            pushTemplateMapper.deleteLogicByIds(pushTemplateMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    /**
     * push模板配置新增版本
     * @param id
     * @return
     */
    @SentinelResource(value = "push_template_add_version", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_template_add_version", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo<PushTemplateInfo> push_template_add_version(String id) {
        try {
            PushTemplateInfo pushTemplateInfo = pushTemplateMapper.selectByPrimaryKey(id);

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pushTemplateInfo.getProduct_code(), pushTemplateInfo.getDim_group(), getAttrAdd());

            pushTemplateInfo.setId(null);
            pushTemplateInfo.setIs_delete(Const.NOT_DELETE);
            pushTemplateInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            pushTemplateInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            pushTemplateMapper.insertSelective(pushTemplateInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", pushTemplateInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

}
