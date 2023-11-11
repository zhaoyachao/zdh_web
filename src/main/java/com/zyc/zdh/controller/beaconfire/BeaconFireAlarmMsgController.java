package com.zyc.zdh.controller.beaconfire;

import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
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
import com.zyc.zdh.dao.BeaconFireAlarmMsgMapper;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 烽火台告警信息服务
 */
@Controller
public class BeaconFireAlarmMsgController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BeaconFireAlarmMsgMapper beaconFireAlarmMsgMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 烽火台告警信息列表首页
     * @return
     */
    @RequestMapping(value = "/beacon_fire_alarm_msg_index", method = RequestMethod.GET)
    @White
    public String beacon_fire_alarm_msg_index() {

        return "beaconfire/beacon_fire_alarm_msg_index";
    }

    /**
     * 烽火台告警信息列表
     * @param context 关键字
     * @return
     */
    @RequestMapping(value = "/beacon_fire_alarm_msg_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<PageResult<List<BeaconFireAlarmMsgInfo>>> beacon_fire_alarm_msg_list(String context,String product_code, String dim_group, int limit, int offset) {
        try{
            Example example=new Example(BeaconFireAlarmMsgInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
            criteria2.orLike("context", getLikeCondition(context));
            }
            example.and(criteria2);

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = beaconFireAlarmMsgMapper.selectCountByExample(example);

            List<BeaconFireAlarmMsgInfo> beaconFireAlarmMsgInfos = beaconFireAlarmMsgMapper.selectByExampleAndRowBounds(example, rowBounds);

            PageResult<List<BeaconFireAlarmMsgInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(beaconFireAlarmMsgInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("烽火台告警信息列表查询失败", e);
        }

    }

    /**
     * 烽火台告警信息删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/beacon_fire_alarm_msg_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo beacon_fire_alarm_msg_delete(String[] ids) {
        try {
            beaconFireAlarmMsgMapper.deleteLogicByIds("beacon_fire_alarm_msg_info",ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

}
