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
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>


<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
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
    <script type="text/javascript">
        function goTab(tabnumber) {

            $('_target').name = "_target" + tabnumber;
            $('command').submit();
        }
    </script>

</head>
<body>
<c:if test="${command.edit}">
    <c:set var="linkDetails" value="javascript:goTab('1');"/>
    <c:set var="linkStaff" value="javascript:goTab('2');"/>
</c:if>
<c:set var="readOnly" value="true"/>
<proctcae:urlAuthorize url="/pages/participant/trueedit">
    <c:set var="readOnly" value="false"/>
</proctcae:urlAuthorize>
<proctcae:urlAuthorize url="/pages/participant/create">
    <c:set var="readOnly" value="false"/>
</proctcae:urlAuthorize>
<tags:tabForm tab="${tab}" flow="${flow}" willSave="false" doNotShowSave="${command.odc || readOnly}" showFinish="true"
              showCreate="true" createLink="/pages/participant/create" createText="Save & add new participant">
<jsp:attribute name="singleFields">

<chrome:division title="participant.label.site">
    <div style="font-weight:bold;margin-top:25px;margin-left:70px;margin-bottom:10px;">${command.siteName}</div>

</chrome:division>
<chrome:division title="participant.label.demographic_information" linkontitle="${linkDetails}"
                 linkurl="/pages/participant/create">
   	<input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}" />
	
    <table border="0" style="width:100%">
        <tr>
            <td>
                <div class="row">
                    <div class="label"><spring:message code='participant.label.first_name' text=''/>:</div>
                    <div class="value">${command.participant.firstName}</div>
                </div>
                <c:if test="${command.mode eq 'N'}">
                    <div class="row">
                        <div class="label"><spring:message code='participant.label.middle_name' text=''/>:</div>
                        <div class="value">${command.participant.middleName}</div>
                    </div>
                </c:if>
                <div class="row">
                    <div class="label"><spring:message code='participant.label.last_name' text=''/>:</div>
                    <div class="value">${command.participant.lastName}</div>
                </div>
            </td>
            <td>
                <c:if test="${command.mode eq 'N'}">
                <div class="row">
                    <div class="label"><spring:message code='participant.label.date_of_birth' text=''/>:</div>
                    <div class="value"><tags:formatDate value="${command.participant.birthDate}"/></div>
                </div>
                </c:if>
                <div class="row">
                    <div class="label"><spring:message code='participant.label.gender' text=''/>:</div>
                    <div class="value">${command.participant.gender}</div>
                </div>
                <c:if test="${command.mode eq 'N'}">
                <div class="row">
                    <div class="label"><spring:message code='participant.label.participant_identifier'
                                                       text=''/>:
                    </div>
                    <div class="value">${command.participant.assignedIdentifier}</div>
                </div>
                </c:if>
            </td>
        </tr>
    </table>
</chrome:division>
<c:if test="${command.mode eq 'N'}">
<chrome:division title="participant.label.contact_information" linkontitle="${linkDetails}"
                 linkurl="/pages/participant/create">

    <table border="0" style="width:100%">
        <tr>
            <td width="50%">
                <div class="row">
                    <div class="label"><spring:message code='participant.label.email_address' text=''/>:</div>
                    <div class="value">${command.participant.emailAddress}</div>
                </div>
            </td>
            <td width="50%">
                <%--<div class="row">--%>
                    <%--<div class="label"><spring:message code='participant.label.phone' text=''/>:</div>--%>
                    <%--<div class="value">${command.participant.phoneNumber}</div>--%>
                <%--</div>--%>
            </td>
        </tr>
    </table>
</chrome:division>
</c:if>
<chrome:division title="participant.label.logininfo" linkontitle="${linkDetails}"
                 linkurl="/pages/participant/create">
    <table cellpadding="0" cellspacing="0">
        <tr>
            <td>
                <div class="row">
                    <div class="label"><spring:message code='participant.label.username'/>:</div>
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
        <div class="label" style="width:15em"><spring:message code='participant.label.study' text=''/>:</div>
        <div class="value">${studyParticipantAssignment.studySite.study.displayName}</div>
    </div>
    <div class="row">
        <div class="label" style="width:15em"><spring:message code='participant.study.identifier' text=''/>:</div>
        <div class="value">${studyParticipantAssignment.studyParticipantIdentifier}</div>
    </div>
    <c:if test="${studyParticipantAssignment.arm.title ne 'Default Arm'}">
        <div class="row">
            <div class="label" style="width:15em"><spring:message code='study.label.arm' text=''/>:</div>
            <div class="value">${studyParticipantAssignment.arm.title}</div>
        </div>
    </c:if>
    <c:forEach items="${studyParticipantAssignment.studyParticipantCrfsForArm}" var="spacrf" varStatus="spacrfstatus">
        <div class="row">
            <div class="label" style="width:15em"><spring:message code="form.tab.form"/>:</div>
            <div class="value">${spacrf.crf.title} (<tags:formatDate value="${spacrf.startDate}"/>)</div>
        </div>
    </c:forEach>

    <chrome:division title="participant.label.sitepi"/><br />
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
    <chrome:division title="participant.label.sitecra"/><br />
    <div align="left" style="margin-left: 100px">
        <table class="tablecontent" width="50%">
            <tr>
                <th class="tableHeader" width="70%">
                    <tags:message code="participant.label.name"/>
                </th>
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
    <chrome:division title="participant.label.clinical.staff.treatingphysician" linkontitle="${linkStaff}" linkurl="/pages/participant/create"/><br />
    <div align="left" style="margin-left: 100px">
	    <c:if test="${fn:length(studyParticipantAssignment.treatingPhysicians) > 0}">
	        <table class="tablecontent" width="50%">
	            <tr>
	                <th class="tableHeader" width="70%">
	                    <tags:message code="participant.label.clinical.staff.treatingphysician"/>
	                </th>
	                <th>
	                    <tags:message code="participant.label.notification"/>
	                </th>
	            </tr>
	        	<c:forEach items="${studyParticipantAssignment.treatingPhysicians}" var="tp" varStatus="tpStatus">
		        	<c:if test="${!(tp.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName eq null)}">
		        	
			            <tr>
			                <td>
			                    ${tp.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName eq null ? 'Not Assigned': tp.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}</div>
			                </td>
			                <td>
			                    ${tp.notify?'Yes':'No'}
			                </td>
			            </tr>
		            </c:if>
	            </c:forEach>
	        </table>
	    </c:if>
    </div>
    <chrome:division title="participant.label.clinical.staff.researchnurse" linkontitle="${linkStaff}" linkurl="/pages/participant/create"/><br />
    <div align="left" style="margin-left: 100px">
    	<c:if test="${fn:length(studyParticipantAssignment.researchNurses) > 0}">
	        <table class="tablecontent" width="50%">
	            <tr>
	                <th class="tableHeader" width="70%">
	                    <tags:message code="participant.label.clinical.staff.researchnurse"/>
	                </th>
	                <th>
	                    <tags:message code="participant.label.notification"/>
	                </th>
	            </tr>        
	            <c:forEach items="${studyParticipantAssignment.researchNurses}" var="rn" varStatus="rnStatus">
	            <c:if test="${!(rn.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName eq null)}">
		            <tr>
		                <td>
		                    ${rn.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName eq null ? 'Not Assigned' :rn.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}
		                </td>
		                <td>
		                    ${rn.notify?'Yes':'No'}
		                </td>
		            </tr>
	            </c:if>
	            </c:forEach>
	        </table>
        </c:if>
    </div>
    <br/>
</c:forEach>
    </jsp:attribute>
</tags:tabForm>
</body>
</html>
