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
<%@taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%
    Object attribute = request.getAttribute("command");
    int numOfSymptoms = ((SubmitFormCommand) attribute).getDisplaySymptoms().size();
    int numOfRows = 0;
    if (numOfSymptoms > 0) {
        if (numOfSymptoms % 3 == 0) {
            numOfRows = numOfSymptoms / 3;
        } else {
            numOfRows = (numOfSymptoms / 3) + 1;
        }
        request.setAttribute("numrows", numOfRows - 1);
    } else {
        request.setAttribute("numrows", numOfRows);
    }
%>
<html>
<head>
<tags:stylesheetLink name="yui-autocomplete"/>
<tags:javascriptLink name="yui-autocomplete"/>
<tags:includeVirtualKeyboard/>
<tags:includePrototypeWindow/>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<style type="text/css">
    body {

    }

    input[type="text"], input[type="password"], textarea {
        background: url(../../images/blue/custom-field.png) repeat-x top;
        border: 1px solid #ccc;
        -moz-border-radius: 6px;
        -webkit-border-radius: 6px;
        border-radius: 6px;
        padding: 9px 10px;
        font-size: 20px;
        -moz-box-shadow: 0 1px 6px #999 inset;
        -webkit-box-shadow: 0 1px 6px #999 inset;
        box-shadow: 0 1px 6px #999 inset;
        text-shadow: 0 1px white;
    }

    input[type="text"]:hover, input[type="password"]:hover, textarea:hover {
        background-position: 0 -50px;
    }

    input[type="text"]:focus, input[type="password"]:focus, textarea:focus {
        background-position: 0 -100px;
        -moz-box-shadow: 0 0px 6px #ccc;
        -webkit-box-shadow: 0 0px 6px #ccc;
        box-shadow: 0 0px 6px #ccc;
    }

    .yui-skin-sam .yui-ac-content li {
        font-size: 20px;
        text-shadow: none;
        font-family: lucida grande, sans-serif;
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
        width: 450px;
        padding-bottom: 43px;
    }

    #participantSymptomAutoComplete .yui-ac-content {
        max-height: 20em;
        overflow: auto;
        overflow-x: hidden; /* set scrolling */
        _height: 30em; /* ie6 */
    }

    /*#mytable td {*/
    /*height: 65px;*/
    /*background: #d6d6d6 url(../../images/lightgray-tall.png) repeat-x top;*/
    /*border: 1px solid lightgray;*/
    /*padding: 5px 15px 5px 40px;*/
    /*-moz-border-radius: 8px;*/
    /*-webkit-border-radius: 8px;*/
    /*border-radius: 8px;*/
    /*text-shadow: 0 1px white;*/
    /*vertical-align: middle;*/
    /*}*/

    .buttonLook {
        height: auto; /*display: block;*/
        background: #d6d6d6 url(../../images/lightgray-tall.png) repeat-x top; /*background: url(/proctcae/images/check-icon.png) no-repeat 0 50%;*/
        border: 1px solid lightgray;
        padding: 10px 15px 10px 15px;
        -moz-border-radius: 8px;
        -webkit-border-radius: 8px;
        border-radius: 8px;
        text-shadow: 0 1px white;
        vertical-align: middle;
        text-align: center;
        font-weight: bold;
        font-size: 20px;
    }

    /*#mytable td:hover {*/
    /*background: #d7ffb0 url(../../images/lightgreen-tall.png) repeat-x bottom;*/
    /*color: #245808;*/
    /*text-shadow: 0 1px white;*/
    /*cursor: pointer;*/
    /*border-color: #86bc56;*/
    /*}*/

    .tdHoverLook {
        background: #d3f8ff url(../../images/lightblue-tall.png) repeat-x bottom;
        color: #245468;
        text-shadow: 0 1px white;
        cursor: pointer;
        border-color: #90cbd9;

    }

    #mytable td input {
        display: none;
    }

    .hideTd {
        display: none;
    }

    .showTd {
        display: block;
        width: 30%; /*height: auto;*/
    /*display: block;*/
    /*background: #d6d6d6 url(../../images/lightgray-tall.png) repeat-x top;*/
    /*background: url(/proctcae/images/check-icon.png) no-repeat 0 50%;*/
    /*border: 1px solid lightgray;*/
    /*padding: 10px 15px 10px 15px;*/
    /*-moz-border-radius: 8px;*/
    /*-webkit-border-radius: 8px;*/
    /*border-radius: 8px;*/
    /*text-shadow: 0 1px white;*/
    /*vertical-align: middle;*/
    /*text-align: center;*/
    /*font-weight:bold;*/
    <%----%>
    }

    #mytable td.selected {
        background: #538f32 url(/proctcae/images/green-selected_tall.png) repeat-x top; /*background-image: url("/proctcae/images/check-icon.png");*/
    /*background: url(/proctcae/images/green-selected.png) 2px 0;*/
        color: white;
        text-shadow: 0 -1px #2a6f04;
        cursor: pointer;
        border-color: #538f32;
    }

    .check {
        display: block;
        background: url(/proctcae/images/check-icon.png) no-repeat 0 50%;
        height: 100%;
        margin-right: 7px;
        float: left;
    }

