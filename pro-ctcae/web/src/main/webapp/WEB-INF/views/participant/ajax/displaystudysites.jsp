<%@ page import="java.util.Date" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<style type="text/css">
    .tableHeader {
        background-color: #2B4186;
        background-image: url(/proctcae/images/blue/eXtableheader_bg.png);
        background-position: center top;
        background-repeat: repeat-x;
        color: white;
        font-size: 13px;
        font-weight: bold;
        margin: 0;
        padding: 4px 3px;
        text-align: left;
    }

    table.widget {
        width: 100%;
        background-color: #e9e8e8;
        border-top: 1px solid #999999;
        border-right: 1px solid #999999;
    }

    td.data {
        vertical-align: top;
        border-bottom: 1px solid #999999;
        border-left: 1px solid #999999;
        padding-left: 5px;
    }
</style>
<table cellpadding="0" width="100%">
    <tr>
        <td class="tableHeader">
            Select
        </td>
        <td class="tableHeader">
            Study
        </td>
        <%--<td class="tableHeader">--%>
        <%--<span class="required-indicator">*</span> Participant study identifier--%>
        <%--</td>--%>

        <c:forEach items="${studyparticipantassignments}" var="studyParticipantAssignment" varStatus="spastatus">
        <c:set var="studysite" value="${studyParticipantAssignment.studySite}"/>

        <td class="tableHeader">
            Treatment End Date
        </td>
    </tr>
    <tr>
        <td>
        </td>
        <td>
            [${studysite.study.assignedIdentifier}] ${studysite.study.shortTitle}
        </td>
            <%--<td>--%>
            <%--<input type="text" name="participantStudyIdentifier_${studysite.id}"--%>
            <%--value="${studyParticipantAssignment.studyParticipantIdentifier}" class="validate-NOTEMPTY">--%>
            <%--</td>--%>
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
        <td>
            &nbsp;
        </td>
        <td colspan="4">
            <table class="widget" cellspacing="0">
                <tr>
                    <td class="data" align="left" colspan="3">
                        <span class="required-indicator">*</span><b> Participant study identifier </b>
                        <input type="text" name="participantStudyIdentifier_${studysite.id}"
                               value="${studyParticipantAssignment.studyParticipantIdentifier}"
                               class="validate-NOTEMPTY">
                    </td>
                </tr>
                <tr>
                    <td class="data" align="left">
                        <b>&nbsp;&nbsp;<spring:message
                                code="study.label.arm"/>: </b> ${studyParticipantAssignment.arm.title}
                    </td>
                    <td class="data" align="left">
                        <b>&nbsp;&nbsp;<spring:message
                                code="study.label.arm.desc"/>: </b> ${studyParticipantAssignment.arm.description}
                    </td>
                </tr>
                <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}" var="spacrf"
                           varStatus="spacrfstatus">
                    <tr>
                        <td class="data" align="left">
                            <b><spring:message code="form.tab.form"/>: </b>${spacrf.crf.title}
                        </td>
                        <td class="data" align="left">
                            &nbsp; <b><spring:message code="participant.label.startdate"/></b><tags:formatDate
                                value="${spacrf.startDate}"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </td>
    </tr>

    </c:forEach>
    <c:forEach items="${unselectedstudysites}" var="studysite">
        <c:forEach items="${studysite.study.crfs}" var="crf">
            <c:if test="${crf.status eq 'Released' and crf.nextVersionId eq null}">
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
                            <span class="required-indicator">*</span><b> Participant study identifier </b>
                            <input type="text" name="participantStudyIdentifier_${studysite.id}"
                                   value="${studyParticipantAssignment.studyParticipantIdentifier}"
                                   title="identifier"
                                   id="participantStudyIdentifier_${studysite.id}"/>
                        </td>
                        <td class="data">
                            <c:if test="${fn:length(studysite.study.nonDefaultArms) > 0}">
                                <b><span class="required-indicator">*</span><spring:message code="study.label.arm"/></b>
                                <select name="arm_${studysite.id}" title="arm"
                                        id="arm_${studysite.id}">
                                    <option value="">Please select</option>
                                    <c:forEach items="${studysite.study.nonDefaultArms}" var="arm">
                                        <option value="${arm.id}">${arm.title}</option>
                                    </c:forEach>
                                </select>
                            </c:if>
                        </td>
                    </tr>
                    <c:if test="${hasforms eq 'true'}">
                        <c:set var="hasforms" value="false"/>
                        <c:forEach items="${studysite.study.crfs}" var="crf">
                            <c:if test="${crf.status eq 'Released' and crf.nextVersionId eq null}">
                                <tr>
                                    <td class="data">
                                        <b><spring:message
                                                code="form.tab.form"/>:</b>&nbsp;&nbsp;${crf.title}
                                    </td>
                                    <td class="data">
                                        <b><spring:message code="participant.label.startdate"/></b>
                                        <tags:renderDate
                                                propertyName="form_date_${crf.id}"
                                                doNotshowLabel="true"
                                                noForm="true" dateValue="<%= new Date()%>"/>
                                    </td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </c:if>
                </table>
            </td>
        </tr>
    </c:forEach>
</table>
