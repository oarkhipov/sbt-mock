/*
 * Copyright (c) 2016, Sberbank and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sberbank or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

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
