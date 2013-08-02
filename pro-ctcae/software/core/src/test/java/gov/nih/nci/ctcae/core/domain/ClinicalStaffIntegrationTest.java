package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;

import java.util.Collection;
import java.util.UUID;

/**
 * @author mehul
 */

public class ClinicalStaffIntegrationTest extends TestDataManager {

    private ClinicalStaff clinicalStaff, inValidClinicalStaff;


    private void saveClinicalStaff() {
        clinicalStaff = Fixture.createClinicalStaff("John" + UUID.randomUUID(), "Dow", "NCI 01");
        clinicalStaff = clinicalStaffRepository.save(clinicalStaff);
    }

    public void testSaveClinicalStaff() {
        saveClinicalStaff();

        assertNotNull(clinicalStaff.getId());

    }


    public void testFindById() {
                
        saveClinicalStaff();

        ClinicalStaff existingClinicalStaff = clinicalStaffRepository.findById(clinicalStaff.getId());
        assertEquals(clinicalStaff.getFirstName(), existingClinicalStaff.getFirstName());
        assertEquals(clinicalStaff.getLastName(), existingClinicalStaff.getLastName());
        assertEquals(clinicalStaff.getNciIdentifier(), existingClinicalStaff.getNciIdentifier());
    }

    public void testFindByFirstName() {
        saveClinicalStaff();

        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByClinicalStaffFirstName("J%");

        Collection<? extends ClinicalStaff> clinicalStaffs = clinicalStaffRepository.find(clinicalStaffQuery);
        assertFalse(clinicalStaffs.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from clinical_Staffs clinicalStaffs where lower(clinicalStaffs.first_name ) like 'j%'");
        assertEquals(size, clinicalStaffs.size());

        for (ClinicalStaff clinicalStaff : clinicalStaffs) {
            assertTrue(clinicalStaff.getFirstName().toLowerCase().contains("j"));
        }
    }


    public void testFindByLastName() {
        saveClinicalStaff();

        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByClinicalStaffLastName("D%");

        Collection<? extends ClinicalStaff> clinicalStaffs = clinicalStaffRepository.find(clinicalStaffQuery);
        assertFalse(clinicalStaffs.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from clinical_Staffs clinicalStaffs where lower(clinicalStaffs.last_name ) like '%d%'");
        assertEquals(size, clinicalStaffs.size());

        for (ClinicalStaff clinicalStaff : clinicalStaffs) {
            assertTrue(clinicalStaff.getLastName().toLowerCase().contains("d"));
        }

    }
    
    public void testDeactivateClinicalStaff(){
    	saveClinicalStaff();
    	assertEquals(RoleStatus.ACTIVE, clinicalStaff.getStatus());
    	clinicalStaff.deactivateClinicalStaff();
    	assertEquals(RoleStatus.IN_ACTIVE, clinicalStaff.getStatus());
    }
    
    public void testActivateClinicalStaff(){
    	saveClinicalStaff();
    	clinicalStaff.deactivateClinicalStaff();
    	assertEquals(RoleStatus.IN_ACTIVE, clinicalStaff.getStatus());
    	clinicalStaff.activateClinicalStaff(null);
    	assertEquals(RoleStatus.ACTIVE, clinicalStaff.getStatus());
    }



    @Override
    protected void onTearDownInTransaction() throws Exception {

        super.onTearDownInTransaction();


    }

}
