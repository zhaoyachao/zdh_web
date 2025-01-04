package com.zyc.zdh.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class DBUtilTest {
    public Logger logger= LoggerFactory.getLogger(this.getClass());
    @Test
    public void dbConnection(){

        String driver="oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@10.136.1.61:1521:orcl";
        String username = "root";
        String password = "123";
        try {
            List<String> list=new DBUtil().R3(driver,url,username,password,"");

            for(String table:list){
                System.out.println("表名:"+table);
            }

        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
        }

    }

    @Test
    public void dbConnection2(){

        String driver="oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@192.168.110.11:1521:XE";
        String username = "zyc";
        String password = "123456";
        try {
            List<String> list=new DBUtil().R3(driver,url,username,password,"");

            for(String table:list){
                System.out.println("表名:"+table);
            }

        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
        }

    }



    @Test
    public void dbConnectionMysql(){

        String driver="com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/mydb?serverTimezone=GMT%2B8&useSSL=false";
        String username = "zyc";
        String password = "123456";
        try {
            List<String> list=new DBUtil().R3(driver,url,username,password,"");

            for(String table:list){
                System.out.println("表名:"+table);
            }

        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
        }

    }

    @Test
    public void dbConnectionGreenPlum(){

        String driver="com.pivotal.jdbc.GreenplumDriver";
        String url = "jdbc:pivotal:greenplum://192.168.110.10:5432;DatabaseName=postgres";
        String username = "zyc";
        String password = "123456";
        try {
            List<Map<String,Object>> re=new DBUtil().R5(driver,url,username,password,"select * from  public.t1 ");

            System.out.println(JsonUtil.formatJsonString(re));
            List<Map<String,Object>> re2=new DBUtil().R5(driver,url,username,password,"select * from information_schema.tables ");

            System.out.println(JsonUtil.formatJsonString(re2));

            List<String> list=new DBUtil().R3(driver,url,username,password,"");

            for(String table:list){
                System.out.println("表名:"+table);
            }

        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
        }

    }

    @Test
    public void dbConnectionGBase(){

        String driver="com.gbase.jdbc.Driver";
        String url = "jdbc:gbase://192.168.110.10:5258/gbase";
        String username = "root";
        String password = "";
        try {
            List<String> list=new DBUtil().R3(driver,url,username,password,"");

            for(String table:list){
                System.out.println("表名:"+table);
            }

        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
        }

    }


    @Test
    public void a(){
        String url="http://DEEP-2020KLZJDI:4040/api/v1/applications/local-1603508571432/jobs";

        try {
            List<NameValuePair> npl=new ArrayList<>();
            //npl.add(new BasicNameValuePair("status1","sab"));
            String result=HttpUtil.getRequest(url,npl);
            System.out.println(result);
            JSONArray jsonArray= JSON.parseArray(result);
            String jobGroup="jobGroup";
            List<String> killJobs=new ArrayList<>();
            for(Object jo:jsonArray){
                JSONObject j=(JSONObject) jo;
                if(j.getString(jobGroup).startsWith("769515191510503424")){
                    killJobs.add(j.getString(jobGroup));
                }
            }
            JSONObject js=new JSONObject();
            js.put("task_logs_id","111");//写日志使用
            js.put("jobGroups",killJobs);
            //发送杀死请求
            String kill_url="http://deep-2020klzjdi:60001/api/v1/kill";
            HttpUtil.postJSON(kill_url,js.toJSONString());
        } catch (Exception e) {
            String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
            logger.error(error, e);
        }

    }

    @Test
    public void testBoolean(){

        System.out.println(Boolean.valueOf("true"));
    }
}