<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="siteClinicalStaff" type="gov.nih.nci.ctcae.core.domain.SiteClinicalStaff" required="true" %>
<%@attribute name="siteClinicalStaffIndex" type="java.lang.Integer" required="true" %>

<c:set var="propertyName"
       value="clinicalStaff.siteClinicalStaffs[${siteClinicalStaffIndex}].organization"></c:set>

<tr id="${propertyName}-row">
    <td style="border-right:none;">


        <tags:renderAutocompleter propertyName="${propertyName}" required="true"
                                  displayName="clinicalStaff.division.site"
                                  doNotshowLabel="true" noForm="true"/>

    </td>

    <td style="border-left:none;">

        <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"
           href="javascript:deleteSite('${siteClinicalStaffIndex}');">
            <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"
                 style="vertical-align:middle">
        </a>
    </td>


</tr>
