package gov.nih.nci.ctcae.web.participant;

import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.repository.secured.StudyRepository;

import java.util.List;
import java.util.ArrayList;

/**
 * @author Mehul Gulati
 * Date: Jun 15, 2009
 */

public class ShowParticipantScheduleController extends AbstractController {

    StudyRepository studyRepository;

    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        ModelAndView  modelAndView = new ModelAndView("participant/showParticipantScheduleTable");
        Integer studyId = Integer.parseInt(request.getParameter("studyId"));
        Integer studySiteId = Integer.parseInt(request.getParameter("studySiteId"));
        Integer crfId  = Integer.parseInt(request.getParameter("crfId"));
        Integer participantId = Integer.parseInt(request.getParameter("participantId"));

        List<StudyParticipantCrfSchedule> studyParticipantCrfSchedules = getScheduledCrfs(studyId, studySiteId, crfId, participantId);
        modelAndView.addObject("scheduledCrfs", studyParticipantCrfSchedules);

        return modelAndView;
    }

    private List<StudyParticipantCrfSchedule> getScheduledCrfs(Integer studyId, Integer studySiteId, Integer crfId, Integer participantId) {

        Study study = studyRepository.findById(studyId);
        StudySite studySite = study.getStudySiteById(studySiteId);
        List<StudyParticipantCrfSchedule> scheduledInprogressCrfs = new ArrayList<StudyParticipantCrfSchedule>();

        for (StudyParticipantAssignment studyParticipantAssignment : studySite.getStudyParticipantAssignments()) {
            if (studyParticipantAssignment.getParticipant().getId().equals(participantId)) {

                for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
                    if (studyParticipantCrf.getCrf().getId().equals(crfId)) {
                        scheduledInprogressCrfs.addAll(studyParticipantCrf.getCrfsByStatus(CrfStatus.SCHEDULED));
                        scheduledInprogressCrfs.addAll(studyParticipantCrf.getCrfsByStatus(CrfStatus.INPROGRESS));
                    }
                }
            }

        }
        
       return scheduledInprogressCrfs;
    }

    public void setStudyRepository(StudyRepository studyRepository) {
        this.studyRepository = studyRepository;
    }
}
