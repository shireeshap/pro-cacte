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
            var baseNamePhysician = 'participant.studyParticipantAssignments[${status.index}].treatingPhysician.studyOrganizationClinicalStaff';

            acCreate(new studyOrganizationClinicalStaffForRoleAutoCompleter(baseNamePhysician, '${command.studySite[0].id}', 'TREATING_PHYSICIAN'))

            initializeAutoCompleter(baseNamePhysician,
                    '${studyParticipantAssignment.treatingPhysician ne null ? studyParticipantAssignment.treatingPhysician.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName:""}',
                    '${studyParticipantAssignment.treatingPhysician ne null ? studyParticipantAssignment.treatingPhysician.studyOrganizationClinicalStaff.id:""}');

            var baseNameNurse = 'participant.studyParticipantAssignments[${status.index}].researchNurse.studyOrganizationClinicalStaff';
            acCreate(new studyOrganizationClinicalStaffForRoleAutoCompleter(baseNameNurse, '${command.studySite[0].id}', 'RESEARCH_NURSE'))
            initializeAutoCompleter(baseNameNurse,
                    '${studyParticipantAssignment.researchNurse ne null ? studyParticipantAssignment.researchNurse.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName:""}',
                    '${studyParticipantAssignment.researchNurse ne null ? studyParticipantAssignment.researchNurse.studyOrganizationClinicalStaff.id:""}');

        <c:forEach items="${studyParticipantAssignment.notificationClinicalStaff}" var="clinicalStaff" varStatus="notificationstatus">
            var baseNameNotification = 'participant.studyParticipantAssignments[${status.index}].notificationClinicalStaff[${notificationstatus.index}].studyOrganizationClinicalStaff';
            acCreate(new studyOrganizationClinicalStaffForRoleAutoCompleter(baseNameNotification, '${command.studySite[0].id}', 'TREATING_PHYSICIAN|RESEARCH_NURSE'))
            initializeAutoCompleter(baseNameNotification,
                    '${studyParticipantAssignment.notificationClinicalStaff[notificationstatus.index].studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}',
                    '${studyParticipantAssignment.notificationClinicalStaff[notificationstatus.index].studyOrganizationClinicalStaff.id}');

        </c:forEach>
        </c:forEach>

        })

        function addNotificationClinicalStaff(index) {
            $('notificationtable_' + index).show();
            var request = new Ajax.Request("<c:url value="/pages/participant/addNotificationClinicalStaff"/>", {
                onComplete:addNotificationClinicalStaffDiv,
                parameters:"subview=subview&index=" + index,
                method:'get'
            })
        }

        function addNotificationClinicalStaffDiv(transport) {

            var response = transport.responseText;
            new Insertion.Before("hiddenDiv", response);
        }

        function deleteNotification(spaIndex, notificationIndex) {
            var request = new Ajax.Request("<c:url value="/pages/confirmationCheck"/>", {
                parameters:"confirmationType=deleteNotificationClinicalStaff&subview=subview&spaIndex="
                        + spaIndex + "&notificationIndex=" + notificationIndex,
                onComplete:function(transport) {
                    showConfirmationWindow(transport, 530, 150);
                } ,
                method:'get'
            });
        }

        function deleteNotificationConfirm(spaIndex, notificationIndex) {
            closeWindow();
            $('notificationIndexToRemove').value = spaIndex + '~' + notificationIndex;
            submitClinicalStaffTabPage();

        }
        function submitClinicalStaffTabPage() {
            $('_target').name = "_target" + 1;
            $('command').submit();
        }


    </script>
    <style type="text/css">
        div.row div.label {
            width: 16em;
        }

        div.row div.value {
            margin-left: 17em;
        }
    </style>
</head>
<body>
<%--<chrome:flashMessage flashMessage="participant.flash.save"></chrome:flashMessage>--%>
<tags:tabForm tab="${tab}" flow="${flow}" notDisplayInBox="true">
    <jsp:attribute name="repeatingFields">
        <form:hidden path="notificationIndexToRemove" id="notificationIndexToRemove"/>

    <c:forEach items="${command.participant.studyParticipantAssignments}" var="studyParticipantAssignment"
               varStatus="status">
        <chrome:box title="${studyParticipantAssignment.studySite.study.shortTitle}" message="false">
            <chrome:division title="participant.primaryclinicalstaff"/>
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

            <br/>
            <chrome:division title="participant.otherclinicalstaff"/>
            <div align="left" style="margin-left: 50px">
                <table width="55%" class="tablecontent"
                       id="notificationtable_${status.index}">
                    <tr id="ss-table-head" class="amendment-table-head">
                        <th width="95%" class="tableHeader"><spring:message
                                code='participant.label.clinical.staff.notificationclinicalstaff'
                                text=''/></th>
                        <th width="5%" class="tableHeader" style=" background-color: none">&nbsp;</th>
                        <td style="border:none;">
                            <div align="right" style="margin-left: 50px">
                                <input type="button"
                                       onClick="javascript:addNotificationClinicalStaff('${status.index}')"
                                       value="<spring:message code='participant.clinical_staff.add'/>"/>
                                    <%--<tags:button type="anchor"--%>
                                    <%--onClick="javascript:addNotificationClinicalStaff('${status.index}')"--%>
                                    <%--value="participant.clinical_staff.add"/>--%>
                            </div>
                        </td>
                    </tr>
                    <c:forEach items="${studyParticipantAssignment.notificationClinicalStaff}"
                               var="clinicalStaff"
                               varStatus="notificationstatus">
                        <tags:notificationClinicalStaff index="${status.index}"
                                                        notificationindex="${notificationstatus.index}"
                                                        clinicalStaff="${clinicalStaff}"
                                                        studySiteId="${command.studySite[0].id}"></tags:notificationClinicalStaff>
                    </c:forEach>
                    <tr id="hiddenDiv" align="center"></tr>
                </table>
            </div>
        </chrome:box>
    </c:forEach>
    </jsp:attribute>

</tags:tabForm>
</body>
</html>