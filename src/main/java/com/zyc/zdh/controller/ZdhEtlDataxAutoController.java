package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.EtlTaskDataxAutoMapper;
import com.zyc.zdh.entity.EtlTaskDataxAutoInfo;
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

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * datax ETL任务服务
 */
@Controller
public class ZdhEtlDataxAutoController extends BaseController{

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EtlTaskDataxAutoMapper etlTaskDataxAutoMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * DATAX ETL首页
     * @return
     */
    @RequestMapping("/etl_task_datax_auto_index")
    public String etl_task_datax_auto_index() {

        return "etl/etl_task_datax_auto_index";
    }

    /**
     * 获取DATAX ETL任务明细
     * @param id 主键ID
     * @return
     */
    @SentinelResource(value = "etl_task_datax_auto_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_datax_auto_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<EtlTaskDataxAutoInfo> etl_task_datax_auto_detail(String id) {
        try{
            EtlTaskDataxAutoInfo eti=etlTaskDataxAutoMapper.selectByPrimaryKey(id);
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, eti.getProduct_code(), eti.getDim_group(), getAttrSelect());
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", eti);
        }catch (Exception e){
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }

    /**
     * 根据条件模糊查询DATAX ETL任务信息
     * @param etl_context 关键字
     * @param file_name 数据源关键字
     * @return
     */
    @SentinelResource(value = "etl_task_datax_auto_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_datax_auto_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<PageResult<List<EtlTaskDataxAutoInfo>>> etl_task_datax_auto_list(String etl_context, String file_name,int limit, int offset, String product_code, String dim_group) {
        try{
            EtlTaskDataxAutoInfo etlTaskDataxAutoInfo = new EtlTaskDataxAutoInfo();
            Example example = new Example(etlTaskDataxAutoInfo.getClass());
            List<EtlTaskDataxAutoInfo> etlTaskDataxAutoInfos = new ArrayList<>();
            Example.Criteria criteria = example.createCriteria();
            //criteria.andEqualTo("owner", getOwner());
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            //动态增加数据权限控制
            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);
            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }

            if (!StringUtils.isEmpty(etl_context)) {
                Example.Criteria cri2 = example.and();
                cri2.andLike("etl_context", getLikeCondition(etl_context));
            }
            example.setOrderByClause("update_time desc");
            RowBounds rowBounds=new RowBounds(offset,limit);
            int total = etlTaskDataxAutoMapper.selectCountByExample(example);

            etlTaskDataxAutoInfos = etlTaskDataxAutoMapper.selectByExampleAndRowBounds(example, rowBounds);

            PageResult<List<EtlTaskDataxAutoInfo>> pageResult=new PageResult<>();
            pageResult.setTotal(total);
            pageResult.setRows(etlTaskDataxAutoInfos);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", pageResult);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return  ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }


