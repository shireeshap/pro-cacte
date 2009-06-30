<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<html>
<head>

</head>
<body>
<chrome:box>
    <form:form method="post" name="myForm">
        <b>Please think back ${command.studyParticipantCrf.crf.recallPeriod}</b>
        <div id="inputResponses">
            <table width="100%" cellpadding="3px" cellspacing="0px" border="0">
                    <%--<tr>--%>
                    <%--<td colspan="5">--%>
                    <%--Study: ${commmand.studyParticipantCrf.studyParticipantAssignment.studySite.study.displayName}--%>
                    <%--</td>--%>
                    <%--</tr>--%>
                <c:set var="myindex" value="1"/>
                <c:forEach items="${command.symptomItems}" var="symptom">
                    <tr>
                        <td><br/></td>
                    </tr>
                    <tr style="background-color:#cccccc;">
                        <td colspan="5">
                            <b>${symptom.key.term} </b>
                        </td>
                    </tr>
                    <c:forEach items="${symptom.value}" var="items">
                            <tr>
                                <td colspan="5">
                                    <b>${myindex}. ${items[0].crfPageItem.proCtcQuestion.questionText}</b>
                                </td>
                            </tr>
                            <tr>
                                <c:forEach items="${items[0].crfPageItem.proCtcQuestion.validValues}" var="validValue">
                                    <td>
                                        <input name="studyParticipantCrfItems[${items[1]}].proCtcValidValue" type="radio"
                                               value="${validValue.id}"> ${validValue} &nbsp;&nbsp;
                                    </td>
                                </c:forEach>
                            </tr>
                        <c:set var="myindex" value="${myindex + 1}"/>
                    </c:forEach>
                </c:forEach>

            </table>
        </div>
        <table width="100%" style="margin-top:10px;">
            <tr>
                <td align="right">
                    <!--<input type="submit" id="save" value="save"/>-->
                    <tags:button color="blue" type="submit" id="flow-update"
                                 cssClass="next" value="Submit" icon="save"/>
                </td>
            </tr>
        </table>
    </form:form>
</chrome:box>

</body>
</html>