<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<chrome:box title="Report">
    <table>
        <tr>
            <td>
                <b>Group by:</b>
                <select id="groupby" onchange="reportResults();">
                    <option <c:if test="${group=='week'}">selected</c:if>>Week</option>
                    <option <c:if test="${group=='month'}">selected</c:if>>Month</option>
                    <option <c:if test="${group=='cycle'}">selected</c:if>>Cycle</option>
                </select>
            </td>
            <c:if test="${fn:length(allAttributes)>1}">
                <td>
                    <b>&nbsp;&nbsp;&nbsp;&nbsp;Display:</b>&nbsp;<c:forEach items="${allAttributes}" var="attribute">
                    <input type="checkbox"
                           <c:if test="${fn:contains(selectedAttributes,attribute)}">checked="true"</c:if>
                           name="attribute"
                           value="${attribute}"
                           onclick="updateChart(this,'${group}');">${attribute}&nbsp;&nbsp;
                </c:forEach>
                    <br/>
                </td>
            </c:if>
        </tr>
    </table>

    </div>
    ${worstResponseChartImageMap}
    <chrome:division title="Average Participant Reported Responses vs. Time for ${symptom} symptom ( Worst responses)"/>
    <br/>

    <div align="center">
        <img src="../../servlet/DisplayChart?filename=${worstResponseChartFileName}" width=700 height=400 border=0
             usemap="#${worstResponseChartFileName}"/>
    </div>
    <br/>
    <c:forEach items="${selectedAttributes}" var="attribute">
        <chrome:division title="${attribute}"/>
        <br/>
        <div align="center">
            <c:choose>
                <c:when test="${attribute eq 'Frequency'}">
                    <img src="../../servlet/DisplayChart?filename=${FrequencyStackedBarChartFileName}" width="700"
                         height="400"
                         border="0"/>
                </c:when>
                <c:when test="${attribute eq 'Severity'}">
                    <img src="../../servlet/DisplayChart?filename=${SeverityStackedBarChartFileName}" width="700"
                         height="400"
                         border="0"/>
                </c:when>
                <c:when test="${attribute eq 'Interference'}">
                    <img src="../../servlet/DisplayChart?filename=${InterferenceStackedBarChartFileName}" width="700"
                         height="400"
                         border="0"/>
                </c:when>
                <c:when test="${attribute eq 'Present/Not present'}">
                    <img src="../../servlet/DisplayChart?filename=${PresentNotpresentStackedBarChartFileName}"
                         width="700" height="400"
                         border="0"/>
                </c:when>
                <c:when test="${attribute eq 'Amount'}">
                    <img src="../../servlet/DisplayChart?filename=${AmountStackedBarChartFileName}" width="700"
                         height="400"
                         border="0"/>
                </c:when>
            </c:choose>
                <%--usemap="#${stackedBarChartFileName}"/>--%>
        </div>
        <br/>

    </c:forEach>
</chrome:box>
