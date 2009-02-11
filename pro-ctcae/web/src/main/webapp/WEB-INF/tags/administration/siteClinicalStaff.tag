<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@attribute name="siteClinicalStaff" type="gov.nih.nci.ctcae.core.domain.SiteClinicalStaff" required="true" %>
<%@attribute name="index" type="java.lang.Integer" required="true" %>
<%@taglib prefix="administration" tagdir="/WEB-INF/tags/administration" %>

<chrome:division title="clinicalStaff.division.sites">

    <%--isSiteAutoCompleter="true" --%>
    <tags:renderAutocompleter propertyName="clinicalStaff.siteClinicalStaffs[${index}].organization"
                              displayName="Site" noForm="true" required="true"
                              propertyValue="${siteClinicalStaff.organization.displayName}"/>


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

            <c:forEach items="${siteClinicalStaff.siteClinicalStaffRoles}" var="siteClinicalStaffRole"
                       varStatus="status">

                <administration:siteClinicalStaffRole siteClinicalStaffRole="${siteClinicalStaffRole}"
                                                      siteClinicalStaffIndex="${index}"
                                                      index="${status.index}"/>
            </c:forEach>
            <tr id="hiddenDivForRole_${index}"></tr>

        </table>

    </div>
    <br>
    <tags:button type="anchor" icon="add" value="clinicalStaff.button.add.role"
                 onClick="javascript:addRole(${index})"></tags:button>
    <br>
    <br>
    <tags:button type="anchor" icon="window_icon"
                 value="clinicalStaff.button.delete.site"
                 onClick="javascript:deleteSite(${index})"></tags:button>


</chrome:division>


