<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<html>
<head>

    <tags:stylesheetLink name="tabbedflow"/>
    <script type="text/javascript">
    </script>
    <style type="text/css">

        table.top-widget {
            border: 1px solid #cccccc;
            background-color: #E7EAF3;
            text-align: left;
        }

        td {
            text-align: left;
        }

        img {
            border: 0 none;
        }
    </style>
    <script type="text/javascript">
        var totalCalendars = 0;
        var repeatvalues = new Array();
        function changeinput(obj, index, value) {
            if (obj.value == 'Indefinitely') {
                $('div_repeatUntilValue_' + index).innerHTML = '';
            }
            if (obj.value == 'Date') {
                $('div_repeatUntilValue_' + index).innerHTML = '<input id="crf.crfCalendars[' + index + '].repeatUntilAmount" class="date validate-DATE" type="text" value="' + value + '" name="crf.crfCalendars[' + index + '].repeatUntilAmount"/> <a id="-calbutton" href="javascript:none();"> <img height="16" width="17" border="0" align="absmiddle" alt="Calendar" src="/ctcae/images/chrome/b-calendar.gif"/> </a> <i>(mm/dd/yyyy)</i>';
            }
            if (obj.value == 'Number') {
                $('div_repeatUntilValue_' + index).innerHTML = '<input size="3" value="' + value + '" name="crf.crfCalendars[' + index + '].repeatUntilAmount" type="text">';
            }
        }


        function none() {
        }
        Event.observe(window, "load", function () {
            for (var i = 0; i < totalCalendars; i++) {
                var item = document.getElementsByName('crf.crfCalendars[' + i + '].repeatUntilUnit')[0];
                changeinput(item, i, repeatvalues[i]);
            }
        })
    </script>
</head>
<body>
<tags:tabForm tab="${tab}" flow="${flow}" willSave="false" formName="myForm">
<jsp:attribute name="singleFields">
<input type="hidden" name="_finish" value="true"/>
    <div align="left" style="margin-left: 50px">
        <c:forEach items="${command.crf.crfCalendars}" var="crfCalendar" varStatus="status">
            <script>
                repeatvalues[totalCalendars] = '${crfCalendar.repeatUntilAmount}';
                totalCalendars = totalCalendars + 1;
            </script>
            <table class="top-widget" width="100%">
                <tr id="repeatprops">
                    <td>
                        <b>Repeat every</b>
                        <input type="text" size="2"
                               name="crf.crfCalendars[${status.index}].repeatEveryAmount"
                               value="${crfCalendar.repeatEveryAmount}"/>
                        <form:select path="crf.crfCalendars[${status.index}].repeatEveryUnit" items="${repetitionunits}"
                                     itemLabel="desc" itemValue="code"/>

                    </td>
                    <td>
                        <b>Form is due after</b>
                        <input type="text" size="2"
                               name="crf.crfCalendars[${status.index}].dueDateAmount"
                               value="${crfCalendar.dueDateAmount}"/>
                        <form:select path="crf.crfCalendars[${status.index}].dueDateUnit" items="${duedateunits}"
                                     itemLabel="desc" itemValue="code"/>
                    </td>
                <tr>
                    <td>
                    </td>
                    <td>
                        <table>
                            <tr>
                                <td>
                                    <b>Repeat until</b>
                                    <form:select path="crf.crfCalendars[${status.index}].repeatUntilUnit"
                                                 items="${repeatuntilunits}"
                                                 itemLabel="desc" itemValue="code"
                                                 onchange="changeinput(this,'${status.index}','');"/>

                                    </select>
                                </td>
                                <td>
                                    <div id="div_repeatUntilValue_${status.index}">

                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
        </c:forEach>
    </div>
</jsp:attribute>
</tags:tabForm>
</body>
</html>