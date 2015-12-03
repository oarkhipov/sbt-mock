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
<div style="font-size: 80%; color: dimgray">
    <b>Integration point:</b> <i><c:out value="${name}"/></i><br/>
    <b>Xpath assertion:</b> <i><c:out value="${xpath}"/></i>
</div>

<form>
    <div id="codeWrapper" style="float: left">
        <span>Message</span>

        <div id="info">&nbsp;</div>
        <div id="error"></div>
        <textarea id="code" name="code"><c:out value="${message}" escapeXml="true"/></textarea>

        <div id="tabs">
            <ul>
                <li><a href="#tabs-1">Script</a></li>
                <li><a href="#tabs-2">Test</a></li>
            </ul>
            <div id="tabs-1">
                <textarea id="scriptCode" name="scriptCode"><c:out value="${script}" escapeXml="true"/></textarea>
            </div>
            <div id="tabs-2">
                <textarea id="testCode" name="testCode"><c:out value="${test}" escapeXml="true"/></textarea>
            </div>
        </div>

        <div id="actionButtonsDiv">
            <input id="reset" type="button" title="Regenerate" class="actionButtons"/>
            <%-- //TODO implement action buttons --%>
            <%--<input id="undo" type="button" title="Undo" class="actionButtons"/>--%>
            <%--<input id="redo" type="button" title="Redo" class="actionButtons"/>--%>
            &nbsp;&nbsp;
            <input id="validate" type="button" title="Validate" class="actionButtons"/>
            <input id="save" type="button" title="Save" class="actionButtons"/>
            &nbsp;&nbsp;
            <input id="test" type="button" title="Test" class="actionButtons"/>
            <c:if test="${link=='driver'}">
                &nbsp;&nbsp;
                <input id="send" type="button" title="Send" class="actionButtons"/>
            </c:if>

        </div>
        <div id="accordion">

            <%--<span>Test message</span>--%>
            <%--<div>--%>
            <%--<textarea id="testCode" name="resCode"></textarea>--%>
            <%--</div>--%>
        </div>
    </div>
    <c:if test="${link=='driver'}">
        <div id="resWrapper">
            <span>Response</span>
            <div style="font-size: 8pt">&nbsp;</div>
            <textarea id="resCode" name="resCode"></textarea>
        </div>
    </c:if>
</form>
<div id="htmlConverter" style="display: none"></div>

<script src="../js/editor.js"></script>
<c:if test="${link=='driver'}">
    <script src="../js/editor_driver.js"></script>
</c:if>
<style>
    <%--resize tabs--%>
    .ui-helper-reset {
        font-size: 75%;
    }
    .ui-tabs .ui-tabs-panel{
        padding: 0;
    }
</style>
<script>
    $().ready(function () {
        $("#info").fadeTo(0, 0);
        $("#tabs").tabs({
            collapsible: true,
            active: false
        });
    });
    var editor = CodeMirror.fromTextArea(document.getElementById("code"), xmlEditorSettings);
    editor.setSize("700", "400");
    var groovyEditor = CodeMirror.fromTextArea(document.getElementById("scriptCode"), groovyEditorSettings);
    groovyEditor.setSize("700", "200");
    var testEditor = CodeMirror.fromTextArea(document.getElementById("testCode"), xmlEditorSettings);
    testEditor.setSize("700", "200");

    <c:if test="${link=='driver'}">
    var resEditor = CodeMirror.fromTextArea(document.getElementById("resCode"), xmlEditorSettings);
    resEditor.setSize("600", "400");
    </c:if>

    //    $(function () {
    //        $("#templateInfo").tooltip({
    //            content: function () {
    //                return $(this).prop('title');
    //            }
    //        });
    //    });
</script>

</body>
</html>