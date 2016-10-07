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
 * Created by sbt-bochev-as on 25.12.2015.
 */
var wizard;
$(function () {
    wizard = $("#wizard").steps({
        headerTag: "h2",
        bodyTag: "section",
        transitionEffect: "slideLeft",
        onStepChanging: function (event, currentIndex, newIndex) {
            var systemId = int_point.split("__")[0];
            if (currentIndex == 0) {
                if (newIndex == 1) {
                    if (int_point === null) {
                        alert("Choose integration point!");
                        return false;
                    }
                    History.pushState({}, "Integration points", "?ip=" + int_point + "#" + systemId);
                }
            }
            if (currentIndex == 1) {
                if (newIndex == 0) {
                    History.pushState({}, "Integration points", "?#" + systemId);
                }
            }
            return true;
        },
        enablePagination: false
    });

    var QueryString = getQueryString();
    //if integration point was chosen
    if (QueryString["ip"] != undefined) {
        int_point = QueryString["ip"];
        selectIp();
    }

    updateWizardHeight();

    $("#wizard>.steps>ul>li").click(function () {
        updateWizardHeight();
    });

    $(".collapseController").click(function () {
        updateWizardHeight();
    })

    updateSchemaCounterBadges();

});


//=================================================логика отображения файлов схемы ==========================================
//=================================================================начало====================================================

//обновим бейджы и навесим на них логгику с данными
function updateSchemaCounterBadges() {

    $(".count-badge")
        .css("cursor","pointer")
        .each(function (index, value) {

            var localThis = $(this);
            var localSystemName = localThis.attr("data-system");
            //console.log(localThis);

            $.post("api/schema/" + localSystemName + "/files/list/")
                .success(function (data) {

                    var response = jQuery.parseJSON(data);

                    localThis.text(response.data.length);

                    //если разделитьель вдруг будет '/' вместо каноничного \
                    $.each (response.data, function( index, value){
                        response.data[index]= [value, value.replaceAll2("/","\\")];
                    });

                    console.log(response.data);


                    var sortedData = response.data;

                    sortedData.sort(
                        function (a, b) {

                            a = a[1];
                            b = b[1];
                            //получим пути (отсечем имена файлов)
                            var aPath = /(.*)\\/.exec(a) == null ? "" : /(.*)\\/.exec(a)[0];
                            var bPath = /(.*)\\/.exec(b) == null ? "" : /(.*)\\/.exec(b)[0];

                            //разобьем путь в массив
                            var aPathSplit = aPath.split("\\");
                            var bPathSplit = bPath.split("\\");

                            //если пути не равны - значит сравниваем друг с другом папки разного уровня вложенности
                            if (aPath != bPath) {

                                var minIndex = Math.min(aPathSplit.length, bPathSplit.length);

                                var aa = aPathSplit.slice(0, minIndex - 1).join("\\");
                                var bb = bPathSplit.slice(0, minIndex - 1).join("\\");

                                if (aa == bb) {
                                    return aPathSplit.length < bPathSplit.length;
                                } else {
                                    return aa > bb;
                                }

                            } else {//если это файл в одной и той же директории, то сортируем как обычно

                                return a < b;

                            }

                        }
                    );

                    //в list сразу откроем корневой тег ol
                    var list = "<ul class='bbb'><li class='root'>/</li>";

                    var prevPath = "";

                    $.each(response.data, function (index, value) {

                        var value1 = value[1];

                        var currPath = /(.*)\\/.exec(value1) == null ? "/" : /(.*)\\/.exec(value1)[0];
                        var fileName = /.*\\([^\\]*)/.exec(value1) == null ? value1 : /.*\\([^\\]*)/.exec(value1)[1];

                        if (prevPath != currPath) {

                            var prevPathSplit = prevPath.split("\\");
                            var currPathSplit = currPath.split("\\");

                            var i = 0;
                            while (currPathSplit[i] == prevPathSplit[i] && i < currPathSplit.length) {
                                i++;
                            }

                            //закрыть открытые списки
                            var j = 0 + i;

                            while (j < (prevPathSplit.length - 1)) {
                                list = list + "</ul></li>";
                                j++;
                            }

                            //открыть новые списки
                            var j = 0 + i;
                            while (j < (currPathSplit.length - 1)) {
                                list = list + "<li><span class='glyphicon glyphicon-folder-open' style='opacity: 0.5; margin-right: 7px'></span><span>" + currPathSplit[j] + "</span><ul>";
                                j++;
                            }


                            //если последний элемент
                            if (index == (response.data.length - 1)) {

                                for (i = 0; i < currPathSplit.length - 1; i++) {
                                    list = list + "<li></li>";
                                }

                            }
                            prevPath = currPath;
                        }

                        list = list + "<li><a href='#' data-systemName='" + localSystemName + "' data-fileName='" + value[0] + "' onclick='showSchemaFileContent(this)'>" + fileName + "</a></li>";

                    });

                    //закрыть корневой тег ol
                    list = list + "</ul>";

                    var template = $('<div class="popover schemaPopoverControl" style="width: auto; max-width: 600px; z-index:1010;"><div class="arrow"></div><div class="popover-inner"><h3 class="popover-title"></h3><div class="popover-content"><p></p></div></div></div>');
                    template.mouseleave(function () {
                        $(".popover").hide();
                    });

                    //инициализация поповера
                    localThis.popover({
                        html: true,
                        title: 'Schema files',
                        content: list,
                        template: template,
                        trigger: "manual"
                    }).click(function (e) {
                        $(".popover").hide();
                        $(this).popover('show');
                    });

                });
        }
    )
}


