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
</head>
<body>
<form:form method="post" name="myForm">
    <div style="clear:both;">
        <div id="left-panel">
            If you are experiencing any additional symptoms then please <a href="../form/addquestion">Click here</a> to add questions relating to them.
        </div>
        <br/>&nbsp;<br/>&nbsp;<br/>
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
    </div>
</form:form>
</body>
</html>