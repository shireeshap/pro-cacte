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
    <c:set var="studyParticipantAssignment" value="${command.selectedStudyParticipantAssignment}"/>
    <c:set var="varIndex" value="0"/>

    <script type="text/javascript">
        Event.observe(window, "load", function() {
            initSearchField()

        <c:forEach items="${command.participant.studyParticipantAssignments}" var="studyParticipantAssignment" varStatus="status">
        <c:if test="${studyParticipantAssignment.id eq command.selectedStudyParticipantAssignment.id}">
            var baseNamePhysician = 'participant.studyParticipantAssignments[${varIndex}].treatingPhysician.studyOrganizationClinicalStaff';

            acCreate(new studyOrganizationClinicalStaffForRoleAutoCompleter(baseNamePhysician, '${studyParticipantAssignment.studySite.id}', 'TREATING_PHYSICIAN'))

            initializeAutoCompleter(baseNamePhysician,
                    '${studyParticipantAssignment.treatingPhysician ne null ? studyParticipantAssignment.treatingPhysician.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName:""}',
                    '${studyParticipantAssignment.treatingPhysician ne null ? studyParticipantAssignment.treatingPhysician.studyOrganizationClinicalStaff.id:""}');

            var baseNameNurse = 'participant.studyParticipantAssignments[${varIndex}].researchNurse.studyOrganizationClinicalStaff';
            acCreate(new studyOrganizationClinicalStaffForRoleAutoCompleter(baseNameNurse, '${studyParticipantAssignment.studySite.id}', 'NURSE'))
            initializeAutoCompleter(baseNameNurse,
                    '${studyParticipantAssignment.researchNurse ne null ? studyParticipantAssignment.researchNurse.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName:""}',
                    '${studyParticipantAssignment.researchNurse ne null ? studyParticipantAssignment.researchNurse.studyOrganizationClinicalStaff.id:""}');

        <c:forEach items="${studyParticipantAssignment.notificationClinicalStaff}" var="clinicalStaff" varStatus="notificationstatus">
            var baseNameNotification = 'participant.studyParticipantAssignments[${varIndex}].notificationClinicalStaff[${notificationstatus.index}].studyOrganizationClinicalStaff';
            acCreate(new studyOrganizationClinicalStaffForRoleAutoCompleter(baseNameNotification, '${studyParticipantAssignment.studySite.id}', 'TREATING_PHYSICIAN|NURSE'))
            initializeAutoCompleter(baseNameNotification,
                    '${studyParticipantAssignment.notificationClinicalStaff[notificationstatus.index].studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}',
                    '${studyParticipantAssignment.notificationClinicalStaff[notificationstatus.index].studyOrganizationClinicalStaff.id}');

        </c:forEach>
        </c:if>
        </c:forEach>

        })

        function addNotificationClinicalStaff(index) {
            $('notificationtable_' + index).show();
            var request = new Ajax.Request("<c:url value="/pages/participant/addNotificationClinicalStaff"/>", {
                onComplete:addNotificationClinicalStaffDiv,
                parameters:<tags:ajaxstandardparams/>+"&index=" + index + "&id=" + gup('id'),
                method:'get'
            })
        }

        function addNotificationClinicalStaffDiv(transport) {
            var response = transport.responseText;
            new Insertion.Before("hiddenDiv", response);
        }

        function deleteNotification(index, notificationIndex) {
            var request = new Ajax.Request("<c:url value="/pages/participant/addNotificationClinicalStaff"/>", {
                parameters:<tags:ajaxstandardparams/>+"&index=" + index + "&notificationIndex=" + notificationIndex + "&action=delete&id=${param['id']}",
                onComplete:function(transport) {
                    $('row-' + index + '-' + notificationIndex).remove();
                } ,
                method:'get'
            });
        }
        function gup(name) {
            name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
            var regexS = "[\\?&]" + name + "=([^&#]*)";
            var regex = new RegExp(regexS);
            var results = regex.exec(window.location.href);
            if (results == null)
                return "";
            else
                return results[1];
        }
    </script>
    <style type="text/css">
        div.row div.label {
            width: 15em;
        }

        div.row div.value {
            margin-left: 4.5em;
        }
        *{
            zoom:0;
        }

    </style>
</head>
<body>
<tags:tabForm tab="${tab}" flow="${flow}" notDisplayInBox="true">
<jsp:attribute name="singleFields">

<div class="row">
    <div class="label" style="width:4em"><spring:message code="participant.label.study"/>:</div>
    <div class="value">${command.selectedStudyParticipantAssignment.studySite.study.displayName}</div>
</div>
<div class="row">
    <div class="label" style="width:4em"><spring:message code="participant.label.site"/>:</div>
    <div class="value">${command.selectedStudyParticipantAssignment.studySite.displayName}</div>
