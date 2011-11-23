package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Mehul Gulati
 *         Date: Apr 10, 2009
 */
public class ParticipantLevelReportController extends AbstractController {

    StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView modelAndView = new ModelAndView("reports/participantLevelReport");
        String scheduleId = request.getParameter("sid");
        String reportType = request.getParameter("rt");
        if(reportType!=null && reportType.equals("worstSymptom")){
            modelAndView = new ModelAndView("reports/participantLevelWorstSymptomReport");
        }

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
            Map m = modelAndView.getModel();
            m.put("study", null);
            m.put("crf", null);
            m.put("studySite", null);
            m.put("participant", null);
            m.put("visits", 0);
        }

        return modelAndView;
    }

    public ParticipantLevelReportController() {
        super();
        setSupportedMethods(new String[]{"GET"});
    }

    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}
    

