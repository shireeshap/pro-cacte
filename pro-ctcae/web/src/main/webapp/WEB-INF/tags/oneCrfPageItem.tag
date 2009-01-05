<%@ attribute name="index" required="true" %>
<%@ attribute name="crfPageItem" type="gov.nih.nci.ctcae.core.domain.CrfPageItem" required="true" %>
<%@ attribute name="crfPageNumber" required="true" %>

<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@taglib prefix="noform" tagdir="/WEB-INF/tags/noform" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<script type="text/javascript">
	updateSelectedCrfItems('${crfPageItem.proCtcQuestion.id}')
</script>
<div class="sortable makeDraggable" id="sortable_${crfPageItem.proCtcQuestion.id}">
	<tags:formbuilderBoxControls delete="true" properties="true" proCtcQuestionId="${crfPageItem.proCtcQuestion.id}"/>
	<table class="formbuilderboxTable">
		<tr>
			<td class="TL"></td>
			<td class="T"></td>
			<td class="TR"></td>
		</tr>
		<tr>
			<td class="L"></td>
			<td class="formbuilderboxContent">
				<%--<img class="arrow" alt="" src="<tags:imageUrl name="arrow.png"/>"
						 id="arrow_${crfPageItem.proCtcQuestion.id}" style="display:none;" />--%>

				<div id="${crfPageItem.proCtcQuestion.proCtcTerm.id}" class="selectedProCtcTerm"
					 style="display:none;"></div>

				<div id="${crfPageItem.displayOrder}" class="sortableSpan">${crfPageItem.displayOrder}</div>
				${crfPageItem.proCtcQuestion.shortText}

				<img class="arrow" alt="Conditional Question" src="<tags:imageUrl name="blue/conditional-icon.png"/>"
										 id="conditionsImage_${crfPageItem.proCtcQuestion.id}" style="display:none;" />

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
<div id="questionPropertiesDiv_${crfPageItem.proCtcQuestion.id}">
	<div id="questionProperties_${crfPageItem.proCtcQuestion.id}" style="display:none;" class="questionProperties leftBox">
		<span class="propertiesHeader">crfItem.label.properties</span>
			<noform:renderTextArea propertyName="studyCrf.crf.crfPages[${crfPageNumber}].crfPageItems[${index}].instructions"
								   displayName="crfItem.label.instructions"
								   propertyValue="${crfPageItem.instructions}"></noform:renderTextArea>

		<noform:renderRadio
			propertyName="studyCrf.crf.crfPages[${crfPageNumber}].crfPageItems[${index}].responseRequired"
			displayName="crfItem.label.response_required"
			propertyValue="${crfPageItem.responseRequired}" items="${responseRequired}"
			questionId="${crfPageItem.proCtcQuestion.id}">

		</noform:renderRadio>


		<noform:renderRadio
			propertyName="studyCrf.crf.crfPages[${crfPageNumber}].crfPageItems[${index}].crfItemAllignment"
			displayName="crfItem.label.allignment"
			propertyValue="${crfPageItem.crfItemAllignment}" items="${crfItemAllignments}"
			questionId="${crfPageItem.proCtcQuestion.id}">

		</noform:renderRadio>





		<span class="propertiesHeader">form.conditional_question</span>
			<tags:instructions code="instruction_conditional_question"/>

		<div align="left" style="margin-left: 50px">
			<table width="95%" class="tablecontent"
				   id="conditionsTable_${crfPageItem.proCtcQuestion.id}" style="display:none;">
				<tr id="ss-table-head" class="amendment-table-head">
					<th width="95%" class="tableHeader"><tags:message
						code='crfItem.label.conditions'/></th>
					<th width="5%" class="tableHeader" style=" background-color: none">&nbsp;</th>

				</tr>

				<tags:conditions crfItemDisplayRuleList="${crfPageItem.crfItemDisplayRules}"
								 selectedQuestionId="${crfPageItem.proCtcQuestion.id}"
								 showDelete="true"></tags:conditions>


				<tr id="conditions_${crfPageItem.proCtcQuestion.id}"></tr>

			</table>

		</div>
		<br>
		<br>

		<div>
			<select name="switchTriggerSelect" id="selectedCrfPageItems_${crfPageItem.proCtcQuestion.id}" multiple=""
					size="20" class="selectedCrfPageItems">
				<option value="">Please select..</option>
				<c:forEach items="${selectedCrfPageItems}" var="selectedCrfPageItem">

					<optgroup label="${selectedCrfPageItem.proCtcQuestion.shortText}"
							  id="condition_${selectedCrfPageItem.proCtcQuestion.id}" class="conditions">
						<c:forEach items="${selectedCrfPageItem.proCtcQuestion.validValues}" var="validValue">
							<option value="${validValue.id}">${validValue.displayName}</option>
						</c:forEach>
					</optgroup>

				</c:forEach>
			</select>
		</div>
		<br>
		<input type="button" value="Add Conditions" onClick="javascript:addConditionalQuestion('${crfPageItem.proCtcQuestion.id}',
			$F('selectedCrfPageItems_${crfPageItem.proCtcQuestion.id}'))" class="button"/>


		<%--<div id="previewQuestion" class="review">--%>
		<%--<tags:questionReview crfItem="${crfItem}" showInstructions="false" displayOrder="${crfItem.displayOrder}"/>--%>
		<%--<br>--%>
		<%--<br>--%>
		<%--</div>--%>
	</div>
</div>
