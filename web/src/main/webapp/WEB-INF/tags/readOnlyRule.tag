<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@attribute name="proCtcAeRule" type="gov.nih.nci.ctcae.core.rules.ProCtcAERule" required="true" %>
<%@attribute name="ruleIndex" required="true" %>

<div id="rule_div_${ruleIndex}">
    <script type="text/javascript">
        registerme(${ruleIndex});
    </script>

    <c:if test="${proCtcAeRule.override == 'Y'}">
        <%--<c:set var="Customizable" value="(Customizable)"/>--%>
    </c:if>
    <chrome:box title="Rule ${ruleIndex + 1} ${Customizable}">
        <input type="hidden" name="rule" value="${proCtcAeRule.ruleId}"/>
        <input type="hidden" name="update_${proCtcAeRule.ruleId}" value="N"/>
        <table>
            <tr>
                <td style="vertical-align:top">
                    <b>Symptom(s):</b>
                </td>
                <td>
                    <c:forEach items="${proCtcAeRule.symptoms}" var="symptom" varStatus="status">
                        <c:if test="${status.index > 0}"> Or </c:if>
                        <input type="hidden" name="symptoms_${ruleIndex}_${status.index}"
                               value="${symptom}"/> <u>${symptom}</u>
                    </c:forEach>
                </td>
            </tr>
            <tr>
                <td style="vertical-align:top">
                    <b>Condition(s): </b>
                </td>
                <td>
                    <c:forEach items="${proCtcAeRule.questiontypes}" var="questiontype" varStatus="status">
                        <c:choose>
                            <c:when test="${status.index == 0 }">
                                <b>If</b>
                            </c:when>
                            <c:otherwise>
                                <b>And</b>
                            </c:otherwise>
                        </c:choose>

                        <input type="hidden" name="questiontypes_${ruleIndex}_${status.index}"
                               value="${questiontype}"/> ${questiontype}
                        <input type="hidden" name="operators_${ruleIndex}_${status.index}"
                               value="${proCtcAeRule.operators[status.index]}"/><tags:textForCode
                            codeType="condition" code="${proCtcAeRule.operators[status.index]}"/>
                        <input type="hidden" name="values_${ruleIndex}_${status.index}"
                               value="${proCtcAeRule.values[status.index]}"/>${proCtcAeRule.values[status.index]}
                        <br/>
                    </c:forEach>

                </td>
            </tr>
            <tr>
                <td style="vertical-align:top">
                    <b>Notify: </b>
                </td>
                <td>
                    <c:forEach items="${proCtcAeRule.notifications}" var="notification" varStatus="status">
                        <c:if test="${status.index > 0}">, </c:if>
                        <input type="hidden" name="notifications_${ruleIndex}_${status.index}"
                               value="${notification}"/><tags:textForCode codeType="notify" code="${notification}"/>
                    </c:forEach>
                </td>
            </tr>
        </table>
        <br/>
    </chrome:box>
    <br>
</div>