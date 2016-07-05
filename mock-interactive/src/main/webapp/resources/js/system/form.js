/**
 * Created by sbt-bochev-as on 05.07.2016.
 */

$().ready(function () {
    $("#protocol-container").find("a").click(function () {
        $("#protocol").val($(this).html());
    });
});
