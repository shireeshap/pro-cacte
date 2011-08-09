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
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <tags:javascriptLink name="submit_btn_animation"/>
    <style type="text/css">
        div.row div.value {
            white-space: normal;
        }

        #left-panel {
            padding-left: 15px;
            width: 100%;
            float: left;
            font-size: 17px;
        }

        #left-panel a {
            text-decoration: none;
        }

        #left-panel a:hover {
            text-decoration: underline;
        }

        h2 {
            font-weight: normal;
        }

        #left-panel h1 {
            font-size: 2em;
        }

    </style>
    <script type="text/javascript">
        var alreadySubmitted = false;
        function submitForm(direction) {
            if (!alreadySubmitted) {
                alreadySubmitted = true;
                document.myForm.direction.value = direction;
                document.myForm.submit();
            }
        }
    </script>
</head>
<body>
<form:form method="post" name="myForm">
    <div style="clear:both;">
        <div id="left-panel">
            <h1>Form: ${command.schedule.studyParticipantCrf.crf.title}</h1>
            <spring:message code="thankYouMessage" />
            <br/>
            <br/>


        </div>
        <br/>&nbsp;<br/>&nbsp;<br/>
        <table width="100%" cellspacing="10">
            <input type="hidden" name="direction"/>
            <input type="hidden" name="r"/>
            <tr>
                <td align="right" width="50%">
                	<spring:message code="back" var="back"/>
                    <a href="#" class="btn big-blue-left" onclick="javascript:submitForm('back')"><span>${back}</span></a>
                </td>
                <td align="left" width="50%">
                	<spring:message code="submit" var="submit"/>
                    <a href="#" class="btn huge-green" onclick="javascript:submitForm('save')"><span>${submit}</span></a>
                </td>
            </tr>
        </table>
    </div>
</form:form>
</body>
</html>