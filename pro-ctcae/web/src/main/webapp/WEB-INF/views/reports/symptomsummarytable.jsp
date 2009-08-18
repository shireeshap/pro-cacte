<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="reports" tagdir="/WEB-INF/tags/reports" %>
<style type="text/css">
    table.report {
        width: 90%;
        border: 1px #999999 solid;
    }

    table.report td {
        border-left: 1px #cccccc solid;
        border-top: 1px #eee solid;
        text-align: center;
        padding: 2px 5px;

    }

    table.report td.right {
        text-align: right;
        padding-left: 5px;
    }

    table.report td.bottom {
        border-top: 2px #666 solid;
    }

    table.report td.header {
        background-color: #cccccc;
        font-weight: bold;
    }

    table.report td.left {
        text-align: left;
        padding-right: 5px;
    }
</style>
<chrome:box title="Report">
    <table>
        <reports:displayarms arms="${arms}" selectedArms="${selectedArms}" resetPopUp="true"/>
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
        </tr>
        <c:forEach items="${questionTypes}" var="questionType">
            <tr>
                <td class="right">${questionType.displayName}:</td>
                <td></td>
                <c:forEach items="${questionType.validValues}" var="validValue">
                    <td>${validValue}</td>
                </c:forEach>
            </tr>
        </c:forEach>
        <tr>
            <td class="left bottom header">Item</td>
            <td class="bottom header">N</td>
            <c:forEach begin="0" end="4" step="1">
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
                        <c:if test="${fn:length(attribute.value)<5}">
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
