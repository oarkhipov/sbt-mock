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
            if (currentIndex == 0) {
                if (newIndex == 1) {
                    if (int_point === null) {
                        alert("Choose integration point!");
                        return false;
                    }
                    History.pushState({}, "Integration points", "?ip=" + int_point);
                }
            }
            if (currentIndex == 1) {
                if (newIndex == 0) {
                    History.pushState({}, "Integration points", "?");
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
});

var int_point = null;

function updateWizardHeight() {
    //set wizard height
    $(".wizard > .content").css("cssText", "height:" + $(".wizard > .content > .body.current").css("height") + " !important;");
}

function chooseIntPoint(pointName) {
    int_point = pointName;
    if (int_point) {
        selectIp();
    }
}

function selectIp() {
    wizard.steps("remove", 1);
    wizard.steps("remove", 1);
    var parts = int_point.split("__");
    if (parts.length != 3) {
        alert("Произошла ошибка! Имя систсемы или точки интеграции содержит служебные символы. Обратитесь к разработчику");
        return false;
    }
    wizard.steps("insert", 1, {
        title: parts[1].toUpperCase() + " Data",
        contentMode: "async",
        contentUrl: parts[1] + "/" + parts[0] + "/" + parts[2] + "/"
    });
    History.pushState({ip: int_point}, int_point, "?ip=" + int_point);
    wizard.steps("next");
}

function addIpForm() {
    var system;
    var type;
    var name;
    var dialog = $("#dialog");
    dialog.html("<iframe id='dialogFrame' src='ip/add/' width='1050' height='350' frameborder='0'/>")
        .dialog({
//                bgiframe: true,
            autoOpen: true,
            height: 450,
            width: 1100,
            modal: true,
            title: "Add Integration point",
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

//Integration point buttons
function importForm() {
    var dialog = $("#dialog");
    dialog.html("<iframe id='dialogFrame' src='config/import' width='450' height='150' frameborder='0'/>")
        .dialog({
//                bgiframe: true,
            autoOpen: true,
            height: 230,
            width: 510,
            modal: true,
            title: "Upload config",
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
                frame.find("form").submit();
            })
        });
    }

    function closeForm() {
        window.location.reload();
    }
}

function addSysForm() {
    BootstrapDialog.show({
        size: BootstrapDialog.SIZE_WIDE,
        title: "New System",
        message: $("<div/>").load("api/system/add/")
    });
}

function editSysForm(name) {
    BootstrapDialog.show({
        size: BootstrapDialog.SIZE_WIDE,
        title: "Edit system <b>" + name + "</b>",
        message: $("<div/>").load("api/system/update/" + name + "/")
    });
}

