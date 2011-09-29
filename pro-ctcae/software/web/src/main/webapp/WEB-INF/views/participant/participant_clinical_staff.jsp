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
                new YUIAutoCompleter('participant.studyParticipantAssignments[${varIndex}].treatingPhysician.studyOrganizationClinicalStaffInput',
                        getStudyOrganizationClinicalStaffForTreatingPhysicianRole, handleSelect);
                    var treatingPhysician = "${command.participant.studyParticipantAssignments[varIndex].treatingPhysician.studyOrganizationClinicalStaff.displayName}";
                    if(treatingPhysician != ''){
                    	$('participant.studyParticipantAssignments[${varIndex}].treatingPhysician.studyOrganizationClinicalStaffInput').value
        	                = "${command.participant.studyParticipantAssignments[varIndex].treatingPhysician.studyOrganizationClinicalStaff.displayName}"
		                $('participant.studyParticipantAssignments[${varIndex}].treatingPhysician.studyOrganizationClinicalStaffInput').removeClassName('pending-search');
                    }
                    
                 function getStudyOrganizationClinicalStaffForTreatingPhysicianRole(sQuery) {
                    showIndicator("participant.studyParticipantAssignments[${varIndex}].treatingPhysician.studyOrganizationClinicalStaffInput-indicator");
                    var callbackProxy = function(results) {
                        aResults = results;
                    };
                    var callMetaData = { callback:callbackProxy, async:false};
                    clinicalStaff.matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole(unescape(sQuery),
                           ${studyParticipantAssignment.studySite.id}, 'TREATING_PHYSICIAN',callMetaData);
                    hideIndicator("participant.studyParticipantAssignments[${varIndex}].treatingPhysician.studyOrganizationClinicalStaffInput-indicator");
                    return aResults;
                 }

               var baseNameNurseDisplayName = 'participant.studyParticipantAssignments[${varIndex}].researchNurse.studyOrganizationClinicalStaff.displayName';
                new YUIAutoCompleter('participant.studyParticipantAssignments[${varIndex}].researchNurse.studyOrganizationClinicalStaffInput',
                        getStudyOrganizationClinicalStaffForNurseRole, handleSelect);
                	var nurseName = "${command.participant.studyParticipantAssignments[varIndex].researchNurse.studyOrganizationClinicalStaff.displayName}";
                	if(nurseName != ''){
                        $('participant.studyParticipantAssignments[${varIndex}].researchNurse.studyOrganizationClinicalStaffInput').value =
                            "${command.participant.studyParticipantAssignments[varIndex].researchNurse.studyOrganizationClinicalStaff.displayName}";
	                    $('participant.studyParticipantAssignments[${varIndex}].researchNurse.studyOrganizationClinicalStaffInput').removeClassName('pending-search');
                    }

                 function getStudyOrganizationClinicalStaffForNurseRole(sQuery) {
                    showIndicator("participant.studyParticipantAssignments[${varIndex}].researchNurse.studyOrganizationClinicalStaffInput-indicator");
                    var callbackProxy = function(results) {
                        aResults = results;
                    };
                    var callMetaData = { callback:callbackProxy, async:false};
                    clinicalStaff.matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole(unescape(sQuery),
                           ${studyParticipantAssignment.studySite.id}, 'NURSE',callMetaData);
                    hideIndicator("participant.studyParticipantAssignments[${varIndex}].researchNurse.studyOrganizationClinicalStaffInput-indicator");
                    return aResults;
                 }

                <c:forEach items="${studyParticipantAssignment.notificationClinicalStaff}" var="clinicalStaff" varStatus="notificationstatus">
                   new YUIAutoCompleter('participant.studyParticipantAssignments[${varIndex}].notificationClinicalStaff[${notificationstatus.index}].studyOrganizationClinicalStaffInput',
                            getStudyOrganizationClinicalStaffForNurseAndTPRole, handleSelect);
                    	var staffName = "${command.participant.studyParticipantAssignments[varIndex].notificationClinicalStaff[notificationstatus.index].studyOrganizationClinicalStaff.displayName}";
                    	if(staffName != ''){
                            $('participant.studyParticipantAssignments[${varIndex}].notificationClinicalStaff[${notificationstatus.index}].studyOrganizationClinicalStaffInput').value
        	                    = "${command.participant.studyParticipantAssignments[varIndex].notificationClinicalStaff[notificationstatus.index].studyOrganizationClinicalStaff.displayName}";
		                    $('participant.studyParticipantAssignments[${varIndex}].notificationClinicalStaff[${notificationstatus.index}].studyOrganizationClinicalStaffInput').removeClassName('pending-search');
                        }

                     function getStudyOrganizationClinicalStaffForNurseAndTPRole(sQuery) {
                        showIndicator("participant.studyParticipantAssignments[${varIndex}].notificationClinicalStaff[${notificationstatus.index}].studyOrganizationClinicalStaffInput-indicator");
                        var callbackProxy = function(results) {
                            aResults = results;
                        };
                        var callMetaData = { callback:callbackProxy, async:false};
                        clinicalStaff.matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole(unescape(sQuery),
                               ${studyParticipantAssignment.studySite.id}, 'TREATING_PHYSICIAN|NURSE',callMetaData);
                        hideIndicator("participant.studyParticipantAssignments[${varIndex}].notificationClinicalStaff[${notificationstatus.index}].studyOrganizationClinicalStaffInput-indicator");
                        return aResults;
                     }

                </c:forEach>
            </c:if>
        </c:forEach>

        })

        function addNotificationClinicalStaff(index) {
            $('notificationtable_' + index).show();
            var request = new Ajax.Request("<c:url value="/pages/participant/addNotificationClinicalStaff"/>", {
                onComplete:addNotificationClinicalStaffDiv,
                parameters:<tags:ajaxstandardparams/>+"&index=" + index + "&id=" + gup('id'),
                method:'get'
            })
        }

        function addNotificationClinicalStaffDiv(transport) {
            var response = transport.responseText;
            new Insertion.Before("hiddenDiv", response);
        }

        function deleteNotification(index, notificationIndex) {
            var request = new Ajax.Request("<c:url value="/pages/participant/addNotificationClinicalStaff"/>", {
                parameters:<tags:ajaxstandardparams/>+"&index=" + index + "&notificationIndex=" + notificationIndex + "&action=delete&id=${param['id']}",
                onComplete:function(transport) {
                    $('row-' + index + '-' + notificationIndex).remove();
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

    <chrome:box title="participant.label.study">
        <div class="row">
            <div class="label" style="width:5em"><spring:message code="study.label.clinical.staff"/>:&nbsp;</div>
            <div class="value">${command.selectedStudyParticipantAssignment.studySite.study.displayName}</div>
        </div>
        <div class="row">
            <div class="label" style="width:7em"><spring:message code="participant.label.site"/>:&nbsp;</div>
            <div class="value">${command.selectedStudyParticipantAssignment.studySite.displayName}</div>
        </div>
    </chrome:box>

    <chrome:box title="participant.label.researchstaff">
        <chrome:division title="participant.label.sitepi"/>
        <div align="left" style="margin-left: 50px">
            <table class="tablecontent" width="40%">
                <tr>
                    <th class="tableHeader" width="70%">
                        <tags:message code="participant.label.name"/>
                    </th>
                    <th width=30%>
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
        <div align="left" style="margin-left: 50px">
            <table class="tablecontent" width="40%">
                <tr>
                    <th class="tableHeader" width="70%">
                        <tags:message code="participant.label.name"/>
                    </th>
                    <th width=30%>
                        <tags:message code="participant.label.notification"/>
                    </th>
                </tr>
                <c:forEach items="${command.selectedStudyParticipantAssignment.siteCRAs}" var="siteCRA"
                           varStatus="crastatus">
                    <tr>
                        <td>
                                ${siteCRA.studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}
                        </td>
                        <td>
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
    <chrome:box title="participant.primaryclinicalstaff">
        <table width="100%" cellpadding="0px" cellspacing="0px">
            <tr>
                <td width="60%">
                    <c:set var="treatingPhysican" value="participant.studyParticipantAssignments[${varIndex}].treatingPhysician.studyOrganizationClinicalStaff"/>

                    <form:input path="${treatingPhysican}" id="${treatingPhysican}" cssStyle="display:none;"/>
                    <div class="row">
                        <div class="label">
                            <tags:message code='participant.label.clinical.staff.treatingphysician'/> &nbsp;
                        </div>
                        <div class="value">
                           <tags:yuiAutocompleter inputName="${treatingPhysican}Input"
                                                 value="${command.participant.studyParticipantAssignments[varIndex].treatingPhysician.studyOrganizationClinicalStaff.displayName}" required="false"
                                                 hiddenInputName="participant.studyParticipantAssignments[${varIndex}].treatingPhysician.studyOrganizationClinicalStaff"/>
                        </div>
                    </div>

                </td>
                <td style="border-right:none;" width="30%">
                    <tags:renderSelect
                            propertyName="participant.studyParticipantAssignments[${varIndex}].treatingPhysician.notify"
                            displayName="participant.label.notification"
                            required="false" options="${notifyOptions}"/>
                </td>
            </tr>
            <tr align="left">
                <td>
                    <c:set var="nurse" value="participant.studyParticipantAssignments[${varIndex}].researchNurse.studyOrganizationClinicalStaff"/>

                    <form:input path="${nurse}" id="${nurse}" cssStyle="display:none;"/>
                    <div class="row">
                        <div class="label">
                            <tags:message code='participant.label.clinical.staff.researchnurse'/> &nbsp;
                        </div>
                        <div class="value">
                            <tags:yuiAutocompleter inputName="${nurse}Input"
                                   value="${command.participant.studyParticipantAssignments[varIndex].researchNurse.studyOrganizationClinicalStaff.displayName}"
                                   required="false"
                                   hiddenInputName="participant.studyParticipantAssignments[${varIndex}].researchNurse.studyOrganizationClinicalStaff"/>
                        </div>
                    </div>
                </td>
                <td style="border-left:none;" width="30%">
                    <tags:renderSelect
                            propertyName="participant.studyParticipantAssignments[${varIndex}].researchNurse.notify"
                            displayName="participant.label.notification"
                            required="false" options="${notifyOptions}"/>
                </td>
            </tr>
        </table>
        <br>
    </chrome:box>
    <chrome:box title="participant.label.otherstaff">
        <div align="left" style="margin-left: 50px">
            <table width="90%" class="tablecontent"
                   id="notificationtable_${varIndex}">
                <tr id="ss-table-head" class="amendment-table-head">
                    <th width="45%" class="tableHeader">
                        <spring:message code='participant.label.name' text=''/>
                    </th>
                    <th width="15%" class="tableHeader">
                        <spring:message code='participant.label.notification' text=''/>
                    </th>
                    <th width="5%" class="tableHeader">&nbsp;</th>
                </tr>
                <c:forEach items="${command.selectedStudyParticipantAssignment.notificationClinicalStaff}"
                           var="clinicalStaff"
                           varStatus="notificationstatus">
                   <%-- <tags:notificationClinicalStaff index="${varIndex}"
                                                    notificationindex="${notificationstatus.index}"
                                                    clinicalStaff="${clinicalStaff}"
                                                    studySiteId="${command.selectedStudyParticipantAssignment.studySite.id}"
                                                    notify="${clinicalStaff.notify}"
                                                    notifyOptions="${notifyOptions}"
                                                    role="${clinicalStaff.studyOrganizationClinicalStaff.role}"></tags:notificationClinicalStaff>--%>
                    <tr id="row-${varIndex}-${notificationstatus.index}">
                        <td style="border-right:none;">
                                <c:set var="property" value="participant.studyParticipantAssignments[${varIndex}].notificationClinicalStaff[${notificationstatus.index}].studyOrganizationClinicalStaff"/>
                                <%--<input type="text" name="${property}"  id="${property}"  style="display:none;"/>--%>
                                <form:input path="${property}" id="${property}" cssStyle="display:none;"/>
                                <tags:yuiAutocompleter inputName="${property}Input"
                                       value="${command.participant.studyParticipantAssignments[varIndex].notificationClinicalStaff[notificationstatus.index].studyOrganizationClinicalStaff.displayName}" required="false"
                                       hiddenInputName="participant.studyParticipantAssignments[${varIndex}].notificationClinicalStaff[${notificationstatus.index}].studyOrganizationClinicalStaff"/>


                        </td>
                        <td style="border-right:none;">
                            <tags:renderSelect
                                    propertyName="participant.studyParticipantAssignments[${varIndex}].notificationClinicalStaff[${notificationstatus.index}].notify"
                                    displayName="participant.label.notification"
                                    required="true" options="${notifyOptions}" noForm="true" propertyValue="${clinicalStaff.notify}"
                                    doNotshowLabel="true"/>

                        </td>
                        <td style="border-left:none;">
                            <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"
                               href="javascript:deleteNotification('${varIndex}','${notificationstatus.index}');">
                                <img src="../../images/checkno.gif" border="0" alt="delete"
                                     style="vertical-align:middle">
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                <tr id="hiddenDiv" align="center"></tr>
            </table>
            <tags:button value="Add" color="blue" type="button" size="small"
                         onclick="javascript:addNotificationClinicalStaff('${varIndex}');return false;" icon="add"/>
        </div>
        <br>
    </chrome:box>
    </jsp:attribute>
</tags:tabForm>
</body>
</html>