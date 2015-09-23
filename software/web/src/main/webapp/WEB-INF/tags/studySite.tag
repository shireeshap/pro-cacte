<%@ attribute name="studyParticipantAssignment" type="gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment" %>
<%@ attribute name="participant" type="gov.nih.nci.ctcae.core.domain.Participant" %>
<%@ attribute name="isEdit" %>
<%@ attribute name="selected" %>
<%@ attribute name="isCreateFlow" type="java.lang.Boolean"%>
<%@ tag import="java.util.Date" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ attribute name="studysite" type="gov.nih.nci.ctcae.core.domain.StudySite" required="true" %>

<style type="text/css">
	div.row div.ssLabel {
    font-weight: bold;
    float: left;
    margin-left: 0.1em;
    text-align: right;
    width: 12em;
	}
	div.row div.ssValue {
    font-weight: normal;
    margin-left: 13em;
    text-align: left;
	}
</style>

<c:forEach items="${studysite.study.crfs}" var="crf">
    <c:if test="${crf.status eq 'RELEASED' and crf.childCrf eq null}">
        <c:set var="hasforms" value="true"/>
    </c:if>
</c:forEach>

<c:if test="${studyParticipantAssignment ne null && studyParticipantAssignment.status.displayName eq 'OffStudy'}">
    <c:set var="offStudy" value="true"/>
</c:if>

<tr>
    <td align="center">
        <input type="radio" name="studySites" value="${studysite.id}"
               onclick="javascript:CP_NS.showForms(this, '${studysite.id}')"
               <c:if test="${selected}">checked</c:if>/>
    </td>
    <td>
        ${studysite.study.displayName}
    </td>
    <c:set var="hweb" value="false"/>
    <c:set var="ivrs" value="false"/>
    <c:set var="hbook" value="false"/>
    <c:forEach items="${command.selectedStudyParticipantAssignment.studyParticipantModes}" var="app" varStatus="status">
        <c:if test="${app.mode.name eq 'HOMEWEB' or app.mode.name eq 'CLINICWEB'}"><c:set var="hweb" value="true"/></c:if>
        <c:if test="${app.mode.name eq 'IVRS'}"><c:set var="ivrs" value="true"/></c:if>
        <c:if test="${app.mode.name eq 'HOMEBOOKLET'}"><c:set var="hbook" value="true"/></c:if>
    </c:forEach>
    
    <c:set var="studyHweb" value="false"/>
    <c:set var="studyIvrs" value="false"/>
    <c:set var="studyHbook" value="false"/>
    <c:forEach items="${studysite.study.studyModes}" var="studyMode">
        <c:if test="${studyMode.mode.name eq 'HOMEWEB' or studyMode.mode.name eq 'CLINICWEB'}"><c:set var="studyHweb" value="true"/></c:if>
        <c:if test="${studyMode.mode.name eq 'IVRS'}"><c:set var="studyIvrs" value="true"/></c:if>
        <c:if test="${studyMode.mode.name eq 'HOMEBOOKLET'}"><c:set var="studyHbook" value="true"/></c:if>
    </c:forEach>
    <c:if test="${isEdit}">
        <td>
        	<c:choose>
                <c:when test="${studyParticipantAssignment.offTreatmentDate ne null}">
                    <tags:formatDate value="${studyParticipantAssignment.offTreatmentDate}"/><br>
                </c:when>
                <c:otherwise>
                    <a href="javascript:CP.participantOffStudy(${studyParticipantAssignment.id})">Off study date...</a>
                    <br>
                    <c:if test="${studyParticipantAssignment.onHoldTreatmentDate eq null}">
                        <a id="putOnHold" href="javascript:CP.participantOnHold('${param.id}', null)">Treatment on
                            hold</a> <br>
                    </c:if>
                    <c:if test="${studyParticipantAssignment.onHoldTreatmentDate ne null}">
                        Treatment on-hold from <tags:formatDate
                            value="${studyParticipantAssignment.onHoldTreatmentDate}"/><br>
                        <a id="putOffHold" href="javascript:CP.participantOffHold('${param.id}', null,0)"> Put
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

    <td class="data" align="right" width="28%">
        <span class="required-indicator">*&nbsp;&nbsp;</span><b> Participant study identifier</b>
    </td>
    <td width="50%" class="data">
        &nbsp;&nbsp;
        <input type="text"
               name="participantStudyIdentifier_${studysite.id}"
               value="${studyParticipantAssignment.studyParticipantIdentifier}"
               title="identifier"
               id="participantStudyIdentifier_${studysite.id}"
               onblur="CP.checkParticipantStudyIdentifier(${studysite.study.id},${studysite.id});"
               class="${not selected?'':'validate-NOTEMPTY'}"/>
        <ul id="uniqueError_${studysite.id}" style="display:none" class="errors">
            <li><spring:message code='participant.unique_assignedIdentifier'
                                text='participant.unique_assignedIdentifier'/></li>
        </ul>
        <ul id="participantStudyIdentifier_${studysite.id}.error" style="display:none;" class="errors">
            <li><spring:message code='special.character.message'
                                text='special.character.message'/></li>
        </ul>
    </td>
    <td></td>
