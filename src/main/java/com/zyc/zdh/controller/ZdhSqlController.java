package com.zyc.zdh.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.zyc.zdh.dao.EtlTaskUpdateLogsMapper;
import com.zyc.zdh.dao.MetaDatabaseMapper;
import com.zyc.zdh.dao.SqlTaskMapper;
import com.zyc.zdh.dao.ZdhHaInfoMapper;
import com.zyc.zdh.entity.*;
import com.zyc.zdh.job.JobCommon2;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.service.ZdhPermissionService;
import com.zyc.zdh.util.Const;
import com.zyc.zdh.util.HttpUtil;
import com.zyc.zdh.util.JsonUtil;
import com.zyc.zdh.util.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * spark sql服务
 */
@Controller
public class ZdhSqlController extends BaseController {

    @Autowired
    private ZdhHaInfoMapper zdhHaInfoMapper;
    @Autowired
    private EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;
    @Autowired
    private SqlTaskMapper sqlTaskMapper;
    @Autowired
    private MetaDatabaseMapper metaDatabaseMapper;
    @Autowired
    private ZdhPermissionService zdhPermissionService;
    /**
     * spark sql任务首页
     * @return
     */
    @RequestMapping("/sql_task_index")
    public String etl_task_ssh_index() {

        return "etl/sql_task_index";
    }

    /**
     * spark sql任务新增首页
     * @return
     */
    @RequestMapping("/sql_task_add_index")
    public String sql_task_add_index() {

        return "etl/sql_task_add_index";
    }


