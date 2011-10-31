<%@ attribute name="studyParticipantAssignment" type="gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment" %>
<%@ attribute name="participant" type="gov.nih.nci.ctcae.core.domain.Participant" %>
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
    <c:if test="${crf.status eq 'RELEASED' and crf.childCrf eq null}">
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
                    <a href="javascript:participantOffStudy(${studyParticipantAssignment.id})">Off study date...</a>
                    <br>
                    <c:if test="${studyParticipantAssignment.onHoldTreatmentDate eq null}">
                        <a href="javascript:participantOnHold('${studyParticipantAssignment.id}', null)">Treatment on
                            hold</a> <br>
                    </c:if>
                    <c:if test="${studyParticipantAssignment.onHoldTreatmentDate ne null}">
                        Treatment on-hold from <tags:formatDate
                            value="${studyParticipantAssignment.onHoldTreatmentDate}"/><br>
                        <a href="javascript:participantOffHold('${studyParticipantAssignment.id}', null,0)"> Put
                            participant
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
<td>
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
                       id="participantStudyIdentifier_${studysite.id}"
                       onblur="checkParticipantStudyIdentifier(${studysite.study.id},${studysite.id});"/>
                <ul id="uniqueError_${studysite.id}" style="display:none" class="errors">
                    <li><spring:message code='participant.unique_assignedIdentifier'
                                        text='participant.unique_assignedIdentifier'/></li>
                </ul>
            <%--</c:otherwise>--%>
        <%--</c:choose>--%>
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
                <td width="50%">
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

<c:if test="${fn:length(studysite.study.studyModes) > 0}">
    <tr valign="top">
        <td align="right" valign="top" width="30%">
            <b>Home reporting option</b>
            <br>
            
            <c:forEach items="${studysite.study.studyModes}" var="studyMode">
                <c:if test="${studyMode.mode.name eq 'HOMEBOOKLET'}">
                    <span id="paper_home_header_${studysite.id}" style="display:none">
                    <table border="0" cellspacing="0">
                        <tr>
                            <td align="right" valign="top" width="30%">
                                <span class="required-indicator">*&nbsp;&nbsp; </span>
                                <b> Preferred language</b>
                            </td>
                        </tr>

                    </table>
                   </span>

                </c:if>
                <c:if test="${studyMode.mode.name eq 'HOMEWEB'}">
                    <span id="emailHeader_${studysite.id}" style="display:none;">
                    <table border="0" cellspacing="0" style="height:40px">
                        <c:if test="${command.mode eq 'Y'}">
                            <tr>
                                <td align="right" valign="bottom" width="30%">
                                    <span class="required-indicator">*&nbsp;</span>
                                    <b> Email Address</b>
                                </td>
                            </tr>
                        </c:if>
                        <tr height="70%">
                            <td align="right" valign="bottom" width="30%"><br>
                                <span class="required-indicator">*&nbsp;&nbsp; </span>
                                <b> Preferred language</b>
                            </td>
                        </tr>
                    </table>
                 </span>
                </c:if>
            </c:forEach>
        </td>


        <td width="50%">
            <c:forEach items="${studysite.study.studyModes}" var="studyMode">
                <c:if test="${studyMode.mode.name eq 'HOMEWEB' || studyMode.mode.name eq 'IVRS' || studyMode.mode.name eq 'HOMEBOOKLET'}">
                    <tags:renderRadio propertyName="participantModes_${studysite.id}"
                                      values="${studyParticipantAssignment.selectedAppModes}"
                                      displayName="${studyMode.mode.displayName}"
                                      propertyValue="${studyMode.mode.name}"
                                      noForm="true" useRenderInput="true"
                                      onclick="javascript:showOrHideEmail(this.checked, '${studyMode.mode.name}', ${studysite.id});"/>
                    <c:if test="${studyMode.mode.name eq 'HOMEBOOKLET'}">
                        <div id="home_paper_${studysite.id}" style="display:none">
                            <table>
                                <tr>
                                    <td>
                                        <select id="home_paper_lang_${studysite.id}"
                                                name="home_paper_lang_${studysite.id}">
                                            <option value="" ${studyParticipantAssignment.homePaperLanguage eq "" ? "selected='selected'" : " "} >
                                                Please select
                                            </option>
                                            <option value="ENGLISH" ${studyParticipantAssignment.homePaperLanguage eq "english" ? "selected='selected'" : " "} >
                                                English
                                            </option>
                                            <option value="SPANISH" ${studyParticipantAssignment.homePaperLanguage eq "spanish" ? "selected='selected'" : " "} >
                                                Spanish
                                            </option>
                                        </select>
                                    </td>
                                </tr>
                            </table>
                        </div>
                    </c:if>
                    <c:if test="${studyMode.mode.name eq 'HOMEWEB'}">
                        <div id="web_${studysite.id}" style="${showWeb eq true ? "":"display:none"}">
                            <input type="checkbox" name="email_${studysite.id}" value="true"
                                   onclick="javascript:showEmail(${studysite.id});"
                                   id="email_${studysite.id}" ${studyParticipantAssignment.studyParticipantModes[0].email ? "checked" : " "} />
                            reminder via email

                        </div>

                        <div id="emailInput_${studysite.id}" style="${showWeb eq true ? "":"display:none"}">
                            <table>
                                <c:if test="${command.mode eq 'Y'}">
                                    <tr>
                                        <td>
                                            <input type="text" name="participant.emailAddress_${studysite.id}"
                                                   value="${studyParticipantAssignment.participant.emailAddress}"
                                                   id="participant.emailAddress_${studysite.id}"
                                                   onblur="javascript:checkParticipantEmailAddress(${studysite.id});"
                                                   size="35"/>
                                            <ul id="emailError_${studysite.id}" style="display:none;"
                                                class="errors">
                                                <li><spring:message
                                                        code='participant.unique_emailAddress'
                                                        text='participant.unique_emailAddress'/></li>
                                            </ul>
                                        </td>
                                    </tr>
                                </c:if>
                                <tr>
                                    <td>
                                        <select id="home_web_lang_${studysite.id}"
                                                name="home_web_lang_${studysite.id}">
                                            <option value="" ${studyParticipantAssignment.homeWebLanguage eq "" ? "selected='selected'" : " "} >
                                                Please select
                                            </option>
                                            <option value="ENGLISH" ${studyParticipantAssignment.homeWebLanguage eq "ENGLISH" ? "selected='selected'" : " "} >
                                                English
                                            </option>
                                            <option value="SPANISH" ${studyParticipantAssignment.homeWebLanguage eq "SPANISH" ? "selected='selected'" : " "} >
                                                Spanish
                                            </option>
                                        </select>
                                    </td>
                                </tr>
                            </table>

                        </div>

                        <div id="webLang_${studysite.id}" style="display:none;">

                        </div>
                    </c:if>
                </c:if>
            </c:forEach>
        </td>
    </tr>
