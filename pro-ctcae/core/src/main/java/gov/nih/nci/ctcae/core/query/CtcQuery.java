package gov.nih.nci.ctcae.core.query;

//
/**
 * The Class CtcQuery.
 *
 * @author mehul gulati
 *         Date: Jan 19, 2009
 */
public class CtcQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "select i from CtcTerm i order by i.id";

    /**
     * The NAME.
     */
    private static String NAME = "term";
    private static String CTEP_CODE = "ctepCode";


    /**
     * Instantiates a new ctc query.
     */
    public CtcQuery() {
        super(queryString);
    }

    /**
     * Filter by name.
     *
     * @param name the name
     */
    public void filterByName(final String name) {
        String searchString = name.toLowerCase();
        andWhere("(lower(i.ctepTerm) = :" + NAME + " or lower(i.term) = :" + NAME + ")");
        setParameter(NAME, searchString);

    }

    public void filterByCtepCode(final String meddraCode) {
        andWhere("i.ctepCode = :" + CTEP_CODE);
        setParameter(CTEP_CODE, meddraCode);
    }

}
