<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="reports" tagdir="/WEB-INF/tags/reports" %>
<chrome:box title="Report">
    <tags:instructions code="reports.symptom.summary.results.instructions"/>
    <table>
        <reports:displayarms arms="${arms}" selectedArms="${selectedArms}" resetPopUp="true" showChartType="false"/>
    </table>
    <chrome:division title="Maximum Grade per Patient"/>
    <table class="report" cellspacing="0" align="center">
        <tr>
            <td class="right">Present/Not Present:</td>
            <td></td>
            <td>No</td>
            <td>Yes</td>
            <td>--</td>
            <td>--</td>
            <td>--</td>
            <td>Not sexually active</td>
            <td>Prefer not to answer</td>
        </tr>
        <c:forEach items="${questionTypes}" var="questionType">
            <tr>
                <td class="right">${questionType.displayName}:</td>
                <td></td>
                <c:forEach items="${questionType.validValues}" var="validValue">
                    <td>${validValue}</td>
                </c:forEach>
                <td>Not sexually active</td>
                <td>Prefer not to answer</td>
            </tr>
        </c:forEach>
        <tr>
            <td class="left bottom header">Item</td>
            <td class="bottom header">N</td>
            <c:forEach begin="0" end="6" step="1">
                <td class="bottom header">N(%)</td>
            </c:forEach>
        </tr>
        <c:forEach items="${results}" var="armData">
            <c:if test="${armData.key ne ''}">
                <tr>
                    <td colspan="7" class="left bottom header">
                        Arm: ${armData.key}
                    </td>
                </tr>
            </c:if>
            <c:forEach items="${armData.value[1]}" var="symptom">
                <c:set var="bottom" value="bottom"/>
                <c:set var="total" value="${armData.value[0]}"/>
                <c:forEach items="${symptom.value}" var="attribute">
                    <tr>
                        <td class="left ${bottom}">
                            <a href="javascript:showChartInPopup(${symptom.key.id})" class="link">
                                    ${symptom.key.term} - ${attribute.key}
                            </a>
                        </td>
                        <td class="${bottom}">
                                ${total}
                        </td>
                        <c:forEach items="${attribute.value}" var="count">
                            <td class="${bottom}">
                                    ${count.value} (<fmt:formatNumber pattern="0.0" value="${(count.value*100)/total}"/>)
                            </td>
                        </c:forEach>
                        <c:if test="${fn:length(attribute.value)<6}">
                            <td class="${bottom}">--</td>
                            <td class="${bottom}">--</td>
                            <td class="${bottom}">--</td>
                        </c:if>
                        <c:set var="bottom" value=""/>
                    </tr>
                </c:forEach>
            </c:forEach>
        </c:forEach>
    </table>
    <br/>

</chrome:box>
