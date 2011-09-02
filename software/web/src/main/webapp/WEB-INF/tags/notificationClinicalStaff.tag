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

<tr id="row-${index}-${notificationindex}">
    <td style="border-right:none;">
            <c:set var="property" value="participant.studyParticipantAssignments[${index}].notificationClinicalStaff[${notificationindex}].studyOrganizationClinicalStaff"/>
            <input type="text" name="${property}"  id="${property}"  style="display:none;"/>
            <%--<form:input path="${property}" id="${property}" cssStyle="display:none;"/>--%>
            <tags:yuiAutocompleter inputName="${property}Input"
                   value="${command.participant.studyParticipantAssignments[index].notificationClinicalStaff[notificationindex].studyOrganizationClinicalStaff.displayName}" required="false"
                   hiddenInputName="participant.studyParticipantAssignments[${index}].notificationClinicalStaff[${notificationindex}].studyOrganizationClinicalStaff"/>


    </td>
    <td style="border-right:none;">
        <tags:renderSelect
                propertyName="participant.studyParticipantAssignments[${index}].notificationClinicalStaff[${notificationindex}].notify"
                displayName="participant.label.notification"
                required="true" options="${notifyOptions}" noForm="true" propertyValue="${notify}"
                doNotshowLabel="true"/>

    </td>
    <td style="border-left:none;">

        <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"
           href="javascript:deleteNotification('${index}','${notificationindex}');">
            <img src="../../images/checkno.gif" border="0" alt="delete"
                 style="vertical-align:middle">
        </a>
    </td>
</tr>




