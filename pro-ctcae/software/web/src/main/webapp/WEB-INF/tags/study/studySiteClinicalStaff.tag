<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@attribute name="studyOrganizationClinicalStaff" type="gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff"
             required="true" %>

<%@attribute name="studyOrganizationClinicalStaffIndex" type="java.lang.Integer" required="true" %>
<%@attribute name="roleStatusOptions" required="true" type="java.util.List" %>
<%@attribute name="readOnly" required="false" type="java.lang.Boolean" %>
<%@attribute name="notifyOptions" type="java.util.List" %>

<p id="splitter"/>

<tags:noForm>
<c:set var="propertyName"
       value="studyOrganizationClinicalStaffs[${studyOrganizationClinicalStaffIndex}]"/>

<tr id="row-${studyOrganizationClinicalStaffIndex}">
    <td style="border-right:none;" width="64%">
        <c:choose>
            <c:when test="${readOnly}">
				${studyOrganizationClinicalStaff.organizationClinicalStaff.displayName} 
            </c:when>
            <c:otherwise>
                <form:input path="${propertyName}.organizationClinicalStaff"
                       id="${propertyName}.organizationClinicalStaff" cssClass="validate-NOTEMPTY" title="Staff"
                       cssStyle="display:none;"/>
                   <tags:yuiAutocompleter inputName="${propertyName}.organizationClinicalStaffInput"
                                          value=" ${studyOrganizationClinicalStaff.organizationClinicalStaff.displayName}" required="false"
                                          hiddenInputName="${propertyName}.organizationClinicalStaff" />

            </c:otherwise>
        </c:choose>
    </td>
     <c:choose>
         <c:when test="${readOnly}">
				<td>
                     <tags:renderSelect propertyName="${propertyName}.notify" doNotshowLabel="true"
                             required="false" options="${notifyOptions}" propertyValue="${notify}"/>
                 </td>
                 <td align="center">
                     <c:choose>
                         <c:when test="${studyOrganizationClinicalStaff.roleStatus.displayName eq 'Active'}">
                             <tags:button color="blue" type="button" value="De-activate"
                                          onclick="changeStatus('${studyOrganizationClinicalStaff.roleStatus.displayName}','${studyOrganizationClinicalStaff.id}')"
                                          size="small"/>
                         </c:when>
                         <c:otherwise>
                             <tags:button color="blue" type="button" value="Activate"
                                          onclick="changeStatus('${studyOrganizationClinicalStaff.roleStatus.displayName}','${studyOrganizationClinicalStaff.id}')"
                                          size="small"/>
                         </c:otherwise>
                     </c:choose>
                 </td>
                 <td>Effectively ${studyOrganizationClinicalStaff.roleStatus} from <tags:formatDate
                         value="${studyOrganizationClinicalStaff.statusDate}"/></td>
         </c:when>
         <c:otherwise>
                 <td>
                     <tags:renderSelect propertyName="${propertyName}.notify" doNotshowLabel="true"
                             required="false" options="${notifyOptions}" propertyValue="${notify}"/>
                 </td>
			     <td colspan="2" align="left" >
                     <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"
                        href="javascript:deleteSiteRole('${studyOrganizationClinicalStaffIndex}');">
                         <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"
                              style="vertical-align:middle;text-align:left">
                     </a>
                 </td> 
         </c:otherwise>
     </c:choose>
</tr>
</tags:noForm>
