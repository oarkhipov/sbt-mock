<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: sbt-bochev-as
  Date: 25.12.2015
  Time: 9:59
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Modify system</title>
</head>
<body>
<form method="post">
    <table>
        <tr>
            <td>System:</td>
            <td>
                <input type="text" name="name" id="name" size="25" value="${system.systemName}"/>
            </td>
        </tr>
        <tr>
            <td>Protocol:</td>
            <td>
                <select name="protocol" id="type">
                    <option value="JMS"
                            <c:if test="${system.protocol.equals('JMS')}">selected</c:if> >JMS
                    </option>
                    <option value="SOAP"
                            <c:if test="${system.protocol.equals('SOAP')}">selected</c:if> >SOAP
                    </option>
                </select>
            </td>
        </tr>
        <tr>
            <td>rootXsd file:</td>
            <td><input type="text" name="rootXsd" size="50" placeholder="File name from system root or URL"
                       value="${system.remoteRootSchema}"/></td>
        </tr>
        <tr>
            <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
            <td style="vertical-align: top">integration Point Selector</td>
            <td>
                <script>
                    function addElement(namespace, elementName) {
                        var span = document.createElement('span');
                        var out = "<input name=\"integrationPointSelectorNamespace[]\" type=\"text\"  placeholder=\"Namespace\" value=\"" +
                                namespace +
                                "\" size=\"80\"/>" +
                                "<input name=\"integrationPointSelectorElementName[]\" type=\"text\" placeholder=\"Blank element name if it's last\" value=\"" +
                                elementName + "\" size=\"25\"><input type=\"button\" value=\"-\" onclick='removeElement(this)'/><br/>";
                        span.innerHTML = out;
                        var div = document.getElementById("integrationPointSelector");
                        div.appendChild(span);
                    }
                    function removeElement(obj) {
                        var parent = obj.parentElement;
                        parent.parentElement.removeChild(parent);
                    }
                </script>
                <div id="integrationPointSelector">
                    <c:forEach var="selector" items="${system.integrationPointSelector}">
                        <script>
                            addElement("${selector.namespace}", "${selector.element}");
                        </script>
                    </c:forEach>
                </div>
                <input type="button" value="+" onclick="addElement('','')"/>
            </td>
        </tr>
        <tr>
            <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
            <td>Queue connection factory</td>
            <td><input type="text" name="queueConnectionFactory" placeholder="JNDI Name" value="${system.queueConnectionFactory}"/></td>
        </tr>
        <tr>
            <td>Mock income queue</td>
            <td><input type="text" name="mockIncomeQueue" placeholder="JNDI Name" value="${system.mockIncomeQueue}"/></td>
        </tr>
        <tr>
            <td>Mock outcome queue</td>
            <td><input type="text" name="mockOutcomeQueue" placeholder="JNDI Name" value="${system.mockOutcomeQueue}"/></td>
        </tr>
        <tr>
            <td>Driver outcome queue</td>
            <td><input type="text" name="driverOutcomeQueue" placeholder="JNDI Name" value="${system.driverOutcomeQueue}"/></td>
        </tr>
        <tr>
            <td>Driver income queue</td>
            <td><input type="text" name="driverIncomeQueue" placeholder="JNDI Name" value="${system.driverIncomeQueue}"/></td>
        </tr>
    </table>
    <br/>
    <input type="submit" value="SAVE"/>
</form>
</body>
</html>
