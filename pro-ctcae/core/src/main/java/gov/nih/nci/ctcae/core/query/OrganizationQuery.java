package gov.nih.nci.ctcae.core.query;

// TODO: Auto-generated Javadoc
/**
 * The Class OrganizationQuery.
 * 
 * @author Vinay Kumar
 * @crated Oct 7, 2008
 */
public class OrganizationQuery extends AbstractQuery {

    /** The query string. */
    private static String queryString = "SELECT distinct(o) from Organization o order by o.id";

    /** The ORGANIZATIO n_ name. */
    private static String ORGANIZATION_NAME = "name";

    /** The NC i_ code. */
    private static String NCI_CODE = "nciInstituteCode";

    /**
     * Instantiates a new organization query.
     */
    public OrganizationQuery() {

        super(queryString);
    }

    /**
     * Filter by organization name.
     * 
     * @param name the name
     */
    public void filterByOrganizationName(final String name) {
        String searchString = "%" + name.toLowerCase() + "%";
        andWhere("lower(o.name) LIKE :" + ORGANIZATION_NAME);
        setParameter(ORGANIZATION_NAME, searchString);
    }

    /**
     * Filter by organization name or nci institute code.
     * 
     * @param text the text
     */
    public void filterByOrganizationNameOrNciInstituteCode(final String text) {
        String searchString = "%" + text.toLowerCase() + "%";
        andWhere(String.format("(lower(o.name) LIKE :%s or lower(o.nciInstituteCode) LIKE :%s)", ORGANIZATION_NAME, NCI_CODE));
        setParameter(ORGANIZATION_NAME, searchString);
        setParameter(NCI_CODE, searchString);
    }

    /**
     * Filter by nci institute code.
     * 
     * @param nciInstituteCode the nci institute code
     */
    public void filterByNciInstituteCode(final String nciInstituteCode) {
        String searchString = "%" + nciInstituteCode.toLowerCase() + "%";
        andWhere("lower(o.nciInstituteCode) LIKE :" + NCI_CODE);
        setParameter(NCI_CODE, searchString);
    }

    /**
     * Filter by nci code exact match.
     * 
     * @param nciCode the nci code
     */
    public void filterByNciCodeExactMatch(final String nciCode) {
        andWhere("o.nciInstituteCode = :" + NCI_CODE);
        setParameter(NCI_CODE, nciCode);
    }

}