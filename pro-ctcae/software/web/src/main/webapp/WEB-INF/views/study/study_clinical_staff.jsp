<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="study" tagdir="/WEB-INF/tags/study" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
            showIndicator("overallDataCoordinator.organizationClinicalStaffInput-indicator");
            var callbackProxy = function(results) {
                aResults = results;
            };
            var callMetaData = { callback:callbackProxy, async:false};
            clinicalStaff.matchOrganizationClinicalStaffByStudyOrganizationId(unescape(sQuery), ${command.study.dataCoordinatingCenter.id}, 'NULL', callMetaData);
            hideIndicator("overallDataCoordinator.organizationClinicalStaffInput-indicator");
            return aResults;
        }

        function getLeadStaff1(sQuery) {
            showIndicator("principalInvestigator.organizationClinicalStaffInput-indicator");
            var callbackProxy = function(results) {
                aResults = results;
            };
            var callMetaData = { callback:callbackProxy, async:false};
            clinicalStaff.matchOrganizationClinicalStaffByStudyOrganizationId(unescape(sQuery), ${command.study.leadStudySite.id}, 'NULL', callMetaData);
            hideIndicator("principalInvestigator.organizationClinicalStaffInput-indicator");
            return aResults;
        }

    	function getLeadStaff(sQuery) {
    	    showIndicator("leadCRAs[0].organizationClinicalStaffInput-indicator");
    	    var callbackProxy = function(results) {
    	        aResults = results;
    	    };
    	    var callMetaData = { callback:callbackProxy, async:false};
    	    clinicalStaff.matchOrganizationClinicalStaffByStudyOrganizationId(unescape(sQuery), ${command.study.leadStudySite.id}, 'LEAD_CRA', callMetaData);
    	    hideIndicator("leadCRAs[0].organizationClinicalStaffInput-indicator");
    	    return aResults;
    	}

        var managerAutoComp;
        Event.observe(window, 'load', function() {
            new YUIAutoCompleter('overallDataCoordinator.organizationClinicalStaffInput', getODCStaff, handleSelect);
            if ('${command.overallDataCoordinator.displayName}' != '') {
                $('overallDataCoordinator.organizationClinicalStaffInput').value = "${command.overallDataCoordinator.displayName}";
                $('overallDataCoordinator.organizationClinicalStaffInput').removeClassName('pending-search');
            }
            new YUIAutoCompleter('principalInvestigator.organizationClinicalStaffInput', getLeadStaff1, handleSelect);
            if ('${command.principalInvestigator.displayName}' != "") {
                $('principalInvestigator.organizationClinicalStaffInput').value = "${command.principalInvestigator.displayName}";
                $('principalInvestigator.organizationClinicalStaffInput').removeClassName('pending-search');
            }
            <c:if test="${fn:length(command.study.leadCRAs) == 0}">
	            new YUIAutoCompleter('leadCRAs[0].organizationClinicalStaffInput', getLeadStaff, handleSelect);
	            if ('${command.leadCRAs[0].organizationClinicalStaff.displayName}' != "") {
	                $('leadCRAs[0].organizationClinicalStaffInput').value = "${command.leadCRAs[0].organizationClinicalStaff.displayName}";
	                $('leadCRAs[0].organizationClinicalStaffInput').removeClassName('pending-search');
	            }
            </c:if>
        });

        function handleSelect(stype, args) {
            var ele = args[0];
            var oData = args[2];
            if (oData == null) {
                ele.getInputEl().value = "(Begin typing here)";
                ele.getInputEl().addClassName('pending-search');
            } else {
                ele.getInputEl().value = oData.displayName;
                var id = ele.getInputEl().id;
                ele.getInputEl().removeClassName('pending-search');
                var hiddenInputId = id.substring(0, id.indexOf('Input'));
                $(hiddenInputId).value = oData.id;
            }
        }

        function clearInput(inputId) {
            $(inputId).clear();
            $(inputId + 'Input').clear();
            $(inputId + 'Input').focus();
            $(inputId + 'Input').blur();
        }


        //Multiple Lead Cras 
        function addLeadCraDiv(transport) {
            $('leadCraTable').show()
			var response = transport.responseText;
	        var responseStr = response.split('<p id="splitter"/>');
	        jQuery('#leadCraTable tr:last').before(responseStr[1]);
	        new Insertion.Before("hiddenDiv", responseStr[0]);
        }
        
        function addLeadCra() {
            var request = new Ajax.Request("<c:url value="/pages/study/addStudyOrganizationalClinicalStaff"/>", {
                onComplete:addLeadCraDiv,
                parameters:<tags:ajaxstandardparams/>,
                method:'get'
            })
        }
        
        function changeStatus(status, id, tabNumber) {
            var request = new Ajax.Request("<c:url value="/pages/study/changeStatus"/>", {
                parameters:<tags:ajaxstandardparams/>+"&id=" + id + "&status=" + status + "&tabNumber=" +tabNumber,
                onComplete:function(transport) {
                    showConfirmationWindow(transport, 580, 200);
                },
                method:'get'
            })

        }
        
        function deleteLeadCra(index) {
            var request = new Ajax.Request("<c:url value="/pages/study/addStudyOrganizationalClinicalStaff"/>", {
                onComplete:function(transport) {
                    $('row-' + index).remove();
                },
                parameters:<tags:ajaxstandardparams/>+"&action=delete&craIndexToRemove=" + index,
                method:'get'
            })
        }
        // Multiple Lead Cras 

    </script>

