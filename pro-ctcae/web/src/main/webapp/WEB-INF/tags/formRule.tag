<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@attribute name="proCtcAeRule" type="gov.nih.nci.ctcae.web.rules.ProCtcAERule" required="true" %>
<%@attribute name="ruleIndex" required="true" %>
<%@attribute name="crfSymptoms" type="java.util.List" required="true" %>
<%@attribute name="questionTypes" required="true" %>
<%@attribute name="comparisonOptions" required="true" %>
<%@attribute name="comparisonValues" required="true" %>
<%@attribute name="notifications" required="true" %>

<script type="text/javascript">
    registerme(${ruleIndex});
</script>

<chrome:box title="Rule ${ruleIndex + 1}">
    <input type="hidden" name="rule" value="dummy"/>
    <chrome:division title="Symptoms"/>
    <div class="row">
        <div class="value">
            <table>
                <c:forEach items="${proCtcAeRule.symptoms}" var="symptom" varStatus="status">
                    <tr>
                        <td></td>
                        <td>
                            <%--<div id="row">--%>
                                <%--<tags:renderSelect doNotshowLabel="true" noForm="true" options="${crfSymptoms}"--%>
                                                   <%--propertyValue="symptom" id="symptoms_${ruleIndex}_${status.index}"--%>
                                                   <%--name="symptoms_${ruleIndex}_${status.index}"/><tags:button icon="x"--%>
                                                                                                              <%--color="red"--%>
                                                                                                              <%--size="small"--%>
                                                                                                              <%--markupWithTag="a"--%>
                                                                                                              <%--value=""--%>
                                                                                                              <%--onclick="deleteMe(this);"/></div>--%>
                        </td>
                    </tr>
                    <br/>
                </c:forEach>

                <tr id="symptomDiv_${ruleIndex}">
                    <td></td>
                    <td>
                        <tags:button icon="add" color="blue" value="Add" size="small"
                                     onclick="addSymptom(${ruleIndex})" markupWithTag="a"/></td>
                </tr>
            </table>
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
        </div>
    </div>
    <chrome:division title="Notifications"/>

    <div class="row">
        <div class="value">
            <table>
                <tr id="notificationDiv_${ruleIndex}">
                    <td></td>
                    <td><tags:button icon="add" color="blue" value="Add" size="small"
                                     onclick="addNotification(${ruleIndex},1)" markupWithTag="a"/></td>
                </tr>
            </table>
        </div>
    </div>
    <br/>
    &nbsp;&nbsp;&nbsp;<input type="checkbox" name="override_"${ruleIndex}/> <b>Can be overridden at site level</b>
    <br/>
    <br/>
</chrome:box>
<script type="text/javascript">
    initializeRule(${ruleIndex})
</script>
<br>
