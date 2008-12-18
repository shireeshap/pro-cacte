<%-- This is the standard decorator for all caAERS pages --%>
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

<div id="questionProperties_${crfItem.proCtcQuestion.id}" style="display:none;" class="questionProperties">
	<chrome:box title="crfItem.label.properties">
		<noform:renderTextArea propertyName="crfItem.instructions" displayName="crfItem.label.instructions"
							   propertyValue="${crfItem.instructions}"></noform:renderTextArea>

		<noform:renderRadio propertyName="crfItem.responseRequired" displayName="crfItem.label.response_required"
							propertyValue="${crfItem.responseRequired}" items="${responseRequired}">

		</noform:renderRadio>


		<noform:renderRadio
			propertyName="crfItem.crfItemAllignment" displayName="crfItem.label.allignment"
			propertyValue="${crfItem.crfItemAllignment}" items="${crfItemAllignments}">

		</noform:renderRadio>

		<chrome:division title="Conditional Question (optional)">
			This question is displayed only if one or more of the following choices is selected:
		</chrome:division>
	</chrome:box>
	<div id="previewQuestion" class="review">
		<tags:questionReview crfItem="${crfItem}" showInstructions="false"/>

		<select name="switchTriggerSelect" id="switchTriggerSelect" multiple="" size="">
			<option value="">Please select..</option>
			<c:forEach items="${remaingCrfItems}" var="crfItem">

				<optgroup label="${crfItem.proCtcQuestion.displayName}">
					<option value="tfa_Morning">Morning</option>
					<option value="tfa_Noon">Noon</option>
					<option value="tfa_Afternoon">Afternoon</option>
					<option value="tfa_Evening">Evening</option>
					<option value="tfa_Other">Other (click me)</option>
				</optgroup>

			</c:forEach>

			<optgroup label="Membership Level">
				<option value="tfa_Bronze">Bronze ($10/sem.)</option>
				<option value="tfa_Silver20month">Silver ($20/sem.)</option>
				<option value="tfa_Gold40sem">Gold ($50/sem.)</option>
				<option value="tfa_Platinum">Platinum ($90/sem.)</option>
			</optgroup>
			<optgroup label="Validity Period:">
				<option value="tfa_6630078167865">Fall 2007</option>
				<option value="tfa_Winter2007">Winter 2007</option>
				<option value="tfa_Spring2008">Spring 2008</option>
				<option value="tfa_Summer2008">Summer 2008</option>
				<option value="tfa_Fall2008">Fall 2008</option>
			</optgroup>
			<optgroup label="Multiple-choices on the same line:">
				<option value="tfa_OptionA">Option A</option>
				<option value="tfa_OptionB">Option B</option>
				<option value="tfa_OptionC">Option C</option>
				<option value="tfa_OptionD">Option D</option>
				<option value="tfa_OptionE">Option E</option>
			</optgroup>
			<optgroup label="Multiple-choices listed vertically:">
				<option value="tfa_OptionA2">Option A</option>
				<option value="tfa_OptionB2">Option B</option>
				<option value="tfa_OptionC2">Option C</option>
				<option value="tfa_OptionD2">Option D</option>
				<option value="tfa_OptionE2">Option E</option>
			</optgroup>
		</select>
		<br>
		<br>
	</div>

	<div class="flow-buttons">

		<input type="button" id="flow-update"
			   class="next" value="Submit" alt="Save"
			   onclick="submitCrfItemPropertiesWindow(${crfItem.proCtcQuestion.id})"/>


		<input type="button" id="flow-cancel"
			   class="previous ibutton" value="Cancel" alt="Cancel" onclick="closeCrfItemPropertiesWindow()"/>
	</div>
</div>