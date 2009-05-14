<%@page import="gov.nih.nci.ctcae.web.ControllersUtils" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <script type="text/javascript">

        function showVirtualKeyBoard(elem) {
            if (elem.checked) {
                VKI_attach($('username'));
            } else {
                VKI_close();
            }
        }

        function attachKeyBoard(elem) {
            if ($('usevirtualkeyboard').checked) {
                if (elem)
                    VKI_attach(elem);
            }
        }
    </script>
    <style type="text/css">
        .box {
            width: 35em;
            margin: 0 auto;
        }

        .submit {
            float: right;
            margin-top: 1em;
        }

        .forgot {
            float: left;
            margin-top: 1em;
        }

    </style>
</head>
<body>
<chrome:box title="Please log in" autopad="true">


    <form method="POST" id="loginForm" action="<c:url value="/pages/j_spring_security_check"/>">

        <c:if test="${not empty param.error}">
            <c:choose>
                <c:when test="${SPRING_SECURITY_LAST_EXCEPTION.message == 'Bad credentials'}">
                    <p class="errors">Incorrect username and/or password. Please try again.</p>
                </c:when>
                <c:otherwise>
                    <p class="errors">Your login attempt was not successful. Please try again after some time.<br/>
                        Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/></p>
                </c:otherwise>
            </c:choose>
        </c:if>

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
            <div class="value">
                <input type="password" name="j_password" id="password" onclick="attachKeyBoard($('password'));"/>
            </div>
        </div>
        <div class="row">
            <input id='usevirtualkeyboard' type="checkbox" onclick="showVirtualKeyBoard(this);">Use virtual keyboard
            <div id="keyboardDiv"></div>
        </div>
        <div class="row">
            <div class="submit">
                <tags:button value="Log in" color="blue"/>
            </div>
        </div>


        <div class="row">
            <div class="forgot">
                <a href='<c:url value="/public/resetPassword" />'>Forgot Password?</a>
            </div>
        </div>


    </form>

</chrome:box>
</body>
</html>



