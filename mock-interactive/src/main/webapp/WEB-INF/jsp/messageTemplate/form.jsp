<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  ~ Copyright (c) 2016, Sberbank and/or its affiliates. All rights reserved.
  ~
  ~ Redistribution and use in source and binary forms, with or without
  ~ modification, are permitted provided that the following conditions
  ~ are met:
  ~
  ~   - Redistributions of source code must retain the above copyright
  ~     notice, this list of conditions and the following disclaimer.
  ~
  ~   - Redistributions in binary form must reproduce the above copyright
  ~     notice, this list of conditions and the following disclaimer in the
  ~     documentation and/or other materials provided with the distribution.
  ~
  ~   - Neither the name of Sberbank or the names of its
  ~     contributors may be used to endorse or promote products derived
  ~     from this software without specific prior written permission.
  ~
  ~ THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
  ~ IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
  ~ THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
  ~ PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
  ~ CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
  ~ EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
  ~ PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
  ~ PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
  ~ LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
  ~ NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
  ~ SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  --%>

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
      action="<%=request.getContextPath()%>/api/messageTemplate/<c:choose><c:when test="${template.templateId ne null}">update/${template.templateId}</c:when><c:otherwise>add/${systemName}/${integrationPointName}</c:otherwise></c:choose>/">
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
        <label for="expression">Dispatcher expression:
            <span id="groovyInfo" style="display: none;" data-toggle="popover" data-content="request variable contains request message. requestDom variable contains XmlParsed request. You should return result to activate GROOVY dispatching mechanism" class="glyphicon glyphicon-question-sign"></span>
        </label>
        <textarea name="expression" style="display: none"></textarea>
        <div id="expression" class="form-control"><c:out value="${template.dispatcherExpression}" escapeXml="true"/></div>
    </div>
    <div class="form-group" id="regexBlock" style="display: none">
        <label for="regexGroups">RegexGroups:
            <span data-toggle="popover" data-content="$0 group gets all chars, that matches pattern, $1 - first group etc." class="glyphicon glyphicon-question-sign"></span>
        </label>
        <input name="regexGroups" id="regexGroups" class="form-control" value="${template.regexGroups}"/>
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
    var regexBlock = $("#regexBlock");
    var groovyInfo = $("#groovyInfo");
    $("#type").change(function () {
        var val = $(this).val();

        if (val != "SEQUENCE") {
           expressionBlock.css("display", "block");
           valueBlock.css("display", "block");
       } else {
           expressionBlock.css("display", "none");
           valueBlock.css("display", "none");
       }

       if (val == "REGEX") {
           regexBlock.css("display", "block");
       } else {
           regexBlock.css("display", "none");
       }

        if (val == "GROOVY") {
            groovyInfo.css("display", "inline");
        } else {
            groovyInfo.css("display", "none");
        }
    });

    var expression = $("[name='expression']");

    expression.closest("form").submit(function () {
        expression.val(expressionEditor.getSession().getValue());

    });

    $().ready(function () {
        $("#type").change();
    });

    $('[data-toggle="popover"]').popover({
        trigger: 'hover',
        'placement': 'top'
    });
</script>
</body>
</html>