    /**
     * 模糊查询Sql任务
     *
     * @param sql_context sql任务说明
     * @param id          sql任务id
     * @return
     */
    @SentinelResource(value = "sql_task_list", blockHandler = "handleReturn")
    @RequestMapping(value = "/sql_task_list", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<SqlTaskInfo>> sql_task_list(String sql_context, String id, String product_code, String dim_group) {

        try{
            List<SqlTaskInfo> sqlTaskInfos = new ArrayList<>();
            if(!StringUtils.isEmpty(sql_context)){
                sql_context=getLikeCondition(sql_context);
            }
            Map<String,List<String>> dimMap = dynamicPermissionByProductAndGroup(zdhPermissionService);
            sqlTaskInfos = sqlTaskMapper.selectByParams(getOwner(), sql_context, id, product_code, dim_group, dimMap.get("product_codes"), dimMap.get("dim_groups"));
            dynamicAuth(zdhPermissionService, sqlTaskInfos);

            return ReturnInfo.buildSuccess(sqlTaskInfos);
        }catch (Exception e){
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("sparksql任务列表", e);
        }

    }

    /**
     * 批量删除sql任务
     *
     * @param ids
     * @return
     */
    @SentinelResource(value = "sql_task_delete", blockHandler = "handleReturn")
    @RequestMapping(value = "/sql_task_delete", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo sql_task_delete(String[] ids) {
        try {
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, sqlTaskMapper, sqlTaskMapper.getTable(), ids, getAttrDel());
            sqlTaskMapper.deleteLogicByIds(sqlTaskMapper.getTable(),ids, new Timestamp(System.currentTimeMillis()));
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "删除成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "删除失败", e);
        }
    }

    /**
     * 新增sql 任务
     *
     * @param sqlTaskInfo
     * @return
     */
    @SentinelResource(value = "sql_task_add", blockHandler = "handleReturn")
    @RequestMapping(value = "/sql_task_add", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    @Transactional(propagation= Propagation.NESTED)
    public ReturnInfo sql_task_add(SqlTaskInfo sqlTaskInfo) {
        //String json_str=JsonUtil.formatJsonString(request.getParameterMap());
        try {
            String owner = getOwner();
            sqlTaskInfo.setOwner(owner);

            sqlTaskInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
            sqlTaskInfo.setCreate_time(new Timestamp(System.currentTimeMillis()));
            sqlTaskInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            sqlTaskInfo.setIs_delete(Const.NOT_DELETE);

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, sqlTaskInfo.getProduct_code(), sqlTaskInfo.getDim_group(), getAttrAdd());

            sqlTaskMapper.insertSelective(sqlTaskInfo);


            if (sqlTaskInfo.getUpdate_context() != null && !sqlTaskInfo.getUpdate_context().equals("")) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(sqlTaskInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(sqlTaskInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insertSelective(etlTaskUpdateLogs);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "新增成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "新增失败", e);
        }
    }

    /**
     * 更新sql 任务
     *
     * @param sqlTaskInfo
     * @return
     */
    @SentinelResource(value = "sql_task_update", blockHandler = "handleReturn")
    @RequestMapping(value = "/sql_task_update", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo sql_task_update(SqlTaskInfo sqlTaskInfo) {
        //String json_str=JsonUtil.formatJsonString(request.getParameterMap());
        try {
            String owner = getOwner();
            sqlTaskInfo.setOwner(owner);
            sqlTaskInfo.setUpdate_time(new Timestamp(System.currentTimeMillis()));
            sqlTaskInfo.setIs_delete(Const.NOT_DELETE);

            SqlTaskInfo oldSqlTaskInfo = sqlTaskMapper.selectByPrimaryKey(sqlTaskInfo.getId());

            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, sqlTaskInfo.getProduct_code(), sqlTaskInfo.getDim_group(), getAttrEdit());
            checkAttrPermissionByProductAndDimGroup(zdhPermissionService, oldSqlTaskInfo.getProduct_code(), oldSqlTaskInfo.getDim_group(), getAttrEdit());

            sqlTaskMapper.updateByPrimaryKeySelective(sqlTaskInfo);

            if (sqlTaskInfo.getUpdate_context() != null && !sqlTaskInfo.getUpdate_context().equals("")
                    && !sqlTaskInfo.getUpdate_context().equals(oldSqlTaskInfo.getUpdate_context())) {
                //插入更新日志表
                EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
                etlTaskUpdateLogs.setId(sqlTaskInfo.getId());
                etlTaskUpdateLogs.setUpdate_context(sqlTaskInfo.getUpdate_context());
                etlTaskUpdateLogs.setUpdate_time(new Timestamp(System.currentTimeMillis()));
                etlTaskUpdateLogs.setOwner(owner);
                etlTaskUpdateLogsMapper.insertSelective(etlTaskUpdateLogs);
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "更新成功", null);
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "更新失败", e);
        }
    }


    /**
     * 加载元数据信息
     *
     * @return
     */
    @SentinelResource(value = "load_meta_databases", blockHandler = "handleReturn")
    @RequestMapping(value = "/load_meta_databases", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo load_meta_databases() {
        Map<String, Object> js = JsonUtil.createEmptyMap();
        if (!SecurityUtils.getSubject().isPermitted("function:load_meta_databases()")) {
            js.put("data", "您没有权限访问,请联系管理员添加权限");
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "您没有权限访问,请联系管理员添加权限", "您没有权限访问,请联系管理员添加权限");
        }
        String url = JobCommon2.getZdhUrl(zdhHaInfoMapper, "").getZdh_url();
        try {
            String owner=getOwner();
            String databases = HttpUtil.postJSON(url + "/show_databases", JsonUtil.formatJsonString(JsonUtil.createEmptyMap()));

            List<meta_database_info> meta_database_infos = new ArrayList<meta_database_info>();

            System.out.println("databases:" + databases);
            List<Object> jary = JsonUtil.toJavaList(databases);
            for (Object o : jary) {
                String tableNames = HttpUtil.postJSON(url + "/show_tables", "{\"databaseName\":\"" + o.toString() + "\"}");
                List<Object> tableAry = JsonUtil.toJavaList(tableNames);
                meta_database_info meta_database_info_d = new meta_database_info();
                meta_database_info_d.setOwner(owner);
                metaDatabaseMapper.delete(meta_database_info_d);
                if (tableAry.isEmpty()) {
                    meta_database_info meta_database_info = new meta_database_info();
                    meta_database_info.setDb_name(o.toString());
                    meta_database_info.setTb_name("");
                    meta_database_info.setOwner(owner);
                    meta_database_info.setCreate_time(new Timestamp(System.currentTimeMillis()));
                    metaDatabaseMapper.insertSelective(meta_database_info);
                }

                for (Object t : tableAry) {
                    meta_database_info meta_database_info = new meta_database_info();
                    meta_database_info.setDb_name(o.toString());
                    meta_database_info.setTb_name(t.toString());
                    meta_database_info.setOwner(owner);
                    meta_database_info.setCreate_time(new Timestamp(System.currentTimeMillis()));
                    meta_database_infos.add(meta_database_info);
                    metaDatabaseMapper.insertSelective(meta_database_info);
                }
            }
            return ReturnInfo.build(RETURN_CODE.SUCCESS.getCode(), "同步成功", null);

        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.build(RETURN_CODE.FAIL.getCode(), "同步失败", e);
        }
    }

    /**
     * 查询当前数据仓库的所有数据库
     *
     * @return
     */
    @SentinelResource(value = "show_databases", blockHandler = "handleReturn")
    @RequestMapping(value = "/show_databases", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ReturnInfo<List<Map<String, Object>>> show_databases() {
        meta_database_info meta_database_info = new meta_database_info();
        try {
            String owner=getOwner();
            meta_database_info.setOwner(owner);
            List<meta_database_info> meta_database_infos = metaDatabaseMapper.select(meta_database_info);
            List<Map<String, Object>> jsa = JsonUtil.createEmptyListMap();
            Map<String, String> dbMap = new HashMap<>();

            for (meta_database_info o : meta_database_infos) {
                dbMap.put(o.getDb_name(), "");
                Map<String, Object> jo1 = JsonUtil.createEmptyMap();
                jo1.put("id", o.getDb_name() + "." + o.getTb_name());
                jo1.put("parent", o.getDb_name());
                jo1.put("text", o.getTb_name());
                jsa.add(jo1);
            }

            for (Map.Entry<String, String> entry : dbMap.entrySet()) {
                String dbName = entry.getKey();
                Map<String, Object> jo1 = JsonUtil.createEmptyMap();
                jo1.put("id", dbName);
                jo1.put("parent", "#");
                jo1.put("text", dbName);
                jsa.add(jo1);
            }

            return ReturnInfo.buildSuccess(jsa);

        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
            return ReturnInfo.buildError("查询数据仓库失败", e);
        }
    }


    /**
     * 获取表结构说明
     *
     * @param table
     * @return
     */
    @RequestMapping(value = "/desc_table", method = RequestMethod.GET)
    @ResponseBody
    public String desc_table(String table) {

        String url = JobCommon2.getZdhUrl(zdhHaInfoMapper, "").getZdh_url();
        try {
            Map<String, Object> p = JsonUtil.createEmptyMap();
            p.put("table", table);
            String desc_table = HttpUtil.postJSON(url + "/desc_table", JsonUtil.formatJsonString(p));
            return desc_table;
        } catch (Exception e) {
            LogUtil.error(this.getClass(), e);
        }


        return "";
    }

}