</c:if>

<tr id="c1_${studysite.id}" style="${showTime eq true ? "":"display:none"}">
    <td align="right" class="data" valign="top" width="30%">
        <span class="required-indicator">*&nbsp;&nbsp; </span>
        <b>Phone</b>&nbsp;
    </td>
    <td valign="top" width="50%">
        <input type="text" name="participant.phoneNumber_${studysite.id}"
               value="${studyParticipantAssignment.participant.phoneNumber}"
               id="participant.phoneNumber_${studysite.id}"
               onblur="checkParticipantPhoneNumber(${studysite.id});" title="Phone number" class="${showTime eq true ? "validate-NOTEMPTY":""}"/>
        <span class="phone-number">###-###-####</span>
        <tags:errors path="participant.phoneNumber"/>
         <ul id="phoneNumberError_${studysite.id}" style="display:none;" class="errors">
            <li><spring:message code='participant.unique_phoneNumber'
                                text='participant.unique_phoneNumber'/></li>
        </ul>
        <ul id="PhonePatternError_${studysite.id}" style="display:none;" class="errors">
            <li><spring:message code='participant.phonenumber_pattern'
                                text='participant.phonenumber_pattern'/></li>
        </ul>
    </td>
</tr>

<tr id="c2_${studysite.id}" style="${showTime eq true ? "":"display:none"}">
    <td align="right" class="data" valign="top" width="30%">
        <span class="required-indicator">*&nbsp;&nbsp; </span>
        <b>IVRS user id</b>&nbsp;
    </td>
    <td>
        <input type="text" name="participantUserNumber_${studysite.id}"
               value="${studyParticipantAssignment.participant.userNumber}"
               id="participant.userNumber_${studysite.id}" title="User Number"
               onblur="checkParticipantUserNumber(${studysite.id});" class="${showTime eq true ? "validate-NOTEMPTY":""}"/>
        <ul id="userNumberError_${studysite.id}" style="display:none;" class="errors">
            <li><spring:message code='participant.unique_userNumber'
                                text='participant.unique_userNumber'/></li>
        </ul>
        <ul id="UserPatternError_${studysite.id}" style="display:none;" class="errors">
            <li><spring:message code='participant.usernumber_pattern'
                                text='participant.usernumber_pattern'/></li>
        </ul>
    </td>
