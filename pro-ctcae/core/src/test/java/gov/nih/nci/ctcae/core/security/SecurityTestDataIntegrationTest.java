package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;

import java.util.Collection;
import java.util.List;
import java.text.ParseException;

/**
 * @author Vinay Kumar
 * @crated Mar 13, 2009
 */
public class SecurityTestDataIntegrationTest extends AbstractHibernateIntegrationTestCase {

    protected Study study2, study3;

    protected User leadCRA1, leadCRA2, siteCRA1, siteCRA2;

    protected CRF defaultCRF, crf1, crf2;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        study2 = createStudy("-10002");
        study3 = createStudy("-1003");

        study2.getStudySites().get(0).setOrganization(wake);
        study3.getStudySites().get(0).setOrganization(wake);

        study2.getLeadStudySite().setOrganization(wake);
        study3.getLeadStudySite().setOrganization(wake);

        defaultStudy.setShortTitle("Study 1");
        study2.setShortTitle("Study 2");
        study3.setShortTitle("Study 3");


        study3 = studyRepository.save(study3);
        study2 = studyRepository.save(study2);
        defaultStudy = studyRepository.save(defaultStudy);


        ClinicalStaff clinicalStaffForLeadCRA2 = Fixture.createClinicalStaffWithOrganization("Paul", "Williams", "-123456", wake);
        clinicalStaffForLeadCRA2 = clinicalStaffRepository.save(clinicalStaffForLeadCRA2);
        commitAndStartNewTransaction();

        addLeadCRA(defaultOrganizationClinicalStaff, defaultStudy);
        leadCRA1 = defaultStudy.getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getUser();


        addLeadCRA(clinicalStaffForLeadCRA2.getOrganizationClinicalStaffs().get(0), study2);
        addLeadCRA(clinicalStaffForLeadCRA2.getOrganizationClinicalStaffs().get(0), study3);


        study3 = studyRepository.save(study3);
        study2 = studyRepository.save(study2);
        commitAndStartNewTransaction();

        leadCRA2 = study2.getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getUser();
        assertNotNull("must save another lead cra also", leadCRA2);

        ClinicalStaff clinicalStaffForSiteCRA1 = Fixture.createClinicalStaffWithOrganization("Bob", "Williams", "-12345", defaultOrganization);
        clinicalStaffForSiteCRA1 = clinicalStaffRepository.save(clinicalStaffForSiteCRA1);
        ClinicalStaff clinicalStaffForSiteCRA4 = Fixture.createClinicalStaffWithOrganization("Matthew", "Hayden", "-12345", defaultOrganization);
        clinicalStaffForSiteCRA4 = clinicalStaffRepository.save(clinicalStaffForSiteCRA4);
        ClinicalStaff clinicalStaffForSiteCRA5 = Fixture.createClinicalStaffWithOrganization("Brian", "Lara", "-12345", defaultOrganization);
        clinicalStaffForSiteCRA5 = clinicalStaffRepository.save(clinicalStaffForSiteCRA5);
        commitAndStartNewTransaction();

        addStudyOrganizationClinicalStaff(addSiteCRA(clinicalStaffForSiteCRA1.getOrganizationClinicalStaffs().get(0), defaultStudy));
        siteCRA1 = defaultStudy.getStudyOrganizationClinicalStaffByRole(Role.SITE_CRA).getOrganizationClinicalStaff().getClinicalStaff().getUser();


        ClinicalStaff clinicalStaffForSiteCRA2 = Fixture.createClinicalStaffWithOrganization("John", "Williams", "-12345", wake);
        clinicalStaffForSiteCRA2 = clinicalStaffRepository.save(clinicalStaffForSiteCRA2);
        addSiteCRA(clinicalStaffForSiteCRA2.getOrganizationClinicalStaffs().get(0), study2);
        addSiteCRA(clinicalStaffForSiteCRA2.getOrganizationClinicalStaffs().get(0), study3);
        study3 = studyRepository.save(study3);
        study2 = studyRepository.save(study2);
        commitAndStartNewTransaction();

        siteCRA2 = study2.getStudyOrganizationClinicalStaffByRole(Role.SITE_CRA).getOrganizationClinicalStaff().getClinicalStaff().getUser();
        assertNotNull("must save another user also", leadCRA2);


    }

    public void testInsertData() throws ParseException {

        login(leadCRA1);
        Participant participant = createParticipant("Brian", defaultStudy.getStudySites().get(0));
        Participant participant3 = createParticipant("Tom", defaultStudy.getStudySites().get(0));
        Participant participant4 = createParticipant("Jake", defaultStudy.getStudySites().get(1));


        login(leadCRA2);
        Participant participant1 = createParticipant("Bruce", study2.getStudySites().get(0));
        Participant participant2 = createParticipant("Laura", study3.getStudySites().get(0));


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
        commitAndStartNewTransaction();


        login(leadCRA1);
        defaultCRF = createCRF(defaultStudy);
        defaultCRF.setTitle("Demo Form 1");

        login(leadCRA2);
        crf1 = createCRF(study2);
        crf1.setTitle("Demo Form 2");

        crf2 = createCRF(study2);
        crf2.setTitle("Demo Form 3");
        commitAndStartNewTransaction();

//        List<StudyParticipantCrfSchedule> pSchedules = participant.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules();
//        pSchedules.get(0).setStatus(CrfStatus.PASTDUE);
//        pSchedules.get(1).setStatus(CrfStatus.COMPLETED);
//        pSchedules.get(2).setStatus(CrfStatus.INPROGRESS);
//        pSchedules.get(3).setStatus(CrfStatus.SCHEDULED);
//
//        List<StudyParticipantCrfSchedule> pSchedules1 = participant3.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules();
//        pSchedules1.get(1).setStatus(CrfStatus.PASTDUE);
//        pSchedules1.get(3).setStatus(CrfStatus.COMPLETED);
//        pSchedules1.get(4).setStatus(CrfStatus.INPROGRESS);
//        pSchedules1.get(5).setStatus(CrfStatus.SCHEDULED);
//
//        List<StudyParticipantCrfSchedule> pSchedules2 = participant4.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules();
//        pSchedules2.get(0).setStatus(CrfStatus.PASTDUE);
//        pSchedules2.get(2).setStatus(CrfStatus.COMPLETED);
//        pSchedules2.get(4).setStatus(CrfStatus.INPROGRESS);
//        pSchedules2.get(6).setStatus(CrfStatus.SCHEDULED);
//
//        participantRepository.save(participant);
//        participantRepository.save(participant3);
//        participantRepository.save(participant4);
//
//        commitAndStartNewTransaction();

    }


}
