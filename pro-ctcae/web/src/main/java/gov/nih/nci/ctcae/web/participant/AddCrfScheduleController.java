package gov.nih.nci.ctcae.web.participant;

import gov.nih.nci.ctcae.core.repository.FinderRepository;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.GregorianCalendar;

//
/**
 * The Class AddCrfScheduleController.
 *
 * @author Harsh Agarwal
 * @created Nov 6, 2008
 * Controller class called via Ajax. Used to add / delete schedule based on the date parameter
 */
public class AddCrfScheduleController extends AbstractController {

    /**
     * The finder repository.
     */
    FinderRepository finderRepository;


    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractController#handleRequestInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected ModelAndView handleRequestInternal(final HttpServletRequest request, final HttpServletResponse response) throws Exception {

        StudyParticipantCommand studyParticipantCommand = ParticipantControllerUtils.getStudyParticipantCommand(request);
        Integer index = Integer.parseInt(request.getParameter("index"));
        String action = request.getParameter("action");
        String date = request.getParameter("date");

        ParticipantSchedule participantSchedule = studyParticipantCommand.getParticipantSchedules().get(index);
        participantSchedule.setFinderRepository(finderRepository);
        Calendar c = new GregorianCalendar();
        int duedate = 24 * 60 * 60 * 1000;
        if ("delall".equals(action)) {
            participantSchedule.removeAllSchedules();
        }

        if ("moveall".equals(action)) {
            int newdate = Integer.parseInt(date.substring(0, date.indexOf(",")));
            int olddate = Integer.parseInt(date.substring(date.indexOf(",") + 1));
            participantSchedule.moveAllSchedules(newdate-olddate);
        }

        c.setTime(participantSchedule.getCalendar().getTime());
        if ("moveallfuture".equals(action)) {
            int newdate = Integer.parseInt(date.substring(0, date.indexOf(",")));
            int olddate = Integer.parseInt(date.substring(date.indexOf(",") + 1));
             c.set(Calendar.DATE, olddate);
            participantSchedule.moveFutureSchedules(c, newdate-olddate);
        }
        if ("delallfuture".equals(action)) {
             c.set(Calendar.DATE, Integer.parseInt(date));
            participantSchedule.deleteFutureSchedules(c);
        }

        if ("add,del".equals(action)) {
            String newdate = date.substring(0, date.indexOf(","));
            String olddate = date.substring(date.indexOf(",") + 1);

            c.set(Calendar.DATE, Integer.parseInt(newdate));
            participantSchedule.createSchedule(c, duedate);

            c.set(Calendar.DATE, Integer.parseInt(olddate));
            participantSchedule.removeSchedule(c);
        }

        if ("add".equals(action)) {
            c.set(Calendar.DATE, Integer.parseInt(date));
            participantSchedule.createSchedule(c, duedate);
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

    /**
     * Sets the finder repository.
     *
     * @param finderRepository the new finder repository
     */
    @Required
    public void setFinderRepository(FinderRepository finderRepository) {
        this.finderRepository = finderRepository;
    }
}