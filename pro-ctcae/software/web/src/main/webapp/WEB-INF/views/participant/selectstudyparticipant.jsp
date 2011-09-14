<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${pageTitle}</title>
    <c:if test="${empty tab}">
        <tags:stylesheetLink name="tabbedflow"/>
        <tags:javascriptLink name="tabbedflow"/>
    </c:if>
    <tags:stylesheetLink name="yui-autocomplete"/>
    <tags:javascriptLink name="yui-autocomplete"/>
    <tags:includeScriptaculous/>
    <tags:dwrJavascriptLink objects="scheduleCrf"/>
    <script type="text/javascript">

        function getParticipants(sQuery) {
            showIndicator("participantInput-indicator");
            var callbackProxy = function(results) {
                aResults = results;
            };
            var callMetaData = { callback:callbackProxy, async:false};
            scheduleCrf.matchParticipants(unescape(sQuery), $('study').value, callMetaData);
            hideIndicator("participantInput-indicator");
            return aResults;
        }
        function getStudies(sQuery) {
            showIndicator("studyInput-indicator");
            var callbackProxy = function(results) {
                aResults = results;
            };
            var callMetaData = { callback:callbackProxy, async:false};
            scheduleCrf.matchStudies(unescape(sQuery), $('participant').value, callMetaData);
            hideIndicator("studyInput-indicator");
            return aResults;
        }

        var managerAutoComp;
        Event.observe(window, 'load', function() {
            new YUIAutoCompleter('studyInput', getStudies, handleSelect);
            new YUIAutoCompleter('participantInput', getParticipants, handleSelect);

            if ("${command.study.displayName}" != '') {
                $('studyInput').value = "${command.study.displayName}";
                $('studyInput').removeClassName('pending-search');
            }
            if ("${command.participant.displayName}" != '') {
                $('participantInput').value = "${command.participant.displayName}";
                $('participantInput').removeClassName('pending-search');
            }

        })
                ;

        function handleSelect(stype, args) {
            var ele = args[0];
            var oData = args[2];
            ele.getInputEl().value = oData.displayName;
            var id = ele.getInputEl().id;
            var hiddenInputId = id.substring(0, id.indexOf('Input'));
            Element.update(hiddenInputId + "-selected-name", oData.displayName)
            $(hiddenInputId + '-selected').show()
            new Effect.Highlight(hiddenInputId + "-selected")
            $(hiddenInputId).value = oData.id;
        }


        function clearInput(inputId) {
            $(inputId).clear();
            $(inputId + 'Input').clear();
            $(inputId + 'Input').focus();
            $(inputId + 'Input').blur();
        }

    </script>
</head>
<body>
<tags:instructions code="participant.schedule_crf.enter"/>
<tags:tabForm tab="${tab}" flow="${flow}" willSave="false" notDisplayInBox="true">
<jsp:attribute name="singleFields">
<div class="autoclear">
    <chrome:box title="schedulecrf.label.select_study" id="study-entry" cssClass="paired">
        <tags:instructions code="participant.schedule_crf.select_study"/>
        <div class="row">
	        <div class="label" style="width:0em;"></div>
	        <div class="value" style="margin-left: 0;">
	        	<form:input path="study" id="study" cssClass="validate-NOTEMPTY" title="Study" cssStyle="display:none;"/>
	        	<tags:yuiAutocompleter inputName="studyInput" value="${command.study.shortTitle}" required="true" hiddenInputName="study"/>
	        </div>
        </div>
        
        <p id="study-selected" style="display: none">
            You have selected the study <span id="study-selected-name"></span>.
        </p>
    </chrome:box>

    <chrome:box title="schedulecrf.label.select_participant" id="participant-entry" cssClass="paired">
        <tags:instructions code="participant.schedule_crf.select_participant"/>
        <div class="row">
	        <div class="label" style="width:0em;"></div>
	        <div class="value" style="margin-left: 0;">
		        <form:input path="participant" id="participant" cssClass="validate-NOTEMPTY"
		                    title="Participant"
		                    cssStyle="display:none;"/>
		        <tags:yuiAutocompleter inputName="participantInput" value="${command.participant.displayName}" required="true"
		                               hiddenInputName="participant"/>
			</div>
		</div>
        <p id="participant-selected" style="display: none">
            You have selected the subject <span id="participant-selected-name"></span>.
        </p>
    </chrome:box>
</div>
</jsp:attribute>
</tags:tabForm>
</body>
</html>