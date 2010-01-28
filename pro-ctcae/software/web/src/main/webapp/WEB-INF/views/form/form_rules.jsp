<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<html>
<head>

    <tags:stylesheetLink name="tabbedflow"/>
    <tags:includeScriptaculous/>
    <tags:includePrototypeWindow/>
    <script type="text/javascript">
        function addRule() {
            var request = new Ajax.Request("<c:url value="/pages/form/addFormRule"/>", {
                onComplete:addRuleDiv,
                parameters:<tags:ajaxstandardparams/>+"&isSite=${isSite}",
                method:'get'
            })
        }
        function addRuleDiv(transport) {
            var response = transport.responseText;
            new Insertion.Before("hiddenDiv", response);
        }
        function deleteRule(ruleId, ruleIndex) {
            if (confirm('Are you sure you want to delete Rule ' + ruleIndex + '?')) {
                $('rule_div_' + ruleId).remove();
                $('rulesToDelete').value += ruleId + ",";
            }
        }
        function deleteCondition(ruleId, conditionIndex) {
            $('tr_condition_' + ruleId + '_' + conditionIndex).remove();
            $('delete_conditions_' + ruleId).value += conditionIndex + ",";
        }
    </script>

</head>
<body>
<table>
    <tr>
        <td style="text-align:right;font-weight:bold;"><tags:message code='form.label.study'/></td>
        <td style="padding-left:10px;">${command.crf.study.displayName}</td>
    </tr>
    <tr>
        <td style="text-align:right;font-weight:bold;"><tags:message code='form.tab.form'/></td>
        <td style="padding-left:10px;">${command.crf.title}</td>
    </tr>
    <tr>
        <td style="text-align:right;font-weight:bold;">Instructions</td>
        <td style="padding-left:10px;"><tags:message code='form.notification.instructions'/></td>
    </tr>
</table>


<tags:tabForm tab="${tab}" flow="${flow}" notDisplayInBox="true" doNotShowSave="${readonlyview}">
    <jsp:attribute name="repeatingFields">
        <input type="hidden" name="rulesToDelete" id="rulesToDelete" value="">
        <c:forEach items="${notificationRules}" var="notificationRule" varStatus="status">
            <tags:formRule rule="${notificationRule}" ruleIndex="${status.index}"/>
        </c:forEach>
        <div id="hiddenDiv"></div>
        <br/>
        <div align="left" style="margin-left:2em">
            <tags:button color="blue" markupWithTag="a" onclick="javascript:addRule()"
                         value="form.rules.add_rule"
                         icon="add"/>
        </div>
    </jsp:attribute>
</tags:tabForm>

</body>
</html>