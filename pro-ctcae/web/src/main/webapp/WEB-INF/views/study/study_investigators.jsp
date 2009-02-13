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


        function addStudySiteClinicalStaffDiv(transport) {
            var response = transport.responseText;
            new Insertion.Before("hiddenDiv", response);
        }


        function addRole(clinicalStaffAssignmentIndex) {
            var request = new Ajax.Request("<c:url value="/pages/study/addStudyComponent"/>", {
                onComplete:function(transport) {
                    var response = transport.responseText;
                    new Insertion.Before("hiddenDivForRole_" + clinicalStaffAssignmentIndex, response);

                },
                parameters:"subview=subview&componentTyep=role&clinicalStaffAssignmentIndex=" + clinicalStaffAssignmentIndex,
                method:'get'
            })
        }

        function addClinicalStaffAssignmentOnStudySite() {
            if ($('clinicalStaffAssignment.clinicalStaff').value == '') {
                alert('please select a clinical staff.')
            } else {
                var request = new Ajax.Request("<c:url value="/pages/study/addStudyComponent"/>", {
                    onComplete:addStudySiteClinicalStaffDiv,
                    parameters:"subview=subview&componentTyep=site&studySiteId=" + $('selectedStudySiteId').value + "&clinicalStaffId=" + $('clinicalStaffAssignment.clinicalStaff').value,
                    method:'get'
                })
            }
        }


        function deleteSiteRole(clinicalStaffAssignmentIndex, clinicalStaffAssignmentRoleIndex) {

            var request = new Ajax.Request("<c:url value="/pages/confirmationCheck"/>", {
                parameters:"confirmationType=deleteClinicalStaffAssignmentRole&subview=subview&clinicalStaffAssignmentIndex="
                        + clinicalStaffAssignmentIndex + "&clinicalStaffAssignmentRoleIndex=" + clinicalStaffAssignmentRoleIndex,
                onComplete:function(transport) {
                    showConfirmationWindow(transport);

                } ,
                method:'get'
            });


        }
        function deleteSiteRoleConfirm(clinicalStaffAssignmentIndex, clinicalStaffAssignmentRoleIndex) {
            closeWindow();
            $('clinicalStaffAssignmentRoleIndexToRemove').value = clinicalStaffAssignmentIndex + '-' + clinicalStaffAssignmentRoleIndex;
            submitInvestigatorsTabPage();
        }

        function deleteInvestigator(clinicalStaffAssignmentIndex) {

            var request = new Ajax.Request("<c:url value="/pages/confirmationCheck"/>", {
                parameters:"confirmationType=deleteClinicalStaffAssignmentForStudy&subview=subview&clinicalStaffAssignmentIndex=" + clinicalStaffAssignmentIndex,
                onComplete:function(transport) {
                    showConfirmationWindow(transport);

                } ,
                method:'get'
            });


        }
        function deleteInvestigatorConfirm(clinicalStaffAssignmentIndex) {
            closeWindow();
            $('clinicalStaffAssignmentIndexToRemove').value = clinicalStaffAssignmentIndex;
            submitInvestigatorsTabPage();

        }
        function submitInvestigatorsTabPage() {
            $('_target').name = "_target" + 2;
            $('command').submit();
        }

        Event.observe(window, "load", function() {


        <c:if test="${not empty command.study.studySites}">

            acCreate(new clinicalStaffAutoComplter('clinicalStaffAssignment.clinicalStaff', '${command.clinicalStaffAssignment.domainObjectId}'))
            initSearchField()

        </c:if>
            initSearchField()


        })


    </script>


</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}">

    <jsp:attribute name="singleFields">

            <p><tags:instructions code="study.study_investigators.top"/></p>

        <form:hidden path="clinicalStaffAssignmentRoleIndexToRemove" id="clinicalStaffAssignmentRoleIndexToRemove"/>
        <form:hidden path="clinicalStaffAssignmentIndexToRemove" id="clinicalStaffAssignmentIndexToRemove"/>

        <%--<tags:renderSelect options="${studySites}" propertyName="selectedStudySiteId" displayName="Site"/>--%>
        <div class="row">
        <div class="label">
            <tags:renderLabel displayName="Site" propertyName="selectedStudySiteId"
                              required="false"/>
        </div>
            <div class="value">
                <form:select path="selectedStudySiteId" items="${studySites}" title="Site"
                             itemLabel="desc" itemValue="code" onchange="submitInvestigatorsTabPage()"/>
            </div>
          </div>



        <c:if test="${not empty command.study.studySites}">
            <tags:renderAutocompleter propertyName="clinicalStaffAssignment.clinicalStaff"
                                      displayName="Clinical  staff" noForm="true" required="false"
                                      propertyValue="${clinicalStaffAssignment.clinicalStaff ne null? clinicalStaffAssignment.clinicalStaff.displayName:''}"/>
        </c:if>

    </jsp:attribute>


    <jsp:attribute name="repeatingFields">


        <div>

            <c:forEach items="${command.clinicalStaffAssignments}"
                       var="clinicalStaffAssignment"
                       varStatus="status">
                <study:clinicalStaffAssignment clinicalStaffAssignment="${clinicalStaffAssignment}"
                                               clinicalStaffAssignmentIndex="${status.index}"/>

                <c:if test="${clinicalStaffAssignment.domainObjectId eq selectedStudySiteId }">
                </c:if>
            </c:forEach>

            <div id="hiddenDiv">


            </div>


        </div>

        <chrome:division title=" ">
            <div class="local-buttons">
                <tags:button type="anchor" onClick="javascript:addClinicalStaffAssignmentOnStudySite()"
                             value="study.button.add_investigator"/>
            </div>
        </chrome:division>


        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
        <br>
    </jsp:attribute>

</tags:tabForm>

</body>
</html>