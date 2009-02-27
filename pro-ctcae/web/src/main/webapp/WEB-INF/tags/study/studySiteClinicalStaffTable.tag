<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="study" tagdir="/WEB-INF/tags/study" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@attribute name="role" type="java.lang.String" required="true" %>
<%@attribute name="studySiteId" type="java.lang.String" required="true" %>
<%@attribute name="studyCommand" type="gov.nih.nci.ctcae.web.study.StudyCommand" required="true" %>
<%@attribute name="roleStatusOptions" required="true" type="java.util.List" %>


<table cellspacing="0" width="100%" >
    <tr>
        <td width="80%">
            <div align="left" style="margin-left: 15px">
                <table class="tablecontent" width="100%">
                    <tr id="ss-table-head" class="amendment-table-head">
                        <th width="50%" class="tableHeader"><tags:requiredIndicator/><tags:message
                                code="study.label.clinical.staff"/></th>

                        <th width="15%" class="tableHeader"><tags:requiredIndicator/><tags:message
                                code="clinicalStaff.label.role.status"/></th>

                        <th width="30%" class="tableHeader"><tags:requiredIndicator/><tags:message
                                code="clinicalStaff.label.role.status.date"/></th>

                        <th width="5%" class="tableHeader" style=" background-color: none">
                            &nbsp;</th>

                    </tr>
                    <c:forEach var="studyOrganizationClinicalStaff"
                               items="${studyCommand.studyOrganizationClinicalStaffs}" varStatus="status">

                        <c:if test="${studyOrganizationClinicalStaff.studyOrganization.id eq studySiteId}">
                            <c:if test="${role eq  studyOrganizationClinicalStaff.role}">
                                <study:studySiteClinicalStaff
                                        studyOrganizationClinicalStaff="${studyOrganizationClinicalStaff}"
                                        studyOrganizationClinicalStaffIndex="${status.index}"
                                        roleStatusOptions="${roleStatusOptions}"/>
                            </c:if>

                        </c:if>

                    </c:forEach>
                    <tr id="hiddenDivForStudySite_${studySiteId}_Role_${role}"></tr>
                </table>
            </div>
        </td>
        <td valign="top" width="10%">
            <input type="button" value="<tags:message code="study.button.add.clinical.staff"/>" onclick="javascript:addClinicalStaff(${studySiteId},'${role}')">
            
        </td>
    </tr>
</table>