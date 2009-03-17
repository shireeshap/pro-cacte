package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.*;
import org.springframework.security.AccessDeniedException;

import java.util.Collection;
import java.util.List;

/**
 * @author Vinay Kumar
 * @crated Mar 3, 2009
 */
public class ODCInstanceLevelAuthorizationIntegrationTest extends AbstractInstanceLevelAuthorizationIntegrationTestCase {

    private User user, anotherUser;
    protected CRF defaultCRF;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        ClinicalStaff clinicalStaff = Fixture.createClinicalStaffWithOrganization("Paula", "Williams", "-1234567", defaultOrganization);
        clinicalStaff = clinicalStaffRepository.save(clinicalStaff);

        addODC(clinicalStaff.getOrganizationClinicalStaffs().get(0), defaultStudy);
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


    public void testClinicalStaffSecurityOnFindMultiple() throws Exception {

        ClinicalStaff clinicalStaff1 = Fixture.createClinicalStaffWithOrganization("David", "Williams", "-123456", wake);
        clinicalStaff1 = clinicalStaffRepository.save(clinicalStaff1);


        ClinicalStaff clinicalStaff2 = Fixture.createClinicalStaffWithOrganization("Bruce", "Williams", "-123456", defaultOrganization);
        clinicalStaff2 = clinicalStaffRepository.save(clinicalStaff2);

        commitAndStartNewTransaction();
        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from CLINICAL_STAFFS") >= 2);

        login(user);
        Collection<ClinicalStaff> clinicalStaffs = clinicalStaffRepository.find(new ClinicalStaffQuery());
        assertFalse("must find clinical staff", clinicalStaffs.isEmpty());
        assertEquals("must see two clinical staff only because these clinicalstaff are on on user's organization", 3, clinicalStaffs.size());
        assertTrue("must see his own clinical staff only ", clinicalStaffs.contains(clinicalStaff2));

        login(anotherUser);

        clinicalStaffs = clinicalStaffRepository.find(new ClinicalStaffQuery());
        assertFalse("must find clinical staff", clinicalStaffs.isEmpty());
        assertEquals("must see two clinical staff only because these clinicalstaff are on on user's organization", 2, clinicalStaffs.size());
        assertTrue("must see his own clinical staff only ", clinicalStaffs.contains(clinicalStaff1));
        assertTrue("must see his own clinical staff only ", clinicalStaffs.contains(anotherClinicalStaff));


    }

    public void testClinicalStaffSecurityOnFindById() throws Exception {

        ClinicalStaff clinicalStaff2 = Fixture.createClinicalStaffWithOrganization("Bruce", "Williams", "-123456", defaultOrganization);
        clinicalStaff2 = clinicalStaffRepository.save(clinicalStaff2);

        commitAndStartNewTransaction();
        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from CLINICAL_STAFFS") >= 2);

        login(user);

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

        ClinicalStaff clinicalStaff2 = Fixture.createClinicalStaffWithOrganization("Bruce", "Williams", "-123456", defaultOrganization);
        clinicalStaff2 = clinicalStaffRepository.save(clinicalStaff2);

        commitAndStartNewTransaction();
        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from CLINICAL_STAFFS") >= 2);

        login(user);
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

        ClinicalStaff clinicalStaff2 = Fixture.createClinicalStaffWithOrganization("Bruce", "Williams", "-123456", defaultOrganization);
        OrganizationClinicalStaff staff = new OrganizationClinicalStaff();
        staff.setOrganization(wake);
        clinicalStaff2.addOrganizationClinicalStaff(staff);
        clinicalStaff2 = clinicalStaffRepository.save(clinicalStaff2);

        commitAndStartNewTransaction();

        login(user);
        ClinicalStaff savedClinicalStaff = clinicalStaffRepository.findById(clinicalStaff2.getId());
        assertEquals("must see this clinical staff because this participant has organization clinical staff  on user's organization", savedClinicalStaff, clinicalStaff2);

        login(anotherUser);
        assertNotNull("this user can also seet his clinical staff because one of the organization clinical staff belongs to user's organizations",
                clinicalStaffRepository.findById(clinicalStaff2.getId()));


    }


    public void testParticipantSecurityOnFindMultiple() throws Exception {
        Participant participant = createParticipant("John", defaultStudy.getStudySites().get(0));

        Participant participant1 = createParticipant("Bruce", study1.getStudySites().get(0));
        Participant participant2 = createParticipant("Laura", study2.getStudySites().get(0));


        login(anotherUser);

        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from PARTICIPANTS") >= 2);
        Collection<Participant> participants = participantRepository.find(new ParticipantQuery());
        assertFalse("must find crfs", participants.isEmpty());
        assertEquals("must see two participants only because these participants has assignments  on user's study", 2, participants.size());
        assertTrue("must see his own participants only", participants.contains(participant1));
        assertTrue("must see his own participants only ", participants.contains(participant2));


    }

    public void testParticipantSecurityOnFindById() throws Exception {
        Participant participant = createParticipant("John", defaultStudy.getStudySites().get(0));
        Participant participant1 = createParticipant("Tom", defaultStudy.getStudySites().get(0));
        Participant participant2 = createParticipant("Jake", defaultStudy.getStudySites().get(0));

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

    public void testParticipantSecurityOnFindSingle() throws Exception {
        Participant participant = createParticipant("John", defaultStudy.getStudySites().get(0));
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
        assertEquals("must see only overall data coordinating center study organizations for his own study site only", 1, studyOrganizations.size());
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

        study = studyRepository.findSingle(studyQuery);
        assertNull("must see his own study only", study);


    }

}