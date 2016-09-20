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
  Date: 13.12.2014
  Time: 16:55
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8">
</head>
<body>
<table>
    <tr>
        <td>
            <div style="font-size: 80%; color: dimgray">
                <div>
                    <b>System: </b> <i><c:out value="${systemName}"/></i>&nbsp;&nbsp;<span class="sup label label-<c:if test="${protocol eq 'JMS'}">warning</c:if><c:if test="${protocol eq 'SOAP'}">info</c:if> ">${protocol}</span>
                </div>
                <div>
                    <b>Integration point:</b> <i><c:out value="${name}"/></i>
                </div>
                <%--WAS 7 compatible style (legacy)--%>
                <c:if test='${protocol eq "JMS"}'>
                    <c:if test="${link eq 'mock' || fn:length(xpath)  gt 0}">
                        <div>
                            <b>Xpath assertion:</b> <i><c:out value="${xpath}"/></i>
                        </div>
                    </c:if>
                </c:if>
                <c:if test="${template ne null}">
                    <div>
                        <input type="hidden" id="templateId" value="${template.templateId}">
                        <b>MessageTemplate:</b> ${template.caption}<c:if test="${template.value}"> (${template.value})</c:if>
                    </div>
                </c:if>
            </div>
        </td>
        <td style="vertical-align: bottom">
            <div class="editActions" style="margin-left: 20px;">
        <span class="btn btn-warning btn-xs glyphicon glyphicon-pencil"
                <c:choose>
                    <c:when test="${template ne null}">
                        onclick="editMessageTemplate('${systemName}', '${name}', '${template.templateId}', '${template.caption}')"
                    </c:when>
                    <c:otherwise>
                        onclick="editIpForm('${systemName}', '${name}')"
                    </c:otherwise>
                </c:choose>
        ></span>
                <span class="btn btn-danger btn-xs glyphicon glyphicon-trash"
                        <c:choose>
                            <c:when test="${template ne null}">
                                onclick="deleteMessageTemplate('${systemName}', '${name}', '${template.templateId}', '${template.caption}')"
                            </c:when>
                            <c:otherwise>
                                onclick="delIpForm('${systemName}', '${name}')"
                            </c:otherwise>
                        </c:choose>
                ></span>
            </div>
        </td>
    </tr>
</table>

<form>
    <div id="codeWrapper">
        <span>Message</span>

        <div id="code"><c:out value="${message}" escapeXml="true"/></div>
        <div id="actionButtonsDiv">
            <label id="filterGenLabel" style="vertical-align: middle; padding-bottom: 6px">
                <input type="checkbox" id="filterGen" name="filterGen" alt="Filter generated message" checked>Filter
            </label>
            <span id="reset" title="Regenerate"
                  class="actionButtons glyphicon glyphicon-refresh btn btn-default"></span>
            &nbsp;&nbsp;
            <span id="validate" title="Validate"
                  class="actionButtons glyphicon glyphicon-ok-circle btn btn-default"></span>
            <span id="save" title="Save"
                  class="actionButtons glyphicon glyphicon-floppy-save btn btn-default"></span>
            &nbsp;&nbsp;
            <span id="test" title="Test"
                  class="actionButtons glyphicon glyphicon-wrench btn btn-default"></span>
            <c:if test="${link eq 'driver' && enabled}">
                &nbsp;&nbsp;
                <button id="send" title="Send" class="actionButtons btn btn-default">
                    <span class="glyphicon glyphicon-send"></span>
                </button>
            </c:if>
        </div>
        <div id="tabs">
            <ul class="nav nav-tabs">
                <li class="active"><a data-toggle="tab" href="#tabs-1"><span class="glyphicon glyphicon-console"></span>
                    Script</a></li>
                <c:if test="${link eq 'mock'}">
                    <li><a data-toggle="tab" href="#tabs-2"><span class="glyphicon glyphicon-list-alt"></span> Test</a>
                    </li>
                </c:if>
            </ul>
            <div class="tab-content">
                <div id="tabs-1" class="tab-pane active">
                    <div id="scriptCode"><c:out value="${script}" escapeXml="true"/></div>
                </div>
                <c:if test="${link eq 'mock'}">
                    <div id="tabs-2" class="tab-pane">
                        <div id="testCode"><c:out value="${test}" escapeXml="true"/></div>
                        <div id="testCodeButtons">
                            <label id="testFilterGenLabel">
                                <input id="testFilterGen" type="checkbox" alt="Filter generated message" checked>Filter
                            </label>
                            <span id="testRegenerate" class="glyphicon glyphicon-refresh btn btn-default btn-xs"></span>
                            <span id="testValidate" class="glyphicon glyphicon-ok-circle btn btn-default btn-xs"></span>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
    </div>
    <div id="resCodeWrapper">
        <span>Response</span>

        <div id="resCode"></div>
    </div>
</form>

<script src="<%=request.getContextPath()%>/resources/js/editor.js"></script>
<c:if test="${link eq 'driver'}">
    <script src="<%=request.getContextPath()%>/resources/js/editor_driver.js"></script>
</c:if>

</body>
</html>