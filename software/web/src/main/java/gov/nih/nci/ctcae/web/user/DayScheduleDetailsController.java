package gov.nih.nci.ctcae.web.user;

import gov.nih.nci.ctcae.commons.utils.DateUtils;
import gov.nih.nci.ctcae.core.domain.StudyParticipantCrfSchedule;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @author mehul
 * Date: 6/6/12
 */
public class DayScheduleDetailsController extends AbstractController {

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse httpServletResponse) throws Exception {
        UserCalendarCommand userCalendarCommand = (UserCalendarCommand) request.getSession().getAttribute("userCalendarCommandObj");
        Integer day = Integer.parseInt(request.getParameter("day"));
        Calendar c = new GregorianCalendar();
        c.setTime(userCalendarCommand.getProCtcAECalendar().getTime());
        c.set(Calendar.DATE, day);
        ModelAndView mv = new ModelAndView("user/dayDetails");
        List<StudyParticipantCrfSchedule> schedules = new ArrayList();
        schedules = userCalendarCommand.getScheduleDates().get(day);
        mv.addObject("schedules", schedules);
        mv.addObject("date", DateUtils.format(c.getTime()));
        mv.addObject("day", day);
        return mv;
    }
}