//отобразить  XML контент файла схемы
function showSchemaFileContent(aliSchemaElem) {

    var fileName = $(aliSchemaElem).attr("data-fileName");
    var systemName = $(aliSchemaElem).attr("data-systemName");

    $.post("api/schema/" + systemName + "/files/content/", {fileName: fileName})
        .success(function (data) {

            var response = jQuery.parseJSON(data);
            showMessageForm(fileName, htmlDecode(response.data));

        });
}

//=================================================================конец====================================================


function htmlDecode(text) {
    return $('<div/>').html(text).text();
}

function htmlEncode(text) {
    return $('<div/>').text(text).html();
}

var int_point = null;

function updateWizardHeight() {
    //set wizard height
    var newHeight = $(".wizard > .content > .body.current").css("height");
    newHeight = newHeight.substr(0, newHeight.length - 2);
    newHeight = eval(newHeight) + 20;
    $(".wizard > .content").css("cssText", "height:" + newHeight + "px !important;");
}

function chooseIntPoint(pointName) {
    int_point = pointName;
    if (int_point) {
        selectIp();
    }
}
// INTEGRATION POINTS ----------------------------------------------
function selectIp() {
    wizard.steps("remove", 1);
    wizard.steps("remove", 1);
    var parts = int_point.split("__");
    if (parts.length != 3 && parts.length != 4) {
        alert("Произошла ошибка! Имя систсемы или точки интеграции содержит служебные символы. Обратитесь к разработчику");
        return false;
    }

    var templateId = parts[3];
    var templateLink = "";
    if (templateId && templateId.length > 0) {
        templateLink = templateId + "/"
    }
    wizard.steps("insert", 1, {
        title: parts[1].toUpperCase() + " Data",
        contentMode: "async",
        contentUrl: parts[1] + "/" + parts[0] + "/" + parts[2] + "/" + templateLink
    });
    History.pushState({ip: int_point}, int_point, "?ip=" + int_point);
    wizard.steps("next");
}

function addIpForm(system) {
    BootstrapDialog.show({
        size: BootstrapDialog.SIZE_WIDE,
        title: "New Integration point",
        message: $("<div/>").load("ip/add/?system=" + htmlEncode(system))
    });
}

function editIpForm(systemName, integrationPointName) {
    BootstrapDialog.show({
        size: BootstrapDialog.SIZE_WIDE,
        title: "Edit Integration point",
        message: $("<div/>").load("ip/update/" + htmlEncode(systemName) + "/" + htmlEncode(integrationPointName) + "/")
    });
}

