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
            acCreate(new organizationClinicalStaffAutoComplter('overallDataCoordinator.organizationClinicalStaff', '${command.study.studyCoordinatingCenter.id}'))
            var leadCRAAutocompleter = acCreate(new organizationClinicalStaffAutoComplter('leadCRA.organizationClinicalStaff', $('leadCRA.studyOrganization').value))
            acCreate(new organizationClinicalStaffAutoComplter('principalInvestigator.organizationClinicalStaff', $('principalInvestigator.studyOrganization').value))

            updateOrganizationClinicalStaffAutoCompleters();
        })

        function updateOrganizationClinicalStaffAutoCompleters() {

            AE.resetAutocompleter('leadCRA.organizationClinicalStaff')
            AE.resetAutocompleter('principalInvestigator.organizationClinicalStaff')
            // clearAutoCompleter('study.leadCRA.organizationClinicalStaff');
            // clearAutoCompleter();
            //leadCRAAutocompleter.studyOrganizationId = $('study.leadCRA.studyOrganization').value

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


           <tags:renderAutocompleter propertyName="overallDataCoordinator.organizationClinicalStaff"
                                     displayName="study.label.clinical.staff" noForm="true" required="true"
                                     />
       </chrome:division>

        <chrome:division title="study.label.clinical.staff.lead.cra">

            <tags:renderSelect options="${studySitesAndCoordinatingCenter}"
                               onchange="updateOrganizationClinicalStaffAutoCompleters()" required="true"
                               displayName="study.label.site" propertyName="leadCRA.studyOrganization"/>


            <tags:renderAutocompleter propertyName="leadCRA.organizationClinicalStaff"
                                      displayName="study.label.clinical.staff" noForm="true" required="true"
                                      />


        </chrome:division>
        <chrome:division title="study.label.clinical.staff.pi">

            <tags:renderSelect options="${studySitesAndCoordinatingCenter}"
                               onchange="updateOrganizationClinicalStaffAutoCompleters()" required="true"
                               displayName="study.label.site"
                               propertyName="principalInvestigator.studyOrganization"/>

            <tags:renderAutocompleter propertyName="principalInvestigator.organizationClinicalStaff"
                                      displayName="study.label.clinical.staff" noForm="true" required="true"
                                      />


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