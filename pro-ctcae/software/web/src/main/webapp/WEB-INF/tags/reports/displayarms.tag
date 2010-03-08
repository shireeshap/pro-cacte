<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@attribute name="arms" required="true" type="java.util.List" %>
<%@attribute name="colspan" %>
<%@attribute name="selectedArms" required="true" type="java.util.HashSet" %>
<%@attribute name="name" %>
<%@attribute name="resetPopUp" %>
<%@attribute name="showChartType" %>

<c:if test="${name eq null}">
    <c:set var="name" value="arm"/>
</c:if>
<c:set var="javascript" value="reportResults();"/>
<c:if test="${resetPopUp == 'true'}">
    <c:set var="javascript" value="resetPopUpFlagAndCallResults();"/>
</c:if>

<c:set var="numOfSelectedArms" value="0"/>
<td>
    <c:if test="${fn:length(arms) > 0}">
        <div class="row">
            <div class="label">Arms:</div>
            <c:forEach items="${arms}" var="arm">
                <c:set var="checked" value=""/>
                <c:forEach items="${selectedArms}" var="selectedArm">
                    <c:if test="${selectedArm eq arm.id}">
                        <c:set var="checked" value="checked='true'"/>
                        <c:set var="numOfSelectedArms" value="${numOfSelectedArms+1}"/>
                    </c:if>
                </c:forEach>
                <div style="margin-left:130px;">
                    <input type="checkbox" name="${name}" value="${arm.id}" ${checked}
                           onclick="${javascript}"/> ${arm.title}
                </div>
            </c:forEach>
        </div>
    </c:if>
</td>
<c:if test="${showChartType ne 'false'}">
    <td style="vertical-align:top;">
        <div id="chartTypeDiv" <c:if test="${numOfSelectedArms < 2}"> style="display:none"</c:if> class="row">
            <div class="label">Chart type</div>
            <div class="value">
                <input type="radio" name="chartType" value="bar" onclick="javascript:reportResults();"
                       <c:if test="${chartType ne 'line'}">checked="true"</c:if>>Bar
                chart
                <input type="radio" name="chartType" value="line" onclick="javascript:reportResults();"
                       <c:if test="${chartType eq 'line'}">checked="true"</c:if>>Scatter chart
            </div>
        </div>
    </td>
</c:if>