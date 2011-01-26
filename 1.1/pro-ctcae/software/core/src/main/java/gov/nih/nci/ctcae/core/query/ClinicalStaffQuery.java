package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.RoleStatus;

import java.util.Date;

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
    private static String EMAIL = "emailAddress";

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
        filterByActive();
    }

    public ClinicalStaffQuery(boolean showInactive) {
        super(queryString);
        if (!showInactive) {
            filterByActive();
        }
    }

    /**
     * Filter by clinical staff first name.
     *
     * @param firstName the first name
     */
    public void filterByClinicalStaffFirstName(final String firstName) {
        String searchString = firstName.toLowerCase();
        andWhere("lower(cs.firstName) LIKE :" + FIRST_NAME);
        setParameter(FIRST_NAME, searchString);
    }

    /**
     * Filter by clinical staff last name.
     *
     * @param lastName the last name
     */
    public void filterByClinicalStaffLastName(final String lastName) {
        String searchString = lastName.toLowerCase();
        andWhere("lower(cs.lastName) LIKE :" + LAST_NAME);
        setParameter(LAST_NAME, searchString);
    }

    public void filterByEmail(final String email) {
        String searchString = email.toLowerCase();
        andWhere("lower(cs.emailAddress) LIKE :" + EMAIL);
        setParameter(EMAIL, searchString);
    }

    public void filterByUserId(final Integer userId) {
        andWhere("cs.user.id =:" + USER_ID);
        setParameter(USER_ID, userId);
    }

    public void filterByUserName(final String userName) {
        andWhere("cs.user.username =:" + "username");
        setParameter("username", userName);
    }

    /**
     * Filter by nci identifier.
     *
     * @param nciIdentifier the nci identifier
     */
    public void filterByNciIdentifier(final String nciIdentifier) {
        String searchString = nciIdentifier.toLowerCase();
        andWhere("lower(cs.nciIdentifier) LIKE :" + NCI_IDENTIFIER);
        setParameter(NCI_IDENTIFIER, searchString);
    }


    public void filterByOrganization(final Integer organizationId) {
        leftJoin("cs.organizationClinicalStaffs as scs");
        andWhere("scs.organization.id = :" + ORGANIZATION_ID);
        setParameter(ORGANIZATION_ID, organizationId);
    }

    public void filterByActive() {
        andWhere("((cs.status =:status1 and cs.effectiveDate <=:effectiveDate) or (cs.status =:status2 and cs.effectiveDate > :effectiveDate))");
        setParameter("status1", RoleStatus.ACTIVE);
        setParameter("status2", RoleStatus.IN_ACTIVE);
        setParameter("effectiveDate",  new Date());
    }

}
