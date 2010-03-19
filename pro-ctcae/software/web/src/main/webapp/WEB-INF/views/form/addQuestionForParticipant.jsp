<%@ page import="gov.nih.nci.ctcae.web.form.SubmitFormCommand" %>
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
<%
    Object attribute = request.getAttribute("command");
    int numOfSymptoms = ((SubmitFormCommand) attribute).getDisplaySymptoms().size();
    int numOfRows = 0;
    if (numOfSymptoms % 3 == 0) {
        numOfRows = numOfSymptoms / 3;
    } else {
        numOfRows = (numOfSymptoms / 3) + 1;
    }
    request.setAttribute("numrows", numOfRows - 1);
%>
<html>
<head>
    <link rel="stylesheet" type="text/css"
          href="http://yui.yahooapis.com/combo?2.8.0r4/build/autocomplete/assets/skins/sam/autocomplete.css">
    <script type="text/javascript"
            src="http://yui.yahooapis.com/combo?2.8.0r4/build/yahoo-dom-event/yahoo-dom-event.js&2.8.0r4/build/animation/animation-min.js&2.8.0r4/build/connection/connection-min.js&2.8.0r4/build/datasource/datasource-min.js&2.8.0r4/build/autocomplete/autocomplete-min.js"></script>
    <tags:includeVirtualKeyboard/>
    <tags:includePrototypeWindow/>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <style type="text/css">
        body {
            font-family: Arial;
        }

        * {
            zoom: 1;
        }

        .label {
            font-weight: bold;
            font-size: 15px;
            vertical-align: top;
        }

        #participantSymptomAutoComplete {
            width: 25em;
            padding-bottom: 2em;
        }

        #participantSymptomAutoComplete .yui-ac-content {
            max-height: 10em;
            overflow: auto;
            overflow-x: hidden; /* set scrolling */
            _height: 30em; /* ie6 */
        }

    </style>
    <tags:includeScriptaculous/>
    <tags:dwrJavascriptLink objects="scheduleCrf"/>
    <script type="text/javascript">
        var oAC;
        function initializeAutoCompleter() {
            var oDS = new YAHOO.util.XHRDataSource("matchSymptoms");
            oDS.responseType = YAHOO.util.XHRDataSource.TYPE_TEXT;
            oDS.responseSchema = {
                recordDelim: ";",
                fieldDelim: "\t"
            };
            oAC = new YAHOO.widget.AutoComplete("participantSymptomInput", "participantSymptomContainer", oDS);
            oAC.maxResultsDisplayed = 100;
        }

        var nextColumnIndex = ${fn:length(command.displaySymptoms)};

        function addNewSymptom(selectedChoice) {
            scheduleCrf.checkIfSymptomAlreadyExistsInForm(selectedChoice, function(values) {
                if (values != '') {
                    var request = new Ajax.Request("<c:url value="/pages/participant/confirmSymptom"/>", {
                        parameters:<tags:ajaxstandardparams/>+"&values=" + values + "&selectedChoice=" + selectedChoice,
                        onComplete:function(transport) {
                            showConfirmationWindow(transport, 600, 250);
                        },
                        method:'get'
                    })

                } else {
                    addSymptom(selectedChoice);
                }
            })
        }
        function addSymptom(selectedChoice) {
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
            clearInput();
            initSearchField();

        }
        function clearInput() {
            $('participantSymptomInput').clear();
        }
        function addCheckbox(selectedChoice) {
            clearInput();
            if (selectedChoice == '') {
                return;
            }
            if (nextColumnIndex % 3 == 0) {
                var tbody = document.getElementById('mytable').getElementsByTagName("TBODY")[0];
                var row = document.createElement("TR");

                var td1 = document.createElement("TD");
                td1.id = 'td_' + nextColumnIndex + '_a';
                $(td1).addClassName('label');

                var td2 = document.createElement("TD");
                td2.id = 'td_' + nextColumnIndex + '_b';
                $(td2).addClassName('label');

                var td3 = document.createElement("TD");
                td3.id = 'td_' + (nextColumnIndex + 1) + '_a';
                $(td3).addClassName('label');

                var td4 = document.createElement("TD");
                td4.id = 'td_' + (nextColumnIndex + 1) + '_b';
                $(td4).addClassName('label');

                var td5 = document.createElement("TD");
                td5.id = 'td_' + (nextColumnIndex + 2) + '_a';
                $(td5).addClassName('label');

                var td6 = document.createElement("TD");
                td6.id = 'td_' + (nextColumnIndex + 2) + '_b';
                $(td6).addClassName('label');

                row.appendChild(td1);
                row.appendChild(td2);
                row.appendChild(td3);
                row.appendChild(td4);
                row.appendChild(td5);
                row.appendChild(td6);
                tbody.appendChild(row);
            }
            var tda = document.getElementById('td_' + nextColumnIndex + '_a');
            var tdb = document.getElementById('td_' + nextColumnIndex + '_b');
            var chkbox = document.createElement('input');
            chkbox.type = "checkbox";
            chkbox.name = 'symptomsByParticipants';
            chkbox.value = selectedChoice;
            chkbox.id = nextColumnIndex
            chkbox.onchange = function() {
                changeClass(chkbox, chkbox.id);
            }
            tda.appendChild(chkbox);
            chkbox.checked = true;
            var div = document.createElement('div');
            div.setAttribute('id', 'div_' + nextColumnIndex);
            div.appendChild(document.createTextNode(selectedChoice));
            tdb.appendChild(div);
            changeClass(chkbox, nextColumnIndex);
            nextColumnIndex++;
        }


        Event.observe(window, "load", function() {
            initializeAutoCompleter()
            initSearchField();
            var input = document.getElementById('participantSymptomInput');

        })

        function changeClass(obj, index) {
            var div = $('div_' + index);
            if (obj.checked) {
                $(div).removeClassName("norm");
                $(div).addClassName('over');
            } else {
                div.removeClassName("over");
                div.addClassName("norm");
            }
        }
        function submitForm(direction) {
            document.myForm.direction.value = direction;
            document.myForm.submit();
        }

    </script>
