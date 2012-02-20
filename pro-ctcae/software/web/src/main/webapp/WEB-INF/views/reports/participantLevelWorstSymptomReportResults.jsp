<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>

</head>
<body>

<chrome:box title="Report - ${participant.displayName}">
    <c:choose>
        <c:when test="${fn:length(resultsMap) > 0}">
        	<br />
	        <div class="summaryvalue">
	                &nbsp;The report has been generated. You can download it to PDF format by 
	                <a href="<c:url value='/pages/reports/participantCarePdf?rt=worstSymptom'/>" target="_blank">
	                   clicking here. <img src="/proctcae/images/table/pdf.gif" alt="pdf"/>
	                </a>
	        </div>
	         <br />
        </c:when>
        <c:otherwise>
            There is no data for this participant.
        </c:otherwise>
    </c:choose>
</chrome:box>
</body>
</html>
