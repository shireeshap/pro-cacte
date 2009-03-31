package gov.nih.nci.ctcae.web.participant;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;

/**
 * @author Mehul Gulati
 */

public class ShowCompletedCrfController extends AbstractController {

    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }

    StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;


     protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

         ModelAndView modelAndView = new ModelAndView("participant/showCompletedCrf");
         String crfId = request.getParameter("id");

         modelAndView.addObject("completedCrf", getCompletedCrf(Integer.valueOf(crfId)));

         return modelAndView;
     }

    private StudyParticipantCrfSchedule getCompletedCrf(Integer crfId){

        StudyParticipantCrfSchedule studyParticipantCrfSchedule = studyParticipantCrfScheduleRepository.findById(crfId);

        return studyParticipantCrfSchedule;
    }
}
