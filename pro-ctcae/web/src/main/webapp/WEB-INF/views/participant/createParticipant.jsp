<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>
    <tags:dwrJavascriptLink objects="studyParticipantAssignment"/>


    <script>

        function getStudySites() {
            var organizationId = $('organizationId').value;
            if (organizationId == '') {
                $("studysitestable").innerHTML = '';
                return;
            }
            var request = new Ajax.Request("<c:url value="/pages/participant/displaystudysites"/>", {
                onComplete:function(transport) {
                    var response = transport.responseText;
                    $("studysitestable").innerHTML = response;
                },
                parameters:"subview=subview&organizationId=" + organizationId,
                method:'get'
            })
        }

        Event.observe(window, 'load', function() {
            getStudySites();
            Event.observe('organizationId', 'change', function() {
                getStudySites();
            })
        });
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
                parameters:"id=" + id + "&subview=subview",
                onComplete:function(transport) {
                    showConfirmationWindow(transport, 600, 350);
                },
                method:'get'
            })


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
</head>
<body>
<%--<chrome:flashMessage flashMessage="participant.flash.save"></chrome:flashMessage>--%>
<tags:tabForm tab="${tab}" flow="${flow}" willSave="true">
   <jsp:attribute name="singleFields">
       <c:choose>
           <c:when test="${command.mode eq 'Y'}">
               <c:set var="required" value="false"/>
               <c:set var="maxLength" value="1"/>
           </c:when>
           <c:otherwise>
               <c:set var="required" value="true"/>
               <c:set var="maxLength" value="1"/>
           </c:otherwise>
       </c:choose>
           <chrome:division title="participant.label.site">
               <c:choose>
                   <c:when test="${not empty command.participant.studyParticipantAssignments}">
                       <input type="hidden" name="organizationId" id="organizationId"
                              value="${command.organizationId}"/>

                       <div class="row">
                           <div class="label"><spring:message code="participant.label.site"/>:</div>
                           <div class="value">${command.siteName}</div>
                       </div>
                   </c:when>
                   <c:otherwise>
                       <tags:renderSelect propertyName="organizationId" displayName="participant.label.site"
                                          required="true" options="${organizationsHavingStudySite}"/>
                   </c:otherwise>
               </c:choose>
           </chrome:division>

           <chrome:division title="participant.label.demographic_information">

               <table border="0" style="width:100%">

                   <tr>
                       <td>
                           <tags:renderText propertyName="participant.firstName"
                                            displayName="participant.label.first_name"
                                            required="true" maxLength="${maxLength}"/>
                           <tags:renderText propertyName="participant.middleName"
                                            displayName="participant.label.middle_name" maxLength="${maxLength}"/>
                           <tags:renderText propertyName="participant.lastName"
                                            displayName="participant.label.last_name"
                                            required="true" maxLength="${maxLength}"/>
                       </td>
                       <td>
                           <tags:renderDate propertyName="participant.birthDate"
                                            displayName="participant.label.date_of_birth"/>
                           <tags:renderSelect propertyName="participant.gender" displayName="participant.label.gender"
                                              required="${required}" options="${genders}"/>
                           <tags:renderText propertyName="participant.assignedIdentifier"
                                            displayName="participant.label.participant_identifier"
                                            required="${required}"/>
                       </td>
                   </tr>
               </table>
           </chrome:division>
           <chrome:division title="participant.label.contact_information">

               <table border="0" style="width:100%">
                   <tr>
                       <td>
                           <tags:renderEmail propertyName="participant.emailAddress"
                                             displayName="participant.label.email_address"
                                             required="false" size="35"/>
                       </td>
                       <td>
                           <tags:renderPhoneOrFax propertyName="participant.phoneNumber"
                                                  displayName="participant.label.phone"
                                                  required="${required}"/>
                       </td>
                   </tr>
               </table>
           </chrome:division>
       <chrome:division title="participant.label.logininfo">
           <tags:renderText propertyName="participant.user.username"
                            displayName="participant.label.username"
                            required="true"/>

           <tags:renderPassword required="true" propertyName="participant.user.password"
                                displayName="participant.label.password"/>
           <tags:renderPassword required="true" propertyName="participant.user.confirmPassword"
                                displayName="participant.label.confirmpassword"/>
       </chrome:division>
           <chrome:division title="participant.label.studies"/>

           <chrome:division id="single-fields">
               <div id="studysitestable"/>
           </chrome:division>
   </jsp:attribute>
</tags:tabForm>
</body>
</html>