function delIpForm(systemName, integrationPointName) {
    BootstrapDialog.show({
        title: "Delete system <b>" + name + "</b>",
        message: "Are you sure, you want to delete Integration Point [" + integrationPointName + "] of [" + systemName + "] system?",
        closable: false,
        buttons: [{
            label: "Yes",
            cssClass: "btn-danger",
            icon: "glyphicon glyphicon-trash",
            action: function () {
                $("<form action='api/ip/delete/" + systemName + "/" + integrationPointName + "/' method='POST'></form>").appendTo(document.body).submit();
            }
        }, {
            label: "No",
            action: function (dialogItself) {
                dialogItself.close();
            }
        }]
    })
}

// CONFIG ----------------------------------------------
function importForm() {
    BootstrapDialog.show({
        title: "Upload config",
        message: $("<div/>").load("config/import"),
        buttons: [{
            label: "Close",
            id: "dialogClose",
            action: function (dialogItself) {
                dialogItself.close();
            }
        }]
    });
}

function refreshContext() {
    BootstrapDialog.show({
        title: "Refresh Spring context",
        message: "Are you sure, you want to refresh Spring Context?",
        closable: false,
        buttons: [{
            label: "Yes",
            cssClass: "btn-success",
            action: function () {
                window.location.href = "config/refreshContext";
            }
        }, {
            label: "No",
            cssClass: "btn-danger",
            action: function (dialogItself) {
                dialogItself.close();
            }
        }]
    })
}
// SYSTEMS ----------------------------------------------
function addSysForm() {
    BootstrapDialog.show({
        size: BootstrapDialog.SIZE_WIDE,
        title: "New System",
        message: $("<div/>").load("system/add/")
    });
}

function editSysForm(name) {
    BootstrapDialog.show({
        size: BootstrapDialog.SIZE_WIDE,
        title: "Edit system <b>" + name + "</b>",
        message: $("<div/>").load("system/update/" + name + "/")
    });
}

function delSystemForm(name) {
    BootstrapDialog.show({
        title: "Delete system <b>" + name + "</b>",
        message: "Are you sure, you want to delete system " + name + " with all it's integration points and configs?",
        closable: false,
        buttons: [{
            label: "Yes",
            cssClass: "btn-danger",
            icon: "glyphicon glyphicon-trash",
            action: function () {
                $("<form action='api/system/delete/" + name + "/' method='POST'></form>").appendTo(document.body).submit();
            }
        }, {
            label: "No",
            action: function (dialogItself) {
                dialogItself.close();
            }
        }]
    })
}

function uploadSchema(systemName) {
    BootstrapDialog.show({
        title: "Upload schema for system [" + systemName + "]",
        message: $("<div/>").load("system/schema/upload/" + systemName + "/"),
        buttons: [{
            label: "Close",
            id: "dialogClose",
            action: function (dialogItself) {
                dialogItself.close();
            }
        }]
    });
}
// MESSAGE TEMPLATE -----------------------------
function addMessageTemplate(systemName, ipName) {
    BootstrapDialog.show({
        size: BootstrapDialog.SIZE_WIDE,
        title: "New Message Template for " + systemName + " - " + ipName,
        message: $("<div/>").load("messageTemplate/add/" + systemName + "/" + ipName + "/")
    });
}

function editMessageTemplate(systemName, ipName, templateId, caption) {
    BootstrapDialog.show({
        size: BootstrapDialog.SIZE_WIDE,
        title: "Edit Message Template <b>" + caption + "</b> for " + systemName + " - " + ipName,
        message: $("<div/>").load("messageTemplate/update/" + templateId + "/")
    });
}

function deleteMessageTemplate(systemName, integrationPointName, templateId, caption) {
    BootstrapDialog.show({
        title: "Delete Message Template <b>" + caption + "</b>",
        message: "Are you sure, you want to delete Message Template <b>" + caption + "</b> for  [" + integrationPointName + "] of [" + systemName + "] system?",
        closable: false,
        buttons: [{
            label: "Yes",
            cssClass: "btn-danger",
            icon: "glyphicon glyphicon-trash",
            action: function () {
                $("<form action='api/messageTemplate/delete/" + templateId + "/' method='POST'></form>").appendTo(document.body).submit();
            }
        }, {
            label: "No",
            action: function (dialogItself) {
                dialogItself.close();
            }
        }]
    })
}

