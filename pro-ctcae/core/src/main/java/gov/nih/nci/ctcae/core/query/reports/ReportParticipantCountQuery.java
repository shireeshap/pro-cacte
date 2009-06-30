package gov.nih.nci.ctcae.core.query.reports;

public class ReportParticipantCountQuery extends AbstractReportQuery {

    private static String queryString = "SELECT " +
            "count(distinct spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id) " +
            "from StudyParticipantCrfItem spci";

    public ReportParticipantCountQuery() {
        super(queryString);
    }
}