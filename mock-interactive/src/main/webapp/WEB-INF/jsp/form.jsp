<%-- 
    Document   : form
    Created on : 09.09.2014, 10:20:05
    Author     : sbt-barinov-sv
--%>

<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mock-interactive</title>
        <style type="text/css">
            .links {
                min-width:100px;
                float:left;
            }
            .workarea {
                float:left;
                width:70%;
                min-width:400px;
                min-height: 300px;
            }
            .objarea {
                display: block;
                width: 100%;
                min-height: 300px;
            }
        </style>
    </head>
    <body>
        <h1>Back: <a href="../">UP</a></h1>
        <div class="parent">
            <div class="links">
                <ul>
                    <c:forEach var="system" items="${list}">
                        <li><a href="${system}/">${system}</a></li>
                    </c:forEach>
                </ul>
            </div>
            <div class="workarea">
                <c:if test="${object != null}">
                    <form method="POST">
                        <div >
                            <textarea class="objarea" name="object">
<c:out value="${object}" escapeXml="true"/>
                            </textarea>

                        </div>
                        <div><input type="submit" value="OK"></div>
                    </form>
                </c:if>
            </div>
        </div>
</body>
</html>