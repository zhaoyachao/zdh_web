package com.zyc.zdh.controller.digitalmarket;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.LabelMapper;
import com.zyc.zdh.entity.LabelInfo;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.Const;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 智能营销-客户管理
 */
@Controller
public class CustomerManageController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private LabelMapper labelMapper;

    /**
     * 客户画像首页
     * @return
     */
    @RequestMapping(value = "/customer_portrait_index", method = RequestMethod.GET)
    public String label_index() {

        return "digitalmarket/customer_portrait_index";
    }

    /**
     * 客户画像明细
     * @param product_code 产品
     * @param uid 用户id
     * @return
     */
    @SentinelResource(value = "customer_portrait_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/customer_portrait_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<Map> variable_detail(String product_code, String uid) {
        try{
            LabelInfo labelInfo = new LabelInfo();
            labelInfo.setIs_delete(Const.NOT_DELETE);
            labelInfo.setLabel_use_type("single");
            labelInfo.setStatus("1");

            List<LabelInfo> labelInfoList = labelMapper.select(labelInfo);

            Map<String, LabelInfo> labelInfoMap = labelInfoList.stream()
                    .collect(Collectors.toMap(LabelInfo::getLabel_code,  Function.identity()));


            String variable_uid=product_code+"_tag_"+uid;
            Map<Object, Object> variable= redisUtil.getRedisTemplate().opsForHash().entries(variable_uid);
            if(variable != null && variable.size()>0){


                for (Object key: variable.keySet()){
                    String value = variable.get(key).toString();
                    JSONObject jsonObject = JSONObject.parseObject(value);
                    if(labelInfoMap.containsKey(key.toString())){
                        jsonObject.put("label_name", labelInfoMap.get(key.toString()).getLabel_context());
                    }
                    variable.put(key, jsonObject);
                }
            }
            return ReturnInfo.buildSuccess(variable);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("变量查询失败", e);
        }
    }

}
