package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.query.reports.AbstractReportQuery;

public class WorstResponsesDetailsQuery extends AbstractReportQuery {
    protected static String queryString = "SELECT max(spci.proCtcValidValue.displayOrder), " +
            "spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id " +
            "from StudyParticipantCrfItem spci group by spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id ";

    public WorstResponsesDetailsQuery() {
        super(queryString);
    }
}