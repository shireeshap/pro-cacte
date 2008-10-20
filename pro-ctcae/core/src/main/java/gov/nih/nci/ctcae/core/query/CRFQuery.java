package gov.nih.nci.ctcae.core.query;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class CRFQuery extends AbstractQuery {

    private static String queryString = "SELECT o from CRF o order by o.id";

    public CRFQuery() {

        super(queryString);
    }
}
