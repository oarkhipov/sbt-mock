<%@ page import ="java.util.Map" language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Http mock</title>
</head>
<body>

<%@ page import = "java.util.*" %>
<b>Parameters:</b><br>
<%
  Enumeration parameterList = request.getParameterNames();
  while( parameterList.hasMoreElements() )
  {
    String sName = parameterList.nextElement().toString();
    
      String[] sMultiple = request.getParameterValues( sName );
      if( 1 >= sMultiple.length )
        // parameter has a single value. print it.
        out.println( sName + " = " + request.getParameter( sName ) + "<br>" );
      else
        for( int i=0; i<sMultiple.length; i++ )
          // if a paramater contains multiple values, print all of them
          out.println( sName + "[" + i + "] = " + sMultiple[i] + "<br>" );
  }
%>

</body>
</html>