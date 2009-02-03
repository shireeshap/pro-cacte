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
    private static String queryString = "SELECT i from ClinicalStaff i order by i.id";

    /**
     * The FIRS t_ name.
     */
    private static String FIRST_NAME = "firstName";

    /**
     * The LAS t_ name.
     */
    private static String LAST_NAME = "lastName";

    /**
     * The NC i_ identifier.
     */
    private static String NCI_IDENTIFIER = "nciIdentifier";


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
        andWhere("lower(i.firstName) LIKE :" + FIRST_NAME);
        setParameter(FIRST_NAME, searchString);
    }

    /**
     * Filter by clinical staff last name.
     *
     * @param lastName the last name
     */
    public void filterByClinicalStaffLastName(final String lastName) {
        String searchString = "%" + lastName.toLowerCase() + "%";
        andWhere("lower(i.lastName) LIKE :" + LAST_NAME);
        setParameter(LAST_NAME, searchString);
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
        andWhere("lower(i.nciIdentifier) LIKE :" + NCI_IDENTIFIER);
        setParameter(NCI_IDENTIFIER, searchString);
    }
}
