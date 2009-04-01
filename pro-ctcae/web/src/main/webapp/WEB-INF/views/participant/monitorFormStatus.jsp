<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>


<html>
<head>
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
            background-color: #cccccc;

        }

        td.header-top {
            border-top: 1px solid #77a9ff;
            border-bottom: 1px solid #77a9ff;
            border-right: 1px solid #77a9ff;
            font-weight: bold;
            text-align: center;
            background-color: #cccccc;
        }

        #formStatusTable {
            text-align: center;
            overflow-x: scroll;
        }

        a.nolink {
            text-decoration: none;
            cursor: default;
        }
    </style>
</head>
<body>
<div id="formStatusTable">
    <c:forEach items="${crfStatusMap}" var="siteCrfStatus">
        <chrome:division title="${siteCrfStatus.key.organization.name}"/>

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
            <c:forEach items="${siteCrfStatus.value}" var="crfStatus">
                <tr>
                    <td class="data-left">
                            ${crfStatus.key.displayName}
                    </td>
                    <c:forEach items="${crfStatus.value}" var="studyParticipantCrfSchedule">
                        <td class="data ${studyParticipantCrfSchedule.status.displayName}">
                                <%--onmouseover="showTip('${studyParticipantCrfSchedule.cycleNumber}','${studyParticipantCrfSchedule.cycleDay}')">--%>
                            <c:choose>
                                <c:when test="${studyParticipantCrfSchedule.status.displayName eq 'Completed'}">
                                    <img src="../../images/blue/${studyParticipantCrfSchedule.status.displayName}.png"/>
                                    <a href="javascript:completedForm(${studyParticipantCrfSchedule.id})"
                                       title="Cycle ${studyParticipantCrfSchedule.cycleNumber}, Day ${studyParticipantCrfSchedule.cycleDay}">
                                            ${studyParticipantCrfSchedule.status.displayName}
                                    </a>
                                </c:when>
                                <c:otherwise>

                                    <img src="../../images/blue/${studyParticipantCrfSchedule.status.displayName}.png"/>
                                    <a class="nolink"
                                       title="Cycle ${studyParticipantCrfSchedule.cycleNumber}, Day ${studyParticipantCrfSchedule.cycleDay}">
                                            ${studyParticipantCrfSchedule.status.displayName}
                                    </a>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </table>
        <br/>

    </c:forEach>
    <input type="hidden" id="pgStartDateNext" value='<tags:formatDate value="${pgStartNext}"/>'/>
    <input type="hidden" id="pgStartDatePrev" value='<tags:formatDate value="${pgStartPrev}"/>'/>


</div>
<br>

</body>
</html>