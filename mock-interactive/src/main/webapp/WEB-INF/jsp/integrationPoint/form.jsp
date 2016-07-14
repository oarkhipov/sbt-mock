<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: sbt-bochev-as
  Date: 23.12.2015
  Time: 17:18
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Modify integration point</title>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/integrationPoint/form.css">
    <script src="<%=request.getContextPath()%>/resources/js/integrationPoint/form.js"></script>
</head>
<body>
<form method="post" class="form-horizontal"
      action="<%=request.getContextPath()%>/api/ip/<c:choose><c:when test="${integrationPointName != ''}">update/${systemName}/${integrationPointName}</c:when><c:otherwise>add</c:otherwise></c:choose>/">
    <div class="form-group">
        <label for="system" class="control-label col-sm-2">System:</label>

        <div class="col-sm-10">
            <c:if test="${!adding && (!empty systemName)}"><input type="hidden" name="system" value="${systemName}"/></c:if>
            <select name="system" class="form-control" id="system" name="system"
                    <c:if test="${!adding && (!empty systemName)}">disabled="disabled"</c:if>
                    onchange="getIntegrationPointSuggestionNames()">
                <option value="-1">--select System Name--</option>
                <c:forEach items="${systems}" var="system">
                    <option name="${system.systemName}"
                            <c:if test="${system.systemName eq systemName}">selected</c:if> >${system.systemName}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="form-group">
        <label for="name" class="control-label col-sm-2">Integration point name:</label>

        <div class="col-sm-10">
            <c:if test="${!adding && (!empty integrationPointName)}"><input type="hidden" name="name" value="${integrationPointName}"/></c:if>
            <select name="name" id="name" class="form-control"
                    <c:if test="${!adding && (!empty systemName)}">disabled="disabled"</c:if>
                    >
                <c:forEach items="${suggestedNames}" var="name">
                    <option name="${name}"
                            <c:if test="${name.equals(integrationPointName)}">selected</c:if> >${name}</option>
                </c:forEach>
            </select>
        </div>
    </div>
    <div class="form-group">
        <label for="type" class="control-label col-sm-2">Integration point type:</label>

        <div class="col-sm-10">
            <select name="type" id="type" class="form-control">
                <option value="Mock"
                        <c:if test="${isMock}">selected</c:if> >MOCK
                </option>
                <option value="Driver"
                        <c:if test="${isDriver}">selected</c:if> >DRIVER
                </option>
            </select>
        </div>
    </div>
    <div class="form-group">
        <label for="xpathValidation" class="control-label col-sm-2">xpathValidation:</label>

        <div class="col-sm-10" style="text-align: right">
            <script>
                function addElement(namespace, elementName) {
                    var span = document.createElement('span');
                    span.setAttribute("class", "selectorContainer");
                    var out = "<input name=\"xpathValidatorNamespace[]\" type=\"text\"  placeholder=\"Namespace\" value=\"" +
                            namespace +
                            "\" class='selectorNamespace'/>" +
                            "<input name=\"xpathValidatorElementName[]\" type=\"text\" placeholder=\"Element name\" value=\"" +
                            elementName + "\" class='selectorElementName'><input type=\"button\" value=\"-\" onclick='removeElement(this)' class='btn btn-s btn-default'/>";
                    span.innerHTML = out;
                    var div = document.getElementById("xpathValidation");
                    div.appendChild(span);
                    applySelectize(span);
                }
                function removeElement(obj) {
                    var parent = obj.parentElement;
                    parent.parentElement.removeChild(parent);
                }
                function getIntegrationPointSuggestionNames() {
                    var systemName = $("#system").val();
                    var integrationPointSelector = $("#name");
                    integrationPointSelector.text("");
                    if (systemName != -1) {
//                            console.log("system chosen: " + systemName);
                        $.ajax({
                            url: "<%=request.getContextPath()%>/api/" + systemName + "/suggestIpName/",
                            success: function (data) {
                                data = $.parseJSON(data);
                                $.each(data, function (key, val) {
                                    integrationPointSelector.append($("<option>", {
                                        value: val,
                                        text: val
                                    }));
                                })
                            }
                        });
                    }
                }
            </script>
            <div id="xpathValidation">
                <c:forEach var="selector" items="${xpathValidation}">
                    <script>
                        addElement("${selector.namespace}", "${selector.element}");
                    </script>
                </c:forEach>
            </div>
            <input type="button" value="+" onclick="addElement('','')" class="btn btn-s btn-default"/>
        </div>
    </div>
    <div class="panel panel-default">
        <div class="panel-heading" style="text-align: left; cursor: pointer;" data-toggle="collapse"
             data-target="#advancedContent">
            <span class="glyphicon glyphicon-collapse-down"></span> <i>Advanced</i>
        </div>
        <div class="panel-body collapse" id="advancedContent">
            <div class="form-group">
                <label for="answerRequired" class="control-label col-sm-2">Answer required:</label>

                <div class="col-sm-10">
                    <input type="checkbox" name="answerRequired" id="answerRequired" class="form-control"
                           <c:if test="${isAnswerRequired==null || isAnswerRequired}">checked</c:if>/>
                </div>
            </div>
            <div class="form-group">
                <label for="xsdFile" class="control-label col-sm-2">Schema file:</label>

                <div class="col-sm-10">
                    <input type="text" name="xsdFile" id="xsdFile" placeholder="File name from system root"
                           value="${xsdFile}"
                           class="form-control"/>
                </div>
            </div>
            <div class="form-group">
                <label for="rootElementDiv" class="control-label col-sm-2">rootElement:</label>

                <div class="col-sm-10">
                    <div id="rootElementDiv" class="selectorContainer">
                        <input name="rootElementNamespace" class='selectorNamespace' type="text" placeholder="Namespace"
                               value="${rootElement.namespace}"/>
                        <input name="rootElementName" class='selectorElementName' type="text" placeholder="Element name"
                               value="${rootElement.element}">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div style="text-align: right">
        <input class="btn btn-success" type="submit" value="SAVE"/>
    </div>
</form>
<script>
    $().ready(function () {
        var system = $('#system');
        if (system.val() != -1) {
            system.change();
        }
    });
</script>
</body>
</html>
