<%@taglib uri="http://www.opensymphony.com/sitemesh/decorator" prefix="decorator" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="chrome" tagdir="/WEB-INF/tags/chrome" %>
<%@ taglib prefix="blue" tagdir="/WEB-INF/tags/blue" %>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="standard" tagdir="/WEB-INF/tags/standard" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div style="width:590px;">
    <form:form method="post">
        <chrome:box title="form.label.version_form">
            <chrome:division>
                <div id="versionForm">
                	<p>
                		Versioning a form creates a copy of this form that you can edit.
                	</p>
					<p>
						Are you sure you want to version this form?
					</p>
                </div>

                <div class="flow-buttons content autoclear">
                    <span class="prev">
                    	<tags:button type="button" id="flow-cancel" cssClass="previous ibutton" value="Cancel" color="blue" icon="x" onclick="closeWindow()"/>
					</span>
					<span class="next">
						<tags:button type="submit" id="flow-update" cssClass="next" value="Version" color="green" icon="check"/>
					</span>
                </div>
            </chrome:division>
        </chrome:box>
    </form:form>
</div>