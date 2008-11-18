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
        .label {
            width: 12em;
            padding: 1px;
            margin-right: 0.5em;
        }

        div.row div.value {
            white-space: normal;
        }

        #studyDetails td.label {
            font-weight: bold;
            float: left;
            margin-left: 0.5em;
            margin-right: 0.5em;
            width: 12em;
            padding: 1px;
        }
    </style>

</head>
<body>
<chrome:flashMessage flashMessage="The Form was saved successfully"></chrome:flashMessage>

<chrome:box title="Confirmation">


    <div class="instructions">
        <div class="summarylabel">Study</div>
        <div class="summaryvalue">${studyCrf.study.displayName}</div>
    </div>


    <div class="instructions">

        <div class="summarylabel">Title</div>
        <div class="summaryvalue">${studyCrf.crf.title}</div>
    </div>
    <br>

    <table id="formbuilderTable">
        <tr>
            <td id="left">
                Questions
                <c:forEach items="${studyCrf.crf.crfItems}" var="crfItem">
                    <tags:reviewQuestion crfItem="${crfItem}"></tags:reviewQuestion>
                </c:forEach>

            </td>
        </tr>

    </table>


</chrome:box>

</body>
</html>