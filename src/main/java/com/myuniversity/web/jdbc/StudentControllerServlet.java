package com.myuniversity.web.jdbc;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet(name = "StudentControllerServlet", urlPatterns = "/StudentControllerServlet")
public class StudentControllerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	private StudentBbUtil studentBbUtil;
	
	@Resource(name="jdbc/my_university")
	DataSource dataSource;
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		// create our student db util .. and pass in the connection pool/data source
		try {
			studentBbUtil = new StudentBbUtil(dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		//read the "command" parameter
		String commandString=req.getParameter("command");
		try {
			// if the command is missing, then default to listing students
			if (commandString == null) {
				commandString="LIST";
			}
			
			// route the appropriate method
			switch(commandString) {
				case "LIST":
					listStudents(req, resp);;
					break;
				case "ADD":
					addStudent(req,resp);
					break;
				case "LOAD":
					loadStudent(req,resp);
				case "UPDATE":
					updateStudent(req, resp);
					break;
				case "DELETE":
					deleteStudent(req, resp);
					break;
				default:
					listStudents(req, resp);
			}
			
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	private void deleteStudent(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// read student if from the form data
		String student_id = req.getParameter("student_id");
		
		// delete student from database
		studentBbUtil.deleteStudent(student_id);
		
		// send them back to list student page
		listStudents(req, resp);
		
	}

	private void updateStudent(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		// read student info from from data
		int id = Integer.parseInt(req.getParameter("StudentId"));
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String email = req.getParameter("email");
		
		// create a new student object
		Student student = new Student(id,firstName, lastName, email);
		
		// perform update on database
		studentBbUtil.updateStudent(student);
		
		// send them back to the list students page
		listStudents(req, resp);
	}

	private void loadStudent(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		
		// read student id from form data
		String id = req.getParameter("student_id");
		
		// get student from database 
		Student student = studentBbUtil.getStudent(id);
		
		// place student in the request attribute
		req.setAttribute("THE_STUDENT", student);
		
		// send to jsp page: update-student-form.jsp
		RequestDispatcher requestDispatcher = req.getRequestDispatcher("/update-student-form.jsp");
		requestDispatcher.forward(req, resp);
		
	}

	private void addStudent(HttpServletRequest req, HttpServletResponse resp) throws SQLException {
		// read student info from form data
		String firstName = req.getParameter("firstName");
		String lastName = req.getParameter("lastName");
		String email = req.getParameter("email");
		
		// create new student object
		Student student = new Student(firstName, lastName, email);
		
		// add the student to the database
		studentBbUtil.addStudent(student);
		
		// send back to main page
		listStudents(req, resp);
		
	}


	private void listStudents(HttpServletRequest req, HttpServletResponse resp) {
		// get Student from db util
		List<Student> students = studentBbUtil.getStudents();
		
		// add students to the request
		req.setAttribute("students_list", students);
		
		// send to JSP page ( view)
		RequestDispatcher requestDispatcher = req.getRequestDispatcher("view_student.jsp");
		
		try {
			requestDispatcher.forward(req, resp);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
