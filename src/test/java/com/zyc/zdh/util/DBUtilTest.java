package com.zyc.zdh.util;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DBUtilTest {

    @Test
    public void dbConnection(){

        String driver="oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@10.136.1.61:1521:orcl";
        String username = "fxjk";
        String password = "fxjk123";
        try {
            List<String> list=new DBUtil().R3(driver,url,username,password,"");

            for(String table:list){
                System.out.println("表名:"+table);
            }

        } catch (Exception e) {
            e.printStackTrace();
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
            e.printStackTrace();
        }

    }
}