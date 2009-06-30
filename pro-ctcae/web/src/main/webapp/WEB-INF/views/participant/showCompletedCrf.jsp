<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>

<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>


<html>
<head>

</head>
<body>
<div id="completedCrfTable">
    <c:set var="myindex" value="1"/>
    <table width="100%" cellpadding="3px" cellspacing="0px" border="0">
                        <%--<tr>--%>
                        <%--<td colspan="5">--%>
                        <%--Study: ${commmand.studyParticipantCrf.studyParticipantAssignment.studySite.study.displayName}--%>
                        <%--</td>--%>
                        <%--</tr>--%>
                    <c:set var="myindex" value="1"/>
                    <c:forEach items="${completedCrf.symptomItems}" var="symptom">
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

                                        <td>
                                            Answer: <u><i>${items[0].proCtcValidValue.value}</i></u>
                                        </td>

                                </tr>
                            <c:set var="myindex" value="${myindex + 1}"/>
                        </c:forEach>
                    </c:forEach>

                </table>



    <c:forEach items="${completedCrf.studyParticipantCrfScheduleAddedQuestions}" var="spCrfAddedQuestion">

        <c:if test="${spCrfAddedQuestion.proCtcQuestion.questionText ne null}">
            <chrome:box>
                <b>${myindex}. ${spCrfAddedQuestion.proCtcQuestion.questionText}</b>
                <br/>
                Answer: <u><i>${spCrfAddedQuestion.proCtcValidValue.value}</i></u>
                <c:set var="myindex" value="${myindex + 1}"/>
            </chrome:box>
        </c:if>
    </c:forEach>

</div>

</body>
</html>