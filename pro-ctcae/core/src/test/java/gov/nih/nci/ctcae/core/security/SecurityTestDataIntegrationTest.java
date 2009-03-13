package gov.nih.nci.ctcae.core.security;

import gov.nih.nci.ctcae.core.AbstractHibernateIntegrationTestCase;
import gov.nih.nci.ctcae.core.Fixture;
import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;

import java.util.Collection;
import java.util.Date;
import java.text.ParseException;

/**
 * @author Vinay Kumar
 * @crated Mar 13, 2009
 */
public class SecurityTestDataIntegrationTest extends AbstractHibernateIntegrationTestCase {

    protected Study study1, study2;

    protected User leadCRA1, leadCRA2, siteCRA1, siteCRA2;

    protected CRF defaultCRF, crf1, crf2;

    @Override
    protected void onSetUpInTransaction() throws Exception {
        super.onSetUpInTransaction();
        study1 = createStudy("-10002");
        study2 = createStudy("-1003");

        study1.getStudySites().get(0).setOrganization(wake);
        study2.getStudySites().get(0).setOrganization(wake);

        study1.getLeadStudySite().setOrganization(wake);
        study2.getLeadStudySite().setOrganization(wake);

        defaultStudy.setShortTitle("Study 1");
        study1.setShortTitle("Study 2");
        study2.setShortTitle("Study 3");


        study2 = studyRepository.save(study2);
        study1 = studyRepository.save(study1);
        defaultStudy = studyRepository.save(defaultStudy);


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

    public void testInsertData() throws ParseException {

        login(leadCRA1);
        Participant participant = createParticipant("John", defaultStudy.getStudySites().get(0));
        Participant participant3 = createParticipant("Tom", defaultStudy.getStudySites().get(0));
        Participant participant4 = createParticipant("Jake", defaultStudy.getStudySites().get(1));


        login(leadCRA2);
        Participant participant1 = createParticipant("Bruce", study1.getStudySites().get(0));
        Participant participant2 = createParticipant("Laura", study2.getStudySites().get(0));


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
        defaultCRF.setEffectiveEndDate(new Date());
        crfRepository.updateStatusToReleased(defaultCRF);

        login(leadCRA2);
        crf1 = createCRF(study1);
        crf1.setTitle("Demo Form 2");
        crf1.setEffectiveEndDate(new Date());
        crfRepository.updateStatusToReleased(crf1);

        crf2 = createCRF(study1);
        crf2.setTitle("Demo Form 3");
        crf2.setEffectiveEndDate(new Date());
        crfRepository.updateStatusToReleased(crf2);

        commitAndStartNewTransaction();

    }


}