</tr>

<tr>
    <c:choose>
        <c:when test="${offStudy}">
            <td align="right" class="data" width="20%">
                <b><span class="required-indicator">*&nbsp;&nbsp;</span><spring:message
                        code="study.label.arm"/></b>
            </td>
            <td class="data">   &nbsp;&nbsp;
                <input type="hidden" name="arm_${studysite.id}"
                       value="${studyParticipantAssignment.arm.id}">${studyParticipantAssignment.arm.title}
            </td>

        </c:when>
        <c:otherwise>
                <c:choose>
                    <c:when test="${fn:length(studysite.study.nonDefaultArms) > 1}">
                        <td class="data" align="right" width="20%">
                            <b><span class="required-indicator">*&nbsp;&nbsp;</span><spring:message
                                    code="study.label.arm"/></b>
                        </td>
                        <td width="50%" class="data" align="left">
                            &nbsp;&nbsp;
                            <select name="arm_${studysite.id}" title="arm"
                                    id="arm_${studysite.id}" onchange="confirmSelection(${isCreateFlow})">
                                <c:choose>
                                    <c:when test="${command.onDefaultArm}">
                                        <option value="${studyParticipantAssignment.arm.id}">Please select</option>
                                    </c:when>
                                    <c:otherwise>
                                        <option value="">Please select</option>
                                    </c:otherwise>
                                </c:choose>
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
                        <c:choose>
                            <c:when test="${command.onDefaultArm}">
                                <td class="data" align="right" width="20%" style="display:none">
                                    <b><span class="required-indicator">&nbsp;&nbsp;</span><spring:message
                                            code="study.label.arm"/></b>
                                </td>
                                <td width="50%" class="data" style="display:none">
                                    &nbsp;&nbsp;
                                    <select name="arm_${studysite.id}" title="arm"
                                            id="arm_${studysite.id}" onchange="confirmSelection(${isCreateFlow})">
                                        <option value="${studyParticipantAssignment.arm.id}" selected>Please select
                                        </option>
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
                            	<c:choose>
                            		<c:when test="${not empty studysite.study.nonDefaultArms[0].title}">
                            			<td align="right" class="data" width="20%">
		                                    <b><span class="required-indicator">*&nbsp;&nbsp;</span><spring:message
		                                            code="study.label.arm"/></b>
		                                </td>
		                                <td class="data">   &nbsp;&nbsp;
		                                    <input type="hidden" name="arm_${studysite.id}"
		                                           value="${studysite.study.nonDefaultArms[0].id}">${studysite.study.nonDefaultArms[0].title}
		                                </td>
                            		</c:when>
                            		<c:otherwise>
                            			<td colspan="2"></td>
                            		</c:otherwise>
                            	</c:choose>
                            </c:otherwise>
                        </c:choose>
                    </c:otherwise>
                </c:choose>
            <td></td>
        </c:otherwise>
    </c:choose>
</tr>

