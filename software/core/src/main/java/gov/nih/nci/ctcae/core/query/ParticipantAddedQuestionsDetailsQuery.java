package gov.nih.nci.ctcae.core.query;

//
/**
 * The Class ProCtcQuestionQuery.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ParticipantAddedQuestionsDetailsQuery extends ParticipantAddedQuestionsReportQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT spcsaq  from StudyParticipantCrfScheduleAddedQuestion spcsaq " +
            "order by spcsaq.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.lastName " +
            ", spcsaq.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.firstName ";

    public ParticipantAddedQuestionsDetailsQuery() {
        super(queryString);
    }
}