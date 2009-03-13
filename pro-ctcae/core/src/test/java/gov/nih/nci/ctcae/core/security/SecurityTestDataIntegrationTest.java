package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;

import java.util.Collection;
import java.util.UUID;

/**
 * @author Vinay Kumar
 * @crated Mar 13, 2009
 */
public class SecurityTestDataIntegrationTest extends AbstractHibernateIntegrationTestCase {

    protected Study study1, study2;

    protected User leadCRA1, leadCRA2, siteCRA1, siteCRA2;


    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        study1 = createStudy("-10002");
        study2 = createStudy("-1003");

        study1.getStudySites().get(0).setOrganization(wake);
        study2.getStudySites().get(0).setOrganization(wake);

        study1.getLeadStudySite().setOrganization(wake);
        study2.getLeadStudySite().setOrganization(wake);

        study2 = studyRepository.save(study2);
        study1 = studyRepository.save(study1);


        ClinicalStaff clinicalStaffForLeadCRA2 = Fixture.createClinicalStaffWithOrganization("Paul", "Williams", "-123456", wake);
        clinicalStaffForLeadCRA2 = clinicalStaffRepository.save(clinicalStaffForLeadCRA2);
        commitAndStartNewTransaction();

        addLeadCRA(defaultOrganizationClinicalStaff, defaultStudy);
        leadCRA1 = defaultStudy.getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getUser();


        addLeadCRA(clinicalStaffForLeadCRA2.getOrganizationClinicalStaffs().get(0), study1);
        addLeadCRA(clinicalStaffForLeadCRA2.getOrganizationClinicalStaffs().get(0), study2);


        study2 = studyRepository.save(study2);
        study1 = studyRepository.save(study1);
        commitAndStartNewTransaction();

        leadCRA2 = study1.getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getUser();
        assertNotNull("must save another lead cra also", leadCRA2);

        ClinicalStaff clinicalStaffForSiteCRA1 = Fixture.createClinicalStaffWithOrganization("Bob", "Williams", "-12345", defaultOrganization);
        clinicalStaffForSiteCRA1 = clinicalStaffRepository.save(clinicalStaffForSiteCRA1);
        commitAndStartNewTransaction();

        addStudyOrganizationClinicalStaff(addSiteCRA(clinicalStaffForSiteCRA1.getOrganizationClinicalStaffs().get(0), defaultStudy));
        siteCRA1 = defaultStudy.getStudyOrganizationClinicalStaffByRole(Role.SITE_CRA).getOrganizationClinicalStaff().getClinicalStaff().getUser();


        ClinicalStaff clinicalStaffForSiteCRA2 = Fixture.createClinicalStaffWithOrganization("John", "Williams", "-12345", wake);
        clinicalStaffForSiteCRA2 = clinicalStaffRepository.save(clinicalStaffForSiteCRA2);
        addSiteCRA(clinicalStaffForSiteCRA2.getOrganizationClinicalStaffs().get(0), study1);
        addSiteCRA(clinicalStaffForSiteCRA2.getOrganizationClinicalStaffs().get(0), study2);
        study2 = studyRepository.save(study2);
        study1 = studyRepository.save(study1);
        commitAndStartNewTransaction();

        siteCRA2 = study1.getStudyOrganizationClinicalStaffByRole(Role.SITE_CRA).getOrganizationClinicalStaff().getClinicalStaff().getUser();
        assertNotNull("must save another user also", leadCRA2);


    }

    public void testInsertData() {

        login(leadCRA1);
        Participant participant = createParticipant("John", defaultStudy.getStudySites().get(0));
        CRF defaultCRF = createCRF(defaultStudy);

        login(leadCRA2);
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


        login(siteCRA2);


        assertTrue("must have atleast 2 results", jdbcTemplate.queryForInt("select count(*) from PARTICIPANTS") >= 2);
        participants = participantRepository.find(new ParticipantQuery());
        assertFalse("must find participants", participants.isEmpty());
        assertEquals("must see two participants only because these participants has assignments  on user's study", 2, participants.size());
        assertTrue("must see his own participants only", participants.contains(participant1));
        assertTrue("must see his own participants only ", participants.contains(participant2));


    }

    protected Participant createParticipant(String firstName, final StudySite studySite) {
        Participant participant = Fixture.createParticipant(firstName, "Doe", "12345");
        StudyParticipantAssignment studyParticipantAssignment = new StudyParticipantAssignment();
        studyParticipantAssignment.setStudySite(studySite);
        studyParticipantAssignment.setStudyParticipantIdentifier("-12345");
        participant.addStudyParticipantAssignment(studyParticipantAssignment);
        participant = participantRepository.save(participant);
        commitAndStartNewTransaction();
        return participant;
    }

    protected CRF createCRF(Study study) {

        Study savedStudy = studyRepository.findById(study.getId());
        assertEquals("must see his own study only", savedStudy, study);

        CRF crf = Fixture.createCrf();
        crf.setTitle("title" + UUID.randomUUID());
        crf.setStudy(savedStudy);
        crf = crfRepository.save(crf);

        assertNotNull("must save crf on his own study", crf);
        commitAndStartNewTransaction();
        return crf;
    }

}
