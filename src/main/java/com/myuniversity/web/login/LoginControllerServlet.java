package com.myuniversity.web.login;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet(name = "LoginControllerServlet", urlPatterns = "/LoginControllerSevlet")
public class LoginControllerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@Resource(name="jdbc/my_university")
	DataSource dataSource;
	
	LoginDbUtils loginDbUtils;
	
	@Override
	public void init() throws ServletException {
		loginDbUtils = new LoginDbUtils(dataSource);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		boolean checker = Checker(req, resp);
		if(checker) {
			RequestDispatcher requestDispatcher = req.getRequestDispatcher("/StudentControllerServlet");
			requestDispatcher.forward(req, resp);
		}
		
	}
	
	protected boolean Checker(HttpServletRequest req, HttpServletResponse resp) {
		String username = req.getParameter("username");
		String password = req.getParameter("password");
		
		User user = new User(username, password);
		
		boolean checker = false;
		
		
		try {
			checker =loginDbUtils.SignIn(user);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return checker;
	}
}
