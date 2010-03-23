package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.cabig.ctms.audit.domain.DataAuditInfo;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.helper.TestDataManager;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;

import java.util.Collection;
import java.util.Date;
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

    public void testValidationExceptionForSavingInValidClinicalStaff() {
        inValidClinicalStaff = new ClinicalStaff();

        try {
            inValidClinicalStaff = clinicalStaffRepository.save(inValidClinicalStaff);
            fail();
        } catch (Exception e) {
            logger.info("expecting this");
        }

        try {
            inValidClinicalStaff.setFirstName("John");
            clinicalStaffRepository.save(inValidClinicalStaff);
        } catch (CtcAeSystemException e) {
            fail();
        }
        try {
            inValidClinicalStaff.setFirstName("John");
            inValidClinicalStaff.setLastName("Doe");
            clinicalStaffRepository.save(inValidClinicalStaff);
        } catch (CtcAeSystemException e) {
            fail();
        }
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
        int size = jdbcTemplate.queryForInt("select count(*) from clinical_Staffs clinicalStaffs where lower(clinicalStaffs.last_name ) like 'd%'");
        assertEquals(size, clinicalStaffs.size());

        for (ClinicalStaff clinicalStaff : clinicalStaffs) {
            assertTrue(clinicalStaff.getLastName().toLowerCase().contains("d"));
        }

    }



    @Override
    protected void onTearDownInTransaction() throws Exception {

        super.onTearDownInTransaction();


    }

}
