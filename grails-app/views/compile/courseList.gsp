<%--
  Created by IntelliJ IDEA.
  User: ejmoore
  Date: 8/17/17
  Time: 12:48 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Course List</title>
</head>

<body>
<h1>Hello ${user.name}!</h1>
<g:each in = "${courseNames}" var = "course">
    <p>"${course}</p>
</g:each>
</body>
</html>