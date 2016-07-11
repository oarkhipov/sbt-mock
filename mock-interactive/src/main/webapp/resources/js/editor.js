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
    codeEditor.$blockScrolling = Infinity;

    resCodeEditor = ace.edit("resCode");
    resCodeEditor.getSession().setMode("ace/mode/xml");
    resCodeEditor.setTheme(editorTheme);
    resCodeEditor.$blockScrolling = Infinity;

    scriptEditor = ace.edit("scriptCode");
    scriptEditor.getSession().setMode("ace/mode/groovy");
    scriptEditor.setTheme(editorTheme);
    scriptEditor.$blockScrolling = Infinity;

    if($("#testCode").length > 0) {
        testEditor = ace.edit("testCode");
        testEditor.getSession().setMode("ace/mode/xml");
        testEditor.setTheme(editorTheme);
        testEditor.$blockScrolling = Infinity;
    }

    updateWizardHeight();
});

function htmlDecode(text) {
    return $('<div/>').html(text).text();
}

function htmlEncode(text) {
    return $('<div/>').text(text).html();
}

String.prototype.replaceAll2 = function (search, replacement) {
    var target = this;
    return target.split(search).join(replacement);
};

function showInfo(text) {
    if (text) {
        $.notify({
            //options
            icon: 'glyphicon glyphicon-info-sign',
            message: htmlDecode(text)
        },{
            //    settings
            type: "success",
            allow_dismiss: true,
            placement: {
                from: "bottom"
            },
            delay: 2000,
            animate: {
                enter: 'animated fadeInUp',
                exit: 'animated fadeOutDown'
            }
        });
    }
}
function showError(text) {
    if (text) {
        text = text.replaceAll2("\\r\\n","<br/>");
        text = text.replaceAll2("\\n\\r","<br/>");
        text = text.replaceAll2("\\n","<br/>");
        text = text.replaceAll2("\\r","");
        $.notify({
            //options
            icon: 'glyphicon glyphicon-warning-sign',
            message: text
        }, {
            //    settings
            type: "danger",
            placement: {
                from: "bottom"
            },
            delay: 0,
            animate: {
                enter: 'animated fadeInUp',
                exit: 'animated fadeOutDown'
            }
        });
    }
}


function setResponse(text) {
    if (text) {
        resCodeEditor.setValue(htmlDecode(text));
    }
}

//Handlers
$("#reset").click(function () {
    var parts = QueryString["ip"].split("__");
    var filtered = $("#filterGen").is(":checked")?"filtered":"";
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/resetToDefault/"+ filtered,
        type: "POST",
        success: function (obj) {
            obj = htmlDecode(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
            codeEditor.setValue( htmlDecode(obj.data).trim(),-1);
        },
        fail: function () {
            showError("Unable to reset! Try again later...");
        }
    });
});

$("#validate").click(function () {
//  alert("Code:"+editor.getValue());
    var parts = QueryString["ip"].split("__");
    var testText = "";
    if (testEditor) {
        testText = testEditor.getValue();
    }
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/validate/",
        type: "POST",
        data: {
            xml: codeEditor.getValue(),
            script: scriptEditor.getValue(),
            test: testText
        },
        success: function (obj) {
            obj = htmlDecode(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
        },
        fail: function () {
            showError("Unable to validate! Try again later...");
        }
    });
});

$("#save").click(function () {
    var parts = QueryString["ip"].split("__");
    var testText = "";
    if (testEditor) {
        testText = testEditor.getValue();
    }
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/save/",
        type: "POST",
        data: {
            xml: codeEditor.getValue(),
            script: scriptEditor.getValue(),
            test: testText
        },
        success: function (obj) {
            obj = htmlDecode(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
        },
        fail: function () {
            showError("Unable to save! Try again later...");
        }
    });
});

$("#test").click(function () {
    setResponse();
    var testText = "";
    if (testEditor) {
        testText = testEditor.getValue();
    }
    var parts = QueryString["ip"].split("__");
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/test/",
        type: "POST",
        data: {
            xml: codeEditor.getValue(),
            script: scriptEditor.getValue(),
            test: testText
        },
        success: function (obj) {
            obj = htmlDecode(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
            setResponse(obj.data);
        },
        error: function (jqXHR, textStatus, obj) {
            showError(obj);
        },
        fail: function () {
            showError("Unable to test! Try again later...");
        }
    });
});

$("#testRegenerate").click(function () {
    var parts = QueryString["ip"].split("__");
    var filtered = $("#testFilterGen").is(":checked")?"filtered":"";
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/resetToDefault/test/"+ filtered,
        type: "POST",
        success: function (obj) {
            obj = htmlDecode(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
            testEditor.setValue( htmlDecode(obj.data).trim(),-1);
        },
        fail: function () {
            showError("Unable to regenerate! Try again later...");
        }
    });
});

$("#testValidate").click(function () {
//  alert("Code:"+editor.getValue());
    var parts = QueryString["ip"].split("__");
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/validate/test/",
        type: "POST",
        data: {
            xml: testEditor.getValue()
        },
        success: function (obj) {
            obj = htmlDecode(obj);
            obj = $.parseJSON(obj);
            showInfo("Test validating:<br/>" + obj.info);
            if (obj.error) {
                showError("Test validating:<br/>" + obj.error);
            }
        },
        fail: function () {
            showError("Unable to validate! Try again later...");
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
