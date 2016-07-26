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
    <input id="upload-input" class="file" type="file" name="file" data-min-file-count="1" accept="application/zip">
    <script>
        var input = $('#upload-input');
        input.fileinput({
            uploadUrl: '<%=request.getContextPath()%>/config/import',
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
