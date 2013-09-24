/*global jQuery, $, clearInput, isPinError, alert */
/*jslint plusplus: true, sloppy: true, browser: true */

//remove leading and trailing spaces
String.prototype.trim = function () {
    return this.replace(/^\s+|\s+$/g, '');
};

String.prototype.isEmpty = function () {
    // if null or undefined or empty string return true
    if (!this || this.trim() === '') {
        return true;
    }
    return false;
};

//Not in CP_NS as its universally invoked from yuiAutocompleter.tag
var clearInput = function (inputId) {
    jQuery('#' + inputId).val('');
    jQuery('#' + inputId + '-input').val('');
    jQuery('#' + inputId + '-input').focus();
    jQuery('#' + inputId + '-input').blur();
};

// The createParticipant Global namespace
var CP_NS = {
    isIdentifierError : false,
    isUserIdError : false,
    isPinError : false,
    isEmail : false,
    isBlackoutCallTime : false
};

// validation check for participant pin number (IVRS)
CP_NS.checkParticipantPinNumber = function (siteId) {
    var pinPattern, pinNumber;
    pinPattern = /^[0-9]{4}$/;
    //escape the dot in the id value. 
    pinNumber = jQuery('#participant\\.pinNumber_' + siteId).val();
    if (!pinNumber.isEmpty()) {
        if (!pinPattern.test(pinNumber)) {
            jQuery('#PinPatternError_' + siteId).show();
            CP_NS.isPinError = true;
        } else {
            CP_NS.isPinError = false;
            jQuery('#PinPatternError_' + siteId).hide();
        }
    } else {
        jQuery('#PinPatternError_' + siteId).hide();
        CP_NS.isPinError = false;
    }
};

CP_NS.showForms = function (obj, id) {
    var sites, i;
    sites = document.getElementsByName('studySites');
    for (i = 0; i < sites.length; i++) {
        $('subform_' + sites[i].value).hide();
        $('participantStudyIdentifier_' + sites[i].value)
            .removeClassName("validate-NOTEMPTY");
        $('participantStudyIdentifier_' + sites[i].value).value = "";
        jQuery('#uniqueError_' + sites[i].value).hide();
        jQuery('#UserPatternError_' + sites[i].value).hide();
        jQuery('#PinPatternError_' + sites[i].value).hide();
        jQuery('#confirmPinError_' + sites[i].value).hide();
        jQuery('#userNumberError_' + sites[i].value).hide();
        jQuery('#PhonePatternError').hide();
        jQuery('#preferred.calltime.error_' + sites[i].value).hide();
        jQuery('#phoneNumberError').hide();
        jQuery('#emailError_' + sites[i].value).hide();
        try {
            $('arm_' + sites[i].value).removeClassName("validate-NOTEMPTY");
        } catch (e1) {
        }
    }
    $('subform_' + id).show();
    $('participantStudyIdentifier_' + id).addClassName("validate-NOTEMPTY");
    CP_NS.isIdentifierError = false;
    CP_NS.isUserIdError = false;
    CP_NS.isPinError = false;
    CP_NS.isEmail = false;
    CP_NS.isBlackoutCallTime = false;
    try {
        $('arm_' + id).addClassName("validate-NOTEMPTY");
    } catch (e2) {
    }
    AE.registerCalendarPopups();
};

CP_NS.showpassword = function (show) {
    if (show) {
        $('passwordfields').show();
        $('resetpass').innerHTML = '<a href="javascript:CP_NS.showpassword(false);">Hide password</a>';
    } else {
        $('passwordfields').hide();
        $('resetpass').innerHTML = '<a href="javascript:CP_NS.showpassword(true);">Reset password</a>';
    }
};

CP_NS.validateAndSubmit = function (date, form) {
    if (date.isEmpty()) {
        alert('Please provide a valid date');
        return;
    }
    form.submit();
};

