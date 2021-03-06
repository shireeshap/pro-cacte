<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<html>
<head>

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
	<input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}" />

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
                       value="${sessionScope['SPRING_SECURITY_LAST_USERNAME']}"/>
            </div>
        </div>
        <div class="row">
            <div class="label">
                Password
            </div>
            <div class="value">
                <input type="password" name="j_password" id="password"/>
            </div>
        </div>

        <div class="row">
            <div class="submit">
                <tags:button value="Log in" color="blue"/>
            </div>
        </div>
    </form>
</chrome:box>
</body>
</html>



