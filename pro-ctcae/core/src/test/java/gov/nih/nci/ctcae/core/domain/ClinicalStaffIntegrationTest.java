package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;

import java.util.Collection;

/**
 * @author mehul
 */

public class ClinicalStaffIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private ClinicalStaff clinicalStaff, inValidClinicalStaff;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        login();
        /*  clinicalStaffRepository.setGenericRepository(new JpaGenericRepository<ClinicalStaff>());   */
    }

    private void saveClinicalStaff() {
        clinicalStaff = new ClinicalStaff();
        clinicalStaff.setFirstName("John");
        clinicalStaff.setLastName("Dow");
        clinicalStaff.setNciIdentifier("NCI 01");
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
        } catch (CtcAeSystemException e) {
            logger.info("expecting this");
        }

        try {
            inValidClinicalStaff.setFirstName("John");
            clinicalStaffRepository.save(inValidClinicalStaff);
            fail();
        } catch (CtcAeSystemException e) {
        }
        try {
            inValidClinicalStaff.setFirstName("John");
            inValidClinicalStaff.setLastName("Doe");
            clinicalStaffRepository.save(inValidClinicalStaff);
            fail();

        } catch (CtcAeSystemException e) {
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
        clinicalStaffQuery.filterByClinicalStaffFirstName("J");

        Collection<? extends ClinicalStaff> clinicalStaffs = clinicalStaffRepository.find(clinicalStaffQuery);
        assertFalse(clinicalStaffs.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from clinical_Staffs clinicalStaffs where lower(clinicalStaffs.first_name ) like '%j%'");
        assertEquals(size, clinicalStaffs.size());

        for (ClinicalStaff clinicalStaff : clinicalStaffs) {
            assertTrue(clinicalStaff.getFirstName().toLowerCase().contains("j"));
        }
    }

    public void testFindByLastName() {
        saveClinicalStaff();

        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByClinicalStaffLastName("D");

        Collection<? extends ClinicalStaff> clinicalStaffs = clinicalStaffRepository.find(clinicalStaffQuery);
        assertFalse(clinicalStaffs.isEmpty());
        int size = jdbcTemplate.queryForInt("select count(*) from clinical_Staffs clinicalStaffs where lower(clinicalStaffs.last_name ) like '%d%'");
        assertEquals(size, clinicalStaffs.size());

        for (ClinicalStaff clinicalStaff : clinicalStaffs) {
            assertTrue(clinicalStaff.getLastName().toLowerCase().contains("d"));
        }

    }


}
