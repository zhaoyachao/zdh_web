package com.zyc.zdh.controller.zdhpush;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.PushConfigMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.PushConfigInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
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
import java.util.List;

/**
 * push 通用配置服务
 *
 * 使用权限控制需要PushConfigInfo 继承BaseProductAuthInfo 或者 BaseProductAndDimGroupAuthInfo
 */
@Controller
public class PushConfigController extends BaseController {

    @Autowired
    private PushConfigMapper pushConfigMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * push 通用配置列表首页
     * @return
     */
    @RequestMapping(value = "/push_config_index", method = RequestMethod.GET)
    public String push_config_index() {

        return "push/push_config_index";
    }

    /**
     * push 通用配置列表
     * @param context 关键字
     * @param product_code 产品
     * @return
     */
    @SentinelResource(value = "push_config_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_config_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<PushConfigInfo>> push_config_list(String context, String product_code) {
        try{
            Example example=new Example(PushConfigInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            //dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);
            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("config_key", getLikeCondition(context));
                criteria2.orLike("config_name", getLikeCondition(context));
                criteria2.orLike("config", getLikeCondition(context));
                example.and(criteria2);
            }

            List<PushConfigInfo> pushConfigInfos = pushConfigMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, pushConfigInfos);

            return ReturnInfo.buildSuccess(pushConfigInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("push 通用配置列表查询失败", e);
        }

    }


    /**
    * push 通用配置分页列表
    * @param context 关键字
    * @param product_code 产品code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "push_config_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_config_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<PushConfigInfo>>> push_config_list_by_page(String context,String product_code, int limit, int offset) {
        try{
            Example example=new Example(PushConfigInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            //dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);
            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("config_key", getLikeCondition(context));
                criteria2.orLike("config_name", getLikeCondition(context));
                criteria2.orLike("config", getLikeCondition(context));
                example.and(criteria2);
            }

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = pushConfigMapper.selectCountByExample(example);

            List<PushConfigInfo> pushConfigInfos = pushConfigMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, pushConfigInfos);

            PageResult<List<PushConfigInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(pushConfigInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("push 通用配置列表分页查询失败", e);
        }

    }

    /**
     * push 通用配置新增首页
     * @return
     */
    @RequestMapping(value = "/push_config_add_index", method = RequestMethod.GET)
    public String push_config_add_index() {

        return "push/push_config_add_index";
    }

    /**
     * push 通用配置明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "push_config_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_config_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PushConfigInfo> push_config_detail(String id) {
        try {
            PushConfigInfo pushConfigInfo = pushConfigMapper.selectByPrimaryKey(id);
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService,  pushConfigInfo.getProduct_code(), pushConfigInfo.getDim_group(), getAttrSelect());
            checkAttrPermissionByProduct(zdhPermissionService,  pushConfigInfo.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pushConfigInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * push 通用配置更新
     * @param pushConfigInfo
     * @return
     */
    @SentinelResource(value = "push_config_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_config_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PushConfigInfo> push_config_update(PushConfigInfo pushConfigInfo) {
        try {

            PushConfigInfo oldPushConfigInfo = pushConfigMapper.selectByPrimaryKey(pushConfigInfo.getId());

            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pushConfigInfo.getProduct_code(), pushConfigInfo.getDim_group(), getAttrEdit());
            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldPushConfigInfo.getProduct_code(), oldPushConfigInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService, pushConfigInfo.getProduct_code(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService, oldPushConfigInfo.getProduct_code(), getAttrEdit());


            pushConfigInfo.setCreate_time(oldPushConfigInfo.getCreate_time());
            pushConfigInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            pushConfigInfo.setIs_delete(Const.NOT_DELETE);
            pushConfigMapper.updateByPrimaryKeySelective(pushConfigInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", pushConfigInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * push 通用配置新增
     * @param pushConfigInfo
     * @return
     */
    @SentinelResource(value = "push_config_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_config_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<PushConfigInfo> push_config_add(PushConfigInfo pushConfigInfo) {
        try {
            pushConfigInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            pushConfigInfo.setOwner(getOwner());
            pushConfigInfo.setIs_delete(Const.NOT_DELETE);
            pushConfigInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            pushConfigInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            //checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pushConfigInfo.getProduct_code(), pushConfigInfo.getDim_group(), getAttrAdd());
            checkAttrPermissionByProduct(zdhPermissionService, pushConfigInfo.getProduct_code(), getAttrAdd());
            pushConfigMapper.insertSelective(pushConfigInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", pushConfigInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * push 通用配置删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "push_config_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_config_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo push_config_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pushConfigMapper, pushConfigMapper.getTable(), ids, getAttrDel());
            pushConfigMapper.deleteLogicByIds(pushConfigMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }
}
