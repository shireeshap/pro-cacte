<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="participantReport" tagdir="/WEB-INF/tags/reports" %>

<html>
<head>
	<script type="text/javascript">
		Event.observe(window, "load", function () {
			pagination = new YAHOO.widget.Paginator({
			    rowsPerPage : 5,
			    totalRecords: 15,
			    containers  : 'navigation'
			});
			pagination.render();
		});
	</script>
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
        	<div id="nav" class="yui-skin-sam">
	            <table width="100%">
		            <tr>
		                <td width="72%">
		                    <div id="navigation" class="yui-pg-container">
	 						</div>
		                </td>
		                <td width="28%" align="right">
		                </td>
		            </tr>
	        	</table>
			</div>
            <div align="right">
                <a href="<c:url value='/pages/reports/participantCarePdf'/>" target="_blank">
                    <img src="/proctcae/images/table/pdf.gif" alt="pdf"/>
                </a> |
                <a href="<c:url value='/pages/reports/participantCareExcel'/>" target="_blank">
                    <img src="/proctcae/images/table/xls.gif" alt="xls"/>
                </a>
            </div>
			
			<c:forEach items="${bundledResultMap}" var="pageResults">
				<participantReport:participantLevelReportResult page="${pageResults.key}" resultsMap="${pageResults.value}" dates="${bundledDates[pageResults.key]}"/>
			</c:forEach>
			
			<input id="totalPageCount" type="hidden" value="${totalPageCount}" />
			<input id="recordsPerPage" type="hidden" value="${recordsPerPage}" />
        </c:when>
        <c:otherwise>
            There is no data for this participant.
        </c:otherwise>
    </c:choose>
</chrome:box>

</body>
</html>
