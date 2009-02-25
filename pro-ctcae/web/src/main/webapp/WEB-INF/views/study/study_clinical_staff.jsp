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
            acCreate(new organizationClinicalStaffAutoComplter('study.overallDataCoordinator.organizationClinicalStaff', '${command.study.studyCoordinatingCenter.id}'))
            var leadCRAAutocompleter = acCreate(new organizationClinicalStaffAutoComplter('study.leadCRA.organizationClinicalStaff', $('studySiteForLeadCRA').value))

            updateOrganizationClinicalStaffAutoCompleters(leadCRAAutocompleter);
        })

        function updateOrganizationClinicalStaffAutoCompleters(leadCRAAutocompleter) {

            Autocompleter.keys().each(function(element) {
                alert(element.id)
            })
            AE.resetAutocompleter('study.leadCRA.organizationClinicalStaff')
            AE.resetAutocompleter('study.principalInvestigator.organizationClinicalStaff')
            // clearAutoCompleter('study.leadCRA.organizationClinicalStaff');
            // clearAutoCompleter();
            leadCRAAutocompleter.studyOrganizationId = $('studySiteForLeadCRA').value
            //acCreate(new organizationClinicalStaffAutoComplter('study.principalInvestigator.organizationClinicalStaff', ))

        }


    </script>


</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}">


    <jsp:attribute name="repeatingFields">
       <chrome:division title="study.label.clinical.staff.odc">

           <div class="row">
               <div class="label"><tags:requiredIndicator/><tags:message code="study.label.site"/></div>
               <div class="value">${command.study.studyCoordinatingCenter.organization.displayName} </div>
           </div>


           <tags:renderAutocompleter propertyName="study.overallDataCoordinator.organizationClinicalStaff"
                                     displayName="study.label.clinical.staff" noForm="true" required="true"
                                     propertyValue="${study.overallDataCoordinator.organizationClinicalStaff ne null? study.overallDataCoordinator.organizationClinicalStaff.clinicalStaff.displayName:''}"/>
       </chrome:division>

        <chrome:division title="study.label.clinical.staff.lead.cra">

            <tags:renderSelect options="${studySitesAndCoordinatingCenter}"
                               onchange="updateOrganizationClinicalStaffAutoCompleters()" required="true"
                               displayName="study.label.site" propertyName="study.leadCRA.studyOrganization"/>


            <tags:renderAutocompleter propertyName="study.leadCRA.organizationClinicalStaff"
                                      displayName="study.label.clinical.staff" noForm="true" required="true"
                                      propertyValue="${study.leadCRA.organizationClinicalStaff ne null? study.leadCRA.organizationClinicalStaff.clinicalStaff.displayName:''}"/>


        </chrome:division>
        <chrome:division title="study.label.clinical.staff.pi">

            <tags:renderSelect options="${studySitesAndCoordinatingCenter}"
                               onchange="updateOrganizationClinicalStaffAutoCompleters()" required="true"
                               displayName="study.label.site"
                               propertyName="study.principalInvestigator.studyOrganization"/>

            <tags:renderAutocompleter propertyName="study.principalInvestigator.organizationClinicalStaff"
                                      displayName="study.label.clinical.staff" noForm="true" required="true"
                                      propertyValue="${study.principalInvestigator.organizationClinicalStaff ne null? study.principalInvestigator.organizationClinicalStaff.clinicalStaff.displayName:''}"/>


        </chrome:division>

        <%--<div>--%>

        <%--<c:forEach items="${command.clinicalStaffAssignments}"--%>
        <%--var="clinicalStaffAssignment"--%>
        <%--varStatus="status">--%>

        <%--<c:if test="${clinicalStaffAssignment.domainObjectId eq command.selectedStudySiteId }">--%>
        <%--<study:clinicalStaffAssignment clinicalStaffAssignment="${clinicalStaffAssignment}"--%>
        <%--clinicalStaffAssignmentIndex="${status.index}"/>--%>

        <%--</c:if>--%>
        <%--</c:forEach>--%>

        <%--<div id="hiddenDiv">--%>


        <%--</div>--%>


        <%--</div>--%>




        </jsp:attribute>

</tags:tabForm>

</body>
</html>