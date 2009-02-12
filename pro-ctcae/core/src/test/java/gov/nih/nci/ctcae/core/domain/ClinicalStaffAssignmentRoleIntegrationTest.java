package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;

import java.util.List;

/**
 * @author Vinay Kumar
 * @created Feb 06, 2009
 */
public class ClinicalStaffAssignmentRoleIntegrationTest extends AbstractHibernateIntegrationTestCase {


    private ClinicalStaffAssignmentRole clinicalStaffAssignmentRole;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        defaultClinicalStaffAssignment.getClinicalStaffAssignmentRoles().clear();

        defaultClinicalStaff = clinicalStaffRepository.save(defaultClinicalStaff);
        commitAndStartNewTransaction();

        defaultClinicalStaff = clinicalStaffRepository.findById(defaultClinicalStaff.getId());
        assertFalse("must have clinical staff assignment", defaultClinicalStaff.getClinicalStaffAssignments().isEmpty());

        defaultClinicalStaffAssignment = defaultClinicalStaff.getClinicalStaffAssignments().get(0);
        assertTrue("must remove clinical staff assignment role", defaultClinicalStaffAssignment.getClinicalStaffAssignmentRoles().isEmpty());

        clinicalStaffAssignmentRole = new ClinicalStaffAssignmentRole();

        defaultClinicalStaffAssignment.addClinicalStaffAssignmentRole(clinicalStaffAssignmentRole);

        saveAndRetrieveClinicalStaffAssignmentRole();


    }

    public void testAddClinicalStaffAssignmentRole() {
        assertEquals("must save role status", RoleStatus.ACTIVE, clinicalStaffAssignmentRole.getRoleStatus());


    }

    public void testUpdateClinicalStaffAssignmentRole() {


        clinicalStaffAssignmentRole.setRoleStatus(RoleStatus.IN_ACTIVE);

        saveAndRetrieveClinicalStaffAssignmentRole();

        assertEquals("must update role status", RoleStatus.IN_ACTIVE, clinicalStaffAssignmentRole.getRoleStatus());


    }

    private void saveAndRetrieveClinicalStaffAssignmentRole() {

        defaultClinicalStaff = clinicalStaffRepository.save(defaultClinicalStaff);
        commitAndStartNewTransaction();
        defaultClinicalStaff = clinicalStaffRepository.findById(defaultClinicalStaff.getId());

        List<ClinicalStaffAssignmentRole> clinicalStaffAssignmentRoles = defaultClinicalStaffAssignment.getClinicalStaffAssignmentRoles();

        clinicalStaffAssignmentRole = clinicalStaffAssignmentRoles.get(0);
        assertFalse("must save site clinical staff role", clinicalStaffAssignmentRoles.isEmpty());


        assertNotNull("must save site clinical staff role", clinicalStaffAssignmentRole.getId());
        assertEquals("clinical staff must be same", defaultClinicalStaffAssignment, clinicalStaffAssignmentRole.getClinicalStaffAssignment());
    }

    @Override
    protected void onTearDownInTransaction() throws Exception {
        deleteFromTables(new String[]{"CS_ASSIGNMENT_ROLES"});
        super.onTearDownInTransaction();


    }
}