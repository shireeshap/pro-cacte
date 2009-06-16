package gov.nih.nci.ctcae.core.query;

//
/**
 * The Class ProCtcQuestionQuery.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class SymptomOverTimeReportQuery extends SymptomSummaryReportQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT spci from StudyParticipantCrfItem spci " +
            " order by spci.studyParticipantCrfSchedule.startDate,  spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.lastName, spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.firstName";

    public SymptomOverTimeReportQuery() {
        super(queryString);
    }
}