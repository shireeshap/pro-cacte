<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
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
            <td class="header-top">0</td>
            <td class="header-top">1</td>
            <td class="header-top">2</td>
            <td class="header-top">3</td>
            <td class="header-top">4</td>
        </tr>
        <c:forEach items="${questionTypes}" var="questionType">
            <tr>
                <td class="header-top">
                        ${questionType}
                </td>
                <c:forEach items="${questionType.validValues}" var="validValue">
                    <td class="help-values">
                            ${validValue}
                    </td>
                </c:forEach>
            </tr>

        </c:forEach>
    </table>
</span>
<c:if test="${fn:length(table)==0}">
    No results found
</c:if>
<c:forEach items="${table}" var="organizationTable">
    <chrome:box title="${organizationTable.key.displayName}">

        <div align="right">
            <a href="<c:url value='/pages/reports/studyLevelReportExcel'/>" target="_blank">
                <img src="/proctcae/images/table/xls.gif" alt="xls"/>
            </a>
        </div>

        <c:forEach items="${organizationTable.value}" var="participantTable">
            <chrome:division
                    title="Participant: ${participantTable.key.displayName}"/>
            <div id="careResultsTable">
                    ${participantTable.value}
                <br/>
            </div>
        </c:forEach>


    </chrome:box>
</c:forEach>
</body>
</html>
