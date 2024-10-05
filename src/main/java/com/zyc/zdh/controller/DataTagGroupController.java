package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.DataTagGroupMapper;
import com.zyc.zdh.entity.DataTagGroupInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
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

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.List;

/**
 * 数据标识组服务
 */
@Controller
public class DataTagGroupController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataTagGroupMapper dataTagGroupMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;


    /**
     * 数据标识组首页
     * @return
     */
    @RequestMapping(value = "/data_tag_group_index", method = RequestMethod.GET)
    public String data_tag_group_index() {

        return "admin/data_tag_group_index";
    }

    /**
     * 数据标识组列表
     * @param tag_context 关键字
     * @param product_code 产品代码
     * @return
     */
    @SentinelResource(value = "data_tag_group_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_tag_group_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<DataTagGroupInfo>> data_tag_group_list(String tag_context, String product_code) {
        try{
            checkParam(product_code, "product_code");
            Example example=new Example(DataTagGroupInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            Example.Criteria criteria2=example.createCriteria();
            if(!org.apache.commons.lang3.StringUtils.isEmpty(tag_context)){
                criteria2.orLike("tag_codes", getLikeCondition(tag_context));
                criteria2.orLike("tag_group_name", getLikeCondition(tag_context));
                criteria2.orLike("tag_group_code", getLikeCondition(tag_context));
                criteria2.orLike("product_code", getLikeCondition(tag_context));
            }
            example.and(criteria2);

            List<DataTagGroupInfo> dataTagGroupInfos = dataTagGroupMapper.selectByExample(example);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", dataTagGroupInfos);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }


    /**
     * 数据标识组新增首页
     * @return
     */
    @RequestMapping(value = "/data_tag_group_add_index", method = RequestMethod.GET)
    public String data_tag_group_add_index() {

        return "admin/data_tag_group_add_index";
    }


    /**
     * 数据标识组明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "data_tag_group_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_tag_group_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<DataTagGroupInfo> data_tag_group_detail(String id) {
        try {
            DataTagGroupInfo dataTagGroupInfo = dataTagGroupMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", dataTagGroupInfo);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 数据标识组更新
     * @param dataTagGroupInfo
     * @return
     */
    @SentinelResource(value = "data_tag_group_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_tag_group_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> data_tag_group_update(DataTagGroupInfo dataTagGroupInfo) {
        try {
            DataTagGroupInfo oldDataTagGroupInfo = dataTagGroupMapper.selectByPrimaryKey(dataTagGroupInfo.getId());

            checkPermissionByProduct(zdhPermissionService, dataTagGroupInfo.getProduct_code());
            checkPermissionByProduct(zdhPermissionService, oldDataTagGroupInfo.getProduct_code());

            dataTagGroupInfo.setOwner(oldDataTagGroupInfo.getOwner());
            dataTagGroupInfo.setCreate_time(oldDataTagGroupInfo.getCreate_time());
            dataTagGroupInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            dataTagGroupInfo.setIs_delete(Const.NOT_DELETE);
            dataTagGroupMapper.updateByPrimaryKeySelective(dataTagGroupInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 数据标识组新增
     * @param dataTagGroupInfo
     * @return
     */
    @SentinelResource(value = "data_tag_group_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_tag_group_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> data_tag_group_add(DataTagGroupInfo dataTagGroupInfo) {
        try {
            dataTagGroupInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            dataTagGroupInfo.setOwner(getOwner());
            dataTagGroupInfo.setIs_delete(Const.NOT_DELETE);
            dataTagGroupInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            dataTagGroupInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkPermissionByProduct(zdhPermissionService, dataTagGroupInfo.getProduct_code());
            dataTagGroupMapper.insertSelective(dataTagGroupInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败",e);
        }
    }

    /**
     * 数据标识组删除
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "data_tag_group_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_tag_group_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> data_tag_group_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, dataTagGroupMapper, dataTagGroupMapper.getTable(), ids, getAttrDel());
            dataTagGroupMapper.deleteLogicByIds(dataTagGroupMapper.getTable(), ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }


    private void debugInfo(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            // 对于每个属性，获取属性名
            String varName = fields[i].getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o;
                try {
                    o = fields[i].get(obj);
                    System.err.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            }
        }
    }


}
