package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.cabig.ctms.web.tabs.Tab;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.StudyOrganizationClinicalStaff;
import gov.nih.nci.ctcae.core.domain.StudyParticipantAssignment;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.helper.Fixture;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.web.ListValues;
import gov.nih.nci.ctcae.web.clinicalStaff.ClinicalStaffAjaxFacade;
import gov.nih.nci.ctcae.web.validation.validator.WebControllerValidatorImpl;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Harsh Agarwal
 * @since June 10, 2009
 */
public class ParticipantControllerTest extends AbstractWebTestCase {
    ParticipantController controller;

    @Override
    protected void onSetUpInTransaction() throws Exception {

        super.onSetUpInTransaction();
        login(StudyTestHelper.getLeadSite_SitePI().getUser().getUsername());
        controller = new ParticipantController();
        controller.setPrivilegeAuthorizationCheck(privilegeAuthorizationCheck);
        controller.setParticipantRepository(participantRepository);
        controller.setStudyOrganizationRepository(studyOrganizationRepository);
        controller.setWebControllerValidator(new WebControllerValidatorImpl());
        List<Tab<ParticipantCommand>> tabs = controller.getFlow().getTabs();
        for (Tab tab : tabs) {
            if (tab instanceof ParticipantDetailsTab) {
                ParticipantDetailsTab pdt = (ParticipantDetailsTab) tab;
                pdt.setCrfRepository(crfRepository);
                pdt.setStudyOrganizationRepository(studyOrganizationRepository);
                pdt.setUserRepository(userRepository);
            }
        }
        request.setMethod("GET");

    }

    public void testCreateParticipant() throws Exception {
        ParticipantCommand pc = null;
        pc = firstTab_ParticipantDetails();
        secondTab_ClinicalStaff(pc);
    }

    private ParticipantCommand firstTab_ParticipantDetails() throws Exception {

        ModelAndView mv = controller.handleRequest(request, response);
        Map m = mv.getModel();
        assertNotNull(m.get("genders"));
        assertNotNull(m.get("organizationsHavingStudySite"));
        List<ListValues> ss = (List<ListValues>) m.get("organizationsHavingStudySite");
        assertEquals(3, ss.size());

        ParticipantCommand pc = (ParticipantCommand) mv.getModel().get("command");
        assertNotNull(pc);
        assertNull(pc.getParticipant().getId());

        Participant p = Fixture.createParticipant("Auto", "Generated", "Participant");
        p.getUser().setConfirmPassword(p.getUser().getPassword());
        List<StudySite> studySite = new ArrayList<StudySite>();
        StudySite ls = StudyTestHelper.getDefaultStudy().getLeadStudySite();
        studySite.add(ls);
        pc.setStudySites(studySite);
        request.setParameter("participantStudyIdentifier_" + ls.getId(), "123");
        pc.setParticipant(p);

        request.setMethod("POST");
        request.setParameter("_target1", "");
        mv = controller.handleRequest(request, response);
        commitAndStartNewTransaction();

        pc = (ParticipantCommand) mv.getModel().get("command");
        Integer id = pc.getParticipant().getId();

        Participant savedParticipant = participantRepository.findById(id);

        assertNotNull(savedParticipant);
        assertEquals("Auto", savedParticipant.getFirstName());
        assertEquals("Generated", savedParticipant.getLastName());
        assertEquals("Participant", savedParticipant.getAssignedIdentifier());
        assertEquals(1, savedParticipant.getStudyParticipantAssignments().size());
        StudyParticipantAssignment spa = savedParticipant.getStudyParticipantAssignments().get(0);
        assertEquals("123", spa.getStudyParticipantIdentifier());
        assertEquals(1, spa.getStudyParticipantCrfs().size());
        assertTrue(13< spa.getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules().size());
        assertEquals("participant/participant_clinical_staff", mv.getViewName());
        assertEquals(2, spa.getStudyParticipantClinicalStaffs().size());
        m = mv.getModel();
        assertNotNull(m.get("studyParticipantAssignments"));
        assertNotNull(m.get("notifyOptions"));

        return pc;

    }

