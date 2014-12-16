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
    $(function () {
      wizard = $("#wizard").steps({
        headerTag: "h2",
        bodyTag: "section",
        transitionEffect: "slideLeft",
        transitionEffectSpeed: 400,
        onStepChanging: function(event, currentIndex, newIndex) {
          if(currentIndex == 0) {
            if(newIndex == 1) {
              if(int_point===null) {
                alert("Choose integration point!");
                return false;
              }
            }
          }
          if(currentIndex == 1) {
            if(newIndex == 0) {
              history.pushState({}, "Integration points", "?");
            }
          }
          return true;
        },
        enablePagination: false
      });

      //if integration point was chosen
      if(QueryString["ip"]!=undefined) {
        int_point=QueryString["ip"];
        selectIp();
      }
    });

    var int_point = null;

    function chooseIntPoint(obj) {
//      alert(obj.value)
      int_point = obj.value;
      selectIp();
    }

    function selectIp() {
      wizard.steps("remove",1);
      wizard.steps("insert",1 ,{
        title: "<c:out value="${type}"/> Data",
        contentMode: "async",
        contentUrl: "/<c:out value="${link}"/>/" + int_point + "/"
      });
      history.pushState({ip:int_point}, int_point, "?ip="+int_point);
      wizard.steps("next");
    }

    var QueryString = function () {
      // This function is anonymous, is executed immediately and
      // the return value is assigned to QueryString!
      var query_string = {};
      var query = window.location.search.substring(1);
      var vars = query.split("&");
      for (var i=0;i<vars.length;i++) {
        var pair = vars[i].split("=");
        // If first entry with this name
        if (typeof query_string[pair[0]] === "undefined") {
          query_string[pair[0]] = pair[1];
          // If second entry with this name
        } else if (typeof query_string[pair[0]] === "string") {
          var arr = [ query_string[pair[0]], pair[1] ];
          query_string[pair[0]] = arr;
          // If third or later entry with this name
        } else {
          query_string[pair[0]].push(pair[1]);
        }
      }
      return query_string;
    } ();

  </script>

  <div id="mock">
    <div id="wizard">
      <h2>Integration point</h2>
      <section>
        <input type="button" value="BACK" onclick="window.location.href='../'"/>
        <span style="line-height: 5pt; display: block">&nbsp;</span>
        <select size="25" onchange="chooseIntPoint(this)">
          <c:forEach var="entry" items="${list}">
            <option value="${entry}">${entry}</option>
          </c:forEach>
        </select>
      </section>

      <h2><c:out value="${type}"/> Data</h2>
      <section class="dyn_url" data-mode="async" data-url="/" style="overflow: scroll">
      </section>
    </div>
  </div>
</div>
</body>
</html>
