<%@ attribute name="proCtctermId" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@attribute name="proCtcQuestionId" %>
<%@attribute name="add" %>
<%@attribute name="delete" %>
<%@attribute name="properties" %>
<c:if test="${(properties) || (add) || (delete)}">
	<div class="formbuilderBoxControls">
		<div class="formbuilderBoxControls-left">
			<c:if test="${properties}">
				<a href="javascript:showCrfItemPropertiesTab('${proCtcQuestionId}');"><img
					src="/ctcae/images/blue/question_properties_btn.png" alt="Configure Item"/></a>
			</c:if>
			<c:if test="${add}">
				<a href="javascript:addCrfItem(${proCtcQuestionId},${proCtctermId})"><img
					src="/ctcae/images/blue/select_question_btn.png"
					alt="Add"/></a>
			</c:if>
			<c:if test="${delete}">
				<a href="javascript:deleteQuestion('${proCtcQuestionId}');"><img src="/ctcae/images/checkno.gif"
																				 alt="Delete"/></a>
			</c:if>
		</div>
		<div class="formbuilderBoxControls-right"></div>
	</div>
</c:if>