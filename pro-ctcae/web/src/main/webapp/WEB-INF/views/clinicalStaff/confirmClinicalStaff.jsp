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
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">

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
            <li id="thirdlevelnav" class="tab selected">
                <div>
                    <a href="createClinicalStaff">Create/Edit Clinical Staff</a>
                </div>
            </li>
            <li id="thirdlevelnav" class="tab">
                <div>
                    <a href="searchClinicalStaff">Search Clinical Staff</a>
                </div>
            </li>
        </ul>
    </div>
</div>
<chrome:flashMessage flashMessage="The Clinical Staff was saved successfully"></chrome:flashMessage>

<chrome:box title="Confirmation">

    <table border="0" style="width:100%">
        <tr>
            <td>

                <div class="row">
                    <div class="label">First name</div>
                    <div class="value">${clinicalStaffCommand.clinicalStaff.firstName} </div>
                </div>
                <div class="row">
                    <div class="label">Middle name</div>
                    <div class="value">${clinicalStaffCommand.clinicalStaff.middleName} </div>
                </div>
                <div class="row">
                    <div class="label">Last name</div>
                    <div class="value">${clinicalStaffCommand.clinicalStaff.lastName} </div>
                </div>
                <div class="row">
                    <div class="label">Clinical Staff identifier</div>
                    <div class="value">${clinicalStaffCommand.clinicalStaff.nciIdentifier} </div>
                </div>
            </td>
            <td style="vertical-align:top">
                <div class="row">
                    <div class="label">Email address</div>
                    <div class="value">${clinicalStaffCommand.clinicalStaff.emailAddress} </div>
                </div>
                <div class="row">
                    <div class="label">Phone</div>
                    <div class="value">${clinicalStaffCommand.clinicalStaff.phoneNumber} </div>
                </div>
                <div class="row">
                    <div class="label">Fax</div>
                    <div class="value">${clinicalStaffCommand.clinicalStaff.faxNumber} </div>
                </div>

            </td>
        </tr>
    </table>
    <c:forEach items="${clinicalStaffCommand.clinicalStaff.clinicalStaffAssignments}" var="clinicalStaffAssignment"
               varStatus="status">
        <chrome:division title="clinicalStaff.division.sites">

            <div class="row">
                <div class="label">Site</div>
                <div class="value">${clinicalStaffAssignment.displayName} </div>
            </div>

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

                    <c:forEach items="${clinicalStaffAssignment.clinicalStaffAssignmentRoles}" var="clinicalStaffAssignmentRole"
                               varStatus="status">

                        <tr>
                            <td style="border-right:none;" width="35%">
                                    ${clinicalStaffAssignmentRole.role}

                            </td>
                            <td style="border-right:none;" width="30%">
                                    ${clinicalStaffAssignmentRole.roleStatus}
                            </td>
                            <td style="border-right:none;" width="30%">
                                <tags:formatDate value="${clinicalStaffAssignmentRole.statusDate}"/>

                            </td>

                            <td style="border-left:none;" width="5%">
                            </td>

                        </tr>
                    </c:forEach>

                </table>

            </div>

        </chrome:division>
    </c:forEach>

</chrome:box>

</body>
</html>