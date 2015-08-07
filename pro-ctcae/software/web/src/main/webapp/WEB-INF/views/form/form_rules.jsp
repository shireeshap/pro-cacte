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
        function addRule(crfId) {
            var request = new Ajax.Request("<c:url value="/pages/form/addFormRule"/>", {
                onComplete:addRuleDiv,
                parameters:<tags:ajaxstandardparams/> + "&isSite=${isSite}" + 
                										"&action=addRule" +
                										"&crfId=" + crfId,
                method:'get'
            })
        }
        function addRuleDiv(transport) {
            var response = transport.responseText;
            new Insertion.Before("hiddenDiv", response);
        }
        function addRuleConditionDiv(ruleIndex, transport) {
            var response = transport.responseText;
            new Insertion.Before("hiddenDivCondition_" + ruleIndex, response);
        }
        function deleteRule(ruleId, ruleIndex) {
            if (confirm('Are you sure you want to delete Rule ' + ruleIndex + '?')) {
                $('rule_div_' + ruleId).remove();
                $('rulesToDelete').value += ruleId + ",";
            }
        }
        function deleteCondition(ruleIndex, conditionIndex) {
            $('tr_condition_' + ruleIndex + '_' + conditionIndex).remove();
            $('delete_conditions_' + ruleIndex).value += conditionIndex + ",";
        }
        function addCondition(ruleIndex, crfId) {
            var request = new Ajax.Request("<c:url value="/pages/form/addFormRule"/>", {
                onComplete:function(transport) {
                    addRuleConditionDiv(ruleIndex, transport);
                },
                parameters:<tags:ajaxstandardparams/> + "&isSite=${isSite}" + 
                										"&dcStr="+ $('delete_conditions_' + ruleIndex).value + 
                										"&action=addCondition" + 
                										"&ruleIndex=" + ruleIndex + 
                										"&crfId=" + crfId,
                method:'get'
            })

        }
        var thresholds = new Array();
        <c:forEach items="${allQuestionTypes}" var="qType" >
        thresholds['${qType.displayName}'] = new Array();
        <c:forEach items="${qType.validValues}" var="vValue" varStatus="vStatus">
        thresholds['${qType.displayName}'][${vStatus.index}] = '${vValue}';
        </c:forEach>
        </c:forEach>

        function changeThresholds(obj, ruleIndex, ruleConditionIndex) {
            var thV = thresholds[obj.value];
            var selectThres = $('threshold_' + ruleIndex + '_' + ruleConditionIndex);
            for (i = selectThres.length - 1; i >= 0; i--) {
                selectThres.remove(i);
            }
            for (var j = 0; j < thV.length; j++) {
                var elOptNew = document.createElement('option');
                elOptNew.text = thV[j];
                elOptNew.value = j;
                selectThres.options[j]=elOptNew;
                
            }
        }
    </script>
</head>

<body>
<tags:tabForm tab="${tab}" flow="${flow}" notDisplayInBox="true" doNotShowSave="${readonlyview}">
    <jsp:attribute name="repeatingFields">
		<chrome:box>
			<table>
			    <tr>
			        <td style="text-align:right;font-weight:bold;"><tags:message code='form.label.study'/>:</td>
			        <td style="padding-left:10px;">${command.crf.study.displayName}</td>
			    </tr>
			    <tr>
			        <td style="text-align:right;font-weight:bold;"><tags:message code='form.tab.form'/>:</td>
			        <td style="padding-left:10px;">${command.crf.title}</td>
			    </tr>
			    <tr>
			        <td style="text-align:right;font-weight:bold;" valign="top">Instructions:</td>
			        <td style="padding-left:10px;"><tags:message code='form.notification.instructions'/></td>
			    </tr>
			</table>
		</chrome:box>

        <input type="hidden" name="rulesToDelete" id="rulesToDelete" value="">
        <c:forEach items="${notificationRules}" var="notificationRule" varStatus="status">
            <tags:formRule rule="${notificationRule}" ruleIndex="${status.index}" crfId="${param.crfId}"/>
        </c:forEach>
        <div id="hiddenDiv"></div>
        <table>
		    <tr>
		    	<td width="25%"></td>
		        <td><tags:button color="blue" markupWithTag="a" onclick="javascript:addRule('${param.crfId}')" value="form.rules.add_rule" icon="add"/></td>
		    </tr>
    	</table>
    </jsp:attribute>
</tags:tabForm>
</body>
</html>