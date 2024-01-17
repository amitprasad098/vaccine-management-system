<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form action="insertVaccineStock" method = "post">
Date:
<input type = "date" name = "date"/>
<br>
Name:
<input type = "text" name = "name"/>
<br>
Count:
<input type = "text" name = "initial"/>
<br>
<input type = "submit"/>
</form>
<a href = "VaccineMenu">vaccine Menu</a>
</body>
</html>