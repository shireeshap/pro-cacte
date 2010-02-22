package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.exception.CtcAeSystemException;
import gov.nih.nci.ctcae.core.query.ParticipantQuery;
import gov.nih.nci.ctcae.core.repository.secured.ParticipantRepository;
import gov.nih.nci.ctcae.core.repository.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.core.repository.UserRepository;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;
import org.springframework.beans.factory.annotation.Required;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author Mehul Gulati
 *         Date: Mar 10, 2009
 */

public class ParticipantResponseReportController extends AbstractController {

    UserRepository userRepository;
    StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository;
    ParticipantRepository participantRepository;

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Participant participant = userRepository.findParticipantForUser(user);
        if (participant == null) {
            throw new CtcAeSystemException("Can not find participant for username " + user.getUsername());
        }
        participant = participantRepository.findById(participant.getId());
        for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
            for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
                studyParticipantCrf.getStudyParticipantCrfSchedules();
            }
        }
        ModelAndView modelAndView = new ModelAndView("participant/responseReport");


        String id = request.getParameter("id");
        StudyParticipantCrfSchedule spcs = null;
        if (StringUtils.isBlank(id)) {
            for (StudyParticipantAssignment studyParticipantAssignment : participant.getStudyParticipantAssignments()) {
                for (StudyParticipantCrf studyParticipantCrf : studyParticipantAssignment.getStudyParticipantCrfs()) {
                    for (StudyParticipantCrfSchedule studyParticipantCrfSchedule : studyParticipantCrf.getStudyParticipantCrfSchedules()) {
                        if (studyParticipantCrfSchedule.getStatus().equals(CrfStatus.COMPLETED)) {
                            spcs = studyParticipantCrfSchedule;
                            break;
                        }
                    }
                    if (spcs != null) break;
                }
                if (spcs != null) break;
            }
        } else {
            Integer scheduleId = Integer.parseInt(id);
            spcs = studyParticipantCrfScheduleRepository.findById(scheduleId);
            if (!spcs.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant().equals(participant)) {
                throw (new AccessDeniedException("Participant does not have access to this form"));
            }
        }


        modelAndView.addObject("completedSchedule", spcs);
        modelAndView.addObject("participant", participant);

        return modelAndView;
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }

    @Required
    public void setParticipantRepository(ParticipantRepository participantRepository) {
        this.participantRepository = participantRepository;
    }
}