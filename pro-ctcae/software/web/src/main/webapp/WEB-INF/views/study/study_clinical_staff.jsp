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
    <tags:stylesheetLink name="yui-autocomplete"/>
    <tags:javascriptLink name="yui-autocomplete"/>


    <script type="text/javascript">

        function getODCStaff(sQuery) {
            var callbackProxy = function(results) {
                aResults = results;
            };
            var callMetaData = { callback:callbackProxy, async:false};
            clinicalStaff.matchOrganizationClinicalStaffByStudyOrganizationId(unescape(sQuery), ${command.study.dataCoordinatingCenter.id}, callMetaData);
            return aResults;
        }

        function getLeadStaff(sQuery) {
            var callbackProxy = function(results) {
                aResults = results;
            };
            var callMetaData = { callback:callbackProxy, async:false};
            clinicalStaff.matchOrganizationClinicalStaffByStudyOrganizationId(unescape(sQuery), ${command.study.leadStudySite.id}, callMetaData);
            return aResults;
        }

        var managerAutoComp;
        Event.observe(window, 'load', function() {
            new YUIAutoCompleter('overallDataCoordinator.organizationClinicalStaffInput', getODCStaff, handleSelect);
            if('${command.overallDataCoordinator.displayName}'!=''){
            $('overallDataCoordinator.organizationClinicalStaffInput').value = "${command.overallDataCoordinator.displayName}";
            $('overallDataCoordinator.organizationClinicalStaffInput').removeClassName('pending-search');
            }
            new YUIAutoCompleter('leadCRA.organizationClinicalStaffInput', getLeadStaff, handleSelect);
            if('${command.leadCRA.displayName}'!=""){
            $('leadCRA.organizationClinicalStaffInput').value = "${command.leadCRA.displayName}";
            $('leadCRA.organizationClinicalStaffInput').removeClassName('pending-search');
            }
            new YUIAutoCompleter('principalInvestigator.organizationClinicalStaffInput', getLeadStaff, handleSelect);
            if('${command.principalInvestigator.displayName}'!=""){
            $('principalInvestigator.organizationClinicalStaffInput').value = "${command.principalInvestigator.displayName}";
            $('principalInvestigator.organizationClinicalStaffInput').removeClassName('pending-search');
            }
        })
                ;

        function handleSelect(stype, args) {
            var ele = args[0];
            var oData = args[2];
            ele.getInputEl().value = oData.displayName;
            var id = ele.getInputEl().id;
            var hiddenInputId = id.substring(0, id.indexOf('Input'));
            $(hiddenInputId).value = oData.id;

        }

        function clearInput(inputId) {
            $(inputId).clear();
            $(inputId + 'Input').clear();
            $(inputId + 'Input').focus();
            $(inputId + 'Input').blur();
        }

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
           <form:input path="overallDataCoordinator.organizationClinicalStaff"
                       id="overallDataCoordinator.organizationClinicalStaff" cssClass="validate-NOTEMPTY"
                       title="Data coordinator"
                       cssStyle="display:none;"/>
           <div class="row">
           <div class="label"><tags:requiredIndicator/><tags:message code='study.label.clinical.staff'/></div>
               <div class="value">
                   <tags:yuiAutocompleter inputName="overallDataCoordinator.organizationClinicalStaffInput"
                                          value="${command.overallDataCoordinator.displayName}" required="false"
                                          hiddenInputName="overallDataCoordinator.organizationClinicalStaff"/>
               </div>
           </div>

       </chrome:division>

        <chrome:division title="study.label.clinical.staff.lead.cra">

            <div class="row">
                <div class="label"><tags:requiredIndicator/><tags:message code="study.label.organization"/></div>
                <div class="value">${command.study.leadStudySite.organization.displayName} </div>
            </div>

             <form:input path="leadCRA.organizationClinicalStaff"
                       id="leadCRA.organizationClinicalStaff" cssClass="validate-NOTEMPTY"
                       title="Data coordinator"
                       cssStyle="display:none;"/>
           <div class="row">
           <div class="label"><tags:requiredIndicator/><tags:message code='study.label.clinical.staff'/></div>
               <div class="value">
                   <tags:yuiAutocompleter inputName="leadCRA.organizationClinicalStaffInput"
                                          value="${command.leadCRA.displayName}" required="false"
                                          hiddenInputName="leadCRA.organizationClinicalStaff"/>
               </div>
           </div>

        </chrome:division>
        <chrome:division title="study.label.clinical.staff.pi">

            <div class="row">
                <div class="label"><tags:requiredIndicator/><tags:message code="study.label.organization"/></div>
                <div class="value">${command.study.leadStudySite.organization.displayName} </div>
            </div>
            <form:input path="principalInvestigator.organizationClinicalStaff"
                       id="principalInvestigator.organizationClinicalStaff" cssClass="validate-NOTEMPTY"
                       title="Data coordinator"
                       cssStyle="display:none;"/>
            <div class="row">
           <div class="label"><tags:requiredIndicator/><tags:message code='study.label.clinical.staff'/></div>
               <div class="value">
                   <tags:yuiAutocompleter inputName="principalInvestigator.organizationClinicalStaffInput"
                                          value="${command.principalInvestigator.displayName}" required="false"
                                          hiddenInputName="principalInvestigator.organizationClinicalStaff"/>
               </div>
           </div>

        </chrome:division>

        </jsp:attribute>

</tags:tabForm>

</body>
</html>