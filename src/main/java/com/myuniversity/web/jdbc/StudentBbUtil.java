package com.myuniversity.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;

public class StudentBbUtil {
	@Resource(name = "jdbc/my_university")
	DataSource dataSource ;
	
	public StudentBbUtil(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public List<Student> getStudents(){
		List<Student> students = new ArrayList<Student>();
		
		Connection myConnection = null;
		Statement myStatement = null;
		ResultSet myResultSet = null;
		
		try {
			//get a connection
			myConnection = dataSource.getConnection();
			
			// create sql statement
			String sql = "select * from student order by id";
			myStatement = myConnection.createStatement();
			
			//execute query
			myResultSet = myStatement.executeQuery(sql);
			
			
			// process the result set
			while( myResultSet.next()) {
				// retrieve data from result set row
				int id = myResultSet.getInt("id");
				String first_name = myResultSet.getString("first_name");
				String last_name = myResultSet.getString("last_name");
				String email = myResultSet.getString("email");
				
				// create new student object
				Student student  = new Student(id,first_name, last_name, email);
				
				// add it to the list of student
				students.add(student);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			// close JDBC object
			close(myConnection, myResultSet, myStatement);
		}
		return students;
	}
	
	
	/**
	 * Close objects
	 * NOTE: doesn't really close it ... just puts backs in connection pool
	 */
	private void close(Connection myConnection, ResultSet myResultSet, Statement myStatement) {
		try {
			if(myResultSet != null) {
				myResultSet.close();
			}
			if(myConnection != null) {
				myResultSet.close();
			}
			if(myStatement != null) {
				myResultSet.close();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}

	public void addStudent(Student student) {
		Connection myConnection = null;
		PreparedStatement myStatement = null;
		
		try {
			// get db connection
			myConnection = dataSource.getConnection();
			
			// create sql for insert
			String sql = "insert into student (first_name, last_name, email) values(?,?,?)";
			myStatement = myConnection.prepareStatement(sql);
			myStatement.setString(1, student.getFirst_name());
			System.out.println(student.getFirst_name());
			myStatement.setString(2, student.getLast_name());
			myStatement.setString(3, student.getEmail());
			
			// set the parameter value for student
			myStatement.execute();
			
			// execute sql insert
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			// clean up JDBC objects
			close(myConnection, null, myStatement);
		}
	}

	public Student getStudent(String id) {
		Student student = null;
		
		Connection myConnection = null;
		PreparedStatement myStatement = null;
		ResultSet mResultSet = null;
		int student_id;
		
		try {
			// convert student id to int
			student_id = Integer.valueOf(id);
			
			// get connection into database
			myConnection = dataSource.getConnection();
			
			// create sql to get selected student
			String sql = "select * from student where id = ?";
			
			// create prepared statement
			myStatement = myConnection.prepareStatement(sql);
			
			// set params
			myStatement.setInt(1, student_id);
			
			// execute statement
			mResultSet = myStatement.executeQuery();
			
			// retrieve data from result set row
			if(mResultSet.next()) {
				String first_name = mResultSet.getString("first_name");
				String last_name = mResultSet.getString("last_name");
				String email = mResultSet.getString("email");
				
				// use the student id during constructor
				student = new Student(student_id, first_name, last_name, email);
			} else {
				throw new Exception("Could not find student id: " + student_id);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//clean up JDBC connection
			close(myConnection, mResultSet, myStatement);
		}
		return student;
	}

	public void updateStudent(Student student) throws Exception{
		Connection myConnection = null;
		PreparedStatement myStatement = null;
		
		// get db connection
		myConnection = dataSource.getConnection();
		
		// create sql update statement
		String sql = "update student set first_name=?, last_name=?, email=? where id=?";
		
		// prepare statement
		myStatement = myConnection.prepareStatement(sql);
		
		// set params
		myStatement.setString(1, student.getFirst_name());
		myStatement.setString(2, student.getLast_name());
		myStatement.setString(3, student.getEmail());
		myStatement.setInt(4, student.getId());
		// execute SQL statement
		myStatement.execute();
		
		// close 
		close(myConnection, null, myStatement);
	}

	public void deleteStudent(String student_id) throws Exception {
		Connection myConnection = null;
		PreparedStatement myStatement = null;
		
		// convert string id to int
		int id = Integer.parseInt(student_id);
		
		// get db connection
		myConnection = dataSource.getConnection();
		
		// create sql delete statement
		String sql = "delete from student where id = ?";
		
		// prepare statement
		myStatement = myConnection.prepareStatement(sql);
		
		// set params
		myStatement.setInt(1, id);
		
		// execute query
		myStatement.execute();
		
		// close 
		close(myConnection, null, myStatement);
	}
}
