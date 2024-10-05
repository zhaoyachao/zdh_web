package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Sets;
import com.zyc.zdh.dao.PermissionDimensionMapper;
import com.zyc.zdh.dao.PermissionUserDimensionValueMapper;
import com.zyc.zdh.entity.PermissionDimensionInfo;
import com.zyc.zdh.entity.PermissionUserDimensionValueInfo;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 用户维度关系信息服务
 */
@Controller
public class PermissionUserDimensionValueController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PermissionUserDimensionValueMapper permissionUserDimensionValueMapper;

    @Autowired
    private PermissionDimensionMapper permissionDimensionMapper;

    /**
     * 用户维度关系信息列表首页
     * @return
     */
    @RequestMapping(value = "/permission_user_dimension_value_index", method = RequestMethod.GET)
    public String permission_user_dimension_value_index() {

        return "admin/permission_user_dimension_value_index";
    }

    /**
     * 用户绑定的维度列表
     * @param context 关键字
     * @return
     */
    @SentinelResource(value = "permission_user_dimension_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_user_dimension_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
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
    @SentinelResource(value = "permission_user_dimension_value_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_user_dimension_value_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
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
    public String permission_user_dimension_value_add_index() {

        return "admin/permission_user_dimension_value_add_index";
    }

    /**
     * xx明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "permission_user_dimension_value_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_user_dimension_value_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PermissionUserDimensionValueInfo> permission_user_dimension_value_detail(String id) {
        try {
            PermissionUserDimensionValueInfo permissionUserDimensionValueInfo = permissionUserDimensionValueMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionUserDimensionValueInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }


    /**
     * 用户维度删除绑定
     * @param ids 维度id
     * @param user_account 账号
     * @return
     */
    @SentinelResource(value = "permission_user_dimension_value_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_user_dimension_value_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PermissionUserDimensionValueInfo> permission_user_dimension_value_delete(String[] ids, String user_account) {
        try {

            //根据ids 查询维度信息
            List<PermissionDimensionInfo> permissionDimensionInfos = permissionDimensionMapper.selectObjectByIds(permissionDimensionMapper.getTable(), ids);

            for (PermissionDimensionInfo permissionDimensionInfo: permissionDimensionInfos){
                checkPermissionByOwner(permissionDimensionInfo.getProduct_code());

                Example example=new Example(PermissionUserDimensionValueInfo.class);
                Example.Criteria criteria=example.createCriteria();
                criteria.andEqualTo("is_delete", Const.NOT_DELETE);
                criteria.andEqualTo("product_code", permissionDimensionInfo.getProduct_code());
                criteria.andEqualTo("user_account", user_account);
                criteria.andEqualTo("dim_code", permissionDimensionInfo.getDim_code());
                permissionUserDimensionValueMapper.deleteByExample(example);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
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
    @SentinelResource(value = "permission_user_dimension_value_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_user_dimension_value_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PermissionUserDimensionValueInfo> permission_user_dimension_value_update(String product_code, String dim_code, String user_account, String[] dim_value_codes) {
        try {

            checkPermissionByOwner(product_code);
            Example example=new Example(PermissionUserDimensionValueInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("user_account", user_account);
            criteria.andEqualTo("dim_code", dim_code);

            if(dim_value_codes==null && dim_value_codes.length==0){
                permissionUserDimensionValueMapper.deleteByPrimaryKey(example);
                return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
            }

            List<PermissionUserDimensionValueInfo> permissionUserDimensionValueInfos = permissionUserDimensionValueMapper.selectByExample(example);

            List<PermissionUserDimensionValueInfo> updatePermissionUserDimensionValueInfos = new ArrayList<>();
            List<PermissionUserDimensionValueInfo> delPermissionUserDimensionValueInfos = new ArrayList<>();

            Set<String> dim_value_code_set = Sets.newHashSet(dim_value_codes);
            for(PermissionUserDimensionValueInfo permissionUserDimensionValueInfo: permissionUserDimensionValueInfos){
                if(dim_value_code_set.contains(permissionUserDimensionValueInfo.getDim_value_code())){
                    updatePermissionUserDimensionValueInfos.add(permissionUserDimensionValueInfo);
                    dim_value_code_set.remove(permissionUserDimensionValueInfo.getDim_value_code());
                }else{
                    delPermissionUserDimensionValueInfos.add(permissionUserDimensionValueInfo);
                    dim_value_code_set.remove(permissionUserDimensionValueInfo.getDim_value_code());
                }
            }

            if(dim_value_code_set.size()>0){
                for(String dim_value_code: dim_value_code_set){
                    PermissionUserDimensionValueInfo permissionUserDimensionValueInfo = new PermissionUserDimensionValueInfo();
                    permissionUserDimensionValueInfo.setProduct_code(product_code);
                    permissionUserDimensionValueInfo.setDim_code(dim_code);
                    permissionUserDimensionValueInfo.setUser_account(user_account);
                    permissionUserDimensionValueInfo.setDim_value_code(dim_value_code);
                    permissionUserDimensionValueInfo.setOwner(getOwner());
                    permissionUserDimensionValueInfo.setIs_delete(Const.NOT_DELETE);
                    permissionUserDimensionValueInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
                    permissionUserDimensionValueInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                    permissionUserDimensionValueMapper.insertSelective(permissionUserDimensionValueInfo);
                }
            }

            for (PermissionUserDimensionValueInfo permissionUserDimensionValueInfo:delPermissionUserDimensionValueInfos){
                permissionUserDimensionValueMapper.deleteByPrimaryKey(permissionUserDimensionValueInfo);
            }

            for (PermissionUserDimensionValueInfo permissionUserDimensionValueInfo:updatePermissionUserDimensionValueInfos){
                permissionUserDimensionValueInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                permissionUserDimensionValueMapper.updateByPrimaryKeySelective(permissionUserDimensionValueInfo);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 用户维度扩展属性
     * @param dim_value_code 维度值code
     * @param product_code 产品code
     * @param user_account 账号
     * @param dim_code 维度code
     * @return
     */
    @SentinelResource(value = "permission_user_dimension_value_attr", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_user_dimension_value_attr", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PermissionUserDimensionValueInfo> permission_user_dimension_value_attr(String dim_value_code,String product_code, String user_account, String dim_code) {
        try{

            checkPermissionByOwner(product_code);
            checkParam(user_account, "用户账户");

            Example example=new Example(PermissionUserDimensionValueInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("user_account", user_account);
            criteria.andEqualTo("dim_code", dim_code);
            criteria.andEqualTo("dim_value_code", dim_value_code);

            List<PermissionUserDimensionValueInfo> permissionUserDimensionValueInfos = permissionUserDimensionValueMapper.selectByExample(example);

            if(permissionUserDimensionValueInfos != null && permissionUserDimensionValueInfos.size()>=1){
                return ReturnInfo.buildSuccess(permissionUserDimensionValueInfos.get(0));
            }
            return ReturnInfo.buildError("无维度属性信息", new Exception());
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("用户维度关系信息列表查询失败", e);
        }
    }

    /**
     * 用户维度关系信息更新
     * @param product_code 产品code
     * @param dim_code 维度code
     * @param user_account 账号
     * @param dim_value_code 维度值code
     * @return
     */
    @SentinelResource(value = "permission_user_dimension_value_attr_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_user_dimension_value_attr_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PermissionUserDimensionValueInfo> permission_user_dimension_value_attr_update(String product_code, String dim_code, String user_account, String dim_value_code,
                                                                                                    String add,String edit,String del,String approve, String auto_attr) {
        try {

            JSONObject ext = new JSONObject();

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

            Example example=new Example(PermissionUserDimensionValueInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("user_account", user_account);
            criteria.andEqualTo("dim_code", dim_code);
            criteria.andEqualTo("dim_value_code", dim_value_code);

            PermissionUserDimensionValueInfo permissionUserDimensionValueInfo = new PermissionUserDimensionValueInfo();
            permissionUserDimensionValueInfo.setProduct_code(product_code);
            permissionUserDimensionValueInfo.setDim_code(dim_code);
            permissionUserDimensionValueInfo.setUser_account(user_account);
            permissionUserDimensionValueInfo.setDim_value_code(dim_value_code);
            permissionUserDimensionValueInfo.setIs_delete(Const.NOT_DELETE);


            PermissionUserDimensionValueInfo oldPermissionUserDimensionValueInfo = permissionUserDimensionValueMapper.selectOne(permissionUserDimensionValueInfo);
            if(oldPermissionUserDimensionValueInfo==null){
                //新增
                permissionUserDimensionValueInfo.setOwner(getOwner());
                permissionUserDimensionValueInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
                permissionUserDimensionValueInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                permissionUserDimensionValueInfo.setExt(ext.toJSONString());
                permissionUserDimensionValueMapper.insertSelective(permissionUserDimensionValueInfo);

            }else{
                //更新
                oldPermissionUserDimensionValueInfo.setExt(ext.toJSONString());
                oldPermissionUserDimensionValueInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                permissionUserDimensionValueMapper.updateByPrimaryKeySelective(oldPermissionUserDimensionValueInfo);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", permissionUserDimensionValueInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

}
