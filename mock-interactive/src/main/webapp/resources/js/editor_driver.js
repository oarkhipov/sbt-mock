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

$().ready(function () {
    applyActionButtonsEvents();
});

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