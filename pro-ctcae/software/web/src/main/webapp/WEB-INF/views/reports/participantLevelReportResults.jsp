<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>

</head>
<body>
<span id="attribute-help-content" class="hint" style="display: none;">
    <table class="widget" cellspacing="0" width="100%" align="center">
        <tr>
            <td colspan="6" align="right">
                <a href="javascript:hideHelp();">X</a>
            </td>
        </tr>
        <tr>
            <td class="header-top"></td>
            <c:forEach begin="0" end="4" step="1" var="index">
                <td class="header-top">${index}</td>
            </c:forEach>
        </tr>
        <c:forEach items="${questionTypes}" var="questionType">
            <tr>
                <td class="header-top">${questionType}</td>
                <c:forEach items="${questionType.validValues}" var="validValue">
                    <td class="help-values">${validValue}</td>
                </c:forEach>
            </tr>

        </c:forEach>
    </table>
</span>
<chrome:box title="Report - ${participant.displayName}">
    <c:choose>
        <c:when test="${fn:length(dates) > 0}">
            <tags:instructions code="participant.report.result.instructions"/>
            <div align="right">
                <a href="<c:url value='/pages/reports/participantCarePdf'/>" target="_blank">
                    <img src="/proctcae/images/table/pdf.gif" alt="pdf"/>
                </a> |
                <a href="<c:url value='/pages/reports/participantCareExcel'/>" target="_blank">
                    <img src="/proctcae/images/table/xls.gif" alt="xls"/>
                </a>
            </div>
            <table class="widget" cellspacing="0">
                <col/>
                <tr>
                    <td class="header-top">Symptom</td>
                    <td class="header-top">Attribute<img alt="Help" src="/proctcae/images/q.gif"
                                                         onclick="$('attribute-help-content').style.display='inline'"/>
                    </td>
                    <c:forEach items="${dates}" var="date">
                    <td class="header-top">${date}</td>
                    </c:forEach>
                    <c:forEach items="${resultsMap}" var="symptomMap">
                        <c:set var="flag" value="false"></c:set>
                    <c:forEach items="${symptomMap.value}" var="careResults">
                <tr>
                    <td class="subcategory-name">
                        <c:if test="${flag eq false}">
                            <%--<a href="javascript:getChart('${symptomMap.key.id}')" class="link">${symptomMap.key.term}</a>--%>
                            <a href="javascript:getChart('${symptomMap.key[0]}')" class="link">${symptomMap.key[1]}</a>
                            <c:set var="flag" value="true"></c:set>
                        </c:if>
                    </td>
                    <td class="actual-question">
                            ${fn:toUpperCase(careResults.key.proCtcQuestionType.displayName)}
                    </td>
                    <c:forEach items="${careResults.value}" var="value">
                        <td class="data displayOrder${value.displayOrder}">
                                ${value.value}
                        </td>
                    </c:forEach>
                </tr>
                </c:forEach>
                </c:forEach>
            </table>
        </c:when>
        <c:otherwise>
            There is no data for this participant.
        </c:otherwise>
    </c:choose>
</chrome:box>
</body>
</html>