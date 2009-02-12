<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="study" tagdir="/WEB-INF/tags/study" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
    acCreate(new siteAutoComplter('study.studySites[${studySiteIndex}].organization'))
    initSearchField()
</script>


<study:studySiteClinicalStaff studySiteClinicalStaffIndex="${studySiteClinicalStaffIndex}" studySiteIndex="${studySiteIndex}" studySiteClinicalStaff="${studySiteClinicalStaff}"/>




