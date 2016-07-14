/**
 * Created by sbt-bochev-as on 05.07.2016.
 */

$().ready(function () {
    $("#protocol-container").find("a").click(function () {
        $("#protocol").val($(this).html());
    });

    $(".rootElementNamespace").selectize({
        persist: false,
        maxItems: 1,
        valueField: 'value',
        labelField: 'value',
        searchField: 'value',
        options: [],
        create: true,
        load: namespaceLoader
    });

    $(".rootElementName").selectize({
        persist: false,
        maxItems: 1,
        valueField: 'elementName',
        labelField: 'elementName',
        searchField: 'elementName',
        options: [],
        sortField: [
            {field: 'elementName', direction: 'asc'}
        ],
        create: true,
        render: {
            option: function (item, escape) {
                return '<div>' +
                    '<span class="selectize-label">' + escape(item.elementName) + '</span><br/>' +
                    '<span class="selectize-caption">' + escape(item.namespace) + '</span>' +
                    '</div>';
            }
        },
        load: function (query, callback) {
            if (!query.length) return callback();
            var systemName = $("#name").val();
            if (systemName) {
                $.ajax({
                    url: 'api/system/' + systemName + '/elements/?namespace=' + $(".rootElementNamespace")[0].selectize.getValue() + '&elementName=' + query,
                    type: 'GET',
                    dataType: 'json',
                    error: function () {
                        callback();
                    },
                    success: function (res) {
                        callback(res);
                    }
                });
            }
        },
        onDropdownOpen: function () {
            $(".selectize-dropdown-content").on('mousedown', 'div[data-selectable]', function (event) {
                var namespace = $(this).find(".selectize-caption").text();
                if (namespace) {
                    var namespaceControl = $(".rootElementNamespace")[0].selectize;
                    namespaceControl.addOption({value: namespace});
                    namespaceControl.setValue(namespace);
                }
            });
        }
    });
});

function addElement(namespace, elementName) {
    var div = document.createElement('span');
    div.innerHTML = "<div style='text-align: left'><select class='integrationPointSelectorNamespace' name=\"integrationPointSelectorNamespace[]\" placeholder=\"Namespace\"></select>" +
    "<select class='integrationPointSelectorElementName' name=\"integrationPointSelectorElementName[]\" placeholder=\"Blank element name if it's last\"></select>" +
    "<input class='btn btn-s btn-default' type=\"button\" value=\"-\" onclick='removeElement(this)'/></div>";
    var mainDiv = document.getElementById("integrationPointSelector");
    mainDiv.appendChild(div);

    var elNamespace = $(".integrationPointSelectorNamespace", div).selectize({
        persist: false,
        maxItems: 1,
        valueField: 'value',
        labelField: 'value',
        searchField: 'value',
        options: [],
        create: true,
        load: namespaceLoader
    });
    var elName = $(".integrationPointSelectorElementName", div).selectize({
        persist: false,
        maxItems: 1,
        valueField: 'elementName',
        labelField: 'elementName',
        searchField: 'elementName',
        options: [],
        sortField: [
            {field: 'elementName', direction: 'asc'}
        ],
        create: true,
        render: {
            option: function (item, escape) {
                return '<div>' +
                    '<span class="selectize-label">' + escape(item.elementName) + '</span><br/>' +
                    '<span class="selectize-caption">' + escape(item.namespace) + '</span>' +
                    '</div>';
            }
        },
        load: function (query, callback) {
            if (!query.length) return callback();
            var systemName = $("#name").val();
            if (systemName) {
                $.ajax({
                    url: 'api/system/' + systemName + '/elements/?namespace=' + elNamespace[0].selectize.getValue() + '&elementName=' + query,
                    type: 'GET',
                    dataType: 'json',
                    error: function () {
                        callback();
                    },
                    success: function (res) {
                        callback(res);
                    }
                });
            }
        },
        onDropdownOpen: function () {
            $(".selectize-dropdown-content").on('mousedown', 'div[data-selectable]', function (event) {
                var namespace = $(this).find(".selectize-caption").text();
                if (namespace) {
                    elNamespace[0].selectize.addOption({value: namespace});
                    elNamespace[0].selectize.setValue(namespace);
                }
            });
        }
    });
    //TODO link with namespace suggestion service
    if (namespace) {
        var elNamespaceControl = elNamespace[0].selectize;
        elNamespaceControl.addOption({value: namespace});
        elNamespaceControl.setValue(namespace);
    }
    if (elementName) {
        var elNameControl = elName[0].selectize;
        elNameControl.addOption({namespace: namespace, elementName: elementName});
        elNameControl.setValue(elementName);
    }
}

function removeElement(obj) {
    var parent = obj.parentElement;
    parent.parentElement.removeChild(parent);
}

// Dataloaders for selectize
var namespaceLoader = function (query, callback) {
    if (!query.length) return callback();
    var systemName = $("#name").val();
    if (systemName) {
        $.ajax({
            url: 'api/system/' + systemName + '/namespaces/?namespace=' + query,
            type: 'GET',
            dataType: 'json',
            error: function () {
                callback();
            },
            success: function (res) {
                callback(res);
            }
        });
    }
};

// Selectize END==============

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
                glyph.removeClass("glyphicon-ok-circle text-success");
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

    if (jndiTesterValues.indexOf(false) == -1) {
        submit.prop("disabled", false);
    }
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
}


