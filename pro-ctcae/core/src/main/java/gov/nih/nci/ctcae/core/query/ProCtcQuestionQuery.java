package gov.nih.nci.ctcae.core.query;

/**
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcQuestionQuery extends AbstractQuery {

    private static String queryString = "SELECT o from ProCtcQuestion o order by o.id";

    public ProCtcQuestionQuery() {

        super(queryString);
    }
}
