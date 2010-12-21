<%@ attribute name="studyParticipantAssignment" type="gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment" %>
<%@ attribute name="isEdit" %>
<%@ attribute name="selected" %>
<%@ tag import="java.util.Date" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
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
        <td>
        <c:choose>
            <c:when test="${studyParticipantAssignment.offTreatmentDate ne null}">
                <tags:formatDate value="${studyParticipantAssignment.offTreatmentDate}"/><br>
            </c:when>
            <c:otherwise>
                <a href="javascript:participantOffStudy(${studyParticipantAssignment.id})">Off study date...</a> <br>
                <c:if test="${studyParticipantAssignment.onHoldTreatmentDate eq null}">
                    <a href="javascript:participantOnHold('${studyParticipantAssignment.id}', null)">Treatment on
                        hold</a> <br>
                </c:if>
                <c:if test="${studyParticipantAssignment.onHoldTreatmentDate ne null}">
                    Treatment on-hold from <tags:formatDate
                        value="${studyParticipantAssignment.onHoldTreatmentDate}"/><br>
                    <a href="javascript:participantOffHold('${studyParticipantAssignment.id}', null)"> Put participant
                        on
                        treatment </a>
                </c:if>
                <c:if test="${studyParticipantAssignment.offHoldTreatmentDate ne null}">
                    Treatment hold removed from <tags:formatDate
                        value="${studyParticipantAssignment.offHoldTreatmentDate}"/> <br>
                </c:if>
            </c:otherwise>
        </c:choose>
      </td>
    </c:if>
</tr>
<tr id="subform_${studysite.id}" <c:if test="${not selected}">style="display:none"</c:if>>
<td></td>
<td >
<table cellspacing="0" border="0">
<tr>
    <td class="data" align="right" width="30%">
        <span class="required-indicator">*&nbsp;&nbsp;</span><b> Participant study identifier</b>
    </td>                                                                                  
    <td width="50%">
        <input type="text"
               name="participantStudyIdentifier_${studysite.id}"
               value="${studyParticipantAssignment.studyParticipantIdentifier}"
               title="identifier"
               id="participantStudyIdentifier_${studysite.id}" onblur="checkParticipantStudyIdentifier(${studysite.study.id},${studysite.id});"/>
        <ul id="uniqueError" style="display:none" class="errors">
           <li><spring:message code='participant.unique_assignedIdentifier' text='participant.unique_assignedIdentifier'/></li>
        </ul>
    </td>
</tr>
<tr>
    <c:if test="${fn:length(studysite.study.nonDefaultArms) > 0}">
        <c:choose>
            <c:when test="${fn:length(studysite.study.nonDefaultArms) > 1}">
                <td class="data" align="right" width="30%">
                    <b><span class="required-indicator">*&nbsp;&nbsp;</span><spring:message
                            code="study.label.arm"/></b>
                </td>
                <td width="50%" >
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
    <td align="right" class="data" width="30%">
        <b>Home reporting option</b>
    </td>

    <td width="50%">
        <c:forEach items="${studysite.study.studyModes}" var="studyMode">
            <c:if test="${studyMode.mode.name eq 'HOMEWEB' || studyMode.mode.name eq 'IVRS' || studyMode.mode.name eq 'HOMEBOOKLET'}">
                <tags:renderRadio propertyName="participantModes_${studysite.id}"
                                  values="${studyParticipantAssignment.selectedAppModes}"
                                  displayName="${studyMode.mode.displayName}"
                                  propertyValue="${studyMode.mode.name}"
                                  noForm="true" useRenderInput="true" help="true"
                                  onclick="javascript:showOrHideEmail(this.checked, '${studyMode.mode.name}', ${studysite.id});"/>
            </c:if>
        </c:forEach>
    </td>
    <td width="${isEdit eq true ? "10%":"40%"}">
        <div id="web_${studysite.id}" style="display:none">
            <input type="checkbox" name="email_${studysite.id}" value="true"
                   id="email_${studysite.id}" ${studyParticipantAssignment.studyParticipantModes[0].email ? "checked" : " "}/>
            reminder
            via email
        </div>
        <br>
    </td>
</tr>

