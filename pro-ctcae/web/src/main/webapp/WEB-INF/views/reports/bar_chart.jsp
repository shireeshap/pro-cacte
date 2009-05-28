<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<chrome:box title="Report">
    <c:choose>
        <c:when test="${group =='week'}">
            <tags:button value="Group by month" color="blue" size="small" markupWithTag="a"
                         onclick="studyLevelReportResults('month');"/>
        </c:when>
        <c:when test="${group =='month'}">
            <tags:button value="Group by week" color="blue" size="small" markupWithTag="a"
                         onclick="studyLevelReportResults('week');"/>
        </c:when>
    </c:choose>

    ${imagemap}
    <div align="center">
        <img src="../../servlet/DisplayChart?filename=${filename}" width=700 height=400 border=0 usemap="#${filename}"/>
    </div>
</chrome:box>
