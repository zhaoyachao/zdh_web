package com.zyc.zdh.controller.beaconfire;

import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.BeaconFireAlarmGroupMapper;
import com.zyc.zdh.entity.BeaconFireAlarmGroupInfo;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 烽火台告警组信息服务
 */
@Controller
public class BeaconFireAlarmGroupController extends BaseController {

    @Autowired
    private BeaconFireAlarmGroupMapper beaconFireAlarmGroupMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 烽火台告警组信息列表首页
     * @return
     */
    @RequestMapping(value = "/beacon_fire_alarm_group_index", method = RequestMethod.GET)
    public String beacon_fire_alarm_group_index() {

        return "beaconfire/beacon_fire_alarm_group_index";
    }

    /**
     * 烽火台告警组信息列表
     * @param context 关键字
     * @return
     */
    @RequestMapping(value = "/beacon_fire_alarm_group_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<BeaconFireAlarmGroupInfo>>> beacon_fire_alarm_group_list(String context,String product_code, String dim_group, int limit, int offset) {
        try{
            Example example=new Example(BeaconFireAlarmGroupInfo.class);
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
                example.and(criteria2);
            }

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = beaconFireAlarmGroupMapper.selectCountByExample(example);

            List<BeaconFireAlarmGroupInfo> beaconFireAlarmGroupInfos = beaconFireAlarmGroupMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, beaconFireAlarmGroupInfos);

            PageResult<List<BeaconFireAlarmGroupInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(beaconFireAlarmGroupInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("烽火台告警组信息列表查询失败", e);
        }

    }

    /**
     * 烽火台告警组信息新增首页
     * @return
     */
    @RequestMapping(value = "/beacon_fire_alarm_group_add_index", method = RequestMethod.GET)
    public String beacon_fire_alarm_group_add_index() {

        return "beaconfire/beacon_fire_alarm_group_add_index";
    }

    /**
     * xx明细
     * @param id 主键ID
     * @return
     */
    @RequestMapping(value = "/beacon_fire_alarm_group_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<BeaconFireAlarmGroupInfo> beacon_fire_alarm_group_detail(String id) {
        try {
            BeaconFireAlarmGroupInfo beaconFireAlarmGroupInfo = beaconFireAlarmGroupMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", beaconFireAlarmGroupInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 烽火台告警组信息更新
     * @param beaconFireAlarmGroupInfo
     * @return
     */
    @RequestMapping(value = "/beacon_fire_alarm_group_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<BeaconFireAlarmGroupInfo> beacon_fire_alarm_group_update(BeaconFireAlarmGroupInfo beaconFireAlarmGroupInfo,String[] phone, String[] sms, String[] email) {
        try {

            BeaconFireAlarmGroupInfo oldBeaconFireAlarmGroupInfo = beaconFireAlarmGroupMapper.selectByPrimaryKey(beaconFireAlarmGroupInfo.getId());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, beaconFireAlarmGroupInfo.getProduct_code(), beaconFireAlarmGroupInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldBeaconFireAlarmGroupInfo.getProduct_code(), oldBeaconFireAlarmGroupInfo.getDim_group(), getAttrEdit());

            Map<String, String> tmp = new HashMap<>();
            tmp.put("phone", StringUtils.join(phone, ","));
            tmp.put("sms", StringUtils.join(sms, ","));
            tmp.put("email", StringUtils.join(email, ","));

            beaconFireAlarmGroupInfo.setAlarm_config(JsonUtil.formatJsonString(tmp));

            beaconFireAlarmGroupInfo.setCreate_time(oldBeaconFireAlarmGroupInfo.getCreate_time());
            beaconFireAlarmGroupInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            beaconFireAlarmGroupInfo.setIs_delete(Const.NOT_DELETE);
            beaconFireAlarmGroupMapper.updateByPrimaryKeySelective(beaconFireAlarmGroupInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", beaconFireAlarmGroupInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 烽火台告警组信息新增
     * @param beaconFireAlarmGroupInfo
     * @return
     */
    @RequestMapping(value = "/beacon_fire_alarm_group_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<BeaconFireAlarmGroupInfo> beacon_fire_alarm_group_add(BeaconFireAlarmGroupInfo beaconFireAlarmGroupInfo,String[] phone, String[] sms, String[] email) {
        try {

            Map<String, String> tmp = new HashMap<>();
            tmp.put("phone", StringUtils.join(phone, ","));
            tmp.put("sms", StringUtils.join(sms, ","));
            tmp.put("email", StringUtils.join(email, ","));

            beaconFireAlarmGroupInfo.setAlarm_config(JsonUtil.formatJsonString(tmp));
            beaconFireAlarmGroupInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            beaconFireAlarmGroupInfo.setOwner(getOwner());
            beaconFireAlarmGroupInfo.setIs_delete(Const.NOT_DELETE);
            beaconFireAlarmGroupInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            beaconFireAlarmGroupInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, beaconFireAlarmGroupInfo.getProduct_code(), beaconFireAlarmGroupInfo.getDim_group(), getAttrAdd());
            beaconFireAlarmGroupMapper.insertSelective(beaconFireAlarmGroupInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", beaconFireAlarmGroupInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 烽火台告警组信息删除
     * @param ids
     * @return
     */
    @RequestMapping(value = "/beacon_fire_alarm_group_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo beacon_fire_alarm_group_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, beaconFireAlarmGroupMapper, beaconFireAlarmGroupMapper.getTable(), ids, getAttrDel());
            beaconFireAlarmGroupMapper.deleteLogicByIds(beaconFireAlarmGroupMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    /**
     * 烽火台告警组信息列表
     * @param product_code 产品code
     * @param dim_group 归属组
     * @return
     */
    @RequestMapping(value = "/beacon_fire_alarm_group_list_by_owner", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<BeaconFireAlarmGroupInfo>> beacon_fire_alarm_group_list_by_owner(String product_code, String dim_group) {
        try{
            Example example=new Example(BeaconFireAlarmGroupInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);

            //criteria.andEqualTo("product_code", product_code);
            //criteria.andEqualTo("dim_group", dim_group);

            List<BeaconFireAlarmGroupInfo> beaconFireAlarmGroupInfos = beaconFireAlarmGroupMapper.selectByExample(example);

            return ReturnInfo.buildSuccess(beaconFireAlarmGroupInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("烽火台告警组信息列表查询失败", e);
        }

    }

}
