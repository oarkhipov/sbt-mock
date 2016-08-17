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
  Date: 22.05.2015
  Time: 11:55
  To change this template use File | Settings | File Templates.
--%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]> <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]> <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
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
    <script src="<%=request.getContextPath()%>/resources/js/modernizr-2.6.2.min.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/jquery-1.9.1.min.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/jquery.cookie-1.3.1.js"></script>

    <!-- Tooltip& -->
    <link rel="stylesheet" href="<%=request.getContextPath()%>/resources/css/jquery-ui.css">
    <script src="<%=request.getContextPath()%>/resources/js/jquery-ui.js"></script>

    <!-- Data Tables -->
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/DataTables-1.10.9/css/dataTables.jqueryui.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/css/DataTables-1.10.9/css/jquery.dataTables.css"/>

    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/libs/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/resources/libs/bootstrap-dialog/css/bootstrap-dialog.css"/>

    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/DataTables-1.10.9/js/jquery.dataTables.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/DataTables-1.10.9/js/jquery.dataTables.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/DataTables-1.10.9/js/dataTables.jqueryui.js"></script>

    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/jquery.hoverIntent.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/libs/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/resources/libs/bootstrap-dialog/js/bootstrap-dialog.js"></script>

</head>
<body>
<!--[if lt IE 7]>
<p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade
    your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to
    improve your experience.</p>
<![endif]-->

<input type="button" value="BACK" onclick="window.location.href='../'"/>

<div id="tooltip" data-toggle="tooltip" data-placement="right" data-animation="false" data-trigger="manual"
     data-html="true" title="Tooltip text" style="position:absolute; width: 100px"></div>

<script type="text/javascript" src="<%=request.getContextPath()%>/resources/libs/ace/ace.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/logDataTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resources/js/vkbeautify.0.99.00.beta.js"></script>
<table style="border: 3px;background: rgb(243, 244, 248); width: 800px">
    <tr>
        <td>
            <table id="example" class="display" cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th class="sorting">TimeStamp</th>
                    <th class="sorting">TransactionId</th>
                    <th class="sorting">Protocol</th>
                    <th class="sorting">SystemName</th>
                    <th class="sorting">IntegrationPointName</th>
                    <th class="sorting">FullEndpoint</th>
                    <th class="sorting">ShortEndpoint</th>
                    <th class="sorting">MessageState</th>
                </tr>
                </thead>
                <tfoot>
                <tr>
                    <th>TimeStamp</th>
                    <th>TransactionId</th>
                    <th>Protocol</th>
                    <th>SystemName</th>
                    <th>IntegrationPointName</th>
                    <th>FullEndpoint</th>
                    <th>ShortEndpoint</th>
                    <th>MessageState</th>
                </tr>
                </tfoot>

            </table>
        </td>
    </tr>
</table>

</body>
</html>
