package gov.nih.nci.ctcae.web.spcSchedule;

import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.repository.GenericRepository;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mehul
 *         Date: 2/17/12
 */
public class RemoveOverdueScheduleController extends AbstractController {

    private StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        String sid = request.getParameter("sid");
        StudyParticipantCrfSchedule studyParticipantCrfSchedule = studyParticipantCrfScheduleRepository.findById(Integer.valueOf(sid));
        if (studyParticipantCrfSchedule != null) {
            studyParticipantCrfSchedule.setMarkDelete(true);
            studyParticipantCrfScheduleRepository.save(studyParticipantCrfSchedule);
        }
        return new ModelAndView(new RedirectView("/proctcae/pages/home"));
    }

    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}
