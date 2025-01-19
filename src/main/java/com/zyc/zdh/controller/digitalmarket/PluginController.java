package com.zyc.zdh.controller.digitalmarket;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.PluginMapper;
import com.zyc.zdh.entity.PluginInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.JsonUtil;
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
import java.util.List;
import java.util.Map;

/**
 * 智能营销-插件服务
 */
@Controller
public class PluginController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PluginMapper pluginMapper;

    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 插件列表首页
     * @return
     */
    @RequestMapping(value = "/plugin_index", method = RequestMethod.GET)
    public String plugin_index() {

        return "digitalmarket/plugin_index";
    }

    /**
     * 插件列表
     * @param plugin_name 关键字
     * @return
     */
    @SentinelResource(value = "plugin_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/plugin_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PluginInfo>> plugin_list(String plugin_name, String product_code, String dim_group) {
        try{
            Example example=new Example(PluginInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            //动态增加数据权限控制
            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(plugin_name)){
                criteria2.orLike("plugin_code", getLikeCondition(plugin_name));
                criteria2.orLike("plugin_name", getLikeCondition(plugin_name));
                criteria2.orLike("plugin_json", getLikeCondition(plugin_name));
                example.and(criteria2);
            }

            List<PluginInfo> pluginInfos = pluginMapper.selectByExample(example);

            return ReturnInfo.buildSuccess(pluginInfos);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("插件列表查询失败", e);
        }

    }

    /**
     * 插件新增首页
     * @return
     */
    @RequestMapping(value = "/plugin_add_index", method = RequestMethod.GET)
    public String plugin_add_index() {

        return "digitalmarket/plugin_add_index";
    }

    /**
     * 插件明细页面
     * @return
     */
    @RequestMapping(value = "/plugin_detail2", method = RequestMethod.GET)
    public String plugin_detail2() {

        return "digitalmarket/plugin_detail2";
    }
    /**
     * 插件明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "plugin_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/plugin_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PluginInfo> plugin_detail(String id) {
        try {
            PluginInfo pluginInfo = pluginMapper.selectByPrimaryKey(id);
            checkPermissionByProduct(zdhPermissionService, pluginInfo.getProduct_code());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pluginInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 根据code查询插件明细
     * @param plugin_code
     * @return
     */
    @SentinelResource(value = "plugin_detail_by_code", blockHandler = "handleReturn")
    @RequestMapping(value = "/plugin_detail_by_code", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PluginInfo> plugin_detail_by_code(String plugin_code) {
        try {

            PluginInfo pluginInfo = new PluginInfo();
            pluginInfo.setPlugin_code(plugin_code);
            pluginInfo = pluginMapper.selectOne(pluginInfo);
            checkPermissionByProduct(zdhPermissionService, pluginInfo.getProduct_code());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pluginInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 插件更新
     * @param pluginInfo
     * @param param_code 参数code
     * @param param_context 参数说明
     * @param param_operate 参数操作符
     * @param param_value 参数可选值
     * @return
     */
    @SentinelResource(value = "plugin_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/plugin_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PluginInfo> plugin_update(PluginInfo pluginInfo,String[] param_code, String[] param_context, String[] param_type,String[] param_operate, String[] param_value) {
        try {
            if(param_code==null || param_code.length<1){
                throw new Exception("参数不可为空");
            }

            List<Map<String, Object>> jsonArray= JsonUtil.createEmptyListMap();
            for (int i=0;i<param_code.length;i++){
                Map<String, Object> jsonObject= JsonUtil.createEmptyMap();
                jsonObject.put("param_code", param_code[i]);
                jsonObject.put("param_context", param_context[i]);
                jsonObject.put("param_operate", param_operate[i]);
                jsonObject.put("param_type", param_type[i]);
                if(i>=param_value.length){
                    jsonObject.put("param_value", "");
                }else{
                    jsonObject.put("param_value", param_value[i]);
                }
                jsonArray.add(jsonObject);
            }

            pluginInfo.setPlugin_json(JsonUtil.formatJsonString(jsonArray));

            PluginInfo oldPluginInfo = pluginMapper.selectByPrimaryKey(pluginInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pluginInfo.getProduct_code(), pluginInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldPluginInfo.getProduct_code(), oldPluginInfo.getDim_group(), getAttrEdit());


            pluginInfo.setOwner(oldPluginInfo.getOwner());
            pluginInfo.setCreate_time(oldPluginInfo.getCreate_time());
            pluginInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            pluginInfo.setIs_delete(Const.NOT_DELETE);
            pluginMapper.updateByPrimaryKeySelective(pluginInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", pluginInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 插件新增
     * @param pluginInfo
     * @param param_code 参数code
     * @param param_context 参数说明
     * @param param_operate 参数操作符
     * @param param_value 参数可选值
     * @return
     */
    @SentinelResource(value = "plugin_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/plugin_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PluginInfo> plugin_add(PluginInfo pluginInfo,String[] param_code, String[] param_context, String[] param_type, String[] param_operate, String[] param_value) {
        try {
            if(param_code==null || param_code.length<1){
               throw new Exception("参数不可为空");
            }
            List<Map<String, Object>> jsonArray= JsonUtil.createEmptyListMap();
            for (int i=0;i<param_code.length;i++){
                Map<String, Object> jsonObject= JsonUtil.createEmptyMap();
                jsonObject.put("param_code", param_code[i]);
                jsonObject.put("param_context", param_context[i]);
                jsonObject.put("param_operate", param_operate[i]);
                jsonObject.put("param_type", param_type[i]);
                if(i>=param_value.length){
                    jsonObject.put("param_value", "");
                }else{
                    jsonObject.put("param_value", param_value[i]);
                }
                jsonArray.add(jsonObject);
            }

            pluginInfo.setPlugin_json(JsonUtil.formatJsonString(jsonArray));

            pluginInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            pluginInfo.setOwner(getOwner());
            pluginInfo.setIs_delete(Const.NOT_DELETE);
            pluginInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            pluginInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pluginInfo.getProduct_code(), pluginInfo.getDim_group(), getAttrAdd());

            pluginMapper.insertSelective(pluginInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", pluginInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 插件删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "plugin_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/plugin_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo plugin_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pluginMapper, pluginMapper.getTable(), ids, getAttrDel());
            pluginMapper.deleteLogicByIds(pluginMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }


}
