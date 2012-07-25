<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@attribute name="rule" type="gov.nih.nci.ctcae.core.domain.rules.NotificationRule" required="true" %>
<%@attribute name="ruleIndex" required="true" %>

<chrome:box title="Rule ${ruleIndex+1} ">
    <div style="margin-left:5em">
        <table>
            <tr>
                <td style="vertical-align:top">
                    <b>Symptom(s):</b>
                </td>
                <td>
                    <c:forEach items="${rule.notificationRuleSymptoms}" var="symptom" varStatus="status">
                        <input type="hidden" name="symptoms_${rule.id}" value="${symptom.proCtcTerm.id}">
                        ${symptom.proCtcTerm.term}<br/>
                    </c:forEach>
                    <br/>
                </td>
            </tr>
            <tr>
                <td style="vertical-align:top">
                    <b>Condition(s): </b>
                </td>
                <td>
                    <c:forEach items="${rule.notificationRuleConditions}" var="condition" varStatus="status">
                        <c:choose>
                            <c:when test="${status.index == 0 }">
                                <b>If</b>
                            </c:when>
                            <c:otherwise>
                                <b>Or</b>
                            </c:otherwise>
                        </c:choose>
                        <input type="hidden" name="conditions_${rule.id}" value="${status.index}"/>
                        ${condition.proCtcQuestionType.displayName}
                        <input type="hidden" name="questiontype_${rule.id}_${status.index}"
                               value="${condition.proCtcQuestionType.code}"/>
                        ${condition.notificationRuleOperator.displayName}
                        <input type="hidden" name="operator_${rule.id}_${status.index}"
                               value="${condition.notificationRuleOperator.code}"/>
                        ${condition.proCtcQuestionType.validValues[(condition.threshold)]}
                        <input type="hidden" name="threshold_${rule.id}_${status.index}"
                               value="${condition.threshold}"/>
                        <br/>
                    </c:forEach>
                </td>
            </tr>
            <tr>
                <td style="vertical-align:top">
                    <b>Notify: </b>
                </td>
                <td>
                    <c:forEach items="${rule.notificationRuleRoles}" var="notification" varStatus="status">
                        <c:if test="${status.index > 0}">, </c:if>
                        <input type="hidden" name="notifications_${rule.id}"
                               value="${notification.role.code}"/>${notification.role.screenText}
                    </c:forEach>
                </td>
            </tr>
        </table>
        <br/>
    </div>
</chrome:box>