</head>
<body>
<form:form method="post" name="myForm">
    <chrome:box title="Form: ${command.schedule.studyParticipantCrf.crf.title}"
                autopad="true" message="false">
        <p>
            <b><tags:message code="participant.form.selectsymptom"/></b>
        </p>
        <table id="mytable">
            <tbody>
            <c:set var="numrows" value="${numrows}"/>
            <c:set var="displaySymptoms" value="${command.displaySymptoms}"/>
            <c:forEach var="i" begin="0" end="${numrows}" varStatus="status">
                <c:if test="${displaySymptoms[i*3+0] ne null}">
                    <tr id="tr_"${i}>
                        <c:forEach var="j" begin="0" end="2" varStatus="status">
                            <td id="td_${i + (numrows+1)*j}_a" class="" style="vertical-align:top" width="1%">
                                <c:if test="${displaySymptoms[i + (numrows+1)*j] ne null}">
                                    <input type="checkbox" name="symptomsByParticipants"
                                           value="${displaySymptoms[i + (numrows+1)*j]}"
                                           onclick="javascript:changeClass(this,'${i + (numrows+1)*j}');"
                                           id="${i + (numrows+1)*j}"/>
                                </c:if>
                            </td>
                            <td id="td_${i + (numrows+1)*j}_b" class="value label" width="32%">
                                <c:if test="${displaySymptoms[i + (numrows+1)*j] ne null}">
                                    <div id="div_${i + (numrows+1)*j}"
                                         class="label">${displaySymptoms[i + (numrows+1)*j]}</div>
                                </c:if>
                            </td>
                        </c:forEach>
                    </tr>
                </c:if>
            </c:forEach>
            </tbody>
        </table>

        <br/>

        <p>
            <b><tags:message code="participant.form.typesymptom"/></b>
        </p>
        <br/>

        <div id="keyboardDiv"></div>
        <br/>
        <%--<input type="text" id="participantquestion-input" value="" class="autocomplete"--%>
        <%--size="60"/>--%>
        <%--<input type="hidden" id="participantquestion"/>--%>
        <%--<img src="/proctcae/images/blue/clear-left-button.png"--%>
        <%--onclick="javascript:$('participantquestion-input').clear();"--%>
        <%--style="vertical-align:top;cursor:pointer"/>--%>
        <%--<tags:indicator id="participantquestion-indicator"/>--%>
        <%--<div id="participantquestion-add" style="display:none">--%>
        <%--</div>--%>

        <%--<div id="participantquestion-choices" class="autocomplete"></div>--%>

        <div class="yui-skin-sam">
            <table cellspacing="10px;">
                <tr>
                    <td>
                        <div id="participantSymptomAutoComplete">
                            <input id="participantSymptomInput" type="text">

                            <div id="participantSymptomContainer"></div>
                        </div>
                    </td>
                    <td>
                        <tags:button onclick="javascript:addNewSymptom($('participantSymptomInput').value)"
                                     icon="add"
                                     size="small" color="blue" value="add" markupWithTag="a"/>
                    </td>
                    <td>
                        <tags:button onclick="javascript:clearInput()"
                                     icon="x"
                                     size="small" color="blue" value="clear" markupWithTag="a"/>
                    </td>
                </tr>
            </table>

        </div>

        <div class="row">
            <input id='usevirtualkeyboard' type="checkbox"
                   onclick="showVirtualKeyBoard(this,'participantSymptomInput');">&nbsp;Use virtual
            keyboard
        </div>

    </chrome:box>

    <table width="100%">
        <input type="hidden" name="direction"/>
        <tr>
            <td align="left" width="50%">
                <tags:button onclick="javascript:submitForm('back')" icon="back" color="blue"
                             value="Back" markupWithTag="a"/>
            </td>
            <td align="right" width="50%">
                <tags:button onclick="javascript:submitForm('continue')" icon="continue"
                             color="green" value="Continue" markupWithTag="a"/>
            </td>
        </tr>
    </table>

</form:form>
</body>
</html>