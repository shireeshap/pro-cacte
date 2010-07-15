<%@ attribute name="studyParticipantAssignment" type="gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment" %>
<%@ attribute name="isEdit" %>
<%@ attribute name="selected" %>
<%@ tag import="java.util.Date" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ attribute name="studysite" type="gov.nih.nci.ctcae.core.domain.StudySite" required="true" %>

<c:forEach items="${studysite.study.crfs}" var="crf">
    <c:if test="${crf.status eq 'Final' and crf.childCrf eq null}">
        <c:set var="hasforms" value="true"/>
    </c:if>
</c:forEach>
<tr>
    <td>

        <input type="radio" name="studySites" value="${studysite.id}"
               onclick="javascript:showForms(this, '${studysite.id}')"
               <c:if test="${selected}">checked</c:if>/>
    </td>
    <td>
        ${studysite.study.displayName}
    </td>
    <c:if test="${isEdit}">
        <c:choose>
            <c:when test="${studyParticipantAssignment.offTreatmentDate ne null}">
                <tags:formatDate value="${studyParticipantAssignment.offTreatmentDate}"/>
            </c:when>
            <c:otherwise>
                <a href="javascript:participantOffStudy(${studyParticipantAssignment.id})">Set treatment end
                    date...</a>
            </c:otherwise>
        </c:choose>

    </c:if>
</tr>
<tr id="subform_${studysite.id}" <c:if test="${not selected}">style="display:none"</c:if>>
    <td></td>
    <td>
        <table class="widget" cellspacing="0">
            <tr>
                <td class="data" width="30%" align="right">
                    <span class="required-indicator">*&nbsp;&nbsp;</span><b> Participant study identifier</b>
                </td>
                <td class="data">
                    <input type="text"
                           name="participantStudyIdentifier_${studysite.id}"
                           value="${studyParticipantAssignment.studyParticipantIdentifier}"
                           title="identifier"
                           id="participantStudyIdentifier_${studysite.id}"/>

                </td>
            </tr>
            <tr>
                <c:if test="${fn:length(studysite.study.nonDefaultArms) > 0}">
                    <c:choose>
                        <c:when test="${fn:length(studysite.study.nonDefaultArms) > 1}">
                            <td align="right" class="data">
                                <b><span class="required-indicator">*&nbsp;&nbsp;</span><spring:message
                                        code="study.label.arm"/></b>
                            </td>
                            <td class="data">
                                <select name="arm_${studysite.id}" title="arm"
                                        id="arm_${studysite.id}">
                                    <option value="">Please select</option>
                                    <c:forEach items="${studysite.study.nonDefaultArms}" var="arm">
                                        <option value="${arm.id}" <c:if
                                                test="${studyParticipantAssignment.arm.id eq arm.id}">selected</c:if>>
                                                ${arm.title}
                                        </option>
                                    </c:forEach>
                                </select>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td align="right" class="data">
                                <b><span class="required-indicator">*&nbsp;&nbsp;</span><spring:message
                                        code="study.label.arm"/></b>
                            </td>
                            <td class="data">
                                <input type="hidden" name="arm_${studysite.id}"
                                       value="${studysite.study.nonDefaultArms[0].id}">${studysite.study.nonDefaultArms[0].title}
                            </td>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </tr>
            <tr>
                <td align="right" class="data">
                    <b><spring:message code="participant.label.startdate"/></b>
                </td>
                <td class="data">
                    <c:choose>
                        <c:when test="${selected}">
                            <tags:renderDate
                                    propertyName="study_date_${studysite.id}"
                                    doNotshowLabel="true" required="true"
                                    noForm="true" dateValue="${studyParticipantAssignment.studyStartDate}"/>
                        </c:when>
                        <c:otherwise>
                            <tags:renderDate
                                    propertyName="study_date_${studysite.id}"
                                    doNotshowLabel="true" required="true"
                                    noForm="true" dateValue="<%= new Date()%>"/>
                        </c:otherwise>
                    </c:choose>
                </td>

            </tr>
            <c:if test="${hasforms eq 'true'}">
                <c:set var="hasforms" value="false"/>
                <c:forEach items="${studysite.study.crfs}" var="crf">
                    <c:if test="${crf.status eq 'Final' and crf.childCrf eq null}">
                        <tr>
                            <td align="right" class="data">
                                <b><spring:message code="form.tab.form"/></b>
                            </td>
                            <td class="data">
                                    ${crf.title}
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>
            </c:if>
        </table>
    </td>
</tr>