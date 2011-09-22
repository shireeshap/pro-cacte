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
        <chrome:box title="form.label.show_form">
            <chrome:division>
                <div id="releaseForm">
                    <p>
                        You are about to unhide <strong>${command.title}</strong>.
                    </p>
                    <p>
                        Do you want to continue?
                    </p>
                </div>
                <div class="content flow-buttons autoclear">
                <span class="prev">
                	<tags:button onclick="closeWindow()" color="blue" value="Cancel" markupWithTag="a" icon="x"/>
				</span>
				<span class="next">
					<tags:button type="submit" id="flow-update" color="red" icon="check" value="Show"/>
				</span>
            </chrome:division>
        </chrome:box>
    </ctcae:form>
</div>