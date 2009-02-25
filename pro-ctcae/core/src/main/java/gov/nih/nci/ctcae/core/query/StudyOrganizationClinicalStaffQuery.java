package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Role;

import java.util.List;

//
/**
 * User: Harsh Agarwal
 * Date: Feb 25, 2008.
 */
public class StudyOrganizationClinicalStaffQuery extends AbstractQuery {

    /**
     * The query string.
     */
    private static String queryString = "SELECT socs from StudyOrganizationClinicalStaff socs order by socs.organizationClinicalStaff.clinicalStaff.firstName";

    private static String ORGANIZATION_ID = "organizationId";

    private static String FIRST_NAME = "firstName";

    private static String LAST_NAME = "lastName";

    private static String NCI_IDENTIFIER = "nciIdentifier";

    private static String ROLE = "role";


    public StudyOrganizationClinicalStaffQuery() {
        super(queryString);
    }


    public void filterByFirstNameOrLastNameOrNciIdentifier(final String searchText) {
        String searchString = "%" + searchText.toLowerCase() + "%";
        andWhere(String.format("(lower(socs.organizationClinicalStaff.clinicalStaff.firstName) LIKE :%s or lower(socs.organizationClinicalStaff.clinicalStaff.lastName) LIKE :%s or lower(socs.organizationClinicalStaff.clinicalStaff.nciIdentifier) LIKE :%s)",
                FIRST_NAME, LAST_NAME, NCI_IDENTIFIER));
        setParameter(FIRST_NAME, searchString);
        setParameter(LAST_NAME, searchString);
        setParameter(NCI_IDENTIFIER, searchString);
    }


    public void filterByOrganization(final Integer organizationId) {
        andWhere("socs.organizationClinicalStaff.organization.id = :" + ORGANIZATION_ID);
        setParameter(ORGANIZATION_ID, organizationId);
    }

    public void filterByRole(final List<Role> roles) {
        andWhere("socs.role in ( :" + ROLE + ")");
        setParameterList(ROLE, roles);
    }


}