<tr id="c_${studysite.id}" style="${showTime eq true ? "":"display:none"}">
    <td align="right" class="data" valign="top" width="30%">
        <b>Call time</b>&nbsp;
    </td>
    <td valign="top" width="50%">

        <div id="ivrs_${studysite.id}" style="${showTime eq true ? "":"display:none"}">
            <select id="call_hour_${studysite.id}" name="call_hour_${studysite.id}">
                <c:forEach items="${hours}" var="hour">
                    <option value="${hour}" ${studyParticipantAssignment.callHour eq hour ? "selected='selected'" : " "} >${hour}</option>
                </c:forEach>
            </select>&nbsp;
            <select id="call_minute_${studysite.id}" name="call_minute_${studysite.id}">
                <c:forEach items="${minutes}" var="minute">
                    <option value="${minute}" ${studyParticipantAssignment.callMinute eq minute ? "selected='selected'" : " "} >${minute}</option>
                </c:forEach>
            </select>&nbsp;
            <select id="call_ampm_${studysite.id}" name="call_ampm_${studysite.id}">
                <option value="am" ${studyParticipantAssignment.callAmPm eq "am" ? "selected='selected'" : " "} >
                    am
                </option>
                <option value="pm" ${studyParticipantAssignment.callAmPm eq "pm" ? "selected='selected'" : " "} >
                    pm
                </option>
            </select>&nbsp;&nbsp;&nbsp;
            <b>Time zone</b>&nbsp;
            <select id="call_timeZone_${studysite.id}" name="call_timeZone_${studysite.id}">
                <option value="America/New_York" ${studyParticipantAssignment.callTimeZone eq "America/New_York" ? "selected='selected'" : " "} >
                    Eastern Time
                </option>
                <option value="America/Chicago" ${studyParticipantAssignment.callTimeZone eq "America/Chicago" ? "selected='selected'" : " "} >
                    Central Time
                </option>
                <option value="America/Denver" ${studyParticipantAssignment.callTimeZone eq "America/Denver" ? "selected='selected'" : " "} >
                    Mountain Time
                </option>
                <option value="America/Los_Angeles" ${studyParticipantAssignment.callTimeZone eq "America/Los_Angeles" ? "selected='selected'" : " "} >
                    Pacific Time
                </option>
                <option value="America/Anchorage" ${studyParticipantAssignment.callTimeZone eq "America/Anchorage" ? "selected='selected'" : " "} >
                    Alaska Time
                </option>
                <option value="America/Adak" ${studyParticipantAssignment.callTimeZone eq "America/Adak" ? "selected='selected'" : " "} >
                    Hawaii-Aleutian Time
                </option>
            </select>
            <br>
        </div>

    </td>
</tr>

<tr id="reminder_${studysite.id}" style="${showTime eq true ? "":"display:none"}">

    <td align="right" class="data" width="30%">
        <b>IVRS reminder options</b>&nbsp;
    </td>
    <td>

        <input type="checkbox" name="call_${studysite.id}" value="true"
               id="call_${studysite.id}" ${studyParticipantAssignment.studyParticipantModes[0].call ? "checked" : " "}/>reminder
        via
        call if the patient hasn't already completed the form <br>
        <input type="checkbox" name="text_${studysite.id}" value="true"
               id="text_${studysite.id}" ${studyParticipantAssignment.studyParticipantModes[0].text ? "checked" : " "}/>reminder
        via text if the patient hasn't already completed the form
    </td>
