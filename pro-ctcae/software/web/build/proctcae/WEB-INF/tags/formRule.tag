<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@attribute name="proCtcAeRule" type="gov.nih.nci.ctcae.core.rules.ProCtcAERule" required="true" %>
<%@attribute name="ruleIndex" required="true" %>
<%@attribute name="isNew" %>
<%@attribute name="isSite" %>
<%@attribute name="siteReadOnlyView" %>
<%@attribute name="notifications" type="java.util.List" %>

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
                <input type="hidden" name="rule" value="${ruleId}"/>
                <chrome:division title="Symptoms"/>
                <div class="row">
                    <div class="value">
                        <table>
                            <c:forEach items="${crfSymptoms}" var="symptom" varStatus="status">
                                <c:set var="checked" value=""/>
                                <c:forEach items="${proCtcAeRule.symptoms}" var="mysym">
                                    <c:if test="${mysym eq symptom}">
                                        <c:set var="checked" value="checked"/>
                                    </c:if>
                                </c:forEach>
                                <c:choose>
                                    <c:when test="${status.index%2 == 0}">
                                        <tr>
                                        <td><input type="checkbox" name="symptoms_${ruleId}"
                                                   value="${symptom}" ${checked}>${symptom}</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td><input type="checkbox" name="symptoms_${ruleId}"
                                                   value="${symptom}" ${checked}>${symptom}</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </table>
                    </div>
                </div>
                <chrome:division title="Attributes"/>
                <%--<div class="row">--%>
                <%--<div class="value">--%>
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
                <%--</div>--%>
                <%--</div>--%>
                <chrome:division title="Notifications"/>
                <div class="row">
                    <div class="value">
                        <table>
                            <c:forEach items="${notifications}" var="notification" varStatus="status">
                                <c:set var="selected" value=""/>
                                <c:forEach items="${proCtcAeRule.notifications}" var="notify">
                                    <c:if test="${notification.code eq notify}">
                                        <c:set var="selected" value="checked"/>
                                    </c:if>
                                </c:forEach>
                                <c:choose>
                                    <c:when test="${status.index%2 == 0}">
                                        <tr>
                                        <td>
                                            <input type="checkbox" name="notifications_${ruleId}"
                                                   value="${notification.code}"
                                                ${selected}/>${notification.desc}</td>
                                    </c:when>
                                    <c:otherwise>
                                        <td><input type="checkbox" name="notifications_${ruleId}"
                                                   value="${notification.code}"
                                            ${selected}/>${notification.desc}</td>
                                        </tr>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </table>
                    </div>
                </div>
                <br/>
                <c:choose>
                    <c:when test="${isSite}">
                        <input type="hidden" name="override_${ruleId}" value="Y"/>
                    </c:when>
                    <c:otherwise>
                        <b>Can these settings be overwritten at the local site level?</b>
                        <select name="override_${ruleId}">
                            <option value="Y" <c:if
                                    test="${proCtcAeRule.override=='Y'}"> selected </c:if> >Yes
                            </option>
                            <option value="N" <c:if
                                    test="${proCtcAeRule.override!='Y'}"> selected </c:if>>No
                            </option>
                        </select>
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