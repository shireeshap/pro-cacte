<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="selected" required="true" %>
<div class="tabpane">
    <div class="workflow-tabs2">
        <ul id="" class="tabs autoclear">
            <proctcae:urlAuthorize url="/pages/reports/participantReport">
                <li id="thirdlevelnav-x" class="tab ${selected=='overallParticipant'?'selected':''}">
                    <div>
                        <a href="participantReport?rt=overallParticipant">
                                <%--<tags:message code="reports.tab.studyLevel"/>--%>
                            Overall Participant
                        </a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/reports/participantReport">
                <li id="thirdlevelnav-x" class="tab ${selected=='worstSymptom'?'selected':''}">
                    <div>
                        <a href="participantReport?rt=worstSymptom">Shared AE Form</a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
        </ul>
    </div>
</div>

