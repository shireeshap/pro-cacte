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
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ attribute name="studysite" type="gov.nih.nci.ctcae.core.domain.StudySite" required="true" %>
<c:forEach items="${studysite.study.crfs}" var="crf">
    <c:if test="${crf.status eq 'RELEASED' and crf.childCrf eq null}">
        <c:set var="hasforms" value="true"/>
    </c:if>
</c:forEach>
<tr>
    <td align="center">
        <input type="radio" name="studySites" value="${studysite.id}"
               onclick="javascript:showForms(this, '${studysite.id}')"
               <c:if test="${selected}">checked</c:if>/>
    </td>
    <td>
        ${studysite.study.displayName}
    </td>
    <c:set var="hweb" value="false"/>
    <c:set var="ivrs" value="false"/>
    <c:set var="hbook" value="false"/>
    <c:forEach items="${command.selectedStudyParticipantAssignment.studyParticipantModes}" var="app" varStatus="status">
        <c:if test="${app.mode.name eq 'HOMEWEB'}"><c:set var="hweb" value="true"/></c:if>
        <c:if test="${app.mode.name eq 'IVRS'}"><c:set var="ivrs" value="true"/></c:if>
        <c:if test="${app.mode.name eq 'HOMEBOOKLET'}"><c:set var="hbook" value="true"/></c:if>
    </c:forEach>
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
    <td width="50%" class="data">

        <input type="text"
               name="participantStudyIdentifier_${studysite.id}"
               value="${studyParticipantAssignment.studyParticipantIdentifier}"
               title="identifier"
               id="participantStudyIdentifier_${studysite.id}"
               onblur="checkParticipantStudyIdentifier(${studysite.study.id},${studysite.id});"
               class="validate-NOTEMPTY"/>
        <ul id="uniqueError_${studysite.id}" style="display:none" class="errors">
            <li><spring:message code='participant.unique_assignedIdentifier'
                                text='participant.unique_assignedIdentifier'/></li>
        </ul>
        <ul id="participantStudyIdentifier_${studysite.id}.error" style="display:none;" class="errors">
            <li><spring:message code='special.character.message'
                                text='special.character.message'/></li>
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
                <td width="50%" class="data">
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
<c:if test="${fn:length(studysite.study.studyModes) > 0}">
<tr>

    <td colspan="2">
        <chrome:division title="Reporting options">
            <c:forEach items="${studysite.study.studyModes}" var="studyMode">
                <c:if test="${studyMode.mode.name eq 'HOMEWEB'}">
                    <c:choose>
                        <c:when test="${hweb}">
                            <input type="checkbox" name="responseModes" checked="true" value="HOMEWEB"
                                   onclick="javascript:showOrHideEmail(this.checked, '${studyMode.mode.name}', '${studysite.id}');"/>&nbsp;&nbsp;&nbsp;Web
                            <br>
                        </c:when>
                        <c:otherwise>
                            <input type="checkbox" name="responseModes" value="HOMEWEB"
                                   onclick="javascript:showOrHideEmail(this.checked, '${studyMode.mode.name}', '${studysite.id}');"/>&nbsp;&nbsp;&nbsp;Web
                            <br>
                        </c:otherwise>
                    </c:choose>
                </c:if>
            </c:forEach>
        </chrome:division>
    </td>

