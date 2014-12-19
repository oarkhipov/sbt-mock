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
  <link rel="stylesheet" href="../css/codemirror/codemirror.css">
  <script src="../lib/codemirror/codemirror.js"></script>
  <script src="../lib/codemirror/show-hint.js"></script>
  <link rel="stylesheet" href="../css/codemirror/show-hint.css">
  <script src="../lib/codemirror/closetag.js"></script>
  <script src="../lib/codemirror/xml-hint.js"></script>
  <script src="../lib/codemirror/xml.js"></script>
  <%--Folding--%>
  <link rel="stylesheet" href="../css/codemirror/foldgutter.css">
  <script src="../lib/codemirror/brace-fold.js"></script>
  <script src="../lib/codemirror/comment-fold.js"></script>
  <script src="../lib/codemirror/foldcode.js"></script>
  <script src="../lib/codemirror/foldgutter.js"></script>
  <script src="../lib/codemirror/markdown-fold.js"></script>
  <script src="../lib/codemirror/xml-fold.js"></script>

  <link rel="stylesheet" href="../css/docs.css">
  <style type="text/css">.CodeMirror {border: 1px solid #eee;} .CodeMirror-scroll { height: 100% }</style>
</head>
<body>
<b>Integration point:</b> <i><c:out value="${name}"/></i>

<form>
  <div id="codeWrapper" style="float: left">
    <div id="info">&nbsp;</div>
    <div id="error"></div>
    <textarea id="code" name="code"><c:out value="${object}" escapeXml="true"/></textarea>
    <div style="text-align: right; width: 1000px; padding-top: 7px">
      <input id="reset" type="button" value="Reset to default" style="display: inline"/>
      &nbsp;&nbsp;&nbsp;
      <input id="undo" type="button" value="Undo" style="display: inline"/>
      <input id="redo" type="button" value="Redo" style="display: inline"/>
      &nbsp;&nbsp;&nbsp;
      <input id="validate" type="button" value="Validate" style="display: inline"/>
      <input id="save" type="button" value="Save" style="display: inline"/>
      <c:if test="${link=='driver'}">
        &nbsp;&nbsp;&nbsp;
        <select id="reqList" name="request" style="width: 120px">
          <c:forEach var="entry" items="${list}">
            <option value="${entry}">${entry}</option>
          </c:forEach>
        </select>
        <input id="listRefresh" type="button" value="Refresh List" style="display: inline"/>
        &nbsp;&nbsp;&nbsp;
        <input id="send" type="button" value="Send" style="display: inline"/>
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

<script src="../lib/editor.js"></script>
<c:if test="${link=='driver'}">
  <script src="../lib/editor_driver.js"></script>
</c:if>
<script>
  var editor = CodeMirror.fromTextArea(document.getElementById("code"), editorSettings);
  editor.setSize("1000","400");
  <c:if test="${link=='driver'}">
    var resEditor = CodeMirror.fromTextArea(document.getElementById("resCode"), editorSettings);
    resEditor.setSize("600","400");
  </c:if>
</script>

</body>
</html>
