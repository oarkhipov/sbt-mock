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