</tr>
<tr>

    <td colspan="2">

        <div id="passwordfields" style="${style}">
            <table border="0" cellpadding="0" cellspacing="0">

                <tr id="web_${studysite.id}"
                    style="${showWeb eq true ? "":"display:none"}">
                    <td>
                        <c:choose>
                            <c:when test="${command.readOnlyUserName}">
                                <div class="row">
                                    <div class="label"><spring:message
                                            code="participant.label.username"/>&nbsp;</div>
                                    <div class="value">${command.participant.user.username}</div>
                                    <input type="hidden" id="participant.user.username"
                                           name="participant.user.username"
                                           value="${command.participant.user.username}">
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="row">
                                    <div class="label"><spring:message
                                            code="participant.label.username"/>&nbsp;</div>
                                    <div class="value">
                                        <input type="text" name="participant.username_${studysite.id}"
                                               value="${command.participant.user.username}"
                                               id="participant.username_${studysite.id}" title="Username"
                                               onblur="checkParticipantUserName(${studysite.id});"
                                               class="${showWeb eq true ? "validate-NOTEMPTY":""}"/>
                                        <ul id="userNameError_${studysite.id}" style="display:none;" class="errors">
                                            <li><spring:message code='participant.unique_userName'
                                                                text='participant.unique_userName'/></li>
                                        </ul>
                                        <ul id="userNameLengthError_${studySite.id}"
                                            style="display:none; padding-left:12em "
                                            class="errors">
                                            <li><spring:message code='participant.username_length'
                                                                text='participant.username_length'/></li>
                                        </ul>
                                            <%--<ul id="UserPatternError_${studysite.id}" style="display:none;" class="errors">--%>
                                            <%--<li><spring:message code='participant.usernumber_pattern'--%>
                                            <%--text='participant.usernumber_pattern'/></li>--%>
                                            <%--</ul>--%>
                                    </div>

                                </div>
                                <%--<tags:renderText propertyName="participant.user.username"--%>
                                <%--displayName="participant.label.username"--%>
                                <%--required="true" onblur="checkParticipantUserName();"/>--%>
                                <%--<ul id="userNameError" style="display:none; padding-left:12em " class="errors">--%>
                                <%--<li><spring:message code='participant.unique_userName'--%>
                                <%--text='participant.unique_userName'/></li>--%>
                                <%--</ul>--%>
                                <%--<ul id="userNameLengthError" style="display:none; padding-left:12em "--%>
                                <%--class="errors">--%>
                                <%--<li><spring:message code='participant.username_length'--%>
                                <%--text='participant.username_length'/></li>--%>
                                <%--</ul>--%>

                            </c:otherwise>
                        </c:choose>
                        <div class="row">
                            <div class="label"><spring:message
                                    code="participant.label.password"/>&nbsp;</div>
                            <div class="value">
                                <input type="password" name="participant.password_${studysite.id}"
                                       value="${participant.user.password}"
                                       id="participant.password_${studysite.id}"
                                       onblur="checkPasswordPolicy(${studysite.id});" title="Password"
                                       class="${showWeb eq true ? "validate-NOTEMPTY":""}"/>
                                <ul id="passwordError_${studysite.id}" style="display:none; padding-left:12em "
                                    class="errors">--%>
                                    <li id="passwordError1_${studysite.id}"></li>
                                </ul>
                            </div>

                        </div>
                            <%--<tags:renderPassword required="true" propertyName="participant.user.password"--%>
                            <%--displayName="participant.label.password"--%>
                            <%--onblur="checkPasswordPolicy();"/>--%>
                            <%--<ul id="passwordError" style="display:none; padding-left:12em " class="errors">--%>
                            <%--<li id="passwordError1"></li>--%>
                            <%--</ul>--%>
                        <div class="row">
                            <div class="label"><spring:message
                                    code="participant.label.email_address"/>&nbsp;</div>
                            <div class="value">
                                <input type="text" name="participant.email_${studysite.id}"
                                       value="${studyParticipantAssignment.participant.emailAddress}"
                                       id="participant.email_${studysite.id}"
                                       onblur="checkParticipantEmail(${studysite.id});" title="Email"
                                       class="${showWeb eq true ? "validate-NOTEMPTY":""}"/>
                                <ul id="userEmailError_${studysite.id}" style="display:none; padding-left:12em " class="errors">
                                    <li><spring:message code='participant.unique_emailAddress'
                                                        text='participant.unique_emailAddress'/>
                                    </li>
                                </ul>
                            </div>

                        </div>
                        <%--<tags:renderEmail propertyName="participant.emailAddress"--%>
                                          <%--displayName="participant.label.email_address"--%>
                                          <%--required="false" size="35" onblur="checkParticipantEmail();"/>--%>
                        <%--<ul id="userEmailError" style="display:none; padding-left:12em " class="errors">--%>
                            <%--<li><spring:message code='participant.unique_emailAddress'--%>
                                                <%--text='participant.unique_emailAddress'/>--%>
                            <%--</li>--%>
                        <%--</ul>--%>

                        <div class="row">
                            <div class="label">
                                <span class="required-indicator">*&nbsp;&nbsp; </span> Preferred language
                            </div>
                            <div class="value">
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
                            </div>
                        </div>
                    </td>
                    <td valign="top"><br><br>

                        <div class="row">
                            <div class="label"><spring:message
                                    code="participant.label.confirmpassword"/>&nbsp;</div>
                            <div class="value">
                                <input type="password" name="participant.confirmPassword_${studysite.id}"
                                       value="${participant.user.password}"
                                       id="participant.confirmPassword_${studysite.id}"
                                       onblur="checkPasswordMatch(${studysite.id});" title="Confirm"
                                       class="${showWeb eq true ? "validate-NOTEMPTY":""}"/>
                                <ul id="passwordErrorConfirm_${studysite.id}" style="display:none; padding-left:12em "
                                    class="errors">
                                    <li id="passwordErrorConfirm1_${studysite.id}"></li>
                                </ul>
                            </div>

                        </div>
                        <div class="row">
                            <div class="label">
                                <input type="checkbox" name="email_${studysite.id}" value="true"
                                       onclick="javascript:showEmail(${studysite.id});"
                                       id="email_${studysite.id}" ${studyParticipantAssignment.studyParticipantModes[0].email ? "checked" : " "} />
                            </div>
                            <div class="value">
                                reminder via email
                            </div>
                        </div>
                    </td>
                </tr>
            </table>

        </div>
    </td>
