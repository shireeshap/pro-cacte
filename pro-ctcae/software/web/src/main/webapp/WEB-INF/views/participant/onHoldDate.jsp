<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<body>

<form:form method="post">
    <chrome:box title="participant.label.on_hold_date">
        <chrome:division>

            <div id="offTreatment">
                <div >
                    <div>Putting the participant treatment on hold will put all the future form schedules on hold.</div>
                </div>

                    Please enter the date from which the participant <strong>${command.participant.displayName}</strong>
                    surveys will be held.
                    <tags:renderDate propertyName="onHoldTreatmentDate"
                                     displayName="participant.label.on_hold_date" required="true"/>
                

            </div>
                   
            <div class="flow-buttons">

                <tags:button type="submit" id="flow-update"
                             cssClass="next" value="Begin Hold" icon="check" color="orange" markupWithTag="a" onclick="validateAndSubmit($('onHoldTreatmentDate').value,document.forms[0])"/>


                <tags:button type="button" id="flow-cancel"
                             cssClass="previous ibutton" value="Cancel" icon="x" color="red"
                             onclick="closeWindow()"/>
            </div>


        </chrome:division>
    </chrome:box>
</form:form>
</body>