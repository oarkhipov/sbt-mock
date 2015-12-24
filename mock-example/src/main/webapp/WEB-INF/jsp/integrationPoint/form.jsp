<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: sbt-bochev-as
  Date: 23.12.2015
  Time: 17:18
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Тест</title>
</head>
<body>
<h3>Integration point info</h3>
<form method="post">
    <table>
        <tr>
            <td>System:</td>
            <td>
                <select name="system">
                    <c:forEach items="${systems}" var="system">
                        <option name="${system.systemName}"
                                <c:if test="${system.systemName.equals(systemName)}">selected</c:if> >${system.systemName}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>Integration point name:</td>
            <td><input type="text" name="name" size="25" value="${integrationPointName}"/></td>
        </tr>
        <tr>
            <td>Integration point type:</td>
            <td>
                <select name="type">
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
                        console.log(div);
                        div.appendChild(span);
                    }
                    function removeElement(obj) {
                        var parent = obj.parentElement;
                        parent.parentElement.removeChild(parent);
                    }
                </script>
                <div id="xpathValidation">
                    <c:forEach var="validatorElement" items="${xpathValidation}">
                        <script>
                            addElement("${validatorElement.namespace}", "${validatorElement.element}");
                        </script>
                    </c:forEach>
                </div>
                <input type="button" value="+" onclick="addElement('','')"/>
            </td>
        </tr>
        <tr>
            <td>Answer required</td>
            <td><label><input type="checkbox" name="answerRequired"
                              <c:if test="${isAnswerRequired}">checked</c:if> />required</label></td>
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
