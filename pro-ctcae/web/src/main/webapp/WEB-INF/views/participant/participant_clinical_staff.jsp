<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="study" tagdir="/WEB-INF/tags/study" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<html>
<head>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>
    <tags:dwrJavascriptLink objects="clinicalStaff"/>
    <script type="text/javascript">
        Event.observe(window, "load", function() {
            initSearchField()
        <c:forEach items="${command.participant.studyParticipantAssignments}" var="studyParticipantAssignment" varStatus="status">
            var baseNamePhysician_${status.index} = 'participant.studyParticipantAssignments[${status.index}].treatingPhysician.studyOrganizationClinicalStaff';
            acCreate(new studyOrganizationClinicalStaffForRoleAutoCompleter(baseNamePhysician_${status.index}, '${command.studySite[0].id}', 'TREATING_PHYSICIAN'))
            initializeAutoCompleter(baseNamePhysician_${status.index},
                    '${studyParticipantAssignment.treatingPhysician ne null ? studyParticipantAssignment.treatingPhysician.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName:""}',
                    '${studyParticipantAssignment.treatingPhysician ne null ? studyParticipantAssignment.treatingPhysician.studyOrganizationClinicalStaff.id:""}');

            var baseNameNurse_${status.index} = 'participant.studyParticipantAssignments[${status.index}].researchNurse.studyOrganizationClinicalStaff';
            acCreate(new studyOrganizationClinicalStaffForRoleAutoCompleter(baseNameNurse_${status.index}, '${command.studySite[0].id}', 'RESEARCH_NURSE'))
            initializeAutoCompleter(baseNameNurse_${status.index},
                    '${studyParticipantAssignment.researchNurse ne null ? studyParticipantAssignment.researchNurse.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName:""}',
                    '${studyParticipantAssignment.researchNurse ne null ? studyParticipantAssignment.researchNurse.studyOrganizationClinicalStaff.id:""}');
        <c:forEach items="${studyParticipantAssignment.notificationClinicalStaff}" var="clinicalStaff" varStatus="notificationstatus">
            var baseNameNotification_${status.index}_${notificationstatus.index} = 'participant.studyParticipantAssignments[${status.index}].notificationClinicalStaff[${notificationstatus.index}]';
            acCreate(new studyOrganizationClinicalStaffForRoleAutoCompleter(baseNameNotification_${status.index}_${notificationstatus.index}, '${command.studySite[0].id}', 'TREATING_PHYSICIAN|RESEARCH_NURSE'))
            initializeAutoCompleter(baseNameNotification_${status.index}_${notificationstatus.index},
                    '${studyParticipantAssignment.notificationClinicalStaff[notificationstatus.index].studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}',
                    '${studyParticipantAssignment.notificationClinicalStaff[notificationstatus.index].studyOrganizationClinicalStaff.id}');

        </c:forEach>
        </c:forEach>

        })

        function addNotificationClinicalStaff(index) {
            var request = new Ajax.Request("<c:url value="/pages/participant/addNotificationClinicalStaff"/>", {
                onComplete:addNotificationClinicalStaffDiv,
                parameters:"subview=subview&index="+index,
                method:'get'
            })
        }

        function addNotificationClinicalStaffDiv(transport) {
            var response = transport.responseText;
            new Insertion.Before("hiddenDiv", response);
        }
    </script>
</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}">
    <jsp:attribute name="repeatingFields">
    <c:forEach items="${command.participant.studyParticipantAssignments}" var="studyParticipantAssignment"
               varStatus="status">
        <chrome:division title="${studyParticipantAssignment.studySite.study.shortTitle}" message="false">
            <tags:renderAutocompleter
                    propertyName="participant.studyParticipantAssignments[${status.index}].treatingPhysician.studyOrganizationClinicalStaff"
                    displayName="participant.label.clinical.staff.treatingphysician" noForm="true"
                    required="true"
                    propertyValue="${studyParticipantAssignment.treatingPhysician ne null ? studyParticipantAssignment.treatingPhysician.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName:''}"/>
            <tags:renderAutocompleter
                    propertyName="participant.studyParticipantAssignments[${status.index}].researchNurse.studyOrganizationClinicalStaff"
                    displayName="participant.label.clinical.staff.researchnurse" noForm="true"
                    required="true"
                    propertyValue="${studyParticipantAssignment.researchNurse ne null ? studyParticipantAssignment.researchNurse.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName:''}"/>

        </chrome:division>
        <table class="top-widget" width="100%">
            <tr>
                <td>
                    <c:forEach items="${studyParticipantAssignment.notificationClinicalStaff}" var="clinicalStaff"
                               varStatus="notificationstatus">
                        <tags:notificationClinicalStaff index="${status.index}"
                                                        notificationindex="${notificationstatus.index}"
                                                        clinicalStaff="${clinicalStaff}"></tags:notificationClinicalStaff>
                    </c:forEach>
                    <div id="hiddenDiv"></div>
                    <div class="local-buttons">
                        <tags:button type="anchor" onClick="javascript:addNotificationClinicalStaff('${status.index}')"
                                     value="participant.clinical_staff.add"/>
                    </div>
                </td>
            </tr>
        </table>
    </c:forEach>
    </jsp:attribute>


</tags:tabForm>

</body>
</html>