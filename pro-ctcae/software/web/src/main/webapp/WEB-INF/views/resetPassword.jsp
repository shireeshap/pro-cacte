<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib prefix="administration" tagdir="/WEB-INF/tags/administration" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>


<html>
<head>

</head>
<body>

<c:choose>
    <c:when test="${expired eq true}">
        <chrome:box title="Link expired" autopad="true">
            The link has expired. Please use the <a href="/proctcae/public/password">Forgot Password</a> link to generate a new link.
        </chrome:box>
    </c:when>
    <c:otherwise>
        <form:form method="post">
            <chrome:box title="Set Password" autopad="true">
                <tags:instructions code="password.expired"/>
                <tags:hasErrorsMessage hideErrorDetails="false"/>
                <table width="100%" align="center">
                    <tr>
                        <td>
                            <tags:renderText propertyName="username"
                                             displayName="user.label.username"
                                             required="true"/>
                            <tags:renderPassword propertyName="password"
                                                 displayName="user.label.newpass"
                                                 required="true"/>
                            <tags:renderPassword propertyName="confirmPassword"
                                                 displayName="user.label.confirmpass"
                                                 required="true"/>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <div class="row">
                                <div class="label"></div>
                                <div class="value">
                                    <tags:button type="submit" color="green" value="Save" icon="save"/>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>
               <%--The minimum password length should be ${passwordPolicy.passwordCreationPolicy.minPasswordLength}--%>
                <%--<c:if test="${passwordPolicy.}"--%>
            </chrome:box>

        </form:form>
    </c:otherwise>
</c:choose>
</body>

</html>