</tr>
<tr>

    <td colspan="2">
        <c:forEach items="${studysite.study.studyModes}" var="studyMode">
            <c:if test="${studyMode.mode.name eq 'IVRS'}">
                <c:choose>
                    <c:when test="${ivrs}">
                        <input type="checkbox" name="responseModes" checked="true" value="IVRS"
                               onclick="javascript:showOrHideEmail(this.checked, '${studyMode.mode.name}', '${studysite.id}');"/>&nbsp;&nbsp;&nbsp;IVRS/Automated
                        Telephone <br>
                    </c:when>
                    <c:otherwise>
                        <input type="checkbox" name="responseModes" value="IVRS"
                               onclick="javascript:showOrHideEmail(this.checked, '${studyMode.mode.name}', '${studysite.id}');"/>&nbsp;&nbsp;&nbsp;IVRS/Automated
                        Telephone <br>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </c:forEach>
    </td>

</tr>
<tr id="c1_${studysite.id}" style="${showTime eq true ? "":"display:none"}">
<td colspan="2">
<table>
    <tr>
        <td>
            <div class="row">
                <div class="label">
                    <span class="required-indicator">*&nbsp;&nbsp; </span> Phone
                </div>
                <div class="value">
                    <input type="text" name="participantPhoneNumber_${studysite.id}"
                           value="${studyParticipantAssignment.participant.phoneNumber}"
                           id="participant.phoneNumber_${studysite.id}"
                           onblur="checkParticipantPhoneNumber(${studysite.id});" title="Phone number"
                           class="${showTime eq true ? "validate-NOTEMPTY":""}"/>
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
                </div>
            </div>
            <div class="row">
                <div class="label">
                    <span class="required-indicator">*&nbsp;&nbsp; </span> IVRS user id
                </div>
                <div class="value">
                    <input type="text" name="participantUserNumber_${studysite.id}"
                           value="${studyParticipantAssignment.participant.userNumber}"
                           id="participant.userNumber_${studysite.id}" title="User Number"
                           onblur="checkParticipantUserNumber(${studysite.id});"
                           class="${showTime eq true ? "validate-NOTEMPTY":""}"/>
                    <ul id="userNumberError_${studysite.id}" style="display:none;" class="errors">
                        <li><spring:message code='participant.unique_userNumber'
                                            text='participant.unique_userNumber'/></li>
                    </ul>
                    <ul id="UserPatternError_${studysite.id}" style="display:none;" class="errors">
                        <li><spring:message code='participant.usernumber_pattern'
                                            text='participant.usernumber_pattern'/></li>
                    </ul>
                </div>
            </div>
            <div class="row">
                <div class="label">
                    <span class="required-indicator">*&nbsp;&nbsp; </span> PIN number
                </div>
                <div class="value">
                    <input type="password" name="participantPinNumber_${studysite.id}"
                           value="${studyParticipantAssignment.participant.pinNumber}"
                           id="participant.pinNumber_${studysite.id}"
                           onblur="checkParticipantPinNumber(${studysite.id});" title="Pin number"
                           class="${showTime eq true ? "validate-NOTEMPTY":""}"/>
                    <ul id="PinPatternError_${studysite.id}" style="display:none;" class="errors">
                        <li><spring:message code='participant.pinnumber_pattern'
                                            text='participant.pinnumber_pattern'/></li>
                    </ul>
                </div>
            </div>
            <div class="row">
                <div class="label">
                    <span class="required-indicator">*&nbsp;&nbsp; </span> Preferred call time
                </div>
                <div id="ivrs_${studysite.id}" class="value" style="${showTime eq true ? "":"display:none"}">
                    <table>
                        <tr>
                            <td>
                                <c:set var="blackout"><tags:message code="callout.blackouttime"/></c:set>
                                <select id="call_hour_${studysite.id}" name="call_hour_${studysite.id}"
                                        title="Hour"
                                        class="${showTime eq true ? "validate-NOTEMPTY":""}"
                                        onblur="validateCalloutTime(${studysite.id},'${blackoutStartTime}','${blackoutEndTime}');">
                                    <option value="" ${studyParticipantAssignment.callHour eq "" ? "selected='selected'" : " "} >
                                        Hr
                                    </option>
                                    <c:forEach items="${hours}" var="hour">
                                        <c:set var="hr" value="${hour}"/>
                                        <c:if test="${hr/10<1}"><c:set var="hr" value="0${hour}"/></c:if>
                                        <option value="${hour}" ${studyParticipantAssignment.callHour eq hour ? "selected='selected'" : " "} >${hr}</option>
                                    </c:forEach>
                                </select>&nbsp;
                                <select id="call_minute_${studysite.id}" name="call_minute_${studysite.id}"
                                        title="Minute"
                                        class="${showTime eq true ? "validate-NOTEMPTY":""}"
                                        onblur="validateCalloutTime(${studysite.id},'${blackoutStartTime}','${blackoutEndTime}');">
                                    <option value="" ${studyParticipantAssignment.callMinute eq "" ? "selected='selected'" : " "} >
                                        Min
                                    </option>
                                    <c:forEach items="${minutes}" var="minute">
                                        <c:set var="min" value="${minute}"/>
                                        <c:if test="${min/10<1}"><c:set var="min" value="0${minute}"/></c:if>
                                        <option value="${minute}" ${studyParticipantAssignment.callMinute eq minute ? "selected='selected'" : " "} >${min}</option>
                                    </c:forEach>
                                </select>&nbsp;
                                <select id="call_ampm_${studysite.id}" name="call_ampm_${studysite.id}"
                                        title="AM PM"
                                        class="${showTime eq true ? "validate-NOTEMPTY":""}"
                                        onblur="validateCalloutTime(${studysite.id},'${blackoutStartTime}','${blackoutEndTime}');">
                                    <option value="am" ${studyParticipantAssignment.callAmPm eq "am" ? "selected='selected'" : " "} >
                                        am
                                    </option>
                                    <option value="pm" ${studyParticipantAssignment.callAmPm eq "pm" ? "selected='selected'" : " "} >
                                        pm
                                    </option>
                                </select>&nbsp;&nbsp;&nbsp;
                            </td>
                        </tr>
                    </table>
                    <ul id="preferred.calltime.error_${studysite.id}" style="display:none;"
                        name="preferredcalltime.error_${studysite.id}" class="errors">
                        <li><spring:message code='callout.blackouttime'
                                            text='callout.blackouttime'
                                            arguments="${blackoutStartTime},${blackoutEndTime}"/></li>
                    </ul>
                </div>
            </div>
            <div class="row">
                <div class="label">
                    <span class="required-indicator">*&nbsp;&nbsp; </span> Preferred language
                </div>
                <div class="value">
                    <select id="ivrs_lang_${studysite.id}" name="ivrs_lang_${studysite.id}"
                            title="Preferred language">
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
                </div>
            </div>
            <div class="row">
                <div class="label">
                    IVRS call-out
                </div>
                <div class="value">
                    <input type="checkbox" name="call_${studysite.id}" value="true"
                           id="call_${studysite.id}" ${studyParticipantAssignment.studyParticipantModes[0].call ? "checked" : " "}/>
                    check if the system should call the participant. <br>
                </div>
            </div>
        </td>
        <td valign="top"><br><br> <br><br>

            <div class="row">
                <div class="label">
                    <span class="required-indicator">*&nbsp;&nbsp; </span> Confirm
                </div>
                <div class="value">
                    <input type="password" name="participantPinNumberConfirm_${studysite.id}"
                           value="${studyParticipantAssignment.participant.confirmPinNumber}"
                           id="participant.confirmPinNumber_${studysite.id}"
                           onblur="checkPinMatch(${studysite.id});" title="Confirm Pin number"
                           class="${showTime eq true ? "validate-NOTEMPTY":""}"/>
                    <ul id="confirmPinError_${studysite.id}" style="display:none;" class="errors">
                        <li><spring:message code='participant.confirm_pinnumber'
                                            text='participant.confirm_pinnumber'/></li>
                    </ul>
                </div>
            </div>

            <div class="row">
                <div class="label">
                    <span class="required-indicator">*&nbsp; </span>Time zone
                </div>
                <div class="value">
                    <select id="call_timeZone_${studysite.id}" name="call_timeZone_${studysite.id}"
                            title="Time zone" class="${showTime eq true ? "validate-NOTEMPTY":""}">
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
                </div>
            </div>
        </td>
    </tr>
