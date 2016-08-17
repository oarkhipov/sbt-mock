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
  Date: 23.01.2015
  Time: 12:40
--%>

<%@ page import="java.util.Enumeration" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  <title>Frame mock</title>

</head>
<body>
<h3>Frame Mock page.</h3><br/>
<b>Parameters:</b><br>
<%
  Enumeration parameterList = request.getParameterNames();
  while( parameterList.hasMoreElements() )
  {
    String sName = parameterList.nextElement().toString();

    String[] sMultiple = request.getParameterValues( sName );
    if( 1 >= sMultiple.length )
      // parameter has a single value. print it.

      out.println( "<b>" + sName + "</b> = " + request.getParameter( sName ) + "<br>" );
    else
      for( int i=0; i<sMultiple.length; i++ )
        // if a parameter contains multiple values, print all of them
        out.println( "<b>" + sName + "[" + i + "]</b> = " + sMultiple[i] + "<br>" );
  }
%>

</body>
</html>
