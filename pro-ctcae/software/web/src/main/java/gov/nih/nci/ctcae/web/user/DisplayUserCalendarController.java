package gov.nih.nci.ctcae.web.user;

import gov.nih.nci.ctcae.core.domain.ProCtcAECalendar;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mehul gulati
 * Date: 5/18/12
 */

public class DisplayUserCalendarController extends AbstractController {

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {

        UserCalendarCommand userCalendarCommand = (UserCalendarCommand) request.getSession().getAttribute("userCalendarCommandObj");
        ModelAndView modelAndView = new ModelAndView("user/ajax/displayUserCalendar");
        String direction = request.getParameter("dir");
        ProCtcAECalendar proCtcAECalendar = userCalendarCommand.getProCtcAECalendar();
        if (direction.equals("prev")) {
            proCtcAECalendar.add(-1);
              userCalendarCommand.setProCtcAECalendar(proCtcAECalendar);
        }
        if (direction.equals("next")) {
              proCtcAECalendar.add(1);
            userCalendarCommand.setProCtcAECalendar(proCtcAECalendar);
        }
        if (direction.equals("refresh")) {
              userCalendarCommand.getProCtcAECalendar().add(0);
        }
        userCalendarCommand.createCurrentMonthScheduleMap();
        modelAndView.addObject("userCalendarCommand", userCalendarCommand);
        return modelAndView;
    }
}