</table>

</td>
</tr>
<tr>

    <td colspan="2">
        <c:forEach items="${studysite.study.studyModes}" var="studyMode">
            <c:if test="${studyMode.mode.name eq 'HOMEBOOKLET'}">
                <c:choose>
                    <c:when test="${hbook}">
                        <input type="checkbox" name="responseModes" checked="true" value="HOMEBOOKLET"
                               onclick="javascript:showOrHideEmail(this.checked, '${studyMode.mode.name}', '${studysite.id}');"/>&nbsp;&nbsp;&nbsp;Paper
                        form
                        <br>
                    </c:when>
                    <c:otherwise>
                        <input type="checkbox" name="responseModes" value="HOMEBOOKLET"
                               onclick="javascript:showOrHideEmail(this.checked, '${studyMode.mode.name}', '${studysite.id}');"/>&nbsp;&nbsp;&nbsp;Paper
                        form
                        <br>
                    </c:otherwise>
                </c:choose>
            </c:if>
        </c:forEach>
    </td>

</tr>
<tr>

    <td colspan="2">


        <table border="0" cellpadding="0" cellspacing="0">

            <tr id="home_paper_${studysite.id}" style="${showBook eq true ? "":"display:none"}">
                <td>
                    <div class="row">
                        <div class="label">
                            <span class="required-indicator">*&nbsp;&nbsp; </span> Preferred language
                        </div>
                        <div class="value">
                            <select id="home_paper_lang_${studysite.id}"
                                    name="home_paper_lang_${studysite.id}">
                                <option value="" ${studyParticipantAssignment.homePaperLanguage eq "" ? "selected='selected'" : " "} >
                                    Please select
                                </option>
                                <option value="ENGLISH" ${studyParticipantAssignment.homePaperLanguage eq "ENGLISH" ? "selected='selected'" : " "} >
                                    English
                                </option>
                                <option value="SPANISH" ${studyParticipantAssignment.homePaperLanguage eq "SPANISH" ? "selected='selected'" : " "} >
                                    Spanish
                                </option>
                            </select>
                        </div>
                    </div>
                </td>
                <td valign="top"><br><br>

                </td>
            </tr>
        </table>


    </td>
</tr>
</c:if>
</table>
</td>
<td></td>
</tr>





