package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.DataTagMapper;
import com.zyc.zdh.entity.DataTagInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.User;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
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

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 数据标识服务
 */
@Controller
public class DataTagController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    DataTagMapper dataTagMapper;

    /**
     * 数据标识首页
     * @return
     */
    @RequestMapping(value = "/data_tag_index", method = RequestMethod.GET)
    public String data_tag_index() {

        return "admin/data_tag_index";
    }

    /**
     * 数据标识列表
     * @param tag_context 关键字
     * @param product_code 产品代码
     * @return
     */
    @RequestMapping(value = "/data_tag_list", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_tag_list(String tag_context, String product_code) {
        Example example=new Example(DataTagInfo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("is_delete", Const.NOT_DELETE);
        Example.Criteria criteria2=example.createCriteria();
        if(!org.apache.commons.lang3.StringUtils.isEmpty(tag_context)){
            criteria2.orLike("tag_code", getLikeCondition(tag_context));
            criteria2.orLike("tag_name", getLikeCondition(tag_context));
            criteria2.orLike("product_code", getLikeCondition(tag_context));
        }
        example.and(criteria2);

        List<DataTagInfo> dataTagInfos = dataTagMapper.selectByExample(example);

        return JSONObject.toJSONString(dataTagInfos);
    }

    /**
     * 根据产品代码获取数据标识
     * @param product_code 产品代码
     * @return
     */
    @RequestMapping(value = "/data_tag_by_product_code", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_tag_by_product_code(String product_code) {
        Example example=new Example(DataTagInfo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("is_delete", Const.NOT_DELETE);
        criteria.andEqualTo("product_code", product_code);


        List<DataTagInfo> dataTagInfos = dataTagMapper.selectByExample(example);

        return JSONObject.toJSONString(dataTagInfos);
    }

    /**
     * 数据标识新增首页
     * @return
     */
    @RequestMapping(value = "/data_tag_add_index", method = RequestMethod.GET)
    public String data_tag_add_index() {

        return "admin/data_tag_add_index";
    }


    /**
     * 数据标识明细
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/data_tag_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo data_tag_detail(String id) {
        try {
            DataTagInfo dataTagInfo = dataTagMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", dataTagInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 数据标识更新
     * @param dataTagInfo
     * @return
     */
    @RequestMapping(value = "/data_tag_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo data_tag_update(DataTagInfo dataTagInfo) {
        try {
            DataTagInfo oldDataTagInfo = dataTagMapper.selectByPrimaryKey(dataTagInfo.getId());

            dataTagInfo.setOwner(oldDataTagInfo.getOwner());
            dataTagInfo.setCreate_time(oldDataTagInfo.getCreate_time());
            dataTagInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            dataTagInfo.setIs_delete(Const.NOT_DELETE);
            dataTagMapper.updateByPrimaryKey(dataTagInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", dataTagInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 数据标识新增
     * @param dataTagInfo
     * @return
     */
    @RequestMapping(value = "/data_tag_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo data_tag_add(DataTagInfo dataTagInfo) {
        try {
            dataTagInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            dataTagInfo.setOwner(getOwner());
            dataTagInfo.setIs_delete(Const.NOT_DELETE);
            dataTagInfo.setCreate_time(new Timestamp(new Date().getTime()));
            dataTagInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            dataTagMapper.insert(dataTagInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e.getMessage());
        }
    }

    /**
     * 数据标识删除
     * @param ids id数组
     * @return
     */
    @RequestMapping(value = "/data_tag_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo data_tag_delete(String[] ids) {
        try {
            dataTagMapper.deleteBatchById(ids, new Timestamp(new Date().getTime()));
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
