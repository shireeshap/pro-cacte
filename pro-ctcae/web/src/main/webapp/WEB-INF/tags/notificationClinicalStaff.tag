<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="clinicalStaff" required="true" %>
<%@attribute name="index" required="true" %>
<%@attribute name="notificationindex" required="true" %>
<%@attribute name="studySiteId" required="true" %>
<%@attribute name="notify" required="false" %>
<%@attribute name="notifyOptions" required="true" type="java.util.List" %>
<%@attribute name="role" required="false" %>
<script type="text/javascript">
    var baseNameNotification_${index}_${notificationindex} = 'participant.studyParticipantAssignments[${index}].notificationClinicalStaff[${notificationindex}].studyOrganizationClinicalStaff';
    acCreate(new studyOrganizationClinicalStaffForRoleAutoCompleter(baseNameNotification_${index}_${notificationindex}, '${studySiteId}', 'TREATING_PHYSICIAN|RESEARCH_NURSE'));
    initSearchField()
</script>
<tr id="${inputName}-row">
    <td style="border-right:none;">
        <tags:renderAutocompleter
                propertyName="participant.studyParticipantAssignments[${index}].notificationClinicalStaff[${notificationindex}].studyOrganizationClinicalStaff"
                displayName="participant.label.clinical.staff.notificationclinicalstaff" noForm="true"
                doNotshowLabel="true"
                propertyValue="${studyParticipantAssignment.notificationClinicalStaff[notificationindex].studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}"/>

    </td>
    <td style="border-right:none;">
      ${role}  
    </td>
    <td style="border-right:none;">
        <tags:renderSelect propertyName="participant.studyParticipantAssignments[${index}].notificationClinicalStaff[${notificationindex}].notify"
                           displayName="participant.label.notification"
                           required="true" options="${notifyOptions}" noForm="true" propertyValue="${notify}" doNotshowLabel="true"/>

    </td>
    <td style="border-left:none;">

        <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"
           href="javascript:deleteNotification('${index}','${notificationindex}');">
            <img src="../../images/checkno.gif" border="0" alt="delete"
                 style="vertical-align:middle">
        </a>
    </td>
</tr>




