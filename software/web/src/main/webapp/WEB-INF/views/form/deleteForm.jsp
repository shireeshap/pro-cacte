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

<style>
	.message_style{
	margin-left : 12px;
	margin-top: 12px;
	}
	
	.p_style{
	padding: 0.1em;
	}
</style>

<div style="width:590px;">
    <ctcae:form method="post">
    	<input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}" />
    
        <chrome:box title="form.label.delete_form" omitBorders="true">
                <div id="releaseForm" class="message_style">
                    <p class="p_style">
                        You are about to delete <strong>${command.title}</strong>.
                    </p>
                    <p class="p_style">
                        You cannot reverse this step.
                        Do you want to continue?
                    </p>
                </div>
                <div class="content flow-buttons autoclear">
                <div class="flow-buttons">
	                <span class="prev">
	                	<tags:button onclick="closeWindow()" color="blue" value="Cancel" markupWithTag="a" icon="x"/>
					</span>
					<span class="next">
						<tags:button type="submit" id="flow-update" color="red" icon="check" value="Delete"/>
					</span>
                </div>
        </chrome:box>
    </ctcae:form>
</div>
