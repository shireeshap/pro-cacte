<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="selected" required="true" %>

<div class="tabpane">
    <div class="workflow-tabs2">
        <ul id="" class="tabs autoclear">
            <proctcae:urlAuthorize url="/pages/reports/studyLevelReport">
                <li id="thirdlevelnav-x" class="tab ${selected=='studyLevelReport'?'selected':''}">
                    <div>
                        <a href="studyLevelReport"><tags:message code="reports.tab.studyLevel"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/reports/symptomsummary">
                <li id="thirdlevelnav-x" class="tab ${selected=='symptomsummary'?'selected':''}">
                    <div>
                        <a href="symptomsummary"><tags:message code="reports.tab.symptomsummary"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/reports/symptomovertime">
                <li id="thirdlevelnav-x" class="tab ${selected=='symptomovertime'?'selected':''}">
                    <div>
                        <a href="symptomovertime"><tags:message code="reports.tab.symptomovertime"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/reports/participantaddedquestions">
                <li id="thirdlevelnav-x" class="tab ${selected=='participantAddedQuestions'?'selected':''}">
                    <div>
                        <a href="participantaddedquestions"><tags:message code="reports.tab.participantaddedquestions"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
        </ul>
    </div>
</div>

