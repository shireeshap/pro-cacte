<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<tags:dwrJavascriptLink
        objects="uniqueParticipantIdentifier,uniqueParticipantUserNumber,uniqueParticipantEmailAddress,userNameValidation"/>
<tags:javascriptLink name="ui_fields_validation"/>
<tags:javascriptLink name="participant/createParticipant"/>

<html>
<head>

<tags:dwrJavascriptLink objects="organization"/>

<script type="text/javascript">
var CP = {
    isUserNameError: false,
    isUserIdError: false,
    isIdentifierError: false,
    isPasswordError: false,
    isConfirmPassError: false,
    isPhoneNumberError: false,
    isBlackoutCallTime: false,
    isEmailError: false
};
 
Event.observe(window, 'load', function() {
    <c:if test="${command.admin eq true && empty command.participant.studyParticipantAssignments}">
        try {
            new YUIAutoCompleter('organizationId-input', CP.getOrgs, CP.handleSelect);
            var orgName = "${command.selectedOrganization.displayName}";
            if (orgName != '') {
                $('organizationId-input').value = "${command.selectedOrganization.displayName}";
                $('organizationId-input').removeClassName('pending-search');
            }
        } catch(err) {
            document.println(err.getMessage());
        }
    </c:if>
    Event.observe('organizationId', 'change', function() {
        CP.getStudySites();
    })
  
    //need to make the ajax call, when there is validation error in create flow
    if (${hasValidationErrors}) {
        if ('${command.participant.id}' == '') {
            //populate the site value-
            if ($('organizationId-input')) {
                //admin user login
                $('organizationId-input').value = '${command.selectedOrganization.displayName}'
            }
            CP.getStudySites();
        }
    }
});

function confirmSelection(isCreateFlow){
	if(isCreateFlow == false){
		alert("Schedule cycles will be deleted and re-created on changing this!");	
	}
}

function confirmDateselection(siteId, isCreateFlow){
	var modifiedValue = jQuery('#'+siteId)[0].value.trim();
	var originalValue = jQuery('#'+siteId)[0].defaultValue.trim();
	 if (originalValue !== modifiedValue){
		confirmSelection(isCreateFlow);
	 }
}



// validation check for username
CP.checkParticipantUserName = function(siteId) {
    var participantId, userName, userId;
    participantId = "${param['id']}";
    userName = $('participant.username_'+siteId).value;
    if (participantId == "") {
        userId = "${userId}";
    }
    jQuery('#userNameError_'+siteId).hide();
    CP.isUserNameError = false;
    if (userName != "") {

        var iChars = "`~!@#$^&*+=[]\\\';,./{}|\":<>?";
        for (var i = 0; i < userName.length; i++) {
            if (iChars.indexOf(userName.charAt(i)) != -1) {
               $('userNameError_'+siteId).show();
               document.getElementById('userNameError_'+siteId).innerHTML = "Use of special character such as '&', '<', '>', ' \" ' is not allowed for usernames.";
               CP.isUserNameError = true;
               return;
            }
        }

        if (userName.length < 6) {
            jQuery('#userNameError_'+siteId).hide();
            jQuery('#userNameLengthError_'+siteId).show();
            CP.isUserNameError = true;
        }
        else {


            userNameValidation.validateDwrUniqueName(userName, userId, {callback: function(returnValue) {
                                                                                  if (returnValue) {
                                                                                      jQuery('#userNameError_' + siteId).show();
                                                                                      CP.isUserNameError = true;
                                                                                  }
                                                                                  else {
                                                                                      CP.isUserNameError = false;
                                                                                  }
                                                                              }});
            jQuery('#userNameLengthError_'+siteId).hide();
            return;
        }
    }
    else {
        jQuery('#userNameError_'+siteId).hide();
        jQuery('#userNameLengthError_'+siteId).hide();
        CP.isUserNameError = false;
    }
}

