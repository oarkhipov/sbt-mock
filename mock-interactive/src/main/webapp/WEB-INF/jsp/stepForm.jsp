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
  Date: 13.12.2014
  Time: 14:26
--%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]> <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]> <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js"> <!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>Mock Service</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width">
    <link rel="shortcut icon" href="<%=request.getContextPath()%>/resources/favicon.ico">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/main.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/normalize.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/jquery.steps.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/animate.css">
    <script src="<%=request.getContextPath()%>/resources/js/modernizr-2.6.2.min.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/jquery-1.9.1.min.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/jquery.steps.min.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/jquery.history.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/attrs.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/base64.js"></script>

    <script src="<%=request.getContextPath()%>/resources/libs/ace/ace.js"></script>

    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/editor.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/docs.css">

    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/jquery-ui.css">
    <script src="<%=request.getContextPath()%>/resources/js/jquery-ui.js"></script>

    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/libs/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/libs/bootstrap/css/bootstrap-theme.css">
    <script src="<%=request.getContextPath()%>/resources/libs/bootstrap/js/bootstrap.js"></script>

    <script type="text/javascript"
            src="<%=request.getContextPath()%>/resources/libs/bootstrap-dialog/js/bootstrap-dialog.js"></script>
    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/resources/libs/bootstrap-dialog/css/bootstrap-dialog.css"/>
    <script src="<%=request.getContextPath()%>/resources/libs/selectize.js/js/standalone/selectize.min.js"></script>
    <link rel="stylesheet"
          href="<%=request.getContextPath()%>/resources/libs/selectize.js/css/selectize.bootstrap3.css">

    <script type="text/javascript"
            src="<%=request.getContextPath()%>/resources/libs/bootstrap-notify/bootstrap-notify.min.js"></script>

    <link rel="stylesheet" type="text/css"
          href="<%=request.getContextPath()%>/resources/libs/bootstrap-fileinput/css/fileinput.min.css">
    <script type="text/javascript"
            src="<%=request.getContextPath()%>/resources/libs/bootstrap-fileinput/js/fileinput.min.js"></script>

    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/vkbeautify.0.99.00.beta.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.cookie.js"></script>
</head>
<body>
<!--[if lt IE 7]>
<p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade
    your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to
    improve your experience.</p>
<![endif]-->

