package gov.nih.nci.ctcae.web.reports;

import gov.nih.nci.ctcae.core.domain.CRF;
import gov.nih.nci.ctcae.core.domain.Participant;
import gov.nih.nci.ctcae.core.domain.Study;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import gov.nih.nci.ctcae.core.domain.StudySite;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * @author Mehul Gulati
 *         Date: Apr 10, 2009
 */
public class ParticipantLevelReportController extends AbstractController {

    StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;
    private static String WORST_SYMPTOM = "worstSymptom";
    private static String REPORT_TYPE = "rt";
    private static String SCHEDULE_ID = "sid";
    private static String CTCAE_GRADES = "ctcaeGrades";

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
    	
        ModelAndView modelAndView = new ModelAndView("reports/participantLevelReport");
        String scheduleId = request.getParameter(SCHEDULE_ID);
        String reportType = request.getParameter(REPORT_TYPE);
        if(reportType!=null && reportType.equals(WORST_SYMPTOM)){
            modelAndView = new ModelAndView("reports/participantLevelWorstSymptomReport");
        } else if(reportType!=null && reportType.equals(CTCAE_GRADES)){
        	modelAndView = new ModelAndView("reports/participantLevelCtcaeGradesReport");
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
            modelAndView.addObject("showReports", true);
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
    

