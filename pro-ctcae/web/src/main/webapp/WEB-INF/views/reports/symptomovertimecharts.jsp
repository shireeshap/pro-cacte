<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="reports" tagdir="/WEB-INF/tags/reports" %>

<chrome:box title="Report">
    <c:set var="colNum" value="1"/>
    <table>
        <tr>
            <td>
                <b>Group by </b>
                <select id="groupby" onchange="reportResults();">
                    <option <c:if test="${group=='cycle'}">selected</c:if>>Cycle</option>
                    <option <c:if test="${group=='week'}">selected</c:if>>Week</option>
                    <option <c:if test="${group=='month'}">selected</c:if>>Month</option>
                </select>
            </td>
            <c:if test="${fn:length(allAttributes)>1}">
                <c:set var="colNum" value="2"/>
                <td>
                    <b>&nbsp;&nbsp;&nbsp;&nbsp;Display </b>&nbsp;<c:forEach items="${allAttributes}" var="attribute">
                    <input type="checkbox"
                           <c:if test="${fn:contains(selectedAttributes,attribute)}">checked="true"</c:if>
                           name="attribute"
                           value="${attribute}"
                           onclick="updateChart(this);">${attribute}&nbsp;&nbsp;
                </c:forEach>
                    <br/>
                </td>
            </c:if>
        </tr>
        <reports:displayarms arms="${arms}" selectedArms="${selectedArms}"/>
    </table>

    <c:forEach items="${results}" var="charts">
        ${charts[2]}
        <chrome:division title="${charts[0]}"/>
        <br/>

        <div align="center">
            <img src="../../servlet/DisplayChart?filename=${charts[1]}" border=0
                 usemap="#${charts[1]}"/>
        </div>
        <br/>
    </c:forEach>

</chrome:box>
