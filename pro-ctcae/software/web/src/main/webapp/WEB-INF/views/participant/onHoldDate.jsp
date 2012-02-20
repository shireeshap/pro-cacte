<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<body>

<ctcae:form method="post">
	<input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}" />
	
    <chrome:box title="participant.label.on_hold_date">
        <chrome:division>

            <div id="offTreatment">
                <div >
                    <div><spring:message code='participant.onHold'/></div>
                </div>
                    <spring:message code='participant.onHold1'/><strong>${command.participant.displayName}</strong>
                     <spring:message code='participant.onHold2'/>
                    <tags:renderDate propertyName="onHoldTreatmentDate"
                                     displayName="participant.label.on_hold_date" required="true"/>
                

            </div>
                   
            <div class="flow-buttons">
                 <spring:message code="participant.button.beginHold" var="beginHold"/>
                <tags:button type="submit" id="flow-update"
                             cssClass="next" value="${beginHold}" icon="check" color="orange" markupWithTag="a" onclick="validateAndSubmit($('onHoldTreatmentDate').value,document.forms[0])"/>

                <spring:message code="participant.button.cancel" var="cancel"/>
                <tags:button type="button" id="flow-cancel"
                             cssClass="previous ibutton" value="${cancel}" icon="x" color="red"
                             onclick="closeWindow()"/>
            </div>


        </chrome:division>
    </chrome:box>
</ctcae:form>
</body>