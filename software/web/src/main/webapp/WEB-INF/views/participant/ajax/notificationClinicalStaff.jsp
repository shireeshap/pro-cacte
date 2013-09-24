<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

    function initializeAutoCompleter1(){
        <c:if test="${addAction == 'addTp'}">
        	new YUIAutoCompleter('participant.studyParticipantAssignments[${index}].treatingPhysicians[${tpIndex}].studyOrganizationClinicalStaffInput',
	                getStudyOrganizationClinicalStaffForTreatingPhysicianRole1, handleSelect);
	            var treatingPhysician = "${command.participant.studyParticipantAssignments[index].treatingPhysicians[tpIndex].studyOrganizationClinicalStaff.displayName}";
	            if(treatingPhysician != ''){
	            	$('participant.studyParticipantAssignments[${index}].treatingPhysicians[${tpIndex}].studyOrganizationClinicalStaffInput').value
		                = "${command.participant.studyParticipantAssignments[index].treatingPhysicians[tpIndex].studyOrganizationClinicalStaff.displayName}"
	                $('participant.studyParticipantAssignments[${index}].treatingPhysicians[${tpIndex}].studyOrganizationClinicalStaffInput').removeClassName('pending-search');
            }
        </c:if>

        <c:if test="${addAction == 'addRn'}">
	        new YUIAutoCompleter('participant.studyParticipantAssignments[${index}].researchNurses[${rnIndex}].studyOrganizationClinicalStaffInput',
	                getStudyOrganizationClinicalStaffForNurseRole1, handleSelect);
	        	var nurseName = "${command.participant.studyParticipantAssignments[index].researchNurses[rnIndex].studyOrganizationClinicalStaff.displayName}";
	        	if(nurseName != ''){
	                $('participant.studyParticipantAssignments[${index}].researchNurses[${rnIndex}].studyOrganizationClinicalStaffInput').value =
	                    "${command.participant.studyParticipantAssignments[index].researchNurses[rnIndex].studyOrganizationClinicalStaff.displayName}";
	                $('participant.studyParticipantAssignments[${index}].researchNurses[${rnIndex}].studyOrganizationClinicalStaffInput').removeClassName('pending-search');
            }
        </c:if>
    }
    
    function getStudyOrganizationClinicalStaffForTreatingPhysicianRole1(sQuery) {
       showIndicator("participant.studyParticipantAssignments[${index}].treatingPhysicians[${tpIndex}].studyOrganizationClinicalStaffInput-indicator");
       var callbackProxy = function(results) {
           aResults = results;
       };
       var callMetaData = { callback:callbackProxy, async:false};
       clinicalStaff.matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole(unescape(sQuery),
              ${studySiteId}, 'TREATING_PHYSICIAN',callMetaData);
       hideIndicator("participant.studyParticipantAssignments[${index}].treatingPhysicians[${tpIndex}].studyOrganizationClinicalStaffInput-indicator");
       return aResults;
    }
    
    function getStudyOrganizationClinicalStaffForNurseRole1(sQuery) {
        showIndicator("participant.studyParticipantAssignments[${index}].researchNurses[${rnIndex}].studyOrganizationClinicalStaffInput-indicator");
        var callbackProxy = function(results) {
            aResults = results;
        };
        var callMetaData = { callback:callbackProxy, async:false};
        clinicalStaff.matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole(unescape(sQuery),
        		${studySiteId}, 'NURSE',callMetaData);
        hideIndicator("participant.studyParticipantAssignments[${index}].researchNurses[${rnIndex}].studyOrganizationClinicalStaffInput-indicator");
        return aResults;
     }

    initializeAutoCompleter1();
</script>

<tags:notificationClinicalStaff addAction="${addAction}" notifyOptions="${notifyOptions}" index="${index}" rnIndex="${rnIndex}" tpIndex="${tpIndex}" />