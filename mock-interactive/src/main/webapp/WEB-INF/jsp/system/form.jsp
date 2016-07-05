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
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/system/form.css">
    <script src="<%=request.getContextPath()%>/resources/js/system/form.js"></script>
</head>
<body>
<form method="post" action="<%=request.getContextPath()%>/system/add/" class="form-horizontal">
    <div class="form-group">
        <label for="name" class="control-label col-sm-2">System:</label>

        <div class="col-sm-10">
            <input type="text" class="form-control" name="name" id="name" size="25" value="${system.systemName}"
                   placeholder="System name"/>
        </div>
    </div>
    <div class="form-group">
        <label class="control-label col-sm-2" for="rootSchema">root schema file:</label>

        <div class="col-sm-10">
            <input type="text" class="form-control" name="rootSchema" id="rootSchema" size="50"
                   placeholder="Schema filename from system root or schema URL"
                   value="${system.remoteRootSchema}"/>
        </div>
    </div>
    <input type="hidden"
           value="<c:if test="${system.protocol.equals('JMS') || !system.protocol.equals('SOAP')}">JMS</c:if>"
           name="protocol" id="protocol"/>

    <div id="protocol-container" class="centered-pills">
        <ul class="nav nav-pills">
            <li class="<c:if test="${system.protocol.equals('JMS') || !system.protocol.equals('SOAP')}">active</c:if>">
                <a href="#JMS" data-toggle="tab">JMS</a></li>
            <li class="<c:if test="${system.protocol.equals('SOAP')}">active</c:if>"><a href="#SOAP"
                                                                                        data-toggle="tab">SOAP</a>
            </li>
        </ul>
        <div class="tab-content">
            <div id="JMS"
                 class="tab-pane <c:if test="${system.protocol.equals('JMS') || !system.protocol.equals('SOAP')}">active</c:if>">

                <div class="form-group">
                    <label class="control-label col-sm-2" for="integrationPointSelector">integration Point
                        Selector</label>

                    <div class="col-sm-10" style="text-align: right">
                        <script>
                            function addElement(namespace, elementName) {
                                var span = document.createElement('span');
                                span.innerHTML = "<div><input name=\"integrationPointSelectorNamespace[]\" type=\"text\"  placeholder=\"Namespace\" value=\"" +
                                namespace +
                                "\" size=\"62\"/>" +
                                "<input name=\"integrationPointSelectorElementName[]\" type=\"text\" placeholder=\"Blank element name if it's last\" value=\"" +
                                elementName + "\" size=\"25\"><input type=\"button\" value=\"-\" style='width: 25px' onclick='removeElement(this)'/></div>";
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
                        <input type="button" value="+" style="width: 25px" onclick="addElement('','')"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2" for="queueConnectionFactory">Connection factory:</label>

                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="queueConnectionFactory"
                               id="queueConnectionFactory"
                               placeholder="JNDI Name"
                               value="${system.queueConnectionFactory}"/>
                    </div>
                </div>

                <div class="panel panel-default">
                    <div class="panel-heading" style="text-align: left">Mock</div>
                    <div class="panel-body">
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="mockIncomeQueue">Income queue:</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="mockIncomeQueue" id="mockIncomeQueue"
                                       placeholder="JNDI Name"
                                       value="${system.mockIncomeQueue}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="mockOutcomeQueue">Outcome queue</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="mockOutcomeQueue" id="mockOutcomeQueue"
                                       placeholder="JNDI Name"
                                       value="${system.mockOutcomeQueue}"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="panel panel-default">
                    <div class="panel-heading" style="text-align: left">Driver</div>
                    <div class="panel-body">
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="driverOutcomeQueue">Outcome queue</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="driverOutcomeQueue"
                                       id="driverOutcomeQueue"
                                       placeholder="JNDI Name"
                                       value="${system.driverOutcomeQueue}"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="driverIncomeQueue">Income queue</label>

                            <div class="col-sm-10">
                                <input type="text" class="form-control" name="driverIncomeQueue" id="driverIncomeQueue"
                                       placeholder="JNDI Name"
                                       value="${system.driverIncomeQueue}"/>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div id="SOAP" class="tab-pane <c:if test="${system.protocol.equals('SOAP')}">active</c:if>">
                <div class="form-group">
                    <label class="control-label col-sm-2" for="driverIncomeQueue">Driver WebService Endpoint:</label>

                    <div class="col-sm-10">
                        <input type="text" class="form-control" name="driverWebServiceEndpoint"
                               id="driverWebServiceEndpoint"
                               placeholder="URL of WebService to send messages"
                               value="${system.driverWebServiceEndpoint}"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div style="text-align: right">
        <input class="btn btn-success" type="submit" value="SAVE"/>
    </div>
</form>
</body>
</html>
