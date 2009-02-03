package gov.nih.nci.ctcae.core.query;

// TODO: Auto-generated Javadoc
/**
 * The Class ProCtcTermQuery.
 * 
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcTermQuery extends AbstractQuery {

    /** The query string. */
    private static String queryString = "SELECT distinct(o) from ProCtcTerm o left join o.proCtcQuestions as proCtcQuestion left join proCtcQuestion.validValues order by o.id ";
    private static String CTC_CATEGORY_ID = "ctcCategoryId";

    /**
     * Instantiates a new pro ctc term query.
     */
    public ProCtcTermQuery() {

        super(queryString);
    }

    /**
     * Filter by ctc term having questions only.
     */
    public void filterByCtcTermHavingQuestionsOnly() {
        innerJoin("o.proCtcQuestions");
    }

    public void filterByCtcCategoryId(Integer ctcCategoryId) {
        andWhere("o.ctcTerm.category.id = :" + CTC_CATEGORY_ID);
        setParameter(CTC_CATEGORY_ID, ctcCategoryId);
    }
}
