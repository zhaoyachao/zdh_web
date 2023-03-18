package com.zyc.zdh.controller.digitalmarket;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.CrowdFileMapper;
import com.zyc.zdh.dao.CrowdRuleMapper;
import com.zyc.zdh.dao.FilterMapper;
import com.zyc.zdh.dao.LabelMapper;
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
 * 智能营销-标签服务
 */
@Controller
public class CrowdRuleController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CrowdRuleMapper crowdRuleMapper;
    @Autowired
    private CrowdFileMapper crowdFileMapper;
    @Autowired
    private FilterMapper filterMapper;

    /**
     * 人群规则列表首页
     * @return
     */
    @RequestMapping(value = "/crowd_rule_index", method = RequestMethod.GET)
    public String label_index() {

        return "digitalmarket/crowd_rule_index";
    }

    /**
     * 人群规则列表
     * @param rule_context 关键字
     * @return
     */
    @RequestMapping(value = "/crowd_rule_list", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String crowd_rule_list(String rule_context) {
        Example example=new Example(CrowdRuleInfo.class);
        Example.Criteria criteria=example.createCriteria();
        criteria.andEqualTo("is_delete", Const.NOT_DELETE);
        Example.Criteria criteria2=example.createCriteria();
        if(!StringUtils.isEmpty(rule_context)){
            criteria2.orLike("rule_context", getLikeCondition(rule_context));
            criteria2.orLike("rule_json", getLikeCondition(rule_context));
        }
        example.and(criteria2);

        List<CrowdRuleInfo> crowdRuleInfos = crowdRuleMapper.selectByExample(example);

        return JSONObject.toJSONString(crowdRuleInfos);
    }

    /**
     * 人群规则新增首页
     * @return
     */
    @RequestMapping(value = "/crowd_rule_add_index", method = RequestMethod.GET)
    public String crowd_rule_add_index() {

        return "digitalmarket/crowd_rule_add_index";
    }


    /**
     * 人群规则明细
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/crowd_rule_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo crowd_rule_detail(String id) {
        try {
            CrowdRuleInfo crowdRuleInfo = crowdRuleMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", crowdRuleInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 人群规则更新
     * @param crowdRuleInfo
     * @return
     */
    @RequestMapping(value = "/crowd_rule_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo crowd_rule_update(CrowdRuleInfo crowdRuleInfo) {
        try {

            CrowdRuleInfo oldCrowdRuleInfo = crowdRuleMapper.selectByPrimaryKey(crowdRuleInfo.getId());

            //crowdRuleInfo.setRule_json(jsonArray.toJSONString());
            crowdRuleInfo.setOwner(oldCrowdRuleInfo.getOwner());
            crowdRuleInfo.setCreate_time(oldCrowdRuleInfo.getCreate_time());
            crowdRuleInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            crowdRuleInfo.setIs_delete(Const.NOT_DELETE);
            crowdRuleMapper.updateByPrimaryKey(crowdRuleInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", crowdRuleInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 人群规则新增
     * @param crowdRuleInfo
     * @return
     */
    @RequestMapping(value = "/crowd_rule_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo crowd_rule_add(CrowdRuleInfo crowdRuleInfo) {
        try {

            crowdRuleInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            crowdRuleInfo.setOwner(getOwner());
            crowdRuleInfo.setIs_delete(Const.NOT_DELETE);
            crowdRuleInfo.setCreate_time(new Timestamp(new Date().getTime()));
            crowdRuleInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            crowdRuleMapper.insert(crowdRuleInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e.getMessage());
        }
    }

    /**
     * 人群规则删除
     * @param ids id数组
     * @return
     */
    @RequestMapping(value = "/crowd_rule_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo crowd_rule_delete(String[] ids) {
        try {
            crowdRuleMapper.deleteLogicByIds("crowd_rule_info",ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
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
     * 人群规则手动执行页面
     * @return
     */
    @RequestMapping(value = "/crowd_task_exe_detail_index", method = RequestMethod.GET)
    public String crowd_task_exe_detail_index() {

        return "digitalmarket/crowd_task_exe_detail_index";
    }

    /**
     * 手动执行人群规则,单独生成人群文件
     * @return
     */
    @RequestMapping(value = "/crowd_task_execute", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo crowd_task_execute() {

        return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "功能暂未支持", "功能暂未支持");
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
     * 人群文件列表
     * @param rule_context 关键字
     * @return
     */
    @RequestMapping(value = "/crowd_file_list_by_owner", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String crowd_file_list_by_owner(String rule_context) {
        try{
            Example example=new Example(CrowdFileInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("owner", getOwner());

            List<CrowdFileInfo> crowdFileInfos = crowdFileMapper.selectByExample(example);

            return JSONObject.toJSONString(crowdFileInfos);
        }catch (Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return "";
        }

    }

    public User getUser() {
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }

}
