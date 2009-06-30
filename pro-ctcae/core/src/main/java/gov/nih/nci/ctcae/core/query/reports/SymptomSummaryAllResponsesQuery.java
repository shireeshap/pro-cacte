package gov.nih.nci.ctcae.core.query.reports;

//
/**
 * @author Harsh Agarwal
 * @created June 26, 2009
 */
public class SymptomSummaryAllResponsesQuery extends AbstractReportQuery {

    private static String queryString = "SELECT " +
            "count(spci.proCtcValidValue.displayOrder), " +
            "spci.proCtcValidValue.displayOrder, " +
            "spci.crfPageItem.proCtcQuestion.proCtcQuestionType " +
            "from StudyParticipantCrfItem spci " +
            "group by spci.proCtcValidValue.displayOrder, " +
            "spci.crfPageItem.proCtcQuestion.proCtcQuestionType " +
            "order by spci.crfPageItem.proCtcQuestion.proCtcQuestionType, " +
            "spci.proCtcValidValue.displayOrder ";

    public SymptomSummaryAllResponsesQuery() {
        super(queryString);
    }
}