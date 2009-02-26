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
        <c:forEach  items="${command.studyOrganizationClinicalStaffs}" var="studyOrganizationClinicalStaff" varStatus="status">
            var studyOrganizationClinicalStafBaseName = 'studyOrganizationClinicalStaffs[${status.index}].organizationClinicalStaff'

            if ($(studyOrganizationClinicalStafBaseName + "-input") != null) {

                initializeAutoCompleter(studyOrganizationClinicalStafBaseName, '${studyOrganizationClinicalStaff.displayName}',
                        '${studyOrganizationClinicalStaff.organizationClinicalStaff.id}');
            }

        </c:forEach>


            initSearchField()

        })

        function addClinicalStaff(studySiteId, role) {
            var request = new Ajax.Request("<c:url value="/pages//study/addStudyComponent"/>", {
                onComplete:function(transport) {
                    var response = transport.responseText;
                    new Insertion.Before("hiddenDivForStudySite_" + studySiteId + "_Role_" + role, response);

                },
                parameters:"subview=subview&componentTyep=studyOrganizationClinicalStaff&studySiteId=" + studySiteId + "&role=" + role,
                method:'get'
            })

        }


        function selectStudySite(selectedStudySiteId) {
            $('_target').name = '_target3';
            $('selectedStudySiteId').value = selectedStudySiteId;
            $('command').submit();
        }


    </script>


</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}" notDisplayInBox="true">


    <jsp:attribute name="singleFields">


        <form:hidden path="selectedStudySiteId" id="selectedStudySiteId"/>

        <chrome:box title="study.label.sites" id="studySites" cssClass="paired" style="width:30%">
            <ul>

                <c:forEach items="${studySites}" var="studySite">
                    <li><a href="javascript:selectStudySite(${studySite.id})">${studySite.organization.displayName}</a>
                    </li>
                </c:forEach></ul>
        </chrome:box>

        <chrome:box title="study.tab.study_site_clinical_staff" id="studySiteClinicalStaff"
                    cssClass="paired" style="width:60%">

            <c:forEach items="${studySites}" var="studySite">

                <c:if test="${studySite.id eq command.selectedStudySiteId}">


                    <div class="instructions">

                        <div class="summarylabel"><tags:message code="study.label.site"/></div>
                        <div class="summaryvalue">${studySite.organization.displayName}</div>
                    </div>


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

                    <chrome:division title="study.label.clinical.staff.lead.treating_physican">

                        <study:studySiteClinicalStaffTable studySiteId="${studySite.id}" role="TREATING_PHYSICIAN"
                                                           roleStatusOptions="${roleStatusOptions}"
                                                           studyCommand="${command}"/>
                    </chrome:division>

                    <chrome:division title="study.label.clinical.staff.lead.research_nurse">

                        <study:studySiteClinicalStaffTable studySiteId="${studySite.id}" role="RESEARCH_NURSE"
                                                           roleStatusOptions="${roleStatusOptions}"
                                                           studyCommand="${command}"/>
                    </chrome:division>
                </c:if>


            </c:forEach>

        </chrome:box>


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