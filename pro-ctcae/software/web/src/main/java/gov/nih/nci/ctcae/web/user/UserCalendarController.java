package gov.nih.nci.ctcae.web.user;

import gov.nih.nci.ctcae.core.domain.*;
import gov.nih.nci.ctcae.web.participant.ParticipantAjaxFacade;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author mehul
 * Date: 5/30/12
*/

public class UserCalendarController extends AbstractController {

    private ParticipantAjaxFacade participantAjaxFacade;

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        UserCalendarCommand userCalendarCommand = new UserCalendarCommand();
        ModelAndView modelAndView = new ModelAndView("user/userCalendar");
        List<Participant> participants = participantAjaxFacade.getParticipantList();
        userCalendarCommand.setParticipants(participants);
        userCalendarCommand.setProCtcAECalendar(new ProCtcAECalendar());
        userCalendarCommand.setCurrentMonthScheduleMap();
        modelAndView.addObject("userCalendarCommand", userCalendarCommand);
        request.getSession().setAttribute("userCalendarCommandObj", userCalendarCommand);
//        request.getSession().getAttribute("userCalendarCommandObj");

        return modelAndView;
    }

    public void setParticipantAjaxFacade(ParticipantAjaxFacade participantAjaxFacade) {
        this.participantAjaxFacade = participantAjaxFacade;
    }
}
