package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.annotation.White;
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
    private RedisUtil redisUtil;

    @Autowired
    private ParamMapper paramMapper;

    /**
     * 系统参数首页
     * @return
     */
    @RequestMapping("/param_index")
    public String param_index() {

        return "service/param_index";
    }

    /**
     * 系统参数新增首页
     * @return
     */
    @RequestMapping("/param_add_index")
    public String param_add_index() {

        return "service/param_add_index";
    }

    /**
     * 系统参数列表
     * @param param_context 关键字
     * @param version 版本号
     * @return
     */
    @SentinelResource(value = "param_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/param_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<ParamInfo>> param_list(String param_context, String version) {
        try{
            Example example=new Example(ParamInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            if(!StringUtils.isEmpty(version)){
                criteria.andEqualTo("version", version);
            }
            Example.Criteria criteria2 = example.createCriteria();
            if(!StringUtils.isEmpty(param_context)){
                criteria2.orLike("param_name", getLikeCondition(param_context));
                criteria2.orLike("param_context", getLikeCondition(param_context));
                criteria2.orLike("param_value", getLikeCondition(param_context));
                criteria2.orLike("param_type", getLikeCondition(param_context));
                example.and(criteria2);
            }

            List<ParamInfo> paramInfos = paramMapper.selectByExample(example);
            return ReturnInfo.buildSuccess(paramInfos);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("系统参数列表查询失败", e);
        }

    }

    /**
     * 系统参数明细
     * @param id id主键
     * @return
     */
    @SentinelResource(value = "param_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/param_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<ParamInfo> param_detail(String id) {
        try{
            ParamInfo paramInfo = paramMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", paramInfo);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 系统参数新增
     * @param paramInfo
     * @return
     */
    @SentinelResource(value = "param_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/param_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo param_add(ParamInfo paramInfo) {
        try{
            paramInfo.setIs_delete(Const.NOT_DELETE);
            paramInfo.setOwner(getUser().getUserName());
            paramInfo.setCreate_time(new Timestamp(new Date().getTime()));
            paramInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            paramMapper.insertSelective(paramInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 系统参数更新
     * @param paramInfo
     * @return
     */
    @SentinelResource(value = "param_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/param_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo param_update(ParamInfo paramInfo) {
        try{
            debugInfo(paramInfo);
            paramInfo.setIs_delete(Const.NOT_DELETE);
            paramInfo.setOwner(getUser().getUserName());
            paramInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            paramMapper.updateByPrimaryKeySelective(paramInfo);
            //改动后邮件通知
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 系统参数删除
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "param_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/param_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo param_delete(String[] ids) {
        try {
            paramMapper.deleteLogicByIds("param_info",ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }

    /**
     * 系统参数写入缓存
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "param_to_redis", blockHandler = "handleReturn")
    @RequestMapping(value = "/param_to_redis", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo param_to_redis(String[] ids) {
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
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 参数合并
     * 用于多版本参数上线后使用 比如 把默认版本参数同步到指定版本
     * @param id
     * @param version
     * @param is_delete
     * @return
     */
    @SentinelResource(value = "param_merge", blockHandler = "handleReturn")
    @RequestMapping(value = "/param_merge", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo param_merge(String id, String version, String is_delete) {
        try{
            ParamInfo paramInfo = paramMapper.selectByPrimaryKey(id);

            if(version.equalsIgnoreCase("default")){
                version = "";
            }
            //根据版本和参数code查询
            Example example=new Example(ParamInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("version", version);
            criteria.andEqualTo("param_name", paramInfo.getParam_name());
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            List<ParamInfo> paramInfos = paramMapper.selectByExample(example);
            if(paramInfos == null || paramInfos.size() > 1 || paramInfos.size() == 0){
                throw new Exception("未找到参数或者存在多个参数,无法更新");
            }
            ParamInfo oldVersionParamInfo = paramInfos.get(0);
            oldVersionParamInfo.setParam_value(paramInfo.getParam_value());
            paramMapper.updateByPrimaryKey(oldVersionParamInfo);
            if(!StringUtils.isEmpty(is_delete)){
                if(is_delete.equalsIgnoreCase(Const.TRUR)){
                    paramMapper.deleteLogicByIds(paramMapper.getTable(), new String[]{id}, new Timestamp(new Date().getTime()));
                }
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
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