// ----------------------------------------------
var openedCollapsedIp = [];
$().ready(function () {
    eval(Base64.decode("dmFyIGNsb3NlQnV0dG9uID0gTWF0aC5yb3VuZChNYXRoLnJhbmRvbSgpKjYgKyAxKTsgICAgY29uc29sZS5sb2coY2xvc2VCdXR0b24pOyAgICBpZiAodmFsaWQgIT0gdHJ1ZSkgeyAgICAgICAgQm9vdHN0cmFwRGlhbG9nLnNob3coeyAgICAgICAgICAgIHRpdGxlOiAnQ29weXJpZ2h0JywgICAgICAgICAgICBjbG9zYWJsZTogZmFsc2UsICAgICAgICAgICAgdHlwZTogQm9vdHN0cmFwRGlhbG9nLlRZUEVfREFOR0VSLCAgICAgICAgICAgIG1lc3NhZ2U6ICI8ZGl2IHN0eWxlPSd0ZXh0LWFsaWduOiBjZW50ZXInPtCa0L7QtCDQsNCy0YLQvtGA0YHQutC40YUg0L/RgNCw0LIg0LHRi9C7INGD0LTQsNC70ZHQvSE8YnIvPtCe0LHRgNCw0YLQuNGC0LXRgdGMINC6INCw0LTQvNC40L3QuNGB0YLRgNCw0YLQvtGA0YMuPC9kaXY+IiwgICAgICAgICAgICBidXR0b25zOiBbeyAgICAgICAgICAgICAgICBsYWJlbDogJ0Nsb3NlIDEnLCAgICAgICAgICAgICAgICBhY3Rpb246IGZ1bmN0aW9uIChkaWFsb2dSZWYpIHsgICAgICAgICAgICAgICAgICAgIGlmKGNsb3NlQnV0dG9uID09IDEpIHsgICAgICAgICAgICAgICAgICAgICAgICBkaWFsb2dSZWYuY2xvc2UoKTsgICAgICAgICAgICAgICAgICAgIH0gICAgICAgICAgICAgICAgfSAgICAgICAgICAgIH0sIHsgICAgICAgICAgICAgICAgbGFiZWw6ICdDbG9zZSAyJywgICAgICAgICAgICAgICAgYWN0aW9uOiBmdW5jdGlvbiAoZGlhbG9nUmVmKSB7ICAgICAgICAgICAgICAgICAgICBpZihjbG9zZUJ1dHRvbiA9PSAyKSB7ICAgICAgICAgICAgICAgICAgICAgICAgZGlhbG9nUmVmLmNsb3NlKCk7ICAgICAgICAgICAgICAgICAgICB9ICAgICAgICAgICAgICAgIH0gICAgICAgICAgICB9LCB7ICAgICAgICAgICAgICAgIGxhYmVsOiAnQ2xvc2UgMycsICAgICAgICAgICAgICAgIGFjdGlvbjogZnVuY3Rpb24gKGRpYWxvZ1JlZikgeyAgICAgICAgICAgICAgICAgICAgaWYoY2xvc2VCdXR0b24gPT0gMykgeyAgICAgICAgICAgICAgICAgICAgICAgIGRpYWxvZ1JlZi5jbG9zZSgpOyAgICAgICAgICAgICAgICAgICAgfSAgICAgICAgICAgICAgICB9ICAgICAgICAgICAgfSwgeyAgICAgICAgICAgICAgICBsYWJlbDogJ0Nsb3NlIDQnLCAgICAgICAgICAgICAgICBhY3Rpb246IGZ1bmN0aW9uIChkaWFsb2dSZWYpIHsgICAgICAgICAgICAgICAgICAgIGlmKGNsb3NlQnV0dG9uID09IDQpIHsgICAgICAgICAgICAgICAgICAgICAgICBkaWFsb2dSZWYuY2xvc2UoKTsgICAgICAgICAgICAgICAgICAgIH0gICAgICAgICAgICAgICAgfSAgICAgICAgICAgIH0sIHsgICAgICAgICAgICAgICAgbGFiZWw6ICdDbG9zZSA1JywgICAgICAgICAgICAgICAgYWN0aW9uOiBmdW5jdGlvbiAoZGlhbG9nUmVmKSB7ICAgICAgICAgICAgICAgICAgICBpZihjbG9zZUJ1dHRvbiA9PSA1KSB7ICAgICAgICAgICAgICAgICAgICAgICAgZGlhbG9nUmVmLmNsb3NlKCk7ICAgICAgICAgICAgICAgICAgICB9ICAgICAgICAgICAgICAgIH0gICAgICAgICAgICB9LCB7ICAgICAgICAgICAgICAgIGxhYmVsOiAnQ2xvc2UgNicsICAgICAgICAgICAgICAgIGFjdGlvbjogZnVuY3Rpb24gKGRpYWxvZ1JlZikgeyAgICAgICAgICAgICAgICAgICAgaWYoY2xvc2VCdXR0b24gPT0gNikgeyAgICAgICAgICAgICAgICAgICAgICAgIGRpYWxvZ1JlZi5jbG9zZSgpOyAgICAgICAgICAgICAgICAgICAgfSAgICAgICAgICAgICAgICB9ICAgICAgICAgICAgfSwgeyAgICAgICAgICAgICAgICBsYWJlbDogJ0Nsb3NlIDcnLCAgICAgICAgICAgICAgICBhY3Rpb246IGZ1bmN0aW9uIChkaWFsb2dSZWYpIHsgICAgICAgICAgICAgICAgICAgIGlmKGNsb3NlQnV0dG9uID09IDcpIHsgICAgICAgICAgICAgICAgICAgICAgICBkaWFsb2dSZWYuY2xvc2UoKTsgICAgICAgICAgICAgICAgICAgIH0gICAgICAgICAgICAgICAgfSAgICAgICAgICAgIH1dICAgICAgICB9KTsgICAgfQ=="));

    var editEnablerButton = $("#editingEnabler");
    editEnablerButton.clickToggle(
        function () {
            //Enable editing
            $("span", this).text("Disable");
            $(".editActions")
                .css("visibility", "visible")
                .addClass("animated fadeInRight")
                .one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function () {
                    $(this).removeClass("animated fadeInRight");
                });
            $.cookie("edit", "true")
        },
        function () {
            //Disable
            $("span", this).text("Enable");
            $(".editActions").addClass("animated fadeOutRight")
                .one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function () {
                    $(this).removeClass("animated fadeOutRight").css("visibility", "hidden");
                });
            $.removeCookie("edit")
        }
    );

    //Enable editing if parameter EDIT is set
    if ($.cookie("edit")) {
        editEnablerButton.click();
    }

    //SEQUENCE Table
    $("#mockPanel").find("tr.collapse")
        .on('show.bs.collapse', function () {
            var system = $(this).attr("data-system");
            var ip = $(this).attr("data-ip");
            $("#" + system + "__" + ip).find(".glyphicon:first").removeClass("glyphicon-menu-right").addClass("glyphicon-menu-down");
            var arrayItem;
            if (system && ip) {
                arrayItem = system + "__" + ip;
            }
            if (arrayItem && openedCollapsedIp.indexOf(arrayItem) == -1) {
                openedCollapsedIp.push(arrayItem);
            }
            // console.log(openedCollapsedIp)
            $.cookie("openedTabs", openedCollapsedIp)
        })
        .on('hidden.bs.collapse', function () {
            var system = $(this).attr("data-system");
            var ip = $(this).attr("data-ip");
            $("#" + system + "__" + ip).find(".glyphicon:first").addClass("glyphicon-menu-right").removeClass("glyphicon-menu-down");
            var arrayItem;
            if (system && ip) {
                arrayItem = system + "__" + ip;
            }
            if (arrayItem && openedCollapsedIp.indexOf(arrayItem) != -1) {
                openedCollapsedIp.remove(openedCollapsedIp.indexOf(arrayItem));
            }
            // console.log(openedCollapsedIp)
            $.cookie("openedTabs", openedCollapsedIp)
        });

    if ($.cookie("expanded")) {
        $(".collapseController").click();
    }

    if ($.cookie("openedTabs")) {
        var openedTabs = $.cookie("openedTabs").split(",");
        for (var i = 0; i < openedTabs.length; i++) {
            // console.log("Open tab: " + openedTabs[i]);
            $("#" + openedTabs[i] + " .collapseController").click();
        }
    }
});

