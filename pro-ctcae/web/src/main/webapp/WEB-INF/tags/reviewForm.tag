<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="display" uri="http://displaytag.sf.net/el" %>
<%@taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ attribute name="studyCrf" type="gov.nih.nci.ctcae.core.domain.StudyCrf" %>


<div class="instructions">
	<div class="summarylabel"><tags:message code='form.label.study'/></div>
	<div class="summaryvalue">${studyCrf.study.displayName}</div>
</div>


<div class="instructions">

	<div class="summarylabel"><tags:message code='form.label.title'/></div>
	<div class="summaryvalue">${studyCrf.crf.title}</div>
</div>
<br>

<table id="formbuilderTable">
	<tr>
		<td id="left">
			<div class="instructions">
				<div class="summarylabel"><tags:message code='form.label.questions'/></div>
			</div>

			<c:forEach items="${studyCrf.crf.crfPages}" var="crfPage">
				<chrome:box title="${crfPage.description}">
					<c:forEach items="${crfPage.crfItemsSortedByDislayOrder}" var="crfPageItem">
						<tags:questionReview crfPageItem="${crfPageItem}" showInstructions="true"
											 displayOrder="${crfPageItem.displayOrder}"></tags:questionReview>
					</c:forEach>
					<br>
				</chrome:box>
			</c:forEach>

		</td>
	</tr>

</table>

<br>
<br>
