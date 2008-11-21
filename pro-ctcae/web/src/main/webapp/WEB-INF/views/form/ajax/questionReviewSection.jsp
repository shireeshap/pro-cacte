<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<br><br>
<c:if test="${previousQuestionIndex ne null }">
    <a id="previousLink" href="javascript:previousQuestion(${previousQuestionIndex})">Previous</a>

</c:if>
<c:if test="${nextQuestionIndex ne null }">
    <a id="nextLink" href="javascript:nextQuestion(${nextQuestionIndex})">Next</a>

</c:if>
<br><br>

<div>
    <tags:questionReview crfItem="${crfItem}" showInstructions="true"></tags:questionReview>

</div>