// validation check for email address
CP.checkParticipantEmail = function() {
    var emailAddress = $('participant.emailAddress').value;
    var participantId = "${param['id']}";
    if (participantId == "") {
        participantId = "${patientId}";
    }
    CP.isEmailError = false;
    jQuery('#userEmailError').hide();
    if (emailAddress != "") {
        uniqueParticipantEmailAddress.validateEmail(emailAddress, participantId, {callback: function(returnValue) {
                                                                                      if (returnValue) {
                                                                                           $('participant.emailAddress').removeClassName( "validate-EMAIL" );
                                                                                           $('participant.emailAddress').removeClassName( "validate-EMAIL&&NOTEMPTY" );
                                                                                           $('participant.emailAddress').addClassName("validate-EMAIL&&NONDUPLICATE");
                                                                                           CP.isEmailError = true;
                                                                                      }
                                                                                      else {
                                                                                    	  $('participant.emailAddress').removeClassName( "validate-EMAIL&&NONDUPLICATE" )
                                                                                    	  if(!$('participant.emailAddress').hasClassName('validate-EMAIL')){
                                                                                    		  $('participant.emailAddress').addClassName( "validate-EMAIL" )
                                                                                    	  }
                                                                                          CP.isEmailError = false;
                                                                                      }
                                                                                  }
        });
    }
}

//validation check for password policy
CP.checkPasswordPolicy = function(siteId) {
    var userPassword = $('participant.password_'+siteId).value;
    var userName;
    if(jQuery('#participant\\.username_'+siteId).length > 0) {
	    userName = $('participant.username_'+siteId).value;
    } else {
    	userName = $('participant.user.username').value;
    }
    var confirmPassword = $('participant.confirmPassword_'+siteId).value;
    if (confirmPassword != "") {
        CP.checkPasswordMatch(siteId);
    }
    if (userPassword != "") {
    	var inValid = CP_NS.isSpclCharForPassword('participant.password_'+siteId);
    	if(inValid) {
    		$('passwordError_'+siteId).show();
            document.getElementById('passwordError1_'+siteId).innerHTML = "Use of special character such as '&', '<', '>', ' \" ' is not allowed for password.";
            CP.isPasswordError = true;
    	} else {
    		$('passwordError_'+siteId).hide();
	        userNameValidation.validatePasswordPolicyDwr("PARTICIPANT", userPassword, userName, CP.passReturnValue);
	        return;
    	}
    }
    else {
        jQuery('#passwordError').hide();
        CP.isPasswordError = false;
    }
}

CP.passReturnValue = function(returnValue) {
    if (returnValue != "") {
        jQuery('#passwordError').show();
        if (document.getElementById('passwordError1')!=null) {
        document.getElementById('passwordError1').innerHTML = returnValue + "";
        }
        CP.isPasswordError = true;
    }
    else {
        jQuery('#passwordError').hide();
        CP.isPasswordError = false;
    }
}

CP.checkPinMatch = function(siteId) {
    var password = $('participant.pinNumber_' + siteId).value;
    var confirmPassword = $('participant.confirmPinNumber_' + siteId).value;
    jQuery('#confirmPinError_' + siteId).hide();
    CP.isConfirmPassError = false;
    if (password != "" && confirmPassword != "") {
        if (password != confirmPassword) {
            jQuery('#confirmPinError_' + siteId).show();
            CP.isConfirmPassError = true;
        }
    }
}

// validation check for confirm password
CP.checkPasswordMatch = function(siteId) {
    var password = $('participant.password_'+siteId).value;
    var confirmPassword = $('participant.confirmPassword_'+siteId).value;
    if (password != "" && confirmPassword != "") {
        if (password == confirmPassword) {
            jQuery('#passwordErrorConfirm_'+siteId).hide();
            CP.isConfirmPassError = false;
        } else {
        	$('participant.confirmPassword_'+siteId).value = "";
            jQuery('#passwordErrorConfirm_'+siteId).show();
            document.getElementById('passwordErrorConfirm1_'+siteId).innerHTML = "Password does not match confirm password.";
            CP.isConfirmPassError = true;
        }
    }
    else {
        jQuery('#passwordErrorConfirm_'+siteId).hide();
        CP.isConfirmPassError = false;
    }
}

