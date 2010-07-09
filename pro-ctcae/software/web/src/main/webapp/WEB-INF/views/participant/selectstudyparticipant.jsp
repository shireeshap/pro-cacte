<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>${pageTitle}</title>
    <style type="text/css">
        input.autocomplete {
            width: 75%;
            font-style: normal;
            background-color: #CCE6FF;
        }

        input.pending-search {
            width: 75%;
            color: gray;
            font-style: italic;
            background-color: #CCE6FF;
        }

        * {
            zoom: 0;
        }

    </style>
    <c:if test="${empty tab}">
        <tags:stylesheetLink name="tabbedflow"/>
        <tags:javascriptLink name="tabbedflow"/>
    </c:if>
    <tags:includeScriptaculous/>
    <tags:dwrJavascriptLink objects="scheduleCrf"/>
    <script type="text/javascript">
        var participantAutocompleterProps = {
            basename: "participant",
            populator: function(autocompleter, text) {
                scheduleCrf.matchParticipants(text, $('study').value, function(values) {
                    autocompleter.setChoices(values)
                })
            },
            valueSelector: function(obj) {
                return obj.displayName
            }
        }

        var studyAutocompleterProps = {
            basename: "study",
            populator: function(autocompleter, text) {
                scheduleCrf.matchStudies(text, $('participant').value, function(values) {
                    autocompleter.setChoices(values)
                })
            },
            valueSelector: function(obj) {
                return obj.displayName;
            }
        }

        function acPostSelect(mode, selectedChoice) {
            Element.update(mode.basename + "-selected-name", mode.valueSelector(selectedChoice))
            $(mode.basename).value = selectedChoice.id;
            $(mode.basename + '-selected').show()
            new Effect.Highlight(mode.basename + "-selected")
        }

        function updateSelectedDisplay(mode) {
            if ($(mode.basename).value) {
                Element.update(mode.basename + "-selected-name", $(mode.basename + "-input").value)
                $(mode.basename + '-selected').show()
            }
        }

        function acCreate(mode) {
            new Autocompleter.DWR(mode.basename + "-input", mode.basename + "-choices",
                    mode.populator, {
                valueSelector: mode.valueSelector,
                afterUpdateElement: function(inputElement, selectedElement, selectedChoice) {
                    acPostSelect(mode, selectedChoice)
                },
                indicator: mode.basename + "-indicator"
            })
            Event.observe(mode.basename + "-clear", "click", function() {
                $(mode.basename + "-selected").hide()
                $(mode.basename).value = ""
                $(mode.basename + "-input").value = ""
            })
        }

        Event.observe(window, "load", function() {
            acCreate(participantAutocompleterProps)
            acCreate(studyAutocompleterProps)
            updateSelectedDisplay(participantAutocompleterProps)
            updateSelectedDisplay(studyAutocompleterProps)
            initSearchField()
        })
    </script>
</head>
<body>
    <tags:instructions code="participant.schedule_crf.enter"/>
<tags:tabForm tab="${tab}" flow="${flow}" willSave="false" notDisplayInBox="true">
   <jsp:attribute name="singleFields">
    <div class="autoclear">
        <chrome:box title="schedulecrf.label.select_participant" id="participant-entry" cssClass="paired"
                    autopad="true">
            <tags:instructions code="participant.schedule_crf.select_participant"/>

            <form:input path="participant" id="participant" cssClass="validate-NOTEMPTY"
                        title="Participant"
                        cssStyle="display:none;"/>

            <tags:requiredIndicator/>
            <input type="text" id="participant-input" value="${command.participant.displayName}"
                   class="autocomplete  validate-NOTEMPTY"/>
            <input id="participant-clear" type="image" style="vertical-align: top;"
                   src="/proctcae/images/blue/clear-left-button.png"
                   onclick="javascript:$('participant-input').clear();$('participant').clear();return false;"
                   value="Clear" name="C"/>
            <%--<tags:button color="blue" id="participant-clear" size="small" type="button" value='Clear'/>--%>
            <!--<input type="button" id="participant-clear" value="Clear"/>-->
            <tags:indicator id="participant-indicator"/>
            <div id="participant-choices" class="autocomplete"></div>
            <tags:errors path="participant"/>
            <p id="participant-selected" style="display: none">
                You have selected the subject <span id="participant-selected-name"></span>.
            </p>
        </chrome:box>
        <chrome:box title="schedulecrf.label.select_study" id="study-entry" cssClass="paired" autopad="true">
            <tags:instructions code="participant.schedule_crf.select_study"/>
            <form:input path="study" id="study" cssClass="validate-NOTEMPTY"
                        title="Study"
                        cssStyle="display:none;"/>

            <tags:requiredIndicator/>
            <input type="text" id="study-input" value="${command.study.shortTitle}" class="autocomplete"/>
            <input id="study-clear" type="image" style="vertical-align: top;"
                   src="/proctcae/images/blue/clear-left-button.png"
                   onclick="javascript:$('study-input').clear();$('study').clear();return false;" value="Clear"
                   name="C"/>
            <!--<input type="button" id="study-clear" value="Clear"/>-->
            <tags:indicator id="study-indicator"/>
            <tags:errors path="study"/>
            <div id="study-choices" class="autocomplete"></div>
            <p id="study-selected" style="display: none">
                You have selected the study <span id="study-selected-name"></span>.
            </p>
        </chrome:box>
    </div>
</jsp:attribute>
</tags:tabForm>
</body>
</html>