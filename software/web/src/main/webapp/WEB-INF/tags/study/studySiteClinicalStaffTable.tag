<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="study" tagdir="/WEB-INF/tags/study" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@attribute name="role" type="java.lang.String" required="true" %>
<%@attribute name="studySiteId" type="java.lang.String" required="true" %>
<%@attribute name="studyCommand" type="gov.nih.nci.ctcae.web.study.StudyCommand" required="true" %>
<%@attribute name="roleStatusOptions" required="true" type="java.util.List" %>
<%@attribute name="notifyOptions" type="java.util.List" %> 


<table cellspacing="0" width="100%" border="0">
    <tr>
        <td width="80%">
            <table class="tablecontent" border="0" width="100%">
            	<c:if test="${fn:length(studyCommand.studyOrganizationClinicalStaffs) > 0}">
                <tr id="ss-table-head" class="amendment-table-head">
                    <th class="tableHeader"><tags:requiredIndicator/><tags:message code="study.label.clinical.staff"/></th>
                    <th colspan="3" class="tableHeader">Notify</th>
                </tr>
                </c:if>
                <c:forEach var="studyOrganizationClinicalStaff"
                           items="${studyCommand.studyOrganizationClinicalStaffs}" varStatus="status">

                    <c:if test="${studyOrganizationClinicalStaff.studyOrganization.id eq studySiteId}">
                        <c:if test="${role eq  studyOrganizationClinicalStaff.role}">
                            <study:studySiteClinicalStaff
                                    studyOrganizationClinicalStaff="${studyOrganizationClinicalStaff}"
                                    studyOrganizationClinicalStaffIndex="${status.index}" notifyOptions="${notifyOptions}"
                                    roleStatusOptions="${roleStatusOptions}" readOnly="true"/>
                        </c:if>

                    </c:if>

                </c:forEach>
                <tr id="hiddenDivForStudySite_${studySiteId}_Role_${role}"></tr>
            </table>
        </td>
    </tr>
    <tr>
        <td valign="top" width="10%" >
            <div align="left" style="margin-left: 13px">
                <tags:button value="Add" color="blue" type="button" size="small"
                             onclick="javascript:addClinicalStaff(${studySiteId},'${role}');return false;" icon="add"/>
            </div>
        </td>
        <td colspan="3"></td>
    </tr>
</table>