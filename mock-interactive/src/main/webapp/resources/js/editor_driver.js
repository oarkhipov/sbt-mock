/**
 * Created by sbt-bochev-as on 19.12.2014.
 */
function sendDisable(disabled) {
    var send = $("#send");
    var glyph = send.find(".glyphicon");
    if(disabled) {
        send.prop("disabled", true);
        glyph.removeClass("glyphicon-send")
            .addClass("glyphicon-refresh glyphicon-refresh-animate");
    } else {
        send.prop("disabled", false);
        glyph.addClass("glyphicon-send")
            .removeClass("glyphicon-refresh glyphicon-refresh-animate");
    }
}

$().ready(function () {
    applyActionButtonsEvents();
});

function applyActionButtonsEvents() {
    $("#send").click(function () {
        //showInfo("Sending...");
        setResponse("Loading...");
        sendDisable(true);
        var parts = QueryString["ip"].split("__");
        $.ajax({
            url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/send/",
            type: "POST",
            data: { xml :codeEditor.getValue(),
                script : scriptEditor.getValue(),
                test : ""},
            success: function (obj) {
                obj = htmlDecode(obj);
                obj = $.parseJSON(obj);
                if(!obj.error) showInfo(obj.info);
                showError(obj.error);
                setResponse(obj.data);
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
        return false;
    });
}
