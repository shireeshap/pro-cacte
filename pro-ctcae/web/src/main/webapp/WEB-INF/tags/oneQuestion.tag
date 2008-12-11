<%@ attribute name="proCtcQuestion" type="gov.nih.nci.ctcae.core.domain.ProCtcQuestion" required="true" %>
<%@ attribute name="displayOrder" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<div class="sortable makeDraggable" id="sortable_${proCtcQuestion.id}">
	<tags:formbuilderBoxControls delete="true" properties="true" proCtcQuestionId="${proCtcQuestion.id}"/>
	<table class="formbuilderboxTable">
		<tr>
			<td class="TL"></td>
			<td class="T"></td>
			<td class="TR"></td>
		</tr>
		<tr>
			<td class="L"></td>
			<td class="formbuilderboxContent">
				<div id="${proCtcQuestion.proCtcTerm.id}" class="selectedProCtcTerm"
					 style="display:none;"></div>

				<div id="${displayOrder}" class="sortableSpan">${displayOrder}</div>
				${proCtcQuestion.questionText}
			</td>
			<td class="R"></td>
		</tr>
		<tr>
			<td class="BL"></td>
			<td class="B"></td>
			<td class="BR"></td>
		</tr>
	</table>
</div>

