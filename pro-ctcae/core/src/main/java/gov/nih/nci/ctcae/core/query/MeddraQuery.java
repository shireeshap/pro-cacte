package gov.nih.nci.ctcae.core.query;

/**
 * @aurthor Mehul Gulati
 * Date: May 27, 2009
 */
public class MeddraQuery extends AbstractQuery {

    private static String queryString = "SELECT llt.meddraTerm from LowLevelTerm llt order by llt.id";
    private static String MEDDRA_TERM = "meddraTerm";

    public MeddraQuery() {
        super(queryString);
    }

    public void filterMeddraWithMatchingText(String text) {
        String searchString = text !=null ? "%" + text.toLowerCase() + "%" : "%";

        andWhere(String.format("(lower(llt.meddraTerm) LIKE :%s)", MEDDRA_TERM));
        setParameter(MEDDRA_TERM, searchString);
    }
}