    public void secondTab_ClinicalStaff(ParticipantCommand pc) throws Exception {
        StudyParticipantAssignment spa = pc.getParticipant().getStudyParticipantAssignments().get(0);

        assertEquals(1, spa.getSitePIs().size());
        assertEquals(1, spa.getSiteCRAs().size());
        assertEquals(StudyTestHelper.getLeadSite_SitePI(), spa.getSitePIs().get(0).getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff());
        assertNull(spa.getTreatingPhysician().getStudyOrganizationClinicalStaff());
        assertNull(spa.getResearchNurse().getStudyOrganizationClinicalStaff());
        assertEquals(0, spa.getNotificationClinicalStaff().size());

        ClinicalStaffAjaxFacade csaf = new ClinicalStaffAjaxFacade();
        csaf.setClinicalStaffRepository(clinicalStaffRepository);
        List<StudyOrganizationClinicalStaff> clinicalStaffList = csaf.matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole("Josh", spa.getStudySite().getId(), "TREATING_PHYSICIAN");
        spa.getTreatingPhysician().setStudyOrganizationClinicalStaff(studyOrganizationClinicalStaffRepository.findById(clinicalStaffList.get(0).getId()));

        clinicalStaffList = csaf.matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole("Dian", spa.getStudySite().getId(), "NURSE");
        spa.getResearchNurse().setStudyOrganizationClinicalStaff(studyOrganizationClinicalStaffRepository.findById(clinicalStaffList.get(0).getId()));

        clinicalStaffList = csaf.matchStudyOrganizationClinicalStaffByStudyOrganizationIdAndRole("%", spa.getStudySite().getId(), "NURSE|TREATING_PHYSICIAN");


        AddNotificationClinicalStaffController controller1 = new AddNotificationClinicalStaffController();
        request.setParameter("index", "0");
        controller1.handleRequestInternal(request, response);
        assertEquals(1, spa.getNotificationClinicalStaff().size());

        spa.getNotificationClinicalStaff().get(0).setStudyOrganizationClinicalStaff(studyOrganizationClinicalStaffRepository.findById(clinicalStaffList.get(0).getId()));

        request.setMethod("POST");
        request.setParameter("_finish", "");
        ModelAndView mv = controller.handleRequest(request, response);
        commitAndStartNewTransaction();

        pc = (ParticipantCommand) mv.getModel().get("command");
        Integer id = pc.getParticipant().getId();
        Participant savedParticipant = participantRepository.findById(id);
        spa = savedParticipant.getStudyParticipantAssignments().get(0);

        assertNotNull(savedParticipant);
        assertEquals(1, spa.getSitePIs().size());
        assertEquals(1, spa.getSiteCRAs().size());
        assertEquals(StudyTestHelper.getLeadSite_SitePI(), spa.getSitePIs().get(0).getStudyOrganizationClinicalStaff().getOrganizationClinicalStaff().getClinicalStaff());
        assertNotNull(spa.getTreatingPhysician().getStudyOrganizationClinicalStaff());
        assertNotNull(spa.getResearchNurse().getStudyOrganizationClinicalStaff());
        assertEquals(1, spa.getNotificationClinicalStaff().size());

    }

    public void testEditParticipant() throws Exception {
        request.setParameter("id", ParticipantTestHelper.getDefaultParticipant().getId().toString());
        ModelAndView mv = controller.handleRequest(request, response);
        Map m = mv.getModel();
        assertNotNull(m.get("genders"));
        assertNotNull(m.get("organizationsHavingStudySite"));
        List<ListValues> ss = (List<ListValues>) m.get("organizationsHavingStudySite");
        assertEquals(3, ss.size());

        ParticipantCommand pc = (ParticipantCommand) mv.getModel().get("command");
        assertNotNull(pc);
        assertNotNull(pc.getParticipant().getId());
        assertEquals(pc.getParticipant(), ParticipantTestHelper.getDefaultParticipant());
    }


    @Override
    protected void onTearDownInTransaction() throws Exception {
        ParticipantQuery query = new ParticipantQuery();
        query.filterByParticipantFirstName("Auto");
        query.filterByParticipantLastName("Generated");
        query.filterByParticipantIdentifier("Participant");

        List<Participant> participants = (List<Participant>) participantRepository.find(query);
        for (Participant p : participants) {
            participantRepository.delete(p);
        }
        commitAndStartNewTransaction();
        super.onTearDownInTransaction();
    }
}