package com.zyc.zdh.controller.digitalmarket;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.RiskEventMapper;
import com.zyc.zdh.dao.StrategyGroupInstanceMapper;
import com.zyc.zdh.dao.ZdhLogsMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.ConfigUtil;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.HttpUtil;
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
import java.util.stream.Collectors;

/**
 * 风控事件信息服务
 */
@Controller
public class RiskEventController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RiskEventMapper riskEventMapper;

    @Autowired
    private ZdhPermissionService zdhPermissionService;

    @Autowired
    private ZdhLogsMapper zdhLogsMapper;

    @Autowired
    private StrategyGroupInstanceMapper strategyGroupInstanceMapper;

    /**
     * 风控事件测试首页
     * @return
     */
    @RequestMapping(value = "/risk_test_index", method = RequestMethod.GET)
    public String risk_test_index() {

        return "digitalmarket/risk_test_index";
    }

    /**
     * 风控测试
     * @param uid
     * @param data_node
     * @param scene
     * @param source
     * @param id_type
     * @param param
     * @return
     */
    @SentinelResource(value = "risk_test", blockHandler = "handleReturn")
    @RequestMapping(value = "/risk_test", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<JSONObject> risk_test(String uid, String data_node, String scene, String source, String id_type, String param) {

        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("uid", uid);
            jsonObject.put("data_node", data_node);
            jsonObject.put("scene", scene);
            jsonObject.put("source", source);
            jsonObject.put("id_type", id_type);
            jsonObject.put("param", param);
            jsonObject.put("product_code", source);

            String url = ConfigUtil.getValue("zdh.ship.url", "http://127.0.0.1:9002/api/v1/ship/accept");
            String ret = HttpUtil.postJSON(url, jsonObject.toJSONString());
            System.out.println(ret);
            return ReturnInfo.buildSuccess(JSONObject.parseObject(ret));
        }catch (Exception e){
            e.printStackTrace();
            return ReturnInfo.buildError(e);
        }
    }

    /**
     * 风控事件信息列表首页
     * @return
     */
    @RequestMapping(value = "/risk_event_index", method = RequestMethod.GET)
    public String risk_event_index() {

        return "digitalmarket/risk_event_index";
    }

    /**
     * 风控事件信息列表
     * @param context 关键字
     * @return
     */
    @SentinelResource(value = "risk_event_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/risk_event_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<RiskEventInfo>> risk_event_list(String context, String product_code, String dim_group) {

        try{
            Example example=new Example(RiskEventInfo.class);
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
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("event_name", getLikeCondition(context));
                criteria2.orLike("event_code", getLikeCondition(context));
            }
            example.and(criteria2);

            List<RiskEventInfo> riskEventInfos = riskEventMapper.selectByExample(example);

            return ReturnInfo.buildSuccess(riskEventInfos);
        }catch (Exception e){
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }

    /**
     * 风控事件信息新增首页
     * @return
     */
    @RequestMapping(value = "/risk_event_add_index", method = RequestMethod.GET)
    public String risk_event_add_index() {

        return "digitalmarket/risk_event_add_index";
    }


    /**
     * 风控事件信息明细页面
     * @return
     */
    @RequestMapping(value = "/risk_event_detail2", method = RequestMethod.GET)
    public String risk_event_detail2() {

        return "digitalmarket/risk_event_detail2";
    }
    /**
     * xx明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "risk_event_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/risk_event_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<RiskEventInfo> risk_event_detail(String id) {
        try {
            RiskEventInfo riskEventInfo = riskEventMapper.selectByPrimaryKey(id);
            checkPermissionByProduct(zdhPermissionService, riskEventInfo.getProduct_code());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", riskEventInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 风控事件信息更新
     * @param riskEventInfo
     * @return
     */
    @SentinelResource(value = "risk_event_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/risk_event_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<RiskEventInfo> risk_event_update(RiskEventInfo riskEventInfo, String[] param_code, String[] param_context, String[] param_type,String[] param_operate, String[] param_value) {
        try {

            RiskEventInfo oldRiskEventInfo = riskEventMapper.selectByPrimaryKey(riskEventInfo.getId());

            JSONArray jsonArray=new JSONArray();
            for (int i=0;i<param_code.length;i++){
                JSONObject jsonObject=new JSONObject();
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
            riskEventInfo.setEvent_json(jsonArray.toJSONString());

            riskEventInfo.setOwner(oldRiskEventInfo.getOwner());
            riskEventInfo.setCreate_time(oldRiskEventInfo.getCreate_time());
            riskEventInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            riskEventInfo.setIs_delete(Const.NOT_DELETE);
            riskEventMapper.updateByPrimaryKeySelective(riskEventInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", riskEventInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 风控事件信息新增
     * @param riskEventInfo
     * @return
     */
    @SentinelResource(value = "risk_event_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/risk_event_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<RiskEventInfo> risk_event_add(RiskEventInfo riskEventInfo,String[] param_code, String[] param_context, String[] param_type,String[] param_operate, String[] param_value) {
        try {
            riskEventInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");

            JSONArray jsonArray=new JSONArray();
            for (int i=0;i<param_code.length;i++){
                JSONObject jsonObject=new JSONObject();
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
            riskEventInfo.setEvent_json(jsonArray.toJSONString());

            riskEventInfo.setOwner(getOwner());
            riskEventInfo.setIs_delete(Const.NOT_DELETE);
            riskEventInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            riskEventInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            riskEventMapper.insertSelective(riskEventInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", riskEventInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 风控事件信息删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "risk_event_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/risk_event_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo risk_event_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, riskEventMapper, riskEventMapper.getTable(), ids, getAttrDel());
            riskEventMapper.deleteLogicByIds(riskEventMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    /**
     * 根据code查询风控事件明细
     * @param plugin_code
     * @return
     */
    @SentinelResource(value = "risk_event_detail_by_code", blockHandler = "handleReturn")
    @RequestMapping(value = "/risk_event_detail_by_code", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<RiskEventInfo> risk_event_detail_by_code(String plugin_code) {
        try {

            RiskEventInfo riskEventInfo = new RiskEventInfo();
            riskEventInfo.setEvent_code(plugin_code);
            riskEventInfo = riskEventMapper.selectOne(riskEventInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", riskEventInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 根据请求id获取命中的策略组实例id
     * @param request_id
     * @return
     */
    @SentinelResource(value = "risk_strategygroupinstance_by_request_id", blockHandler = "handleReturn")
    @RequestMapping(value = "/risk_strategygroupinstance_by_request_id", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<StrategyGroupInstance>> risk_strategygroupinstance_by_request_id(String request_id) {
        try {

            Example example = new Example(ZdhLogs.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("task_logs_id", request_id);
            List<ZdhLogs> zdhLogs = zdhLogsMapper.selectByExample(example);
            List<String> res = new ArrayList<>();
            if(zdhLogs != null && zdhLogs.size() > 0){
                res = zdhLogs.stream().map(s -> s.getJob_id()).collect(Collectors.toList());
            }
            List<StrategyGroupInstance> strategyGroupInstances = strategyGroupInstanceMapper.selectObjectByIds(strategyGroupInstanceMapper.getTable(), res.toArray(new String[]{}));

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", strategyGroupInstances);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 根据请求id获取命中的策略组实例id
     * @param request_id
     * @return
     */
    @SentinelResource(value = "risk_result_by_request_id_and_strategygroupinstance_id", blockHandler = "handleReturn")
    @RequestMapping(value = "/risk_result_by_request_id_and_strategygroupinstance_id", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<JSONObject> risk_result_by_request_id_and_strategygroupinstance_id(String request_id, String strategy_group_instance_id) {
        try {

            Example example = new Example(ZdhLogs.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("task_logs_id", request_id);
            criteria.andEqualTo("job_id", strategy_group_instance_id);
            List<ZdhLogs> zdhLogs = zdhLogsMapper.selectByExample(example);
            List<String> res = new ArrayList<>();
            JSONObject jsonObject = new JSONObject();
            if(zdhLogs != null && zdhLogs.size() > 0){
                jsonObject = JSONObject.parseObject(zdhLogs.get(0).getMsg());
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", jsonObject);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }


}
