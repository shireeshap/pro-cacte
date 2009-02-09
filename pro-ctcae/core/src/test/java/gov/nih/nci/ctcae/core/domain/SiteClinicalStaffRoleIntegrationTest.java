package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;

import java.util.List;

/**
 * @author Vinay Kumar
 * @created Feb 06, 2009
 */
public class SiteClinicalStaffRoleIntegrationTest extends AbstractHibernateIntegrationTestCase {


    private SiteClinicalStaffRole siteClinicalStaffRole;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


    }

    public void testAddSiteClinicalStaffRole() {
        siteClinicalStaffRole = new SiteClinicalStaffRole();
        defaultSiteClinicalStaff.addSiteClinicalStaffRole(siteClinicalStaffRole);

        saveAndRetrieveSiteClinicalStaffRole();
        assertEquals("must save role status", RoleStatus.ACTIVE, siteClinicalStaffRole.getRoleStatus());


    }

    public void testUpdateSiteClinicalStaffRole() {


        saveAndRetrieveSiteClinicalStaffRole();

        siteClinicalStaffRole.setRoleStatus(RoleStatus.IN_ACTIVE);

        saveAndRetrieveSiteClinicalStaffRole();

        assertEquals("must update role status", RoleStatus.IN_ACTIVE, siteClinicalStaffRole.getRoleStatus());


    }

    private void saveAndRetrieveSiteClinicalStaffRole() {

        defaultClinicalStaff = clinicalStaffRepository.save(defaultClinicalStaff);
        commitAndStartNewTransaction();
        defaultClinicalStaff = clinicalStaffRepository.findById(defaultClinicalStaff.getId());

        List<SiteClinicalStaffRole> siteClinicalStaffRoles = defaultSiteClinicalStaff.getSiteClinicalStaffRoles();

        siteClinicalStaffRole = siteClinicalStaffRoles.get(0);
        assertFalse("must save site clinical staff role", siteClinicalStaffRoles.isEmpty());


        assertNotNull("must save site clinical staff role", siteClinicalStaffRole.getId());
        assertEquals("clinical staff must be same", defaultSiteClinicalStaff, siteClinicalStaffRole.getSiteClinicalStaff());
    }

    @Override
    protected void onTearDownInTransaction() throws Exception {
        deleteFromTables(new String[]{"site_clinical_staff_roles"});
        super.onTearDownInTransaction();


    }
}