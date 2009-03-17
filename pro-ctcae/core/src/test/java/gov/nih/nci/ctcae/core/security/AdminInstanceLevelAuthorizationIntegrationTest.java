package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.ClinicalStaff;
import gov.nih.nci.ctcae.core.domain.Organization;
import gov.nih.nci.ctcae.core.domain.User;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;

import java.util.Collection;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class AdminInstanceLevelAuthorizationIntegrationTest extends AbstractInstanceLevelAuthorizationIntegrationTestCase {

    private User user;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        user = userRepository.loadUserByUsername(SYSTEM_ADMIN);
        login(user);


    }


    public void testOrganizationInstanceSecurity() throws Exception {

        OrganizationQuery organizationQuery = new OrganizationQuery();

        List<Organization> organizations = (List<Organization>) organizationRepository.find(organizationQuery);

        int numberOfOrganizations = jdbcTemplate.queryForInt("select count(*) from organizations");
        assertFalse("must find organizations", organizations.isEmpty());
        assertEquals("user should all  organizations because this is admin role .", numberOfOrganizations, organizations.size());


    }

    public void testClinicalStaffSecurityOnCreateAndEdit() throws Exception {


        ClinicalStaff clinicalStaff2 = Fixture.createClinicalStaffWithOrganization("Bruce", "Williams", "-123456", defaultOrganization);
        clinicalStaff2 = clinicalStaffRepository.save(clinicalStaff2);

        commitAndStartNewTransaction();


    }


    public void testClinicalStaffSecurityOnFindMultiple() throws Exception {

        ClinicalStaff clinicalStaff1 = Fixture.createClinicalStaffWithOrganization("David", "Williams", "-123456", wake);
        clinicalStaff1 = clinicalStaffRepository.save(clinicalStaff1);


        ClinicalStaff clinicalStaff2 = Fixture.createClinicalStaffWithOrganization("Bruce", "Williams", "-123456", defaultOrganization);
        clinicalStaff2 = clinicalStaffRepository.save(clinicalStaff2);

        commitAndStartNewTransaction();
        int numberOfClinicalStaffs = jdbcTemplate.queryForInt("select count(*) from CLINICAL_STAFFS");
        assertTrue("must have atleast 2 results", numberOfClinicalStaffs >= 2);


        Collection<ClinicalStaff> clinicalStaffs = clinicalStaffRepository.find(new ClinicalStaffQuery());
        assertFalse("must find all clinical staff", clinicalStaffs.isEmpty());
        assertEquals("must see all clinical staff because this is admin user", numberOfClinicalStaffs, clinicalStaffs.size());


    }

    public void testClinicalStaffSecurityOnFindById() throws Exception {

        ClinicalStaff clinicalStaff2 = Fixture.createClinicalStaffWithOrganization("Bruce", "Williams", "-123456", defaultOrganization);
        clinicalStaff2 = clinicalStaffRepository.save(clinicalStaff2);

        commitAndStartNewTransaction();


        ClinicalStaff savedClinicalStaff = clinicalStaffRepository.findById(clinicalStaff2.getId());
        assertEquals("must see all clinical staff ", savedClinicalStaff, clinicalStaff2);


    }

    public void testClinicalStafffSecurityOnFindSingle() throws Exception {

        ClinicalStaff clinicalStaff2 = Fixture.createClinicalStaffWithOrganization("Bruce", "Williams", "-123456", defaultOrganization);
        clinicalStaff2 = clinicalStaffRepository.save(clinicalStaff2);

        commitAndStartNewTransaction();
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByClinicalStaffFirstName("Bruce");
        ClinicalStaff savedClinicalStaff = clinicalStaffRepository.findSingle(clinicalStaffQuery);
        assertNotNull("must see all clinial staff", savedClinicalStaff);


    }


}