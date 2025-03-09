package com.zyc.zdh.controller.zdhpush;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.annotation.White;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.PushAppMapper;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.PushAppInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * push app 配置服务
 */
@Controller
public class PushAppController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private PushAppMapper pushAppMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * push app 配置列表首页
     * @return
     */
    @RequestMapping(value = "/push_app_index", method = RequestMethod.GET)
    @White
    public String push_app_index() {

        return "push/push_app_index";
    }

    /**
     * push app 配置列表
     * @param context 关键字
     * @param product_code 产品
     * @param dim_group 归属组
     * @return
     */
    @SentinelResource(value = "push_app_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_app_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<List<PushAppInfo>> push_app_list(String context, String product_code, String dim_group) {
        try{
            Example example=new Example(PushAppInfo.class);
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

            List<PushAppInfo> pushAppInfos = pushAppMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, pushAppInfos);

            return ReturnInfo.buildSuccess(pushAppInfos);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("push app 配置列表查询失败", e);
        }

    }


    /**
    * push app 配置分页列表
    * @param context 关键字
    * @param product_code 产品code
    * @param dim_group 归属组code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "push_app_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_app_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<PageResult<List<PushAppInfo>>> push_app_list_by_page(String context,String product_code, String dim_group, int limit, int offset) {
        try{
            Example example=new Example(PushAppInfo.class);
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
            int total = pushAppMapper.selectCountByExample(example);

            List<PushAppInfo> pushAppInfos = pushAppMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, pushAppInfos);

            PageResult<List<PushAppInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(pushAppInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("push app 配置列表分页查询失败", e);
        }

    }

    /**
     * push app 配置新增首页
     * @return
     */
    @RequestMapping(value = "/push_app_add_index", method = RequestMethod.GET)
    @White
    public String push_app_add_index() {

        return "push/push_app_add_index";
    }

    /**
     * xx明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "push_app_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_app_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @White
    public ReturnInfo<PushAppInfo> push_app_detail(String id) {
        try {
            PushAppInfo pushAppInfo = pushAppMapper.selectByPrimaryKey(id);
            checkPermissionByProductAndDimGroup(zdhPermissionService, pushAppInfo.getProduct_code(), pushAppInfo.getDim_group());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pushAppInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * push app 配置更新
     * @param pushAppInfo
     * @return
     */
    @SentinelResource(value = "push_app_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_app_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo<PushAppInfo> push_app_update(PushAppInfo pushAppInfo) {
        try {
            check_push_app(pushAppInfo.getApp(), pushAppInfo.getId());

            PushAppInfo oldPushAppInfo = pushAppMapper.selectByPrimaryKey(pushAppInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pushAppInfo.getProduct_code(), pushAppInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldPushAppInfo.getProduct_code(), oldPushAppInfo.getDim_group(), getAttrEdit());

            pushAppInfo.setCreate_time(oldPushAppInfo.getCreate_time());
            pushAppInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            pushAppInfo.setIs_delete(Const.NOT_DELETE);
            pushAppMapper.updateByPrimaryKeySelective(pushAppInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", pushAppInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * push app 配置新增
     * @param pushAppInfo
     * @return
     */
    @SentinelResource(value = "push_app_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_app_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo<PushAppInfo> push_app_add(PushAppInfo pushAppInfo) {
        try {

            check_push_app(pushAppInfo.getApp(), "");
            pushAppInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            pushAppInfo.setOwner(getOwner());
            pushAppInfo.setIs_delete(Const.NOT_DELETE);
            pushAppInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            pushAppInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, pushAppInfo.getProduct_code(), pushAppInfo.getDim_group(), getAttrAdd());
            pushAppMapper.insertSelective(pushAppInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", pushAppInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * push app 配置删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "push_app_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/push_app_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    @White
    public ReturnInfo push_app_delete(String[] ids) {
        try {
            checkPermissionByProductAndDimGroup(zdhPermissionService, pushAppMapper, pushAppMapper.getTable(), ids);
            pushAppMapper.deleteLogicByIds(pushAppMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }


    /**
     * 校验app唯一性
     * @param app
     * @param id
     * @throws Exception
     */
    private void check_push_app(String app,String id) throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("app", app);
        List<PushAppInfo> pushAppInfos = pushAppMapper.selectListByMap(pushAppMapper.getTable(), params);
        if(StringUtils.isEmpty(id)){
            if(pushAppInfos != null && pushAppInfos.size()>0){
                throw new Exception("app已经存在,使用信息的app英文名");
            }
        }else{
            if(pushAppInfos != null && pushAppInfos.size()>0){
                //遍历app
                for(PushAppInfo pushAppInfo: pushAppInfos){
                    if(pushAppInfo.getId().equalsIgnoreCase(id)){
                        continue;
                    }else{
                        throw new Exception("app已经存在,使用信息的app英文名");
                    }
                }

            }
        }
    }
}
