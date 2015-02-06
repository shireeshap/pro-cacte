<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">

    <style type="text/css">
        .label {
            width: 12em;
            padding: 1px;
            margin-right: 0.5em;
        }

        div.row div.value {
            white-space: normal;
        }

        #studyDetails td.label {
            font-weight: bold;
            float: left;
            margin-left: 0.5em;
            margin-right: 0.5em;
            width: 12em;
            padding: 1px;
        }
    </style>
</head>
<body>
<div class="tabpane">
    <div class="workflow-tabs2">
        <ul id="" class="tabs autoclear">
            <li id="thirdlevelnav-x" class="tab selected">
                <div>
                    <a href="createClinicalStaff"><tags:message code="clinicalStaff.tab.createStaff"/></a>
                </div>
            </li>
            <li id="thirdlevelnav-x" class="tab">
                <div>
                    <a href="searchClinicalStaff"><tags:message code="clinicalStaff.tab.searchStaff"/></a>
                </div>
            </li>
        </ul>
    </div>
</div>
<c:choose>
    <c:when test="${param['clinicalStaffId'] eq null}">
        <chrome:flashMessage flashMessage="New staff profile has been created successfully"/>
    </c:when>
    <c:otherwise>
        <chrome:flashMessage flashMessage="Staff profile has been updated successfully"/>
    </c:otherwise>
</c:choose>

<chrome:box title="Confirmation">
    <chrome:division title="clinicalStaff.division.details">
        <table border="0" style="width:100%">
            <tr>
                <td>

                    <div class="row">
                        <div class="label"><spring:message code="clinicalStaff.label.first_name"/>:</div>
                        <div class="value">${clinicalStaffCommand.clinicalStaff.firstName} </div>
                    </div>
                    <div class="row">
                        <div class="label"><spring:message code="clinicalStaff.label.middle_name"/>:</div>
                        <div class="value">${clinicalStaffCommand.clinicalStaff.middleName} </div>
                    </div>
                    <div class="row">
                        <div class="label"><spring:message code="clinicalStaff.label.last_name"/>:</div>
                        <div class="value">${clinicalStaffCommand.clinicalStaff.lastName} </div>
                    </div>
                </td>
                <td style="vertical-align:top">
                    <div class="row">
                        <div class="label"><spring:message code="clinicalStaff.label.phone"/>:</div>
                        <div class="value">${clinicalStaffCommand.clinicalStaff.phoneNumber} </div>
                    </div>
                    <div class="row">
                        <div class="label"><spring:message code="clinicalStaff.label.email_address"/>:</div>
                        <div class="value">${clinicalStaffCommand.clinicalStaff.emailAddress} </div>
                    </div>
                    <div class="row">
                        <div class="label"><spring:message code="clinicalStaff.label.identifier"/>:</div>
                        <div class="value">${clinicalStaffCommand.clinicalStaff.nciIdentifier} </div>
                    </div>
                </td>
            </tr>
        </table>
    </chrome:division>
    <c:if test="${clinicalStaffCommand.clinicalStaff.user.username ne null}">
        <chrome:division title="clinicalStaff.division.user_account">
            <table border="0" style="width:100%">
                <tr>
                    <td>

                        <div class="row">
                            <div class="label"><spring:message code="participant.label.username"/>:</div>
                            <div class="value">${clinicalStaffCommand.clinicalStaff.user.username} </div>
                        </div>
                    </td>
                </tr>
            </table>
        </chrome:division>
    </c:if>
    <chrome:division title="clinicalStaff.division.sites">

        <div align="left" style="margin-left: 50px">
            <table width="55%" class="tablecontent">
                <tr id="ss-table-head" class="amendment-table-head">
                    <th width="95%" class="tableHeader"><spring:message code="study.label.sites"/></th>
                    <th width="5%" class="tableHeader" style=" background-color: none">&nbsp;</th>

                </tr>
                <tr>
                    <td style="border-right:none;">
                        <c:forEach items="${clinicalStaffCommand.clinicalStaff.organizationClinicalStaffs}"
                                   var="organizationClinicalStaff">
                            <div class="row">
                                    ${organizationClinicalStaff.organization.displayName}
                            </div>
                        </c:forEach>
                    </td>
                    <td style="border-left:none;">
                    </td>
                </tr>
            </table>
        </div>
    </chrome:division>
</chrome:box>
<div style="float:right">
    <table>
        <tr>
            <td><tags:button value="Return to search results" color="blue" markupWithTag="a"
                             href="searchClinicalStaff?searchString=${clinicalStaffSearchString}"/></td>
            <td>
            <td><tags:button value="Create New Staff Profile" color="blue" markupWithTag="a"
                             href="createClinicalStaff"/></td>
            <td>
            <td><tags:button value="Edit" color="blue" markupWithTag="a"
                             href="createClinicalStaff?clinicalStaffId=${clinicalStaffCommand.clinicalStaff.id}"/></td>
            <td>
                <tags:button value="Finish" color="blue" markupWithTag="a"
                             href="/proctcae"/>
            </td>
        </tr>
    </table>


</div>
</body>
</html>