CP.validateCalloutTime = function(siteId, startTime, endTime) {
    var participantId = "${param['id']}";
    if (participantId == "") {
        participantId = "${patientId}";
    }
    CP.isBlackoutCallTime = false;
    $('preferred.calltime.error_' + siteId).hide();
    var callHour = $('call_hour_' + siteId).value;
    var callMin = $('call_minute_' + siteId).value;
    var callAmPm = $('call_ampm_' + siteId).value;

    if (callHour == null || callHour == "" || callMin == null || callMin == "" || callAmPm == null || callAmPm == "") {
        // no action
    } else {
        var selectedTime = callHour + ":" + callMin + " " + callAmPm;
        var selectedDate = new Date("1/1/2007 " + selectedTime);
        var blackoutStartTime = new Date("1/1/2007 " + startTime);
        var blackoutEndTime = new Date("1/1/2007 " + endTime);
        var start = startTime.split(":");
        var end = endTime.split(":");
        var hhStart = parseInt(start[0], 10);
        var mmStart = parseInt(start[1], 10);
        var hhEnd = parseInt(end[0], 10);
        var mmEnd = parseInt(end[1], 10);

        var callHour = parseInt(callHour, 10);
        var callMin = parseInt(callMin, 10);

        if (callHour < 12 && callAmPm == 'pm') {
            var callHour = callHour + 12;
        }
        if (callHour == 12 && callAmPm == 'am') {
            callHour = callHour - 12;
        }
        var blockPreferredTime = false;
        var isSameDay = isSameDay1(hhStart, mmStart, hhEnd, mmEnd);

        //isSameDay == false means timings like 21:00 to 04:59
        if (!isSameDay) {
            if (callHour > hhStart || callHour < hhEnd) {
                var blockPreferredTime = true;
            } else if (callHour == hhStart) {
                if (callMin >= mmStart) {
                    var blockPreferredTime = true;
                }
            } else if (callHour == hhEnd) {
                if (callMin <= mmEnd) {
                    var blockPreferredTime = true;
                }
            }
        } else {
            //isSameDay == true means timings like 01:00 to 04:59
            if (callHour > hhStart && callHour < hhEnd) {
                var blockPreferredTime = true;
            } else if (callHour == hhStart || callHour == hhEnd) {
                if (callMin >= mmStart && callMin <= mmEnd) {
                    var blockPreferredTime = true;
                }
            }
        }

        if (blockPreferredTime) {
            $('preferred.calltime.error_' + siteId).show();
            CP.isBlackoutCallTime = true;
            //     $('call_hour_' + siteId).value = "";
            //    $('call_minute_' + siteId).value = "";
        }
    }

    function isSameDay1(hhStart, mmStart, hhEnd, mmEnd) {
        var isSameDay = false;
        if (hhStart > hhEnd) {
            isSameDay = false;
        } else if (hhStart < hhEnd) {
            isSameDay = true;
        } else {
            if (mmStart > mmEnd) {
                isSameDay = false;
            } else if (mmStart < mmEnd) {
                isSameDay = true;
            }
        }
        return isSameDay;
    }
}


CP.checkParticipantPhoneNumber = function() {
    var phoneNumberPattern = /^\(?([0-9]{3})\)?[-]?([0-9]{3})[-]?([0-9]{4})$/;
    var participantId = "${param['id']}";
    if (participantId == "") {
        participantId = "${patientId}";
    }
    var phoneNumber = $('participant.phoneNumber').value;
    if (phoneNumber != "") {
        if (!phoneNumberPattern.test(phoneNumber)) {
            jQuery('#PhonePatternError').show();
            jQuery('#phoneNumberError').hide();
            CP.isPhoneNumberError = true;
        }
        else {
            var re = /[-]/g;
            var nonFormattedPhoneNumber = phoneNumber.replace(re, "");
            phoneNumber = nonFormattedPhoneNumber.substring(0, 3) + "-" + nonFormattedPhoneNumber.substring(3, 6) + "-" + nonFormattedPhoneNumber.substring(6, 10);
            $('participant.phoneNumber').value = phoneNumber;
            uniqueParticipantUserNumber.validatePhoneNumber(phoneNumber, participantId, {callback:
                                                                                         function(returnValue) {
                                                                                             jQuery('#PhonePatternError').hide();
                                                                                             showOrHideErrorField(returnValue, '#phoneNumberError');
                                                                                             if (returnValue) {
                                                                                                 CP.isPhoneNumberError = true;
                                                                                             }
                                                                                             else {
                                                                                                 CP.isPhoneNumberError = false;
                                                                                                 //commenting out the part which defaults the user number to the phone number.
                                                                                                 //var userNumber = $('participant.userNumber_' + siteId).value;
                                                                                                 //if (userNumber == null || userNumber == "") {
                                                                                                 //    var re = /[-]/g;
                                                                                                 //    $('participant.userNumber_' + siteId).value = nonFormattedPhoneNumber;
                                                                                                 //}
                                                                                             }
                                                                                         }});
        }
    }
    else {
        jQuery('#phoneNumberError').hide();
        jQuery('#PhonePatternError').hide();
        CP.isPhoneNumberError = false;
    }
}

