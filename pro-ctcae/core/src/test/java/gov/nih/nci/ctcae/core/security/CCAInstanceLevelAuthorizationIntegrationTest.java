package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ClinicalStaffQuery;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.query.StudyQuery;

import java.util.Collection;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class CCAInstanceLevelAuthorizationIntegrationTest extends AbstractInstanceLevelAuthorizationIntegrationTestCase {

    private User user;
    protected CRF defaultCRF;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();

        UserRole userRole = new UserRole();
        userRole.setRole(Role.CCA);
        anotherClinicalStaff.getUser().addUserRole(userRole);
        anotherClinicalStaff = clinicalStaffRepository.save(anotherClinicalStaff);
        commitAndStartNewTransaction();

        user = anotherClinicalStaff.getUser();
        login(user);

        study1 = createStudy("-10004");
        study2 = createStudy("-1005");


        study2 = studyRepository.save(study2);
        study1 = studyRepository.save(study1);
        commitAndStartNewTransaction();


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


    public void testOrganizationClinicalStaffSecurityOnFind() throws Exception {


        ClinicalStaff clinicalStaff1 = Fixture.createClinicalStaffWithOrganization("Clinical staff1", "Williams", "-1234568", duke);
        clinicalStaff1 = clinicalStaffRepository.save(clinicalStaff1);
        ClinicalStaff clinicalStaff2 = Fixture.createClinicalStaffWithOrganization("Clinical staff2", "Williams", "-1234568", duke);
        clinicalStaff2 = clinicalStaffRepository.save(clinicalStaff2);

        commitAndStartNewTransaction();

        int numberOfOrganizationClinicalStaffs = jdbcTemplate.queryForInt("select count(*) from ORGANIZATION_CLINICAL_STAFFS where organization_id=?", new Object[]{defaultStudySite.getOrganization().getId()});

        List<OrganizationClinicalStaff> organizationClinicalStaffs = organizationClinicalStaffRepository.findByStudyOrganizationId("%", defaultStudySite.getId());

        assertFalse("must find organizationClinicalStaffs", organizationClinicalStaffs.isEmpty());
        assertEquals("must see all organization clinical staff", numberOfOrganizationClinicalStaffs, organizationClinicalStaffs.size());


    }


    public void testStudyInstanceSecurity() throws Exception {

        Collection<Study> studies = studyRepository.find(new StudyQuery());
        assertFalse("must find studies", studies.isEmpty());

        int numberOfStudies = jdbcTemplate.queryForInt("select count(*) from STUDIES");

        assertEquals("must see all studies ", numberOfStudies, studies.size());


    }


    public void testStudyInstanceSecurityForMultipleStudies() throws Exception {

        //this user can see two studies
        Collection<Study> studies = studyRepository.find(new StudyQuery());
        assertFalse("must find studies", studies.isEmpty());
        int numberOfStudies = jdbcTemplate.queryForInt("select count(*) from STUDIES");

        assertEquals("must see all studies", numberOfStudies, studies.size());


    }

    public void testStudyInstanceSecurityForStudyOrganizations() throws Exception {

        List<? extends StudyOrganization> studyOrganizations = (List<? extends StudyOrganization>) studyOrganizationRepository.find(new StudyOrganizationQuery());

        assertFalse("must find study organizations", studyOrganizations.isEmpty());

        int numberOfStudyOrganization = jdbcTemplate.queryForInt("select count(*) from STUDY_ORGANIZATIONS");

        assertEquals("must see all study organizations ", numberOfStudyOrganization, studyOrganizations.size());


    }

    public void testStudyInstanceSecurityByUsingFindById() throws Exception {

        //this user can see all studies
        Study study = studyRepository.findById(study1.getId());
        assertEquals("must see all studies", study, study1);


    }

    public void testStudyInstanceSecurityByUsingFindSingle() throws Exception {

        //this user can see two studies
        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterByAssignedIdentifierExactMatch(study1.getAssignedIdentifier());
        Study study = studyRepository.findSingle(studyQuery);
        assertEquals("must see his all studies ", study, study1);

        studyQuery = new StudyQuery();
        studyQuery.filterByAssignedIdentifierExactMatch(defaultStudy.getAssignedIdentifier());

        study = studyRepository.findSingle(studyQuery);
        assertNotNull("must see his all studies ", study);


    }


}