</tr>
<tr id="c3_${studysite.id}" style="${showTime eq true ? "":"display:none"}" >
    <td align="right" class="data" valign="top" width="30%">
        <span class="required-indicator">*&nbsp;&nbsp; </span>
        <b>PIN number</b>&nbsp;
    </td>
    <td>
        <input type="text" name="participantPinNumber_${studysite.id}"
               value="${studyParticipantAssignment.participant.pinNumber}"
               id="participant.pinNumber_${studysite.id}"
               onblur="checkParticipantPinNumber(${studysite.id});" title="Pin number" class="${showTime eq true ? "validate-NOTEMPTY":""}"/>
        <ul id="PinPatternError_${studysite.id}" style="display:none;" class="errors">
            <li><spring:message code='participant.pinnumber_pattern'
                                text='participant.pinnumber_pattern'/></li>
        </ul>
    </td>
</tr>
<tr id="c_${studysite.id}" style="${showTime eq true ? "":"display:none"}">
    <td align="right" class="data" valign="top" width="30%">
        <span class="required-indicator">*&nbsp;&nbsp; </span>
        <b>Preferred call time</b>&nbsp;
    </td>
    <td valign="top" width="50%">

        <div id="ivrs_${studysite.id}" style="${showTime eq true ? "":"display:none"}">
            <table><tr><td>
            <select id="call_hour_${studysite.id}" name="call_hour_${studysite.id}"  title="Hour" class="${showTime eq true ? "validate-NOTEMPTY":""}">
                <option value="" ${studyParticipantAssignment.callHour eq "" ? "selected='selected'" : " "} >
                    Hr
                </option>
                <c:forEach items="${hours}" var="hour">
                    <c:set var="hr" value="${hour}"/>
                    <c:if test="${hr/10<1}"><c:set var="hr" value="0${hour}"/></c:if>
                    <option value="${hour}" ${studyParticipantAssignment.callHour eq hour ? "selected='selected'" : " "} >${hr}</option>
                </c:forEach>
            </select>&nbsp;
            <select id="call_minute_${studysite.id}" name="call_minute_${studysite.id}"  title="Minute" class="${showTime eq true ? "validate-NOTEMPTY":""}">
                <option value="" ${studyParticipantAssignment.callMinute eq "" ? "selected='selected'" : " "} >
                    Min
                </option>
                <c:forEach items="${minutes}" var="minute">
                    <c:set var="min" value="${minute}"/>
                    <c:if test="${min/10<1}"><c:set var="min" value="0${minute}"/></c:if>
                    <option value="${minute}" ${studyParticipantAssignment.callMinute eq minute ? "selected='selected'" : " "} >${min}</option>
                </c:forEach>
            </select>&nbsp;
            <select id="call_ampm_${studysite.id}" name="call_ampm_${studysite.id}"  title="AM PM" class="${showTime eq true ? "validate-NOTEMPTY":""}">
                <option value="am" ${studyParticipantAssignment.callAmPm eq "am" ? "selected='selected'" : " "} >
                    am
                </option>
                <option value="pm" ${studyParticipantAssignment.callAmPm eq "pm" ? "selected='selected'" : " "} >
                    pm
                </option>
            </select>&nbsp;&nbsp;&nbsp;
                </td><td>
            <b> <span class="required-indicator">*&nbsp; </span>Time zone</b>&nbsp;
            <select id="call_timeZone_${studysite.id}" name="call_timeZone_${studysite.id}"  title="Time zone" class="${showTime eq true ? "validate-NOTEMPTY":""}">
               <%-- <option value="" ${studyParticipantAssignment.callTimeZone eq "" ? "selected='selected'" : " "} >
                    Please select
                </option>--%>
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
                </td></tr></table>
        </div>

    </td>
</tr>
<tr id="ivrsLang_${studysite.id}" style="${showTime eq true ? "":"display:none"}">
    <td align="right" valign="top" width="30%">
        <span class="required-indicator">*&nbsp;&nbsp; </span>
        <b>Preferred language</b>
    </td>
    <td>
        <select id="ivrs_lang_${studysite.id}" name="ivrs_lang_${studysite.id}" title="Preferred language">
            <option value="" ${studyParticipantAssignment.ivrsLanguage eq "" ? "selected='selected'" : " "} >
                Please select
            </option>
            <option value="ENGLISH" ${studyParticipantAssignment.ivrsLanguage eq "ENGLISH" ? "selected='selected'" : " "} >
                English
            </option>
            <option value="SPANISH" ${studyParticipantAssignment.ivrsLanguage eq "SPANISH" ? "selected='selected'" : " "} >
                Spanish
            </option>
        </select>
    </td>
</tr>

