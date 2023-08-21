package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.entity.PermissionUserGroupDimensionValueInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.User;
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
import com.zyc.zdh.dao.PermissionUserGroupDimensionValueMapper;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 用户组维度关系信息服务
 */
@Controller
public class PermissionUserGroupDimensionValueController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PermissionUserGroupDimensionValueMapper permissionUserGroupDimensionValueMapper;

    /**
     * 用户组维度关系信息列表首页
     * @return
     */
    @RequestMapping(value = "/permission_user_group_dimension_value_index", method = RequestMethod.GET)
    @White
    public String permission_user_group_dimension_value_index() {

        return "permission_user_group_dimension_value_index";
    }

    /**
     * 用户组维度关系信息列表
     * @param context 关键字
     * @return
     */
    @RequestMapping(value = "/permission_user_group_dimension_value_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<List<PermissionUserGroupDimensionValueInfo>> permission_user_group_dimension_value_list(String context) {
        try{
            Example example=new Example(PermissionUserGroupDimensionValueInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
            criteria2.orLike("context", getLikeCondition(context));
            }
            example.and(criteria2);

            List<PermissionUserGroupDimensionValueInfo> permissionUserGroupDimensionValueInfos = permissionUserGroupDimensionValueMapper.selectByExample(example);

            return ReturnInfo.buildSuccess(permissionUserGroupDimensionValueInfos);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("用户组维度关系信息列表查询失败", e);
        }

    }

    /**
     * 用户组维度关系信息新增首页
     * @return
     */
    @RequestMapping(value = "/permission_user_group_dimension_value_add_index", method = RequestMethod.GET)
    @White
    public String permission_user_group_dimension_value_add_index() {

        return "permission_user_group_dimension_value_add_index";
    }

    /**
     * 用户组维度关系信息明细页面
     * @return
     */
    @RequestMapping(value = "/permission_user_group_dimension_value_detail", method = RequestMethod.GET)
    @White
    public String permission_user_group_dimension_value_detail() {

        return "permission_user_group_dimension_value_detail";
    }
    /**
     * xx明细
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/permission_user_group_dimension_value_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<PermissionUserGroupDimensionValueInfo> permission_user_group_dimension_value_detail(String id) {
        try {
            PermissionUserGroupDimensionValueInfo permissionUserGroupDimensionValueInfo = permissionUserGroupDimensionValueMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionUserGroupDimensionValueInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 用户组维度关系信息更新
     * @param permissionUserGroupDimensionValueInfo
     * @return
     */
    @RequestMapping(value = "/permission_user_group_dimension_value_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo<PermissionUserGroupDimensionValueInfo> permission_user_group_dimension_value_update(PermissionUserGroupDimensionValueInfo permissionUserGroupDimensionValueInfo) {
        try {

            PermissionUserGroupDimensionValueInfo oldPermissionUserGroupDimensionValueInfo = permissionUserGroupDimensionValueMapper.selectByPrimaryKey(permissionUserGroupDimensionValueInfo.getId());


            permissionUserGroupDimensionValueInfo.setCreate_time(oldPermissionUserGroupDimensionValueInfo.getCreate_time());
            permissionUserGroupDimensionValueInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            permissionUserGroupDimensionValueInfo.setIs_delete(Const.NOT_DELETE);
            permissionUserGroupDimensionValueMapper.updateByPrimaryKey(permissionUserGroupDimensionValueInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", permissionUserGroupDimensionValueInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 用户组维度关系信息新增
     * @param permissionUserGroupDimensionValueInfo
     * @return
     */
    @RequestMapping(value = "/permission_user_group_dimension_value_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo<PermissionUserGroupDimensionValueInfo> permission_user_group_dimension_value_add(PermissionUserGroupDimensionValueInfo permissionUserGroupDimensionValueInfo) {
        try {
            permissionUserGroupDimensionValueInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            permissionUserGroupDimensionValueInfo.setOwner(getOwner());
            permissionUserGroupDimensionValueInfo.setIs_delete(Const.NOT_DELETE);
            permissionUserGroupDimensionValueInfo.setCreate_time(new Timestamp(new Date().getTime()));
            permissionUserGroupDimensionValueInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            permissionUserGroupDimensionValueMapper.insert(permissionUserGroupDimensionValueInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", permissionUserGroupDimensionValueInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 用户组维度关系信息删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/permission_user_group_dimension_value_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo permission_user_group_dimension_value_delete(String[] ids) {
        try {
            permissionUserGroupDimensionValueMapper.deleteLogicByIds("permission_usergroup_dimension_value_info",ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    public User getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

}
