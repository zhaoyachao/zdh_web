package com.zyc.zdh.controller.zdhqueue;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zyc.queue.client.ConsumerHandlerImpl;
import com.zyc.queue.client.ProducerImpl;
import com.zyc.queue.core.QueueDataInfo;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.util.ConfigUtil;
import com.zyc.zdh.util.DBUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 优先级队列服务
 * 具体zdh_queue服务可参见zdh_queue项目
 */
@Controller
public class QueueController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment ev;

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
     * @param queue
     * @param limit
     * @param offset
     * @return
     */
    @RequestMapping(value = "/zdh_queue_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo zdh_queue_list(String queue,String limit, String offset) {
        try {

            String SQL="select a.* from queue_data_info a ";
            String COUNT="select count(1) as num from queue_data_info a";
            if(!StringUtils.isEmpty(queue) && !queue.contains(";")){
                SQL = SQL + " where queue_name='"+queue+"'";
                COUNT = COUNT + " where queue_name='"+queue+"'";
            }
            SQL = SQL + " order by priority desc  limit "+offset+" , "+ limit +" ";

            DBUtil dbUtil=new DBUtil();
            String url = ConfigUtil.getValue("queue.server.db.url");
            String driver = ConfigUtil.getValue("queue.server.db.driver");
            String user = ConfigUtil.getValue("queue.server.db.user");
            String password = ConfigUtil.getValue("queue.server.db.password");

            logger.info(SQL);
            Map<String,String> asParams=new HashMap<>();
            //asParams.put("request_id", "id");
            List<Map<String,Object>> result = dbUtil.R6(driver,url,user,password,SQL,asParams);
            List<Map<String,Object>> count = dbUtil.R5(driver,url,user,password,COUNT,new Object());
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("total", count.get(0).get("num"));
            jsonObject.put("rows", result);
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

            if(StringUtils.isEmpty(id)){
                throw new Exception("id参数必选");
            }
            String SQL="select a.* from queue_data_info a where id="+id;


            DBUtil dbUtil=new DBUtil();
            String url = ConfigUtil.getValue("queue.server.db.url");
            String driver = ConfigUtil.getValue("queue.server.db.driver");
            String user = ConfigUtil.getValue("queue.server.db.user");
            String password = ConfigUtil.getValue("queue.server.db.password");

            logger.info(SQL);
            Map<String,String> asParams=new HashMap<>();
            List<Map<String,Object>> result = dbUtil.R6(driver,url,user,password,SQL,asParams);

            return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "查询成功", result.get(0));
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
     * @param queueDataInfo
     * @return
     */
    @White
    @RequestMapping(value = "/zdh_queue_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String zdh_queue_add(QueueDataInfo queueDataInfo) {

        try{

            if(StringUtils.isEmpty(queueDataInfo.getQueue_name())){
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "修改失败", "queue_name参数为必选");
            }
            //获取数据信息

            String host = ConfigUtil.getValue("queue.server.host","127.0.0.1");
            int port = Integer.parseInt(ConfigUtil.getValue("queue.server.port","9001"));

            queueDataInfo.setRequest_id(UUID.randomUUID().toString());
            debugInfo(queueDataInfo);
            //调用zdh_queue
            ProducerImpl producer=new ProducerImpl();
            producer.init(host, port);
            producer.setQueue(queueDataInfo.getQueue_name());//必须设置,值为字符串,消费者根据此名字消费队列消息
            producer.setConsumerHandler(new ConsumerHandlerImpl());//已废弃,必须写,历史版本兼容使用
            if(!producer.is_connect(5)){
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "修改失败", "优先级队列链接失败");
            }
            int b=producer.send(queueDataInfo.getMsg(),queueDataInfo.getPriority(),5);
            if(b==0){
                return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "修改成功", queueDataInfo);
            }
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "修改失败", "优先级队列内部错误,请联系管理员解决");
        }catch (Exception e){
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "修改失败", e);
        }
    }

    /**
     * 更新优先级
     * @param queueDataInfo
     * @return
     */
    @RequestMapping(value = "/zdh_queue_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String zdh_queue_update(QueueDataInfo queueDataInfo) {

        try{

            if(StringUtils.isEmpty(queueDataInfo.getQueue_name())){
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "修改失败", "queue_name参数为必选");
            }
            //获取数据信息
            DBUtil dbUtil=new DBUtil();
            String url = ConfigUtil.getValue("queue.server.db.url");
            String driver = ConfigUtil.getValue("queue.server.db.driver");
            String user = ConfigUtil.getValue("queue.server.db.user");
            String password = ConfigUtil.getValue("queue.server.db.password");

            String host = ConfigUtil.getValue("queue.server.host","127.0.0.1");
            int port = Integer.parseInt(ConfigUtil.getValue("queue.server.port","9001"));

            String SQL="select a.* from queue_data_info a where id='"+queueDataInfo.getId()+"'";
            List<Map<String,Object>> result = dbUtil.R6(driver,url,user,password,SQL,null);

            if(result.size()<1){
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "修改失败", "获取消息队列消息异常");
            }

            System.out.println(JSON.toJSONString(result.get(0)));
            QueueDataInfo queueDataInfo1=new QueueDataInfo();
            queueDataInfo1.setHandler(result.get(0).get("handler").toString());
            queueDataInfo1.setRequest_id(result.get(0).get("request_id").toString());
            queueDataInfo1.setRequest_id(result.get(0).get("id").toString());
            queueDataInfo1.setMsg(result.get(0).get("msg").toString());
            queueDataInfo1.setQueue_name(result.get(0).get("queue_name").toString());
            queueDataInfo1.setPriority(queueDataInfo.getPriority());

            debugInfo(queueDataInfo1);
            //调用zdh_queue
            ProducerImpl producer=new ProducerImpl();
            producer.init(host, port);
            producer.setQueue(queueDataInfo.getQueue_name());//必须设置,值为字符串,消费者根据此名字消费队列消息
            producer.setConsumerHandler(new ConsumerHandlerImpl());//已废弃,必须写,历史版本兼容使用
            if(!producer.is_connect(5)){
                return ReturnInfo.createInfo(RETURN_CODE.FAIL.getCode(), "修改失败", "优先级队列链接失败");
            }
            int b=producer.resetPriority(queueDataInfo1,5);
            producer.close();
            if(b==0){
                return ReturnInfo.createInfo(RETURN_CODE.SUCCESS.getCode(), "修改成功", queueDataInfo1);
            }
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