CP.userNumberPattern = /^[0-9]{10}$/;
// validation check for participant user number (IVRS)
CP.checkParticipantUserNumber = function(siteId) {
    var participantId = "${param['id']}";
    if (participantId == "") {
        participantId = "${patientId}";
    }
    var userNumber = $('participant.userNumber_' + siteId).value;
    if (userNumber != "") {
        if (!CP.userNumberPattern.test(userNumber)) {
            jQuery('#UserPatternError_' + siteId).show();
            jQuery('#userNumberError_' + siteId).hide();
            CP.isUserIdError = true;
        }
        else {
            uniqueParticipantUserNumber.validateUserNumber(userNumber, participantId, {callback:
                                                                                       function(returnValue) {
                                                                                           jQuery('#UserPatternError_' + siteId).hide();
                                                                                           showOrHideErrorField(returnValue, '#userNumberError_' + siteId);
                                                                                           if (returnValue) {
                                                                                               CP.isUserIdError = true;
                                                                                           }
                                                                                           else {
                                                                                               CP.isUserIdError = false;
                                                                                           }
                                                                                       }});
        }
    }
    else {
        jQuery('#userNumberError_' + siteId).hide();
        jQuery('#UserPatternError_' + siteId).hide();
        CP.isUserIdError = false;
    }
}


//validation check for participant study identifier
CP.checkParticipantStudyIdentifier = function(id, siteId) {
    var participantId = "${param['id']}";
    if (participantId == "") {
        participantId = "${patientId}";
        }
     var identifier = $('participantStudyIdentifier_' + siteId).value;
     if (CP_NS.isSpclChar('participantStudyIdentifier_' + siteId)) {
         return;
     }
     if (identifier != "") {
         uniqueParticipantIdentifier.validateUniqueParticipantIdentifier(id, identifier,
             participantId, {callback:
                             function(returnValue) {
                                 showOrHideErrorField(returnValue, '#uniqueError_' + siteId);
                                 if (returnValue) {
                                     CP.isIdentifierError = true;
                                 }
                                 else {
                                     CP.isIdentifierError = false;
                                 }
                             }});
         return;
     } else {
         jQuery('#uniqueError_' + siteId).hide();
         CP.isIdentifierError = false;
     }
}

//validation check for participant  identifier
CP.checkParticipantMrn = function() {
    var participantId = "${param['id']}";
    if (participantId == "") {
        participantId = "${patientId}";
    }
    var mrn = $('participant.assignedIdentifier').value;
    if (CP_NS.isSpclChar('participant.assignedIdentifier')) {
        return;
        }
    var siteId = $('organizationId').value;
    if (siteId != "" && mrn != "") {
        uniqueParticipantIdentifier.validateUniqueParticipantMrn(siteId, mrn,
             participantId, { callback:
                                  function(returnValue) {
                                      showOrHideErrorField(returnValue, '#uniqueError_mrn');
                                      if (returnValue) {
                                          CP.isIdentifierError = true;
                                      } else {
                                          CP.isIdentifierError = false;
                                      }
                                  }
                             });
         return;
     } else {
         jQuery('#uniqueError_mrn').hide();
         CP.isIdentifierError = false;
     }
}

