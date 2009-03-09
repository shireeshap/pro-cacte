<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <style type="text/css">
        .label {
            width: 12em;
            padding: 1px;
            margin-right: 0.5em;
        }

        div.row div.value {
            white-space: normal;
        }

        #studyDetails td.label {
            font-weight: bold;
            float: left;
            margin-left: 0.5em;
            margin-right: 0.5em;
            width: 12em;
            padding: 1px;
        }
    </style>

</head>
<body>
<chrome:flashMessage flashMessage="participant.flash.save"></chrome:flashMessage>

<chrome:box title="participant.label.confirmation">

    <chrome:division title="participant.label.site"/>
    <b>&nbsp;&nbsp;&nbsp;&nbsp;${command.siteName}</b>
    <br>
    <br>
    <chrome:division title="participant.label.demographic_information"/>

    <table border="0" style="width:100%">
        <tr>
            <td>
                <div class="row">
                    <div class="label"><spring:message code='participant.label.first_name' text=''/></div>
                    <div class="value">${command.participant.firstName}</div>
                </div>
                <div class="row">
                    <div class="label"><spring:message code='participant.label.middle_name' text=''/></div>
                    <div class="value">${command.participant.middleName}</div>
                </div>
                <div class="row">
                    <div class="label"><spring:message code='participant.label.last_name' text=''/></div>
                    <div class="value">${command.participant.lastName}</div>
                </div>
            </td>
            <td>
                <div class="row">
                    <div class="label"><spring:message code='participant.label.participant_identifier'
                                                       text=''/></div>
                    <div class="value">${command.participant.assignedIdentifier}</div>
                </div>
                <div class="row">
                    <div class="label"><spring:message code='participant.label.date_of_birth' text=''/></div>
                    <div class="value"><tags:formatDate value="${command.participant.birthDate}"/></div>
                </div>
                <div class="row">
                    <div class="label"><spring:message code='participant.label.gender' text=''/></div>
                    <div class="value">${command.participant.gender}</div>
                </div>
                <div class="row">
                    <div class="label">&nbsp;</div>
                    <div class="value">&nbsp;</div>
                </div>
            </td>
        </tr>
    </table>
</chrome:box>
<c:if test="${not empty command.participant.studyParticipantAssignments}">
    <c:forEach items="${command.participant.studyParticipantAssignments}" var="studyParticipantAssignment">
        <chrome:box title="${studyParticipantAssignment.studySite.study.shortTitle}" message="false">
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
                    <c:forEach items="${studyParticipantAssignment.sitePIs}" var="sitePI" varStatus="pistatus">
                        <tr>
                            <td>
                                    ${sitePI.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}
                            </td>
                            <td>
                                    ${sitePI.notify?'Yes':'No'}

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
                    <c:forEach items="${studyParticipantAssignment.siteCRAs}" var="siteCRA" varStatus="crastatus">
                        <tr>
                            <td>
                                    ${siteCRA.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}
                            </td>
                            <td>
                                    ${siteCRA.notify?'Yes':'No'}
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <br>
                <table width="50%">
                    <tr>
                        <td width="70%">
                            <b><spring:message
                                    code="participant.label.clinical.staff.treatingphysician"/>:</b> ${studyParticipantAssignment.treatingPhysician.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}
                        </td>
                        <td width="30%">
                            <b><spring:message
                                    code="participant.label.notification"/>:</b> ${studyParticipantAssignment.treatingPhysician.notify?'Yes':'No'}
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <b><spring:message
                                    code="participant.label.clinical.staff.researchnurse"/>:</b> ${studyParticipantAssignment.researchNurse.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}
                        </td>
                        <td>
                            <b><spring:message
                                    code="participant.label.notification"/>:</b> ${studyParticipantAssignment.researchNurse.notify?'Yes':'No'}
                        </td>
                    </tr>
                </table>
            </div>
            <br>

            <div align="left" style="margin-left: 50px">
                <table width="90%" class="tablecontent"
                       id="notificationtable_${status.index}">
                    <tr id="ss-table-head" class="amendment-table-head">
                        <th width="55%" class="tableHeader">
                            <spring:message code='participant.label.name' text=''/>
                        </th>
                        <th width="20%" class="tableHeader">
                            <spring:message code='participant.label.role' text=''/>
                        </th>
                        <th width="20%%" class="tableHeader">
                            <spring:message code='participant.label.notification' text=''/>
                        </th>
                    </tr>
                    <c:forEach items="${studyParticipantAssignment.notificationClinicalStaff}" var="clinicalStaff"
                               varStatus="notificationstatus">
                        <tr>
                            <td>
                                    ${clinicalStaff.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}
                            </td>
                            <td>
                                    ${clinicalStaff.studyOrganizationClinicalStaff.role}
                            </td>
                            <td>
                                    ${clinicalStaff.notify?'Yes':'No'}
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </div>
        </chrome:box>
    </c:forEach>
</c:if>
</body>
</html>
