<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<tr id="tr_condition_${ruleId}_${ruleConditionIndex}">
    <td>
        <c:if test="${ruleIndex>0}"><b>OR&nbsp;</b>
        </c:if><input type="hidden" name="conditions_${ruleId}" value="${ruleConditionIndex}"/>
    </td>
    <td>
        ${condition.proCtcQuestionType.displayName}
        <input type="hidden" name="questiontype_${ruleId}_${ruleConditionIndex}"
               value="${condition.proCtcQuestionType.code}"/>
    </td>
    <td>
        ${condition.notificationRuleOperator.displayName}
        <input type="hidden" name="operator_${ruleId}_${ruleConditionIndex}"
               value="${condition.notificationRuleOperator.code}"/>
    </td>
    <td>
        ${condition.proCtcQuestionType.validValues[(condition.threshold)]}
        <input type="hidden" name="threshold_${ruleId}_${ruleConditionIndex}"
               value="${condition.threshold}"/>
    </td>
    <td>
        <tags:button icon="x" color="red" size="small" markupWithTag="a" value=""
                     onclick="deleteCondition('${ruleId}','${ruleConditionIndex}');"/>
    </td>
</tr>
