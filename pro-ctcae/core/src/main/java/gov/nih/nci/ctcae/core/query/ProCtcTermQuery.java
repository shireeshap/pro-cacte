package gov.nih.nci.ctcae.core.query;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcTermQuery extends AbstractQuery {

    private static String queryString = "SELECT o from ProCtcTerm o order by o.id";

    public ProCtcTermQuery() {

        super(queryString);
    }
}
