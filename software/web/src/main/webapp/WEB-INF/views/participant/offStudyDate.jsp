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

    <chrome:box title="participant.label.off_study_date" omitBorders="true">
            <div id="offTreatment">
                <div style="border:1px solid #f00; height:100px; padding:0px; margin-left: 12px; margin-bottom:10px;">
                    <img src="<chrome:imageUrl name="../blue/stop_sign.png" />" alt="Stop!"
                         style="float:left; margin-right:30px; margin-left:55px;"/>

                    <div style="font-size:20px; margin-bottom:5px;"><spring:message code='participant.offStudy'/></div>
                    <div><spring:message code='participant.offStudy1'/></div>
                </div>
                <p style="margin-left: 12px;">
                    <spring:message code='participant.offStudy2'/> <strong>${command.participant.displayName}</strong>
                    <spring:message code='participant.offStudy3'/>
                    <tags:renderDate propertyName="offTreatmentDate"
                                     displayName="participant.label.off_study_date" required="true"/>
                </p>

            </div>

            <div class="flow-buttons" style="margin-left:12px;">
                 <spring:message code="participant.button.assignDate" var="assignDate"/>
                <tags:button type="submit" id="flow-update"
                             cssClass="next" value="${assignDate}" icon="check" color="orange" markupWithTag="a" onclick="CP_NS.validateAndSubmit($('offTreatmentDate').value,document.forms[0])"
                             overRideStyle=" position:relative; top:0px;" />

                 <spring:message code="participant.button.cancel" var="cancel"/>
                <tags:button type="button" id="flow-cancel"
                             cssClass="previous ibutton" value="${cancel}" icon="x" color="red"
                             onclick="closeWindow()"/>
            </div>
    </chrome:box>
</ctcae:form>
</body>