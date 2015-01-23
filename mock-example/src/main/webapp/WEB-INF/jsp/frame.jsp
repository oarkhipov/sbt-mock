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
