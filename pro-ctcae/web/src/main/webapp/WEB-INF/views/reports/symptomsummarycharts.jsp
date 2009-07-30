<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="reports" tagdir="/WEB-INF/tags/reports" %>

<chrome:box title="Report">
    <table>
        <c:if test="${fn:length(allAttributes)>1}">
            <tr>
                <td>
                    <b>Display </b>&nbsp;<c:forEach items="${allAttributes}" var="attribute">
                    <input type="checkbox"
                           <c:if test="${fn:contains(selectedAttributes,attribute)}">checked="true"</c:if>
                           name="attribute"
                           value="${attribute}"
                           onclick="updateChart(this);">${attribute}&nbsp;&nbsp;
                </c:forEach>
                </td>
            </tr>
        </c:if>
        <reports:displayarms arms="${arms}" selectedArms="${selectedArms}" name="armPop"/>
    </table>
    <br/>
    <br/>

    ${worstResponseChartImageMap}
    <chrome:division title="Participant reported responses for ${symptom} symptom (Worst responses)"/>
    <br/>

    <div align="center">
        <img src="../../servlet/DisplayChart?filename=${worstResponseChartFileName}" width=700 height=400 border=0
             usemap="#${worstResponseChartFileName}"/>
    </div>
    <br/>
</chrome:box>
