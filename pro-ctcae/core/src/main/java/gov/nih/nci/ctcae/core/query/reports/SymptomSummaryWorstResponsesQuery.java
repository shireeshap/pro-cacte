package gov.nih.nci.ctcae.core.query.reports;

//
/**
 * @author Harsh Agarwal
 * @created June 26, 2009
 */
public class SymptomSummaryWorstResponsesQuery extends AbstractReportQuery {

    private static String queryString = "SELECT " +
            "max(spci.proCtcValidValue.displayOrder) , " +
            "spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id , " +
            "spci.crfPageItem.proCtcQuestion.proCtcQuestionType " +
            "from StudyParticipantCrfItem spci " +
            "group by " +
            "spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id, " +
            "spci.crfPageItem.proCtcQuestion.proCtcQuestionType " +
            "order by " +
            "max(spci.proCtcValidValue.displayOrder), " +
            "spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id";


    public SymptomSummaryWorstResponsesQuery() {
        super(queryString);
    }
}