<tr id="reminder_${studysite.id}" style="${showTime eq true ? "":"display:none"}">

    <td align="right" class="data" width="30%">
        <b>IVRS reminder options</b>&nbsp;
    </td>
    <td>

        <input type="checkbox" name="call_${studysite.id}" value="true"
               id="call_${studysite.id}" ${studyParticipantAssignment.studyParticipantModes[0].call ? "checked" : " "}/>
        reminder via call if the patient hasn't already completed the form <br>
        <!-- <input type="checkbox" name="text_${studysite.id}" value="true"
               id="text_${studysite.id}" ${studyParticipantAssignment.studyParticipantModes[0].text ? "checked" : " "}/>
        reminder via text if the patient hasn't already completed the form -->
    </td>
</tr>

<c:if test="${fn:length(studysite.study.studyModes) > 0}">
<tr>
    <td align="right" class="data" width="30%">
        <b>In-clinic reporting option</b>
        <br>
        <c:forEach items="${studysite.study.studyModes}" var="studyMode">
            <c:if test="${studyMode.mode.name eq 'CLINICBOOKLET'}">
              <span id="paper_clinic_header_${studysite.id}" style="display:none">
                    <table border="0" cellspacing="0">
                        <tr>
                            <td align="right" valign="bottom" width="30%">
                                <span class="required-indicator">*&nbsp;&nbsp; </span>
                                <b> Preferred language</b>
                            </td>
                        </tr>
                    </table>
                   </span>
            </c:if>
            <c:if test="${studyMode.mode.name eq 'CLINICWEB'}"> <br>
             <span id="web_clinic_header_${studysite.id}" style="display:none">
                    <table border="0" cellspacing="0">
                        <tr>
                            <td align="right" valign="bottom" width="30%">
                                <span class="required-indicator">*&nbsp;&nbsp; </span>
                                <b> Preferred language</b>
                            </td>
                        </tr>
                    </table>
                   </span>
            </c:if>
        </c:forEach>

    </td>
    <td width="10%">
        <c:forEach items="${studysite.study.studyModes}" var="studyMode">
            <c:if test="${studyMode.mode.name eq 'CLINICWEB' || studyMode.mode.name eq 'CLINICBOOKLET'}">
                <tags:renderRadio propertyName="participantClinicModes_${studysite.id}"
                                  values="${studyParticipantAssignment.selectedAppModes}"
                                  displayName="${studyMode.mode.displayName}"
                                  propertyValue="${studyMode.mode.name}"
                                  noForm="true" useRenderInput="true"
                                  onclick="javascript:showOrHideLanguage(this.checked, '${studyMode.mode.name}', ${studysite.id});"/>
            </c:if>
            <c:if test="${studyMode.mode.name eq 'CLINICBOOKLET'}">
                <div id="clinicPaper_${studysite.id}" style="display:none">
                    <table>
                        <tr>
                            <td>
                                <select id="clinic_paper_lang_${studysite.id}"
                                        name="clinic_paper_lang_${studysite.id}">
                                    <option value="" ${studyParticipantAssignment.clinicPaperLanguage eq "" ? "selected='selected'" : " "} >
                                        Please select
                                    </option>
                                    <option value="english" ${studyParticipantAssignment.clinicPaperLanguage eq "english" ? "selected='selected'" : " "} >
                                        English
                                    </option>
                                    <option value="spanish" ${studyParticipantAssignment.clinicPaperLanguage eq "spanish" ? "selected='selected'" : " "} >
                                        Spanish
                                    </option>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
            </c:if>
            <c:if test="${studyMode.mode.name eq 'CLINICWEB'}">
                <div id="clinicWeb_${studysite.id}" style="display:none">
                    <table>
                        <tr>
                            <td>
                                <select id="clinic_web_lang_${studysite.id}"
                                        name="clinic_web_lang_${studysite.id}">
                                    <option value="" ${studyParticipantAssignment.clinicWebLanguage eq "" ? "selected='selected'" : " "} >
                                        Please select
                                    </option>
                                    <option value="ENGLISH" ${studyParticipantAssignment.clinicWebLanguage eq "ENGLISH" ? "selected='selected'" : " "} >
                                        English
                                    </option>
                                    <option value="SPANISH" ${studyParticipantAssignment.clinicWebLanguage eq "SPANISH" ? "selected='selected'" : " "} >
                                        Spanish
                                    </option>
                                </select>
                            </td>
                        </tr>
                    </table>
                </div>
            </c:if>

        </c:forEach>
    </td>
    </c:if>
</tr>
<tr>

</tr>
<tr>
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
        <c:if test="${crf.status eq 'RELEASED' and crf.childCrf eq null}">
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