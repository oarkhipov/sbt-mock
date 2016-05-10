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
    <link rel="stylesheet" type="text/css" href="../css/jquery.tooltip.css"/>

    <script type="text/javascript" src="../js/DataTables-1.10.9/js/jquery.dataTables.js"></script>
    <script type="text/javascript" src="../js/DataTables-1.10.9/js/jquery.dataTables.js"></script>
    <script type="text/javascript" src="../js/DataTables-1.10.9/js/dataTables.jqueryui.js"></script>
    <script type="text/javascript" src="../js/AutoFill-2.0.0/js/dataTables.autoFill.js"></script>
    <script type="text/javascript" src="../js/AutoFill-2.0.0/js/autoFill.jqueryui.js"></script>
    <script type="text/javascript" src="../js/jquery.tooltip.js"></script>


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
    td{
        word-wrap: break-word;
    }
</style>
<![endif]-->


<input type="button" value="BACK" onclick="window.location.href='../'"/>
<div id="tooltip" style="display: none"></div>
<script>
    var table;
    $(document).ready(function() {

        table=$('#example').DataTable( {
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
            "fnDrawCallback": function () {
                //Get page number on client. Please note: number start from 0 So
                //for the first page you will see 0 second page 1 third page 2...
                //Un-comment below alert to see page number
                //alert("Current page number: "+this.fnPagingInfo().iPage);
                $("#example tbody tr").each( function() {
                    var nTds = $('td',this);
//            this.setAttribute("title", $(nTds[2]).text());
                    this.setAttribute("title", '123');
                });
            },
            initComplete: function () {
                this.api().columns().every( function () {
                    var column = this;
                    if (this.index() != 0)
                    {
                        var select = $('<select><option value=""></option></select>')
                                .appendTo( $(column.footer()).empty() )
                                .on( 'change', function () {
                                    var val = $.fn.dataTable.util.escapeRegex(
                                            $(this).val()
                                    );

                                    column
                                            .search( val ? '^'+val+'$' : '', true, false )
                                            .draw();
                                } );

                        column.data().unique().sort().each( function ( d, j ) {
                            select.append( '<option value="'+d+'">'+d+'</option>' )
                        } );
                    }
                } );
            },
            "aoColumns":[
                { "mData": "ts","bSearchable":false},
                { "mData": "protocol"},
                { "mData": "systemName"},
                { "mData": "integrationPointName"},
                { "mData": "fullEndpoint"},
                { "mData": "shortEndpoint"},
                { "mData": "messageState"}
            ]
        } );

        var tableElement = $("#example tbody");

        tableElement.on('click', 'tr', function() {
            var data = table.row(this).data();
            var ts = encodeURIComponent(data.ts);
            $.ajax({
                url: "../api/log/getMessage/" + ts + "/",
                success: function (data) {
                    alert(data);
                }
            });
        });

        table.$('tr').tooltip({
            "delay": 0,
            html: true,
            placement: '',
            "fade": 250
        });
    });
</script>
<table style="border: 3px;background: rgb(243, 244, 248); width: 800px"><tr><td>
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
</td></tr></table>


</body>
</html>
