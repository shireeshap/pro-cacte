package gov.nih.nci.ctcae.core.query;

//
/**
 * The Class ProCtcQuestionQuery.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class SymptomSummaryReportDetailsQuery extends SymptomSummaryReportQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT spci.studyParticipantCrfSchedule,spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant " +
            "from StudyParticipantCrfItem spci order by  spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.lastName, spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.firstName, " +
            "spci.studyParticipantCrfSchedule.startDate";

    public SymptomSummaryReportDetailsQuery() {
        super(queryString);
    }
}