<tr>

    <td align="right" class="data" width="20%">
        <b><spring:message code="participant.label.startdate"/></b>
    </td>
    <td class="data" width="50%">  &nbsp;&nbsp;
        <c:choose>
            <c:when test="${selected}">
                <tags:renderDate
                        propertyName="study_date_${studysite.id}"
                        doNotshowLabel="true" required="true" onchange="confirmDateselection('study_date_${studysite.id}', ${isCreateFlow})"
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
    <td></td>

</tr>
<c:if test="${offStudy}">
    <p align="center">
        <font color="#990000"><br><b> This participant has been taken off study. </b></font>
    </p>
</c:if>

<c:if test="${fn:length(studysite.study.studyModes) > 0}">
<tr>
    <td colspan="3">
    &nbsp;&nbsp;&nbsp; 
    <chrome:division title="Reporting options">
        <br />
            <c:if test="${studyHweb}">
                    <c:choose>
                        <c:when test="${hweb}">
                            &nbsp;&nbsp;&nbsp;&nbsp; <input type="checkbox" name="responseModes" checked="true" value="HOMEWEB"
                                                            onclick="javascript:CP_NS.showOrHideEmail(this.checked, 'HOMEWEB', '${studysite.id}');"/>&nbsp;&nbsp;&nbsp;Web
                        </c:when>
                        <c:otherwise>
                            &nbsp;&nbsp;&nbsp;&nbsp; <input type="checkbox" name="responseModes" value="HOMEWEB"
                                                            onclick="javascript:CP_NS.showOrHideEmail(this.checked, 'HOMEWEB', '${studysite.id}');"/>&nbsp;&nbsp;&nbsp;Web
                        </c:otherwise>
                    </c:choose>
                    <br /><c:set var="isWebAdded" value="true"/>
            </c:if>
    </chrome:division>
    </td>
