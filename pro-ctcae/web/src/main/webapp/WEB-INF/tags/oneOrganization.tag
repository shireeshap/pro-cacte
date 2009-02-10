<%@ attribute name="displayError" type="java.lang.Boolean" %>
<%@ attribute name="inputName" required="true" %>
<%@ attribute name="title" required="true" %>
<%@ attribute name="index" type="java.lang.String" required="false" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="display" tagdir="/WEB-INF/tags/display" %>


<script type="text/javascript">
    acCreate(new siteAutoComplter('${inputName}'))
    initSearchField()
</script>

<tr id="${inputName}-row">
    <td style="border-right:none;">


        <tags:renderAutocompleter propertyName="${inputName}" displayName="${title}" required="${required}"
                                  size="${size}" doNotshowLabel="true"/>

    </td>

    <td style="border-left:none;">

        <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"
           href="javascript:fireDelete('${index}','${inputName}-row');">
            <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"
                 style="vertical-align:middle">
        </a>
    </td>


</tr>


