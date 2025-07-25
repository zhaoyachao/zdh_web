package com.zyc.zdh.controller.digitalmarket;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.FilterMapper;
import com.zyc.zdh.entity.FilterInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.shiro.RedisUtil;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
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
 * 智能营销-过滤规则服务
 */
@Controller
public class FilterController extends BaseController {

    @Autowired
    private FilterMapper filterMapper;

    @Autowired
    private ZdhPermissionService zdhPermissionService;

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 过滤列表首页
     * @return
     */
    @RequestMapping(value = "/filter_index", method = RequestMethod.GET)
    public String filter_index() {

        return "digitalmarket/filter_index";
    }

    /**
     * 过滤列表
     * @param filter_name 关键字
     * @return
     */
    @SentinelResource(value = "filter_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/filter_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public  ReturnInfo<List<FilterInfo>> filter_list(String filter_name, String product_code, String dim_group) {
        try{
            Example example=new Example(FilterInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            //动态增加数据权限控制
            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(filter_name)){
                criteria2.orLike("filter_name", getLikeCondition(filter_name));
                criteria2.orLike("filter_code", getLikeCondition(filter_name));
                example.and(criteria2);
            }


            List<FilterInfo> filterInfos = filterMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, filterInfos);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", filterInfos);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }

    /**
     * 过滤新增首页
     * @return
     */
    @RequestMapping(value = "/filter_add_index", method = RequestMethod.GET)
    public String filter_add_index() {

        return "digitalmarket/filter_add_index";
    }


    /**
     * 过滤明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "filter_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/filter_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<FilterInfo> filter_detail(String id) {
        try {
            FilterInfo filterInfo = filterMapper.selectByPrimaryKey(id);
            checkPermissionByProduct(zdhPermissionService, filterInfo.getProduct_code());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", filterInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 过滤更新
     * @param filterInfo
     * @return
     */
    @SentinelResource(value = "filter_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/filter_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<FilterInfo> filter_update(FilterInfo filterInfo) {
        try {

            FilterInfo oldFilterInfo = filterMapper.selectByPrimaryKey(filterInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, filterInfo.getProduct_code(), filterInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldFilterInfo.getProduct_code(), oldFilterInfo.getDim_group(), getAttrEdit());

            //strategyGroupInfo.setRule_json(jsonArray.toJSONString());
            filterInfo.setOwner(oldFilterInfo.getOwner());
            filterInfo.setCreate_time(oldFilterInfo.getCreate_time());
            filterInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            filterInfo.setIs_delete(Const.NOT_DELETE);
            filterMapper.updateByPrimaryKeySelective(filterInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", filterInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 过滤新增
     * @param filterInfo
     * @return
     */
    @SentinelResource(value = "filter_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/filter_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo filter_add(FilterInfo filterInfo) {
        try {

            filterInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            filterInfo.setOwner(getOwner());
            filterInfo.setIs_delete(Const.NOT_DELETE);
            filterInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            filterInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, filterInfo.getProduct_code(), filterInfo.getDim_group(), getAttrAdd());

            filterMapper.insertSelective(filterInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e.getMessage());
        }
    }

    /**
     * 过滤删除
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "filter_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/filter_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo filter_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, filterMapper, filterMapper.getTable(), ids, getAttrDel());
            filterMapper.deleteLogicByIds(filterMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

    /**
     * 过滤值新增首页
     * @return
     */
    @RequestMapping(value = "/filter_add_value_index", method = RequestMethod.GET)
    public String filter_add_value_index() {

        return "digitalmarket/filter_add_value_index";
    }

    /**
     * 过滤值新增
     * @param id 过滤规则ID
     * @param filter_value 过滤值
     * @param filter_operate 操作,del:出过滤 ,add: 入过滤
     * @return
     */
    @SentinelResource(value = "filter_add_value", blockHandler = "handleReturn")
    @RequestMapping(value = "/filter_add_value", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo filter_add_value(String id, String filter_value, String filter_operate) {
        try {
            if(StringUtils.isEmpty(filter_value)){
                throw new Exception("过滤值不可为空");
            }
            if(StringUtils.isEmpty(filter_operate)){
                throw new Exception("过滤操作不可为空");
            }
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, filterMapper, filterMapper.getTable(), new String[]{id}, getAttrAdd());
            FilterInfo filterInfo = filterMapper.selectByPrimaryKey(id);
            if(!filterInfo.getEngine_type().equalsIgnoreCase("redis")){
                throw new Exception("过滤值新增当前仅支持redis引擎的过滤规则");
            }

            if(filter_operate.equalsIgnoreCase(Const.FILTER_OPERATE_ADD)){
                redisUtil.getRedisTemplate().opsForValue().set(filterInfo.getProduct_code()+"_filter:"+filterInfo.getFilter_code()+":"+filter_value, filter_value);
            }
            if(filter_operate.equalsIgnoreCase(Const.FILTER_OPERATE_DEL)){
                redisUtil.getRedisTemplate().delete(filterInfo.getProduct_code()+"_filter:"+filterInfo.getFilter_code()+":"+filter_value);
            }

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "操作成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "操作失败", e.getMessage());
        }
    }

}
