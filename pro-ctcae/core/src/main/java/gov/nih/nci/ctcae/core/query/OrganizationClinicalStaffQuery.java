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
    private static String FIRST_LASTNAME = "firstLastName";

    public OrganizationClinicalStaffQuery() {
        super(queryString);
    }

    public OrganizationClinicalStaffQuery(final Integer organizationId) {
        super(queryString);
        andWhere("scs.organization.id = :" + ORGANIZATION_ID);
        setParameter(ORGANIZATION_ID, organizationId);
    }


    public void filterByFirstNameOrLastNameOrNciIdentifier(final String searchText) {
        String searchString = "%" + searchText.toLowerCase() + "%";
        andWhere(String.format("(lower(scs.clinicalStaff.firstName) LIKE :%s or lower(scs.clinicalStaff.lastName) LIKE :%s or lower(scs.clinicalStaff.nciIdentifier) LIKE :%s or lower(scs.clinicalStaff.firstName) || ' ' || lower(scs.clinicalStaff.lastName) LIKE :%s)",
                FIRST_NAME, LAST_NAME, NCI_IDENTIFIER, FIRST_LASTNAME));
        setParameter(FIRST_NAME, searchString);
        setParameter(LAST_NAME, searchString);
        setParameter(NCI_IDENTIFIER, searchString);
        setParameter(FIRST_LASTNAME, searchString);
    }

    public void filterByExactMatchNciIdentifier(final String nciIdentifier) {
        andWhere(String.format("lower(scs.clinicalStaff.nciIdentifier) = :%s)", NCI_IDENTIFIER));
        setParameter(NCI_IDENTIFIER, nciIdentifier.toLowerCase());
    }
}