</tr>
<tr>
    <td colspan="2">

        <div id="passwordfields" style="margin-left:45px">
            <table border="0" cellpadding="0" cellspacing="0">

                <tr id="web_${studysite.id}"
                    style="${showWeb eq true ? "":"display:none"}">
                    <td>
                        <c:choose>
                            <c:when test="${command.readOnlyUserName}">
                                <div class="row">
                                    <div class="ssLabel"><spring:message
                                            code="participant.label.username"/>&nbsp;</div>
                                    <div class="ssValue">${command.participant.user.username}</div>
                                    <input type="hidden" id="participant.user.username"
                                           name="participant.user.username"
                                           value="${command.participant.user.username}">
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="row">
                                    <div class="ssLabel"><span
                                            class="required-indicator">*&nbsp;&nbsp; </span><spring:message
                                            code="participant.label.username"/>&nbsp;</div>
                                    <div class="ssValue">
                                        <input type="text" name="participant.username_${studysite.id}"
                                               value="${command.participant.user.username}"
                                               id="participant.username_${studysite.id}" title="Username"
                                               onblur="CP.checkParticipantUserName(${studysite.id});"
                                               class="${showWeb and selected ? "validate-NOTEMPTY":""}"/>
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

                                    </div>

                                </div>

                            </c:otherwise>
                        </c:choose>
                        
                        <c:choose>
							<c:when test="${not isEdit}">
								<div class="row">
		                            <div class="ssLabel"><span class="required-indicator">*&nbsp;&nbsp; </span>
		                            	<spring:message code="participant.label.password"/>&nbsp;
		                            </div>
		                            <div class="ssValue">
		                                <input type="password" name="participant.password_${studysite.id}"
		                                       value="${participant.user.password}"
		                                       id="participant.password_${studysite.id}"
		                                       onblur="CP.checkPasswordPolicy(${studysite.id});" title="Password"
		                                       class="${showWeb and selected ? "validate-NOTEMPTY":""}"/>
		                                <ul id="passwordError_${studysite.id}" style="display:none; padding-left:12em " class="errors">
		                                    <li id="passwordError1_${studysite.id}"></li>
		                                </ul>
		                            </div>
		                        </div>
		                        <div class="row">
		                            <div class="ssLabel"><span class="required-indicator">*&nbsp;&nbsp; </span><spring:message
		                                    code="participant.label.confirmpassword"/>&nbsp;</div>
		                            <div class="ssValue">
		                                <input type="password" name="participant.confirmPassword_${studysite.id}"
		                                       value="${participant.user.password}"
		                                       id="participant.confirmPassword_${studysite.id}"
		                                       onblur="CP.checkPasswordMatch(${studysite.id});" title="Confirm"
		                                       class="${showWeb and selected ? "validate-NOTEMPTY":""}"/>
		                                <ul id="passwordErrorConfirm_${studysite.id}" style="display:none; padding-left:12em "
		                                    class="errors">
		                                    <li id="passwordErrorConfirm1_${studysite.id}"></li>
		                                </ul>
		                            </div>
		                    	</div>
							</c:when>
							<c:otherwise>
		                        <div id="updatePasswordDiv">
		                            <div class="row">
			                            <div class="ssLabel"><span class="required-indicator">*&nbsp;&nbsp; </span>
			                            	<spring:message code="participant.label.password"/>&nbsp;
			                            </div>
			                            <div class="ssValue">
			                            	<tags:button color="blue" type="button" value="Update Password"
			                            		onclick="CP_NS.showPasswordDiv(${studysite.id})" size="small"/>
			                            </div>
		                            </div>
		                        </div>
		                        
		                        <div id="passwordDiv" style="display:none">
			                        <div class="row">
			                            <div class="ssLabel"><span class="required-indicator">*&nbsp;&nbsp; </span>
			                            	<spring:message code="participant.label.password"/>&nbsp;
			                            </div>
			                            <div class="ssValue">
			                                <input type="password" name="participant.password_${studysite.id}"
			                                       value="${participant.user.password}"
			                                       id="participant.password_${studysite.id}"
			                                       onblur="CP.checkPasswordPolicy(${studysite.id});" title="Password"
			                                       class="${showWeb and selected ? "validate-NOTEMPTY":""}"/>
			                                <ul id="passwordError_${studysite.id}" style="display:none; padding-left:12em " class="errors">
			                                    <li id="passwordError1_${studysite.id}"></li>
			                                </ul>
			                            </div>
			                        </div>
			                        
			                        <div class="row">
			                            <div class="ssLabel"><span class="required-indicator">*&nbsp;&nbsp; </span><spring:message
			                                    code="participant.label.confirmpassword"/>&nbsp;</div>
			                            <div class="ssValue">
			                                <input type="password" name="participant.confirmPassword_${studysite.id}"
			                                       value="${participant.user.password}"
			                                       id="participant.confirmPassword_${studysite.id}"
			                                       onblur="CP.checkPasswordMatch(${studysite.id});" title="Confirm"
			                                       class="${showWeb and selected ? "validate-NOTEMPTY":""}"/>
			                                <ul id="passwordErrorConfirm_${studysite.id}" style="display:none; padding-left:12em "
			                                    class="errors">
			                                    <li id="passwordErrorConfirm1_${studysite.id}"></li>
			                                </ul>
			                                <tags:button color="red" type="button" value="Hide Password"
			                                	onclick="CP_NS.hidePasswordDiv(${studysite.id})" size="small"/>
			                            </div>
			                        </div>
		                        </div>
                        
                        	</c:otherwise>
                        </c:choose>
                        
                        <script type="text/javascript">
                        	jQuery(document).ready(function() {
		                        jQuery("participant\\.confirmPassword_"+${studysite.id}).focus(function() {
		                    	    jQuery('#passwordErrorConfirm_'+${studysite.id}).hide();
		                        });
                        	});
                        </script>

						<c:choose>
							<c:when test="${!empty studyParticipantAssignment.homeWebLanguage and studyParticipantAssignment.homeWebLanguage != ''}">
								<c:set var="langPref" value="${studyParticipantAssignment.homeWebLanguage}" />
							</c:when>
							<c:otherwise>
								<c:set var="langPref" value="${studyParticipantAssignment.clinicWebLanguage}" />
							</c:otherwise>	
						</c:choose>
                        <div class="row">
                            <div class="ssLabel">
                                <span class="required-indicator">*&nbsp;&nbsp; </span> Preferred language
                            </div>
                            <div class="ssValue">
                                <select id="home_web_lang_${studysite.id}" name="home_web_lang_${studysite.id}" class="${showWeb and selected ? "validate-NOTEMPTY":""}" title="Preferred language">
                                    <option value="" ${langPref eq "" ? "selected='selected'" : " "} >
                                        Please select
                                    </option>
                                    <option value="ENGLISH" ${langPref eq "ENGLISH" ? "selected='selected'" : " "} >
                                        English
                                    </option>
                                    <option value="SPANISH" ${langPref eq "SPANISH" ? "selected='selected'" : " "} >
                                        Spanish
                                    </option>
                                </select>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="ssLabel">
                                Get email reminders
                            </div>
                            <div class="ssValue">
                            	<input type="checkbox" name="email_${studysite.id}" value="true"
                                   onclick="javascript:CP_NS.showEmail(${studysite.id}, this.checked);"
                                   id="email_${studysite.id}" ${studyParticipantAssignment.studyParticipantModes[0].email ? "checked" : " "} />
                            </div>
                        </div>
                    </td>
                    
                    <td>
                    </td>
                </tr>
            </table>

        </div>
    </td>
    <td></td>
