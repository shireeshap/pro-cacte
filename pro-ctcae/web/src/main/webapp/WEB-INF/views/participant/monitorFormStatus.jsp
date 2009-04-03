<%@ page import="java.util.Date" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


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
            white-space: nowrap;
            text-align: center;

        }

        td.data-left {
            border-bottom: 1px solid #77a9ff;
            border-left: 1px solid #77a9ff;
            border-right: 1px solid #77a9ff;
            font-weight: bold;
            white-space: nowrap;
            background-color: #cccccc;
            text-align: center;
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
        /*text-align: center;*/
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
    <table>
        <tr>
            <td>
                Scheduled = <img src="../../images/blue/Scheduled.png"/>  &nbsp;&nbsp;
            </td>
            <td>
                In-progress = <img src="../../images/blue/In-progress.png"/>  &nbsp;&nbsp;
            </td>
            <td>
                Completed = <img src="../../images/blue/Completed.png"/>  &nbsp;&nbsp;
            </td>
            <td>
                Past-due = <img src="../../images/blue/Past-due.png"/>
            </td>
        </tr>
        <tr>
            <td>
                Year = <fmt:formatDate value="${pgStartPrev}" pattern="yyyy"/>
            </td>
        </tr>
    </table>
    <c:forEach items="${crfStatusMap}" var="siteCrfStatus">
        <chrome:division title="${siteCrfStatus.key.organization.name}"/>

        <table class="widget" cellspacing="0">


            <tr>
                <td class="header-top"><spring:message code="schedulecrf.label.participant"/>
                </td>
                <td class="header-top">Start date</td>
                <c:forEach items="${calendar}" var="date">
                    <td class="header-top">
                        <fmt:formatDate value="${date}" pattern="MMM-dd"/>
                    </td>
                </c:forEach>
            </tr>
            <c:forEach items="${siteCrfStatus.value}" var="crfStatus">
                <tr>
                    <td class="data-left">
                            ${crfStatus.key.displayName} [${crfStatus.key.assignedIdentifier}]
                    </td>
                    <td class="data">
                            <%--${crfStatus.value[0]}ss--%>
                            <%----%>
                        <c:set var="startDate"/>
                        <c:forEach items="${crfStatus.value}" var="studyParticipantCrfSchedule">
                            <c:if test="${!empty studyParticipantCrfSchedule}">
                                <c:set var="startDate"
                                       value="${studyParticipantCrfSchedule.startDate}"/>
                            </c:if>
                        </c:forEach>
                        <tags:formatDate value="${startDate}"/>
                    </td>
                    <c:forEach items="${crfStatus.value}" var="studyParticipantCrfSchedule">
                        <td class="data">
                            <c:choose>
                            <c:when test="${studyParticipantCrfSchedule.status.displayName eq 'Completed'}">
                                <a href="javascript:completedForm(${studyParticipantCrfSchedule.id})"
                                   title="Cycle ${studyParticipantCrfSchedule.cycleNumber}, Day ${studyParticipantCrfSchedule.cycleDay}">
                                    <img src="../../images/blue/${studyParticipantCrfSchedule.status.displayName}.png"/>
                                </a>
                            </c:when>
                            <c:otherwise>
                            <c:set var="todaysdate" value="<%= new Date()%>"/>
                            <a class="nolink"
                               title="Cycle ${studyParticipantCrfSchedule.cycleNumber}, Day ${studyParticipantCrfSchedule.cycleDay}">
                                <c:choose>
                                    <c:when test="${todaysdate > studyParticipantCrfSchedule.dueDate && (studyParticipantCrfSchedule.status eq 'Scheduled' || studyParticipantCrfSchedule.status eq 'In-progress')}">
                                        <img src="../../images/blue/Past-due.png"/>
                                    </c:when>
                                    <c:otherwise>
                                        <img src="../../images/blue/${studyParticipantCrfSchedule.status.displayName}.png"/>
                                    </c:otherwise>
                                </c:choose>

                                </c:otherwise>
                                </c:choose>
                            </a>
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
        </table>
        <br/>

    </c:forEach>
    <input type="hidden" id="pgStartDateNext" value='<tags:formatDate value="${pgStartNext}"/>'/>
    <input type="hidden" id="pgStartDatePrev" value='<tags:formatDate value="${pgStartPrev}"/>'/>
    <input type="hidden" id="periodButton" value="${tablePeriod}"/>

</div>
<br>
</body>
</html>