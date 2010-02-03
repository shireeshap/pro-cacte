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
<tags:tabForm tab="${tab}" flow="${flow}" willSave="false" doNotShowSave="${command.odc}">
<jsp:attribute name="singleFields">

<chrome:division title="participant.label.site">
    <b>${command.siteName}</b>
</chrome:division>
<chrome:division title="participant.label.demographic_information">

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
                    <div class="label"><spring:message code='participant.label.date_of_birth' text=''/></div>
                    <div class="value"><tags:formatDate value="${command.participant.birthDate}"/></div>
                </div>
                <div class="row">
                    <div class="label"><spring:message code='participant.label.gender' text=''/></div>
                    <div class="value">${command.participant.gender}</div>
                </div>
                <div class="row">
                    <div class="label"><spring:message code='participant.label.participant_identifier'
                                                       text=''/></div>
                    <div class="value">${command.participant.assignedIdentifier}</div>
                </div>
            </td>
        </tr>
    </table>
</chrome:division>
<chrome:division title="participant.label.contact_information">

    <table border="0" style="width:100%">
        <tr>
            <td width="50%">
                <div class="row">
                    <div class="label"><spring:message code='participant.label.email_address' text=''/></div>
                    <div class="value">${command.participant.emailAddress}</div>
                </div>
            </td>
            <td width="50%">
                <div class="row">
                    <div class="label"><spring:message code='participant.label.phone' text=''/></div>
                    <div class="value">${command.participant.phoneNumber}</div>
                </div>
            </td>
        </tr>
    </table>
</chrome:division>
<chrome:division title="participant.label.logininfo">
    <table cellpadding="0" cellspacing="0">
        <tr>
            <td>
                <div class="row">
                    <div class="label"><spring:message code='participant.label.username'/></div>
                    <div class="value">${command.participant.user.username}</div>
                </div>
            </td>
            <td>
            </td>
        </tr>
    </table>
</chrome:division>

<chrome:division title="participant.label.assigned_studies"/>
<c:forEach items="${command.participant.studyParticipantAssignments}" var="studyParticipantAssignment">
    <div class="row">
        <div class="label"><spring:message code='participant.label.study' text=''/></div>
        <div class="value">${studyParticipantAssignment.studySite.study.displayName}</div>
    </div>
    <c:if test="${fn:length(studyParticipantAssignment.arm.title)>0}">
        <div class="row">
            <div class="label"><spring:message code='study.label.arm' text=''/></div>
            <div class="value">${studyParticipantAssignment.arm.title}</div>
        </div>
    </c:if>
    <c:forEach items="${studyParticipantAssignment.studyParticipantCrfs}" var="spacrf" varStatus="spacrfstatus">
        <div class="row">
            <div class="label"><spring:message code="form.tab.form"/></div>
            <div class="value">${spacrf.crf.title} (<tags:formatDate value="${spacrf.startDate}"/>)</div>
        </div>
    </c:forEach>

    <chrome:division title="participant.label.sitepi"/>
    <div align="left" style="margin-left: 100px">
        <table class="tablecontent" width="50%">
            <tr>
                <th class="tableHeader" width="70%">
                    <tags:message code="participant.label.name"/>
                </th class="tableHeader">
                <th>
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
    <div align="left" style="margin-left: 100px">
        <table class="tablecontent" width="50%">
            <tr>
                <th class="tableHeader" width="70%">
                    <tags:message code="participant.label.name"/>
                </th class="tableHeader">
                <th>
                    <tags:message code="participant.label.notification"/>
                </th>
            </tr>
            <c:forEach items="${studyParticipantAssignment.siteCRAs}" var="siteCRA"
                       varStatus="crastatus">
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
    </div>
    <br/>
    <chrome:division title="participant.primaryclinicalstaff"/>
    <table>
        <tr>
            <td>
                <div class="row">
                    <div class="label"><spring:message
                            code="participant.label.clinical.staff.treatingphysician"/></div>
                    <div class="value"> ${studyParticipantAssignment.treatingPhysician.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}</div>
                </div>
            </td>
            <td>
                <div class="row">
                    <div class="label"><spring:message code="participant.label.notification"/></div>
                    <div class="value">${studyParticipantAssignment.treatingPhysician.notify?'Yes':'No'}</div>
                </div>
            </td>
        </tr>
        <tr>
            <td>
                <div class="row">
                    <div class="label"><spring:message code="participant.label.clinical.staff.researchnurse"/></div>
                    <div class="value">
                            ${studyParticipantAssignment.researchNurse.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}
                    </div>
                </div>
            </td>
            <td>
                <div class="row">
                    <div class="label"><spring:message code="participant.label.notification"/></div>
                    <div class="value">
                            ${studyParticipantAssignment.researchNurse.notify?'Yes':'No'}</div>
                </div>
            </td>
        </tr>
    </table>
    <chrome:division title="participant.label.otherstaff"/>
    <div align="left" style="margin-left: 100px">
        <table class="tablecontent" width="50%">
            <tr id="ss-table-head" class="amendment-table-head">
                <th class="tableHeader" width="70%">
                    <spring:message code='participant.label.name' text=''/>
                </th>
                <th class="tableHeader">
                    <spring:message code='participant.label.notification' text=''/>
                </th>
            </tr>
            <c:forEach items="${studyParticipantAssignment.notificationClinicalStaff}"
                       var="clinicalStaff"
                       varStatus="notificationstatus">
                <tr>
                    <td>
                            ${clinicalStaff.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}
                    </td>
                    <td>
                            ${clinicalStaff.notify?'Yes':'No'}
                    </td>
                </tr>
            </c:forEach>
        </table>
    </div>
    <br/>
</c:forEach>
    </jsp:attribute>
</tags:tabForm>
</body>
</html>
