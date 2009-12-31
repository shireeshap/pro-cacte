<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="administration" tagdir="/WEB-INF/tags/administration" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
    acCreate(new clinicalStaffSiteAutoComplter('clinicalStaff.organizationClinicalStaffs[${organizationClinicalStaffIndex}].organization'))
    initSearchField()
</script>


<administration:organizationClinicalStaff organizationClinicalStaff="${organizationClinicalStaff}"
                                          organizationClinicalStaffIndex="${organizationClinicalStaffIndex}"/>


