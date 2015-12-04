/**
 * Created by sbt-bochev-as on 19.12.2014.
 */
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

var xmlEditorSettings = {
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
};

var groovyEditorSettings = {
    mode: "groovy",
    lineNumbers: true,
    lineWrapping: true,
    gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter"]
};

function showInfo(text) {
    if(text) {
        //IE8 trim compatibility
        text = $.trim(text);
        var info = $("#info");
        info.html(text).clearQueue().show().fadeTo(0, 0.7);
        info.delay(800).fadeTo(800, 0);
    }
}

function showError(text) {
    if(text) {
        //IE8 trim compatibility
        text = $.trim(text).replace(new RegExp('\\\\r\\\\n','g'),"<br/>");
        $("#error").css("display","block").html(text);
    } else {
        $("#error").css("display","none");
    }
}

function htmlConvert(data) {
    var converter = $("#htmlConverter");
    converter.html(data);
    //IE8 trim compatibility
    return $.trim(converter.text());
}

function showResponse(text) {
    if(text) {
        $("#resWrapper").css("display", "block");
        resEditor.setValue(text);
        autoFormatResponse();
    } else {
        $("#resWrapper").css("display", "none");
    }
}

function autoFormatResponse() {
    var totalLines = resEditor.lineCount();
    var totalChars = resEditor.getTextArea().value.length;
    resEditor.autoFormatRange({line:0, ch:0}, {line:totalLines, ch: totalChars});
    resEditor.setCursor({line:0, ch:0});
}

//Handlers
$("#validate").click(function(){
//  alert("Code:"+editor.getValue());
    var parts = QueryString["ip"].split("__");
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/validate/",
        type: "POST",
        data: { xml :editor.getValue(),
            script : groovyEditor.getValue(),
            test : testEditor.getValue()},
        success: function(obj) {
            obj = htmlConvert(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info)
            showError(obj.error);
        },
        fail: function() {
            showError("Unable to verify! Try Later...");
        }
    });
});

$("#save").click(function(){
//  alert("Saving...");
    //encodeURIComponent() ??
    var parts = QueryString["ip"].split("__");
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/save/",
        type: "POST",
        data: { xml :editor.getValue(),
                script : groovyEditor.getValue(),
                test : testEditor.getValue()},
        success: function(obj) {
            obj = htmlConvert(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
        },
        fail: function() {
            showError("Unable to save! Try Later...");
        }
    });
});

$("#undo").click(function(){
    var parts = QueryString["ip"].split("__");
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/undo/",
        type: "POST",
        success: function(obj) {
            obj = htmlConvert(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
            if(obj.data) {
                editor.setValue(htmlConvert(obj.data));
            }
        },
        fail: function() {
            showError("Unable to Undo! Try Later...");
        }
    });
});


$("#redo").click(function(){
    var parts = QueryString["ip"].split("__");
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/redo/",
        type: "POST",
        success: function(obj) {
            obj = htmlConvert(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
            if(obj.data) {
                editor.setValue(htmlConvert(obj.data));
            }
        },
        fail: function() {
            showError("Unable to save! Try Later...");
        }
    });
});

$("#reset").click(function(){
//  alert("Restore defaults...");
    var parts = QueryString["ip"].split("__");
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/resetToDefault/",
        type: "POST",
        success: function(obj) {
            obj = htmlConvert(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
            editor.setValue(htmlConvert(obj.data));
        },
        fail: function() {
            showError("Unable to save! Try Later...");
        }
    });
});

$("#test").click(function () {
    showResponse();
    var parts = QueryString["ip"].split("__");
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/test/",
        type: "POST",
        data: { xml :editor.getValue(),
            script : groovyEditor.getValue(),
            test : testEditor.getValue()},
        success: function (obj) {
            obj = htmlConvert(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
            showResponse(htmlConvert(obj.data));
        },
        error: function (jqXHR, textStatus, obj) {
            showError(obj);
        },
        fail: function () {
            showError("Unable to send! Try Later...");
        }
    });
});

var QueryString = getQueryString();
