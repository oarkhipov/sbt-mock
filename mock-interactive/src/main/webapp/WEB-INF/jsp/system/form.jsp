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
<form method="post"
      action="<%=request.getContextPath()%>/api/system/<c:choose><c:when test="${system != null && system.systemName != ''}">update/${system.systemName}</c:when><c:otherwise>add</c:otherwise></c:choose>/"
      class="form-horizontal">
    <div class="form-group">
        <label for="enabled" class="control-label col-sm-2">Enabled:</label>

        <div class="col-sm-10">
            <input type="checkbox" name="enabled" id="enabled" class="form-control"
                   <c:if test="${system.enabled==null || system.enabled}">checked</c:if>/>
        </div>
    </div>
    <div class="form-group">
        <label for="validationEnabled" class="control-label col-sm-2">Validation enabled:</label>

        <div class="col-sm-10">
            <input type="checkbox" name="validationEnabled" id="validationEnabled" class="form-control"
                   <c:if test="${system.validationEnabled==null || system.validationEnabled}">checked</c:if>/>
        </div>
    </div>
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
           value="<c:choose><c:when test="${system != null && system.protocol != ''}">${system.protocol}</c:when><c:otherwise>JMS</c:otherwise></c:choose>"
           name="protocol" id="protocol"/>

    <div id="protocol-container" class="centered-pills">
        <ul class="nav nav-pills">
            <li class="<c:if test="${system.protocol == 'JMS' || system.protocol != 'SOAP'}">active</c:if>">
                <a href="#JMS" data-toggle="tab">JMS</a></li>
            <li class="<c:if test="${system.protocol == 'SOAP'}">active</c:if>"><a href="#SOAP"
                                                                                   data-toggle="tab">SOAP</a>
            </li>
        </ul>
        <div class="tab-content">
            <div id="JMS"
                 class="tab-pane <c:if test="${system.protocol == 'JMS' || system.protocol != 'SOAP'}">active</c:if>">

                <div class="form-group">
                    <label class="control-label col-sm-2" for="integrationPointSelector">integration Point
                        Selector</label>

                    <div class="col-sm-10" style="text-align: right">
                        <div id="integrationPointSelector">
                            <c:forEach var="selector" items="${system.integrationPointSelector.elementSelectors}">
                                <script>
                                    addElement("${selector.namespace}", "${selector.element}");
                                </script>
                            </c:forEach>
                        </div>
                        <input type="button" value="+" class="btn btn-default btn-s" onclick="addElement('','')"/>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label col-sm-2" for="queueConnectionFactory">Connection factory:</label>

                    <div class="col-sm-10 inner-addon right-addon">
                        <i class="glyphicon"></i>
                        <input type="text" class="form-control" name="queueConnectionFactory"
                               id="queueConnectionFactory"
                               placeholder="JNDI Name"
                               value="${system.queueConnectionFactory}"
                               onkeyup="checkJndiName(event, this)"
                               onfocusout="checkJndiName(event, this)"/>
                    </div>
                </div>

                <div class="panel panel-default">
                    <div class="panel-heading" style="text-align: left">Mock</div>
                    <div class="panel-body">
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="mockIncomeQueue">Income queue:</label>

                            <div class="col-sm-10 inner-addon right-addon">
                                <i class="glyphicon"></i>
                                <input type="text" class="form-control" name="mockIncomeQueue" id="mockIncomeQueue"
                                       placeholder="JNDI Name"
                                       value="${system.mockIncomeQueue}"
                                       onkeyup="checkJndiName(event, this)"
                                       onfocusout="checkJndiName(event, this)"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="mockOutcomeQueue">Outcome queue</label>

                            <div class="col-sm-10 inner-addon right-addon">
                                <i class="glyphicon"></i>
                                <input type="text" class="form-control" name="mockOutcomeQueue" id="mockOutcomeQueue"
                                       placeholder="JNDI Name"
                                       value="${system.mockOutcomeQueue}"
                                       onkeyup="checkJndiName(event, this)"
                                       onfocusout="checkJndiName(event, this)"/>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="panel panel-default">
                    <div class="panel-heading" style="text-align: left">Driver</div>
                    <div class="panel-body">
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="driverOutcomeQueue">Outcome queue</label>

                            <div class="col-sm-10 inner-addon right-addon">
                                <i class="glyphicon"></i>
                                <input type="text" class="form-control" name="driverOutcomeQueue"
                                       id="driverOutcomeQueue"
                                       placeholder="JNDI Name"
                                       value="${system.driverOutcomeQueue}"
                                       onkeyup="checkJndiName(event, this)"
                                       onfocusout="checkJndiName(event, this)"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="driverIncomeQueue">Income queue</label>

                            <div class="col-sm-10 inner-addon right-addon">
                                <i class="glyphicon"></i>
                                <input type="text" class="form-control" name="driverIncomeQueue" id="driverIncomeQueue"
                                       placeholder="JNDI Name"
                                       value="${system.driverIncomeQueue}"
                                       onkeyup="checkJndiName(event, this)"
                                       onfocusout="checkJndiName(event, this)"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading" style="text-align: left; cursor: pointer;" data-toggle="collapse" data-target="#rootElementBodyDiv">
                        <span class="glyphicon glyphicon-collapse-down"></span> <i>Advanced</i>
                    </div>
                    <div class="panel-body collapse" id="rootElementBodyDiv">
                        <div class="form-group">
                            <label class="control-label col-sm-2" for="rootElementDiv">Root element</label>

                            <div class="col-sm-10">
                                <div id="rootElementDiv">
                                    <input class='rootElementNamespace' name="rootElementNamespace" placeholder="Namespace" value="${system.rootElement.namespace}"/>
                                    <input class='rootElementName' name="rootElementName" placeholder="Element name" value="${system.rootElement.element}"/>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
            <div id="SOAP" class="tab-pane <c:if test="${system.protocol == 'SOAP'}">active</c:if>">
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
