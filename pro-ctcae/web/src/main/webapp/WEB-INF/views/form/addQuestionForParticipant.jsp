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
        tr{
            border-bottom:1px solid #123121;
        }
    </style>
</head>
<body>
<chrome:flashMessage flashMessage="${command.flashMessage}"></chrome:flashMessage>
<form:form method="post" name="myForm">
    <chrome:box title="Form: ${command.studyParticipantCrfSchedule.studyParticipantCrf.crf.title}"
                autopad="true" message="false">
        <p>
            <b>Please select additional symptoms from below:</b>
        </p>

        <table>
            <c:forEach items="${command.sortedSymptoms}" var="term" varStatus="status">
                <c:if test="${status.index%3==0}">
                    <tr>
                </c:if>
                <td width="33%">
                    <input type="checkbox" name="symptomsByParticipants"
                           value="${term}"/> ${term}
                </td>
                <c:if test="${status.index%3==2}">
                    </tr>
                </c:if>
            </c:forEach>
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