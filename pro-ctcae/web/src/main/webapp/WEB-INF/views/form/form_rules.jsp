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

        var rules = new Array();
        function registerme(ruleindex) {
            rules[ruleindex] = new Array();
            rules[ruleindex]['symptom'] = 0;
            rules[ruleindex]['condition'] = 0;
            rules[ruleindex]['notification'] = 0;
        }
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
        function getCopyOfSelect(original, id) {
            var copy = original.cloneNode(true);
            copy.id = id;
            copy.name = id;
            return copy;
        }

        function addFields(ruleindex, fieldtype, properties, value) {
            var index = rules[ruleindex][fieldtype]++;

            var tr = new Element('TR', {'style':"font-weight:bold",'id':fieldtype + 'row_' + ruleindex + '_' + index});
            new Insertion.Before(fieldtype + "Div_" + ruleindex, tr);
            if (index == 0) {
                tr.appendChild(getColumnFor(document.createTextNode(properties[0]), true));
            } else {
                tr.appendChild(getColumnFor(document.createTextNode(properties[1]), true));
            }

            for (var i = 2; i < properties.length; i++) {
                if (properties[i].indexOf('%') == -1) {
                    var select = getCopyOfSelect($('templateSelect_' + properties[i]), properties[i] + '_' + ruleindex + '_' + index);
                } else {
                    var temp = properties[i].substring(0, properties[i].indexOf('%') - 1);
                    var dependsonstr = properties[i].substring(properties[i].indexOf('%') + 1);
                    if (typeof(value) != 'undefined') {
                        var select = $(dependsonstr + '_' + ruleindex + '_' + index);
                        setSelectValue(select, value);
                    }
                    var dependsonobj = $(dependsonstr + '_' + ruleindex + '_' + index);
                    var dependsonval = dependsonobj.options[dependsonobj.selectedIndex].value;
                    var select = getCopyOfSelect($('templateSelect_' + temp + '_' + dependsonval), temp + '_' + ruleindex + '_' + index);
                    //                    alert('templateSelect_' + temp + '_' + dependsonval + ',' + temp + '_' + ruleindex + '_' + index);

                }
                tr.appendChild(getColumnFor(select, false));
            }

            if (index > 0) {
                tr.appendChild(getDeleteColumn(fieldtype, ruleindex, index));
            }
            return index;

        }

        function setSelectValue(select, value) {
            var opts = select.options;
            var cleanValue = '';
            if (typeof(value) != 'undefined') {
                cleanValue = trimWhitespace(value);
            } else {
                return;
            }
            for (var i = 0; i < opts.length; i++) {
                //                alert(',' + opts[i].value + ',' + cleanValue + ',' + (opts[i].value == cleanValue));
                if (opts[i].value == cleanValue) {
                    opts[i].selected = true;
                    return;
                }
            }

        }
        function addSymptom(ruleindex, value) {
            var props = new Array();
            props[0] = 'For symptom';
            props[1] = 'Or';
            props[2] = 'symptoms';
            var index = addFields(ruleindex, 'symptom', props);
            var select = $('symptoms_' + ruleindex + '_' + index);
            setSelectValue(select, value);
        }

        function addCondition(ruleindex, questiontype, operator, value) {

            var props = new Array();
            props[0] = 'If';
            props[1] = 'And';
            props[2] = 'questiontypes';
            props[3] = 'operators';
            props[4] = 'values_%questiontypes';
            var index = addFields(ruleindex, 'condition', props, questiontype);
            var select = $('questiontypes_' + ruleindex + '_' + index);
            setSelectValue(select, questiontype);
            select = $('operators_' + ruleindex + '_' + index);
            setSelectValue(select, operator);
            select = $('values_' + ruleindex + '_' + index);
            setSelectValue(select, value);
        }

        function getColumnFor(element, first) {
            var td;
            if (first) {
                td = new Element('TD', { 'id':'td_' + element.id,'style':'text-align:right'});
                td.width = 90;
            } else {
                td = new Element('TD', { 'id':'td_' + element.id,'style':'text-align:left'});
            }
            td.appendChild(element);
            return td;
        }

        function getDeleteColumn(type, ruleindex, rowindex) {
            var obj = $("templateButton_remove").cloneNode(true);
            var onclickmethod = 'a';
            obj.onclick = function deleteDiv() {
                var row = $(type + 'row_' + ruleindex + '_' + rowindex);
                row.remove();
            };

            var td = new Element('TD', {});
            td.appendChild(obj);
            return td;
        }

        function initializeRule(ruleindex) {
            addSymptom(ruleindex);
            addCondition(ruleindex);
        }

        function changeoptions(obj) {
            var indexes = obj.id.substring(obj.id.indexOf('_') + 1);
            var ruleindex = indexes.substring(0, indexes.indexOf('_'));
            var index = indexes.substring(indexes.indexOf('_') + 1);
            var selectedType = obj.options[obj.selectedIndex].value;
            var newObjId = 'values_' + ruleindex + '_' + index;
            var valueSelect = getCopyOfSelect($('templateSelect_values_' + selectedType), newObjId);
            var td = $('td_' + newObjId);
            td.removeChild($(newObjId));
            td.appendChild(valueSelect);
        }

        function deleteMe(obj) {
            obj.remove();
        }

        function deleteRule(ruleIndex) {
            $('rule_div_' + ruleIndex).remove();
            $('rulesToDelete').value += ruleIndex + ",";
        }
        function editRules() {
            $('_target').name = "_target" + 0;
            $('command').submit();
        }

    </script>
