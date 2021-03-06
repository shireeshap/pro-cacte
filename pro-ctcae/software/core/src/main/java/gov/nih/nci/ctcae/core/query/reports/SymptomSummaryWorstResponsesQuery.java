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
            "spci.crfPageItem.proCtcQuestion.proCtcQuestionType, " +
            "spci.crfPageItem.proCtcQuestion.proCtcTerm.proCtcTermVocab.termEnglish " +
            "from StudyParticipantCrfItem spci " +
            " where spci.studyParticipantCrfSchedule.studyParticipantCrf.crf.eq5d != 'true' " +
            "group by " +
            "spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id, " +
            "spci.crfPageItem.proCtcQuestion.proCtcQuestionType, " +
            "spci.crfPageItem.proCtcQuestion.proCtcTerm.proCtcTermVocab.termEnglish " +
            "order by " +
            "max(spci.proCtcValidValue.displayOrder), " +
            "spci.studyParticipantCrfSchedule.studyParticipantCrf.studyParticipantAssignment.participant.id";


    public SymptomSummaryWorstResponsesQuery() {
        super(queryString);
    }
}