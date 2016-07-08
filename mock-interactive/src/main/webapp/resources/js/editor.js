/**
 * Created by sbt-bochev-as on 19.12.2014.
 */

var codeEditor;
var resCodeEditor;
var scriptEditor;
var testEditor;


$().ready(function (){
    var editorTheme = "ace/theme/xcode";

    codeEditor = ace.edit("code");
    codeEditor.getSession().setMode("ace/mode/xml");
    codeEditor.setTheme(editorTheme);

    resCodeEditor = ace.edit("resCode");
    resCodeEditor.getSession().setMode("ace/mode/xml");
    resCodeEditor.setTheme(editorTheme);

    scriptEditor = ace.edit("scriptCode");
    scriptEditor.getSession().setMode("ace/mode/groovy");
    scriptEditor.setTheme(editorTheme);

    testEditor = ace.edit("testCode");
    testEditor.getSession().setMode("ace/mode/xml");
    testEditor.setTheme(editorTheme);

    updateWizardHeight();
});

function showInfo(text) {
    if (text) {
        //IE8 trim compatibility
        text = $.trim(text);
        var info = $("#info");
        info.html(text).clearQueue().show().fadeTo(0, 0.7);
        info.delay(800).fadeTo(800, 0);
    }
}

function showError(text) {
    if (text) {
        //IE8 trim compatibility
        text = $.trim(text).replace(/\\r/g, "").replace(/\\n/g, "<br/>");
        $("#error").css("display", "block").html(text);
    } else {
        $("#error").css("display", "none");
    }
}


function showResponse(text) {
    if (text) {
        $("#resWrapper").css("display", "block");
        resEditor.setValue(text);
        autoFormatResponse();
    } else {
        $("#resWrapper").css("display", "none");
    }
}


//Handlers
$("#validate").click(function () {
//  alert("Code:"+editor.getValue());
    var parts = QueryString["ip"].split("__");
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/validate/",
        type: "POST",
        data: {
            xml: editor.getValue(),
            script: groovyEditor.getValue(),
            test: testEditor.getValue()
        },
        success: function (obj) {
            obj = htmlConvert(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
        },
        fail: function () {
            showError("Unable to verify! Try Later...");
        }
    });
});

$("#save").click(function () {
    var parts = QueryString["ip"].split("__");
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/save/",
        type: "POST",
        data: {
            xml: editor.getValue(),
            script: groovyEditor.getValue(),
            test: testEditor.getValue()
        },
        success: function (obj) {
            obj = htmlConvert(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
        },
        fail: function () {
            showError("Unable to save! Try Later...");
        }
    });
});

$("#undo").click(function () {
    var parts = QueryString["ip"].split("__");
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/undo/",
        type: "POST",
        success: function (obj) {
            obj = htmlConvert(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
            if (obj.data) {
                editor.setValue(htmlConvert(obj.data));
            }
        },
        fail: function () {
            showError("Unable to Undo! Try Later...");
        }
    });
});


$("#redo").click(function () {
    var parts = QueryString["ip"].split("__");
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/redo/",
        type: "POST",
        success: function (obj) {
            obj = htmlConvert(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
            if (obj.data) {
                editor.setValue(htmlConvert(obj.data));
            }
        },
        fail: function () {
            showError("Unable to save! Try Later...");
        }
    });
});

$("#reset").click(function () {
    var parts = QueryString["ip"].split("__");
    var filtered = $("#filterGen").is(":checked")?"filtered":"";
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/resetToDefault/"+ filtered,
        type: "POST",
        success: function (obj) {
            obj = htmlConvert(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
            editor.setValue(htmlConvert(obj.data));
        },
        fail: function () {
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
        data: {
            xml: editor.getValue(),
            script: groovyEditor.getValue(),
            test: testEditor.getValue()
        },
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


//Integration point buttons
function editIpForm() {
    var system;
    var type;
    var name;
    var dialog = $("#dialog");

    dialog.html(updateUrl)
        .dialog({
            autoOpen: true,
            height: 450,
            width: 1100,
            modal: true,
            title: "Edit Integration point",
            draggable: false,
            resizable: false,
            open: openForm(),
            close: function () {
                closeForm()
            }
        });

    function openForm() {
        console.log("opened");
        $("#dialogFrame").on('load', function () {
            var frame = $("#dialogFrame").contents();
            if (frame.find("body").html() == "OK") {
                dialog.dialog('close');
            }

            frame.find("form input[type=submit]").click(function () {
                system = frame.find("#system").val();
                type = frame.find("#type").val().toLowerCase();
                name = frame.find("#name").val();
                frame.find("form").submit();
            })
        });
    }

    function closeForm() {
        console.log("go to " + system + type + name);
        if (system && type && name)
            window.location.href = "?ip=" + system + "__" + type + "__" + name;
    }


}
function delIpForm() {
    var dialog = $("#dialog");
    var confirmed = false;
    dialog.html(deleteUrl)
        .dialog({
            autoOpen: true,
            height: 200,
            width: 320,
            modal: true,
            title: "Delete Integration point",
            draggable: false,
            resizable: false,
            open: openForm(),
            close: function () {
                if( confirmed ) {
                    window.location.href = window.location.href.split("?")[0];
                }
            }
        });

    function openForm() {
        $("#dialogFrame").on('load', function () {
            var frame = $("#dialogFrame").contents();
            if (frame.find("body").html() == "OK") {
                confirmed = true;
                dialog.dialog('close');
            }

            frame.find("form #no").click(function () {
                dialog.dialog('close');
            })
        });
    }
}
