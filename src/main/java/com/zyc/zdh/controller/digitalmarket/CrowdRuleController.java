package com.zyc.zdh.controller.digitalmarket;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.CrowdRuleMapper;
import com.zyc.zdh.entity.CrowdRuleInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.JsonUtil;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;

/**
 * 智能营销-客群规则服务
 * 废弃
 */
@Controller
public class CrowdRuleController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CrowdRuleMapper crowdRuleMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

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
     * 废弃
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
            example.and(criteria2);
        }

        List<CrowdRuleInfo> crowdRuleInfos = crowdRuleMapper.selectByExample(example);

        return JsonUtil.formatJsonString(crowdRuleInfos);
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
     * 人群规则更新
     * @param crowdRuleInfo
     * @return
     */
    @SentinelResource(value = "crowd_rule_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/crowd_rule_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo crowd_rule_update(CrowdRuleInfo crowdRuleInfo) {
        try {

            CrowdRuleInfo oldCrowdRuleInfo = crowdRuleMapper.selectByPrimaryKey(crowdRuleInfo.getId());

//            checkPermissionByProductAndDimGroup(zdhPermissionService, crowdRuleInfo.getProduct_code(), crowdRuleInfo.getDim_group());
//            checkPermissionByProductAndDimGroup(zdhPermissionService, oldCrowdRuleInfo.getProduct_code(), oldCrowdRuleInfo.getDim_group());

            //crowdRuleInfo.setRule_json(jsonArray.toJSONString());
            crowdRuleInfo.setOwner(oldCrowdRuleInfo.getOwner());
            crowdRuleInfo.setCreate_time(oldCrowdRuleInfo.getCreate_time());
            crowdRuleInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            crowdRuleInfo.setIs_delete(Const.NOT_DELETE);
            crowdRuleMapper.updateByPrimaryKeySelective(crowdRuleInfo);

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
    @SentinelResource(value = "crowd_rule_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/crowd_rule_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo crowd_rule_add(CrowdRuleInfo crowdRuleInfo) {
        try {

            crowdRuleInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            crowdRuleInfo.setOwner(getOwner());
            crowdRuleInfo.setIs_delete(Const.NOT_DELETE);
            crowdRuleInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            crowdRuleInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            //checkPermissionByProductAndDimGroup(zdhPermissionService, crowdRuleInfo.getProduct_code(), crowdRuleInfo.getDim_group());

            crowdRuleMapper.insertSelective(crowdRuleInfo);
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
    @SentinelResource(value = "crowd_rule_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/crowd_rule_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo crowd_rule_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, crowdRuleMapper, crowdRuleMapper.getTable(), ids, getAttrDel());
            crowdRuleMapper.deleteLogicByIds(crowdRuleMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
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
    @SentinelResource(value = "crowd_task_execute", blockHandler = "handleReturn")
    @RequestMapping(value = "/crowd_task_execute", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo crowd_task_execute() {

        return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "功能暂未支持", "功能暂未支持");
    }


}
