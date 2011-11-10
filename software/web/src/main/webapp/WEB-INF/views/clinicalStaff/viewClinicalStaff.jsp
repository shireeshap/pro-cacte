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
<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>


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

<chrome:box title="Overview">
    <chrome:division title="clinicalStaff.division.details">
        <table border="0" style="width:100%">
            <tr>
                <td>

                    <div class="row">
                        <div class="label"><spring:message code="clinicalStaff.label.first_name"/>:</div>
                        <div class="value">${command.firstName} </div>
                    </div>
                    <div class="row">
                        <div class="label"><spring:message code="clinicalStaff.label.middle_name"/>:</div>
                        <div class="value">${command.middleName} </div>
                    </div>
                    <div class="row">
                        <div class="label"><spring:message code="clinicalStaff.label.last_name"/>:</div>
                        <div class="value">${command.lastName} </div>
                    </div>
                </td>
                <td style="vertical-align:top">
                    <div class="row">
                        <div class="label"><spring:message code="clinicalStaff.label.phone"/>:</div>
                        <div class="value">${command.phoneNumber} </div>
                    </div>
                    <div class="row">
                        <div class="label"><spring:message code="clinicalStaff.label.email_address"/>:</div>
                        <div class="value">${command.emailAddress} </div>
                    </div>
                    <div class="row">
                        <div class="label"><spring:message code="clinicalStaff.label.identifier"/>:</div>
                        <div class="value">${command.nciIdentifier} </div>
                    </div>
                </td>
            </tr>
        </table>
    </chrome:division>
    <c:if test="${command.user.username ne null}">
        <chrome:division title="clinicalStaff.division.user_account">
            <table border="0" style="width:100%">
                <tr>
                    <td>

                        <div class="row">
                            <div class="label"><spring:message code="participant.label.username"/>:</div>
                            <div class="value">${command.user.username} </div>
                        </div>
                        <div class="row">
                            <div class="label"><spring:message code="participant.label.password"/>:</div>
                            <div class="value">******</div>
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
                        <c:forEach items="${command.organizationClinicalStaffs}"
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
                             href="searchClinicalStaff"/></td>
            <td>

        </tr>
    </table>


</div>
</body>
</html>