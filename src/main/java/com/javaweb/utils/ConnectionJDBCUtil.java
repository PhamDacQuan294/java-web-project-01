package com.javaweb.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionJDBCUtil {
	static final String DB_URL = "jdbc:mysql://localhost:3306/java_web_project01";
	static final String USER = "root";
	static final String PASS = "123456";
	
	public static Connection getConnection() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
}
