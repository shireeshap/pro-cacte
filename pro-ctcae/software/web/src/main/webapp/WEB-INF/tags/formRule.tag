<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@attribute name="rule" type="gov.nih.nci.ctcae.core.domain.rules.NotificationRule" required="true" %>
<%@ attribute name="ruleIndex" type="java.lang.Integer" required="true" %>

<c:set var="notificationRule" value="${rule}"/>
<input type="hidden" name="ruleIds" value="${rule.id}"/>
<c:set var="margin" value="15em"/>
<div id="rule_div_${rule.id}">
    <chrome:box title="Rule ${ruleIndex+1} " enableDelete="true"
                deleteParams="deleteRule('${rule.id}','${ruleIndex+1}');">
        <chrome:division title="Symptoms"/>
        <div style="margin-left:${margin}">
            <table>
                <c:forEach items="${crfSymptoms}" var="symptom" varStatus="status">
                    <c:set var="checked" value=""/>
                    <c:forEach items="${notificationRule.notificationRuleSymptoms}" var="mysym">
                        <c:if test="${mysym.proCtcTerm.term eq symptom.term}">
                            <c:set var="checked" value="checked"/>
                        </c:if>
                    </c:forEach>
                    <c:choose>
                        <c:when test="${status.index%2 == 0}">
                            <tr>
                            <td><input type="checkbox" name="symptoms_${rule.id}"
                                       value="${symptom.id}" ${checked}>${symptom.term}</td>
                        </c:when>
                        <c:otherwise>
                            <td><input type="checkbox" name="symptoms_${rule.id}"
                                       value="${symptom.id}" ${checked}>${symptom.term}</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </table>
        </div>
        <chrome:division title="Attributes"/>
        <div style="margin-left:${margin}">
            <input type="hidden" name="delete_conditions_${rule.id}" value=""/>
            <table>
                <c:forEach items="${notificationRule.notificationRuleConditions}" var="condition" varStatus="status">
                    <tr id="tr_condition_${rule.id}_${status.index}">
                        <td>
                            <c:if test="${status.index>0}"><b>OR&nbsp;</b>
                            </c:if><input type="hidden" name="conditions_${rule.id}" value="${status.index}"/>
                        </td>
                        <td>
                                ${condition.proCtcQuestionType.displayName}
                            <input type="hidden" name="questiontype_${rule.id}_${status.index}"
                                   value="${condition.proCtcQuestionType.code}"/>
                        </td>
                        <td>
                                ${condition.notificationRuleOperator.displayName}
                            <input type="hidden" name="operator_${rule.id}_${status.index}"
                                   value="${condition.notificationRuleOperator.code}"/>
                        </td>
                        <td>
                                ${condition.proCtcQuestionType.validValues[(condition.threshold)]}
                            <input type="hidden" name="threshold_${rule.id}_${status.index}"
                                   value="${condition.threshold}"/>
                        </td>
                        <td>
                            <tags:button icon="x" color="red" size="small" markupWithTag="a" value=""
                                         onclick="deleteCondition('${rule.id}','${status.index}');"/>
                        </td>
                    </tr>
                </c:forEach>
                <tr>
                    <td></td>
                    <td colspan="3"><tags:button icon="add" color="blue" value="Add" size="small"
                                                 onclick="addCondition('${ruleId}')"
                                                 markupWithTag="a"/>
                    </td>
                    <td></td>
                </tr>
            </table>
        </div>
        <chrome:division title="Notifications"/>
        <div style="margin-left:${margin}">
            <table>
                <c:forEach items="${notifications}" var="notification" varStatus="status">
                    <c:set var="selected" value=""/>
                    <c:forEach items="${notificationRule.notificationRuleRoles}" var="notify">
                        <c:if test="${notification.code eq notify.role.code}">
                            <c:set var="selected" value="checked"/>
                        </c:if>
                    </c:forEach>
                    <c:choose>
                        <c:when test="${status.index%2 == 0}">
                            <tr>
                            <td>
                                <input type="checkbox" name="notifications_${rule.id}"
                                       value="${notification.code}"
                                    ${selected}/>${notification.desc}</td>
                        </c:when>
                        <c:otherwise>
                            <td><input type="checkbox" name="notifications_${rule.id}"
                                       value="${notification.code}"
                                ${selected}/>${notification.desc}</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>
            </table>
        </div>
        <%--<br/>--%>
        <%--<c:choose>--%>
        <%--<c:when test="${isSite}">--%>
        <%--<input type="hidden" name="override_${ruleId}" value="Y"/>--%>
        <%--</c:when>--%>
        <%--<c:otherwise>--%>
        <%--<b>Can these settings be overwritten at the local site level?</b>--%>
        <%--<select name="override_${ruleId}">--%>
        <%--<option value="Y" <c:if--%>
        <%--test="${proCtcAeRule.override=='Y'}"> selected </c:if> >Yes--%>
        <%--</option>--%>
        <%--<option value="N" <c:if--%>
        <%--test="${proCtcAeRule.override!='Y'}"> selected </c:if>>No--%>
        <%--</option>--%>
        <%--</select>--%>
        <%--</c:otherwise>--%>
        <%--</c:choose>--%>
    </chrome:box>
</div>