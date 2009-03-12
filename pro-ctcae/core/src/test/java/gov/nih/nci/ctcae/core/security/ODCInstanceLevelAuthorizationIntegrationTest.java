package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import org.springframework.security.AccessDeniedException;

import java.util.Collection;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class ODCInstanceLevelAuthorizationIntegrationTest extends AbstractInstanceLevelAuthorizationIntegrationTest {

    private User user, anotherUser;
    protected CRF defaultCRF;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        addODC(defaultOrganizationClinicalStaff, defaultStudy);
        defaultStudy = studyRepository.save(defaultStudy);
        user = defaultStudy.getOverallDataCoordinator().getOrganizationClinicalStaff().getClinicalStaff().getUser();


        study1.getDataCoordinatingCenter().setOrganization(wake);
        study2.getDataCoordinatingCenter().setOrganization(wake);


        addODC(anotherClinicalStaff.getOrganizationClinicalStaffs().get(0), study1);
        addODC(anotherClinicalStaff.getOrganizationClinicalStaffs().get(0), study2);


        study2 = studyRepository.save(study2);
        study1 = studyRepository.save(study1);
        commitAndStartNewTransaction();

        anotherUser = study1.getOverallDataCoordinator().getOrganizationClinicalStaff().getClinicalStaff().getUser();
        assertNotNull("must save another lead cra also", anotherUser);


    }


    public void testOrganizationInstanceSecurityForCreateClinicalStaff() throws Exception {
//        login(user);
//
//        List<Organization> organizations = (List<Organization>) organizationRepository.find(new OrganizationQuery());
//
//        assertFalse("must find organizations", organizations.isEmpty());
//        assertEquals("user should see his organization only.", 1, organizations.size());
//        login(anotherUser);
//        try {
//            organizationRepository.findById(wake.getId());
//            fail("user must not see other organizations.");
//        } catch (AccessDeniedException e) {
//
//        }

        fail("fix this test case");
    }

    public void testOrganizationClinicalStaffSecurityOnFind() throws Exception {

        login(user);

        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from ORGANIZATION_CLINICAL_STAFFS") >= 2);
        List<OrganizationClinicalStaff> organizationClinicalStaffs = organizationClinicalStaffRepository.findByStudyOrganizationId("%", defaultStudySite.getId());

        assertFalse("must find organizationClinicalStaffs", organizationClinicalStaffs.isEmpty());
        assertEquals("must see this because this organizationClinicalStaff is created on user's study", 2, organizationClinicalStaffs.size());
        for (OrganizationClinicalStaff organizationClinicalStaff : organizationClinicalStaffs) {
            assertEquals(defaultStudySite.getOrganization(), organizationClinicalStaff.getOrganization());
        }

        login(anotherUser);

        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from ORGANIZATION_CLINICAL_STAFFS") >= 2);
        organizationClinicalStaffs = organizationClinicalStaffRepository.findByStudyOrganizationId("%", defaultStudySite.getId());
        assertTrue("must not find any organizationClinicalStaffs because user can not access this study site", organizationClinicalStaffs.isEmpty());


    }


    public void testStudyInstanceSecurity() throws Exception {
        login(user);

        Collection<Study> studies = studyRepository.find(new StudyQuery());
        assertFalse("must find studies", studies.isEmpty());
        assertEquals("must see one study only because this user is lead CRA on that study only", 1, studies.size());
        assertEquals("must see his own study only", defaultStudy, studies.iterator().next());


    }


    public void testStudyInstanceSecurityForCreateStudy() throws Exception {

        //this user can see two studies
        login(anotherUser);
        try {
            defaultStudy.setAssignedIdentifier("test");
            studyRepository.save(defaultStudy);
            fail("user must not edit other studies");
        } catch (AccessDeniedException e) {

        }

    }

    public void testStudyInstanceSecurityForMultipleStudies() throws Exception {

        //this user can see two studies
        login(anotherUser);
        Collection<Study> studies = studyRepository.find(new StudyQuery());
        assertFalse("must find studies", studies.isEmpty());
        assertEquals("must see one study only because this user is lead CRA on that study only", 2, studies.size());
        assertTrue("must see his own study only", studies.contains(study1));
        assertTrue("must see his own study only", studies.contains(study2));


    }

    public void testStudyInstanceSecurityForStudyOrganizations() throws Exception {

        login(user);
        List<? extends StudyOrganization> studyOrganizations = (List<? extends StudyOrganization>) studyOrganizationRepository.find(new StudyOrganizationQuery());

        assertFalse("must find study organizations", studyOrganizations.isEmpty());
        assertEquals("must see all study organizations for his own study only (1  for data coordinating center)", 1, studyOrganizations.size());
        for (StudyOrganization studyOrganization : studyOrganizations) {
            assertEquals("must see study organizations of his own study only", studyOrganization.getStudy(), defaultStudy);
            assertEquals("must see study organizations of his own study only", studyOrganization, defaultStudy.getDataCoordinatingCenter());

        }


    }

    public void testStudyInstanceSecurityByUsingFindById() throws Exception {

        //this user can see two studies
        login(anotherUser);
        Study study = studyRepository.findById(study1.getId());
        assertEquals("must see his own study only", study, study1);

        try {
            studyRepository.findById(defaultStudy.getId());
            fail("must see his own study only");
        } catch (AccessDeniedException e) {

        }

    }

    public void testStudyInstanceSecurityByUsingFindSingle() throws Exception {

        //this user can see two studies
        login(anotherUser);
        StudyQuery studyQuery = new StudyQuery();
        studyQuery.filterByAssignedIdentifierExactMatch(study1.getAssignedIdentifier());
        Study study = studyRepository.findSingle(studyQuery);
        assertEquals("must see his own study only", study, study1);

        studyQuery = new StudyQuery();
        studyQuery.filterByAssignedIdentifierExactMatch(defaultStudy.getAssignedIdentifier());

        try {
            study = studyRepository.findSingle(studyQuery);
            fail("must see his own study only");
        } catch (AccessDeniedException e) {

        }


    }


}