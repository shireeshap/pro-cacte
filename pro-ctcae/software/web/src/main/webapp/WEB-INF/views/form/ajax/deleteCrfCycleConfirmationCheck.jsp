<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>

	<chrome:box title="form.button.delete.cycle">
		<p>&nbsp;&nbsp;&nbsp;&nbsp;<tags:message code="form.button.delete.cycle.instructions"/></p>
		<c:set var="delete">
			<tags:message code="label.delete"/>
		</c:set>
		<c:set var="cancel">
			<tags:message code="label.cancel"/>
		</c:set>
		
		<div class="flow-buttons">
		    <tags:button type="button" id="flow-update" cssClass="next" value="${delete}"
		           icon="x" color="red" onclick="deleteCycleConfirm('${crfCycleIndex}')" overRideStyle=" position:relative; top:0px;"/>
		    <tags:button type="button" id="flow-cancel" cssClass="previous ibutton" value="${cancel}"
		           color="blue" onclick="closeWindow()"/>
		</div>
    </chrome:box>
