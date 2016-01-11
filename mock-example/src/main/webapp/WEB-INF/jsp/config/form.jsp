<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: sbt-bochev-as
  Date: 23.12.2015
  Time: 17:18
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<form method="post" enctype="multipart/form-data">
    <table width="100%">
        <tr>
            <td>Config zip:</td>
            <td>
                <input type="file" accept="application/zip" name="file"/>
            </td>
        </tr>
        <tr>
            <td colspan="2" align="right" style="padding-top: 15px">
                <input type="submit" value="Upload"/>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
