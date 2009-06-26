<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<chrome:box title="Report">
    <c:choose>
        <c:when test="${group =='week'}">
            <tags:button value="Group by month" color="blue" size="small" markupWithTag="a"
                         onclick="reportResults('month');"/>
        </c:when>
        <c:when test="${group =='month'}">
            <tags:button value="Group by week" color="blue" size="small" markupWithTag="a"
                         onclick="reportResults('week');"/>
        </c:when>
    </c:choose>

    <c:if test="${fn:length(allAttributes)>1}">
        <div align="center">
            <c:forEach items="${allAttributes}" var="attribute">

                <input type="checkbox"
                       <c:if test="${fn:contains(selectedAttributes,attribute)}">checked="true"</c:if> name="attribute"
                       value="${attribute}"
                       onclick="updateChart(this);">${attribute}&nbsp;&nbsp;
            </c:forEach>

        </div>
        <br/>
    </c:if>

    ${worstResponseChartImageMap}
    <div align="center">
        <img src="../../servlet/DisplayChart?filename=${worstResponseChartFileName}" width=700 height=400 border=0
             usemap="#${worstResponseChartFileName}"/>
    </div>
    ${allResponseChartImageMap}
    <br/>
    <chrome:division title=" "/>
    <br/>

    <div align="center">
        <img src="../../servlet/DisplayChart?filename=${allResponseChartFileName}" width=700 height=400 border=0
             usemap="#${allResponseChartFileName}"/>
    </div>
    <br/>
</chrome:box>