<div class="content">
    <script src="<%=request.getContextPath()%>/resources/js/stepForm.js"></script>
    <div id="mock">
        <div id="wizard">
            <h2>Integration point</h2>
            <section>
                <div>
                    <table width="100%">
                        <tr>
                            <td>
                                <button class="btn btn-default btn-sm" onclick="expandAllIp()">
                                    <span class="glyphicon glyphicon-expand"></span> Expand
                                </button>
                                <button class="btn btn-default btn-sm" onclick="collapseAllIp()">
                                    <span class="glyphicon glyphicon-collapse-up"></span> Collapse
                                </button>
                            </td>
                            <td align="right">
                                <button class="btn btn-success btn-sm" onclick="addSysForm()">
                                    <span class="glyphicon glyphicon-plus"></span> Add System
                                </button>
                                <button class="btn btn-default btn-sm" onclick="showSettings()">
                                    <span class="glyphicon glyphicon-cog"></span>
                                </button>
                            </td>
                        </tr>
                    </table>
                    <span style="line-height: 5pt; display: block">&nbsp;</span>

                    <div id="mockPanel" class="panel panel-default" style="margin-bottom: 5pt">
                        <div class="panel-body">
                            <table class="table-striped table-hover" width="100%">
                                <thead>
                                <tr>
                                    <th width="10%">System name</th>
                                    <th width="50"></th>
                                    <th width="*">Integration point name</th>
                                    <th width="5%">Validation</th>
                                    <th width="15%" style="text-align: right">Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="system" items="${list.systems}">
                                    <tr id="${system.systemName}">
                                        <td style="vertical-align: top; padding-top: 10px"
                                            class="<c:if test="${system.enabled == false}">disabledByConfig</c:if>">
                                            <span class="label label-<c:if test="${system.protocol eq 'JMS'}">warning</c:if><c:if test="${system.protocol eq 'SOAP'}">info</c:if> ">${system.protocol}</span>
                                                ${system.systemName}
                                        </td>
                                        <td colspan="2">&nbsp;</td>
                                        <td align="center">
                                            <c:choose>
                                                <c:when test="${system.validationEnabled == false}">
                                                    <span class="glyphicon glyphicon-ban-circle btn-danger btn btn-xs"
                                                          title="Validation disabled"></span>
                                                </c:when>
                                                <c:when test="${globalValidation == false}">
                                                    <span class="glyphicon glyphicon-ban-circle icon-grey-link btn btn-danger btn-xs"
                                                          title="Validation disabled (inherited)"></span>
                                                </c:when>
                                                <%--<c:otherwise>--%>
                                                <%--<span class="glyphicon glyphicon-ok-circle btn-success btn-xs icon-grey-link"--%>
                                                <%--title="Validation Enabled"></span>--%>
                                                <%--</c:otherwise>--%>
                                            </c:choose>
                                        </td>
                                        <td align="right">
                                            <div class="dropdown" style="margin-top: 5px; margin-bottom: 5px">
                                                <button id="dropdownMenu_${system.systemName}"
                                                        class="btn btn-xs btn-default"
                                                        type="button"
                                                        data-toggle="dropdown"
                                                        aria-haspopup="true" aria-expanded="false">
                                                <span class="glyphicon glyphicon-menu-hamburger"
                                                      aria-hidden="true"></span>
                                                </button>
                                                <ul class="dropdown-menu dropdown-menu-right"
                                                    aria-labelledby="dropdownMenu_${system.systemName}">
                                                    <li><a href="#" onclick="addIpForm('${system.systemName}')">Add
                                                        integration point</a></li>
                                                    <li><a href="#" onclick="uploadSchema('${system.systemName}')">Upload
                                                        schema</a></li>
                                                    <li><a href="#" onclick="reinitValidator('${system.systemName}')">Update
                                                        Validator</a></li>
                                                    <c:if test="${system.protocol=='SOAP'}">
                                                        <li role="separator" class="divider"></li>
                                                        <li>
                                                            <a href="<%=request.getContextPath()%>/ws/${system.systemName}?wsdl"
                                                               target="_blank">Show WSDL</a></li>
                                                    </c:if>
                                                    <li role="separator" class="divider"></li>
                                                    <li><a href="#" onclick="editSysForm('${system.systemName}')">Edit
                                                        System</a></li>
                                                    <li><a href="#" onclick="delSystemForm('${system.systemName}')">Delete
                                                        System</a></li>
                                                </ul>
                                            </div>
                                        </td>
                                    </tr>

                                    <%-- MOCKS --%>

                                    <c:forEach var="mockIntegrationPoint" items="${system.mockIntegrationPoints}">
                                        <tr id="${system.systemName}__${mockIntegrationPoint.name}"
                                            class="<c:if test="${system.enabled == false || mockIntegrationPoint.enabled == false}">disabledByConfig</c:if>">
                                            <td align="right">
                                                <span class="glyphicon glyphicon-menu-right"
                                                      style="opacity: 0.6"></span>
                                            </td>
                                            <td align="center">
                                                <span class="label label-success">Mock</span>
                                            </td>
                                            <td class="text-primary collapseController" data-toggle="collapse"
                                                data-target=".${mockIntegrationPoint.name}_templates"
                                                style="cursor: pointer;">${mockIntegrationPoint.name}</td>
                                            <td align="center">
                                                <c:choose>
                                                    <c:when test="${mockIntegrationPoint.validationEnabled == false}">
                                                        <span class="glyphicon glyphicon-ban-circle btn-danger btn btn-xs"
                                                              title="Validation disabled"></span>
                                                    </c:when>
                                                    <c:when test="${system.validationEnabled == false}">
                                                        <span class="glyphicon glyphicon-ban-circle icon-grey-link btn btn-danger btn-xs"
                                                              title="Validation disabled (inherited)"></span>
                                                    </c:when>
                                                    <c:when test="${globalValidation == false}">
                                                        <span class="glyphicon glyphicon-ban-circle icon-grey-link btn btn-danger btn-xs"
                                                              title="Validation disabled (inherited)"></span>
                                                    </c:when>
                                                    <%--<c:otherwise>--%>
                                                    <%--<span class="glyphicon glyphicon-ok-circle btn-success btn-xs icon-grey-link"--%>
                                                    <%--title="Validation Enabled"></span>--%>
                                                    <%--</c:otherwise>--%>
                                                </c:choose>
                                            </td>
                                            <td class="subsystemTd">
                                                <div class="editActions">
                                                    <button class="btn btn-xs btn-success"
                                                            onclick="addMessageTemplate('${system.systemName}','${mockIntegrationPoint.name}')">
                                                        <span class=" glyphicon glyphicon-plus"></span> Message
                                                    </button>
                                                    <button class="btn btn-xs btn-warning"
                                                            onclick="editIpForm('${system.systemName}','${mockIntegrationPoint.name}')">
                                                        <span class="glyphicon glyphicon-pencil"
                                                              aria-hidden="true"></span>
                                                    </button>
                                                    <button class="btn btn-xs btn-danger"
                                                            onclick="delIpForm('${system.systemName}','${mockIntegrationPoint.name}')">
                                                        <span class="glyphicon glyphicon-trash"></span>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>

                                        <%-- MESSAGE TEMPLATES--%>

                                        <c:forEach var="messageTemplate"
                                                   items="${mockIntegrationPoint.messageTemplates.messageTemplateList}">
                                            <tr data-system="${system.systemName}"
                                                data-ip="${mockIntegrationPoint.name}"
                                                class="collapse ${mockIntegrationPoint.name}_templates">
                                                <td colspan="2">&nbsp;</td>
                                                <td>
                                                    <a href="#"
                                                       onclick="chooseIntPoint('${system.systemName}__mock__${mockIntegrationPoint.name}__${messageTemplate.templateId}'); return false;">
                                                        <span class="glyphicon glyphicon-link"></span>
                                                            ${messageTemplate.caption}<c:if
                                                                test="${messageTemplate.value.length() > 0}">
                                                        (${messageTemplate.value})</c:if></a>
                                                </td>
                                                <td>&nbsp;</td>
                                                <td class="subsystemTd">
                                                    <div class="editActions">
                                                            <%--<span class="btn btn-xs btn-default glyphicon glyphicon-arrow-up" onclick="moveMessageUp(${messageTemplate.templateId})"></span>--%>
                                                            <%--<span class="btn btn-xs btn-default glyphicon glyphicon-arrow-down"></span>--%>
                                                        <span class="btn btn-xs btn-warning glyphicon glyphicon-pencil"
                                                              onclick="editMessageTemplate('${system.systemName}', '${mockIntegrationPoint.name}', '${messageTemplate.templateId}', '${messageTemplate.caption}')"></span>
                                                        <span class="btn btn-xs btn-danger glyphicon glyphicon-trash"
                                                              onclick="deleteMessageTemplate('${system.systemName}', '${mockIntegrationPoint.name}', '${messageTemplate.templateId}', '${messageTemplate.caption}')"></span>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <tr class="collapse ${mockIntegrationPoint.name}_templates"
                                            data-system="${system.systemName}"
                                            data-ip="${mockIntegrationPoint.name}">
                                            <td colspan="2">&nbsp;</td>
                                            <td>
                                                <a href="#"
                                                   onclick="chooseIntPoint('${system.systemName}__mock__${mockIntegrationPoint.name}'); return false;">
                                                    <span class="glyphicon glyphicon-flash"></span>
                                                    <i>Default message</i></a>
                                            </td>
                                            <td>&nbsp;</td>
                                        </tr>
                                    </c:forEach>

                                    <%-- DRIVERS --%>

                                    <c:forEach var="driverIntegrationPoint" items="${system.driverIntegrationPoints}">
                                        <tr id="${system.systemName}__${driverIntegrationPoint.name}"
                                            class="<c:if test="${system.enabled == false || driverIntegrationPoint.enabled == false}">disabledByConfig</c:if>">
                                            <td align="right">
                                                <span class="glyphicon glyphicon-menu-right"
                                                      style="opacity: 0.6"></span>
                                            </td>
                                            <td align="center">
                                                <span class="label label-primary">Driver</span>
                                            </td>
                                            <td class="text-primary collapseController" data-toggle="collapse"
                                                data-target=".${driverIntegrationPoint.name}_templates"
                                                style="cursor: pointer;">${driverIntegrationPoint.name}</td>
                                            <td align="center">
                                                <c:choose>
                                                    <c:when test="${driverIntegrationPoint.validationEnabled == false}">
                                                        <span class="glyphicon glyphicon-ban-circle btn-danger btn btn-xs"
                                                              title="Validation disabled"></span>
                                                    </c:when>
                                                    <c:when test="${system.validationEnabled == false}">
                                                        <span class="glyphicon glyphicon-ban-circle icon-grey-link btn btn-danger btn-xs"
                                                              title="Validation disabled (inherited)"></span>
                                                    </c:when>
                                                    <c:when test="${globalValidation == false}">
                                                        <span class="glyphicon glyphicon-ban-circle icon-grey-link btn btn-danger btn-xs"
                                                              title="Validation disabled (inherited)"></span>
                                                    </c:when>
                                                    <%--<c:otherwise>--%>
                                                    <%--<span class="glyphicon glyphicon-ok-circle btn-success btn-xs icon-grey-link"--%>
                                                    <%--title="Validation Enabled"></span>--%>
                                                    <%--</c:otherwise>--%>
                                                </c:choose>
                                            </td>
                                            <td class="subsystemTd">
                                                <div class="editActions">
                                                    <button class="btn btn-xs btn-success"
                                                            onclick="addMessageTemplate('${system.systemName}','${driverIntegrationPoint.name}')">
                                                        <span class=" glyphicon glyphicon-plus"></span> Message
                                                    </button>
                                                    <button class="btn btn-default btn-xs btn-warning"
                                                            onclick="editIpForm('${system.systemName}','${driverIntegrationPoint.name}')">
                                                        <span class="glyphicon glyphicon-pencil"
                                                              aria-hidden="true"></span>
                                                    </button>
                                                    <button class="btn btn-xs btn-danger"
                                                            onclick="delIpForm('${system.systemName}','${driverIntegrationPoint.name}')">
                                                        <span class="glyphicon glyphicon-trash"></span>
                                                    </button>
                                                </div>
                                            </td>
                                        </tr>

                                        <%-- MESSAGE TEMPLATES--%>

                                        <c:forEach var="messageTemplate"
                                                   items="${driverIntegrationPoint.messageTemplates.messageTemplateList}">
                                            <tr data-system="${system.systemName}"
                                                data-ip="${driverIntegrationPoint.name}"
                                                class="collapse ${driverIntegrationPoint.name}_templates">
                                                <td colspan="2">&nbsp;</td>
                                                <td>
                                                    <a href="#"
                                                       onclick="chooseIntPoint('${system.systemName}__driver__${driverIntegrationPoint.name}__${messageTemplate.templateId}'); return false;">
                                                        <span class="glyphicon glyphicon-link"></span>
                                                            ${messageTemplate.caption}<c:if
                                                                test="${messageTemplate.value.length() > 0}">
                                                        (${messageTemplate.value})</c:if></a>
                                                </td>
                                                <td>&nbsp;</td>
                                                <td class="subsystemTd">
                                                    <div class="editActions">
                                                            <%--<span class="btn btn-xs btn-default glyphicon glyphicon-arrow-up" onclick="moveMessageUp(${messageTemplate.templateId})"></span>--%>
                                                            <%--<span class="btn btn-xs btn-default glyphicon glyphicon-arrow-down"></span>--%>
                                                        <span class="btn btn-xs btn-warning glyphicon glyphicon-pencil"
                                                              onclick="editMessageTemplate('${system.systemName}', '${driverIntegrationPoint.name}', '${messageTemplate.templateId}', '${messageTemplate.caption}')"></span>
                                                        <span class="btn btn-xs btn-danger glyphicon glyphicon-trash"
                                                              onclick="deleteMessageTemplate('${system.systemName}', '${driverIntegrationPoint.name}', '${messageTemplate.templateId}', '${messageTemplate.caption}')"></span>
                                                    </div>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <tr class="collapse ${driverIntegrationPoint.name}_templates"
                                            data-system="${system.systemName}"
                                            data-ip="${driverIntegrationPoint.name}">
                                            <td colspan="2">&nbsp;</td>
                                            <td>
                                                <a href="#"
                                                   onclick="chooseIntPoint('${system.systemName}__driver__${driverIntegrationPoint.name}'); return false;">
                                                    <span class="glyphicon glyphicon-flash"></span>
                                                    <i>Default message</i></a>
                                            </td>
                                            <td>&nbsp;</td>
                                        </tr>
                                    </c:forEach>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div style="width:100%; text-align: right">
                        <button class="btn btn-default btn-sm"
                                onclick="window.location.href='<%=request.getContextPath()%>/config/'">
                            <span class="glyphicon glyphicon-list"></span> Show config
                        </button>
                        <button class="btn btn-default btn-sm"
                                onclick="window.location.href='<%=request.getContextPath()%>/config/export/'">
                            <span class="glyphicon glyphicon-cloud-download"></span> Export
                        </button>
                        <button class="btn btn-default btn-sm" onclick="importForm()">
                            <span class="glyphicon glyphicon-cloud-upload"></span> Import
                        </button>
                        &nbsp;&nbsp;
                        <button class="btn btn-default btn-sm" onclick="window.location.href='FrameMock/'">
                            <span class="glyphicon glyphicon-modal-window"></span> FrameMock
                        </button>
                        <button class="btn btn-info btn-sm" onclick="window.location.href='log/'">
                            <span class="glyphicon glyphicon-list-alt"></span> Show Logs
                        </button>
                    </div>
                </div>
            </section>
        </div>
    </div>
