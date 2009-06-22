package gov.nih.nci.ctcae.web.reports;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.core.repository.Repository;
import gov.nih.nci.ctcae.core.domain.*;

/**
 * @author Mehul Gulati
 *         Date: Apr 10, 2009
 */
public class ParticipantCareMonitorController extends AbstractController {

    StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/participantCareMonitor");
        String scheduleId = request.getParameter("sid");

        if (!StringUtils.isBlank(scheduleId)) {
            StudyParticipantCrfSchedule schedule = studyParticipantCrfScheduleRepository.findById(Integer.parseInt(scheduleId));
            Study study = schedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite().getStudy();
            CRF crf = schedule.getStudyParticipantCrf().getCrf();
            StudySite studySite = schedule.getStudyParticipantCrf().getStudyParticipantAssignment().getStudySite();
            Participant participant = schedule.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant();

            modelAndView.addObject("study", study);
            modelAndView.addObject("crf", crf);
            modelAndView.addObject("studySite", studySite);
            modelAndView.addObject("participant", participant);
            modelAndView.addObject("visits", "ALL");
        } else {
            modelAndView.addObject("study", null);
            modelAndView.addObject("crf", null);
            modelAndView.addObject("studySite", null);
            modelAndView.addObject("participant", null);
            modelAndView.addObject("visits", 0);
        }

        return modelAndView;
    }

    public ParticipantCareMonitorController() {
        super();
        setSupportedMethods(new String[]{"GET"});
    }

    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}
    

