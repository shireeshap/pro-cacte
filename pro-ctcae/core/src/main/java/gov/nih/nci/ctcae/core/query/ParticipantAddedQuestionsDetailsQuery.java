package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;
import gov.nih.nci.ctcae.core.domain.CrfStatus;

import java.util.Set;
import java.util.Date;

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
    private static String queryString = "SELECT spcaq , spcaq.studyParticipantCrfScheduleAddedQuestions from StudyParticipantCrfAddedQuestion spcaq " +
            "order by spcaq.studyParticipantCrf.studyParticipantAssignment.participant.lastName "+
            ", spcaq.studyParticipantCrf.studyParticipantAssignment.participant.firstName ";

    public ParticipantAddedQuestionsDetailsQuery() {
        super(queryString);
    }
}