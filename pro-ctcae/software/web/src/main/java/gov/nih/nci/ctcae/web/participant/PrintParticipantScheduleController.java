package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Mehul Gulati
 *         Date: Jun 17, 2009
 */
public class PrintParticipantScheduleController extends AbstractController {
    StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        PrintSchedulePdfView printSchedulePdfView = new PrintSchedulePdfView();
        printSchedulePdfView.setStudyParticipantCrfScheduleRepository(studyParticipantCrfScheduleRepository);
        return new ModelAndView(printSchedulePdfView);
    }

    @Required
    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}
