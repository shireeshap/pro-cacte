<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <style type="text/css">
        tr {
            border-bottom: 1px solid #123121;
        }
    </style>
    <tags:includeScriptaculous/>
    <tags:dwrJavascriptLink objects="scheduleCrf"/>
    <script type="text/javascript">
        var participantQuestionAutocompleterProps = {
            basename: "participantquestion",
            populator: function(autocompleter, text) {
                scheduleCrf.matchSymptoms(text, '${command.studyParticipantCrfSchedule.id}', function(values) {
                    autocompleter.setChoices(values)
                })
            },
            valueSelector: function(obj) {
                return obj
            }
        }
        var currentIndex = 1;
        function acPostSelect(mode, selectedChoice) {
            var checkboxitems = document.getElementsByName('symptomsByParticipants');
            var itemfound = false;
            for (var i = 0; i < checkboxitems.length; i++) {
                if (checkboxitems[i].value == selectedChoice) {
                    checkboxitems[i].checked = true;
                    itemfound = true
                    break;
                }
            }
            if (!itemfound) {
                addCheckbox(selectedChoice);
            }
        }

        function updateSelectedDisplay(mode) {
            //            if ($(mode.basename).value) {
            //                //add option
            //            }
        }

        function addCheckbox(selectedChoice) {
            var td = document.getElementById('td_' + currentIndex);
            var chkbox = document.createElement('input');
            chkbox.type = "checkbox";
            chkbox.name = 'symptomsByParticipants';
            chkbox.value = selectedChoice;
            chkbox.checked = true;
            var mydiv = document.createElement('div');
            mydiv.appendChild(chkbox);
            mydiv.appendChild(document.createTextNode(selectedChoice));
            td.appendChild(mydiv);
            currentIndex++;
            if (currentIndex > 3) {
                currentIndex = 1;
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
                $(mode.basename + "-input").value = "";
                initSearchField();
            })
        }

        Event.observe(window, "load", function() {
            acCreate(participantQuestionAutocompleterProps)
            updateSelectedDisplay(participantQuestionAutocompleterProps)
            initSearchField()
        })

    </script>
</head>
<body>
<chrome:flashMessage flashMessage="${command.flashMessage}"></chrome:flashMessage>
<form:form method="post" name="myForm">
    <chrome:box title="Form: ${command.studyParticipantCrfSchedule.studyParticipantCrf.crf.title}"
                autopad="true" message="false">
        <p>
            <b>Please select additional symptoms from below:</b>
        </p>

        <%--<input type="hidden" id="participantquestion" value=""/>--%>
        <input type="text" id="participantquestion-input" value="" class="autocomplete  validate-NOTEMPTY" size="40"/>
        <input type="button" id="participantquestion-clear" value="Clear"/>
        <tags:indicator id="participantquestion-indicator"/>
        <div id="participantquestion-choices" class="autocomplete"></div>
        </p>
        <br/>&nbsp;<br/>
        <table>
            <tr>
                <td width="33%" id="td_1">
                    <c:forEach var="i" begin="1" end="7" varStatus="status">
                        <input type="checkbox" name="symptomsByParticipants"
                               value="${command.sortedSymptoms[i]}"/> ${command.sortedSymptoms[i]}
                        <br/>
                    </c:forEach>
                </td>
                <td width="33%" id="td_2">
                    <c:forEach var="i" begin="8" end="14" varStatus="status">
                        <input type="checkbox" name="symptomsByParticipants"
                               value="${command.sortedSymptoms[i]}"/> ${command.sortedSymptoms[i]}
                        <br/>
                    </c:forEach>
                </td>
                <td width="33%" id="td_3">
                    <c:forEach var="i" begin="15" end="21" varStatus="status">
                        <input type="checkbox" name="symptomsByParticipants"
                               value="${command.sortedSymptoms[i]}"/> ${command.sortedSymptoms[i]}
                        <br/>
                    </c:forEach>
                </td>
            </tr>
        </table>

    </chrome:box>
    <table width="100%">
        <input type="hidden" name="direction"/>
        <tr>
            <td align="left" width="50%">
                <input onclick="document.myForm.direction.value='back'" type="image"
                       src="/ctcae/images/blue/back_btn.png" alt="back &raquo;"/>
            </td>
            <td align="right" width="50%">
                <input onclick="document.myForm.direction.value='continue'" type="image"
                       src="/ctcae/images/blue/continue_btn.png" alt="continue &raquo;"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>