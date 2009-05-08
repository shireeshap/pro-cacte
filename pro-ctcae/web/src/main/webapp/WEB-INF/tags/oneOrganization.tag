<%@ attribute name="displayError" type="java.lang.Boolean" %>
<%@ attribute name="inputName" required="true" %>
<%@ attribute name="required" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="isLeadSite" required="false" %>
<%@ attribute name="index" type="java.lang.String" required="false" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tr id="${inputName}-row" <c:if test="${isLeadSite}"> style="display:none"</c:if>>
    <td style="border-right:none;">
        <tags:renderAutocompleter propertyName="${inputName}" displayName="${title}" required="${required}"
                                  size="${size}" doNotshowLabel="true" noForm="true"/>

    </td>
    <td style="border-left:none;"></td>
</tr>

<script type="text/javascript">
    acCreate(new siteAutoComplter('${inputName}'))
    initSearchField()
</script>

