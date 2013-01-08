package gov.nih.nci.ctcae.core.query;

import gov.nih.nci.ctcae.core.domain.QueryStrings;
import gov.nih.nci.ctcae.core.domain.Role;
import gov.nih.nci.ctcae.core.domain.RoleStatus;

import java.util.Collection;
import java.util.Date;

/**
 * User: Harsh Agarwal
 * Date: Feb 25, 2008.
 */
public class StudyOrganizationClinicalStaffQuery extends AbstractQuery {

    private static String STUDY_ORGANIZATION_ID = "studyOrganizationId";
    private static String CLINICAL_STAFF_ID = "clinicalStaffId";
    private static String FIRST_NAME = "firstName";
    private static String LAST_NAME = "lastName";
    private static String NCI_IDENTIFIER = "nciIdentifier";
    private static String ROLE = "role";
    private static String ROLE_STATUS = "roleStatus";
    private static String TODAYS_DATE = "todaysDate";

    public StudyOrganizationClinicalStaffQuery() {
        super(QueryStrings.SOCS_QUERY_BASIC);
    }

    public void filterByFirstNameOrLastNameOrNciIdentifier(final String searchText) {
        String searchString = "%" + searchText.toLowerCase() + "%";
        andWhere(String.format("(lower(socs.organizationClinicalStaff.clinicalStaff.firstName) LIKE :%s or lower(socs.organizationClinicalStaff.clinicalStaff.lastName) LIKE :%s or lower(socs.organizationClinicalStaff.clinicalStaff.nciIdentifier) LIKE :%s)",
                FIRST_NAME, LAST_NAME, NCI_IDENTIFIER));
        setParameter(FIRST_NAME, searchString);
        setParameter(LAST_NAME, searchString);
        setParameter(NCI_IDENTIFIER, searchString);
    }


    public void filterByStudyOrganization(final Integer studyOrganizationId) {
        andWhere("socs.studyOrganization.id = :" + STUDY_ORGANIZATION_ID);
        setParameter(STUDY_ORGANIZATION_ID, studyOrganizationId);
    }

    public void filterByClinicalStaffId(final Integer clinicalStaffId) {
        andWhere("socs.organizationClinicalStaff.clinicalStaff.id = :" + CLINICAL_STAFF_ID);
        setParameter(CLINICAL_STAFF_ID, clinicalStaffId);
    }

    public void filterByRole(final Collection<Role> roles) {
        andWhere("socs.role in ( :" + ROLE + ")");
        setParameterList(ROLE, roles);
    }


    public void filterByActiveStatus() {
        andWhere("socs.roleStatus in ( :" + ROLE_STATUS + ")");
        setParameter(ROLE_STATUS, RoleStatus.ACTIVE);
        andWhere("socs.statusDate <= :" + TODAYS_DATE);
        setParameter(TODAYS_DATE, new Date());
    }

}