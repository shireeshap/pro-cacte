<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="clinicalStaffAssignment" type="gov.nih.nci.ctcae.core.domain.ClinicalStaffAssignment"
             required="true" %>
<%@attribute name="clinicalStaffAssignmentIndex" type="java.lang.Integer" required="true" %>
<%@taglib prefix="study" tagdir="/WEB-INF/tags/study" %>

<chrome:division title="${clinicalStaffAssignment.clinicalStaff.displayName}" enableDelete="true" deleteParams="deleteInvestigator(${clinicalStaffAssignmentIndex});">

    <div class="row">
        <div class="label">Site</div>
        <div class="value">${clinicalStaffAssignment.displayName} </div>
    </div>

    <table cellspacing="0" width="90%">
        <tr>
            <td>
                <div align="left" style="margin-left: 145px;">
                    <table class="tablecontent" width="80%">
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

                            <study:clinicalStaffAssignmentRole
                                    clinicalStaffAssignmentRole="${clinicalStaffAssignmentRole}"
                                    clinicalStaffAssignmentIndex="${clinicalStaffAssignmentIndex}"
                                    index="${status.index}"/>
                        </c:forEach>
                        <tr id="hiddenDivForRole_${clinicalStaffAssignmentIndex}"></tr>

                    </table>
                </div>
            </td>
            <td valign="top">
                <tags:button type="anchor" icon="add" value="clinicalStaff.button.add.role"
                             onClick="javascript:addRole(${clinicalStaffAssignmentIndex})"></tags:button>
            </td>
        </tr>
    </table>


</chrome:division>


