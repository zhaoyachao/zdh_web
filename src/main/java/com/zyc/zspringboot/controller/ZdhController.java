package com.zyc.zspringboot.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zyc.zspringboot.entity.*;
import com.zyc.zspringboot.service.DataSourcesService;
import com.zyc.zspringboot.service.DispatchTaskService;
import com.zyc.zspringboot.service.EtlTaskService;
import com.zyc.zspringboot.service.ZdhLogsService;
import com.zyc.zspringboot.service.impl.DataSourcesServiceImpl;
import com.zyc.zspringboot.util.DBUtil;
import com.zyc.zspringboot.util.HttpUtil;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
public class ZdhController {


    @Autowired
    DataSourcesService dataSourcesServiceImpl;
    @Autowired
    EtlTaskService etlTaskService;
    @Autowired
    DispatchTaskService dispatchTaskService;
    @Autowired
    ZdhLogsService zdhLogsService;

    @RequestMapping("/data_sources_index")
    public String data_sources_index() {

        return "etl/data_sources_index";
    }

    @RequestMapping(value = "/data_sources_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String data_sources_list(String[] ids) {

        DataSourcesInfo dataSourcesInfo=new DataSourcesInfo();
        dataSourcesInfo.setOwner(getUser().getId());
        List<DataSourcesInfo> list = dataSourcesServiceImpl.select(dataSourcesInfo);

        return JSON.toJSONString(list);
    }

