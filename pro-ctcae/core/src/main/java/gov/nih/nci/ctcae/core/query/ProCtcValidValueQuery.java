package gov.nih.nci.ctcae.core.query;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcValidValueQuery extends AbstractQuery {

    private static String queryString = "SELECT o from ProCtcValidValue o order by o.id";

    public ProCtcValidValueQuery() {

        super(queryString);
    }
    
}
