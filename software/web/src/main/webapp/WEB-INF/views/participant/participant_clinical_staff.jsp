<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="study" tagdir="/WEB-INF/tags/study" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>
    <tags:dwrJavascriptLink objects="clinicalStaff"/>
    <tags:stylesheetLink name="yui-autocomplete"/>
    <tags:javascriptLink name="yui-autocomplete"/>
    <c:set var="studyParticipantAssignment" value="${command.selectedStudyParticipantAssignment}"/>
    <c:set var="varIndex" value="0"/>

    <script type="text/javascript">
        function handleSelect(stype, args) {
            var ele = args[0];
            var oData = args[2];
            if(oData == null){
             	ele.getInputEl().value="(Begin typing here)";
             	ele.getInputEl().addClassName('pending-search');
             } else {
	            ele.getInputEl().value = oData.displayName;
	            ele.getInputEl().removeClassName('pending-search');
	            var id = ele.getInputEl().id;
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

        Event.observe(window, "load", function() {
	        <c:forEach items="${command.participant.studyParticipantAssignments}" var="studyParticipantAssignment" varStatus="status">
	        	<c:if test="${studyParticipantAssignment.id eq command.selectedStudyParticipantAssignment.id}">
			    	<c:forEach items="${studyParticipantAssignment.treatingPhysicians}" var="tp" varStatus="tpStatus">
		                new YUIAutoCompleter('participant.studyParticipantAssignments[${varIndex}].treatingPhysicians[${tpStatus.index}].studyOrganizationClinicalStaffInput',
		                        getStudyOrganizationClinicalStaffForTreatingPhysicianRole, handleSelect);
		                    var treatingPhysician = "${command.participant.studyParticipantAssignments[varIndex].treatingPhysicians[tpStatus.index].studyOrganizationClinicalStaff.displayName}";
		                    if(treatingPhysician != ''){
		                    	$('participant.studyParticipantAssignments[${varIndex}].treatingPhysicians[${tpStatus.index}].studyOrganizationClinicalStaffInput').value
		        	                = "${command.participant.studyParticipantAssignments[varIndex].treatingPhysicians[tpStatus.index].studyOrganizationClinicalStaff.displayName}"
				                $('participant.studyParticipantAssignments[${varIndex}].treatingPhysicians[${tpStatus.index}].studyOrganizationClinicalStaffInput').removeClassName('pending-search');
		                    }
		                    
		                 function getStudyOrganizationClinicalStaffForTreatingPhysicianRole(sQuery) {
		                    showIndicator("participant.studyParticipantAssignments[${varIndex}].treatingPhysicians[${tpStatus.index}].studyOrganizationClinicalStaffInput-indicator");
		                    var callbackProxy = function(results) {
		                        aResults = results;
		                    };
		                    var callMetaData = { callback:callbackProxy, async:false};
		                    clinicalStaff.matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole(unescape(sQuery),
		                           ${studyParticipantAssignment.studySite.id}, 'TREATING_PHYSICIAN',callMetaData);
		                    hideIndicator("participant.studyParticipantAssignments[${varIndex}].treatingPhysicians[${tpStatus.index}].studyOrganizationClinicalStaffInput-indicator");
		                    return aResults;
		                 }
					</c:forEach>
		
			    	<c:forEach items="${studyParticipantAssignment.researchNurses}" var="rn" varStatus="rnStatus">
		                new YUIAutoCompleter('participant.studyParticipantAssignments[${varIndex}].researchNurses[${rnStatus.index}].studyOrganizationClinicalStaffInput',
		                        getStudyOrganizationClinicalStaffForNurseRole, handleSelect);
		                	var nurseName = "${command.participant.studyParticipantAssignments[varIndex].researchNurses[rnStatus.index].studyOrganizationClinicalStaff.displayName}";
		                	if(nurseName != ''){
		                        $('participant.studyParticipantAssignments[${varIndex}].researchNurses[${rnStatus.index}].studyOrganizationClinicalStaffInput').value =
		                            "${command.participant.studyParticipantAssignments[varIndex].researchNurses[rnStatus.index].studyOrganizationClinicalStaff.displayName}";
			                    $('participant.studyParticipantAssignments[${varIndex}].researchNurses[${rnStatus.index}].studyOrganizationClinicalStaffInput').removeClassName('pending-search');
		                    }
		
		                 function getStudyOrganizationClinicalStaffForNurseRole(sQuery) {
		                    showIndicator("participant.studyParticipantAssignments[${varIndex}].researchNurses[${rnStatus.index}].studyOrganizationClinicalStaffInput-indicator");
		                    var callbackProxy = function(results) {
		                        aResults = results;
		                    };
		                    var callMetaData = { callback:callbackProxy, async:false};
		                    clinicalStaff.matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole(unescape(sQuery),
		                           ${studyParticipantAssignment.studySite.id}, 'NURSE',callMetaData);
		                    hideIndicator("participant.studyParticipantAssignments[${varIndex}].researchNurses[${rnStatus.index}].studyOrganizationClinicalStaffInput-indicator");
		                    return aResults;
		                 }
		     		</c:forEach>
	            </c:if>
	        </c:forEach>

        })

        function addTreatingPhysician(index) {
            var request = new Ajax.Request("<c:url value="/pages/participant/addNotificationClinicalStaff"/>", {
                onComplete:function(transport){
	            	$('tpTable').show();
	                var response = transport.responseText;
	                var responseStr = response.split('<p id="splitter"/>');
	                jQuery('#tpTable tr:last').before(responseStr[1]);
	                new Insertion.Before("hiddenDiv", responseStr[0]);
            	},
                parameters:<tags:ajaxstandardparams/>+"&index=" + index + "&id=" + gup('id') +  "&action=addTp",
                method:'get'
            })
        }

        function addResearchNurse(index) {
            var request = new Ajax.Request("<c:url value="/pages/participant/addNotificationClinicalStaff"/>", {
                onComplete:function(transport){
	            	$('rnTable').show();
	                var response = transport.responseText;
	                var responseStr = response.split('<p id="splitter"/>');
	                jQuery('#rnTable tr:last').before(responseStr[1]);
	                new Insertion.Before("hiddenDiv", responseStr[0]);
            	},
                parameters:<tags:ajaxstandardparams/>+"&index=" + index + "&id=" + gup('id') +  "&action=addRn",
                method:'get'
            })
        }

        function deleteTreatingPhysician(index, tpIndex) {
            var request = new Ajax.Request("<c:url value="/pages/participant/addNotificationClinicalStaff"/>", {
                parameters:<tags:ajaxstandardparams/>+"&index=" + index + "&tpIndex=" + tpIndex + "&action=deleteTp&id=${param['id']}",
                onComplete:function(transport) {
                    $('tpRow-' + index + '-' + tpIndex).remove();
                } ,
                method:'get'
            });
        }

        function deleteResearchNurse(index, rnIndex) {
            var request = new Ajax.Request("<c:url value="/pages/participant/addNotificationClinicalStaff"/>", {
                parameters:<tags:ajaxstandardparams/>+"&index=" + index + "&rnIndex=" + rnIndex + "&action=deleteRn&id=${param['id']}",
                onComplete:function(transport) {
                    $('rnRow-' + index + '-' + rnIndex).remove();
                } ,
                method:'get'
            });
        }

        function gup(name) {
            name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
            var regexS = "[\\?&]" + name + "=([^&#]*)";
            var regex = new RegExp(regexS);
            var results = regex.exec(window.location.href);
            if (results == null)
                return "";
            else
                return results[1];
        }
    </script>

    <style type="text/css">
        div.row div.label {
            width: 15em;
        }

        div.row div.value {
            margin-left: 3.5em;
        }
        input.div > *{
            zoom:1;
        }
    </style>
</head>
<body>
<tags:tabForm tab="${tab}" flow="${flow}" notDisplayInBox="true">

<jsp:attribute name="singleFields">

    <chrome:box title="">
        <table>
            <tr>
                <td align="right" style="font-weight:bold;" width="25%"><spring:message code="reports.label.participant"/>:&nbsp;&nbsp;</td>
                <td>${command.selectedStudyParticipantAssignment.participant.displayName}</td>
            </tr>
            <tr>
                <td align="right" style="font-weight:bold;" width="25%"><spring:message code="study.label.clinical.staff"/>:&nbsp;&nbsp;</td>
                <td>${command.selectedStudyParticipantAssignment.studySite.study.displayName}</td>
            </tr>
            <tr>
                <td align="right" style="font-weight:bold;" width="25%"><spring:message code="participant.label.site"/>:&nbsp;&nbsp;</td>
                <td>${command.selectedStudyParticipantAssignment.studySite.displayName}</td>
            </tr>
        </table>
    </chrome:box>

    <chrome:box title="participant.label.researchstaff">
        <chrome:division title="participant.label.sitepi"/>
        <br/>
        <div align="left" style="margin-left: 50px">
            <table class="tablecontent" width="65%">
                <tr>
                    <th class="tableHeader" width="82%">
                        <tags:message code="participant.label.name"/>
                    </th>
                    <th width=18%>
                        <tags:message code="participant.label.notification"/>
                    </th>
                </tr>
                <c:forEach items="${command.selectedStudyParticipantAssignment.sitePIs}" var="sitePI"
                           varStatus="pistatus">
                    <tr>
                        <td>
                                ${sitePI.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}
                        </td>
                        <td>
                            <tags:renderSelect
                                    propertyName="participant.studyParticipantAssignments[${varIndex}].sitePIs[${pistatus.index}].notify"
                                    required="true" options="${notifyOptions}" doNotshowLabel="true"/>

                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <br>
        <chrome:division title="participant.label.sitecra"/>
        <br/>
        <div align="left" style="margin-left: 50px">
            <table class="tablecontent" width="65%">
                <tr>
                    <th class="tableHeader" width="82%">
                        <tags:message code="participant.label.name"/>
                    </th>
                    <th width=18%>
                        <tags:message code="participant.label.notification"/>
                    </th>
                </tr>
                <c:forEach items="${command.selectedStudyParticipantAssignment.siteCRAs}" var="siteCRA"
                           varStatus="crastatus">
                    <tr>
                        <td>
                                ${siteCRA.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}
                        </td>
                        <td  align="center">
                            <tags:renderSelect
                                    propertyName="participant.studyParticipantAssignments[${varIndex}].siteCRAs[${crastatus.index}].notify"
                                    required="true" options="${notifyOptions}" doNotshowLabel="true"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </div>
        <br>
    </chrome:box>
    <chrome:box title="participant.primaryclinicalstaff" autopad="true">
   		<chrome:division title="participant.label.clinical.staff.treatingphysician">
   		<br/>
        <div align="left" style="margin-left: 50px">
	    	<table class="tablecontent" width="65%" id="tpTable">
	            <tr>
	                <th class="tableHeader" width="76%">
	                    <tags:message code="participant.label.name"/>
	                </th>
	                <th width=19%>
	                    <tags:message code="participant.label.notification"/>
	                </th>
	                <th width="5%" class="tableHeader">&nbsp;</th>
	            </tr>
	            <tbody>
	            <c:forEach items="${command.participant.studyParticipantAssignments[varIndex].treatingPhysicians}" var="tp" varStatus="tpStatus">
		            <tr id="tpRow-${varIndex}-${tpStatus.index}" >
		                <td valign="middle">
			                 <c:set var="treatingPhysican" value="participant.studyParticipantAssignments[${varIndex}].treatingPhysicians[${tpStatus.index}].studyOrganizationClinicalStaff"/>
			                 <form:input path="${treatingPhysican}" id="${treatingPhysican}" cssStyle="display:none;" cssClass="validate-NOTEMPTY" title="Treating Physician"/>
			                 <tags:yuiAutocompleter inputName="${treatingPhysican}Input"
		                                              value="${command.participant.studyParticipantAssignments[varIndex].treatingPhysicians[tpStatus.index].studyOrganizationClinicalStaff.displayName}" required="false"
		                                              hiddenInputName="participant.studyParticipantAssignments[${varIndex}].treatingPhysicians[${tpStatus.index}].studyOrganizationClinicalStaff"/>
		                </td>
		                <td>
		                	<tags:renderSelect propertyName="participant.studyParticipantAssignments[${varIndex}].treatingPhysicians[${tpStatus.index}].notify"
		                        			   required="false" options="${notifyOptions}" doNotshowLabel="true"/>
						</td>
						<td align="center">
                            <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}" href="javascript:deleteTreatingPhysician('${varIndex}', '${tpStatus.index}');">
                                <img src="../../images/checkno.gif" border="0" alt="delete" style="vertical-align:middle">
                            </a>
                        </td>		                
		            </tr>
	            </c:forEach>
	            <tr></tr>
	            </tbody>
	        </table>
	        <br/>
        <tags:button value="Add" color="blue" type="button" size="small"  onclick="javascript:addTreatingPhysician('${varIndex}');" icon="add"/>
        </div>
        <br/>
        </chrome:division>
        
        <chrome:division title="participant.label.clinical.staff.researchnurse">
	        <br/>
	        <div align="left" style="margin-left: 50px">
		        <table class="tablecontent" width="65%" id="rnTable">
		            <tr class="amendment-table-head">
		                <th class="tableHeader" width="76%">
		                    <tags:message code='participant.label.name'/>
		                </th>
		                <th width=19%>
		                    <tags:message code="participant.label.notification"/>
		                </th>
		                <th width="5%" class="tableHeader">&nbsp;</th>
		            </tr>
			        <tbody>
		            <c:forEach items="${command.participant.studyParticipantAssignments[varIndex].researchNurses}" var="rn" varStatus="rnStatus">
			            <tr id="rnRow-${varIndex}-${rnStatus.index}" >
			            	 <td valign="middle">
			                    <c:set var="nurse" value="participant.studyParticipantAssignments[${varIndex}].researchNurses[${rnStatus.index}].studyOrganizationClinicalStaff"/>
			                    <form:input path="${nurse}" id="${nurse}" cssStyle="display:none;" cssClass="validate-NOTEMPTY" title="Research Nurse"/>
			                    <tags:yuiAutocompleter inputName="${nurse}Input" required="false"
			                           value="${command.participant.studyParticipantAssignments[varIndex].researchNurses[rnStatus.index].studyOrganizationClinicalStaff.displayName}"
			                           hiddenInputName="participant.studyParticipantAssignments[${varIndex}].researchNurses[${rnStatus.index}].studyOrganizationClinicalStaff"/>
			                 </td>   
				             <td>
			             		<tags:renderSelect propertyName="participant.studyParticipantAssignments[${varIndex}].researchNurses[${rnStatus.index}].notify"
			                                       required="false" options="${notifyOptions}" doNotshowLabel="true"/>
			                 </td>
			                 <td align="center">
			                     <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}" href="javascript:deleteResearchNurse('${varIndex}', '${rnStatus.index}');">
			                         <img src="../../images/checkno.gif" border="0" alt="delete" style="vertical-align:middle">
			                     </a>
				             </td>
				         </tr>
			             
			         </c:forEach>
	        		 <tr></tr>
			         </tbody>
		         </table>
		         <br/>
     			 <tags:button value="Add" color="blue" type="button" size="small"  onclick="javascript:addResearchNurse('${varIndex}');" icon="add"/>
	        </div>
	        <br />
        </chrome:division>
    </chrome:box>
	<div id="hiddenDiv"></div>
    </jsp:attribute>
</tags:tabForm>
</body>
</html>