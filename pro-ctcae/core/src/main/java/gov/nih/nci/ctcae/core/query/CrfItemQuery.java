package gov.nih.nci.ctcae.core.query;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class CrfItemQuery extends AbstractQuery {

    private static String queryString = "SELECT o from CrfItem o order by o.id";

    public CrfItemQuery() {

        super(queryString);
    }
}
