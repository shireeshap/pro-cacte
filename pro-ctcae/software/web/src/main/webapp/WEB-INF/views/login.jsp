<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="gov.nih.nci.ctcae.web.ControllersUtils" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>


<%
    boolean isMobile = ControllersUtils.isRequestComingFromMobile(request);
    if (isMobile) {
        response.sendRedirect("../mobile/login");
    }
%>
<html>
<head>
    <tags:includeVirtualKeyboard/>
    <style type="text/css">
        .box {
            width: 35em;
            margin: 0 auto;
        }

        .submit {
            float: right; /*margin-top: 1em;*/
        }

        .forgot {
            float: left;
            margin-top: 1em;
        }

        * {
            zoom: 1;
        }


    </style>
    <!--[if IE]>
        <style>
            div.row div.value {
                margin-left:7px;
            }
        </style>
    <![endif]-->
</head>
<body>
<spring:message code="login.title" var="loginTitle" />
<chrome:box title="${loginTitle}" autopad="true">
    <form method="POST" id="loginForm" action="<c:url value="/pages/j_spring_security_check"/>">
    <c:set var="showLogin" value="true"/>
    <c:if test="${not empty param.error}">
        <c:choose>
            <c:when test="${SPRING_SECURITY_LAST_EXCEPTION.message == 'Bad credentials'}">
                <p class="errors">Incorrect username and/or password. Please try again.</p>
            </c:when>
            <c:when test="${SPRING_SECURITY_LAST_EXCEPTION.message == 'User account is locked'}">
                <p class="errors">
                    <jsp:forward page="accountLocked.jsp"/>
                </p>
                <c:set var="showLogin" value="false"/>
            </c:when>
            <c:when test="${fn:contains(SPRING_SECURITY_LAST_EXCEPTION.message,'Password expired')}">
                <p class="errors">
                    <jsp:forward page="forwardToPasswordExpire.jsp"/>
                </p>
            </c:when>
            <%--<c:when test="${fn:contains(SPRING_SECURITY_LAST_EXCEPTION.message, 'Use of web not activated')}">--%>
                <%--<p class="errors">Use of web is not allowed.</p>--%>
            <%--</c:when>--%>
            <c:otherwise>
                <%--${SPRING_SECURITY_LAST_EXCEPTION.message}--%>
                <p class="errors">User is inactive.</p>
            </c:otherwise>
        </c:choose>
    </c:if>
    <c:if test="${showLogin}">
        <div class="row">
            <div class="label">
                <tags:message code="login.username"/>
            </div>
            <div class="value">
                <input type="text" name="j_username" id="username"
                       value="${sessionScope['SPRING_SECURITY_LAST_USERNAME']}"
                       onclick="attachKeyBoard($('username'));"/>
            </div>
        </div>
        <div class="row">
            <div class="label">
                <tags:message code="login.password"/>
            </div>
            <div class="value" style="text-align:left; padding:0;">
                <input type="password" name="j_password" id="password" onclick="attachKeyBoard($('password'));"
                       style="margin-left:0;"/>
            </div>
        </div>
        <div class="row">
            <input id='usevirtualkeyboard' type="checkbox" onclick="showVirtualKeyBoard(this,'username');">&nbsp;
            	<tags:message code="login.userVirtualKeyboard"/>
        </div>
        <div class="row">
            <div class="submit">
            	<spring:message code="login.submit" var="loginSubmit" />
                <tags:button type="submit" value="${loginSubmit}" color="blue"/>
            </div>
        </div>
        </form>
        <div class="row">
            <a href='<c:url value="password"/>'><tags:message code="login.forgotPassword"/></a>
        </div>
        <div class="row">
            <a href='<c:url value="forgotusername"/>'><tags:message code="login.forgotUsername"/></a>
        </div>
    </c:if>
    <%--<p align="left">--%>
         <tags:message code="login.disclaimer.1"/><b><tags:message code="login.disclaimer.2"/></b>
    <%--</p>--%>

</chrome:box>
<div id="keyboardDiv"></div>
<br>
<chrome:box>
    <p align="center">

    <p align="center"><b><tags:message code="login.warning.label"/></b></p>
    <%--<ul>--%>
        <%--<li> --%>
        	<tags:message code="login.warning.message.1"/>
        <%--</li>--%>
        <%--<li>--%>
        	<tags:message code="login.warning.message.2"/>
        <%--</li>--%>
        	<tags:message code="login.warning.message.3"/>
    <%--</ul>--%>
    </p>
</chrome:box>
</body>
</html>