</tr>

<tr>
    <td colspan="3">
            <c:if test="${studyIvrs}">
                <c:choose>
                    <c:when test="${ivrs}">
                        &nbsp;&nbsp;&nbsp;&nbsp; <input type="checkbox" name="responseModes" checked="true" value="IVRS"
                                                        onclick="javascript:CP_NS.showOrHideEmail(this.checked, 'IVRS', '${studysite.id}');CP_NS.populateDefaultUserNumber(${studysite.id});"/>&nbsp;&nbsp;&nbsp;IVRS/Automated
                        Telephone <br>
                    </c:when>
                    <c:otherwise>
                        &nbsp;&nbsp;&nbsp;&nbsp; <input type="checkbox" name="responseModes" value="IVRS"
                                                        onclick="javascript:CP_NS.showOrHideEmail(this.checked, 'IVRS', '${studysite.id}');CP_NS.populateDefaultUserNumber(${studysite.id});"/>&nbsp;&nbsp;&nbsp;IVRS/Automated
                        Telephone <br>
                    </c:otherwise>
                </c:choose>
            </c:if>
    </td>
</tr>

<tr id="c1_${studysite.id}" style="${showTime eq true ? "":"display:none"}">
<td colspan="2">
<div style="margin-left:45px">
<table border="0">
<tr>
<td>
    <div class="row">
        <div class="ssLabel">
            <span class="required-indicator">*&nbsp;&nbsp; </span> IVRS user id
        </div>
        <div class="ssValue">
            <input type="text" name="participantUserNumber_${studysite.id}"
                   value="${studyParticipantAssignment.participant.userNumber}"
                   id="participant.userNumber_${studysite.id}" title="User Number"
                   onblur="CP.checkParticipantUserNumber(${studysite.id});"
                   class="${showTime eq true ? "validate-NOTEMPTY":""}"/> ( 10 digits )
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
    
    <c:choose>
		<c:when test="${not isEdit}">
			<div class="row">
		        <div class="ssLabel">
		            <span class="required-indicator">*&nbsp;&nbsp; </span> PIN number
		        </div>
		        <div class="ssValue">
		            <input type="password" name="participantPinNumber_${studysite.id}"
		                   value="${studyParticipantAssignment.participant.pinNumber}"
		                   id="participant.pinNumber_${studysite.id}"
		                   onblur="CP_NS.checkParticipantPinNumber(${studysite.id});" title="Pin number"
		                   class="${showTime eq true ? "validate-NOTEMPTY":""}"/>
		            <ul id="PinPatternError_${studysite.id}" style="display:none;" class="errors">
		                <li><spring:message code='participant.pinnumber_pattern'
		                                    text='participant.pinnumber_pattern'/></li>
		            </ul>
		        </div>
    		</div>
		    <div class="row">
		        <div class="ssLabel">
		            <span class="required-indicator">*&nbsp;&nbsp; </span> Confirm
		        </div>
		        <div class="ssValue">
		            <input type="password" name="participantPinNumberConfirm_${studysite.id}"
		                   value="${studyParticipantAssignment.participant.confirmPinNumber}"
		                   id="participant.confirmPinNumber_${studysite.id}"
		                   onblur="CP.checkPinMatch(${studysite.id});" title="Confirm Pin number"
		                   class="${showTime eq true ? "validate-NOTEMPTY":""}"/>
		            <ul id="confirmPinError_${studysite.id}" style="display:none;" class="errors">
		                <li><spring:message code='participant.confirm_pinnumber'
		                                    text='participant.confirm_pinnumber'/></li>
		            </ul>
		        </div>
		    </div>
		</c:when>
		
		<c:otherwise>
		    <div id="updateUserPinDiv" class="row">
		        <div class="ssLabel">
		            <span class="required-indicator">*&nbsp;&nbsp; </span> PIN number
		        </div>
		        <div class="ssValue">
		        	 <tags:button color="blue" type="button" value="Update Pin"
		                 onclick="CP_NS.showUserPinDiv(${studysite.id});" size="small"/>
		        </div>
		    </div>
		    <div id="userPinDiv" style="display:none">
			    <div class="row">
			        <div class="ssLabel">
			            <span class="required-indicator">*&nbsp;&nbsp; </span> PIN number
			        </div>
			        <div class="ssValue">
			            <input type="password" name="participantPinNumber_${studysite.id}"
			                   value="${studyParticipantAssignment.participant.pinNumber}"
			                   id="participant.pinNumber_${studysite.id}"
			                   onblur="CP_NS.checkParticipantPinNumber(${studysite.id});" title="Pin number"
			                   class="${showTime eq true ? "validate-NOTEMPTY":""}"/>
			            <ul id="PinPatternError_${studysite.id}" style="display:none;" class="errors">
			                <li><spring:message code='participant.pinnumber_pattern'
			                                    text='participant.pinnumber_pattern'/></li>
			            </ul>
			        </div>
			    </div>
			    <div class="row">
			        <div class="ssLabel">
			            <span class="required-indicator">*&nbsp;&nbsp; </span> Confirm
			        </div>
			        <div class="ssValue">
			            <input type="password" name="participantPinNumberConfirm_${studysite.id}"
			                   value="${studyParticipantAssignment.participant.confirmPinNumber}"
			                   id="participant.confirmPinNumber_${studysite.id}"
			                   onblur="CP.checkPinMatch(${studysite.id});" title="Confirm Pin number"
			                   class="${showTime eq true ? "validate-NOTEMPTY":""}"/>
			            <ul id="confirmPinError_${studysite.id}" style="display:none;" class="errors">
			                <li><spring:message code='participant.confirm_pinnumber'
			                                    text='participant.confirm_pinnumber'/></li>
			            </ul>
			            <tags:button color="red" type="button"  value="Hide Pin" 
			            	onclick="CP_NS.hideUserPinDiv(${studysite.id});" size="small"/>
			        </div>
			    </div>
		    </div>
    	</c:otherwise>
	</c:choose>
	
    <div class="row">
        <div class="ssLabel">
            IVRS call-out
        </div>
        <div class="ssValue">
            <input type="checkbox" name="call_${studysite.id}" value="true"
                   onclick="javascript:CP_NS.showPhone(${studysite.id}, this.checked);"
                   id="call_${studysite.id}" ${studyParticipantAssignment.studyParticipantModes[0].call ? "checked" : " "}/>
            check if the system should call the participant. <br>
        </div>
    </div>
    
    <div id="callTime_${studysite.id}" class="row" style="${studyParticipantAssignment.studyParticipantModes[0].call ? '':'display:none'}">
        <div class="ssLabel">
            <span class="required-indicator">*&nbsp; </span> Preferred call time
        </div>
        <div id="ivrs_${studysite.id}" class="ssValue" style="${showTime eq true ? "":"display:none"}">
            <table>
                <tr>
                    <td>
                        <c:set var="blackout"><tags:message code="callout.blackouttime"/></c:set>
                        <select id="call_hour_${studysite.id}" name="call_hour_${studysite.id}"
                                title="Hour"
                                class="${studyParticipantAssignment.studyParticipantModes[0].call eq true ? "validate-NOTEMPTY":""}"
                                onblur="CP.validateCalloutTime(${studysite.id},'${blackoutStartTime}','${blackoutEndTime}');">
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
                                class="${studyParticipantAssignment.studyParticipantModes[0].call eq true ? "validate-NOTEMPTY":""}"
                                onblur="CP.validateCalloutTime(${studysite.id},'${blackoutStartTime}','${blackoutEndTime}');">
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
                                class="${studyParticipantAssignment.studyParticipantModes[0].call eq true ? "validate-NOTEMPTY":""}"
                                onblur="CP.validateCalloutTime(${studysite.id},'${blackoutStartTime}','${blackoutEndTime}');">
                            <option value="am" ${studyParticipantAssignment.callAmPm eq "am" ? "selected='selected'" : " "} >
                                am
                            </option>
                            <option value="pm" ${studyParticipantAssignment.callAmPm eq "pm" ? "selected='selected'" : " "} >
                                pm
                            </option>
                        </select>&nbsp;
                    </td>
                </tr>
            </table>
            <ul id="preferred.calltime.error_${studysite.id}" style="display:none;" name="preferredcalltime.error_${studysite.id}" class="errors">
                <li><spring:message code='callout.blackouttime' text='callout.blackouttime' arguments="${blackoutStartTime},${blackoutEndTime}"/></li>
            </ul>
        </div>
    </div>
    <div id="ivrsLanguage_${studysite.id}" class="row" style="${studyParticipantAssignment.studyParticipantModes[0].call ? '':'display:none'}">
        <div class="ssLabel">
            <span class="required-indicator">*&nbsp;&nbsp; </span> Preferred language
        </div>
        <div class="ssValue">
            <select id="ivrs_lang_${studysite.id}" name="ivrs_lang_${studysite.id}"
                    title="Preferred language" class="${studyParticipantAssignment.studyParticipantModes[0].call eq true ? "validate-NOTEMPTY":""}">
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

