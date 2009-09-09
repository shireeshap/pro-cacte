<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<div class="tabpane">
    <div class="workflow-tabs2">
        <ul id="menu" class="tabs autoclear">
            <li id="thirdlevelnav-x" class="tab" name='questionsLink'>
                <div>
                    <a href="javascript:showTab('questions')"><tags:message code="form.tab.form_details"/></a>
                </div>
            </li>
            <li id="thirdlevelnav-x" class="tab" name="schedulesLink">
                <div>
                    <a href="javascript:showTab('schedules')"><tags:message code="form.tab.calendar_template"/></a>
                </div>
            </li>
            <li id="thirdlevelnav-x" class="tab" name="notificationsLink">
                <div>
                    <a href="javascript:showTab('notifications')"><tags:message code="form.tab.rules"/></a>
                </div>
            </li>
        </ul>
    </div>
</div>

