<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="study" tagdir="/WEB-INF/tags/study" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
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
        function changeStatus(status, id, tabNumber) {
            var request = new Ajax.Request("<c:url value="/pages/study/changeStatus"/>", {
                parameters:<tags:ajaxstandardparams/>+"&id=" + id + "&status=" + status + "&tabNumber=" +tabNumber,
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
	                var responseStr = response.split('<p id="splitter"/>');
	                
	                jQuery('#hiddenDivForStudySite_' + studySiteId + '_Role_' + role).before(responseStr[1]);
	                new Insertion.Before("hiddenDiv", responseStr[0]);
                    
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
            $('_target').name = null;
            $('changingStudySite').value = true;
            $('command').submit();
        }
    </script>

    <style type="text/css">
        div.row div.label {
            width: 10em;
        }

        * {
            zoom: 1;
        }
    </style>
</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}" notDisplayInBox="true">
    <jsp:attribute name="singleFields">

        <chrome:box>
             <div class="row">
                 <div class="label"><spring:message code="form.label.study"/>: &nbsp;</div>
                 <div class="value">${command.study.shortTitle}</div>
             </div>
             <div class="row">
                 <div class="label"><spring:message code="study.label.assigned_identifier"/>: &nbsp;</div>
                 <div class="value">${command.study.assignedIdentifier} </div>
             </div>

         	<input type="hidden" name="changingStudySite" id="changingStudySite" value="false"/>
            <tags:renderSelectForDomainObject displayName="study.label.site" options="${studySites}"
                                              propertyName="selectedStudySite" required="false"
                                              onchange="changeStudySite()" itemLabel="organization.displayName"/>
         </chrome:box>
         
        <c:forEach items="${studySites}" var="studySite">

            <c:if test="${studySite.id eq command.selectedStudySite.id}">
                <proctcae:urlAuthorize url="/pages/admin/clinicalStaff/assignStudySiteResearchStaff" objectId="${command.studyInstanceSpecificPrivilege}">
                    <chrome:box title="study.tab.investigator" id="studySiteClinicalStaff">
                    	<tags:instructions code="study.study_research_staff.top"/>
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

                <proctcae:urlAuthorize url="/pages/admin/clinicalStaff/assignStudySiteClinicalStaff" objectId="${command.studyInstanceSpecificPrivilege}">
                    <chrome:box title="study.tab.research_staff" id="studySiteClinicalStaff">
                        <tags:instructions code="study.study_clinical_staff.top"/>
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
		<div id="hiddenDiv"></div>
        </jsp:attribute>
</tags:tabForm>

</body>
</html>