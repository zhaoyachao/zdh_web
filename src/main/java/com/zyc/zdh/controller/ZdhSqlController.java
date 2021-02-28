package com.zyc.zdh.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zdh.dao.EtlTaskUpdateLogsMapper;
import com.zyc.zdh.dao.MetaDatabaseMapper;
import com.zyc.zdh.dao.SqlTaskMapper;
import com.zyc.zdh.dao.ZdhHaInfoMapper;
import com.zyc.zdh.entity.EtlTaskUpdateLogs;
import com.zyc.zdh.entity.SqlTaskInfo;
import com.zyc.zdh.entity.meta_database_info;
import com.zyc.zdh.job.JobCommon2;
import com.zyc.zdh.job.SnowflakeIdWorker;
import com.zyc.zdh.util.HttpUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class ZdhSqlController extends BaseController{

    @Autowired
    ZdhHaInfoMapper zdhHaInfoMapper;
    @Autowired
    EtlTaskUpdateLogsMapper etlTaskUpdateLogsMapper;
    @Autowired
    SqlTaskMapper sqlTaskMapper;
    @Autowired
    MetaDatabaseMapper metaDatabaseMapper;

    /**
     * 模糊查询Sql任务
     * @param sql_context sql任务说明
     * @param id sql任务id
     * @return
     */
    @RequestMapping(value = "/sql_task_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String sql_task_list(String sql_context, String id) {

        List<SqlTaskInfo> sqlTaskInfos = new ArrayList<>();

        sqlTaskInfos = sqlTaskMapper.selectByParams(getUser().getId(), sql_context, id);

        return JSON.toJSONString(sqlTaskInfos);
    }

    /**
     * 批量删除sql任务
     * @param ids
     * @return
     */
    @RequestMapping("/sql_task_delete")
    @ResponseBody
    public String sql_task_delete(String[] ids) {

        for (String id : ids)
            sqlTaskMapper.deleteByPrimaryKey(id);

        JSONObject json = new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }

    /**
     * 新增sql 任务
     * @param sqlTaskInfo
     * @return
     */
    @RequestMapping("/sql_task_add")
    @ResponseBody
    public String sql_task_add(SqlTaskInfo sqlTaskInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        String owner = getUser().getId();
        sqlTaskInfo.setOwner(owner);
        debugInfo(sqlTaskInfo);

        sqlTaskInfo.setId(SnowflakeIdWorker.getInstance().nextId() + "");
        sqlTaskInfo.setCreate_time(new Timestamp(new Date().getTime()));


        sqlTaskMapper.insert(sqlTaskInfo);


        if (sqlTaskInfo.getUpdate_context() != null && !sqlTaskInfo.getUpdate_context().equals("")) {
            //插入更新日志表
            EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
            etlTaskUpdateLogs.setId(sqlTaskInfo.getId());
            etlTaskUpdateLogs.setUpdate_context(sqlTaskInfo.getUpdate_context());
            etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTaskUpdateLogs.setOwner(getUser().getId());
            etlTaskUpdateLogsMapper.insert(etlTaskUpdateLogs);
        }

        //dataSourcesServiceImpl.insert(dataSourcesInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    /**
     * 更新sql 任务
     * @param sqlTaskInfo
     * @return
     */
    @RequestMapping("/sql_task_update")
    @ResponseBody
    public String sql_task_update(SqlTaskInfo sqlTaskInfo) {
        //String json_str=JSON.toJSONString(request.getParameterMap());
        String owner = getUser().getId();
        sqlTaskInfo.setOwner(owner);
        debugInfo(sqlTaskInfo);

        sqlTaskMapper.updateByPrimaryKey(sqlTaskInfo);

        SqlTaskInfo sti = sqlTaskMapper.selectByPrimaryKey(sqlTaskInfo.getId());

        if (sqlTaskInfo.getUpdate_context() != null && !sqlTaskInfo.getUpdate_context().equals("")
                && !sqlTaskInfo.getUpdate_context().equals(sti.getUpdate_context())) {
            //插入更新日志表
            EtlTaskUpdateLogs etlTaskUpdateLogs = new EtlTaskUpdateLogs();
            etlTaskUpdateLogs.setId(sqlTaskInfo.getId());
            etlTaskUpdateLogs.setUpdate_context(sqlTaskInfo.getUpdate_context());
            etlTaskUpdateLogs.setUpdate_time(new Timestamp(new Date().getTime()));
            etlTaskUpdateLogs.setOwner(getUser().getId());
            etlTaskUpdateLogsMapper.insert(etlTaskUpdateLogs);
        }

        //dataSourcesServiceImpl.insert(dataSourcesInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    /**
     * 加载元数据信息
     * @return
     */
    @RequestMapping(value="/load_meta_databases", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String load_meta_databases() {
        JSONObject js=new JSONObject();
        if(!SecurityUtils.getSubject().isPermitted("function:load_meta_databases()")){
            js.put("data","您没有权限访问,请联系管理员添加权限");
            return js.toJSONString();
        }
        String url = JobCommon2.getZdhUrl(zdhHaInfoMapper,"").getZdh_url();
        try {
            String databases = HttpUtil.postJSON(url + "/show_databases", new JSONObject().toJSONString());

            List<meta_database_info> meta_database_infos = new ArrayList<meta_database_info>();

            System.out.println("databases:" + databases);
            JSONArray jary = JSON.parseArray(databases);
            for (Object o : jary) {
                JSONObject jo = new JSONObject();
                String tableNames = HttpUtil.postJSON(url + "/show_tables", "{\"databaseName\":\"" + o.toString() + "\"}");
                JSONArray tableAry = JSON.parseArray(tableNames);
                meta_database_info meta_database_info_d = new meta_database_info();
                meta_database_info_d.setOwner(getUser().getId());
                metaDatabaseMapper.delete(meta_database_info_d);
                if (tableAry.isEmpty()) {
                    meta_database_info meta_database_info = new meta_database_info();
                    meta_database_info.setDb_name(o.toString());
                    meta_database_info.setTb_name("");
                    meta_database_info.setOwner(getUser().getId());
                    meta_database_info.setCreate_time(new Timestamp(new Date().getTime()));
                    metaDatabaseMapper.insert(meta_database_info);
                }

                for (Object t : tableAry) {
                    meta_database_info meta_database_info = new meta_database_info();
                    meta_database_info.setDb_name(o.toString());
                    meta_database_info.setTb_name(t.toString());
                    meta_database_info.setOwner(getUser().getId());
                    meta_database_info.setCreate_time(new Timestamp(new Date().getTime()));
                    meta_database_infos.add(meta_database_info);
                    metaDatabaseMapper.insert(meta_database_info);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();

        }

        js.put("data","同步完成");
        return js.toJSONString();
    }

    /**
     * 查询当前数据仓库的所有数据库
     * @return
     */
    @RequestMapping("/show_databases")
    @ResponseBody
    public String show_databases() {
        meta_database_info meta_database_info = new meta_database_info();
        meta_database_info.setOwner(getUser().getId());
        List<meta_database_info> meta_database_infos = metaDatabaseMapper.select(meta_database_info);
        try {
            JSONArray jsa = new JSONArray();
            Map<String, String> dbMap = new HashMap<>();

            for (meta_database_info o : meta_database_infos) {
                dbMap.put(o.getDb_name(), "");
                JSONObject jo1 = new JSONObject();
                jo1.put("id", o.getDb_name() + "." + o.getTb_name());
                jo1.put("parent", o.getDb_name());
                jo1.put("text", o.getTb_name());
                jsa.add(jo1);
            }

            for (Map.Entry<String, String> entry : dbMap.entrySet()) {
                String dbName = entry.getKey();
                JSONObject jo1 = new JSONObject();
                jo1.put("id", dbName);
                jo1.put("parent", "#");
                jo1.put("text", dbName);
                jsa.add(jo1);
            }

            return jsa.toJSONString();

        } catch (Exception e) {
            e.printStackTrace();

        }


        return "";
    }

    /**
     * 此服务停用
     * @return
     */
    @RequestMapping("/show_tables")
    @ResponseBody
    public String show_tables() {

        String url = JobCommon2.getZdhUrl(zdhHaInfoMapper,"").getZdh_url();
        try {
            String tableNames = HttpUtil.postJSON(url + "/show_tables", "");

            return tableNames;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return "";
    }


    /**
     * 获取表结构说明
     * @param table
     * @return
     */
    @RequestMapping("/desc_table")
    @ResponseBody
    public String desc_table(String table) {

        String url = JobCommon2.getZdhUrl(zdhHaInfoMapper,"").getZdh_url();
        try {
            JSONObject p = new JSONObject();
            p.put("table", table);
            String desc_table = HttpUtil.postJSON(url + "/desc_table", p.toJSONString());
            return desc_table;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "";
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
                    e.printStackTrace();
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            }
        }
    }

}
