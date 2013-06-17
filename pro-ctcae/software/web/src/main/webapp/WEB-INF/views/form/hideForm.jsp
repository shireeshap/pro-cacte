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
<div style="width:590px;">
    <ctcae:form method="post">
    	<input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}" />
    
        <chrome:box title="form.label.hide_form">
            <chrome:division>
                <div id="releaseForm">
                        You are about to hide <strong>${command.title}</strong>.  This will remove access to the form and form data on the system, and the form will be removed from all patient schedules.  If you wish to view this form and the data at a later time, you can choose to unhide the form.
                    <p>
                        Do you want to continue?
                    </p>
                </div>
                <div class="content flow-buttons autoclear">
                <span class="prev" style="position:relative; top:5px;">
                	<tags:button onclick="closeWindow()" color="blue" value="Cancel" markupWithTag="a" icon="x"/>
				</span>
				
				<span class="next" style="position:relative; top:5px; right:5px">
					<tags:button type="submit" id="flow-update" color="red" icon="check" value="Hide"/>
				</span>
            </chrome:division>
        </chrome:box>
    </ctcae:form>
</div>