</tr>
<%--<tr id="reminder_time_${studysite.id}" style="${showTime eq true ? "":"display:none"}">--%>
    <%--<td valign="top" align="right" width="30%">--%>
        <%--<b>Reminder time</b>&nbsp;--%>
    <%--</td>--%>
    <%--<td valign="top" width="50%">--%>
        <%--<select id="reminder_hour_${studysite.id}" name="reminder_hour_${studysite.id}">--%>
            <%--<c:forEach items="${hours}" var="hour">--%>
                <%--<option value="${hour}" ${studyParticipantAssignment.reminderHour eq hour ? "selected='selected'" : " "} >${hour}</option>--%>
            <%--</c:forEach>--%>
        <%--</select>&nbsp;--%>
        <%--<select id="reminder_minute_${studysite.id}" name="reminder_minute_${studysite.id}">--%>
            <%--<c:forEach items="${minutes}" var="minute">--%>
                <%--<option value="${minute}" ${studyParticipantAssignment.reminderMinute eq minute ? "selected='selected'" : " "} >${minute}</option>--%>
            <%--</c:forEach>--%>
        <%--</select>&nbsp;--%>
        <%--<select id="reminder_ampm_${studysite.id}" name="reminder_ampm_${studysite.id}">--%>
            <%--<option value="am" ${studyParticipantAssignment.reminderAmPm eq "am" ? "selected='selected'" : " "} >--%>
                <%--am--%>
            <%--</option>--%>
            <%--<option value="pm" ${studyParticipantAssignment.reminderAmPm eq "pm" ? "selected='selected'" : " "} >--%>
                <%--pm--%>
            <%--</option>--%>
        <%--</select>&nbsp;&nbsp;&nbsp;--%>
        <%--<b>Time zone</b>&nbsp;--%>
        <%--<select id="reminder_timeZone_${studysite.id}" name="reminder_timeZone_${studysite.id}">--%>
            <%--<option value="America/New_York" ${studyParticipantAssignment.reminderTimeZone eq "America/New_York" ? "selected='selected'" : " "} >--%>
                <%--Eastern Time--%>
            <%--</option>--%>
            <%--<option value="America/Chicago" ${studyParticipantAssignment.reminderTimeZone eq "America/Chicago" ? "selected='selected'" : " "} >--%>
                <%--Central Time--%>
            <%--</option>--%>
            <%--<option value="America/Denver" ${studyParticipantAssignment.reminderTimeZone eq "America/Denver" ? "selected='selected'" : " "} >--%>
                <%--Mountain Time--%>
            <%--</option>--%>
            <%--<option value="America/Los_Angeles" ${studyParticipantAssignment.reminderTimeZone eq "America/Los_Angeles" ? "selected='selected'" : " "} >--%>
                <%--Pacific Time--%>
            <%--</option>--%>
            <%--<option value="America/Anchorage" ${studyParticipantAssignment.reminderTimeZone eq "America/Anchorage" ? "selected='selected'" : " "} >--%>
                <%--Alaska Time--%>
            <%--</option>--%>
            <%--<option value="America/Adak" ${studyParticipantAssignment.reminderTimeZone eq "America/Adak" ? "selected='selected'" : " "} >--%>
                <%--Hawaii-Aleutian Time--%>
            <%--</option>--%>
        <%--</select>--%>
    <%--</td>--%>
<%--</tr>--%>

<tr>
    <td align="right" class="data" width="30%">
        <b>In-clinic reporting option</b>
    </td>
    <td width="10%">
        <c:forEach items="${studysite.study.studyModes}" var="studyMode">
            <c:if test="${studyMode.mode.name eq 'CLINICWEB' || studyMode.mode.name eq 'CLINICBOOKLET'}">
                <tags:renderRadio propertyName="participantClinicModes_${studysite.id}"
                                  values="${studyParticipantAssignment.selectedAppModes}"
                                  displayName="${studyMode.mode.displayName}"
                                  propertyValue="${studyMode.mode.name}"
                                  noForm="true" useRenderInput="true" help="true"/>
            </c:if>
        </c:forEach>
    </td>

    <%--<td align="left" width="43%">--%>
        <%--<c:forEach items="${studysite.study.studyModes}" var="studyMode">--%>
        <%--<c:if test="${studyMode.mode.name eq 'CLINICWEB' || studyMode.mode.name eq 'CLINICBOOKLET'}">--%>
        <%--&nbsp;&nbsp;&nbsp;(no reminder will be sent)<br>--%>
        <%--</c:if>--%>
        <%--</c:forEach>--%>
    <%--</td>--%>
</tr>
<tr >
<td align="right">
<c:if test="${fn:length(studyParticipantAssignment.studyParticipantReportingModeHistoryItems) > 0}">
<a href="javascript:participantRptModeHistoryDisplay(${studyParticipantAssignment.id})">
                       <tags:message code="participant.reportmodehist.label.link"/> </a>
</c:if>                        
</td>
</tr>
<tr>
    <td align="right" class="data" width="30%">
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