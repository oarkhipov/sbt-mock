<%--
  Created by IntelliJ IDEA.
  User: sbt-bochev-as
  Date: 13.12.2014
  Time: 14:26
--%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<!--[if lt IE 7]>      <html class="no-js lt-ie9 lt-ie8 lt-ie7"> <![endif]-->
<!--[if IE 7]>         <html class="no-js lt-ie9 lt-ie8"> <![endif]-->
<!--[if IE 8]>         <html class="no-js lt-ie9"> <![endif]-->
<!--[if gt IE 8]><!--> <html class="no-js"> <!--<![endif]-->
<head>
  <meta charset="utf-8">
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
  <title>jQeury.steps Demos</title>
  <meta name="description" content="">
  <meta name="viewport" content="width=device-width">
  <link rel="stylesheet" href="/css/normalize.css">
  <link rel="stylesheet" href="/css/main.css">
  <link rel="stylesheet" href="/css/jquery.steps.css">
  <script src="/lib/modernizr-2.6.2.min.js"></script>
  <script src="/lib/jquery-1.9.1.min.js"></script>
  <script src="/lib/jquery.cookie-1.3.1.js"></script>
  <script src="/lib/jquery.steps.min.js"></script>
</head>
<body>
<!--[if lt IE 7]>
<p class="chromeframe">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> or <a href="http://www.google.com/chromeframe/?redirect=true">activate Google Chrome Frame</a> to improve your experience.</p>
<![endif]-->

<div class="content">
  <script>
    var wizard;
    $(function ()
    {
      wizard = $("#wizard").steps({
        headerTag: "h2",
        bodyTag: "section",
        transitionEffect: "slideLeft",
        onStepChanging: function(event, currentIndex, newIndex) {
          if(currentIndex ==0) {
            if(newIndex == 1) {
              if(int_point===null) {
                alert("Choose integration point!");
                return false;
              }
            }
          }
          return true;
        },
        enablePagination: false
      });

    });

    var int_point = null;

    function chooseIntPoint(obj) {
//      alert(obj.value)
      int_point = obj.value;
      wizard.steps("remove",1);
      wizard.steps("insert",1 ,{
        title: "Response Data",
        contentMode: "async",
        contentUrl: "/mock/" + int_point + "/"
      });
      wizard.steps("next");
    }
  </script>

  <div id="mock">
    <div id="wizard">
      <h2>Integration point</h2>
      <section>
        <select size="25" onchange="chooseIntPoint(this)">
          <c:forEach var="entry" items="${list}">
            <option value="${entry}">${entry}</option>
          </c:forEach>
        </select>
      </section>

      <h2>Response Data</h2>
      <section class="dyn_url" data-mode="async" data-url="/">
      </section>
    </div>
  </div>
</div>
</body>
</html>
