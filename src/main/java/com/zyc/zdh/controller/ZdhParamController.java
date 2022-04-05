package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.zyc.zdh.dao.ParamMapper;
import com.zyc.zdh.entity.ParamInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.shiro.RedisUtil;
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

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 参数配置服务
 */
@Controller
public class ZdhParamController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    ParamMapper paramMapper;

    @RequestMapping("/param_index")
    public String param_index() {

        return "service/param_index";
    }

    @RequestMapping("/param_add_index")
    public String param_add_index() {

        return "service/param_add_index";
    }

    @RequestMapping(value = "/param_list", method = RequestMethod.GET, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String param_list(String param_context) {

        Example example=new Example(ParamInfo.class);
        Example.Criteria criteria2 = example.createCriteria();
        criteria2.andEqualTo("is_delete", Const.NOT_DELETE);
        Example.Criteria criteria = example.createCriteria();
        if(!StringUtils.isEmpty(param_context)){
            criteria.orLike("param_name", getLikeCondition(param_context));
            criteria.orLike("param_context", getLikeCondition(param_context));
            criteria.orLike("param_value", getLikeCondition(param_context));
            criteria.orLike("param_type", getLikeCondition(param_context));
            example.and(criteria);
        }
        List<ParamInfo> paramInfos = paramMapper.selectByExample(example);
        return JSON.toJSONString(paramInfos);
    }

    @RequestMapping(value = "/param_detail", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String param_detail(String id) {
        try{
            ParamInfo paramInfo = paramMapper.selectByPrimaryKey(id);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "查询成功", paramInfo);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    @RequestMapping(value = "/param_add", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String param_add(ParamInfo paramInfo) {
        try{
            paramInfo.setIs_delete(Const.NOT_DELETE);
            paramInfo.setOwner(getUser().getId());
            paramInfo.setCreate_time(new Timestamp(new Date().getTime()));
            paramInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            paramMapper.insert(paramInfo);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    @RequestMapping(value = "/param_update", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String param_update(ParamInfo paramInfo) {
        try{
            debugInfo(paramInfo);
            paramInfo.setIs_delete(Const.NOT_DELETE);
            paramInfo.setOwner(getUser().getId());
            paramInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            paramMapper.updateByPrimaryKey(paramInfo);
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    @RequestMapping(value = "/param_delete", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public String param_delete(String[] ids) {
        try {
            paramMapper.deleteBatchById(ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }

    @RequestMapping(value = "/param_to_redis", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String param_to_redis(String[] ids) {
        try{
            Example example=new Example(ParamInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andIn("id", Arrays.asList(ids));
            List<ParamInfo> paramInfos = paramMapper.selectByExample(example);
            for (ParamInfo paramInfo:paramInfos){
                if(paramInfo.getParam_timeout().equalsIgnoreCase("-1")){
                    redisUtil.set(paramInfo.getParam_name(),  paramInfo.getParam_value());
                }else{
                    redisUtil.set(paramInfo.getParam_name(),  paramInfo.getParam_value(), StringUtils.isEmpty(paramInfo.getParam_timeout())?300L:Long.parseLong(paramInfo.getParam_timeout()), TimeUnit.SECONDS);
                }
            }
            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "更新失败", e);
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
                    logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" + e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" + e);
            }
        }
    }

}
