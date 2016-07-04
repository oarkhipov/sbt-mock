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
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/main.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/normalize.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/jquery.steps.css">
    <script src="<%=request.getContextPath()%>/resources/js/modernizr-2.6.2.min.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/jquery-1.9.1.min.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/jquery.cookie-1.3.1.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/jquery.steps.min.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/jquery.history.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/attrs.js"></script>

    <%-- editor pre-load to fix editor display bug, when first time loaded--%>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/codemirror/codemirror.css">
    <script src="<%=request.getContextPath()%>/resources/js/codemirror/codemirror.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/codemirror/show-hint.js"></script>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/codemirror/show-hint.css">
    <script src="<%=request.getContextPath()%>/resources/js/codemirror/closetag.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/codemirror/xml-hint.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/codemirror/xml.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/codemirror/groovy.js"></script>
    <%--Folding--%>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/codemirror/foldgutter.css">
    <script src="<%=request.getContextPath()%>/resources/js/codemirror/brace-fold.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/codemirror/comment-fold.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/codemirror/foldcode.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/codemirror/foldgutter.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/codemirror/markdown-fold.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/codemirror/xml-fold.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/codemirror/formatting.js"></script>

    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/editor.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/docs.css">

    <%--Tooltip--%>
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/jquery-ui.css">
    <script src="<%=request.getContextPath()%>/resources/js/jquery-ui.js"></script>

    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/libs/bootstrap/css/bootstrap.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/libs/bootstrap/css/bootstrap-theme.css">
    <script src="<%=request.getContextPath()%>/resources/libs/bootstrap/js/bootstrap.js"></script>
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
                <div id="dialog"></div>
                <div align="right">
                    <button class="btn btn-success btn-sm" onclick="systemForm()">
                        <span class="glyphicon glyphicon-plus"></span> Add System
                    </button>
                    <span style="line-height: 5pt; display: block">&nbsp;</span>

                    <div class="panel panel-default" style="margin-bottom: 5pt">
                        <div class="panel-body">
                            <table class="table-striped" width="100%">
                                <tr>
                                    <th width="10%">System name</th>
                                    <th width="50"></th>
                                    <th width="*">Integration point name</th>
                                    <th width="10%" style="text-align: right">Actions</th>
                                </tr>
                                <c:forEach var="system" items="${list.systems}">
                                    <tr>
                                        <td <%--rowspan="${system.mockIntegrationPoints.size() + system.driverIntegrationPoints.size() + 1}"--%>
                                            style="vertical-align: top; text-align: center; padding-top: 10px">
                                            <c:out value="${system.systemName}"/></td>
                                        <td colspan="2">&nbsp;</td>
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
                                                    <li><a href="#" onclick="addIpForm()">Add integration point</a></li>
                                                    <li><a href="#">Upload schema</a></li>
                                                    <li><a href="#">Update Validator</a></li>
                                                    <li role="separator" class="divider"></li>
                                                    <li><a href="#">Edit System</a></li>
                                                    <li><a href="#">Delete System</a></li>
                                                </ul>
                                            </div>
                                        </td>
                                    </tr>
                                    <c:forEach var="mockIntegrationPoint" items="${system.mockIntegrationPoints}">
                                        <tr>
                                            <td align="right">
                                                <span class="glyphicon glyphicon-menu-right" style="opacity: 0.6"></span>
                                            </td>
                                            <td align="center">
                                                <span class="label label-success">Mock</span>
                                            </td>
                                            <td>
                                                <a href="#" onclick="chooseIntPoint('${system.systemName}__mock__${mockIntegrationPoint.name}'); return false;">
                                                <c:out value="${mockIntegrationPoint.name}"/></a>
                                            </td>
                                            <td align="right" style="padding-right: 30px">
                                                <button class="btn btn-default btn-xs btn-warning">
                                                    <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                                </button>
                                                <button class="btn btn-xs btn-danger">
                                                    <span class="glyphicon glyphicon-trash"></span>
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                    <c:forEach var="driverIntegrationPoint" items="${system.driverIntegrationPoints}">
                                        <tr>
                                            <td align="right">
                                                <span class="glyphicon glyphicon-menu-right" style="opacity: 0.6"></span>
                                            </td>
                                            <td align="center">
                                                <span class="label label-primary">Driver</span>
                                            </td>
                                            <td>
                                                <a href="#" onclick="chooseIntPoint('${system.systemName}__driver__${driverIntegrationPoint.name}'); return false;">
                                                ${driverIntegrationPoint.name}</a>
                                            </td>
                                            <td align="right" style="padding-right: 30px">
                                                <button class="btn btn-default btn-xs btn-warning">
                                                    <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
                                                </button>
                                                <button class="btn btn-xs btn-danger">
                                                    <span class="glyphicon glyphicon-trash"></span>
                                                </button>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:forEach>
                            </table>
                        </div>
                    </div>
                    <button class="btn btn-default btn-sm" onclick="window.location.href='<%=request.getContextPath()%>/config/'">
                        <span class="glyphicon glyphicon-list"></span> Show config
                    </button>
                    <button class="btn btn-default btn-sm" onclick="window.location.href='<%=request.getContextPath()%>/config/export/'">
                        <span class="glyphicon glyphicon-cloud-download"></span> Export
                    </button>
                    <button class="btn btn-default btn-sm" onclick="importForm()">
                        <span class="glyphicon glyphicon-cloud-upload"></span> Import
                    </button>
                    &nbsp;&nbsp;
                    <button class="btn btn-info btn-sm" onclick="window.location.href='log/'">
                        <span class="glyphicon glyphicon-list-alt"></span> Show Logs
                    </button>
                </div>
            </section>
        </div>
    </div>
</div>
</body>
</html>
