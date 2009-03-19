package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;

import java.text.ParseException;
import java.util.Collection;

/**
 * @author Vinay Kumar
 * @crated Mar 13, 2009
 */
public class SecurityTestDataIntegrationTest extends AbstractHibernateIntegrationTestCase {

    protected Study study2, study3, study5, study6;

    protected User leadCRA1, leadCRA2, siteCRA1, siteCRA2;

    protected CRF defaultCRF, crf1, crf2;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        study2 = createStudy("-10002");
        study3 = createStudy("-1003");
        study5 = createStudy("-104");
        study6 = createStudy("-105");

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

        addPIOrLeadCRA(defaultOrganizationClinicalStaff, defaultStudy, Role.LEAD_CRA);
        leadCRA1 = defaultStudy.getLeadCRA().getOrganizationClinicalStaff().getClinicalStaff().getUser();


        addPIOrLeadCRA(clinicalStaffForLeadCRA2.getOrganizationClinicalStaffs().get(0), study2, Role.LEAD_CRA);
        addPIOrLeadCRA(clinicalStaffForLeadCRA2.getOrganizationClinicalStaffs().get(0), study3, Role.LEAD_CRA);


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
        ClinicalStaff clinicalStaffForODC = Fixture.createClinicalStaffWithOrganization("Hurley", "Dyer", "-12345", defaultOrganization);
        clinicalStaffForODC = clinicalStaffRepository.save(clinicalStaffForODC);
        addODC(clinicalStaffForODC.getOrganizationClinicalStaffs().get(0), defaultStudy);
        commitAndStartNewTransaction();

        addStudyOrganizationClinicalStaff(addSiteCRAOrSitePI(clinicalStaffForSiteCRA1.getOrganizationClinicalStaffs().get(0), defaultStudy, Role.SITE_CRA));
        siteCRA1 = defaultStudy.getStudyOrganizationClinicalStaffByRole(Role.SITE_CRA).getOrganizationClinicalStaff().getClinicalStaff().getUser();


        ClinicalStaff clinicalStaffForSiteCRA2 = Fixture.createClinicalStaffWithOrganization("John", "Williams", "-12345", wake);
        clinicalStaffForSiteCRA2 = clinicalStaffRepository.save(clinicalStaffForSiteCRA2);
        addSiteCRAOrSitePI(clinicalStaffForSiteCRA2.getOrganizationClinicalStaffs().get(0), study2, Role.SITE_CRA);
        addSiteCRAOrSitePI(clinicalStaffForSiteCRA2.getOrganizationClinicalStaffs().get(0), study3, Role.SITE_CRA);
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
        crfRepository.save(defaultCRF);

        login(leadCRA2);
        crf1 = createCRF(study2);
        crf1.setTitle("Demo Form 2");
        crfRepository.save(crf1);

        crf2 = createCRF(study2);
        crf2.setTitle("Demo Form 3");
        crfRepository.save(crf2);
        commitAndStartNewTransaction();

        ClinicalStaff clinicalStaff1 = Fixture.createClinicalStaffWithOrganization("cs1mskcc", "cs1mskcc", "-12345", defaultOrganization);
        clinicalStaff1 = clinicalStaffRepository.save(clinicalStaff1);

        ClinicalStaff clinicalStaff2 = Fixture.createClinicalStaffWithOrganization("cs1wake", "cs1wake", "-12345", wake);
        clinicalStaff2 = clinicalStaffRepository.save(clinicalStaff2);

        ClinicalStaff clinicalStaff3 = Fixture.createClinicalStaffWithOrganization("cs2wake", "cs2wake", "-12345", wake);
        clinicalStaff3 = clinicalStaffRepository.save(clinicalStaff3);

        ClinicalStaff clinicalStaff4 = Fixture.createClinicalStaffWithOrganization("cs1duke", "cs1duke", "-12345", duke);
        clinicalStaff4 = clinicalStaffRepository.save(clinicalStaff4);

        ClinicalStaff clinicalStaff5 = Fixture.createClinicalStaffWithOrganization("cs2duke", "cs2duke", "-12345", duke);
        clinicalStaff5 = clinicalStaffRepository.save(clinicalStaff5);

        ClinicalStaff clinicalStaff6 = Fixture.createClinicalStaffWithOrganization("cs3duke", "cs3duke", "-12345", duke);
        clinicalStaff6 = clinicalStaffRepository.save(clinicalStaff6);

        ClinicalStaff clinicalStaff7 = Fixture.createClinicalStaffWithOrganization("cs4duke", "cs4duke", "-12345", duke);
        clinicalStaff7 = clinicalStaffRepository.save(clinicalStaff7);

        ClinicalStaff clinicalStaff8 = Fixture.createClinicalStaffWithOrganization("cs1nci", "cs1nci", "-12345", nci);
        clinicalStaff8 = clinicalStaffRepository.save(clinicalStaff8);

        ClinicalStaff clinicalStaff9 = Fixture.createClinicalStaffWithOrganization("cs2nci", "cs2nci", "-12345", nci);
        clinicalStaff9 = clinicalStaffRepository.save(clinicalStaff9);

        ClinicalStaff clinicalStaff10 = Fixture.createClinicalStaffWithOrganization("cs3nci", "cs3nci", "-12345", nci);
        clinicalStaff10 = clinicalStaffRepository.save(clinicalStaff10);

        ClinicalStaff clinicalStaff11 = Fixture.createClinicalStaffWithOrganization("cs4nci", "cs4nci", "-12345", nci);
        clinicalStaff11 = clinicalStaffRepository.save(clinicalStaff11);

        ClinicalStaff clinicalStaff12 = Fixture.createClinicalStaffWithOrganization("cs1queens", "cs1queens", "-12345", queens);
        clinicalStaff12 = clinicalStaffRepository.save(clinicalStaff12);

        ClinicalStaff clinicalStaff13 = Fixture.createClinicalStaffWithOrganization("cs1orange", "cs1orange", "-12345", orange);
        clinicalStaff13 = clinicalStaffRepository.save(clinicalStaff13);

        ClinicalStaff clinicalStaff14 = Fixture.createClinicalStaffWithOrganization("cs2orange", "cs2orange", "-12345", orange);
        clinicalStaff14 = clinicalStaffRepository.save(clinicalStaff14);

        ClinicalStaff clinicalStaff15 = Fixture.createClinicalStaffWithOrganization("cs1cerim", "cs1cerim", "-12345", cerim);
        clinicalStaff15 = clinicalStaffRepository.save(clinicalStaff15);

        ClinicalStaff clinicalStaff16 = Fixture.createClinicalStaffWithOrganization("cs2cerim", "cs2cerim", "-12345", cerim);
        clinicalStaff16 = clinicalStaffRepository.save(clinicalStaff16);

        ClinicalStaff clinicalStaff17 = Fixture.createClinicalStaffWithOrganization("cs3cerim", "cs3cerim", "-12345", cerim);
        clinicalStaff17 = clinicalStaffRepository.save(clinicalStaff17);

        ClinicalStaff clinicalStaff18 = Fixture.createClinicalStaffWithOrganization("cs4cerim", "cs4cerim", "-12345", cerim);
        clinicalStaff18 = clinicalStaffRepository.save(clinicalStaff18);














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
