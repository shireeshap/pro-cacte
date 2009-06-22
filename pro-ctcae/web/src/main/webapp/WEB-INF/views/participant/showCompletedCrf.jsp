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
    <c:forEach items="${completedCrf.studyParticipantCrfItems}" var="spCrfItem">
        <c:if test="${spCrfItem.crfPageItem.proCtcQuestion.questionText ne null}">
            <chrome:box>
                <b>${myindex}. ${spCrfItem.crfPageItem.proCtcQuestion.questionText}</b>
                <br/>
                Answer: <u><i>${spCrfItem.proCtcValidValue.value}</i></u>
                <c:set var="myindex" value="${myindex + 1}"/>
            </chrome:box>
        </c:if>
    </c:forEach>

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