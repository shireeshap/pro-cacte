package gov.nih.nci.ctcae.core.query;



/**
 * The Class ProCtcTermQuery.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcTermQuery extends AbstractQuery {

    /**
     * The query string. removed the "order by o.proCtcTermVocab.termEnglish" portion
     */
    private static String queryString = "SELECT distinct(o) from ProCtcTerm o left join o.proCtcQuestions as proCtcQuestion left join proCtcQuestion.validValues";
    private static String CTC_CATEGORY_ID = "ctcCategoryId";
    private static String CTC_NAME = "ctcName";
    private static String CTC_TERM_ID = "ctcTermId";
    private static String PROCTC_TERM = "symptom";
    private static String CURRENCY = "currency";

    /**
     * Instantiates a new pro ctc term query.
     */
    public ProCtcTermQuery() {
        super(queryString);
        leftJoin("o.ctcTerm.categoryTermSets as categoryTerm");
        andWhere("categoryTerm.category.ctc.name = :" + CTC_NAME);
        setParameter(CTC_NAME, "CTC v4.0");
    }

    public ProCtcTermQuery(boolean allVersion) {
        super(queryString);
    }

    /**
     * Filter by ctc term having questions only.
     */
    public void filterByCtcTermHavingQuestionsOnly() {
        innerJoin("o.proCtcQuestions");
    }

    public void filterByCtcCategoryId(Integer ctcCategoryId) {
        andWhere("categoryTerm.category.id = :" + CTC_CATEGORY_ID);
        setParameter(CTC_CATEGORY_ID, ctcCategoryId);
    }

    public void filterByCtcTermId(Integer ctcTermId) {
        andWhere("o.ctcTerm.id = :" + CTC_TERM_ID);
        setParameter(CTC_TERM_ID, ctcTermId);
    }

    public void filterByCurrency() {
        andWhere("o.currency = :" + CURRENCY);
        setParameter(CURRENCY, "Y");
    }

    public void filterByTerm(final String term) {
        String searchString = term.toLowerCase();
        andWhere("lower(o.proCtcTermVocab.termEnglish) = :" + PROCTC_TERM);
        setParameter(PROCTC_TERM, searchString);
    }

    public void filterBySpanishTerm(final String term) {
        String searchString = term.toLowerCase();
        andWhere("lower(o.proCtcTermVocab.termSpanish) = :" + PROCTC_TERM);
        setParameter(PROCTC_TERM, searchString);
    }

    public void filterByCoreItemsOnly() {
        andWhere("o.core = true");
    }
}