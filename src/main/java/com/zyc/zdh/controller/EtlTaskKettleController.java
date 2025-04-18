package com.zyc.zdh.controller;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.EtlTaskKettleMapper;
import com.zyc.zdh.entity.EtlTaskKettleInfo;
import com.zyc.zdh.entity.PageResult;
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
 * 服务
 */
@Controller
public class EtlTaskKettleController extends BaseController {

    @Autowired
    private EtlTaskKettleMapper etlTaskKettleMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 列表首页
     * @return
     */
    @RequestMapping(value = "/etl_task_kettle_index", method = RequestMethod.GET)
    public String etl_task_kettle_index() {

        return "etl/etl_task_kettle_index";
    }

    /**
     * 列表
     * @param context 关键字
     * @param product_code
     * @param dim_group
     * @return
     */
    @SentinelResource(value = "etl_task_kettle_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_kettle_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<EtlTaskKettleInfo>> etl_task_kettle_list(String context, String product_code, String dim_group) {
        try{
            Example example=new Example(EtlTaskKettleInfo.class);
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
                criteria2.orLike("etl_context", getLikeCondition(context));
                criteria2.orLike("kettle_repository_path", getLikeCondition(context));
                example.and(criteria2);
            }

            List<EtlTaskKettleInfo> etlTaskKettleInfos = etlTaskKettleMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, etlTaskKettleInfos);

            return ReturnInfo.buildSuccess(etlTaskKettleInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("列表查询失败", e);
        }

    }


    /**
    * 列表
    * @param context 关键字
    * @param product_code 产品code
    * @param dim_group 归属组code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "etl_task_kettle_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_kettle_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<EtlTaskKettleInfo>>> etl_task_kettle_list_by_page(String context,String product_code, String dim_group, int limit, int offset) {
        try{
            Example example=new Example(EtlTaskKettleInfo.class);
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
            int total = etlTaskKettleMapper.selectCountByExample(example);

            List<EtlTaskKettleInfo> etlTaskKettleInfos = etlTaskKettleMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, etlTaskKettleInfos);

            PageResult<List<EtlTaskKettleInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(etlTaskKettleInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("烽火台信息列表查询失败", e);
        }

    }

    /**
     * 新增首页
     * @return
     */
    @RequestMapping(value = "/etl_task_kettle_add_index", method = RequestMethod.GET)
    public String etl_task_kettle_add_index() {

        return "etl/etl_task_kettle_add_index";
    }

    /**
     * xx明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "etl_task_kettle_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_kettle_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<EtlTaskKettleInfo> etl_task_kettle_detail(String id) {
        try {
            EtlTaskKettleInfo etlTaskKettleInfo = etlTaskKettleMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskKettleInfo.getProduct_code(), etlTaskKettleInfo.getDim_group(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", etlTaskKettleInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 更新
     * @param etlTaskKettleInfo
     * @return
     */
    @SentinelResource(value = "etl_task_kettle_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_kettle_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<EtlTaskKettleInfo> etl_task_kettle_update(EtlTaskKettleInfo etlTaskKettleInfo) {
        try {

            EtlTaskKettleInfo oldEtlTaskKettleInfo = etlTaskKettleMapper.selectByPrimaryKey(etlTaskKettleInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskKettleInfo.getProduct_code(), etlTaskKettleInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldEtlTaskKettleInfo.getProduct_code(), oldEtlTaskKettleInfo.getDim_group(), getAttrEdit());

            etlTaskKettleInfo.setCreate_time(oldEtlTaskKettleInfo.getCreate_time());
            etlTaskKettleInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskKettleInfo.setIs_delete(Const.NOT_DELETE);
            etlTaskKettleMapper.updateByPrimaryKeySelective(etlTaskKettleInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", etlTaskKettleInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 新增
     * @param etlTaskKettleInfo
     * @return
     */
    @SentinelResource(value = "etl_task_kettle_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_kettle_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<EtlTaskKettleInfo> etl_task_kettle_add(EtlTaskKettleInfo etlTaskKettleInfo) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskKettleInfo.getProduct_code(), etlTaskKettleInfo.getDim_group(), getAttrAdd());

            etlTaskKettleInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            etlTaskKettleInfo.setOwner(getOwner());
            etlTaskKettleInfo.setIs_delete(Const.NOT_DELETE);
            etlTaskKettleInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskKettleInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskKettleMapper.insertSelective(etlTaskKettleInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", etlTaskKettleInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "etl_task_kettle_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_kettle_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_kettle_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskKettleMapper, etlTaskKettleMapper.getTable(), ids, getAttrDel());
            etlTaskKettleMapper.deleteLogicByIds("etl_task_kettle_info",ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }
}
