package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;

/**
 * @author Vinay Kumar
 * @created Feb 06, 2009
 */
public class ClinicalStaffAssignmentRoleIntegrationTest extends AbstractHibernateIntegrationTestCase {


    private ClinicalStaffAssignmentRole clinicalStaffAssignmentRole;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


        clinicalStaffAssignmentRole = saveAndRetrieveClinicalStaffAssignmentRole();


    }

    public void testAddClinicalStaffAssignmentRole() {
        assertEquals("must save role status", RoleStatus.ACTIVE, clinicalStaffAssignmentRole.getRoleStatus());


    }

    public void testUpdateClinicalStaffAssignmentRole() {


        clinicalStaffAssignmentRole.setRoleStatus(RoleStatus.IN_ACTIVE);

        clinicalStaffAssignmentRole = saveAndRetrieveClinicalStaffAssignmentRole();

        assertEquals("must update role status", RoleStatus.IN_ACTIVE, clinicalStaffAssignmentRole.getRoleStatus());


    }


    @Override
    protected void onTearDownInTransaction() throws Exception {
        deleteFromTables(new String[]{"CS_ASSIGNMENT_ROLES"});
        super.onTearDownInTransaction();


    }
}