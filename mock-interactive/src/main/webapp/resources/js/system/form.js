/**
 * Created by sbt-bochev-as on 05.07.2016.
 */

$().ready(function () {
    $("#protocol-container").find("a").click(function () {
        $("#protocol").val($(this).html());
    });
});

function addElement(namespace, elementName) {
    var div = document.createElement('span');
    div.innerHTML = "<div style='text-align: left'><select class='integrationPointSelectorNamespace' name=\"integrationPointSelectorNamespace[]\" placeholder=\"Namespace\" value=\"" +
    namespace + "\"></select><select class='integrationPointSelectorElementName' name=\"integrationPointSelectorElementName[]\" placeholder=\"Blank element name if it's last\" value=\"" +
    elementName + "\"></select><input class='btn btn-s btn-default' type=\"button\" value=\"-\" onclick='removeElement(this)'/></div>";
    var mainDiv = document.getElementById("integrationPointSelector");
    mainDiv.appendChild(div);
    var elNamespace = $(".integrationPointSelectorNamespace", div).selectize({
        maxItems: 1,
        valueField: 'value',
        labelField: 'value',
        searchField: 'value',
        options: [],
        create: true
    });
    var elName = $(".integrationPointSelectorElementName", div).selectize({
        maxItems: 1,
        valueField: 'value',
        labelField: 'value',
        searchField: 'value',
        options: [],
        create: true
    });
    //TODO link with namespace suggestion service
    if (namespace) {
        var elNamespaceControl = elNamespace[0].selectize;
        elNamespaceControl.addOption({value: namespace});
        elNamespaceControl.setValue(namespace);
    }
    if (elementName) {
        var elNameControl = elName[0].selectize;
        elNameControl.addOption({value: elementName});
        elNameControl.setValue(elementName);
    }
}

function removeElement(obj) {
    var parent = obj.parentElement;
    parent.parentElement.removeChild(parent);
}

function checkJndiName(event, obj) {
    var key = event.keyCode;
    //detect Ctrl, Shift and Alt
    if (key >= 16 && key <= 18) return;
    obj = $(obj);
    var value = obj.val();
    if (value.length > 3) {
        var encodedName = encodeURIComponent(value);
        var glyph = $(".glyphicon", $(obj).parent());

        $.getJSON("api/checkJndi/?name=" + encodedName, function (data) {
            if (data.result) {
                glyph.removeClass("glyphicon-remove-circle text-danger");
                glyph.addClass("glyphicon-ok-circle text-success");
                enableSubmit(obj);
            } else {
                glyph.removeClass("glyphicon-ok-circle text-success")
                glyph.addClass("glyphicon-remove-circle text-danger");
                disableSubmit(obj);
            }
        });
    }
}

var jndiTesterIds = [];
var jndiTesterValues = [];
function enableSubmit(obj) {
    var form = $(obj).closest("form");
    var submit = $("[type=submit]", form);
    var objectId = obj.attr("id");
    var id = jndiTesterIds.indexOf(objectId);
    if (id >= 0) {
        jndiTesterValues[id] = true;
    } else {
        jndiTesterIds.push(objectId);
        jndiTesterValues.push(true);
    }

    if(jndiTesterValues.indexOf(false) == -1) {
        submit.prop("disabled", false);
    }
    console.log(jndiTesterIds);
    console.log(jndiTesterValues);
}
function disableSubmit(obj) {
    var form = $(obj).closest("form");
    var submit = $("[type=submit]", form);
    var objectId = obj.attr("id");
    var id = jndiTesterIds.indexOf(objectId);
    if (id >= 0) {
        jndiTesterValues[id] = false;
    } else {
        jndiTesterIds.push(objectId);
        jndiTesterValues.push(false);
    }
    submit.prop("disabled", true);
    console.log(jndiTesterIds);
    console.log(jndiTesterValues);
}


