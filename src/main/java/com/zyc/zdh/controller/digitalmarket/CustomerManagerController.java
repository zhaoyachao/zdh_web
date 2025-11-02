package com.zyc.zdh.controller.digitalmarket;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.controller.BaseController;
import com.zyc.zdh.entity.PageResult;
import com.zyc.zdh.entity.CustomerManagerInfo;
import com.zyc.zdh.entity.RETURN_CODE;
import com.zyc.zdh.entity.ReturnInfo;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.service.ZdhPermissionService;
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
import com.zyc.zdh.dao.CustomerManagerMapper;


import java.sql.Timestamp;
import java.util.List;

/**
 * 客户信息表服务
 */
@Controller
public class CustomerManagerController extends BaseController {

    @Autowired
    private CustomerManagerMapper customerManagerMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 客户信息表列表首页
     * @return
     */
    @RequestMapping(value = "/customer_manager_index", method = RequestMethod.GET)
    public String customer_manager_index() {

        return "digitalmarket/customer_manager_index";
    }

    /**
     * 客户信息表列表
     * @param context 关键字
     * @param product_code 产品
     * @return
     */
    @SentinelResource(value = "customer_manager_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/customer_manager_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<CustomerManagerInfo>> customer_manager_list(String context, String product_code) {
        try{
            Example example=new Example(CustomerManagerInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            List<CustomerManagerInfo> customerManagerInfos = customerManagerMapper.selectByExample(example);
            dynamicAuth(zdhPermissionService, customerManagerInfos);

            return ReturnInfo.buildSuccess(customerManagerInfos);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("客户信息表列表查询失败", e);
        }

    }


    /**
    * 客户信息表分页列表
    * @param context 关键字
    * @param product_code 产品code
    * @param limit 分页大小
    * @param offset 分页下标
    * @return
    */
    @SentinelResource(value = "customer_manager_list_by_page", blockHandler = "handleReturn")
    @RequestMapping(value = "/customer_manager_list_by_page", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<CustomerManagerInfo>>> customer_manager_list_by_page(String context,String product_code, int limit, int offset) {
        try{
            Example example=new Example(CustomerManagerInfo.class);
            Example.Criteria criteria=example.createCriteria();
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);

            dynamicPermissionByProduct(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }

            Example.Criteria criteria2=example.createCriteria();
            if(!StringUtils.isEmpty(context)){
                criteria2.orLike("context", getLikeCondition(context));
                example.and(criteria2);
            }

            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = customerManagerMapper.selectCountByExample(example);

            List<CustomerManagerInfo> customerManagerInfos = customerManagerMapper.selectByExampleAndRowBounds(example, rowBounds);
            dynamicAuth(zdhPermissionService, customerManagerInfos);

            PageResult<List<CustomerManagerInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(customerManagerInfos);

            return ReturnInfo.buildSuccess(pageResult);
        }catch(Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("客户信息表列表分页查询失败", e);
        }

    }

    /**
     * 客户信息表新增首页
     * @return
     */
    @RequestMapping(value = "/customer_manager_add_index", method = RequestMethod.GET)
    public String customer_manager_add_index() {

        return "digitalmarket/customer_manager_add_index";
    }

    /**
     * 客户信息表明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "customer_manager_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/customer_manager_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<CustomerManagerInfo> customer_manager_detail(String id) {
        try {
            CustomerManagerInfo customerManagerInfo = customerManagerMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProduct(zdhPermissionService,  customerManagerInfo.getProduct_code(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", customerManagerInfo);
        } catch (Exception e) {
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }
    }

    /**
     * 客户信息表更新
     * @param customerManagerInfo
     * @return
     */
    @SentinelResource(value = "customer_manager_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/customer_manager_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<CustomerManagerInfo> customer_manager_update(CustomerManagerInfo customerManagerInfo) {
        try {

            CustomerManagerInfo oldCustomerManagerInfo = customerManagerMapper.selectByPrimaryKey(customerManagerInfo.getId());

            checkAttrPermissionByProduct(zdhPermissionService, customerManagerInfo.getProduct_code(), getAttrEdit());
            checkAttrPermissionByProduct(zdhPermissionService, oldCustomerManagerInfo.getProduct_code(), getAttrEdit());


            customerManagerInfo.setCreate_time(oldCustomerManagerInfo.getCreate_time());
            customerManagerInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            customerManagerInfo.setIs_delete(Const.NOT_DELETE);
            customerManagerMapper.updateByPrimaryKeySelective(customerManagerInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", customerManagerInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 客户信息表新增
     * @param customerManagerInfo
     * @return
     */
    @SentinelResource(value = "customer_manager_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/customer_manager_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<CustomerManagerInfo> customer_manager_add(CustomerManagerInfo customerManagerInfo) {
        try {
            customerManagerInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            customerManagerInfo.setOwner(getOwner());
            customerManagerInfo.setIs_delete(Const.NOT_DELETE);
            customerManagerInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            customerManagerInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));

            checkAttrPermissionByProduct(zdhPermissionService, customerManagerInfo.getProduct_code(), getAttrAdd());
            customerManagerMapper.insertSelective(customerManagerInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", customerManagerInfo);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 客户信息表删除
     * @param ids
     * @return
     */
    @SentinelResource(value = "customer_manager_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/customer_manager_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo customer_manager_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, customerManagerMapper, customerManagerMapper.getTable(), ids, getAttrDel());
            customerManagerMapper.deleteLogicByIds(customerManagerMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e.getMessage());
        }
    }
}
