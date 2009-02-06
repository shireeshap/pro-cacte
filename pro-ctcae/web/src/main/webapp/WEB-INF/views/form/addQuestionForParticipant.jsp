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

        .label {
            font-weight: bold;
            font-size: 16px;
            vertical-align: top;
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

        var nextRowIndex = 7;
        var nextColumnIndex = 21;

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


        function addCheckbox(selectedChoice) {
            if (nextColumnIndex % 3 == 0) {
                var tbody = document.getElementById('mytable').getElementsByTagName("TBODY")[0];

                var row = document.createElement("TR")

                var td1 = document.createElement("TD")
                td1.id = 'td_' + nextColumnIndex + '_a';
                td1.addClassName('label');

                var td2 = document.createElement("TD")
                td2.id = 'td_' + nextColumnIndex + '_b';
                td2.addClassName('label');

                var td3 = document.createElement("TD")
                td3.id = 'td_' + (nextColumnIndex + 1) + '_a';
                td3.addClassName('label');

                var td4 = document.createElement("TD")
                td4.id = 'td_' + (nextColumnIndex + 1) + '_b';
                td4.addClassName('label');

                var td5 = document.createElement("TD")
                td5.id = 'td_' + (nextColumnIndex + 2) + '_a';
                td5.addClassName('label');

                var td6 = document.createElement("TD")
                td6.id = 'td_' + (nextColumnIndex + 2) + '_b';
                td6.addClassName('label');

                row.appendChild(td1);
                row.appendChild(td2);
                row.appendChild(td3);
                row.appendChild(td4);
                row.appendChild(td5);
                row.appendChild(td6);
                tbody.appendChild(row);
                nextRowIndex++;
            }
            var tda = document.getElementById('td_' + nextColumnIndex + '_a');
            var tdb = document.getElementById('td_' + nextColumnIndex + '_b');
            var chkbox = document.createElement('input');
            chkbox.type = "checkbox";
            chkbox.name = 'symptomsByParticipants';
            chkbox.value = selectedChoice;
            chkbox.checked = true;
            tda.appendChild(chkbox);
            tdb.appendChild(document.createTextNode(selectedChoice));
            nextColumnIndex++;
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
            <b>NEED TEXT HERE</b>
        </p>

        <table id="mytable">
            <tbody>
            <c:forEach var="i" begin="0" end="6" varStatus="status">
                <c:if test="${command.sortedSymptoms[i*3+0] ne null}">
                    <tr id="tr_"${i}>
                        <c:forEach var="j" begin="0" end="2" varStatus="status">
                            <td id="td_${i*3+j}_a" class="label">
                                <c:if test="${command.sortedSymptoms[i*3+j] ne null}">
                                    <input type="checkbox" name="symptomsByParticipants"
                                           value="${command.sortedSymptoms[i*3+j]}"/>
                                </c:if>
                            </td>
                            <td id="td_${i*3+j}_b" class="label">
                                <c:if test="${command.sortedSymptoms[i*3+0] ne null}">
                                    ${command.sortedSymptoms[i*3+j]}
                                </c:if>
                            </td>
                        </c:forEach>
                    </tr>
                </c:if>
            </c:forEach>
            </tbody>
        </table>

        <p>
            <b>NEED TEXT HERE : If you are experiencing any other symptoms not listed above, then please type them in
                the below search box</b>
        </p>

        <input type="text" id="participantquestion-input" value="" class="autocomplete  validate-NOTEMPTY" size="60"/>
        <input type="button" id="participantquestion-clear" value="Clear"/>
        <tags:indicator id="participantquestion-indicator"/>
        <div id="participantquestion-choices" class="autocomplete"></div>
        </p>
        <br/>&nbsp;<br/>
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