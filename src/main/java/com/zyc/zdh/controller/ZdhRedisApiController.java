package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
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

import java.util.Set;

/**
 * redis服务
 */
@Controller
public class ZdhRedisApiController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisUtil redisUtil;


    /**
     * 获取参数
     * @param key
     * @return
     */
    @SentinelResource(value = "redis_get", blockHandler = "handleReturn")
    @RequestMapping(value = "/redis/get/{key}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<String> get_key(@PathVariable("key") String key) {
        if (redisUtil.exists(key)) {
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", redisUtil.get(key).toString());
        }
        return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", "没有找到key: " + key);
    }

    /**
     * 获取参数
     * @param key
     * @return
     */
    @SentinelResource(value = "redis_get2", blockHandler = "handleReturn")
    @RequestMapping(value = "/redis/get2/{key}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<String> get_bound_value(@PathVariable("key") String key) {
        if (redisUtil.exists(key)) {
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", redisUtil.getBoundValueOps(key).toString());
        }
        return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", "没有找到key: " + key);
    }

    /**
     * 删除参数
     * @param key
     * @return
     */
    @SentinelResource(value = "redis_del", blockHandler = "handleReturn")
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
    @SentinelResource(value = "redis_keys", blockHandler = "handleReturn")
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
    @SentinelResource(value = "redis_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/redis/add/{key}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo add_key(@PathVariable("key") String key, String value) {
        redisUtil.set(key, value);
        return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
    }

}
