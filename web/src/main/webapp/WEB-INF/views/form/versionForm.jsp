<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>
<%@taglib prefix="ctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div style="width:590px; height:165px;">
    <ctcae:form method="post">
        <chrome:box title="form.label.version_form" omitBorders="true">
        	<input type="hidden" id="CSRF_TOKEN" name="CSRF_TOKEN" value="${sessionScope.CSRF_TOKEN}" />
        
                <div id="versionForm" style="margin-left: 12px; margin-top: 12px;">
                	<p>
                        By versioning a form you will lose access to the old version of this form and will not be able to edit or change administration schedule.
                	</p>
					<p>
						Are you sure you want to version this form?
					</p>
                </div>

                <div class="flow-buttons content autoclear">
                    <span class="prev">
                    	<tags:button type="button" id="flow-cancel" cssClass="previous ibutton" value="Cancel" color="blue" icon="x" onclick="closeWindow()"
                    	overRideStyle=" position:relative; top:-10px;"/>
					</span>
					<span class="next">
						<tags:button type="submit" id="flow-update" cssClass="next" value="Version" color="green" icon="check"
						overRideStyle=" position:relative; top:-10px;"/>
					</span>
                </div>
        </chrome:box>
    </ctcae:form>
</div>