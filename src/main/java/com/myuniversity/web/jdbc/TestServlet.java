package com.myuniversity.web.jdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet(name = "TestServlet", urlPatterns = "/testServlet")
public class TestServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	// Define data source/connection pool for Resource Injection
	@Resource(name = "jdbc/my_university")
	private DataSource dataSource;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Step 1: Set up the printWriter
		PrintWriter out = resp.getWriter();
		resp.setContentType("text/plain");
		
		// Step 2: Get a connection to the database
		Connection myConnection = null;
		Statement myStatement = null;
		ResultSet  myResultSet = null;
		
		try {
			myConnection = dataSource.getConnection();
			
			// Step 3: create a SQL state
			String sql = "SELECT * FROM STUDENT";
			myStatement = myConnection.createStatement();
			
			// Step 4: Execute SQL query
			myResultSet = myStatement.executeQuery(sql);
			
			// Step 5: Process the result process
			while (myResultSet.next()) {
				String email = myResultSet.getString("email");
				out.println(email);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
