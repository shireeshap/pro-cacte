package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.query.reports.AbstractReportQuery;

public class SymptomSummaryReportDetailsQuery extends AbstractReportQuery {
    private static String queryString = "SELECT spci.studyParticipantCrfSchedule,spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant " +
            "from StudyParticipantCrfItem spci order by  spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.lastName, spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.firstName, " +
            "spci.studyParticipantCrfSchedule.startDate";

    public SymptomSummaryReportDetailsQuery() {
        super(queryString);
    }
}