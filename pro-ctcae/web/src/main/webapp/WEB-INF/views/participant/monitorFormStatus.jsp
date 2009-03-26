<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<html>
<head><title></title>
    <style type="text/css">
        table.widget {
            border-left: 1px solid #C3D9FF;
            border-bottom: 1px solid #C3D9FF;
            width: 100%;
            font-size: small;
        }

        td.data {
            border-bottom: 1px solid #77a9ff;
            border-right: 1px solid #77a9ff;
            font-size: small;
        }

        td.data-left {
            border-bottom: 1px solid #77a9ff;
            border-left: 1px solid #77a9ff;
            border-right: 1px solid #77a9ff;
            font-weight: bold;
            white-space: nowrap;
        }

        td.header-top {
            border-top: 1px solid #77a9ff;
            border-bottom: 1px solid #77a9ff;
            border-right: 1px solid #77a9ff;
            font-weight: bold;
            text-align: center;
            background-color:#cccccc;
        }

        #formStatusTable {
            text-align: center;
        }

        td.Scheduled {
            /*background-color: #0051fc;*/
            /*color: white;*/
        }

        td.In-progress {
            /*background-color: #ff9900;*/
            /*color: white;*/
        }

        td.Completed {
            /*background-color: #00cc00;*/
            /*color: white;*/
        }

        td.Past due {
            /*background-color: red;*/
        }

    </style>
</head>
<body>
<div id="formStatusTable">
    <table class="widget" cellspacing="0">
        <tr>
            <td class="header-top"><spring:message code="schedulecrf.label.participant"/>
            </td>
            <c:forEach items="${calendar}" var="date">
                <td class="header-top">
                        ${date}
                </td>
            </c:forEach>
        </tr>
        <c:forEach items="${crfStatusMap}" var="crfStatus">
            <tr>
                <td class="data-left">
                        ${crfStatus.key.displayName}
                </td>
                <c:forEach items="${crfStatus.value}" var="studyParticipantCrfSchedule">
                    <td class="data ${studyParticipantCrfSchedule.status.displayName}">
                        <img src="../../images/blue/${studyParticipantCrfSchedule.status.displayName}.png" alt=""/>&nbsp;${studyParticipantCrfSchedule.status.displayName}
                        ${studyParticipantCrfSchedule.cycleNumber}
                    </td>
                </c:forEach>
            </tr>
        </c:forEach>
        <tr>
            <td>&nbsp;</td>
        </tr>
    </table>
    <input type="hidden" id="pgStartDateNext" value='<tags:formatDate value="${pgStartNext}"/>'/>
    <input type="hidden" id="pgStartDatePrev" value='<tags:formatDate value="${pgStartPrev}"/>'/>


</div>
<br>

</body>
</html>