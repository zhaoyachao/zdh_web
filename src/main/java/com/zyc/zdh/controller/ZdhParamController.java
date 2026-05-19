package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.ParamMapper;
import com.zyc.zdh.entity.ParamInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.Const;
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
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 参数配置服务
 */
@Controller
public class ZdhParamController extends BaseController {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private ParamMapper paramMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

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
    public ReturnInfo<List<ParamInfo>> param_list(String product_code, String param_context, String version) {
        try{
            Example example=new Example(ParamInfo.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            if(!StringUtils.isEmpty(version)){
                criteria.andEqualTo("version", version);
            }
            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }

            dynamicPermissionByProduct(zdhPermissionService, criteria);

            Example.Criteria criteria2 = example.createCriteria();
            if(!StringUtils.isEmpty(param_context)){
                criteria2.orLike("param_name", getLikeCondition(param_context));
                criteria2.orLike("param_context", getLikeCondition(param_context));
                criteria2.orLike("param_value", getLikeCondition(param_context));
                criteria2.orLike("param_type", getLikeCondition(param_context));
                example.and(criteria2);
            }

            List<ParamInfo> paramInfos = paramMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, paramInfos);

            return ReturnInfo.buildSuccess(paramInfos);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
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
            checkAttrPermissionByProduct(zdhPermissionService, paramInfo.getProduct_code(), getAttrSelect());

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", paramInfo);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
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

            checkAttrPermissionByProduct(zdhPermissionService, paramInfo.getProduct_code(), getAttrAdd());

            paramInfo.setIs_delete(Const.NOT_DELETE);
            paramInfo.setOwner(getUser().getUserName());
            paramInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            paramInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            paramMapper.insertSelective(paramInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
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

            checkAttrPermissionByProduct(zdhPermissionService, paramInfo.getProduct_code(), getAttrEdit());

            paramInfo.setIs_delete(Const.NOT_DELETE);
            paramInfo.setOwner(getUser().getUserName());
            paramInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            paramMapper.updateByPrimaryKeySelective(paramInfo);
            //改动后邮件通知
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
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
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, paramMapper, paramMapper.getTable(), ids, getAttrDel());
            paramMapper.deleteLogicByIds("param_info",ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
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
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, paramMapper, paramMapper.getTable(), ids, getAttrEdit());

            List<ParamInfo> paramInfos = paramMapper.selectByExample(example);
            for (ParamInfo paramInfo:paramInfos){
                String version = paramInfo.getVersion();
                String key = paramInfo.getProduct_code()+"_"+paramInfo.getParam_name();
                if(!StringUtils.isEmpty(version)){
                    key = version+"_"+key;
                }

                if(paramInfo.getParam_timeout().equalsIgnoreCase("-1")){
                    redisUtil.set(key,  paramInfo.getParam_value());
                }else{
                    redisUtil.set(key,  paramInfo.getParam_value(), StringUtils.isEmpty(paramInfo.getParam_timeout())?300L:Long.parseLong(paramInfo.getParam_timeout()), TimeUnit.SECONDS);
                }
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
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
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo param_merge(String id, String version, String is_delete) {
        try{
            ParamInfo paramInfo = paramMapper.selectByPrimaryKey(id);

            checkAttrPermissionByProduct(zdhPermissionService, paramInfo.getProduct_code(), getAttrEdit());

            if(version.equalsIgnoreCase("default")){
                version = "";
            }
            //根据版本和参数code查询
            Example example=new Example(ParamInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("product_code", paramInfo.getProduct_code());
            criteria.andEqualTo("version", version);
            criteria.andEqualTo("param_name", paramInfo.getParam_name());
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            List<ParamInfo> paramInfos = paramMapper.selectByExample(example);
            if(paramInfos == null || paramInfos.size() > 1 || paramInfos.size() == 0){
                throw new Exception("未找到参数或者存在多个参数,无法更新");
            }
            ParamInfo oldVersionParamInfo = paramInfos.get(0);
            oldVersionParamInfo.setParam_value(paramInfo.getParam_value());
            oldVersionParamInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            paramMapper.updateByPrimaryKey(oldVersionParamInfo);
            if(!StringUtils.isEmpty(is_delete)){
                if(is_delete.equalsIgnoreCase(Const.TRUR)){
                    paramMapper.deleteLogicByIds(paramMapper.getTable(), new String[]{id}, new Timestamp(System.currentTimeMillis()));
                }
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

}