</head>
<body>

<tags:tabForm tab="${tab}" flow="${flow}" notDisplayInBox="true">

    <jsp:attribute name="repeatingFields">
        
        <chrome:box>
            <div class="row">
                <div class="label"><spring:message code="form.label.study"/>:</div>
                <div class="value">${command.study.shortTitle} </div>
            </div>
            <div class="row">
                <div class="label"><spring:message code="study.label.assigned_identifier"/>:</div>
                <div class="value">${command.study.assignedIdentifier} </div>
            </div>
       </chrome:box>
       
       <chrome:box title="Overall Study Staff">
           <tags:instructions code="study.study_clinical_staff.top"/><br/>
       		
	       <chrome:division title="study.label.clinical.staff.odc">
	           <div class="row">
	               <div class="label"><tags:requiredIndicator/><tags:message code="study.label.organization"/></div>
	               <div class="value">${command.study.dataCoordinatingCenter.organization.displayName} </div>
	           </div>
	           <form:input path="overallDataCoordinator.organizationClinicalStaff"
	                       id="overallDataCoordinator.organizationClinicalStaff" cssClass="validate-NOTEMPTY"
	                       title="Overall Data Coordinator "
	                       cssStyle="display:none;"/>
	           <div class="row">
	               <div class="label"><tags:message code='study.label.clinical.staff'/></div>
	               <div class="value">
	                   <tags:studyStaffAutocompleter inputName="overallDataCoordinator.organizationClinicalStaffInput"
	                                          value="${command.overallDataCoordinator.displayName}" required="false"
	                                          hiddenInputName="overallDataCoordinator.organizationClinicalStaff"
	                                          staff="${command.overallDataCoordinator}"/>
	               </div>
	           </div>
	       </chrome:division>
	
	        <chrome:division title="study.label.clinical.staff.lead.cra">
	        	<div class="row">
	                <div class="label"><tags:requiredIndicator/><tags:message code="study.label.organization"/></div>
	                <div class="value">${command.study.leadStudySite.organization.displayName} </div>
	            </div>
		        <div style="margin-left:150px;">
		            <table id="leadCraTable" class="tablecontent" width="80%" border="0">
			            <tr id="ss-table-head" class="amendment-table-head">
		                    <th width="50%" class="tableHeader">&nbsp;
		                        <tags:message code='study.label.clinical.staff'/></th>
		                    <th width="20%" class="tableHeader" style=" background-color: none">&nbsp;</th>
		                    <th width="25%" class="tableHeader" style=" background-color: none">&nbsp;</th>
		                    <th width="5%" class="tableHeader" style=" background-color: none">&nbsp;</th>
		                </tr>
		                <tbody>
		                	<c:if test="${fn:length(command.study.leadCRAs) == 0}">
			                	<tr><td colspan="2">
	       						   <form:input path="leadCRAs[0].organizationClinicalStaff" id="leadCRAs[0].organizationClinicalStaff" cssClass="validate-NOTEMPTY"
						            		title="Lead Site CRA" cssStyle="display:none;"/>
				       				<tags:yuiAutocompleter inputName="leadCRAs[0].organizationClinicalStaffInput"
				                              value="${command.leadCRAs[0].displayName}" required="false" hiddenInputName="leadCRAs[0].organizationClinicalStaff"/>
				                </td>
			                </tr>
		                	</c:if>
		                
			                <c:forEach items="${command.study.leadCRAs}" var="leadCra" varStatus="status">
			                        <tags:oneClinicalStaff index="${status.index}" leadCRA="${leadCra}" readOnly="true" inputName="leadCRAs[${status.index}].organizationClinicalStaff"/>
			                </c:forEach>
			                <tr></tr>
		                </tbody>
		            </table>
	            </div>
	            <div style="margin-left:150px;margin-top:10px;margin-bottom:10px;">
		        	<tags:button color="blue" markupWithTag="a" onclick="javascript:addLeadCra()"
		            	         value="study.button.add_study_site" icon="add" size="small"/>
				</div>
			    <div id="hiddenDiv"></div>
		    </chrome:division>
	        
	        <chrome:division title="study.label.clinical.staff.pi">
	            <div class="row">
	                <div class="label"><tags:requiredIndicator/><tags:message code="study.label.organization"/></div>
	                <div class="value">${command.study.leadStudySite.organization.displayName} </div>
	            </div>
	           
           		<form:input path="principalInvestigator.organizationClinicalStaff"
                       id="principalInvestigator.organizationClinicalStaff" cssClass="validate-NOTEMPTY"
                       title="Overall PI "
                       cssStyle="display:none;"/>
	            <div class="row">
	                <div class="label"><tags:message code='study.label.clinical.staff'/></div>
	                <div class="value">
	                    <tags:studyStaffAutocompleter inputName="principalInvestigator.organizationClinicalStaffInput"
	                                           value="${command.principalInvestigator.displayName}" required="false"
	                                           hiddenInputName="principalInvestigator.organizationClinicalStaff"
	                                           staff="${command.principalInvestigator}"/>
	                </div>
	            </div>
	        </chrome:division>
		</chrome:box>
        </jsp:attribute>

</tags:tabForm>

</body>
</html>