package com.zyc.zdh.controller;

import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.shiro.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * redis服务
 */
@Controller
public class ZdhRedisApiController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisUtil redisUtil;


    /**
     * 获取参数
     * @param key
     * @return
     */
    @RequestMapping(value = "/redis/get/{key}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<String> get_key(@PathVariable("key") String key) {
        if (redisUtil.exists(key)) {
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", redisUtil.get(key).toString());
        }
        return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", "没有找到key: " + key);
    }

    /**
     * 删除参数
     * @param key
     * @return
     */
    @RequestMapping(value = "/redis/del/{key}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo del_key(@PathVariable("key") String key) {
        if (redisUtil.exists(key)) {
            redisUtil.remove(key);
        }
        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
    }

    /**
     * 获取所有参数名
     * @return
     */
    @RequestMapping(value = "/redis/keys", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Set<String>> keys() {
        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", redisUtil.keys());
    }

    /**
     * 新增参数
     * @param key
     * @param value
     * @return
     */
    @RequestMapping(value = "/redis/add/{key}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo add_key(@PathVariable("key") String key, String value) {
        redisUtil.set(key, value);
        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
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
