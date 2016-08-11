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

});

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
    newHeight = newHeight.substr(0,newHeight.length-2);
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

Array.prototype.remove = function (from, to) {
    var rest = this.slice((to || from) + 1 || this.length);
    this.length = from < 0 ? this.length + from : from;
    return this.push.apply(this, rest)
};

function moveMessageUp(templateId) {
    window.location.hash = openedCollapsedIp;
}

function expandAllIp() {
    $("tr[data-system]").each(function () {
        // openedCollapsedIp
    });
    // $.cookie("openedTabs",openedCollapsedIp);
    //window.location.reload();
}

function collapseAllIp() {
    $.removeCookie("openedTabs");
    window.location.reload();
}