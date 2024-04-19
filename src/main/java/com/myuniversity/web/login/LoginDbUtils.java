package com.myuniversity.web.login;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.annotation.Resource;
import javax.sql.DataSource;

public class LoginDbUtils {
	
	@Resource(name="jdbc/my_university")
	DataSource dataSource;
	
	public LoginDbUtils(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	public boolean SignIn(User user) throws Exception {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet resultSet = null;
		
		String username = user.getUsername();
		String password = user.getPassword();
		
		// get connection
		connection = dataSource.getConnection();
		
		// create statement
		String sql = "select * from \"user\" where username = ? and password = ?";
		statement = connection.prepareStatement(sql);
		statement.setString(1, username);
		statement.setString(2, password);
		
		// execute statement
		resultSet = statement.executeQuery();
		
		
		String db_user_name = null;
		String db_password = null;
		while(resultSet.next()) {
			
			db_user_name = resultSet.getString("username");
			db_password = resultSet.getString("password");
			
		}
		
		return db_password != null && db_user_name != null;
		
	}
	
}
