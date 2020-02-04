package com.zyc.zspringboot.util;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;



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
    public List<String> R(String driver,String url,String username,String password,String sql, Object ...args){
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
     * 数据库记录增删改的方法
     * @param sql        字符串，要执行的sql语句  如果其中有变量的话，就用  ‘"+变量+"’
     */
    public void CUD(String driver,String url,String username,String password,String sql, Object ...args){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;
        try {
            connection = getConnection(driver,url,username,password);
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i+1, args[i]);
            }
            result = preparedStatement.executeUpdate();
            //这里可以根据返回结果(影响记录的条数)进行判断，该语句是否执行成功
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            release(connection, preparedStatement, null);
        }
    }

}