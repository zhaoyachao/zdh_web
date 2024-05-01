package com.zyc.zdh.controller.digitalmarket;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.LabelMapper;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.shiro.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

/**
 * 智能营销-变量服务
 */
@Controller
public class VariableController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private LabelMapper labelMapper;

    /**
     * 变量首页
     * @return
     */
    @RequestMapping(value = "/variable_index", method = RequestMethod.GET)
    public String label_index() {

        return "digitalmarket/variable_index";
    }

    /**
     * 查询变量值
     * @param product_code 产品
     * @param uid 用户id
     * @param variable_code 变量code
     * @return
     */
    @SentinelResource(value = "variable_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/variable_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Map> variable_detail(String product_code, String uid, String variable_code) {
        try{
            String variable_uid=product_code+"_tag_"+uid;
            String params = redisUtil.getRedisTemplate().opsForHash().get(variable_uid, variable_code).toString();

            Map map = JSONObject.parseObject(params, Map.class);
            return ReturnInfo.buildSuccess(map);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("变量查询失败", e);
        }
    }

    /**
     * 变量新增首页
     * @return
     */
    @RequestMapping(value = "/variable_add_index", method = RequestMethod.GET)
    public String variable_add_index() {

        return "digitalmarket/variable_add_index";
    }


    /**
     * 变量更新
     * @param product_code 产品
     * @param uid 用户id
     * @param variable_code 变量code
     * @param param json结构的字符串
     * @return
     */
    @SentinelResource(value = "variable_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/variable_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo variable_update(String product_code, String uid, String variable_code, String param) {
        try {
            String variable_uid=product_code+"_tag_"+uid;
            redisUtil.getRedisTemplate().opsForHash().put(variable_uid, variable_code, param);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", "");
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 标签删除
     * @param product_code 产品
     * @param uid 用户id
     * @param variable_code 变量code
     * @return
     */
    @SentinelResource(value = "variable_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/variable_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo label_delete(String product_code, String uid, String variable_code) {
        try {
            String variable_uid=product_code+"_tag_"+uid;
            redisUtil.getRedisTemplate().opsForHash().delete(variable_uid, variable_code);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    /**
     * 查询所有变量
     * @param product_code 产品
     * @param uid 用户id
     * @return
     */
    @SentinelResource(value = "variable_all", blockHandler = "handleReturn")
    @RequestMapping(value = "/variable_all", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Map> variable_all(String product_code, String uid) {
        try{
            String variable_uid=product_code+"_tag_"+uid;

            //根据product_code获取所有变量

            Map<Object, Object> variable= redisUtil.getRedisTemplate().opsForHash().entries(variable_uid);

            return ReturnInfo.buildSuccess(variable);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("变量查询失败", e);
        }
    }

}
