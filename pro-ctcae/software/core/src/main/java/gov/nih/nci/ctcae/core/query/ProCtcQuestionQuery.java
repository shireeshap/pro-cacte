package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.ProCtcQuestionType;

/**
 * The Class ProCtcQuestionQuery.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcQuestionQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT o from ProCtcQuestion o order by o.id";

    private static String QUESTION_TYPE = "proCtcQuestionType";
    private static String PROCTC_TERM = "proCtcTerm";
    private static String PROCTC_SYS_ID ="proCtcSystemId";

    /**
     * Instantiates a new pro ctc question query.
     */
    public ProCtcQuestionQuery() {

        super(queryString);
    }

    public void filterByQuestionType(ProCtcQuestionType proCtcQuestionType) {
        andWhere("o.proCtcQuestionType = :" + QUESTION_TYPE);
        setParameter(QUESTION_TYPE, proCtcQuestionType);
    }

    public void filterByTerm(final String term) {
        String searchString = term.toLowerCase();
        andWhere("lower(o.proCtcTerm.proCtcTermVocab.termEnglish) = :" + PROCTC_TERM);
        setParameter(PROCTC_TERM, searchString);

    }
    
    public void filterByTermSystemId(final Integer sysId) {
        andWhere("o.proCtcTerm.proCtcSystemId = :" + PROCTC_SYS_ID);
        setParameter(PROCTC_SYS_ID, sysId);

    }
}
