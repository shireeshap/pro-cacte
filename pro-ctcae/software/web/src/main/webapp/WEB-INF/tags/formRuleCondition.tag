<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<%@ attribute name="ruleId" type="java.lang.Integer" required="true" %>
<%@ attribute name="ruleConditionIndex" type="java.lang.Integer" required="true" %>
<%@ attribute name="questionTypes" type="java.util.List" required="false" %>
<%@ attribute name="operators" type="java.util.List" required="false" %>
<%@ attribute name="condition" type="gov.nih.nci.ctcae.core.domain.rules.NotificationRuleCondition" required="true" %>

<tr id="tr_condition_${ruleId}_${ruleConditionIndex}">
    <td align="left">
        <c:if test="${ruleConditionIndex>0 && showOr}"><b>OR&nbsp;</b></c:if>
        <input type="hidden" name="conditions_${ruleId}" value="${ruleConditionIndex}"/>
    </td>
    <td align="left">
        <tags:renderSelect noForm="true" name="questiontype_${ruleId}_${ruleConditionIndex}" options="${questionTypes}"
                           propertyValue="${condition.proCtcQuestionType.code}" doNotshowLabel="true"
                           onchange="javascript:changeThresholds(this,${ruleId},${ruleConditionIndex});"/>
    </td>
    <td align="left">
        <tags:renderSelect noForm="true" name="operator_${ruleId}_${ruleConditionIndex}" options="${operators}"
                           propertyValue="${condition.notificationRuleOperator.code}" doNotshowLabel="true"/>
    </td>
    <td>
        <select name="threshold_${ruleId}_${ruleConditionIndex}"  id="threshold_${ruleId}_${ruleConditionIndex}">
            <c:forEach items="${condition.proCtcQuestionType.validValues}" var="item" varStatus="status">
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