    @RequestMapping("/data_sources_delete")
    @ResponseBody
    public String deleteIds(Long[] ids) {
        dataSourcesServiceImpl.deleteBatchById(ids);

        JSONObject json = new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/data_sources_add")
    public String data_sources_add(HttpServletRequest request, HttpServletResponse response,Long id) {

        return "etl/data_sources_add";
    }

    @RequestMapping("/add_data_sources")
    @ResponseBody
    public String add_data_sources(DataSourcesInfo dataSourcesInfo) {
        dataSourcesInfo.setOwner(getUser().getId());
        dataSourcesServiceImpl.insert(dataSourcesInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/data_sources_update")
    @ResponseBody
    public String data_sources_update(DataSourcesInfo dataSourcesInfo) {
        dataSourcesInfo.setOwner(getUser().getId());
        dataSourcesServiceImpl.update(dataSourcesInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping("/etl_task_index")
    public String etl_task_index() {

        //选择输入数据源
        //配置表名/文件名
        //配置选择的列

        // 转换选择的列

        //选择输出数据源
        //配置表名/文件名

        return "etl/etl_task_index";
    }


    @RequestMapping(value = "/etl_task_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String etl_task_list(String[] ids) {
        List<EtlTaskInfo> list=new ArrayList<>();
        EtlTaskInfo etlTaskInfo=new EtlTaskInfo();
        etlTaskInfo.setOwner(getUser().getId());
        if(ids==null)
            list= etlTaskService.select(etlTaskInfo);
        else
            list.add(etlTaskService.selectById(ids[0]));

        return JSON.toJSONString(list);
    }

    @RequestMapping("/etl_task_delete")
    @ResponseBody
    public String etl_task_delete(Long[] ids) {
        etlTaskService.deleteBatchById(ids);

        JSONObject json = new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/etl_task_add_index")
    public String etl_task_add(HttpServletRequest request, HttpServletResponse response,Long id,String edit) {

        System.out.println(edit);
        request.setAttribute("edit",edit);
        return "etl/etl_task_add_index";
    }


    @RequestMapping("/etl_task_add")
    @ResponseBody
    public String etl_task_add(EtlTaskInfo etlTaskInfo) {
        etlTaskInfo.setOwner(getUser().getId());
        debugInfo(etlTaskInfo);

        etlTaskInfo.getColumn_data_list().forEach(column_data -> {
            System.out.println(column_data.getColumn_expr()+"="+column_data.getColumn_alias());
        });

        etlTaskService.insert(etlTaskInfo);
        //dataSourcesServiceImpl.insert(dataSourcesInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/etl_task_update")
    @ResponseBody
    public String etl_task_update(EtlTaskInfo etlTaskInfo) {

        etlTaskInfo.setOwner(getUser().getId());
        etlTaskService.update(etlTaskInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping("/etl_task_tables")
    @ResponseBody
    public String etl_task_tables(String id) {

        DataSourcesInfo dataSourcesInfo=dataSourcesServiceImpl.selectById(id);

        String jsonArrayStr=tables(dataSourcesInfo);

        System.out.println(jsonArrayStr);
        return jsonArrayStr;
    }

    private String tables(DataSourcesInfo dataSourcesInfo){

        String url=dataSourcesInfo.getUrl();


        if(url.toLowerCase().contains("jdbc:oracle")){

            List<String> list=new DBUtil().R(dataSourcesInfo.getDriver(),dataSourcesInfo.getUrl(),dataSourcesInfo.getUsername(),dataSourcesInfo.getPassword(),
                    "SELECT TABLE_NAME FROM USER_TABLES");
            return JSON.toJSONString(list);
        }else if(url.toLowerCase().contains("jdbc:mysql") || url.toLowerCase().contains("jdbc:mariadb")){
            List<String> list= new DBUtil().R(dataSourcesInfo.getDriver(),dataSourcesInfo.getUrl(),dataSourcesInfo.getUsername(),dataSourcesInfo.getPassword(),
                    "SELECT table_name FROM information_schema.TABLES where table_schema=?",tableSchema(dataSourcesInfo.getUrl()));

            return JSON.toJSONString(list);
        }else if(url.toLowerCase().contains("jdbc:postgresql")){

        }else if(url.toLowerCase().contains("jdbc:hive2")){

        }

        return "";
    }

    /**
     * mysql 使用
     * @param url
     * @return
     */
    private String tableSchema(String url){

        int index=url.split("\\?")[0].lastIndexOf("/");
        return url.split("\\?")[0].substring(index+1);

    }

    @RequestMapping("/etl_task_schema")
    @ResponseBody
    public String etl_task_schema(String id,String table_name) {

        DataSourcesInfo dataSourcesInfo=dataSourcesServiceImpl.selectById(id);

        String jsonArrayStr=schema(dataSourcesInfo,table_name);

        System.out.println(jsonArrayStr);
        return jsonArrayStr;
    }



    private String schema(DataSourcesInfo dataSourcesInfo,String table_name){

        String url=dataSourcesInfo.getUrl();


        if(url.toLowerCase().contains("jdbc:oracle")){

           List<String> list=new DBUtil().R(dataSourcesInfo.getDriver(),dataSourcesInfo.getUrl(),dataSourcesInfo.getUsername(),dataSourcesInfo.getPassword(),
                    "select COLUMN_NAME from user_tab_columns WHERE TABLE_NAME = ?",table_name);
            return JSON.toJSONString(list);
        }else if(url.toLowerCase().contains("jdbc:mysql") || url.toLowerCase().contains("jdbc:mariadb")){
            List<String> list= new DBUtil().R(dataSourcesInfo.getDriver(),dataSourcesInfo.getUrl(),dataSourcesInfo.getUsername(),dataSourcesInfo.getPassword(),
                    "select COLUMN_NAME from information_schema.COLUMNS where table_name = ?",table_name);

            return JSON.toJSONString(list);
        }else if(url.toLowerCase().contains("jdbc:postgresql")){

        }else if(url.toLowerCase().contains("jdbc:hive2")){

        }

        return "";
    }


    @RequestMapping("/dispatch_task_index")
    public String dispatch_task_index(){

        //配置调度环境
        //调度条件---定时,特定条件
        //选则ETL任务
        //点击执行任务


        return "/etl/dispatch_task_index";
    }

    @RequestMapping(value = "/dispatch_task_list", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String dispatch_task_list(String[] ids) {
        List<DispatchTaskInfo> list=new ArrayList<>();
        DispatchTaskInfo dispatchTaskInfo=new DispatchTaskInfo();
        dispatchTaskInfo.setOwner(getUser().getId());
        if(ids==null)
            list= dispatchTaskService.select(dispatchTaskInfo);
        else
            list.add(dispatchTaskService.selectById(ids[0]));

        return JSON.toJSONString(list);
    }

    @RequestMapping("/dispatch_task_add_index")
    public String dispatch_task_add_index(){

        //配置调度环境
        //调度条件---定时,特定条件
        //选则ETL任务
        //点击执行任务

        return "/etl/dispatch_task_add_index";
    }

    @RequestMapping("/dispatch_task_add")
    @ResponseBody
    public String dispatch_task_add(DispatchTaskInfo dispatchTaskInfo) {
        dispatchTaskInfo.setOwner(getUser().getId());
        debugInfo(dispatchTaskInfo);
        dispatchTaskService.insert(dispatchTaskInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping("/dispatch_task_delete")
    @ResponseBody
    public String dispatch_task_delete(Long[] ids) {
        dispatchTaskService.deleteBatchById(ids);

        JSONObject json = new JSONObject();
        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/dispatch_task_update")
    @ResponseBody
    public String dispatch_task_update(DispatchTaskInfo dispatchTaskInfo) {

        dispatchTaskInfo.setOwner(getUser().getId());
        dispatchTaskService.update(dispatchTaskInfo);

        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }

    @RequestMapping("/dispatch_task_execute")
    @ResponseBody
    public String dispatch_task_execute(DispatchTaskInfo dispatchTaskInfo) {

        debugInfo(dispatchTaskInfo);

       // dispatchTaskService.update(dispatchTaskInfo);
        String url="http://127.0.0.1:60001/api/v1/zdh";

        //获取调度任务信息
        DispatchTaskInfo dti=dispatchTaskService.selectById(dispatchTaskInfo.getId());

        String etl_task_id=dti.getEtl_task_id();
        //获取etl 任务信息
        EtlTaskInfo etlTaskInfo=etlTaskService.selectById(etl_task_id);

        Map<String,Object> map=(Map<String,Object>) JSON.parseObject(dti.getParams());
        //此处做参数匹配转换
        if(map!=null){
            System.out.println("自定义参数不为空,开始替换:"+dti.getParams());
            map.forEach((k,v) ->{
                System.out.println("key:"+k+",value:"+v);
                String filter=etlTaskInfo.getData_sources_filter_input();
                etlTaskInfo.setData_sources_filter_input(filter.replace(k,v.toString()));
                String clear=etlTaskInfo.getData_sources_clear_output();
                etlTaskInfo.setData_sources_clear_output(clear.replace(k,v.toString()));
            });


        }

        //获取数据源信息
        String data_sources_choose_input=etlTaskInfo.getData_sources_choose_input();
        String data_sources_choose_output=etlTaskInfo.getData_sources_choose_output();
        DataSourcesInfo dataSourcesInfoInput=dataSourcesServiceImpl.selectById(data_sources_choose_input);
        DataSourcesInfo dataSourcesInfoOutput=null;
        if( !data_sources_choose_input.equals(data_sources_choose_output)){
            dataSourcesInfoOutput=dataSourcesServiceImpl.selectById(data_sources_choose_output);
        }else{
            dataSourcesInfoOutput=dataSourcesInfoInput;
        }

        ZdhInfo zdhInfo=new ZdhInfo();
        zdhInfo.setZdhInfo(dataSourcesInfoInput,etlTaskInfo,dataSourcesInfoOutput,dti);


        try{
            HttpUtil.postJSON(url,JSON.toJSONString(zdhInfo));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }


        JSONObject json = new JSONObject();

        json.put("success", "200");
        return json.toJSONString();
    }


    @RequestMapping("/log_txt")
    public String log_txt() {

        return "etl/log_txt";
    }

    @RequestMapping(value="/zhd_logs", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String zhd_logs(String id,String start_time,String end_time){
        System.out.println("id:"+id+",start_time:"+start_time+",end_time:"+end_time);

        Timestamp ts_start=null;
        Timestamp ts_end=null;
        if(!start_time.equals("")){
            ts_start=Timestamp.valueOf(start_time+":00");
        }else{
            ts_start=Timestamp.valueOf("1970-01-01 00:00:00");
        }
        if(!start_time.equals("")){
            ts_end=Timestamp.valueOf(end_time+":00");
        }else{
            ts_end=Timestamp.valueOf("2999-01-01 00:00:00");
        }

        List<ZdhLogs> zhdLogs=zdhLogsService.selectByTime(id,ts_start,ts_end);
        Iterator<ZdhLogs> it=zhdLogs.iterator();
        StringBuilder sb=new StringBuilder();
        while(it.hasNext()){
            ZdhLogs next=it.next();
            String info="任务ID:"+next.getEtl_task_id()+",任务执行时间:"+next.getLog_time().toString()+",日志:"+next.getMsg();
            sb.append(info+"\r\n");
        }

        JSONObject jsonObject=new JSONObject();
        jsonObject.put("logs",sb.toString());
        return jsonObject.toJSONString();
    }


    public User getUser(){
        User user = (User) SecurityUtils.getSubject().getPrincipal();
        return user;
    }


    @RequestMapping("/layer")
    public String getLayer() {

        return "layer";
    }



    @RequestMapping("/form_basic")
    public String getform_basic() {

        return "form_basic";
    }

    @RequestMapping("/toastr_notifications")
    public String gettoastr_notifications() {

        return "toastr_notifications";
    }

    @RequestMapping("/graph_echarts")
    public String graph_echarts() {

        return "graph_echarts";
    }

    @RequestMapping("/index_v1")
    public String index_v1() {

        return "graph_echarts";
    }


    private void debugInfo(Object obj){
        Field[] fields = obj.getClass().getDeclaredFields();
        for(int i = 0 , len = fields.length; i < len; i++) {
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
