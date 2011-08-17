<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="isEdit" value="${param['id'] eq '' ? false : true}"/>
<tags:noForm>
<table cellpadding="0" width="100%" border="0">
    <tr>
        <td class="tableHeader" width="5%">
            Select
        </td>
        <td class="tableHeader">
            Study
        </td>
        <c:if test="${isEdit}">
            <td width="20%" class="tableHeader">
                Treatment End/On-hold Date
            </td>
        </c:if>
    </tr>
    <c:forEach items="${studyparticipantassignments}" var="studyParticipantAssignment" varStatus="spastatus">
        <!-- BJ : selected -->
        <c:set var="studysite" value="${studyParticipantAssignment.studySite}"/>
        <tags:studySite studysite="${studysite}" selected="true" isEdit="${isEdit}"
                        studyParticipantAssignment="${studyParticipantAssignment}" participant="${command.participant}"/>
    </c:forEach>

    <c:if test="${not isEdit}">
        <!-- BJ : unselected-->
        <c:forEach items="${unselectedstudysites}" var="studysite">
            <tags:studySite studysite="${studysite}" selected="false" isEdit="${isEdit}"/>
        </c:forEach>
    </c:if>
</table>
</tags:noForm>
