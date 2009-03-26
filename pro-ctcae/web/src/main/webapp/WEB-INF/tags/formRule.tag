<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@attribute name="ruleSet" type="com.semanticbits.rules.brxml.RuleSet" required="true" %>
<%@attribute name="ruleIndex" required="true" %>
<%--<chrome:box title="${ruleSet.rule[ruleIndex].metaData.name}">--%>
<chrome:box title="Rule ${ruleIndex + 1}">
    <chrome:division title="Symptoms"/>
    <div class="row">
        <div class="value">
            <table>
                <tr id="symptomDiv_${ruleIndex}">
                    <td></td>
                    <td><tags:button icon="add" color="blue" value="Add" size="small"
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
</chrome:box>

<script type="text/javascript">
    initializeRule(${ruleIndex})
</script>

<br>
