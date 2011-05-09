package gov.nih.nci.ctcae.core.query;

/**
 * @aurthor Mehul Gulati
 * Date: May 27, 2009
 */
public class MeddraQuery extends AbstractQuery {

    private static String queryString = "SELECT llt.lowLevelTermVocab.meddraTermEnglish from LowLevelTerm llt order by llt.id";
    private static String queryString1 = "SELECT llt from LowLevelTerm llt order by llt.id";
    private static String MEDDRA_TERM = "meddraTermEnglish";

    public MeddraQuery() {
        super(queryString1);
    }

    public MeddraQuery(boolean onlyMeddraTerm) {
        super(queryString);
    }

    public void filterMeddraWithMatchingText(String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : "%";
        andWhere("(llt.participantAdded IS NULL OR llt.participantAdded = FALSE)");
        andWhere(String.format("(lower(llt.lowLevelTermVocab.meddraTermEnglish) LIKE :%s)", MEDDRA_TERM));
        setParameter(MEDDRA_TERM, searchString);
    }

    public void filterByMeddraTerm(final String term) {
        String searchString = term.toLowerCase();
        andWhere("lower(llt.lowLevelTermVocab.meddraTermEnglish) = :" + MEDDRA_TERM);
        setParameter(MEDDRA_TERM, searchString);

    }

    public void filterByMeddraPtId(Integer meddraPtId) {
        andWhere("llt.meddraPtId = :" + MEDDRA_TERM);
        setParameter(MEDDRA_TERM, meddraPtId);
    }
}
