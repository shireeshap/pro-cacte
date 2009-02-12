<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="studySiteClinicalStaff" type="gov.nih.nci.ctcae.core.domain.StudySiteClinicalStaff" required="true" %>
<%@attribute name="studySiteClinicalStaffIndex" type="java.lang.Integer" required="true" %>
<%@attribute name="studySiteIndex" type="java.lang.Integer" required="true" %>
<%@taglib prefix="study" tagdir="/WEB-INF/tags/study" %>

<chrome:division title="clinicalStaff.division.sites">

    <%--isSiteAutoCompleter="true" --%>
    <tags:renderAutocompleter propertyName="study.studySites[${studySiteIndex}].studySiteClinicalStaffs[${studySiteClinicalStaffIndex}].siteClinicalStaff"
                              displayName="Site" noForm="true" required="true"
                              propertyValue="${studySiteClinicalStaff.siteClinicalStaff.clinicalStaff.firstName}"/>


    <div align="left" style="margin-left: 145px">
        <table width="50%" class="tablecontent">
            <tr id="ss-table-head" class="amendment-table-head">
                <th width="35%" class="tableHeader"><tags:requiredIndicator/><tags:message
                        code="clinicalStaff.label.role"/></th>

                <th width="30%" class="tableHeader"><tags:requiredIndicator/><tags:message
                        code="clinicalStaff.label.role.status"/></th>
                <th width="30%" class="tableHeader"><tags:requiredIndicator/><tags:message
                        code="clinicalStaff.label.role.status.date"/></th>
                <th width="5%" class="tableHeader" style=" background-color: none">&nbsp;</th>

            </tr>

            <c:forEach items="${studySiteClinicalStaff.clinicalStaffAssignmentRoles}" var="clinicalStaffAssignmentRole"
                       varStatus="status">

                <administration:clinicalStaffAssignmentRole clinicalStaffAssignmentRole="${clinicalStaffAssignmentRole}"
                                                      clinicalStaffAssignmentIndex="${studySiteClinicalStaffIndex}"
                                                      index="${status.index}"/>

            </c:forEach>
            <tr id="hiddenDivForRole_${studySiteClinicalStaffIndex}"></tr>

        </table>

    </div>
    <br>
    <tags:button type="anchor" icon="add" value="clinicalStaff.button.add.role"
                 onClick="javascript:addRole(${studySiteClinicalStaffIndex})"></tags:button>
    <br>
    <br>
    <tags:button type="anchor" icon="window_icon"
                 value="clinicalStaff.button.delete.site"
                 onClick="javascript:deleteSite(${studySiteClinicalStaffIndex})"></tags:button>


</chrome:division>


