<%--
  Created by IntelliJ IDEA.
  User: sbt-bochev-as
  Date: 22.05.2015
  Time: 11:55
  To change this template use File | Settings | File Templates.
--%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]> <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]> <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<!DOCTYPE html>
<!--[if lt IE 7]> <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]> <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]> <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!-->
<html class="no-js"> <!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>Mock Service</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width">
    <link rel="stylesheet" href="../css/main.css">
    <link rel="stylesheet" href="../css/normalize.css">
    <script src="../js/modernizr-2.6.2.min.js"></script>
    <script src="../js/jquery-1.9.1.min.js"></script>
    <script src="../js/jquery.cookie-1.3.1.js"></script>

    <!-- Tooltip& -->
    <link rel="stylesheet" href="../css/jquery-ui.css">
    <script src="../js/jquery-ui.min.js"></script>
    <script src="../js/jquery-ui.js"></script>

    <!-- Data Tables -->
    <link rel="stylesheet" type="text/css" href="../css/DataTables-1.10.9/css/dataTables.jqueryui.css"/>
    <link rel="stylesheet" type="text/css" href="../css/DataTables-1.10.9/css/jquery.dataTables.css"/>

    <link rel="stylesheet" type="text/css" href="../libs/bootstrap/css/bootstrap.css"/>
    <link rel="stylesheet" type="text/css" href="../libs/bootstrap-dialog/css/bootstrap-dialog.css"/>

    <script type="text/javascript" src="../js/DataTables-1.10.9/js/jquery.dataTables.js"></script>
    <script type="text/javascript" src="../js/DataTables-1.10.9/js/jquery.dataTables.js"></script>
    <script type="text/javascript" src="../js/DataTables-1.10.9/js/dataTables.jqueryui.js"></script>
    <script type="text/javascript" src="../js/AutoFill-2.0.0/js/dataTables.autoFill.js"></script>
    <script type="text/javascript" src="../js/AutoFill-2.0.0/js/autoFill.jqueryui.js"></script>

    <script type="text/javascript" src="../js/jquery.hoverIntent.js"></script>
    <script type="text/javascript" src="../libs/bootstrap/js/bootstrap.js"></script>
    <script type="text/javascript" src="../libs/bootstrap-dialog/js/bootstrap-dialog.js"></script>


</head>
<body>
<!--[if lt IE 7]>
<p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade
    your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to
    improve your experience.</p>
<![endif]-->

<!--[if IE]>
<style type="text/css">
    #info {
        background: transparent;
        zoom: 1;
        /*rgba(121, 255, 120, .7)*/
        /* IE8 */
        filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#B379FF78, endColorstr=#B379FF78);
        /*-ms-filter: "progid:DXImageTransform.Microsoft.Alpha(Opacity=0)";*/
        /* IE 5-7 */
        /*filter: alpha(opacity=0)*/
    }

    #error {
        background: transparent;
        zoom: 1;
        /*rgba(256, 182, 193, .7)*/
        filter: progid:DXImageTransform.Microsoft.gradient(startColorstr=#B3FFB6C1, endColorstr=#B3FFB6C1);
    }
</style>
<![endif]-->
<style>
    td {
        word-wrap: break-word;
    }

    .glyphicon-refresh-animate {
        -webkit-animation: spin2 .7s infinite linear;
        -o-animation: spin .7s infinite linear;
        animation: spin .7s infinite linear;
    }

    @-webkit-keyframes spin2 {
        from {
            -webkit-transform: rotate(0deg);
        }
        to {
            -webkit-transform: rotate(360deg);
        }
    }

    @keyframes spin {
        from {
            transform: scale(1) rotate(0deg);
        }
        to {
            transform: scale(1) rotate(360deg);
        }
    }

    .tooltip-inner {
        max-width: 600px;
    }
</style>

<input type="button" value="BACK" onclick="window.location.href='../'"/>

<div id="tooltip" data-toggle="tooltip" data-placement="right" data-animation="false" data-trigger="manual"
     data-html="true" title="Tooltip text" style="position:absolute;"></div>
<script>
    var table;
    $(document).ready(function () {

        table = $('#example').DataTable({
            "ajax": "data.web",
            "bProcessing": true,
            "bServerSide": true,
            <!--"sort": "position",-->
            //bStateSave variable you can use to save state on client cookies: set value "true"
            "bStateSave": true,
            //Default: Page display length
            "iDisplayLength": 10,
            //We will use below variable to track page number on server side(For more information visit: http://legacy.datatables.net/usage/options#iDisplayStart)
            "iDisplayStart": 0,
            "search": {
                "regex": true
            },
            "fnDrawCallback": function () {
                //Get page number on client. Please note: number start from 0 So
                //for the first page you will see 0 second page 1 third page 2...
                //Un-comment below alert to see page number
            },
            initComplete: function () {
                this.api().columns().every(function () {
                    var column = this;
                    if (this.index() != 0) {
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
            if (ts) {
                $.ajax({
                    url: "../api/log/getMessage/" + ts + "/",
                    success: function (data) {
                        $('[data-toggle="tooltip"]').tooltip('hide');
                        BootstrapDialog.show({
                            title: row.systemName + " " + row.integrationPointName + " (" + row.messageState + ") message",
                            message: data
                        });
                    }
                });
            }
        });

        $(tableElement).hoverIntent({
            over: showTooltip,
            out: hideTooltip,
            selector: 'tr',
            interval: 200
        });
        function showTooltip() {
            tooltipVisible = true;
            var tooltip = $("#tooltip");
            var data = table.row(this).data();
            var ts = encodeURIComponent(data.ts);
            if (ts && (ts != tooltip.attr("data-ts"))) {
                tooltip.attr('title', '<span class="glyphicon-refresh-animate glyphicon-refresh glyphicon"></span> Loading...')
                        .attr('data-original-title', '<span class="glyphicon-refresh-animate glyphicon-refresh glyphicon"></span> Loading...')
                        .attr('data-ts', ts);
                $.ajax({
                    url: "../api/log/getMessage/" + ts + "/",
                    success: function (data) {
                        tooltip.attr('title', data)
                                .attr('data-original-title', data);
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
            tooltip.css({top: e.pageY, left: e.pageX + 5});
            if  (tooltipVisible) {
                $('[data-toggle="tooltip"]').tooltip('show')
            }

        });

    });
</script>
<table style="border: 3px;background: rgb(243, 244, 248); width: 800px">
    <tr>
        <td>
            <table id="example" class="display" cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th class="sorting">TimeStamp</th>
                    <th class="sorting">Protocol</th>
                    <th class="sorting">SystemName</th>
                    <th class="sorting">IntegrationPointName</th>
                    <th class="sorting">FullEndpoint</th>
                    <th class="sorting">ShortEndpoint</th>
                    <th class="sorting">MessageState</th>
                </tr>
                </thead>
                <tfoot>
                <tr>
                    <th>TimeStamp</th>
                    <th>Protocol</th>
                    <th>SystemName</th>
                    <th>IntegrationPointName</th>
                    <th>FullEndpoint</th>
                    <th>ShortEndpoint</th>
                    <th>MessageState</th>
                </tr>
                </tfoot>

            </table>
        </td>
    </tr>
</table>


</body>
</html>