    /**
     * 根据条件模糊查询DATAX ETL任务信息
     * @return
     */
    @SentinelResource(value = "etl_task_datax_auto_all_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_datax_auto_all_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<EtlTaskDataxAutoInfo>> etl_task_datax_auto_all_list() {
        try{
            EtlTaskDataxAutoInfo etlTaskDataxAutoInfo = new EtlTaskDataxAutoInfo();
            Example example = new Example(etlTaskDataxAutoInfo.getClass());
            List<EtlTaskDataxAutoInfo> etlTaskDataxAutoInfos = new ArrayList<>();
            Example.Criteria cri = example.createCriteria();
            //cri.andEqualTo("owner", getOwner());
            cri.andEqualTo("is_delete", Const.NOT_DELETE);

            dynamicPermissionByProductAndGroup(zdhPermissionService, cri);

            example.setOrderByClause("update_time desc");

            etlTaskDataxAutoInfos = etlTaskDataxAutoMapper.selectByExample(example);


            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", etlTaskDataxAutoInfos);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return  ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }


    /**
     * 批量删除DATAX ETL任务
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "etl_task_datax_auto_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_datax_auto_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_datax_auto_delete(String[] ids) {
        try{
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskDataxAutoMapper, etlTaskDataxAutoMapper.getTable(), ids, getAttrDel());
            etlTaskDataxAutoMapper.deleteLogicByIds(etlTaskDataxAutoMapper.getTable(), ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),RETURN_CODE.SUCCESS.getDesc(), null);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),e.getMessage(), null);
        }
    }

    /**
     * 新增DATAX ETL任务首页
     * @return
     */
    @RequestMapping("/etl_task_datax_auto_add_index")
    public String etl_task_datax_auto_add() {

        return "etl/etl_task_datax_auto_add_index";
    }


    /**
     * 新增DATAX ETL任务
     * 如果输入数据源类型是外部上传,会补充文件服务器信息
     * @param etlTaskDataxAutoInfo
     * @return
     */
    @SentinelResource(value = "etl_task_datax_auto_add", blockHandler = "handleReturn")
    @RequestMapping(value="/etl_task_datax_auto_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_datax_auto_add(EtlTaskDataxAutoInfo etlTaskDataxAutoInfo) {
        //String json_str=JsonUtil.formatJsonString(request.getParameterMap());
        try{
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskDataxAutoInfo.getProduct_code(), etlTaskDataxAutoInfo.getDim_group(), getAttrAdd());

            etlTaskDataxAutoInfo.setId(SnowflakeIdWorker.getInstance().nextId()+"");
            etlTaskDataxAutoInfo.setOwner(getOwner());
            etlTaskDataxAutoInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskDataxAutoInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskDataxAutoInfo.setIs_delete(Const.NOT_DELETE);

            etlTaskDataxAutoMapper.insertSelective(etlTaskDataxAutoInfo);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),RETURN_CODE.SUCCESS.getDesc(), null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),e.getMessage(), null);
        }
    }


    /**
     * DATAX ETL任务更新
     * todo 此次是否每次都更新文件服务器信息,待优化
     * @param etlTaskDataxAutoInfo
     * @return
     */
    @SentinelResource(value = "etl_task_datax_auto_update", blockHandler = "handleReturn")
    @RequestMapping(value="/etl_task_datax_auto_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_datax_auto_update(EtlTaskDataxAutoInfo etlTaskDataxAutoInfo) {
        try{

            EtlTaskDataxAutoInfo oldEtlTaskDataxAutoInfo= etlTaskDataxAutoMapper.selectByPrimaryKey(etlTaskDataxAutoInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, etlTaskDataxAutoInfo.getProduct_code(), etlTaskDataxAutoInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldEtlTaskDataxAutoInfo.getProduct_code(), oldEtlTaskDataxAutoInfo.getDim_group(), getAttrEdit());

            etlTaskDataxAutoInfo.setOwner(oldEtlTaskDataxAutoInfo.getOwner());
            etlTaskDataxAutoInfo.setCreate_time(oldEtlTaskDataxAutoInfo.getCreate_time());
            etlTaskDataxAutoInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            etlTaskDataxAutoInfo.setIs_delete(Const.NOT_DELETE);
            etlTaskDataxAutoMapper.updateByPrimaryKeySelective(etlTaskDataxAutoInfo);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(),RETURN_CODE.SUCCESS.getDesc(), null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
			logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(),e.getMessage(), null);
        }
    }


    private void debugInfo(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (int i = 0, len = fields.length; i < len; i++) {
            // 对于每个属性，获取属性名
            String varName = fields[i].getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o;
                try {
                    o = fields[i].get(obj);
                    System.err.println("传入的对象中包含一个如下的变量：" + varName + " = " + o);
                } catch (IllegalAccessException e) {
                    // TODO Auto-generated catch block
                    String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
                    logger.error(error, e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                 logger.error("类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}", e);
            }
        }
    }

}
