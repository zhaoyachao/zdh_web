package com.zyc.zdh.controller.digitalmarket;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.CrowdFileMapper;
import com.zyc.zdh.dao.CrowdRuleMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * 智能营销-配置策略时使用的配置页面
 */
@Controller
public class CommonController extends BaseController {

    @Autowired
    private CrowdRuleMapper crowdRuleMapper;
    @Autowired
    private CrowdFileMapper crowdFileMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;


    /**
     * 策略参数页面
     * @return
     */
    @RequestMapping(value = "/strategy_params_index", method = RequestMethod.GET)
    public String strategy_params_index() {

        return "digitalmarket/strategy_params_index";
    }

    /**
     * 人群规则明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "crowd_rule_detail2", blockHandler = "handleReturn")
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
     * 用户池页面
     *
     * @return
     */
    @RequestMapping(value = "/user_pool_detail", method = RequestMethod.GET)
    public String user_pool_detail() {
        return "digitalmarket/user_pool_detail";
    }

    /**
     * 人群运算模板页面
     * @return
     */
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
    public String rights_detail() {
        return "digitalmarket/rights_detail";
    }

    /**
     * T+N页面
     * @return
     */
    @RequestMapping(value = "/tn_detail", method = RequestMethod.GET)
    public String tn_detail() {
        return "digitalmarket/tn_detail";
    }

    /**
     * 人工确认
     * manual_confirm_detail页面
     * @return
     */
    @RequestMapping(value = "/manual_confirm_detail", method = RequestMethod.GET)
    public String manual_confirm_detail() {
        return "digitalmarket/manual_confirm_detail";
    }

    /**
     * 代码块
     * code_block_detail页面
     * @return
     */
    @RequestMapping(value = "/code_block_detail", method = RequestMethod.GET)
    public String code_block_detail() {
        return "digitalmarket/code_block_detail";
    }


    /**
     * 自定义名单
     * custom_list_detail页面
     * @return
     */
    @RequestMapping(value = "/custom_list_detail", method = RequestMethod.GET)
    public String custom_list_detail() {
        return "digitalmarket/custom_list_detail";
    }

    /**
     * 代码块-代码例子页面
     *
     * @return
     */
    @RequestMapping(value = "/code_block_demo_index", method = RequestMethod.GET)
    public String code_block_demo_index() {
        return "digitalmarket/code_block_demo_index";
    }

    /**
     * 变量池页面
     *
     * @return
     */
    @RequestMapping(value = "/varpool_detail", method = RequestMethod.GET)
    public String varpool_detail() {
        return "digitalmarket/varpool_detail";
    }

    /**
     * 变量计算页面
     *
     * @return
     */
    @RequestMapping(value = "/variable_detail", method = RequestMethod.GET)
    public String variable_detail() {
        return "digitalmarket/variable_detail";
    }


    /**
     * 人群文件列表
     * @param rule_context 关键字
     * @return
     */
    @SentinelResource(value = "crowd_file_list_by_owner", blockHandler = "handleReturn")
    @RequestMapping(value = "/crowd_file_list_by_owner", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<CrowdFileInfo>> crowd_file_list_by_owner(String rule_context) {
        try{
            Example example=new Example(CrowdFileInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("owner", getOwner());

            List<CrowdFileInfo> crowdFileInfos = crowdFileMapper.selectByExample(example);

            return ReturnInfo.buildSuccess(crowdFileInfos);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("人群文件查询失败", e);
        }

    }


    /**
     * 获取当前登录用户的归属组
     * @return
     */
    @SentinelResource(value = "get_dim_group", blockHandler = "handleReturn")
    @RequestMapping(value = "/get_dim_group", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionDimensionValueInfo>> get_dim_group() {
        try {
            String owner = getOwner();
            List<PermissionDimensionValueInfo> result = zdhPermissionService.get_dim_group(getProductCode(), getOwner(), getUserGroup());

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", result);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 获取当前登录用户的归属产品
     * @return
     */
    @SentinelResource(value = "get_dim_product", blockHandler = "handleReturn")
    @RequestMapping(value = "/get_dim_product", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PermissionDimensionValueInfo>> get_dim_product() {
        try {
            String owner = getOwner();
            List<PermissionDimensionValueInfo> result = zdhPermissionService.get_dim_product(getProductCode(), getOwner(), getUserGroup());

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", result);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

}
