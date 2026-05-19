package com.zyc.zdh.hadoop;


import java.sql.*;


/**
 * 数据库操作工具类
 */
public class DBUtil {
    /**
     * 得到数据库连接
     *
     * @return
     * @throws Exception
     */
    public Connection getConnection(String driver, String url, String username, String password) throws Exception {
        //3. 通过key-value 的方式得到对应的值
        //4.加载运行时类对象
        Class.forName(driver);
        //5通过DriverManager得到连接
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection;

    }

    /**
     * 释放资源的方法
     *
     * @param connection
     * @param statement
     * @param resultSet
     */
    public void release(Connection connection, Statement statement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (Exception e) {
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 数据库记录增删改的方法
     *
     * @param sql 字符串，要执行的sql语句  如果其中有变量的话，就用  ‘"+变量+"’
     */
    public String[] CUD(String driver, String url, String username, String password, String sql, Object... args) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        int result = 0;
        String ret = "true";
        String e_msg = "";
        try {
            connection = getConnection(driver, url, username, password);
            preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }
            preparedStatement.execute();
            //这里可以根据返回结果(影响记录的条数)进行判断，该语句是否执行成功
            System.out.println(result);
        } catch (Exception e) {
            String error = "类:" + Thread.currentThread().getStackTrace()[1].getClassName() + " 函数:" + Thread.currentThread().getStackTrace()[1].getMethodName() + " 异常: {}";
            e_msg = e.getMessage();
            ret = "false";
        } finally {
            release(connection, preparedStatement, null);
        }

        return new String[]{ret, e_msg};
    }

    public String[] CUD(String driver, String url, String username, String password, String[] sqls) {
        Connection connection = null;
        Statement preparedStatement = null;
        int result = 0;
        String ret = "true";
        String e_msg = "";
        try {
            connection = getConnection(driver, url, username, password);
            preparedStatement = connection.createStatement();
            for (String sql : sqls) {
                preparedStatement.addBatch(sql);
            }
            preparedStatement.executeBatch();
            //这里可以根据返回结果(影响记录的条数)进行判断，该语句是否执行成功
            System.out.println(result);
        } catch (Exception e) {
            e_msg = e.getMessage();
            ret = "false";
        } finally {
            release(connection, preparedStatement, null);
        }

        return new String[]{ret, e_msg};
    }
}