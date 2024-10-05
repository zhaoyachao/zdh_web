//package com.zyc.zdh.jdbc;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.SQLException;
//
//
//
///**
// * ClassName: MySqlUtil
// * @author zyc-admin
// * @date 2017年12月29日
// * @Description: TODO
// */
//public class MySqlUtil {
//	public Logger logger = LoggerFactory.getLogger(this.getClass());
//	String url = "jdbc:mysql://localhost:3306/mydb?"
//            + "user=root&password=123456&useUnicode=true&characterEncoding=UTF8";
//	Connection connection;
//	public void connect(){
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			connection = DriverManager.getConnection(url);
//
//
//		} catch (ClassNotFoundException e) {
//			// TODO Auto-generated catch block
//			String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
//			logger.error(error, e);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			String error = "类:"+Thread.currentThread().getStackTrace()[1].getClassName()+" 函数:"+Thread.currentThread().getStackTrace()[1].getMethodName()+ " 异常: {}";
//			logger.error(error, e);
//		}
//
//
//	}
//}
