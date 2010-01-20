package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.domain.ParticipantSchedule;
import gov.nih.nci.ctcae.core.repository.secured.CRFRepository;
import gov.nih.nci.ctcae.commons.utils.DateUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;

//
/**
 * The Class AddCrfScheduleController.
 *
 * @author Harsh Agarwal
 * @created Nov 6, 2008
 * Controller class called via Ajax. Used to add / delete schedule based on the date parameter
 */
public class AddCrfScheduleController extends AbstractController {


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        StudyParticipantCommand studyParticipantCommand = ParticipantControllerUtils.getStudyParticipantCommand(request);
        Integer index = Integer.parseInt(request.getParameter("index"));
        String action = request.getParameter("action");
        String date = request.getParameter("date");

        ParticipantSchedule participantSchedule = studyParticipantCommand.getParticipantSchedules().get(index);
        
        Calendar c = new GregorianCalendar();
        int duedate = 24 * 60 * 60 * 1000;
        if ("delall".equals(action)) {
            participantSchedule.removeAllSchedules();
        }
        c.setTime(participantSchedule.getCalendar().getTime());

        if ("moveall".equals(action)) {
            String strNewdate = date.substring(0, date.indexOf(","));
            Date newDate = DateUtils.parseDate(strNewdate);
            int olddate = Integer.parseInt(date.substring(date.indexOf(",") + 1));
            c.set(Calendar.DATE, olddate);
            participantSchedule.moveAllSchedules(DateUtils.daysBetweenDates(newDate, c.getTime()));
        }

        if ("moveallfuture".equals(action)) {
            String strNewdate = date.substring(0, date.indexOf(","));
            Date newDate = DateUtils.parseDate(strNewdate);
            int olddate = Integer.parseInt(date.substring(date.indexOf(",") + 1));
            c.set(Calendar.DATE, olddate);
            participantSchedule.moveFutureSchedules(c, DateUtils.daysBetweenDates(newDate, c.getTime()));
        }
        if ("delallfuture".equals(action)) {
            c.set(Calendar.DATE, Integer.parseInt(date));
            participantSchedule.deleteFutureSchedules(c);
        }

        if ("add,del".equals(action)) {
            String strNewdate = date.substring(0, date.indexOf(","));
            Date newDate = DateUtils.parseDate(strNewdate);
            String olddate = date.substring(date.indexOf(",") + 1);

            c.set(Calendar.DATE, Integer.parseInt(olddate));
            participantSchedule.removeSchedule(c);

            c.setTime(newDate);
            participantSchedule.createSchedule(c, duedate, -1, -1);

        }

        if ("add".equals(action)) {
            c.set(Calendar.DATE, Integer.parseInt(date));
            participantSchedule.createSchedule(c, duedate, -1, -1);
        }
        if ("del".equals(action)) {
            c.set(Calendar.DATE, Integer.parseInt(date));
            participantSchedule.removeSchedule(c);
        }

        return null;
    }


    /**
     * Instantiates a new adds the crf schedule controller.
     */
    public AddCrfScheduleController() {
        super();
        setSupportedMethods(new String[]{"GET"});

    }
}