package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;

import java.util.List;

/**
 * @author Vinay Kumar
 * @created Feb 06, 2009
 */
public class StudySiteClinicalStaffRoleIntegrationTest extends AbstractHibernateIntegrationTestCase {


    private StudySiteClinicalStaffRole studySiteClinicalStaffRole;

    private StudySiteClinicalStaff studySiteClinicalStaff;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        studySiteClinicalStaff = addStudySiteClinicalStaff();
        studySiteClinicalStaffRole = new StudySiteClinicalStaffRole();
        studySiteClinicalStaffRole.setRole(Role.SITE_CRA);
        studySiteClinicalStaffRole.setRoleStatus(RoleStatus.ACTIVE);
        studySiteClinicalStaff.addStudySiteClinicalStaffRole(studySiteClinicalStaffRole);

    }

    public void testStudyAddSiteClinicalStaffRole() {

        saveAndRetrieveStudySiteClinicalStaffRole();
        assertEquals("must save role status", RoleStatus.ACTIVE, studySiteClinicalStaffRole.getRoleStatus());


    }

    public void testDeleteStudySiteClinicalStaffRole() {

        saveAndRetrieveStudySiteClinicalStaffRole();

        //now remove it
        studySiteClinicalStaff.getStudySiteClinicalStaffRoles().clear();
        defaultStudy = studyRepository.save(defaultStudy);

        commitAndStartNewTransaction();


        studySiteClinicalStaff = finderRepository.findById(StudySiteClinicalStaff.class, studySiteClinicalStaff.getId());
        assertNotNull("must find study clinical staff", studySiteClinicalStaff);


        List<StudySiteClinicalStaffRole> studySiteClinicalStaffRoles = studySiteClinicalStaff.getStudySiteClinicalStaffRoles();

        assertTrue("must remove  study site clinical staff role", studySiteClinicalStaffRoles.isEmpty());

    }


    private void saveAndRetrieveStudySiteClinicalStaffRole() {

        defaultStudy = studyRepository.save(defaultStudy);

        commitAndStartNewTransaction();

        studySiteClinicalStaff = finderRepository.findById(StudySiteClinicalStaff.class, studySiteClinicalStaff.getId());
        assertNotNull("must find study clinical staff", studySiteClinicalStaff);


        List<StudySiteClinicalStaffRole> studySiteClinicalStaffRoles = studySiteClinicalStaff.getStudySiteClinicalStaffRoles();

        assertFalse("must save site clinical staff role", studySiteClinicalStaffRoles.isEmpty());
        studySiteClinicalStaffRole = studySiteClinicalStaffRoles.get(0);


        assertNotNull("must save site clinical staff role", studySiteClinicalStaffRole.getId());
        assertEquals("clinical staff must be same", studySiteClinicalStaff, studySiteClinicalStaffRole.getStudySiteClinicalStaff());
    }

    @Override
    protected void onTearDownInTransaction() throws Exception {
        deleteFromTables(new String[]{"ss_clinical_staff_roles"});
        super.onTearDownInTransaction();


    }
}