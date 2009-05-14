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
            var row = $('forms_' + id);
            try {
                if (obj.checked) {
                    row.show();
                } else {
                    row.hide();
                }
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
</head>
<body>
<%--<chrome:flashMessage flashMessage="participant.flash.save"></chrome:flashMessage>--%>
<tags:tabForm tab="${tab}" flow="${flow}" willSave="true">
   <jsp:attribute name="singleFields">
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
                                            required="true"/>
                           <tags:renderText propertyName="participant.middleName"
                                            displayName="participant.label.middle_name"/>
                           <tags:renderText propertyName="participant.lastName"
                                            displayName="participant.label.last_name"
                                            required="true"/>
                       </td>
                       <td>
                           <tags:renderDate propertyName="participant.birthDate"
                                            displayName="participant.label.date_of_birth"
                                            required="true"/>
                           <tags:renderSelect propertyName="participant.gender" displayName="participant.label.gender"
                                              required="true" options="${genders}"/>
                           <tags:renderText propertyName="participant.assignedIdentifier"
                                            displayName="participant.label.participant_identifier"
                                            required="true"/>
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
                                                  required="true"/>
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

           <%--<div class="row">--%>
           <%--<div class="label"><span class="required-indicator">*</span> <spring:message--%>
           <%--code="participant.label.password"/></div>--%>
           <%--<div class="value"><input type="password" name="participant.user.password"--%>
           <%--id="participant.user.password"--%>
           <%--value="${command.participant.user.password}" class="validate-NOTEMPTY"/></div>--%>
           <%--</div>--%>
           <%--<div class="row">--%>
           <%--<div class="label"><span class="required-indicator">*</span> <spring:message--%>
           <%--code="participant.label.confirmpassword"/></div>--%>
           <%--<div class="value"><input type="password"--%>
           <%--name="participant.user.confirmPassword"--%>
           <%--id="participant.user.confirmPassword"--%>
           <%--value="${command.participant.user.confirmPassword}" class="validate-NOTEMPTY"/>--%>
           <%--</div>--%>
           <%--</div>--%>
       </chrome:division>
           <chrome:division title="participant.label.studies"/>

           <chrome:division id="single-fields">
               <div id="studysitestable"/>
           </chrome:division>
   </jsp:attribute>
</tags:tabForm>
</body>
</html>