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
  <link rel="stylesheet" href="/css/codemirror/codemirror.css">
  <script src="/lib/codemirror/codemirror.js"></script>
  <script src="/lib/codemirror/show-hint.js"></script>
  <link rel="stylesheet" href="/css/codemirror/show-hint.css">
  <script src="/lib/codemirror/closetag.js"></script>
  <script src="/lib/codemirror/xml-hint.js"></script>
  <script src="/lib/codemirror/xml.js"></script>
  <%--Folding--%>
  <link rel="stylesheet" href="/css/codemirror/foldgutter.css">
  <script src="/lib/codemirror/brace-fold.js"></script>
  <script src="/lib/codemirror/comment-fold.js"></script>
  <script src="/lib/codemirror/foldcode.js"></script>
  <script src="/lib/codemirror/foldgutter.js"></script>
  <script src="/lib/codemirror/markdown-fold.js"></script>
  <script src="/lib/codemirror/xml-fold.js"></script>



  <link rel="stylesheet" href="/css/docs.css">
  <style type="text/css">.CodeMirror {border: 1px solid #eee;} .CodeMirror-scroll { height: 100% }</style>
</head>
<body>
<h4>Integration point: <i><c:out value="${name}"/></i></h4>

<form>
  <textarea id="code" name="code"><c:out value="${object}" escapeXml="true"/></textarea>
  <div style="text-align: right; width: 1000px; padding-top: 7px">
    <input id="reset" type="button" value="Reset to default" style="display: inline"/>
    &nbsp;&nbsp;&nbsp;
    <input id="validate" type="button" value="Validate" style="display: inline"/>
    <input id="save" type="button" value="Save" style="display: inline"/>
    <c:if test="${link=='driver'}">
      <input id="send" type="button" value="Send" style="display: inline"/>
    </c:if>

  </div>
</form>

<script>
//  var dummy = {
//    attrs: {
//      color: ["red", "green", "blue", "purple", "white", "black", "yellow"],
//      size: ["large", "medium", "small"],
//      description: null
//    },
//    children: []
//  };
//
//  var tags = {
//    "!top": ["top"],
//    "!attrs": {
//      id: null,
//      class: ["A", "B", "C"]
//    },
//    top: {
//      attrs: {
//        lang: ["en", "de", "fr", "nl"],
//        freeform: null
//      },
//      children: ["animal", "plant"]
//    },
//    animal: {
//      attrs: {
//        name: null,
//        isduck: ["yes", "no"]
//      },
//      children: ["wings", "feet", "body", "head", "tail"]
//    },
//    plant: {
//      attrs: {name: null},
//      children: ["leaves", "stem", "flowers"]
//    },
//    wings: dummy, feet: dummy, body: dummy, head: dummy, tail: dummy,
//    leaves: dummy, stem: dummy, flowers: dummy
//  };

  function completeAfter(cm, pred) {
    var cur = cm.getCursor();
    if (!pred || pred()) setTimeout(function() {
      if (!cm.state.completionActive)
        cm.showHint({completeSingle: false});
    }, 100);
    return CodeMirror.Pass;
  }

  function completeIfAfterLt(cm) {
    return completeAfter(cm, function() {
      var cur = cm.getCursor();
      return cm.getRange(CodeMirror.Pos(cur.line, cur.ch - 1), cur) == "<";
    });
  }

  function completeIfInTag(cm) {
    return completeAfter(cm, function() {
      var tok = cm.getTokenAt(cm.getCursor());
      if (tok.type == "string" && (!/['"]/.test(tok.string.charAt(tok.string.length - 1)) || tok.string.length == 1)) return false;
      var inner = CodeMirror.innerMode(cm.getMode(), tok.state).state;
      return inner.tagName;
    });
  }

  var editor = CodeMirror.fromTextArea(document.getElementById("code"), {
    mode: "xml",
    lineNumbers: true,
    extraKeys: {
      "'<'": completeAfter,
      "'/'": completeIfAfterLt,
      "' '": completeIfInTag,
      "'='": completeIfInTag,
      "Ctrl-Space": completeAfter,
      "Ctrl-Q": function(cm){cm.foldCode(cm.getCursor());},
      "Ctrl--": function(cm){cm.foldCode(cm.getCursor());}
    },
//    hintOptions: {schemaInfo: tags},
    autoCloseTags: true,
    lineWrapping: true,
    foldGutter: true,
    gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter"]
  });
  editor.setSize("1000","400");

$("#validate").click(function(){
  alert("Code:"+editor.getValue());
});

$("#save").click(function(){
  alert("Saving...");
});

$("#reset").click(function(){
  alert("Restore defaults...");
});

<c:if test="${link=='driver'}">
  $("#send").click(function(){
    alert("sending...");
  });
  </c:if>
</script>
</body>
</html>
