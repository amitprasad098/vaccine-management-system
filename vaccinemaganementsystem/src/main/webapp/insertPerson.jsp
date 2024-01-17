<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<form action="savePerson" method = "post">
Aadhar Number:
<input type = "text" name = "aadharNumber"/>
<br>
Name:
<input type = "text" name = "name"/>
<br>
Age:
<input type = "text" name = "age"/>
<br>
Date:
<input type = "date" name = "shotOneDate"/>
<br>
Current Address
<input type = "text" name = "addressOne"/>
<br>
Permanent Address
<input type = "text" name = "addressTwo"/>
<br>
<input type = "submit"/>
</form>
<a href= "PersonMenu">person Menu</a>
</body>
</html>