CP_NS.addIVRSRemoveEmailClassName = function (id) {
    $('participant.userNumber_' + id).addClassName("validate-NOTEMPTY");
    $('participant.phoneNumber').addClassName("validate-NOTEMPTY&&US_PHONE_NO");
    $('participant.pinNumber_' + id).addClassName("validate-NOTEMPTY");
    $('participant.confirmPinNumber_' + id).addClassName("validate-NOTEMPTY");
    $('call_hour_' + id).addClassName("validate-NOTEMPTY");
    $('call_minute_' + id).addClassName("validate-NOTEMPTY");
    $('call_ampm_' + id).addClassName("validate-NOTEMPTY");
    $('call_timeZone_' + id).addClassName("validate-NOTEMPTY");
    $('ivrs_lang_' + id).addClassName("validate-NOTEMPTY");
    $('home_web_lang_' + id).removeClassName("validate-NOTEMPTY");
    $('home_web_lang_' + id).required = false;
};

CP_NS.showPhone = function (id, val) {
    if (val) {
        jQuery('#ivrsLanguage_' + id).show();
        jQuery('#callTime_' + id).show();
        $('call_hour_' + id).addClassName("validate-NOTEMPTY");
        $('call_minute_' + id).addClassName("validate-NOTEMPTY");
        $('call_ampm_' + id).addClassName("validate-NOTEMPTY");
        $('call_timeZone_' + id).addClassName("validate-NOTEMPTY");
        $('ivrs_lang_' + id).addClassName("validate-NOTEMPTY");
        $('participant.phoneNumber').disabled = false;
        $('declinePhNum').checked = false;
        $('declinePhNum').disabled = true;
        $('participant.phoneNumber').removeClassName("validate-US_PHONE_NO");
        $('participant.phoneNumber')
            .addClassName("validate-NOTEMPTY&&US_PHONE_NO");
    } else {
        jQuery('#ivrsLanguage_' + id).hide();
        jQuery('#callTime_' + id).hide();
        $('call_hour_' + id).removeClassName("validate-NOTEMPTY");
        $('call_hour_' + id).required = false;
        $('call_minute_' + id).removeClassName("validate-NOTEMPTY");
        $('call_minute_' + id).required = false;
        $('call_ampm_' + id).removeClassName("validate-NOTEMPTY");
        $('call_ampm_' + id).required = false;
        $('call_timeZone_' + id).removeClassName("validate-NOTEMPTY");
        $('call_timeZone_' + id).required = false;
        $('ivrs_lang_' + id).removeClassName("validate-NOTEMPTY");
        $('ivrs_lang_' + id).required = false;
        $('declinePhNum').disabled = false;
        $('participant.phoneNumber').required = false;
    }
};

CP_NS.showEmail = function (id, val) {
    if (val) {
        $('participant.emailAddress').removeClassName("validate-EMAIL");
        $('participant.emailAddress').addClassName("validate-EMAIL&&NOTEMPTY");
    } else {
        $('participant.emailAddress').removeClassName("validate-NOTEMPTY");
        $('participant.emailAddress')
            .removeClassName("validate-EMAIL&&NOTEMPTY");
        $('participant.emailAddress').addClassName("validate-EMAIL");
        $('participant.emailAddress').required = false;
    }
};

CP_NS.addEmailRemoveIVRSClassName = function () {
    var participantId = "${param['id']}";
    if (participantId.isEmpty()) {
        participantId = "${patientId}";
    }
};

