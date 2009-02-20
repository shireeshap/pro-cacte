package gov.nih.nci.ctcae.core.query;

//
/**
 * User: Vinay Kumar
 * Date: Oct 15, 2008.
 */
public class OrganizationClinicalStaffQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT scs from OrganizationClinicalStaff scs order by scs.clinicalStaff.firstName";

    private static String ORGANIZATION_ID = "organizationId";

    private static String FIRST_NAME = "firstName";

    private static String LAST_NAME = "lastName";

    private static String NCI_IDENTIFIER = "nciIdentifier";


    public OrganizationClinicalStaffQuery() {

        super(queryString);
    }


    public void filterByFirstNameOrLastNameOrNciIdentifier(final String searchText) {
        String searchString = "%" + searchText.toLowerCase() + "%";
        andWhere(String.format("(lower(scs.clinicalStaff.firstName) LIKE :%s or lower(scs.clinicalStaff.lastName) LIKE :%s or lower(scs.clinicalStaff.nciIdentifier) LIKE :%s)",
                FIRST_NAME, LAST_NAME, NCI_IDENTIFIER));
        setParameter(FIRST_NAME, searchString);
        setParameter(LAST_NAME, searchString);
        setParameter(NCI_IDENTIFIER, searchString);
    }


    public void filterByOrganization(final Integer organizationId) {
        andWhere("scs.organization.id = :" + ORGANIZATION_ID);
        setParameter(ORGANIZATION_ID, organizationId);
    }


}