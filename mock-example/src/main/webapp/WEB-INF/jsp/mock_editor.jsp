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
  <title>CodeMirror: XML Autocomplete Demo</title>
  <link rel="stylesheet" href="/css/codemirror.css">
  <script src="/lib/codemirror.js"></script>
  <script src="/lib/show-hint.js"></script>
  <link rel="stylesheet" href="/css/show-hint.css">
  <script src="/lib/closetag.js"></script>
  <script src="/lib/xml-hint.js"></script>
  <script src="/lib/xml.js"></script>

  <link rel="stylesheet" href="/css/docs.css">
  <style type="text/css">.CodeMirror {border: 1px solid #eee;} .CodeMirror-scroll { height: 100% }</style>
</head>
<body>
<h1><c:out value="${name}"/></h1>

<form>
  <textarea id="code" name="code"><c:out value="${object}" escapeXml="true"/></textarea>
</form>

<script>
  var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
    value: '',
    mode: 'text/html',
    lineNumbers: true,
    extraKeys: {
      "'>'": function(cm) { cm.closeTag(cm, '>'); },
      "'/'": function(cm) { cm.closeTag(cm, '/'); },
      "' '": function(cm) { CodeMirror.xmlHint(cm, ' '); },
      "'<'": function(cm) { CodeMirror.xmlHint(cm, '<'); },
      "Ctrl-Space": function(cm) { CodeMirror.xmlHint(cm, ''); }
    }
  });
  editor.setSize("90%", "60%");
</script>
</body>
</html>
