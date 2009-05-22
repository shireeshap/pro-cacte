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
public class SymptomSummaryParticipantCountQuery extends SymptomSummaryReportQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT count(distinct spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id) from StudyParticipantCrfItem spci ";
    public SymptomSummaryParticipantCountQuery() {
        super(queryString);
    }
}