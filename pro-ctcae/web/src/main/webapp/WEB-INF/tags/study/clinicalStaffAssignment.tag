<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="clinicalStaffAssignment" type="gov.nih.nci.ctcae.core.domain.ClinicalStaffAssignment"
             required="true" %>
<%@attribute name="clinicalStaffAssignmentIndex" type="java.lang.Integer" required="true" %>
<%@taglib prefix="study" tagdir="/WEB-INF/tags/study" %>

<chrome:division title="${clinicalStaffAssignment.clinicalStaff.displayName}">

    <div class="row">
        <div class="label">Site</div>
        <div class="value">${clinicalStaffAssignment.displayName} </div>
    </div>

    <div align="left" style="margin-left: 145px">
        <table width="70%" class="tablecontent">
            <tr id="ss-table-head" class="amendment-table-head">
                <th width="35%" class="tableHeader"><tags:requiredIndicator/><tags:message
                        code="clinicalStaff.label.role"/></th>

                <th width="30%" class="tableHeader"><tags:requiredIndicator/><tags:message
                        code="clinicalStaff.label.role.status"/></th>
                <th width="30%" class="tableHeader"><tags:requiredIndicator/><tags:message
                        code="clinicalStaff.label.role.status.date"/></th>
                <th width="5%" class="tableHeader" style=" background-color: none">&nbsp;</th>

            </tr>

            <c:forEach items="${clinicalStaffAssignment.clinicalStaffAssignmentRoles}" var="clinicalStaffAssignmentRole"
                       varStatus="status">

                <study:clinicalStaffAssignmentRole clinicalStaffAssignmentRole="${clinicalStaffAssignmentRole}"
                                                   clinicalStaffAssignmentIndex="${clinicalStaffAssignmentIndex}"
                                                   index="${status.index}"/>
            </c:forEach>
            <tr id="hiddenDivForRole_${clinicalStaffAssignmentIndex}"></tr>

        </table>

    </div>
    <br>
    <tags:button type="anchor" icon="add" value="clinicalStaff.button.add.role"
                 onClick="javascript:addRole(${clinicalStaffAssignmentIndex})"></tags:button>
    <br>
    <br>
    <tags:button type="anchor" icon="window_icon"
                 value="study.button.delete.investigator"
                 onClick="javascript:deleteInvestigator(${clinicalStaffAssignmentIndex})"></tags:button>


</chrome:division>


