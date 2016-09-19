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

var codeEditor;
var resCodeEditor;
var scriptEditor;
var testEditor;


$().ready(function () {
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

    if ($("#testCode").length > 0) {
        testEditor = ace.edit("testCode");
        testEditor.getSession().setMode("ace/mode/xml");
        testEditor.setTheme(editorTheme);
        testEditor.$blockScrolling = Infinity;
    }

    updateWizardHeight();
});

function setResponse(text) {
    if (text) {
        resCodeEditor.setValue(vkbeautify.xml(htmlDecode(text)), -1);
    }
}

//Handlers
$("#reset").click(function () {
    var parts = QueryString["ip"].split("__");
    var filtered = $("#filterGen").is(":checked") ? "filtered" : "";
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/resetToDefault/" + filtered,
        type: "POST",
        success: function (obj) {
            obj = htmlDecode(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
            codeEditor.setValue(htmlDecode(obj.data).trim(), -1);
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
    var message = codeEditor.getValue();
    if (message) {
        $.ajax({
            url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/validate/",
            type: "POST",
            data: {
                xml: message,
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
    }
});

$("#save").click(function () {
    var parts = QueryString["ip"].split("__");
    var testText = "";
    if (testEditor) {
        testText = testEditor.getValue();
    }
    var templateId = $("#templateId").val();
    var message = codeEditor.getValue();
    if (message) {
        $.ajax({
            url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/save/",
            type: "POST",
            data: {
                xml: message,
                script: scriptEditor.getValue(),
                test: testText,
                templateId: templateId
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
    }
});

$("#test").click(function () {
    setResponse();
    var testText = "";
    if (testEditor) {
        testText = testEditor.getValue();
    }
    var parts = QueryString["ip"].split("__");
    var message = codeEditor.getValue();
    if (message) {
        $.ajax({
            url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/test/",
            type: "POST",
            data: {
                xml: message,
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
    }
});

$("#testRegenerate").click(function () {
    var parts = QueryString["ip"].split("__");
    var filtered = $("#testFilterGen").is(":checked") ? "filtered" : "";
    $.ajax({
        url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/resetToDefault/test/" + filtered,
        type: "POST",
        success: function (obj) {
            obj = htmlDecode(obj);
            obj = $.parseJSON(obj);
            showInfo(obj.info);
            showError(obj.error);
            testEditor.setValue(htmlDecode(obj.data).trim(), -1);
        },
        fail: function () {
            showError("Unable to regenerate! Try again later...");
        }
    });
});

$("#testValidate").click(function () {
    var message = testEditor.getValue();
    if (message) {
        var parts = QueryString["ip"].split("__");
        $.ajax({
            url: parts[1] + "/" + parts[0] + "/" + parts[2] + "/validate/test/",
            type: "POST",
            data: {
                xml: message
            },
            success: function (obj) {
                obj = htmlDecode(obj);
                obj = $.parseJSON(obj);
                if (obj.info.length > 0) {
                    showInfo("Test validating:<br/>" + obj.info);
                }
                if (obj.error.length > 0) {
                    showError("Test validating:<br/>" + obj.error);
                }
            },
            fail: function () {
                showError("Unable to validate! Try again later...");
            }
        });
    }
});

var QueryString = getQueryString();
