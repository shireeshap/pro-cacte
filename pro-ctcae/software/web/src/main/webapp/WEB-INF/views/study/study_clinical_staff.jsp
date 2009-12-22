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


            var odcAutoComplterBaseName = 'overallDataCoordinator.organizationClinicalStaff';
            acCreate(new organizationClinicalStaffAutoComplter(odcAutoComplterBaseName, '${command.study.dataCoordinatingCenter.id}'))

            var leadCRAAutoComplterBaseName = 'leadCRA.organizationClinicalStaff';
            var leadCRAAutocompleter = acCreate(new organizationClinicalStaffAutoComplter(leadCRAAutoComplterBaseName, '${command.study.leadStudySite.id}'))


            var piAutoCompleterBaseName = 'principalInvestigator.organizationClinicalStaff';
            acCreate(new organizationClinicalStaffAutoComplter(piAutoCompleterBaseName, '${command.study.leadStudySite.id}'))

            initializeAutoCompleter(odcAutoComplterBaseName, '${command.overallDataCoordinator.displayName}',
                    '${command.overallDataCoordinator.organizationClinicalStaff.id}')


            initializeAutoCompleter(piAutoCompleterBaseName, '${command.principalInvestigator.displayName}',
                    '${command.principalInvestigator.organizationClinicalStaff.id}')

            initializeAutoCompleter(leadCRAAutoComplterBaseName, '${command.leadCRA.displayName}',
                    '${command.leadCRA.organizationClinicalStaff.id}');

            initSearchField()

        })


    </script>


</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}">


    <jsp:attribute name="repeatingFields">
       <chrome:division title="study.label.clinical.staff.odc">

           <div class="row">
               <div class="label"><tags:requiredIndicator/><tags:message code="study.label.organization"/></div>
               <div class="value">${command.study.dataCoordinatingCenter.organization.displayName} </div>
           </div>


           <tags:renderAutocompleter propertyName="overallDataCoordinator.organizationClinicalStaff"
                                     displayName="study.label.clinical.staff" noForm="true" required="true"
                   />
       </chrome:division>

        <chrome:division title="study.label.clinical.staff.lead.cra">

            <div class="row">
                <div class="label"><tags:requiredIndicator/><tags:message code="study.label.organization"/></div>
                <div class="value">${command.study.leadStudySite.organization.displayName} </div>
            </div>


            <tags:renderAutocompleter propertyName="leadCRA.organizationClinicalStaff"
                                      displayName="study.label.clinical.staff" noForm="true" required="true"
                    />


        </chrome:division>
        <chrome:division title="study.label.clinical.staff.pi">

            <div class="row">
                <div class="label"><tags:requiredIndicator/><tags:message code="study.label.organization"/></div>
                <div class="value">${command.study.leadStudySite.organization.displayName} </div>
            </div>


            <tags:renderAutocompleter propertyName="principalInvestigator.organizationClinicalStaff"
                                      displayName="study.label.clinical.staff" noForm="true" required="true"
                    />


        </chrome:division>

        </jsp:attribute>

</tags:tabForm>

</body>
</html>