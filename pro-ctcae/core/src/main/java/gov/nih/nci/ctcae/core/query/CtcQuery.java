package gov.nih.nci.ctcae.core.query;

/**
 * @author mehul gulati
 * Date: Jan 19, 2009
 */
public class CtcQuery extends AbstractQuery {

    private static String queryString = "select i from CtcTerm i order by i.id";

    private static String NAME = "term";


    public CtcQuery() {
        super(queryString);
 }

    public void filterByName(final String name){
        String searchString = name.toLowerCase();
        andWhere("(lower(i.ctepTerm) = :" + NAME + " or lower(i.term) = :" + NAME + ")");
        setParameter(NAME, searchString);

    }

}
