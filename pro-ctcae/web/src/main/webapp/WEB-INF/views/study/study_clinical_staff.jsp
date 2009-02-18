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
            acCreate(new siteClinicalStaffAutoComplter('overallDataCoordinator.siteClinicalStaff', '${command.study.studyCoordinatingCenter.id}'))

            updateSiteClinicalStaffAutoCompleters();
        })

        function updateSiteClinicalStaffAutoCompleters() {
            clearAutoCompleter('leadCRA.siteClinicalStaff');
            clearAutoCompleter('principalInvestigator.siteClinicalStaff');
            acCreate(new siteClinicalStaffAutoComplter('leadCRA.siteClinicalStaff', $('leadCRA.studyOrganization').value))
            acCreate(new siteClinicalStaffAutoComplter('principalInvestigator.siteClinicalStaff', $('principalInvestigator.studyOrganization').value))

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


           <tags:renderAutocompleter propertyName="overallDataCoordinator.siteClinicalStaff"
                                     displayName="study.label.clinical.staff" noForm="true" required="true"
                                     propertyValue="${overallDataCoordinator.siteClinicalStaff ne null? overallDataCoordinator.siteClinicalStaff.clinicalStaff.displayName:''}"/>
       </chrome:division>

        <chrome:division title="study.label.clinical.staff.lead.cra">


            <div class="row">
                <div class="label">
                    <tags:renderLabel displayName="study.label.site"
                                      propertyName="leadCRA.studyOrganization"
                                      required="true"/>
                </div>
                <div class="value">
                    <form:select path="leadCRA.studyOrganization"
                                 items="${studySitesAndCoordinatingCenter}" title="Site"
                                 itemLabel="desc" itemValue="code" onchange="updateSiteClinicalStaffAutoCompleters()"/>
                </div>
            </div>

            <tags:renderAutocompleter propertyName="leadCRA.siteClinicalStaff"
                                      displayName="study.label.clinical.staff" noForm="true" required="true"
                                      propertyValue="${leadCRA.siteClinicalStaff ne null? leadCRA.siteClinicalStaff.clinicalStaff.displayName:''}"/>


        </chrome:division>
        <chrome:division title="study.label.clinical.staff.pi">

            <div class="row">
                <div class="label">
                    <tags:renderLabel displayName="study.label.site"
                                      propertyName="principalInvestigator.studyOrganization"
                                      required="true"/>
                </div>
                <div class="value">
                    <form:select path="principalInvestigator.studyOrganization"
                                 items="${studySitesAndCoordinatingCenter}" title="Site"
                                 itemLabel="desc" itemValue="code" onchange="updateSiteClinicalStaffAutoCompleters()"/>
                </div>
            </div>


            <tags:renderAutocompleter propertyName="principalInvestigator.siteClinicalStaff"
                                      displayName="study.label.clinical.staff" noForm="true" required="true"
                                      propertyValue="${principalInvestigator.siteClinicalStaff ne null? principalInvestigator.siteClinicalStaff.clinicalStaff.displayName:''}"/>


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