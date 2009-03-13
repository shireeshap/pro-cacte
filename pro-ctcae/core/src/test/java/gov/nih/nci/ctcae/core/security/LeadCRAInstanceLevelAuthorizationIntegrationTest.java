package gov.nih.nci.ctcae.core.security;

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
public class LeadCRAInstanceLevelAuthorizationIntegrationTest extends AbstractInstanceLevelAuthorizationIntegrationTest {

    private User user, anotherUser;
    protected CRF defaultCRF;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        addLeadCRA(defaultOrganizationClinicalStaff, defaultStudy);
        user = defaultStudy.getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getUser();

        study1.getLeadStudySite().setOrganization(wake);
        study2.getLeadStudySite().setOrganization(wake);

        addLeadCRA(anotherClinicalStaff.getOrganizationClinicalStaffs().get(0), study1);
        addLeadCRA(anotherClinicalStaff.getOrganizationClinicalStaffs().get(0), study2);


        study2 = studyRepository.save(study2);
        study1 = studyRepository.save(study1);
        commitAndStartNewTransaction();

        anotherUser = study1.getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getUser();
        assertNotNull("must save another lead cra also", anotherUser);


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
        assertEquals("must see this because this organizationClinicalStaff is created on user's study", 2, organizationClinicalStaffs.size());
        for (OrganizationClinicalStaff organizationClinicalStaff : organizationClinicalStaffs) {
            assertEquals(defaultStudySite.getOrganization(), organizationClinicalStaff.getOrganization());
        }

        login(anotherUser);

        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from ORGANIZATION_CLINICAL_STAFFS") >= 2);
        organizationClinicalStaffs = organizationClinicalStaffRepository.findByStudyOrganizationId("%", defaultStudySite.getId());
        assertTrue("must not find any organizationClinicalStaffs because user can not access this study site", organizationClinicalStaffs.isEmpty());


    }

    public void testParticipantSecurityOnFindMultiple() throws Exception {
        login(user);
        Participant participant = createParticipant("John", defaultStudy.getStudySites().get(0));
        defaultCRF = createCRF(defaultStudy);

        login(anotherUser);
        Participant participant1 = createParticipant("Bruce", study1.getStudySites().get(0));
        Participant participant2 = createParticipant("Laura", study2.getStudySites().get(0));


        CRF crf1 = createCRF(study1);
        CRF crf2 = createCRF(study1);


        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from PARTICIPANTS") >= 2);
        Collection<Participant> participants = participantRepository.find(new ParticipantQuery());
        assertFalse("must find crfs", participants.isEmpty());
        assertEquals("must see two participants only because these participants has assignments  on user's study", 2, participants.size());
        assertTrue("must see his own participants only", participants.contains(participant1));
        assertTrue("must see his own participants only ", participants.contains(participant2));


    }

    public void testParticipantSecurityOnFindById() throws Exception {
        login(user);
        Participant participant = createParticipant("John", defaultStudy.getStudySites().get(0));
        Participant participant1 = createParticipant("Tom", defaultStudy.getStudySites().get(0));
        Participant participant2 = createParticipant("Jake", defaultStudy.getStudySites().get(0));


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

    public void testCreateScheduleSecurity() throws Exception {


        login(user);
        Participant participant = createParticipant("John", defaultStudy.getStudySites().get(0));

        CRF crf = createCRF(defaultStudy);

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

    public void testInstanceSecurityForFindingStudyParticipantAssignment() throws Exception {


        login(user);
        Participant participant = createParticipant("John", defaultStudy.getStudySites().get(0));
        defaultCRF = createCRF(defaultStudy);

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


    public void testStudyInstanceSecurity() throws Exception {
        login(user);

        Collection<Study> studies = studyRepository.find(new StudyQuery());
        assertFalse("must find studies", studies.isEmpty());
        assertEquals("must see one study only because this user is lead CRA on that study only", 1, studies.size());
        assertEquals("must see his own study only", defaultStudy, studies.iterator().next());


    }

    public void testCRFSecurityOnFind() throws Exception {
        login(anotherUser);
        createCRF(study1);
        createCRF(study1);

        login(user);
        defaultCRF = createCRF(defaultStudy);

        login(user);

        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from crfs") >= 2);
        Collection<CRF> crfs = crfRepository.find(new CRFQuery());
        assertFalse("must find crfs", crfs.isEmpty());
        assertEquals("must see one crf only because this crf is created on user's study", 1, crfs.size());
        assertEquals("must see one crf only because this crf is created on user's study", defaultCRF, crfs.iterator().next());


    }

    public void testCRFSecurityOnFindMultiple() throws Exception {
        login(user);
        defaultCRF = createCRF(defaultStudy);

        login(anotherUser);
        CRF crf1 = createCRF(study1);
        CRF crf2 = createCRF(study1);


        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from crfs") >= 2);
        Collection<CRF> crfs = crfRepository.find(new CRFQuery());
        assertFalse("must find crfs", crfs.isEmpty());
        assertEquals("must see two crfs only because these crfs are created on user's study", 2, crfs.size());
        assertTrue("must see his own crf only", crfs.contains(crf1));
        assertTrue("must see his own crf only ", crfs.contains(crf2));


    }

    public void testCRFSecurityOnFindById() throws Exception {
        login(anotherUser);
        defaultCRF = createCRF(study1);

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
        login(anotherUser);
        defaultCRF = createCRF(study1);

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


    public void testCRFSecurityOnCreateAndEdit() throws Exception {

        login(user);
        CRF crf = createCRF(defaultStudy);


        crf.setStudy(study1);
        try {
            crf = crfRepository.save(crf);
            fail("must edit CRF for his own studies only");
        } catch (AccessDeniedException e) {

        }

        try {
            createCRF(study1);
            fail("must save CRF for his own studies only");
        } catch (AccessDeniedException e) {

        }


    }

    public void testCRFSecurityOnVersion() throws Exception {

        login(user);
        CRF crf = createCRF(defaultStudy);
        crf = crfRepository.versionCrf(crf);
        crf.setStudy(study1);
        try {
            crfRepository.versionCrf(crf);
            fail("must not version CRF on other studies");
        } catch (AccessDeniedException e) {

        }


    }

//    public void testCRFSecurityOnCopy() throws Exception {
//
//        login(user);
//        CRF crf = createCRF(defaultStudy);
//        crf = crfRepository.copy(crf);
//        crf.setStudy(study2);
//        try {
//            crfRepository.copy(crf);
//            fail("must not copy CRF on other studies");
//        } catch (AccessDeniedException e) {
//
//        }
//
//
//    }

    public void testCRFSecurityOnRelease() throws Exception {

        login(user);
        CRF crf = createCRF(defaultStudy);
        crfRepository.updateStatusToReleased(crf);
        crf = crfRepository.findById(crf.getId());
        crf.setStudy(study1);
        try {
            crfRepository.updateStatusToReleased(crf);
            fail("must not update CRF on other studies");
        } catch (AccessDeniedException e) {

        }


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
        assertEquals("must see all study organizations for his own study only (4 +2 study site)", 6, studyOrganizations.size());
        for (StudyOrganization studyOrganization : studyOrganizations) {
            assertEquals("must see study organizations of his own study only", studyOrganization.getStudy(), defaultStudy);
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