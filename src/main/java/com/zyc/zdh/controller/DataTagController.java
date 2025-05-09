package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.DataTagMapper;
import com.zyc.zdh.entity.DataTagInfo;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.LogUtil;
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
 * 数据标识服务
 * 2024-12-31 v5.6.0+版本废弃
 */
@Deprecated
@Controller
public class DataTagController extends BaseController {


    @Autowired
    private DataTagMapper dataTagMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 数据标识首页
     * @return
     */
    @RequestMapping(value = "/data_tag_index", method = RequestMethod.GET)
    public String data_tag_index() {

        return "admin/data_tag_index";
    }

    /**
     * 数据标识列表
     * @param tag_context 关键字
     * @param product_code 产品code
     * @param limit
     * @param offset
     * @return
     */
    @SentinelResource(value = "data_tag_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_tag_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<DataTagInfo>>> data_tag_list(String tag_context, String product_code,int limit, int offset) {
        try{

            checkParam(product_code, "产品代码");
            Example example=new Example(DataTagInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            Example.Criteria criteria2=example.createCriteria();
            if(!org.apache.commons.lang3.StringUtils.isEmpty(tag_context)){
                criteria2.orLike("tag_code", getLikeCondition(tag_context));
                criteria2.orLike("tag_name", getLikeCondition(tag_context));
                criteria2.orLike("product_code", getLikeCondition(tag_context));
                example.and(criteria2);
            }

            example.setOrderByClause("id desc");
            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = dataTagMapper.selectCountByExample(example);

            List<DataTagInfo> dataTagInfos = dataTagMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, dataTagInfos);

            PageResult<List<DataTagInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(dataTagInfos);
            return ReturnInfo.buildSuccess(pageResult);

        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError(e);
        }
    }

    /**
     * 根据产品代码获取数据标识
     * @param product_code 产品代码
     * @return
     */
    @SentinelResource(value = "data_tag_by_product_code", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_tag_by_product_code", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<DataTagInfo>> data_tag_by_product_code(String product_code) {
        try{
            Example example=new Example(DataTagInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            criteria.andEqualTo("product_code", product_code);
            List<DataTagInfo> dataTagInfos = dataTagMapper.selectByExample(example);
            return ReturnInfo.buildSuccess(dataTagInfos);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError(e);
        }
    }

    /**
     * 数据标识新增首页
     * @return
     */
    @RequestMapping(value = "/data_tag_add_index", method = RequestMethod.GET)
    public String data_tag_add_index() {

        return "admin/data_tag_add_index";
    }


    /**
     * 数据标识明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "data_tag_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_tag_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<DataTagInfo> data_tag_detail(String id) {
        try {
            DataTagInfo dataTagInfo = dataTagMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", dataTagInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 数据标识更新
     * @param dataTagInfo
     * @return
     */
    @SentinelResource(value = "data_tag_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_tag_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<DataTagInfo> data_tag_update(DataTagInfo dataTagInfo) {
        try {
            DataTagInfo oldDataTagInfo = dataTagMapper.selectByPrimaryKey(dataTagInfo.getId());

            checkPermissionByProduct(zdhPermissionService, dataTagInfo.getProduct_code());
            checkPermissionByProduct(zdhPermissionService, oldDataTagInfo.getProduct_code());

            dataTagInfo.setOwner(oldDataTagInfo.getOwner());
            dataTagInfo.setCreate_time(oldDataTagInfo.getCreate_time());
            dataTagInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            dataTagInfo.setIs_delete(Const.NOT_DELETE);
            dataTagMapper.updateByPrimaryKeySelective(dataTagInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", dataTagInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 数据标识新增
     * @param dataTagInfo
     * @return
     */
    @SentinelResource(value = "data_tag_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_tag_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> data_tag_add(DataTagInfo dataTagInfo) {
        try {
            dataTagInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            dataTagInfo.setOwner(getOwner());
            dataTagInfo.setIs_delete(Const.NOT_DELETE);
            dataTagInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            dataTagInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            checkPermissionByProduct(zdhPermissionService, dataTagInfo.getProduct_code());
            dataTagMapper.insertSelective(dataTagInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 数据标识删除
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "data_tag_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/data_tag_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<Object> data_tag_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, dataTagMapper, dataTagMapper.getTable(), ids, getAttrDel());
            dataTagMapper.deleteLogicByIds(dataTagMapper.getTable(), ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }

}