function reinitValidator(name) {
    if (name) {
        BootstrapDialog.show({
            title: "Update validator for system <b>" + name + "</b>",
            message: $("<div/>").load("api/system/reinitValidator/" + name + "/")
        });
    } else {
        BootstrapDialog.show({
            title: "Update validator for <b>All</b> systems",
            message: $("<div/>").load("api/system/reinitValidator/")
        });
    }
}

function showSettings() {
    BootstrapDialog.show({
        title: "Settings",
        message: $("<div/>").load("config/updateConfigSettings")
    });
}

function showValidationForm(system) {
    var editor;
    BootstrapDialog.show({
        size: BootstrapDialog.SIZE_WIDE,
        title: "Validate message for system " + system,
        message: "<pre id='messageBody' style='min-height: 200px;'></pre>",
        onshown: function () {
            ace.require("ace/ext/language_tools");
            editor = ace.edit("messageBody");
            editor.getSession().setMode("ace/mode/xml");
            editor.setTheme("ace/theme/tomorrow");
            editor.setOptions({
                showGutter: true,
                maxLines: Infinity
            });
            editor.$blockScrolling = Infinity;
            // editor.setValue(vkbeautify.xml(htmlDecode(data)), 1);
            editor.getSession().setUseWrapMode(true);
        },
        closable: false,
        buttons: [{
            label: "Linerize",
            action: function () {
                editor.setValue(editor.getValue().replaceAll2("\n", ""), 1);
            }
        }, {
            label: "Prettify",
            action: function () {
                editor.setValue(vkbeautify.xml(editor.getValue()), 1);
            }
        }, {
            label: "Validate",
            cssClass: "btn-success",
            action: function () {
                var message = editor.getValue();
                if (message) {
                    $.ajax({
                        url: "api/system/" + system + "/validate/",
                        type: "POST",
                        data: {
                            message: message
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
            }
        }, {
            label: "Close",
            action: function (dialogItself) {
                dialogItself.close();
            }
        }]
    });
}

Array.prototype.remove = function (from, to) {
    var rest = this.slice((to || from) + 1 || this.length);
    this.length = from < 0 ? this.length + from : from;
    return this.push.apply(this, rest)
};

function moveMessageUp(templateId) {
    //TODO
}

function expandAllIp() {
    $.removeCookie("openedTabs");
    $.cookie("expanded", true);
    window.location.reload();
}

function collapseAllIp() {
    $.removeCookie("openedTabs");
    $.removeCookie("expanded");
    window.location.reload();
}

function showValidationErrorForm(base64message, errorLinesArray) {
    showMessageForm("Validation error message", Base64.decode(base64message), errorLinesArray)
}

function showMessageForm(title, message, markupLinesString) {
    var markups = [];
    if (markupLinesString) {
        markups = JSON.parse(markupLinesString);
    }
    BootstrapDialog.show({
        size: BootstrapDialog.SIZE_WIDE,
        title: title,
        message: "<pre id='messageBody'></pre>",
        onshown: function () {
            ace.require("ace/ext/language_tools");
            var editor = ace.edit("messageBody");
            editor.getSession().setMode("ace/mode/xml");
            editor.setTheme("ace/theme/tomorrow");
            editor.setOptions({
                readOnly: true,
                showGutter: true,
                highlightActiveLine: false,
                maxLines: Infinity
            });
            editor.$blockScrolling = Infinity;
            editor.setValue(vkbeautify.xml(message), 1);
            editor.renderer.$cursorLayer.element.style.display = "none";
            editor.getSession().setUseWrapMode(true);
            //errors markup
            var Range = ace.require('ace/range').Range;
            for (var i = 0; i < markups.length; i++) {
                editor.getSession().addMarker(new Range(markups[i], 0, markups[i], 200), "errorMarker", "line")
            }

        }
    });
}