</div>

    <chrome:box title="participant.label.researchstaff">
        <chrome:division title="participant.label.sitepi"/>
        <div align="left" style="margin-left: 50px">
            <table class="tablecontent" width="40%">
                <tr>
                    <th class="tableHeader" width="70%">
                        <tags:message code="participant.label.name"/>
                    </th class="tableHeader">
                    <th width=30%>
                        <tags:message code="participant.label.notification"/>
                    </th>
                </tr>
                <c:forEach items="${command.selectedStudyParticipantAssignment.sitePIs}" var="sitePI"
                           varStatus="pistatus">
                    <tr>
                        <td>
                                ${sitePI.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}
                        </td>
                        <td>
                            <tags:renderSelect
                                    propertyName="participant.studyParticipantAssignments[${varIndex}].sitePIs[${pistatus.index}].notify"
                                    required="true" options="${notifyOptions}" doNotshowLabel="true"/>

                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <br>
        <chrome:division title="participant.label.sitecra"/>
        <div align="left" style="margin-left: 50px">
            <table class="tablecontent" width="40%">
                <tr>
                    <th class="tableHeader" width="70%">
                        <tags:message code="participant.label.name"/>
                    </th class="tableHeader">
                    <th width=30%>
                        <tags:message code="participant.label.notification"/>
                    </th>
                </tr>
                <c:forEach items="${command.selectedStudyParticipantAssignment.siteCRAs}" var="siteCRA"
                           varStatus="crastatus">
                    <tr>
                        <td>
                                ${siteCRA.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}
                        </td>
                        <td>
                            <tags:renderSelect
                                    propertyName="participant.studyParticipantAssignments[${varIndex}].siteCRAs[${crastatus.index}].notify"
                                    required="true" options="${notifyOptions}" doNotshowLabel="true"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <br>
    </chrome:box>
    <chrome:box title="participant.primaryclinicalstaff">
        <table>
            <tr>
                <td>
                    <tags:renderAutocompleter
                            propertyName="participant.studyParticipantAssignments[${varIndex}].treatingPhysician.studyOrganizationClinicalStaff"
                            displayName="participant.label.clinical.staff.treatingphysician" noForm="true"
                            required="true"
                            propertyValue="${studyParticipantAssignment.treatingPhysician ne null ? studyParticipantAssignment.treatingPhysician.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName:''}"
                            size="70"/>
                </td>
                <td>
                    <tags:renderSelect
                            propertyName="participant.studyParticipantAssignments[${varIndex}].treatingPhysician.notify"
                            displayName="participant.label.notification"
                            required="true" options="${notifyOptions}"/>
                </td>
            </tr>
            <tr>
                <td>
                    <tags:renderAutocompleter
                            propertyName="participant.studyParticipantAssignments[${varIndex}].researchNurse.studyOrganizationClinicalStaff"
                            displayName="participant.label.clinical.staff.researchnurse" noForm="true"
                            required="true"
                            propertyValue="${studyParticipantAssignment.researchNurse ne null ? studyParticipantAssignment.researchNurse.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName:''}"
                            size="70"/>
                </td>
                <td>
                    <tags:renderSelect
                            propertyName="participant.studyParticipantAssignments[${varIndex}].researchNurse.notify"
                            displayName="participant.label.notification"
                            required="true" options="${notifyOptions}"/>
                </td>
            </tr>
        </table>
        <br>
    </chrome:box>
    <chrome:box title="participant.label.otherstaff">
        <div align="left" style="margin-left: 50px">
            <table width="90%" class="tablecontent"
                   id="notificationtable_${varIndex}">
                <tr id="ss-table-head" class="amendment-table-head">
                    <th width="45%" class="tableHeader">
                        <spring:message code='participant.label.name' text=''/>
                    </th>
                    <th width="15%" class="tableHeader">
                        <spring:message code='participant.label.notification' text=''/>
                    </th>
                    <th width="5%" class="tableHeader">&nbsp;</th>
                </tr>
                <c:forEach items="${command.selectedStudyParticipantAssignment.notificationClinicalStaff}"
                           var="clinicalStaff"
                           varStatus="notificationstatus">
                    <tags:notificationClinicalStaff index="${varIndex}"
                                                    notificationindex="${notificationstatus.index}"
                                                    clinicalStaff="${clinicalStaff}"
                                                    studySiteId="${command.selectedStudyParticipantAssignment.studySite.id}"
                                                    notify="${clinicalStaff.notify}"
                                                    notifyOptions="${notifyOptions}"
                                                    role="${clinicalStaff.studyOrganizationClinicalStaff.role}"></tags:notificationClinicalStaff>
                </c:forEach>
                <tr id="hiddenDiv" align="center"></tr>
            </table>
            <tags:button value="Add" color="blue" type="button" size="small"
                         onclick="javascript:addNotificationClinicalStaff('${varIndex}');return false;" icon="add"/>
        </div>
        <br>
    </chrome:box>
    </jsp:attribute>
</tags:tabForm>
</body>
</html>