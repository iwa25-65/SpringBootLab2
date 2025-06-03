<%@ page language="java" contentType="text/html; chartset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://www.springframework.org/tags/form" prefix="forms" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; chartset=UTF-8">
    <title>Add Student</title>
</head>
<body>
<h1>${message}</h1>

<h2>Student</h2>
<forms:form method="post" action="addStudent" modelAttribute="student">
    <form:label path="firstname">Firstname</form:label>
    <form:input path="firstname"/>
    <br>
    <form:label path="lastname">Lastname</form:label>
    <form:input path="lastname"/>
    <br>
    <form:label path="email">Email</form:label>
    <form:input path="email"/>
    <br>
    <form:label path="phone">Phone</form:label>
    <form:input path="phone"/>
    <br>
    <input type="submit" value="Add Student"/>
</forms:form>
</body>
</html>