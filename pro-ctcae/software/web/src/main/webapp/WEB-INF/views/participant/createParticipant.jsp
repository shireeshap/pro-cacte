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
        objects="uniqueParticipantIdentifier,uniqueParticipantUserNumber,uniqueParticipantEmailAddress,userNameValidation,uniqueParticipantEmailAddress"/>
<tags:javascriptLink name="ui_fields_validation"/>

<html>
<head>
<tags:stylesheetLink name="tabbedflow"/>
<tags:includeScriptaculous/>
<tags:includePrototypeWindow/>

<tags:stylesheetLink name="yui-autocomplete"/>
<tags:javascriptLink name="yui-autocomplete"/>
<tags:dwrJavascriptLink objects="organization"/>

<script>

var isUserNameError = false;
var isUserIdError = false;
var isPinError = false;
var isIdentifierError = false;
var isPasswordError = false;
var isConfirmPassError = false;
var isEmail = false;
var isPhoneNumberError = false;
var isBlackoutCallTime = false;
var isEmailError = false;
var isConfirmPinError = false;

// validation check for username
function checkParticipantUserName() {
    var participantId = "${param['id']}";
    var userName = $('participant.user.username').value;
    if (participantId == "") {
        var userId = "${userId}";
    }
    if (userName != "") {
        if (userName.length < 6) {
            jQuery('#userNameError').hide();
            jQuery('#userNameLengthError').show();
            isUserNameError = true;
        }
        else {
            userNameValidation.validateDwrUniqueName(userName, userId, userReturnValue);
            jQuery('#userNameLengthError').hide();
            return;
        }
    }
    else {
        jQuery('#userNameError').hide();
        jQuery('#userNameLengthError').hide();
        isUserNameError = false;
    }
    checkError();
}

// validation check for email address
function checkParticipantEmail() {
    var emailAddress = $('participant.emailAddress').value;
    var participantId = "${param['id']}";
    if (participantId == "") {
        participantId = "${patientId}";
    }
    isEmailError = false;
    jQuery('#userEmailError').hide();
    if (emailAddress != "") {
            uniqueParticipantEmailAddress.validateEmail(emailAddress, participantId,{callback:
                                                                                function(returnValue) {
                                                                                       if (returnValue) {
                                                                                           jQuery('#userEmailError').show();
                                                                                           isEmailError = true;
                                                                                       }
                                                                                       else {
                                                                                           isEmailError = false;
                                                                                       }
                                                                                       checkError();
                                                                                }
                                                                            }
                                                );
    }
    else {
        checkError();
    }
    checkError();
}

function userReturnValue(returnValue) {
    showOrHideErrorField(returnValue, '#userNameError');
    if (returnValue) {
        isUserNameError = true;
    }
    else {
        isUserNameError = false;
    }
    checkError();
}

function checkError() {
    if (isUserNameError || isPasswordError || isConfirmPassError || isUserIdError || isIdentifierError || isPinError || isPhoneNumberError || isBlackoutCallTime || isEmailError || isConfirmPinError) {
        jQuery('#flow-update').attr('disabled', true);
        jQuery('#flow-next').attr('disabled', true);
    }
    else {
        jQuery('#flow-update').attr('disabled', false);
        jQuery('#flow-next').attr('disabled', false);
    }
}

//validation check for password policy
function checkPasswordPolicy() {
    var userPassword = $('participant.user.password').value;
    var userName = $('participant.user.username').value;
    var confirmPassword = $('participant.user.confirmPassword').value;
    if (confirmPassword != "") {
        checkPasswordMatch();
    }
    if (userPassword != "") {
        userNameValidation.validatePasswordPolicyDwr("PARTICIPANT", userPassword, userName, passReturnValue);
        return;
    }
    else {
        jQuery('#passwordError').hide();
        isPasswordError = false;
    }
    checkError();
}

function passReturnValue(returnValue) {
    if (returnValue != "") {
        jQuery('#passwordError').show();
        document.getElementById('passwordError1').innerHTML = returnValue + "";
        isPasswordError = true;
    }
    else {
        jQuery('#passwordError').hide();
        isPasswordError = false;
    }
    checkError();
}

function checkPinMatch(siteId) {
    var password = $('participant.pinNumber_'+siteId).value;
    var confirmPassword = $('participant.confirmPinNumber_'+siteId).value;
    jQuery('#confirmPinError_'+siteId).hide();
    isConfirmPassError = false;
    if (password != "" && confirmPassword != "") {
        if (password != confirmPassword) {
            jQuery('#confirmPinError_'+siteId).show();
            isConfirmPassError = true;
        }
    }
    checkError();
}

