package gov.nih.nci.ctcae.core.query;

/**
 * @aurthor Mehul Gulati
 * Date: May 27, 2009
 */
public class MeddraQuery extends AbstractQuery {

    private static String queryString = "SELECT llt.meddraTerm from LowLevelTerm llt order by llt.id";
    private static String queryString1 = "SELECT llt from LowLevelTerm llt order by llt.id";
    private static String MEDDRA_TERM = "meddraTerm";

    public MeddraQuery() {
        super(queryString1);
    }

    public MeddraQuery(boolean onlyMeddraTerm) {
        super(queryString);
    }

    public void filterMeddraWithMatchingText(String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : "%";

        andWhere(String.format("(lower(llt.meddraTerm) LIKE :%s)", MEDDRA_TERM));
        setParameter(MEDDRA_TERM, searchString);
    }

    public void filterByMeddraTerm(final String term) {
        String searchString = term.toLowerCase();
        andWhere("lower(llt.meddraTerm) = :" + MEDDRA_TERM);
        setParameter(MEDDRA_TERM, searchString);

    }
}
