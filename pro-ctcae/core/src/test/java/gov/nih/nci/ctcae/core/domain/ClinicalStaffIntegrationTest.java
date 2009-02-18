package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;

import java.util.Collection;
import java.util.List;

/**
 * @author mehul
 */

public class ClinicalStaffIntegrationTest extends AbstractHibernateIntegrationTestCase {

    private ClinicalStaff clinicalStaff, inValidClinicalStaff;
    protected final Integer DEFAULT_ORGANIZATION_ID = 105051;

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


    public void testDeleteSiteClinicalStaff() {


        List<SiteClinicalStaff> siteClinicalStaffs = defaultClinicalStaff.getSiteClinicalStaffs();
        assertFalse("must have site clinical staffs", siteClinicalStaffs.isEmpty());
        defaultClinicalStaff.removeSiteClinicalStaff(defaultSiteClinicalStaff);
        assertTrue("must remove site clinical staffs", siteClinicalStaffs.isEmpty());

        defaultClinicalStaff = clinicalStaffRepository.save(defaultClinicalStaff);

        commitAndStartNewTransaction();

        SiteClinicalStaff siteClinicalStaff = finderRepository.findById(SiteClinicalStaff.class, defaultSiteClinicalStaff.getId());

        assertNull("must remove site clinical staff ", siteClinicalStaff);
    }

    public void testfindByOrganizationId() {

        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByOrganization(DEFAULT_ORGANIZATION_ID);

        Collection<? extends ClinicalStaff> clinicalStaffs = clinicalStaffRepository.find(clinicalStaffQuery);
        assertFalse(clinicalStaffs.isEmpty());


        int size = jdbcTemplate.queryForInt("select count(*) from clinical_Staffs cs  left outer join site_clinical_staffs scs on cs.id=scs.clinical_staff_id " +
                "where scs.organization_id =" + DEFAULT_ORGANIZATION_ID);
        assertEquals(size, clinicalStaffs.size());

        boolean organizationFound = false;
        for (ClinicalStaff clinicalStaff : clinicalStaffs) {


            List<SiteClinicalStaff> siteClinicalStaffs = clinicalStaff.getSiteClinicalStaffs();
            for (SiteClinicalStaff siteClinicalStaff : siteClinicalStaffs) {
                if (siteClinicalStaff.getOrganization().getId().equals(DEFAULT_ORGANIZATION_ID)) {
                    organizationFound = true;
                }
            }

            if (!organizationFound) {
                fail("query is not returning clinical staffs on expected organization.");
            }
        }

    }

    public void testfindBySiteClinicalStaffByOrganizationId() {

        List<SiteClinicalStaff> siteClinicalStaffs = clinicalStaffRepository.findByStudyOrganizationId("b", defaultStudySite.getId());

        assertFalse(siteClinicalStaffs.isEmpty());

        String searchString = "%b%";
        int size = jdbcTemplate.queryForInt("select count(*) from site_clinical_staffs scs,clinical_Staffs cs where cs.id=scs.clinical_staff_id " +
                "and scs.organization_id =? and (lower(cs.first_name) like ? or lower(cs.last_name) like ? or lower(cs.nci_identifier) like ?)",
                new Object[]{DEFAULT_ORGANIZATION_ID, searchString, searchString, searchString});

        assertEquals(size, siteClinicalStaffs.size());

        for (SiteClinicalStaff siteClinicalStaff : siteClinicalStaffs) {
            assertEquals(DEFAULT_ORGANIZATION_ID, siteClinicalStaff.getOrganization().getId());
        }


    }

    @Override
    protected void onTearDownInTransaction() throws Exception {

        super.onTearDownInTransaction();


    }

}
