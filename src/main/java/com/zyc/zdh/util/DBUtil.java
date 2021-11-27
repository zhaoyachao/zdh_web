package com.zyc.zdh.util;


import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DBUtil{
    /**
     * 得到数据库连接
     * @return
     * @throws Exception
     */
    public Connection getConnection(String driver,String url,String username,String password) throws Exception{
        //3. 通过key-value 的方式得到对应的值
        //4.加载运行时类对象
        Class.forName(driver);
        //5通过DriverManager得到连接
        Connection connection = DriverManager.getConnection(url,username,password);
        return connection;

    }
    /**
     * 释放资源的方法
     * @param connection
     * @param statement
     * @param resultSet
     */
    public void release(Connection connection,Statement statement,ResultSet resultSet){
        try {
            if(resultSet!=null){
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
             if(statement!=null){
                 statement.close();
             }
        } catch (Exception e) {
            // TODO: handle exception
        }
       try{
            if(connection!=null){
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    /**
     * 查询数据库的方法
     * @param sql        字符串，要执行的sql语句  如果其中有变量的话，就用  ‘"+变量+"’
     */
    public List<String> R(String driver,String url,String username,String password,String sql, Object ...args) throws Exception{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection(driver,url,username,password);
            preparedStatement = connection.prepareStatement(sql);
            if(args !=null){
                for (int i = 0; i < args.length; i++) {
                    preparedStatement.setObject(i+1, args[i]);
                }
            }
            resultSet = preparedStatement.executeQuery();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            List<String> result=new ArrayList<String>();
            while(resultSet.next()!=false){
                //这里可以执行一些其他的操作
                for (int i = 1; i <= columnCount; i++) {
                    System.out.println(resultSet.getString(i));
                }
                result.add(resultSet.getString(1));
            }


           return result;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }finally {
            release(connection, preparedStatement, resultSet);
        }
    }

    /**
     * 查询数据库的方法
     * @param sql        字符串，要执行的sql语句  如果其中有变量的话，就用  ‘"+变量+"’
     */
    public List<String> R2(String driver,String url,String username,String password,String sql, Object ...args){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection(driver,url,username,password);
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i+1, args[i]);
            }
            resultSet = preparedStatement.executeQuery();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            List<String> result=new ArrayList<String>();
            while(resultSet.next()!=false){
                //这里可以执行一些其他的操作
                for (int i = 1; i <= columnCount; i++) {
                    System.out.println(resultSet.getString(i));
                }
                result.add(resultSet.getString(1));
            }


            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            release(connection, preparedStatement, resultSet);
        }
    }

    /**
     * 获取所有的表名
     * @param driver
     * @param url
     * @param username
     * @param password
     * @param sql
     * @param args
     * @return
     * @throws Exception
     */
    public List<String> R3(String driver,String url,String username,String password,String sql, Object ...args) throws Exception{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            List<String> result=new ArrayList<String>();
            connection = getConnection(driver,url,username,password);

            if(url.startsWith("jdbc:clickhouse")){
                preparedStatement=connection.prepareStatement("select database,name from system.tables where database != 'system'");
                resultSet=preparedStatement.executeQuery();
                while (resultSet.next()) {
                    result.add(resultSet.getString("database")+"."+resultSet.getString("name"));
                }

                return result;
            }

            DatabaseMetaData dbm=connection.getMetaData();

            ResultSet rs=dbm.getTables(connection.getCatalog(), username.toUpperCase(), "%", new String[] { "TABLE"});
            while (rs.next()) {
                if(url.startsWith("jdbc:mysql")){
                    result.add(rs.getString("TABLE_NAME"));
                }else if(url.startsWith("jdbc:oracle")){
                    if(rs.getString("TABLE_SCHEM").toLowerCase().equals(username.toLowerCase()))
                        result.add(rs.getString("TABLE_NAME"));
                }else if(url.startsWith("jdbc:postgresql")){
                    result.add(rs.getString("TABLE_NAME"));
                }else{
                    result.add(rs.getString("TABLE_NAME"));
                }

            }
            return result;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }finally {
            release(connection, preparedStatement, resultSet);
        }
    }

    public List<String> R4(String driver,String url,String username,String password,String sql, Object ...args) throws Exception{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection(driver,url,username,password);
            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount=resultSetMetaData.getColumnCount();
            List<String> result=new ArrayList<String>();
            for (int i = 1; i <= columnCount; i++) {
                System.out.println(resultSetMetaData.getColumnName(i));
                result.add(resultSetMetaData.getColumnName(i));
            }
            return result;

        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }finally {
            release(connection, preparedStatement, resultSet);
        }
    }


    public List<Map<String,Object>> R5(String driver,String url,String username,String password,String sql, Object ...args) throws Exception{
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = getConnection(driver,url,username,password);
            preparedStatement = connection.prepareStatement(sql);

            resultSet = preparedStatement.executeQuery();

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            List<Map<String,Object>> result=new ArrayList<>();
            while(resultSet.next()!=false){
                //这里可以执行一些其他的操作
                Map<String,Object> rmap=new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    rmap.put(resultSetMetaData.getColumnName(i),resultSet.getString(i));
                }
                result.add(rmap);
            }

            return result;

        } catch (Exception ex) {
            //ex.printStackTrace();
            throw ex;
        }finally {
            release(connection, preparedStatement, resultSet);
        }
    }

    /**
     * 数据库记录增删改的方法
     * @param sql        字符串，要执行的sql语句  如果其中有变量的话，就用  ‘"+变量+"’
     */
    public String[] CUD(String driver,String url,String username,String password,String sql, Object ...args){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;
        String ret="true";
        String e_msg="";
        try {
            connection = getConnection(driver,url,username,password);
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i+1, args[i]);
            }
            preparedStatement.execute();
            //这里可以根据返回结果(影响记录的条数)进行判断，该语句是否执行成功
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            e_msg=e.getMessage();
            ret="false";
        }finally {
            release(connection, preparedStatement, null);
        }

        return new String[]{ret,e_msg};
    }

    public String[] CUD(String driver,String url,String username,String password,String[] sqls){
        Connection connection = null;
        Statement preparedStatement = null;
        int result = 0;
        String ret="true";
        String e_msg="";
        try {
            connection = getConnection(driver,url,username,password);
            preparedStatement = connection.createStatement();
            for(String sql : sqls){
                preparedStatement.addBatch(sql);
            }
            preparedStatement.executeBatch();
            //这里可以根据返回结果(影响记录的条数)进行判断，该语句是否执行成功
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            e_msg=e.getMessage();
            ret="false";
        }finally {
            release(connection, preparedStatement, null);
        }

        return new String[]{ret,e_msg};
    }
}