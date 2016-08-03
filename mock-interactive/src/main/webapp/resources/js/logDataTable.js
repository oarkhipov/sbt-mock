/**
 * Created by sbt-bochev-as on 01.07.2016.
 */
var table;
var tooltipVisible = false;
$(document).ready(function () {

    table = $('#example').DataTable({
        "ajax": "data.web",
        "bProcessing": true,
        "bServerSide": true,
        <!--"sort": "position",-->
        //bStateSave variable you can use to save state on client cookies: set value "true"
        "bStateSave": false,
        //Default: Page display length
        "iDisplayLength": 10,
        //We will use below variable to track page number on server side(For more information visit: http://legacy.datatables.net/usage/options#iDisplayStart)
        "iDisplayStart": 0,
        "search": {
            "regex": true
        },
        "order": [[0, "desc"],[1, "desc"]],
        "fnDrawCallback": function () {
            //Get page number on client. Please note: number start from 0 So
            //for the first page you will see 0 second page 1 third page 2...
            //Un-comment below alert to see page number
        },
        initComplete: function () {
            this.api().columns().every(function () {
                var column = this;
                if (this.index() > 1) {
                    var select = $('<select><option value=""></option></select>')
                        .appendTo($(column.footer()).empty())
                        .on('change', function () {
                            var val = $.fn.dataTable.util.escapeRegex(
                                $(this).val()
                            );

                            column
                                .search(val ? '^' + val + '$' : '', true, false)
                                .draw();
                        });

                    column.data().unique().sort().each(function (d, j) {
                        select.append('<option value="' + d + '">' + d + '</option>')
                    });
                }
            });
        },
        "aoColumns": [
            {"mData": "ts", "bSearchable": false},
            {"mData": "transactionId"},
            {"mData": "protocol"},
            {"mData": "systemName"},
            {"mData": "integrationPointName"},
            {"mData": "fullEndpoint"},
            {"mData": "shortEndpoint"},
            {"mData": "messageState"}
        ]
    });

    var tableElement = $("#example tbody");

    tableElement.on('click', 'tr', function () {
        var row = table.row(this).data();
        var ts = encodeURIComponent(row.ts);
        hideTooltip();
        if (ts) {
            $.ajax({
                url: "../api/log/getMessage/" + ts + "/",
                success: function (data) {
                    BootstrapDialog.show({
                        title: row.systemName + " " + row.integrationPointName + " (" + row.messageState + ") message",
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
                            editor.setValue(vkbeautify.xml(htmlDecode(data)), 1);
                            editor.renderer.$cursorLayer.element.style.display = "none";
                            editor.getSession().setUseWrapMode(true);
                            //for errors markup
                            // var Range = ace.require('ace/range').Range;
                            // editor.getSession().addMarker(new Range(1,0,1,200), "errorMarker", "line")
                        }
                    });

                }
            });
        }
    });

    $(tableElement).hoverIntent({
        over: showTooltip,
        out: hideTooltip,
        selector: 'tr',
        interval: 1000
    });

    function showTooltip() {
        tooltipVisible = true;
        var tooltip = $("#tooltip");
        var data = table.row(this).data();
        var ts = encodeURIComponent(data.ts);
        if (ts && (ts != tooltip.attr("data-ts"))) {
            tooltip.attr('title', '<span class="glyphicon-refresh-animate glyphicon-refresh glyphicon"></span> Loading...</div>')
                .attr('data-original-title', '<span class="glyphicon-refresh-animate glyphicon-refresh glyphicon"></span> Loading...')
                .attr('data-ts', ts);
            $.ajax({
                url: "../api/log/getMessage/" + ts + "/",
                success: function (data) {
                    var xml = htmlEncode(vkbeautify.xml(htmlDecode(data)));
                    tooltip.attr('title', "")
                        .attr('data-original-title', "<div id=\"tooltipBody\" style='text-align: left'>" + xml + "</div>");
                    $('[data-toggle="tooltip"]').tooltip('show')
                }
            });
        }
    }

    function hideTooltip() {
        tooltipVisible = false;
        $('[data-toggle="tooltip"]').tooltip('hide')
    }

    tableElement.on('mousemove', 'tr', function (e) {
        var tooltip = $("#tooltip");
        tooltip.css({top: e.pageY, left: e.pageX - 80});
        if  (tooltipVisible) {
            $('[data-toggle="tooltip"]').tooltip('show')
        }

    });
});

function htmlDecode(text) {
    return $('<div/>').html(text).text();
}

function htmlEncode(text) {
    return $('<div/>').text(text).html();
}