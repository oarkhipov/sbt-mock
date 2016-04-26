<%--
  Created by IntelliJ IDEA.
  User: sbt-bochev-as
  Date: 25.12.2015
  Time: 17:32
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<form action="/ip/delete/${system}/${integrationPoint}/" method="post">
    <table width="100%">
        <tr>
            <td colspan="2" align="center">
                <h3>Sure?</h3>
            </td>
        </tr>
        <tr>
            <td align="center">
                <input type="submit" value="Yes"/>
            </td>
            <td align="center">
                <input type="button" value="No" id="no"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