// validation check for confirm password
function checkPasswordMatch() {
    var password = $('participant.user.password').value;
    var confirmPassword = $('participant.user.confirmPassword').value;
    if (password != "" && confirmPassword != "") {
        if (password == confirmPassword) {
            jQuery('#passwordErrorConfirm').hide();
            isConfirmPassError = false;
        }
        else {
            jQuery('#passwordErrorConfirm').show();
            document.getElementById('passwordErrorConfirm1').innerHTML = "Password does not match confirm password.";
            isConfirmPassError = true;
        }
    }
    else {
        jQuery('#passwordErrorConfirm').hide();
        isConfirmPassError = false;
    }
    checkError();
}

function validateCalloutTime(siteId, startTime, endTime) {
    var participantId = "${param['id']}";
    if (participantId == "") {
        participantId = "${patientId}";
    }
    isBlackoutCallTime = false;
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
        var hhStart = parseInt(start[0],10);
        var mmStart = parseInt(start[1],10);
        var hhEnd = parseInt(end[0],10);
        var mmEnd = parseInt(end[1],10);

        var callHour = parseInt(callHour, 10);
        var callMin = parseInt(callMin, 10);

        if (callHour < 12 &&callAmPm == 'pm') {
            var callHour = callHour + 12;
        }
        if(callHour == 12 && callAmPm == 'am'){
           callHour=callHour-12;
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
        } else{
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
            isBlackoutCallTime = true;
       //     $('call_hour_' + siteId).value = "";
        //    $('call_minute_' + siteId).value = "";
        }

        checkError();
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

//validation check for participant email address
/*function checkParticipantEmailAddress(siteId) {
    var participantId = "${param['id']}";
    if (participantId == "") {
        participantId = "${patientId}";
    }
    var email = $('participant.emailAddress_' + siteId).value;
    if (email != "") {
        uniqueParticipantEmailAddress.validateEmail(email, participantId,
        {callback:
         function(returnValue) {
             showOrHideErrorField(returnValue, '#emailError_' + siteId);
             if (returnValue) {
                 isEmail = true;
             }
             else {
                 isEmail = false;
             }
             checkError();
         }});
    }
    else {
        jQuery('#emailError_' + siteId).hide();
        isEmail = false;
        checkError();
    }
}*/


var phoneNumberPattern =  /^\(?([0-9]{3})\)?[-]?([0-9]{3})[-]?([0-9]{4})$/;
function checkParticipantPhoneNumber(siteId){
    var participantId = "${param['id']}";
    if (participantId == "") {
        participantId = "${patientId}";
    }
    var phoneNumber = $('participant.phoneNumber_'+ siteId).value;
    if (phoneNumber != "") {
        if (!phoneNumberPattern.test(phoneNumber)) {
            jQuery('#PhonePatternError_' + siteId).show();
            jQuery('#phoneNumberError_' + siteId).hide();
            isPhoneNumberError = true;
        }
        else {
            var re = /[-]/g;
            var nonFormattedPhoneNumber = phoneNumber.replace(re,"");
            phoneNumber = nonFormattedPhoneNumber.substring(0,3)+"-"+nonFormattedPhoneNumber.substring(3,6)+"-"+nonFormattedPhoneNumber.substring(6,10);
            $('participant.phoneNumber_'+ siteId).value = phoneNumber;
            uniqueParticipantUserNumber.validatePhoneNumber(phoneNumber, participantId, {callback:
                                                                                       function(returnValue) {
                                                                                           jQuery('#PhonePatternError_' + siteId).hide();
                                                                                           showOrHideErrorField(returnValue, '#phoneNumberError_' + siteId);
                                                                                           if (returnValue) {
                                                                                               isPhoneNumberError = true;
                                                                                           }
                                                                                           else {
                                                                                                isPhoneNumberError = false;
                                                                                                var userNumber = $('participant.userNumber_' + siteId).value;
                                                                                                if (userNumber==null || userNumber=="") {
                                                                                                    var re = /[-]/g;
                                                                                                    $('participant.userNumber_' + siteId).value = nonFormattedPhoneNumber;
                                                                                                }
                                                                                           }
                                                                                           checkError();
                                                                                       }});
        }
    }
    else {
        jQuery('#phoneNumberError_' + siteId).hide();
        jQuery('#PhonePatternError_' + siteId).hide();
        isPhoneNumberError = false;
        checkError();
    }
    checkError();
}

var userNumberPattern = /^[0-9]{10}$/;
// validation check for participant user number (IVRS)
function checkParticipantUserNumber(siteId) {
    var participantId = "${param['id']}";
    if (participantId == "") {
        participantId = "${patientId}";
    }
    var userNumber = $('participant.userNumber_' + siteId).value;
    if (userNumber != "") {
        if (!userNumberPattern.test(userNumber)) {
            jQuery('#UserPatternError_' + siteId).show();
            jQuery('#userNumberError_' + siteId).hide();
            isUserIdError = true;
        }
        else {
            uniqueParticipantUserNumber.validateUserNumber(userNumber, participantId, {callback:
                                                                                       function(returnValue) {
                                                                                           jQuery('#UserPatternError_' + siteId).hide();
                                                                                           showOrHideErrorField(returnValue, '#userNumberError_' + siteId);
                                                                                           if (returnValue) {
                                                                                               isUserIdError = true;
                                                                                           }
                                                                                           else {
                                                                                               isUserIdError = false;
                                                                                           }
                                                                                           checkError();
                                                                                       }});
        }
    }
    else {
        jQuery('#userNumberError_' + siteId).hide();
        jQuery('#UserPatternError_' + siteId).hide();
        isUserIdError = false;
        checkError();
    }
    checkError();
}
var pinPattern =  /^[0-9]{4}$/;
// validation check for participant pin number (IVRS)
function checkParticipantPinNumber(siteId) {
    var participantId = "${param['id']}";
    if (participantId == "") {
        participantId = "${patientId}";
    }
    var pinNumber = $('participant.pinNumber_' + siteId).value;
 //   jQuery('#PinPatternError_' + siteId).hide();
    if (pinNumber != "") {
        if (!pinPattern.test(pinNumber)) {
            jQuery('#PinPatternError_' + siteId).show();
            isPinError = true;
        }
        else {
            isPinError = false;
            jQuery('#PinPatternError_' + siteId).hide();
        }
    }
    else {
        jQuery('#PinPatternError_' + siteId).hide();
        isPinError = false;
    }
    checkError();
}

// validation check for participant study identifier
function checkParticipantStudyIdentifier(id, siteId) {
    var participantId = "${param['id']}";
    if (participantId == "") {
        participantId = "${patientId}";
    }
    var identifier = $('participantStudyIdentifier_' + siteId).value;
    if(isSpclChar('participantStudyIdentifier_' + siteId)){
        return;
    }
    if (identifier != "") {
        uniqueParticipantIdentifier.validateUniqueParticipantIdentifier(id, identifier,
                participantId, {callback:
                                function(returnValue) {
                                    showOrHideErrorField(returnValue, '#uniqueError_' + siteId);
                                    if (returnValue) {
                                        isIdentifierError = true;
                                    }
                                    else {
                                        isIdentifierError = false;
                                    }
                                    checkError();
                                }});
        return;
    }
    else {
        jQuery('#uniqueError_' + siteId).hide();
        isIdentifierError = false;
        checkError();
    }
}

//validation check for participant  identifier
function checkParticipantMrn() {
    var participantId = "${param['id']}";
    if (participantId == "") {
        participantId = "${patientId}";
    }
    var mrn = $('participant.assignedIdentifier').value;
     if(isSpclChar('participant.assignedIdentifier')){
        return;
    }
    var siteId = $('organizationId').value;
    if (siteId != "" && mrn != "") {
        uniqueParticipantIdentifier.validateUniqueParticipantMrn(siteId, mrn,
                participantId, {callback:
                                function(returnValue) {
                                    showOrHideErrorField(returnValue, '#uniqueError_mrn');
                                    if (returnValue) {
                                        isIdentifierError = true;
                                    }
                                    else {
                                        isIdentifierError = false;
                                    }
                                    checkError();
                                }});
        return;
    }
    else {
        jQuery('#uniqueError_mrn').hide();
        isIdentifierError = false;
        checkError();
    }
}

function getStudySites() {
    var organizationId = $('organizationId').value;
    if (organizationId == '') {
        $("studysitestable").innerHTML = '';
        return;
    }
    var id = '${param['id']}';
    if (id == '') {
        id = '${command.participant.id}';
    }
    var request = new Ajax.Request("<c:url value="/pages/participant/displaystudysites"/>", {
        onComplete:function(transport) {
            var response = transport.responseText;
            $("studysitestable").update(response);
        },
        parameters:<tags:ajaxstandardparams/> + "&organizationId=" + organizationId + "&id=" + id,
        method:'get'
    })
}

function getOrgs(sQuery) {
    showIndicator("organizationId-input-indicator");
    var callbackProxy = function(results) {
        aResults = results;
    };
    var callMetaData = { callback:callbackProxy, async:false};
    organization.matchOrganizationForStudySites(unescape(sQuery), callMetaData);
    hideIndicator("organizationId-input-indicator");
    return aResults;
}

function handleSelect(stype, args) {
    var ele = args[0];
    var oData = args[2];
    if(oData == null){
    	ele.getInputEl().value="(Begin typing here)";
    	ele.getInputEl().addClassName('pending-search');
    } else {
	    ele.getInputEl().value = oData.displayName;
	    ele.getInputEl().removeClassName('pending-search');
	    var id = ele.getInputEl().id;
	    var hiddenInputId = id.substring(0, id.indexOf('-input'));
	    $(hiddenInputId).value = oData.id;
	    getStudySites();
	    jQuery('#studies').show();
    }
    checkParticipantMrn();
}

function clearInput(inputId) {
    $(inputId).clear();
    $(inputId + '-input').clear();
    $(inputId + '-input').focus();
    $(inputId + '-input').blur();
}

Event.observe(window, 'load', function() {

    <c:if test="${command.admin eq true && empty command.participant.studyParticipantAssignments}">
    try {
//        acCreate(new siteAutoComplter('organizationId'));
//        initSearchField();
        new YUIAutoCompleter('organizationId-input', getOrgs, handleSelect);
        var orgName = "${command.selectedOrganization.displayName}";
        if (orgName != '') {
            $('organizationId-input').value = "${command.selectedOrganization.displayName}";
            $('organizationId-input').removeClassName('pending-search');
        }
    } catch(err) {
    }
    </c:if>
    Event.observe('organizationId', 'change', function() {
        getStudySites();
    })

    //need to make the ajax call, when there is validation error in create flow
    if (${hasValidationErrors}) {
        if ('${command.participant.id}' == '') {
            //populate the site value-
            if ($('organizationId-input')) {
                //admin user login
                $('organizationId-input').value = '${command.selectedOrganization.displayName}'
            }
            getStudySites();
        }

    }

});
function doPostProcessing() {
    getStudySites();
}


function showForms(obj, id) {
    var sites = document.getElementsByName('studySites');
    for (var i = 0; i < sites.length; i++) {
        $('subform_' + sites[i].value).hide();
        $('participantStudyIdentifier_' + sites[i].value).removeClassName("validate-NOTEMPTY");
        $('participantStudyIdentifier_' + sites[i].value).value = "";
        jQuery('#uniqueError_' + sites[i].value).hide();
        jQuery('#UserPatternError_' + sites[i].value).hide();
        jQuery('#PinPatternError_' + sites[i].value).hide();
        jQuery('#confirmPinError_' + sites[i].value).hide();
        jQuery('#userNumberError_' + sites[i].value).hide();
        jQuery('#PhonePatternError_' + sites[i].value).hide();
        jQuery('#preferred.calltime.error_' + sites[i].value).hide();
        jQuery('#phoneNumberError_' + sites[i].value).hide();
        jQuery('#emailError_' + sites[i].value).hide();
        try {
            $('arm_' + sites[i].value).removeClassName("validate-NOTEMPTY");
        } catch(e) {
        }
    }
    $('subform_' + id).show();
    $('participantStudyIdentifier_' + id).addClassName("validate-NOTEMPTY");
    isIdentifierError = false;
    isUserIdError = false;
    isPinError = false;
    isEmail = false;
    isBlackoutCallTime = false;
    checkError();
    try {
        $('arm_' + id).addClassName("validate-NOTEMPTY");
    } catch(e) {
    }
    AE.registerCalendarPopups();
}


function participantOffStudy(id) {
    var request = new Ajax.Request("<c:url value="/pages/participant/participantOffStudy"/>", {
        parameters:<tags:ajaxstandardparams/>+"&flow=participant&id=" + id  ,
        onComplete:function(transport) {
            showConfirmationWindow(transport, 600, 350);
        },
        method:'get'
    })
}


var _winOffHold;
function participantOffHold(id, date) {
    var url = "<c:url value="/pages/participant/participantOffHold"/>" + "?flow=participant&id=" + id + "&date=" + date + "&subview=x";
    _winOffHold = showModalWindow(url, 600, 350);
}

function participantOnHold(id, date) {
    var request = new Ajax.Request("<c:url value="/pages/participant/participantOnHold"/>", {
        parameters:<tags:ajaxstandardparams/>+"&flow=participant&id=" + id + "&date=" + date,
        onComplete:function(transport) {
            showConfirmationWindow(transport, 600, 350);
        },
        method:'get'
    })
}

function participantRptModeHistoryDisplay(id) {
    var request = new Ajax.Request("<c:url value="/pages/participant/participantReportingModeHistory"/>", {
        parameters:<tags:ajaxstandardparams/>+"&flow=participant&id=" + id,
        onComplete:function(transport) {
            showConfirmationWindow(transport, 600, 350);
        },
        method:'get'
    })
}

function showpassword(show) {
    if (show) {
        $('passwordfields').show();
        $('resetpass').innerHTML = '<a href="javascript:showpassword(false);">Hide password</a>';
    } else {
        $('passwordfields').hide();
        $('resetpass').innerHTML = '<a href="javascript:showpassword(true);">Reset password</a>';
    }
}

function validateAndSubmit(date, form) {
    if (date == '') {
        alert('Please provide a valid date');
        return;
    }
    form.submit();
}

function addEmailRemoveIVRSClassName(id) {
    var participantId = "${param['id']}";
    if (participantId == "") {
        participantId = "${patientId}";
    }

 //   $('participant.emailAddress_' + id).addClassName("validate-NOTEMPTY");
    $('home_web_lang_' + id).addClassName("validate-NOTEMPTY");
    $('participant.userNumber_' + id).removeClassName("validate-NOTEMPTY");
    $('participant.phoneNumber_' + id).removeClassName("validate-NOTEMPTY&&US_PHONE_NO");
    $('participant.pinNumber_' + id).removeClassName("validate-NOTEMPTY");
    $('call_hour_' + id).removeClassName("validate-NOTEMPTY");
    $('call_minute_' + id).removeClassName("validate-NOTEMPTY");
    $('call_ampm_' + id).removeClassName("validate-NOTEMPTY");
    $('call_timeZone_' + id).removeClassName("validate-NOTEMPTY");
    $('ivrs_lang_' + id).removeClassName("validate-NOTEMPTY");

    if (participantId == "") {
        $('participant.userNumber_' + id).value = "";
        $('participant.phoneNumber_' + id).value = "";
        $('participant.pinNumber_' + id).value = "";
        $('call_hour_' + id).value = "";
        $('call_minute_' + id).value = "";
        $('call_ampm_' + id).value = "";
        $('call_timeZone_' + id).value = "";
    }
}

function addIVRSRemoveEmailClassName(id) {
    var participantId = "${param['id']}";
    if (participantId == "") {
        participantId = "${patientId}";
    }
    if($('participant.emailAddress_' + id) != null){
        $('participant.emailAddress_' + id).removeClassName("validate-NOTEMPTY");
    }
    $('participant.userNumber_' + id).addClassName("validate-NOTEMPTY");
    $('participant.phoneNumber_' + id).addClassName("validate-NOTEMPTY&&US_PHONE_NO");
    $('participant.pinNumber_' + id).addClassName("validate-NOTEMPTY");
    $('call_hour_' + id).addClassName("validate-NOTEMPTY");
    $('call_minute_' + id).addClassName("validate-NOTEMPTY");
    $('call_ampm_' + id).addClassName("validate-NOTEMPTY");
    $('call_timeZone_' + id).addClassName("validate-NOTEMPTY");
    $('ivrs_lang_' + id).addClassName("validate-NOTEMPTY");
    $('home_web_lang_' + id).removeClassName("validate-NOTEMPTY");
    if (participantId == "") {
        $('participant.emailAddress_' + id).value = "";
    }
}

function removeEmailClassName(id) {
    var participantId = "${param['id']}";
    if (participantId == "") {
        participantId = "${patientId}";
    }
    $('home_web_lang_' + id).removeClassName("validate-NOTEMPTY");
    $('participant.emailAddress_' + id).removeClassName("validate-NOTEMPTY");
    $('participant.userNumber_' + id).removeClassName("validate-NOTEMPTY");
    $('participant.phoneNumber_' + id).removeClassName("validate-NOTEMPTY&&US_PHONE_NO");
    $('participant.pinNumber_' + id).removeClassName("validate-NOTEMPTY");
    $('call_hour_' + id).removeClassName("validate-NOTEMPTY");
    $('call_minute_' + id).removeClassName("validate-NOTEMPTY");
    $('call_ampm_' + id).removeClassName("validate-NOTEMPTY");
    $('call_timeZone_' + id).removeClassName("validate-NOTEMPTY");
    $('ivrs_lang_' + id).removeClassName("validate-NOTEMPTY");
    if (participantId == "") {
        $('participant.emailAddress_' + id).value = "";
        $('participant.userNumber_' + id).value = "";
        $('participant.phoneNumber_' + id).value = "";
        $('participant.pinNumber_' + id).value = "";
        $('call_hour_' + id).value = "";
        $('call_minute_' + id).value = "";
        $('call_ampm_' + id).value = "";
        $('call_timeZone_' + id).value = "";
        $('ivrs_lang_' + id).value = "";

    }
}

function showEmail(id) {
    if (jQuery('input:checkbox:checked').val()) {
        jQuery('#emailInput_' + id).show();
        jQuery('#emailHeader_' + id).show();
        jQuery('#webLang_' + id).show();
        addEmailRemoveIVRSClassName(id);
    }
    else {
        jQuery('#emailInput_' + id).hide();
        jQuery('#emailHeader_' + id).hide();
        jQuery('#emailError_' + id).hide();
        jQuery('#webLang_' + id).hide();
        isEmail = false;
        removeEmailClassName(id);
    }
    checkError();
}
<%--var clickCount = ${homeModeCount};--%>
function showOrHideLanguage(value1, value2, id) {
    jQuery("#clinic_paper_lang_" + id).val('');
    jQuery('#clinic_paper_lang_' + id + '-msg').hide();
    jQuery('#paper_clinic_header_' + id).hide();
    jQuery('#clinicPaper_' + id).hide();
    $('clinic_paper_lang_' + id).removeClassName("validate-NOTEMPTY");
    jQuery('#participantClinicModes_'+id).attr("checked",false);
    jQuery(this).attr();
    jQuery("#clinic_web_lang_" + id).val('');
    jQuery('#clinic_web_lang_' + id + '-msg').hide();
    jQuery('#web_clinic_header_' + id).hide();
    jQuery('#clinicWeb_' + id).hide();
    $('clinic_web_lang_' + id).removeClassName("validate-NOTEMPTY");

    if (value1 && value2 == "CLINICWEB") {
        jQuery("#clinic_paper_lang_" + id).val('');
        jQuery('#web_clinic_header_' + id).show();
        jQuery('#clinicWeb_' + id).show();
        jQuery('#clinic_paper_lang_' + id + '-msg').hide();
        jQuery('#paper_clinic_header_' + id).hide();
        jQuery('#clinicPaper_' + id).hide();
        $('clinic_paper_lang_' + id).removeClassName("validate-NOTEMPTY");
        $('clinic_web_lang_' + id).addClassName("validate-NOTEMPTY");
    }
    if (value1 && value2 == "CLINICBOOKLET") {
        jQuery("#clinic_web_lang_" + id).val('');
        jQuery('#clinic_web_lang_' + id + '-msg').hide();
        jQuery('#web_clinic_header_' + id).hide();
        jQuery('#clinicWeb_' + id).hide();
        jQuery('#paper_clinic_header_' + id).show();
        jQuery('#clinicPaper_' + id).show();
        $('clinic_paper_lang_' + id).addClassName("validate-NOTEMPTY");
        $('clinic_web_lang_' + id).removeClassName("validate-NOTEMPTY");
    }
}
function showOrHideEmail(value1, value2, id) {
        jQuery('#home_web_lang_'+ id + '-msg').hide();
        jQuery('#home_paper_lang_'+ id + '-msg').hide();
        jQuery('#participantPhoneNumber_' + id + '-msg').hide();
        jQuery('#participantUserNumber_' + id + '-msg').hide();
        jQuery('#participantPinNumber_' + id + '-msg').hide();
        jQuery('#call_hour_' + id + '-msg').hide();
        jQuery('#call_minute_' + id + '-msg').hide();
        jQuery('#call_ampm_' + id + '-msg').hide();
        jQuery('#call_timeZone_' + id + '-msg').hide();
        jQuery('#ivrs_lang_' + id + '-msg').hide();

        jQuery('#UserPatternError_' + id).hide();
        jQuery('#PhonePatternError_' + id).hide();
        jQuery('#phoneNumberError_' + id).hide();
        jQuery('#PinPatternError_' + id).hide();
        jQuery('#confirmPinError_' + id).hide();
        jQuery('#userNumberError_' + id).hide();
        jQuery('#emailError_' + id).hide();
        jQuery('#preferred.calltime.error_' + id).hide();
        $('preferred.calltime.error_' + id).hide();

        isUserIdError = false;
        isPinError = false;
        isEmail = false;
        isPhoneNumberError = false;
        isBlackoutCallTime = false;
        checkError();

    if (value1 && value2 == "HOMEWEB") {
        jQuery("#ivrs_lang_" + id).val('');
        jQuery("#home_paper_lang_" + id).val('');
        jQuery('#div_contact').show();
        jQuery('#web_' + id).show();
        jQuery('#email_' + id).attr('checked', true);
        jQuery('#call_' + id).attr('checked', false);
        jQuery('#emailInput_' + id).show();
        jQuery('#webLang_' + id).show();
        jQuery('#emailHeader_' + id).show();
        jQuery('#div_contact_ivrs').hide();
        jQuery('#paper_home_header_' + id).hide();
        jQuery('#home_paper_' + id).hide();
        jQuery('#div_contact').hide();
        jQuery('#div_contact_ivrs').hide();
        jQuery('#ivrs_' + id).hide();
        jQuery('#c_' + id).hide();
        jQuery('#c1_' + id).hide();
        jQuery('#c2_' + id).hide();
        jQuery('#c3_' + id).hide();
        jQuery('#c4_' + id).hide();
        jQuery('#reminder_' + id).hide();
        jQuery('#ivrs_reminder_' + id).hide();
        jQuery('#ivrsLang_' + id).hide();
        $('home_web_lang_' + id).addClassName("validate-NOTEMPTY");
        $('ivrs_lang_' + id).removeClassName("validate-NOTEMPTY");
        $('home_paper_lang_' + id).removeClassName("validate-NOTEMPTY");
        addEmailRemoveIVRSClassName(id);
    } else {
        jQuery('#web_' + id).show();
    }
    if (value1 && value2 == "HOMEBOOKLET") {
        jQuery("#home_web_lang_" + id).val('');
        jQuery("#ivrs_lang_" + id).val('');
        jQuery('#paper_home_header_' + id).show();
        jQuery('#home_paper_' + id).show();
        jQuery('#web_' + id).hide();
        jQuery('#emailInput_' + id).hide();
        jQuery('#webLang_' + id).hide();
        jQuery('#emailHeader_' + id).hide();
        jQuery('#div_contact').hide();
        jQuery('#div_contact_ivrs').hide();
        jQuery('#ivrs_' + id).hide();
        jQuery('#c_' + id).hide();
        jQuery('#c1_' + id).hide();
        jQuery('#c2_' + id).hide();
        jQuery('#c3_' + id).hide();
        jQuery('#c4_' + id).hide();
        jQuery('#reminder_' + id).hide();
        jQuery('#ivrs_reminder_' + id).hide();
        jQuery('#ivrsLang_' + id).hide();
        $('home_web_lang_' + id).removeClassName("validate-NOTEMPTY");
        $('ivrs_lang_' + id).removeClassName("validate-NOTEMPTY");
        $('home_paper_lang_' + id).addClassName("validate-NOTEMPTY");
        removeEmailClassName(id);
    }
    if (value1 && value2 == "IVRS") {
        jQuery("#home_paper_lang_" + id).val('');
        jQuery("#home_web_lang_" + id).val('');
        jQuery('#div_contact').show();
        jQuery('#div_contact_ivrs').show();
        jQuery('#ivrs_' + id).show();
        jQuery('#c_' + id).show();
        jQuery('#c1_' + id).show();
        jQuery('#c2_' + id).show();
        jQuery('#c3_' + id).show();
        jQuery('#c4_' + id).show();
        jQuery('#reminder_' + id).show();
        jQuery('#ivrs_reminder_' + id).show();
        jQuery('#ivrsLang_' + id).show();
        jQuery('#call_' + id).attr('checked', true);
        jQuery('#email_' + id).attr('checked', false);

        jQuery('#web_' + id).hide();
        jQuery('#emailInput_' + id).hide();
        jQuery('#webLang_' + id).hide();
        jQuery('#emailHeader_' + id).hide();
        jQuery('#paper_home_header_' + id).hide();
        jQuery('#home_paper_' + id).hide();

        $('home_web_lang_' + id).removeClassName("validate-NOTEMPTY");
        $('ivrs_lang_' + id).addClassName("validate-NOTEMPTY");
        $('home_paper_lang_' + id).removeClassName("validate-NOTEMPTY");
        addIVRSRemoveEmailClassName(id);
    } else {
        jQuery('#ivrs_' + id).hide();
        jQuery('#ivrs_reminder_' + id).hide();
        jQuery('#reminder_' + id).hide();
        jQuery('#ivrsLang_' + id).hide();
        jQuery('#c_' + id).hide();
        jQuery('#c1_' + id).hide();
        jQuery('#c2_' + id).hide();
        jQuery('#c3_' + id).hide();
        jQuery('#c4_' + id).hide();
    }

}

    function isSpclChar(fieldName){
        var iChars = "!@#$%^&*+=[]\\\';,./{}|\":<>?";
        var fieldValue = $(fieldName).value;
        jQuery('#'+ fieldName + '.error').hide();
        $(fieldName + '.error').hide();
        for (var i = 0; i < fieldValue.length; i++) {
            if (iChars.indexOf(fieldValue.charAt(i)) != -1) {
               // alert ("The box has special characters. \nThese are not allowed.\n");
                jQuery('#'+ fieldName + '.error').show();
                $(fieldName + '.error').show();
                $(fieldName).value="";
                return true;
            }
        }
        return false;
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
               <c:set var="maxLength" value="40"/>
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
                                                              value="${command.selectedOrganization.displayName}" required="false"
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
                                               getStudySites();
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
                                            required="true" maxLength="${maxLength}" size="${maxLength}" onblur="isSpclChar('participant.firstName');"/>
                           <ul id="participant.firstName.error" style="display:none;left-padding:8em;" class="errors">
                                <li><spring:message code='special.character.message'
                                                    text='special.character.message'/></li>
                           </ul>
                           <c:if test="${command.mode eq 'N'}">
                               <tags:renderText propertyName="participant.middleName"
                                                displayName="participant.label.middle_name" maxLength="${maxLength}"
                                                size="${maxLength}" onblur="isSpclChar('participant.middleName');"/>
                               <ul id="participant.middleName.error" style="display:none;" class="errors">
                                    <li><spring:message code='special.character.message'
                                                    text='special.character.message'/></li>
                               </ul>
                           </c:if>
                           <tags:renderText propertyName="participant.lastName"
                                            displayName="participant.label.last_name"
                                            required="true" maxLength="${maxLength}" size="${maxLength}" onblur="isSpclChar('participant.lastName');"/>
                           <ul id="participant.lastName.error" style="display:none;" class="errors">
                                    <li><spring:message code='special.character.message'
                                                    text='special.character.message'/></li>
                           </ul>
                       </td>

                       <td width="50%" >
                           <c:if test="${command.mode eq 'N'}">
                               <tags:renderDate propertyName="participant.birthDate"
                                                displayName="participant.label.date_of_birth" required="true"/>
                           </c:if>
                           <tags:renderSelect propertyName="participant.gender" displayName="participant.label.gender"
                                              required="${required}" options="${genders}"/>
                           <c:if test="${command.mode eq 'N'}">
                               <tags:renderText propertyName="participant.assignedIdentifier"
                                                displayName="participant.label.participant_identifier"
                                                required="true" onblur="checkParticipantMrn();"/>
                               <ul id="uniqueError_mrn" style="display:none; padding-left:4em " class="errors">
				                    <li><spring:message code='participant.unique_mrn' /></li>
				               </ul>
                               <ul id="participant.assignedIdentifier.error" style="display:none;padding-left:4em;" class="errors">
                                    <li><spring:message code='special.character.message'
                                                    text='special.character.message'/></li>
                               </ul>
                           </c:if>
                       </td>
                   </tr>
               </table>
           </chrome:division>
       <c:choose>
           <c:when test="${command.mode eq 'N'}">
               <chrome:division title="participant.label.contact_information">

                   <table border="0" style="width:100%">
                       <tr>
                           <td width="50%">
                               <tags:renderEmail propertyName="participant.emailAddress"
                                                 displayName="participant.label.email_address"
                                                 required="false" size="35" onblur="checkParticipantEmail();"/>
                                <ul id="userEmailError" style="display:none; padding-left:12em " class="errors">
                                   <li><spring:message code='participant.unique_emailAddress'
                                                       text='participant.unique_emailAddress'/>
                                   </li>
                               </ul>
                           </td>
                           <!--       <td width="50%">
                               <tags:renderPhoneOrFax propertyName="participant.phoneNumber"
                                                      displayName="participant.label.phone"
                                                      required="${required}"/>
                           </td> -->
                       </tr>
                   </table>
               </chrome:division>
           </c:when>
           <c:otherwise/>
       </c:choose>
       <chrome:division title="participant.label.logininfo">
           <table cellpadding="0" cellspacing="0">
               <tr>
                   <td>
                       <c:choose>
                           <c:when test="${command.readOnlyUserName}">
                               <div class="row">
                                   <div class="label"><spring:message code="participant.label.username"/>&nbsp;</div>
                                   <div class="value">${command.participant.user.username}</div>
                                   <input type="hidden" id="participant.user.username"
                                          name="participant.user.username"
                                          value="${command.participant.user.username}">
                               </div>
                           </c:when>
                           <c:otherwise>
                               <tags:renderText propertyName="participant.user.username"
                                                displayName="participant.label.username"
                                                required="true" onblur="checkParticipantUserName();"/>
                               <ul id="userNameError" style="display:none; padding-left:12em " class="errors">
                                   <li><spring:message code='participant.unique_userName'
                                                       text='participant.unique_userName'/></li>
                               </ul>
                               <ul id="userNameLengthError" style="display:none; padding-left:12em " class="errors">
                                   <li><spring:message code='participant.username_length'
                                                       text='participant.username_length'/></li>
                               </ul>

                           </c:otherwise>
                       </c:choose>
                   </td>
                   <td>
                       <c:if test="${command.readOnlyUserName}">
                           <c:set var="style" value="display:none"/>
                           <div id="resetpass" class="label">
                               &nbsp;<a href="javascript:showpassword(true);">Reset password</a></div>
                       </c:if>
                   </td>
               </tr>
           </table>
           <div id="passwordfields" style="${style}">
               <table border="0" cellpadding="0" cellspacing="0">
                   <tr>
                       <td>

                           <tags:renderPassword required="true" propertyName="participant.user.password"
                                                displayName="participant.label.password"
                                                onblur="checkPasswordPolicy();"/>
                           <ul id="passwordError" style="display:none; padding-left:12em " class="errors">
                               <li id="passwordError1"></li>
                           </ul>
                       </td>
                       <td>
                               <%--<c:if test="${empty command.participant.id}">
                               (The minimum password length should
                               be ${command.passwordPolicy.passwordCreationPolicy.minPasswordLength})
                               </c:if>--%>
                       </td>
                   </tr>
                   <tr>
                       <td>
                           <tags:renderPassword required="true" propertyName="participant.user.confirmPassword"
                                                displayName="participant.label.confirmpassword"
                                                onblur="checkPasswordMatch();"/>
                           <ul id="passwordErrorConfirm" style="display:none; padding-left:12em " class="errors">
                               <li id="passwordErrorConfirm1"></li>
                           </ul>
                       </td>
                   </tr>
               </table>

           </div>
       <br>
       </chrome:division>
       <div id="studies" style="display:none">
           <chrome:division title="participant.label.studies"/>
       </div>
        <div id="studysitestable">

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
                                        participant="${command.participant}"/>
                    </c:forEach>

                </table>


            </c:if>

        </div>
</jsp:attribute>
</tags:tabForm>
</body>
</html>