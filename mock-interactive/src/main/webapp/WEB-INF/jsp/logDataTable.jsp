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
<html class="no-js"> <!--<![endif]-->
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title>Mock Service</title>
    <meta name="description" content="">
    <meta name="viewport" content="width=device-width">
    <link rel="stylesheet" href="../css/main.css">
    <link rel="stylesheet" href="../css/normalize.css">
    <link rel="stylesheet" href="../css/jquery.steps.css">
    <script src="../js/modernizr-2.6.2.min.js"></script>
    <script src="../js/jquery-1.9.1.min.js"></script>
    <script src="../js/jquery.cookie-1.3.1.js"></script>
    <script src="../js/jquery.steps.min.js"></script>
    <script src="../js/jquery.history.js"></script>
    <script src="../js/attrs.js"></script>

    <%--Tooltip--%>
    <link rel="stylesheet" href="../css/jquery-ui.css">
    <script src="../js/jquery-ui.min.js"></script>

    <%--Data Tables --%>
    <link rel="stylesheet" type="text/css" href="../css/DataTables-1.10.9/css/dataTables.jqueryui.css"/>
    <link rel="stylesheet" type="text/css" href="../css/AutoFill-2.0.0/css/autoFill.jqueryui.min.css"/>
    <link rel="stylesheet" type="text/css" href="../css/FixedHeader-3.0.0/css/fixedHeader.jqueryui.css"/>

    <script type="text/javascript" src="../js/DataTables-1.10.9/js/jquery.dataTables.js"></script>
    <script type="text/javascript" src="../js/DataTables-1.10.9/js/dataTables.jqueryui.js"></script>
    <script type="text/javascript" src="../js/AutoFill-2.0.0/js/dataTables.autoFill.js"></script>
    <script type="text/javascript" src="../js/AutoFill-2.0.0/js/autoFill.jqueryui.js"></script>
    <script type="text/javascript" src="../js/FixedHeader-3.0.0/js/dataTables.fixedHeader.js"></script>


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

<div class="content">
    <script>
        var wizard;
        $(function () {
            wizard = $("#wizard").steps({
                headerTag: "h2",
                bodyTag: "section",
                enablePagination: false
            });
        });

        jQuery.fn.dataTableExt.oApi.fnPagingInfo = function ( oSettings )
        {
            return {
                "iStart":         oSettings._iDisplayStart,
                "iEnd":           oSettings.fnDisplayEnd(),
                "iLength":        oSettings._iDisplayLength,
                "iTotal":         oSettings.fnRecordsTotal(),
                "iFilteredTotal": oSettings.fnRecordsDisplay(),
                "iPage":          oSettings._iDisplayLength === -1 ?
                        0 : Math.ceil( oSettings._iDisplayStart / oSettings._iDisplayLength ),
                "iTotalPages":    oSettings._iDisplayLength === -1 ?
                        0 : Math.ceil( oSettings.fnRecordsDisplay() / oSettings._iDisplayLength )
            };
        };

    </script>
    <div id="mock">
        <div id="wizard">
            <h2><c:out value="${title}" escapeXml="true"/></h2>
            <section>
                <input type="button" value="BACK" onclick="window.location.href='../'"/>
                <script>
                    $(document).ready(function() {
                        $('#example').DataTable( {
                            "bProcessing": true,
                            "bServerSide": true,
                            "sort": "position",
                            //bStateSave variable you can use to save state on client cookies: set value "true"
                            "bStateSave": false,
                            //Default: Page display length
                            "iDisplayLength": 10,
                            //We will use below variable to track page number on server side(For more information visit: http://legacy.datatables.net/usage/options#iDisplayStart)
                            "iDisplayStart": 0,
                            "fnDrawCallback": function () {
                                //Get page numer on client. Please note: number start from 0 So
                                //for the first page you will see 0 second page 1 third page 2...
                                //Un-comment below alert to see page number
                                //alert("Current page number: "+this.fnPagingInfo().iPage);
                            },
                            "sAjaxSource": "springPaginationDataTables.web",
                            "aoColumns": [
                                { "mData": "timestamp" },
                                { "mData": "queue" },
                                { "mData": "msgtype" },
                                { "mData": "payload" }
                            ]
                        } );
                    });
                </script>
                <table width="70%" style="border: 3px;background: rgb(243, 244, 248);"><tr><td>
                    <table id="example" class="display" cellspacing="0" width="100%">
                        <thead>
                        <tr>
                            <th>TimeStamp</th>
                            <th>Queue</th>
                            <th>Message Type</th>
                            <th>Data</th>
                        </tr>
                        </thead>
                    </table>
                </td></tr></table>

            </section>
        </div>
    </div>

    <script>
    </script>
</div>
</body>
</html>
