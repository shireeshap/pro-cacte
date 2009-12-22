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
        function changeStatus(status, id) {
            var request = new Ajax.Request("<c:url value="/pages/study/changeStatus"/>", {
                parameters:<tags:ajaxstandardparams/>+"&id=" + id + "&status=" + status,
                onComplete:function(transport) {
                    showConfirmationWindow(transport, 650, 280);
                },
                method:'get'
            })

        }
        Event.observe(window, "load", function() {
        <c:forEach  items="${command.studyOrganizationClinicalStaffs}" var="studyOrganizationClinicalStaff" varStatus="status">
            var studyOrganizationClinicalStafBaseName = 'studyOrganizationClinicalStaffs[${status.index}].organizationClinicalStaff'

            if ($(studyOrganizationClinicalStafBaseName + "-input") != null) {

                acCreate(new organizationClinicalStaffAutoComplter(studyOrganizationClinicalStafBaseName,
                        '${studyOrganizationClinicalStaff.studyOrganization.id}'))

                initializeAutoCompleter(studyOrganizationClinicalStafBaseName, '${studyOrganizationClinicalStaff.displayName}',
                        '${studyOrganizationClinicalStaff.organizationClinicalStaff.id}');
            }

        </c:forEach>


            initSearchField()

        })

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

        function changeStudySite() {
            $('_target').name = '_target1';
            $('command').submit();
        }
    </script>

    <style type="text/css">
        div.row div.label {
            width: 20em;
        }
    </style>
</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}" notDisplayInBox="true">


    <jsp:attribute name="singleFields">
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

                <chrome:box title="study.tab.investigator" id="studySiteClinicalStaff">

                    <chrome:division title="study.label.clinical.staff.lead.site_pi">

                        <study:studySiteClinicalStaffTable studySiteId="${studySite.id}" role="SITE_PI"
                                                           roleStatusOptions="${roleStatusOptions}"
                                                           studyCommand="${command}"/>
                    </chrome:division>

                    <chrome:division title="study.label.clinical.staff.lead.site_cra">

                        <study:studySiteClinicalStaffTable studySiteId="${studySite.id}" role="SITE_CRA"
                                                           roleStatusOptions="${roleStatusOptions}"
                                                           studyCommand="${command}"/>
                    </chrome:division>
                </chrome:box>
                <chrome:box title="study.tab.research_staff" id="studySiteClinicalStaff">
                    <chrome:division title="study.label.clinical.staff.lead.treating_physican">

                        <study:studySiteClinicalStaffTable studySiteId="${studySite.id}" role="TREATING_PHYSICIAN"
                                                           roleStatusOptions="${roleStatusOptions}"
                                                           studyCommand="${command}"/>
                    </chrome:division>

                    <chrome:division title="study.label.clinical.staff.lead.nurse">

                        <study:studySiteClinicalStaffTable studySiteId="${studySite.id}" role="NURSE"
                                                           roleStatusOptions="${roleStatusOptions}"
                                                           studyCommand="${command}"/>
                    </chrome:division>
                </chrome:box>

            </c:if>


        </c:forEach>



        </jsp:attribute>

</tags:tabForm>

</body>
</html>