<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="reports" tagdir="/WEB-INF/tags/reports" %>

<chrome:box title="Report">
    <table>
        <c:if test="${fn:length(arms)>1}">
            <tr>
                <reports:displayarms arms="${arms}" selectedArms="${selectedArms}"/>
            </tr>
        </c:if>
        <tr>
            <td>
                <div class="row">
                    <div class="label">Group by</div>
                    <div class="value">
                        <select id="groupby" onchange="reportResults();">
                            <option <c:if test="${group=='cycle'}">selected</c:if>>Cycle</option>
                            <option <c:if test="${group=='week'}">selected</c:if>>Week</option>
                            <option <c:if test="${group=='month'}">selected</c:if>>Month</option>
                        </select>
                    </div>
                </div>
            </td>
            <td>
                <c:if test="${fn:length(allAttributes)>1}">
                    <div class="row">
                        <div class="label">Display</div>
                        <div class="value">
                            <c:forEach items="${allAttributes}" var="attribute">
                                <input type="checkbox"
                                       <c:if test="${fn:contains(selectedAttributes,attribute)}">checked="true"</c:if>
                                       name="attribute"
                                       value="${attribute}"
                                       onclick="updateChart(this);">${attribute}&nbsp;&nbsp;
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
            </td>
        </tr>
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
