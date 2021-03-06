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
 * Created by sbt-bochev-as on 01.07.2016.
 */
var table;
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
        "order": [[0, "desc"], [1, "desc"]],
        "fnDrawCallback": function () {
            //Get page number on client. Please note: number start from 0 So
            //for the first page you will see 0 second page 1 third page 2...
            //Un-comment below alert to see page number
            setTransactionHandlers();
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
        ],
        "fnRowCallback": function (nRow, aData, iDisplayIndex) {
            $('td:eq(1)', nRow).html("<a href=# class='transactionFilter'>" + aData.transactionId + "</a>");
            return nRow;
        }
    });

    var tableElement = $("#example tbody");

    tableElement.on('click', 'tr', function () {
        var row = table.row(this).data();
        var ts = encodeURIComponent(row.ts);
        if (ts) {
            $.ajax({
                url: "../api/log/getMessage/" + ts + "/",
                success: function (data) {
                    BootstrapDialog.show({
                        size: BootstrapDialog.SIZE_WIDE,
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
                        }
                    });

                }
            });
        }
    });
});

function htmlDecode(text) {
    return $('<div/>').html(text).text();
}

function setTransactionHandlers() {
    $(".transactionFilter").click(function (event) {
        event.preventDefault();
        var value = $(this).html();
        table.search(value).draw();
        return false;
    });
}