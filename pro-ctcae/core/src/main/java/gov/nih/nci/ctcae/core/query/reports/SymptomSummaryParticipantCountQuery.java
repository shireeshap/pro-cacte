package gov.nih.nci.ctcae.core.query.reports;

public class SymptomSummaryParticipantCountQuery extends AbstractReportQuery {

    private static String queryString = "SELECT " +
            "count(distinct spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id) " +
            "from StudyParticipantCrfItem spci";

    public SymptomSummaryParticipantCountQuery() {
        super(queryString);
    }
}