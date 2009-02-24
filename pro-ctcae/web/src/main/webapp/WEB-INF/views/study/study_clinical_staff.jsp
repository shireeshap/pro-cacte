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

            updateOrganizationClinicalStaffAutoCompleters();
        })

        function updateOrganizationClinicalStaffAutoCompleters() {
            clearAutoCompleter('leadCRA.organizationClinicalStaff');
            clearAutoCompleter('principalInvestigator.organizationClinicalStaff');
            acCreate(new organizationClinicalStaffAutoComplter('leadCRA.organizationClinicalStaff', $('leadCRA.study').value))
            acCreate(new organizationClinicalStaffAutoComplter('principalInvestigator.organizationClinicalStaff', $('principalInvestigator.study').value))

        }


    </script>


</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}">

    
    <jsp:attribute name="repeatingFields">
       <chrome:division title="study.label.clinical.staff.odc">

           <div class="row">
               <div class="label"><tags:message code="study.label.site"/></div>
               <div class="value">${command.study.studyCoordinatingCenter.organization.displayName} </div>
           </div>


           <tags:renderAutocompleter propertyName="overallDataCoordinator.organizationClinicalStaff"
                                     displayName="study.label.clinical.staff" noForm="true" required="true"
                                     propertyValue="${overallDataCoordinator.organizationClinicalStaff ne null? overallDataCoordinator.organizationClinicalStaff.clinicalStaff.displayName:''}"/>
       </chrome:division>

        <chrome:division title="study.label.clinical.staff.lead.cra">

            <tags:renderSelect options="${studySitesAndCoordinatingCenter}" noForm="true"
                               onchange="updateOrganizationClinicalStaffAutoCompleters()" required="false"
                               displayName="study.label.site"/>


            <tags:renderAutocompleter propertyName="leadCRA.organizationClinicalStaff"
                                      displayName="study.label.clinical.staff" noForm="true" required="true"
                                      propertyValue="${leadCRA.organizationClinicalStaff ne null? leadCRA.organizationClinicalStaff.clinicalStaff.displayName:''}"/>


        </chrome:division>
        <chrome:division title="study.label.clinical.staff.pi">

            <div class="row">
                <div class="label">
                    <tags:renderLabel displayName="study.label.site"
                                      propertyName="principalInvestigator.study"
                                      required="true"/>
                </div>
                <div class="value">
                    <form:select path="principalInvestigator.study"
                                 items="${studySitesAndCoordinatingCenter}" title="Site"
                                 itemLabel="desc" itemValue="code"
                                 onchange="updateOrganizationClinicalStaffAutoCompleters()"/>
                </div>
            </div>


            <tags:renderAutocompleter propertyName="principalInvestigator.organizationClinicalStaff"
                                      displayName="study.label.clinical.staff" noForm="true" required="true"
                                      propertyValue="${principalInvestigator.organizationClinicalStaff ne null? principalInvestigator.organizationClinicalStaff.clinicalStaff.displayName:''}"/>


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