CP_NS.showOrHideEmail = function (value1, value2, id) {
    CP_NS.isUserIdError = false;
    CP_NS.isPinError = false;
    CP_NS.isEmail = false;
    CP_NS.isBlackoutCallTime = false;

    if (value1 && value2 === "HOMEWEB") {
        jQuery("#home_web_lang_" + id).show();
        jQuery('#div_contact').show();
        jQuery('#web_' + id).show();
        jQuery('#email_' + id).attr('checked', true);
        jQuery('#emailInput_' + id).show();
        if ($('participant.username_' + id) !== null) {
            $('participant.username_' + id).addClassName("validate-NOTEMPTY");
        }
        $('participant.password_' + id).addClassName("validate-NOTEMPTY");
        $('participant.confirmPassword_' + id)
                .addClassName("validate-NOTEMPTY");
        $('home_web_lang_' + id).addClassName("validate-NOTEMPTY");
        $('participant.emailAddress').addClassName("validate-NOTEMPTY");
        CP_NS.showEmail(id, true);
    }

    if (!value1 && value2 === "HOMEWEB") {
        jQuery('#web_' + id).show();
        jQuery("#home_web_lang_" + id).hide();
        jQuery('#web_' + id).hide();
        jQuery('#email_' + id).attr('checked', false);
        if ($('participant.username_' + id) !== null) {
            $('participant.username_' + id)
                    .removeClassName("validate-NOTEMPTY");
            $('participant.username_' + id).required = false;
        }
        $('home_web_lang_' + id).removeClassName("validate-NOTEMPTY");
        $('home_web_lang_' + id).required = false;
        $('participant.password_' + id).removeClassName("validate-NOTEMPTY");
        $('participant.password_' + id).required = false;
        $('participant.confirmPassword_' + id)
            .removeClassName("validate-NOTEMPTY");
        $('participant.confirmPassword_' + id).required = false;
        $('participant.emailAddress').removeClassName("validate-NOTEMPTY");
        $('participant.emailAddress')
            .removeClassName("validate-EMAIL&&NOTEMPTY");
        $('participant.emailAddress').required = false;
    }

    if (value1 && value2 === "HOMEBOOKLET") {
        jQuery('#home_paper_' + id).show();
        $('home_paper_lang_' + id).addClassName("validate-NOTEMPTY");
    }

    if (!value1 && value2 === "HOMEBOOKLET") {
        jQuery('#home_paper_' + id).hide();
        $('home_paper_lang_' + id).removeClassName("validate-NOTEMPTY");
        $('home_paper_lang_' + id).required = false;
        jQuery("#home_paper_lang_" + id).val('');
    }

    if (value1 && value2 === "IVRS") {
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
        CP_NS.addIVRSRemoveEmailClassName(id);
        CP_NS.showPhone(id, true);
    }

    if (!value1 && value2 === "IVRS") {
        jQuery('#call_' + id).attr('checked', false);
        jQuery('#participantUserNumber_' + id).val('');
        jQuery('#participantPinNumberConfirm_' + id).val('');
        CP_NS.addEmailRemoveIVRSClassName();
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
};

CP_NS.isSpclChar = function (fieldName) {
    var iChars, fieldValue, i;
    iChars = "!@#$%^&*+=[]\\\';,./{}|\":<>?";
    fieldValue = $(fieldName).value;
    jQuery('#' + fieldName + '.error').hide();
    $(fieldName + '.error').hide();
    for (i = 0; i < fieldValue.length; i++) {
        if (iChars.indexOf(fieldValue.charAt(i)) !== -1) {
            jQuery('#' + fieldName + '.error').show();
            $(fieldName + '.error').show();
            $(fieldName).value = "";
            return true;
        }
    }
    return false;
};

CP_NS.populateDefaultUserNumber = function (siteId) {
    var pNumber, nonFormattedPNumber, re, uNumber;
    pNumber = $('participant.phoneNumber').value;
    if (!pNumber.isEmpty()) {
        re = /[\-]/g;
        nonFormattedPNumber = pNumber.replace(re, "");
        uNumber = $('participant.userNumber_' + siteId).value;
        if (uNumber.isEmpty()) {
            $('participant.userNumber_' + siteId).value = nonFormattedPNumber;
        }
    }
};

CP_NS.togglePhoneNumber = function (isChecked) {
    if (isChecked) {
        $('participant.phoneNumber')
            .removeClassName("validate-NOTEMPTY&&US_PHONE_NO");
        $('participant.phoneNumber').disabled = true;
    } else {
        $('participant.phoneNumber').disabled = false;
        $('participant.phoneNumber')
            .addClassName("validate-NOTEMPTY&&US_PHONE_NO");
    }
};
