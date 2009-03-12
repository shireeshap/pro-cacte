package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import org.springframework.security.AccessDeniedException;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class TreatingPhysicanInstanceLevelAuthorizationIntegrationTest extends AbstractInstanceLevelAuthorizationIntegrationTest {

    private User user, anotherUser;
    protected CRF defaultCRF;
    protected Participant participant;
    protected Participant participant1;
    protected Participant participant2;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


        addTreatingPhysicanOrResearchNurse(defaultOrganizationClinicalStaff, defaultStudy.getStudySites().get(0), Role.TREATING_PHYSICIAN);
        defaultStudy = studyRepository.save(defaultStudy);
        commitAndStartNewTransaction();

        user = defaultStudy.getStudyOrganizationClinicalStaffByRole(Role.TREATING_PHYSICIAN).getOrganizationClinicalStaff().getClinicalStaff().getUser();


        addTreatingPhysicanOrResearchNurse(anotherClinicalStaff.getOrganizationClinicalStaffs().get(0), study1.getStudySites().get(0), Role.TREATING_PHYSICIAN);
        addTreatingPhysicanOrResearchNurse(anotherClinicalStaff.getOrganizationClinicalStaffs().get(0), study2.getStudySites().get(0), Role.TREATING_PHYSICIAN);


        study2 = studyRepository.save(study2);
        study1 = studyRepository.save(study1);
        commitAndStartNewTransaction();

        anotherUser = study1.getStudyOrganizationClinicalStaffByRole(Role.TREATING_PHYSICIAN).getOrganizationClinicalStaff().getClinicalStaff().getUser();
        assertNotNull("must save another user also", anotherUser);


        participant = createParticipant("John", defaultStudy.getStudySites().get(0));

        participant1 = createParticipant("Bruce", study1.getStudySites().get(0));
        participant2 = createParticipant("Laura", study2.getStudySites().get(0));


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

    public void testParticipantSecurityOnFindSingle() throws Exception {

        login(user);

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

    public void testParticipantSecurityOnFindMultiple() throws Exception {


        login(anotherUser);

        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from PARTICIPANTS") >= 2);
        Collection<Participant> participants = participantRepository.find(new ParticipantQuery());
        assertFalse("must find crfs", participants.isEmpty());
        assertEquals("must see two participants only because these participants has assignments  on user's study", 2, participants.size());
        assertTrue("must see his own participants only", participants.contains(participant1));
        assertTrue("must see his own participants only ", participants.contains(participant2));


    }

    public void testParticipantSecurityOnFindById() throws Exception {

        login(user);

        Participant savedParticipant = participantRepository.findById(participant.getId());
        assertEquals("must see this participant because this participant has assigment on user's study", savedParticipant, participant);

        login(anotherUser);
        try {
            participantRepository.findById(participant.getId());
            fail("must not find  participant for un-authorized studies");
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


}