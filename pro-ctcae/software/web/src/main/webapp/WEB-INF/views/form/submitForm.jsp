<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    <style type="text/css">
        div.row div.value {
            white-space: normal;
        }

        .formbuilderBox {
            padding-left: 2px;
        }

        .label {
            font-weight: bold;
            margin-left: 0.5em;
            margin-right: 0.5em;
            padding: 1px;
            font-size: 20px;
        }

        .currentPagediv {
            color: #666666;
            font-size: 8pt;
            padding-right: 200px;
            text-align: right;
            margin-bottom: 15px;
        }
    </style>
</head>
<body>
<form:form method="post" name="myForm">
    <tags:hasErrorsMessage hideErrorDetails="false"/>
    <div class='progress-bar-outer'>
        <div class='progress-bar-inner' style="width: ${(command.currentPageIndex/command.totalPages)*150}px;"></div>
    </div>
    <div class="currentPagediv">
        Progress:
    </div>
    <div class="label" style="margin-bottom:10px;">
        <tags:recallPeriodFormatter
                desc="Please think back ${command.studyParticipantCrfSchedule.studyParticipantCrf.crf.recallPeriod}"/>
    </div>
    <c:forEach items="${command.displayQuestions}" var="displayQuestionList">
        <c:forEach items="${displayQuestionList.value}" var="displayQuestion">
            
        </c:forEach>
    </c:forEach>
    <table width="100%" style="margin-top:10px;">
        <input type="hidden" name="direction"/>
        <tr>
            <td align="left" width="50%">
                <c:if test="${command.currentPageIndex gt 1}">
                    <tags:button color="blue" icon="back" onclick="javascript:submitForm('back')" value="Back"/>
                </c:if>
            </td>
            <td align="right" width="50%">
                <c:choose>
                    <c:when test="${command.currentPageIndex le command.totalPages}">
                        <tags:button color="green" icon="next" onclick="javascript:submitForm('continue')"
                                     value="Continue"/>
                    </c:when>
                </c:choose>
            </td>
        </tr>
    </table>

</form:form>
</body>
</html>