package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.PermissionUserGroupDimensionValueMapper;
import com.zyc.zdh.entity.PermissionDimensionInfo;
import com.zyc.zdh.entity.PermissionUserGroupDimensionValueInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.util.Const;
import org.apache.commons.lang3.StringUtils;
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
    @RequestMapping(value = "/permission_usergroup_dimension_value_index", method = RequestMethod.GET)
    public String permission_usergroup_dimension_value_index() {

        return "admin/permission_usergroup_dimension_value_index";
    }


    /**
     * 用户组绑定的维度列表
     * @param context 关键字
     * @return
     */
    @SentinelResource(value = "permission_usergroup_dimension_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_usergroup_dimension_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionDimensionInfo>> permission_usergroup_dimension_list(String context, String product_code, String group_code) {
        try{

            checkPermissionByOwner(product_code);
            checkParam(group_code, "用户组");

            Example example=new Example(PermissionUserGroupDimensionValueInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("group_code", group_code);

            List<PermissionDimensionInfo> permissionUserDimensionValueInfos = permissionUserGroupDimensionValueMapper.selectDimByGroup(product_code, group_code);

            return ReturnInfo.buildSuccess(permissionUserDimensionValueInfos);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("用户维度关系信息列表查询失败", e);
        }

    }

    /**
     * 用户组维度关系信息列表
     * @param context 关键字
     * @return
     */
    @SentinelResource(value = "permission_usergroup_dimension_value_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_usergroup_dimension_value_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionUserGroupDimensionValueInfo>> permission_usergroup_dimension_value_list(String context,String product_code, String group_code, String dim_code) {
        try{
            checkPermissionByOwner(product_code);
            checkParam(group_code, "用户组");


            Example example=new Example(PermissionUserGroupDimensionValueInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("group_code", group_code);
            criteria.andEqualTo("dim_code", dim_code);
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
    @RequestMapping(value = "/permission_usergroup_dimension_value_add_index", method = RequestMethod.GET)
    public String permission_usergroup_dimension_value_add_index() {

        return "admin/permission_usergroup_dimension_value_add_index";
    }

    /**
     * 用户组维度关系信息更新
     * @param product_code 产品code
     * @param dim_code 维度code
     * @param group_code 用户组
     * @param dim_value_codes 维度值code
     * @return
     */
    @SentinelResource(value = "permission_usergroup_dimension_value_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_usergroup_dimension_value_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PermissionUserGroupDimensionValueInfo> permission_usergroup_dimension_value_update(String product_code, String dim_code, String group_code, String[] dim_value_codes) {
        try {
            Example example=new Example(PermissionUserGroupDimensionValueInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("group_code", group_code);
            criteria.andEqualTo("dim_code", dim_code);
            permissionUserGroupDimensionValueMapper.deleteByExample(example);

            PermissionUserGroupDimensionValueInfo permissionUserDimensionValueInfo = new PermissionUserGroupDimensionValueInfo();
            permissionUserDimensionValueInfo.setProduct_code(product_code);
            permissionUserDimensionValueInfo.setDim_code(dim_code);
            permissionUserDimensionValueInfo.setGroup_code(group_code);
            permissionUserDimensionValueInfo.setOwner(getOwner());
            permissionUserDimensionValueInfo.setIs_delete(Const.NOT_DELETE);
            permissionUserDimensionValueInfo.setCreate_time(new Timestamp(new Date().getTime()));
            permissionUserDimensionValueInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            for (String dim_value_code: dim_value_codes){
                permissionUserDimensionValueInfo.setDim_value_code(dim_value_code);
                permissionUserGroupDimensionValueMapper.insertSelective(permissionUserDimensionValueInfo);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", permissionUserDimensionValueInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

}
