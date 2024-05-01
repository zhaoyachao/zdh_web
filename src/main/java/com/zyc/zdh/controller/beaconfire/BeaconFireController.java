package com.zyc.zdh.controller.beaconfire;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.BeaconFireMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.JobModel;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.quartz.QuartzManager2;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
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
 * 烽火台信息服务
 */
@Controller
public class BeaconFireController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private BeaconFireMapper beaconFireMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;
    @Autowired
    private QuartzManager2 quartzManager2;

    /**
     * 烽火台信息列表首页
     * @return
     */
    @RequestMapping(value = "/beacon_fire_index", method = RequestMethod.GET)
    public String beacon_fire_index() {

        return "beaconfire/beacon_fire_index";
    }

    /**
     * 烽火台信息列表
     * @param context 关键字
     * @return
     */
    @SentinelResource(value = "beacon_fire_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/beacon_fire_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<BeaconFireInfo>>> beacon_fire_list(String context,String product_code, String dim_group, int limit, int offset) {
        try{
            Example example=new Example(BeaconFireInfo.class);
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
            int total = beaconFireMapper.selectCountByExample(example);

            List<BeaconFireInfo> beaconFireInfos = beaconFireMapper.selectByExampleAndRowBounds(example, rowBounds);

            PageResult<List<BeaconFireInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(beaconFireInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("烽火台信息列表查询失败", e);
        }

    }

    /**
     * 烽火台信息新增首页
     * @return
     */
    @RequestMapping(value = "/beacon_fire_add_index", method = RequestMethod.GET)
    public String beacon_fire_add_index() {

        return "beaconfire/beacon_fire_add_index";
    }

    /**
     * 烽火台-脚本demo首页
     * @return
     */
    @RequestMapping(value = "/beacon_fire_demo_index", method = RequestMethod.GET)
    public String beacon_fire_demo_index() {

        return "beaconfire/beacon_fire_demo_index";
    }

    /**
     * xx明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "beacon_fire_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/beacon_fire_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<BeaconFireInfo> beacon_fire_detail(String id) {
        try {
            BeaconFireInfo beaconFireInfo = beaconFireMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", beaconFireInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 烽火台信息更新
     * @param beaconFireInfo
     * @return
     */
    @SentinelResource(value = "beacon_fire_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/beacon_fire_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<BeaconFireInfo> beacon_fire_update(BeaconFireInfo beaconFireInfo) {
        try {

            BeaconFireInfo oldBeaconFireInfo = beaconFireMapper.selectByPrimaryKey(beaconFireInfo.getId());
            checkPermissionByProductAndDimGroup(zdhPermissionService, beaconFireInfo.getProduct_code(), beaconFireInfo.getDim_group());
            checkPermissionByProductAndDimGroup(zdhPermissionService, oldBeaconFireInfo.getProduct_code(), oldBeaconFireInfo.getDim_group());

            beaconFireInfo.setCreate_time(oldBeaconFireInfo.getCreate_time());
            beaconFireInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            beaconFireInfo.setIs_delete(Const.NOT_DELETE);
            beaconFireMapper.updateByPrimaryKeySelective(beaconFireInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", beaconFireInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 烽火台信息新增
     * @param beaconFireInfo
     * @return
     */
    @SentinelResource(value = "beacon_fire_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/beacon_fire_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<BeaconFireInfo> beacon_fire_add(BeaconFireInfo beaconFireInfo) {
        try {
            beaconFireInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            beaconFireInfo.setOwner(getOwner());
            beaconFireInfo.setIs_delete(Const.NOT_DELETE);
            beaconFireInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            beaconFireInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkPermissionByProductAndDimGroup(zdhPermissionService, beaconFireInfo.getProduct_code(), beaconFireInfo.getDim_group());

            beaconFireMapper.insertSelective(beaconFireInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", beaconFireInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 烽火台信息删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "beacon_fire_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/beacon_fire_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo beacon_fire_delete(String[] ids) {
        try {
            checkPermissionByProductAndDimGroup(zdhPermissionService, beaconFireMapper, beaconFireMapper.getTable(), ids);
            beaconFireMapper.deleteLogicByIds(beaconFireMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    /**
     * 告警任务-开启/关闭
     * @param id
     * @param status
     * @return
     */
    @SentinelResource(value = "beacon_fire_status", blockHandler = "handleReturn")
    @RequestMapping(value = "/beacon_fire_status", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo beacon_fire_status(String id, String status) {
        try {
            BeaconFireInfo beaconFireInfo = new BeaconFireInfo();
            beaconFireInfo.setId(id);
            beaconFireInfo.setStatus(status);
            beaconFireMapper.updateByPrimaryKeySelective(beaconFireInfo);

            if(status.equalsIgnoreCase(Const.ON)){
                BeaconFireInfo oldBeaconFireInfo = beaconFireMapper.selectByPrimaryKey(id);
                String expr = oldBeaconFireInfo.getExpr();
                QuartzJobInfo quartzJobInfo = quartzManager2.createQuartzJobInfo("BEACONFIRE", JobModel.REPEAT.getValue(), new Date(), new Date(), "检查失败重试任务", expr, "-1", "", "beaconfire");
                quartzJobInfo.setJob_id(beaconFireInfo.getId());
                quartzManager2.addBeaconFireTaskToQuartz(quartzJobInfo);
            }else{
                quartzManager2.deleteTask(beaconFireInfo.getId(), "beaconfire");
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", beaconFireInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

}
