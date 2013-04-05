<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<html>
<body>
	<br><br>
	<chrome:box title="reports.fullStudyLevelReport.results">
		<div id="downloadExcelReport" style="position:relative; left:25px">
			<br>
			<a href="<c:url value='/pages/reports/studyLevelFullReportExcel'/>" target="_blank">
                <tags:message code="reports.fullStudyLevelReport.download"/> <img src="/proctcae/images/table/xls.gif" alt="xls"/>
    		</a>
    		<br>
		</div>
	</chrome:box>
</body>
</html>