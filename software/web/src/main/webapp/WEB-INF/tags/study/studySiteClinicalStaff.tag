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

<c:set var="propertyName"
       value="studyOrganizationClinicalStaffs[${studyOrganizationClinicalStaffIndex}]"/>


<tr id="row-${studyOrganizationClinicalStaffIndex}">
    <td style="border-right:none;" width="40%">
        <c:choose>
            <c:when test="${readOnly}">
                ${studyOrganizationClinicalStaff.organizationClinicalStaff.displayName}
            </c:when>
            <c:otherwise>
                <tags:renderAutocompleter propertyName="${propertyName}.organizationClinicalStaff"
                                          displayName="study.label.clinical.staff" noForm="true" required="true"
                                          doNotshowLabel="true" size="70" doNotshowClear="true"/>


            </c:otherwise>
        </c:choose>
    </td>
    <td style="border-right:none;" width="60%">
        <c:choose>
            <c:when test="${readOnly}">
                <table cellpadding="0" cellspacing="0">
                    <tr>
                        <td>
                            <tags:renderSelect
                                    propertyName="${propertyName}.notify"
                                    displayName="participant.label.notification"
                                    required="false" options="${notifyOptions}" propertyValue="${notify}"/>

                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${studyOrganizationClinicalStaff.roleStatus.displayName eq 'Active'}">
                                    <tags:button color="blue" markupWithTag="a" value="De-activate"
                                                 onclick="changeStatus('${studyOrganizationClinicalStaff.roleStatus}','${studyOrganizationClinicalStaff.id}')"
                                                 size="small"/>
                                </c:when>
                                <c:otherwise>
                                    <tags:button color="blue" markupWithTag="a" value="Activate"
                                                 onclick="changeStatus('${studyOrganizationClinicalStaff.roleStatus}','${studyOrganizationClinicalStaff.id}')"
                                                 size="small"/>
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>Effectively ${studyOrganizationClinicalStaff.roleStatus} from <tags:formatDate
                                value="${studyOrganizationClinicalStaff.statusDate}"/></td>
                    </tr>
                </table>
            </c:when>
            <c:otherwise>
                <table>
                    <tr>
                        <td>
                            <tags:renderSelect
                                    propertyName="${propertyName}.notify"
                                    displayName="participant.label.notification"
                                    required="false" options="${notifyOptions}" propertyValue="${notify}"/>

                        </td>
                        <td>
                            <a id="del-${empty idSuffix ? index : idSuffix}" class="del-${cssClass}"
                               href="javascript:deleteSiteRole('${studyOrganizationClinicalStaffIndex}');">
                                <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"
                                     style="vertical-align:middle;text-align:left">
                            </a>
                        </td>
                    </tr>
                </table>


            </c:otherwise>
        </c:choose>
    </td>
</tr>