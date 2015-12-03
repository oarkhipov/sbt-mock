/**
 * Created by sbt-bochev-as on 19.12.2014.
 */
function sendDisable(disabled) {
    var send = $("#send");
    send.prop("disabled",disabled);
    if(disabled) {
        send.css("color","gray");
    } else {
        send.css("color","black");
    }
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

$().ready(function () {
    applyActionButtonsEvents();
});

function fillList(data) {
    if(data) {
        //console.log(data);
        data = data.split(",");
        //console.log(data);
        var list = $("#reqList");
        list.find('option').remove();
        $.each(data, function () {
            list.append($("<option />").val(this).text(this));
        });
    }
}

function applyActionButtonsEvents() {
    $("#send").click(function () {
        showInfo("Sending...");
        sendDisable(true);
        showResponse();
        var parts = QueryString["ip"].split("__");
        $.ajax({
            url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/send/",
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
                sendDisable(false);
            },
            error: function (jqXHR, textStatus, obj) {
                showError(obj);
                sendDisable(false);
            },
            fail: function () {
                showError("Unable to send! Try Later...");
                sendDisable(false);
            }
        });
    });
}
