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
    } else {
        $("#resWrapper").css("display", "none");
    }
}

$("#send").click(function(){
    showInfo("Sending...");
    sendDisable(true);
    $.ajax({
        url: QueryString["ip"]+ "/send/",
        type: "POST",
        data: {xml: editor.getValue() , request: $('select[name=request]').val() },
        success: function(obj) {
            obj = htmlConvert(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
            showResponse(htmlConvert(obj.data));
            sendDisable(false);
        },
        error: function (jqXHR, textStatus,obj) {
            showError(obj);
            sendDisable(false);
        },
        fail: function() {
            showError("Unable to send! Try Later...");
            sendDisable(false);
        }
    });
});

function fillList(data) {
    if(data) {
        console.log(data);
        data = data.split(",");
        console.log(data);
        var list = $("#reqList");
        list.find('option').remove();
        $.each(data, function () {
            list.append($("<option />").val(this).text(this));
        });
    }
}

$("#listRefresh").click(function(){
    $.ajax({
        url: QueryString["ip"]+ "/list/",
        type: "POST",
        data: {xml: editor.getValue()},
        success: function(obj) {
            obj = htmlConvert(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
            fillList(htmlConvert(obj.data));
        },
        fail: function() {
            showError("Unable to refresh! Try Later...");
        }
    });
});
