package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.document.AbstractPdfView;

/**
 * @author Mehul Gulati
 *         Date: Jul 1, 2009
 */
public class PrintParticipantCareScheduleControllerTest extends AbstractWebTestCase {

    public void testHandleRequestInternal() throws Exception {

        PrintParticipantScheduleController controller = new PrintParticipantScheduleController();
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        assertTrue(modelAndView.getView() instanceof AbstractPdfView);
    }

    public void testPdfGeneration() throws Exception {

        PrintParticipantScheduleController controller = new PrintParticipantScheduleController();
        controller.setStudyParticipantCrfScheduleRepository(studyParticipantCrfScheduleRepository);
        StudyParticipantCrfSchedule spcs = ParticipantTestHelper.getDefaultParticipant().getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules().get(0);
        request.setParameter("id", spcs.getId().toString());
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        PrintSchedulePdfView view = (PrintSchedulePdfView) modelAndView.getView();
        view.render(null, request, response);
        assertEquals("application/pdf", response.getContentType());
    }
}
