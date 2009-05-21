<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
            border: 1px solid #eaeaea;
            border-collapse: collapse;
            width: 90%;
        }

        table.widget col {
            width: 200px;
        }

        td.data {
            border: 1px solid #eaeaea; /*background-color: #D5D5D5;*/
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
<chrome:box title="Report">
    <table class="widget" cellspacing="0" align="center">
        <col/>
        <tr>
            <td class="header-top">
                Participant
            </td>
            <td class="header-top">
                Response date
            </td>
            <td class="header-top">
                Response
            </td>
            <c:forEach items="${results}" var="lineItem">
                <c:set var="schedule" value="${lineItem[0]}"/>
                <c:set var="participant" value="${lineItem[1]}"/>
        <tr>
            <td class="data">
                    ${participant.displayName}
            </td>
            <td class="data">
                <a href="javascript:showResponses('${schedule.id}')"><tags:formatDate value="${schedule.startDate}"/></a>
            </td>
            <td class="data">
                    ${response}
            </td>
        </tr>
        </c:forEach>
    </table>
    <br/>
</chrome:box>
<tags:button value="Back" color="blue" icon="back" size="medium" markupWithTag="a"
             onclick="studyLevelReportResults();"/>
</body>
</html>



