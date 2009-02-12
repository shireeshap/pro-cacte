<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="clinicalStaffAssignmentRole" type="gov.nih.nci.ctcae.core.domain.ClinicalStaffAssignmentRole" required="true" %>
<%@attribute name="clinicalStaffAssignmentIndex" type="java.lang.Integer" required="true" %>
<%@attribute name="index" type="java.lang.Integer" required="true" %>

<c:set var="propertyName"
       value="clinicalStaff.clinicalStaffAssignments[${clinicalStaffAssignmentIndex}].clinicalStaffAssignmentRoles[${index}]"></c:set>


<tr id="${propertyName}-row">
    <td style="border-right:none;" width="35%">
        <tags:renderSelect propertyName="${propertyName}.role" options="${siteRoles}"
                           propertyValue="${clinicalStaffAssignmentRole.role}" noForm="true" doNotshowLabel="true"
                           required="true"
                           displayName="clinicalStaff.label.role"/>
    </td>
    <td style="border-right:none;" width="30%">
        <tags:renderSelect propertyName="${propertyName}.roleStatus" options="${roleStatus}"
                           propertyValue="${clinicalStaffAssignmentRole.roleStatus}" noForm="true" doNotshowLabel="true"
                           required="true"
                           displayName="clinicalStaff.label.role.status"/>
    </td>
    <td style="border-right:none;" width="30%">


        <tags:renderDate propertyName="${propertyName}.statusDate"
                         displayName="clinicalStaff.label.role.status.date"
                         required="true" noForm="true"
                         dateValue="${clinicalStaffAssignmentRole.statusDate}"
                         doNotShowFormat="true" doNotshowLabel="true"/>

    </td>

    <td style="border-left:none;" width="5%">

        <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"
           href="javascript:deleteSiteRole('${clinicalStaffAssignmentIndex}','${index}');">
            <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"
                 style="vertical-align:middle">
        </a>
    </td>

</tr>