participantOffHoldPost = function(index, date, cycle, day, action, participantId) {
    if (date == null || date == '') {
        alert('Please enter a date');
        return;
    }
    closeWindow();
    if (action == 'cancel') {
        getCalendar(index, "dir=refresh", participantId);
    } else {
    	jQuery('#ajaxLoadingImgDiv').show();
    	var request = new Ajax.Request("<c:url value='/pages/participant/addCrfSchedule'/>", {
	        onComplete:function(transport) {
	            jQuery('#ajaxLoadingImgDiv').hide();
	            if (transport.responseText == "getCalendar") {
	            	reloadStudyDetailsSection(participantId);
	            } else {
	                showConfirmationWindow(transport, 650, 210);
	            }
	         },
	         parameters:<tags:ajaxstandardparams/> +"&index=" + index + 
	         										"&offHoldDate=" + date + 
         											"&cycle=" + cycle  + 
         											"&day=" + day + 
         											"&action=" + action +
         											"&id=" + participantId,
	         method:'get'
         })
    }
};

CP.getStudySites = function() {
    var organizationId = $('organizationId').value;
    if (organizationId == '') {
        $("studysitestable").innerHTML = '';
        return;
    }
    var id = "${param['id']}";
    if (id == '') {
        id = '${command.participant.id}';
    }
    var request = new Ajax.Request("<c:url value='/pages/participant/displaystudysites'/>", {
        onComplete:function(transport) {
        var response = transport.responseText;
        $("studysitestable").update(response);
    },
    parameters:<tags:ajaxstandardparams/> + "&organizationId=" + organizationId + "&id=" + id,
    method:'get'
 })
}

CP.getOrgs = function(sQuery) {
    showIndicator("organizationId-input-indicator");
    var callbackProxy = function(results) {
        aResults = results;
    };
    var ALL_STUDY_SITES='GetAllStudySites';
    var callMetaData = { callback:callbackProxy, async:false};
    organization.matchOrganizationForStudySites(unescape(sQuery),ALL_STUDY_SITES, callMetaData);
    hideIndicator("organizationId-input-indicator");
    return aResults;
}

CP.handleSelect = function(stype, args) {
    var ele = args[0];
    var oData = args[2];
    if (oData == null) {
        ele.getInputEl().value = "(Begin typing here)";
        ele.getInputEl().addClassName('pending-search');
    } else {
        ele.getInputEl().value = oData.displayName;
        ele.getInputEl().removeClassName('pending-search');
        var id = ele.getInputEl().id;
        var hiddenInputId = id.substring(0, id.indexOf('-input'));
        $(hiddenInputId).value = oData.id;
        CP.getStudySites();
        jQuery('#studies').show();
     }
     CP.checkParticipantMrn();
}

CP.participantOffStudy = function(id) {
    var request = new Ajax.Request("<c:url value='/pages/participant/participantOffStudy'/>", {
        parameters:<tags:ajaxstandardparams/>+"&flow=participant&id=" + id  ,
        onComplete:function(transport) {
            showConfirmationWindow(transport, 600, 350);
        },
        method:'get'
    })
}

function reloadStudyDetailsSection(participantId){
	 var request = new Ajax.Request("<c:url value='/pages/participant/reloadSectionController'/>", {
         onComplete:function(transport) {
        	 jQuery("#studysitestablecontentmarker").empty();
        	 jQuery("#studysitestablecontentmarker").append(transport.responseText);
			 getCalendar(index, "dir=refresh", participantId);
         },
         parameters:<tags:ajaxstandardparams/> + "&id=" + participantId,
         method:'get'
     })
}

beginHoldOnSchedules = function(index, date, action, pid) {
    if (date == null || date == '') {
        alert('Please enter a date');
        return;
    }
    closeWindow();
    if (action == 'cancel') {
        getCalendar(index, "dir=refresh", pid);
    } else {
    	jQuery('#ajaxLoadingImgDiv').show();
        var request = new Ajax.Request("<c:url value='/pages/participant/addCrfSchedule'/>", {
            onComplete:function(transport) {
				 jQuery('#ajaxLoadingImgDiv').hide();
            	 if (transport.responseText == "getCalendar") {
	            	reloadStudyDetailsSection(pid);
	            } else {
	                showConfirmationWindow(transport, 650, 210);
	            }
            },
            parameters:<tags:ajaxstandardparams/> +"&index=" + index + 
            										"&date=" + date + 
            										"&action=" + action + 
            										"&id=" + pid,
            method:'get'
        })
    }
}


