<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tags:dwrJavascriptLink objects="clinicalStaff"/>
<tags:stylesheetLink name="yui-autocomplete"/>
<tags:javascriptLink name="yui-autocomplete"/>

<script type="text/javascript">

     function handleSelect(stype, args) {
            var ele = args[0];
            var oData = args[2];
            ele.getInputEl().value = oData.displayName;
            var id = ele.getInputEl().id;
            var hiddenInputId = id.substring(0, id.indexOf('Input'));
    //            Element.update(hiddenInputId + "-selected-name", oData.displayName)
    //            $(hiddenInputId + '-selected').show()
    //            new Effect.Highlight(hiddenInputId + "-selected")
            $(hiddenInputId).value = oData.id;
        }

       function clearInput(inputId) {
            $(inputId).clear();
            $(inputId + 'Input').clear();
            $(inputId + 'Input').focus();
            $(inputId + 'Input').blur();
        }

    function initializeAutoCompleter1(){

        new YUIAutoCompleter('participant.studyParticipantAssignments[${index}].notificationClinicalStaff[${notificationindex}].studyOrganizationClinicalStaffInput',
                getStudyOrganizationClinicalStaffForNurseAndTPRole1, handleSelect);
        	var staffDisplayName = "${command.participant.studyParticipantAssignments[index].notificationClinicalStaff[notificationindex].studyOrganizationClinicalStaff.displayName}";
        	if(staffDisplayName != ''){
                $('participant.studyParticipantAssignments[${index}].notificationClinicalStaff[${notificationindex}].studyOrganizationClinicalStaffInput').value
                	= "${command.participant.studyParticipantAssignments[index].notificationClinicalStaff[notificationindex].studyOrganizationClinicalStaff.displayName}";
	        	$('participant.studyParticipantAssignments[${index}].notificationClinicalStaff[${notificationindex}].studyOrganizationClinicalStaffInput').removeClassName('pending-search');
        	}
    }

    function getStudyOrganizationClinicalStaffForNurseAndTPRole1(sQuery) {
        var callbackProxy = function(results) {
            aResults = results;
        };
        var callMetaData = { callback:callbackProxy, async:false};
        clinicalStaff.matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole(unescape(sQuery),
               ${studySiteId}, 'TREATING_PHYSICIAN|NURSE',callMetaData);
        return aResults;
     }

    initializeAutoCompleter1();
</script>
<tags:notificationClinicalStaff notifyOptions="${notifyOptions}" clinicalStaff="${clinicalStaff}" index="${index}" notificationindex="${notificationindex}" studySiteId="${studySiteId}"/>
