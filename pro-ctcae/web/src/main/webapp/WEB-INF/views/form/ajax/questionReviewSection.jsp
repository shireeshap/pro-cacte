<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<br>

<div class="flow-buttons">
    <c:if test="${previousQuestionIndex ne null }">
        <input type="button" id="previousLink"
               class="next" value="Previous" alt="Previous"
               onclick="previousQuestion(${previousQuestionIndex})"/>


    </c:if>
    <c:if test="${nextQuestionIndex ne null }">
        <input type="button" id="nextLink"
               class="previous ibutton" value="Next" alt="Next" onclick="nextQuestion(${nextQuestionIndex})"/>
    </c:if>


</div>


<br><br>

<div>
    <%--<chrome:box title="form.label.preview" >--%>

    <tags:questionReview crfPageItem="${crfPageItem}" showInstructions="true"></tags:questionReview>

    <%--</chrome:box>--%>
</div>
