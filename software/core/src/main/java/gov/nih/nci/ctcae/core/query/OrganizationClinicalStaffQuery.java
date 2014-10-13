package gov.nih.nci.ctcae.core.query;
import gov.nih.nci.ctcae.core.domain.QueryStrings;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.RoleStatus;
import java.util.Date;

/**
 * User: Vinay Kumar
 * Date: Oct 15, 2008.
 */

public class OrganizationClinicalStaffQuery extends AbstractQuery {

    private static String ORGANIZATION_ID = "organizationId";
    private static String FIRST_NAME = "firstName";
    private static String LAST_NAME = "lastName";
    private static String NCI_IDENTIFIER = "nciIdentifier";
    private static String FIRST_LASTNAME = "firstLastName";

    /**
     * Instantiates a new organization clinical staff query.
     */
    public OrganizationClinicalStaffQuery() {
        super(QueryStrings.OCS_QUERY_BASIC);
    }

    /**
     * Instantiates a new organization clinical staff query.
     *
     * @param organizationId the organization id
     * @param originalStudyOrganizationId the original study organization id
     */
    public OrganizationClinicalStaffQuery(final Integer organizationId, final Integer originalStudyOrganizationId, Role role) {
        super(QueryStrings.OCS_QUERY_BASIC);
        andWhere("scs.organization.id = :" + ORGANIZATION_ID);
        setParameter(ORGANIZATION_ID, organizationId);
        filterByActive();
        filterDuplicateStaff(originalStudyOrganizationId, role);
    }

    
    /**
     * Filter duplicate staff.
     *
     * @param organizationId the organization id
     */
    public void filterDuplicateStaff(final Integer organizationId, Role role){
    	andWhere(" scs.id not in ( select socs.organizationClinicalStaff.id from StudyOrganizationClinicalStaff socs where socs.studyOrganization.id = "+organizationId+" and socs.roleStatus ='ACTIVE' and socs.role ='" + role+ "') ");
    }

    /**
     * Filter by first name or last name or nci identifier.
     *
     * @param searchText the search text
     */
    public void filterByFirstNameOrLastNameOrNciIdentifier(final String searchText) {
        String searchString = "%" + searchText.toLowerCase() + "%";
        andWhere(String.format("(lower(scs.clinicalStaff.firstName) LIKE :%s or lower(scs.clinicalStaff.lastName) LIKE :%s or lower(scs.clinicalStaff.nciIdentifier) LIKE :%s or lower(scs.clinicalStaff.firstName) || ' ' || lower(scs.clinicalStaff.lastName) LIKE :%s)",
                FIRST_NAME, LAST_NAME, NCI_IDENTIFIER, FIRST_LASTNAME));
        setParameter(FIRST_NAME, searchString);
        setParameter(LAST_NAME, searchString);
        setParameter(NCI_IDENTIFIER, searchString);
        setParameter(FIRST_LASTNAME, searchString);
    }

    /**
     * Filter by exact match nci identifier.
     *
     * @param nciIdentifier the nci identifier
     */
    public void filterByExactMatchNciIdentifier(final String nciIdentifier) {
        andWhere(String.format("lower(scs.clinicalStaff.nciIdentifier) = :%s)", NCI_IDENTIFIER));
        setParameter(NCI_IDENTIFIER, nciIdentifier.toLowerCase());
    }
      
    /**
     * Filter by exact match nci identifier and organization
     * 
     * @param nciIdentifier
     * @param organizationId
     */
    public void filterByExactMatchNciIdentifierAndOrganization(final String nciIdentifier, final Integer organizationId) {
        andWhere(String.format("lower(scs.clinicalStaff.nciIdentifier) = :%s)", NCI_IDENTIFIER));
        setParameter(NCI_IDENTIFIER, nciIdentifier.toLowerCase());
        andWhere("scs.organization.id = :" + ORGANIZATION_ID);
        setParameter(ORGANIZATION_ID, organizationId);
    }

    /**
     * Filter by active.
     */
    public void filterByActive() {
        andWhere("((scs.clinicalStaff.status =:status1 and scs.clinicalStaff.effectiveDate <=:effectiveDate) or (scs.clinicalStaff.status =:status2 and scs.clinicalStaff.effectiveDate > :effectiveDate))");
        setParameter("status1", RoleStatus.ACTIVE);
        setParameter("status2", RoleStatus.IN_ACTIVE);
        setParameter("effectiveDate",  new Date());
    }
}