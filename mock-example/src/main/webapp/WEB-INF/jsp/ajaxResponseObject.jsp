<%--
  Created by IntelliJ IDEA.
  User: sbt-bochev-as
  Date: 17.12.2014
  Time: 15:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
{
  "info":"<c:out value="${info}" escapeXml="true"/>",
  "error":"<c:out value="${error}" escapeXml="true"/>",
  "data":"<c:out value="${data}" escapeXml="true"/>"
}