</div>

<div id="mainMenu">
    <div class="dropup" style="margin-top: 5px; margin-bottom: 5px; position: static">
        <button id="dropdownMenu_main"
                class="btn btn-xs btn-default"
                type="button"
                data-toggle="dropdown"
                aria-haspopup="true" aria-expanded="true">
            <span class="glyphicon glyphicon-menu-hamburger" aria-hidden="true"></span>
        </button>
        <ul class="dropdown-menu dropdown-menu-right"
            aria-labelledby="dropdownMenu_main">
            <li id="editingEnabler"><a href="#"><span>Enable</span> editing</a></li>
            <li><a href="#" onclick="reinitValidator()">Update all validators</a></li>
            <li><a href="#">Validate all IPs</a></li>
            <li class="divider"></li>
            <li><a tabindex="-1" href="<%=request.getContextPath()%>/info" target="_blank">Show Spring
                context</a></li>
            <li><a tabindex="-1" href="#" onclick="refreshContext()">Refresh Spring context</a></li>
            <li class="divider"></li>
            <li><a href="#" onclick="showSettings()"><span class="glyphicon glyphicon-cog"></span> Settings</a></li>
            <li class="divider"></li>
            <li><a href="#"><span class="glyphicon glyphicon-copyright-mark"></span> Copyright</a></li>
            <li class="disabled"><a href="#">${version}</a></li>
        </ul>
    </div>
</div>
</body>
</html>
