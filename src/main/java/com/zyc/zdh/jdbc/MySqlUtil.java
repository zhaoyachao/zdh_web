package com.zyc.zdh.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;



/**
 * ClassName: MySqlUtil   
 * @author zyc-admin
 * @date 2017年12月29日  
 * @Description: TODO  
 */
public class MySqlUtil {
	String url = "jdbc:mysql://localhost:3306/mydb?"
            + "user=root&password=123456&useUnicode=true&characterEncoding=UTF8";
	Connection connection;
	public void connect(){
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(url);
			
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}
}
