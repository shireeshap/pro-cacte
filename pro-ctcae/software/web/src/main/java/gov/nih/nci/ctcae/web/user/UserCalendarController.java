package gov.nih.nci.ctcae.web.user;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.core.query.StudyParticipantCrfScheduleQuery;
import gov.nih.nci.ctcae.core.repository.secured.StudyParticipantCrfScheduleRepository;
import gov.nih.nci.ctcae.web.participant.ParticipantAjaxFacade;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author mehul
 *         Date: 5/30/12
 */

public class UserCalendarController extends AbstractController {

    private StudyParticipantCrfScheduleRepository spcsRepository;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        UserCalendarCommand userCalendarCommand = new UserCalendarCommand();
        userCalendarCommand.setProCtcAECalendar(new ProCtcAECalendar());
         userCalendarCommand.setSpcsRepository(spcsRepository);
        ModelAndView modelAndView = new ModelAndView("user/userCalendar");
        userCalendarCommand.createCurrentMonthScheduleMap();
        modelAndView.addObject("userCalendarCommand", userCalendarCommand);
        request.getSession().setAttribute("userCalendarCommandObj", userCalendarCommand);
        return modelAndView;
    }

    public void setSpcsRepository(StudyParticipantCrfScheduleRepository spcsRepository) {
        this.spcsRepository = spcsRepository;
    }
}
