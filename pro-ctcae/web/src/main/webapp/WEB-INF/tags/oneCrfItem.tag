<%@ attribute name="index" required="true" %>
<%@ attribute name="crfItem" type="gov.nih.nci.ctcae.core.domain.CrfItem" required="true" %>

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


<div class="sortable makeDraggable" id="sortable_${crfItem.proCtcQuestion.id}">
	<tags:formbuilderBoxControls delete="true" properties="true" proCtcQuestionId="${crfItem.proCtcQuestion.id}"/>
	<table class="formbuilderboxTable">
		<tr>
			<td class="TL"></td>
			<td class="T"></td>
			<td class="TR"></td>
		</tr>
		<tr>
			<td class="L"></td>
			<td class="formbuilderboxContent">
				<img class="arrow" alt="" src="<tags:imageUrl name="arrow.png"/>"
						 id="arrow_${crfItem.proCtcQuestion.id}" style="display:none;" />
				   
				<div id="${crfItem.proCtcQuestion.proCtcTerm.id}" class="selectedProCtcTerm"
					 style="display:none;"></div>

				<div id="${crfItem.displayOrder}" class="sortableSpan">${crfItem.displayOrder}</div>
				${crfItem.proCtcQuestion.shortText}
				
				<img class="arrow" alt="" src="<tags:imageUrl name="ajax-loading.gif"/>"
										 id="conditionsImage_${crfItem.proCtcQuestion.id}" style="display:none;"/>

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
<div id="questionPropertiesDiv_${crfItem.proCtcQuestion.id}">
	<div id="questionProperties_${crfItem.proCtcQuestion.id}" style="display:none;" class="questionProperties">
		<chrome:box title="crfItem.label.properties">
			<noform:renderTextArea propertyName="studyCrf.crf.crfItems[${index}].instructions"
								   displayName="crfItem.label.instructions"
								   propertyValue="${crfItem.instructions}"></noform:renderTextArea>

			<noform:renderRadio propertyName="studyCrf.crf.crfItems[${index}].responseRequired"
								displayName="crfItem.label.response_required"
								propertyValue="${crfItem.responseRequired}" items="${responseRequired}"
								questionId="${crfItem.proCtcQuestion.id}">

			</noform:renderRadio>


			<noform:renderRadio
				propertyName="studyCrf.crf.crfItems[${index}].crfItemAllignment" displayName="crfItem.label.allignment"
				propertyValue="${crfItem.crfItemAllignment}" items="${crfItemAllignments}"
				questionId="${crfItem.proCtcQuestion.id}">

			</noform:renderRadio>


		</chrome:box>


		<chrome:box title="form.conditional_question">
			<tags:instructions code="instruction_conditional_question"/>

			<div align="left" style="margin-left: 50px">
				<table width="95%" class="tablecontent"
					   id="conditionsTable_${crfItem.proCtcQuestion.id}" style="display:none;">
					<tr id="ss-table-head" class="amendment-table-head">
						<th width="95%" class="tableHeader"><tags:message
							code='crfItem.label.conditions'/></th>
						<th width="5%" class="tableHeader" style=" background-color: none">&nbsp;</th>

					</tr>

					<tags:conditions crfItemDisplayRuleList="${crfItem.crfItemDisplayRules}"
									 selectedQuestionId="${crfItem.proCtcQuestion.id}"></tags:conditions>


					<tr id="conditions_${crfItem.proCtcQuestion.id}"></tr>

				</table>

			</div>
			 <br>
			 <br>
			<div>
				<select name="switchTriggerSelect" id="selectedCrfItems_${crfItem.proCtcQuestion.id}" multiple=""
						size="20" class="selectedCrfItems">
					<option value="">Please select..</option>
					<c:forEach items="${selectedCrfItems}" var="selectedCrfItem">

						<optgroup label="${selectedCrfItem.proCtcQuestion.shortText}">
							<c:forEach items="${selectedCrfItem.proCtcQuestion.validValues}" var="validValue">
								<option value="${validValue.id}">${validValue.displayName}</option>
							</c:forEach>
						</optgroup>

					</c:forEach>
				</select>
			</div>
			<br>
			<input type="button" value="Add Conditions" onClick="javascript:addConditionalQuestion('${crfItem.proCtcQuestion.id}',
			$F('selectedCrfItems_${crfItem.proCtcQuestion.id}'))" class="button"/>
		</chrome:box>

		<%--<div id="previewQuestion" class="review">--%>
		<%--<tags:questionReview crfItem="${crfItem}" showInstructions="false" displayOrder="${crfItem.displayOrder}"/>--%>
		<%--<br>--%>
		<%--<br>--%>
		<%--</div>--%>
	</div>
</div>
