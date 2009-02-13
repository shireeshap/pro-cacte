<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib uri="http://www.extremecomponents.org" prefix="ec" %>
<link rel="stylesheet" type="text/css"
      href="<c:url value="/css/extremecomponents.css"/>">

<html>
<head>
    <script>

        function getStudySites() {
            var organizationId = ${participantCommand.organizationId};
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
        });

        function showForms(obj, id) {
            var row = $('forms_' + id);
            if (obj.checked) {
                row.show();
            } else {
                row.hide();
            }
        }

    </script>
</head>
<body>

<form:form method="post" commandName="participantCommand">
    <chrome:box title="participant.participant_details" autopad="true">
        <p><tags:instructions code="participant.participant_overview.top"/></p>
        <chrome:division title="participant.label.site">
            <b>${participantCommand.siteName}</b>
        </chrome:division>
        <chrome:division title="participant.label.demographic_information">
            <input type="hidden" name="participant.id" value="${participantCommand.participant.id}"/>

            <table border="0" style="width:100%">
                <tr>
                    <td>
                        <tags:renderText propertyName="participant.firstName" displayName="participant.label.first_name"
                                         required="true"/>
                        <tags:renderText propertyName="participant.lastName" displayName="participant.label.last_name"
                                         required="true"/>
                        <tags:renderText propertyName="participant.middleName"
                                         displayName="participant.label.middle_name"/>
                        <tags:renderText propertyName="participant.assignedIdentifier"
                                         displayName="participant.label.participant_identifier"
                                         required="true"/>
                    </td>
                    <td>
                        <tags:renderDate propertyName="participant.birthDate"
                                         displayName="participant.label.date_of_birth"
                                         required="true"/>
                        <tags:renderSelect propertyName="participant.gender" displayName="participant.label.gender"
                                           required="true" options="${genders}"/>
                        <tags:renderSelect propertyName="participant.ethnicity"
                                           displayName="participant.label.ethnicity"
                                           required="true" options="${ethnicities}"/>
                        <tags:renderSelect propertyName="participant.race" displayName="participant.label.race"
                                           required="true" options="${races}"/>
                    </td>
                </tr>
            </table>
        </chrome:division>
        <chrome:division title="participant.label.studies"/>

        <chrome:division id="single-fields">
            <div id="studysitestable"/>
        </chrome:division>
    </chrome:box>
    <tags:tabControls willSave="true"/>
</form:form>
</body>
</html>
