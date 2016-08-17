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

$().ready(function () {

    applySelectize($("#rootElementDiv"));

});

// Dataloaders for selectize
var namespaceLoader = function (query, callback) {
    if (!query.length) return callback();
    var systemName = $("#system").val();
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

function applySelectize(parent) {
    var namespaceSelect = $(".selectorNamespace", parent).selectize({
        persist: false,
        maxItems: 1,
        valueField: 'value',
        labelField: 'value',
        searchField: 'value',
        options: [],
        create: true,
        load: namespaceLoader
    });

    $(".selectorElementName", parent).selectize({
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
            var systemName = $("#system").val();
            if (systemName) {
                $.ajax({
                    url: 'api/system/' + systemName + '/elements/?namespace=' + namespaceSelect[0].selectize.getValue() + '&elementName=' + query,
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
            $(".selectize-dropdown-content", parent).on('mousedown', 'div[data-selectable]', function (event) {
                var namespace = $(this).find(".selectize-caption").text();
                if (namespace) {
                    var namespaceControl = namespaceSelect[0].selectize;
                    namespaceControl.addOption({value: namespace});
                    namespaceControl.setValue(namespace);
                }
            });
        }
    });
}
