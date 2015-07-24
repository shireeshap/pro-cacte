<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>

<style>
	.message_style{
		margin-top: 12px;
	}
	
	.flow_button_style{
		margin-top: 62px;
	}
</style>

	<chrome:box title="form.button.delete.cycle" omitBorders="true">
		<div id="message" class="message_style">
			<p>&nbsp;&nbsp;&nbsp;&nbsp;<tags:message code="form.button.delete.cycle.instructions"/></p>
		</div>
		<c:set var="delete">
			<tags:message code="label.delete"/>
		</c:set>
		<c:set var="cancel">
			<tags:message code="label.cancel"/>
		</c:set>
		<div class="flow-buttons flow_button_style">
		    <tags:button type="button" id="flow-update" cssClass="next" value="${delete}"
		           icon="x" color="red" onclick="deleteCycleConfirm('${crfCycleIndex}')" overRideStyle=" position:relative; top:0px;"/>
		    <tags:button type="button" id="flow-cancel" cssClass="previous ibutton" value="${cancel}"
		           color="blue" onclick="closeWindow()"/>
		</div>
    </chrome:box>
