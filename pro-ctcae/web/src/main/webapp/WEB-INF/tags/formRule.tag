<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@attribute name="proCtcAeRule" type="gov.nih.nci.ctcae.web.rules.ProCtcAERule" required="true" %>
<%@attribute name="ruleIndex" required="true" %>
<%@attribute name="isNew" %>
<div id="rule_div_${ruleIndex}">
    <script type="text/javascript">
        registerme(${ruleIndex});
    </script>

    <chrome:box title="Rule ${ruleIndex + 1}" enableDelete="true" deleteParams="deleteRule(${ruleIndex});">
        <input type="hidden" name="rule" value="${ruleIndex}"/>
        <chrome:division title="Symptoms"/>
        <div class="row">
            <div class="value">
                <table>
                    <tr id="symptomDiv_${ruleIndex}">
                        <td></td>
                        <td>
                            <tags:button icon="add" color="blue" value="Add" size="small"
                                         onclick="addSymptom(${ruleIndex})" markupWithTag="a"/></td>
                    </tr>
                </table>
                <c:forEach items="${proCtcAeRule.symptoms}" var="symptom">
                    <script type="text/javascript">
                        addSymptom(${ruleIndex}, '${symptom}');
                    </script>
                </c:forEach>
            </div>
        </div>
        <chrome:division title="Conditions"/>

        <div class="row">
            <div class="value">
                <table>
                    <tr id="conditionDiv_${ruleIndex}">
                        <td></td>
                        <td colspan="3"><tags:button icon="add" color="blue" value="Add" size="small"
                                                     onclick="addCondition(${ruleIndex})" markupWithTag="a"/></td>
                    </tr>
                </table>
                <c:forEach items="${proCtcAeRule.questiontypes}" var="questiontype" varStatus="status">
                    <script type="text/javascript">
                        addCondition(${ruleIndex}, '${questiontype}', '${proCtcAeRule.operators[status.index]}', '${proCtcAeRule.values[status.index]}');
                    </script>
                </c:forEach>

            </div>
        </div>
        <chrome:division title="Notifications"/>

        <div class="row">
            <div class="value">
                <table>
                    <tr id="notificationDiv_${ruleIndex}">
                        <td></td>
                        <td><tags:button icon="add" color="blue" value="Add" size="small"
                                         onclick="addNotification(${ruleIndex})" markupWithTag="a"/></td>
                    </tr>
                </table>
                <c:forEach items="${proCtcAeRule.notifications}" var="notification">
                    <script type="text/javascript">
                        addNotification(${ruleIndex}, '${notification}');
                    </script>
                </c:forEach>

            </div>
        </div>
        <br/>
        &nbsp;&nbsp;&nbsp;<input type="checkbox" name="override_${ruleIndex}" <c:if
            test="${proCtcAeRule.override=='Y'}"> checked </c:if> />
        <b>Can be overridden at site level</b>
        <br/>
        <br/>
    </chrome:box>
    <c:if test="${isNew == true}">
        <script type="text/javascript">
            initializeRule(${ruleIndex})
        </script>
    </c:if>
    <br>
</div>