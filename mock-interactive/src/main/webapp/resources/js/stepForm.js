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