<%--
  Created by IntelliJ IDEA.
  User: sbt-bochev-as
  Date: 13.12.2014
  Time: 16:55
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <style type="text/css">.CodeMirror {
        border: 1px solid #eee;
    }

    .CodeMirror-scroll {
        height: 100%
    }</style>
</head>
<body>
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
<b>Integration point:</b> <i><c:out value="${name}"/></i>
<c:if test="${link=='mock'}">
    <br/><b><span style="color: #009900">Linked Tag: </span></b><c:out value="${linkedTag}"/>
</c:if>


<form>
    <div id="codeWrapper" style="float: left">
        <div id="info">&nbsp;</div>
        <div id="error"></div>
        <textarea id="code" name="code"><c:out value="${object}" escapeXml="true"/></textarea>

        <div id="actionButtonsDiv">
            <c:if test="${link=='driver'}">
                <input id="templateInfo" type="button" title="<c:out value="${templateInfo}"/>" class="actionButtons"/>
                &nbsp;&nbsp;
                <style>
                    /*jQuery UI customization*/
                    .ui-tooltip {
                        /*override*/
                        font-size: 70%;
                    }
                </style>
            </c:if>
            <input id="reset" type="button" title="Load precompiled template" class="actionButtons"/>
            <input id="undo" type="button" title="Undo" class="actionButtons"/>
            <input id="redo" type="button" title="Redo" class="actionButtons"/>
            &nbsp;&nbsp;
            <input id="validate" type="button" title="Validate" class="actionButtons"/>
            <input id="save" type="button" title="Save" class="actionButtons"/>
            &nbsp;&nbsp;
            <input id="test" type="button" title="Test" class="actionButtons"/>
            <c:if test="${link=='driver'}">
                <select id="reqList" name="request" style="width: 120px">
                    <c:forEach var="entry" items="${list}">
                        <option value="${entry}">${entry}</option>
                    </c:forEach>
                </select>
                <input id="listRefresh" type="button" title="Refresh List" class="actionButtons"/>
                &nbsp;&nbsp;
                <input id="send" type="button" title="Send" class="actionButtons"/>
            </c:if>

        </div>
    </div>
    <c:if test="${link=='driver'}">
        <div id="resWrapper">
            Response
            <textarea id="resCode" name="resCode"></textarea>
        </div>
    </c:if>
</form>
<div id="htmlConverter" style="display: none"></div>

<script src="../js/editor.js"></script>
<c:if test="${link=='driver'}">
    <script src="../js/editor_driver.js"></script>
</c:if>
<script>
    $().ready(function () {
        $("#info").fadeTo(0, 0);
    });
    var editor = CodeMirror.fromTextArea(document.getElementById("code"), editorSettings);
    editor.setSize("700", "400");
    <c:if test="${link=='driver'}">
    var resEditor = CodeMirror.fromTextArea(document.getElementById("resCode"), editorSettings);
    resEditor.setSize("600","400");
    applyActionButtonsEvents();
  </c:if>

    $(function () {
      $("#templateInfo").tooltip({
        content: function () {
          return $(this).prop('title');
        }
      });
    });

</script>

</body>
</html>