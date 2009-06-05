package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.*;
import org.springframework.security.AccessDeniedException;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class SiteCRAInstanceLevelAuthorizationIntegrationTest extends AbstractInstanceLevelAuthorizationIntegrationTestCase {

    protected CRF defaultCRF;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


        addStudyOrganizationClinicalStaff(addSiteCRAOrSitePI(defaultOrganizationClinicalStaff, defaultStudy, getRole()));
        user = defaultStudy.getStudyOrganizationClinicalStaffByRole(getRole()).getOrganizationClinicalStaff().getClinicalStaff().getUser();

        addSiteCRAOrSitePI(anotherClinicalStaff.getOrganizationClinicalStaffs().get(0), study1, getRole());
        addSiteCRAOrSitePI(anotherClinicalStaff.getOrganizationClinicalStaffs().get(0), study2, getRole());
        study2 = studyRepository.save(study2);
        study1 = studyRepository.save(study1);
        commitAndStartNewTransaction();

        anotherUser = study1.getStudyOrganizationClinicalStaffByRole(getRole()).getOrganizationClinicalStaff().getClinicalStaff().getUser();
        assertNotNull("must save another user also", anotherUser);


    }

    protected Role getRole() {
        return Role.SITE_CRA;
    }


    public void testOrganizationInstanceSecurity() throws Exception {
        login(user);

        OrganizationQuery organizationQuery = new OrganizationQuery();

        List<Organization> organizations = (List<Organization>) organizationRepository.find(organizationQuery);

        assertFalse("must find organizations", organizations.isEmpty());
        assertEquals("user should see his organization only.", 1, organizations.size());
        assertEquals("user should see his organization only.", defaultOrganization, organizationRepository.findById(defaultOrganization.getId()));


        organizationQuery = new OrganizationQuery();
        organizationQuery.filterByNciCodeExactMatch(defaultOrganization.getNciInstituteCode());
        assertEquals("user should see his organization only.", defaultOrganization, organizationRepository.findSingle(organizationQuery));

        login(anotherUser);
        try {
            organizationRepository.findById(defaultOrganization.getId());
            fail("user must not see other organizations.");
        } catch (AccessDeniedException e) {

        }

    }

    public void testOrganizationClinicalStaffSecurityOnFind() throws Exception {

        login(user);

        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from ORGANIZATION_CLINICAL_STAFFS") >= 2);
        List<OrganizationClinicalStaff> organizationClinicalStaffs = organizationClinicalStaffRepository.findByStudyOrganizationId("%", defaultStudySite.getId());

        assertFalse("must find organizationClinicalStaffs", organizationClinicalStaffs.isEmpty());
        assertEquals("must see this because this organizationClinicalStaff is created on user's study", 1, organizationClinicalStaffs.size());
        for (OrganizationClinicalStaff organizationClinicalStaff : organizationClinicalStaffs) {
            assertEquals(defaultStudySite.getOrganization(), organizationClinicalStaff.getOrganization());
        }

        login(anotherUser);

        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from ORGANIZATION_CLINICAL_STAFFS") >= 2);
        try {
            organizationClinicalStaffs = organizationClinicalStaffRepository.findByStudyOrganizationId("%", defaultStudySite.getId());
            fail("must not find any organizationClinicalStaffs because user can not access this study site");

        } catch (AccessDeniedException e) {

        }


    }

    public void testClinicalStaffSecurityOnCreateAndEdit() throws Exception {

        login(user);

        ClinicalStaff clinicalStaff2 = Fixture.createClinicalStaffWithOrganization("Bruce", "Williams", "-123456", defaultOrganization);
        clinicalStaff2 = clinicalStaffRepository.save(clinicalStaff2);

        commitAndStartNewTransaction();

        //now edit this clinical staff ;
        login(anotherUser);
        try {
            clinicalStaffRepository.save(clinicalStaff2);
            fail("must not save clinical staff for un-authorized user");
        } catch (AccessDeniedException e) {

        }

    }


    public void testClinicalStaffSecurityOnFindMultiple() throws Exception {

        login(anotherUser);
        ClinicalStaff clinicalStaff1 = Fixture.createClinicalStaffWithOrganization("David", "Williams", "-123456", wake);
        clinicalStaff1 = clinicalStaffRepository.save(clinicalStaff1);

        login(user);

        ClinicalStaff clinicalStaff2 = Fixture.createClinicalStaffWithOrganization("Bruce", "Williams", "-123456", defaultOrganization);
        clinicalStaff2 = clinicalStaffRepository.save(clinicalStaff2);

        commitAndStartNewTransaction();
        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from CLINICAL_STAFFS") >= 2);


        Collection<ClinicalStaff> clinicalStaffs = clinicalStaffRepository.find(new ClinicalStaffQuery());
        assertFalse("must find clinical staff", clinicalStaffs.isEmpty());
        assertEquals("must see two clinical staff only because these clinicalstaff are on on user's organization", 2, clinicalStaffs.size());
        assertTrue("must see his own clinical staff only ", clinicalStaffs.contains(clinicalStaff2));

        login(anotherUser);

        clinicalStaffs = clinicalStaffRepository.find(new ClinicalStaffQuery());
        assertFalse("must find clinical staff", clinicalStaffs.isEmpty());
        assertEquals("must see two clinical staff only because these clinicalstaff are on on user's organization", 2, clinicalStaffs.size());
        assertTrue("must see his own clinical staff only ", clinicalStaffs.contains(clinicalStaff1));
        assertTrue("must see his own clinical staff only ", clinicalStaffs.contains(anotherClinicalStaff));


    }

    public void testClinicalStaffSecurityOnFindById() throws Exception {
        login(user);

        ClinicalStaff clinicalStaff2 = Fixture.createClinicalStaffWithOrganization("Bruce", "Williams", "-123456", defaultOrganization);
        clinicalStaff2 = clinicalStaffRepository.save(clinicalStaff2);

        commitAndStartNewTransaction();
        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from CLINICAL_STAFFS") >= 2);


        ClinicalStaff savedClinicalStaff = clinicalStaffRepository.findById(clinicalStaff2.getId());
        assertEquals("must see this clinical staff because this participant has organization clinical staff  on user's organization", savedClinicalStaff, clinicalStaff2);

        login(anotherUser);
        try {
            clinicalStaffRepository.findById(clinicalStaff2.getId());
            fail("must not find  participant for un-authorized studies");
        } catch (AccessDeniedException e) {

        }


    }

    public void testClinicalStafffSecurityOnFindSingle() throws Exception {
        login(user);

        ClinicalStaff clinicalStaff2 = Fixture.createClinicalStaffWithOrganization("Bruce", "Williams", "-123456", defaultOrganization);
        clinicalStaff2 = clinicalStaffRepository.save(clinicalStaff2);

        commitAndStartNewTransaction();
        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from CLINICAL_STAFFS") >= 2);
        ClinicalStaffQuery clinicalStaffQuery = new ClinicalStaffQuery();
        clinicalStaffQuery.filterByUserId(user.getId());
        ClinicalStaff savedClinicalStaff = clinicalStaffRepository.findSingle(clinicalStaffQuery);
        assertNotNull("must see this clinial staff because this clinical staff is on user's organization", savedClinicalStaff);

        login(anotherUser);
        try {
            clinicalStaffRepository.findSingle(clinicalStaffQuery);
            fail("must not find  participant for un-authorized studies");
        } catch (AccessDeniedException e) {

        }


    }


    public void testClinicalStaffHavingMultipleOrganizationClinicalStaffSecurityOnFindById() throws Exception {
        login(user);

        ClinicalStaff clinicalStaff2 = Fixture.createClinicalStaffWithOrganization("Bruce", "Williams", "-123456", defaultOrganization);
        OrganizationClinicalStaff staff = new OrganizationClinicalStaff();
        staff.setOrganization(wake);
        clinicalStaff2.addOrganizationClinicalStaff(staff);
        clinicalStaff2 = clinicalStaffRepository.save(clinicalStaff2);

        commitAndStartNewTransaction();


        ClinicalStaff savedClinicalStaff = clinicalStaffRepository.findById(clinicalStaff2.getId());
        assertEquals("must see this clinical staff because this participant has organization clinical staff  on user's organization", savedClinicalStaff, clinicalStaff2);

        login(anotherUser);
        assertNotNull("this user can also seet his clinical staff because one of the organization clinical staff belongs to user's organizations",
                clinicalStaffRepository.findById(clinicalStaff2.getId()));


    }


    public void testParticipantSecurityOnFindMultiple() throws Exception {
        login(user);
        Participant participant = createParticipant("John", defaultStudy.getStudySites().get(0));

        login(anotherUser);
        Participant participant1 = createParticipant("Bruce", study1.getStudySites().get(0));
        Participant participant2 = createParticipant("Laura", study2.getStudySites().get(0));


        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from PARTICIPANTS") >= 2);
        Collection<Participant> participants = participantRepository.find(new ParticipantQuery());
        assertFalse("must find participants", participants.isEmpty());
        assertEquals("must see two participants only because these participants has assignments  on user's study", 2, participants.size());
        assertTrue("must see his own participants only", participants.contains(participant1));
        assertTrue("must see his own participants only ", participants.contains(participant2));


    }


    public void testInstanceSecurityForFindingStudyParticipantAssignment() throws Exception {


        login(user);
        Participant participant = createParticipant("John", defaultStudy.getStudySites().get(0));

        login(anotherUser);
        Participant participant1 = createParticipant("Bruce", study1.getStudySites().get(0));
        Participant participant2 = createParticipant("Laura", study2.getStudySites().get(0));

        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from STUDY_PARTICIPANT_ASSIGNMENTS") >= 2);

        StudyParticipantAssignmentQuery query = new StudyParticipantAssignmentQuery();


        Collection<StudyParticipantAssignment> studyParticipantAssignments = studyParticipantAssignmentRepository.find(query);

        assertFalse("must find assigment", studyParticipantAssignments.isEmpty());
        assertEquals("must see two assignments only because these participants has assignments  on user's study", 2, studyParticipantAssignments.size());
        assertTrue("must see his own assignment only", studyParticipantAssignments.contains(participant1.getStudyParticipantAssignments().get(0)));
        assertTrue("must see his own assignment only", studyParticipantAssignments.contains(participant2.getStudyParticipantAssignments().get(0)));

        login(user);

        studyParticipantAssignments = studyParticipantAssignmentRepository.find(query);
        assertEquals("must not find only 1 assignment because only one of  them are created on user'study", 1, studyParticipantAssignments.size());
        assertTrue("must see his own assignment only", studyParticipantAssignments.contains(participant.getStudyParticipantAssignments().get(0)));

    }

    public void testParticipantSecurityOnFindById() throws Exception {
        login(user);
        Participant participant = createParticipant("John", defaultStudy.getStudySites().get(0));


        Participant savedParticipant = participantRepository.findById(participant.getId());
        assertEquals("must see this participant because this participant has assigment on user's study", savedParticipant, participant);

        login(anotherUser);
        try {
            participantRepository.findById(participant.getId());
            fail("must not find  participant for un-authorized studies");
        } catch (AccessDeniedException e) {

        }


    }

    public void testParticipantSecurityOnFindSingle() throws Exception {
        login(user);
        Participant participant = createParticipant("John", defaultStudy.getStudySites().get(0));

        ParticipantQuery participantQuery = new ParticipantQuery();
        participantQuery.filterByParticipantFirstName(participant.getFirstName());
        Participant savedParticipant = participantRepository.findSingle(participantQuery);
        assertEquals("must see this participant because this participant has assigment on user's study", savedParticipant, participant);

        login(anotherUser);
        try {
            participantQuery = new ParticipantQuery();
            participantQuery.filterByParticipantFirstName(participant.getFirstName());
            participantRepository.findSingle(participantQuery);
            fail("must not find  participant for un-authorized studies");
        } catch (AccessDeniedException e) {

        }


    }


    public void testParticipantSecurityOnCreateAndEdit() throws Exception {

        login(user);
        Participant participant = createParticipant("John", defaultStudy.getStudySites().get(0));


        assertEquals("must save participant", participant, participant);

        //now edit this participant and add assignment for some other study site;
        participant.getStudyParticipantAssignments().get(0).setStudySite(study1.getStudySites().get(0));
        try {
            participantRepository.save(participant);
            fail("must not save participant for un-authorized study site");
        } catch (AccessDeniedException e) {

        }

    }

    public void testCRFSecurityOnFind() throws Exception {
        createCRF(study1);
        createCRF(study1);

        defaultCRF = createCRF(defaultStudy);

        login(user);

        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from crfs") >= 2);
        Collection<CRF> crfs = crfRepository.find(new CRFQuery());
        assertFalse("must find crfs", crfs.isEmpty());
        assertEquals("must see one crf only because this crf is created on user's study", 1, crfs.size());
        assertEquals("must see one crf only because this crf is created on user's study", defaultCRF, crfs.iterator().next());


    }

    public void testCRFSecurityOnFindMultiple() throws Exception {
        defaultCRF = createCRF(defaultStudy);

        CRF crf1 = createCRF(study1);
        CRF crf2 = createCRF(study1);

        login(anotherUser);

        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from crfs") >= 2);
        Collection<CRF> crfs = crfRepository.find(new CRFQuery());
        assertFalse("must find crfs", crfs.isEmpty());
        assertEquals("must see two crfs only because these crfs are created on user's study", 2, crfs.size());
        assertTrue("must see his own crf only", crfs.contains(crf1));
        assertTrue("must see his own crf only ", crfs.contains(crf2));


    }

    public void testCRFSecurityOnFindById() throws Exception {
        defaultCRF = createCRF(study1);
        login(anotherUser);

        CRF crf = crfRepository.findById(defaultCRF.getId());
        assertEquals("must see this crf  because this crf is created on user's study", defaultCRF, crf);

        login(user);
        try {
            crfRepository.findById(defaultCRF.getId());
            fail("must not see crfs for other studies");
        } catch (AccessDeniedException e) {
        }
    }

    public void testCRFSecurityOnFindSingle() throws Exception {
        defaultCRF = createCRF(study1);
        login(anotherUser);

        CRFQuery query = new CRFQuery();
        query.filterByTitleExactMatch(defaultCRF.getTitle());
        CRF crf = crfRepository.findSingle(query);
        assertEquals("must see this crf  because this crf is created on user's study", defaultCRF, crf);

        login(user);
        try {
            query = new CRFQuery();
            query.filterByTitleExactMatch(defaultCRF.getTitle());

            crfRepository.findSingle(query);
            fail("must not see crfs for other studies");
        } catch (AccessDeniedException e) {
        }
    }


    public void testCreateCRFScheduleInstanceSecurity() throws Exception {
        CRF crf = createCRF(defaultStudy);
        crfRepository.updateStatusToReleased(crf);
        login(user);
        Participant participant = createParticipant("John", defaultStudy.getStudySites().get(0));


        assertEquals("must save participant", participant, participant);

        StudyParticipantAssignment studyParticipantAssignment = participant.getStudyParticipantAssignments().get(0);
        StudyParticipantCrf studyParticipantCrf = new StudyParticipantCrf();
        studyParticipantCrf.setCrf(crf);
        studyParticipantCrf.setStartDate(new Date());

        studyParticipantAssignment.addStudyParticipantCrf(studyParticipantCrf);
        studyParticipantAssignment = studyParticipantAssignmentRepository.save(studyParticipantAssignment);

        login(anotherUser);

        try {
            studyParticipantAssignmentRepository.save(studyParticipantAssignment);
            fail("must not save participant for un-authorized user");
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

        study = studyRepository.findSingle(studyQuery);
        assertNull("must see his own study only", study);


    }

    public void testStudyOrganizationsInstanceSecurity() throws Exception {

        login(anotherUser);
        Collection<StudyOrganization> studyOrganizations = studyOrganizationRepository.find(new StudyOrganizationQuery());

        assertFalse("must find study organizations", studyOrganizations.isEmpty());
        assertEquals("must see how own study organizations only (or only 1 study site for each study)", 2, studyOrganizations.size());
        for (StudyOrganization studyOrganization : studyOrganizations) {
            assertEquals("must see how own study organizations only (or only 1 study site for each study)", wake, studyOrganization.getOrganization());
        }

    }


}