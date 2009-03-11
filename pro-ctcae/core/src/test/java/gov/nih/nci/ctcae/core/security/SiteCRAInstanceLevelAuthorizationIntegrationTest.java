package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.query.StudyOrganizationQuery;
import gov.nih.nci.ctcae.core.query.StudyQuery;
import org.springframework.security.AccessDeniedException;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class SiteCRAInstanceLevelAuthorizationIntegrationTest extends AbstractInstanceLevelAuthorizationIntegrationTest {

    protected CRF defaultCRF;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


        addStudyOrganizationClinicalStaff(addSiteCRA(defaultOrganizationClinicalStaff, defaultStudy));
        user = defaultStudy.getStudyOrganizationClinicalStaffByRole(Role.SITE_CRA).getOrganizationClinicalStaff().getClinicalStaff().getUser();

        addSiteCRA(anotherClinicalStaff.getOrganizationClinicalStaffs().get(0), study1);
        addSiteCRA(anotherClinicalStaff.getOrganizationClinicalStaffs().get(0), study2);
        study2 = studyRepository.save(study2);
        study1 = studyRepository.save(study1);
        commitAndStartNewTransaction();

        anotherUser = study1.getStudyOrganizationClinicalStaffByRole(Role.SITE_CRA).getOrganizationClinicalStaff().getClinicalStaff().getUser();
        assertNotNull("must save another user also", anotherUser);


    }


//    public void testOrganizationInstanceSecurityForCreateClinicalStaff() throws Exception {
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
//
//    }

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

        try {
            study = studyRepository.findSingle(studyQuery);
            fail("must see his own study only");
        } catch (AccessDeniedException e) {

        }


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