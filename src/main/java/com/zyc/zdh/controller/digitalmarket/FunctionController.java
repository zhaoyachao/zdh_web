package com.zyc.zdh.controller.digitalmarket;

import cn.hutool.core.lang.JarClassLoader;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.FunctionMapper;
import com.zyc.zdh.entity.FunctionInfo;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.GroovyFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
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

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

/**
 * 函数信息服务
 */
@Controller
public class FunctionController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FunctionMapper functionMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 函数信息列表首页
     * @return
     */
    @RequestMapping(value = "/function_index", method = RequestMethod.GET)
    public String function_index() {

        return "digitalmarket/function_index";
    }

    /**
     * 函数信息列表
     * @param context 关键字
     * @return
     */
    @SentinelResource(value = "function_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/function_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<FunctionInfo>> function_list(String context) {
        try{
            Example example=new Example(FunctionInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            dynamicPermissionByProduct(zdhPermissionService, criteria);

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
            criteria2.orLike("context", getLikeCondition(context));
            }
            example.and(criteria2);

            List<FunctionInfo> functionInfos = functionMapper.selectByExample(example);

            return ReturnInfo.buildSuccess(functionInfos);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("函数信息列表查询失败", e);
        }

    }


    /**
    * 函数信息列表
    * @param context 关键字
    * @param product_code 产品code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "function_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/function_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<FunctionInfo>>> function_list_by_page(String context,String product_code, int limit, int offset) {
        try{
            Example example=new Example(FunctionInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
            }
            example.and(criteria2);

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = functionMapper.selectCountByExample(example);

            List<FunctionInfo> functionInfos = functionMapper.selectByExampleAndRowBounds(example, rowBounds);

            PageResult<List<FunctionInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(functionInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("烽火台信息列表查询失败", e);
        }

    }

    /**
     * 函数信息新增首页
     * @return
     */
    @RequestMapping(value = "/function_add_index", method = RequestMethod.GET)
    public String function_add_index() {

        return "digitalmarket/function_add_index";
    }

    /**
     * 函数测试首页
     * @return
     */
    @RequestMapping(value = "/function_execute_index", method = RequestMethod.GET)
    public String function_execute_index() {

        return "digitalmarket/function_execute_index";
    }

    /**
     * 函数明细页面
     * @return
     */
    @RequestMapping(value = "/function_detail2", method = RequestMethod.GET)
    public String function_detail2() {

        return "digitalmarket/function_detail2";
    }

    /**
     * xx明细
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/function_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<FunctionInfo> function_detail(String id) {
        try {
            FunctionInfo functionInfo = functionMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", functionInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 函数信息更新
     * @param functionInfo
     * @return
     */
    @RequestMapping(value = "/function_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<FunctionInfo> function_update(FunctionInfo functionInfo, String[] param_code, String[] param_context, String[] param_type) {
        try {

            FunctionInfo oldFunctionInfo = functionMapper.selectByPrimaryKey(functionInfo.getId());

            JSONArray jsonArray = new JSONArray();
            if(param_code != null && param_code.length>0){
                for (int i=0;i<param_code.length;i++){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("param_code", param_code[i]);
                    jsonObject.put("param_context", param_context[i]);
                    jsonObject.put("param_type", param_type[i]);
                    jsonArray.add(jsonObject);
                }
            }

            functionInfo.setParam_json(jsonArray.toJSONString());
            functionInfo.setCreate_time(oldFunctionInfo.getCreate_time());
            functionInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            functionInfo.setIs_delete(Const.NOT_DELETE);
            functionMapper.updateByPrimaryKeySelective(functionInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", functionInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 函数信息新增
     * @param functionInfo
     * @return
     */
    @RequestMapping(value = "/function_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<FunctionInfo> function_add(FunctionInfo functionInfo, String[] param_code, String[] param_context, String[] param_type) {
        try {
            JSONArray jsonArray = new JSONArray();
            if(param_code != null && param_code.length>0){
                for (int i=0;i<param_code.length;i++){
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("param_code", param_code[i]);
                    jsonObject.put("param_context", param_context[i]);
                    jsonObject.put("param_type", param_type[i]);
                    jsonArray.add(jsonObject);
                }
            }

            functionInfo.setParam_json(jsonArray.toJSONString());

            functionInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            functionInfo.setOwner(getOwner());
            functionInfo.setIs_delete(Const.NOT_DELETE);
            functionInfo.setCreate_time(new Timestamp(new Date().getTime()));
            functionInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            functionMapper.insertSelective(functionInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", functionInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 函数信息删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/function_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo function_delete(String[] ids) {
        try {
            functionMapper.deleteLogicByIds(functionMapper.getTable(),ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }


    /**
     * 根据code查询插件明细
     * @param function_name
     * @return
     */
    @SentinelResource(value = "function_detail_by_code", blockHandler = "handleReturn")
    @RequestMapping(value = "/function_detail_by_code", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<FunctionInfo> function_detail_by_code(String function_name) {
        try {

            FunctionInfo functionInfo = new FunctionInfo();
            functionInfo.setFunction_name(function_name);
            functionInfo.setIs_delete(Const.NOT_DELETE);
            functionInfo = functionMapper.selectOne(functionInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", functionInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 函数测试
     * @param id
     * @return
     */
    @RequestMapping(value = "/function_execute", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo function_execute(String id, String[] param_value) {
        try {
            FunctionInfo functionInfo = functionMapper.selectByPrimaryKey(id);
            String function_name = functionInfo.getFunction_name();
            String function_class = functionInfo.getFunction_class();
            String function_load_path = functionInfo.getFunction_load_path();
            String function_script = functionInfo.getFunction_script();
            JSONArray jsonArray = functionInfo.getParam_json_object();

            Map<String, Object> objectMap = new LinkedHashMap<>();
            List<String> params = new ArrayList<>();
            for(int i=0;i<jsonArray.size();i++){
                String param_code = jsonArray.getJSONObject(i).getString("param_code");
                objectMap.put(param_code, param_value[i]);
                params.add(param_code);
            }

            if(!StringUtils.isEmpty(function_class)){
                String[] function_packages = function_class.split(",");
                String clsName = ArrayUtil.get(function_packages, function_packages.length-1);
                String clsInstanceName = StringUtils.uncapitalize(clsName);

                //加载三方工具类
                if(!StringUtils.isEmpty(function_load_path)){
                    JarClassLoader jarClassLoader = JarClassLoader.loadJar(new File(function_load_path));
                    Class cls = jarClassLoader.loadClass(function_class);
                    Object clsInstance = cls.newInstance();
                    objectMap.put(clsInstanceName, clsInstance);
                    function_script = clsInstanceName+"."+function_name+"("+StringUtils.join(params, ",")+")";
                    Object ret = GroovyFactory.execExpress(function_script, objectMap);
                    return ReturnInfo.buildSuccess(ret);
                }else{
                    Object clsInstance = ClassLoaderUtil.loadClass(function_class).newInstance();
                    objectMap.put(clsInstanceName, clsInstance);
                    function_script = clsInstanceName+"."+function_name+"("+StringUtils.join(params, ",")+")";
                    Object ret = GroovyFactory.execExpress(function_script, objectMap);
                    return ReturnInfo.buildSuccess(ret);
                }
            }
            if(!StringUtils.isEmpty(function_script)){

                Object ret = GroovyFactory.execExpress(function_script, function_name, objectMap);
                return ReturnInfo.buildSuccess(ret);
            }
            return ReturnInfo.buildSuccess(null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "执行失败", e.getMessage());
        }
    }





}
