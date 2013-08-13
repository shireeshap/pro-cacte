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
    private static String CTC_CATEGORY_NAMES = "ctcCategoryIds";

    /*
     *  Fetch ProCtc terms only (only version v4.0)
     */
    public ProCtcTermQuery() {
        super(queryString);
        leftJoin("o.ctcTerm.categoryTermSets as categoryTerm");
        andWhere(" categoryTerm.category.name not in ('EQ5D-5L', 'EQ5D-3L') ");
        andWhere(" categoryTerm.category.ctc.name = :" + CTC_NAME);
        setParameter(CTC_NAME, "CTC v4.0");
    }

    /*
     *  Fetch ProCtc terms only (all versions)
     */
    public ProCtcTermQuery(boolean allVersion) {
        super(queryString);
    }
    
    /*
     *  Fetch Eq5d terms only
     */
    public ProCtcTermQuery(boolean ctcVersionFour, boolean eq5dTermsOnly) {
    	super(queryString);
        leftJoin("o.ctcTerm.categoryTermSets as categoryTerm");
        andWhere(" categoryTerm.category.name in ('EQ5D-5L') ");
        andWhere(" categoryTerm.category.ctc.name = :" + CTC_NAME);
        setParameter(CTC_NAME, "CTC v4.0");
    }
    
    /*
     *  Fetch all ProCtc & Eq5d terms 
     */
    public ProCtcTermQuery(String allTerms) {
        super(queryString);
        leftJoin("o.ctcTerm.categoryTermSets as categoryTerm");
        andWhere("categoryTerm.category.ctc.name = :" + CTC_NAME);
        setParameter(CTC_NAME, "CTC v4.0");
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
