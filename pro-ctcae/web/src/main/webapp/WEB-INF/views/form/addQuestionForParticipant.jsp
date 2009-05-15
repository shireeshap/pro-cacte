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
    <tags:includeVirtualKeyboard/>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <style type="text/css">
        body {
            font-family: Arial;
        }

        .label {
            font-weight: bold;
            font-size: 15px;
            vertical-align: top;
        }

        /*.blue {*/
        /*background-color: #4545c3;*/
        /*}*/

        /*.norm {*/
        /*background-color: white;*/
        /*}*/

    </style>
    <tags:includeScriptaculous/>
    <tags:dwrJavascriptLink objects="scheduleCrf"/>
    <script type="text/javascript">
        var participantQuestionAutocompleterProps = {
            basename: "participantquestion",
            populator: function(autocompleter, text) {
                scheduleCrf.matchSymptoms(text, '${command.studyParticipantCrfSchedule.id}', function(values) {
                    autocompleter.setChoices(values);
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
                    changeClass(checkboxitems[i], checkboxitems[i].id);
                    break;
                }
            }
            if (!itemfound) {
                addCheckbox(selectedChoice);
            }
            $("participantquestion-input").value = "";
            initSearchField();

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
            chkbox.id = nextColumnIndex
            chkbox.onchange = function() {
                changeClass(chkbox, chkbox.id);
            }
            tda.appendChild(chkbox);
            var div = document.createElement('div');
            div.setAttribute('id', 'div_' + nextColumnIndex);
            div.appendChild(document.createTextNode(selectedChoice));
            tdb.appendChild(div);
            changeClass(chkbox, nextColumnIndex);
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


        function changeClass(obj, index) {
            var div = $('div_' + index);
            if (obj.checked) {
                div.removeClassName("norm");
                div.addClassName('over');
            } else {
                div.removeClassName("over");
                div.addClassName("norm");
            }
        }
    </script>
</head>
<body>
<chrome:flashMessage flashMessage="${command.flashMessage}"></chrome:flashMessage>
<form:form method="post" name="myForm">
    <chrome:box title="Form: ${command.studyParticipantCrfSchedule.studyParticipantCrf.crf.title}"
                autopad="true" message="false">
        <p>
            <b><tags:message code="participant.form.selectsymptom"/></b>
        </p>
        <table id="mytable">
            <tbody>
            <c:set var="numrows" value="6"/>
            <c:forEach var="i" begin="0" end="${numrows}" varStatus="status">
                <c:if test="${command.displaySymptoms[i*3+0] ne null}">
                    <tr id="tr_"${i}>
                        <c:forEach var="j" begin="0" end="2" varStatus="status">
                            <td id="td_${i + (numrows+1)*j}_a" class="" width="1%">
                                <c:if test="${command.displaySymptoms[i + (numrows+1)*j] ne null}">
                                    <input type="checkbox" name="symptomsByParticipants"
                                           value="${command.displaySymptoms[i + (numrows+1)*j]}"
                                           onchange="javascript:changeClass(this,'${i + (numrows+1)*j}');"
                                           id="${i + (numrows+1)*j}"/>
                                </c:if>
                            </td>
                            <td id="td_${i + (numrows+1)*j}_b" class="value" width="32%">
                                <c:if test="${command.displaySymptoms[i + (numrows+1)*j] ne null}">
                                    <div id="div_${i + (numrows+1)*j}"
                                         class="label">${command.displaySymptoms[i + (numrows+1)*j]}</div>
                                </c:if>
                            </td>
                        </c:forEach>
                    </tr>
                </c:if>
            </c:forEach>
            </tbody>
        </table>

        <br/>
        <br/>

        <p>
            <b><tags:message code="participant.form.typesymptom"/></b>
        </p>

        <input type="text" id="participantquestion-input" value="" class="autocomplete"
               size="60"/>
        <img src="/proctcae/images/blue/clear-left-button.png"
             onclick="javascript:$('participantquestion-input').clear();"
             style="vertical-align:top;cursor:pointer"/>
        <tags:indicator id="participantquestion-indicator"/>
        <div id="participantquestion-choices" class="autocomplete"></div>
        </p>
        <br/>&nbsp;<br/>
    </chrome:box>
    <table width="100%">
        <input type="hidden" name="direction"/>
        <tr>
            <td align="left" width="50%">
                <tags:button onclick="document.myForm.direction.value='back'" type="submit" icon="back" color="blue"
                             value="Back"/>
            </td>
            <td align="right" width="50%">
                <tags:button onclick="document.myForm.direction.value='continue'" type="submit" icon="continue"
                             color="green" value="Continue"/>
            </td>
        </tr>
    </table>

</form:form>
</body>
</html>