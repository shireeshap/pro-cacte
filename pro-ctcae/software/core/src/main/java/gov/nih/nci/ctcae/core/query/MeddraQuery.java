package gov.nih.nci.ctcae.core.query;

import java.util.Currency;

/**
 * @aurthor Mehul Gulati
 * Date: May 27, 2009
 */
public class MeddraQuery extends AbstractQuery {

    private static String queryString = "SELECT llt.lowLevelTermVocab.meddraTermEnglish from LowLevelTerm llt order by llt.id";
    private static String queryString2 = "SELECT llt.lowLevelTermVocab.meddraTermEnglish from LowLevelTerm llt, CtcTerm ctcTerm, ProCtcTerm proCtcTerm order by llt.id";
//    private static String queryString2 = "SELECT llt.lowLevelTermVocab.meddraTermEnglish || " + "' ('" + " || proCtcTerm.proCtcTermVocab.termEnglish || " + "')'" + " from LowLevelTerm llt, CtcTerm ctcTerm, ProCtcTerm proCtcTerm order by llt.id";
    private static String queryString1 = "SELECT llt from LowLevelTerm llt order by llt.id";
    private static String queryString3 = "SELECT llt.lowLevelTermVocab.meddraTermSpanish from LowLevelTerm llt order by llt.id";
    private static String queryString4 = "select llt.meddraCode from LowLevelTerm llt";
    private static String MEDDRA_TERM = "meddraTermEnglish";
    private static String MEDDRA_TERM_SPANISH = "meddraTermSpanish";
    private static String MEDDRA_CODE = "meddraCode";
    private static String CURRENCY = "currency";

    public MeddraQuery() {
        super(queryString1);
    }

    public MeddraQuery(boolean onlyMeddraTerm) {
        super(queryString);
    }

    public MeddraQuery(boolean proCtc, boolean meddra){
        super(queryString2);
    }

    public MeddraQuery (String language) {
        super(queryString3);
    }

    public MeddraQuery (boolean allCode, String language) {
        super(queryString4);
    }

    public void filterMeddraWithCurrency(String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : "%";
        andWhere("(llt.participantAdded IS NULL OR llt.participantAdded = FALSE)");
        andWhere("llt.currency = :" + CURRENCY);
        andWhere(String.format("(lower(llt.lowLevelTermVocab.meddraTermEnglish) LIKE :%s)", MEDDRA_TERM));
        setParameter(MEDDRA_TERM, searchString);
        setParameter(CURRENCY, "Y");
    }

    public void filterMeddraWithMatchingText(String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : "%";
        andWhere("(llt.participantAdded IS NULL OR llt.participantAdded = FALSE)");
//        andWhere("(llt.meddraCode = ctcTerm.ctepCode)");
//        andWhere("(ctcTerm.id = proCtcTerm.ctcTerm)");
//        andWhere("(proCtcTerm.proCtcTermVocab.termEnglish IS NOT NULL)");
        andWhere("llt.currency = :" + CURRENCY);
        andWhere(String.format("(lower(llt.lowLevelTermVocab.meddraTermEnglish) LIKE :%s)", MEDDRA_TERM));
        setParameter(MEDDRA_TERM, searchString);
        setParameter(CURRENCY, "Y");
    }

    public void filterMeddraWithSpanishText (String text) {
         String searchString = text != null ? "%" + text.toLowerCase() + "%" : "%";
        andWhere("(llt.participantAdded IS NULL OR llt.participantAdded = FALSE)");
//        andWhere("(llt.meddraCode = ctcTerm.ctepCode)");
//        andWhere("(ctcTerm.id = proCtcTerm.ctcTerm)");
//        andWhere("(proCtcTerm.proCtcTermVocab.termEnglish IS NOT NULL)");
        andWhere("llt.currency = :" + CURRENCY);
        andWhere(String.format("(lower(llt.lowLevelTermVocab.meddraTermSpanish) LIKE :%s)", MEDDRA_TERM_SPANISH));
        setParameter(MEDDRA_TERM_SPANISH, searchString);
        setParameter(CURRENCY, "Y");
    }

    public void filterMeddraForProCtc(String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : "%";
        andWhere("(llt.meddraCode = ctcTerm.ctepCode)");
        andWhere("(ctcTerm.id = proCtcTerm.ctcTerm)");
        andWhere("(proCtcTerm.proCtcTermVocab.termEnglish IS NOT NULL)");
        andWhere(String.format("(lower(llt.lowLevelTermVocab.meddraTermEnglish) LIKE :%s)", MEDDRA_TERM));
        setParameter(MEDDRA_TERM, searchString);

    }

    public void filterByMeddraTerm(final String term) {
        String searchString = term.toLowerCase();
        andWhere("lower(llt.lowLevelTermVocab.meddraTermEnglish) = :" + MEDDRA_TERM);
        setParameter(MEDDRA_TERM, searchString);
    }

    public void filterBySpanishMeddraTerm(final String term) {
        String searchString = term.toLowerCase();
        andWhere("lower(llt.lowLevelTermVocab.meddraTermSpanish) = :" + MEDDRA_TERM_SPANISH);
        setParameter(MEDDRA_TERM_SPANISH, searchString);
    }

    public void filterByMeddraPtId(Integer meddraPtId) {
        andWhere("llt.meddraPtId = :" + MEDDRA_TERM);
        setParameter(MEDDRA_TERM, meddraPtId);
    }

    public void filterByMeddraCode(String meddraCode) {
        andWhere("llt.meddraCode = :" + MEDDRA_CODE);
        setParameter(MEDDRA_CODE, meddraCode);
    }
}
