package gov.nih.nci.ctcae.core.query;

/**
 * @author  Vinay Kumar
 * @crated Oct 7, 2008
 */
public class OrganizationQuery extends AbstractQuery {

    private static String queryString = "SELECT o from Organization o order by o.id";

    private static String ORGANIZATION_NAME = "name";

    private static String NCI_CODE = "nciInstituteCode";

    public OrganizationQuery() {

        super(queryString);
    }

    public void filterByOrganizationName(final String name) {
        String searchString = "%" + name.toLowerCase() + "%";
        andWhere("lower(o.name) LIKE :" + ORGANIZATION_NAME);
        setParameter(ORGANIZATION_NAME, searchString);
    }
    public void filterByOrganizationNameOrNciInstituteCode(final String text) {
        String searchString = "%" + text.toLowerCase() + "%";
        andWhere(String.format("(lower(o.name) LIKE :%s or lower(o.nciInstituteCode) LIKE :%s)" , ORGANIZATION_NAME,NCI_CODE));
        setParameter(ORGANIZATION_NAME, searchString);
        setParameter(NCI_CODE, searchString);
    }

    public void filterByNciInstituteCode(final String nciInstituteCode) {
        String searchString = "%" + nciInstituteCode.toLowerCase() + "%";
        andWhere("lower(o.nciInstituteCode) LIKE :" + NCI_CODE);
        setParameter(NCI_CODE, searchString);
    }

    public void filterByNciCodeExactMatch(final String nciCode) {
        andWhere("o.nciInstituteCode = :" + NCI_CODE);
        setParameter(NCI_CODE, nciCode);
    }

}