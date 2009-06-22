package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.Role;
//import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
//import gov.nih.nci.ctcae.core.helper.StudyTestHelper;
//import gov.nih.nci.ctcae.core.domain.Participant;
//import gov.nih.nci.ctcae.core.domain.Study;
import org.springframework.web.servlet.ModelAndView;

/**
 * User: Harsh
 * Date: Jun 22, 2009
 * Time: 12:21:32 PM
 */
public class ParticipantCareMonitorTest extends AbstractWebTestCase {
    public void testController_WithoutId() throws Exception {
        request.setMethod("GET");
        ParticipantCareMonitorController controller = new ParticipantCareMonitorController();
        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertEquals("reports/participantCareMonitor", modelAndView.getViewName());
        assertEquals(0, modelAndView.getModel().get("visits"));
        assertNull(modelAndView.getModel().get("study"));
    }

    public void testController_WithId() throws Exception {
        login(StudyTestHelper.getNonLeadSiteStaffByRole(Role.SITE_CRA).getUser().getUsername());
        request.setMethod("GET");

        Integer id = ParticipantTestHelper.getDefaultParticipant().getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules().get(0).getId();
        ParticipantCareMonitorController controller = new ParticipantCareMonitorController();
        controller.setStudyParticipantCrfScheduleRepository(studyParticipantCrfScheduleRepository);
        request.setParameter("sid", id.toString());
        ModelAndView modelAndView = controller.handleRequest(request, response);
        assertEquals("reports/participantCareMonitor", modelAndView.getViewName());
        assertEquals("ALL", modelAndView.getModel().get("visits"));

        Study study = StudyTestHelper.getDefaultStudy();
        Participant participant = ParticipantTestHelper.getDefaultParticipant();
        assertEquals(study, modelAndView.getModel().get("study"));
        assertEquals(participant, modelAndView.getModel().get("participant"));
        assertEquals(study.getCrfs().get(0), modelAndView.getModel().get("crf"));
        assertEquals(participant.getStudyParticipantAssignments().get(0).getStudySite(), modelAndView.getModel().get("studySite"));
    }

}
