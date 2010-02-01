<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tr id="tr_condition_${ruleId}_${ruleConditionIndex}">
    <td align="left">
        <c:if test="${ruleConditionIndex>0}"><b>OR&nbsp;</b></c:if>
        <input type="hidden" name="conditions_${ruleId}" value="${ruleConditionIndex}"/>
    </td>
    <td align="left">
        <tags:renderSelect noForm="true" name="questiontype_${ruleId}_${ruleConditionIndex}" options="${questionTypes}"
                           propertyValue="${condition.proCtcQuestionType.code}" doNotshowLabel="true"/>
    </td>
    <td align="left">
        <tags:renderSelect noForm="true" name="operator_${ruleId}_${ruleConditionIndex}" options="${operators}"
                           propertyValue="${condition.notificationRuleOperator.code}" doNotshowLabel="true"/>
    </td>
    <td>
        <select name="threshold_${ruleId}_${ruleConditionIndex}">
            <c:forEach items="${thresholds}" var="item" varStatus="status">
                <c:choose>
                    <c:when test="${status.index eq condition.threshold}">
                        <option value="${status.index}" selected="selected">${item}</option>
                    </c:when><c:otherwise>
                    <option value="${status.index}">${item}</option>
                </c:otherwise>
                </c:choose>
            </c:forEach>
        </select>
    </td>
    <td>
        <tags:button icon="x" color="red" size="small" markupWithTag="a" value=""
                     onclick="deleteCondition('${ruleId}','${ruleConditionIndex}');"/>
    </td>
</tr>
<script type="text/javascript">

    var thresholds_${ruleConditionIndex} = new Array();
    <c:forEach items="${questionTypes}" var="qType" varStatus="status">
    thresholds_${ruleConditionIndex}[${status.index}] = new Array();
    <c:forEach items="${qType.validValues}" var="vValue" varStatus="vStatus">
    thresholds_${ruleConditionIndex}[${status.index}][${vStatus.index}] =
    </c:forEach>
    </c:forEach>
</script>