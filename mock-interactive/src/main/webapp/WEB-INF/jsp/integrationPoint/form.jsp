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
    <script src="<%=request.getContextPath()%>/resources/js/jquery-1.9.1.min.js"></script>
</head>
<body>
<form method="post">
    <table>
        <tr>
            <td>System:</td>
            <td>
                <select name="system" id="system" <c:if test="${!empty systemName}">disabled</c:if> onchange="getIntegrationPointSuggestionNames()">
                    <option value="-1">--select System Name--</option>
                    <c:forEach items="${systems}" var="system">
                        <option name="${system.systemName}"
                                <c:if test="${system.systemName.equals(systemName)}">selected</c:if> >${system.systemName}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>Integration point name:</td>
            <td>
                <select name="name" id="name">
                    <c:forEach items="${suggestedNames}" var="name">
                        <option name="${name}"
                                    <c:if test="${name.equals(integrationPointName)}">selected</c:if> >${name}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>Integration point type:</td>
            <td>
                <select name="type" id="type">
                    <option value="Mock"
                            <c:if test="${isMock}">selected</c:if> >MOCK
                    </option>
                    <option value="Driver"
                            <c:if test="${isDriver}">selected</c:if> >DRIVER
                    </option>
                </select>
            </td>
        </tr>
        <tr>
            <td>xpathValidation</td>
            <td>
                <script>
                    function addElement(namespace, elementName) {
                        var span = document.createElement('span');
                        var out = "<input name=\"xpathValidatorNamespace[]\" type=\"text\"  placeholder=\"Namespace\" value=\"" +
                                namespace +
                                "\" size=\"80\"/>" +
                                "<input name=\"xpathValidatorElementName[]\" type=\"text\" placeholder=\"Element name\" value=\"" +
                                elementName + "\" size=\"25\"><input type=\"button\" value=\"-\" onclick='removeElement(this)'/><br/>";
                        span.innerHTML = out;
                        var div = document.getElementById("xpathValidation");
                        div.appendChild(span);
                    }
                    function removeElement(obj) {
                        var parent = obj.parentElement;
                        parent.parentElement.removeChild(parent);
                    }
                    function getIntegrationPointSuggestionNames() {
                        var systemName = $("#system").val();
                        var integrationPointSelector = $("#name");
                        integrationPointSelector.text("");
                        if(systemName!=-1) {
                            console.log("system chosen: " + systemName);
                            $.ajax({
                                url: "../../api/" + systemName + "/suggestIpName/",
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
                <input type="button" value="+" onclick="addElement('','')"/>
            </td>
        </tr>
        <tr>
            <td>Answer required</td>
            <td><label><input type="checkbox" name="answerRequired"
                              <c:if test="${isAnswerRequired==null || isAnswerRequired}">checked</c:if> required</label></td>
        </tr>
        <tr>
            <td>xsdFile:</td>
            <td><input type="text" name="xsdFile" placeholder="File name from system root" value="${xsdFile}"/></td>
        </tr>
        <tr>
            <td>rootElement</td>
            <td>
                <input name="rootElementNamespace" type="text" placeholder="Namespace" value="${rootElement.namespace}"
                       size="80"/>
                <input name="rootElementName" type="text" placeholder="Element name" value="${rootElement.element}"
                       size="25"><br/>
            </td>
        </tr>
    </table>
    <br/>
    <input type="submit" value="SAVE"/>
</form>
</body>
</html>
