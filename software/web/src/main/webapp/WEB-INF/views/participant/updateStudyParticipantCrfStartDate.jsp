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
<div style="width:650px;">
    <ctcae:form method="post">
    	<input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}" />
    
        <div id="releaseForm">
            <div style="border:1px solid #ccc; height:85px; padding:9px; margin-bottom:10px;">
                <img src="<chrome:imageUrl name="../blue/stop_sign_small.png" />" alt="Stop!"
                     style="float:left; margin-right:15px; margin-left:50px;"/>

                <div style="font-size:20px; margin-bottom:5px;">Are you sure you want to change the start date for this
                    form and participant assignment?
                </div>
                <div>Changing the start date will remove all forms assigned to this participant and create them
                    according to the new date.
                </div>
            </div>
            Please provide a new date for this form
            <tags:renderDate propertyName="effectiveStartDate"
                             displayName="form.label.effective_start_date" required="true" noForm="true"
                             doNotshowLabel="true"/>
        </div>
        <br>

        <div class="flow-buttons">
            <tags:button color="orange" type="submit" id="flow-update"
                         cssClass="next" value="Save" icon="check"
                    />
            <tags:button color="blue" type="button" id="flow-cancel"
                         cssClass="previous ibutton" value="Cancel" icon="x"
                         onclick="closeWindow()"/>
        </div>
    </ctcae:form>
</div>

