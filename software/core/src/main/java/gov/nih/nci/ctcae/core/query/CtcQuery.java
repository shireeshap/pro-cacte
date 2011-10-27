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
    private static String CTC_NAME = "ctcName";

    /**
     * The NAME.
     */
    private static String NAME = "term";

    /**
     * Instantiates a new ctc query.
     */
    public CtcQuery() {
        super(queryString);
        andWhere("i.category.ctc.name = :" + CTC_NAME);
        setParameter(CTC_NAME, "CTC v4.0");
    }

    public CtcQuery(boolean useAllVersion) {
        super(queryString);
    }

    /**
     * Filter by name.
     *
     * @param name the name
     */
    public void filterByName(final String name) {
        String searchString = name.toLowerCase();
        andWhere("lower(i.ctcTermVocab.termEnglish) = :" + NAME + ")");
        setParameter(NAME, searchString);

    }

    public void filterByCtepCode(final String ctepCode) {
        String searchString = ctepCode.toLowerCase();
        andWhere("lower(i.ctepCode) = :" + NAME + ")");
        setParameter(NAME, searchString);

    }
}
