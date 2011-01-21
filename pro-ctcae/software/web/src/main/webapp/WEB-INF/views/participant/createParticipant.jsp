<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<tags:dwrJavascriptLink objects="uniqueParticipantIdentifier,uniqueParticipantUserNumber,uniqueParticipantEmailAddress"/>
<%--<tags:dwrJavascriptLink objects="uniqueParticipantUserNumber"/>--%>


<html>
<head>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>
    <%--<tags:dwrJavascriptLink objects="studyParticipantAssignment"/>--%>


    <script>
        function checkParticipantEmailAddress(){
            var participantId = "${param['id']}";
            var email = $('participant.emailAddress').value;
            if(email !=""){
                uniqueParticipantEmailAddress.validateEmail(email,participantId,emailReturnValue);
                return;
            }
            else{
                jQuery('#emailError').hide();
            }
        }
        function emailReturnValue(returnValue){
              if(returnValue){
                jQuery('#emailError').show();
            }
            else{
                jQuery('#emailError').hide();
            }
        }
        function checkParticipantUserNumber(){
            var participantId = "${param['id']}";
            var userNumber = $('participant.userNumber').value;
            if(userNumber !=""){
                uniqueParticipantUserNumber.validateUserNumber(userNumber,participantId,returnedValue);
                return;
            }
            else{
                jQuery('#userNameError').hide();
            }

        }

        function returnedValue(returnValue){
            if(returnValue){
                jQuery('#userNameError').show();
            }
            else{
                jQuery('#userNameError').hide();   
            }

        }
        function checkParticipantStudyIdentifier(id,siteId) {
             var participantId = "${param['id']}";
             var identifier = $('participantStudyIdentifier_'+siteId).value;
            if (identifier != "") {
                 uniqueParticipantIdentifier.validateUniqueParticipantIdentifier(id,identifier,participantId,postCommentHandler);
                 return;
            }
            else {
                jQuery('#uniqueError').hide();
            }

        }

         function postCommentHandler(returnvalue) {
            if (returnvalue)
            {
                jQuery('#uniqueError').show();

            }
             else{
                 jQuery('#uniqueError').hide();
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

        Event.observe(window, 'load', function() {
       
        <c:if test="${command.admin eq true && empty command.participant.studyParticipantAssignments}">
            try {
                acCreate(new siteAutoComplter('organizationId'))
            } catch(err) {
            }
        </c:if>
            Event.observe('organizationId', 'change', function() {
                getStudySites();
            })
        });
        function doPostProcessing() {
            getStudySites();
        }

        function showForms(obj, id) {
            var sites = document.getElementsByName('studySites');
            for (var i = 0; i < sites.length; i++) {
                $('subform_' + sites[i].value).hide();
                $('participantStudyIdentifier_' + sites[i].value).removeClassName("validate-NOTEMPTY");
                try {
                    $('arm_' + sites[i].value).removeClassName("validate-NOTEMPTY");
                } catch(e) {
                }
            }
            $('subform_' + id).show();
            $('participantStudyIdentifier_' + id).addClassName("validate-NOTEMPTY");
            try {
                $('arm_' + id).addClassName("validate-NOTEMPTY");
            } catch(e) {
            }
            AE.registerCalendarPopups();
        }

        function participantOffStudy(id) {
            var request = new Ajax.Request("<c:url value="/pages/participant/participantOffStudy"/>", {
                parameters:<tags:ajaxstandardparams/>+"&id=" + id  ,
                onComplete:function(transport) {
                    showConfirmationWindow(transport, 600, 350);
                },
                method:'get'
            })
        }
        function participantOffHold(id, date) {
            var request = new Ajax.Request("<c:url value="/pages/participant/participantOffHold"/>", {
                parameters:<tags:ajaxstandardparams/>+"&id=" + id + "&date=" + date,
                onComplete:function(transport) {
                    showConfirmationWindow(transport, 600, 350);
                },
                method:'get'
            })
        }

        function participantOnHold(id, date) {
            var request = new Ajax.Request("<c:url value="/pages/participant/participantOnHold"/>", {
                parameters:<tags:ajaxstandardparams/>+"&id=" + id + "&date=" + date,
                onComplete:function(transport) {
                    showConfirmationWindow(transport, 600, 350);
                },
                method:'get'
            })
        }

        function participantRptModeHistoryDisplay(id) {
            var request = new Ajax.Request("<c:url value="/pages/participant/participantReportingModeHistory"/>", {
                parameters:<tags:ajaxstandardparams/>+"&id=" + id, 
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

        <%--var clickCount = ${homeModeCount};--%>
        function showOrHideEmail(value1, value2, id) {
            if (value1 && value2 == "HOMEWEB") {
                jQuery('#div_contact').show();
                jQuery('#div_contact_ivrs').hide();
                jQuery('#web_' + id).show();
                jQuery('#email_' + id).attr('checked', true);
                jQuery('#call_' + id).attr('checked', false);

            } else {
                jQuery('#web_' + id).show();
            }
            if (value1 && value2 == "IVRS") {
                jQuery('#div_contact').show();
                jQuery('#div_contact_ivrs').show();
                jQuery('#ivrs_' + id).show();
                jQuery('#c_' + id).show();
                jQuery('#reminder_' + id).show();
                jQuery('#ivrs_reminder_' + id).show();
                jQuery('#call_' + id).attr('checked', true);
                jQuery('#email_' + id).attr('checked', false);
                jQuery('#web_' + id).show();

            } else {
                jQuery('#ivrs_' + id).hide();
                jQuery('#ivrs_reminder_' + id).hide();
                jQuery('#reminder_' + id).hide();
                jQuery('#c_' + id).hide();
            }
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
<tags:tabForm tab="${tab}" flow="${flow}" willSave="true">
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
       
           <chrome:division title="participant.label.site">
               <c:choose>
                   <c:when test="${not empty command.participant.studyParticipantAssignments}">
                       <input type="hidden" name="organizationId" id="organizationId"
                              value="${command.organizationId}"/>

                       <div class="row">
                           <div class="label"><spring:message code="participant.label.site"/>:&nbsp;</div>
                           <div class="value">${command.siteName}</div>
                       </div>
                   </c:when>
                   <c:otherwise>
                       <c:choose>
                           <c:when test="${command.admin}">
                               <tags:renderAutocompleter propertyName="organizationId"
                                                         displayName="participant.label.site"
                                                         required="true" size="70"/>
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

                   <tr>
                       <td width="50%">
                           <tags:renderText propertyName="participant.firstName"
                                            displayName="participant.label.first_name"
                                            required="true" maxLength="${maxLength}" size="${maxLength}"/>
                           <c:if test="${command.mode eq 'N'}">
                               <tags:renderText propertyName="participant.middleName"
                                                displayName="participant.label.middle_name" maxLength="${maxLength}"
                                                size="${maxLength}"/>
                           </c:if>
                           <tags:renderText propertyName="participant.lastName"
                                            displayName="participant.label.last_name"
                                            required="true" maxLength="${maxLength}" size="${maxLength}"/>
                       </td>

                       <td width="50%" valign="top">
                           <c:if test="${command.mode eq 'N'}">
                               <tags:renderDate propertyName="participant.birthDate"
                                                displayName="participant.label.date_of_birth" required="true"/>
                           </c:if>
                           <tags:renderSelect propertyName="participant.gender" displayName="participant.label.gender"
                                              required="${required}" options="${genders}"/>
                           <c:if test="${command.mode eq 'N'}">
                               <tags:renderText propertyName="participant.assignedIdentifier"
                                                displayName="participant.label.participant_identifier"
                                                required="true"/>
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
                                                 required="false" size="35"/>
                           </td>
                           <td width="50%">
                               <tags:renderPhoneOrFax propertyName="participant.phoneNumber"
                                                      displayName="participant.label.phone"
                                                      required="${required}"/>
                           </td>
                       </tr>
                   </table>
               </chrome:division>
           </c:when>
           <c:otherwise>
               <div id="div_contact">
                   <chrome:division title="participant.label.contact_information">

                       <table border="0" style="width:100%">
                           <tr>
                               <td width="50%">
                                   <tags:renderEmail propertyName="participant.emailAddress"
                                                     displayName="participant.label.email_address"
                                                     required="${emailreq}" size="35" onblur="javascript:checkParticipantEmailAddress()"/>
                               </td>
                               <td width="50%">
                                   <tags:renderPhoneOrFax propertyName="participant.phoneNumber"
                                                          displayName="participant.label.phone"
                                                          required="${phonereq}"/>
                               </td>
                           </tr>
                            <tr>
                               <td>
                                   <ul id="emailError" style="display:none; padding-left:12em " class="errors">
                                        <li><spring:message code='participant.unique_emailAddress'
                                                       text='participant.unique_emailAddress'/></li>
                                   </ul>
                               </td>
                           </tr>
                       </table>
                   </chrome:division>
               </div>

           </c:otherwise>
       </c:choose>
       <%--<c:if test="${command.mode eq 'N' || showContact == true}">--%>
       <%----%>
       <%--</c:if>--%>


       <%--<div id="div_contact" style="display:none">--%>
       <%--<chrome:division title="participant.label.contact_information">--%>
       <%--<div id="div_email" style="display:none">--%>
       <%--<table border="0" style="width:100%">--%>
       <%----%>
       <%--<tr>--%>
       <%--<td width="50%">--%>
       <%--<tags:renderEmail propertyName="participant.emailAddress"--%>
       <%--displayName="participant.label.email_address"--%>
       <%--required="false" size="35"/>--%>
       <%----%>
       <%--</td>--%>
       <%----%>
       <%--</tr>--%>
       <%--</table>--%>


       <%--<table border="0" style="width:100%">--%>
       <%--<tr>--%>
       <%--<td width="50%">--%>
       <%--<tags:renderPhoneOrFax propertyName="participant.phoneNumber"--%>
       <%--displayName="participant.label.phone"--%>
       <%--required="${req}"/>--%>
       <%--</td>--%>
       <%----%>
       <%--</tr>--%>
       <%--</table>--%>
       <%--</div>--%>
       <%--</chrome:division>--%>
       <%--</div>--%>
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
                                                required="true"/>
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
                                                displayName="participant.label.password"/>
                       </td>
                       <td>(The minimum password length should
                           be ${command.passwordPolicy.passwordCreationPolicy.minPasswordLength})
                       </td>
                   </tr>
                   <tr>
                       <td>
                           <tags:renderPassword required="true" propertyName="participant.user.confirmPassword"
                                                displayName="participant.label.confirmpassword"/>
                       </td>
                   </tr>
               </table>

           </div>
      
       <div id="div_contact_ivrs" style="display:none;">

                       <table border="0" cellpadding="0" cellspacing="0">
                           <tr>
                               <td>
                                   <tags:renderText propertyName="participant.userNumber"
                                                    displayName="participant.label.user_number"
                                                    onblur="checkParticipantUserNumber();"/>
                                    <ul id="userNameError" style="display:none; padding-left:12em " class="errors">
                                        <li><spring:message code='participant.unique_userNumber'
                                                       text='participant.unique_userNumber'/></li>
                                   </ul>
                               </td>

                           </tr>
                           <tr>
                               <td>


                                    <tags:renderText propertyName="participant.pinNumber"
                                                     displayName="participant.label.pin_number"/>
                               </td>
                           </tr>
                       </table>

                     </chrome:division>
               </div>
    <chrome:division title="participant.label.studies"/>
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
                    <c:forEach items="${command.participant.studyParticipantAssignments}" var="studyParticipantAssignment" varStatus="spastatus">
                        <c:set var="studysite" value="${studyParticipantAssignment.studySite}"/>
                        <tags:studySite studysite="${studysite}" selected="true" isEdit="true"
                                        studyParticipantAssignment="${studyParticipantAssignment}"/>
                    </c:forEach>

                </table>



            </c:if>

        </div>
</jsp:attribute>
</tags:tabForm>
</body>
</html>