CP.participantOffHold = function(id, date, index) {
    var request = new Ajax.Request("<c:url value='/pages/participant/participantOffHold'/>", {
        onComplete:function(transport) {
                showConfirmationWindow(transport, 600, 200);
        },
        parameters:<tags:ajaxstandardparams/> + "&id=" + id + 
        										"&date=" + date + 
        										"&index=" + 0,
        method:'get'
    })
}


CP.participantOnHold = function(id, date) {
    var request = new Ajax.Request("<c:url value='/pages/participant/participantOnHold'/>", {
        parameters:<tags:ajaxstandardparams/>+"&flow=participant&id=" + id + "&date=" + date + "&index=" +0,
        onComplete:function(transport) {
            showConfirmationWindow(transport, 600, 200);
        },
        method:'get'
    })
   
}

function doPostProcessing() {
    CP.getStudySites();
}
</script>

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
        border-top: 0px solid #999999;
        border-right: 0px solid #999999;
    }

    td.data {
        vertical-align: top;
        border-bottom: 0px solid #999999;
        border-left: 0px solid #999999;
        padding-left: 5px;
    }
</style>
<!--[if IE]>
<style>
    div.row div.value {
        margin-left: 0;
    }
</style>
<![endif]-->
</head>
<body>
<tags:tabForm tab="${tab}" flow="${flow}" willSave="true" formName="createPartForm">
<jsp:attribute name="singleFields">
       <c:choose>
           <c:when test="${command.mode eq 'Y'}">
               <c:set var="required" value="true"/>
               <c:set var="maxLength" value="1"/>
           </c:when>
           <c:otherwise>
               <c:set var="required" value="false"/>
               <c:set var="maxLength" value="35"/>
           </c:otherwise>
       </c:choose>
           <tags:instructions code="participant.participant_details.top"/><br/>
           <chrome:division title="participant.label.site">
               <c:choose>
                   <c:when test="${not empty command.participant.studyParticipantAssignments}">
                       <input type="hidden" name="organizationId" id="organizationId"
                              value="${command.organizationId}"/>

                       <div class="row">
                           <div class="label"><spring:message code="study.label.clinical.staff"/>:&nbsp;</div>
                           <div class="value">${command.siteName}</div>
                       </div>
                   </c:when>
                   <c:otherwise>
                       <c:choose>
                           <c:when test="${command.admin}">

                               <%--<input name="organizationId" id="organizationId" class="validate-NOTEMPTY" style="display:none;"/>--%>
                               <div class="row">
                                   <div class="label">
                                       <tags:requiredIndicator/>
                                       <tags:message code='participant.label.site'/>
                                   </div>
                                   <div class="value">
                                       <form:input path="organizationId" id="organizationId"
                                                   cssClass="validate-NOTEMPTY"
                                                   title="Site"
                                                   cssStyle="display:none;"/>
                                       <tags:yuiAutocompleter inputName="organizationId-input"
                                                              value="${command.selectedOrganization.displayName}"
                                                              required="false"
                                                              hiddenInputName="organizationId"/>
                                           <%--<tags:renderAutocompleter propertyName="organizationId"--%>
                                           <%--displayName="participant.label.site"--%>
                                           <%--required="true" size="70"/>--%>
                                   </div>
                               </div>

                           </c:when>
                           <c:otherwise>
                               <c:choose>
                                   <c:when test="${fn:length(organizationsHavingStudySite) eq 1}">
                                       <div class="row">
                                           <div class="label"><spring:message code="participant.label.site"/>:</div>
                                           <div class="value">${organizationsHavingStudySite[0].desc}
                                               <input type="hidden" name="organizationId" id="organizationId"
                                                      value="${organizationsHavingStudySite[0].code}"/>
                                           </div>
                                       </div>
                                       <script type="text/javascript">
                                           Event.observe(window, 'load', function() {
                                               CP.getStudySites();
                                           });
                                       </script>
                                   </c:when>
                                   <c:otherwise>
                                       <tags:renderSelect propertyName="organizationId"
                                                          displayName="participant.label.site"
                                                          required="true" options="${organizationsHavingStudySite}"/>
                                   </c:otherwise>
                               </c:choose>
                           </c:otherwise>
                       </c:choose>
                   </c:otherwise>
               </c:choose>
           </chrome:division>

           <chrome:division title="participant.label.demographic_information">

               <c:if test="${command.mode eq 'Y'}">
                   <tags:instructions code="participant.create.deidentified"/>
               </c:if>
               <table border="0" style="width:100%">

                   <tr valign="top">
                       <td width="50%">
                           <tags:renderText propertyName="participant.firstName"
                                            displayName="participant.label.first_name"
                                            required="true" maxLength="${maxLength}" size="${maxLength}"
                                            onblur="CP_NS.isSpclChar('participant.firstName');"/>
                           <ul id="participant.firstName.error" style="display:none;left-padding:8em;" class="errors">
                               <li><spring:message code='special.character.message'
                                                   text='special.character.message'/></li>
                           </ul>
                           <c:if test="${command.mode eq 'N'}">
                               <tags:renderText propertyName="participant.middleName"
                                                displayName="participant.label.middle_name" maxLength="${maxLength}"
                                                size="${maxLength}" onblur="CP_NS.isSpclChar('participant.middleName');"/>
                               <ul id="participant.middleName.error" style="display:none;" class="errors">
                                   <li><spring:message code='special.character.message'
                                                       text='special.character.message'/></li>
                               </ul>
                           </c:if>
                           <tags:renderText propertyName="participant.lastName"
                                            displayName="participant.label.last_name"
                                            required="true" maxLength="${maxLength}" size="${maxLength}"
                                            onblur="CP_NS.isSpclChar('participant.lastName');"/>
                           <ul id="participant.lastName.error" style="display:none;" class="errors">
                               <li><spring:message code='special.character.message' text='special.character.message'/></li>
                           </ul>
                           
                           <div class="row">
	                           <div class="label">
	                           		<spring:message code='participant.label.email_address' text=''/>&nbsp;&nbsp;
	                           </div>
	                           <div class="value">
		                           <input type="text" name="participant.emailAddress" value="${command.participant.emailAddress}"
		                                       id="participant.emailAddress" onblur="CP.checkParticipantEmail();" title="Email" maxlength="35" size="35"
		                                       class="${command.participant.studyParticipantAssignments[0].studyParticipantModes[0].email ? "validate-EMAIL&&NOTEMPTY":"validate-EMAIL"}"/>
								   
		                           <ul id="userEmailError" style="display:none; padding-left:4em " class="errors">
		                              <li><spring:message code='participant.unique_emailAddress' text='participant.unique_emailAddress'/>
		                              </li>
		                           </ul> 
	                           </div>                                         
                           </div>
                            
                       </td>

                       <td width="50%">
                           <c:if test="${command.mode eq 'N'}">
                               <tags:renderDate propertyName="participant.birthDate"
                                                displayName="participant.label.date_of_birth" required="true"/>
                           </c:if>
                           
                           <tags:renderSelect propertyName="participant.gender" displayName="participant.label.gender"
                                              required="${required}" options="${genders}"/>
                             
                           <c:if test="${command.mode eq 'N'}">
	                           <div class="row">
		                           <div class="label">
		                           		<span class="required-indicator">*&nbsp;&nbsp;</span><spring:message code='participant.label.participant_identifier' />&nbsp;&nbsp;
		                           </div>
		                           <div class="value">
			                           <input type="text" name="participant.assignedIdentifier" value="${command.participant.assignedIdentifier}"
			                                       id="participant.assignedIdentifier" onblur="CP.checkParticipantMrn();" title="MRN" class="validate-NOTEMPTY"/>
									   
		                              <ul id="uniqueError_mrn" style="display:none; padding-left:4em " class="errors">
		                                   <li><spring:message code='participant.unique_mrn'/></li>
		                              </ul>
		                              <ul id="participant.assignedIdentifier.error" style="display:none;padding-left:4em;" class="errors">
		                                   <li><spring:message code='special.character.message' text='special.character.message'/></li>
		                              </ul>
		                           </div>                                         
	                           </div>
                          </c:if>
                          
                          <c:set var="isPhNumDisabled" value="${empty command.participant.phoneNumber and not empty patientId}" />
    					   <div class="row">
	                           <div class="label">
	                           		<span class="required-indicator">*&nbsp;&nbsp;</span><spring:message code='participant.label.phone' text=''/>&nbsp;&nbsp;
	                           </div>
	                           <div class="value">
	                           	   
		                           <input type="text" name="participant.phoneNumber" value="${command.participant.phoneNumber}"
		                                       id="participant.phoneNumber" onblur="CP.checkParticipantPhoneNumber();" title="Phone" 
		                                       class="${isPhNumDisabled ? '':'validate-NOTEMPTY&&US_PHONE_NO'}"
		                                       ${isPhNumDisabled ? 'disabled':''}/>
								  <input type="checkbox" id="declinePhNum" name="declinePhNum" value="declinePhNum" 
								  		 ${isPhNumDisabled ? 'checked':''}
								  		 ${command.participant.studyParticipantAssignments[0].studyParticipantModes[0].call ? 'disabled':''}
                                         onclick="javascript:CP_NS.togglePhoneNumber(this.checked);"/>&nbsp;Decline to provide.
								  <tags:errors path="participant.phoneNumber"/>
					              <ul id="phoneNumberError" style="display:none" class="errors">
					                  <li><spring:message code='participant.unique_phoneNumber' text='participant.unique_phoneNumber'/></li>
					              </ul>
					              <ul id="PhonePatternError" style="display:none" class="errors">
					                  <li><spring:message code='participant.phonenumber_pattern' text='participant.phonenumber_pattern'/></li>
					              </ul>
	                           </div>                                         
                           </div>
                           <div class="row">
	                           <div class="label">
	                           		<span class="required-indicator">*&nbsp;&nbsp;</span>Timezone&nbsp;&nbsp;
	                           </div>
	                           <div class="value">
									<select id="call_timeZone" name="call_timeZone"
											title="Time zone" class="validate-NOTEMPTY" required="true">
									
									    <option value="America/New_York" ${command.callTimeZone eq "America/New_York" ? "selected='selected'" : " "} >
									        Eastern Time
									    </option>
									    <option value="America/Chicago" ${command.callTimeZone eq "America/Chicago" ? "selected='selected'" : " "} >
									        Central Time
									    </option>
									    <option value="America/Denver" ${command.callTimeZone eq "America/Denver" ? "selected='selected'" : " "} >
									        Mountain Time
									    </option>
									    <option value="America/Los_Angeles" ${command.callTimeZone eq "America/Los_Angeles" ? "selected='selected'" : " "} >
									        Pacific Time
									    </option>
									    <option value="America/Anchorage" ${command.callTimeZone eq "America/Anchorage" ? "selected='selected'" : " "} >
									        Alaska Time
									    </option>
									    <option value="America/Adak" ${command.callTimeZone eq "America/Adak" ? "selected='selected'" : " "} >
									        Hawaii-Aleutian Time
									    </option>
									</select>
	                           </div>                                         
                           </div>
                       
                       </td>
                   </tr>
               </table>
           </chrome:division>
       <c:choose>
           <c:when test="${command.mode eq 'N'}">
           </c:when>
           <c:otherwise/>
       </c:choose>
       <div id="studies" style="display:none">
           <chrome:division title="participant.label.studies"/>
       </div>
       
        <div id='ajaxLoadingImgDiv'>
		</div>
		
        <div id="studysitestable">
 			
 			<div id = "studysitestablecontentmarker">
 			
            <c:if test="${not empty command.participant.id}">

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
                   
                    
	                    <c:forEach items="${command.participant.studyParticipantAssignments}"
	                               var="studyParticipantAssignment" varStatus="spastatus">
	                        <c:set var="studysite" value="${studyParticipantAssignment.studySite}"/>
							
	                        <tags:studySite studysite="${studysite}" selected="true" isEdit="true"
	                                        studyParticipantAssignment="${studyParticipantAssignment}"
	                                        participant="${command.participant}" isCreateFlow="${isCreateFlow}"/>
	                    </c:forEach>
					
					
					
                </table>

            </c:if>
            
			</div>	
        </div>
</jsp:attribute>
</tags:tabForm>
</body>
</html>