</td>
<td valign="top"><br><br> <br><br>

</td>
</tr>

</table>
</div>
</td>
<td></td>
</tr>
<tr>
    <td colspan="2">
        <c:if test="${studyHbook}">
            <c:choose>
                <c:when test="${hbook}">
                    &nbsp;&nbsp;&nbsp;&nbsp; <input type="checkbox" name="responseModes" checked="true" value="HOMEBOOKLET"
                                                    onclick="javascript:CP_NS.showOrHideEmail(this.checked, 'HOMEBOOKLET', '${studysite.id}');"/>&nbsp;&nbsp;&nbsp;Paper form
                    <br />
                </c:when>
                <c:otherwise>
                    &nbsp;&nbsp;&nbsp;&nbsp; <input type="checkbox" name="responseModes" value="HOMEBOOKLET"
                                                    onclick="javascript:CP_NS.showOrHideEmail(this.checked, 'HOMEBOOKLET', '${studysite.id}');"/>&nbsp;&nbsp;&nbsp;Paper form
                    <br />
                </c:otherwise>
            </c:choose>
        </c:if>
    </td>
</tr>

<tr>
    <td colspan="2">
        <div style="margin-left:45px">
            <table border="0" cellpadding="0" cellspacing="0">

                <tr id="home_paper_${studysite.id}" style="${showBook eq true ? "":"display:none"}">
                    <td>
                        <div class="row">
                            <div class="ssLabel">
                                <span class="required-indicator">*&nbsp;&nbsp; </span> Preferred language
                            </div>
                            <div class="ssValue">
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
        </div>
    </td>
</tr>
</c:if>

</table>
</td>
<td width="19%"></td>
</tr>





