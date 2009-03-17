package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.CRFQuery;
import gov.nih.nci.ctcae.core.query.OrganizationQuery;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import org.springframework.security.AccessDeniedException;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class TreatingPhysicanInstanceLevelAuthorizationIntegrationTest extends AbstractInstanceLevelAuthorizationIntegrationTestCase {

    protected Participant participant;
    protected Participant participant1;
    protected Participant participant2;
    protected CRF defaultCRF;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();


        addTreatingPhysicanOrResearchNurse(defaultOrganizationClinicalStaff, defaultStudy.getStudySites().get(0), getRole());
        defaultStudy = studyRepository.save(defaultStudy);
        commitAndStartNewTransaction();

        user = defaultStudy.getStudyOrganizationClinicalStaffByRole(getRole()).getOrganizationClinicalStaff().getClinicalStaff().getUser();


        addTreatingPhysicanOrResearchNurse(anotherClinicalStaff.getOrganizationClinicalStaffs().get(0), study1.getStudySites().get(0), getRole());
        addTreatingPhysicanOrResearchNurse(anotherClinicalStaff.getOrganizationClinicalStaffs().get(0), study2.getStudySites().get(0), getRole());


        study2 = studyRepository.save(study2);
        study1 = studyRepository.save(study1);
        commitAndStartNewTransaction();

        anotherUser = study1.getStudyOrganizationClinicalStaffByRole(getRole()).getOrganizationClinicalStaff().getClinicalStaff().getUser();
        assertNotNull("must save another user also", anotherUser);


        participant = createParticipant("John", defaultStudy.getStudySites().get(0));

        participant1 = createParticipant("Bruce", study1.getStudySites().get(0));
        participant2 = createParticipant("Laura", study2.getStudySites().get(0));


    }

    protected Role getRole() {
        return Role.TREATING_PHYSICIAN;
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
        Participant participant = createParticipant("John", defaultStudy.getStudySites().get(0));

        login(user);

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