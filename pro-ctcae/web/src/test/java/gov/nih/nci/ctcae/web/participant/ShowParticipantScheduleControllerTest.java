package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.web.AbstractWebTestCase;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.helper.ParticipantTestHelper;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

/**
 * @author Mehul Gulati
 *         Date: Jun 16, 2009
 */

public class ShowParticipantScheduleControllerTest extends AbstractWebTestCase {

//    ShowParticipantScheduleController createParticipantController;
//
//    public void testController() throws Exception {
//        createParticipantController = new ShowParticipantScheduleController();
//        createParticipantController.setStudyRepository(studyRepository);
//        Study s = StudyTestHelper.getDefaultStudy();
//        Integer studyId = s.getId();
//        StudySite ss = s.getLeadStudySite();
//        Integer studySiteId = ss.getId();
//        Integer participantId = ss.getStudyParticipantAssignments().get(0).getParticipant().getId();
//        Integer crfId = ss.getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0).getCrf().getId();
//
//        request.setParameter("studyId", studyId.toString());
//        request.setParameter("studySiteId", studySiteId.toString());
//        request.setParameter("participantId", participantId.toString());
//        request.setParameter("crfId", crfId.toString());
//
//        ModelAndView mv = createParticipantController.handleRequestInternal(request, response);
//        Map m = mv.getModel();
//        assertNotNull(m.get("scheduledCrfs"));
//    }

    public void testPdfGeneration() throws Exception {

        PrintParticipantScheduleController controller = new PrintParticipantScheduleController();
        controller.setStudyParticipantCrfScheduleRepository(studyParticipantCrfScheduleRepository);
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = ParticipantTestHelper.getDefaultParticipant().getStudyParticipantAssignments().get(0).getStudyParticipantCrfs().get(0).getStudyParticipantCrfSchedules().get(0);
        request.setParameter("id", studyParticipantCrfSchedule.getId().toString());
        ModelAndView modelAndView = controller.handleRequestInternal(request, response);
        PrintSchedulePdfView view = (PrintSchedulePdfView) modelAndView.getView();
        view.render(null, request, response);
        assertEquals("application/pdf", response.getContentType());
        File f = new File("generatedpdf.pdf");
        if (f.exists()) {
            f.delete();
        }
        f.createNewFile();
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(f));
        bufferedOutputStream.write(response.getContentAsByteArray());
        bufferedOutputStream.close();


    }

}
