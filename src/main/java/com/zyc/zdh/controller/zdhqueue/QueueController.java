package com.zyc.zdh.controller.zdhqueue;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.zyc.rqueue.RQueueClient;
import com.zyc.rqueue.RQueueManager;
import com.zyc.rqueue.RQueueMode;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 优先级队列服务
 * 具体zdh_rqueue服务可参见zdh_rqueue项目
 */
@Controller
public class QueueController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 队列消息列表首页
     * @return
     */
    @RequestMapping(value = "/zdh_queue_index", method = RequestMethod.GET)
    public String zdh_queue_index() {

        return "queue/zdh_queue_index";
    }

    /**
     * 队列消息列表
     * @param queue_name
     * @param limit
     * @param offset
     * @return
     */
    @RequestMapping(value = "/zdh_queue_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo zdh_queue_list(String queue_name,String limit, String offset) {
        try {
            JSONObject jsonObject=new JSONObject();
            if(StringUtils.isEmpty(queue_name)){

                jsonObject.put("total", 0);
                jsonObject.put("rows", new ArrayList<>());
                return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", jsonObject);
            }
            RQueueClient<String> rQueueClient = RQueueManager.getRQueueClient(queue_name, RQueueMode.PRIORITYQUEUE);

            Iterator iterator = rQueueClient.iterator();
            List<String> ret = new ArrayList<>();
            if(iterator != null){
                ret = Lists.newArrayList(iterator);
            }

            List<String> res =ret.subList( Integer.valueOf(offset), Integer.valueOf(offset)+ Integer.valueOf(limit));
            List<JSONObject> rows = new ArrayList<>();
            for (String value: res){
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("queue_name", queue_name);
                jsonObject1.put("msg", JSONObject.parseObject(value).getString("t"));
                jsonObject1.put("priority", JSONObject.parseObject(value).getString("priority"));
                rows.add(jsonObject1);
            }
            jsonObject.put("total", ret.size());
            jsonObject.put("rows", rows);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", jsonObject);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }

    /**
     * 队列消息明细
     * @param id 主键ID
     * @return
     */
    @White
    @RequestMapping(value = "/zdh_queue_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String zdh_queue_detail(String id) {
        try {

            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "查询失败", "当前接口废弃");
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }

    /**
     * 队列新增消息首页
     * @return
     */
    @RequestMapping(value = "/zdh_queue_add_index", method = RequestMethod.GET)
    public String zdh_queue_add_index() {

        return "queue/zdh_queue_add_index";
    }

    /**
     * 新增队列消息
     * @param queue_name
     * @param msg
     * @param priority
     * @return
     */
    @White
    @RequestMapping(value = "/zdh_queue_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String zdh_queue_add(String queue_name,String msg ,String priority) {

        try{

            if(StringUtils.isEmpty(queue_name)){
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "修改失败", "queue_name参数为必选");
            }
            //获取数据信息

            RQueueClient<String> rQueueClient = RQueueManager.getRQueueClient(queue_name, RQueueMode.PRIORITYQUEUE);

            if(StringUtils.isEmpty(priority)){
                priority="10";
            }
            boolean b = rQueueClient.offer(msg, Integer.valueOf(priority));


            if(b){
                return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "修改成功", "");
            }
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "修改失败", "优先级队列内部错误,请联系管理员解决");
        }catch (Exception e){
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "修改失败", e);
        }
    }

    /**
     * 更新优先级(废弃)
     * @param queue_name
     * @param msg
     * @param priority
     * @return
     */
    @RequestMapping(value = "/zdh_queue_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String zdh_queue_update(String queue_name,String msg ,String priority) {

        try{
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "修改失败", "优先级队列内部错误,请联系管理员解决");
        }catch (Exception e){
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "修改失败", e);
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
