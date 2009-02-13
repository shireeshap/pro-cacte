<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="clinicalStaffAssignment" type="gov.nih.nci.ctcae.core.domain.ClinicalStaffAssignment"
             required="true" %>
<%@attribute name="index" type="java.lang.Integer" required="true" %>
<%@taglib prefix="administration" tagdir="/WEB-INF/tags/administration" %>


<chrome:division title="clinicalStaff.division.sites">
    <div style="text-align:right">
        <a class="del-${cssClass}" href="javascript:deleteSite(${index});">
            <img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete" style="vertical-align:middle">
        </a>
            <%--<tags:button type="anchor" icon="window_icon"--%>
            <%--value="clinicalStaff.button.delete.site"--%>
            <%--onClick="javascript:"></tags:button>--%>
    </div>
    <%--isSiteAutoCompleter="true" --%>
    <tags:renderAutocompleter propertyName="clinicalStaff.clinicalStaffAssignments[${index}].domainObjectId"
                              displayName="Site" noForm="true" required="true"
                              propertyValue="${clinicalStaffAssignment.displayName}" size="80"/>


    <table cellspacing="0" width="80%">
        <tr>
            <td>
                <div align="left" style="margin-left: 145px">
                    <table class="tablecontent" style="table-layout:fixed;">
                        <tr id="ss-table-head" class="amendment-table-head">
                            <th width="25%" class="tableHeader"><tags:requiredIndicator/><tags:message
                                    code="clinicalStaff.label.role"/></th>

                            <th width="25%" class="tableHeader"><tags:requiredIndicator/><tags:message
                                    code="clinicalStaff.label.role.status"/></th>
                            <th width="45%" class="tableHeader"><tags:requiredIndicator/><tags:message
                                    code="clinicalStaff.label.role.status.date"/></th>
                            <th width="5%" class="tableHeader" style=" background-color: none">&nbsp;</th>

                        </tr>

                        <c:forEach items="${clinicalStaffAssignment.clinicalStaffAssignmentRoles}"
                                   var="clinicalStaffAssignmentRole"
                                   varStatus="status">

                            <administration:clinicalStaffAssignmentRole
                                    clinicalStaffAssignmentRole="${clinicalStaffAssignmentRole}"
                                    clinicalStaffAssignmentIndex="${index}"
                                    clinicalStaffAssignmentRoleIndex="${status.index}"/>
                        </c:forEach>
                        <tr id="hiddenDivForRole_${index}"></tr>
                    </table>
                </div>
            </td>
            <td valign="top">
                <tags:button type="anchor" icon="add" value="clinicalStaff.button.add.role"
                             onClick="javascript:addRole(${index})"></tags:button>
            </td>
        </tr>
    </table>
    <br>


    <%--<a id="del-${empty idSuffix ? clinicalStaffAssignmentRoleIndex : idSuffix}" class="del-${cssClass}"--%>
    <%--href="javascript:deleteSiteRole('${clinicalStaffAssignmentIndex}','${clinicalStaffAssignmentRoleIndex}');">--%>
    <%--<img src="<chrome:imageUrl name="../checkno.gif"/>" border="0" alt="delete"--%>
    <%--style="vertical-align:middle">--%>
    <%--</a>--%>


</chrome:division>


