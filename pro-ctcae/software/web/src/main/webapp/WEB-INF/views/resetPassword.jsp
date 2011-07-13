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
    	<spring:message code="rp.expiredlink" var="expiredlink" />
        <chrome:box title="${expiredlink}" autopad="true">
        	<spring:message code="rp.message.1" /><a href="/proctcae/public/password"><spring:message code="rp.message.2" /></a><spring:message code="rp.message.3" />
        </chrome:box>
    </c:when>
    <c:otherwise>
        <form:form method="post">
        	<spring:message code="rp.title" var="rpTitle" />
        	<spring:message code="rp.passwordPolicy" var="passwordPolicy" />
            <chrome:box title="${rpTitle}" autopad="true">
                <chrome:division title="${passwordPolicy}">
                     <spring:message code="rp.minlength" />${passwordPolicy.passwordCreationPolicy.minPasswordLength}
                <c:if test="${passwordPolicy.passwordCreationPolicy.combinationPolicy.upperCaseAlphabetRequired}">
                    <spring:message code="rp.uppercase" />
                </c:if>  <br/>
                <c:if test="${passwordPolicy.passwordCreationPolicy.combinationPolicy.lowerCaseAlphabetRequired}">
                    <spring:message code="rp.lowercase" />
                </c:if>  <br/>
                <c:if test="${passwordPolicy.passwordCreationPolicy.combinationPolicy.nonAlphaNumericRequired}">
                    <spring:message code="rp.alphanumeric" />
                </c:if>  <br/>
                <c:if test="${passwordPolicy.passwordCreationPolicy.combinationPolicy.baseTenDigitRequired}">
                    <spring:message code="rp.tendigit" />
                </c:if>
                </chrome:division>
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
                                	<spring:message code="save" var="save" />
                                    <tags:button type="submit" color="green" value="${save}" icon="save"/>
                                </div>
                            </div>
                        </td>
                    </tr>
                </table>

            </chrome:box>

        </form:form>
    </c:otherwise>
</c:choose>
</body>

</html>