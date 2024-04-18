<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page import="com.myuniversity.web.jdbc.Student"%>
<%@ page import = "java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>View Students</title>
	
	<link type="text/css" rel="stylesheet" href="css/style.css">
</head>
<body>
	<div id="wrapper">
		<div id="header">
			<h2> My University</h2>
		</div>
	</div>
	
	<div id = "container">
		<div id = "content">
			<!--put a new button: Add student  -->
			<input type="button" value="Add Student"
					onclick="window.location.href='add_student_form.jsp'; return false;"
					class="add_student_button"/>
			<table border = "1">
				<tr>
					<th>ID</th>
					<th>First Name</th>
					<th>Last Name</th>
					<th>Email</th>
					<th>Action</th>
				</tr>
				
				<c:forEach var="temp_student" items="${students_list}">
					<!--Set up a link for each student  -->
					<c:url var="templink" value="StudentControllerServlet">
						<c:param name="command" value="LOAD"></c:param>
						<c:param name="student_id" value="${temp_student.id}"></c:param>
					</c:url>
					<!-- Set up a link for delete -->
					<c:url var="deletelink" value="StudentControllerServlet">
						<c:param name="command" value="DELETE"></c:param>
						<c:param name="student_id" value="${temp_student.id}"></c:param>
					</c:url>
					<tr>
						<td>${temp_student.id}</td>
						<td>${temp_student.first_name}</td>
						<td>${temp_student.last_name}</td>
						<td>${temp_student.email}</td>
						<td><a href="${templink}">Update</a>
							| 
							<a href="${deletelink}" onclick="if (!(confirm('Are you sure want to delete this student?'))) return false">Delete</a>
						</td>
					</tr>
				</c:forEach>
			
			</table>
		</div>
	</div>
	
</body>
</html>