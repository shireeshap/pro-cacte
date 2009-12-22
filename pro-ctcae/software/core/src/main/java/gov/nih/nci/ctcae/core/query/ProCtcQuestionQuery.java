package gov.nih.nci.ctcae.core.query;

//
/**
 * The Class ProCtcQuestionQuery.
 *
 * @author Harsh Agarwal
 * @created Oct 14, 2008
 */
public class ProCtcQuestionQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT o from ProCtcQuestion o order by o.id";

    /**
     * Instantiates a new pro ctc question query.
     */
    public ProCtcQuestionQuery() {

        super(queryString);
    }
}
