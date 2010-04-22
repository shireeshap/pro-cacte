<%@page import="gov.nih.nci.ctcae.web.ControllersUtils" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

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
<chrome:box title="Please log in" autopad="true">


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
            <c:otherwise>
                <%--${SPRING_SECURITY_LAST_EXCEPTION.message}--%>
                <p class="errors">User is inactive.</p>
            </c:otherwise>
        </c:choose>
    </c:if>
    <c:if test="${showLogin}">
        <div class="row">
            <div class="label">
                Username
            </div>
            <div class="value">
                <input type="text" name="j_username" id="username"
                       value="${sessionScope['SPRING_SECURITY_LAST_USERNAME']}"
                       onclick="attachKeyBoard($('username'));"/>
            </div>
        </div>
        <div class="row">
            <div class="label">
                Password
            </div>
            <div class="value" style="text-align:left; padding:0;">
                <input type="password" name="j_password" id="password" onclick="attachKeyBoard($('password'));"
                       style="margin-left:0;"/>
            </div>
        </div>
        <div class="row">
            <input id='usevirtualkeyboard' type="checkbox" onclick="showVirtualKeyBoard(this,'username');">&nbsp;Use
            virtual
            keyboard
        </div>
        <div class="row">
            <a href='<c:url value="password"/>'>Forgot password?</a>
        </div>
        <div class="row">
            <div class="submit">
                <tags:button type="submit" value="Log in" color="blue"/>
            </div>
        </div>
        </form>
    </c:if>
</chrome:box>
<div id="keyboardDiv"></div>
<br>

<p align="center">

<p align="center">***WARNING*** </p>
<ul>
    <li> You are accessing a U.S. Government information system, which includes (1) this computer, (2) this computer
        network, (3) all computers connected to this network, and (4) all devices and storage media attached to this
        network or to a computer on this network. This information system is provided for U.S. Government-authorized use
        only.
    </li>
    <li>Unauthorized or improper use of this system may result in disciplinary action, as well as civil and criminal
        penalties.
    </li>
    <li> By using this information system, you understand and consent to the following:
        You have no reasonable expectation of privacy regarding any communications or data transiting or stored on this
        information system. At any time, and for any lawful Government purpose, the government may monitor, intercept,
        record and search and seize any communication or data transiting or stored on this information system.
    </li>
    Any communication or data transiting or stored on this information system may be disclosed or used for any lawful
    Government purpose.
</ul>

</p>

</body>
</html>



