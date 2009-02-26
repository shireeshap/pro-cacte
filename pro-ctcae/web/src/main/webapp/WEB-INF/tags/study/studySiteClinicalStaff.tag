<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="studyOrganizationClinicalStaff" type="gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff"
             required="true" %>

<%@attribute name="studyOrganizationClinicalStaffIndex" type="java.lang.Integer" required="true" %>
<%@attribute name="roleStatusOptions" required="true" type="java.util.List" %>

<c:set var="propertyName"
       value="studyOrganizationClinicalStaffs[${studyOrganizationClinicalStaffIndex}]"></c:set>


<tr id="${propertyName}-row">
    <td style="border-right:none;" width="40%">
        <tags:renderAutocompleter propertyName="${propertyName}.organizationClinicalStaff"
                                  displayName="study.label.clinical.staff" noForm="true" required="true"
                                  doNotshowLabel="true" size="20" doNotshowClear="true"/>
    </td>

    <td style="border-right:none;" width="15%">
        <tags:renderSelect propertyName="${propertyName}.roleStatus"
                           options="${roleStatusOptions}"
                           propertyValue="${studyOrganizationClinicalStaff.roleStatus}" noForm="true"
                           doNotshowLabel="true"
                           required="true"
                           displayName="clinicalStaff.label.role.status"/>
    </td>
    <td style="border-right:none;" width="40%">


        <tags:renderDate propertyName="${propertyName}.statusDate"
                         displayName="clinicalStaff.label.role.status.date"
                         required="true" noForm="true"
                         dateValue="${studyOrganizationClinicalStaff.statusDate}"
                         doNotShowFormat="true" doNotshowLabel="true" size="10"/>

    </td>

    <td style="border-left:none;" width="5%">

        <%--<a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"--%>
        <%--href="javascript:deleteSiteRole('${studyOrganizationClinicalStaffIndex}','${index}');">--%>
        <%--<img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"--%>
        <%--style="vertical-align:middle">--%>
        <%--</a>--%>
    </td>

</tr>