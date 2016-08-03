<%--
  Created by IntelliJ IDEA.
  User: SBT-Bochev-AS
  Date: 01.08.2016
  Time: 18:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Mock settings</title>
</head>
<body>
<form method="post" action="<%=request.getContextPath()%>/config/updateConfigSettings/">
    <div class="form-group">
        <label for="driverTimeout">Driver Timeout:</label>

        <input type="text" class="form-control" name="driverTimeout" id="driverTimeout"
               value="${config.driverTimeout}"
               placeholder="Time in ms"/>
    </div>
    <div class="form-group">
        <label for="maxLogsCount">Maximum log rows in DB:</label>

        <input type="text" class="form-control" name="maxLogsCount" id="maxLogsCount"
               value="${config.maxLogsCount}"
               placeholder="number rows"/>
    </div>
    <div class="form-group">
        <label for="validationEnabled">
            <input type="checkbox" name="validationEnabled"
                   id="validationEnabled"
                   <c:if test="${config.validationEnabled==null || config.validationEnabled}">checked</c:if>/>
            Enable Global message validation
        </label>
    </div>
    <div style="text-align: right">
        <input class="btn btn-success" type="submit" value="SAVE"/>
    </div>
</form>
</body>
</html>
