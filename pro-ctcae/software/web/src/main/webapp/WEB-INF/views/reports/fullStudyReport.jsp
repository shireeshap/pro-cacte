<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<html>
<body>
	<br><br>
	<div id="downloadExcelReport">
		<tags:message code="reports.fullStudyLevelReport.download"/>
	</div>
	<a href="<c:url value='/pages/reports/studyLevelFullReportExcel'/>" target="_blank">
                <img src="/proctcae/images/table/xls.gif" alt="xls"/>
    </a>
</body>
</html>