</style>
<tags:includeScriptaculous/>
<tags:dwrJavascriptLink objects="scheduleCrf"/>
<script type="text/javascript">
    var oAC;
    var greeting = "Begin typing here";
    <c:if test="${pageContext.response.locale == 'es'}">
         greeting = "Comienza a escribir aqu?";
    </c:if>

    function initializeAutoCompleter() {
        var oDS = new YAHOO.util.XHRDataSource("matchSymptoms");
        oDS.responseType = YAHOO.util.XHRDataSource.TYPE_TEXT;
        oDS.responseSchema = {
            recordDelim: ";",
            fieldDelim: "\t"
        };
        oAC = new YAHOO.widget.AutoComplete("participantSymptomInput", "participantSymptomContainer", oDS);

        oAC.maxResultsDisplayed = 100;
        $('participantSymptomInput').addClassName('pending-search');
        $('participantSymptomInput').value = greeting;

        Event.observe($('participantSymptomInput'), 'click', function() {
            if ($('participantSymptomInput').value == greeting) {
                $('participantSymptomInput').value = '';
            }
            $('participantSymptomInput').removeClassName('pending-search');
        })
        Event.observe($('participantSymptomInput'), 'blur', function() {
            if ($('participantSymptomInput').value == '')
            {
                $('participantSymptomInput').value = greeting;
                $('participantSymptomInput').addClassName('pending-search');
            }
        })
    }

    var nextColumnIndex = 0;
    var tdCount = 0;
    function addNewSymptom(selectedChoice) {
        if($F('participantSymptomInput') != greeting){
            scheduleCrf.checkIfSymptomAlreadyExistsInForm(selectedChoice, function(values) {
                if (values != '') {

                    var request = new Ajax.Request("<c:url value="/pages/participant/confirmSymptom?subview=subview"/>", {
                        parameters:<tags:ajaxstandardparams/>+"&values=" + values + "&selectedChoice=" + selectedChoice + "&isMapped=" + false,
                        onComplete:function(transport) {
                            showConfirmationWindow(transport, 600, 250);
                            clearInput();
                        },
                        method:'post'
                    })

                } else {
        //            alertForAdd();
                    checkMapping(selectedChoice);
                    addSymptom(selectedChoice);
                }

            })
        }
    }

    function checkMapping(selectedChoice) {
        scheduleCrf.checkIfSymptomMapsToProctc(selectedChoice, function(values) {
            if (values != '') {
                var request = new Ajax.Request("<c:url value="/pages/participant/confirmSymptom?subview=subview"/>", {
                    parameters:<tags:ajaxstandardparams/>+"&mappedValues=" + values + "&selectedChoice=" + selectedChoice + "&isMapped=" + true,
                    onComplete:function(transport) {
                        showConfirmationWindow(transport, 500, 150);
                    },
                    method:'post'
                })

            }
        })
    }

    function alertForAdd() {
        var request = new Ajax.Request("<c:url value="/pages/participant/alertForAdd"/>", {
            onComplete:function(transport) {
                showConfirmationWindow(transport, 650, 180);
            },
            parameters:<tags:ajaxstandardparams/> +"&index=ind",
            method:'get'
        })
    }


    function addSymptom(escapedSelectedChoice) {
        var selectedChoice = unescape(escapedSelectedChoice);
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
        $('participantSymptomInput').value = greeting;
    }
    function addCheckbox(selectedChoice) {
        clearInput();

        if (selectedChoice == '') {
            return;
        }
    //    if (nextColumnIndex % 3 == 0) {
    //        var idVar = Math.floor(nextColumnIndex / 3) * 3;
          var idVar = nextColumnIndex ;
            var tbody = document.getElementById('mytable').getElementsByTagName("TBODY")[0];
            var row = document.createElement("TR");

            var td2 = document.createElement("TD");
            td2.id = 'td_' + nextColumnIndex + '_b';
            $(td2).addClassName('buttonLook');
            td2.onmouseover = function() {
                addHoverClass(idVar);
            }
            td2.onmouseout = function() {
                removeHoverClass(idVar);
            }


    //        var td4 = document.createElement("TD");
    //        td4.id = 'td_' + (nextColumnIndex + 1) + '_b';
    //        $(td4).addClassName('tdHoverLook');
    //            td4.onmouseover = function() {
    //                addHoverClass(idVar + 1);
    //            }
    //            td4.onmouseout = function() {
    //                removeHoverClass(idVar + 1);
    //            }

    //        var td6 = document.createElement("TD");
    //        td6.id = 'td_' + (nextColumnIndex + 2) + '_b';
    //        $(td6).addClassName('tdHoverLook');
    //            td6.onmouseover = function() {
    //                addHoverClass(idVar + 2);
    //            }
    //            td6.onmouseout = function() {
    //                removeHoverClass(idVar + 2);
    //            }

            tdCount = nextColumnIndex + 2;

            row.appendChild(td2);
    //        row.appendChild(td4);
    //        row.appendChild(td6);
            tbody.appendChild(row);

            td2.onclick = function() {
                var v = idVar;
                changeTdClass(v);

            }
    //        td4.onclick = function() {
    //            var v = idVar + 1;
    //            changeTdClass(v);
    //        }
    //        td6.onclick = function() {
    //            var v = idVar + 2;
    //            changeTdClass(v);
    //        }
    //    }


        var tdb = document.getElementById('td_' + nextColumnIndex + '_b');
        var chkbox = document.createElement('input');
        chkbox.type = "checkbox";
        chkbox.name = 'symptomsByParticipants';
        chkbox.value = selectedChoice;
        chkbox.id = nextColumnIndex;
        chkbox.onchange = function() {
            changeClass(chkbox, chkbox.id);
        }

        var divTag = document.createElement("div");
        divTag.id = "div1_" + nextColumnIndex;
        divTag.setAttribute("align", "left");
        divTag.style.margin = "0px auto";
        divTag.className = "check";
        divTag.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;";
    //    alert(tdb.innerHTML);
    //     if (tdb.innerHTML != '') {
    //            tdb.onmouseover = function() {
    //                addHoverClass(nextColumnIndex-1);
    //            }
    //            tdb.onmouseout = function() {
    //                removeHoverClass(nextColumnIndex-1);
    //            }
    //        }
        if (nextColumnIndex > 0) {
            tdb.addClassName('buttonLook');
    //        tdb.addClassName('showTd');
        }
        tdb.appendChild(chkbox);
        tdb.appendChild(divTag);
        selectCheckBox(nextColumnIndex);

        var div = document.createElement('div');
        div.setAttribute('id', 'div_' + nextColumnIndex);
        div.appendChild(document.createTextNode(selectedChoice));
        tdb.appendChild(div);
        changeClass(chkbox, nextColumnIndex);
    //    alert(tdCount);
    //    alert(nextColumnIndex);
    //    if (tdCount != nextColumnIndex) {
    //        removeTdClass(nextColumnIndex, tdCount);
    //    }
    //    alert(nextColumnIndex);
        nextColumnIndex++;
    }

    function selectCheckBox(index) {
        var x = document.getElementById(index);
        var td = $('td_' + index + '_b');
        x.checked = true;
        td.addClassName("selected");
    }

    function changeTdClass(index) {
    //    alert(index-1);
        var ind = index;
    //           alert(ind);
        var x = $('' + ind);
    //    alert(x);
        var td = $('td_' + ind + '_b');
        var div = $('div1_' + ind);
    //    alert(td);
        if (x.checked) {
    //            alert("checked");
            x.checked = false;
    //            document.getElementById("img_"+index).style.display = "none";
            $(td).removeClassName("selected");
            $(td).addClassName('');
            div.removeClassName('check');
            div.addClassName('hideTd');
        } else {
    //            alert("uncheck");
    //                document.getElementById("img_" + index).style.display = "block";
            x.checked = true;
            td.removeClassName("");
            td.addClassName("selected");
            div.addClassName('check');
        }
    }

    function removeTdClass(index, count) {
        var columnIndex = index + 1;
    //            alert("a");
        while (columnIndex <= count) {
    //                alert(columnIndex);
    //                alert(count);
            $('td_' + columnIndex + '_b').removeClassName('buttonLook');
    //        $('td_' + columnIndex + '_b').addClassName('hideTd');
            columnIndex++;
        }
    }

    function addHoverClass(index) {
    //    alert(1);
        var columnIndex = index;
        $('td_' + columnIndex + '_b').addClassName('tdHoverLook');
    }
    function removeHoverClass(index) {
        var columnIndex = index;
        $('td_' + columnIndex + '_b').removeClassName('tdHoverLook');
    }

    Event.observe(window, "load", function() {
        initializeAutoCompleter()
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
        if(($F('participantSymptomInput'))!= greeting){
            alertForAdd();
            return;
        }
        document.myForm.direction.value = direction;
        document.myForm.submit();
    }

    function sendConfirmedSymptom() {
        var selectedValueNew = escape($('participantSymptomInput').value);
        addSymptom(selectedValueNew);
        closeWindow();
    }
    </script>
</head>
<body>
<chrome:box autopad="true" message="false">
    <p style="font-size:18px">
        <b><tags:message code="participant.form.typesymptom"/></b>
    </p>

    <div id="keyboardDiv"></div>
    <br/>
    <div class="yui-skin-sam">
        <c:set value=" ${sessionScope['org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE']}"
               var="lang"/>
        <table cellspacing="10px;" border="0">
            <tr>
                <td width="80%">
                    <div id="participantSymptomAutoComplete">
                        <input id="participantSymptomInput" type="text">

                        <div id="participantSymptomContainer"></div>
                    </div>
                </td>
                <td>
                    <spring:message code="add" var="add"/>
                    <a onclick="javascript:addNewSymptom($('participantSymptomInput').value)"
                       class="btn green-med"><span><spring:message code="add"/></span></a>

                    <!--  <spring:message code="add" var="add"/>
                    <tags:button onclick="javascript:addNewSymptom($('participantSymptomInput').value)"
                                 icon="add"
                                 size="big" color="blue" value="${add}" markupWithTag="a"/>-->
                </td>
                <td>
                    <spring:message code="clear" var="clear"/>
                    <a onclick="javascript:clearInput()" class="btn red-med"><span><spring:message code="clear"/></span></a>

                    <!--  <spring:message code="clear" var="clear"/>
                    <tags:button onclick="javascript:clearInput()"
                                 icon="x"
                                 size="small" color="blue" value="${clear}" markupWithTag="a"/> -->
                </td>
            </tr>
        </table>

    </div>

    <div class="row" style="margin-left: 15px;font-size:20px">
        <input id='usevirtualkeyboard' type="checkbox"
               onclick="showVirtualKeyBoard(this,'participantSymptomInput');">
         <img src="/proctcae/images/keyboard-icon.png"/>
        <spring:message code="virtualKeyboard"/>

    </div>
    <ctcae:form method="post" name="myForm">
        <table id="mytable" width="100%" border="0">
            <tbody>
            <tr>
                    <%--<td class="" style="vertical-align:top" width="1%"></td>--%>
                <td width="32%" colspan="1">
                        <%--<td class="" style="vertical-align:top" width="1%"></td>--%>
                <td width="32%" colspan="1">
                        <%--<td class="" style="vertical-align:top" width="1%"></td>--%>
                <td width="32%" colspan="1">
            </tr>
                <%--<c:set var="numrows" value="${numrows}"/>--%>
                <%--<c:set var="displaySymptoms" value="${command.displaySymptoms}"/>--%>
                <%--<c:forEach var="i" begin="0" end="${numrows}" varStatus="status">--%>
                <%--<c:if test="${displaySymptoms[i*3+0] ne null}">--%>
                <%--<tr id="tr_"${i}>--%>
                <%--<c:forEach var="j" begin="0" end="2" varStatus="status">--%>
                <%--<td id="td_${i + (numrows+1)*j}_a" class="" style="vertical-align:top" width="1%">--%>
                <%--<c:if test="${displaySymptoms[i + (numrows+1)*j] ne null}">--%>
                <%--<input type="checkbox" name="symptomsByParticipants"--%>
                <%--value="${displaySymptoms[i + (numrows+1)*j]}"--%>
                <%--onclick="javascript:changeClass(this,'${i + (numrows+1)*j}');"--%>
                <%--id="${i + (numrows+1)*j}"/>--%>
                <%--</c:if>--%>
                <%--</td>--%>
                <%--<td id="td_${i + (numrows+1)*j}_b" class="value label" width="32%">--%>
                <%--<c:if test="${displaySymptoms[i + (numrows+1)*j] ne null}">--%>
                <%--<div id="div_${i + (numrows+1)*j}"--%>
                <%--class="label">${displaySymptoms[i + (numrows+1)*j]}</div>--%>
                <%--</c:if>--%>
                <%--</td>--%>
                <%--</c:forEach>--%>
                <%--</tr>--%>
                <%--</c:if>--%>
                <%--</c:forEach>--%>
            </tbody>
        </table>
        <input type="hidden" name="direction"/>
    </ctcae:form>
</chrome:box>

<table width="100%" cellspacing="10">
    <tr>
        <td align="right" width="50%">
            <spring:message code="back" var="back"/>
            <a href="#" class="btn big-blue-left" onclick="javascript:submitForm('back')"><span>${back}</span></a>
        </td>
        <td align="left" width="50%">
            <spring:message code="next" var="next"/>
            <a href="#" class="btn huge-green" onclick="javascript:submitForm('continue')"><span>${next}</span></a>
        </td>
    </tr>
</table>


</body>
</html>