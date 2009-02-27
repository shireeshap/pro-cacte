package gov.nih.nci.ctcae.core.query;

//
/**
 * User: Mehul Gulati
 * Date: Oct 15, 2008.
 */
public class ClinicalStaffQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT cs from ClinicalStaff cs order by cs.id";

    private static String ORGANIZATION_ID = "organizationId";
    private static String ORGANIZATION_CLASS = "domainObjectClass";

    /**
     * The FIRS t_ name.
     */
    private static String FIRST_NAME = "firstName";

    /**
     * The LAS t_ name.
     */
    private static String LAST_NAME = "lastName";
    private static String USER_ID = "userId";

    /**
     * The NC i_ identifier.
     */
    private static String NCI_IDENTIFIER = "nciIdentifier";
    private String ROLES = "roles";


    /**
     * Instantiates a new clinical staff query.
     */
    public ClinicalStaffQuery() {

        super(queryString);
    }

    /**
     * Filter by clinical staff first name.
     *
     * @param firstName the first name
     */
    public void filterByClinicalStaffFirstName(final String firstName) {
        String searchString = "%" + firstName.toLowerCase() + "%";
        andWhere("lower(cs.firstName) LIKE :" + FIRST_NAME);
        setParameter(FIRST_NAME, searchString);
    }

    public void filterByFirstNameOrLastNameOrNciIdentifier(final String searchText) {
        String searchString = "%" + searchText.toLowerCase() + "%";
        andWhere(String.format("(lower(cs.firstName) LIKE :%s or lower(cs.lastName) LIKE :%s or lower(cs.nciIdentifier) LIKE :%s)",
                FIRST_NAME, LAST_NAME, NCI_IDENTIFIER));
        setParameter(FIRST_NAME, searchString);
        setParameter(LAST_NAME, searchString);
        setParameter(NCI_IDENTIFIER, searchString);
    }

    /**
     * Filter by clinical staff last name.
     *
     * @param lastName the last name
     */
    public void filterByClinicalStaffLastName(final String lastName) {
        String searchString = "%" + lastName.toLowerCase() + "%";
        andWhere("lower(cs.lastName) LIKE :" + LAST_NAME);
        setParameter(LAST_NAME, searchString);
    }

    public void filterByUserId(final Integer userId) {
        andWhere("cs.user.id =:" + USER_ID);
        setParameter(USER_ID, userId);
    }
    /*
    public void filterByNciIdentifier(final String text) {
        String searchString = text != null ? "%" + text.toLowerCase() + "%" : null;

        andWhere(String.format("lower(clinicalstaff.nciIdentifier) LIKE :%s", NCI_IDENTIFIER));
        setParameter(NCI_IDENTIFIER, searchString);

    }
    */

    /**
     * Filter by nci identifier.
     *
     * @param nciIdentifier the nci identifier
     */
    public void filterByNciIdentifier(final String nciIdentifier) {
        String searchString = "%" + nciIdentifier.toLowerCase() + "%";
        andWhere("lower(cs.nciIdentifier) LIKE :" + NCI_IDENTIFIER);
        setParameter(NCI_IDENTIFIER, searchString);
    }


    public void filterByOrganization(final Integer organizationId) {
        leftJoin("cs.organizationClinicalStaffs as scs");
        andWhere("scs.organization.id = :" + ORGANIZATION_ID);
        setParameter(ORGANIZATION_ID, organizationId);
    }


}
