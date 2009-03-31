<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<html>
<head>
    <script type="text/javascript">
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

    <c:if test="${not empty param.login_error}">
        <font color="red">
            Your login attempt was not successful, try again.<br/><br/>
            Reason: <c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>.
        </font>
    </c:if>


    <form method="POST" id="loginForm" action="<c:url value="/pages/j_spring_security_check"/>">

        <c:if test="${not empty param.login_error}">
            <p class="errors">Incorrect username and/or password. Please try again.</p>
        </c:if>

        <div class="row">
            <div class="label">
                Username
            </div>
            <div class="value">
                <input type="text" name="j_username"
                       value="${sessionScope['SPRING_SECURITY_LAST_USERNAME']}"
                        />
            </div>
        </div>
        <div class="row">
            <div class="label">
                Password
            </div>
            <div class="value">
                <input type="password" name="j_password"/>
            </div>
        </div>

            <%--<div class="row">--%>
            <%--<div class="label">--%>
            <%--Remember me:--%>
            <%--</div>--%>
            <%--<div class="value">--%>
            <%--<input type="checkbox" name="_spring_security_remember_me"/>--%>
            <%--</div>--%>
            <%--</div>--%>

        <div class="row">
            <div class="submit">
                <input type="submit" value="Log in"/>
            </div>
        </div>


        <div class="row">
            <div class="forgot">
                <a href='<c:url value="/public/resetPassword" />'>Forgot Password?</a>
            </div>
        </div>


    </form>
    <%--<chrome:division title="Test user accounts">--%>
        <!--<table class="tablecontent">-->
            <!--<tr id="ss-table-head" class="amendment-table-head">-->
                <!--<th class="tableHeader">Username</th>-->
                <!--<th class="tableHeader">Password</th>-->
                <!--<th class="tableHeader">Role</th>-->

            <!--</tr>-->
            <!--<tr>-->
                <!--<td>SYSTEM_ADMIN</td>-->
                <!--<td>system_admin</td>-->
                <!--<td>admin</td>-->
            <!--</tr>-->
            <!--<tr>-->
                <!--<td>cca</td>-->
                <!--<td>system_admin</td>-->
                <!--<td>CCA</td>-->
            <!--</tr>-->
            <!--<tr>-->
                <!--<td>cs1mskcc</td>-->
                <!--<td>system_admin</td>-->
                <!--<td>Overall Data Coordinator (ODC)</td>-->
            <!--</tr>-->
            <!--<tr>-->
                <!--<td>cs1wake</td>-->
                <!--<td>system_admin</td>-->
                <!--<td>Lead CRA</td>-->
            <!--</tr>-->
            <!--<tr>-->
                <!--<td>cs2wake</td>-->
                <!--<td>system_admin</td>-->
                <!--<td>Overall PI</td>-->
            <!--</tr>-->
            <!--<tr>-->
                <!--<td>cs1duke</td>-->
                <!--<td>system_admin</td>-->
                <!--<td>Site PI</td>-->
            <!--</tr>-->
            <!--<tr>-->
                <!--<td>cs2duke</td>-->
                <!--<td>system_admin</td>-->
                <!--<td>Site CRA</td>-->
            <!--</tr>-->
            <!--<tr>-->
                <!--<td>cs3duke</td>-->
                <!--<td>system_admin</td>-->
                <!--<td>Site Investigator</td>-->
            <!--</tr>-->
            <!--<tr>-->
                <!--<td>cs4duke</td>-->
                <!--<td>system_admin</td>-->
                <!--<td>Nurse</td>-->
            <!--</tr>-->
        <!--</table>-->
        <!--<br>-->
        <!--<br>-->
        <!--<a href="https://wiki.nci.nih.gov/display/CTMS/Roles">Click here </a> for security document.-->
    <%--</chrome:division>--%>
</chrome:box>
</body>
</html>


