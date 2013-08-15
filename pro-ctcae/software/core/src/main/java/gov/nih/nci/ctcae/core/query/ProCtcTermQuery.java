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
     * Default no-arg constructor: gets ptoCtcTerms only (no eq5dTerms), of version 4.0
     */
    public ProCtcTermQuery() {
    	super(queryString);
    	leftJoin("o.ctcTerm.categoryTermSets as categoryTerm");
    	andWhere(" categoryTerm.category.name not in ('EQ5D-5L', 'EQ5D-3L') ");
    	andWhere(" categoryTerm.category.ctc.name = :" + CTC_NAME);
    	setParameter(CTC_NAME, "CTC v4.0");
    }
    
    /**Returns both pro and eq5d if pro and eq5d boolean are both set to true or both set to false.
     * @param allVersion - gets all versions if true and version 4.0 if false 
     * @param proOnly - gets only proTerms if true
     * @param eq5dOnly - gets only eq5dTerms if true
     */
    public ProCtcTermQuery(boolean allVersion, boolean pro, boolean eq5d) {
        super(queryString);
        leftJoin("o.ctcTerm.categoryTermSets as categoryTerm");

        if(pro && !eq5d){
        	andWhere(" categoryTerm.category.name not in ('EQ5D-5L', 'EQ5D-3L') ");
        } else if(!pro && eq5d){
        	// Eq5D only
        	 andWhere(" categoryTerm.category.name in ('EQ5D-5L') ");
        }
        	// All Version or only V4.0
        if(!allVersion){
        	andWhere(" categoryTerm.category.ctc.name = :" + CTC_NAME);
        	setParameter(CTC_NAME, "CTC v4.0");
        }
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
