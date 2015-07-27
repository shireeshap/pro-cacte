<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@attribute name="rule" type="gov.nih.nci.ctcae.core.domain.rules.NotificationRule" required="true" %>
<%@ attribute name="ruleIndex" type="java.lang.Integer" required="true" %>
<%@ attribute name="readOnly" type="java.lang.Boolean" required="false" %>
<%@ attribute name="isSite" type="java.lang.Boolean" required="false" %>

<c:set var="readOnly" value="${(isSite && !rule.siteOverRide) || readOnly}"/>
<c:set var="notificationRule" value="${rule}"/>
<input type="hidden" name="ruleIndices" value="${ruleIndex}"/>
<c:set var="margin" value="15em"/>
<div id="rule_div_${ruleIndex}">
    <c:choose>
        <c:when test="${readOnly}">
            <tags:readOnlyRule rule="${rule}" ruleIndex="${ruleIndex}"/>
        </c:when>
        <c:otherwise>
            <chrome:box title="Rule ${ruleIndex+1} " enableDelete="true"
                        deleteParams="deleteRule('${ruleIndex}','${ruleIndex+1}');">
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
                            <tr>
                            <c:choose>
                                <c:when test="${status.index%2 == 0}">
                                    
                                    <td><input type="checkbox" name="symptoms_${ruleIndex}"
                                               value="${symptom.id}" ${checked}>&nbsp;${symptom.term}</td>
                                </c:when>
                                <c:otherwise>
                                    <td><input type="checkbox" name="symptoms_${ruleIndex}"
                                               value="${symptom.id}" ${checked}>&nbsp;${symptom.term}</td>
                                </c:otherwise>
                            </c:choose>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
                <chrome:division title="Attributes"/>
                <div style="margin-left:${margin}">
                    <input type="hidden" name="delete_conditions_${ruleIndex}" id="delete_conditions_${ruleIndex}" value=""/>
                    <table>
                        <c:forEach items="${notificationRule.notificationRuleConditions}" var="condition"
                                   varStatus="status">
                            <tags:formRuleCondition ruleIndex="${ruleIndex}"
                                                    ruleConditionIndex="${status.index}"
                                                    condition="${condition}"
                                                    operators="${operators}"
                                                    questionTypes="${questionTypes}"
                                    />

                        </c:forEach>
                        <tr id="hiddenDivCondition_${ruleIndex}"></tr>
                        <tr>
                            <td></td>
                            <td colspan="3">
                                <c:if test="${!empty questionTypes }">
                                <tags:button icon="add" color="blue" value="Add" size="small"
                                                         onclick="addCondition('${ruleIndex}')"
                                                         markupWithTag="a"/>
                                </c:if>
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
                                        <input type="checkbox" name="notifications_${ruleIndex}"
                                               value="${notification.code}"
                                            ${selected}/>&nbsp;${notification.desc}</td>
                                </c:when>
                                <c:otherwise>
                                    <td><input type="checkbox" name="notifications_${ruleIndex}"
                                               value="${notification.code}"
                                        ${selected}/>&nbsp;${notification.desc}</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                    </table>
                </div>
                <br/>
                <c:choose>
                    <c:when test="${isSite}">
                        <input type="hidden" name="override_${ruleIndex}" value="true"/>
                    </c:when>
                    <c:otherwise>
                        <b>&nbsp;&nbsp;Can these settings be overwritten at the local site level?</b>
                        <select name="override_${ruleIndex}">
                            <option value="true" <c:if
                                    test="${rule.siteOverRide}"> selected </c:if> >Yes
                            </option>
                            <option value="false" <c:if
                                    test="${!rule.siteOverRide}"> selected </c:if>>No
                            </option>
                        </select>
                    </c:otherwise>
                </c:choose>
            </chrome:box>
        </c:otherwise>
    </c:choose>

</div>