package gov.nih.nci.ctcae.core.query;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcQuery extends AbstractQuery {

    private static String queryString = "SELECT o from ProCtc o order by o.id";
    private static String PRO_CTC_VERSION = "proCtcVersion";

    public ProCtcQuery() {

        super(queryString);
    }
    
    public void filterByProCtcVersion(final String proCtcVersion) {
        andWhere("o.proCtcVersion = :" + PRO_CTC_VERSION);
        setParameter(PRO_CTC_VERSION, proCtcVersion);
    }
}
