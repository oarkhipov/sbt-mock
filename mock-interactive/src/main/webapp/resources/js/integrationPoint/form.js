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
