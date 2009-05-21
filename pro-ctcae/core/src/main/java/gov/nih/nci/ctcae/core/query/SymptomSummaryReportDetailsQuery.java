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