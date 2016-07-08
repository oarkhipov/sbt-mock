<%--
  Created by IntelliJ IDEA.
  User: sbt-bochev-as
  Date: 13.12.2014
  Time: 16:55
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
</head>
<body>
<div style="font-size: 80%; color: dimgray">
    <script>
        var systemName = "${systemName}";
        var integrationPointName = "${name}";
    </script>
    <b>Integration point:</b> <i><c:out value="${name}"/></i>&nbsp;&nbsp;
    <div class="editActions">
        <span class="btn btn-warning btn-xs glyphicon glyphicon-pencil" onclick="editIpForm()"></span>
        <span class="btn btn-danger btn-xs glyphicon glyphicon-trash" onclick="delIpForm()"></span>
    </div>
    <br/>
    <%--WAS 7 compatible style (legacy)--%>
    <c:if test='${protocol eq "JMS"}'>
        <b>Xpath assertion:</b> <i><c:out value="${xpath}"/></i>
    </c:if>
</div>

<form>
    <div id="codeWrapper">
        <span>Message</span>

        <div id="code"><c:out value="${message}" escapeXml="true"/></div>
        <div id="actionButtonsDiv">
            <label id="filterGenLabel" style="vertical-align: middle; padding-bottom: 6px">
                <input type="checkbox" id="filterGen" name="filterGen" alt="Filter generated message" checked>Filter
            </label>
            <span id="reset" title="Regenerate"
                  class="actionButtons glyphicon glyphicon-refresh btn btn-default"></span>
            &nbsp;&nbsp;
            <span id="validate" title="Validate"
                  class="actionButtons glyphicon glyphicon-ok-circle btn btn-default"></span>
            <span id="save" title="Save"
                  class="actionButtons glyphicon glyphicon-floppy-save btn btn-default"></span>
            &nbsp;&nbsp;
            <span id="test" title="Test"
                  class="actionButtons glyphicon glyphicon-wrench btn btn-default"></span>
            <c:if test="${link=='driver'}">
                &nbsp;&nbsp;
                <span id="send" title="Send"
                      class="actionButtons glyphicon glyphicon-send btn btn-default"></span>
            </c:if>
        </div>
        <div id="tabs">
            <ul class="nav nav-tabs">
                <li class="active"><a data-toggle="tab" href="#tabs-1"><span class="glyphicon glyphicon-console"></span> Script</a></li>
                <li><a data-toggle="tab" href="#tabs-2"><span class="glyphicon glyphicon-list-alt"></span> Test</a></li>
            </ul>
            <div class="tab-content">
                <div id="tabs-1" class="tab-pane active">
                    <div id="scriptCode"><c:out value="${script}" escapeXml="true"/></div>
                </div>
                <div id="tabs-2" class="tab-pane">
                    <div id="testCode"><c:out value="${test}" escapeXml="true"/></div>
                    <div id="testCodeButtons">
                        <span class="glyphicon glyphicon-refresh btn btn-default btn-xs"></span>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="resCodeWrapper">
        <span>Response</span>

        <div id="resCode"></div>
    </div>
</form>

<script src="<%=request.getContextPath()%>/resources/js/editor.js"></script>
<c:if test="${link=='driver'}">
    <script src="<%=request.getContextPath()%>/resources/js/editor_driver.js"></script>
</c:if>

</body>
</html>