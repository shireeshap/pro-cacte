<%@ page import="java.util.Date" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="isEdit" value="${param['id'] eq '' ? false : true}"/>
<script type="text/javascript">

    <c:if test="${not isEdit}">
    <c:forEach items="${unselectedstudysites}" var="studysite">
    Event.observe($('a_${studysite.id}'), 'click', function() {
    <c:forEach items="${studysite.study.crfs}" var="crf">
    <c:if test="${crf.status eq 'Final' and crf.childCrf eq null}">
        $('form_date_${crf.id}').value = $('study_date_${studysite.id}').value;
    </c:if>
    </c:forEach>
    })

    </c:forEach>
    </c:if>

</script>
<table cellpadding="0" width="100%">

    <c:if test="${not isEdit}">
        <tr>
            <td class="tableHeader">
                Select
            </td>
            <td class="tableHeader">
                Study
            </td>
            <td class="tableHeader">
                Treatment End Date
            </td>
        </tr>
    </c:if>
    <c:forEach items="${studyparticipantassignments}" var="studyParticipantAssignment" varStatus="spastatus">
        <c:set var="studysite" value="${studyParticipantAssignment.studySite}"/>
        <tr>
            <c:if test="${not isEdit}">
                <td></td>
            </c:if>
            <td>
                    ${studysite.study.displayName}
            </td>
            <td>
                <c:choose>
                    <c:when test="${studyParticipantAssignment.offTreatmentDate ne null}">
                        <tags:formatDate value="${studyParticipantAssignment.offTreatmentDate}"/>
                    </c:when>
                    <c:otherwise>
                        <a href="javascript:participantOffStudy(${studyParticipantAssignment.id})">Set treatment end
                            date...</a>
                    </c:otherwise>
                </c:choose>
            </td>

        </tr>
        <tr>
            <c:if test="${not isEdit}">
                <td>&nbsp;</td>
            </c:if>
            <td colspan="4">
                <table class="widget" cellspacing="0">
                    <tr>
                        <td class="data" align="left">
                            <span class="required-indicator">*</span><b>Participant study identifier&nbsp;</b>
                            <input type="text" name="participantStudyIdentifier_${studysite.id}"
                                   value="${studyParticipantAssignment.studyParticipantIdentifier}"
                                   class="validate-NOTEMPTY">
                        </td>
                        <td class="data" align="left">
                            <c:if test="${fn:length(studysite.study.nonDefaultArms) > 0}">
                                <b>&nbsp;&nbsp;<spring:message
                                        code="study.label.arm"/>:&nbsp;</b> ${studyParticipantAssignment.arm.title}
                            </c:if>
                        </td>
                    </tr>
                    <tr>
                        <td class="data" colspan="2">
                            <b><spring:message code="participant.label.startdate"/>:</b>&nbsp;<tags:formatDate
                                value="${studyParticipantAssignment.studyStartDate}"/>
                        </td>

                    </tr>
                    <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}" var="spacrf"
                               varStatus="spacrfstatus">
                        <tr>
                            <td class="data" align="left" colspan="2">
                                <b><spring:message code="form.tab.form"/>: </b>${spacrf.crf.title}
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </td>
        </tr>
    </c:forEach>
    <c:if test="${not isEdit}">
        <c:forEach items="${unselectedstudysites}" var="studysite">
            <c:forEach items="${studysite.study.crfs}" var="crf">
                <c:if test="${crf.status eq 'Final' and crf.childCrf eq null}">
                    <c:set var="hasforms" value="true"/>
                </c:if>
            </c:forEach>
            <tr>
                <td>
                    <input type="radio" name="studySites" value="${studysite.id}"
                           onclick="javascript:showForms(this, '${studysite.id}')"/>
                </td>
                <td>
                        ${studysite.study.displayName}
                </td>
            </tr>
            <tr id="subform_${studysite.id}" style="display:none">
                <td>
                    &nbsp;
                </td>
                <td>
                    <table class="widget" cellspacing="0">
                        <tr>
                            <td class="data">
                                <span class="required-indicator">*</span><b> Participant study identifier&nbsp;</b>
                                <input type="text" name="participantStudyIdentifier_${studysite.id}"
                                       value="${studyParticipantAssignment.studyParticipantIdentifier}"
                                       title="identifier"
                                       id="participantStudyIdentifier_${studysite.id}"/>
                            </td>
                            <td class="data">
                                <c:if test="${fn:length(studysite.study.nonDefaultArms) > 0}">
                                    <c:choose>
                                        <c:when test="${fn:length(studysite.study.nonDefaultArms) > 1}">
                                            <b><span class="required-indicator">*</span><spring:message
                                                    code="study.label.arm"/></b>
                                            <select name="arm_${studysite.id}" title="arm"
                                                    id="arm_${studysite.id}">
                                                <option value="">Please select</option>
                                                <c:forEach items="${studysite.study.nonDefaultArms}" var="arm">
                                                    <option value="${arm.id}">${arm.title}</option>
                                                </c:forEach>
                                            </select>
                                        </c:when>
                                        <c:otherwise>
                                            <input type="hidden" name="arm_${studysite.id}"
                                                   value="${studysite.study.nonDefaultArms[0].id}"> ${studysite.study.nonDefaultArms[0].title}
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </td>
                        </tr>
                        <tr>
                            <td class="data" colspan="2">
                                <table>
                                    <tr>
                                        <td colspan="2">
                                            <b><spring:message code="participant.label.startdate"/></b>
                                            <tags:renderDate
                                                    propertyName="study_date_${studysite.id}"
                                                    doNotshowLabel="true" required="true"
                                                    noForm="true" dateValue="<%= new Date()%>"/>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <c:if test="${hasforms eq 'true'}">
                            <c:set var="hasforms" value="false"/>
                            <c:forEach items="${studysite.study.crfs}" var="crf">
                                <c:if test="${crf.status eq 'Final' and crf.childCrf eq null}">
                                    <tr>
                                        <td class="data" colspan="2">
                                            <b><spring:message code="form.tab.form"/>:</b>&nbsp;&nbsp;${crf.title}
                                        </td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                        </c:if>
                    </table>
                </td>
            </tr>
        </c:forEach>
    </c:if>
</table>
