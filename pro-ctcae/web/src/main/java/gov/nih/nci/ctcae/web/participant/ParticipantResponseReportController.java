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

    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Participant participant = userRepository.findParticipantForUser(user);
        if (participant == null) {
            throw new CtcAeSystemException("Can not find participant for username " + user.getUsername());
        }
        ModelAndView modelAndView = new ModelAndView("participant/responseReport");

        Integer scheduleId = Integer.parseInt(request.getParameter("id"));
        StudyParticipantCrfSchedule spcs = studyParticipantCrfScheduleRepository.findById(scheduleId);

        if (!spcs.getStudyParticipantCrf().getStudyParticipantAssignment().getParticipant().equals(participant)) {
            throw (new AccessDeniedException("Participant does not have access to this form"));
        }


        modelAndView.addObject("completedSchedule", spcs);

        return modelAndView;
    }

    @Required
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void setStudyParticipantCrfScheduleRepository(StudyParticipantCrfScheduleRepository studyParticipantCrfScheduleRepository) {
        this.studyParticipantCrfScheduleRepository = studyParticipantCrfScheduleRepository;
    }
}