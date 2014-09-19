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
    private static String queryStringEnglish = "SELECT distinct o.proCtcTermVocab.termEnglish from ProCtcTerm o left join o.proCtcQuestions as proCtcQuestion left join proCtcQuestion.validValues";
    private static String queryStringSpanish = "SELECT distinct o.proCtcTermVocab.termSpanish from ProCtcTerm o left join o.proCtcQuestions as proCtcQuestion left join proCtcQuestion.validValues";
    private static String CTC_CATEGORY_ID = "ctcCategoryId";
    private static String CTC_NAME = "ctcName";
    private static String CTC_TERM_ID = "ctcTermId";
    private static String PROCTC_TERM = "symptom";
    private static String CURRENCY = "currency";
    private static String PRO_CTC_SYSTEM_ID = "proCtcSystemId";

    
    /*
     * Default no-arg constructor: gets ptoCtcTerms only (no eq5dTerms), of version 4.0
     */
    public ProCtcTermQuery() {
    	super(queryString);
    	leftJoin("o.ctcTerm.categoryTermSets as categoryTerm");
    	filterOutEq5dTerms();
    	andWhere(" categoryTerm.category.ctc.name = :" + CTC_NAME);
    	setParameter(CTC_NAME, "CTC v4.0");
    }
    
    public ProCtcTermQuery(String englishQuery) {
    	super(queryStringEnglish);
    	leftJoin("o.ctcTerm.categoryTermSets as categoryTerm");
    	filterOutEq5dTerms();
    	andWhere(" categoryTerm.category.ctc.name = :" + CTC_NAME);
    	setParameter(CTC_NAME, "CTC v4.0");
    }
    
    public ProCtcTermQuery(boolean spanishQuery) {
    	super(queryStringSpanish);
    	leftJoin("o.ctcTerm.categoryTermSets as categoryTerm");
    	filterOutEq5dTerms();
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
        	filterOutEq5dTerms();
        } else if(!pro && eq5d){
        	// Eq5D only
        	filterByEq5d5LTerms();
        }
        	// All Version or only V4.0
        if(!allVersion){
        	andWhere(" categoryTerm.category.ctc.name = :" + CTC_NAME);
        	setParameter(CTC_NAME, "CTC v4.0");
        }
    }
    
    public void filterOutEq5dTerms() {
    	andWhere(" categoryTerm.category.name not in ('EQ5D-5L', 'EQ5D-3L') ");
    }
    
    public void filterByEq5d5LTerms() {
   	 andWhere(" categoryTerm.category.name in ('EQ5D-5L') ");
    }
    
    public void findByProCtcSystemId(final int proCtcSysId) {
        andWhere("o.proCtcSystemId = :" + PRO_CTC_SYSTEM_ID);
        setParameter(PRO_CTC_SYSTEM_ID, proCtcSysId);
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
    
    public void filterWithMatchingEnglishText(final String term) {
    	String searchString = term != null ? "%" + term.toLowerCase() + "%" : "%";
        andWhere(String.format("lower(o.proCtcTermVocab.termEnglish) LIKE :%s", PROCTC_TERM));
        setParameter(PROCTC_TERM, searchString);
    }
    
    public void filterWithMatchingSpanishText(final String term) {
    	String searchString = term != null ? "%" + term.toLowerCase() + "%" : "%";
        andWhere(String.format("lower(o.proCtcTermVocab.termSpanish) LIKE :%s", PROCTC_TERM));
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
