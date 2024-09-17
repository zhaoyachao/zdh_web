package com.zyc.zdh.controller.digitalmarket;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.entity.CommonTreeInfo;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.*;

/**
 * 智能营销-服务控制
 */
@Controller
public class ServiceManagerController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisUtil redisUtil;

    public static String SERVICE_NAME_KEY = "ZDH_SERVER_MANAGER_SERVICE_NAME";

    public static String SERVICE_INSTANCE_KEY = "ZDH_SERVER_MANAGER_SERVICE_NAME:";

    public static String SERVICE_MODE_STOP = "stop";
    public static String SERVICE_MODE_SUSPEND = "suspend";
    public static String SERVICE_MODE_RUN = "run";


    /**
     * 服务控制首页
     * @return
     */
    @RequestMapping(value = "/service_manager_index", method = RequestMethod.GET)
    public String service_manager_index() {

        return "digitalmarket/service_manager_index";
    }

    /**
     * 服务控制首页
     * @return
     */
    @RequestMapping(value = "/service_manager_detail_index", method = RequestMethod.GET)
    public String service_manager_detail_index() {

        return "digitalmarket/service_manager_detail_index";
    }

    @RequestMapping(value = "/service_manager_slot_index", method = RequestMethod.GET)
    public String service_manager_slot_index() {

        return "digitalmarket/service_manager_slot_index";
    }

    /**
     * 查询服务
     * @return
     */
    @SentinelResource(value = "service_manager_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/service_manager_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<CommonTreeInfo>> service_manager_list() {
        try{

            List<CommonTreeInfo> commonTreeInfos = new ArrayList<>();
            Map<Object, Object> entries = redisUtil.getRedisTemplate().opsForHash().entries(SERVICE_NAME_KEY);
            Collection<Object> values = entries.keySet();

            if(values != null && values.size()>0){
                for (Object serviceName: values){
                    CommonTreeInfo commonTreeInfo = CommonTreeInfo.build(serviceName.toString(), "#", serviceName.toString(), "1");
                    commonTreeInfos.add(commonTreeInfo);
                }
            }

            return ReturnInfo.buildSuccess(commonTreeInfos);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("变量查询失败", e);
        }
    }

    /**
     * 查询服务
     * @return
     */
    @SentinelResource(value = "service_manager_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/service_manager_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<JSONObject>> service_manager_detail(String service_name) {
        try{

            List<JSONObject> serviceList = new ArrayList<>();

            Map<Object, Object> services = redisUtil.getRedisTemplate().opsForHash().entries(SERVICE_INSTANCE_KEY+""+service_name);
            for (Map.Entry<Object, Object> entry: services.entrySet()){

                JSONObject jsonObject = new JSONObject();
                String instanceId = entry.getKey().toString();

                Object serviceConfStr = redisUtil.getRedisTemplate().opsForHash().get(instanceId, "service");
                if(serviceConfStr == null){
                    continue;
                }

                JSONObject serviceConf = JSONObject.parseObject(serviceConfStr.toString());
                String host = serviceConf.getString("host");
                String pid = serviceConf.getString("pid");
                String register_time = serviceConf.getString("register_time");
                String last_time = serviceConf.getOrDefault("last_time", "").toString();


                jsonObject.put("service_name", service_name);
                jsonObject.put("instance_id", instanceId);

                register_time = DateUtil.formatTime(new Timestamp(Long.valueOf(register_time)));
                String content = "注册时间: "+ register_time;

                if(!StringUtils.isEmpty(last_time)){
                    last_time = DateUtil.formatTime(new Timestamp(Long.valueOf(last_time)));
                }

                content = content+" ,最后更新时间: "+last_time;

                Object status= redisUtil.getRedisTemplate().opsForHash().get(instanceId, "mode");
                if(status==null){
                    status = "未知";
                }
                content = content+" ,当前服务状态: "+status.toString();

                //占用槽位
                Object slot= redisUtil.getRedisTemplate().opsForHash().get(instanceId, "slot");

                if(slot==null){
                    slot = "未知";
                }

                jsonObject.put("slot_str", slot.toString());
                content = content+" ,服务槽位: "+slot.toString();

                jsonObject.put("data_context", content);

                serviceList.add(jsonObject);
            }

            return ReturnInfo.buildSuccess(serviceList);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("服务查询失败", e);
        }
    }

    /**
     * 暂停服务
     * @return
     */
    @SentinelResource(value = "service_manager_suspend", blockHandler = "handleReturn")
    @RequestMapping(value = "/service_manager_suspend", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<String> service_manager_suspend(String service_name, String instance_id) {
        try{

            redisUtil.getRedisTemplate().opsForHash().put(instance_id, "mode",SERVICE_MODE_SUSPEND);
            return ReturnInfo.buildSuccess();
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("服务暂停失败", e);
        }
    }

    /**
     * 停止服务
     * @return
     */
    @SentinelResource(value = "service_manager_stop", blockHandler = "handleReturn")
    @RequestMapping(value = "/service_manager_stop", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<JSONObject>> service_manager_stop(String service_name, String instance_id) {
        try{
            redisUtil.getRedisTemplate().opsForHash().put(instance_id, "mode",SERVICE_MODE_STOP);
            return ReturnInfo.buildSuccess();
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("服务停止失败", e);
        }
    }

    /**
     * 重启服务
     * @return
     */
    @SentinelResource(value = "service_manager_run", blockHandler = "handleReturn")
    @RequestMapping(value = "/service_manager_run", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<JSONObject>> service_manager_run(String service_name, String instance_id) {
        try{
            redisUtil.getRedisTemplate().opsForHash().put(instance_id, "mode",SERVICE_MODE_RUN);
            return ReturnInfo.buildSuccess();
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("服务重启失败", e);
        }
    }

    /**
     * 更新slot
     * @return
     */
    @SentinelResource(value = "service_manager_slot_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/service_manager_slot_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<JSONObject>> service_manager_slot_update(String slot, String instance_id) {
        try{

            String[] slots = slot.split(";");

            Object serviceStr = redisUtil.getRedisTemplate().opsForHash().get(instance_id, "service");
            if(serviceStr == null){
                throw new Exception("无法获取实例对应服务信息");
            }
            JSONObject serviceConf = JSONObject.parseObject(serviceStr.toString());
            String service_name = serviceConf.getString("service_name");

            //检查非当前实例是否有重复slot
            Map<Object, Object> services = redisUtil.getRedisTemplate().opsForHash().entries(SERVICE_INSTANCE_KEY+""+service_name);
            for (Map.Entry<Object, Object> entry: services.entrySet()) {

                String instanceId = entry.getKey().toString();
                if(instanceId.equalsIgnoreCase(instance_id)){
                    continue;
                }

                //任意的区间包含slots中值及可认为重复
                Object slotStr = redisUtil.getRedisTemplate().opsForHash().get(instanceId, "slot");
                if(slotStr == null){
                    continue;
                }

                String[] runSlots = slotStr.toString().split(",");

                if(Integer.valueOf(runSlots[0]) >= Integer.valueOf(slots[0]) && Integer.valueOf(runSlots[1]) <= Integer.valueOf(slots[0])){
                    throw new Exception("slot信息重复,命中实例: "+instanceId);
                }

                if(Integer.valueOf(runSlots[0]) >= Integer.valueOf(slots[1]) && Integer.valueOf(runSlots[1]) <= Integer.valueOf(slots[1])){
                    throw new Exception("slot信息重复,命中实例: "+instanceId);
                }

            }

            redisUtil.getRedisTemplate().opsForHash().put(instance_id, "slot", slot.replaceAll(";", ","));
            return ReturnInfo.buildSuccess();
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("更新槽位失败", e);
        }
    }
}
