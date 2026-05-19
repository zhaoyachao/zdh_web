package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.google.common.collect.Sets;
import com.zyc.zdh.dao.PermissionDimensionMapper;
import com.zyc.zdh.dao.PermissionUserGroupDimensionValueMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户组维度关系信息服务
 */
@Controller
public class PermissionUserGroupDimensionValueController extends BaseController {

    @Autowired
    private PermissionUserGroupDimensionValueMapper permissionUserGroupDimensionValueMapper;
    @Autowired
    private PermissionDimensionMapper permissionDimensionMapper;

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
            LogUtil.error(this.getClass(), e);
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
                example.and(criteria2);
            }

            List<PermissionUserGroupDimensionValueInfo> permissionUserGroupDimensionValueInfos = permissionUserGroupDimensionValueMapper.selectByExample(example);

            return ReturnInfo.buildSuccess(permissionUserGroupDimensionValueInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
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
            checkPermissionByOwner(product_code);

            Example example=new Example(PermissionUserGroupDimensionValueInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("group_code", group_code);
            criteria.andEqualTo("dim_code", dim_code);


            if(dim_value_codes==null && dim_value_codes.length==0){
                permissionUserGroupDimensionValueMapper.deleteByPrimaryKey(example);
                return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
            }


            List<PermissionUserGroupDimensionValueInfo> permissionUserGroupDimensionValueInfos = permissionUserGroupDimensionValueMapper.selectByExample(example);

            List<PermissionUserGroupDimensionValueInfo> updatePermissionUserGroupDimensionValueInfos = new ArrayList<>();
            List<PermissionUserGroupDimensionValueInfo> delPermissionUserGroupDimensionValueInfos = new ArrayList<>();

            Set<String> dim_value_code_set = Sets.newHashSet(dim_value_codes);
            for(PermissionUserGroupDimensionValueInfo permissionUserGroupDimensionValueInfo: permissionUserGroupDimensionValueInfos){
                if(dim_value_code_set.contains(permissionUserGroupDimensionValueInfo.getDim_value_code())){
                    updatePermissionUserGroupDimensionValueInfos.add(permissionUserGroupDimensionValueInfo);
                    dim_value_code_set.remove(permissionUserGroupDimensionValueInfo.getDim_value_code());
                }else{
                    delPermissionUserGroupDimensionValueInfos.add(permissionUserGroupDimensionValueInfo);
                    dim_value_code_set.remove(permissionUserGroupDimensionValueInfo.getDim_value_code());
                }
            }


            if(dim_value_code_set.size()>0){
                for(String dim_value_code: dim_value_code_set){
                    PermissionUserGroupDimensionValueInfo permissionUserGroupDimensionValueInfo = new PermissionUserGroupDimensionValueInfo();
                    permissionUserGroupDimensionValueInfo.setProduct_code(product_code);
                    permissionUserGroupDimensionValueInfo.setDim_code(dim_code);
                    permissionUserGroupDimensionValueInfo.setGroup_code(group_code);
                    permissionUserGroupDimensionValueInfo.setDim_value_code(dim_value_code);
                    permissionUserGroupDimensionValueInfo.setOwner(getOwner());
                    permissionUserGroupDimensionValueInfo.setIs_delete(Const.NOT_DELETE);
                    permissionUserGroupDimensionValueInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
                    permissionUserGroupDimensionValueInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                    permissionUserGroupDimensionValueMapper.insertSelective(permissionUserGroupDimensionValueInfo);
                }
            }


            for (PermissionUserGroupDimensionValueInfo permissionUserGroupDimensionValueInfo:delPermissionUserGroupDimensionValueInfos){
                permissionUserGroupDimensionValueMapper.deleteByPrimaryKey(permissionUserGroupDimensionValueInfo);
            }

            for (PermissionUserGroupDimensionValueInfo permissionUserGroupDimensionValueInfo:updatePermissionUserGroupDimensionValueInfos){
                permissionUserGroupDimensionValueInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                permissionUserGroupDimensionValueMapper.updateByPrimaryKeySelective(permissionUserGroupDimensionValueInfo);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 用户组维度删除绑定
     * @param ids 维度id
     * @param group_code 用户组
     * @return
     */
    @SentinelResource(value = "permission_usergroup_dimension_value_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_usergroup_dimension_value_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PermissionUserDimensionValueInfo> permission_usergroup_dimension_value_delete(String[] ids, String group_code) {
        try {

            //根据ids 查询维度信息
            List<PermissionDimensionInfo> permissionDimensionInfos = permissionDimensionMapper.selectObjectByIds(permissionDimensionMapper.getTable(), ids);

            for (PermissionDimensionInfo permissionDimensionInfo: permissionDimensionInfos){
                checkPermissionByOwner(permissionDimensionInfo.getProduct_code());

                Example example=new Example(PermissionUserGroupDimensionValueInfo.class);
                Example.Criteria criteria=example.createCriteria();
                criteria.andEqualTo("is_delete", Const.NOT_DELETE);
                criteria.andEqualTo("product_code", permissionDimensionInfo.getProduct_code());
                criteria.andEqualTo("group_code", group_code);
                criteria.andEqualTo("dim_code", permissionDimensionInfo.getDim_code());
                permissionUserGroupDimensionValueMapper.deleteByExample(example);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 用户组维度扩展属性
     * @param dim_value_code 维度值code
     * @param product_code 产品code
     * @param group_code 组code
     * @param dim_code 维度code
     * @return
     */
    @SentinelResource(value = "permission_usergroup_dimension_value_attr", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_usergroup_dimension_value_attr", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PermissionUserGroupDimensionValueInfo> permission_usergroup_dimension_value_attr(String dim_value_code, String product_code, String group_code, String dim_code) {
        try{

            checkPermissionByOwner(product_code);
            checkParam(group_code, "用户组");

            Example example=new Example(PermissionUserGroupDimensionValueInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("group_code", group_code);
            criteria.andEqualTo("dim_code", dim_code);
            criteria.andEqualTo("dim_value_code", dim_value_code);

            List<PermissionUserGroupDimensionValueInfo> permissionUserGroupDimensionValueInfos = permissionUserGroupDimensionValueMapper.selectByExample(example);

            if(permissionUserGroupDimensionValueInfos != null && permissionUserGroupDimensionValueInfos.size()>=1){
                return ReturnInfo.buildSuccess(permissionUserGroupDimensionValueInfos.get(0));
            }
            return ReturnInfo.buildError("无维度属性信息", new Exception());
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("用户组维度关系信息列表查询失败", e);
        }
    }

    /**
     * 用户维度关系信息更新
     * @param product_code 产品code
     * @param dim_code 维度code
     * @param group_code 用户组
     * @param dim_value_code 维度值code
     * @return
     */
    @SentinelResource(value = "permission_usergroup_dimension_value_attr_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_usergroup_dimension_value_attr_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PermissionUserGroupDimensionValueInfo> permission_usergroup_dimension_value_attr_update(String product_code, String dim_code, String group_code, String dim_value_code,
                                                                                                    String add,String edit,String del,String approve, String auto_attr) {
        try {

            checkPermissionByOwner(product_code);

            Map<String, Object> ext = JsonUtil.createEmptyMap();

            if(!StringUtils.isEmpty(add)){
                add=add.equalsIgnoreCase("on")?"true":"";
                ext.put("add",add);
            }
            if(!StringUtils.isEmpty(edit)){
                edit=edit.equalsIgnoreCase("on")?"true":"";
                ext.put("edit",edit);
            }
            if(!StringUtils.isEmpty(del)){
                del=del.equalsIgnoreCase("on")?"true":"";
                ext.put("del",del);
            }
            if(!StringUtils.isEmpty(approve)){
                approve=approve.equalsIgnoreCase("on")?"true":"";
                ext.put("approve",approve);
            }
            if(!StringUtils.isEmpty(auto_attr)){
                String[] auto_attrs = auto_attr.split(";");
                for (int i=0;i<auto_attrs.length;i++){
                    String[] kv = auto_attrs[i].split(":");
                    ext.put(kv[0],kv[1]);
                }
            }

            Example example=new Example(PermissionUserGroupDimensionValueInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("group_code", group_code);
            criteria.andEqualTo("dim_code", dim_code);
            criteria.andEqualTo("dim_value_code", dim_value_code);

            PermissionUserGroupDimensionValueInfo permissionUserGroupDimensionValueInfo = new PermissionUserGroupDimensionValueInfo();
            permissionUserGroupDimensionValueInfo.setProduct_code(product_code);
            permissionUserGroupDimensionValueInfo.setDim_code(dim_code);
            permissionUserGroupDimensionValueInfo.setGroup_code(group_code);
            permissionUserGroupDimensionValueInfo.setDim_value_code(dim_value_code);
            permissionUserGroupDimensionValueInfo.setIs_delete(Const.NOT_DELETE);


            PermissionUserGroupDimensionValueInfo oldPermissionUserGroupDimensionValueInfo = permissionUserGroupDimensionValueMapper.selectOne(permissionUserGroupDimensionValueInfo);
            if(oldPermissionUserGroupDimensionValueInfo==null){
                //新增
                permissionUserGroupDimensionValueInfo.setOwner(getOwner());
                permissionUserGroupDimensionValueInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
                permissionUserGroupDimensionValueInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                permissionUserGroupDimensionValueInfo.setExt(JsonUtil.formatJsonString(ext));
                permissionUserGroupDimensionValueMapper.insertSelective(permissionUserGroupDimensionValueInfo);

            }else{
                //更新
                oldPermissionUserGroupDimensionValueInfo.setExt(JsonUtil.formatJsonString(ext));
                oldPermissionUserGroupDimensionValueInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                permissionUserGroupDimensionValueMapper.updateByPrimaryKeySelective(oldPermissionUserGroupDimensionValueInfo);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", permissionUserGroupDimensionValueInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }
}
