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
      action="<%=request.getContextPath()%>/api/ip/<c:choose><c:when test="${integrationPointName ne ''}">update/${systemName}/${integrationPointName}</c:when><c:otherwise>add</c:otherwise></c:choose>/">
    <div class="form-group">
        <label for="enabled" class="control-label col-sm-2">Enabled:</label>

        <div class="col-sm-10">
            <input type="checkbox" name="enabled" id="enabled" class="form-control"
                   <c:if test="${enabled eq null || enabled}">checked</c:if>/>
        </div>
    </div>
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
                <c:forEach items="${suggestedNames}" var="ipName">
                    <option name="${ipName}"
                            <c:if test="${ipName eq integrationPointName}">selected</c:if> >${ipName}</option>
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
        <label for="delayMs" class="control-label col-sm-2">Delay:</label>

        <div class="col-sm-10">
            <input type="text" name="delayMs" id="delayMs" placeholder="Delay of integration point in ms"
                   value="${delayMs}"
                   class="form-control"/>
        </div>
    </div>
    <div class="form-group">
        <label for="sequenceEnabled" class="control-label col-sm-2">Sequence enabled:</label>

        <div class="col-sm-10">
            <input type="checkbox" name="sequenceEnabled" id="sequenceEnabled" class="form-control"
                   <c:if test="${sequenceEnabled eq null || sequenceEnabled}">checked</c:if>/>
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
                    if ((systemName != -1) && ${empty integrationPointName}) {
                        var integrationPointSelector = $("#name");
                        integrationPointSelector.text("");
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
                <label for="validationEnabled" class="control-label col-sm-2">Validation enabled:</label>

                <div class="col-sm-10">
                    <input type="checkbox" name="validationEnabled" id="validationEnabled" class="form-control"
                           <c:if test="${validationEnabled eq null || validationEnabled}">checked</c:if>/>
                </div>
            </div>
            <div class="form-group">
                <label for="answerRequired" class="control-label col-sm-2">Answer required:</label>

                <div class="col-sm-10">
                    <input type="checkbox" name="answerRequired" id="answerRequired" class="form-control"
                           <c:if test="${isAnswerRequired eq null || isAnswerRequired}">checked</c:if>/>
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
