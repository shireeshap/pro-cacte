<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@attribute name="clinicalStaff" required="true" %>
<%@attribute name="index" required="true" %>
<%@attribute name="notificationindex" required="true" %>
<tags:renderAutocompleter
        propertyName="participant.studyParticipantAssignments[${index}].notificationClinicalStaff[${notificationindex}]"
        displayName="participant.label.clinical.staff.notificationclinicalstaff" noForm="true"
        propertyValue="${studyParticipantAssignment.notificationClinicalStaff[notificationindex].studyOrganizationClinicalStaff.organizationClinicalStaff.clinicalStaff.displayName}"/>





