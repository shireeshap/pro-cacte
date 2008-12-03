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
        div.row div.value {
            white-space: normal;
        }

        .label {
            font-weight: bold;
            float: left;
            margin-left: 0.5em;
            margin-right: 0.5em;
            padding: 1px;
            font-size: 12px;
        }
    </style>

</head>
<body>
<chrome:flashMessage flashMessage="${command.flashMessage}"></chrome:flashMessage>
<form:form method="post" name="myForm">
<chrome:box title="Form: ${command.studyParticipantCrfSchedule.studyParticipantCrf.studyCrf.crf.title}"
                autopad="true" message="false">

    <chrome:division title="Submit">
        <div class="label">
            <p>
                You are about to submit the form. You can not change the responses after submission.
                <br/>
                You can <a href="">click here</a> to review your responses and make changes.
            </p>
        </div>
    </chrome:division>

    <table width="100%">
        <input type="hidden" name="direction"/>
        <tr align="right">
            <td>
                <input onclick="document.myForm.direction.value='save'" type="image"
                       src="/ctcae/images/blue/submit_btn.png" alt="save &raquo;"/>
            </td>
        </tr>
    </table>

</chrome:box>
</form:form>
</body>
</html>