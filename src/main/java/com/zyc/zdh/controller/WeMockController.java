package com.zyc.zdh.controller;

import com.zyc.zdh.annotation.White;
import com.zyc.zdh.dao.WeMockTreeMapper;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.entity.WeMockTreeInfo;
import com.zyc.zdh.service.JemailService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * mock数据服务
 */
@Controller
public class WeMockController extends BaseController{

    @Autowired
    Environment ev;

    @Autowired
    WeMockTreeMapper weMockTreeMapper;

    /**
     * mock数据首页
     * @return
     */
    @White
    @RequestMapping("/wemock_index")
    public String wemock_index() {

        return "wemock/wemock_index";
    }

    /**
     * mock数据新增页面
     * @return
     */
    @RequestMapping("/wemock_jstree_add_index")
    public String wemock_add_index() {

        return "wemock/wemock_jstree_add_index";
    }


    @White
    @RequestMapping(value = "/wemock_jstree_node", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<WeMockTreeInfo>> wemock_jstree_node(String product_code) {

        try{
            if(StringUtils.isEmpty(product_code)){
                throw new Exception("参数产品代码必填");
            }
            Example example=new Example(WeMockTreeInfo.class);

            Example.Criteria criteria=example.createCriteria();

            criteria.andEqualTo("product_code", product_code);

            List<WeMockTreeInfo> weMockTreeInfos=weMockTreeMapper.selectByExample(example);
            weMockTreeInfos.sort(Comparator.comparing(WeMockTreeInfo::getOrderN));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", weMockTreeInfos);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * mock数据更新
     * @param context
     * @param receiver
     * @param subject
     * @return
     */
    @RequestMapping(value = "/wemock_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo wemock_update(String context,String receiver,String subject) {

        try{

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "发送成功", "请检查邮箱是否收到发送成功通知,如果5分钟内没由收到邮件,则可能发送邮件失败,请尝试再次发信");
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "发送失败", e);
        }
    }

    /**
     * mock数据新增
     * @param context
     * @param receiver
     * @param subject
     * @return
     */
    @RequestMapping(value = "/wemock_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo wemock_add(String context,String receiver,String subject) {

        try{

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "发送成功", "请检查邮箱是否收到发送成功通知,如果5分钟内没由收到邮件,则可能发送邮件失败,请尝试再次发信");
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "发送失败", e);
        }
    }

    /**
     * mock数据删除
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/wemock_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo wemock_delete(String[] id) {

        try{
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "发送成功", "请检查邮箱是否收到发送成功通知,如果5分钟内没由收到邮件,则可能发送邮件失败,请尝试再次发信");
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "发送失败", e);
        }
    }

}
