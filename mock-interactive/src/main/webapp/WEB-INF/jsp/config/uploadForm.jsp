<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  ~ Copyright (c) 2016, Sberbank and/or its affiliates. All rights reserved.
  ~
  ~ Redistribution and use in source and binary forms, with or without
  ~ modification, are permitted provided that the following conditions
  ~ are met:
  ~
  ~   - Redistributions of source code must retain the above copyright
  ~     notice, this list of conditions and the following disclaimer.
  ~
  ~   - Redistributions in binary form must reproduce the above copyright
  ~     notice, this list of conditions and the following disclaimer in the
  ~     documentation and/or other materials provided with the distribution.
  ~
  ~   - Neither the name of Sberbank or the names of its
  ~     contributors may be used to endorse or promote products derived
  ~     from this software without specific prior written permission.
  ~
  ~ THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
  ~ IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
  ~ THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
  ~ PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
  ~ CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
  ~ EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
  ~ PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
  ~ PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
  ~ LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
  ~ NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
  ~ SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  --%>

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
