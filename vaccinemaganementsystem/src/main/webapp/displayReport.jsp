<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

<h3>Report</h3>
<table border=1>
<tr>
<th>AadharNumber</th>
<th>Name</th>
<th>Age</th>
<th>shotOneDate</th>
<th>shotTwoDate</th>
<th>addressOne</th>
<th>addressTwo</th>
</tr>
<c:forEach items="${persons}" var="person" >
<tr>
 <td> <c:out value="${person.aadharNumber}" /></td>
 <td> <c:out value="${person.name}" /></td>
 <td> <c:out value="${person.age}" /></td>
 <td> <c:out value="${person.shotOneDate}" /></td>
 <td> <c:out value="${person.shotTwoDate}" /></td>
  <td> <c:out value="${person.addressOne}" /></td>
   <td> <c:out value="${person.addressTwo}" /></td>
</tr>
</c:forEach>
</table>
<br>
<a href="PersonMenu" >Main Menu</a>

</body>
</html>