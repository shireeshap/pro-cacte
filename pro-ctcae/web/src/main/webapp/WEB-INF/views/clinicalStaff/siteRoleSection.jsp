<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="display" tagdir="/WEB-INF/tags/display" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--<%@attribute name="siteClinicalStaffRole" type="gov.nih.nci.ctcae.core.domain.SiteClinicalStaffRole" %>--%>

<c:set var="propertyName"
       value="clinicalStaff.siteClinicalStaffs[${siteClinicalStaffIndex}].siteClinicalStaffRoles[${index}]"></c:set>


<tr id="${propertyName}-row">
    <td style="border-right:none;" width="35%">
        <display:displaySelect propertyName="${propertyName}.role" items="${roles}"
                               propertyValue="${siteClinicalStaffRole.role}"/>
    </td>
    <td style="border-right:none;" width="30%">
        <display:displaySelect propertyName="${propertyName}.role" items="${roleStatus}"
                               propertyValue="${siteClinicalStaffRole.roleStatus}"/>
    </td>
    <td style="border-right:none;" width="30%">

        <tags:renderDate propertyName="${propertyName}.role"
                         displayName="participant.label.date_of_birth"
                         required="true" noForm="true"
                         propertyValue="${siteClinicalStaffRole.statusDate}"
                         doNotShowFormat="true" doNotshowLabel="true" />

    </td>

    <td style="border-left:none;" width="5%">

        <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"
           href="javascript:fireDelete('${index}','${propertyName}-row');">
            <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"
                 style="vertical-align:middle">
        </a>
    </td>

</tr>