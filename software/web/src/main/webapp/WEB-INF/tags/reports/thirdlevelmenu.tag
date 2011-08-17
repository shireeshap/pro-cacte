<%@ taglib prefix="proctcae" uri="http://gforge.nci.nih.gov/projects/proctcae/tags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ attribute name="selected" required="true" %>

<div class="tabpane">
    <div class="workflow-tabs2">
        <ul id="" class="tabs autoclear">
            <proctcae:urlAuthorize url="/pages/reports/report">
                <li id="thirdlevelnav-x" class="tab ${selected=='overallStudy'?'selected':''}">
                    <div>
                        <a href="report?rt=overallStudy"><tags:message code="reports.tab.studyLevel"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/reports/report">
                <li id="thirdlevelnav-x" class="tab ${selected=='symptomSummary'?'selected':''}">
                    <div>
                        <a href="report?rt=symptomSummary"><tags:message code="reports.tab.symptomsummary"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/reports/report">
                <li id="thirdlevelnav-x" class="tab ${selected=='symptomOverTime'?'selected':''}">
                    <div>
                        <a href="report?rt=symptomOverTime"><tags:message code="reports.tab.symptomovertime"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/reports/report">
                <li id="thirdlevelnav-x" class="tab ${selected=='participantAddedQuestions'?'selected':''}">
                    <div>
                        <a href="report?rt=participantAddedQuestions"><tags:message
                                code="reports.tab.participantaddedquestions"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/reports/report">
                <li id="thirdlevelnav-x" class="tab ${selected=='enrollmentReport'?'selected':''}">
                    <div>
                        <a href="report?rt=enrollmentReport"><tags:message
                                code="reports.tab.enrollmentReport"/></a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
            <proctcae:urlAuthorize url="/pages/reports/report">
                <li id="thirdlevelnav-x" class="tab ${selected=='enrollmentReport'?'selected':''}">
                    <div align="right">
                        <a href="/proctcae/pages/reports/overallStudyExcel" target="_blank">
                            Overall study data
                        </a>
                    </div>
                </li>
            </proctcae:urlAuthorize>
        </ul>
    </div>
</div>

