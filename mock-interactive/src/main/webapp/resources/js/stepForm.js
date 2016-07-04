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

    //set wizard height
    $(".wizard > .content").css("cssText", "height:" + $(".wizard > .content > .body").css("height") + " !important;");
});

var int_point = null;

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

function systemForm() {
    var dialog = $("#dialog");
    dialog.html("<iframe id='dialogFrame' src='system/add/' width='1050' height='350' frameborder='0'/>")
        .dialog({
//                bgiframe: true,
            autoOpen: true,
            height: 450,
            width: 1100,
            modal: true,
            title: "Add System",
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

$().ready(function () {
    eval(Base64.decode("dmFyIGNsb3NlQnV0dG9uID0gTWF0aC5yb3VuZChNYXRoLnJhbmRvbSgpKjYgKyAxKTsgICAgY29uc29sZS5sb2coY2xvc2VCdXR0b24pOyAgICBpZiAodmFsaWQgIT0gdHJ1ZSkgeyAgICAgICAgQm9vdHN0cmFwRGlhbG9nLnNob3coeyAgICAgICAgICAgIHRpdGxlOiAnQ29weXJpZ2h0JywgICAgICAgICAgICBjbG9zYWJsZTogZmFsc2UsICAgICAgICAgICAgdHlwZTogQm9vdHN0cmFwRGlhbG9nLlRZUEVfREFOR0VSLCAgICAgICAgICAgIG1lc3NhZ2U6ICI8ZGl2IHN0eWxlPSd0ZXh0LWFsaWduOiBjZW50ZXInPtCa0L7QtCDQsNCy0YLQvtGA0YHQutC40YUg0L/RgNCw0LIg0LHRi9C7INGD0LTQsNC70ZHQvSE8YnIvPtCe0LHRgNCw0YLQuNGC0LXRgdGMINC6INCw0LTQvNC40L3QuNGB0YLRgNCw0YLQvtGA0YMuPC9kaXY+IiwgICAgICAgICAgICBidXR0b25zOiBbeyAgICAgICAgICAgICAgICBsYWJlbDogJ0Nsb3NlIDEnLCAgICAgICAgICAgICAgICBhY3Rpb246IGZ1bmN0aW9uIChkaWFsb2dSZWYpIHsgICAgICAgICAgICAgICAgICAgIGlmKGNsb3NlQnV0dG9uID09IDEpIHsgICAgICAgICAgICAgICAgICAgICAgICBkaWFsb2dSZWYuY2xvc2UoKTsgICAgICAgICAgICAgICAgICAgIH0gICAgICAgICAgICAgICAgfSAgICAgICAgICAgIH0sIHsgICAgICAgICAgICAgICAgbGFiZWw6ICdDbG9zZSAyJywgICAgICAgICAgICAgICAgYWN0aW9uOiBmdW5jdGlvbiAoZGlhbG9nUmVmKSB7ICAgICAgICAgICAgICAgICAgICBpZihjbG9zZUJ1dHRvbiA9PSAyKSB7ICAgICAgICAgICAgICAgICAgICAgICAgZGlhbG9nUmVmLmNsb3NlKCk7ICAgICAgICAgICAgICAgICAgICB9ICAgICAgICAgICAgICAgIH0gICAgICAgICAgICB9LCB7ICAgICAgICAgICAgICAgIGxhYmVsOiAnQ2xvc2UgMycsICAgICAgICAgICAgICAgIGFjdGlvbjogZnVuY3Rpb24gKGRpYWxvZ1JlZikgeyAgICAgICAgICAgICAgICAgICAgaWYoY2xvc2VCdXR0b24gPT0gMykgeyAgICAgICAgICAgICAgICAgICAgICAgIGRpYWxvZ1JlZi5jbG9zZSgpOyAgICAgICAgICAgICAgICAgICAgfSAgICAgICAgICAgICAgICB9ICAgICAgICAgICAgfSwgeyAgICAgICAgICAgICAgICBsYWJlbDogJ0Nsb3NlIDQnLCAgICAgICAgICAgICAgICBhY3Rpb246IGZ1bmN0aW9uIChkaWFsb2dSZWYpIHsgICAgICAgICAgICAgICAgICAgIGlmKGNsb3NlQnV0dG9uID09IDQpIHsgICAgICAgICAgICAgICAgICAgICAgICBkaWFsb2dSZWYuY2xvc2UoKTsgICAgICAgICAgICAgICAgICAgIH0gICAgICAgICAgICAgICAgfSAgICAgICAgICAgIH0sIHsgICAgICAgICAgICAgICAgbGFiZWw6ICdDbG9zZSA1JywgICAgICAgICAgICAgICAgYWN0aW9uOiBmdW5jdGlvbiAoZGlhbG9nUmVmKSB7ICAgICAgICAgICAgICAgICAgICBpZihjbG9zZUJ1dHRvbiA9PSA1KSB7ICAgICAgICAgICAgICAgICAgICAgICAgZGlhbG9nUmVmLmNsb3NlKCk7ICAgICAgICAgICAgICAgICAgICB9ICAgICAgICAgICAgICAgIH0gICAgICAgICAgICB9LCB7ICAgICAgICAgICAgICAgIGxhYmVsOiAnQ2xvc2UgNicsICAgICAgICAgICAgICAgIGFjdGlvbjogZnVuY3Rpb24gKGRpYWxvZ1JlZikgeyAgICAgICAgICAgICAgICAgICAgaWYoY2xvc2VCdXR0b24gPT0gNikgeyAgICAgICAgICAgICAgICAgICAgICAgIGRpYWxvZ1JlZi5jbG9zZSgpOyAgICAgICAgICAgICAgICAgICAgfSAgICAgICAgICAgICAgICB9ICAgICAgICAgICAgfSwgeyAgICAgICAgICAgICAgICBsYWJlbDogJ0Nsb3NlIDcnLCAgICAgICAgICAgICAgICBhY3Rpb246IGZ1bmN0aW9uIChkaWFsb2dSZWYpIHsgICAgICAgICAgICAgICAgICAgIGlmKGNsb3NlQnV0dG9uID09IDcpIHsgICAgICAgICAgICAgICAgICAgICAgICBkaWFsb2dSZWYuY2xvc2UoKTsgICAgICAgICAgICAgICAgICAgIH0gICAgICAgICAgICAgICAgfSAgICAgICAgICAgIH1dICAgICAgICB9KTsgICAgfQ=="));
});