package gov.nih.nci.ctcae.core.query;

//
/**
 * The Class ProCtcQuery.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT o from ProCtc o order by o.id";

    /**
     * The PR o_ ct c_ version.
     */
    private static String PRO_CTC_VERSION = "proCtcVersion";

    /**
     * Instantiates a new pro ctc query.
     */
    public ProCtcQuery() {

        super(queryString);
    }

    /**
     * Filter by pro ctc version.
     *
     * @param proCtcVersion the pro ctc version
     */
    public void filterByProCtcVersion(final String proCtcVersion) {
        andWhere("o.proCtcVersion = :" + PRO_CTC_VERSION);
        setParameter(PRO_CTC_VERSION, proCtcVersion);
    }
}
