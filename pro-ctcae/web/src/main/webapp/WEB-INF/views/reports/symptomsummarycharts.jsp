<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>

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
        <c:if test="${fn:length(arms)>1}">
            <tr>
                <td colspan="${colNum}"><b>Arms to compare </b>
                    <c:forEach items="${arms}" var="arm">
                        <input type="checkbox" name="armPop" value="${arm.id}"
                               <c:if test="${fn:contains(selectedArms,arm.id)}">checked="true"</c:if>
                               onclick="reportResults();"/>${arm.title}
                    </c:forEach>
                </td>
            </tr>
        </c:if>
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
