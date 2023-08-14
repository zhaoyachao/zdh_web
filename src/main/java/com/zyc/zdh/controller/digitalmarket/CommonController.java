package com.zyc.zdh.controller.digitalmarket;

import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.CrowdFileMapper;
import com.zyc.zdh.dao.CrowdRuleMapper;
import com.zyc.zdh.dao.FilterMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
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
import java.util.Date;
import java.util.List;

/**
 * 智能营销-配置策略时使用的配置页面
 */
@Controller
public class CommonController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CrowdRuleMapper crowdRuleMapper;
    @Autowired
    private CrowdFileMapper crowdFileMapper;


    /**
     * 人群规则明细
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/crowd_rule_detail2", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo crowd_rule_detail2(String id) {
        try {
            CrowdRuleInfo crowdRuleInfo = crowdRuleMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", crowdRuleInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }


    /**
     * 规则模板页面
     * @return
     */
    @RequestMapping(value = "/rule_detail", method = RequestMethod.GET)
    public String rule_index() {

        return "digitalmarket/rule_detail";
    }

    /**
     * 人群文件模板页面
     * @return
     */
    @RequestMapping(value = "/crowd_file_detail", method = RequestMethod.GET)
    public String crowd_file_detail() {
        return "digitalmarket/crowd_file_detail";
    }

    /**
     * 人群运算模板页面
     * @return
     */
    @White()
    @RequestMapping(value = "/crowd_operate_detail", method = RequestMethod.GET)
    public String crowd_operate_detail() {
        return "digitalmarket/crowd_operate_detail";
    }

    /**
     * 人群规则模板页面
     * @return
     */
    @RequestMapping(value = "/crowd_rule_detail", method = RequestMethod.GET)
    public String crowd_rule_detail() {
        return "digitalmarket/crowd_rule_detail";
    }

    /**
     * 过滤模板页面
     * @return
     */
    @RequestMapping(value = "/filter_detail", method = RequestMethod.GET)
    public String filter_detail() {
        return "digitalmarket/filter_detail";
    }

    /**
     * 分流模板页面
     * @return
     */
    @RequestMapping(value = "/shunt_detail", method = RequestMethod.GET)
    public String shunt_detail() {
        return "digitalmarket/shunt_detail";
    }

    /**
     * 权益模板页面
     * @return
     */
    @RequestMapping(value = "/rights_detail", method = RequestMethod.GET)
    @White
    public String rights_detail() {
        return "digitalmarket/rights_detail";
    }

    /**
     * T+N页面
     * @return
     */
    @RequestMapping(value = "/tn_detail", method = RequestMethod.GET)
    @White
    public String tn_detail() {
        return "digitalmarket/tn_detail";
    }

    /**
     * 人工确认
     * manual_confirm_detail页面
     * @return
     */
    @RequestMapping(value = "/manual_confirm_detail", method = RequestMethod.GET)
    @White
    public String manual_confirm_detail() {
        return "digitalmarket/manual_confirm_detail";
    }

    /**
     * 人群文件列表
     * @param rule_context 关键字
     * @return
     */
    @RequestMapping(value = "/crowd_file_list_by_owner", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<List<CrowdFileInfo>> crowd_file_list_by_owner(String rule_context) {
        try{
            Example example=new Example(CrowdFileInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("owner", getOwner());

            List<CrowdFileInfo> crowdFileInfos = crowdFileMapper.selectByExample(example);

            return ReturnInfo.buildSuccess(crowdFileInfos);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("人群文件查询失败", e);
        }

    }

    public User getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

}
