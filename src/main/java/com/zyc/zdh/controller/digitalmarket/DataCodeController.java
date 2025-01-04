package com.zyc.zdh.controller.digitalmarket;


import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.dao.DataCodeMapper;
import com.zyc.zdh.entity.DataCodeInfo;
import com.zyc.zdh.entity.PageResult;
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
import java.util.List;

/**
 * data节点配置服务
 */
@Controller
public class DataCodeController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private DataCodeMapper dataCodeMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * data节点配置列表首页
     * @return
     */
    @RequestMapping(value = "/data_code_index", method = RequestMethod.GET)
    public String data_code_index() {

        return "digitalmarket/data_code_index";
    }

    /**
     * data节点配置列表
     * @param context 关键字
     * @param product_code 产品
     * @param dim_group 归属组
     * @return
     */
    @SentinelResource(value = "data_code_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_code_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<DataCodeInfo>> data_code_list(String context, String product_code, String dim_group) {
        try{
            Example example=new Example(DataCodeInfo.class);
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

            List<DataCodeInfo> dataCodeInfos = dataCodeMapper.selectByExample(example);

            return ReturnInfo.buildSuccess(dataCodeInfos);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("data节点配置列表查询失败", e);
        }

    }


    /**
    * data节点配置分页列表
    * @param context 关键字
    * @param product_code 产品code
    * @param dim_group 归属组code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "data_code_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_code_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<DataCodeInfo>>> data_code_list_by_page(String context,String product_code, String dim_group, int limit, int offset) {
        try{
            Example example=new Example(DataCodeInfo.class);
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
                criteria2.orLike("code_desc", getLikeCondition(context));
                criteria2.orLike("code_name", getLikeCondition(context));
                criteria2.orLike("code", getLikeCondition(context));
                example.and(criteria2);
            }

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = dataCodeMapper.selectCountByExample(example);

            List<DataCodeInfo> dataCodeInfos = dataCodeMapper.selectByExampleAndRowBounds(example, rowBounds);

            PageResult<List<DataCodeInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(dataCodeInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("data节点配置列表分页查询失败", e);
        }

    }

    /**
     * data节点配置新增首页
     * @return
     */
    @RequestMapping(value = "/data_code_add_index", method = RequestMethod.GET)
    public String data_code_add_index() {

        return "digitalmarket/data_code_add_index";
    }

    /**
     * data节点配置明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "data_code_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_code_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<DataCodeInfo> data_code_detail(String id) {
        try {
            DataCodeInfo dataCodeInfo = dataCodeMapper.selectByPrimaryKey(id);
            checkPermissionByProductAndDimGroup(zdhPermissionService, dataCodeInfo.getProduct_code(), dataCodeInfo.getDim_group());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", dataCodeInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * data节点配置更新
     * @param dataCodeInfo
     * @return
     */
    @SentinelResource(value = "data_code_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_code_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<DataCodeInfo> data_code_update(DataCodeInfo dataCodeInfo) {
        try {

            DataCodeInfo oldDataCodeInfo = dataCodeMapper.selectByPrimaryKey(dataCodeInfo.getId());

            //校验code是否唯一
            Example example=new Example(DataCodeInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("code", dataCodeInfo.getCode());
            criteria.andNotEqualTo("id", dataCodeInfo.getId());

            List<DataCodeInfo> dcis = dataCodeMapper.selectByExample(example);
            if(dcis!=null && dcis.size()>0){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"code不唯一", dataCodeInfo);
            }


            checkPermissionByProductAndDimGroup(zdhPermissionService, dataCodeInfo.getProduct_code(), dataCodeInfo.getDim_group());
            checkPermissionByProductAndDimGroup(zdhPermissionService, oldDataCodeInfo.getProduct_code(), oldDataCodeInfo.getDim_group());

            dataCodeInfo.setCreate_time(oldDataCodeInfo.getCreate_time());
            dataCodeInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            dataCodeInfo.setIs_delete(Const.NOT_DELETE);
            dataCodeMapper.updateByPrimaryKeySelective(dataCodeInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", dataCodeInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * data节点配置新增
     * @param dataCodeInfo
     * @return
     */
    @SentinelResource(value = "data_code_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_code_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<DataCodeInfo> data_code_add(DataCodeInfo dataCodeInfo) {
        try {

            //校验code是否唯一
            Example example=new Example(DataCodeInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("code", dataCodeInfo.getCode());

            List<DataCodeInfo> dcis = dataCodeMapper.selectByExample(example);
            if(dcis!=null && dcis.size()>0){
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),"code不唯一", dataCodeInfo);
            }

            dataCodeInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            dataCodeInfo.setOwner(getOwner());
            dataCodeInfo.setIs_delete(Const.NOT_DELETE);
            dataCodeInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            dataCodeInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkPermissionByProductAndDimGroup(zdhPermissionService, dataCodeInfo.getProduct_code(), dataCodeInfo.getDim_group());
            dataCodeMapper.insertSelective(dataCodeInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", dataCodeInfo);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * data节点配置删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "data_code_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_code_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo data_code_delete(String[] ids) {
        try {
            checkPermissionByProductAndDimGroup(zdhPermissionService, dataCodeMapper, dataCodeMapper.getTable(), ids);
            dataCodeMapper.deleteLogicByIds(dataCodeMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }
}
