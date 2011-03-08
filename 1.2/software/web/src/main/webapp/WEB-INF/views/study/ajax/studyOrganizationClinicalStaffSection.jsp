<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="study" tagdir="/WEB-INF/tags/study" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>


<script type="text/javascript">
    acCreate(new organizationClinicalStaffAutoComplter('studyOrganizationClinicalStaffs[${studyOrganizationClinicalStaffIndex}].organizationClinicalStaff',
            '${studyOrganizationClinicalStaff.studyOrganization.id}'))
    initSearchField()
</script>
<tags:noForm>  
    <study:studySiteClinicalStaff studyOrganizationClinicalStaff="${studyOrganizationClinicalStaff}"
                                  studyOrganizationClinicalStaffIndex="${studyOrganizationClinicalStaffIndex}"
                                  roleStatusOptions="${roleStatusOptions}" notifyOptions="${notifyOptions}"/>
</tags:noForm>