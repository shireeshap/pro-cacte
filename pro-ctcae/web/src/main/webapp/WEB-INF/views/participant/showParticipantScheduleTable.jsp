<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<html>
<head>
    <tags:stylesheetLink name="table_menu"/>
    <style type="text/css">
        table.widget {
            border: 1px solid #eaeaea;
            border-collapse: collapse;
        }

        table.widget col {
            width: 200px;
        }

        td.data {
            border: 1px solid #eaeaea;
            background-color: #D5D5D5;
            white-space: nowrap;
            text-align: center;

        }

        td.header-top {
            border: 1px solid #eaeaea;
            font-weight: bold;
            text-align: center;
            background-color: #cccccc;
            width: 100px;
        }


    </style>

</head>
<body>
<chrome:box title="Schedules">

    <table class="widget" cellspacing="0" width="100%">
        <tr>
            <td style="width:2px;background-color:#cccccc;border:1px solid #eaeaea;">

            </td>
            <td class="header-top">
                Schedules
            </td>
            <td class="header-top">
                Status
            </td>
            <td class="header-top">
                Treating Physician
            </td>
            <td class="header-top">
                Research Nurse
            </td>
        </tr>

        <c:forEach items="${scheduledCrfs}" var="schedule" varStatus="status">
            <tr id="details_row_${status.index}" onmouseover="highlightrow('${status.index}');"
                onmouseout="removehighlight('${status.index}');">
                <td align="right">
                    <div id="img_${status.index}" class="indIcon"
                         onclick="showPopUpMenu('${status.index}', '${schedule.id}',-105,-130)">
                        <img src="../../images/menu.png" alt=""/>
                    </div>
                </td>
                <td align="center">
                  <tags:formatDate value="${schedule.startDate}"/>
                </td>
                <td align="center">
                        ${schedule.status}
                </td>
                <td align="center">
                        ${schedule.studyParticipantCrf.studyParticipantAssignment.treatingPhysician.studyOrganizationClinicalStaff.organizationClinicalStaff.displayName}
                </td>
                <td align="center">
                        ${schedule.studyParticipantCrf.studyParticipantAssignment.researchNurse.studyOrganizationClinicalStaff.organizationClinicalStaff.displayName}
                </td>
            </tr>
        </c:forEach>
    </table>

</chrome:box>


</body>
</html>       