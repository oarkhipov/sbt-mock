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
    <link rel="stylesheet" href="../css/main.css">
    <link rel="stylesheet" href="../css/normalize.css">
    <link rel="stylesheet" href="../css/jquery.steps.css">
    <script src="../js/modernizr-2.6.2.min.js"></script>
    <script src="../js/jquery-1.9.1.min.js"></script>
    <script src="../js/jquery.cookie-1.3.1.js"></script>
    <script src="../js/jquery.steps.min.js"></script>
    <script src="../js/jquery.history.js"></script>
    <script src="../js/attrs.js"></script>

    <%-- editor pre-load to fix editor display bug, when first time loaded--%>
    <link rel="stylesheet" href="../css/codemirror/codemirror.css">
    <script src="../js/codemirror/codemirror.js"></script>
    <script src="../js/codemirror/show-hint.js"></script>
    <link rel="stylesheet" href="../css/codemirror/show-hint.css">
    <script src="../js/codemirror/closetag.js"></script>
    <script src="../js/codemirror/xml-hint.js"></script>
    <script src="../js/codemirror/xml.js"></script>
    <script src="../js/codemirror/groovy.js"></script>
    <%--Folding--%>
    <link rel="stylesheet" href="../css/codemirror/foldgutter.css">
    <script src="../js/codemirror/brace-fold.js"></script>
    <script src="../js/codemirror/comment-fold.js"></script>
    <script src="../js/codemirror/foldcode.js"></script>
    <script src="../js/codemirror/foldgutter.js"></script>
    <script src="../js/codemirror/markdown-fold.js"></script>
    <script src="../js/codemirror/xml-fold.js"></script>
    <script src="../js/codemirror/formatting.js"></script>

    <link rel="stylesheet" href="../css/editor.css">
    <link rel="stylesheet" href="../css/docs.css">

    <%--Tooltip--%>
    <link rel="stylesheet" href="../css/jquery-ui.css">
    <script src="../js/jquery-ui.js"></script>
</head>
<body>
<!--[if lt IE 7]>
<p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade
    your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to
    improve your experience.</p>
<![endif]-->

<div class="content">
    <script>
        var wizard;
        $(function () {
            wizard = $("#wizard").steps({
                headerTag: "h2",
                bodyTag: "section",
                transitionEffect: "slideLeft",
                onStepChanging: function (event, currentIndex, newIndex) {
                    if (currentIndex == 0) {
                        if (newIndex == 1) {
                            if (int_point === null) {
                                alert("Choose integration point!");
                                return false;
                            }
                            History.pushState({}, "Integration points", "?ip=" + int_point);
                        }
                    }
                    if (currentIndex == 1) {
                        if (newIndex == 0) {
                            History.pushState({}, "Integration points", "?");
                        }
                    }
                    return true;
                },
                enablePagination: false
            });

            var QueryString = getQueryString();
            //if integration point was chosen
            if (QueryString["ip"] != undefined) {
                int_point = QueryString["ip"];
                selectIp();
            }
        });

        var int_point = null;

        function chooseIntPoint(obj) {
            int_point = obj.value;
            if (int_point) {
                selectIp();
            }
        }

        function selectIp() {
            wizard.steps("remove", 1);
            var parts = int_point.split("__");
            if(parts.length!=3) {
                alert("Произошла ошибка! Имя систсемы или точки интеграции содержит служебные символы. Обратитесь к разработчику");
                return false;
            }
            wizard.steps("insert", 1, {
                title: parts[1].toUpperCase() + " Data",
                contentMode: "async",
                contentUrl: parts[1] + "/" + parts[0] + "/" + parts[2] + "/"
            });
            History.pushState({ip: int_point}, int_point, "?ip=" + int_point);
            wizard.steps("next");
        }
    </script>

    <div id="mock">
        <div id="wizard">
            <h2>Integration point</h2>
            <section>
                <input type="button" value="BACK" onclick="window.location.href='../'"/>
                <span style="line-height: 5pt; display: block">&nbsp;</span>
                <select size="25" onclick="chooseIntPoint(this)">
                    <%--Systems--%>
                    <c:forEach var="system" items="${list.systems}">
                        <optgroup label="${system.systemName}">
                                <%--Mocks--%>
                            <c:if test="${system.mockIntegrationPoints!=null}">
                                <optgroup label="└ mocks">
                                    <c:forEach var="mockIntegrationPoint" items="${system.mockIntegrationPoints}">
                                        <option value="${system.systemName}__mock__${mockIntegrationPoint.name}">└ ${mockIntegrationPoint.name}</option>
                                    </c:forEach>
                                </optgroup>
                            </c:if>
                                <%--Drivers--%>
                            <c:if test="${system.driverIntegrationPoints!=null}">
                                <optgroup label="└ drivers">
                                    <c:forEach var="driverIntegrationPoint" items="${system.driverIntegrationPoints}">
                                        <option value="${system.systemName}__driver__${driverIntegrationPoint.name}">└ ${driverIntegrationPoint.name}</option>
                                    </c:forEach>
                                </optgroup>
                            </c:if>
                        </optgroup>
                    </c:forEach>
                </select>
            </section>

            <%--<h2>Mock Data</h2>--%>
            <%--<section class="dyn_url" data-mode="async" data-url="/" style="overflow: scroll">--%>
            <%--</section>--%>
        </div>
    </div>
</div>
</body>
</html>
