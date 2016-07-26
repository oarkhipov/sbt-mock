<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: sbt-bochev-as
  Date: 13.07.2016
  Time: 16:34
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<body>
<form method="post" enctype="multipart/form-data">
    <input id="upload-input" class="file" type="file" name="file" data-min-file-count="1" accept="application/zip">
    <script>
        var input = $('#upload-input');
        input.fileinput({
            uploadUrl: '<%=request.getContextPath()%>/api/system/schema/upload/${system.systemName}/',
            showPreview: false,
            maxFileCount: 1,
            allowedFileExtensions: ['zip']
        });
        input.on('filebatchuploadcomplete', function () {
            window.location.reload();
        });
    </script>
</form>
</body>
</html>
