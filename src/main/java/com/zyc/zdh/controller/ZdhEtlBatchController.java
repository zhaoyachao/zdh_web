package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSON;
import com.zyc.zdh.dao.DataSourcesMapper;
import com.zyc.zdh.dao.EtlTaskBatchMapper;
import com.zyc.zdh.dao.EtlTaskUpdateLogsMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.EtlTaskService;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.DBUtil;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
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
import java.util.Date;
import java.util.List;

/**
 * 批量任务服务
 */
@Controller
public class ZdhEtlBatchController extends BaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private EtlTaskService etlTaskService;
    @Autowired
    private EtlTaskBatchMapper etlTaskBatchMapper;
    @Autowired
    private EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;
    @Autowired
    private DataSourcesMapper dataSourcesMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;

    /**
     * 批量任务首页
     *
     * @return
     */
    @RequestMapping("/etl_task_batch_index")
    public String etl_task_batch_index() {

        return "etl/etl_task_batch_index";
    }

    /**
     * 批量任务新增首页
     *
     * @return
     */
    @RequestMapping("/etl_task_batch_add_index")
    public String etl_task_batch_add_index() {
        return "etl/etl_task_batch_add_index";
    }

    /**
     * 批量任务明细
     *
     * @param id
     * @return
     */
    @SentinelResource(value = "etl_task_batch_detail", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_batch_detail", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<EtlTaskBatchInfo> etl_task_batch_detail(String id) {
        try {
            EtlTaskBatchInfo eti = etlTaskBatchMapper.selectByPrimaryKey(id);
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "查询成功", eti);
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "查询失败", e);
        }

    }

    /**
     * 根据条件模糊查询批量任务信息
     *
     * @param etl_context 关键字
     * @return
     */
    @SentinelResource(value = "etl_task_batch_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_batch_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<EtlTaskBatchInfo>> etl_task_batch_list(String etl_context, String product_code, String dim_group) {
        try{
            List<EtlTaskBatchInfo> list = new ArrayList<>();
            Example example = new Example(EtlTaskBatchInfo.class);


            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("owner", getOwner());
            criteria.andEqualTo("is_delete", Const.NOT_DELETE);
            //动态增加数据权限控制
            dynamicPermissionByProductAndGroup(zdhPermissionService, criteria);

            if(!StringUtils.isEmpty(product_code)){
                criteria.andEqualTo("product_code", product_code);
            }
            if(!StringUtils.isEmpty(dim_group)){
                criteria.andEqualTo("dim_group", dim_group);
            }

            Example.Criteria criteria2 = example.createCriteria();
            if (!StringUtils.isEmpty(etl_context)) {
                criteria2.andLike("etl_pre_context", getLikeCondition(etl_context));
                criteria2.orLike("etl_suffix_context", getLikeCondition(etl_context));
            }
            example.and(criteria2);
            list = etlTaskBatchMapper.selectByExample(example);
            return ReturnInfo.buildSuccess(list);
        }catch (Exception e){
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
            return ReturnInfo.buildError("批量生成任务查询列表失败", e);
        }

    }

    /**
     * 批量删除'批量任务信息
     *
     * @param ids id数组
     * @return
     */
    @SentinelResource(value = "etl_task_batch_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_batch_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_batch_delete(String[] ids) {
        try {
            checkPermissionByProductAndDimGroup(zdhPermissionService, etlTaskBatchMapper, etlTaskBatchMapper.getTable(), ids);
            etlTaskBatchMapper.deleteLogicByIds(etlTaskBatchMapper.getTable(),ids, new Timestamp(new Date().getTime()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), RETURN_CODE.SUCCESS.getDesc(), null);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), e.getMessage(), null);
        }
    }


    /**
     * 新增批量任务
     *
     * @param etlTaskBatchInfo
     * @return
     */
    @SentinelResource(value = "etl_task_batch_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_batch_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_batch_add(EtlTaskBatchInfo etlTaskBatchInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        try {
            String owner = getOwner();
            etlTaskBatchInfo.setOwner(owner);
            debugInfo(etlTaskBatchInfo);

            checkPermissionByProductAndDimGroup(zdhPermissionService, etlTaskBatchInfo.getProduct_code(), etlTaskBatchInfo.getDim_group());
            etlTaskBatchInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
            etlTaskBatchInfo.setCreate_time(new Timestamp(new Date().getTime()));
            etlTaskBatchInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTaskBatchInfo.setIs_delete(Const.NOT_DELETE);
            etlTaskBatchInfo.setStatus(Const.BATCH_INIT);
            etlTaskBatchMapper.insertSelective(etlTaskBatchInfo);

            if (etlTaskBatchInfo.getUpdate_context() != null && !etlTaskBatchInfo.getUpdate_context().equals("")) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskBatchInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlTaskBatchInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insertSelective(etlTaskUpdateLogs);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), RETURN_CODE.SUCCESS.getDesc(), null);
        } catch (Exception e) {

            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), e.getMessage(), null);
        }
    }


    /**
     * 批量任务更新
     *
     * @param etlTaskBatchInfo
     * @return
     */
    @SentinelResource(value = "etl_task_batch_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_batch_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo etl_task_batch_update(EtlTaskBatchInfo etlTaskBatchInfo) {
        try {
            String owner = getOwner();
            etlTaskBatchInfo.setOwner(owner);
            etlTaskBatchInfo.setIs_delete(Const.NOT_DELETE);
            etlTaskBatchInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            debugInfo(etlTaskBatchInfo);


            //获取旧数据是否更新说明
            EtlTaskBatchInfo oldEtlTaskBatchInfo = etlTaskBatchMapper.selectByPrimaryKey(etlTaskBatchInfo.getId());

            checkPermissionByProductAndDimGroup(zdhPermissionService, etlTaskBatchInfo.getProduct_code(), etlTaskBatchInfo.getDim_group());
            checkPermissionByProductAndDimGroup(zdhPermissionService, oldEtlTaskBatchInfo.getProduct_code(), oldEtlTaskBatchInfo.getDim_group());

            etlTaskBatchMapper.updateByPrimaryKeySelective(etlTaskBatchInfo);

            if (etlTaskBatchInfo.getUpdate_context() != null && !etlTaskBatchInfo.getUpdate_context().equals("")
                    && !oldEtlTaskBatchInfo.getUpdate_context().equals(etlTaskBatchInfo.getUpdate_context())) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(etlTaskBatchInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(etlTaskBatchInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insertSelective(etlTaskUpdateLogs);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), RETURN_CODE.SUCCESS.getDesc(), null);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), e.getMessage(), null);
        }
    }


    /**
     * 批量任务新增
     * @param etlTaskBatchInfo
     * @return
     */
    @SentinelResource(value = "etl_task_batch_create", blockHandler = "handleReturn")
    @RequestMapping(value = "/etl_task_batch_create", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo<List<String>> etl_task_batch_create(EtlTaskBatchInfo etlTaskBatchInfo) {
        try {
            String owner = getOwner();
            debugInfo(etlTaskBatchInfo);
            EtlTaskBatchInfo etbi = etlTaskBatchMapper.selectByPrimaryKey(etlTaskBatchInfo);

            //校验状态
            if (!etbi.getStatus().equalsIgnoreCase(Const.BATCH_INIT) && !etbi.getStatus().equalsIgnoreCase(Const.BATCH_FAIL)) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "批量任务状态必须是未执行或失败", null);
            }
            //校验数据源是否JDBC类型
            if (!etbi.getData_source_type_input().equalsIgnoreCase("jdbc")) {
                return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "批量任务输入数据源必须是JDBC类型", null);
            }

            //拉取JDBC下满足的表
            DataSourcesInfo dataSourcesInfo = dataSourcesMapper.selectByPrimaryKey(etbi.getData_sources_choose_input());
            debugInfo(dataSourcesInfo);

            String url = dataSourcesInfo.getUrl();
            List<String> tables = new DBUtil().R3(dataSourcesInfo.getDriver(), dataSourcesInfo.getUrl(), dataSourcesInfo.getUsername(),
                    dataSourcesInfo.getPassword(), "");
            List<String> tab = new ArrayList<>();
            for (String table : tables) {
                if (StringUtils.isEmpty(etbi.getData_sources_file_name_input()) || table.matches(etbi.getData_sources_file_name_input())) {
                    tab.add(table);
                }
            }

            etbi.setStatus(Const.BATCH_RUNNING);
            etbi.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTaskBatchMapper.updateByPrimaryKeySelective(etbi);

            //生成单源ETL
            for (String table : tab) {
                EtlTaskInfo eti = new EtlTaskInfo();
                BeanUtils.copyProperties(eti, etbi);
                eti.setId(SnowflakeIdWorker.getInstance().nextId() + "");
                eti.setEtl_context(etbi.getEtl_pre_context() + "_" + table);
                if (!StringUtils.isEmpty(etbi.getEtl_suffix_context())) {
                    eti.setEtl_context(etbi.getEtl_pre_context() + "_" + table + "_" + etbi.getEtl_suffix_context());
                }

                eti.setData_sources_table_name_input(table);
                eti.setData_sources_file_name_input(table);

                List<column_data> cds = new ArrayList<>();
                List<String> cols = schema(dataSourcesInfo, table);
                for (String col : cols) {
                    column_data cd = new column_data();
                    cd.setColumn_expr(col);
                    cd.setColumn_alias(col);
                    cd.setColumn_type("string");
                    cd.setColumn_md5(SnowflakeIdWorker.getInstance().nextId() + "");
                    cds.add(cd);
                }

                eti.setData_sources_table_columns(StringUtils.join(cols, ","));
                eti.setData_sources_file_columns(StringUtils.join(cols, ","));
                eti.setColumn_datas(JSON.toJSONString(cds));

                eti.setData_sources_table_name_output(table);
                eti.setData_sources_file_name_output(table);

                etlTaskService.insert(eti);
            }
            etbi.setStatus(Const.BATCH_SUCCESS);
            etbi.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTaskBatchMapper.updateByPrimaryKeySelective(etbi);

            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), RETURN_CODE.SUCCESS.getDesc(), tab);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            logger.error(error, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            etlTaskBatchInfo.setStatus(Const.BATCH_FAIL);
            etlTaskBatchInfo.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTaskBatchMapper.updateByPrimaryKeySelective(etlTaskBatchInfo);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), e.getMessage(), null);
        }
    }

    private List<String> schema(DataSourcesInfo dataSourcesInfo, String table_name) {

        String url = dataSourcesInfo.getUrl();

        try {
            return new DBUtil().R4(dataSourcesInfo.getDriver(), dataSourcesInfo.getUrl(), dataSourcesInfo.getUsername(), dataSourcesInfo.getPassword(),
                    "select * from " + table_name + " where 1=2 limit 1", table_name);
        } catch (Exception e) {
            logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            return new ArrayList<String>();
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
                    logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException e) {
                logger.error("类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}" , e);
            }
        }
    }

}
