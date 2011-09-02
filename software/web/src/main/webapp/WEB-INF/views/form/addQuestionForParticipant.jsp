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
            max-height: 20em;
            overflow: auto;
            overflow-x: hidden; /* set scrolling */
            _height: 30em; /* ie6 */
        }

        #mytable td .label {

        }

        #mytable td .label p {
            margin: auto;
            font-size: 20px;

        }

        #mytable td {
            height: 65px;
            background: #d6d6d6 url(../../images/lightgray-tall.png) repeat-x top;
            border: 1px solid lightgray;
            padding: 5px 15px 5px 40px;
            -moz-border-radius: 8px;
            -webkit-border-radius: 8px;
            border-radius: 8px;
            text-shadow: 0 1px white;
            vertical-align: middle;
        }

        #mytable td:hover {
            background: #d7ffb0 url(../../images/lightblue-tall.png) repeat;
            color: #245808;
            text-shadow: 0 1px white;
            cursor: pointer;
            border-color: #86bc56;
        }

        #mytable td input {
            display: none;
        }

        #mytable td.selected {
            background: #538f32 url(/proctcae/images/green-selected_tall.png) repeat-x top;
             /*background-image: url("/proctcae/images/check-icon.png");*/
            /*background: url(/proctcae/images/green-selected.png) 2px 0;*/
            color: white;
            text-shadow: 0 -1px #2a6f04;
            cursor: pointer;
            border-color: #538f32;
        }

        #mytable td.selected .check {
             background: url(/proctcae/images/check-icon.png) no-repeat 0 50%;
            height: 100%;
            margin-right: 7px;
            float: left;
        }

        p {
            font-size: 18px;
        }

    </style>
    <tags:includeScriptaculous/>
    <tags:dwrJavascriptLink objects="scheduleCrf"/>
    <script type="text/javascript">
        var nextColumnIndex = ${fn:length(command.displaySymptoms)};

        function clearInput() {
            $('participantSymptomInput').clear();
        }

        function changeClass(index) {
            var x = document.getElementById(index);
            var td = $('td_' + index + '_b');
            if (x.checked) {
//            alert(1);
                x.checked = false;
            document.getElementById("img_"+index).style.display = "none";
                $(td).removeClassName("selected");
                $(td).addClassName('');
            } else {
//            alert(2);
                document.getElementById("img_" + index).style.display = "block";
                x.checked = true;
                td.removeClassName("");
                td.addClassName("selected");
            }

        }
        function submitForm(direction) {
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
    <form:form method="post" name="myForm">
        <p>
            <b><tags:message code="participant.form.selectsymptom"/></b>
        </p>
        <table id="mytable" cellspacing="13">
            <tbody>
            <c:set var="numrows" value="${numrows}"/>
            <c:set var="displaySymptoms" value="${command.displaySymptoms}"/>
            <c:forEach var="i" begin="0" end="${numrows}" varStatus="status">
                <c:if test="${displaySymptoms[i*3+0] ne null}">
                    <tr id="tr_"${i}>
                        <c:forEach var="j" begin="0" end="2" varStatus="status">
                            <c:if test="${displaySymptoms[i + (numrows+1)*j] ne null}">
                                <td id="td_${i + (numrows+1)*j}_b" class="" width="32%"
                                    onclick="javascript:changeClass('${i + (numrows+1)*j}');">

                                    <input type="checkbox" name="symptomsByParticipants"
                                           value="${displaySymptoms[i + (numrows+1)*j]}"

                                           id="${i + (numrows+1)*j}"/>
                                        <%--</c:if>--%>
                                        <%--</td>--%>
                                        <%--<td id="td_${i + (numrows+1)*j}_b" class="value label" width="32%">--%>
                                        <%--<c:if test="${displaySymptoms[i + (numrows+1)*j] ne null}">--%>

                                    <div id="div_${i + (numrows+1)*j}" class="label">
                                          <div id="img_${i + (numrows+1)*j}" class="check" style="display:block; width:15px">&nbsp; </div>
                                        <%--<img id="img_${i + (numrows+1)*j}" src="/proctcae/images/check-icon.png"--%>
                                             <%--alt="/proctcae/images/check-icon.png" style="display:none">--%>

                                      <p>${displaySymptoms[i + (numrows+1)*j]}</p>

                                    </div>
                                </td>
                            </c:if>
                        </c:forEach>
                    </tr>
                </c:if>
            </c:forEach>
            </tbody>
        </table>
        <input type="hidden" name="direction"/>
    </form:form>
    <br/>

 <!--   <p>
        <b><tags:message code="participant.selectsymptom.footer"/></b>
    </p> -->
    <%--<p>--%>
    <%--<b><tags:message code="participant.form.typesymptom"/></b>--%>
    <%--</p>--%>
    <%--<br/>--%>

    <%--<div id="keyboardDiv"></div>--%>
    <%--<br/>--%>

    <%--<div class="yui-skin-sam">--%>
    <%--<table cellspacing="10px;">--%>
    <%--<tr>--%>
    <%--<td>--%>
    <%--<div id="participantSymptomAutoComplete">--%>
    <%--<input id="participantSymptomInput" type="text">--%>
    <%----%>
    <%--<div id="participantSymptomContainer"></div>--%>
    <%--</div>--%>
    <%--</td>--%>
    <%--<td>--%>
    <%--<tags:button onclick="javascript:addNewSymptom($('participantSymptomInput').value)"--%>
    <%--icon="add"--%>
    <%--size="small" color="blue" value="add" markupWithTag="a"/>--%>
    <%--</td>--%>
    <%--<td>--%>
    <%--<tags:button onclick="javascript:clearInput()"--%>
    <%--icon="x"--%>
    <%--size="small" color="blue" value="clear" markupWithTag="a"/>--%>
    <%--</td>--%>
    <%--</tr>--%>
    <%--</table>--%>
    <%----%>
    <%--</div>--%>

    <%--<div class="row">--%>
    <%--<input id='usevirtualkeyboard' type="checkbox"--%>
    <%--onclick="showVirtualKeyBoard(this,'participantSymptomInput');">&nbsp;Use virtual--%>
    <%--keyboard--%>
    <%--</div>--%>

</chrome:box>

<table width="100%" cellspacing="10">
    <tr>
        <td align="right" width="50%">
        	<spring:message code="back" var="back" />
            <a href="#" class="btn big-blue-left" onclick="javascript:submitForm('back')"><span>${back}</span></a>
        </td>
        <td align="left" width="50%">
        	<spring:message code="next" var="next" />
            <a href="#" class="btn huge-green" onclick="javascript:submitForm('continue')"><span>${next}</span></a>
        </td>
    </tr>
</table>


</body>
</html>