package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.PermissionDimensionValueMapper;
import com.zyc.zdh.entity.PermissionDimensionValueInfo;
import com.zyc.zdh.entity.PermissionDimensionValueNodeInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.util.Const;
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

/**
 * 维度值信息服务
 */
@Controller
public class PermissionDimensionValueController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PermissionDimensionValueMapper permissionDimensionValueMapper;

    /**
     * 维度值信息列表首页
     * @return
     */
    @RequestMapping(value = "/permission_dimension_value_index", method = RequestMethod.GET)
    public String permission_dimension_value_index() {

        return "admin/permission_dimension_value_index";
    }

    /**
     * 维度值信息列表
     * @param product_code 产品代码
     * @param dim_code 维度code
     * @return
     */
    @SentinelResource(value = "permission_dimension_value_node", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_dimension_value_node", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionDimensionValueNodeInfo>> permission_dimension_value_node(String product_code, String dim_code) {
        try{
            checkPermissionByOwner(product_code);

            Example example=new Example(PermissionDimensionValueInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("dim_code", dim_code);

            List<PermissionDimensionValueInfo> permissionDimensionValueInfos = permissionDimensionValueMapper.selectByExample(example);
            List<PermissionDimensionValueNodeInfo> permissionDimensionValueNodeInfos=new ArrayList<>();
            for (PermissionDimensionValueInfo permissionDimensionValueInfo: permissionDimensionValueInfos){
                permissionDimensionValueNodeInfos.add(PermissionDimensionValueNodeInfo.build(permissionDimensionValueInfo));
            }

            return ReturnInfo.buildSuccess(permissionDimensionValueNodeInfos);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("维度值信息列表查询失败", e);
        }
    }

    /**
     * 维度值信息列表
     * @param product_code 产品代码
     * @param dim_code 维度code
     * @return
     */
    @SentinelResource(value = "permission_dimension_value_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_dimension_value_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionDimensionValueInfo>> permission_dimension_value_list(String product_code, String dim_code) {
        try{
            checkPermissionByOwner(product_code);

            Example example=new Example(PermissionDimensionValueInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            criteria.andEqualTo("dim_code", dim_code);

            List<PermissionDimensionValueInfo> permissionDimensionValueInfos = permissionDimensionValueMapper.selectByExample(example);

            return ReturnInfo.buildSuccess(permissionDimensionValueInfos);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("维度值信息列表查询失败", e);
        }
    }

    /**
     * 维度值信息新增首页
     * @return
     */
    @RequestMapping(value = "/permission_dimension_value_add_index", method = RequestMethod.GET)
    public String permission_dimension_value_add_index() {

        return "admin/permission_dimension_value_add_index";
    }

    /**
     * 查询维度值明细
     * @param product_code 产品code
     * @param dim_code 维度code
     * @param dim_value_code 维度值code
     * @return
     */
    @SentinelResource(value = "permission_dimension_value_detail_by_code", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_dimension_value_detail_by_code", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PermissionDimensionValueInfo> permission_dimension_value_detail_by_code(String product_code, String dim_code,String dim_value_code) {
        try {
            checkPermissionByOwner(product_code);

            PermissionDimensionValueInfo permissionDimensionValueInfo=new PermissionDimensionValueInfo();
            permissionDimensionValueInfo.setProduct_code(product_code);
            permissionDimensionValueInfo.setDim_code(dim_code);
            permissionDimensionValueInfo.setDim_value_code(dim_value_code);
            permissionDimensionValueInfo.setIs_delete(Const.NOT_DELETE);
            permissionDimensionValueInfo = permissionDimensionValueMapper.selectOne(permissionDimensionValueInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionDimensionValueInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }


    /**
     * xx明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "permission_dimension_value_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_dimension_value_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PermissionDimensionValueInfo> permission_dimension_value_detail(String id) {
        try {
            PermissionDimensionValueInfo permissionDimensionValueInfo = permissionDimensionValueMapper.selectByPrimaryKey(id);
            checkPermissionByOwner(permissionDimensionValueInfo.getProduct_code());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", permissionDimensionValueInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 维度值信息更新
     * @param permissionDimensionValueInfo
     * @return
     */
    @SentinelResource(value = "permission_dimension_value_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_dimension_value_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PermissionDimensionValueInfo> permission_dimension_value_update(PermissionDimensionValueInfo permissionDimensionValueInfo) {
        try {

            checkPermissionByOwner(permissionDimensionValueInfo.getProduct_code());
            PermissionDimensionValueInfo oldPermissionDimensionValueInfo = permissionDimensionValueMapper.selectByPrimaryKey(permissionDimensionValueInfo.getId());
            checkPermissionByOwner(oldPermissionDimensionValueInfo.getProduct_code());

            oldPermissionDimensionValueInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            oldPermissionDimensionValueInfo.setIs_delete(Const.NOT_DELETE);
            permissionDimensionValueMapper.updateByPrimaryKeySelective(permissionDimensionValueInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", oldPermissionDimensionValueInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 维度值更新父节点
     * @param permissionDimensionValueInfo
     * @return
     */
    @SentinelResource(value = "permission_dimension_value_parent", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_dimension_value_parent", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PermissionDimensionValueInfo> permission_dimension_value_parent(PermissionDimensionValueInfo permissionDimensionValueInfo) {
        try {


            PermissionDimensionValueInfo oldPermissionDimensionValueInfo = permissionDimensionValueMapper.selectByPrimaryKey(permissionDimensionValueInfo.getId());
            checkPermissionByOwner(oldPermissionDimensionValueInfo.getProduct_code());

            oldPermissionDimensionValueInfo.setDim_value_name(permissionDimensionValueInfo.getDim_value_name());
            oldPermissionDimensionValueInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            oldPermissionDimensionValueInfo.setIs_delete(Const.NOT_DELETE);
            oldPermissionDimensionValueInfo.setParent_dim_value_code(permissionDimensionValueInfo.getParent_dim_value_code());
            permissionDimensionValueMapper.updateByPrimaryKeySelective(oldPermissionDimensionValueInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", oldPermissionDimensionValueInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 维度值信息新增
     * @param permissionDimensionValueInfo
     * @return
     */
    @SentinelResource(value = "permission_dimension_value_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_dimension_value_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PermissionDimensionValueInfo> permission_dimension_value_add(PermissionDimensionValueInfo permissionDimensionValueInfo) {
        try {
            checkPermissionByOwner(permissionDimensionValueInfo.getProduct_code());

            permissionDimensionValueInfo.setOwner(getOwner());
            permissionDimensionValueInfo.setIs_delete(Const.NOT_DELETE);
            permissionDimensionValueInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            permissionDimensionValueInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            permissionDimensionValueMapper.insertSelective(permissionDimensionValueInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", permissionDimensionValueInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 维度值信息删除
     * @param id
     * @return
     */
    @SentinelResource(value = "permission_dimension_value_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/permission_dimension_value_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo permission_dimension_value_delete(String id) {
        try {
            List<PermissionDimensionValueInfo> permissionDimensionValueInfos = permissionDimensionValueMapper.selectObjectByIds(permissionDimensionValueMapper.getTable(), new String[]{id});
            if(permissionDimensionValueInfos != null){
                for (PermissionDimensionValueInfo permissionDimensionValueInfo: permissionDimensionValueInfos){
                    checkPermissionByOwner(permissionDimensionValueInfo.getProduct_code());
                }
            }
            permissionDimensionValueMapper.deleteLogicByIds(permissionDimensionValueMapper.getTable(),new String[]{id}, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

}
