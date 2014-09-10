<%-- 
    Document   : list
    Created on : 09.09.2014, 10:00:21
    Author     : sbt-barinov-sv
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World! LIST</h1>
        <ul>
            <c:forEach var="entry" items="${list}">
                <li><a href="${entry}">${entry}</a></li>
            </c:forEach>
        </ul>
    </body>
</html>
