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
<b>Integration point:</b> <i><c:out value="${name}"/></i>

<form>
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
      <input id="send" type="button" value="Send" style="display: inline"/>
    </c:if>

  </div>
</form>
<div id="htmlConverter" style="display: none"></div>

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

function showInfo(text) {
  var info = $("#info");
  info.html(text).fadeTo(0,0.7);
  info.delay(800).fadeTo(800,0);
}

function showError(text) {
  text = text.trim();
  if(text && text!="true") {
    $("#error").css("display","block").html(text);
  } else {
    $("#error").css("display","none");
    showInfo("OK!")
  }
}

//Handlers
$("#validate").click(function(){
//  alert("Code:"+editor.getValue());
  $.ajax({
    url: QueryString["ip"]+ "/validate/",
    type: "POST",
    data: "xml="+editor.getValue(),
    success: function(msg) {
      showError(msg);
    },
    fail: function() {
      showError("Unable to verify! Try Later...");
    }
  });
});

$("#save").click(function(){
//  alert("Saving...");
  $.ajax({
    url: QueryString["ip"]+ "/save/",
    type: "POST",
    data: "xml="+editor.getValue(),
    success: function(msg) {
      showInfo(msg);
    },
    fail: function() {
      showError("Unable to save! Try Later...");
    }
  });
});

$("#rollback").click(function(){
//  alert("Saving...");
  $.ajax({
    url: QueryString["ip"]+ "/rollback/",
    type: "POST",
    data: "xml="+editor.getValue(),
    success: function(msg) {
      var converter = $("#htmlConverter");
      converter.html(msg);
      msg = converter.text().trim();
      editor.setValue(msg);
      showInfo("Reset complete!");
    },
    fail: function() {
      showError("Unable to rollback! Try Later...");
    }
  });
});

$("#reset").click(function(){
//  alert("Restore defaults...");
  $.ajax({
    url: QueryString["ip"]+ "/resetToDefault/",
    type: "POST",
    success: function(msg) {
      var converter = $("#htmlConverter");
      converter.html(msg);
      msg = converter.text().trim();
      editor.setValue(msg);
//      showError(msg);
//      location.reload();
      showInfo("Reset complete!");
    },
    fail: function() {
      showError("Unable to save! Try Later...");
    }
  });
});

<c:if test="${link=='driver'}">

  function sendDisable(disabled) {
    var send = $("#send");
    send.prop("disabled",disabled);
    if(disabled) {
      send.css("color","gray");
    } else {
      send.css("color","black");
    }
  }

  $("#send").click(function(){
    showInfo("Sending...");
    sendDisable(true);
    $.ajax({
      url: QueryString["ip"]+ "/send/",
      type: "POST",
      data: "xml="+editor.getValue(),
      success: function(msg) {
        showError(msg);
        sendDisable(false);
      },
      fail: function() {
        showError("Unable to send! Try Later...");
        sendDisable(false);
      }
    });
  });
  </c:if>

var QueryString = function () {
  // This function is anonymous, is executed immediately and
  // the return value is assigned to QueryString!
  var query_string = {};
  var query = window.location.search.substring(1);
  var vars = query.split("&");
  for (var i=0;i<vars.length;i++) {
    var pair = vars[i].split("=");
    // If first entry with this name
    if (typeof query_string[pair[0]] === "undefined") {
      query_string[pair[0]] = pair[1];
      // If second entry with this name
    } else if (typeof query_string[pair[0]] === "string") {
      var arr = [ query_string[pair[0]], pair[1] ];
      query_string[pair[0]] = arr;
      // If third or later entry with this name
    } else {
      query_string[pair[0]].push(pair[1]);
    }
  }
  return query_string;
} ();
</script>
</body>
</html>
