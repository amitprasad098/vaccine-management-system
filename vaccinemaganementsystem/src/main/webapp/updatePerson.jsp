<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form action="updatePerson" method = "post">
Aadhar Number:
<input type = "text" name = "aadharNumber"/>
<br>
Date:
<input type = "date" min = "2021-01-01" max="2030-12-31" name = "shotDate"/>
<br>
<input type = "submit"/>
</form>
<a href="PersonMenu">person Menu</a>
</body>
</html>