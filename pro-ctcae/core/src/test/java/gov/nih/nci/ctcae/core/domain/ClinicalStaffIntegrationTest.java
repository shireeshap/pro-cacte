package gov.nih.nci.ctcae.core.domain;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
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

    public void testfindByOrganizationId() {

        ClinicalStaffAssignmentRole clinicalStaffAssignmentRole = saveAndRetrieveClinicalStaffAssignmentRole();

        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByOrganizationAndRole(DEFAULT_ORGANIZATION_ID);

        Collection<? extends ClinicalStaff> clinicalStaffs = clinicalStaffRepository.find(clinicalStaffQuery);


        int size = jdbcTemplate.queryForInt("select count(*) from clinical_Staffs cs  left outer join CLINICAL_STAFF_ASSIGNMENTS csa on cs.id=csa.clinical_staff_id " +
                "left outer join CS_ASSIGNMENT_ROLES csar on csa.id=csar.clinical_staff_assignment_id " +
                "where csa.domain_object_id =? and csa.domain_object_class=? and csar.role in (?,?)", new Object[]{DEFAULT_ORGANIZATION_ID,
                Organization.class.getName(), Role.CRA.toString(), Role.PHYSICAN.toString()});

        assertEquals(size, clinicalStaffs.size());
        assertFalse(clinicalStaffs.isEmpty());

        boolean organizationFound = false;
        for (ClinicalStaff clinicalStaff : clinicalStaffs) {


            List<ClinicalStaffAssignment> clinicalStaffAssignments = clinicalStaff.getClinicalStaffAssignments();
            for (ClinicalStaffAssignment clinicalStaffAssignment : clinicalStaffAssignments) {
                if (StringUtils.equals(clinicalStaffAssignment.getDomainObjectClass(), Organization.class.getName())
                        && clinicalStaffAssignment.getDomainObjectId().equals(DEFAULT_ORGANIZATION_ID)) {
                    organizationFound = true;
                }
            }

            if (!organizationFound) {
                fail("query is not returning clinical staffs on expected organization.");
            }
        }

    }

    public void testDeleteClinicalStaffAssignment() {

        jdbcTemplate.execute("delete from CS_ASSIGNMENT_ROLES");
        jdbcTemplate.execute("delete from CLINICAL_STAFF_ASSIGNMENTS where domain_object_id=-1234");
        setComplete();
        endTransaction();
        startNewTransaction();

        ClinicalStaffAssignment clinicalStaffAssignment = new ClinicalStaffAssignment();
        clinicalStaffAssignment.setDomainObjectClass(Organization.class.getName());
        clinicalStaffAssignment.setDomainObjectId(-1234); //non existing org
        clinicalStaffAssignment.setClinicalStaff(defaultClinicalStaff);


        List<ClinicalStaffAssignment> clinicalStaffAssignments = new ArrayList<ClinicalStaffAssignment>();
        clinicalStaffAssignments.add(clinicalStaffAssignment);

        clinicalStaffRepository.save(clinicalStaffAssignments);

        commitAndStartNewTransaction();
        assertNotNull("must save clinical staff assignment", clinicalStaffAssignment.getId());
        //now remove it

        clinicalStaffRepository.removeClinicalStaffAssignment(clinicalStaffAssignment);


//        defaultClinicalStaff.removeClinicalStaffAssignment(clinicalStaffAssignment);
//        defaultClinicalStaff = clinicalStaffRepository.save(defaultClinicalStaff);

        commitAndStartNewTransaction();

        ClinicalStaffAssignment deletedClinicalStaffAssignment = finderRepository.findById(ClinicalStaffAssignment.class, clinicalStaffAssignment.getId());

        assertNull("must remove clinical staff assignment", clinicalStaffAssignment);
    }

    public void testfindByOrganizationIdAndSearchString() {


        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByOrganizationAndRole(DEFAULT_ORGANIZATION_ID);
        clinicalStaffQuery.filterByFirstNameOrLastNameOrNciIdentifier("bR");

        Collection<? extends ClinicalStaff> clinicalStaffs = clinicalStaffRepository.find(clinicalStaffQuery);
        assertFalse(clinicalStaffs.isEmpty());


        int size = jdbcTemplate.queryForInt("select count(*) from clinical_Staffs cs  left outer join CLINICAL_STAFF_ASSIGNMENTS csa on cs.id=csa.clinical_staff_id " +
                "where csa.domain_object_id =? and csa.domain_object_class=? and " +
                "(lower(cs.first_name) like ? or lower(cs.last_name) like ? or lower(cs.nci_identifier) like ?)",
                new Object[]{DEFAULT_ORGANIZATION_ID, Organization.class.getName(), "%br%", "%br%", "%br%"});
        assertEquals(size, clinicalStaffs.size());

        boolean organizationFound = false;
        for (ClinicalStaff clinicalStaff : clinicalStaffs) {

            assertTrue("query is not searching for first name and last name and nci identifier",
                    (StringUtils.contains(clinicalStaff.getFirstName().toLowerCase(), "br"))
                            || StringUtils.contains(clinicalStaff.getLastName().toLowerCase(), "br")
                            || StringUtils.contains(clinicalStaff.getNciIdentifier().toLowerCase(), "br"));

            List<ClinicalStaffAssignment> clinicalStaffAssignments = clinicalStaff.getClinicalStaffAssignments();
            for (ClinicalStaffAssignment clinicalStaffAssignment : clinicalStaffAssignments) {
                if (StringUtils.equals(clinicalStaffAssignment.getDomainObjectClass(), Organization.class.getName())
                        && clinicalStaffAssignment.getDomainObjectId().equals(DEFAULT_ORGANIZATION_ID)) {
                    organizationFound = true;
                }
            }

            if (!organizationFound) {
                fail("query is not returning clinical staffs on expected organization.");
            }
        }

    }


    @Override
    protected void onTearDownInTransaction() throws Exception {

        super.onTearDownInTransaction();


    }

}
