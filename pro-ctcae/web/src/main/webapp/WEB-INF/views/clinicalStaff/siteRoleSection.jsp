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
        <tags:renderSelect propertyName="${propertyName}.role" options="${roles}"
                           propertyValue="${siteClinicalStaffRole.role}" noForm="true" doNotshowLabel="true" required="true"
                displayName="clinicalStaff.label.role"/>
    </td>
    <td style="border-right:none;" width="30%">
        <tags:renderSelect propertyName="${propertyName}.roleStatus" options="${roleStatus}"
                           propertyValue="${siteClinicalStaffRole.roleStatus}" noForm="true" doNotshowLabel="true" required="true"
                displayName="clinicalStaff.label.role.status"/>
    </td>
    <td style="border-right:none;" width="30%">

        <tags:renderDate propertyName="${propertyName}.statusDate"
                         displayName="clinicalStaff.label.role.status.date"
                         required="true" noForm="true"
                         propertyValue="${siteClinicalStaffRole.statusDate}"
                         doNotShowFormat="true" doNotshowLabel="true"/>

    </td>

    <td style="border-left:none;" width="5%">

        <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"
           href="javascript:fireDelete('${index}','${propertyName}-row');">
            <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"
                 style="vertical-align:middle">
        </a>
    </td>

</tr>