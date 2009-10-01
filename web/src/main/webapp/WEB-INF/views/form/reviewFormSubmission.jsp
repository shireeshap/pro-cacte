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
        function goback() {
            document.myForm.direction.value = 'back_review';
            document.myForm.r.value = 'n';
            document.myForm.submit();
        }
    </script>
</head>
<body>
<form:form method="post" name="myForm">
    <div style="clear:both;">
        <div id="left-panel">
            <h1>Form: ${command.studyParticipantCrfSchedule.studyParticipantCrf.crf.title}</h1>
            Thank you for completing the questionnaire. If you would like to make any changes to your answers, please
            use the back button. If you are satisfied with your answers, please press the submit button below.
            <br/>
            <br/>


        </div>
        <br/>&nbsp;<br/>&nbsp;<br/>
        <table width="100%">
            <input type="hidden" name="direction"/>
            <input type="hidden" name="r"/>
            <tr>
                <td align="left" width="50%">
                    <tags:button onclick="document.myForm.direction.value='back'" type="submit"
                            value="Back" icon="back" color="blue"/>
                </td>
                <td align="right" width="50%">
                    <tags:button onclick="document.myForm.direction.value='save'" type="submit"
                           value="Submit" icon="check" color="orange"/>
                </td>
            </tr>
        </table>
    </div>
</form:form>
</body>
</html>