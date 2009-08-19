<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@attribute name="proCtcAeRule" type="gov.nih.nci.ctcae.core.rules.ProCtcAERule" required="true" %>
<%@attribute name="ruleIndex" required="true" %>
<%@attribute name="isNew" %>
<%@attribute name="isSite" %>
<%@attribute name="siteReadOnlyView" %>
<c:set var="ruleId" value="${proCtcAeRule.ruleId}"/>
<div id="rule_div_${ruleId}">
    <script type="text/javascript">
        registerme('${ruleId}');
    </script>
    <c:choose>
        <c:when test="${siteReadOnlyView || (isSite && proCtcAeRule.override=='N')}">
            <tags:readOnlyRule proCtcAeRule="${proCtcAeRule}" ruleIndex="${ruleIndex}"/>
        </c:when>
        <c:otherwise>
            <chrome:box title="Rule ${ruleIndex+1} " enableDelete="true" deleteParams="deleteRule('${ruleId}');">
                <%--<chrome:box title="Rule ${ruleIndex + 1} " enableDelete="true" deleteParams="deleteRule(${ruleId});">--%>
                <input type="hidden" name="rule" value="${ruleId}"/>
                <chrome:division title="Symptoms"/>
                <div class="row">
                    <div class="value">
                        <table>
                            <tr id="symptomDiv_${ruleId}">
                                <td></td>
                                <td>
                                    <tags:button icon="add" color="blue" value="Add" size="small"
                                                 onclick="addSymptom('${ruleId}')" markupWithTag="a"/>
                                </td>
                            </tr>
                        </table>
                        <c:forEach items="${proCtcAeRule.symptoms}" var="symptom">
                            <script type="text/javascript">
                                addSymptom('${ruleId}', '${symptom}');
                            </script>
                        </c:forEach>
                    </div>
                </div>
                <chrome:division title="Conditions"/>

                <div class="row">
                    <div class="value">
                        <table>
                            <tr id="conditionDiv_${ruleId}">
                                <td></td>
                                <td colspan="3"><tags:button icon="add" color="blue" value="Add" size="small"
                                                             onclick="addCondition('${ruleId}')"
                                                             markupWithTag="a"/>
                                </td>
                            </tr>
                        </table>
                        <c:forEach items="${proCtcAeRule.questiontypes}" var="questiontype" varStatus="status">
                            <script type="text/javascript">
                                addCondition('${ruleId}', '${questiontype}', '${proCtcAeRule.operators[status.index]}', '${proCtcAeRule.values[status.index]}');
                            </script>
                        </c:forEach>
                    </div>
                </div>
                <chrome:division title="Notifications"/>
                <div class="row">
                    <div class="value">
                        <table>
                            <tr id="notificationDiv_${ruleId}">
                                <td></td>
                                <td><tags:button icon="add" color="blue" value="Add" size="small"
                                                 onclick="addNotification('${ruleId}')" markupWithTag="a"/>
                                </td>
                            </tr>
                        </table>
                        <c:forEach items="${proCtcAeRule.notifications}" var="notification">
                            <script type="text/javascript">
                                addNotification('${ruleId}', '${notification}');
                            </script>
                        </c:forEach>

                    </div>
                </div>
                <br/>
                <c:choose>

                    <c:when test="${isSite}">
                        <input type="hidden" name="override_${ruleId}" value="Y"/>
                    </c:when>
                    <c:otherwise>
                        &nbsp;&nbsp;&nbsp;<input type="checkbox" name="override_${ruleId}" value="Y" <c:if
                            test="${proCtcAeRule.override=='Y'}"> checked </c:if> />
                        <b>Can be overridden at site level</b>
                    </c:otherwise>
                </c:choose>
            </chrome:box>
            <c:if test="${isNew == true}">
                <script type="text/javascript">
                    initializeRule('${ruleId}')
                </script>
            </c:if>
            <br>
        </c:otherwise>
    </c:choose>

</div>