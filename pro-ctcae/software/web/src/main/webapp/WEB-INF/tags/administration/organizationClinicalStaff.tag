<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="organizationClinicalStaff" type="gov.nih.nci.ctcae.core.domain.OrganizationClinicalStaff"
             required="true" %>
<%@attribute name="organizationClinicalStaffIndex" type="java.lang.Integer" required="true" %>
<%@attribute name="readOnly" type="java.lang.Boolean" required="false" %>

<c:set var="propertyName"
       value="clinicalStaff.organizationClinicalStaffs[${organizationClinicalStaffIndex}].organization"></c:set>

<tr id="row-${organizationClinicalStaffIndex}">
    <td style="border-right:none;">
        <c:choose>
            <c:when test="${readOnly}">
                ${organizationClinicalStaff.organization.displayName}
            </c:when>
            <c:otherwise>
                <tags:renderAutocompleter propertyName="${propertyName}" required="true"
                                          displayName="clinicalStaff.division.site"
                                          doNotshowLabel="true" noForm="true" size="100"/>
            </c:otherwise>
        </c:choose>


    </td>

    <td style="border-left:none;">
        <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"
           href="javascript:deleteSite('${organizationClinicalStaffIndex}');">
            <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"
                 style="vertical-align:middle">
        </a>
    </td>


</tr>
