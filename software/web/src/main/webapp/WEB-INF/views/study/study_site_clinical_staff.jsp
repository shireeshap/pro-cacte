<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="study" tagdir="/WEB-INF/tags/study" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>


<html>
<head>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>
    <tags:dwrJavascriptLink objects="clinicalStaff"/>
    <tags:stylesheetLink name="yui-autocomplete"/>
    <tags:javascriptLink name="yui-autocomplete"/>


    <script type="text/javascript">
        function changeStatus(status, id) {
            var request = new Ajax.Request("<c:url value="/pages/study/changeStatus"/>", {
                parameters:<tags:ajaxstandardparams/>+"&id=" + id + "&status=" + status,
                onComplete:function(transport) {
                    showConfirmationWindow(transport, 580, 200);
                },
                method:'get'
            })

        }

        function addClinicalStaff(studySiteId, role) {
            var request = new Ajax.Request("<c:url value="/pages/study/addStudyComponent"/>", {
                onComplete:function(transport) {
                    var response = transport.responseText;
                    new Insertion.Before("hiddenDivForStudySite_" + studySiteId + "_Role_" + role, response);
                    AE.registerCalendarPopups();
                },
                parameters:<tags:ajaxstandardparams/>+"&componentType=studyOrganizationClinicalStaff&studySiteId=" + studySiteId + "&role=" + role,
                method:'get'
            })
        }

        function deleteSiteRole(studyOrganizationClinicalStaffIndex) {
            var request = new Ajax.Request("<c:url value="/pages/study/addStudyComponent"/>", {
                onComplete:function(transport) {
                    $('row-' + studyOrganizationClinicalStaffIndex).remove();
                },
                parameters:<tags:ajaxstandardparams/>+"&componentType=studyOrganizationClinicalStaff&studyOrganizationClinicalStaffIndex=" + studyOrganizationClinicalStaffIndex + "&action=delete",
                method:'get'
            })
        }

        function clearInput(inputId) {
               $(inputId).clear();
               $(inputId + 'Input').clear();
               $(inputId + 'Input').focus();
               $(inputId + 'Input').blur();
           }

        function changeStudySite() {
            $('_target').name = '_target' + $('_page').value;
            $('changingStudySite').value = true;
            $('command').submit();
        }
    </script>

    <style type="text/css">
        div.row div.label {
            width: 10em;
        }

        * {
            zoom: 0;
        }
    </style>
</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}" notDisplayInBox="true">
    <jsp:attribute name="singleFields">
        <input type="hidden" name="changingStudySite" id="changingStudySite" value="false"/>
            <tags:renderSelectForDomainObject displayName="study.label.site" options="${studySites}"
                                              propertyName="selectedStudySite" required="true"
                                              onchange="changeStudySite()" itemLabel="organization.displayName"/>

        <chrome:box title="study.tab.clinical_staff">
            <div class="row">
                <div class="label"><spring:message code="study.label.clinical.staff.lead.cra"/>: &nbsp;</div>
                <div class="value">${leadCRA.displayName}</div>
            </div>
            <div class="row">
                <div class="label"><spring:message code="study.label.clinical.staff.pi"/>: &nbsp;</div>
                <div class="value">${OverallPI.displayName}</div>
            </div>
        </chrome:box>
        <c:forEach items="${studySites}" var="studySite">

            <c:if test="${studySite.id eq command.selectedStudySite.id}">
            	<proctcae:urlAuthorize url="/pages/admin/clinicalStaff/assignStudySiteResearchStaff">
	                <chrome:box title="study.tab.investigator" id="studySiteClinicalStaff">
	                    <chrome:division title="study.label.clinical.staff.lead.site_pi">
	                        <study:studySiteClinicalStaffTable studySiteId="${studySite.id}" role="SITE_PI"
	                                                           roleStatusOptions="${roleStatusOptions}"
	                                                           studyCommand="${command}"/>
	                    </chrome:division>
						<br/>
	                    <chrome:division title="study.label.clinical.staff.lead.site_cra">
	                        <study:studySiteClinicalStaffTable studySiteId="${studySite.id}" role="SITE_CRA"
	                                                           roleStatusOptions="${roleStatusOptions}"
	                                                           studyCommand="${command}"/>
	                    </chrome:division>
	                    <br/>
	                </chrome:box>
				</proctcae:urlAuthorize>
				
                <proctcae:urlAuthorize url="/pages/admin/clinicalStaff/assignStudySiteClinicalStaff">
                    <chrome:box title="study.tab.research_staff" id="studySiteClinicalStaff">
                        <chrome:division title="study.label.clinical.staff.lead.treating_physican">
                            <study:studySiteClinicalStaffTable studySiteId="${studySite.id}" role="TREATING_PHYSICIAN"
                                                               roleStatusOptions="${roleStatusOptions}"
                                                               studyCommand="${command}"/>
                        </chrome:division>
						<br/>
                        <chrome:division title="study.label.clinical.staff.lead.nurse">
                            <study:studySiteClinicalStaffTable studySiteId="${studySite.id}" role="NURSE"
                                                               roleStatusOptions="${roleStatusOptions}"
                                                               studyCommand="${command}"
                                                               notifyOptions="${notifyOptions}"/>
                        </chrome:division>
                        <br/>
                    </chrome:box>
                </proctcae:urlAuthorize>
            </c:if>

        </c:forEach>

        </jsp:attribute>
</tags:tabForm>

</body>
</html>