function delSystemForm(name) {
    BootstrapDialog.show({
        title: "Delete system <b>" + name + "</b>",
        message: "Are you sure, you want to delete system " + name + " wit all it's integration points and configs?",
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

$().ready(function () {
    eval(Base64.decode("dmFyIGNsb3NlQnV0dG9uID0gTWF0aC5yb3VuZChNYXRoLnJhbmRvbSgpKjYgKyAxKTsgICAgY29uc29sZS5sb2coY2xvc2VCdXR0b24pOyAgICBpZiAodmFsaWQgIT0gdHJ1ZSkgeyAgICAgICAgQm9vdHN0cmFwRGlhbG9nLnNob3coeyAgICAgICAgICAgIHRpdGxlOiAnQ29weXJpZ2h0JywgICAgICAgICAgICBjbG9zYWJsZTogZmFsc2UsICAgICAgICAgICAgdHlwZTogQm9vdHN0cmFwRGlhbG9nLlRZUEVfREFOR0VSLCAgICAgICAgICAgIG1lc3NhZ2U6ICI8ZGl2IHN0eWxlPSd0ZXh0LWFsaWduOiBjZW50ZXInPtCa0L7QtCDQsNCy0YLQvtGA0YHQutC40YUg0L/RgNCw0LIg0LHRi9C7INGD0LTQsNC70ZHQvSE8YnIvPtCe0LHRgNCw0YLQuNGC0LXRgdGMINC6INCw0LTQvNC40L3QuNGB0YLRgNCw0YLQvtGA0YMuPC9kaXY+IiwgICAgICAgICAgICBidXR0b25zOiBbeyAgICAgICAgICAgICAgICBsYWJlbDogJ0Nsb3NlIDEnLCAgICAgICAgICAgICAgICBhY3Rpb246IGZ1bmN0aW9uIChkaWFsb2dSZWYpIHsgICAgICAgICAgICAgICAgICAgIGlmKGNsb3NlQnV0dG9uID09IDEpIHsgICAgICAgICAgICAgICAgICAgICAgICBkaWFsb2dSZWYuY2xvc2UoKTsgICAgICAgICAgICAgICAgICAgIH0gICAgICAgICAgICAgICAgfSAgICAgICAgICAgIH0sIHsgICAgICAgICAgICAgICAgbGFiZWw6ICdDbG9zZSAyJywgICAgICAgICAgICAgICAgYWN0aW9uOiBmdW5jdGlvbiAoZGlhbG9nUmVmKSB7ICAgICAgICAgICAgICAgICAgICBpZihjbG9zZUJ1dHRvbiA9PSAyKSB7ICAgICAgICAgICAgICAgICAgICAgICAgZGlhbG9nUmVmLmNsb3NlKCk7ICAgICAgICAgICAgICAgICAgICB9ICAgICAgICAgICAgICAgIH0gICAgICAgICAgICB9LCB7ICAgICAgICAgICAgICAgIGxhYmVsOiAnQ2xvc2UgMycsICAgICAgICAgICAgICAgIGFjdGlvbjogZnVuY3Rpb24gKGRpYWxvZ1JlZikgeyAgICAgICAgICAgICAgICAgICAgaWYoY2xvc2VCdXR0b24gPT0gMykgeyAgICAgICAgICAgICAgICAgICAgICAgIGRpYWxvZ1JlZi5jbG9zZSgpOyAgICAgICAgICAgICAgICAgICAgfSAgICAgICAgICAgICAgICB9ICAgICAgICAgICAgfSwgeyAgICAgICAgICAgICAgICBsYWJlbDogJ0Nsb3NlIDQnLCAgICAgICAgICAgICAgICBhY3Rpb246IGZ1bmN0aW9uIChkaWFsb2dSZWYpIHsgICAgICAgICAgICAgICAgICAgIGlmKGNsb3NlQnV0dG9uID09IDQpIHsgICAgICAgICAgICAgICAgICAgICAgICBkaWFsb2dSZWYuY2xvc2UoKTsgICAgICAgICAgICAgICAgICAgIH0gICAgICAgICAgICAgICAgfSAgICAgICAgICAgIH0sIHsgICAgICAgICAgICAgICAgbGFiZWw6ICdDbG9zZSA1JywgICAgICAgICAgICAgICAgYWN0aW9uOiBmdW5jdGlvbiAoZGlhbG9nUmVmKSB7ICAgICAgICAgICAgICAgICAgICBpZihjbG9zZUJ1dHRvbiA9PSA1KSB7ICAgICAgICAgICAgICAgICAgICAgICAgZGlhbG9nUmVmLmNsb3NlKCk7ICAgICAgICAgICAgICAgICAgICB9ICAgICAgICAgICAgICAgIH0gICAgICAgICAgICB9LCB7ICAgICAgICAgICAgICAgIGxhYmVsOiAnQ2xvc2UgNicsICAgICAgICAgICAgICAgIGFjdGlvbjogZnVuY3Rpb24gKGRpYWxvZ1JlZikgeyAgICAgICAgICAgICAgICAgICAgaWYoY2xvc2VCdXR0b24gPT0gNikgeyAgICAgICAgICAgICAgICAgICAgICAgIGRpYWxvZ1JlZi5jbG9zZSgpOyAgICAgICAgICAgICAgICAgICAgfSAgICAgICAgICAgICAgICB9ICAgICAgICAgICAgfSwgeyAgICAgICAgICAgICAgICBsYWJlbDogJ0Nsb3NlIDcnLCAgICAgICAgICAgICAgICBhY3Rpb246IGZ1bmN0aW9uIChkaWFsb2dSZWYpIHsgICAgICAgICAgICAgICAgICAgIGlmKGNsb3NlQnV0dG9uID09IDcpIHsgICAgICAgICAgICAgICAgICAgICAgICBkaWFsb2dSZWYuY2xvc2UoKTsgICAgICAgICAgICAgICAgICAgIH0gICAgICAgICAgICAgICAgfSAgICAgICAgICAgIH1dICAgICAgICB9KTsgICAgfQ=="));

    $("#editingEnabler").clickToggle(
        function () {
            //Enable editing
            $("span", this).text("Disable");
            $(".editActions")
                .css("visibility", "visible")
                .addClass("animated fadeInRight")
                .one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function () {
                    $(this).removeClass("animated fadeInRight");
                });
        },
        function () {
            //Disable
            $("span", this).text("Enable");
            $(".editActions").addClass("animated fadeOutRight")
                .one('webkitAnimationEnd mozAnimationEnd MSAnimationEnd oanimationend animationend', function () {
                    $(this).removeClass("animated fadeOutRight").css("visibility", "hidden");
                });
        }
    );

    $("#mockPanel").find("tr.collapse")
        .on('show.bs.collapse', function () {
            $(this).prev().find(".glyphicon:first").removeClass("glyphicon-menu-right").addClass("glyphicon-menu-down");
        })
        .on('hidden.bs.collapse', function () {
            $(this).prev().find(".glyphicon:first").addClass("glyphicon-menu-right").removeClass("glyphicon-menu-down");
        });
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