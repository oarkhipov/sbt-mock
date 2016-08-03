<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: sbt-bochev-as
  Date: 02.08.2016
  Time: 17:18
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Message template</title>
</head>
<body>
<form method="post"
      action="<%=request.getContextPath()%>/api/messageTemplate/<c:choose><c:when test="${template.templateId != null}">update/${template.templateId}</c:when><c:otherwise>add/${systemName}/${integrationPointName}</c:otherwise></c:choose>/">
    <div class="form-group">
        <label for="name">Message template caption:</label>
        <input type="text" id="name" name="name" class="form-control" value="${template.caption}">
    </div>
    <div class="form-group">
        <label for="type">Dispatcher type:</label>
        <select name="type" id="type" class="form-control">
            <c:forEach var="type" items="${dispatcherTypes}">
                <option value="${type}"
                        <c:if test="${type eq template.dispatcherType}">selected</c:if> >${type}</option>
            </c:forEach>
        </select>
    </div>
    <div class="form-group" id="expressionBlock" style="display: none">
        <label for="expression">Dispatcher expression:</label>
        <textarea name="expression" style="display: none"></textarea>
        <div id="expression" class="form-control"><c:out value="${template.dispatcherExpression}" escapeXml="true"/></div>
    </div>
    <div class="form-group" id="valueBlock" style="display: none">
        <label for="value">Expected value:</label>
        <input name="value" id="value" class="form-control" value="${template.value}"/>
    </div>
    <div style="text-align: right">
        <input class="btn btn-success" type="submit" value="SAVE"/>
    </div>
</form>
<style>
    #expression {
        height: 300px;
    }
</style>
<script>
    expressionEditor = ace.edit("expression");
    expressionEditor.getSession().setMode("ace/mode/groovy");
    expressionEditor.setTheme("ace/theme/xcode");
    expressionEditor.$blockScrolling = Infinity;
    var expressionBlock = $("#expressionBlock");
    var valueBlock = $("#valueBlock");
    $("#type").change(function () {
       if ($(this).val() != "SEQUENCE") {
           expressionBlock.css("display", "block");
           valueBlock.css("display", "block");
       } else {
           expressionBlock.css("display", "none");
           valueBlock.css("display", "none");
       }
    });

    var expression = $("[name='expression']");

    expression.closest("form").submit(function () {
        expression.val(expressionEditor.getSession().getValue());

    });

    $().ready(function () {
        $("#type").change();
    })
</script>
</body>
</html>
