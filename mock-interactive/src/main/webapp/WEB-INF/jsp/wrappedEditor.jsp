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

    <style type="text/css">.CodeMirror {
        border: 1px solid #eee;
    }

    .CodeMirror-scroll {
        height: 100%
    }</style>
</head>
<body>
<!--[if lt IE 7]>
<p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade
    your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to
    improve your experience.</p>
<![endif]-->

<!--[if IE]>
<style type="text/css">
    #info {
        background: transparent;
        zoom: 1;
        /*rgba(121, 255, 120, .7)*/
        /* IE8 */
        filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#B379FF78, endColorstr=#B379FF78);
        /*-ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=0)";*/
        /* IE 5-7 */
        /*filter: alpha(opacity=0)*/
    }

    #error {
        background: transparent;
        zoom: 1;
        /*rgba(256, 182, 193, .7)*/
        filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#B3FFB6C1, endColorstr=#B3FFB6C1);
    }
</style>
<![endif]-->

<div class="content">
    <script>
        var wizard;
        $(function () {
            wizard = $("#wizard").steps({
                headerTag: "h2",
                bodyTag: "section",
                enablePagination: false
            });
        });

    </script>
    <div id="mock">
        <div id="wizard">
            <h2><c:out value="${title}" escapeXml="true"/></h2>
            <section>
                <%--<input type="button" value="BACK" onclick="window.location.href=''"/>--%>
                <span style="line-height: 5pt; display: block">&nbsp;</span>

                <form>
                    <div id="codeWrapper" style="float: left">
                        <div id="info">&nbsp;</div>
                        <div id="error"></div>
                        <textarea id="code" name="code"><c:out value="${object}" escapeXml="true"/></textarea>

                        <div id="actionButtonsDiv">
                            <input id="templateInfo" type="button" title="<c:out value="${templateInfo}"/>"
                                   class="actionButtons"/>
                            &nbsp;&nbsp;
                            <style>
                                /*jQuery UI customization*/
                                .ui-tooltip {
                                    /*override*/
                                    font-size: 70%;
                                }
                            </style>
                            <input id="send" type="button" title="Send" class="actionButtons"/>

                        </div>
                    </div>
                    <div id="resWrapper">
                        Response
                        <textarea id="resCode" name="resCode"></textarea>
                    </div>
                </form>

                <div id="htmlConverter" style="display: none"></div>

            </section>
        </div>
    </div>

    <script>
        var editor;
        var resEditor;
        $().ready(function () {
            $("#info").fadeTo(0, 0);
            QueryString["ip"] = ".";

            editor = CodeMirror.fromTextArea(document.getElementById("code"), xmlEditorSettings);
            editor.setSize(700, 400);
            resEditor = CodeMirror.fromTextArea(document.getElementById("resCode"), xmlEditorSettings);
            resEditor.setSize(600, 400);

            $(function () {
                $("#templateInfo").tooltip({
                    content: function () {
                        return $(this).prop('title');
                    }
                });
            });
        });
    </script>
    <script src="<%=request.getContextPath()%>/resources/js/editor.js"></script>
    <script src="<%=request.getContextPath()%>/resources/js/editor_sender.js"></script>

</div>
</body>
</html>
