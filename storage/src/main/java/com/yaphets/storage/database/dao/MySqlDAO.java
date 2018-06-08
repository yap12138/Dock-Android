package com.yaphets.storage.database.dao;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySqlDAO {
	private static final String driverName = "com.mysql.jdbc.Driver";
	private static final String url = "jdbc:mysql://120.78.139.85:3306/game_distribution_platform?useSSL=false";
	private static final String userName = "hogason";
	private static final String password = "ddd123456";

	public static Connection getConnection() {
		try {
			Class.forName(driverName);
			return DriverManager.getConnection(url, userName, password);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static void release(Connection con, Statement state) {
		if (state != null) {
			try {
				state.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
