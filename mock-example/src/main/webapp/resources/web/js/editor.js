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

var editorSettings = {
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

function showInfo(text) {
    if(text) {
        //IE8 trim compatibility
        text = $.trim(text);
        var info = $("#info");
        info.html(text).fadeTo(0, 0.7);
        info.delay(800).fadeTo(800, 0);
    }
}

function showError(text) {
    if(text) {
        //IE8 trim compatibility
        text = $.trim(text);
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

//Handlers
$("#validate").click(function(){
//  alert("Code:"+editor.getValue());
    $.ajax({
        url: QueryString["ip"]+ "/validate/",
        type: "POST",
        data: "xml="+encodeURIComponent(editor.getValue()),
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
    $.ajax({
        url: QueryString["ip"]+ "/save/",
        type: "POST",
        data: "xml="+encodeURIComponent(editor.getValue()),
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
    $.ajax({
        url: QueryString["ip"]+ "/undo/",
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
    $.ajax({
        url: QueryString["ip"]+ "/redo/",
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
    $.ajax({
        url: QueryString["ip"]+ "/resetToDefault/",
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

var QueryString = getQueryString();