</head>
<body>
<tags:instructions code="form.notification.instructions"/>
<div id="templateSelects" style="display:none">
    <tags:renderSelect options="${crfSymptoms}" noForm="true" id="templateSelect_symptoms"/>
    <tags:renderSelect options="${questionTypes}" noForm="true" id="templateSelect_questiontypes"
                       onchange="changeoptions(this);"/>
    <tags:renderSelect options="${comparisonOptions}" noForm="true" id="templateSelect_operators"/>
    <c:forEach items="${comparisonValues}" var="cOptions">
        <c:if test="${not empty cOptions.value}">
            <select id="templateSelect_values_${cOptions.key}">
                <c:forEach items="${cOptions.value}" var="option">
                    <option value="${option.displayOrder}">${option.value}</option>
                </c:forEach>
            </select>
        </c:if>
    </c:forEach>
    <tags:button icon="x" color="red" size="small" markupWithTag="a" value="" id="templateButton_remove"/>
    <tags:renderSelect options="${notifications}" noForm="true" id="templateSelect_notifications"/>
</div>
<c:set var="readonlyview" value="${command.readonlyview}"/>
<tags:tabForm tab="${tab}" flow="${flow}" notDisplayInBox="true" doNotShowSave="${readonlyview}">
    <jsp:attribute name="repeatingFields">
        <c:if test="${!isSite}">
            <input type="hidden" name="_finish" value="true" id="_finish">
        </c:if>
        <input type="hidden" name="rulesToDelete" id="rulesToDelete" value="">
        <input type="hidden" name="readonlyview" value="${readonlyview}"/>
        <c:forEach items="${command.formOrStudySiteRules}" var="proCtcAeRule" varStatus="status">
            <tags:formRule proCtcAeRule="${proCtcAeRule}" ruleIndex="${status.index}" siteReadOnlyView="${readonlyview}"
                           isSite="${isSite}" notifications="${notifications}"/>
        </c:forEach>
        <div id="hiddenDiv"></div>
        <c:if test="${!readonlyview}">
            <div align="left">
                <tags:button color="blue" markupWithTag="a" onclick="javascript:addRule()"
                             value="form.rules.add_rule"
                             icon="add"/>
            </div>
        </c:if>
        <c:if test="${readonlyview}">
            <div align="right">
                <tags:button color="blue" markupWithTag="a" onclick="javascript:editRules()"
                             value="form.rules.edit_rules"
                             icon="edit"/>
            </div>
        </c:if>
    </jsp:attribute>
</tags:tabForm>

</body>
</html>