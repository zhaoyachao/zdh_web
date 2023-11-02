package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.PermissionDimensionMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.PermissionDimensionInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.util.Const;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Date;
import java.util.List;

/**
 * 维度信息服务
 */
@Controller
public class PermissionDimensionController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PermissionDimensionMapper permissionDimensionMapper;

    /**
     * 维度信息列表首页
     * @return
     */
    @RequestMapping(value = "/permission_dimension_index", method = RequestMethod.GET)
    public String permission_dimension_index() {

        return "admin/permission_dimension_index";
    }

    /**
     * 根据产品code查询维度列表
     * @param product_code 产品code
     * @return
     */
    @SentinelResource(value = "permission_dimension_list_by_product_code", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_dimension_list_by_product_code", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionDimensionInfo>> permission_dimension_list_by_product_code(String product_code) {
        try{

            Example example=new Example(PermissionDimensionInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            example.setDistinct(true);
            example.selectProperties("dim_code","dim_name");
            List<PermissionDimensionInfo> permissionDimensionInfos = permissionDimensionMapper.selectByExample(example);
            return ReturnInfo.buildSuccess(permissionDimensionInfos);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("维度信息列表查询失败", e);
        }

    }


    /**
     * 维度信息列表
     * @param context 关键字
     * @return
     */
    @SentinelResource(value = "permission_dimension_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_dimension_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<PermissionDimensionInfo>>> permission_dimension_list(String context,String product_code,int limit, int offset) {
        try{

            checkPermissionByOwner(product_code);

            Example example=new Example(PermissionDimensionInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
            criteria2.orLike("context", getLikeCondition(context));
            }
            example.and(criteria2);
            example.setOrderByClause("update_time desc");
            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = permissionDimensionMapper.selectCountByExample(example);

            List<PermissionDimensionInfo> permissionDimensionInfos = permissionDimensionMapper.selectByExampleAndRowBounds(example, rowBounds);

            PageResult<List<PermissionDimensionInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(permissionDimensionInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("维度信息列表查询失败", e);
        }

    }

    /**
     * 维度信息新增首页
     * @return
     */
    @RequestMapping(value = "/permission_dimension_add_index", method = RequestMethod.GET)
    public String permission_dimension_add_index() {

        return "admin/permission_dimension_add_index";
    }

    /**
     * 维度信息明细页面
     * @return
     */
    @RequestMapping(value = "/permission_dimension_detail", method = RequestMethod.GET)
    public String permission_dimension_detail() {

        return "admin/permission_dimension_detail";
    }
    /**
     * xx明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "permission_dimension_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_dimension_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PermissionDimensionInfo> permission_dimension_detail(String id) {
        try {
            PermissionDimensionInfo permissionDimensionInfo = permissionDimensionMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionDimensionInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 维度信息更新
     * @param permissionDimensionInfo
     * @return
     */
    @SentinelResource(value = "permission_dimension_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_dimension_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PermissionDimensionInfo> permission_dimension_update(PermissionDimensionInfo permissionDimensionInfo) {
        try {

            PermissionDimensionInfo oldPermissionDimensionInfo = permissionDimensionMapper.selectByPrimaryKey(permissionDimensionInfo.getId());

            permissionDimensionInfo.setCreate_time(oldPermissionDimensionInfo.getCreate_time());
            permissionDimensionInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            permissionDimensionInfo.setIs_delete(Const.NOT_DELETE);
            permissionDimensionMapper.updateByPrimaryKeySelective(permissionDimensionInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", permissionDimensionInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 维度信息新增
     * @param permissionDimensionInfo
     * @return
     */
    @SentinelResource(value = "permission_dimension_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_dimension_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PermissionDimensionInfo> permission_dimension_add(PermissionDimensionInfo permissionDimensionInfo) {
        try {
            permissionDimensionInfo.setOwner(getOwner());
            permissionDimensionInfo.setIs_delete(Const.NOT_DELETE);
            permissionDimensionInfo.setCreate_time(new Timestamp(new Date().getTime()));
            permissionDimensionInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            permissionDimensionMapper.insertSelective(permissionDimensionInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", permissionDimensionInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 维度信息删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "permission_dimension_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_dimension_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo permission_dimension_delete(String[] ids) {
        try {
            permissionDimensionMapper.deleteLogicByIds("permission_dimension_info",ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

}
