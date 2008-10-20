package gov.nih.nci.ctcae.core.query;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class CtcTermQuery extends AbstractQuery {

    private static String queryString = "SELECT o from CtcTerm o order by o.id";

    public CtcTermQuery() {

        super(queryString);
    }
}
