package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
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
import com.zyc.zdh.dao.PermissionUserDimensionValueMapper;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户维度关系信息服务
 */
@Controller
public class PermissionUserDimensionValueController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PermissionUserDimensionValueMapper permissionUserDimensionValueMapper;

    /**
     * 用户维度关系信息列表首页
     * @return
     */
    @RequestMapping(value = "/permission_user_dimension_value_index", method = RequestMethod.GET)
    @White
    public String permission_user_dimension_value_index() {

        return "admin/permission_user_dimension_value_index";
    }

    /**
     * 用户绑定的维度列表
     * @param context 关键字
     * @return
     */
    @RequestMapping(value = "/permission_user_dimension_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<List<PermissionDimensionInfo>> permission_user_dimension_list(String context,String product_code, String user_account) {
        try{

            checkPermissionByOwner(product_code);
            checkParam(user_account, "用户账户");

            Example example=new Example(PermissionUserDimensionValueInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("user_account", user_account);

            List<PermissionDimensionInfo> permissionUserDimensionValueInfos = permissionUserDimensionValueMapper.selectDimByUser(product_code, user_account);

            return ReturnInfo.buildSuccess(permissionUserDimensionValueInfos);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("用户维度关系信息列表查询失败", e);
        }

    }

    /**
     * 用户维度关系信息列表
     * @param context 关键字
     * @return
     */
    @RequestMapping(value = "/permission_user_dimension_value_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<List<PermissionUserDimensionValueInfo>> permission_user_dimension_value_list(String context,String product_code, String user_account, String dim_code) {
        try{

            checkPermissionByOwner(product_code);
            checkParam(user_account, "用户账户");

            Example example=new Example(PermissionUserDimensionValueInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("user_account", user_account);
            criteria.andEqualTo("dim_code", dim_code);

            List<PermissionUserDimensionValueInfo> permissionUserDimensionValueInfos = permissionUserDimensionValueMapper.selectByExample(example);

            return ReturnInfo.buildSuccess(permissionUserDimensionValueInfos);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("用户维度关系信息列表查询失败", e);
        }

    }

    /**
     * 用户维度关系信息新增首页
     * @return
     */
    @RequestMapping(value = "/permission_user_dimension_value_add_index", method = RequestMethod.GET)
    @White
    public String permission_user_dimension_value_add_index() {

        return "admin/permission_user_dimension_value_add_index";
    }

    /**
     * xx明细
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/permission_user_dimension_value_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<PermissionUserDimensionValueInfo> permission_user_dimension_value_detail(String id) {
        try {
            PermissionUserDimensionValueInfo permissionUserDimensionValueInfo = permissionUserDimensionValueMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionUserDimensionValueInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 用户维度关系信息更新
     * @param product_code 产品code
     * @param dim_code 维度code
     * @param user_account 账号
     * @param dim_value_codes 维度值code
     * @return
     */
    @RequestMapping(value = "/permission_user_dimension_value_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo<PermissionUserDimensionValueInfo> permission_user_dimension_value_update(String product_code, String dim_code, String user_account, String[] dim_value_codes) {
        try {

            Example example=new Example(PermissionUserDimensionValueInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("user_account", user_account);
            criteria.andEqualTo("dim_code", dim_code);
            permissionUserDimensionValueMapper.deleteByExample(example);

            PermissionUserDimensionValueInfo permissionUserDimensionValueInfo = new PermissionUserDimensionValueInfo();
            permissionUserDimensionValueInfo.setProduct_code(product_code);
            permissionUserDimensionValueInfo.setDim_code(dim_code);
            permissionUserDimensionValueInfo.setUser_account(user_account);
            permissionUserDimensionValueInfo.setOwner(getOwner());
            permissionUserDimensionValueInfo.setIs_delete(Const.NOT_DELETE);
            permissionUserDimensionValueInfo.setCreate_time(new Timestamp(new Date().getTime()));
            permissionUserDimensionValueInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            for (String dim_value_code: dim_value_codes){
                permissionUserDimensionValueInfo.setDim_value_code(dim_value_code);
                permissionUserDimensionValueMapper.insert(permissionUserDimensionValueInfo);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", permissionUserDimensionValueInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    public User getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

}
