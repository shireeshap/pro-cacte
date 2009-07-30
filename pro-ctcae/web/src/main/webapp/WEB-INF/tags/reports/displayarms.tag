<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@attribute name="arms" required="true" type="java.util.List" %>
<%@attribute name="colspan" %>
<%@attribute name="selectedArms" required="true" type="java.util.HashSet" %>
<%@attribute name="name" %>
<%@attribute name="resetPopUp" %>

<c:if test="${name eq null}">
    <c:set var="name" value="arm"/>
</c:if>
<c:set var="javascript" value="reportResults();"/>
<c:if test="${resetPopUp == 'true'}">
    <c:set var="javascript" value="resetPopUpFlagAndCallResults();"/>
</c:if>
<c:if test="${fn:length(arms)>1}">
    <tr>
        <td colspan="${colNum}"><b>Arms </b>
            <c:forEach items="${arms}" var="arm">
                <c:set var="checked" value=""/>
                <c:forEach items="${selectedArms}" var="selectedArm">
                    <c:if test="${selectedArm eq arm.id}">
                        <c:set var="checked" value="checked='true'"/>
                    </c:if>
                </c:forEach>
                <input type="checkbox" name="${name}" value="${arm.id}" ${checked}
                       onclick="${javascript}"/>${arm.title}
            </c:forEach>
        </td>
    </tr>
</c:if>
